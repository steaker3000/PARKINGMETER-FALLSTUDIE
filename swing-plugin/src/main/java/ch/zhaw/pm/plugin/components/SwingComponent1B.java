package ch.zhaw.pm.plugin.components;

import java.util.Map;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;

/**
 * 
 * A dummy component. Implements the start method
 * The service description must follow the path and the classname: 
 * path: src/main/resources META-INF/services
 * file: ch.zhaw.pm.plugin.components.PluginComponent
 * content: 
 * ch.zhaw.pm.plugin.components.SwingComponent1A
 * ch.zhaw.pm.plugin.components.SwingComponent1B

 *
 */
public class SwingComponent1B extends AbstractPluginComponent {

	public SwingComponent1B() {
		this.name = "Dummy SwingComponent1B";
		this.type = PluginComponentType.DUMMY;
	}
	
	/**
	 * Starts the component
	 */
	public void start(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {
		super.start(domainService, controllers);
	}

}
