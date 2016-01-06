package ch.zhaw.pm.fe.view.console;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.domain.util.Helpers;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.view.console.text.ConsoleWriter;
import ch.zhaw.pm.fe.view.console.text.MsgCode;

public class ViewHelper {

	/**
	 * create possible coin commands
	 * 
	 * @param domainService
	 * @return CoinValues
	 */
	public static String createPossibleCoinCommands(

	ParkingMeterDomainService domainService) {

		StringBuffer bf = new StringBuffer();
		bf.append("[");
		List<Double> coinValues = domainService.getCoinValues();
		for (double coinValue : coinValues) {
			if (bf.length() > 2)
				bf.append("  ");

			bf.append(Command.INSERTCOIN.getCmdString()
					+ Helpers.formatDecimal(coinValue));
		}
		bf.append("]");
		return bf.toString();
	}

	/**
	 * create possible spotNo commands
	 * 
	 * @param domainService
	 * @return String with spotNo
	 */
	public static String createPossibleSpotNoCommands(

	ParkingMeterDomainService domainService) {

		StringBuffer bf = new StringBuffer();
		bf.append("[");
		List<String> parkingSpots = domainService.getParkingSpots();
		for (String spotNo : parkingSpots) {
			if (bf.length() > 2)
				bf.append("  ");
			bf.append(Command.SELECTSPOT.getCmdString() + spotNo);
		}
		bf.append("]");
		return bf.toString();
	}

	/**
	 * Write the message insert coin to the customer
	 * 
	 * @param consoleWriter
	 *            Writes to Console
	 * @param domainService
	 *            ParkingMeterDomainService
	 */
	public static void printInsertCoins(ConsoleWriter consoleWriter,
			ParkingMeterDomainService domainService) {
		consoleWriter.print(MsgCode.INSERTCOINS);
		consoleWriter.print(createPossibleCoinCommands(domainService));
		consoleWriter.print(" ");
		consoleWriter.print(MsgCode.OR);
		consoleWriter.print(MsgCode.ENTERSPOTNO);
		consoleWriter.println(createPossibleSpotNoCommands(domainService));

		consoleWriter.print(MsgCode.COINPROMPT);
	}

	/**
	 * Write the message enter spot no to the customer
	 * 
	 * @param consoleWriter
	 *            Writes to Console
	 * @param domainService
	 *            ParkingMeterDomainService
	 */
	public static void printEnterSpotNo(ConsoleWriter consoleWriter,
			ParkingMeterDomainService domainService) {
		consoleWriter.print(MsgCode.ENTERSPOTNO);
		consoleWriter.println(createPossibleSpotNoCommands(domainService));
		consoleWriter.print(MsgCode.SPOTPROMPT);
	}

	/**
	 * Write the message parkingSpot selected to the customer
	 * 
	 * @param consoleWriter
	 *            Writes to Console
	 * @param domainService
	 *            ParkingMeterDomainService
	 */
	public static void printParkingSpotSelected(ConsoleWriter consoleWriter,
			ParkingMeterDomainService domainService) {
		String spotNo = domainService.getSelectedSpotNo();
		int minutes = domainService.getParkingTime(spotNo);

		consoleWriter.print(MsgCode.SELECTEDSPOTNO);
		consoleWriter.print(spotNo + " ");

		consoleWriter.print(MsgCode.REMAININGTIME);
		DecimalFormat df = new DecimalFormat("#00");
		consoleWriter.print(df.format(minutes) + " ");
		consoleWriter.println(MsgCode.MINUTE);

	}

	/**
	 * Prints the start message
	 * 
	 * @param consoleWriter
	 */
	public static void printWelcomeMessage(ConsoleWriter consoleWriter) {
		consoleWriter.print(MsgCode.STARTPARKINGMETER);
		consoleWriter.println(Helpers.formatDate(new Date()));

	}

}
