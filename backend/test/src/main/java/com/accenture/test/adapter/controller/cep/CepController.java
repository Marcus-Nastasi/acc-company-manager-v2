package com.accenture.test.adapter.controller.cep;

import com.accenture.test.adapter.output.cep.CepResponseDTO;
import com.accenture.test.application.usecase.cep.CepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/cep")
public class CepController {

    @Autowired
    private CepService cepService;

    @GetMapping(value = "/{cep}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar dados de endereço baseado no CEP",
        description = "Nessa rota você pode consultar dados detalhados de endereço baseado em um CEP"
    )
    @ApiResponse(responseCode = "200", description = "Retornando dados da API dos correios")
    public ResponseEntity<Mono<ResponseEntity<CepResponseDTO>>> buscar_cep(
            @PathVariable("cep") String cep
    ) {
        Mono<ResponseEntity<CepResponseDTO>> cr = cepService.buscarCep(cep);
        if (cr == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(cr);
    }
}
