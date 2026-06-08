package com.dangosil.cashflow;

import org.springframework.boot.SpringApplication;

public class TestCashflowApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(CashflowApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
