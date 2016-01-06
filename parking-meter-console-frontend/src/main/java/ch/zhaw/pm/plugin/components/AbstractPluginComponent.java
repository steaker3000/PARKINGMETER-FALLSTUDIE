package ch.zhaw.pm.plugin.components;

import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;

/**
 * Abstract Component, defining minimum implementation detail
 *
 */
public abstract class AbstractPluginComponent implements PluginComponent {

	protected PluginComponentType type = PluginComponentType.DUMMY;
	protected String name = "A dummy component";

	@Override
	public void start(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {

		Logger.getLogger(getClass().getName()).info(
				"Started " + getClass().getSimpleName() + ", Type ='"
						+ type.getTypeName() + "'" + ", Name='" + name + "'");
	}

	@Override
	public PluginComponentType getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

}
