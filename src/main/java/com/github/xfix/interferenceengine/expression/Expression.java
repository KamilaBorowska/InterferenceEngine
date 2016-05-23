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

import com.github.xfix.interferenceengine.tokenizer.Tokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konrad Borowski
 */
public class Expression {

    List<Action> actions;

    private Expression(List<Action> actions) {
        this.actions = actions;
        System.out.println(actions);
    }

    public static Expression fromCode(String code) {
        return new Expression(Parser.parse(Tokenizer.tokenize(code)));
    }

    public boolean run() {
        final ExpressionStack<Boolean> results = new ExpressionStack<>();
        for (final Action action : actions) {
            Object[] arguments = results.popCount(action.getArgumentCount());
            boolean[] booleanArguments = new boolean[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                booleanArguments[i] = (boolean) arguments[i];
            }
            boolean result = action.getValue(booleanArguments);
            results.push(result);
        }
        return results.get(0);
    }

    public ArrayList<Variable> getDependencies() {
        ArrayList<Variable> variables = new ArrayList<>();
        for (Action action : actions) {
            if (action instanceof Variable) {
                Variable variable = (Variable) action;
                if (!variable.isKnown()) {
                    variables.add(variable);
                }
            }
        }
        return variables;
    }
}
