package org.circedroid.core.geodesy;

public class Datum extends AbstractComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6840007721286783626L;

	public enum DatumType {

		/**
		 * Datum Géodesique
		 */
		GeodeticDatum(), VerticalDatum();

	}

	/**
	 * Identifiant du méridien d'origine.
	 */
	private String primeMeridianId;

	/**
	 * Identifiant de l'ellipsoïde.
	 */
	private String ellipsoidId;

	private DatumType type;

	public DatumType getType() {
		return type;
	}

	public void setType(DatumType type) {
		this.type = type;
	}

	public String getPrimeMeridianId() {
		return primeMeridianId;
	}

	public void setPrimeMeridianId(String primeMeridianId) {
		this.primeMeridianId = primeMeridianId;
	}

	public String getEllipsoidId() {
		return ellipsoidId;
	}

	public void setEllipsoidId(String ellipsoidId) {
		this.ellipsoidId = ellipsoidId;
	}

	@Override
	public String toString() {
		return "Datum [id=" + id + ", name=" + name + ", primeMeridianId="
				+ primeMeridianId + ", ellipsoidId=" + ellipsoidId + ", type="
				+ type + "]";
	}

	public static class DatumParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5257402536946615371L;

		public DatumParseException(String message) {
			super(message);
		}
	}

	public void check() throws DatumParseException {
		if (null == id) {
			throw new DatumParseException("Id null" + this);
		}
		if (null == name) {
			throw new DatumParseException("Name null" + this);
		}
		if (null == type) {
			throw new DatumParseException("Type null" + this);
		}
		if (type == DatumType.GeodeticDatum && null == primeMeridianId) {
			throw new DatumParseException("PrimeMeridian null" + this);
		}
		if (type == DatumType.GeodeticDatum && null == ellipsoidId) {
			throw new DatumParseException("Ellipsoid null" + this);
		}
	}

}
