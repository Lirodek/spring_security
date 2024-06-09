package com.security.demosecurity.form;

import com.security.demosecurity.account.Account;
import com.security.demosecurity.account.UserAccount;
import com.security.demosecurity.book.BookRepository;
import com.security.demosecurity.common.CurrentUser;
import com.security.demosecurity.common.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * SampleController
 */
@RestController
public class SampleController {
    @Autowired
    SampleService sampleService;

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/")
    public ModelAndView getMethodName(ModelAndView mav, @CurrentUser Account principal) {
        if (Optional.ofNullable(principal).isPresent()) {
            mav.addObject("message", "Hello ," + principal.getUsername());
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

    @GetMapping("/user")
    public ModelAndView user(ModelAndView mav, Principal principal) {
        mav.addObject("message", "Hello User ," + principal.getName());
        mav.addObject("books", bookRepository.findCurrentUserBook());
        mav.setViewName("user");

        return mav;
    }

    @GetMapping("/admin")
    public ModelAndView admin(ModelAndView mav, Principal principal) {
        mav.addObject("message", "Hello Admin ," + principal.getName());
        mav.setViewName("admin");

        return mav;
    }

    @GetMapping("/async-handller")
    @ResponseBody
    public Callable<String> asynHandler(ModelAndView mav) {
        SecurityLogger.log("MVC");
        return () -> {
            SecurityLogger.log("Callable");
            return "Async Handler";
        };
    }

    @GetMapping("/async-service")
    @ResponseBody
    public String asynService(ModelAndView mav) {
        SecurityLogger.log("MVC before async service");
        sampleService.asyncService();
        SecurityLogger.log("MVC after async service");

        return "Async Service";
    }
}