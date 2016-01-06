package ch.zhaw.pm.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observer;

import ch.zhaw.pm.domain.exception.DomainException;
import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.model.CoinBox;
import ch.zhaw.pm.domain.model.ParkingSpotMeter;
import ch.zhaw.pm.domain.model.SpotSelectionState;
import ch.zhaw.pm.domain.model.SpotSelectionTimer;
import ch.zhaw.pm.domain.util.DateTime;
import ch.zhaw.pm.domain.util.Helpers;
import ch.zhaw.pm.persistence.service.ConfigService;
import ch.zhaw.pm.persistence.service.ParkingMeterService;
import ch.zhaw.pm.persistence.service.PaymentTransactionService;
import ch.zhaw.pm.persistence.service.exception.PersistenceException;

/**
 * Stub for testing purposes. Simulates the model and persistence services. Can
 * be used to develop the front end with out backend. Contains the domain
 * services for the parking meter. Provides business logic and business rules to
 * the front end
 *
 */

public class ParkingMeterDomainServiceStub implements ParkingMeterDomainService {

	// Services
	@SuppressWarnings("unused")
	private ConfigService configService = null;
	@SuppressWarnings("unused")
	private ParkingMeterService parkingMeterService = null;
	@SuppressWarnings("unused")
	private PaymentTransactionService paymentTransactionService = null;

	private final int MAXPARKINGTIME = 240;
	private final int MINPARKINGTIME = -240;
	private final String EXITCODE = "9999";
	private final String ALLINFOCODE = "1234";
	private final String CURRENCYSTRING = "CHF";
	private final String LOCALESTRING = "de_CH";
	private final int COININSERTIMEINSECONDS = 10;

	private final double[] COINVALUES = { 0.50, 1.0, 2.0 };
	private final String[] PARKINGSPOTNOS = { "11", "12", "13" };
	private final double COINTOTIMEFACTOR = 60;
	private List<CoinBox> coinBoxes = null;
	private List<ParkingSpotMeter> parkingSpotMeters = null;
	private SpotSelectionTimer spotSelectionTimer;

	/**
	 * Initialize the ParkingMeterDomainService with instances from the
	 * persistence services
	 * 
	 * @param configService
	 * @param parkingMeterService
	 * @param paymentTransactionService
	 * @throws PersistenceException
	 */
	public ParkingMeterDomainServiceStub(ConfigService configService,
			ParkingMeterService parkingMeterService,
			PaymentTransactionService paymentTransactionService) {
		this.configService = configService;
		this.parkingMeterService = parkingMeterService;
		this.paymentTransactionService = paymentTransactionService;

		this.spotSelectionTimer = new SpotSelectionTimer(COININSERTIMEINSECONDS);
		createMockCoinValues();
		createMockParkingSpots();

	}

	private void createMockCoinValues() {
		coinBoxes = new ArrayList<CoinBox>();
		for (double value : COINVALUES) {
			coinBoxes.add(new CoinBox(value, 0, CURRENCYSTRING));
		}

	}

