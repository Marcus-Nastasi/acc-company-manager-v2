package com.accenture.test.infrastructure.mapper;

import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.infrastructure.entity.EmpresaEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class EmpresaEntityMapper {

    @Autowired
    private FornecedorEntityMapper fornecedorEntityMapper;

    public Empresa mapFromEntity(EmpresaEntity empresaEntity) {
        return new Empresa(
            empresaEntity.getId(),
            empresaEntity.getCnpj(),
            empresaEntity.getNome_fantasia(),
            empresaEntity.getCep(),
            new ArrayList<>(empresaEntity.getFornecedores().stream().map(fornecedorEntityMapper::mapFromEntity).toList())
        );
    }

    public EmpresaEntity mapToEntity(Empresa empresa) {
        return new EmpresaEntity(
            empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome_fantasia(),
            empresa.getCep(),
            new ArrayList<>(empresa.getFornecedores().stream().map(fornecedorEntityMapper::mapFromFornecedorToEntity).toList()));
    }
}
