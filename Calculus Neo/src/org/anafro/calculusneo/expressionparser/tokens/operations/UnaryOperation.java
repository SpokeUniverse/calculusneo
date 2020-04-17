package org.anafro.calculusneo.expressionparser.tokens.operations;

@FunctionalInterface
public interface UnaryOperation extends BasicOperation {
	public double operate(double a);
}
