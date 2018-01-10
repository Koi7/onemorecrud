package com.ramazan.myapp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyAppController {

    @RequestMapping(value =  "/", method = RequestMethod.GET)
    public String index()  {
       return "";
    }
}
