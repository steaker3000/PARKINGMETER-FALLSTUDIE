package ch.zhaw.pm.fe.controller;

import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;

/**
 * 
 * The front controller is parsing the user messages. Based on the parsing
 * result the processing of the messages is routed to the related specialized
 * controller.
 *
 */
public class FrontController extends Controller {

	private boolean exitFlag = false;
	private ParkingMeterDomainService domainService;
	private Dispatcher dispatcher;

	/**
	 * 
	 * @param domainService
	 */
	public FrontController(ParkingMeterDomainService domainService,
			Dispatcher dispatcher) {

		this.domainService = domainService;
		this.dispatcher = dispatcher;

	}

	/**
	 * Parsing the user message. Checks if it is a select spotNo, an insert coin
	 * or a maintenance code
	 * 
	 * @param message
	 *            s{x} select spot, c{x} insert coin, {xxxx} maintenance code
	 * @return true if maintenance code exit is recognized
	 */
	@Override
	public synchronized void processRequest(String message) {

		String cmd = message.trim().substring(0, 1).toUpperCase();
		String arg = message.trim().substring(1);

		if (cmd.equalsIgnoreCase(Command.SELECTSPOT.getCmdString()))
			spotSelectionMessageHandler(arg);
		else if (cmd.equalsIgnoreCase(Command.INSERTCOIN.getCmdString()))
			insertCoinMessageHandler(arg);
		else
			defaultMessageHandler(message);

	}

	/**
	 * @return the exit flag
	 */

	public boolean isExit() {

		return this.exitFlag;
	}

	/**
	 * Takes care about spot selection
	 * 
	 * @param spotNo
	 */
	private void spotSelectionMessageHandler(String spotNo) {

		dispatcher.dispatchRequest(Command.SELECTSPOT, spotNo);

	}

	/**
	 * Takes care about coin insert message. Verifies if the parking meter is in
	 * correct state to accept coins
	 * 
	 * @param coinValue
	 */
	private void insertCoinMessageHandler(String coinValue) {

		dispatcher.dispatchRequest(Command.INSERTCOIN, coinValue);

	}

	/**
	 * checks for maintenance code
	 * 
	 * @param message
	 * @return false if no exit maintenance code was chosen
	 */
	private void defaultMessageHandler(String message) {

		// check if maintenance code -> if not error message
		if (!domainService.isMaintenanceCode(message)) {
			setChanged();
			notifyObservers(ErrCode.UNKNOWNCOMMAND);
		} else {

			this.exitFlag = dispatcher.dispatchRequest(Command.MAINTENANCE,
					message);

		}

	}

}
