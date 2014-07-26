package org.circedroid.core.value;

import java.io.Serializable;

/**
 * Bbox
 * 
 * Implémentation des domaines de validité.
 * 
 * @author thibbo
 *
 */

public class Bbox implements Serializable{

	/**
	 * Longitude maximum 
	 */
	private double east = 180.0;
	
	/**
	 * Longitude minimum
	 */
	private double west = -180.0;
	
	/**
	 * Latitude minimum
	 */
	private double south = -90.0;
	
	/**
	 * Latitude maximum
	 */
	private double north = 90.0;

	@Override
	public String toString() {
		return "Bbox [west=" + west + ", south=" + south + ", east=" + east
				+ ", north=" + north + "]";
	}

	public double getEast() {
		return east;
	}

	public void setEast(double east) {
		this.east = east;
	}

	public double getWest() {
		return west;
	}

	public void setWest(double west) {
		this.west = west;
	}

	public double getSouth() {
		return south;
	}

	public void setSouth(double south) {
		this.south = south;
	}

	public double getNorth() {
		return north;
	}

	public void setNorth(double north) {
		this.north = north;
	}

}
