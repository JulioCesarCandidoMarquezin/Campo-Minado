package org.example.console;

import org.example.entity.Campo;
import org.example.entity.Tabuleiro;
import org.example.exception.ExplosaoException;
import org.example.exception.SairException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class TabuleiroConsole {
    private Tabuleiro tabuleiro;
    private Scanner entrada = new Scanner(System.in);

    public TabuleiroConsole(Tabuleiro tabuleiro)
    {
        this.tabuleiro = tabuleiro;

        executarJogo();
    }

    private void executarJogo()
    {
        try {
            boolean continuar = true;

            while(continuar) {
                cicloDoJogo();
                
                System.out.println("Outra partida? (S/n) ");
                String resposta = entrada.nextLine();

                if(resposta.equalsIgnoreCase("n")) {
                    continuar = false;
                } else tabuleiro.reiniciar();
            }
        } catch (SairException e) {
            System.out.println("Tchau");
        } finally {
            entrada.close();
        }
    }

    private void cicloDoJogo()
    {
        try {
            while (!tabuleiro.objectivoAlcancado()) {
                System.out.println(tabuleiro);

                String digitado = capturarValorDigitado("Digite (x, y): ");

                Iterator<Integer> xy = Arrays.stream(digitado.split(",")).map(Integer::parseInt).iterator();
                int x = xy.next();
                int y = xy.next();

                digitado = capturarValorDigitado("1 - Abrir ou 2 - (Des)Marcar");

                Campo campo = tabuleiro.getCampo(x,y);
                if("1".equalsIgnoreCase(digitado)) tabuleiro.abrir(campo);
                else if("2".equalsIgnoreCase(digitado)) tabuleiro.alterarMarcacao(campo);
            }

            System.out.println("Você ganhou");
        } catch (ExplosaoException e) {
            System.out.println(tabuleiro);
            System.out.println("Você perdeu");
        }
    }

    private String capturarValorDigitado(String texto)
    {
        System.out.println(texto);
        String digitado = entrada.nextLine();

        if("sair".equalsIgnoreCase(digitado)) throw new SairException();

        return digitado;
    }
}
