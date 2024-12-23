package com.accenture.test.infrastructure.configuration.supplier;

import com.accenture.test.adapter.mapper.supplier.SupplierDtoMapper;
import com.accenture.test.application.gateways.supplier.SupplierGateway;
import com.accenture.test.application.usecase.supplier.SupplierUseCase;
import com.accenture.test.infrastructure.gateway.supplier.SupplierRepoGateway;
import com.accenture.test.infrastructure.mapper.SupplierEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupplierConfiguration {

    @Bean
    public SupplierGateway fornecedorGateway() {
        return new SupplierRepoGateway();
    }

    @Bean
    public SupplierUseCase fornecedorUseCase(SupplierGateway supplierGateway) {
        return new SupplierUseCase(supplierGateway);
    }

    @Bean
    public SupplierEntityMapper fornecedorEntityMapper() {
        return new SupplierEntityMapper();
    }

    @Bean
    public SupplierDtoMapper fornecedorDtoMapper() {
        return new SupplierDtoMapper();
    }
}
