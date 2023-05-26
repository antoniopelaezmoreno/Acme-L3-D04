<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.tutorial.form.label.code" path="code"/>
	<acme:input-textbox code="authenticated.tutorial.form.label.title" path="title"/>	
	<acme:input-select code="authenticated.tutorial.form.label.course" path="course" choices="${courses}"/>
	<jstl:if test="${showAssistant}">
		<acme:input-textbox code="authenticated.tutorial.form.label.assistant" path="assistant" readonly="true"/>
	</jstl:if>
	<jstl:if test="${!showAssistant}">
		<acme:hidden-data path="assistant"/>
	</jstl:if>
	<acme:input-textarea code="authenticated.tutorial.form.label.tutorialAbstract" path="tutorialAbstract"/>
	<acme:input-textarea code="authenticated.tutorial.form.label.goals" path="goals"/>
	<acme:input-moment code="authenticated.tutorial.form.label.estimatedTime" path="estimatedTime"/>
	<acme:hidden-data path="id"/>
	<acme:hidden-data path="published"/>
</acme:form>