package br.com.vinicios;

import br.com.vinicios.ui.custom.frame.MainFrame;
import br.com.vinicios.ui.custom.panel.MainPanel;
import br.com.vinicios.ui.custom.screen.MainScreen;



import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class UIMain {

    public static void main(String[] args){
        final var gameConfig = Stream.of(args).collect(toMap(k -> k.split(";")[0], v -> v.split(";")[1]));
        var mainScreen = new MainScreen(gameConfig);
        mainScreen.buildMainScreen();
    }



}