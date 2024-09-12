package com.accenture.test.Controller.Fornecedor;

import com.accenture.test.Domain.Fornecedor.DTO.FornecedorEmpResponseDTO;
import com.accenture.test.Domain.Fornecedor.DTO.RegistrarFornecedorDTO;
import com.accenture.test.Service.Fornecedor.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping()
    public ResponseEntity<List<FornecedorEmpResponseDTO>> buscar_tudo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cnpj_cpf", required = false) String cnpj_cpf
    ) {
        if (size < 1) size = 10;
        List<FornecedorEmpResponseDTO> fornecedorList = fornecedorService
            .buscar_tudo(page, size, nome, cnpj_cpf);
        return ResponseEntity.ok(fornecedorList);
    }

    @PostMapping(value = "/registrar")
    public ResponseEntity<FornecedorEmpResponseDTO> registrar(
            @RequestBody @Valid RegistrarFornecedorDTO data
    ) {
        FornecedorEmpResponseDTO fornecedor = fornecedorService.registrar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedor);
    }

    @PatchMapping(value = "/atualizar/{id}")
    public ResponseEntity<FornecedorEmpResponseDTO> atualizar(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RegistrarFornecedorDTO data
    ) {
        FornecedorEmpResponseDTO fornecedor = fornecedorService.atualizar(id, data);
        return ResponseEntity.status(HttpStatus.OK).body(fornecedor);
    }

    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<FornecedorEmpResponseDTO> deletar(@PathVariable("id") UUID id)  {
        FornecedorEmpResponseDTO fornecedor = fornecedorService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body(fornecedor);
    }
}
