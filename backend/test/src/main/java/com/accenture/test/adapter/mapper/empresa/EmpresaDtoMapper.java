package com.accenture.test.adapter.mapper.empresa;

import com.accenture.test.adapter.input.empresa.EmpresaRequestDto;
import com.accenture.test.adapter.mapper.fornecedor.FornecedorDtoMapper;
import com.accenture.test.adapter.output.empresa.EmpresaCleanDto;
import com.accenture.test.adapter.output.empresa.EmpresaResponseDto;
import com.accenture.test.domain.empresa.Empresa;
import org.springframework.beans.factory.annotation.Autowired;

public class EmpresaDtoMapper {

    @Autowired
    private FornecedorDtoMapper fornecedorDtoMapper;

    public Empresa mapFromRequest(EmpresaRequestDto empresaRequestDto) {
        return new Empresa(
                empresaRequestDto.id(),
                empresaRequestDto.cnpj(),
                empresaRequestDto.nome_fantasia(),
                empresaRequestDto.cep(),
                empresaRequestDto.fornecedores()
        );
    }

    public EmpresaResponseDto mapToResponse(Empresa empresa) {
        return new EmpresaResponseDto(
                empresa.getId(),
                empresa.getCnpj(),
                empresa.getNome_fantasia(),
                empresa.getCep(),
                empresa.getFornecedores().stream().map(fornecedorDtoMapper::mapToClean).toList()
        );
    }

    public EmpresaCleanDto mapToClean(Empresa empresa) {
        return new EmpresaCleanDto(
                empresa.getId(),
                empresa.getCnpj(),
                empresa.getNome_fantasia(),
                empresa.getCep()
        );
    }
}
