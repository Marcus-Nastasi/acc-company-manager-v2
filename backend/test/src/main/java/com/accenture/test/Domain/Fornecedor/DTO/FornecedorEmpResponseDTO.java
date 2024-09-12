package com.accenture.test.Domain.Fornecedor.DTO;

import com.accenture.test.Domain.Empresa.DTO.EmpresaResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record FornecedorEmpResponseDTO(
        UUID id,
        String cnpj_cpf,
        String rg,
        LocalDate nascimento,
        String nome,
        String email,
        String cep,
        boolean e_pf,
        List<EmpresaResponseDTO> empresas
) {}
