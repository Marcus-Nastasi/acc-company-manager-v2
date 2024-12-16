package com.accenture.test.application.gateways.fornecedor;

import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorEmpresa;
import com.accenture.test.domain.fornecedor.FornecedorPagResponse;

import java.util.UUID;

public interface FornecedorGateway {

    FornecedorPagResponse<Fornecedor> getAll(int page, int size, String nome, String cnpj_cpf);

    FornecedorEmpresa get(UUID id);

    FornecedorEmpresa save(FornecedorEmpresa fornecedorEmpresa);

    FornecedorEmpresa delete(UUID id);
}
