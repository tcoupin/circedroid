package org.circedroid.core.value;

import org.circedroid.core.value.Angle.AngleUoM;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AngleTest extends TestCase {
	
	public static final double PI = Math.PI;
	public static final double CENT_QUATRE_VINGT = 180d;
	public static final double DEUX_CENT = 200d;
	public static final double CENT_QUATRE_VINGT_SECONDE = 648000d;

	public void testRad2deg(){
		double rad = PI;
		double result = Angle.rad2deg(rad);
		Assert.assertEquals(CENT_QUATRE_VINGT, result);
	}
	
	public void testDeg2Rad(){
		double deg = CENT_QUATRE_VINGT;
		double result = Angle.deg2rad(deg);
		Assert.assertEquals(PI, result);
	}
	
	public void testGon2deg(){
		double gon = DEUX_CENT;
		double result = Angle.gon2deg(gon);
		Assert.assertEquals(CENT_QUATRE_VINGT, result);
	}
	
	public void testDeg2Gon(){
		double deg = CENT_QUATRE_VINGT;
		double result = Angle.deg2gon(deg);
		Assert.assertEquals(DEUX_CENT, result);
	}
	
	public void testAngleConstuctorFromRadian(){
		Angle angle = new Angle(AngleUoM.Radian, PI);
		Assert.assertNotNull(angle);
		Assert.assertNotNull(angle.getValue());
		Assert.assertEquals(CENT_QUATRE_VINGT,angle.getValue());
	}
	
	public void testAngleConstuctorFromDegDec(){
		Angle angle = new Angle(AngleUoM.DegDec, CENT_QUATRE_VINGT);
		Assert.assertNotNull(angle);
		Assert.assertNotNull(angle.getValue());
		Assert.assertEquals(CENT_QUATRE_VINGT,angle.getValue());
	}
	public void testAngleConstuctorFromGrad(){
		Angle angle = new Angle(AngleUoM.Grad, DEUX_CENT);
		Assert.assertNotNull(angle);
		Assert.assertNotNull(angle.getValue());
		Assert.assertEquals(CENT_QUATRE_VINGT,angle.getValue());
	}
	public void testAngleConstuctorFromSecond(){
		Angle angle = new Angle(AngleUoM.Second, CENT_QUATRE_VINGT_SECONDE);
		Assert.assertNotNull(angle);
		Assert.assertNotNull(angle.getValue());
		Assert.assertEquals(CENT_QUATRE_VINGT,angle.getValue());
	}
	
	public void testAngleUoMgetByUom(){
		AngleUoM uom = AngleUoM.getByUom(null);
		Assert.assertNull(uom);
		
		uom = AngleUoM.getByUom(AngleUoM.DegDec.getUoM());
		Assert.assertNotNull(uom);
		Assert.assertEquals(AngleUoM.DegDec, uom);
	}
	
}
