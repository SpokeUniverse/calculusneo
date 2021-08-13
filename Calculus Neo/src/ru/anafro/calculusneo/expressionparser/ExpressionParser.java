package org.anafro.calculusneo.expressionparser;

import static org.anafro.calculusneo.expressionparser.tokens.AbstractOperator.Priority.HIGH;
import static org.anafro.calculusneo.expressionparser.tokens.AbstractOperator.Priority.LOW;
import static org.anafro.calculusneo.expressionparser.tokens.AbstractOperator.Priority.MEDIUM;

import java.util.ArrayList;

import org.anafro.calculusneo.expressionparser.tokens.AbstractOperator;
import org.anafro.calculusneo.expressionparser.tokens.AbstractToken;
import org.anafro.calculusneo.expressionparser.tokens.Constant;
import org.anafro.calculusneo.expressionparser.tokens.DoubleOperator;
import org.anafro.calculusneo.expressionparser.tokens.LiteralProcessor;
import org.anafro.calculusneo.expressionparser.tokens.NumberLiteral;

public class ExpressionParser {
	public static class MaltypedExpressionException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		private String message;
		public MaltypedExpressionException(String message) {
			this.message = message;
		}
		@Override
		public String getMessage() {
			return message;
		}
	}
	private static boolean debugMode = false;
	public static double parse(String expression) {
		expression += ' ';
		final ArrayList<AbstractToken> tokens = new ArrayList<>();
		StringBuilder buffer = new StringBuilder();
		int priorityBoost = 1;
		log("Начинаю работу...");
		for(char c : expression.toCharArray()) {
			log("Текущий символ: '" + c + "', содержание буфера: '" + buffer.toString() + "'");
			//Поиск литералов
			log("Начинаю поиск литералов");
			for(LiteralProcessor<?> processor : LiteralProcessor.getProcessors()) {
				//Если после добавления нового символа литерал потерял смысл, то добавляем литерал в список токенов
				if(processor.getDetector().isLiteral(buffer.toString()) && !processor.getDetector().isLiteral(buffer.toString() + c)) {
					log("Литерал обнаружен: '" + buffer.toString() + "', добавляю его в список литералов и очищаю буфер");
					tokens.add(processor.detect(buffer.toString()));
					buffer.setLength(0);
					break;
				} else {
					log("Литерал не выявлен, перехожу к следующему");
				}
			}
			//Поиск операторов
			boolean operatorHasDetected = false;
			log("Начинаю поиск операторов...");
			for(AbstractOperator<?> operator : AbstractOperator.getOperators()) {
				if(c == operator.getFace()) {
					log("Оператор обнаружен: '" + c + "', добавляю его в список токенов");
					operatorHasDetected = true;
					tokens.add(operator.withPriorityBoost(priorityBoost));
					break;
				}
			}
			if(c == '(') {
				priorityBoost++;
				log("Обнаружена открывающая скобка. Увеличивается приоритет последующих операторов (сейчас " + priorityBoost + ")");
			} else if(c == ')') {
				if(priorityBoost == 1) throw new MaltypedExpressionException("Used ')' without '('");
				priorityBoost--;
				log("Обнаружена закрывающая скобка. Снижается приоритет последующих операторов (сейчас " + priorityBoost + ")");
			}
			if(operatorHasDetected || c == ' ' || c == '(' || c == ')') continue;
			buffer.append(c);
		}
		log("Работа завершена. Были обнаружены следующие токены: ");
		for(AbstractToken token : tokens) log(token.toString());
		
		log("Начинаю считать!");
		while(true) {
			int selfPosition = -1;
			int maxPriority = -1;
			AbstractOperator<?> theMostPrioritiestOperator = null;
			for(int i = 0; i < tokens.size(); i++) {
				if(tokens.get(i) instanceof AbstractOperator<?>) {
					AbstractOperator<?> maybe = (AbstractOperator<?>) tokens.get(i);
					if(maybe.getPriority() > maxPriority) {
						selfPosition = i;
						maxPriority = maybe.getPriority();
						theMostPrioritiestOperator = maybe;
					}
				}
			}
			if(theMostPrioritiestOperator == null) break;
			theMostPrioritiestOperator.fire(tokens, selfPosition);
		}
		if(tokens.size() != 1 || !(tokens.get(0) instanceof NumberLiteral)) throw new MaltypedExpressionException("Expression is wrong");
		return ((NumberLiteral) tokens.get(0)).getMeaning();
	}
	/**
	 * There's all operators were written.
	 */
	public static void init() {
		new DoubleOperator('+', (a, b) -> new NumberLiteral(a.getMeaning() + b.getMeaning()), LOW);
		new DoubleOperator('-', (a, b) -> new NumberLiteral(a.getMeaning() - b.getMeaning()), LOW);
		new DoubleOperator('*', (a, b) -> new NumberLiteral(a.getMeaning() * b.getMeaning()), MEDIUM);
		new DoubleOperator('/', (a, b) -> new NumberLiteral(a.getMeaning() / b.getMeaning()), MEDIUM);
		new DoubleOperator('%', (a, b) -> new NumberLiteral(a.getMeaning() % b.getMeaning()), MEDIUM);
		new DoubleOperator('^', (a, b) -> new NumberLiteral(Math.pow(a.getMeaning(), b.getMeaning())), HIGH);
		
		new LiteralProcessor<Double>(string -> {try {Double.parseDouble(string); return !string.contains(" "); } catch (Exception e) { return false; } }, string -> new NumberLiteral(Double.parseDouble(string)));
		
		new Constant("pi", Math.PI);
		new Constant("e", Math.E);
		new Constant("tau", Math.PI * 2);
		new Constant("infinity", Double.POSITIVE_INFINITY);
		new Constant("nan", Double.NaN);
	}
	public static void setDebugMode(boolean debugMode) {
		ExpressionParser.debugMode = debugMode;
	}
	private static void log(String message) {
		if(debugMode) System.out.println("Режим отладки: " + message);
	}
}
