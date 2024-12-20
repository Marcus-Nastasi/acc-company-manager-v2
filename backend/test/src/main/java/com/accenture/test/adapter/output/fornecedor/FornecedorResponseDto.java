package com.accenture.test.adapter.output.fornecedor;

import com.accenture.test.adapter.output.empresa.EmpresaCleanDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record FornecedorResponseDto(
        UUID id,
        String cnpj_cpf,
        String rg,
        LocalDate nascimento,
        String nome,
        String email,
        String cep,
        boolean e_pf,
        List<EmpresaCleanDto> empresas
) implements Serializable {}
