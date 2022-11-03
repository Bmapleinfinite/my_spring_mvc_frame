package com.springway.app;

public class App {
    public static void main(String[] args) {
        String test = "com.springway.controller";
        test = test.replace(".", "\\");
        System.out.println(test);
    }
}
