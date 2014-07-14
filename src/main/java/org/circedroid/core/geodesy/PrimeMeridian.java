package org.circedroid.core.geodesy;

import org.circedroid.core.value.Angle;

public class PrimeMeridian extends AbstractComponent {

	private static final long serialVersionUID = 265396655126471787L;

	private Angle greenwichLongitude;

	public Angle getGreenwichLongitude() {
		return greenwichLongitude;
	}

	public void setGreenwichLongitude(Angle greenwichLongitude) {
		this.greenwichLongitude = greenwichLongitude;
	}

	@Override
	public String toString() {
		return "PrimeMeridian [id=" + id + ", name=" + name
				+ ", greenwichLongitude=" + greenwichLongitude + "]";
	}

	public static class PrimeMeridianParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2929412001611904824L;

		public PrimeMeridianParseException(String message) {
			super(message);
		}

	}

	public void check() throws PrimeMeridianParseException {
		if (null == id) {
			throw new PrimeMeridianParseException("Id null" + this);
		}
		if (null == name) {
			throw new PrimeMeridianParseException("Name null" + this);
		}
		if (null == greenwichLongitude) {
			throw new PrimeMeridianParseException("PrimeMeridian null" + this);
		}
	}

}
