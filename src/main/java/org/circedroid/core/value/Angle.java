package org.circedroid.core.value;

import org.circedroid.core.geodesy.Axis;


public class Angle implements Value {

	public enum AngleUoM {
		Radian("rad"), Grad("gr"), DegDec("dec"), Second("sec");

		private String uom;

		private AngleUoM(String uom) {
			this.uom = uom;
		}

		public boolean equals(String uomtotest) {
			return this.uom.equals(uomtotest);
		}
		
		public String getUoM(){
			return uom;
		}
		
		public static AngleUoM getByUom(String uom){
			for (AngleUoM it : AngleUoM.values()){
				if (it.equals(uom)){
					return it;
				}
			}
			return null;
		}

	}

	private Double degValue;

	public Angle(Angle.AngleUoM uom, Double value) {
		if (uom == AngleUoM.Radian) {
			this.degValue = rad2deg(value);
		} else if (uom == AngleUoM.DegDec) {
			this.degValue = value;
		} else if (uom == AngleUoM.Second) {
			this.degValue = value / 3600d;
		} else if (uom == AngleUoM.Grad) {
			this.degValue = gon2deg(value);
		}
	}

	public static Double rad2deg(Double rad) {
		return rad / Math.PI * 180.0d;
	}
	
	public static Double gon2deg(Double gon) {
		return gon / 200.0d * 180.0d;
	}

	public static Double deg2rad(Double deg) {
		return deg * Math.PI / 180.0d;
	}
	public static Double deg2gon(Double deg) {
		return deg * 200.0d / 180.0d;
	}

	public String toString() {
		return degValue + "°";
	}


	public Double getValue() {
		return degValue;
	}
}
