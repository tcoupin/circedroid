package org.circedroid.core.tools;

import junit.framework.Assert;
import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

	public static final String stringWithSpeChar = "Hé mais ça éèêàîôùûHéOuI";
	public static final String stringNormalized = "he mais ca eeeaiouuheoui";

	public void testNormalize() {
		String result = StringUtils.normalize(stringWithSpeChar);
		Assert.assertEquals(stringNormalized, result);
	}

	public static final String chiens = "Chiens";
	public static final String niche = "niCHe";
	public static final float chiens_niche = 1f / 6f;
	public static final float zero = 0f;
	public static final float un = 1f;

	public void testLeven() {
		float result;

		result = StringUtils.leven(chiens, null);
		Assert.assertEquals(zero, result);

		result = StringUtils.leven(null, niche);
		Assert.assertEquals(zero, result);

		result = StringUtils.leven(chiens, "");
		Assert.assertEquals(zero, result);

		result = StringUtils.leven("", niche);
		Assert.assertEquals(zero, result);

		result = StringUtils.leven(chiens, niche);
		Assert.assertEquals(chiens_niche, result);

		result = StringUtils.leven(niche, chiens);
		Assert.assertEquals(chiens_niche, result);
		
		result = StringUtils.leven(niche, niche);
		Assert.assertEquals(un, result);
		
		result = StringUtils.leven(chiens, chiens);
		Assert.assertEquals(un, result);

	}
}
