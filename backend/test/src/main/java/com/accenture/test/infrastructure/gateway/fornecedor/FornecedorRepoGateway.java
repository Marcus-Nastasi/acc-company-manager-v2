package com.accenture.test.infrastructure.gateway.fornecedor;

import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.gateways.fornecedor.FornecedorGateway;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorPag;
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
    public FornecedorPag<Fornecedor> getAll(int page, int size, String nome, String cnpj_cpf) {
        Page<FornecedorEntity> entityPage = fornecedorRepo.filtrarFornecedores(nome, cnpj_cpf, PageRequest.of(page, size));
        return new FornecedorPag<>(
            entityPage.getContent().stream().map(fornecedorEntityMapper::mapFromEntity).toList(),
            entityPage.getNumber(),
            entityPage.getTotalPages(),
            entityPage.getTotalPages() - entityPage.getNumber()
        );
    }

    @Override
    public Fornecedor get(UUID id) {
        return fornecedorEntityMapper.mapFromEntity(fornecedorRepo.findById(id).orElseThrow());
    }

    @Override
    public Fornecedor save(Fornecedor fornecedor) {
        return fornecedorEntityMapper.mapFromEntity(fornecedorRepo.save(fornecedorEntityMapper.mapFromFornecedorToEntity(fornecedor)));
    }

    @Override
    public Fornecedor delete(UUID id) {
        Fornecedor fornecedorEmpresa = get(id);
        if (fornecedorEmpresa == null) throw new AppException("Fornecedor not found");
        fornecedorRepo.deleteById(id);
        return fornecedorEmpresa;
    }
}
