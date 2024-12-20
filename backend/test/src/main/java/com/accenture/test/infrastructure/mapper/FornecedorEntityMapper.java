package com.accenture.test.infrastructure.mapper;

import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.infrastructure.entity.FornecedorEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class FornecedorEntityMapper {

    @Autowired
    private EmpresaEntityMapper empresaEntityMapper;

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
            new ArrayList<>(fornecedor.getEmpresas().stream().map(empresaEntityMapper::mapFromEntity).toList())
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
            new ArrayList<>(fornecedor.getEmpresas().stream().map(empresaEntityMapper::mapToEntity).toList())
        );
    }
}
