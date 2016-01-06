package ch.zhaw.pm.domain.model;

import ch.zhaw.pm.domain.util.Helpers;

/**
 * Contains the information about a CoinBox (coin + amount of coins).
 * 
 *
 */
public class CoinBox extends Coin {

	private Integer id;
	private int amount = 0;

	public CoinBox() {
	}

	/**
	 * Constructs a coinbox.
	 * 
	 * @param coinValue
	 *            the value of the coin itself (e.g. 1.00 €).
	 * @param amount
	 *            the number of coins this coin cassette has.
	 * @param currencyString
	 *            the currency string (e.g. €)
	 */
	public CoinBox(Integer id, double coinValue, int amount, String currencyISO) {
		super(coinValue, currencyISO);
		this.id = id;
		this.amount = amount;
	}

	/**
	 * Constructs a coinbox.
	 * 
	 * @param coinValue
	 *            the value of the coin itself (e.g. 1.00 €).
	 * @param amount
	 *            the number of coins this coin cassette has.
	 * @param currencyString
	 *            the currency string (e.g. €)
	 */
	public CoinBox(double coinValue, int amount, String currencyISO) {
		super(coinValue, currencyISO);
		this.amount = amount;
	}

	/**
	 * @return the id
	 */
	public final Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the amount of coins in the box
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount of coins in the box
	 */
	public final void setAmount(int amount) {
		this.amount = amount;
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Formats a string with Coin. Example:<br>
	 * <code> 1.00 € | Amount: 2 | Value: 2.00 € | </code>
	 * 
	 * @return the formated string
	 */
	public String toString() {
		return "CoinBox: "
				+ Helpers.formatID(id)
				+ "  |  "
				+ Helpers.formatMoney(coinValue,
						Helpers.ISOtoCurrencySymbol(this.currencyISO))
				+ "  |  "
				+ "Amount: "
				+ Helpers.formatMoney(amount,
						Helpers.ISOtoCurrencySymbol(this.currencyISO)) + " | ";
	}
}
