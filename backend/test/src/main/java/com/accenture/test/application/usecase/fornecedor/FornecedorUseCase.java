package com.accenture.test.application.usecase.fornecedor;

import com.accenture.test.application.gateways.fornecedor.FornecedorGateway;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorEmpresa;
import com.accenture.test.domain.fornecedor.FornecedorPagResponse;
import com.accenture.test.application.exception.AppException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

public class FornecedorUseCase {

    private final FornecedorGateway fornecedorGateway;

    public FornecedorUseCase(FornecedorGateway fornecedorGateway) {
        this.fornecedorGateway = fornecedorGateway;
    }

    public FornecedorPagResponse<Fornecedor> buscar_tudo(int page, int size, String nome, String cnpj_cpf) {
        return fornecedorGateway.getAll(page, size, nome, cnpj_cpf);
    }

    public FornecedorEmpresa buscar_um(UUID id) {
        return fornecedorGateway.get(id);
    }

    public FornecedorEmpresa registrar(FornecedorEmpresa data) {
        data.setEmpresaEntities(List.of());
        if (data.isE_pf()) {
            if (data.getRg() == null || data.getNascimento() == null) throw new AppException(
                "É necessário informar um RG e data de " +
                "nascimento para cadastro de fornecedor pessoa física"
            );
        }
        return fornecedorGateway.save(data);
    }

    public FornecedorEmpresa atualizar(UUID id, FornecedorEmpresa data) {
        FornecedorEmpresa fornecedorEntity = buscar_um(id);
        fornecedorEntity.setCnpj_cpf(data.getCnpj_cpf());
        fornecedorEntity.setNome(data.getNome());
        fornecedorEntity.setEmail(data.getEmail());
        fornecedorEntity.setCep(data.getCep());
        fornecedorEntity.setE_pf(data.isE_pf());
        if (fornecedorEntity.isE_pf()) {
            if (data.getRg() == null || data.getNascimento() == null) throw new AppException(
                "É necessário informar um RG e data de " +
                "nascimento para cadastro de fornecedor pessoa física"
            );
            fornecedorEntity.setRg(data.getRg());
            fornecedorEntity.setNascimento(data.getNascimento());
        }
        return fornecedorGateway.save(fornecedorEntity);
    }

    public FornecedorEmpresa deletar(UUID id) {
        return fornecedorGateway.delete(id);
    }

    public boolean validaFornecedorMenor(LocalDate nascimento) {
        return Period.between(nascimento, LocalDate.now()).getYears() <= 18;
    }
}
