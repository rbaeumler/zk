/* Updatable.java

{{IS_NOTE
	$Id: Updatable.java,v 1.3 2006/02/27 03:54:51 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Thu Jul 21 18:50:03     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package com.potix.zk.ui.ext;

import com.potix.zk.ui.UiException;

/**
 * Decorates {@link com.potix.zk.ui.Component} such that it supports special
 * updates (other than async-update).
 *
 * <h2>Supported special updates</h2>
 * <h3>File upload</h3>
 * <ol>
 * <li>Component uses to use inner-frame or other mechanism to submit a file
 * to {@link com.potix.zk.au.http.DHtmlUpdateServlet}'s /upload.
 * <li>DHtmlUpdateServlet than store the result in desktop's attribute,
 * and ask client to do a standard async-update called doUpdatable.
 * <li>When client sends doUpdatable request to the server, server invokes
 * {@link #setResult} to put the upload result to the component.
 * </ol>
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @version $Revision: 1.3 $ $Date: 2006/02/27 03:54:51 $
 */
public interface Updatable {
	/** Sets the result when it is updated from the client successfully.
	 */
	public void setResult(Object result);
}
