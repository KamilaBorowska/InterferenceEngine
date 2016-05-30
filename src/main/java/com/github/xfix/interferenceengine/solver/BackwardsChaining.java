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
import com.github.xfix.interferenceengine.expression.Expression;
import com.github.xfix.interferenceengine.expression.Variable;
import java.util.ArrayList;

/**
 *
 * @author Konrad Borowski
 */
public class BackwardsChaining implements Solver {

    @Override
    public void solve(List<Variable> rulesList) {
        ArrayList<Variable> rules = new ArrayList<>(rulesList);
        for (Variable variable : rules) {
            if (variable.isKnown()) {
                continue;
            }
            boolean[] statuses = {false, true};
            for (boolean status : statuses) {
                Optional<Expression> expression = variable.getExpression(status);
                if (expression.isPresent()) {
                    ArrayList<Variable> dependencies = expression.get().getDependencies();
                    try {
                        bruteforce(variable, dependencies, 0, status);
                    } catch (CannotProveException e) {
                        // Oh well.
                    }
                    variable.setChecked(false);
                }
            }
        }
    }

    private void bruteforce(Variable variable, ArrayList<Variable> dependencies,
            int currentDependency, boolean negative) throws CannotProveException {
        if (currentDependency == dependencies.size()) {
            boolean result = variable.check(negative);
            if (variable.isChecked() && variable.getValue() != result) {
                variable.setKnown(false);
                throw new CannotProveException();
            } else if (result) {
                variable.set(!negative);
            }
            variable.setChecked(true);
            return;
        }
        Variable dependency = dependencies.get(currentDependency);
        // If it's known, let's not bruteforce then.
        if (dependency.isKnown()) {
            bruteforce(variable, dependencies, currentDependency + 1, negative);
        } else {
            boolean[] statuses = {false, true};
            for (boolean status : statuses) {
                dependency.setValue(status);
                bruteforce(variable, dependencies, currentDependency + 1, negative);
            }
        }
    }

    private static class CannotProveException extends Exception {
        public CannotProveException() {
        }
    }
}
