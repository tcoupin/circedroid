package org.circedroid.core.tools;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ToolsTest extends TestCase {

	public static final String IGNFURL = "http://registre.ign.fr/ign/IGNF/crs/IGNF/LAMB93#LAMB93";
	public static final String IGNFID = "IGNF:LAMB93";
	public static final String EPSGURL = "urn:ogc:def:crs:EPSG:6.11.2:2154";
	public static final String EPSGID = "EPSG:2154";

	public void testurl2id() {
		String id;

		// Null input
		id = Tools.url2id(null);
		Assert.assertNull(id);

		// IGNF url
		id = Tools.url2id(IGNFURL);
		Assert.assertEquals(IGNFID, id);

		// EPSG URI
		id = Tools.url2id(EPSGURL);
		Assert.assertEquals(EPSGID, id);
	}
}
