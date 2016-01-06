package ch.zhaw.pm.fe.view.console;

import java.io.PrintStream;
import java.util.Map;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.view.console.text.ConsoleWriter;

public class ConsoleView {
	private SpotSelectionView spotSelectionView;
	private MaintenanceView maintenanceView;
	private InsertCoinView insertCoinView;
	private ErrorView errorView;
	private ConsoleWriter consoleWriter;

	public ConsoleView(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers, PrintStream out,
			PrintStream err) {

		consoleWriter = new ConsoleWriter(out, err, domainService.getLocale());
		this.spotSelectionView = new SpotSelectionView(consoleWriter,
				domainService);
		this.insertCoinView = new InsertCoinView(consoleWriter, domainService);
		this.maintenanceView = new MaintenanceView(consoleWriter,
				domainService, controllers);

		this.errorView = new ErrorView(consoleWriter, domainService,
				controllers);

	}

	/**
	 * @return the spotSelectionView
	 */
	public SpotSelectionView getSpotSelectionView() {
		return spotSelectionView;
	}

	/**
	 * @return the maintenanceView
	 */
	public MaintenanceView getMaintenanceView() {
		return maintenanceView;
	}

	/**
	 * @return the insertCoinView
	 */
	public InsertCoinView getInsertCoinView() {
		return insertCoinView;
	}

	/**
	 * @return the errorView
	 */
	public ErrorView getErrorView() {
		return errorView;
	}

	/**
	 * @return the consoleWriter
	 */
	public ConsoleWriter getConsoleWriter() {
		return consoleWriter;
	}

}
