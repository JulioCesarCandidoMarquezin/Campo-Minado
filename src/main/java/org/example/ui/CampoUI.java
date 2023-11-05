package org.example.ui;

import org.example.entity.Campo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CampoUI extends JButton {
    private final TabuleiroUI parent;
    private final Campo campo;
    private final CampoUI instacia;
    public CampoUI(TabuleiroUI tabuleiro, Campo campo)
    {
        this.campo = campo;
        this.parent = tabuleiro;
        this.instacia = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) parent.abrirCampo(instacia);
                if(SwingUtilities.isRightMouseButton(e)) parent.alterMarcacaoCampo(instacia);
                parent.atualizarCampos();
            }
        });

        setText(campo.toString());
        setForeground(Color.WHITE);
        setVisible(true);
        setBackground(Color.WHITE);
    }

    public Campo getCampo()
    {
        return campo;
    }
}
