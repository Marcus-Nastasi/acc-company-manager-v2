package com.accenture.test.application.gateways.fornecedor;

import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorPag;

import java.util.UUID;

public interface FornecedorGateway {

    FornecedorPag<Fornecedor> getAll(int page, int size, String nome, String cnpj_cpf);

    Fornecedor get(UUID id);

    Fornecedor save(Fornecedor fornecedor);

    Fornecedor delete(UUID id);
}
