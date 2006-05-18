/* Set.java

{{IS_NOTE
	$Id: Set.java,v 1.4 2006/02/27 03:54:31 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Tue Sep  6 15:11:19     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package com.potix.web.servlet.dsp.action;

import java.io.IOException;

import com.potix.web.mesg.MWeb;
import com.potix.web.servlet.ServletException;

/**
 * The set action used to set an attribute.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @version $Revision: 1.4 $ $Date: 2006/02/27 03:54:31 $
 */
public class Set extends AbstractAction {
	private int _scope = ActionContext.PAGE_SCOPE;
	private String _var;
	private Object _val;

	/** Returns the scope. */
	public int getScope() {
		return _scope;
	}
	/** Sets the scope. */
	public void setScope(String scope) {
		_scope = toScope(scope);
	}
	/** Returns the attribute name. */
	public String getVar() {
		return _var;
	}
	/** Sets the attribute name. */
	public void setVar(String var) {
		_var = var;
	}
	/** Returns the attribute value. */
	public Object getValue() {
		return _val;
	}
	/** Sets the attribute value. */
	public void setValue(Object val) {
		_val = val;
	}

	//-- Action --//
	public void render(ActionContext ac, boolean nested)
	throws javax.servlet.ServletException, IOException {
		if (!isEffective())
			return;
		if (nested)
			throw new ServletException(MWeb.DSP_NESTED_ACTION_NOT_ALLOWED,
				new Object[] {this, new Integer(ac.getLineNumber())});
		if (_var == null)
			throw new ServletException(MWeb.DSP_ATTRIBUTE_REQUIRED,
				new Object[] {this, "var", new Integer(ac.getLineNumber())});
		ac.setAttribute(_var, _val, _scope);
	}

	//-- Object --//
	public String toString() {
		return "set";
	}
}
