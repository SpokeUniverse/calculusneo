package org.anafro.calculusneo.expressionparser.tokens;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class LiteralProcessor<T> {
	public static interface LiteralDetector { public boolean isLiteral(String literal); }
	public static interface LiteralCastingMachine<T extends AbstractLiteral> { public T cast(String literal); }
	
	private static final ArrayList<LiteralProcessor> processors = new ArrayList<>();
	private final LiteralDetector detector;
	private final LiteralCastingMachine<AbstractLiteral<T>> castMachine;
	private boolean detected = false;
	public LiteralProcessor(LiteralDetector detector, LiteralCastingMachine<AbstractLiteral<T>> castMachine) {
		this.detector = detector;
		this.castMachine = castMachine;
		processors.add(this);
	}
	public LiteralDetector getDetector() {
		return detector;
	}
	public LiteralCastingMachine<AbstractLiteral<T>> getMachine() {
		return castMachine;
	}
	public static ArrayList<LiteralProcessor> getProcessors() {
		return processors;
	}
	public AbstractLiteral<T> detect(String maybeLiteral) {
		if(detector.isLiteral(maybeLiteral)) {
			detected();
			return castMachine.cast(maybeLiteral); 
		} else {
			flushDetection();
			return null;
		}
	}
	public void detected() {
		detected = true;
	}
	public void flushDetection() {
		detected = false;
	}
	public boolean isDetected() {
		return detected;
	}
}
