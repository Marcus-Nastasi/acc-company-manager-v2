package com.accenture.test.adapter.controller.cep;

import com.accenture.test.application.usecase.cep.CepUseCase;
import com.accenture.test.domain.cep.Cep;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/cep")
public class CepController {

    @Autowired
    private CepUseCase cepUseCase;

    @GetMapping(value = "/{cep}")
    @Cacheable("cep")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar dados de endereço baseado no CEP", description = "Nessa rota você pode consultar dados detalhados de endereço baseado em um CEP")
    @ApiResponse(responseCode = "200", description = "Retornando dados da API dos correios")
    public ResponseEntity<Cep> buscar_cep(@PathVariable("cep") String cep) {
        Cep cr = cepUseCase.buscarCep(cep);
        if (cr == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(cr);
    }
}
