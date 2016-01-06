package ch.zhaw.pm.fe.view.console;

import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import ch.zhaw.pm.domain.exception.DomainException;
import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.view.console.text.ConsoleWriter;

public class ErrorView implements Observer {

	ConsoleWriter consoleWriter;
	ParkingMeterDomainService domainService;

	/**
	 * Register all Controllers to ErrorView
	 * 
	 * @param consoleWriter
	 * @param domainService
	 * @param map
	 */
	public ErrorView(ConsoleWriter consoleWriter,
			ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {
		this.consoleWriter = consoleWriter;
		this.domainService = domainService;
		Collection<Controller> ctrls = controllers.values();

		ctrls.forEach(o -> o.addObserver(this));
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof ErrCode) {
			ErrCode errCode = (ErrCode) arg;
			consoleWriter.println(errCode);
			writeInstructions();

		} else if (arg instanceof String) {
			String err = (String) arg;
			consoleWriter.printErrln(err);
			writeInstructions();

		} else if (arg instanceof DomainException) {
			DomainException err = (DomainException) arg;
			consoleWriter.println(err.getErrCode());
			writeInstructions();
		}

	}

	/*
	 * Write instructions to the customer about insert coins or enter spotNo
	 */
	private void writeInstructions() {
		switch (domainService.getSpotSelectionState()) {
		case SPOTISSELECTED:
			ViewHelper.printInsertCoins(consoleWriter, domainService);
			break;
		case SPOTNOTSELECTED:
			ViewHelper.printEnterSpotNo(consoleWriter, domainService);
		}

	}

}
