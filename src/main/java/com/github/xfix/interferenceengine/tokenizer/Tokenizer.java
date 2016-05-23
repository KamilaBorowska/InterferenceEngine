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
package com.github.xfix.interferenceengine.tokenizer;

import com.github.xfix.interferenceengine.tokenizer.factory.VariableTokenFactory;
import com.github.xfix.interferenceengine.tokenizer.factory.OpenParenTokenFactory;
import com.github.xfix.interferenceengine.tokenizer.factory.TrueTokenFactory;
import com.github.xfix.interferenceengine.tokenizer.factory.AndTokenFactory;
import com.github.xfix.interferenceengine.tokenizer.factory.FalseTokenFactory;
import com.github.xfix.interferenceengine.tokenizer.factory.OrTokenFactory;
import com.github.xfix.interferenceengine.tokenizer.factory.NotTokenFactory;
import com.github.xfix.interferenceengine.tokenizer.factory.CloseParenTokenFactory;
import com.github.xfix.interferenceengine.tokenizer.tokens.Token;
import com.github.xfix.interferenceengine.tokenizer.factory.TokenFactory;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Konrad Borowski
 */
public class Tokenizer implements Iterator<Token>, Iterable<Token> {

    private final TokenFactory[] patterns;
    private final String input;
    private Token nextElement;
    private boolean nextExists = false;
    private int currentPosition = 0;

    private static final Pattern WHITESPACE = Pattern.compile("\\G\\s+");

    private static final TokenFactory[] PATTERNS = {
        new AndTokenFactory(),
        new CloseParenTokenFactory(),
        new NotTokenFactory(),
        new OpenParenTokenFactory(),
        new OrTokenFactory(),
        new FalseTokenFactory(),
        new TrueTokenFactory(),
        new VariableTokenFactory(),
    };

    private Tokenizer(TokenFactory[] patterns, String input) {
        this.patterns = patterns;
        this.input = input;
    }

    public static Iterable<Token> tokenize(String input) {
        return new Tokenizer(PATTERNS, input);
    }

    @Override
    public boolean hasNext() {
        if (nextExists) {
            return true;
        }
        try {
            nextElement = next();
            nextExists = true;
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public Token next() throws NoSuchElementException {
        // This method is a really lazy implementation next method
        // in iterators.
        if (nextExists) {
            nextExists = false;
            Token current = nextElement;
            // Help GC a bit
            nextElement = null;
            return current;
        }
        return getNextToken();
    }

    private Token getNextToken() throws NoSuchElementException {
        final Matcher whiteSpaceMatcher = WHITESPACE.matcher(input);
        whiteSpaceMatcher.useAnchoringBounds(false);
        if (whiteSpaceMatcher.find(currentPosition)) {
            currentPosition = whiteSpaceMatcher.end();
        }
        if (currentPosition == input.length()) {
            throw new NoSuchElementException();
        }
        for (TokenFactory factory : patterns) {
            final Matcher matcher = factory.getPattern().matcher(input);
            if (!matcher.find(currentPosition)) {
                continue;
            }
            currentPosition = matcher.end();
            return factory.getToken(matcher);
        }
        throw new NullPointerException();
    }

    @Override
    public Iterator<Token> iterator() {
        return this;
    }
}
