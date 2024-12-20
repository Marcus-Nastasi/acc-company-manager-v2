package com.accenture.test.adapter.controller.fornecedor;

import com.accenture.test.adapter.mapper.fornecedor.FornecedorDtoMapper;
import com.accenture.test.adapter.input.fornecedor.FornecedorRequestDto;
import com.accenture.test.adapter.output.fornecedor.FornecedorPagResponseDto;
import com.accenture.test.adapter.output.fornecedor.FornecedorResponseDto;
import com.accenture.test.application.usecase.fornecedor.FornecedorUseCase;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.domain.fornecedor.FornecedorPag;
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
    @Operation(summary = "Buscar todos os fornecedores", description = "Nessa rota você pode consultar dados de todos os fornecedores, com paginação e filtro por nome e cnpj/cpf")
    @ApiResponse(responseCode = "200", description = "Retornando dados dos fornecedores")
    public FornecedorPagResponseDto buscar_tudo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cnpj_cpf", required = false) String cnpj_cpf
    ) {
        if (size < 1) size = 10;
        FornecedorPag<Fornecedor> fornecedorPag = fornecedorUseCase.buscar_tudo(page, size, nome, cnpj_cpf);
        return new FornecedorPagResponseDto(
            fornecedorPag.getDados().stream().map(fornecedorDtoMapper::mapToClean).toList(),
            fornecedorPag.getPaginaAtual(),
            fornecedorPag.getTotalPaginas(),
            fornecedorPag.getPaginasRestantes() - 1
        );
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar um único fornecedor", description = "Nessa rota você pode consultar dados detalhados de um único fornecedor")
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor único")
    public FornecedorResponseDto buscar_um(@PathVariable("id") UUID id) {
        return fornecedorDtoMapper.mapToResponse(fornecedorUseCase.buscar_um(id));
    }

    @PostMapping(value = "/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo fornecedor", description = "Nessa rota você pode cadastrar um novo fornecedor")
    @ApiResponse(responseCode = "201", description = "Retornando dados do fornecedor criado")
    public ResponseEntity<FornecedorResponseDto> registrar(@RequestBody @Valid FornecedorRequestDto data) {
        Fornecedor fornecedor = fornecedorUseCase.registrar(fornecedorDtoMapper.mapFromRequest(data));
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorDtoMapper.mapToResponse(fornecedor));
    }

    @PatchMapping(value = "/atualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar dados de um fornecedor", description = "Nessa rota você pode atualizar os dados de um fornecedor")
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor atualizado")
    public ResponseEntity<FornecedorResponseDto> atualizar(@PathVariable("id") UUID id, @RequestBody @Valid FornecedorRequestDto data) {
        Fornecedor fornecedor = fornecedorUseCase.atualizar(id, fornecedorDtoMapper.mapFromRequest(data));
        return ResponseEntity.status(HttpStatus.OK).body(fornecedorDtoMapper.mapToResponse(fornecedor));
    }

    @DeleteMapping(value = "/deletar/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deletar um fornecedor", description = "Nessa rota você pode deletar os dados de um fornecedor")
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor deletado")
    public ResponseEntity<FornecedorResponseDto> deletar(@PathVariable("id") UUID id)  {
        Fornecedor fornecedor = fornecedorUseCase.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body(fornecedorDtoMapper.mapToResponse(fornecedor));
    }
}
