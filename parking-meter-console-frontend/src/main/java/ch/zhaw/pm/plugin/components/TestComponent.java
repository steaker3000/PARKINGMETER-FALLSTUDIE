package ch.zhaw.pm.plugin.components;

import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;

public class TestComponent extends AbstractPluginComponent {

	private final Logger logger = Logger.getLogger(getClass().getName());

	public TestComponent() {
		super();
		this.name = "Dummy Test Component";
		this.type = PluginComponentType.INPUT;
	}

	@Override
	public void start(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {
		super.start(domainService, controllers);

		if (domainService == null | controllers == null) {
			logger.info("domainSerivce is null");
		} else {

		}

	}

}
