package ch.zhaw.pm.app.config;

import java.util.Map;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.persistence.service.ConfigService;
import ch.zhaw.pm.persistence.service.ParkingMeterService;
import ch.zhaw.pm.persistence.service.PaymentTransactionService;
import ch.zhaw.pm.plugin.PluginRegistry;

/**
 * Application is a singleton for services for the parking meter
 * 
 *
 */
public class Application {

	public static synchronized Application getInstance() {
		if (instance == null) {
			instance = new Application();
		}
		return instance;
	}

	private static Application instance;

	// Controllers of MVC
	private Map<Command, Controller> controllers;

	// Plugins
	private PluginRegistry pluginRegistry;

	// Domain Service
	private ParkingMeterDomainService domainService = null;

	// Persistence Service
	private ConfigService configService = null;
	private ParkingMeterService parkingMeterService = null;
	private PaymentTransactionService paymentTransactionService = null;

	/**
	 * @return the domainService
	 */
	public final ParkingMeterDomainService getDomainService() {
		return domainService;
	}

	/**
	 * @param domainService
	 *            the domainService to set
	 */
	public final void setDomainService(ParkingMeterDomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the configService
	 */
	public final ConfigService getConfigService() {
		return configService;
	}

	/**
	 * @param configService
	 *            the configService to set
	 */
	public final void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	/**
	 * @return the parkingMeterService
	 */
	public final ParkingMeterService getParkingMeterService() {
		return parkingMeterService;
	}

	/**
	 * @param parkingMeterService
	 *            the parkingMeterService to set
	 */
	public final void setParkingMeterService(
			ParkingMeterService parkingMeterService) {
		this.parkingMeterService = parkingMeterService;
	}

	/**
	 * @return the paymentTransactionService
	 */
	public final PaymentTransactionService getPaymentTransactionService() {
		return paymentTransactionService;
	}

	/**
	 * @param paymentTransactionService
	 *            the paymentTransactionService to set
	 */
	public final void setPaymentTransactionService(
			PaymentTransactionService paymentTransactionService) {
		this.paymentTransactionService = paymentTransactionService;
	}

	public PluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}

	public void setPluginRegistry(PluginRegistry pluginRegistry) {
		this.pluginRegistry = pluginRegistry;
	}

	public Map<Command, Controller> getControllers() {
		return controllers;
	}

	public void setControllers(Map<Command, Controller> map) {
		this.controllers = map;
	}

}
