package com.accenture.test.Service.Empresa;

import com.accenture.test.Domain.Cep.DTO.CepResponseDTO;
import com.accenture.test.Domain.Empresa.DTO.*;
import com.accenture.test.Domain.Empresa.Empresa;
import com.accenture.test.Domain.Fornecedor.Fornecedor;
import com.accenture.test.Exception.AppException;
import com.accenture.test.Repository.Empresa.EmpresaRepo;
import com.accenture.test.Repository.Fornecedor.FornecedorRepo;
import com.accenture.test.Service.Cep.CepService;
import com.accenture.test.Service.Fornecedor.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    @Autowired
    private CepService cepService;

    public EmpPagResponseDTO<EmpresaFornResponseDTO> buscar_tudo(
            int page,
            int size,
            String nome_fantasia,
            String cnpj,
            String cep
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Empresa> empresaPage = empresaRepo
            .filtrarEmpresa(nome_fantasia, cnpj, cep, pageable);
        List<EmpresaFornResponseDTO> empresaFornList = empresaPage
                .map(this::mapToEmpresaFornResponseDTO)
                .toList();
        return new EmpPagResponseDTO<>(
            empresaFornList,
            empresaPage.getNumber(),
            empresaPage.getTotalPages(),
            empresaPage.getNumber() - 1
        );
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
        Empresa empresa = empresaRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Empresa não encontrada"));
        empresa.setCnpj(data.cnpj());
        empresa.setNome_fantasia(data.nome_fantasia());
        empresa.setCep(data.cep());
        empresaRepo.save(empresa);
        return mapToEmpresaFornResponseDTO(empresa);
    }

    public EmpresaFornResponseDTO deletar(UUID id) {
        Empresa empresa = empresaRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Empresa não encontrada"));
        empresa.getFornecedores().forEach((f) -> {
            f.getEmpresas().remove(empresa);
            fornecedorRepo.save(f);
        });
        empresaRepo.deleteById(id);
        return mapToEmpresaFornResponseDTO(empresa);
    }

    public EmpresaFornResponseDTO associarFornecedor(UUID id_fornecedor, UUID id) {
        Empresa empresa = empresaRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Empresa não encontrada"));
        Fornecedor fornecedor = fornecedorRepo
            .findById(id_fornecedor)
            .orElseThrow(() -> new AppException("Fornecedor não encontrado"));
        if (fornecedor.isE_pf()) {
            if (isPr(empresa.getCep())) {
                if (fornecedorService.validaFornecedorMenor(fornecedor.getNascimento())) {
                    throw new AppException(
                        "Não é permitido cadastrar um fornecedor menor de idade no Paraná"
                    );
                }
            }
        }
        vinculaEmpresaFornecedor(empresa, fornecedor);
        return mapToEmpresaFornResponseDTO(empresa);
    }

    public boolean isPr(String cep) {
        Mono<ResponseEntity<CepResponseDTO>> response = cepService.buscarCep(cep);
        if (response == null) throw new AppException("Erro ao buscar o CEP");
        CepResponseDTO c = response.map(ResponseEntity::getBody).block();
        if (c == null) throw new AppException("Erro ao resolver resposta de CEP");
        return c.uf().equalsIgnoreCase("PR");
    }

    private void vinculaEmpresaFornecedor(Empresa empresa, Fornecedor fornecedor) {
        empresa.getFornecedores().add(fornecedor);
        fornecedor.getEmpresas().add(empresa);
        empresaRepo.save(empresa);
        fornecedorRepo.save(fornecedor);
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
