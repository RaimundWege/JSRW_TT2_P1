package de.colacar.util;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public enum Direction {
	NORTH, EAST, SOUTH, WEST, BLOCKED, TODECIDE
}
