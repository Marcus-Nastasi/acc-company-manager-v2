package com.accenture.test.infrastructure.gateway.cep;

import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.gateways.cep.CepGateway;
import com.accenture.test.domain.cep.Cep;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

public class CepRepoGateway implements CepGateway {

    private final WebClient webClient = WebClient.create();

    @Override
    public Cep buscarCep(String cep) {
        try {
            cep = cep.replace(".", "").replace("-", "");
            if (cep.length() > 8) throw new AppException("Cep inválido");
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            return Objects.requireNonNull(webClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(Cep.class)
                .block()
            ).getBody();
        } catch (RuntimeException e) {
            throw new AppException("Erro ao buscar CEP: " + e.getMessage());
        }
    }
}
