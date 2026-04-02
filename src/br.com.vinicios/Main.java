package br.com.vinicios;

import br.com.vinicios.model.Board;
import br.com.vinicios.model.Cell;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static br.com.vinicios.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main{

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;



    public static void main(String[] args){

        final var positions = Stream.of(args).collect(Collectors.toMap(k -> k.split(";")[0], v -> v.split(";")[1]));

        int option = -1;

        while(true){
            System.out.println("Selecione uma das opções a seguir:");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar o jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishedGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção invalida. Seleciona uma das opções do menu");
            }


        }
    }
    private static void startGame(final Map<String, String> positions) {
        if (nonNull(board)){
            System.out.println("O jogo já foi iniciado");
            return;
        }

        List<List<Cell>> cells = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = positions.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentCell = new Cell(expected, fixed);
                cells.get(i).add(currentCell);
            }
        }
        board = new Board(cells);
        System.out.println("O jogo está pronto para começar!");

    }

    private static void inputNumber(){
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.println("Informe a coluna que o número será inserido:");
        int col = runUtilGetValidNumber(0,8);
        System.out.println("Informe a linha que o número será inserido:");
        int row = runUtilGetValidNumber(0,8);
        System.out.println("Informe o número que deseja inserir:");
        int value = runUtilGetValidNumber(1,9);

        if(!board.changeValue(col, row, value)){
            System.out.printf("A posição [%d,%d] tem um valor fixo\n", col, row);
        }

    }

    private static void removeNumber(){
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.println("Informe a coluna que o número será removido:");
        int col = runUtilGetValidNumber(0,8);
        System.out.println("Informe a linha que o número será removido:");
        int row = runUtilGetValidNumber(0,8);

        if(!board.clearValue(col, row)){
            System.out.printf("A posição [%d,%d] tem um valor fixo\n", col, row);
        }
    }

    private static void showCurrentGame(){
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col: board.getCells()){
                args[argPos ++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BOARD_TEMPLATE) + "\n", args);

    }

    private static void showGameStatus(){
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.printf("O status do jogo é: %s", board.getStatus().getLabel());

        if(board.hasErrors()) System.out.println(" O jogo contém erros!");
        else System.out.println("O jogo não contém erros!");
    }

    private static void clearGame(){
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.println("Tem certeza que deseja limpar o seu jogo: Sim | Não (Está ação vai apagar todo o seu progresso)");
        String confirm = scanner.nextLine();
        while(!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")){
            System.out.println("Escolha apenas uma das opções: Sim ou Não");
            confirm = scanner.nextLine();
        }
        if(confirm.equalsIgnoreCase("sim")){
            board.reset();
        }
    }

    private static void finishedGame(){
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        if(board.gameIsFinished()){
            System.out.println("Parabéns, o jogo foi concluido!!!");
            showCurrentGame();
            board = null;
        }else if(board.hasErrors()){
            System.out.println("Seu jogo contém erros! Verifique o tabuleiro");
        }else {
            System.out.println("Você ainda precisa preencher algum espaço!");
        }
    }

    private static int runUtilGetValidNumber(final int min, final int max){
        while(true){
            try{
                int current = scanner.nextInt();
                if(current >= min && current <= max){
                    return current;
                }

                System.out.printf("Informe um número entre %d e %d\n", min, max);

            } catch(InputMismatchException e){
                System.err.println("Erro: O valor informado não é um numero valido!");
                System.err.println("Detalhes: " + e.getMessage());
                scanner.nextLine();
            }
        }

    }

}
