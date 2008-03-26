/* WebManager.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Jun 15 13:28:19     2006, Created by tomyeh
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.http;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.lang.D;
import org.zkoss.util.logging.Log;
import org.zkoss.util.resource.Labels;
import org.zkoss.util.resource.Locator;

import org.zkoss.web.util.resource.ServletContextLocator;
import org.zkoss.web.util.resource.ServletLabelLocator;
import org.zkoss.web.util.resource.ServletLabelResovler;
import org.zkoss.web.util.resource.ClassWebResource;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Richlet;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zk.ui.metainfo.PageDefinitions;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.metainfo.LanguageDefinition;
import org.zkoss.zk.ui.sys.DesktopCtrl;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zk.ui.sys.UiFactory;
import org.zkoss.zk.ui.sys.SessionCtrl;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zk.ui.sys.UiEngine;
import org.zkoss.zk.ui.sys.ConfigParser;
import org.zkoss.zk.ui.sys.RequestInfo;
import org.zkoss.zk.ui.impl.RequestInfoImpl;

/**
 * A bridge bewteen Web server and ZK.
 *
 * <p>Each Web application that uses ZK will have an independent instance
 * of {@link WebManager}.
 *
 * @author tomyeh
 */
public class WebManager {
	private static final Log log = Log.lookup(WebManager.class);

	/** A session attribute. */
	private static final String ATTR_SESS = "javax.zkoss.zk.ui.Session";
		//Naming with javax to be able to shared among portlets

	/** A context attribute for storing an instance of this class. */
	/*package*/ static final String ATTR_WEB_MANAGER
		= "javax.zkoss.zk.ui.WebManager";
	/** A request attribute to store the current desktop.
	 * Because we store this portlet request, we have to name it
	 * with javax such that it is visible to other servlets and portlets.
	 */
	/*package*/ static final String ATTR_DESKTOP = "javax.zkoss.zk.ui.desktop";

	/** Map(ServletContext, List(WebManagerActivationListener)). */
	private static final Map _actListeners = new HashMap();
	/** Used to inter-communicate among portlet. */
//	private final static ThreadLocal _reqLocal = new ThreadLocal();

	private final ServletContext _ctx;
	private final WebApp _wapp;
	private final String _updateURI;
	private final ClassWebResource _cwr;

	/** Creates a new Web
	 */
	/*package*/ WebManager(ServletContext ctx, String updateURI) {
		if (log.debugable()) log.debug("Starting WebManager at "+ctx);

		if (ctx == null || updateURI == null)
			throw new IllegalArgumentException("null");
		_ctx = ctx;
		_updateURI = updateURI;

		Labels.register(new ServletLabelLocator(_ctx));
		Labels.setVariableResolver(new ServletLabelResovler());

		_cwr = ClassWebResource.getInstance(_ctx, _updateURI);
		_cwr.setCompress(new String[] {"js", "css"});
		_ctx.setAttribute(ATTR_WEB_MANAGER, this);

		//Register resource processors for each extension
		//FUTURE: Extendlet can be specified in zk.xml
		//Note: getAll loads config.xml, which must be processed before zk.xml
		ZumlExtendlet extlet = null;
		for (Iterator it = LanguageDefinition.getAll().iterator();
		it.hasNext();) {
			final LanguageDefinition langdef = (LanguageDefinition)it.next();
			final List exts = langdef.getExtensions();
			if (!exts.isEmpty()) {
				if (extlet == null)
					extlet = new ZumlExtendlet();
				_cwr.addExtendlet((String)exts.get(0), extlet);
				//Add to the first extension only (the main one)
			}
		}

		final Configuration config = new Configuration();
		try {
			final URL cfgUrl = _ctx.getResource("/WEB-INF/zk.xml");
			if (cfgUrl != null)
				new ConfigParser()
					.parse(cfgUrl, config, new ServletContextLocator(_ctx));
		} catch (Throwable ex) {
			log.error("Unable to load /WEB-INF/zk.xml", ex);
		}

		//create a WebApp instance
		_wapp = newWebApp(config);
		((WebAppCtrl)_wapp).init(_ctx, config);
		_cwr.setDebugJS(config.isDebugJS());
			//Note: when ConfigParser ran, WebApp is not assigned to
			//Configuration yet (so we have to setDebugJS here)

		final List listeners = (List)_actListeners.remove(_ctx); //called and drop
		if (listeners != null) {
			for (Iterator it = listeners.iterator(); it.hasNext();) {
				try {
					((WebManagerActivationListener)it.next()).didActivate(this);
				} catch (java.util.ConcurrentModificationException ex) {
					throw ex;
				} catch (Throwable ex) {
					log.realCause(ex);
				}
			}
		}
	}
	private static WebApp newWebApp(Configuration config) {
		Class cls = config.getWebAppClass();
		if (cls == null) {
			return new SimpleWebApp();
		} else {
			try {
				return (WebApp)cls.newInstance();
			} catch (Exception ex) {
				throw UiException.Aide.wrap(ex, "Unable to construct "+cls);
			}
		}
	}

