package com.accenture.test.infrastructure.mapper;

import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.infrastructure.entity.CompanyEntity;
import com.accenture.test.infrastructure.entity.SupplierEntity;

import java.util.ArrayList;

public class CompanyEntityMapper {

    public Company mapFromEntity(CompanyEntity companyEntity) {
        return new Company(
            companyEntity.getId(),
            companyEntity.getCnpj(),
            companyEntity.getNome_fantasia(),
            companyEntity.getCep(),
            new ArrayList<>(companyEntity.getFornecedores().stream().map(this::mapFornecedorWithoutEmpresas).toList())
        );
    }

    public CompanyEntity mapToEntity(Company company) {
        return new CompanyEntity(
            company.getId(),
            company.getCnpj(),
            company.getNome_fantasia(),
            company.getCep(),
            new ArrayList<>(company.getFornecedores().stream().map(this::mapFornecedorEntityWithoutEmpresas).toList()));
    }

    private SupplierEntity mapFornecedorEntityWithoutEmpresas(Supplier supplier) {
        return new SupplierEntity(
            supplier.getId(),
            supplier.getCnpj_cpf(),
            supplier.getRg(),
            supplier.getNascimento(),
            supplier.getNome(),
            supplier.getEmail(),
            supplier.getCep(),
            supplier.isE_pf(),
            null
        );
    }

    private Supplier mapFornecedorWithoutEmpresas(SupplierEntity fornecedor) {
        return new Supplier(
            fornecedor.getId(),
            fornecedor.getCnpj_cpf(),
            fornecedor.getRg(),
            fornecedor.getNascimento(),
            fornecedor.getNome(),
            fornecedor.getEmail(),
            fornecedor.getCep(),
            fornecedor.isE_pf(),
            null
        );
    }
}
