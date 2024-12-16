package com.accenture.test.infrastructure.gateway.fornecedor;

import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.gateways.fornecedor.FornecedorGateway;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorEmpresa;
import com.accenture.test.domain.fornecedor.FornecedorPagResponse;
import com.accenture.test.infrastructure.entity.FornecedorEntity;
import com.accenture.test.infrastructure.mapper.FornecedorEntityMapper;
import com.accenture.test.infrastructure.persistence.FornecedorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public class FornecedorRepoGateway implements FornecedorGateway {

    @Autowired
    private FornecedorRepo fornecedorRepo;
    @Autowired
    private FornecedorEntityMapper fornecedorEntityMapper;

    @Override
    public FornecedorPagResponse<Fornecedor> getAll(int page, int size, String nome, String cnpj_cpf) {
        Page<FornecedorEntity> entityPage = fornecedorRepo.filtrarFornecedores(nome, cnpj_cpf, PageRequest.of(page, size));
        return new FornecedorPagResponse<>(
            entityPage.getContent().stream().map(fornecedorEntityMapper::mapFromFornecedorEntity).toList(),
            entityPage.getNumber(),
            entityPage.getTotalPages(),
            entityPage.getTotalPages() - entityPage.getNumber()
        );
    }

    @Override
    public FornecedorEmpresa get(UUID id) {
        return fornecedorEntityMapper.mapFromEntityToFornecedorEmpresa(fornecedorRepo.findById(id).orElseThrow());
    }

    @Override
    public FornecedorEmpresa save(FornecedorEmpresa fornecedorEmpresa) {
        return fornecedorEntityMapper.mapFromEntityToFornecedorEmpresa(fornecedorRepo.save(fornecedorEntityMapper.mapToFornecedorEntity(fornecedorEmpresa)));
    }

    @Override
    public FornecedorEmpresa delete(UUID id) {
        FornecedorEmpresa fornecedorEmpresa = get(id);
        if (fornecedorEmpresa == null) throw new AppException("");
        fornecedorRepo.deleteById(id);
        return fornecedorEmpresa;
    }
}
