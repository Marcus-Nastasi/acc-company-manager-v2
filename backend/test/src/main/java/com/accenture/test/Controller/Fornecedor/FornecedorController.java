package com.accenture.test.Controller.Fornecedor;

import com.accenture.test.Domain.Fornecedor.DTO.FornecedorEmpResponseDTO;
import com.accenture.test.Domain.Fornecedor.DTO.FornecedorPagResponseDTO;
import com.accenture.test.Domain.Fornecedor.DTO.RegistrarFornecedorDTO;
import com.accenture.test.Service.Fornecedor.FornecedorService;
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
    private FornecedorService fornecedorService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar todos os fornecedores",
        description = "Nessa rota você pode consultar dados de todos os fornecedores, com paginação e filtro por nome e cnpj/cpf"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados dos fornecedores")
    public ResponseEntity<FornecedorPagResponseDTO<FornecedorEmpResponseDTO>> buscar_tudo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cnpj_cpf", required = false) String cnpj_cpf
    ) {
        if (size < 1) size = 10;
        FornecedorPagResponseDTO<FornecedorEmpResponseDTO> fornecedorList = fornecedorService
            .buscar_tudo(page, size, nome, cnpj_cpf);
        return ResponseEntity.ok(fornecedorList);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar um único fornecedor",
        description = "Nessa rota você pode consultar dados detalhados de um único fornecedor"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor único")
    public ResponseEntity<FornecedorEmpResponseDTO> buscar_um(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(fornecedorService.buscar_um(id));
    }

    @PostMapping(value = "/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cadastrar novo fornecedor",
        description = "Nessa rota você pode cadastrar um novo fornecedor"
    )
    @ApiResponse(responseCode = "201", description = "Retornando dados do fornecedor criado")
    public ResponseEntity<FornecedorEmpResponseDTO> registrar(
            @RequestBody @Valid RegistrarFornecedorDTO data
    ) {
        FornecedorEmpResponseDTO fornecedor = fornecedorService.registrar(data);
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
        FornecedorEmpResponseDTO fornecedor = fornecedorService.atualizar(id, data);
        return ResponseEntity.status(HttpStatus.OK).body(fornecedor);
    }

    @DeleteMapping(value = "/deletar/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Deletar um fornecedor",
        description = "Nessa rota você pode deletar os dados de um fornecedor"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados de fornecedor deletado")
    public ResponseEntity<FornecedorEmpResponseDTO> deletar(@PathVariable("id") UUID id)  {
        FornecedorEmpResponseDTO fornecedor = fornecedorService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body(fornecedor);
    }
}
