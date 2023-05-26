<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="student.activity.form.label.title" path="title"/>	
	<acme:input-textarea code="student.activity.form.label.activityAbstract" path="activityAbstract"/>
	<jstl:if test="${_command != 'create'}">
		<acme:input-select code="student.activity.form.label.indicator" path="indicator" choices="${indicators}"/>
	</jstl:if>
	<acme:input-moment code="student.activity.form.label.periodStart" path="periodStart"/>
	<acme:input-moment code="student.activity.form.label.periodEnd" path="periodEnd"/>
	<acme:input-url code="student.activity.form.label.link" path="link"/>
	<acme:input-select code="student.activity.form.label.enrolment-code" path="enrolment" choices="${enrolments}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="student.activity.form.button.update" action="/student/activity/update"/>
			<acme:submit code="student.activity.form.button.delete" action="/student/activity/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="student.activity.form.button.create" action="/student/activity/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
