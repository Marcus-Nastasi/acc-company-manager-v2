package com.accenture.test.adapter.output.empresa;

import com.accenture.test.adapter.output.fornecedor.FornecedorCleanDto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record EmpresaResponseDto(
       UUID id,
       String cnpj,
       String nome_fantasia,
       String cep,
       List<FornecedorCleanDto> fornecedores
) implements Serializable {}
