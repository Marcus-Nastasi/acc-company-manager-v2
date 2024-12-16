package com.accenture.test.adapter.input.fornecedor;

import java.time.LocalDate;

public record RegistrarFornecedorDTO(
        String cnpj_cpf,
        String rg,
        LocalDate nascimento,
        String nome,
        String email,
        String cep,
        boolean e_pf
) {}
