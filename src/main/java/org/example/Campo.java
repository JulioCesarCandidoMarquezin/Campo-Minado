package org.example;

import org.example.exceptions.ExplosaoException;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    private int linha;
    private int coluna;

    private boolean aberto;
    private boolean minado;
    private boolean marcado;

    private List<Campo> vizinhos = new ArrayList<>();

    public Campo(int linha, int coluna)
    {
        this.linha = linha;
        this.coluna = coluna;
    }

    public boolean adicionarVizinho(Campo vizinho)
    {
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunadDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunadDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        if(deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if(deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        }

        return false;
    }

    public void alterarMarcacao()
    {
        marcado = !marcado;
    }

    public boolean abrir()
    {
        if(!aberto && !marcado) {
            aberto = true;

            if(minado) {
                throw new ExplosaoException();
            }

            if(vizinhancaSegura()) {
                vizinhos.forEach(Campo::abrir);
            }

            return true;
        }
        return false;
    }

    public boolean vizinhancaSegura()
    {
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    public void minar()
    {
        minado = !minado;
    }

    public boolean isMinado()
    {
        return minado;
    }

    public boolean isMarcado()
    {
        return marcado;
    }

    public void setAberto(boolean aberto)
    {
        this.aberto = aberto;
    }

    public boolean isAberto()
    {
        return aberto;
    }

    public int getLinha()
    {
        return linha;
    }

    public int getColuna()
    {
        return coluna;
    }

    public boolean objetivoAlcancado()
    {
        boolean desvendado = !marcado && aberto;
        boolean protegido = minado && marcado;

        return desvendado || protegido;
    }

    public long minasNaVizinhanca()
    {
        return vizinhos.stream().filter(v -> v.minado).count();
    }

    public void reiniciar()
    {
        aberto = false;
        minado = false;
        marcado = false;
    }

    @Override
    public String toString()
    {
        if(marcado) return "x";
        if(aberto && minado) return "#";
        if(aberto && minasNaVizinhanca() > 0) return Long.toString(minasNaVizinhanca());
        if(aberto) return "0";
        return "?";
    }
}