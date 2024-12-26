package com.accenture.test.application.usecase.company;

import com.accenture.test.domain.cep.Cep;
import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.company.CompanyPag;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.usecase.cep.CepUseCase;
import com.accenture.test.application.usecase.supplier.SupplierUseCase;
import com.accenture.test.application.gateways.company.CompanyGateway;

import java.util.UUID;

public class CompanyUseCase {

    private final CompanyGateway companyGateway;
    private final SupplierUseCase supplierUseCase;
    private final CepUseCase cepUseCase;

    public CompanyUseCase(CompanyGateway companyGateway, SupplierUseCase supplierUseCase, CepUseCase cepUseCase) {
        this.companyGateway = companyGateway;
        this.supplierUseCase = supplierUseCase;
        this.cepUseCase = cepUseCase;
    }

    public CompanyPag getAll(int page, int size, String name, String cnpj, String cep) {
        return companyGateway.getAll(page, size, name, cnpj, cep);
    }

    public Company get(UUID id) {
        return companyGateway.get(id);
    }

    public Company register(Company data) {
        return companyGateway.save(data);
    }

    public Company update(UUID id, Company data) {
        Company company = get(id);
        company.setCnpj(data.getCnpj());
        company.setName(data.getName());
        company.setCep(data.getCep());
        return companyGateway.save(company);
    }

    public Company delete(UUID id) {
        return companyGateway.delete(id);
    }

    public Company associateSupplier(UUID id_supplier, UUID id) {
        Company company = get(id);
        Supplier supplier = supplierUseCase.get(id_supplier);
        if (supplier.isE_pf() && isPr(company.getCep()) && supplierUseCase.validatesUnderageSuppliers(supplier.getBirth())) {
            throw new AppException("It's not permitted to register an underage supplier in Paraná");
        }
        linkCompanySupplier(company, supplier);
        return company;
    }

    public Company disassociateSupplier(UUID supplier_id, UUID company_id) {
        Company company = get(company_id);
        Supplier supplier = supplierUseCase.get(supplier_id);
        unlinkCompanySupplier(company, supplier);
        return company;
    }

    public boolean isPr(String cep) {
        Cep response = cepUseCase.getCep(cep);
        if (response == null) throw new AppException("Error getting CEP");
        return response.getUf().equalsIgnoreCase("PR");
    }

    public void linkCompanySupplier(Company company, Supplier supplier) {
        company.getSuppliers().add(supplier);
        supplier.getCompanies().add(company);
        companyGateway.save(company);
        supplierUseCase.save(supplier);
    }

    public void unlinkCompanySupplier(Company company, Supplier supplier) {
        supplier.getCompanies().removeIf(e -> e.getId().equals(company.getId()));
        company.getSuppliers().removeIf(f -> f.getId().equals(supplier.getId()));
        supplierUseCase.save(supplier);
        companyGateway.save(company);
    }
}
