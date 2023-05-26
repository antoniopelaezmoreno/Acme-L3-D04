<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="student.lecture.list.label.title" path="title" width="30%"/>
	<acme:list-column code="student.lecture.list.label.lectureAbstract" path="lectureAbstract" width="50%"/>
	<acme:list-column code="student.lecture.list.label.indicator" path="indicator" width="20%"/>
</acme:list>