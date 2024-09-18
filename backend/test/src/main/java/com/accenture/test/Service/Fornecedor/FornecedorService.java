package com.accenture.test.Service.Fornecedor;

import com.accenture.test.Domain.Fornecedor.DTO.FornecedorEmpResponseDTO;
import com.accenture.test.Domain.Fornecedor.DTO.FornecedorPagResponseDTO;
import com.accenture.test.Domain.Fornecedor.DTO.FornecedorResponseDTO;
import com.accenture.test.Domain.Fornecedor.Fornecedor;
import com.accenture.test.Domain.Fornecedor.DTO.RegistrarFornecedorDTO;
import com.accenture.test.Exception.AppException;
import com.accenture.test.Repository.Fornecedor.FornecedorRepo;
import com.accenture.test.Service.Empresa.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepo fornecedorRepo;
    @Autowired
    private EmpresaService empresaService;

    public FornecedorPagResponseDTO<FornecedorEmpResponseDTO> buscar_tudo(
            int page,
            int size,
            String nome,
            String cnpj_cpf
    ) {
        Page<Fornecedor> fornecedorPage = fornecedorRepo
            .filtrarFornecedores(nome, cnpj_cpf, PageRequest.of(page, size));
        List<FornecedorEmpResponseDTO> fornEmpresasList = fornecedorPage
                .map(this::mapToFornecedorEmpResponseDTO)
                .toList();
        return new FornecedorPagResponseDTO<>(
            fornEmpresasList,
            fornecedorPage.getNumber(),
            fornecedorPage.getTotalPages(),
            fornecedorPage.getNumber() - 1
        );
    }

    public FornecedorEmpResponseDTO buscar_um(UUID id) {
        Fornecedor fornecedor = fornecedorRepo
            .findById(id)
            .orElseThrow(() -> new AppException("erro ao encontrar o fornecedor"));
        return this.mapToFornecedorEmpResponseDTO(fornecedor);
    }

    public FornecedorEmpResponseDTO registrar(RegistrarFornecedorDTO data) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj_cpf(data.cnpj_cpf());
        fornecedor.setNome(data.nome());
        fornecedor.setEmail(data.email());
        fornecedor.setCep(data.cep());
        fornecedor.setE_pf(data.e_pf());
        fornecedor.setEmpresas(List.of());
        if (fornecedor.isE_pf()) {
            if (data.rg() == null || data.nascimento() == null)
                throw new AppException(
                    "É necessário informar um RG e data de " +
                    "nascimento para cadastro de fornecedor pessoa física"
                );
            fornecedor.setRg(data.rg());
            fornecedor.setNascimento(data.nascimento());
        }
        fornecedorRepo.save(fornecedor);
        return mapToFornecedorEmpResponseDTO(fornecedor);
    }

    public FornecedorEmpResponseDTO atualizar(UUID id, RegistrarFornecedorDTO data) {
        Fornecedor fornecedor = fornecedorRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Erro ao encontrar fornecedor"));
        fornecedor.setCnpj_cpf(data.cnpj_cpf());
        fornecedor.setNome(data.nome());
        fornecedor.setEmail(data.email());
        fornecedor.setCep(data.cep());
        fornecedor.setE_pf(data.e_pf());
        if (fornecedor.isE_pf()) {
            if (data.rg() == null || data.nascimento() == null)
                throw new AppException(
                    "É necessário informar um RG e data de " +
                    "nascimento para cadastro de fornecedor pessoa física"
                );
            fornecedor.setRg(data.rg());
            fornecedor.setNascimento(data.nascimento());
        }
        fornecedorRepo.save(fornecedor);
        return mapToFornecedorEmpResponseDTO(fornecedor);
    }

    public FornecedorEmpResponseDTO deletar(UUID id) {
        Fornecedor fornecedor = fornecedorRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Erro ao encontrar fornecedor"));
        fornecedorRepo.deleteById(id);
        return mapToFornecedorEmpResponseDTO(fornecedor);
    }

    public boolean validaFornecedorMenor(LocalDate nascimento) {
        return Period.between(nascimento, LocalDate.now()).getYears() <= 18;
    }

    public FornecedorResponseDTO mapToFornecedorResponseDTO(Fornecedor fornecedor) {
        return new FornecedorResponseDTO(
            fornecedor.getId(),
            fornecedor.getCnpj_cpf(),
            fornecedor.getRg(),
            fornecedor.getNascimento(),
            fornecedor.getNome(),
            fornecedor.getEmail(),
            fornecedor.getCep(),
            fornecedor.isE_pf()
        );
    }

    public FornecedorEmpResponseDTO mapToFornecedorEmpResponseDTO(Fornecedor fornecedor) {
        return new FornecedorEmpResponseDTO(
            fornecedor.getId(),
            fornecedor.getCnpj_cpf(),
            fornecedor.getRg(),
            fornecedor.getNascimento(),
            fornecedor.getNome(),
            fornecedor.getEmail(),
            fornecedor.getCep(),
            fornecedor.isE_pf(),
            fornecedor.getEmpresas()
                .stream()
                .map((e) -> empresaService.mapToEmpresaResponseDTO(e)).toList()
        );
    }
}
