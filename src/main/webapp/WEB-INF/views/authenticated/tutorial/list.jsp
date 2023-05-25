<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.tutorial.list.label.code" path="code" width="25%"/>
	<acme:list-column code="authenticated.tutorial.list.label.title" path="title" width="50%"/>
	<acme:list-column code="authenticated.tutorial.list.label.estimatedTime" path="estimatedTime" width="15%"/>
</acme:list>
