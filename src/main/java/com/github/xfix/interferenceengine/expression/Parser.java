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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.github.xfix.interferenceengine.tokenizer.tokens.OpenParenToken;
import com.github.xfix.interferenceengine.tokenizer.tokens.OperatorToken;
import com.github.xfix.interferenceengine.tokenizer.tokens.Token;

/**
 *
 * @author Konrad Borowski
 */
public class Parser {
    private final ArrayList<Action> actions = new ArrayList<>();
    private final Stack<OperatorToken> operators = new Stack<>();
    
    public void addAction(Action action) {
        actions.add(action);
    }
    
    public Stack<OperatorToken> getStack() {
        return operators;
    }
    
    private List<Action> parseTokens(Iterable<Token> tokens) {
        for (Token token : tokens) {
            token.getParsed(this);
        }
        while (!operators.isEmpty()) {
            OperatorToken operator = operators.pop();
            if (!(operator instanceof OpenParenToken)) {
                addAction(operator.toAction());
            }
        }
        return actions;
    }
    
    public static List<Action> parse(Iterable<Token> tokens) {
        return new Parser().parseTokens(tokens);
    }
}
