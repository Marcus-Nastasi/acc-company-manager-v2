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

    public CompanyPag buscar_tudo(int page, int size, String nome_fantasia, String cnpj, String cep) {
        return companyGateway.getAll(page, size, nome_fantasia, cnpj, cep);
    }

    public Company buscar_um(UUID id) {
        return companyGateway.get(id);
    }

    public Company registrar(Company data) {
        return companyGateway.save(data);
    }

    public Company atualizar(UUID id, Company data) {
        Company company = buscar_um(id);
        company.setCnpj(data.getCnpj());
        company.setNome_fantasia(data.getNome_fantasia());
        company.setCep(data.getCep());
        return companyGateway.save(company);
    }

    public Company deletar(UUID id) {
        return companyGateway.delete(id);
    }

    public Company associarFornecedor(UUID id_fornecedor, UUID id) {
        Company company = buscar_um(id);
        Supplier supplier = supplierUseCase.buscar_um(id_fornecedor);
        if (supplier.isE_pf() && isPr(company.getCep()) && supplierUseCase.validaFornecedorMenor(supplier.getNascimento())) {
            throw new AppException("Não é permitido cadastrar um fornecedor menor de idade no Paraná");
        }
        vincularEmpresaFornecedor(company, supplier);
        return company;
    }

    public Company desassociarFornecedor(UUID id_fornecedor, UUID id) {
        Company company = buscar_um(id);
        Supplier supplier = supplierUseCase.buscar_um(id_fornecedor);
        desvincularEmpresaFornecedor(company, supplier);
        return company;
    }

    public boolean isPr(String cep) {
        Cep response = cepUseCase.buscarCep(cep);
        if (response == null) throw new AppException("Erro ao buscar o CEP");
        return response.getUf().equalsIgnoreCase("PR");
    }

    public void vincularEmpresaFornecedor(Company company, Supplier supplier) {
        company.getFornecedores().add(supplier);
        supplier.getEmpresas().add(company);
        companyGateway.save(company);
        supplierUseCase.save(supplier);
    }

    public void desvincularEmpresaFornecedor(Company company, Supplier supplier) {
        supplier.getEmpresas().removeIf(e -> e.getId().equals(company.getId()));
        company.getFornecedores().removeIf(f -> f.getId().equals(supplier.getId()));
        supplierUseCase.save(supplier);
        companyGateway.save(company);
    }
}
