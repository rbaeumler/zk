<%--
audio.dsp

{{IS_NOTE
	$Id: audio.dsp,v 1.6 2006/03/17 10:06:30 tomyeh Exp $
	Purpose:
		
	Description:
		
	History:
		Wed Nov 16 17:04:52     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
--%><%@ taglib uri="/WEB-INF/tld/web/core.dsp.tld" prefix="c" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<embed id="${self.uuid}"${self.outerAttrs}${self.innerAttrs} mastersound="mastersound" zk_type="zul.html.audio.Audio"/>