package de.colacar.driver;

import java.util.ArrayList;
import java.util.List;

public class DriverPool {

	private List<Thread> driverList;
	
	public DriverPool(int numberOfDriver) {
        driverList = new ArrayList<Thread>();
        for (int i = 0; i < numberOfDriver; i++) {
            Thread driver = new Driver();
            driverList.add(driver);
        }
    }

    public void startAllDrivers(){
        for (Thread thread : driverList) {
            thread.start();
        }
    }

    public void stopAllDrivers(){
        for (Thread thread : driverList) {
            thread.interrupt();
        }
    }
}
