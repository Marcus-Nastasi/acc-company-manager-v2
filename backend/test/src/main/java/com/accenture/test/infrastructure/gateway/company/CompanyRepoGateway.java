package com.accenture.test.infrastructure.gateway.company;

import com.accenture.test.application.gateways.company.CompanyGateway;
import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.company.CompanyPag;
import com.accenture.test.infrastructure.entity.CompanyEntity;
import com.accenture.test.infrastructure.exception.InfraException;
import com.accenture.test.infrastructure.mapper.CompanyEntityMapper;
import com.accenture.test.infrastructure.persistence.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public class CompanyRepoGateway implements CompanyGateway {

    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private CompanyEntityMapper companyEntityMapper;

    @Override
    public CompanyPag getAll(int page, int size, String nome_fantasia, String cnpj, String cep) {
        Page<CompanyEntity> empresaEntityPage = companyRepo.filtrarEmpresa(nome_fantasia, cnpj, cep, PageRequest.of(page, size));
        return new CompanyPag(
            empresaEntityPage.getContent().stream().map(companyEntityMapper::mapFromEntity).toList(),
            empresaEntityPage.getNumber(),
            empresaEntityPage.getTotalPages(),
            empresaEntityPage.getTotalPages()
        );
    }

    @Override
    public Company get(UUID id) {
        return companyEntityMapper.mapFromEntity(companyRepo.findById(id).orElseThrow(() -> new InfraException("Not able to get empresa")));
    }

    @Override
    public Company save(Company data) {
        return companyEntityMapper.mapFromEntity(companyRepo.save(companyEntityMapper.mapToEntity(data)));
    }

    @Override
    public Company delete(UUID id) {
        Company companyFornecedor = get(id);
        if (companyFornecedor == null) throw new InfraException("Not able to delete empresa");
        companyRepo.deleteById(id);
        return companyFornecedor;
    }
}
