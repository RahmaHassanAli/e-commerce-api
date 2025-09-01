package com.cgc.e_commerce;

import com.cgc.e_commerce.model.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceApplication {

	public static void main(String[] args) {
		Product p=new Product();

		SpringApplication.run(ECommerceApplication.class, args);
	}

}
