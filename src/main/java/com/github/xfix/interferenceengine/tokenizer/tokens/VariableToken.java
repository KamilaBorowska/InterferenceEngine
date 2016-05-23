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
package com.github.xfix.interferenceengine.tokenizer.tokens;

import java.util.Objects;
import java.util.Stack;
import com.github.xfix.interferenceengine.expression.Parser;
import com.github.xfix.interferenceengine.expression.Variable;

/**
 *
 * @author Konrad Borowski
 */
public class VariableToken implements Token {

    private final String name;
    
    public VariableToken(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VariableToken other = (VariableToken) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void getParsed(Parser parser) {
        parser.addAction(Variable.named(this));
        Stack<OperatorToken> stack = parser.getStack();
        while (!stack.empty() && stack.peek() instanceof UnaryToken) {
            OperatorToken token = stack.pop();
            parser.addAction(token.toAction());
        }
    }
}
