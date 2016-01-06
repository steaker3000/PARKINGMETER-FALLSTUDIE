package ch.zhaw.pm.domain.model;

import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The SpotSelectionTimer handles the input state of the parkingMeter. After the
 * selection of a parking spot, the customer can insert coins during a defined
 * time. Each coin insert shall reschedule the timer. After this time the
 * customer must again select a parking spot. During the coin insert time period
 * a unique transaction code is produced. It allows to identify all transactions
 * from a customer for a spot.<br>
 * <br>
 * The SpotSelectionTimer is an observable. Views can register to get informed
 * about a status change.
 *
 */
public class SpotSelectionTimer extends Observable {

	private Timer timer = new Timer();
	private SpotSelectionTimerTask spotSelectionTimerTask = null;
	private int selectionTimeInSeconds = 60;
	private long transactionCode = 0;
	private String spotNo = "";
	private SpotSelectionState spotSelectionState = SpotSelectionState.SPOTNOTSELECTED;

	public SpotSelectionTimer(int selectionTimeInSeconds) {
		super();
		this.selectionTimeInSeconds = selectionTimeInSeconds;
	}

	/**
	 * @return the selectionTimeInSeconds
	 */
	public int getSelectionTimeInSeconds() {
		return selectionTimeInSeconds;
	}

	/**
	 * @param selectionTimeInSeconds
	 *            the selectionTimeInSeconds to set
	 */
	public void setSelectionTimeInSeconds(int selectionTimeInSeconds) {
		this.selectionTimeInSeconds = selectionTimeInSeconds;
	}

	/**
	 * @return the transactionCode
	 */
	public long getTransactionCode() {
		return transactionCode;
	}

	/**
	 * @return the spotSelectionState
	 */
	public SpotSelectionState getSpotSelectionState() {
		return spotSelectionState;
	}

	/**
	 * @return the spotNo
	 */
	public String getSpotNo() {
		return spotNo;
	}

	/**
	 * The timer is rescheduled to its original value {selectionTimeInSeconds}.
	 * 
	 * @param spotNo
	 *            the parking spot no
	 */
	public void rescheduleTimer(String spotNo) {

		timer.cancel();
		timer = new Timer();
		spotSelectionTimerTask = new SpotSelectionTimerTask();
		timer.schedule(spotSelectionTimerTask, selectionTimeInSeconds * 1000);
		if (spotSelectionState == SpotSelectionState.SPOTNOTSELECTED
				|| !this.spotNo.equalsIgnoreCase(spotNo)) {
			// state changed -> update state, transactionCode and spotNo and
			// notify observers
			spotSelectionState = SpotSelectionState.SPOTISSELECTED;
			transactionCode = new Date().getTime();
			this.spotNo = spotNo;
			setChanged();
			notifyObservers(this);
		}
	}

	/*
	 * Handels the timeOver -> informs the observers
	 */
	private void selectionTimeOver() {
		spotSelectionState = SpotSelectionState.SPOTNOTSELECTED;
		transactionCode = 0;
		spotNo = "";
		setChanged();
		notifyObservers(this);
	}

	private class SpotSelectionTimerTask extends TimerTask {
		@Override
		public void run() {
			selectionTimeOver();
		}
	}

}
