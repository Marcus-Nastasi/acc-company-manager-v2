package com.accenture.test.application.gateways.supplier;

import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.domain.supplier.SupplierPag;

import java.util.UUID;

public interface SupplierGateway {

    SupplierPag<Supplier> getAll(int page, int size, String nome, String cnpj_cpf);

    Supplier get(UUID id);

    Supplier save(Supplier supplier);

    Supplier delete(UUID id);
}
