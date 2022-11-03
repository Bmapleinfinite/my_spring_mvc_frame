package com.springway.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放需要放到作用域里的数据
 * model的作用域是request
 */
public class ModelAndView {
    private Model model = new Model();
    private String view;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getView() {
        return view;
    }

    public ConcurrentHashMap<String, Object> getAttributes() {
        return model.getAttributes();
    }

    public void setAttribute(String key, Object value) {
        model.setAttribute(key, value);
    }

    public void setView(String view) {
        this.view = view;
    }
}
