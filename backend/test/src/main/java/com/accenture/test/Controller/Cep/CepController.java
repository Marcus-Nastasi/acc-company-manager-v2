package com.accenture.test.Controller.Cep;

import com.accenture.test.Domain.Cep.DTO.CepResponseDTO;
import com.accenture.test.Service.Cep.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/cep")
public class CepController {

    @Autowired
    private CepService cepService;

    @GetMapping(value = "/{cep}")
    public ResponseEntity<Mono<ResponseEntity<CepResponseDTO>>> buscar_cep(
            @PathVariable("cep") String cep
    ) {
        Mono<ResponseEntity<CepResponseDTO>> cr = cepService.buscarCep(cep);
        if (cr == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(cr);
    }
}
