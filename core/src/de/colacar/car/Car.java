package de.colacar.car;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceId;

import de.colacar.util.Direction;

public class Car implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer positionX, positionY;
	private Direction direction;

	public Car() {
	}

	public Car(Integer id, Integer positionX, Integer positionY, Direction direction) {
        this.id = id;
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
		return "CarImpl [id=" + id + ", positionX=" + positionX
				+ ", positionY=" + positionY + ", direction=" + direction + "]";
	}
}
