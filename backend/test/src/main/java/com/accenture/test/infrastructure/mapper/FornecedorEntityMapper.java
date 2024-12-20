package com.accenture.test.infrastructure.mapper;

import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.infrastructure.entity.EmpresaEntity;
import com.accenture.test.infrastructure.entity.FornecedorEntity;

import java.util.ArrayList;

public class FornecedorEntityMapper {

    public Fornecedor mapFromEntity(FornecedorEntity fornecedor) {
        return new Fornecedor(
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

    public FornecedorEntity mapFromFornecedorToEntity(Fornecedor fornecedor) {
        return new FornecedorEntity(
            fornecedor.getId(),
            fornecedor.getCnpj_cpf(),
            fornecedor.getRg(),
            fornecedor.getNascimento(),
            fornecedor.getNome(),
            fornecedor.getEmail(),
            fornecedor.getCep(),
            fornecedor.isE_pf(),
            new ArrayList<>(fornecedor.getEmpresas().stream().map(this::mapEmpresaEntityWithoutFornecedores).toList())
        );
    }

    private EmpresaEntity mapEmpresaEntityWithoutFornecedores(Empresa empresa) {
        return new EmpresaEntity(
            empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome_fantasia(),
            empresa.getCep(),
            null
        );
    }

    private Empresa mapEmpresaWithoutFornecedores(EmpresaEntity empresa) {
        return new Empresa(
            empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome_fantasia(),
            empresa.getCep(),
            null
        );
    }
}
