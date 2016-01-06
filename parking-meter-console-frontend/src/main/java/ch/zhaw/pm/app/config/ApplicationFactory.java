package ch.zhaw.pm.app.config;

import java.util.logging.Logger;

import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Dispatcher;
import ch.zhaw.pm.fe.view.console.text.MsgCode;
import ch.zhaw.pm.persistence.service.exception.PersistenceException;
import ch.zhaw.pm.plugin.ServiceLoaderPluginRegistry;
import ch.zhaw.pm.plugin.components.PluginComponent;

/**
 * The Application factory interface provides a configure method to initialize
 * the parking meter with the desired configuration.
 *
 */
public abstract class ApplicationFactory {

	private final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * configure the parking meter application with the desired configuration
	 * (standalone, rest, cdi with wedl)
	 * 
	 * @param application
	 * @throws PersistenceException
	 */
	public abstract Application createApplication() throws PersistenceException;

	/**
	 * Boots the application
	 * 
	 * @param application
	 *            Application instance
	 * @param domainService
	 *            DomainService instance
	 * @throws PersistenceException
	 */
	protected void boot(Application application) throws PersistenceException {

		// Read the config File and initialize the parking meter
		bootDomainService(application);

		application.setPluginRegistry(new ServiceLoaderPluginRegistry());

		// create the dispatcher - holds all controllers
		Dispatcher dispatcher = new Dispatcher(application.getDomainService());
		application.setControllers(dispatcher.getControllers());

		// finally start the plugins
		startPlugins(application);

	}

	/**
	 * Start all Plugin Components
	 * 
	 * @param application
	 */
	private void startPlugins(Application application) {
		for (PluginComponent component : application.getPluginRegistry()
				.getPlugins(PluginComponent.class)) {
			application.getDomainService();
			component.start(application.getDomainService(),
					application.getControllers());
		}
	}

	/**
	 * Read the configFile and initialize the parking meter
	 * 
	 * @param domainService
	 * @param initialView
	 * @throws PersistenceException
	 */
	private ParkingMeterDomainService bootDomainService(Application application)
			throws PersistenceException {

		ParkingMeterDomainService domainService = application
				.getDomainService();
		logger.info(MsgCode.READCONFIG.getMsg());
		try {
			domainService.readConfig();
		} catch (PersistenceException e) {
			logger.info(ErrCode.READCONFIGFILE.getMsg());
			throw e;
		}

		try {
			domainService.initParkingMeter();

		} catch (PersistenceException e) {
			logger.info(ErrCode.INITPARKINGMETER.getMsg() + "\n"
					+ e.getMessage() + "\n" + e.getCause());
			throw e;
		}

		return domainService;

	}

}