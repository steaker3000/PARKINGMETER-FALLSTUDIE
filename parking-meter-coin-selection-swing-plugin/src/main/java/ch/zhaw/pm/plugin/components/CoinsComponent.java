package ch.zhaw.pm.plugin.components;

import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.gui.CoinButtons;
import ch.zhaw.pm.plugin.components.AbstractPluginComponent;
import ch.zhaw.pm.plugin.components.PluginComponentType;

public class CoinsComponent extends AbstractPluginComponent {

	private final Logger logger = Logger.getLogger(getClass().getName());

	public CoinsComponent() {
		this.name = "Coin Buttons";
		this.type = PluginComponentType.INPUT;
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
			CoinButtons ex = new CoinButtons(domainService, controllers);
			ex.setVisible(false);

		}

	}

}
