package ch.zhaw.pm.fe.controller;

import java.util.Observable;

/**
 * Common Controller class
 *
 */
public abstract class Controller extends Observable {
	public abstract void processRequest(String message);

}
