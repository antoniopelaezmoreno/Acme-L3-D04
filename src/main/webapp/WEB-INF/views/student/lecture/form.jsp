<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form readonly="true">
	<acme:input-textbox code="student.lecture.form.label.title" path="title"/>	
	<acme:input-textarea code="student.lecture.form.label.lectureAbstract" path="lectureAbstract"/>
	<acme:input-textbox code="student.lecture.form.label.estimatedTime" path="estimatedTime"/>
	<acme:input-textarea code="student.lecture.form.label.body" path="body"/>
	<acme:input-select code="student.lecture.form.label.indicator" path="indicator" choices="${indicators}"/>
	<acme:input-url code="student.lecture.form.label.link" path="link"/>
</acme:form>
