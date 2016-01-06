package ch.zhaw.pm.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * The plugin registry handles loading the plugins from a folder with the name
 * plugin. A plugin can hold several components. The components it self must
 * extend from AbstractComponent. The components must be listed in the file
 * META-INF/services/ch.zhaw.pm.app.components.Component
 *
 */
public class ServiceLoaderPluginRegistry implements PluginRegistry {

	private final static Logger logger = Logger
			.getLogger(ServiceLoaderPluginRegistry.class.getName());

	private ClassLoader pluginClassLoader;

	private AtomicBoolean initialized = new AtomicBoolean();

	protected String pluginLookupPath = System.getProperty(
			"ch.zhaw.plugin.lookupPath", "./plugin/");

	protected String pluginFileExtension = ".ext.jar";

	protected FilenameFilter pluginJarFilter = createPluginJarFilter();

	public void init() {
		this.pluginClassLoader = createPluginClassLoader(lookupPluginUrls());
		this.initialized.set(true);
	}

	/**
	 * Return the found plugin components
	 */
	@Override
	public <TPlugin extends Plugin> List<TPlugin> getPlugins(
			Class<TPlugin> pluginClass) {

		if (!this.initialized.get()) {
			init();
		}

		ServiceLoader<TPlugin> pluginLoader = ServiceLoader.load(pluginClass,
				getPluginClassLoader());

		List<TPlugin> plugins = new ArrayList<TPlugin>();
		for (Iterator<TPlugin> iter = pluginLoader.iterator(); iter.hasNext();) {
			TPlugin plugin = iter.next();
			logger.info(String.format("Found plugin for %s: %s from Path: %s",
					pluginClass.getName(), plugin, plugin.getClass()
							.getProtectionDomain().getCodeSource()
							.getLocation()));
			plugins.add(plugin);
		}

		if (plugins.isEmpty()) {
			logger.info(String.format("No plugins were found for %s",
					pluginClass.getName()));
		}

		return plugins;
	}

	/*
	 * Creates a class loader with a list of found plugins
	 */
	private URLClassLoader createPluginClassLoader(List<URL> pluginJars) {
		return new URLClassLoader(
				pluginJars.toArray(new URL[pluginJars.size()]));
	}

	/*
	 * Search for plugins in the plugin folder
	 */
	private List<URL> lookupPluginUrls() {
		File pluginFolder = new File(getPluginLookupPath());

		logger.info("Using plugin folder: " + pluginFolder.getAbsolutePath());

		List<URL> pluginUrlList = new ArrayList<URL>();
		File[] pluginJars = pluginFolder.listFiles(getPluginJarFilter());

		if (pluginJars == null || pluginJars.length == 0) {
			logger.info("Found 0 plugin jars");
		} else {
			logger.info(String.format("Found %s plugins", pluginJars.length));
			for (File pluginJar : pluginJars) {
				try {
					URL pluginJarUrl = pluginJar.toURI().toURL();
					logger.info("Found plugin jar: " + pluginJarUrl);
					pluginUrlList.add(pluginJarUrl);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}

		return pluginUrlList;
	}

	protected FilenameFilter createPluginJarFilter() {
		return new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(pluginFileExtension);
			}
		};
	}

	public String getPluginLookupPath() {
		return pluginLookupPath;
	}

	public void setPluginLookupPath(String pluginLookupPath) {
		this.pluginLookupPath = pluginLookupPath;
	}

	public String getPluginFileExtension() {
		return pluginFileExtension;
	}

	public void setExtensionFileExtension(String pluginFileExtension) {
		this.pluginFileExtension = pluginFileExtension;
	}

	public FilenameFilter getPluginJarFilter() {
		return pluginJarFilter;
	}

	public void setPluginJarFilter(FilenameFilter pluginJarFilter) {
		this.pluginJarFilter = pluginJarFilter;
	}

	public ClassLoader getPluginClassLoader() {
		return pluginClassLoader;
	}

	public void setPluginClassLoader(ClassLoader pluginClassLoader) {
		this.pluginClassLoader = pluginClassLoader;
	}

}
