package org.circedroid.core.geodesy;

import java.util.ArrayList;
import java.util.List;

public class CoordinateSystem extends AbstractComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1100018847068557349L;

	public enum CSType {
		CartesianCS(), EllipsoidalCS(), VerticalCS();
	}

	private CSType type;

	private List<String> axisIds;

	public CoordinateSystem() {
		this.axisIds = new ArrayList<String>();
	}

	public CSType getType() {
		return type;
	}

	public void setType(CSType type) {
		this.type = type;
	}

	public List<String> getAxisIds() {
		return axisIds;
	}

	public void addAxisId(String axisId) {
		this.axisIds.add(axisId);
	}

	public static class CSParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2734588994404100864L;

		public CSParseException(String message) {
			super(message);
		}
	}

	public void check() throws CSParseException {
		if (null == id) {
			throw new CSParseException("Id null" + this);
		}
		if (null == name) {
			throw new CSParseException("Name null" + this);
		}
		if (null == axisIds) {
			throw new CSParseException("Axis null" + this);
		}
		if (axisIds.size() == 0) {
			throw new CSParseException("Axis empty" + this);
		}
	}

	@Override
	public String toString() {
		return "CoordinateSystem [id=" + id + ", type=" + type + ", name="
				+ name + ", axisIds=" + axisIds + "]";
	}

}
