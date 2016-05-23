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
package com.github.xfix.interferenceengine.tokenizer.factory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.xfix.interferenceengine.tokenizer.tokens.OrToken;


public class OrTokenFactory implements TokenFactory {
    private static final Pattern PATTERN = Pattern.compile("\\G\\|\\|");
    
    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    @Override
    public OrToken getToken(Matcher matches) {
        return new OrToken();
    }
}
