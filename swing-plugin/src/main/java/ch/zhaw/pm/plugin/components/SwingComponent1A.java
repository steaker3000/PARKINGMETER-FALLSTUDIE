package ch.zhaw.pm.plugin.components;

import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.gui.ParkingSpotButtons;

/**
 * Swing component for Parking Selection Buttons. Implements the start method
 * The service description must follow the path and the classname: 
 * path: src/main/resources META-INF/services
 * file: ch.zhaw.pm.plugin.components.PluginComponent
 * content: 
 * ch.zhaw.pm.plugin.components.SwingComponent1A
 * ch.zhaw.pm.plugin.components.SwingComponent1B
 *
 */
public class SwingComponent1A extends AbstractPluginComponent {

	private final Logger logger = Logger.getLogger(getClass().getName());

	public SwingComponent1A() {
		this.name = "Parking Spot Buttons";
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
			ParkingSpotButtons ex = new ParkingSpotButtons(domainService, controllers);
			ex.setVisible(true);

		}

	}



}
