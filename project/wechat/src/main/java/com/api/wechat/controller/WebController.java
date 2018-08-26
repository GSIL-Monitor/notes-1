package com.api.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author   NieYinjun
 * Date     2018/8/17 17:23
 * TAG
 */
@Controller
public class WebController {
    @RequestMapping(value = "/fail")
    public String testWeb() {
        System.out.println("---------------------");
        return "fail";
    }
}
