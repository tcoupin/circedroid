package org.circedroid.core.value;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DefaultValueTest extends TestCase {
	
	public static final double value = 123d;
	
	public void testDefaultValue(){
		DefaultValue val = new DefaultValue(value);
		Assert.assertNotNull(val);
		Assert.assertNotNull(val.getValue());
		Assert.assertEquals(value, val.getValue());
	}

}
