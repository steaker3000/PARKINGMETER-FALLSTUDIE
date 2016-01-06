package ch.zhaw.pm.fe.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.zhaw.pm.domain.exception.DomainException;
import ch.zhaw.pm.domain.model.ParkingSpotMeter;
import ch.zhaw.pm.domain.model.SpotSelectionState;
import ch.zhaw.pm.domain.model.SpotSelectionTimer;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.controller.InsertCoinController;

/**
 * This class represents a display simulation for the parking meter
 * @author Manuel
 *
 */
public class Display extends JFrame implements Observer, ActionListener {

	private static final long serialVersionUID = 7665905272760144336L;
	private InsertCoinController coinController;
	private ParkingMeterDomainService domainService;

	private ArrayList<DisplayTextLine> lines;

	private static final String defaultText = "please select a parking spot";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE MMM dd - HH:mm");

	private Timer timer;
	
	private static final int DISPLAY_SIZE = 32;
	private static final int DISPLAY_NUMBER_OF_LINES = 2;
	
	private static final int TIMER_COIN_DISPLAY_TIME = 5000;
	private static final int TIMER_RETURN_TIME = 3000;

	public Display(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {

		coinController = (InsertCoinController) controllers
				.get(Command.INSERTCOIN);
		this.domainService = domainService;
		lines = new ArrayList<DisplayTextLine>();
		timer = new Timer(0, this);
		setLookAndFeel();
		initUI();
		registerEvents();
		displayText("please select a parking spot");
	}

	private void setLookAndFeel() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			Logger.getLogger(getClass().getName()).info(
					"can not set system look and feel");
		}
	}

	private void initUI() {

		addLines(DISPLAY_NUMBER_OF_LINES);
		setTitle("Display");
		setSize(300, 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setResizable(false);

	}

	private void registerEvents() {
		domainService.addObserverToSpotSelectionTimer(this);
		domainService.addObserverToCoinBoxes(this);
		domainService.addObserverToParkingSpotMeters(this);
		coinController.addObserver(this);
	}

	/**
	 * Display as given text on the display simulation
	 * Every line of the text is displayed on a display-line.
	 * If there are not enough lines for every text-line the remaining text will all be displayed on the last line
	 * @param text A <code>String</code> containing the text to display
	 */
	public void displayText(String text) {

		// Clear lines
		for (DisplayTextLine line : lines) {
			line.setText("");
		}

		int i = 0;
		for (String line : text.split("\n")) {
			if (i < lines.size())
				lines.get(i).setText(line);
			else
				lines.get(lines.size() - 1).setText(
						lines.get(lines.size() - 1).getText() + " " + line);

			i += 1;
		}

	}

	private void addLines(int numberOfLines) {
		Container pane = getContentPane();
		BoxLayout bl = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(bl);

		DisplayTextLine line;
		for (int i = 0; i < numberOfLines; i++) {
			line = new DisplayTextLine(DISPLAY_SIZE);
			pane.add(line);
			lines.add(line);
		}

	}

	private void displaySpotSelection(String spotNo) {
		displayText("parking spot " + spotNo + " selected: "
				+ domainService.getParkingTime(spotNo)
				+ " minutes remaining\nplease enter additional coins");
	}

	@Override
	public void update(Observable o, Object message) {
		if (o instanceof SpotSelectionTimer) {
			// While parking spot is selected
			SpotSelectionTimer sst = (SpotSelectionTimer) o;

			if (sst.getSpotSelectionState() == SpotSelectionState.SPOTISSELECTED) {
				displaySpotSelection(sst.getSpotNo());
			} else if (sst.getSpotSelectionState() == SpotSelectionState.SPOTNOTSELECTED) {
				displayText(defaultText);
			}
		} else if (o instanceof InsertCoinController) {
			// If a coin is inserted
			if (message instanceof DomainException) {
				displayText("coin not accepted\n"
						+ ((DomainException) message).getMessage());
			}

			timer.setInitialDelay(TIMER_COIN_DISPLAY_TIME);
			timer.setRepeats(false);
			timer.setActionCommand("displaySpotInfo:"
					+ domainService.getSelectedSpotNo());
			timer.start();

		} else if (o instanceof ParkingSpotMeter) {
			// If a parking spot has been reserved
			ParkingSpotMeter psm = (ParkingSpotMeter) o;

			displayText("parking spot " + psm.getSpotNo() + "\nreserved until "
					+ sdf.format(psm.getMeter()));

			timer.stop();
			timer.setInitialDelay(TIMER_RETURN_TIME);
			timer.setRepeats(false);
			timer.setActionCommand("displaySpotInfo:" + psm.getSpotNo());
			timer.start();
		}

	}

	/**
	 * Handles Timer events to set display
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().startsWith("displaySpotInfo")) {
			// Return to display of parking spot selection
			displaySpotSelection(e.getActionCommand().split(":")[1]);
		}

	}
}
