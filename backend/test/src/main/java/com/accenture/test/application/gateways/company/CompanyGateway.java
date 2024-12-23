package com.accenture.test.application.gateways.company;

import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.company.CompanyPag;

import java.util.UUID;

public interface CompanyGateway {

    CompanyPag getAll(int page, int size, String name, String cnpj, String cep);

    Company get(UUID id);

    Company save(Company data);

    Company delete(UUID id);
}
