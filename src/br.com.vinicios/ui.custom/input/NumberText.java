package br.com.vinicios.ui.custom.input;

import br.com.vinicios.model.Cell;
import br.com.vinicios.model.EventEnum;
import br.com.vinicios.service.EventListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.Font;

import static br.com.vinicios.model.EventEnum.CLEAR_SPACE;

public class NumberText extends JTextField implements EventListener {

    private final Cell cell;

    public NumberText(final Cell cell){
        this.cell = cell;

        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", Font.PLAIN, 20));
        this.setHorizontalAlignment(CENTER);
        this.setDocument(new NumberTextLimit());
        this.setEnabled(!cell.isFixed());
        if(cell.isFixed()){
            this.setText(cell.getActual().toString());
        }
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeCell();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeCell();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeCell();
            }

            private void changeCell(){
                if(getText().isEmpty()){
                    cell.clearCell();
                    return;
                }
                cell.setActual(Integer.parseInt(getText()));
            }
        });

    }

    @Override
    public void update(EventEnum eventType) {
        if(eventType.equals(CLEAR_SPACE) && (this.isEnabled())){
            this.setText("");
        }
    }
}