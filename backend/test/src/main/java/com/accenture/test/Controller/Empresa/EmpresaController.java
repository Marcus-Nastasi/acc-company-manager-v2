package com.accenture.test.Controller.Empresa;

import com.accenture.test.Domain.Empresa.DTO.AtualizarEmpresaDTO;
import com.accenture.test.Domain.Empresa.DTO.EmpresaFornResponseDTO;
import com.accenture.test.Domain.Empresa.DTO.RegistrarEmpresaDTO;
import com.accenture.test.Service.Empresa.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping()
    public ResponseEntity<List<EmpresaFornResponseDTO>> buscar_tudo(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        if (size < 1) size = 10;
        List<EmpresaFornResponseDTO> empresas = empresaService
            .buscar_tudo(page, size);
        return ResponseEntity.ok(empresas);
    }

    @PostMapping(value = "/registrar")
    public ResponseEntity<EmpresaFornResponseDTO> registrar(@RequestBody RegistrarEmpresaDTO data) {
        EmpresaFornResponseDTO empresa = empresaService.registrar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
    }

    @PatchMapping(value = "/atualizar/{id}")
    public ResponseEntity<EmpresaFornResponseDTO> atualizar(
            @PathVariable("id") UUID id,
            @RequestBody AtualizarEmpresaDTO data
    ) {
        EmpresaFornResponseDTO empresa = empresaService.atualizar(id, data);
        return ResponseEntity.status(HttpStatus.OK).body(empresa);
    }

    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<EmpresaFornResponseDTO> deletar(
            @PathVariable("id") UUID id
    ) {
        EmpresaFornResponseDTO empresa = empresaService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body(empresa);
    }

    @PatchMapping(value = "/associar/{id}/{id_fornecedor}")
    public ResponseEntity<EmpresaFornResponseDTO> atualizar(
            @PathVariable("id_empresa") UUID id_empresa,
            @PathVariable("id_fornecedor") UUID id_fornecedor
    ) {
        EmpresaFornResponseDTO empresa = empresaService
            .associarFornecedor(id_fornecedor, id_empresa);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(empresa);
    }
}
