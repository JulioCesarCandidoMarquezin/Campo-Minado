package org.example.ui;

import org.example.entity.Campo;
import org.example.entity.Tabuleiro;
import org.example.exception.ExplosaoException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TabuleiroUI extends JFrame {
    private final Tabuleiro tabuleiro;
    private final JPanel painelCampos;
    private final JLabel marcacoes = new JLabel("Campos marcados: 0");
    private final JLabel tempo = new JLabel("Segundos: 0");
    private final List<CampoUI> campos = new ArrayList<>();
    private long tempoInicial = System.currentTimeMillis();

    public TabuleiroUI(Tabuleiro tabuleiro) {
        JLabel minas = new JLabel();
        minas.setText("Minas: " + tabuleiro.getMinas());

        JPanel painelEstatisticas = new JPanel(new GridBagLayout());
        painelEstatisticas.setLocation(0,0);
        painelEstatisticas.setMinimumSize(new Dimension(0, 50));
        painelEstatisticas.setPreferredSize(new Dimension(getWidth(), 50));
        painelEstatisticas.setMaximumSize(new Dimension(getWidth(), 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        painelEstatisticas.add(minas, gbc);

        gbc.gridx = 1;
        painelEstatisticas.add(marcacoes, gbc);

        gbc.gridx = 2;
        painelEstatisticas.add(tempo, gbc);

        this.tabuleiro = tabuleiro;

        painelCampos = new JPanel(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        JScrollPane painelTabuleiro = new JScrollPane(painelCampos);
        initializeCampos(tabuleiro.getCampos());

        Dimension dimensoesTela = Toolkit.getDefaultToolkit().getScreenSize();

        int larguraTela = Math.min(tabuleiro.getColunas() * 50, dimensoesTela.width);
        int alturaTela = Math.min((tabuleiro.getLinhas() * 50) - 50, dimensoesTela.height - 50);

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(painelEstatisticas, BorderLayout.NORTH);
        painel.add(painelTabuleiro, BorderLayout.CENTER);

        atualizaTemporizador();

        pack();
        setContentPane(painel);
        setLocationRelativeTo(null);
        setSize(larguraTela, alturaTela);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeCampos(List<Campo> campos)
    {
        final int[] index = {0};
        campos.forEach((campo) -> {
            CampoUI campoUI = new CampoUI(this, campo);
            this.campos.add(campoUI);
            painelCampos.add(campoUI, index[0]);
        });
    }

    private void atualizaTemporizador()
    {
        Thread atualizacao = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1000);
                    long tempoAtual = System.currentTimeMillis();
                    tempo.setText("Segundos: " + (tempoAtual - tempoInicial) / 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        atualizacao.start();
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
    }

    protected void alterMarcacaoCampo(CampoUI campo)
    {
        tabuleiro.alterarMarcacao(campo.getCampo());
        marcacoes.setText("Campos marcados: " + tabuleiro.getCampos().stream().filter(Campo::isMarcado).count());
    }

    protected void abrirCampos()
    {
        tabuleiro.getCampos().forEach(Campo::abrir);
    }

    protected void atualizarCampos()
    {
        campos.forEach((campoUI -> campoUI.setText(campoUI.getCampo().toString())));
        if(tabuleiro.objectivoAlcancado()) {
            JOptionPane.showMessageDialog(this, "Você ganhou");
            novoJogo();
        }
    }

    private void novoJogo()
    {
        int resposta = JOptionPane.showConfirmDialog(this, "Recomeçar?");
        if (resposta != 0) dispose();
        tabuleiro.reiniciar();
        atualizarCampos();
        tempoInicial = System.currentTimeMillis();
        marcacoes.setText("Campos marcados: 0");
        tempo.setText("Segundos: 0");
    }
}