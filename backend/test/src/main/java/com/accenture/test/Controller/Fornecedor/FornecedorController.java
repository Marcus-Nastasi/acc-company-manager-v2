package com.accenture.test.Controller.Fornecedor;

import com.accenture.test.Domain.Fornecedor.Fornecedor;
import com.accenture.test.Domain.Fornecedor.RegistrarFornecedorDTO;
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
    public ResponseEntity<List<Fornecedor>> buscar_tudo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<Fornecedor> fornecedorList = fornecedorService.buscar_tudo(page, size);
        return ResponseEntity.ok(fornecedorList);
    }

    @PostMapping(value = "/registrar")
    public ResponseEntity<Fornecedor> registrar(@RequestBody @Valid RegistrarFornecedorDTO data) {
        try {
            Fornecedor fornecedor = fornecedorService.registrar(data);
            return ResponseEntity.status(HttpStatus.CREATED).body(fornecedor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/atualizar/{id}")
    public ResponseEntity<Fornecedor> atualizar(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RegistrarFornecedorDTO data
    ) {
        Fornecedor fornecedor = fornecedorService.atualizar(id, data);
        return ResponseEntity.status(HttpStatus.OK).body(fornecedor);
    }

    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<Fornecedor> deletar(@PathVariable("id") UUID id)  {
        Fornecedor fornecedor = fornecedorService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body(fornecedor);
    }
}
