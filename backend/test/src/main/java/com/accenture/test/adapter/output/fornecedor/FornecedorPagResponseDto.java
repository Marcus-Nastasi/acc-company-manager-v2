package com.accenture.test.adapter.output.fornecedor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FornecedorPagResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<FornecedorCleanDto> dados;
    private int paginaAtual;
    private int totalPaginas;
    private int paginasRestantes;
}
