package com.sge.qrcoderest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QrCodeGeneratorRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrCodeGeneratorRestApplication.class, args);
		System.out.println("server is ready for requests");
	}

}
