package com.accenture.test.Controller.Empresa;

import com.accenture.test.Domain.Empresa.AtualizarEmpresaDTO;
import com.accenture.test.Domain.Empresa.Empresa;
import com.accenture.test.Domain.Empresa.RegistrarEmpresaDTO;
import com.accenture.test.Service.Empresa.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping()
    public ResponseEntity<List<Empresa>> buscar_tudo(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        if (size < 1) size = 10;
        List<Empresa> empresas = empresaService.buscar_tudo(page, size);
        return ResponseEntity.ok(empresas);
    }

    @PostMapping(value = "/registrar")
    public ResponseEntity<Empresa> registrar(@RequestBody RegistrarEmpresaDTO data) {
        Empresa empresa = empresaService.registrar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
    }

    @PatchMapping(value = "/atualizar/{cnpj}")
    public ResponseEntity<Empresa> atualizar(@PathVariable("cnpj") String cnpj, @RequestBody AtualizarEmpresaDTO data) {
        Empresa empresa = empresaService.atualizar(cnpj, data);
        return ResponseEntity.status(HttpStatus.OK).body(empresa);
    }

    @DeleteMapping(value = "/deletar/{cnpj}")
    public ResponseEntity<Empresa> deletar(@PathVariable("cnpj") String cnpj) {
        Empresa empresa = empresaService.deletar(cnpj);
        return ResponseEntity.status(HttpStatus.OK).body(empresa);
    }
}
