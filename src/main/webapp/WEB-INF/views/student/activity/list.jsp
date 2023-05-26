<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="student.activity.list.label.title" path="title" width="50%"/>
	<acme:list-column code="student.activity.list.label.indicator" path="indicator" width="25%"/>
	<acme:list-column code="student.activity.list.label.enrolment-code" path="enrolment" width="25%"/>
</acme:list>

<acme:button code="student.activity.form.button.create" action="/student/activity/create"/>