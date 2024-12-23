package com.accenture.test.infrastructure.gateway.supplier;

import com.accenture.test.application.gateways.supplier.SupplierGateway;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.domain.supplier.SupplierPag;
import com.accenture.test.infrastructure.entity.SupplierEntity;
import com.accenture.test.infrastructure.exception.InfraException;
import com.accenture.test.infrastructure.mapper.SupplierEntityMapper;
import com.accenture.test.infrastructure.persistence.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public class SupplierRepoGateway implements SupplierGateway {

    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private SupplierEntityMapper supplierEntityMapper;

    @Override
    public SupplierPag<Supplier> getAll(int page, int size, String nome, String cnpj_cpf) {
        Page<SupplierEntity> entityPage = supplierRepo.filter(nome, cnpj_cpf, PageRequest.of(page, size));
        return new SupplierPag<>(
            entityPage.getContent().stream().map(supplierEntityMapper::mapFromEntity).toList(),
            entityPage.getNumber(),
            entityPage.getTotalPages(),
            entityPage.getTotalPages() - entityPage.getNumber()
        );
    }

    @Override
    public Supplier get(UUID id) {
        return supplierEntityMapper.mapFromEntity(supplierRepo.findById(id).orElseThrow(() -> new InfraException("Not able to get supplier")));
    }

    @Override
    public Supplier save(Supplier supplier) {
        return supplierEntityMapper.mapFromEntity(supplierRepo.save(supplierEntityMapper.mapFromSupplierToEntity(supplier)));
    }

    @Override
    public Supplier delete(UUID id) {
        Supplier supplierCompanies = get(id);
        if (supplierCompanies == null) throw new InfraException("Supplier not found");
        supplierRepo.deleteById(id);
        return supplierCompanies;
    }
}
