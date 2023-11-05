package org.example;

import javax.swing.SwingUtilities;

public class Start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Configuracoes().setVisible(true));
    }
}