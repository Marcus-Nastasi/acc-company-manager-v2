package com.accenture.test.adapter.output.fornecedor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FornecedorPagResponseDTO<T> {
    private List<T> dados;
    private int paginaAtual;
    private int totalPaginas;
    private int paginasRestantes;
}
