<%--
columnchildren.dsp

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Jul 18 14:24:07 TST 2008, Created by jumperchen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
--%><%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>
<%@ taglib uri="http://www.zkoss.org/dsp/zk/core" prefix="z" %>
<%@ taglib uri="http://www.zkoss.org/dsp/zul/core" prefix="u" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<div z.type="zkex.zul.columnlayout.ColumnChildren" id="${self.uuid}">
<div id="${self.uuid}!real"${self.outerAttrs}${self.innerAttrs}>
<div class="${self.sclass}-bwrap"><div id="${self.uuid}!cave" class="${self.sclass}-body">
	<c:forEach var="child" items="${self.children}">
${z:redraw(child, null)}
	</c:forEach>
</div></div></div></div>
