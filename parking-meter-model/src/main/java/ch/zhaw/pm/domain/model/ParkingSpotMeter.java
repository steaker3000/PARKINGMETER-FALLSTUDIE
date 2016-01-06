package ch.zhaw.pm.domain.model;

import java.util.Date;
import java.util.Observable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.zhaw.pm.domain.util.DateTime;
import ch.zhaw.pm.domain.util.DateTimeAdapter;
import ch.zhaw.pm.domain.util.Helpers;

/**
 * Handles one parking spot information. The information consists of the parking
 * spot name and the meter.
 * 
 *
 */
@XmlRootElement
public class ParkingSpotMeter extends Observable {

	private Integer id;
	private String spotNo;
	private Date meter;

	public ParkingSpotMeter() {
		this.meter = new Date();
	}

	/**
	 * Constructs a parking spot
	 * 
	 * @param parkingSpotName
	 *            the name of the parking spot (e.h. '51')
	 */
	public ParkingSpotMeter(String spotNo) {
		this.spotNo = spotNo;
		this.meter = new Date();
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
	 * @param spotNo
	 *            the spotNo to set
	 */
	public final void setSpotNo(String spotNo) {
		this.spotNo = spotNo;
	}

	/**
	 * Returns the parking spot name
	 * 
	 * @return the parking spot name
	 */
	public String getSpotNo() {
		return spotNo;
	}

	/**
	 * Returns the parking meter (until which time the parking spot is payed)
	 * 
	 * @return the parking meter (until which time the parking spot is payed)
	 */
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	public Date getMeter() {
		return meter;
	}

	/**
	 * Set the parking meter date (until which time the parking spot is payed)
	 * 
	 * @param meter
	 *            the meter date
	 */
	public void setMeter(Date meter) {
		this.meter = meter;
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Check it the parking time is already over.
	 * 
	 * @return <code>true</code> if the parking time is over.
	 */
	public boolean checkParkingTimeOver() {
		if (DateTime.isDate1OlderThenDate2(new Date(), meter))
			return true;
		return false;
	}

	/**
	 * Check it the parking time is already over.
	 * 
	 * @return <code>true</code> if the parking time is over.
	 */
	public boolean checkParkingTimeOver(Date date) {
		if (DateTime.isDate1OlderThenDate2(date, meter))
			return true;
		return false;
	}

	/**
	 * Computes the time difference in milliseconds between now and the parking
	 * meter date
	 * 
	 * @return the time difference in milliseconds
	 */
	public long computeOvertime() {

		return DateTime.computeTimeDifference(new Date(), meter);
	}

	/**
	 * Computes the time difference in milliseconds between now and the parking
	 * meter date
	 * 
	 * @return the time difference in milliseconds
	 */
	public long computeOvertime(Date date) {

		return DateTime.computeTimeDifference(date, meter);
	}

	/**
	 * Formates a string of one parking meter. Example:<br>
	 * <code>Parkuhr[1]  |  bezahlt bis 2013-12-28 22:14  |  Differenz 00:50:58 ** Parkzeit abgelaufen **  |  </code>
	 * 
	 * @return the formated string
	 */
	public String toString() {
		String overtime = "";

		Date date = DateTime.addTime(new Date(), 100L);
		if (checkParkingTimeOver(date)) {
			overtime = " ** Parktime is over **";
		}
		return Helpers.formatID(id) + "  |  " + "spotNo [" + spotNo
				+ "] payed til " + Helpers.formatDate(meter) + "  |  "
				+ "difference " + Helpers.formatTime(computeOvertime())
				+ overtime;
	}

}
