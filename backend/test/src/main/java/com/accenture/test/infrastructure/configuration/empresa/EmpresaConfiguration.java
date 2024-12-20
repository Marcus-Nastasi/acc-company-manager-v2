package com.accenture.test.infrastructure.configuration.empresa;

import com.accenture.test.adapter.mapper.empresa.EmpresaDtoMapper;
import com.accenture.test.application.gateways.empresa.EmpresaGateway;
import com.accenture.test.application.gateways.fornecedor.FornecedorGateway;
import com.accenture.test.application.usecase.cep.CepService;
import com.accenture.test.application.usecase.empresa.EmpresaUseCase;
import com.accenture.test.application.usecase.fornecedor.FornecedorUseCase;
import com.accenture.test.infrastructure.gateway.empresa.EmpresaRepoGateway;
import com.accenture.test.infrastructure.mapper.EmpresaEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmpresaConfiguration {

    @Bean
    public EmpresaGateway empresaGateway() {
        return new EmpresaRepoGateway();
    }

    @Bean
    public EmpresaUseCase empresaUseCase(EmpresaGateway empresaGateway, FornecedorGateway fornecedorGateway, FornecedorUseCase fornecedorUseCase, CepService cepService) {
        return new EmpresaUseCase(empresaGateway, fornecedorGateway, fornecedorUseCase, cepService);
    }

    @Bean
    public EmpresaEntityMapper empresaEntityMapper() {
        return new EmpresaEntityMapper();
    }

    @Bean
    public EmpresaDtoMapper empresaDtoMapper() {
        return new EmpresaDtoMapper();
    }
}
