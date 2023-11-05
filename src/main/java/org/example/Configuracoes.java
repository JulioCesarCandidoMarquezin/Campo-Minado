package org.example;

import org.example.console.TabuleiroConsole;
import org.example.entity.Tabuleiro;
import org.example.ui.TabuleiroUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Configuracoes extends JFrame {
    private static final JTextField linhasField = new JTextField();
    private static final JTextField colunasField = new JTextField();
    private static final JTextField minasField = new JTextField();
    private static Tabuleiro tabuleiro;

    public Configuracoes() {
        setTitle("Configurações do Jogo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10,10));

        panel.add(new JLabel("Número de Linhas:"));
        panel.add(linhasField);

        panel.add(new JLabel("Número de Colunas:"));
        panel.add(colunasField);

        panel.add(new JLabel("Número de Minas:"));
        panel.add(minasField);

        JButton consoleButton = new JButton("Jogar no Console");
        consoleButton.addActionListener(Configuracoes::iniciarJogoConsole);

        JButton uiButton = new JButton("Jogar na Interface Gráfica");
        uiButton.addActionListener(Configuracoes::iniciarJogoUI);

        panel.add(consoleButton);
        panel.add(uiButton);

        add(panel);
    }

    private static void criarTabuleiro()
    {
        try {
            int linhas = Integer.parseInt(linhasField.getText());
            int colunas = Integer.parseInt(colunasField.getText());
            int minas = Integer.parseInt(minasField.getText());
            tabuleiro = new Tabuleiro(linhas, colunas, minas);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valores inválidos");
        }
    }

    private static void iniciarJogoConsole(ActionEvent event)
    {
        criarTabuleiro();
        if(tabuleiro != null) new TabuleiroConsole(tabuleiro);
        fecharConfiguracoes(event);
    }

    private static void iniciarJogoUI(ActionEvent event)
    {
        criarTabuleiro();
        if(tabuleiro != null) new TabuleiroUI(tabuleiro);
        fecharConfiguracoes(event);
    }

    private static void fecharConfiguracoes(ActionEvent event)
    {
        JButton button = (JButton) event.getSource();
        JPanel painel = (JPanel) button.getParent();
        JFrame frame = (JFrame) painel.getTopLevelAncestor();

        frame.dispose();
    }
}
