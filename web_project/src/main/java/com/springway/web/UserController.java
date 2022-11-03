package com.springway.web;

import com.springway.anno.Controller;
import com.springway.anno.RequestMapping;
import com.springway.anno.RequsetBody;
import com.springway.anno.ResponseBody;
import com.springway.entity.User;
import com.springway.model.Model;
import com.springway.model.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/index.action")
    public ModelAndView index(@RequsetBody User loginUser){
        ModelAndView mv = new ModelAndView();
        mv.setAttribute("loginUser", loginUser);
        mv.setView("../home.jsp");
        return mv;
    }

    @RequestMapping("/login.action")
    public String login(@RequsetBody User user){
        System.out.println(user);
        return "";
    }

    @RequestMapping("/reg.action")
    @ResponseBody
    public User reg(@RequsetBody List<User> users, String text, Model model) throws ServletException, IOException {
        System.out.println(users);
        model.setAttribute("users", users);
        return users.get(0);
    }

    @RequestMapping("/jsonTest.action")
    @ResponseBody
    public User jsonTest(@RequsetBody User users, @RequsetBody User admins) throws ServletException, IOException {
        System.out.println(users);
        System.out.println(admins);
        return users;
    }

    @RequestMapping("/jsonListTest.action")
    @ResponseBody
    public User jsonListTest(@RequsetBody List<User> users, @RequsetBody User admins) throws ServletException, IOException {
        System.out.println(users);
        System.out.println(admins);
        return users.get(0);
    }

    @RequestMapping("/listTest.action")
    @ResponseBody
    public User listTest(List<User> users) throws ServletException, IOException {
        System.out.println(users);
        return users.get(0);
    }

    @RequestMapping("/exp.action")
    @ResponseBody
    public String exp() throws ServletException, IOException {
        int num = 5 / 0;

        return "";
    }

    @RequestMapping("/filter.action")
    public String filter(String name) throws ServletException, IOException {
        return name;
    }
}
