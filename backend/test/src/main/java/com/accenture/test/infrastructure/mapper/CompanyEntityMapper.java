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
            companyEntity.getName(),
            companyEntity.getCep(),
            new ArrayList<>(companyEntity.getSuppliers().stream().map(this::mapSupplierWithoutCompanies).toList())
        );
    }

    public CompanyEntity mapToEntity(Company company) {
        return new CompanyEntity(
            company.getId(),
            company.getCnpj(),
            company.getName(),
            company.getCep(),
            new ArrayList<>(company.getSuppliers().stream().map(this::mapSupplierEntityWithoutCompanies).toList()));
    }

    private SupplierEntity mapSupplierEntityWithoutCompanies(Supplier supplier) {
        return new SupplierEntity(
            supplier.getId(),
            supplier.getCnpj_cpf(),
            supplier.getRg(),
            supplier.getBirth(),
            supplier.getName(),
            supplier.getEmail(),
            supplier.getCep(),
            supplier.isE_pf(),
            null
        );
    }

    private Supplier mapSupplierWithoutCompanies(SupplierEntity supplier) {
        return new Supplier(
            supplier.getId(),
            supplier.getCnpj_cpf(),
            supplier.getRg(),
            supplier.getBirth(),
            supplier.getName(),
            supplier.getEmail(),
            supplier.getCep(),
            supplier.isE_pf(),
            null
        );
    }
}
