<%--
text.jsp

{{IS_NOTE
	$Id: text.dsp,v 1.3 2006/02/27 03:54:34 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Wed Jun  8 18:56:09     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
--%><%@ taglib uri="/WEB-INF/tld/web/core.dsp.tld" prefix="c" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<span id="${self.uuid}"><c:out value="${self.value}"/></span>