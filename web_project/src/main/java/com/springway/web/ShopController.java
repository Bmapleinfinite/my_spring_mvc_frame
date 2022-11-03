package com.springway.web;

import com.springway.anno.Controller;
import com.springway.anno.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @RequestMapping("/show.action")
    public String show(){
        System.out.println("嗨嗨嗨");
        return "你好呀";
    }

    @RequestMapping("/say.action")
    public String say(String name, Integer number){
        System.out.println("听我说");
        return "谢谢你";
    }
}
