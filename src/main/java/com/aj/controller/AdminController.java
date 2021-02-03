package com.aj.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@Transactional
@EnableWebMvc
public class AdminController
{
    @GetMapping(value = "/login")
    public String login()
    {
        return "login";
    }

    @GetMapping(value = "/accountInfo")
    public ModelAndView accountInfo()
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ModelAndView("accountInfo", "userDetails", userDetails);
    }
}
