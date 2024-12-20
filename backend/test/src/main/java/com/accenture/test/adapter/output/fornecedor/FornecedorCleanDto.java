package com.accenture.test.adapter.output.fornecedor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public record FornecedorCleanDto(
        UUID id,
        String cnpj_cpf,
        String rg,
        LocalDate nascimento,
        String nome,
        String email,
        String cep,
        boolean e_pf
) implements Serializable {}
