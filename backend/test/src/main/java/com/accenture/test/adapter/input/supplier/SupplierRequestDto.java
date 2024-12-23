package com.accenture.test.adapter.input.supplier;

import java.time.LocalDate;

public record SupplierRequestDto(
        String cnpj_cpf,
        String rg,
        LocalDate nascimento,
        String nome,
        String email,
        String cep,
        boolean e_pf
) {}
