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
    @Cacheable("company")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all companies", description = "In this route you can consult data from all companies, with pagination and filter by name, cnpj and zip code")
    @ApiResponse(responseCode = "200", description = "Returning data from companies")
    public CompanyPagResponseDto getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "nome", defaultValue = "", required = false) String name,
            @RequestParam(name = "cnpj", defaultValue = "", required = false) String cnpj,
            @RequestParam(name = "cep", defaultValue = "", required = false) String cep
    ) {
        if (size < 1) size = 10;
        CompanyPag companyPag = companyUseCase.getAll(page, size, name, cnpj, cep);
        return new CompanyPagResponseDto(
            companyPag.getData().stream().map(companyDtoMapper::mapToClean).toList(),
            companyPag.getPage(),
            companyPag.getTotal(),
            companyPag.getRemainingPages() - 1
        );
    }

    @GetMapping(value = "/{id}")
    @Cacheable("company")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get single company", description = "In this route you can consult detailed data for a single company")
    @ApiResponse(responseCode = "200", description = "Returning single company data")
    public CompanyResponseDto getSingle(@PathVariable("id") UUID id) {
        return companyDtoMapper.mapToResponse(companyUseCase.get(id));
    }

    @PostMapping(value = "/registrar")
    @CacheEvict(value = "company", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new company", description = "In this route you can register a new company")
    @ApiResponse(responseCode = "201", description = "Returning data from the created company")
    public ResponseEntity<CompanyResponseDto> register(@RequestBody @Valid CompanyRequestDto data) {
        Company company = companyUseCase.register(companyDtoMapper.mapFromRequest(data));
        return ResponseEntity.status(HttpStatus.CREATED).body(companyDtoMapper.mapToResponse(company));
    }

    @PatchMapping(value = "/atualizar/{id}")
    @CacheEvict(value = "company", allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update company data", description = "In this route you can update a company's data")
    @ApiResponse(responseCode = "200", description = "Returning updated company data")
    public ResponseEntity<CompanyResponseDto> update(@PathVariable("id") UUID id, @RequestBody @Valid CompanyRequestDto data) {
        Company company = companyUseCase.update(id, companyDtoMapper.mapFromRequest(data));
        return ResponseEntity.ok(companyDtoMapper.mapToResponse(company));
    }

    @DeleteMapping(value = "/deletar/{id}")
    @CacheEvict(value = {"company", "supplier"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a company", description = "In this route you can delete a company's data")
    @ApiResponse(responseCode = "200", description = "Returning deleted company data")
    public ResponseEntity<CompanyResponseDto> delete(@PathVariable("id") UUID id) {
        Company company = companyUseCase.delete(id);
        return ResponseEntity.ok(companyDtoMapper.mapToResponse(company));
    }

    @PatchMapping(value = "/associar/{id_empresa}/{id_fornecedor}")
    @CacheEvict(value = {"company", "supplier"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Associate company and supplier", description = "In this route you can associate a supplier with a company")
    @ApiResponse(responseCode = "200", description = "Returning data from a company associated with a new supplier")
    public ResponseEntity<CompanyResponseDto> associate(@PathVariable("id_empresa") UUID company_id, @PathVariable("id_fornecedor") UUID supplier_id) {
        Company company = companyUseCase.associateSupplier(supplier_id, company_id);
        return ResponseEntity.ok(companyDtoMapper.mapToResponse(company));
    }

    @PatchMapping(value = "/desassociar/{id_empresa}/{id_fornecedor}")
    @CacheEvict(value = {"company", "supplier"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Disassociate company and supplier", description = "In this route you can disassociate a supplier and a company")
    @ApiResponse(responseCode = "200", description = "Returning data from a company disconnected from a supplier")
    public ResponseEntity<CompanyResponseDto> disassociate(@PathVariable("id_empresa") UUID company_id, @PathVariable("id_fornecedor") UUID supplier_id) {
        Company company = companyUseCase.disassociateSupplier(supplier_id, company_id);
        return ResponseEntity.ok(companyDtoMapper.mapToResponse(company));
    }
}
