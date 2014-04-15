package de.colacar.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.colacar.car.Car;
import de.colacar.car.CarPosition;
import de.colacar.roxel.Roxel;

public class Utility {

	private static int currentId;

	private Utility() {
	}

	/**
	 * Generates a new space id
	 * 
	 * @return
	 */
	private static synchronized Integer getNewId() {
		return currentId++;
	}

	/**
	 * Creates an empty roxel with an id
	 * 
	 * @param positionX
	 * @param positionY
	 * @param direction
	 * @param isCrossroad
	 * @return
	 */
	public static Roxel createRoxel(int positionX, int positionY,
			Direction direction) {
		return new Roxel(getNewId(), -1, positionX, positionY, direction);
	}

	/**
	 * Creates a list of roxels
	 * 
	 * @param worldSizeX
	 * @param worldSizeY
	 * @param blockSize
	 * @return
	 */
	public static List<Roxel> createRoxels(int blockSpread, int blockSize) {
		List<Roxel> result = new LinkedList<Roxel>();

		// Structure of one block
		// _._._._._
		// |_|_|_|v|^|
		// |_|_|_|v|^|
		// |_|_|_|v|^|
		// |<|<|<|+|+|
		// |>|>|>|+|+|

		for (int x = 0; x < blockSpread; x++) {
			for (int y = 0; y < blockSpread; y++) {
				int positionX = x * blockSize;
				int positionY = y * blockSize;

				// Horizontal road in both directions
				for (int xx = 0; xx < blockSize - 2; xx++) {
					result.add(createRoxel(positionX + xx, positionY
							+ (blockSize - 2), Direction.WEST));
					result.add(createRoxel(positionX + xx, positionY
							+ (blockSize - 1), Direction.EAST));
				}

				// Vertical road in both directions
				for (int yy = 0; yy < blockSize - 2; yy++) {
					result.add(createRoxel(positionX + (blockSize - 2),
							positionY + yy, Direction.NORTH));
					result.add(createRoxel(positionX + (blockSize - 1),
							positionY + yy, Direction.SOUTH));
				}

				// Crossroad
				result.add(createRoxel(positionX + (blockSize - 2), positionY
						+ (blockSize - 2), Direction.TODECIDE));
				result.add(createRoxel(positionX + (blockSize - 2), positionY
						+ (blockSize - 1), Direction.TODECIDE));
				result.add(createRoxel(positionX + (blockSize - 1), positionY
						+ (blockSize - 2), Direction.TODECIDE));
				result.add(createRoxel(positionX + (blockSize - 1), positionY
						+ (blockSize - 1), Direction.TODECIDE));
			}
		}
		return result;
	}

	/**
	 * Creates a list of cars
	 * 
	 * @param numberOfCars
	 * @return
	 */
	public static List<Car> createCars(int numberOfCars) {
		List<Car> result = new ArrayList<Car>();
		for (int i = 0; i < numberOfCars; i++) {
			result.add(createCar());
		}
		return result;
	}

	/**
	 * Creates a car
	 * 
	 * @return
	 */
	public static Car createCar() {
		return new Car(getNewId(), -1, -1, Direction.TODECIDE);
	}

	/**
	 * Creates car positions
	 * 
	 * @param roxels
	 * @param cars
	 * @return
	 */
	public static List<CarPosition> createCarPositions(List<Roxel> roxels,
			List<Car> cars) {
		List<CarPosition> result = new ArrayList<CarPosition>();
		List<Roxel> freeRoxels = new ArrayList<Roxel>(roxels);
		Random rand = new Random();
		for (Car car : cars) {
			Roxel freeRoxel = null;
			while (freeRoxel == null && freeRoxels.size() > 0) {
				freeRoxel = freeRoxels
						.remove(rand.nextInt(freeRoxels.size()));
				if (freeRoxel.getDirection() != Direction.TODECIDE) {
					freeRoxel.setCarId(car.getId());
					car.setPositionX(freeRoxel.getPositionX());
					car.setPositionY(freeRoxel.getPositionY());
					car.setDirection(freeRoxel.getDirection());
					result.add(createCarPosition(car));
				} else {
					freeRoxel = null;
				}
			}
		}
		return result;
	}
	
	public static CarPosition createCarPosition(Car car) {
		float positionX = car.getPositionX().floatValue();
		float positionY = car.getPositionY().floatValue();
		float rotationY = 0f;
		if (car.getDirection() == Direction.SOUTH) {
			rotationY = 180f;
		} else if (car.getDirection() == Direction.EAST) {
			rotationY = 90f;
		} else if (car.getDirection() == Direction.WEST) {
			rotationY = -90f;
		}
		return new CarPosition(getNewId(), car.getId(), positionX, positionY, rotationY);
	}
}
