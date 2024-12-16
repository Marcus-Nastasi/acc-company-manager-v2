package com.accenture.test;

import com.accenture.test.application.usecase.cep.CepService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CepTest {

    @Autowired
    private CepService cepService;

    @Test
    void cepTest() {
        Assertions.assertDoesNotThrow(() -> {
            cepService.buscarCep("04632-011");
        });
    }
}
