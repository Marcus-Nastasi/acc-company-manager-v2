package com.accenture.test.Domain.Empresa.DTO;

import com.accenture.test.Domain.Fornecedor.DTO.FornecedorResponseDTO;

import java.util.List;
import java.util.UUID;

public record EmpresaFornResponseDTO(
        UUID id,
        String cnpj,
        String nome_fantasia,
        String cep,
        List<FornecedorResponseDTO> fornecedores
) {}
