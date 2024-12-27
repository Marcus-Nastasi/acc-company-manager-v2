package com.accenture.test;

import com.accenture.test.application.usecase.cep.CepUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CepIntegrationTest {

    @Autowired
    private CepUseCase cepUseCase;

    @Test
    void cepTest() {
        assertEquals("Avenida Vicente Machado", cepUseCase.getCep("80420-010").getLogradouro());
        assertEquals("PR", cepUseCase.getCep("80420-010").getUf());
        assertDoesNotThrow(() -> cepUseCase.getCep("80420-010"));
    }
}
