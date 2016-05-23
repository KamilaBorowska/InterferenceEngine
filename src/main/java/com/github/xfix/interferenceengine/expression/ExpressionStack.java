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

import java.util.Stack;

/**
 *
 * @author Konrad Borowski
 * @param <T> Stack type
 */
public class ExpressionStack<T> extends Stack<T> {
    public Object[] popCount(int count) {
        final Object[] result = new Object[count];
        for (int i = 0; i < count; i++) {
            result[count - i - 1] = pop();
        }
        return result;
    }
}
