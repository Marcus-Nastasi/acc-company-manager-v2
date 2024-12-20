package com.accenture.test.domain.empresa;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class EmpresaPag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Empresa> dados;
    private int paginaAtual;
    private int totalPaginas;
    private int paginasRestantes;

    public EmpresaPag(List<Empresa> dados, int paginaAtual, int totalPaginas, int paginasRestantes) {
        this.dados = dados;
        this.paginaAtual = paginaAtual;
        this.totalPaginas = totalPaginas;
        this.paginasRestantes = paginasRestantes;
    }

    public List<Empresa> getDados() {
        return dados;
    }

    public void setDados(List<Empresa> dados) {
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
