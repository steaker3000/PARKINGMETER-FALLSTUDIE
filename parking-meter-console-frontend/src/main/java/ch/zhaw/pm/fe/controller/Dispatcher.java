package ch.zhaw.pm.fe.controller;

import java.util.HashMap;
import java.util.Map;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;

public class Dispatcher {

	private Map<Command, Controller> controllers = new HashMap<Command, Controller>();

	/**
	 * Instantiates all Controllers
	 * 
	 * @param domainService
	 */
	public Dispatcher(ParkingMeterDomainService domainService) {

		controllers.put(Command.ALL, new FrontController(domainService, this));

		controllers.put(Command.SELECTSPOT, new SpotSelectionStateController(
				domainService));
		controllers.put(Command.INSERTCOIN, new InsertCoinController(
				domainService));
		controllers.put(Command.MAINTENANCE, new MaintenanceController(
				domainService));

	}

	/**
	 * Dispatching the request to the controller indicated by the command
	 * 
	 * @param command
	 *            Command enum
	 * @param arg
	 *            message
	 * @return true if exitFlag
	 */

	public boolean dispatchRequest(Command command, String arg) {

		Controller controller = controllers.get(command);
		controller.processRequest(arg);
		if (command == Command.MAINTENANCE) {
			MaintenanceController mc = (MaintenanceController) controller;
			return mc.isExit();
		}
		return false;

	}

	/**
	 * @return the controllers
	 */
	public Map<Command, Controller> getControllers() {
		return controllers;
	}

	/**
	 * Adds a new controller to the dispatcher
	 * 
	 * @param command
	 *            from Command enum
	 * @param controller
	 *            a SubClass from Controller
	 */
	public void addController(Command command, Controller controller) {
		controllers.put(command, controller);

	}

	/**
	 * Console Commands
	 *
	 */

	public enum Command {
		ALL(""),
		SELECTSPOT("S"),
		INSERTCOIN("C"),
		MAINTENANCE("");
		String commandString;

		Command(String commandString) {
			this.commandString = commandString;
		}

		/**
		 * @return the command
		 */
		public final String getCmdString() {
			return commandString;
		}

		/**
		 * Return the message String from message number
		 * 
		 * @param msgNo
		 * @return the message String
		 */
		public static Command getCommand(String command) {
			for (Command e : Command.values()) {
				if (command == e.getCmdString())
					return e;
			}
			return null;
		}
	}

}
