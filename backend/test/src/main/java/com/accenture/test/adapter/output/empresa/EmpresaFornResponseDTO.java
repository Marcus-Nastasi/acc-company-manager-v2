package com.accenture.test.adapter.output.empresa;

import com.accenture.test.adapter.output.fornecedor.FornecedorResponseDTO;

import java.util.List;
import java.util.UUID;

public record EmpresaFornResponseDTO(
        UUID id,
        String cnpj,
        String nome_fantasia,
        String cep,
        List<FornecedorResponseDTO> fornecedores
) {}
