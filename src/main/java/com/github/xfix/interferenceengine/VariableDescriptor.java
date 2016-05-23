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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Konrad Borowski
 */
public class VariableDescriptor {
    protected StringProperty variableName = new SimpleStringProperty();
    protected BooleanProperty negated = new SimpleBooleanProperty();
    
    public VariableDescriptor(String variableName, boolean negated) {
        setNegated(negated);
        setVariableName(variableName);
    }

    public StringProperty variableNameProperty() {
        return variableName;
    }
    
    public String getVariableName() {
        return variableName.get();
    }

    public void setVariableName(String name) {
        while (name.startsWith("!")) {
            negated.set(!negated.get());
            name = name.substring(1);
        }
        variableName.set(name);
    }

    public BooleanProperty negatedProperty() {
        return negated;
    }
    
    /**
     * Gets negated property of a variable
     *
     * When solving logical problems, implications can
     * set a value to be false only if a rule is specified
     * to affect negated version of a variable
     *
     * @return negated property
     */
    public boolean getNegated() {
        return negated.get();
    }

    public void setNegated(boolean isNegated) {
        negated.set(isNegated);
    }
}
