package org.anafro.calculusneo.expressionparser.tokens;

import java.util.ArrayList;
import org.anafro.calculusneo.expressionparser.tokens.operations.BasicOperation;

@SuppressWarnings("rawtypes")
public abstract class AbstractOperator<T extends BasicOperation> extends AbstractToken implements Comparable<AbstractOperator<T>>{
	public static final class Priority {
		public static final int 
			LOW = 1,
			MEDIUM = 2,
			HIGH = 3,
			HIGHEST = 4;
	}
	public static ArrayList<AbstractOperator> operators = new ArrayList<>();
	protected final char face;
	protected final T operation;
	protected int priority;
	protected AbstractOperator(char face, T operation, int priority) {
		this.face = face;
		this.operation = operation;
		this.priority = priority;
		operators.add(this);
	}
	public static ArrayList<AbstractOperator> getOperators() {
		return operators;
	}
	public T getOperation() {
		return operation;
	}
	public char getFace() {
		return face;
	}
	public int getPriority() {
		return priority;
	}
	public AbstractOperator<T> withPriorityBoost(int boost) {
		this.priority *= boost;
		return this;
	}
	@Override
	public String toString() {
		return "@Operator '" + face + "' with priority " + priority;
	}
	@Override
	public int compareTo(AbstractOperator<T> o) {
		return (int) Math.signum(priority - o.priority);
	}
	public abstract void fire(ArrayList<AbstractToken> parsedExpression, int selfPosition);
}
