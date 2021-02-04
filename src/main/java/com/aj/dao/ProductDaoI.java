package com.aj.dao;

import com.aj.entity.Product;
import com.aj.model.PaginationResult;
import com.aj.model.ProductInfo;

public interface ProductDaoI
{
    Product findProduct(String code);
    ProductInfo findProductInfo(String code) ;
    PaginationResult<ProductInfo> queryProducts(int page,
                                                int maxResult, int maxNavigationPage  );
    PaginationResult<ProductInfo> queryProducts(int page, int maxResult,
                                                       int maxNavigationPage, String likeName);
    void save(ProductInfo productInfo);
}
