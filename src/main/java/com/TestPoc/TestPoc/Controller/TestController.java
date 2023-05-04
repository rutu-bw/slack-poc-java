package com.TestPoc.TestPoc.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/")
    public String welcome(){
        return "Welcome!!";
    }

}
