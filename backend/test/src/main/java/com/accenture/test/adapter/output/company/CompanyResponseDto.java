package com.accenture.test.adapter.output.company;

import com.accenture.test.adapter.output.supplier.SupplierCleanDto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record CompanyResponseDto(
       UUID id,
       String cnpj,
       String nome_fantasia,
       String cep,
       List<SupplierCleanDto> fornecedores
) implements Serializable {}
