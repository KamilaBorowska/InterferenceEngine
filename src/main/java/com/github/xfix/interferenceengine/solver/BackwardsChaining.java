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
package com.github.xfix.interferenceengine.solver;

import java.util.List;
import java.util.Optional;
import com.github.xfix.interferenceengine.RuleTable;
import com.github.xfix.interferenceengine.expression.Expression;
import com.github.xfix.interferenceengine.expression.Variable;

/**
 *
 * @author Konrad Borowski
 */
public class BackwardsChaining implements Solver {

    private boolean didSomething = true;
    private final List<Variable> rules;

    public BackwardsChaining(List<Variable> rules) {
        this.rules = rules;
    }

    @Override
    public void solve() {
        while (didSomething) {
            didSomething = false;
            for (Variable rule : rules) {
                check(rule, true);
                check(rule, false);
            }
        }
    }

    private void check(Variable variable, boolean negated) {
        Optional<Expression> expression = variable.getExpression(negated);
        if (!variable.isKnown() && expression.isPresent() && expression.get().getDependencies().isEmpty() && variable.check(negated)) {
            variable.set(!negated);
            didSomething = true;
        }
    }
}
