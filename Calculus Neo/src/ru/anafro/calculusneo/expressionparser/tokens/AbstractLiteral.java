package org.anafro.calculusneo.expressionparser.tokens;

public abstract class AbstractLiteral<T> extends AbstractToken {
	private T meaning;
	
	public AbstractLiteral(T meaning) {
		this.meaning = meaning;
	}
	public T getMeaning() {
		return meaning;
	}
	public void setMeaning(T meaning) {
		this.meaning = meaning;
	}
	@Override
	public String toString() {
		return "@Literal: " + meaning.toString();
	}
}
