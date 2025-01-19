package com.vaccine.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sba301/v1/user")
public class UserController {

    @GetMapping("/test")
    public String testne() {
        return "testne";
    }

}
