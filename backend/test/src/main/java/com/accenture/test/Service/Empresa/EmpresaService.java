package com.accenture.test.Service.Empresa;

import com.accenture.test.Domain.Empresa.DTO.AtualizarEmpresaDTO;
import com.accenture.test.Domain.Empresa.DTO.EmpresaFornResponseDTO;
import com.accenture.test.Domain.Empresa.DTO.EmpresaResponseDTO;
import com.accenture.test.Domain.Empresa.Empresa;
import com.accenture.test.Domain.Empresa.DTO.RegistrarEmpresaDTO;
import com.accenture.test.Domain.Fornecedor.Fornecedor;
import com.accenture.test.Repository.Empresa.EmpresaRepo;
import com.accenture.test.Repository.Fornecedor.FornecedorRepo;
import com.accenture.test.Service.Fornecedor.FornecedorService;
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
    @Autowired
    private FornecedorRepo fornecedorRepo;
    @Autowired
    private FornecedorService fornecedorService;

    public List<EmpresaFornResponseDTO> buscar_tudo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Empresa> empresaPage = empresaRepo.findAll(pageable);
        return empresaPage.map(this::mapToEmpresaFornResponseDTO).toList();
    }

    public EmpresaFornResponseDTO registrar(RegistrarEmpresaDTO data) {
        Empresa empresa = new Empresa();
        empresa.setCnpj(data.cnpj());
        empresa.setNome_fantasia(data.nome_fantasia());
        empresa.setCep(data.cep());
        empresa.setFornecedores(List.of());
        empresaRepo.save(empresa);
        return this.mapToEmpresaFornResponseDTO(empresa);
    }

    public EmpresaFornResponseDTO atualizar(UUID id, AtualizarEmpresaDTO data) {
        Empresa empresa = empresaRepo.findById(id).orElseThrow();
        empresa.setCnpj(data.cnpj());
        empresa.setNome_fantasia(data.nome_fantasia());
        empresa.setCep(data.cep());
        empresaRepo.save(empresa);
        return this.mapToEmpresaFornResponseDTO(empresa);
    }

    public EmpresaFornResponseDTO deletar(UUID id) {
        Empresa empresa = empresaRepo.findById(id).orElseThrow();
        empresa.getFornecedores().forEach((f) -> {
            f.getEmpresas().remove(empresa);
            fornecedorRepo.save(f);
        });
        empresaRepo.deleteById(id);
        return this.mapToEmpresaFornResponseDTO(empresa);
    }

    public EmpresaFornResponseDTO associarFornecedor(UUID id_fornecedor, UUID id) {
        Empresa empresa = empresaRepo.findById(id).orElseThrow();
        Fornecedor fornecedor = fornecedorRepo.findById(id_fornecedor).orElseThrow();
        empresa.getFornecedores().add(fornecedor);
        fornecedor.getEmpresas().add(empresa);
        empresaRepo.save(empresa);
        fornecedorRepo.save(fornecedor);
        return this.mapToEmpresaFornResponseDTO(empresa);
    }

    public EmpresaFornResponseDTO mapToEmpresaFornResponseDTO(Empresa empresa) {
        return new EmpresaFornResponseDTO(
            empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome_fantasia(),
            empresa.getCep(),
            empresa.getFornecedores()
                .stream()
                .map((fornecedor) -> fornecedorService.mapToFornecedorResponseDTO(fornecedor))
                .toList()
        );
    }

    public EmpresaResponseDTO mapToEmpresaResponseDTO(Empresa empresa) {
        return new EmpresaResponseDTO(
            empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome_fantasia(),
            empresa.getCep()
        );
    }
}