	public void destroy() {
		((WebAppCtrl)_wapp).destroy();
	}

	/** Returns the handler to retrieve resource from class path,
	 * under /web.
	 */
	public final ClassWebResource getClassWebResource() {
		return _cwr;
	}

	/** Returns the Web application.
	 * Notice: a Web application is allocated for each servlet.
	 */
	public final WebApp getWebApp() {
		return _wapp;
	}

	//-- static --//
	/** Initializes the request-local storage that is used to lift the limitation
	 * of incapability of inter-portlet communication.
	 *
	 * <p>In other words, {@link #getRequestLocal} will look for this storage
	 * in addition to request's attributes.
	 */
/* deprecated (reason: multi-desktop per HTML page is allowed)
	static void initRequestLocal() {
		_reqLocal.set(new HashMap());
	}*/
	/** Cleans up the request-local storage set by {@link #initRequestLocal}.
	 */
/* deprecated
	static void cleanRequestLocal() {
		_reqLocal.set(null);
	}*/
	/** Returns the value of the specified attribute in the request.
	 * The implementation shall use this method instead of request.getAttribute,
	 * since it resolves the limitation of incapability of inter-portlet
	 * communication.
	 *
	 * @param name the attribute's name
	 */
	public static Object getRequestLocal(ServletRequest request, String name) {
		return request.getAttribute(name);
/* deprecated
		final Object o = request.getAttribute(name);
		if (o != null) return o;

		final Map local = (Map)_reqLocal.get();
		return local != null ? local.get(name): null;
*/
	}
	/** Sets the value of the specified attribute in the request.
	 * The implementation shall use this method instead of request.setAttribute,
	 * since it resolves the limitation of incapability of inter-portlet
	 * communication.
	 * @param name the attribute's name
	 * @param value the attribute's value
	 */
	public static
	void setRequestLocal(ServletRequest request, String name, Object value) {
		request.setAttribute(name, value);
/* deprecated
		final Map local = (Map)_reqLocal.get();
		if (local != null) local.put(name, value);
*/
	}

	/** Register a listener to the specified context such that
	 * it will be invoked if the corresponding {@link WebManager} is created.
	 *
	 * <p>Note: if the Web manager is created already, {@link WebManagerActivationListener#didActivate}
	 * will be invoked immediately before this method returns.
	 *
	 * @since 2.4.0
	 */
	public static final
	void addActivationListener(ServletContext ctx, WebManagerActivationListener listener) {
		if (ctx == null || listener == null)
			throw new IllegalArgumentException("null");

		final WebManager webman = getWebManagerIfAny(ctx);
		if (webman != null) {
			listener.didActivate(webman);
		} else {
			synchronized (WebManager.class) {
				List l = (List)_actListeners.get(ctx);
				if (l == null) _actListeners.put(ctx, l = new LinkedList());
				l.add(listener);
			}
		}
	}

