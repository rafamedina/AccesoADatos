package com.example.demo;

import com.example.demo.Controller.Controller;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootYLombokApplication {
@Autowired
public static Controller controller;
	public static void main(String[] args) {
        SpringApplication.run(SpringBootYLombokApplication.class, args);
	}

}