	private void createMockParkingSpots() {

		parkingSpotMeters = new ArrayList<ParkingSpotMeter>();
		for (String spotNo : PARKINGSPOTNOS) {
			parkingSpotMeters.add(new ParkingSpotMeter(spotNo));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.pm.domain.service.ParkingMeterDomainService#readConfig()
	 */
	@Override
	public void readConfig() throws PersistenceException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#initParkingMeter()
	 */
	@Override
	public void initParkingMeter() throws PersistenceException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#getParkingTime(java
	 * .lang.String)
	 */
	@Override
	public int getParkingTime(String spotNo) {

		int timeDiffinMin = getRealParkingTimeInMin(spotNo);

		if (timeDiffinMin > MAXPARKINGTIME)
			timeDiffinMin = MAXPARKINGTIME;
		if (timeDiffinMin < MINPARKINGTIME)
			timeDiffinMin = MINPARKINGTIME;
		return timeDiffinMin;

	}

	/*
	 * Computes the time difference in minutes between the current time and the
	 * meter time. Adds the coin insert time to the meter time.
	 */
	private int getRealParkingTimeInMin(String spotNo) { //

		// add coin insert time
		long timeDiffinms = getRealParkingTimeMilliSec(spotNo)
				+ (COININSERTIMEINSECONDS * 2000);

		int timeDiffinMin = (int) Math.round(timeDiffinms / 60000);
		return timeDiffinMin;

	}

	/*
	 * Computes the time difference in milliseconds between the current time and
	 * the meter time
	 */
	private long getRealParkingTimeMilliSec(String spotNo) {
		long currentTime = new Date().getTime();
		long meterTime = 0;

		ParkingSpotMeter psm = getSpotMeter(spotNo);
		meterTime = psm.getMeter().getTime();
		return meterTime - currentTime;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#isMoreThenMaxParkingTime
	 * (java .lang.String)
	 */
	@Override
	public boolean isMoreThenMaxParkingTime(String spotNo, double coinValue) {

		int meterTimeDiffInMin = getRealParkingTimeInMin(spotNo);
		int minutesToAdd = computeMinutesToAdd(coinValue);
		int totalTimeInMin = meterTimeDiffInMin + minutesToAdd;
		return (totalTimeInMin > MAXPARKINGTIME);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#validateSpotNo(java
	 * .lang.String)
	 */
	@Override
	public boolean validateSpotNo(String spotNo) {
		return (getSpotMeter(spotNo) != null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#isMaintenanceCode
	 * (java.lang.String)
	 */
	@Override
	public boolean isMaintenanceCode(String key) {
		if (getMaintenanceCode(key).length() == 0)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#getMaintenanceCode
	 * (java.lang.String)
	 */
	@Override
	public String getMaintenanceCode(String key) {
		String command = "";
		switch (key) {
		case EXITCODE:
			command = "exit";
			break;
		case ALLINFOCODE:
			command = "allinfo";

		}
		return command;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#processCoinInsert
	 * (double, java.lang.String, long)
	 */
	@Override
	public void processCoinInsert(double coinValue, String spotNo)
			throws DomainException {

		ParkingSpotMeter psm = getSpotMeter(spotNo);
		if (psm == null) {
			throw new DomainException(ErrCode.PARKINGSPOTNOTVALID);
		}

		// a spot must be selected
		if (spotSelectionTimer.getSpotSelectionState() == SpotSelectionState.SPOTNOTSELECTED) {
			throw new DomainException(ErrCode.NO_PARKING_SPOT_SELECTED);
		}

		// we have again full coin insert time
		this.rescheduleTimer(spotNo);

		// is it a valid coin?
		if (!this.isValidCoin(coinValue)) {
			throw new DomainException(ErrCode.INVALIDCOIN);

		}
		// is the max time for this spot reached?
		if (isMoreThenMaxParkingTime(spotNo, coinValue)) {
			throw new DomainException(ErrCode.MORETHANMAXPARKINGTIME);
		}

		int minutesToAdd = computeMinutesToAdd(coinValue);

		if (getRealParkingTimeInMin(spotNo) <= 0) {
			// if negative time, will start at 0
			psm.setMeter(DateTime.addTime(new Date(), minutesToAdd));
		} else {
			psm.setMeter(DateTime.addTime(psm.getMeter(), minutesToAdd));
		}

		// increment coin
		CoinBox cb = getCoinBox(coinValue);
		cb.setAmount(cb.getAmount() + 1);

	}

	/*
	 * Computes the minutes from coinValue
	 */
	private int computeMinutesToAdd(double coinValue) {

		return (int) (coinValue * COINTOTIMEFACTOR);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#getCurrencyString()
	 */
	@Override
	public String getCurrencyString() {
		return CURRENCYSTRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.pm.domain.service.ParkingMeterDomainService#getLocale()
	 */
	@Override
	public Locale getLocale() {
		Locale locale = Helpers.localeFromString(LOCALESTRING);
		return locale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.pm.domain.service.ParkingMeterDomainService#
	 * getSelectionTimeInSeconds()
	 */
	@Override
	public int getSelectionTimeInSeconds() {
		return COININSERTIMEINSECONDS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#isValidCoin(double)
	 */
	@Override
	public boolean isValidCoin(double coinValue) {

		return (getCoinBox(coinValue) != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.pm.domain.service.ParkingMeterDomainService#getCoinValues()
	 */
	@Override
	public List<Double> getCoinValues() {
		List<Double> coinValues = new ArrayList<Double>();
		coinBoxes.forEach(cv -> coinValues.add(cv.getCoinValue()));
		return coinValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#getParkingSpots()
	 */
	@Override
	public List<String> getParkingSpots() {
		List<String> spots = new ArrayList<String>();
		parkingSpotMeters.forEach(s -> spots.add(s.getSpotNo()));
		return spots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#getSelectedSpotNo()
	 */
	@Override
	public String getSelectedSpotNo() {

		return spotSelectionTimer.getSpotNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#getTransactionCode()
	 */
	@Override
	public long getTransactionCode() {

		return spotSelectionTimer.getTransactionCode();
	}

	/*
	 * Find Coinbox from coinValue
	 */
	private CoinBox getCoinBox(double coinValue) {
		for (CoinBox cb : coinBoxes) {
			if (Math.abs(coinValue - cb.getCoinValue()) < 0.001) {
				return cb;
			}
		}
		return null;
	}

	/*
	 * Find ParkingSpotMeter from spotNo
	 */
	private ParkingSpotMeter getSpotMeter(String spotNo) {
		for (ParkingSpotMeter psm : parkingSpotMeters) {
			if (spotNo.equalsIgnoreCase(psm.getSpotNo())) {
				return psm;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#getSpotSelectionState
	 * ()
	 */
	@Override
	public SpotSelectionState getSpotSelectionState() {
		return this.spotSelectionTimer.getSpotSelectionState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.pm.domain.service.ParkingMeterDomainService#rescheduleTimer
	 * ()
	 */
	@Override
	public void rescheduleTimer(String spotNo) throws DomainException {
		if (this.validateSpotNo(spotNo)) {
			this.spotSelectionTimer.rescheduleTimer(spotNo);
		} else {
			throw new DomainException(ErrCode.PARKINGSPOTNOTVALID);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.pm.domain.service.ParkingMeterDomainService#addObserverToSpotTimer
	 * ()
	 */
	@Override
	public void addObserverToSpotSelectionTimer(Observer o) {

		spotSelectionTimer.addObserver(o);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.pm.domain.service.ParkingMeterDomainService#
	 * addObserverToParkingMeters ()
	 */
	@Override
	public void addObserverToParkingSpotMeters(Observer o) {

		parkingSpotMeters.forEach(psm -> psm.addObserver(o));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.pm.domain.service.ParkingMeterDomainService#
	 * addObserverToParkingMeters ()
	 */
	@Override
	public void addObserverToCoinBoxes(Observer o) {

		coinBoxes.forEach(cb -> cb.addObserver(o));

	}

}
