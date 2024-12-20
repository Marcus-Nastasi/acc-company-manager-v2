package com.accenture.test.application.usecase.fornecedor;

import com.accenture.test.application.gateways.fornecedor.FornecedorGateway;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorPag;
import com.accenture.test.application.exception.AppException;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class FornecedorUseCase {

    private final FornecedorGateway fornecedorGateway;

    public FornecedorUseCase(FornecedorGateway fornecedorGateway) {
        this.fornecedorGateway = fornecedorGateway;
    }

    public FornecedorPag<Fornecedor> buscar_tudo(int page, int size, String nome, String cnpj_cpf) {
        return fornecedorGateway.getAll(page, size, nome, cnpj_cpf);
    }

    public Fornecedor buscar_um(UUID id) {
        return fornecedorGateway.get(id);
    }

    public Fornecedor save(Fornecedor fornecedor) {
        return fornecedorGateway.save(fornecedor);
    }

    public Fornecedor registrar(Fornecedor data) {
        if (data.isE_pf()) {
            if (data.getRg() == null || data.getNascimento() == null) throw new AppException(
                "É necessário informar um RG e data de " +
                "nascimento para cadastro de fornecedor pessoa física"
            );
        }
        return save(data);
    }

    public Fornecedor atualizar(UUID id, Fornecedor data) {
        Fornecedor fornecedor = buscar_um(id);
        fornecedor.setCnpj_cpf(data.getCnpj_cpf());
        fornecedor.setNome(data.getNome());
        fornecedor.setEmail(data.getEmail());
        fornecedor.setCep(data.getCep());
        fornecedor.setE_pf(data.isE_pf());
        if (fornecedor.isE_pf()) {
            if (data.getRg() == null || data.getNascimento() == null) throw new AppException(
                "É necessário informar um RG e data de " +
                "nascimento para cadastro de fornecedor pessoa física"
            );
            fornecedor.setRg(data.getRg());
            fornecedor.setNascimento(data.getNascimento());
        }
        return save(fornecedor);
    }

    public Fornecedor deletar(UUID id) {
        return fornecedorGateway.delete(id);
    }

    public boolean validaFornecedorMenor(LocalDate nascimento) {
        return Period.between(nascimento, LocalDate.now()).getYears() <= 18;
    }
}
