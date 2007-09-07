/* TabpanelsDefault.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Sep 6, 2007 6:59:53 PM , Created by robbiecheng
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.render;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.zkoss.zk.fn.ZkFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.ComponentRenderer;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanels;

/**
 * {@link Tabpanels}'s default mold.
 * 
 * @author robbiecheng
 * 
 * @since 3.0.0
 */

public class TabpanelsDefault implements ComponentRenderer {

	/**
	 * tabpanel.dsp
<c:set var="self" value="${requestScope.arg.self}"/>
<tbody id="${self.uuid}"${self.outerAttrs}${self.innerAttrs}>
	<c:forEach var="child" items="${self.children}">
	${z:redraw(child, null)}
	</c:forEach>
</tbody>

vtabpanel.dsp
<td id="${self.uuid}"${self.outerAttrs}${self.innerAttrs}>
	<c:forEach var="child" items="${self.children}">
	${z:redraw(child, null)}
	</c:forEach>
</td>
	 */
	public void render(Component comp, Writer out) throws IOException {
		final WriterHelper wh = new WriterHelper(out);
		final Tabpanels self = (Tabpanels) comp;
		
		if(((Tabbox)self.getParent()).getOrient().equals("vertical")){
			wh.write("<td id=\"" + self.getUuid() + "\"" + self.getOuterAttrs() + self.getInnerAttrs()+ ">");		
			for (Iterator it = self.getChildren().iterator(); it.hasNext();) {
				final Component child = (Component) it.next();
				ZkFns.redraw(child, null);
			}	
			wh.write("</td>");
		}
		else{
			wh.write("<tbody id=\"" + self.getUuid() + "\"" + self.getOuterAttrs() + self.getInnerAttrs()+ ">");		
			for (Iterator it = self.getChildren().iterator(); it.hasNext();) {
				final Component child = (Component) it.next();
				ZkFns.redraw(child, null);
			}	
			wh.write("</tbody>");
		}
	}

}
