/* TextNode.java

{{IS_NOTE
	$Id: TextNode.java,v 1.4 2006/02/27 03:54:32 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Sat Sep 17 14:11:45     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2004 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package com.potix.web.servlet.dsp.impl;

import java.io.Writer;
import java.io.IOException;

/**
 * Represents a node holding a plain text.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @version $Revision: 1.4 $ $Date: 2006/02/27 03:54:32 $
 */
class TextNode extends Node {
	private final String _text;
	TextNode(String text) {
		_text = text;
	}

	//-- super --//
	void interpret(InterpretContext ic)
	throws javax.servlet.ServletException, IOException {
		ic.dc.getOut().write(_text);
	}
	void addChild(Node node) {
		throw new IllegalStateException("No child allowed");
	}

	public String toString() {
		return "TextNode["+
			(_text.length() > 20 ? _text.substring(0, 20): _text)+']';
	}
}
