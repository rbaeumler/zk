/* GlobalDesktopCacheProvider.java

{{IS_NOTE
	$Id: GlobalDesktopCacheProvider.java,v 1.1.1.1 2006/04/24 04:04:05 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Tue Apr 18 14:12:16     2006, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package com.potix.zk.ui.impl;

import com.potix.zk.ui.WebApp;
import com.potix.zk.ui.Session;
import com.potix.zk.ui.sys.DesktopCacheProvider;
import com.potix.zk.ui.sys.DesktopCache;

/**
 * A implementation of {@link DesktopCacheProvider} that stores all
 * desktops from the same Web application into one desktop cache.
 *
 * <p>In other words, it ignores the session, and it depends only on
 * {@link WebApp}.
 * 
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2006/04/24 04:04:05 $
 * @see SessionDesktopCacheProvider
 */
public class GlobalDesktopCacheProvider implements DesktopCacheProvider {
	private static final String ATTR_CACHE = "javax.potix.zk.desktop-cache";

	//-- DesktopCacheProvider --//
	public DesktopCache getCache(Session sess) {
		final WebApp wapp = sess.getWebApp();
		DesktopCache dc = (DesktopCache)wapp.getAttribute(ATTR_CACHE);
		if (dc == null) {
			synchronized (this) {
				dc = (DesktopCache)wapp.getAttribute(ATTR_CACHE);
				if (dc == null) {
					dc = new SimpleDesktopCache();
					wapp.setAttribute(ATTR_CACHE, dc);
				}
			}
		}
		return dc;
	}
	public void sessionDestroyed(Session sess) {
		//ignore it
	}

	public void start(WebApp wapp) {
		//ignore it
	}
	public void stop(WebApp wapp) {
		DesktopCache dc = (DesktopCache)wapp.getAttribute(ATTR_CACHE);
		if (dc != null) {
			wapp.removeAttribute(ATTR_CACHE);
			dc.stop();
		}
	}
}
