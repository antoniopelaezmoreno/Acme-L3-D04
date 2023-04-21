<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.auditing-record.list.label.subject" path="subject"/>
	<acme:list-column code="auditor.auditing-record.list.label.periodStart" path="periodStart"/>
	<acme:list-column code="auditor.auditing-record.list.label.periodEnd" path="periodEnd"/>
	<acme:list-column code="auditor.auditing-record.list.label.mark" path="mark"/>
	<acme:list-column code="auditor.auditing-record.list.label.published" path="published"/>
</acme:list>

<acme:button code="auditor.auditing-record.list.button.create" action="/auditor/auditing-records/create?masterId=${masterId}"/>
