package com.accenture.test.infrastructure.mapper;

import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.infrastructure.entity.EmpresaEntity;
import com.accenture.test.infrastructure.entity.FornecedorEntity;

import java.util.ArrayList;

public class EmpresaEntityMapper {

    public Empresa mapFromEntity(EmpresaEntity empresaEntity) {
        return new Empresa(
            empresaEntity.getId(),
            empresaEntity.getCnpj(),
            empresaEntity.getNome_fantasia(),
            empresaEntity.getCep(),
            new ArrayList<>(empresaEntity.getFornecedores().stream().map(this::mapFornecedorWithoutEmpresas).toList())
        );
    }

    public EmpresaEntity mapToEntity(Empresa empresa) {
        return new EmpresaEntity(
            empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome_fantasia(),
            empresa.getCep(),
            new ArrayList<>(empresa.getFornecedores().stream().map(this::mapFornecedorEntityWithoutEmpresas).toList()));
    }

    private FornecedorEntity mapFornecedorEntityWithoutEmpresas(Fornecedor fornecedor) {
        return new FornecedorEntity(
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

    private Fornecedor mapFornecedorWithoutEmpresas(FornecedorEntity fornecedor) {
        return new Fornecedor(
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
