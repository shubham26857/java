package com.example.splitpay;

import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.services.SplitPayUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class SplitpayApplication {

	// I am trying to autowire this
	public static void main(String[] args) {
		SpringApplication.run(SplitpayApplication.class, args);
		System.out.println("Server is running");
	}

}

