package ch.zhaw.pm.domain.model;

import java.util.Observable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains the information about a coin.
 * 
 * @author Matthias Bachmann
 *
 */
@XmlRootElement
public class Coin extends Observable {
	protected double coinValue = 0;
	protected String currencyISO;

	public Coin() {
	}

	/**
	 * Constructs a coin.
	 * 
	 * @param coinValue
	 *            the value of the coin itself (e.g. 1.00 €).
	 * @param currencyString
	 *            the currency string (e.g. €)
	 */
	public Coin(double coinValue, String currencyISO) {
		this.coinValue = coinValue;
		this.currencyISO = currencyISO;
	}

	/**
	 * @return the coinValue
	 */
	public final double getCoinValue() {
		return coinValue;
	}

	/**
	 * @param coinValue
	 *            the coinValue to set
	 */
	public final void setCoinValue(double coinValue) {
		this.coinValue = coinValue;
	}

	/**
	 * @return the currencyISO
	 */
	public final String getCurrencyISO() {
		return currencyISO;
	}

	/**
	 * @param currencyISO
	 *            the currencyISO to set
	 */
	public final void setCurrencyISO(String currencyISO) {
		this.currencyISO = currencyISO;
	}

}
