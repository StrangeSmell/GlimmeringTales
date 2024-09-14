package dev.xkmc.glimmeringtales.content.research.logic;

public abstract class LocateResult {

	public abstract ResultType getType();

	public abstract double getX();

	public abstract double getY();

	public enum ResultType {
		CELL, ARROW
	}

}
