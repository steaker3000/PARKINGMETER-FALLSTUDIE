package ch.zhaw.pm.fe.controller;

import ch.zhaw.pm.domain.exception.DomainException;
import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;

/**
 * Manages the state for the input mode. The default mode is selecting a parking
 * spot. After a parking spot has been selected, the user can insert coins
 * during the period {selectionTimeInSeconds}. After this period the state is
 * falling back to its default mode.
 *
 */

public class SpotSelectionStateController extends Controller {

	ParkingMeterDomainService domainService;

	/**
	 * 
	 * @param domainService
	 *            ParkingMeterDomainService instance for business logic
	 * @param selectionTimeInSeconds
	 *            time for keeping the state for inserting coins
	 */
	public SpotSelectionStateController(ParkingMeterDomainService domainService) {
		super();
		this.domainService = domainService;
	}

	/**
	 * Is processing the selection of a parking spot. It validates the spotNo
	 * against the domainService. If valid it starts a timer during the period
	 * {selectionTimeInSeconds} in which the user can insert coins.
	 * 
	 * @param spotNo
	 */
	@Override
	public void processRequest(String spotNo) {

		// start the active time for coins insert
		try {
			domainService.rescheduleTimer(spotNo);
		} catch (DomainException e) {
			super.setChanged();
			super.notifyObservers(new DomainException(
					ErrCode.PARKINGSPOTNOTVALID));
		}

	}

}
