package com.beautycare.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase; // 1. Importar
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // 2. Añadir esta línea
class BeautycareApiApplicationTests {

	@Test
	void contextLoads() {
	}

}