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
package com.github.xfix.interferenceengine.parser;

import com.github.xfix.interferenceengine.Rule;
import com.github.xfix.interferenceengine.RuleTable;
import com.github.xfix.interferenceengine.VariableDescriptor;

/**
 *
 * @author Konrad Borowski
 */
public class FileParser {

    private final RuleTable table;

    public FileParser(RuleTable table) {
        this.table = table;
    }

    public void parseLine(String line, int lineNumber) throws SyntaxError {
        try {
            line = line.trim();
            if (line.equals("")) {
                return;
            }
            if (line.contains("=>")) {
                parseImplication(line);
                return;
            }
            if (line.contains("=")) {
                parseAssignment(line);
                return;
            }
            throw new UnrecognizedLineError(line);
        } catch (ParseError e) {
            throw new SyntaxError(e, lineNumber);
        }
    }

    public void startParsing() {
        table.clear();
    }

    private void parseImplication(String line) throws ParseError {
        String[] parts = line.split("=>");
        String expression = parts[0].trim();
        String variable = parts[1].trim();

        table.addRow(new Rule(new VariableDescriptor(variable, false), expression));
    }

    private static boolean getBooleanValue(String part) throws ParseError {
        switch (part.toLowerCase()) {
            case "t":
            case "true":
            case "p":
            case "prawda":
                return true;
            case "f":
            case "false":
            case "falsz":
            case "fałsz":
                return false;
            default:
                throw new UnrecognizedTruthValueError(part);
        }
    }

    private void parseAssignment(String line) throws ParseError {
        String[] parts = line.split("=");
        String variable = parts[0].trim();
        boolean value = !getBooleanValue(parts[1].trim());

        table.addRow(new Rule(new VariableDescriptor(variable, value), "1"));
    }
}
