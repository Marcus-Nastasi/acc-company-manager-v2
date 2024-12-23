package com.accenture.test.infrastructure.configuration.company;

import com.accenture.test.adapter.mapper.company.CompanyDtoMapper;
import com.accenture.test.application.gateways.company.CompanyGateway;
import com.accenture.test.application.usecase.cep.CepUseCase;
import com.accenture.test.application.usecase.company.CompanyUseCase;
import com.accenture.test.application.usecase.supplier.SupplierUseCase;
import com.accenture.test.infrastructure.gateway.company.CompanyRepoGateway;
import com.accenture.test.infrastructure.mapper.CompanyEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanyConfiguration {

    @Bean
    public CompanyGateway empresaGateway() {
        return new CompanyRepoGateway();
    }

    @Bean
    public CompanyUseCase empresaUseCase(CompanyGateway companyGateway, SupplierUseCase supplierUseCase, CepUseCase cepUseCase) {
        return new CompanyUseCase(companyGateway, supplierUseCase, cepUseCase);
    }

    @Bean
    public CompanyEntityMapper empresaEntityMapper() {
        return new CompanyEntityMapper();
    }

    @Bean
    public CompanyDtoMapper empresaDtoMapper() {
        return new CompanyDtoMapper();
    }
}
