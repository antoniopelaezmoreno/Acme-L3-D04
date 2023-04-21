<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2>
	<acme:message code="lecturer.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.number-of-lecture-theorical"/>
		</th>
		<td>
			<acme:print value="${totalLecturesNumberByIndication.get(Indication.THEORETICAL)}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.number-of-lecture-handsOn"/>
		</th>
		<td>
			<acme:print value="${totalLecturesNumberByIndication.get(Indication.HANDS_ON)}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.average-lecture"/>
		</th>
		<td>
			<acme:print value="${lectureStatistics.average}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.max-lecture"/>
		</th>
		<td>
			<acme:print value="${lectureStatistics.max}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.min-lecture"/>
		</th>
		<td>
			<acme:print value="${lectureStatistics.min}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.average-course"/>
		</th>
		<td>
			<acme:print value="${courseStatistics.average}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.max-course"/>
		</th>
		<td>
			<acme:print value="${courseStatistics.max}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.min-course"/>
		</th>
		<td>
			<acme:print value="${courseStatistics.min}"/>
		</td>
	</tr>
</table>

<div style="display: flex; flex-direction: row;">
  <div style="flex: 1;">
    <h2><acme:message code="lecturer.dashboard.form.title.application-indication-lecture"/></h2>
    <canvas id="LecturesIndications"></canvas>
  </div>
  
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var courseData = {
			labels : [
				"THEORETICAL", "HANDS_ON"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${ratioOfLectureTheoretical}"/>, 
						<jstl:out value="${ratioOfLectureHandsOn}"/> 
					],
					backgroundColor: [
						'rgba(54, 162, 235, 0.2)', // Blue
						'rgba(255, 99, 132, 0.2)', // Red
					]
				}
			]
		};
		var courseOptions = {
			legend : {
				display : true
			}
		};
	
		var courseCanvas, courseContext;
	
		courseCanvas = document.getElementById("LecturesIndications");
		courseContext = courseCanvas.getContext("2d");
		new Chart(courseContext, {
			type : "pie",
			data : courseData,
			options : courseOptions
		});
		

	});
</script>

<acme:return/>