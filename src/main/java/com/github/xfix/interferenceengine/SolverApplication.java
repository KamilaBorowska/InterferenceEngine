/*
 * Copyright (C) 2016 Konrad Borowski <xfix at protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.xfix.interferenceengine;

import com.github.xfix.interferenceengine.expression.Variable;
import com.github.xfix.interferenceengine.graph.GraphDisplay;
import com.github.xfix.interferenceengine.parser.FileParser;
import com.github.xfix.interferenceengine.parser.SyntaxError;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.github.xfix.interferenceengine.solver.BackwardsChaining;
import java.util.ArrayList;

/**
 * JavaFX application that solves logical expressions
 *
 * @author Konrad Borowski
 */
public class SolverApplication extends Application {

    private final RuleTable table = new RuleTable();

    private void tryDeletingRows() {
        final int deleted = table.deleteCurrentRows();
        if (deleted == 0) {
            Alert noRows = new Alert(AlertType.INFORMATION, "Żaden rząd nie został zaznaczony.");
            noRows.setTitle("Wiadomość");
            noRows.setHeaderText("Brak rzędów");
            noRows.showAndWait();
        }
    }

    public static void error(String message) {
        Alert errorAlert = new Alert(AlertType.ERROR, message);
        errorAlert.setTitle("Błąd");
        errorAlert.setHeaderText("Błąd");
        errorAlert.showAndWait();
    }

    private void loadFile(Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Otwórz plik danych");
        final File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            FileParser parser = new FileParser(table);
            parser.startParsing();
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber += 1;
                final String line;
                line = scanner.nextLine();
                parser.parseLine(line, lineNumber);
            }
        } catch (FileNotFoundException ex) {
            error(String.format("Plik %s nie istnieje.", file));
        } catch (SyntaxError ex) {
            error(ex.toString());
        }
    }

    private ArrayList<Variable> getRules() {
        return RuleParser.parseRules(table.getRules());
    }

    private void solve() {
        ArrayList<Variable> rules = getRules();
        new BackwardsChaining(rules).solve();
        StringBuilder output = new StringBuilder();
        for (Variable variable : rules) {
            output
                    .append(variable)
                    .append(" = ")
                    .append(variable.isKnown() ? variable.getValue() ? '1' : '0' : '?')
                    .append('\n');
        }
        Alert alert = new Alert(AlertType.INFORMATION, output.toString());
        alert.setTitle("Wynik");
        alert.setHeaderText("Wyniki rozwiązywania układu równań");
        alert.showAndWait();
    }

    /**
     * Starts an application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts a logic solver JavaFX application
     *
     * @param primaryStage An initial scene of JavaFX application
     */
    @Override
    public void start(final Stage primaryStage) {
        VBox root = new VBox();

        HBox buttons = new HBox(4);
        Button loadButton = new Button("Wczytaj");
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                loadFile(primaryStage);
            }
        });
        Button addRowButton = new Button("Dodaj rząd");
        addRowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                table.addRow();
            }
        });
        Button deleteRowButton = new Button("Usuń rząd");
        deleteRowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                tryDeletingRows();
            }
        });
        EventHandler<ActionEvent> solveHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                solve();
            }
        };
        Button solveButton = new Button("Wnioskowanie w przód");
        solveButton.setOnAction(solveHandler);
        Button solveBackButton = new Button("Wnioskowanie w tył");
        solveBackButton.setOnAction(solveHandler);
        Button graphButton = new Button("Wyświetl graf");
        graphButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    GraphDisplay.draw(getRules());
                } catch (IllegalArgumentException ex) {
                    error("Masz nie zdefiniowane wartości");
                }
            }
        });
        buttons.getChildren().addAll(loadButton, addRowButton, deleteRowButton,
                solveButton, solveBackButton, graphButton);

        VBox.setVgrow(table.getTable(), Priority.ALWAYS);
        root.getChildren().addAll(buttons, table.getTable());

        Scene scene = new Scene(root);

        primaryStage.setTitle("Silnik wnioskujący");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
