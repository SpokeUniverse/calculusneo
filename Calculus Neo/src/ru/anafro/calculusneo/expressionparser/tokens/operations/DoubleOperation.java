package org.anafro.calculusneo.expressionparser.tokens.operations;

import org.anafro.calculusneo.expressionparser.tokens.NumberLiteral;

@FunctionalInterface
public interface DoubleOperation extends BasicOperation {
	public NumberLiteral operate(NumberLiteral a, NumberLiteral b);
}
