package com.leno.crawler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leon
 * @date 2018-06-21 10:14
 * @desc
 */
@RestController
public class MovieController {
    @RequestMapping("test")
    public String test(){
        return "test";
    }
}
