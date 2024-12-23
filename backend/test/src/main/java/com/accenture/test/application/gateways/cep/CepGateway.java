package com.accenture.test.application.gateways.cep;

import com.accenture.test.domain.cep.Cep;

public interface CepGateway {

    Cep getCep(String cep);
}
