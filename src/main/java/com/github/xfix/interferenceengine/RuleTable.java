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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * A table that stores logical rules in it
 *
 * @author Konrad Borowski
 */
public class RuleTable {

    private static final int NEGATED_WIDTH = 24;
    private static final int EXPRESSION_WIDTH = 700;

    TableView table = new TableView(FXCollections.observableArrayList());

    public RuleTable() {
        super();
        addColumns();

        table.setEditable(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private <T> void setEditHandler(TableColumn column, final BiConsumer<Rule, T> consumer) {
        column.setOnEditCommit(new EventHandler<CellEditEvent<Rule, T>>() {
            @Override
            public void handle(CellEditEvent<Rule, T> event) {
                final int row = event.getTablePosition().getRow();
                consumer.accept(event.getTableView().getItems().get(row), event.getNewValue());
            }
        });
    }

    private void addColumns() {
        final ObservableList<TableColumn> columns = table.getColumns();

        final TableColumn negated = new TableColumn("!");
        final TableColumn variableName = new TableColumn("Nazwa");
        final TableColumn variable = new TableColumn("Zmienna");
        variable.getColumns().addAll(negated, variableName);
        final TableColumn expression = new TableColumn("Wyrażenie");

        negated.setCellValueFactory(new PropertyValueFactory<>("negated"));
        variableName.setCellValueFactory(new PropertyValueFactory<>("variableName"));
        expression.setCellValueFactory(new PropertyValueFactory<>("expression"));

        variableName.setCellFactory(TextFieldTableCell.forTableColumn());
        negated.setCellFactory(CheckBoxTableCell.forTableColumn(negated));
        expression.setCellFactory(TextFieldTableCell.forTableColumn());

        setEditHandler(variableName, new BiConsumer<Rule, String>() {
            @Override
            public void accept(Rule rule, String variableName) {
                rule.setVariableName(variableName);
            }
        });
        setEditHandler(negated, new BiConsumer<Rule, Boolean>() {
            @Override
            public void accept(Rule rule, Boolean negated) {
                rule.setNegated(negated);
            }
        });
        setEditHandler(expression, new BiConsumer<Rule, String>() {
            @Override
            public void accept(Rule rule, String expression) {
                rule.setExpression(expression);
            }
        });

        negated.setPrefWidth(NEGATED_WIDTH);
        expression.setPrefWidth(EXPRESSION_WIDTH);

        columns.addAll(variable, expression);
    }

    /**
     * Adds an empty row to end of a table
     */
    public void addRow() {
        addRow(new Rule(new VariableDescriptor("", false), ""));
    }

    /**
     * Adds a prefilled row to end of a table
     *
     * @param rule Rule to add
     */
    synchronized public void addRow(Rule rule) {
        table.getItems().add(rule);
    }

    /**
     * Removes all entries from a table
     */
    public void clear() {
        table.getItems().clear();
    }

    /**
     * Deletes currently selected rows
     *
     * @return Number of rows that were removed
     */
    public int deleteCurrentRows() {
        MultipleSelectionModel<?> selectionModel = table.getSelectionModel();
        // As elements change order during deletion, delete later
        // elements first to prevent elements from moving around.
        final ArrayList<Integer> elements = new ArrayList<>(selectionModel.getSelectedIndices());
        Collections.sort(elements, Collections.reverseOrder());
        for (final int indice : elements) {
            table.getItems().remove(indice);
        }
        return elements.size();
    }

    /**
     * Gets list of rules in a table
     *
     * @return list of rules
     */
    public List<Rule> getRules() {
        return table.getItems();
    }

    public TableView getTable() {
        return table;
    }
}
