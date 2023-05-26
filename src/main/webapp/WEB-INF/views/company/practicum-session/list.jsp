<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="company.practicumSession.list.label.title" path="title" width="30%"/>
	<acme:list-column code="company.practicumSession.list.label.periodStart" path="periodStart" width="25%"/>
	<acme:list-column code="company.practicumSession.list.label.periodEnd" path="periodEnd" width="25%"/>
	<acme:list-column code="company.practicumSession.list.label.published" path="published" width="20%"/>
</acme:list>
<acme:button test="${!publishedPracticum}" code="company.practicumSession.list.button.create" action="/company/practicum-session/create?masterId=${masterId}"/>
<acme:button test="${publishedPracticum && addendumAvailable}" code="company.practicumSession.list.button.createAddendum" action="/company/practicum-session/create?masterId=${masterId}"/>