package ch.zhaw.pm.app.config;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.domain.service.ParkingMeterDomainServiceStub;
import ch.zhaw.pm.persistence.service.exception.PersistenceException;

/**
 * The Application factory provides a configure method to initialize the parking
 * meter with stub domain service configuration.
 *
 */
public class ApplicationFactoryStubImpl extends ApplicationFactory {

	/**
	 * Initialize the parking meter application with stub domain service
	 * configuration
	 * 
	 * @throws PersistenceException
	 */
	@Override
	public Application createApplication() throws PersistenceException {

		// domain service
		Application application = Application.getInstance();
		ParkingMeterDomainService domainService = new ParkingMeterDomainServiceStub(
				application.getConfigService(),
				application.getParkingMeterService(),
				application.getPaymentTransactionService());
		application.setDomainService(domainService);

		boot(application);

		return application;
	}
}
