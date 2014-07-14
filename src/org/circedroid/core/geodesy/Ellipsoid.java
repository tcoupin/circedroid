package org.circedroid.core.geodesy;

public class Ellipsoid extends AbstractComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1098352909539431783L;
	private Double a;
	private Double b;
	private Double f;
	private Double es;
	private boolean isSphere = false;

	public Double getA() {
		return a;
	}

	public void setA(Double a) {
		this.a = a;
	}

	public Double getB() {
		if (this.b == null) {
			if (this.isSphere) {
				this.b = this.a;
			} else {
				this.b = this.a * (1.0 - 1.0 / getF());
			}
		}
		return b;
	}

	public void setB(Double b) {
		this.b = b;
	}

	public Double getF() {
		if (this.f == null) {
			this.f = this.a / (this.a - getB());
		}
		return f;
	}

	public void setF(Double f) {
		this.f = f;
	}

	public void setSphere() {
		this.isSphere = true;
	}

	public Double getESquare() {
		if (this.es == null) {
			this.es = this.a * this.a - getB() * getB();
		}
		return this.es;
	}

	public static class EllipsoidParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7069526633326889569L;

		public EllipsoidParseException(String message) {
			super(message);
		}
	}

	public void check() throws EllipsoidParseException {
		if (null == id) {
			throw new EllipsoidParseException("Id null" + this);
		}
		if (null == name) {
			throw new EllipsoidParseException("Name null" + this);
		}
		if (null == a) {
			throw new EllipsoidParseException("SemiMajorAxis null" + this);
		}
		if (!isSphere && null == b && null == f) {
			throw new EllipsoidParseException(
					"SemiMinorAxis and InverseFlattening null" + this);
		}
	}

	@Override
	public String toString() {
		return "Ellipsoid [id=" + id + ", name=" + name + ", a=" + a + ", b="
				+ b + ", f=" + f + ", isSphere=" + isSphere + "]";
	}

}
