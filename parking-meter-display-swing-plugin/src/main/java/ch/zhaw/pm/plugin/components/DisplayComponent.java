package ch.zhaw.pm.plugin.components;

import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.gui.Display;

public class DisplayComponent extends AbstractPluginComponent {

	private final Logger logger = Logger.getLogger(getClass().getName());

	public DisplayComponent() {
		this.name = "Display";
		this.type = PluginComponentType.VIEW;
	}

	/**
	 * Starts the component
	 */
	@Override
	public void start(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {
		super.start(domainService, controllers);
		if (domainService == null | controllers == null) {
			logger.info("domainSerivce is null");
		} else {
			Display ex = new Display(domainService, controllers);
			ex.setVisible(true);

		}

	}

}
