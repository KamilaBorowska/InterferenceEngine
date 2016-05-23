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
package com.github.xfix.interferenceengine.expression;

import java.util.Optional;
import java.util.WeakHashMap;
import com.github.xfix.interferenceengine.tokenizer.tokens.VariableToken;

/**
 *
 * @author Konrad Borowski
 */
public class Variable implements Action {
    private final VariableToken name;
    private Optional<Expression> positiveExpression = Optional.empty();
    private Optional<Expression> negativeExpression = Optional.empty();
    private boolean known = false;
    private boolean value;
    
    private static final WeakHashMap<VariableToken, Variable> variables = new WeakHashMap<>();
    
    private Variable(VariableToken name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
    
    public static Variable named(VariableToken name) {
        Variable variable = variables.get(name);
        if (variable != null) {
            return variable;
        }
        variable = new Variable(name);
        variables.put(name, variable);
        return variable;
    }

    private void setPositiveExpression(Expression expression) {
        positiveExpression = Optional.of(expression);
    }
    
    private void setNegativeExpression(Expression expression) {
        negativeExpression = Optional.of(expression);
    }
    
    public void setExpression(Expression expression, boolean negated) {
        if (negated) {
            setNegativeExpression(expression);
        } else {
            setPositiveExpression(expression);
        }
    }
    
    private Optional<Expression> getNegativeExpression() {
        return negativeExpression;
    }

    private Optional<Expression> getPositiveExpression() {
        return positiveExpression;
    }
    
    public Optional<Expression> getExpression(boolean negated) {
        if (negated) {
            return getNegativeExpression();
        } else {
            return getPositiveExpression();
        }
    }

    @Override
    public int getArgumentCount() {
        return 0;
    }

    @Override
    public boolean getValue(boolean[] values) {
        return value;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean check(boolean negated) {
        Optional<Expression> expression = getExpression(negated);
        return expression.get().run();
    }

    public void set(boolean value) {
        setValue(value);
        setKnown(true);
    }
}
