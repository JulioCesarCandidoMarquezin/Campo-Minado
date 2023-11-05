package org.example.ui;

import org.example.Campo;
import org.example.Tabuleiro;
import org.example.exceptions.ExplosaoException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TabuleiroUI extends JFrame {
    private final Tabuleiro tabuleiro;
    private final JPanel painel;
    private final List<CampoUI> campos = new ArrayList<>();

    public TabuleiroUI(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;

        painel = new JPanel(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        initializeCampos(tabuleiro.getCampos());

        int largura = tabuleiro.getColunas() * 50;
        int altura = tabuleiro.getLinhas() * 50;

        Dimension dimensoesTela = Toolkit.getDefaultToolkit().getScreenSize();

        int larguraTela = dimensoesTela.width;
        int alturaTela = dimensoesTela.height;

        pack();
        setContentPane(painel);
        setLocationRelativeTo(null);
        setSize(Math.min(largura, larguraTela), Math.min(altura, alturaTela));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeCampos(List<Campo> campos)
    {
        final int[] index = {0};
        campos.forEach((campo) -> {
            CampoUI campoUI = new CampoUI(this, campo);
            this.campos.add(campoUI);
            painel.add(campoUI, index[0]);
        });
    }

    protected void abrirCampo(CampoUI campo)
    {
        try {
            tabuleiro.abrir(campo.getCampo());
        } catch (ExplosaoException e) {
            abrirCampos();
            JOptionPane.showMessageDialog(this, "Você perdeu");
            novoJogo();
        }
       if(tabuleiro.objectivoAlcancado()) {
           JOptionPane.showMessageDialog(this, "Você ganhou");
           novoJogo();
       }
    }

    protected void alterMarcacaoCampo(CampoUI campo)
    {
        tabuleiro.alterarMarcacao(campo.getCampo());
    }

    protected void abrirCampos()
    {
        tabuleiro.getCampos().forEach(Campo::abrir);
    }

    protected void atualizarCampos()
    {
        campos.forEach((campoUI -> campoUI.setText(campoUI.getCampo().toString())));
    }

    protected void novoJogo()
    {
        int resposta = JOptionPane.showConfirmDialog(this, "Recomeçar?");
        if (resposta != 0) dispose();
        tabuleiro.reiniciar();
        atualizarCampos();
    }

    public static void main(String[] args)
    {
        Tabuleiro tabuleiro = new Tabuleiro(5,5,5);
        SwingUtilities.invokeLater(() -> new TabuleiroUI(tabuleiro));
    }
}