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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import com.github.xfix.interferenceengine.expression.Expression;
import com.github.xfix.interferenceengine.expression.Variable;
import com.github.xfix.interferenceengine.tokenizer.tokens.VariableToken;

/**
 *
 * @author Konrad Borowski
 */
class RuleParser {
    public static ArrayList<Variable> parseRules(List<Rule> rules) {
        HashMap<String, Variable> existingVariables = new HashMap<>();
        ArrayList<Variable> variables = new ArrayList<>();
        for (Rule rule : rules) {
            String variableName = rule.getVariableName();
            Optional<Variable> retrievedVariable = Optional.ofNullable(existingVariables.get(variableName));
            if (!retrievedVariable.isPresent()) {
                final Variable newVariable = Variable.named(new VariableToken(variableName));
                existingVariables.put(variableName, newVariable);
                variables.add(newVariable);
                retrievedVariable = Optional.of(newVariable);
            }
            Expression expression = Expression.fromCode(rule.getExpression());
            retrievedVariable.get().setExpression(expression, rule.getNegated());
        }
        return variables;
    }
}
