package com.accenture.test.infrastructure.mapper;

import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.infrastructure.entity.CompanyEntity;
import com.accenture.test.infrastructure.entity.SupplierEntity;

import java.util.ArrayList;

public class SupplierEntityMapper {

    public Supplier mapFromEntity(SupplierEntity fornecedor) {
        return new Supplier(
            fornecedor.getId(),
            fornecedor.getCnpj_cpf(),
            fornecedor.getRg(),
            fornecedor.getNascimento(),
            fornecedor.getNome(),
            fornecedor.getEmail(),
            fornecedor.getCep(),
            fornecedor.isE_pf(),
            new ArrayList<>(fornecedor.getEmpresas().stream().map(this::mapEmpresaWithoutFornecedores).toList())
        );
    }

    public SupplierEntity mapFromFornecedorToEntity(Supplier supplier) {
        return new SupplierEntity(
            supplier.getId(),
            supplier.getCnpj_cpf(),
            supplier.getRg(),
            supplier.getNascimento(),
            supplier.getNome(),
            supplier.getEmail(),
            supplier.getCep(),
            supplier.isE_pf(),
            new ArrayList<>(supplier.getEmpresas().stream().map(this::mapEmpresaEntityWithoutFornecedores).toList())
        );
    }

    private CompanyEntity mapEmpresaEntityWithoutFornecedores(Company company) {
        return new CompanyEntity(
            company.getId(),
            company.getCnpj(),
            company.getNome_fantasia(),
            company.getCep(),
            null
        );
    }

    private Company mapEmpresaWithoutFornecedores(CompanyEntity empresa) {
        return new Company(
            empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome_fantasia(),
            empresa.getCep(),
            null
        );
    }
}
