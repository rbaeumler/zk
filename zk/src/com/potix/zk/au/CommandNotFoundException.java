/* CommandNotFoundException.java

{{IS_NOTE
	$Id: CommandNotFoundException.java,v 1.2 2006/02/27 03:54:44 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Mon Jun  6 12:56:02     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package com.potix.zk.au;

import com.potix.zk.ui.UiException;

/**
 * Represents an update-relevant exception.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @version $Revision: 1.2 $ $Date: 2006/02/27 03:54:44 $
 */
public class CommandNotFoundException extends UiException {
	public CommandNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public CommandNotFoundException(String s) {
		super(s);
	}
	public CommandNotFoundException(Throwable cause) {
		super(cause);
	}
	public CommandNotFoundException() {
	}

	public CommandNotFoundException(int code, Object[] fmtArgs, Throwable cause) {
		super(code, fmtArgs, cause);
	}
	public CommandNotFoundException(int code, Object fmtArg, Throwable cause) {
		super(code, fmtArg, cause);
	}
	public CommandNotFoundException(int code, Object[] fmtArgs) {
		super(code, fmtArgs);
	}
	public CommandNotFoundException(int code, Object fmtArg) {
		super(code, fmtArg);
	}
	public CommandNotFoundException(int code, Throwable cause) {
		super(code, cause);
	}
	public CommandNotFoundException(int code) {
		super(code);
	}
}
