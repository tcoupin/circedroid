package org.circedroid.core.tools;

import org.circedroid.core.value.Bbox;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BboxTest extends TestCase {
	public static final double east = 1d;
	public static final double west = 2d;
	public static final double north = 3d;
	public static final double south = 4d;
	
	public static final double CENT_QUATRE_VINGT = 180d;
	public static final double QUATRE_VINGT_DIX = 90d;
	
	
	public void testBbox(){
		Bbox bbox = new Bbox();
		Assert.assertNotNull(bbox);
		
		//Defaults values
		Assert.assertEquals(CENT_QUATRE_VINGT, bbox.getEast());
		Assert.assertEquals(-CENT_QUATRE_VINGT, bbox.getWest());
		Assert.assertEquals(QUATRE_VINGT_DIX, bbox.getNorth());
		Assert.assertEquals(-QUATRE_VINGT_DIX, bbox.getSouth());
		
		bbox.setEast(east);
		bbox.setNorth(north);
		bbox.setSouth(south);
		bbox.setWest(west);
		
		Assert.assertEquals(east, bbox.getEast());
		Assert.assertEquals(north, bbox.getNorth());
		Assert.assertEquals(south, bbox.getSouth());
		Assert.assertEquals(west, bbox.getWest());
		
	}

}


