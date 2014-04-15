package de.colacar;

import java.rmi.RemoteException;
import java.util.List;

import org.openspaces.core.GigaSpace;

import de.colacar.car.Car;
import de.colacar.car.CarPosition;
import de.colacar.roxel.Roxel;
import de.colacar.util.Constants;
import de.colacar.util.GigaSpaceConnector;
import de.colacar.util.Utility;

public class ColaCarGigaspace {

	private static GigaSpace gs;
	private static List<Roxel> roxels;
	private static List<Car> cars;
	private static List<CarPosition> carPositions;
	
	public static void main(String[] args) {
		System.out.println("Connect to gigaspace.");
		gs = GigaSpaceConnector.getGigaSpace();
		
		try {
			System.out.println("Clean gigaspace.");
			gs.getSpace().clean();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		System.out.println("Create roxels.");
		roxels = Utility.createRoxels(Constants.BLOCK_SPREAD, Constants.BLOCK_SIZE);
		System.out.println("Create cars.");
		cars = Utility.createCars(Constants.NUMBER_OF_CARS);
		System.out.println("Create car positions.");
		carPositions = Utility.createCarPositions(roxels, cars);
		System.out.println("Add " + roxels.size() + " roxels to gigaspace.");
		gs.writeMultiple(roxels.toArray());
		System.out.println("Add " + cars.size() + " cars to gigaspace.");
		gs.writeMultiple(cars.toArray());
		System.out.println("Add " + carPositions.size() + " car positions to gigaspace.");
		gs.writeMultiple(carPositions.toArray());
		System.out.println("Done.");
	}
}