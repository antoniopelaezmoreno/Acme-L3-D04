
package acme.features.auditor.auditorDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.forms.AuditorDashboard;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditortDashboardShowService extends AbstractService<Auditor, AuditorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	//	@Override
	//	public void load() {
	//		final AuditorDashboard auditorDashboard;
	//		final Map<Indication, Integer> numberAuditsByIndication;
	//
	//		final Integer numberOfAuditsTheorical = numberAuditsByIndication.get(Indication.THEORETICAL);
	//		final Integer numberOfAuditsHandsOn = numberAuditsByIndication.get(Indication.HANDS_ON);
	//
	//		final Statistics auditStatistics;
	//		final Statistics auditingRecordStatistics;
	//
	//		final Double auditAverage;
	//		final Double auditDeviation;
	//		final Double auditMax;
	//		final Double auditMin;
	//
	//		final Double auditingRecordAverage;
	//		final Double auditingRecordDeviation;
	//		final Double auditingRecordMax;
	//		final Double auditingRecordMin;
	//
	//		final Principal principal;
	//		final Integer userId;
	//
	//		final List<Integer> estimatedTimes;
	//		final List<Integer> auditingRecordDurations;
	//
	//		principal = super.getRequest().getPrincipal();
	//		userId = principal.getActiveRoleId();
	//
	//		numberOfAuditsTheorical = this.repository.numberOfTutorialTheorical(userId);
	//		numberOfAuditsHandsOn = this.repository.numberOfTutorialHandsOn(userId);
	//
	//		tutorialAverage = this.repository.tutorialAverage(userId);
	//		estimatedTimes = this.repository.getTutorialEstimatedTimes(userId);
	//		tutorialDeviation = Math.sqrt(estimatedTimes.stream().mapToDouble(Integer::doubleValue).map(time -> Math.pow(time - estimatedTimes.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0), 2)).average().orElse(0.0));
	//		tutorialMax = this.repository.tutorialMax(userId);
	//		tutorialMin = this.repository.tutorialMin(userId);
	//
	//		sessionAverage = this.repository.sessionAverage(userId);
	//		sessionDurations = this.repository.getSessionDurations(userId);
	//		sessionDeviation = Math.sqrt(sessionDurations.stream().mapToDouble(Integer::doubleValue).map(duration -> Math.pow(duration - sessionDurations.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0), 2)).average().orElse(0.0));
	//		sessionMax = this.repository.sessionMax(userId);
	//		sessionMin = this.repository.sessionMin(userId);
	//
	//		tutorialStatistics = new Statistics();
	//		tutorialStatistics.setAverage(tutorialAverage);
	//		tutorialStatistics.setDeviation(tutorialDeviation);
	//		tutorialStatistics.setMax(tutorialMax);
	//		tutorialStatistics.setMin(tutorialMin);
	//
	//		sessionStatistics = new Statistics();
	//		sessionStatistics.setAverage(sessionAverage);
	//		sessionStatistics.setDeviation(sessionDeviation);
	//		sessionStatistics.setMax(sessionMax);
	//		sessionStatistics.setMin(sessionMin);
	//
	//		auditorDashboard = new AuditorDashboard();
	//		auditorDashboard.setNumberOfTutorialTheorical(numberOfTutorialTheorical);
	//		auditorDashboard.setNumberOfTutorialHandsOn(numberOfTutorialHandsOn);
	//		auditorDashboard.setSessionStatistics(sessionStatistics);
	//		auditorDashboard.setTutorialStatistics(tutorialStatistics);
	//
	//		super.getBuffer().setData(assistantDashboard);
	//	}
	//
	//	@Override
	//	public void unbind(final AuditorDashboard object) {
	//		Tuple tuple;
	//		final Principal principal;
	//		final Integer userId;
	//		final Integer numberOfTutorialBalanced;
	//
	//		principal = super.getRequest().getPrincipal();
	//		userId = principal.getActiveRoleId();
	//
	//		numberOfTutorialBalanced = this.repository.numberOfTutorialBalanced(userId);
	//
	//		tuple = super.unbind(object, "numberOfTutorialTheorical", "numberOfTutorialHandsOn", "sessionStatistics", "tutorialStatistics");
	//		tuple.put("ratioOfCourseTheoretical", object.getNumberOfTutorialTheorical() / this.repository.numberTutorials(userId));
	//		tuple.put("ratioOfCourseHandsOn", object.getNumberOfTutorialHandsOn() / this.repository.numberTutorials(userId));
	//		tuple.put("ratioOfCourseBalanced", numberOfTutorialBalanced / this.repository.numberTutorials(userId));
	//
	//		tuple.put("ratioOfSessionTheoretical", this.repository.getNumberOfSessionTheorical(userId) / this.repository.numberSessions(userId));
	//		tuple.put("ratioOfSessionHandsOn", this.repository.getNumberOfSessionHandsOn(userId) / this.repository.numberSessions(userId));
	//		tuple.put("ratioOfSessionBalanced", this.repository.numberOfSessionBalanced(userId) / this.repository.numberSessions(userId));
	//
	//		super.getResponse().setData(tuple);
	//	}

}
