/* B80_ZK_3182Test.java

	Purpose:

	Description:

	History:
		Wed Apr 27 18:34:32 CST 2016, Created by sefi

Copyright (C) 2016 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zktest.zats.test2;

import org.junit.Test;
import org.zkoss.zel.PropertyNotFoundException;
import org.zkoss.zktest.zats.ZATSTestCase;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author sefi
 */
public class B80_ZK_3182Test extends ZATSTestCase {
	@Test public void test() {
		try {
			connect();
		} catch (Exception e){
			fail("Should not throw any exception, every label must have value");
		}
		assertTrue(true);
	}
}
