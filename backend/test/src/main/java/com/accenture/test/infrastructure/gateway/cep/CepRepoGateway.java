package com.accenture.test.infrastructure.gateway.cep;

import com.accenture.test.application.gateways.cep.CepGateway;
import com.accenture.test.domain.cep.Cep;
import com.accenture.test.infrastructure.exception.InfraException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

public class CepRepoGateway implements CepGateway {

    private final WebClient webClient = WebClient.create();

    @Override
    public Cep getCep(String cep) {
        try {
            cep = cep.replaceAll("\\D", "").trim();
            if (cep.length() > 8) throw new InfraException("Invalid cep");
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            return Objects.requireNonNull(webClient.get().uri(url).retrieve().toEntity(Cep.class).block()).getBody();
        } catch (RuntimeException e) {
            throw new InfraException("Error getting CEP: " + e.getMessage());
        }
    }
}
