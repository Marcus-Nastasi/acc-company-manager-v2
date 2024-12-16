package com.accenture.test.adapter.mapper.fornecedor;

import com.accenture.test.adapter.input.fornecedor.RegistrarFornecedorDTO;
import com.accenture.test.adapter.output.fornecedor.FornecedorEmpResponseDTO;
import com.accenture.test.adapter.output.fornecedor.FornecedorResponseDTO;
import com.accenture.test.application.usecase.empresa.EmpresaService;
import com.accenture.test.domain.fornecedor.FornecedorEmpresa;
import com.accenture.test.infrastructure.entity.FornecedorEntity;
import com.accenture.test.infrastructure.mapper.EmpresaEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class FornecedorDtoMapper {

    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private EmpresaEntityMapper empresaEntityMapper;

    public FornecedorResponseDTO mapToFornecedorResponseDTO(FornecedorEntity fornecedorEntity) {
        return new FornecedorResponseDTO(
                fornecedorEntity.getId(),
                fornecedorEntity.getCnpj_cpf(),
                fornecedorEntity.getRg(),
                fornecedorEntity.getNascimento(),
                fornecedorEntity.getNome(),
                fornecedorEntity.getEmail(),
                fornecedorEntity.getCep(),
                fornecedorEntity.isE_pf()
        );
    }

    public FornecedorEmpResponseDTO mapToFornecedorEmpResponseDTO(FornecedorEmpresa fornecedorEntity) {
        return new FornecedorEmpResponseDTO(
                fornecedorEntity.getId(),
                fornecedorEntity.getCnpj_cpf(),
                fornecedorEntity.getRg(),
                fornecedorEntity.getNascimento(),
                fornecedorEntity.getNome(),
                fornecedorEntity.getEmail(),
                fornecedorEntity.getCep(),
                fornecedorEntity.isE_pf(),
                fornecedorEntity.getEmpresaEntities().stream().map(e -> empresaEntityMapper.mapFromEntity(e)).toList()
        );
    }

    public FornecedorEmpresa mapFromRequestToFornecedorEmpresa(RegistrarFornecedorDTO fornecedorEntity) {
        return new FornecedorEmpresa(
            null,
            fornecedorEntity.cnpj_cpf(),
            fornecedorEntity.rg(),
            fornecedorEntity.nascimento(),
            fornecedorEntity.nome(),
            fornecedorEntity.email(),
            fornecedorEntity.cep(),
            fornecedorEntity.e_pf(),
            null
        );
    }
}
