package org.bpm.abcbook.controller;

import jakarta.inject.Named;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
@Named
public class HelloBean {
    private String name;
    private String message;

    public void sayHello() {
        this.message = "Hello, " + name + "!";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
