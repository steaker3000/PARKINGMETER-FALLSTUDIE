package ch.zhaw.pm.fe.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.fe.controller.Controller;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.controller.FrontController;

public class ParkingSpotButtons extends JFrame {

	private static final long serialVersionUID = -2938950581823484153L;
	private FrontController frontController;
	private ParkingMeterDomainService domainService;

	public ParkingSpotButtons(ParkingMeterDomainService domainService,
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

		JButton quitButton = new JButton("Quit");

		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		List<JButton> buttons = new ArrayList<JButton>();
		List<String> spots = domainService.getParkingSpots();
		for (String spotNo : spots) {
			JButton spotButton = new JButton(spotNo);
			buttons.add(spotButton);
			spotButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					String cmd = event.getActionCommand();
					frontController.processRequest(Command.SELECTSPOT
							.getCmdString() + cmd);
				}
			});
		}

		addButtons(buttons);
		createLayout(quitButton);
		setTitle("Parking Spot Selection");
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

	private void createLayout(JComponent... arg) {

		Container pane = getContentPane();
		FlowLayout fl = new FlowLayout();
		pane.setLayout(fl);

		for (JComponent comp : arg) {
			pane.add(comp);
		}

	}

}
