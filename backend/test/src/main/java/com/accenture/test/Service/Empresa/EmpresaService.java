package com.accenture.test.Service.Empresa;

import com.accenture.test.Domain.Empresa.AtualizarEmpresaDTO;
import com.accenture.test.Domain.Empresa.Empresa;
import com.accenture.test.Domain.Empresa.RegistrarEmpresaDTO;
import com.accenture.test.Repository.Empresa.EmpresaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepo empresaRepo;

    public List<Empresa> buscar_tudo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Empresa> empresaPage = empresaRepo.findAll(pageable);
        return empresaPage.toList();
    }

    public Empresa registrar(RegistrarEmpresaDTO data) {
        Empresa empresa = new Empresa();
        empresa.setCnpj(data.cnpj());
        empresa.setNome_fantasia(data.nome_fantasia());
        empresa.setCep(data.cep());
        empresa.setFornecedores(List.of());
        empresaRepo.save(empresa);
        return empresa;
    }

    public Empresa atualizar(UUID id, AtualizarEmpresaDTO data) {
        Empresa empresa = empresaRepo.findById(id).orElseThrow();
        empresa.setCnpj(data.cnpj());
        empresa.setNome_fantasia(data.nome_fantasia());
        empresa.setCep(data.cep());
        empresaRepo.save(empresa);
        return empresa;
    }

    public Empresa deletar(UUID id) {
        Empresa empresa = empresaRepo.findById(id).orElseThrow();
        empresaRepo.deleteById(id);
        return empresa;
    }
}
