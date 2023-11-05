package org.example;

import org.example.exceptions.ExplosaoException;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {

    private int linhas;
    private int colunas;
    private int minas;

    private final List<Campo> campos = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas)
    {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public int getLinhas() {
        return linhas;
    }

    public void setLinhas(int linhas) {
        this.linhas = linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public void setColunas(int colunas) {
        this.colunas = colunas;
    }

    public int getMinas() {
        return minas;
    }

    public void setMinas(int minas) {
        this.minas = minas;
    }

    public List<Campo> getCampos() {
        return campos;
    }

    public Campo getCampo(int linha, int coluna)
    {
        return getCampos().stream().filter((c) -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().get();
    }

    public void abrir(Campo campo)
    {
        try {
            campos.parallelStream()
                    .filter(c -> c.getLinha() == campo.getLinha() && c.getColuna() == campo.getColuna())
                    .findFirst()
                    .ifPresent(Campo::abrir);
        } catch (ExplosaoException e) {
            campos.forEach(c -> c.setAberto(true));
            throw e;
        }
    }

    public void alterarMarcacao(Campo campo)
    {
        campos.parallelStream()
                .filter(c -> c.getLinha() == campo.getLinha() && c.getColuna() == campo.getColuna())
                .findFirst()
                .ifPresent(Campo::alterarMarcacao);
    }

    private void gerarCampos()
    {
        for (int linha = 1; linha <= linhas; linha++) {
            for (int coluna = 1; coluna <= colunas; coluna++) {
                campos.add(new Campo(linha, coluna));
            }
        }
    }

    private void associarVizinhos()
    {
        for(Campo campo1 : campos) {
            for(Campo campo2 : campos) {
                campo1.adicionarVizinho(campo2);
            }
        }
    }

    private void sortearMinas()
    {
        long minasArmadas;
        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(Campo::isMinado).count();
        } while(minasArmadas < minas);
    }

    public boolean objectivoAlcancado()
    {
        return campos.stream().allMatch(Campo::objetivoAlcancado);
    }

    public void reiniciar()
    {
        campos.forEach(Campo::reiniciar);
        sortearMinas();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(" ");
        sb.append(" ");

        for (int coluna = 1; coluna <= colunas; coluna++) {
            sb.append(" ");
            sb.append(coluna);
            sb.append(" ");
        }

        sb.append("\n");
        
        int i = 0;
        for (int linha = 0; linha < linhas; linha++) {
            sb.append(linha+1);
            sb.append(" ");
            for (int coluna = 0; coluna < colunas; coluna++) {
                sb.append(" ");
                sb.append(campos.get(i));
                sb.append(" ");
                i++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}