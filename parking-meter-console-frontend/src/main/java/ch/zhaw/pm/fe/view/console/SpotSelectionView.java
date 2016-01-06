package ch.zhaw.pm.fe.view.console;

import java.util.Observable;
import java.util.Observer;

import ch.zhaw.pm.domain.exception.DomainException;
import ch.zhaw.pm.domain.exception.ErrCode;
import ch.zhaw.pm.domain.model.SpotSelectionState;
import ch.zhaw.pm.domain.model.SpotSelectionTimer;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.view.console.text.ConsoleWriter;

/**
 * The spot selection view is responsible outputting messages regarding the spot
 * selection. Messages are received through the observer update method.
 *
 */
public class SpotSelectionView implements Observer {

	ConsoleWriter consoleWriter;
	ParkingMeterDomainService domainService;

	public SpotSelectionView(ConsoleWriter consoleWriter,
			ParkingMeterDomainService domainService) {
		this.consoleWriter = consoleWriter;
		this.domainService = domainService;
		domainService.addObserverToSpotSelectionTimer(this);

	}

	/**
	 * Observer updates receives messages for outputting. The message object is
	 * parsed: (SpotViewModel, SpotSelectionState, DomainException)
	 */
	@Override
	public void update(Observable o, Object message) {

		if (message instanceof SpotSelectionTimer) {

			SpotSelectionTimer sst = (SpotSelectionTimer) message;

			if (sst.getSpotSelectionState() == SpotSelectionState.SPOTISSELECTED) {
				ViewHelper.printParkingSpotSelected(consoleWriter,
						domainService);
				ViewHelper.printInsertCoins(consoleWriter, domainService);

			} else if (sst.getSpotSelectionState() == SpotSelectionState.SPOTNOTSELECTED) {
				consoleWriter.println("\n");
				ViewHelper.printEnterSpotNo(consoleWriter, domainService);
			}

		} else if (message instanceof DomainException) {
			DomainException exception = (DomainException) message;
			consoleWriter.println(exception.getErrCode());
			if (exception.getErrCode() == ErrCode.PARKINGSPOTNOTVALID)
				ViewHelper.printEnterSpotNo(consoleWriter, domainService);
		}
	}

}
