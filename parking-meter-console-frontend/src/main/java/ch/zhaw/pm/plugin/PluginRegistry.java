package ch.zhaw.pm.plugin;

import java.util.List;

/**
 * The plugin registry handles loading the plugins from a folder with the name
 * plugin. A plugin can hold several components. The components it self must
 * extend from AbstractComponent. The components must be listed in the file
 * META-INF/services/ch.zhaw.pm.app.components.Component
 *
 */

public interface PluginRegistry {

	public <TPlugin extends Plugin> List<TPlugin> getPlugins(
			Class<TPlugin> pluginClass);

}
