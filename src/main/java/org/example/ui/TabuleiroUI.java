package org.example.ui;

import org.example.Configuracoes;
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
        JLabel minas = new JLabel(){{
            setText("Minas: " + tabuleiro.getMinas());
        }};
        JButton novasConfiguracoes = new JButton("Novas configurações");
        novasConfiguracoes.addActionListener((e) -> {
            this.dispose();
            new Configuracoes().setVisible(true);
        });

        JPanel painelUtil = new JPanel(new FlowLayout());
        painelUtil.setLocation(0,0);
        painelUtil.setMinimumSize(new Dimension(0, 50));
        painelUtil.setPreferredSize(new Dimension(getWidth(), 50));
        painelUtil.setMaximumSize(new Dimension(getWidth(), 50));

        painelUtil.add(minas);
        painelUtil.add(marcacoes);
        painelUtil.add(tempo);
        painelUtil.add(novasConfiguracoes);

        this.tabuleiro = tabuleiro;

        painelCampos = new JPanel(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        JScrollPane painelTabuleiro = new JScrollPane(painelCampos);
        initializeCampos(tabuleiro.getCampos());

        Dimension dimensoesTela = Toolkit.getDefaultToolkit().getScreenSize();
        Toolkit.getDefaultToolkit().beep();

        int larguraTela = Math.max(500, Math.min(tabuleiro.getColunas() * 50, dimensoesTela.width));
        int alturaTela = Math.max(500, Math.min((tabuleiro.getLinhas() * 50) - 50, dimensoesTela.height - 50));


        JPanel painel = new JPanel(new BorderLayout());
        painel.add(painelUtil, BorderLayout.NORTH);
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
            campoUI.setFont(new Font("Arial", Font.BOLD, 14));
            campoUI.setForeground(Color.DARK_GRAY);
            this.campos.add(campoUI);
            painelCampos.add(campoUI, index[0]);
        });
    }

    private void atualizaTemporizador()
    {
        Thread temporizador = new Thread(() -> {
            while (!tabuleiro.objectivoAlcancado()) {
                try {
                    Thread.sleep(1000);
                    long tempoAtual = System.currentTimeMillis();
                    tempo.setText("Segundos: " + (tempoAtual - tempoInicial) / 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        temporizador.start();
    }

    protected void abrirCampo(CampoUI campo)
    {
        try {
            tabuleiro.abrir(campo.getCampo());
            atualizarCampos();
        } catch (ExplosaoException e) {
            atualizarCampos();
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
        marcacoes.setText("Campos marcados: " + tabuleiro.getCampos().stream().filter(Campo::isMarcado).count());
        if(tabuleiro.objectivoAlcancado()) {
            JOptionPane.showMessageDialog(this, "Você ganhou");
            novoJogo();
        }
    }

    protected void atualizarCampos()
    {
        campos.forEach((campoUI -> {
            Campo campo = campoUI.getCampo();
            String texto = campo.toString();
            campoUI.setText(texto);
            if(campo.isAberto() || campo.isMarcado()) {
                campoUI.setBackground(Color.LIGHT_GRAY);
                switch (texto) {
                    case "0" -> campoUI.setForeground(Color.GREEN);
                    case "1", "2", "3" -> campoUI.setForeground(Color.YELLOW);
                    case "4", "5", "6" -> campoUI.setForeground(Color.ORANGE);
                    case "7", "8" -> campoUI.setForeground(Color.RED);
                    case "x" -> campoUI.setForeground(Color.BLUE);
                    case "#" -> campoUI.setForeground(Color.BLACK);
                }
            } else {
                campoUI.setForeground(Color.DARK_GRAY);
            }
        }));
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
        atualizaTemporizador();
        atualizarCampos();
    }
}