	/** Returns the Web manager of the specified context.
	 * @exception UiException if not found (i.e., not initialized
	 * properly)
	 */
	public static final WebManager getWebManager(ServletContext ctx) {
		final WebManager webman = getWebManagerIfAny(ctx);
		if (webman == null)
			throw new UiException("The Web manager not found. Make sure <load-on-startup> is specified for "+DHtmlLayoutServlet.class.getName());
		return webman;
	}
	/** Returns the Web manager of the specified {@link WebApp}.
	 * @since 3.0.4
	 */
	public static final WebManager getWebManager(WebApp wapp) {
		return getWebManager((ServletContext)wapp.getNativeContext());
	}
	/** Returns the Web manager, or null if not found.
	 */
	/*package*/ static final WebManager getWebManagerIfAny(ServletContext ctx) {
		return (WebManager)ctx.getAttribute(ATTR_WEB_MANAGER);
	}

	/**  Returns the current session associated with this request,
	 * or if the request does not have a session, creates one.
	 */
	public static final
	Session getSession(ServletContext ctx, HttpServletRequest request) {
		return getSession(ctx, request, request.getSession());
	}
	/**Returns the current HttpSession  associated with this request or,
	 * if there is no current session and create is true, returns a new session.
	 *
	 * @param create true to create a new session for this request if necessary;
	 * false to return null  if there's no current session
	 * @since 3.0.1
	 */
	public static final
	Session getSession(ServletContext ctx, HttpServletRequest request,
	boolean create) {
		final HttpSession hsess = request.getSession(create);
		return hsess != null ? getSession(ctx, request, hsess): null;
	}
	/*package*/ static final Session newSession(WebApp wapp,
	HttpSession hsess, Object request) {
		if (D.ON && log.debugable()) log.debug("Creating a new sess for "+hsess);

		final Session sess = ((WebAppCtrl)wapp).getUiFactory()
			.newSession(wapp, hsess, request);
		hsess.setAttribute(ATTR_SESS, sess);

		//Note: we set timeout here, because HttpSession might have been created
		//by other servlet or filter
		final Configuration config = wapp.getConfiguration();
		final int v = config.getSessionMaxInactiveInterval();
		if (v != 0) sess.setMaxInactiveInterval(v);
		return sess;
	}
	private static final Session getSession(ServletContext ctx,
	HttpServletRequest request, HttpSession hsess) {
		final Session sess = getSession(hsess);
		return sess != null ? sess:
			newSession(getWebManager(ctx).getWebApp(), hsess, request);
	}
	/** Returns the session of the specified HTTP session, or null if n/a. */
	/*package*/ static final Session getSession(HttpSession hsess) {
		final Session sess =
			(Session)hsess.getAttribute(ATTR_SESS);
		if (sess != null && sess.getNativeSession() == null)
			((SessionCtrl)sess).recover(hsess);
		return sess;
	}
	/** Called when a HTTP session listner is notified.
	 * <p>Once called the session is cleaned. All desktops are dropped.
	 */
	/*package*/ static final
	void onSessionDestroyed(HttpSession hsess) {
		final Session sess = (Session)getSession(hsess);
		if (sess != null) {
			try {
				final WebApp wapp =
					getWebManager(hsess.getServletContext()).getWebApp();
				((WebAppCtrl)wapp)
					.getDesktopCacheProvider().sessionDestroyed(sess);
			} catch (Throwable ex) {
				log.error("Failed to cleanup session", ex);
			}

			try {
				((SessionCtrl)sess).onDestroyed();
			} catch (Throwable ex) {
				log.error("Failed to cleanup session", ex);
			}
			hsess.removeAttribute(ATTR_SESS);
		}
	}

