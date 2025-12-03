package com.pizzamdp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {
    @GetMapping({"/oms/any", "/oms/other", "/oms/ordenes", "/oms/ordenes/status", "/catalogo/any", "/stock/any"})
    public String handleAll() {
        return "ok";
    }
}
