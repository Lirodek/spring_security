package com.security.demosecurity.form;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * SampleController
 */
@RestController
public class SampleController {

    @GetMapping("/")
    public ModelAndView getMethodName(ModelAndView mav, Principal principal) {
        if(Optional.ofNullable(principal).isPresent()){
            mav.addObject("message", "Hello ," + principal.getName());
        } else {
            mav.addObject("message", "Hello Spring Security!!");
        }

        
        mav.setViewName("index");

        return mav;
    }

    @GetMapping("/info")
    public ModelAndView info(ModelAndView mav) {
        mav.addObject("message", "Info");
        mav.setViewName("info");
        
        return mav;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(ModelAndView mav, Principal principal) {
        mav.addObject("message", "Hello ," + principal.getName());
        mav.setViewName("dashboard");
        
        return mav;
    }

    @GetMapping("/admin")
    public ModelAndView admin(ModelAndView mav, Principal principal) {
        mav.addObject("message", "Hello Admin ," + principal.getName());
        mav.setViewName("admin");
        
        return mav;
    }
    
}