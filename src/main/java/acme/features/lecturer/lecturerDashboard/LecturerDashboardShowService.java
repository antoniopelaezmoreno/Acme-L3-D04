
package acme.features.lecturer.lecturerDashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.Statistics;
import acme.enums.Indication;
import acme.forms.LecturerDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerDashboardShowService extends AbstractService<Lecturer, LecturerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Lecturer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final LecturerDashboard lecturerDashboard;

		Integer numberOfLectureTheorical;
		Integer numberOfLecturelHandsOn;

		Map<Indication, Integer> totalLecturesNumberByIndication;

		final Statistics lectureStatistics;
		final Statistics courseStatistics;

		final Double lectureAverage;
		final Double lectureDeviation;
		final Double lectureMax;
		final Double lectureMin;

		final Double courseAverage;
		final Double courseDeviation;
		final Double courseMax;
		final Double courseMin;

		final Principal principal;
		final Integer userId;

		final List<Integer> estimatedTimes;
		final List<Integer> sessionDurations;

		principal = super.getRequest().getPrincipal();
		userId = principal.getActiveRoleId();

		numberOfLectureTheorical = this.repository.numberOfLectureTheorical(userId);
		numberOfLecturelHandsOn = this.repository.numberOfLectureHandsOn(userId);

		totalLecturesNumberByIndication = new HashMap<Indication, Integer>();
		totalLecturesNumberByIndication.put(Indication.THEORETICAL, numberOfLectureTheorical);
		totalLecturesNumberByIndication.put(Indication.HANDS_ON, numberOfLecturelHandsOn);

		lectureAverage = this.repository.lectureLearningTimeAverage(userId);
		lectureDeviation = 0.0;
		lectureMax = this.repository.lectureLearningTimeMax(userId);
		lectureMin = this.repository.lectureLearningTimeMin(userId);

		//		courseAverage = this.repository.courseLearningTimeAverage(userId);
		courseAverage = 0.0;
		courseDeviation = 0.0;
		//		courseMax = this.repository.courseLearningTimeMax(userId);
		courseMax = 0.;
		//		courseMin = this.repository.courseLearningTimeMin(userId);
		courseMin = 0.;

		lectureStatistics = new Statistics();
		lectureStatistics.setAverage(lectureAverage);
		lectureStatistics.setDeviation(lectureDeviation);
		lectureStatistics.setMax(lectureMax);
		lectureStatistics.setMin(lectureMin);

		courseStatistics = new Statistics();
		courseStatistics.setAverage(courseAverage);
		courseStatistics.setDeviation(courseDeviation);
		courseStatistics.setMax(courseMax);
		courseStatistics.setMin(courseMin);

		lecturerDashboard = new LecturerDashboard();
		lecturerDashboard.setTotalLecturesNumberByIndication(totalLecturesNumberByIndication);
		lecturerDashboard.setLectureStatistics(lectureStatistics);
		lecturerDashboard.setCourseStatistics(courseStatistics);

		super.getBuffer().setData(lecturerDashboard);
	}

	@Override
	public void unbind(final LecturerDashboard object) {
		Tuple tuple;
		final Principal principal;
		final Integer userId;
		Integer numLectureTeor;
		Integer numLectureHandsOn;

		numLectureTeor = object.getTotalLecturesNumberByIndication().get(Indication.THEORETICAL);
		numLectureHandsOn = object.getTotalLecturesNumberByIndication().get(Indication.HANDS_ON);

		principal = super.getRequest().getPrincipal();
		userId = principal.getActiveRoleId();

		tuple = super.unbind(object, "totalLecturesNumberByIndication", "lectureStatistics", "courseStatistics");
		tuple.put("ratioOfLecturesTheoretical", this.repository.numberOfLectureTheorical(userId) / this.repository.numberOfLectures(userId));
		tuple.put("ratioOfLecturesHandsOn", this.repository.numberOfLectureHandsOn(userId) / this.repository.numberOfLectures(userId));
		//tuple.put("numLectureTeor", numLectureTeor);
		//tuple.put("numLectureHandsOn", numLectureHandsOn);
		super.getResponse().setData(tuple);
	}
}
