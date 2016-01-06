package ch.zhaw.pm.domain.service;

import java.util.List;
import java.util.Locale;
import java.util.Observer;

import ch.zhaw.pm.domain.exception.DomainException;
import ch.zhaw.pm.domain.model.SpotSelectionState;
import ch.zhaw.pm.persistence.service.exception.PersistenceException;

/**
 * The domain service contains the business logic for the parking meter. It
 * holds the model instances parkingMeter and parkintMeterConfig. The service is
 * updating the model classes.
 * 
 *
 */

public interface ParkingMeterDomainService {

	/**
	 * Read the configuration file config.properties. If the file does not exist
	 * a new one is created
	 * 
	 * @throws PersistenceException
	 */
	public void readConfig() throws PersistenceException;

	/**
	 * Get the Configuration information and the parkingMeter instance.
	 * 
	 * @throws PersistenceException
	 */
	public void initParkingMeter() throws PersistenceException;

	/**
	 * Get the remaining parking time in minutes from a parking spot
	 * 
	 * @param spotNo
	 * @return
	 */
	public int getParkingTime(String spotNo);

	/**
	 * Validate user input of the parking spot no
	 * 
	 * @param spotNo
	 * @return
	 */
	public boolean validateSpotNo(String spotNo);

	/**
	 * Verify if the user input is a maintenance code
	 * 
	 * @return
	 */
	public boolean isMaintenanceCode(String key);

	/**
	 * Get the maintenance code based on the secret key
	 * 
	 * @param key
	 * @return
	 */
	public String getMaintenanceCode(String key);

	/**
	 * Process the transaction of a coin insert. The payment transaction service
	 * is used and the dependent coin box amount is incremented.
	 * 
	 * @param coinValue
	 *            inserted coin value
	 * @param spotNo
	 *            parking spot number
	 * @param transactionCode
	 * @throws DomainException
	 */
	public void processCoinInsert(double coinValue, String spotNo)
			throws DomainException;

	/**
	 * Gets the ISO currency string from the parking meter configuration
	 * 
	 * @return IOS currency string like CHF or EUR
	 */
	public String getCurrencyString();

	/**
	 * Gets the locale object from the parking meter configuration
	 * 
	 * @return the locale object
	 */
	public Locale getLocale();

	/**
	 * The time in seconds during a customer can insert coins. After this time,
	 * the customer must select a parking spot no once more.
	 * 
	 * @return the time in seconds
	 */
	public int getSelectionTimeInSeconds();

	/**
	 * Checks if the inserted coin is a valid coin in the parking meter
	 * configuration
	 * 
	 * @return true if valid
	 */
	public boolean isValidCoin(double coinValue);

	/**
	 * Get a list of coin values from the ParkingMeterConfiguration
	 * 
	 * @return an List of coin values
	 */
	public List<Double> getCoinValues();

	/**
	 * Get a list of parkingSpots from the ParkingMeterConfiguration
	 * 
	 * @return
	 */
	public List<String> getParkingSpots();

	/**
	 * Simulates the payment to a certain spot verifying if the max parking time
	 * will exceed.
	 * 
	 * @param spotNo
	 * @param coinValue
	 * @return true if parking time would exceed
	 */
	public boolean isMoreThenMaxParkingTime(String spotNo, double coinValue);

	/**
	 * The selected spot Number
	 * 
	 * @return spotNo
	 */
	public String getSelectedSpotNo();

	/**
	 * The selected spot Number
	 * 
	 * @return spotNo
	 */
	public long getTransactionCode();

	/**
	 * The SpotSelectionState (insert coin mode)
	 * 
	 * @return SpotSelectionState
	 */

	public SpotSelectionState getSpotSelectionState();

	/**
	 * The timer for coin insert time is rescheduled to its original value
	 * {selectionTimeInSeconds}.
	 * 
	 * @param spotNo
	 *            the parking spot no
	 * @throws DomainException
	 */
	public void rescheduleTimer(String spotNo) throws DomainException;

	/**
	 * adds an Observer to the ParkingSpotSelectionTimer
	 * 
	 * @param o
	 *            the Observer to add
	 */
	void addObserverToSpotSelectionTimer(Observer o);

	/**
	 * adds an Observer to each of the ParkingSpotMeters
	 * 
	 * @param o
	 *            the Observer to add
	 */
	void addObserverToParkingSpotMeters(Observer o);

	/**
	 * adds an Observer to each of the CoinBoxes
	 * 
	 * @param o
	 *            the Observer to add
	 */
	void addObserverToCoinBoxes(Observer o);

}