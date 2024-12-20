package com.accenture.test.application.gateways.cep;

import com.accenture.test.domain.cep.Cep;

public interface CepGateway {

    Cep buscarCep(String cep);
}
