package com.accenture.test.adapter.output.fornecedor;

import java.time.LocalDate;
import java.util.UUID;

public record FornecedorResponseDTO(
        UUID id,
        String cnpj_cpf,
        String rg,
        LocalDate nascimento,
        String nome,
        String email,
        String cep,
        boolean e_pf
) {}
