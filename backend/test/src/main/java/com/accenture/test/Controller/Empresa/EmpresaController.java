package com.accenture.test.Controller.Empresa;

import com.accenture.test.Domain.Empresa.DTO.AtualizarEmpresaDTO;
import com.accenture.test.Domain.Empresa.DTO.EmpPagResponseDTO;
import com.accenture.test.Domain.Empresa.DTO.EmpresaFornResponseDTO;
import com.accenture.test.Domain.Empresa.DTO.RegistrarEmpresaDTO;
import com.accenture.test.Service.Empresa.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar todas as empresas",
        description = "Nessa rota você pode consultar dados de todas as empresas, com paginação e filtro por nome, cnpj e cep"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados das empresas")
    public ResponseEntity<EmpPagResponseDTO<EmpresaFornResponseDTO>> buscar_tudo(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "nome", defaultValue = "", required = false) String nome_fantasia,
            @RequestParam(name = "cnpj", defaultValue = "", required = false) String cnpj,
            @RequestParam(name = "cep", defaultValue = "", required = false) String cep
    ) {
        if (size < 1) size = 10;
        EmpPagResponseDTO<EmpresaFornResponseDTO> empresas = empresaService
            .buscar_tudo(page, size, nome_fantasia, cnpj, cep);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar uma única empresa",
        description = "Nessa rota você pode consultar dados detalhados de uma única empresa"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa única")
    public ResponseEntity<EmpresaFornResponseDTO> buscar_um(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(empresaService.buscar_um(id));
    }

    @PostMapping(value = "/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cadastrar nova empresa",
        description = "Nessa rota você pode cadastrar uma nova empresa"
    )
    @ApiResponse(responseCode = "201", description = "Retornando dados da empresa criada")
    public ResponseEntity<EmpresaFornResponseDTO> registrar(
            @RequestBody @Valid RegistrarEmpresaDTO data
    ) {
        EmpresaFornResponseDTO empresa = empresaService.registrar(data);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(empresa);
    }

    @PatchMapping(value = "/atualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Atualizar dados de uma empresa",
        description = "Nessa rota você pode atualizar os dados de uma empresa"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa atualizada")
    public ResponseEntity<EmpresaFornResponseDTO> atualizar(
            @PathVariable("id") UUID id,
            @RequestBody @Valid AtualizarEmpresaDTO data
    ) {
        EmpresaFornResponseDTO empresa = empresaService.atualizar(id, data);
        return ResponseEntity.ok(empresa);
    }

    @DeleteMapping(value = "/deletar/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Deletar uma empresa",
        description = "Nessa rota você pode deletar os dados de uma empresa"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa deletada")
    public ResponseEntity<EmpresaFornResponseDTO> deletar(
            @PathVariable("id") UUID id
    ) {
        EmpresaFornResponseDTO empresa = empresaService.deletar(id);
        return ResponseEntity.ok(empresa);
    }

    @PatchMapping(value = "/associar/{id_empresa}/{id_fornecedor}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Associar empresa e fornecedor",
        description = "Nessa rota você pode associar um fornecedor com uma empresa"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados de empresa associada à um novo fornecedor")
    public ResponseEntity<EmpresaFornResponseDTO> atualizar(
            @PathVariable("id_empresa") UUID id_empresa,
            @PathVariable("id_fornecedor") UUID id_fornecedor
    ) {
        EmpresaFornResponseDTO empresa = empresaService
            .associarFornecedor(id_fornecedor, id_empresa);
        return ResponseEntity.ok(empresa);
    }
}
