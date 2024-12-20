package com.accenture.test.adapter.mapper.fornecedor;

import com.accenture.test.adapter.input.fornecedor.FornecedorRequestDto;
import com.accenture.test.adapter.mapper.empresa.EmpresaDtoMapper;
import com.accenture.test.adapter.output.fornecedor.FornecedorCleanDto;
import com.accenture.test.adapter.output.fornecedor.FornecedorResponseDto;
import com.accenture.test.domain.fornecedor.Fornecedor;
import org.springframework.beans.factory.annotation.Autowired;

public class FornecedorDtoMapper {

    @Autowired
    private EmpresaDtoMapper empresaDtoMapper;

    public FornecedorResponseDto mapToResponse(Fornecedor fornecedor) {
        return new FornecedorResponseDto(
                fornecedor.getId(),
                fornecedor.getCnpj_cpf(),
                fornecedor.getRg(),
                fornecedor.getNascimento(),
                fornecedor.getNome(),
                fornecedor.getEmail(),
                fornecedor.getCep(),
                fornecedor.isE_pf(),
                fornecedor.getEmpresas().stream().map(empresaDtoMapper::mapToClean).toList()
        );
    }

    public Fornecedor mapFromRequest(FornecedorRequestDto fornecedorRequestDto) {
        return new Fornecedor(
                null,
                fornecedorRequestDto.cnpj_cpf(),
                fornecedorRequestDto.rg(),
                fornecedorRequestDto.nascimento(),
                fornecedorRequestDto.nome(),
                fornecedorRequestDto.email(),
                fornecedorRequestDto.cep(),
                fornecedorRequestDto.e_pf(),
                null
        );
    }

    public FornecedorCleanDto mapToClean(Fornecedor fornecedor) {
        return new FornecedorCleanDto(
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
}
