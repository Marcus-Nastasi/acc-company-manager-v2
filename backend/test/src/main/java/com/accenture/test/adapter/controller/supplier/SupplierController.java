package com.accenture.test.adapter.controller.supplier;

import com.accenture.test.adapter.mapper.supplier.SupplierDtoMapper;
import com.accenture.test.adapter.input.supplier.SupplierRequestDto;
import com.accenture.test.adapter.output.supplier.SupplierPagResponseDto;
import com.accenture.test.adapter.output.supplier.SupplierResponseDto;
import com.accenture.test.application.usecase.supplier.SupplierUseCase;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.domain.supplier.SupplierPag;
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
@RequestMapping(value = "/api/fornecedor")
public class SupplierController {

    @Autowired
    private SupplierUseCase supplierUseCase;
    @Autowired
    private SupplierDtoMapper supplierDtoMapper;

    @GetMapping()
    @Cacheable("fornecedor")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar todos os fornecedores", description = "Nessa rota você pode consultar dados de todos os fornecedores, com paginação e filtro por nome e cnpj/cpf")
    @ApiResponse(responseCode = "200", description = "Retornando dados dos fornecedores")
    public SupplierPagResponseDto buscar_tudo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cnpj_cpf", required = false) String cnpj_cpf
    ) {
        if (size < 1) size = 10;
        SupplierPag<Supplier> supplierPag = supplierUseCase.buscar_tudo(page, size, nome, cnpj_cpf);
        return new SupplierPagResponseDto(
            supplierPag.getDados().stream().map(supplierDtoMapper::mapToClean).toList(),
            supplierPag.getPaginaAtual(),
            supplierPag.getTotalPaginas(),
            supplierPag.getPaginasRestantes() - 1
        );
    }

    @GetMapping(value = "/{id}")
    @Cacheable("fornecedor")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar um único fornecedor", description = "Nessa rota você pode consultar dados detalhados de um único fornecedor")
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor único")
    public SupplierResponseDto buscar_um(@PathVariable("id") UUID id) {
        return supplierDtoMapper.mapToResponse(supplierUseCase.buscar_um(id));
    }

    @PostMapping(value = "/registrar")
    @CacheEvict(value = "fornecedor", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo fornecedor", description = "Nessa rota você pode cadastrar um novo fornecedor")
    @ApiResponse(responseCode = "201", description = "Retornando dados do fornecedor criado")
    public ResponseEntity<SupplierResponseDto> registrar(@RequestBody @Valid SupplierRequestDto data) {
        Supplier supplier = supplierUseCase.registrar(supplierDtoMapper.mapFromRequest(data));
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierDtoMapper.mapToResponse(supplier));
    }

    @PatchMapping(value = "/atualizar/{id}")
    @CacheEvict(value = "fornecedor", allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar dados de um fornecedor", description = "Nessa rota você pode atualizar os dados de um fornecedor")
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor atualizado")
    public ResponseEntity<SupplierResponseDto> atualizar(@PathVariable("id") UUID id, @RequestBody @Valid SupplierRequestDto data) {
        Supplier supplier = supplierUseCase.atualizar(id, supplierDtoMapper.mapFromRequest(data));
        return ResponseEntity.status(HttpStatus.OK).body(supplierDtoMapper.mapToResponse(supplier));
    }

    @DeleteMapping(value = "/deletar/{id}")
    @CacheEvict(value = "fornecedor", allEntries = true)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deletar um fornecedor", description = "Nessa rota você pode deletar os dados de um fornecedor")
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor deletado")
    public ResponseEntity<SupplierResponseDto> deletar(@PathVariable("id") UUID id)  {
        Supplier supplier = supplierUseCase.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body(supplierDtoMapper.mapToResponse(supplier));
    }
}
