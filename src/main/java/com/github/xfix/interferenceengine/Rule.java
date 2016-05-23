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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Representation of logical rule
 * 
 * Stores a variable name, whether that name is negated
 * and an expression.
 * 
 * @author Konrad Borowski
 */
public class Rule {
    private final StringProperty expression;
    private final VariableDescriptor variableDescriptor;

    /**
     * Creates a rule
     * @param descriptor Variable name
     * @param expression Expression to be assigned to a rule
     */
    public Rule(VariableDescriptor descriptor, String expression) {
        variableDescriptor = descriptor;
        this.expression = new SimpleStringProperty(expression);
    }


    public String getExpression() {
        return expression.get();
    }

    public void setExpression(String expression) {
        this.expression.set(expression);
    }
    
    public StringProperty expressionProperty() {
        return expression;
    }
    
    public String getVariableName() {
        return variableDescriptor.getVariableName();
    }

    public void setVariableName(String name) {
        variableDescriptor.setVariableName(name);
    }
    
    public StringProperty variableNameProperty() {
        return variableDescriptor.variableNameProperty();
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
        return variableDescriptor.getNegated();
    }

    public void setNegated(boolean isNegated) {
        variableDescriptor.setNegated(isNegated);
    }
    
    public BooleanProperty negatedProperty() {
        return variableDescriptor.negatedProperty();
    }
}
