package com.accenture.test.Controller.Empresa;

import com.accenture.test.Domain.Empresa.DTO.AtualizarEmpresaDTO;
import com.accenture.test.Domain.Empresa.DTO.EmpPagResponseDTO;
import com.accenture.test.Domain.Empresa.DTO.EmpresaFornResponseDTO;
import com.accenture.test.Domain.Empresa.DTO.RegistrarEmpresaDTO;
import com.accenture.test.Domain.Fornecedor.DTO.FornecedorEmpResponseDTO;
import com.accenture.test.Service.Empresa.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/empresa")
@CrossOrigin(origins = "http://localhost:3000")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping()
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
    public ResponseEntity<EmpresaFornResponseDTO> buscar_um(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(empresaService.buscar_um(id));
    }

    @PostMapping(value = "/registrar")
    public ResponseEntity<EmpresaFornResponseDTO> registrar(
            @RequestBody @Valid RegistrarEmpresaDTO data
    ) {
        EmpresaFornResponseDTO empresa = empresaService.registrar(data);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(empresa);
    }

    @PatchMapping(value = "/atualizar/{id}")
    public ResponseEntity<EmpresaFornResponseDTO> atualizar(
            @PathVariable("id") UUID id,
            @RequestBody @Valid AtualizarEmpresaDTO data
    ) {
        EmpresaFornResponseDTO empresa = empresaService.atualizar(id, data);
        return ResponseEntity.ok(empresa);
    }

    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<EmpresaFornResponseDTO> deletar(
            @PathVariable("id") UUID id
    ) {
        EmpresaFornResponseDTO empresa = empresaService.deletar(id);
        return ResponseEntity.ok(empresa);
    }

    @PatchMapping(value = "/associar/{id_empresa}/{id_fornecedor}")
    public ResponseEntity<EmpresaFornResponseDTO> atualizar(
            @PathVariable("id_empresa") UUID id_empresa,
            @PathVariable("id_fornecedor") UUID id_fornecedor
    ) {
        EmpresaFornResponseDTO empresa = empresaService
            .associarFornecedor(id_fornecedor, id_empresa);
        return ResponseEntity.ok(empresa);
    }
}
