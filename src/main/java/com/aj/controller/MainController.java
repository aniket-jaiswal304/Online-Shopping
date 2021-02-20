package com.aj.controller;

import com.aj.dao.OrderDaoI;
import com.aj.dao.ProductDaoI;
import com.aj.entity.Product;
import com.aj.model.CartInfo;
import com.aj.model.CustomerInfo;
import com.aj.model.PaginationResult;
import com.aj.model.ProductInfo;
import com.aj.util.Utils;
import com.aj.validator.CustomerInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Transactional
@EnableWebMvc
public class MainController
{
    @Autowired
    private OrderDaoI orderDao;

    @Autowired
    private ProductDaoI productDao;

    @Autowired
    private CustomerInfoValidator customerInfoValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder)
    {
        Object target = dataBinder.getTarget();
        if (target == null)
        {
            return;
        }
        System.out.println("Target=" + target);

        // For Cart Form.
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class)
        {
        }
        // For Customer Form.
        // (@ModelAttribute("customerForm") @Validated CustomerInfo
        // customerForm)
        else if (target.getClass() == CustomerInfo.class)
        {
            dataBinder.setValidator(customerInfoValidator);
        }
    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home()
    {
        return "home";
    }

    // Product List page.
    @RequestMapping({ "/productList" })
    public String listProductHandler(Model model, @RequestParam(value = "name", defaultValue = "") String likeName,
                                     @RequestParam(value = "page", defaultValue = "1") int page)
    {
        final int maxResult = 5;
        final int maxNavigationPage = 10;

        PaginationResult<ProductInfo> result = productDao.queryProducts(page, maxResult, maxNavigationPage, likeName);

        model.addAttribute("paginationProducts", result);
        return "productList";
    }

    @RequestMapping({ "/buyProduct" })
    public String listProductHandler(HttpServletRequest request, Model model,
                                     @RequestParam(value = "code", defaultValue = "") String code)
    {
        Product product = null;
        if (code != null && code.length() > 0)
        {
            product = productDao.findProduct(code);
        }
        if (product != null)
        {
            // Cart info stored in Session.
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.addProduct(productInfo, 1);
        }
        // Redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }

    @RequestMapping({ "/shoppingCartRemoveProduct" })
    public String removeProductHandler(HttpServletRequest request, Model model,
                                       @RequestParam(value = "code", defaultValue = "") String code)
    {
        Product product = null;
        if (code != null && code.length() > 0)
        {
            product = productDao.findProduct(code);
        }
        if (product != null)
        {
            // Cart Info stored in Session.
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.removeProduct(productInfo);

        }
        // Redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }

    // POST: Update quantity of products in cart.
    @PostMapping("/shoppingCart")
    public String shoppingCartUpdateQty(HttpServletRequest request, Model model, @ModelAttribute("cartForm") CartInfo cartForm)
    {
        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);

        // Redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }

    // GET: Show Cart
    @GetMapping("/shoppingCart")
    public String shoppingCartHandler(HttpServletRequest request, Model model)
    {
        CartInfo myCart = Utils.getCartInSession(request);

        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    // GET: Enter customer information.
    @GetMapping("/shoppingCartCustomer")
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model)
    {
        CartInfo cartInfo = Utils.getCartInSession(request);

        // Cart is empty.
        if (cartInfo.isEmpty())
        {
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        }

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        if (customerInfo == null)
        {
            customerInfo = new CustomerInfo();
        }

        model.addAttribute("customerForm", customerInfo);

        return "shoppingCartCustomer";
    }

    // POST: Save customer information.
    @PostMapping("/shoppingCartCustomer")
    public String shoppingCartCustomerSave(HttpServletRequest request, Model model,
                                           @ModelAttribute("customerForm") @Validated CustomerInfo customerForm,
                                           BindingResult result, final RedirectAttributes redirectAttributes)
    {
        // If has Errors.
        if (result.hasErrors())
        {
            customerForm.setValid(false);
            // Forward to reenter customer info.
            return "shoppingCartCustomer";
        }

        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);

        cartInfo.setCustomerInfo(customerForm);

        // Redirect to Confirmation page.
        return "redirect:/shoppingCartConfirmation";
    }

    // GET: Review Cart to confirm.
    @GetMapping("/shoppingCartConfirmation")
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model)
    {
        CartInfo cartInfo = Utils.getCartInSession(request);

        // Cart have no products.
        if (cartInfo.isEmpty())
        {
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        }
        else if (!cartInfo.isValidCustomer())
        {
            // Enter customer info.
            return "redirect:/shoppingCartCustomer";
        }
        return "shoppingCartConfirmation";
    }

    // POST: Send Cart (Save).
    @PostMapping("/shoppingCartConfirmation")
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model)
    {
        CartInfo cartInfo = Utils.getCartInSession(request);

        // Cart have no products.
        if (cartInfo.isEmpty())
        {
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        }
        else if (!cartInfo.isValidCustomer())
        {
            // Enter customer info.
            return "redirect:/shoppingCartCustomer";
        }
        try
        {
            orderDao.saveOrder(cartInfo);
        }
        catch (Exception e)
        {
            // Need: Propagation.NEVER?
            return "shoppingCartConfirmation";
        }
        // Remove Cart In Session.
        Utils.removeCartInSession(request);

        // Store Last ordered cart to Session.
        Utils.storeLastOrderedCartInSession(request, cartInfo);

        // Redirect to successful page.
        return "redirect:/shoppingCartFinalize";
    }

    @GetMapping("/shoppingCartFinalize")
    public String shoppingCartFinalize(HttpServletRequest request, Model model)
    {
        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);

        if (lastOrderedCart == null)
        {
            return "redirect:/shoppingCart";
        }
        return "shoppingCartFinalize";
    }

    @GetMapping("/productImage")
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException
    {
        Product product = null;
        if (code != null)
        {
            product = this.productDao.findProduct(code);
        }
        if (product != null && product.getImage() != null)
        {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.getImage());
        }
        response.getOutputStream().close();
    }
}
