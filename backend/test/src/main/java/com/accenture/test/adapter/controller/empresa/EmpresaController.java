package com.accenture.test.adapter.controller.empresa;

import com.accenture.test.adapter.mapper.empresa.EmpresaDtoMapper;
import com.accenture.test.adapter.output.empresa.EmpPagResponseDto;
import com.accenture.test.adapter.input.empresa.EmpresaRequestDto;
import com.accenture.test.adapter.output.empresa.EmpresaResponseDto;
import com.accenture.test.application.usecase.empresa.EmpresaUseCase;
import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.domain.empresa.EmpresaPag;
import com.accenture.test.infrastructure.mapper.EmpresaEntityMapper;
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
public class EmpresaController {

    @Autowired
    private EmpresaUseCase empresaUseCase;
    @Autowired
    private EmpresaEntityMapper empresaEntityMapper;
    @Autowired
    private EmpresaDtoMapper empresaDtoMapper;

    @GetMapping()
    @Cacheable("empresa")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar todas as empresas", description = "Nessa rota você pode consultar dados de todas as empresas, com paginação e filtro por nome, cnpj e cep")
    @ApiResponse(responseCode = "200", description = "Retornando dados das empresas")
    public EmpPagResponseDto buscar_tudo(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "nome", defaultValue = "", required = false) String nome_fantasia,
            @RequestParam(name = "cnpj", defaultValue = "", required = false) String cnpj,
            @RequestParam(name = "cep", defaultValue = "", required = false) String cep
    ) {
        if (size < 1) size = 10;
        EmpresaPag empresaPag = empresaUseCase.buscar_tudo(page, size, nome_fantasia, cnpj, cep);
        return new EmpPagResponseDto(
            empresaPag.getDados().stream().map(empresaDtoMapper::mapToClean).toList(),
            empresaPag.getPaginaAtual(),
            empresaPag.getTotalPaginas(),
            empresaPag.getPaginasRestantes() - 1
        );
    }

    @GetMapping(value = "/{id}")
    @Cacheable("empresa")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar uma única empresa", description = "Nessa rota você pode consultar dados detalhados de uma única empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa única")
    public EmpresaResponseDto buscar_um(@PathVariable("id") UUID id) {
        return empresaDtoMapper.mapToResponse(empresaUseCase.buscar_um(id));
    }

    @PostMapping(value = "/registrar")
    @CacheEvict(value = "empresa", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar nova empresa", description = "Nessa rota você pode cadastrar uma nova empresa")
    @ApiResponse(responseCode = "201", description = "Retornando dados da empresa criada")
    public ResponseEntity<EmpresaResponseDto> registrar(@RequestBody @Valid EmpresaRequestDto data) {
        Empresa empresa = empresaUseCase.registrar(empresaDtoMapper.mapFromRequest(data));
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaDtoMapper.mapToResponse(empresa));
    }

    @PatchMapping(value = "/atualizar/{id}")
    @CacheEvict(value = "empresa", allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar dados de uma empresa", description = "Nessa rota você pode atualizar os dados de uma empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa atualizada")
    public ResponseEntity<EmpresaResponseDto> atualizar(@PathVariable("id") UUID id, @RequestBody @Valid EmpresaRequestDto data) {
        Empresa empresa = empresaUseCase.atualizar(id, empresaDtoMapper.mapFromRequest(data));
        return ResponseEntity.ok(empresaDtoMapper.mapToResponse(empresa));
    }

    @DeleteMapping(value = "/deletar/{id}")
    @CacheEvict(value = {"empresa", "fornecedor"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deletar uma empresa", description = "Nessa rota você pode deletar os dados de uma empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa deletada")
    public ResponseEntity<EmpresaResponseDto> deletar(@PathVariable("id") UUID id) {
        Empresa empresa = empresaUseCase.deletar(id);
        return ResponseEntity.ok(empresaDtoMapper.mapToResponse(empresa));
    }

    @PatchMapping(value = "/associar/{id_empresa}/{id_fornecedor}")
    @CacheEvict(value = {"empresa", "fornecedor"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Associar empresa e fornecedor", description = "Nessa rota você pode associar um fornecedor com uma empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa associada à um novo fornecedor")
    public ResponseEntity<EmpresaResponseDto> associar(@PathVariable("id_empresa") UUID id_empresa, @PathVariable("id_fornecedor") UUID id_fornecedor) {
        Empresa empresa = empresaUseCase.associarFornecedor(id_fornecedor, id_empresa);
        return ResponseEntity.ok(empresaDtoMapper.mapToResponse(empresa));
    }

    @PatchMapping(value = "/desassociar/{id_empresa}/{id_fornecedor}")
    @CacheEvict(value = {"empresa", "fornecedor"}, allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Desassociar empresa e fornecedor", description = "Nessa rota você pode desassociar um fornecedor e uma empresa")
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa desvinculada de um fornecedor")
    public ResponseEntity<EmpresaResponseDto> desassociar(@PathVariable("id_empresa") UUID id_empresa, @PathVariable("id_fornecedor") UUID id_fornecedor) {
        Empresa empresa = empresaUseCase.desassociarFornecedor(id_fornecedor, id_empresa);
        return ResponseEntity.ok(empresaDtoMapper.mapToResponse(empresa));
    }
}
