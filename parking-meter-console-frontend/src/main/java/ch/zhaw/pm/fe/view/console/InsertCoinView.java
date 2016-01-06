package ch.zhaw.pm.fe.view.console;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import ch.zhaw.pm.domain.model.CoinBox;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.domain.util.Helpers;
import ch.zhaw.pm.fe.view.console.text.ConsoleWriter;
import ch.zhaw.pm.fe.view.console.text.MsgCode;

/**
 * The insert coin view is responsible outputting messages regarding the coin
 * inserting use case.
 *
 */
public class InsertCoinView implements Observer {

	ConsoleWriter consoleWriter;
	String possibleCoinCommands;
	String possibleSpotNoCommands;
	ParkingMeterDomainService domainService;

	public InsertCoinView(ConsoleWriter consoleWriter,
			ParkingMeterDomainService domainService) {
		this.consoleWriter = consoleWriter;
		this.domainService = domainService;
		possibleCoinCommands = ViewHelper
				.createPossibleCoinCommands(domainService);
		possibleSpotNoCommands = ViewHelper
				.createPossibleSpotNoCommands(domainService);
		domainService.addObserverToCoinBoxes(this);

	}

	/**
	 * Observer update entry for the View. Processes the message according the
	 * InsertCoinViewMessage
	 * 
	 */
	@Override
	public void update(Observable o, Object message) {

		if (message instanceof CoinBox) {
			CoinBox cb = (CoinBox) message;

			String spotNo = domainService.getSelectedSpotNo();
			Locale locale = domainService.getLocale();

			printCoinInsert(cb.getCoinValue(), cb.getCurrencyISO(), spotNo,
					domainService.getParkingTime(spotNo), locale);

		}

	}

	/**
	 * Writes the string inserted {coinValue} {currency} for spot {spotNo}
	 * remaining time {parkingTimeInMinutes} min
	 * 
	 * @param coinValue
	 * @param currency
	 * @param spotNo
	 * @param parkingTimeInMinutes
	 */
	private void printCoinInsert(double coinValue, String currency,
			String spotNo, int parkingTimeInMinutes, Locale locale) {
		consoleWriter.print(MsgCode.INSERTED);
		consoleWriter.print(Helpers.formatMoney(coinValue, currency, locale)
				+ " ");
		consoleWriter.print(MsgCode.FORSPOT);
		consoleWriter.print(spotNo);
		consoleWriter.print(" ");
		consoleWriter.print(MsgCode.REMAININGTIME);
		DecimalFormat df = new DecimalFormat("#00");
		consoleWriter.print(df.format(parkingTimeInMinutes) + " ");
		consoleWriter.println(MsgCode.MINUTE);

		ViewHelper.printInsertCoins(consoleWriter, domainService);

	}

}
