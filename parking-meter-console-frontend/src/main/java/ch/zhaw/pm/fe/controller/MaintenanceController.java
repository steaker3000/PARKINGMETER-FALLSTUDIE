package ch.zhaw.pm.fe.controller;

import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.model.SpotSelectionState;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.view.console.MaintenanceView.MaintenanceViewMessage;

/**
 * The maintenance controller is responsible for the processing of all
 * maintenance use cases. The processMessage method is parsing the message
 * through a domainService. The parser is routing is routing the processing to
 * the responsible method
 *
 */
public class MaintenanceController extends Controller {

	ParkingMeterDomainService domainService;
	boolean exitFlag = false;

	/**
	 * The controller needs a ParkingMeterDomainService and a MaintenanceView
	 * instance.
	 * 
	 * @param domainService
	 */
	public MaintenanceController(ParkingMeterDomainService domainService) {
		super();
		this.domainService = domainService;
	}

	/**
	 * Main entry point for processing the maintenance use cases. The message is
	 * parsed and processed.
	 * 
	 * @param message
	 * @return true if exit
	 */
	public void processRequest(String message) {

		if (!checkMaintenanceAllowed())
			return;
		String command = domainService.getMaintenanceCode(message)
				.toLowerCase();
		switch (command) {
		case "exit":
			super.setChanged();
			super.notifyObservers(MaintenanceViewMessage.EXIT);
			exitFlag = true;
			break;
		case "allinfo":
			super.setChanged();
			super.notifyObservers(MaintenanceViewMessage.ALLINFO);

			break;
		default:

		}

	}

	public boolean isExit() {
		return this.exitFlag;
	}

	private boolean checkMaintenanceAllowed() {
		// check the current state - maintenance only in SPOTNOTSELECT Mode
		// allowed
		SpotSelectionState state = domainService.getSpotSelectionState();
		if (state == SpotSelectionState.SPOTISSELECTED) {
			// in coin insert mode - no maintenance
			ErrCode errCode = ErrCode.MAINTENANCENOTALLOWED;
			setChanged();
			notifyObservers(errCode);
			return false;
		}
		return true;

	}

}
