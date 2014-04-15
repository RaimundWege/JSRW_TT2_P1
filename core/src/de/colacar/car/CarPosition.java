package de.colacar.car;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceId;

public class CarPosition implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer carId;
	private Float positionX, positionY;
    private Float rotationY;
    
    public CarPosition() {}
    
    public CarPosition(Integer id, Integer carId, Float positionX, Float positionY, Float rotationY) {
    	this.id = id;
        this.carId = carId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.rotationY = rotationY;
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

	public Float getPositionX() {
		return positionX;
	}

	public void setPositionX(Float positionX) {
		this.positionX = positionX;
	}

	public Float getPositionY() {
		return positionY;
	}

	public void setPositionY(Float positionY) {
		this.positionY = positionY;
	}

	public Float getRotationY() {
		return rotationY;
	}

	public void setRotationY(Float rotationY) {
		this.rotationY = rotationY;
	}

	@Override
	public String toString() {
		return "CarPosition [id=" + id + ", carId=" + carId + ", positionX="
				+ positionX + ", positionY=" + positionY + ", rotationY="
				+ rotationY + "]";
	}
}
