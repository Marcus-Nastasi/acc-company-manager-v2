package com.accenture.test.Controller.Empresa;

import com.accenture.test.Domain.Empresa.Empresa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/empresa")
public class EmpresaController {

    @PostMapping(value = "/registrar")
    public ResponseEntity<Empresa> registrar() {
        return ResponseEntity.ok(new Empresa());
    }
}
