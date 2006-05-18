<%--
html.dsp

{{IS_NOTE
	$Id: html.dsp,v 1.4 2006/03/17 10:06:32 tomyeh Exp $
	Purpose:
		Display any HTML tags
	Description:
		
	History:
		Mon Jul 25 11:32:14     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
--%><%@ taglib uri="/WEB-INF/tld/web/core.dsp.tld" prefix="c" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<span id="${self.uuid}"${self.outerAttrs}${self.innerAttrs}>
${self.content}<%-- don't escape --%>
</span>