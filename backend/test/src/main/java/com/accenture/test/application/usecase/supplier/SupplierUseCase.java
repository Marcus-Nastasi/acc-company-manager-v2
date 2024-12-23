package com.accenture.test.application.usecase.supplier;

import com.accenture.test.application.gateways.supplier.SupplierGateway;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.domain.supplier.SupplierPag;
import com.accenture.test.application.exception.AppException;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class SupplierUseCase {

    private final SupplierGateway supplierGateway;

    public SupplierUseCase(SupplierGateway supplierGateway) {
        this.supplierGateway = supplierGateway;
    }

    public SupplierPag<Supplier> buscar_tudo(int page, int size, String nome, String cnpj_cpf) {
        return supplierGateway.getAll(page, size, nome, cnpj_cpf);
    }

    public Supplier buscar_um(UUID id) {
        return supplierGateway.get(id);
    }

    public Supplier save(Supplier supplier) {
        return supplierGateway.save(supplier);
    }

    public Supplier registrar(Supplier data) {
        if (data.isE_pf()) if (data.getRg() == null || data.getNascimento() == null) {
            throw new AppException("É necessário informar um RG e data de " + "nascimento para cadastro de fornecedor pessoa física");
        }
        return save(data);
    }

    public Supplier atualizar(UUID id, Supplier data) {
        Supplier supplier = buscar_um(id);
        supplier.setCnpj_cpf(data.getCnpj_cpf());
        supplier.setNome(data.getNome());
        supplier.setEmail(data.getEmail());
        supplier.setCep(data.getCep());
        supplier.setE_pf(data.isE_pf());
        if (supplier.isE_pf()) {
            if (data.getRg() == null || data.getNascimento() == null)
                throw new AppException("É necessário informar um RG e data de nascimento para cadastro de fornecedor pessoa física");
            supplier.setRg(data.getRg());
            supplier.setNascimento(data.getNascimento());
        }
        return save(supplier);
    }

    public Supplier deletar(UUID id) {
        return supplierGateway.delete(id);
    }

    public boolean validaFornecedorMenor(LocalDate nascimento) {
        return Period.between(nascimento, LocalDate.now()).getYears() <= 18;
    }
}
