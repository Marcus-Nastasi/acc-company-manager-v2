package com.accenture.test.adapter.controller.Fornecedor;

import com.accenture.test.adapter.mapper.fornecedor.FornecedorDtoMapper;
import com.accenture.test.adapter.output.fornecedor.FornecedorEmpResponseDTO;
import com.accenture.test.adapter.input.fornecedor.RegistrarFornecedorDTO;
import com.accenture.test.application.usecase.fornecedor.FornecedorUseCase;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorEmpresa;
import com.accenture.test.domain.fornecedor.FornecedorPagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorUseCase fornecedorUseCase;
    @Autowired
    private FornecedorDtoMapper fornecedorDtoMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar todos os fornecedores",
        description = "Nessa rota você pode consultar dados de todos os fornecedores, com paginação e filtro por nome e cnpj/cpf"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados dos fornecedores")
    public FornecedorPagResponse<Fornecedor> buscar_tudo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cnpj_cpf", required = false) String cnpj_cpf
    ) {
        if (size < 1) size = 10;
        return fornecedorUseCase.buscar_tudo(page, size, nome, cnpj_cpf);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar um único fornecedor",
        description = "Nessa rota você pode consultar dados detalhados de um único fornecedor"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor único")
    public FornecedorEmpresa buscar_um(@PathVariable("id") UUID id) {
        return fornecedorUseCase.buscar_um(id);
    }

    @PostMapping(value = "/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo fornecedor", description = "Nessa rota você pode cadastrar um novo fornecedor")
    @ApiResponse(responseCode = "201", description = "Retornando dados do fornecedor criado")
    public ResponseEntity<FornecedorEmpresa> registrar(@RequestBody @Valid RegistrarFornecedorDTO data) {
        FornecedorEmpresa fornecedor = fornecedorUseCase.registrar(fornecedorDtoMapper.mapFromRequestToFornecedorEmpresa(data));
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedor);
    }

    @PatchMapping(value = "/atualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Atualizar dados de um fornecedor",
        description = "Nessa rota você pode atualizar os dados de um fornecedor"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor atualizado")
    public ResponseEntity<FornecedorEmpResponseDTO> atualizar(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RegistrarFornecedorDTO data
    ) {
        FornecedorEmpresa fornecedor = fornecedorUseCase.atualizar(id, fornecedorDtoMapper.mapFromRequestToFornecedorEmpresa(data));
        return ResponseEntity.status(HttpStatus.OK).body(fornecedorDtoMapper.mapToFornecedorEmpResponseDTO(fornecedor));
    }

    @DeleteMapping(value = "/deletar/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deletar um fornecedor", description = "Nessa rota você pode deletar os dados de um fornecedor")
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor deletado")
    public ResponseEntity<FornecedorEmpresa> deletar(@PathVariable("id") UUID id)  {
        FornecedorEmpresa fornecedor = fornecedorUseCase.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body(fornecedor);
    }
}
