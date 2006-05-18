/* Script.java

{{IS_NOTE
	$Id: Script.java,v 1.2 2006/02/27 03:54:40 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Tue Dec 13 15:04:35     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package com.potix.zhtml;

import com.potix.zhtml.impl.AbstractTag;

/**
 * The SCRIPT tag.
 * 
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @version $Revision: 1.2 $ $Date: 2006/02/27 03:54:40 $
 */
public class Script extends AbstractTag {
	public Script() {
		super("script");
	}
}
