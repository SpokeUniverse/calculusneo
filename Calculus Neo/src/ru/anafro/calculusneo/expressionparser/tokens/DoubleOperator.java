package org.anafro.calculusneo.expressionparser.tokens;

import java.util.ArrayList;

import org.anafro.calculusneo.expressionparser.ExpressionParser;
import org.anafro.calculusneo.expressionparser.tokens.operations.DoubleOperation;

public class DoubleOperator extends AbstractOperator<DoubleOperation> {

	public DoubleOperator(char face, DoubleOperation operation, int priority) {
		super(face, operation, priority);
	}

	@Override
	public void fire(ArrayList<AbstractToken> parsedExpression, int selfPosition) {
		try {
			if (selfPosition == parsedExpression.size() - 1)
				throw new ExpressionParser.MaltypedExpressionException(
						"Double operator '" + face + "' is located on the right edge of the expression");
			if (selfPosition == 0) {
				if (face != '+' && face != '-')
					throw new ExpressionParser.MaltypedExpressionException(
							"Multiplicative operator '" + face + "' was used as an unary");
				if (!(parsedExpression.get(selfPosition + 1) instanceof NumberLiteral))
					throw new ExpressionParser.MaltypedExpressionException(
							"Unary operator '" + face + "' used with a not a number");
				parsedExpression.remove(selfPosition);
				NumberLiteral argument = (NumberLiteral) parsedExpression.get(selfPosition);
				if (face == '-') argument.setMeaning(-argument.getMeaning());
				return;
			} else if (!(parsedExpression.get(selfPosition - 1) instanceof NumberLiteral) || !(parsedExpression.get(selfPosition + 1) instanceof NumberLiteral)) {
				throw new ExpressionParser.MaltypedExpressionException("Double operator '" + face + "' is located between not a numbers (or one of them)");
			}
			NumberLiteral argument1 = (NumberLiteral) parsedExpression.get(selfPosition - 1);
			NumberLiteral argument2 = (NumberLiteral) parsedExpression.get(selfPosition + 1);
			NumberLiteral result = operation.operate(argument1, argument2);
			parsedExpression.set(selfPosition, result);
			parsedExpression.remove(selfPosition + 1);
			parsedExpression.remove(selfPosition - 1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExpressionParser.MaltypedExpressionException("Unknown");
		}
	}
}
