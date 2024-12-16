package com.accenture.test.infrastructure.mapper;

import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.infrastructure.entity.EmpresaEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class EmpresaEntityMapper {

    @Autowired
    private FornecedorEntityMapper fornecedorEntityMapper;

    public Empresa mapFromEntity(EmpresaEntity empresaEntity) {
        return new com.accenture.test.domain.empresa.Empresa(
            empresaEntity.getId(),
            empresaEntity.getCnpj(),
            empresaEntity.getNome_fantasia(),
            empresaEntity.getCep(),
            empresaEntity.getFornecedores().stream().map(fornecedorEntityMapper::mapFromFornecedorEntity).toList()
        );
    }

    public EmpresaEntity mapToEntity(com.accenture.test.domain.empresa.Empresa empresa) {
        return new EmpresaEntity(
            empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome_fantasia(),
            empresa.getCep(),
            empresa.getFornecedores().stream().map(f -> fornecedorEntityMapper.mapToFornecedorEntity(f)).toList()
        );
    }
}
