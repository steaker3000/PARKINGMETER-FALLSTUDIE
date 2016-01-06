package ch.zhaw.pm.plugin.components;

import java.util.Map;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.plugin.Plugin;

public interface PluginComponent extends Plugin {

	void start(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers);

	PluginComponentType getType();

	String getName();

}
