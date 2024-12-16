package com.accenture.test.infrastructure.mapper;

import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorEmpresa;
import com.accenture.test.infrastructure.entity.FornecedorEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class FornecedorEntityMapper {

    @Autowired
    private EmpresaEntityMapper empresaEntityMapper;

    public FornecedorEmpresa mapFromEntityToFornecedorEmpresa(FornecedorEntity fornecedor) {
        return new FornecedorEmpresa(
            fornecedor.getId(),
            fornecedor.getCnpj_cpf(),
            fornecedor.getRg(),
            fornecedor.getNascimento(),
            fornecedor.getNome(),
            fornecedor.getEmail(),
            fornecedor.getCep(),
            fornecedor.isE_pf(),
            fornecedor.getEmpresaEntityEntities().stream().map(empresaEntityMapper::mapFromEntity).toList()
        );
    }

    public Fornecedor mapFromFornecedorEntity(FornecedorEntity fornecedor) {
        return new Fornecedor(
            fornecedor.getId(),
            fornecedor.getCnpj_cpf(),
            fornecedor.getRg(),
            fornecedor.getNascimento(),
            fornecedor.getNome(),
            fornecedor.getEmail(),
            fornecedor.getCep(),
            fornecedor.isE_pf()
        );
    }

    public FornecedorEntity mapToFornecedorEntity(FornecedorEmpresa fornecedor) {
        return new FornecedorEntity(
            fornecedor.getId(),
            fornecedor.getCnpj_cpf(),
            fornecedor.getRg(),
            fornecedor.getNascimento(),
            fornecedor.getNome(),
            fornecedor.getEmail(),
            fornecedor.getCep(),
            fornecedor.isE_pf(),
            fornecedor.getEmpresaEntities().stream().map(empresaEntityMapper::mapToEntity).toList()
        );
    }
}
