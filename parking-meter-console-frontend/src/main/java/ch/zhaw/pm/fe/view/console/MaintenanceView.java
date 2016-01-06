package ch.zhaw.pm.fe.view.console;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.view.console.text.ConsoleWriter;
import ch.zhaw.pm.fe.view.console.text.MsgCode;

/**
 * The maintenance view is responsible outputting messages regarding the
 * maintenance use cases. Messages are printed to outstream through the provided
 * methods.
 *
 */
public class MaintenanceView implements Observer {

	private ParkingMeterDomainService domainService;

	public enum MaintenanceViewMessage {
		EXIT,
		ALLINFO
	}

	ConsoleWriter consoleWriter;

	public MaintenanceView(ConsoleWriter consoleWriter,
			ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {
		this.consoleWriter = consoleWriter;
		this.domainService = domainService;
		controllers.get(Command.MAINTENANCE).addObserver(this);

	}

	/**
	 * Observer update entry for the View. Processes the message according the
	 * MaintenanceViewMessage
	 * 
	 */
	@Override
	public void update(Observable o, Object message) {
		if (message instanceof MaintenanceViewMessage) {

			MaintenanceViewMessage viewMessage = (MaintenanceViewMessage) message;
			switch (viewMessage) {
			case EXIT:
				printExit();
				break;
			case ALLINFO:
				printAllInfo();

				break;
			}
		}
	}

	/**
	 * Prints the exit parking meter message
	 */
	private void printExit() {
		consoleWriter.println(MsgCode.EXIT);
	}

	/**
	 * Prints all info message to the supporter
	 */
	private void printAllInfo() {
		consoleWriter.println(MsgCode.ALLINFO);
		ViewHelper.printEnterSpotNo(consoleWriter, domainService);

	}

}
