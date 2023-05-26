
package acme.features.auditor.auditingRecords;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.auditingRecords.AuditingRecords;
import acme.framework.controllers.AbstractController;
import acme.roles.Auditor;

@Controller
public class AuditorAuditingRecordController extends AbstractController<Auditor, AuditingRecords> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AuditorAuditingRecordListService	listService;

	@Autowired
	AuditorAuditingRecordShowService	showService;

	@Autowired
	AuditorAuditingRecordCreateService	createService;

	@Autowired
	AuditorAuditingRecordUpdateService	updateService;

	@Autowired
	AuditorAuditingRecordDeleteService	deleteService;

	@Autowired
	AuditorAuditingRecordPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);

	}
}
