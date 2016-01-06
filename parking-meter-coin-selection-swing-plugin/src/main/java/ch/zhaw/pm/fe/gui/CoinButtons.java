package ch.zhaw.pm.fe.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.zhaw.pm.domain.model.SpotSelectionState;
import ch.zhaw.pm.domain.model.SpotSelectionTimer;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.controller.FrontController;

/**
 * A simulation frame for the coin entry in the parking spot
 * Reads all available coin types from the model and displays a button for each
 * @author Manuel
 *
 */
public class CoinButtons extends JFrame {

	private static final long serialVersionUID = 7638055841982745777L;
	private FrontController frontController;
	private ParkingMeterDomainService domainService;

	public CoinButtons(ParkingMeterDomainService domainService,
			Map<Command, Controller> controllers) {

		frontController = (FrontController) controllers.get(Command.ALL);
		this.domainService = domainService;
		setLookAndFeel();
		initUI();
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

		domainService
				.addObserverToSpotSelectionTimer(new SpotSelectionObserver(this));

		List<JButton> buttons = new ArrayList<JButton>();
		List<Double> coinValues = domainService.getCoinValues();
		for (Double coinValue : coinValues) {
			JButton coinButton = new JButton(coinValue.toString());
			buttons.add(coinButton);
			coinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					String cmd = event.getActionCommand();
					frontController.processRequest(Command.INSERTCOIN
							.getCmdString() + cmd);
				}
			});
		}

		addButtons(buttons);
		setTitle("Coin Input");
		setSize(300, 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void addButtons(List<JButton> buttons) {
		Container pane = getContentPane();
		FlowLayout fl = new FlowLayout();
		pane.setLayout(fl);

		for (JComponent comp : buttons) {
			pane.add(comp);
		}

	}

	private class SpotSelectionObserver implements Observer {

		private CoinButtons buttons;

		public SpotSelectionObserver(CoinButtons buttons) {
			this.buttons = buttons;
		}

		@Override
		public void update(Observable o, Object message) {

			if (message instanceof SpotSelectionTimer) {

				SpotSelectionTimer sst = (SpotSelectionTimer) message;

				// display frame depending on parking spot selected or not
				if (sst.getSpotSelectionState() == SpotSelectionState.SPOTISSELECTED) {
					buttons.setVisible(true);

				} else if (sst.getSpotSelectionState() == SpotSelectionState.SPOTNOTSELECTED) {
					buttons.setVisible(false);
				}

			}

		}

	}

}
