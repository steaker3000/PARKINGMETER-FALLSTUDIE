package ch.zhaw.pm.fe.controller;

import ch.zhaw.pm.domain.exception.DomainException;
import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;

/**
 * The controller is processing the insert coin use case. If the parsing of
 * coinValue was successful then the domainService shall process the transaction
 * with persisting.
 *
 */
public class InsertCoinController extends Controller {

	ParkingMeterDomainService domainService;
	String currencyString;

	public InsertCoinController(ParkingMeterDomainService domainService) {
		this.domainService = domainService;
		currencyString = domainService.getCurrencyString();
	}

	/**
	 * Parses the argument. If valid
	 * 
	 * @param arg
	 * @param spotNo
	 * @param transactionCode
	 */
	public void processRequest(String arg) {

		String spotNo = domainService.getSelectedSpotNo();
		double coinValue = 0;
		try {
			coinValue = Double.parseDouble(arg);
		} catch (NumberFormatException e) {
			super.setChanged();
			super.notifyObservers(ErrCode.INVALIDCOIN);
			return;
		}

		try {
			domainService.processCoinInsert(coinValue, spotNo);
		} catch (DomainException e) {
			super.setChanged();
			super.notifyObservers(e);
		}
	}
}
