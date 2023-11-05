package org.example;

import org.example.ui.TabuleiroUI;

public class Start {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro(9,9, 20);
        new TabuleiroConsole(tabuleiro);
        new TabuleiroUI(tabuleiro);
    }
}