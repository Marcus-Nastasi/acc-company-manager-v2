package com.accenture.test.domain.supplier;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SupplierPag<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> dados;
    private int paginaAtual;
    private int totalPaginas;
    private int paginasRestantes;

    public SupplierPag(List<T> dados, int paginaAtual, int totalPaginas, int paginasRestantes) {
        this.dados = dados;
        this.paginaAtual = paginaAtual;
        this.totalPaginas = totalPaginas;
        this.paginasRestantes = paginasRestantes;
    }

    public List<T> getDados() {
        return dados;
    }

    public void setDados(List<T> dados) {
        this.dados = dados;
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(int paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public int getPaginasRestantes() {
        return paginasRestantes;
    }

    public void setPaginasRestantes(int paginasRestantes) {
        this.paginasRestantes = paginasRestantes;
    }
}
