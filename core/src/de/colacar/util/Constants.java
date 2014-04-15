package de.colacar.util;

public class Constants {

	private Constants() {}
	
	public static final int BLOCK_SPREAD = 3;
	
	public static final int BLOCK_SIZE = 5;

	public static final int NUMBER_OF_CARS = 40;
	
	public static final int TRAVEL_TIME = 2000;
	
	public static int MAP_SIZE() {
		return BLOCK_SPREAD * BLOCK_SIZE;
	}
	
	public static int MAP_SIZE_HALF() {
		return MAP_SIZE() / 2;
	}
}