	/** Returns the desktop of the specified request, or null
	 * if not found and autocreate is false.
	 * @param path the path of the ZUML page.
	 * @param autocreate whether to create one if not found
	 * @since 3.0.0
	 */
	public Desktop getDesktop(Session sess, ServletRequest request,
	ServletResponse response, String path, boolean autocreate) {
		Desktop desktop = (Desktop)getRequestLocal(request, ATTR_DESKTOP);
		if (desktop == null && autocreate) {
			if (D.ON && log.debugable()) log.debug("Create desktop for "+path);
			setRequestLocal(request, ATTR_DESKTOP,
				desktop = newDesktop(sess, request, response, path));
		}
		return desktop;
	}
	/** Returns the desktop of the specified request, or null if not found.
	 * @deprecated As of release 3.0.0, replaced by
	 * {@link #getDesktop(Session,ServletRequest,ServletResponse,String,boolean)}.
	 */
	public Desktop getDesktop(Session sess, ServletRequest request,
	String path, boolean autocreate) {
		return getDesktop(sess, request, null, path, autocreate);
	}
	/** Creates an desktop. */
	/*package*/ Desktop newDesktop(Session sess, ServletRequest request,
	ServletResponse response, String path) {
		final Locator loc = PageDefinitions.getLocator(_wapp, path);
			//we have to retrieve locator before setting execution,
			//since it assumes exec.getDesktop not null
			//which is true except this moment (before desktop is created)

		final Execution exec = ExecutionsCtrl.getCurrent();
		if (exec == null) //it shall be null, but, just in case,
			ExecutionsCtrl.setCurrent(
				new ExecutionImpl(
					_ctx, (HttpServletRequest)request,
					(HttpServletResponse)response, null, null));
		try {
			return ((WebAppCtrl)_wapp).getUiFactory().newDesktop(
				new RequestInfoImpl(_wapp, sess, null, request, loc),
				_updateURI, path);
		} finally {
			if (exec == null)
				ExecutionsCtrl.setCurrent(null);
		}
	}
	/** Sets the desktop to the specified request.
	 * Used internally for implementation only.
	 */
	public static void setDesktop(HttpServletRequest request,
	Desktop desktop) {
		/*if (D.ON) {
			final Desktop dt = (Desktop)getRequestLocal(ATTR_DESKTOP);
			assert dt == null || dt == desktop: "old:"+dt+", new:"+desktop;
		}*/
		setRequestLocal(request, ATTR_DESKTOP, desktop);
	}
	/** Creates a page.
	 * It invokes {@link UiFactory#newPage}. However, it prepares
	 * {@link Executions#getCurrent} for {@link org.zkoss.zk.ui.sys.IdGenerator#nextPageUuid}
	 *
	 * <p>Note: Use this method to create a page, rather than invoking
	 * {@link UiFactory#newPage} directly.
	 */
	/*package*/ static
	Page newPage(UiFactory uf, RequestInfo ri, PageDefinition pagedef,
	ServletResponse response, String path) {
		final Execution exec = ExecutionsCtrl.getCurrent();
		if (exec == null) { //it shall be null, but, just in case,
			final ExecutionImpl ei = new ExecutionImpl(
				(ServletContext)ri.getWebApp().getNativeContext(),
				(HttpServletRequest)ri.getNativeRequest(),
				(HttpServletResponse)response, ri.getDesktop(), null);
			((DesktopCtrl)ri.getDesktop()).setExecution(ei);
			ExecutionsCtrl.setCurrent(ei);
		}
		try {
			return uf.newPage(ri, pagedef, path);
		} finally {
			if (exec == null) {
				ExecutionsCtrl.setCurrent(null);
				((DesktopCtrl)ri.getDesktop()).setExecution(null);
			}
		}
	}
	/** Creates a page.
	 * It invokes {@link UiFactory#newPage}. However, it prepares
	 * {@link Executions#getCurrent} for {@link org.zkoss.zk.ui.sys.IdGenerator#nextPageUuid}
	 *
	 * <p>Note: Use this method to create a page, rather than invoking
	 * {@link UiFactory#newPage} directly.
	 */
	/*package*/ static
	Page newPage(UiFactory uf, RequestInfo ri, Richlet richlet,
	ServletResponse response, String path) {
		final Execution exec = ExecutionsCtrl.getCurrent();
		if (exec == null) { //it shall be null, but, just in case,
			final ExecutionImpl ei = new ExecutionImpl(
				(ServletContext)ri.getWebApp().getNativeContext(),
				(HttpServletRequest)ri.getNativeRequest(),
				(HttpServletResponse)response, ri.getDesktop(), null);
			((DesktopCtrl)ri.getDesktop()).setExecution(ei);
			ExecutionsCtrl.setCurrent(ei);
		}
		try {
			return uf.newPage(ri, richlet, path);
		} finally {
			if (exec == null) {
				ExecutionsCtrl.setCurrent(null);
				((DesktopCtrl)ri.getDesktop()).setExecution(null);
			}
		}
	}
}
