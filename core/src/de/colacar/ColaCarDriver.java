package de.colacar;

import de.colacar.driver.DriverPool;
import de.colacar.util.Constants;

public class ColaCarDriver {

	public static void main(String[] args) {
		DriverPool driverPool = new DriverPool(Constants.NUMBER_OF_CARS);
		driverPool.startAllDrivers();
	}
}
