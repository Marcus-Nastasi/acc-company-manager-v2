package com.accenture.test.application.usecase.cep;

import com.accenture.test.application.gateways.cep.CepGateway;
import com.accenture.test.domain.cep.Cep;

public class CepUseCase {

    private CepGateway cepGateway;

    public CepUseCase(CepGateway cepGateway) {
        this.cepGateway = cepGateway;
    }

    public Cep getCep(String cep) {
        return cepGateway.getCep(cep);
    }
}
