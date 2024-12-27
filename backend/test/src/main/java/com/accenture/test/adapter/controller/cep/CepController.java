package com.accenture.test.adapter.controller.cep;

import com.accenture.test.adapter.output.cep.CepResponseDto;
import com.accenture.test.application.usecase.cep.CepUseCase;
import com.accenture.test.domain.cep.Cep;
import com.accenture.test.infrastructure.exception.InfraException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/cep")
public class CepController {

    @Autowired
    private CepUseCase cepUseCase;

    @GetMapping(value = "/{cep}")
    @Cacheable("cep")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search address data based on zip code", description = "In this route you can consult detailed address data based on a zip code")
    @ApiResponse(responseCode = "200", description = "Returning data from the postal API")
    public CepResponseDto getByCep(@PathVariable("cep") String cep) {
        Cep cr = cepUseCase.getCep(cep);
        if (cr == null) throw new InfraException("Could not get the address based on this cep.");
        return new CepResponseDto(
            cr.getCep(),
            cr.getLogradouro(),
            cr.getComplemento(),
            cr.getUnidade(),
            cr.getBairro(),
            cr.getLocalidade(),
            cr.getUf(),
            cr.getEstado(),
            cr.getRegiao(),
            cr.getIbge(),
            cr.getGia(),
            cr.getDdd(),
            cr.getSiafi()
        );
    }
}
