package org.anafro.calculusneo;

import java.util.Scanner;

import org.anafro.calculusneo.expressionparser.ExpressionParser;

public class Main {
	private static final Scanner consoleIn = new Scanner(System.in);

	public static void main(String[] args) {
		ExpressionParser.init();
		while (true) {
			System.out.print("Type an expression or 'exit': ");
			String expression = consoleIn.nextLine();
			if(expression.equals("exit")) break;
			System.out.println("The result is: " + ExpressionParser.parse(expression));
		}
		consoleIn.close();
	}
}
