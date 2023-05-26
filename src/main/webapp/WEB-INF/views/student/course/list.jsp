<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="student.course.list.label.code" path="code" width="20%"/>
	<acme:list-column code="student.course.list.label.title" path="title" width="30%"/>
	<acme:list-column code="student.course.list.label.indicator" path="indicator" width="50%"/>
</acme:list>