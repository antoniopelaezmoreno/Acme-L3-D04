<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:input-checkbox code="auditor.auditing-record.form.label.published" path="published"/>
	<acme:input-textbox code="auditor.auditing-record.form.label.subject" path="subject"/>	
	<acme:input-textbox code="auditor.auditing-record.form.label.assessment" path="assessment"/>
	<acme:input-moment code="auditor.auditing-record.form.label.periodStart" path="periodStart"/>
	<acme:input-moment code="auditor.auditing-record.form.label.periodEnd" path="periodEnd"/>
	<acme:input-select code="auditor.auditing-record.form.label.mark" path="mark" choices="${marks}"/>
	<acme:input-url code="auditor.auditing-record.form.label.link" path="link"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="auditor.auditing-record.form.label.confirmation" path="confirmation"/>
			<acme:submit code="auditor.auditing-record.form.button.create" action="/auditor/auditing-records/create?masterId=${masterId}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<jstl:if test="${published == false}">
				<acme:submit code="auditor.auditing-record.form.button.update" action="/auditor/auditing-records/update"/>
				<acme:submit code="auditor.auditing-record.form.button.delete" action="/auditor/auditing-records/delete"/>
			</jstl:if>			
		</jstl:when>	
	</jstl:choose>

</acme:form>