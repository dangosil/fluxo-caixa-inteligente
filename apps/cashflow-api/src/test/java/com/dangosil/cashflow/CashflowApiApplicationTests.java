package com.dangosil.cashflow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CashflowApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
