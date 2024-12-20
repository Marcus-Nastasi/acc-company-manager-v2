package com.accenture.test.infrastructure.gateway.empresa;

import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.gateways.empresa.EmpresaGateway;
import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.domain.empresa.EmpresaPag;
import com.accenture.test.infrastructure.entity.EmpresaEntity;
import com.accenture.test.infrastructure.mapper.EmpresaEntityMapper;
import com.accenture.test.infrastructure.persistence.EmpresaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public class EmpresaRepoGateway implements EmpresaGateway {

    @Autowired
    private EmpresaRepo empresaRepo;
    @Autowired
    private EmpresaEntityMapper empresaEntityMapper;

    @Override
    public EmpresaPag buscar_tudo(int page, int size, String nome_fantasia, String cnpj, String cep) {
        Page<EmpresaEntity> empresaEntityPage = empresaRepo.filtrarEmpresa(nome_fantasia, cnpj, cep, PageRequest.of(page, size));
        return new EmpresaPag(
            empresaEntityPage.getContent().stream().map(empresaEntityMapper::mapFromEntity).toList(),
            empresaEntityPage.getNumber(),
            empresaEntityPage.getTotalPages(),
            empresaEntityPage.getTotalPages()
        );
    }

    @Override
    public Empresa buscar_um(UUID id) {
        return empresaEntityMapper.mapFromEntity(empresaRepo.findById(id).orElseThrow());
    }

    @Override
    public Empresa save(Empresa data) {
        return empresaEntityMapper.mapFromEntity(empresaRepo.save(empresaEntityMapper.mapToEntity(data)));
    }

    @Override
    public Empresa deletar(UUID id) {
        Empresa empresaFornecedor = buscar_um(id);
        if (empresaFornecedor == null) throw new AppException("Not able to delete empresa");
        empresaRepo.deleteById(id);
        return empresaFornecedor;
    }
}
