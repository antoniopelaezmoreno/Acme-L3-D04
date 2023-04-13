<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.course.list.label.code" path="code" width="10%"/>
	<acme:list-column code="any.course.list.label.title" path="title" width="30%"/>
	<acme:list-column code="any.course.list.label.courseAbstract" path="courseAbstract" width="60%"/>
</acme:list>