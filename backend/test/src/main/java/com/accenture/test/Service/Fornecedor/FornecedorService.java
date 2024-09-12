package com.accenture.test.Service.Fornecedor;

import com.accenture.test.Domain.Fornecedor.Fornecedor;
import com.accenture.test.Domain.Fornecedor.RegistrarFornecedorDTO;
import com.accenture.test.Repository.Fornecedor.FornecedorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepo fornecedorRepo;

    public List<Fornecedor> buscar_tudo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Fornecedor> fornecedorPage = fornecedorRepo.findAll(pageable);
        return fornecedorPage.toList();
    }

    public Fornecedor registrar(RegistrarFornecedorDTO data) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj_cpf(data.cnpj_cpf());
        fornecedor.setNome(data.nome());
        fornecedor.setEmail(data.email());
        fornecedor.setCep(data.cep());
        fornecedor.setE_pf(data.e_pf());
        fornecedor.setEmpresas(List.of());
        if (fornecedor.isE_pf()) {
            fornecedor.setRg(data.rg());
            fornecedor.setNascimento(data.nascimento());
        }
        fornecedorRepo.save(fornecedor);
        return fornecedor;
    }

    public Fornecedor atualizar(UUID id, RegistrarFornecedorDTO data) {
        Fornecedor fornecedor = fornecedorRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Id não encontrado"));
        fornecedor.setCnpj_cpf(data.cnpj_cpf());
        fornecedor.setNome(data.nome());
        fornecedor.setEmail(data.email());
        fornecedor.setCep(data.cep());
        fornecedor.setE_pf(data.e_pf());
        if (fornecedor.isE_pf()) {
            fornecedor.setRg(data.rg());
            fornecedor.setNascimento(data.nascimento());
        }
        fornecedorRepo.save(fornecedor);
        return fornecedor;
    }

    public Fornecedor deletar(UUID id) {
        Fornecedor fornecedor = fornecedorRepo.findById(id).orElseThrow();
        fornecedorRepo.deleteById(id);
        return fornecedor;
    }
}
