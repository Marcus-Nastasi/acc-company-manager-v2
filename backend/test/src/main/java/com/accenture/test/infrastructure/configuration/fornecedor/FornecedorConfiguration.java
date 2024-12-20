package com.accenture.test.infrastructure.configuration.fornecedor;

import com.accenture.test.adapter.mapper.fornecedor.FornecedorDtoMapper;
import com.accenture.test.application.gateways.fornecedor.FornecedorGateway;
import com.accenture.test.application.usecase.fornecedor.FornecedorUseCase;
import com.accenture.test.infrastructure.gateway.fornecedor.FornecedorRepoGateway;
import com.accenture.test.infrastructure.mapper.FornecedorEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FornecedorConfiguration {

    @Bean
    public FornecedorGateway fornecedorGateway() {
        return new FornecedorRepoGateway();
    }

    @Bean
    public FornecedorUseCase fornecedorUseCase(FornecedorGateway fornecedorGateway) {
        return new FornecedorUseCase(fornecedorGateway);
    }

    @Bean
    public FornecedorEntityMapper fornecedorEntityMapper() {
        return new FornecedorEntityMapper();
    }

    @Bean
    public FornecedorDtoMapper fornecedorDtoMapper() {
        return new FornecedorDtoMapper();
    }
}
