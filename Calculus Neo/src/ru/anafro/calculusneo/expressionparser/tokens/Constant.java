package org.anafro.calculusneo.expressionparser.tokens;

public class Constant extends NumberLiteral {
	
	private final String face;
	public Constant(String face, Double meaning) {
		super(meaning);
		this.face = face;
		new LiteralProcessor<>(face::equals, string -> this);
	}
	public String getFace() {
		return face;
	}
	@Override
	public String toString() {
		return super.toString() + " is @Constant " + face;
	}
}
