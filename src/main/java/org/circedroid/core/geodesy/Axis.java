package org.circedroid.core.geodesy;

public class Axis extends AbstractComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1351052529230939615L;

	public enum UoM {
		Sexa("EPSG:9121"), Meter("IGNF:m"), Grad("IGNF:gr"), Deg("IGNF:deg");

		private String uom;

		private UoM(String uom) {
			this.uom = uom;
		}

		public boolean equals(String uomtotest) {
			return this.uom.equals(uomtotest);
		}

	}

	private String abbrev;
	private UoM uom;

	public String getAbbrev() {
		return abbrev;
	}

	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}

	public UoM getUom() {
		return uom;
	}

	public void setUom(UoM uom) {
		this.uom = uom;
	}

	public static class AxisParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7998013584900119647L;

		public AxisParseException(String message) {
			super(message);
		}

	}

	public void check() throws AxisParseException {
		if (null == id) {
			throw new AxisParseException("Id null" + this);
		}
		if (null == abbrev) {
			throw new AxisParseException("Abbrev null" + this);
		}
		if (null == uom) {
			throw new AxisParseException("UoM null" + this);
		}
	}

	@Override
	public String toString() {
		return "Axis [id=" + id + ", abbrev=" + abbrev + ", uom=" + uom + "]";
	}

}
