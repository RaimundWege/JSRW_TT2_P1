package de.colacar.driver;

import org.openspaces.core.GigaSpace;

import com.gigaspaces.client.ChangeSet;
import com.j_spaces.core.client.SQLQuery;

import de.colacar.car.Car;
import de.colacar.car.CarPosition;
import de.colacar.roxel.Roxel;
import de.colacar.util.Constants;
import de.colacar.util.GigaSpaceConnector;

public class Driver extends Thread {

	private GigaSpace gs;
	private Car car;
	private float startX, startY, endX, endY;

	public Driver() {
		gs = GigaSpaceConnector.getGigaSpace();
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			takeCar();
			moveCar();
			updateCarPosition(startX, startY);
			long endTime = System.currentTimeMillis() + Constants.TRAVEL_TIME;
			while (System.currentTimeMillis() < endTime) {
				float factor = (float)(endTime - System.currentTimeMillis()) / Constants.TRAVEL_TIME;
				float positionX = (startX * factor) + (endX * (1f - factor));
				float positionY = (startY * factor) + (endY * (1f - factor));
				updateCarPosition(positionX, positionY);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			updateCarPosition(endX, endY);
			releaseCar();
		}
	}

	private void takeCar() {
		car = gs.take(new Car());
		System.out.println("Driver " + getId() + " takes car " + car.getId());
	}

	private void moveCar() {
		switch (car.getDirection()) {
		case EAST:
			System.out.println("Driver " + this.getId() + " drives east.");
			driveEast();
			break;
		case WEST:
			System.out.println("Driver " + this.getId() + " drives west.");
			driveWest();
			break;
		case SOUTH:
			System.out.println("Driver " + this.getId() + " drives south.");
			driveSouth();
			break;
		case NORTH:
			System.out.println("Driver " + this.getId() + " drives north.");
			driveNorth();
			break;
		default:
			break;
		}
	}

	private void releaseCar() {
		gs.write(car);
		car = null;
	}

	private void driveNorth() {
		Roxel current = takeCurrentRoxel();
		int currentY = current.getPositionY();
		int nextY = (currentY < Constants.MAP_SIZE() - 1 ? currentY + 1 : 0);
		SQLQuery<Roxel> sql = new SQLQuery<Roxel>(Roxel.class,
				"carId = ? AND positionY = ? AND positionX = ?", -1, nextY,
				current.getPositionX());
		Roxel next = gs.take(sql, Long.MAX_VALUE);

		startX = current.getPositionX();
		startY = current.getPositionY();
		endX = startX;
		endY = startY + 1;

		changePosition(current, next);
		releaseRoxel(current, next);
	}

	private void driveSouth() {
		Roxel current = takeCurrentRoxel();
		int currentY = current.getPositionY();
		int nextY = (currentY > 0 ? currentY - 1 : Constants.MAP_SIZE() - 1);
		SQLQuery<Roxel> sql = new SQLQuery<Roxel>(Roxel.class,
				"carId = ? AND positionY = ? AND positionX = ?", -1, nextY,
				current.getPositionX());
		Roxel next = gs.take(sql, Long.MAX_VALUE);

		startX = current.getPositionX();
		startY = current.getPositionY();
		endX = startX;
		endY = startY - 1;

		changePosition(current, next);
		releaseRoxel(current, next);
	}

	private void driveWest() {
		Roxel current = takeCurrentRoxel();
		int currentX = current.getPositionX();
		int nextX = (currentX > 0 ? currentX - 1 : Constants.MAP_SIZE() - 1);
		SQLQuery<Roxel> sql = new SQLQuery<Roxel>(Roxel.class,
				"carId = ? AND positionY = ? AND positionX = ?", -1,
				current.getPositionY(), nextX);
		Roxel next = gs.take(sql, Long.MAX_VALUE);

		startX = current.getPositionX();
		startY = current.getPositionY();
		endX = startX - 1;
		endY = startY;

		changePosition(current, next);
		releaseRoxel(current, next);
	}

	private void driveEast() {
		Roxel current = takeCurrentRoxel();
		int currentX = current.getPositionX();
		int nextX = (currentX < Constants.MAP_SIZE() - 1 ? currentX + 1 : 0);
		SQLQuery<Roxel> sql = new SQLQuery<Roxel>(Roxel.class,
				"carId = ? AND positionY = ? AND positionX = ?", -1,
				current.getPositionY(), nextX);
		Roxel next = gs.take(sql, Long.MAX_VALUE);

		startX = current.getPositionX();
		startY = current.getPositionY();
		endX = startX + 1;
		endY = startY;

		changePosition(current, next);
		releaseRoxel(current, next);
	}

	private Roxel takeCurrentRoxel() {
		SQLQuery<Roxel> sql = new SQLQuery<Roxel>(Roxel.class, "carId=?",
				car.getId());
		return gs.take(sql, Long.MAX_VALUE);
	}

	public Roxel readCurrentRoxel() {
		SQLQuery<Roxel> sql = new SQLQuery<Roxel>(Roxel.class, "carId=?",
				car.getId());
		return gs.read(sql, Long.MAX_VALUE);
	}

	private void changePosition(Roxel from, Roxel to) {
		from.setCarId(-1);
		to.setCarId(car.getId());
	}

	private void releaseRoxel(Roxel from, Roxel to) {
		Roxel[] updateRoxels = { from, to };
		gs.writeMultiple(updateRoxels);
	}

	private void updateCarPosition(float positionX, float positionY) {
		SQLQuery<CarPosition> sql = new SQLQuery<CarPosition>(
				CarPosition.class, "carId = ?", car.getId());
		gs.change(
				sql,
				new ChangeSet().set("positionX", positionX).set("positionY",
						positionY));
	}
}
