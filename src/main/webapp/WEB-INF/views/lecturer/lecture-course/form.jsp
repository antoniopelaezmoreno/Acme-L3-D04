<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	
	<acme:input-select code="lecturer.lectureCourse.form.label.course" path="course" choices="${choicesCourses}"/>
	<acme:input-select code="lecturer.lectureCourse.form.label.lecture" path="lecture" choices="${choicesLectures}"/>
	

	<acme:submit code="lecturer.lectureCourse.form.button.add" action="/lecturer/lecture-course/create"/>

</acme:form>