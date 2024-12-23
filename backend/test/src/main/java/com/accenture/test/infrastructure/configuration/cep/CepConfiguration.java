package com.accenture.test.infrastructure.configuration.cep;

import com.accenture.test.application.gateways.cep.CepGateway;
import com.accenture.test.application.usecase.cep.CepUseCase;
import com.accenture.test.infrastructure.gateway.cep.CepRepoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CepConfiguration {

    @Bean
    public CepGateway cepGateway() {
        return new CepRepoGateway();
    }

    @Bean
    public CepUseCase cepUseCase(CepGateway cepGateway) {
        return new CepUseCase(cepGateway);
    }
}
