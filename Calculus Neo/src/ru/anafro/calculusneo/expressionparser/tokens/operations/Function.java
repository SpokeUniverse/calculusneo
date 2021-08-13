package org.anafro.calculusneo.expressionparser.tokens.operations;

@FunctionalInterface
public interface Function extends BasicOperation {
	public double operate(double... values);
}
