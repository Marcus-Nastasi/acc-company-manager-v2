package com.accenture.test.Service.Cep;

import com.accenture.test.Domain.Cep.DTO.CepResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CepService {

    private final WebClient webClient = WebClient.create();

    public Mono<ResponseEntity<CepResponseDTO>> buscarCep(String cep) {
        try {
            if (cep.length() > 8) throw new RuntimeException("Cep inválido");
            cep = cep.replace("-", "");
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            return webClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(CepResponseDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
