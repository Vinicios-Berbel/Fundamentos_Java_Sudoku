package br.com.vinicios.ui.custom.screen;

import br.com.vinicios.model.Cell;
import br.com.vinicios.service.BoardService;
import br.com.vinicios.service.NotifierService;
import br.com.vinicios.ui.custom.button.CheckGameStatusButton;
import br.com.vinicios.ui.custom.button.FinishedGameButton;
import br.com.vinicios.ui.custom.button.ResetButton;
import br.com.vinicios.ui.custom.frame.MainFrame;
import br.com.vinicios.ui.custom.input.NumberText;
import br.com.vinicios.ui.custom.panel.MainPanel;
import br.com.vinicios.ui.custom.panel.SudokuSector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.vinicios.model.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showMessageDialog;

import static javax.swing.JOptionPane.*;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton finishGameButton;
    private JButton checkGameStatusButton;
    private JButton resetButton;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r = 0; r < 9; r += 3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c += 3) {
                var endCol = c + 2;
                var cells = getCellsFromSector(boardService.getCell(), c, endCol, r, endRow);
                JPanel sector = generateSection(cells);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Cell> getCellsFromSector(final List<List<Cell>> spaces,
                                          final int initCol, final int endCol,
                                          final int initRow, final int endRow) {
        List<Cell> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
            for (int c = initCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }

    private JPanel generateSection(final List<Cell> spaces) {
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }

    private void addFinishGameButton(final JPanel mainPanel) {
        finishGameButton = new FinishedGameButton(e -> {
            if (boardService.gameIsFinished()) {
                showMessageDialog(null, "Parabéns você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                var message = "Seu jogo tem alguma inconsistência, ajuste e tente novamente";
                showMessageDialog(null, message);
            }
        });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(final JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NO_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está imcompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            showMessageDialog(null, message);
        });
        mainPanel.add(MainScreen.this.checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
            var dialogResult = showConfirmDialog(null, "Deseja realmente reiniciar o jogo?", "Limpar o jogo", YES_NO_OPTION, QUESTION_MESSAGE
            );
            if (dialogResult == 0) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);

    }
}
