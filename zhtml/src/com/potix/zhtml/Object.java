/* Object.java

{{IS_NOTE
	$Id: Object.java,v 1.2 2006/02/27 03:54:39 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Tue Dec 13 15:03:49     2005, Created by tomyeh@potix.com
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
 * The OBJECT tag.
 * 
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @version $Revision: 1.2 $ $Date: 2006/02/27 03:54:39 $
 */
public class Object extends AbstractTag {
	public Object() {
		super("object");
	}
}
