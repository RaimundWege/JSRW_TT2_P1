package de.colacar.roxel;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceId;

import de.colacar.util.Direction;

public class Roxel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer carId;
	private Integer positionX, positionY;
	private Direction direction;

	public Roxel() {
	}

	public Roxel(Integer id, Integer carId, Integer positionX,
			Integer positionY, Direction direction) {
		this.id = id;
		this.carId = carId;
		this.positionX = positionX;
		this.positionY = positionY;
		this.direction = direction;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}
	
	public Integer getPositionX() {
		return positionX;
	}

	public void setPositionX(Integer positionX) {
		this.positionX = positionX;
	}

	public Integer getPositionY() {
		return positionY;
	}

	public void setPositionY(Integer positionY) {
		this.positionY = positionY;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "Roxel [id=" + id + ", carId=" + carId + ", positionX="
				+ positionX + ", positionY=" + positionY + ", direction="
				+ direction + "]";
	}
}
