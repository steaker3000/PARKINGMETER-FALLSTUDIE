package ch.zhaw.pm.app;

import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import ch.zhaw.pm.app.config.Application;
import ch.zhaw.pm.app.config.ApplicationFactory;
import ch.zhaw.pm.domain.exception.DomainException;
import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.controller.FrontController;
import ch.zhaw.pm.fe.view.console.ConsoleView;
import ch.zhaw.pm.fe.view.console.ViewHelper;
import ch.zhaw.pm.fe.view.console.text.MsgCode;
import ch.zhaw.pm.persistence.service.exception.PersistenceException;

/**
 * Provides the main entry for initialization and message loop of the parking
 * meter
 *
 */

public class App {

	private final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * main entry point to parking meter.
	 * 
	 * @param applicationFactory
	 *            contains bindings to the dependent components (rest or stand
	 *            alone)
	 * @throws PersistenceException
	 * @throws DomainException
	 */
	public void run(ApplicationFactory applicationFactory)
			throws PersistenceException, DomainException {

		logger.info(MsgCode.BOOTINGPARKINGMETER.getMsg());

		Application application = createApplication(applicationFactory);

		doConsoleInput(application.getDomainService(),
				application.getControllers());

		System.exit(0);

	}

	/**
	 * create the application with a a factory - handles the exception with the
	 * logger
	 * 
	 * @param applicationFactory
	 * @return
	 * @throws DomainException
	 */
	private Application createApplication(ApplicationFactory applicationFactory)
			throws DomainException {
		Application application;
		try {
			application = applicationFactory.createApplication();

		} catch (Exception e) {
			logger.info(e.getMessage() + "\n" + e.getCause());
			logger.severe(ErrCode.DBOPENBYOTHERPROCESS.getMsg());
			throw new DomainException(ErrCode.DBOPENBYOTHERPROCESS);
		}
		return application;

	}

	/**
	 * Handles the console input by reading the console and sending the commands
	 * to the front controller
	 * 
	 * @param domainService
	 * @param controllers
	 */
	private void doConsoleInput(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {

		ConsoleView cv = new ConsoleView(domainService, controllers,
				System.out, System.err);

		ViewHelper.printWelcomeMessage(cv.getConsoleWriter());
		ViewHelper.printEnterSpotNo(cv.getConsoleWriter(), domainService);

		Scanner scanner = new Scanner(System.in);

		FrontController fc = (FrontController) controllers.get(Command.ALL);

		boolean exitFlag = false;
		while (!exitFlag) {
			String message = scanner.next();
			fc.processRequest(message);
			exitFlag = fc.isExit();
		}

		scanner.close();

	}

}
