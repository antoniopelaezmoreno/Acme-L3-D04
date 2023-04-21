<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.audit.form.label.code" path="code"/>	
	<acme:input-textarea code="authenticated.audit.form.label.conclusion" path="conclusion"/>
	<acme:input-textarea code="authenticated.audit.form.label.strongPoints" path="strongPoints"/>
	<acme:input-textarea code="authenticated.audit.form.label.weakPoints" path="weakPoints"/>
	<acme:input-select code="authenticated.audit.form.label.mark" path="mark" choices="${marks}"/>
	<acme:input-select code="authenticated.audit.form.label.course" path="course" choices="${courses}"/>
	<acme:input-checkbox code="authenticated.audit.form.label.published" path="published"/>
	
	<h1><acme:message code="auditor.audit.auditor.data"/></h1>
	<acme:input-textbox code="auditor.audit.form.label.auditor.userName" path="userName"/>
	<acme:input-textarea code="auditor.audit.form.label.auditor.firm" path="firm"/>
	<acme:input-url code="auditor.audit.form.label.auditor.link" path="link"/>
</acme:form>