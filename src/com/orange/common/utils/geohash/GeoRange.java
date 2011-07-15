package com.orange.common.utils.geohash;

public class GeoRange {

	private String max;

	private String min;

	private double radius;

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "min:" + min + " ;max:" + max;
	}
}
