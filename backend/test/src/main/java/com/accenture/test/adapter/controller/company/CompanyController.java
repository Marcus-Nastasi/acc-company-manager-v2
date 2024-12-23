package com.accenture.test.adapter.controller.company;

import com.accenture.test.adapter.mapper.company.CompanyDtoMapper;
import com.accenture.test.adapter.output.company.CompanyPagResponseDto;
import com.accenture.test.adapter.input.company.CompanyRequestDto;
import com.accenture.test.adapter.output.company.CompanyResponseDto;
import com.accenture.test.application.usecase.company.CompanyUseCase;
import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.company.CompanyPag;
import com.accenture.test.infrastructure.mapper.CompanyEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/empresa")
public class CompanyController {

    @Autowired
    private CompanyUseCase companyUseCase;
    @Autowired
    private CompanyEntityMapper companyEntityMapper;
    @Autowired
    private CompanyDtoMapper companyDtoMapper;

    @GetMapping()
    @Cacheable("empresa")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar todas as empresas", description = "Nessa rota você pode consultar dados de todas as empresas, com paginação e filtro por nome, cnpj e cep")
    @ApiResponse(responseCode = "200", description = "Retornando dados das empresas")
    public CompanyPagResponseDto buscar_tudo(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "nome", defaultValue = "", required = false) String nome_fantasia,
            @RequestParam(name = "cnpj", defaultValue = "", required = false) String cnpj,
            @RequestParam(name = "cep", defaultValue = "", required = false) String cep
    ) {
        if (size < 1) size = 10;
        CompanyPag companyPag = companyUseCase.getAll(page, size, nome_fantasia, cnpj, cep);
        return new CompanyPagResponseDto(
            companyPag.getData().stream().map(companyDtoMapper::mapToClean).toList(),
            companyPag.getPage(),
            companyPag.getTotal(),
            companyPag.getRemainingPages() - 1
        );
    }

    @GetMapping(value = "/{id}")
    @Cacheable("empresa")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar uma única empresa", description = "Nessa rota você pode consultar dados detalhados de uma única empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa única")
    public CompanyResponseDto buscar_um(@PathVariable("id") UUID id) {
        return companyDtoMapper.mapToResponse(companyUseCase.get(id));
    }

    @PostMapping(value = "/registrar")
    @CacheEvict(value = "empresa", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar nova empresa", description = "Nessa rota você pode cadastrar uma nova empresa")
    @ApiResponse(responseCode = "201", description = "Retornando dados da empresa criada")
    public ResponseEntity<CompanyResponseDto> registrar(@RequestBody @Valid CompanyRequestDto data) {
        Company company = companyUseCase.register(companyDtoMapper.mapFromRequest(data));
        return ResponseEntity.status(HttpStatus.CREATED).body(companyDtoMapper.mapToResponse(company));
    }

    @PatchMapping(value = "/atualizar/{id}")
    @CacheEvict(value = "empresa", allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar dados de uma empresa", description = "Nessa rota você pode atualizar os dados de uma empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa atualizada")
    public ResponseEntity<CompanyResponseDto> atualizar(@PathVariable("id") UUID id, @RequestBody @Valid CompanyRequestDto data) {
        Company company = companyUseCase.update(id, companyDtoMapper.mapFromRequest(data));
        return ResponseEntity.ok(companyDtoMapper.mapToResponse(company));
    }

    @DeleteMapping(value = "/deletar/{id}")
    @CacheEvict(value = {"empresa", "fornecedor"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deletar uma empresa", description = "Nessa rota você pode deletar os dados de uma empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa deletada")
    public ResponseEntity<CompanyResponseDto> deletar(@PathVariable("id") UUID id) {
        Company company = companyUseCase.delete(id);
        return ResponseEntity.ok(companyDtoMapper.mapToResponse(company));
    }

    @PatchMapping(value = "/associar/{id_empresa}/{id_fornecedor}")
    @CacheEvict(value = {"empresa", "fornecedor"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Associar empresa e fornecedor", description = "Nessa rota você pode associar um fornecedor com uma empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa associada à um novo fornecedor")
    public ResponseEntity<CompanyResponseDto> associar(@PathVariable("id_empresa") UUID id_empresa, @PathVariable("id_fornecedor") UUID id_fornecedor) {
        Company company = companyUseCase.associateSupplier(id_fornecedor, id_empresa);
        return ResponseEntity.ok(companyDtoMapper.mapToResponse(company));
    }

    @PatchMapping(value = "/desassociar/{id_empresa}/{id_fornecedor}")
    @CacheEvict(value = {"empresa", "fornecedor"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Desassociar empresa e fornecedor", description = "Nessa rota você pode desassociar um fornecedor e uma empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa desvinculada de um fornecedor")
    public ResponseEntity<CompanyResponseDto> desassociar(@PathVariable("id_empresa") UUID id_empresa, @PathVariable("id_fornecedor") UUID id_fornecedor) {
        Company company = companyUseCase.disassociateSupplier(id_fornecedor, id_empresa);
        return ResponseEntity.ok(companyDtoMapper.mapToResponse(company));
    }
}
