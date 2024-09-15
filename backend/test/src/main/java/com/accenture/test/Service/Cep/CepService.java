package com.accenture.test.Service.Cep;

import com.accenture.test.Domain.Cep.DTO.CepResponseDTO;
import com.accenture.test.Exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CepService {

    private final WebClient webClient = WebClient.create();

    public Mono<ResponseEntity<CepResponseDTO>> buscarCep(String cep) {
        try {
            cep = cep.replace("-", "");
            if (cep.length() > 8) throw new AppException("Cep inválido");
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            return webClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(CepResponseDTO.class);
        } catch (RuntimeException e) {
            throw new AppException("Erro ao buscar CEP");
        }
    }
}
