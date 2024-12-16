package com.accenture.test.application.usecase.empresa;

import com.accenture.test.adapter.output.cep.CepResponseDTO;
import com.accenture.test.adapter.input.empresa.AtualizarEmpresaDTO;
import com.accenture.test.adapter.input.empresa.RegistrarEmpresaDTO;
import com.accenture.test.adapter.output.empresa.EmpPagResponseDTO;
import com.accenture.test.adapter.output.empresa.EmpresaFornResponseDTO;
import com.accenture.test.adapter.output.empresa.EmpresaResponseDTO;
import com.accenture.test.infrastructure.entity.EmpresaEntity;
import com.accenture.test.infrastructure.entity.FornecedorEntity;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.infrastructure.persistence.EmpresaRepo;
import com.accenture.test.infrastructure.persistence.FornecedorRepo;
import com.accenture.test.application.usecase.cep.CepService;
import com.accenture.test.application.usecase.fornecedor.FornecedorUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private FornecedorUseCase fornecedorUseCase;
    @Autowired
    private CepService cepService;

    public EmpPagResponseDTO<EmpresaFornResponseDTO> buscar_tudo(
            int page,
            int size,
            String nome_fantasia,
            String cnpj,
            String cep
    ) {
        Page<EmpresaEntity> empresaPage = empresaRepo
            .filtrarEmpresa(nome_fantasia, cnpj, cep, PageRequest.of(page, size));
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

    public EmpresaFornResponseDTO buscar_um(UUID id) {
        EmpresaEntity empresaEntity = empresaRepo
            .findById(id)
            .orElseThrow(() -> new AppException("erro ao encontrar empresa"));
        return this.mapToEmpresaFornResponseDTO(empresaEntity);
    }

    public EmpresaFornResponseDTO registrar(RegistrarEmpresaDTO data) {
        EmpresaEntity empresaEntity = new EmpresaEntity();
        empresaEntity.setCnpj(data.cnpj());
        empresaEntity.setNome_fantasia(data.nome_fantasia());
        empresaEntity.setCep(data.cep());
        empresaEntity.setFornecedores(List.of());
        empresaRepo.save(empresaEntity);
        return this.mapToEmpresaFornResponseDTO(empresaEntity);
    }

    public EmpresaFornResponseDTO atualizar(UUID id, AtualizarEmpresaDTO data) {
        EmpresaEntity empresaEntity = empresaRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Empresa não encontrada"));
        empresaEntity.setCnpj(data.cnpj());
        empresaEntity.setNome_fantasia(data.nome_fantasia());
        empresaEntity.setCep(data.cep());
        empresaRepo.save(empresaEntity);
        return mapToEmpresaFornResponseDTO(empresaEntity);
    }

    public EmpresaFornResponseDTO deletar(UUID id) {
        EmpresaEntity empresaEntity = empresaRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Empresa não encontrada"));
        empresaEntity.getFornecedores().forEach(f -> {
            f.getEmpresaEntityEntities().remove(empresaEntity);
            fornecedorRepo.save(f);
        });
        empresaRepo.deleteById(id);
        return mapToEmpresaFornResponseDTO(empresaEntity);
    }

    public EmpresaFornResponseDTO associarFornecedor(UUID id_fornecedor, UUID id) {
        EmpresaEntity empresaEntity = empresaRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Empresa não encontrada"));
        FornecedorEntity fornecedorEntity = fornecedorRepo
            .findById(id_fornecedor)
            .orElseThrow(() -> new AppException("Fornecedor não encontrado"));
        if (fornecedorEntity.isE_pf()
                && isPr(empresaEntity.getCep())
                && fornecedorUseCase.validaFornecedorMenor(fornecedorEntity.getNascimento())) {
            throw new AppException("Não é permitido cadastrar um fornecedor menor de idade no Paraná");
        }
        vincularEmpresaFornecedor(empresaEntity, fornecedorEntity);
        return mapToEmpresaFornResponseDTO(empresaEntity);
    }

    public EmpresaFornResponseDTO desassociarFornecedor(UUID id_fornecedor, UUID id) {
        EmpresaEntity empresaEntity = empresaRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Empresa não encontrada"));
        FornecedorEntity fornecedorEntity = fornecedorRepo
            .findById(id_fornecedor)
            .orElseThrow(() -> new AppException("Fornecedor não encontrado"));
        desvincularEmpresaFornecedor(empresaEntity, fornecedorEntity);
        return mapToEmpresaFornResponseDTO(empresaEntity);
    }

    public boolean isPr(String cep) {
        Mono<ResponseEntity<CepResponseDTO>> response = cepService.buscarCep(cep);
        if (response == null) throw new AppException("Erro ao buscar o CEP");
        CepResponseDTO c = response.map(ResponseEntity::getBody).block();
        if (c == null) throw new AppException("Erro ao resolver resposta do CEP");
        return c.uf().equalsIgnoreCase("PR");
    }

    public void vincularEmpresaFornecedor(EmpresaEntity empresaEntity, FornecedorEntity fornecedorEntity) {
        empresaEntity.getFornecedores().add(fornecedorEntity);
        fornecedorEntity.getEmpresaEntityEntities().add(empresaEntity);
        empresaRepo.save(empresaEntity);
        fornecedorRepo.save(fornecedorEntity);
    }

    public void desvincularEmpresaFornecedor(EmpresaEntity empresaEntity, FornecedorEntity fornecedorEntity) {
        empresaEntity.getFornecedores().remove(fornecedorEntity);
        fornecedorEntity.getEmpresaEntityEntities().remove(empresaEntity);
        empresaRepo.save(empresaEntity);
        fornecedorRepo.save(fornecedorEntity);
    }

    public EmpresaFornResponseDTO mapToEmpresaFornResponseDTO(EmpresaEntity empresaEntity) {
        return new EmpresaFornResponseDTO(
            empresaEntity.getId(),
            empresaEntity.getCnpj(),
            empresaEntity.getNome_fantasia(),
            empresaEntity.getCep(),
            empresaEntity.getFornecedores()
                .stream()
                .map((fornecedor) -> fornecedorUseCase.mapToFornecedorResponseDTO(fornecedor))
                .toList()
        );
    }

    public EmpresaResponseDTO mapToEmpresaResponseDTO(EmpresaEntity empresaEntity) {
        return new EmpresaResponseDTO(
            empresaEntity.getId(),
            empresaEntity.getCnpj(),
            empresaEntity.getNome_fantasia(),
            empresaEntity.getCep()
        );
    }
}
