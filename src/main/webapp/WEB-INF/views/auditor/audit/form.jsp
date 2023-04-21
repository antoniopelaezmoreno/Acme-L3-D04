<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:input-checkbox code="auditor.audit.form.label.published" path="published"/>
	<acme:input-textbox code="auditor.audit.form.label.code" path="code"/>	
	<acme:input-textarea code="auditor.audit.form.label.conclusion" path="conclusion"/>
	<acme:input-textarea code="auditor.audit.form.label.strongPoints" path="strongPoints"/>
	<acme:input-textarea code="auditor.audit.form.label.weakPoints" path="weakPoints"/>
	
	
	<jstl:if test="${_command == 'create'}">
		<acme:input-select code="auditor.audit.form.label.course" path="course" choices="${courses}"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'show'}">
		<acme:input-select code="auditor.audit.form.label.course" path="course" choices="${courses}"/>
		<acme:button code="auditor.audit.auditingRecords" action="/auditor/auditing-record/list?masterId=${id}"/>
	</jstl:if>

	<br>
	<br>

	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.audit.form.button.create" action="/auditor/audit/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<jstl:if test="${published == false}">
				<acme:button code="auditor.audit.auditingRecords" action="/auditor/auditing-record/list?masterId=${id}"/>
				<acme:submit code="auditor.audit.form.button.update" action="/auditor/audit/update"/>
				<acme:submit code="auditor.audit.form.button.delete" action="/auditor/audit/delete"/>
			</jstl:if>			
		</jstl:when>	
	</jstl:choose>

</acme:form>