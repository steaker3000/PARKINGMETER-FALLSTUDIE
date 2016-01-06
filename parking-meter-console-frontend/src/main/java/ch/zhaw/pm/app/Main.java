package ch.zhaw.pm.app;

import ch.zhaw.pm.app.config.ApplicationFactoryStubImpl;

/**
 * Main Entry point for a JAVA SE console front end - communication over rest
 * with a backend
 *
 */
public class Main {

	public static void main(String[] args) {

		try {
			new App().run(new ApplicationFactoryStubImpl());
		} catch (Exception e) {
			e.getMessage();
		}
	}

}