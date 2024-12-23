package com.accenture.test.adapter.output.cep;

import java.io.Serializable;

public record CepResponseDto(
        String cep,
        String logradouro,
        String complemento,
        String unidade,
        String bairro,
        String localidade,
        String uf,
        String estado,
        String regiao,
        String ibge,
        String gia,
        String ddd,
        String siafi
) implements Serializable {}
