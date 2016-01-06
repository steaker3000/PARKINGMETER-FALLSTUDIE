package ch.zhaw.pm.fe.controller;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.zhaw.pm.app.config.Application;
import ch.zhaw.pm.app.config.ApplicationFactory;
import ch.zhaw.pm.app.config.ApplicationFactoryStubImpl;
import ch.zhaw.pm.domain.service.ParkingMeterDomainService;
import ch.zhaw.pm.domain.util.Helpers;
import ch.zhaw.pm.fe.controller.Dispatcher.Command;
import ch.zhaw.pm.fe.view.console.ConsoleView;
import ch.zhaw.pm.fe.view.console.ViewHelper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FrontControllerStubTest {

	ByteArrayOutputStream os;
	PrintStream out;
	ByteArrayOutputStream es;
	PrintStream err;
	ConsoleView cv;
	FrontController fc;
	ParkingMeterDomainService domainService;
	Application application;

	@Before
	public void setUp() throws Exception {
		os = new ByteArrayOutputStream();
		out = new PrintStream(os);
		es = new ByteArrayOutputStream();
		err = new PrintStream(es);
		ApplicationFactory af = new ApplicationFactoryStubImpl();
		application = af.createApplication();
		domainService = application.getDomainService();
		fc = (FrontController) application.getControllers().get(Command.ALL);
		cv = new ConsoleView(domainService, application.getControllers(), out,
				err);
	}

	@Test
	public void stage1_Welcome() {
		ViewHelper.printWelcomeMessage(cv.getConsoleWriter());
		assertEquals("boot: parking meter started at:".substring(0, 20), os
				.toString().substring(0, 20));
		System.out.println(os.toString());

	}

	@Test
	public void stage2_testInitialSpotSelection()
			throws UnsupportedEncodingException {

		ViewHelper.printEnterSpotNo(cv.getConsoleWriter(), domainService);
		String msg = os.toString("UTF8");
		System.out.print(os.toString());

		assertEquals("enter spot no: [S11  S12  S13]spot > ", replaceCRLF(msg));

	}

	@Test
	public void stage3_testUnknowCommand() throws UnsupportedEncodingException {
		String cmd = "xxxx";
		System.out.println(cmd);

		fc.processRequest(cmd);

		System.err.print(es.toString());
		System.out.print(os.toString());

		wait10ms();

		String msg = os.toString("UTF8");
		String err = es.toString("UTF8");
		assertEquals("enter spot no: [S11  S12  S13]spot > ", replaceCRLF(msg));
		assertEquals("error-002: unknown command", replaceCRLF(err));

	}

	@Test
	public void stage4_testWrongSpotSelection()
			throws UnsupportedEncodingException {
		String cmd = "s1";
		System.out.println(cmd);

		fc.processRequest(cmd);
		System.err.print(es.toString());
		wait10ms();
		System.out.print(os.toString());

		String msg = os.toString("UTF8");
		String err = es.toString("UTF8");

		assertEquals("enter spot no: [S11  S12  S13]spot > ", replaceCRLF(msg));
		assertEquals("error-006: parking spot is not valid", replaceCRLF(err));

	}

	@Test
	public void stage5_testCorrectSpotSelection()
			throws UnsupportedEncodingException {
		String cmd = "s11";
		System.out.println(cmd);

		fc.processRequest(cmd);
		System.err.print(es.toString());
		wait10ms();
		System.out.print(os.toString());

		String msg = os.toString("UTF8");
		String err = es.toString("UTF8");

		String sep = Helpers.getDecimalSeparator();

		assertEquals(
				"selected spot no: 11 | remaining time 00 min insert coins: "
						+ "[C0" + sep + "50  C1" + sep + "00  C2" + sep
						+ "00] or enter spot no: [S11  S12  S13]coin > ",
				replaceCRLF(msg));
		assertEquals("", replaceCRLF(err));

	}

	@Test
	public void stage6_testWrongCoinInsert()
			throws UnsupportedEncodingException {
		String cmd = "s11";
		fc.processRequest(cmd);
		os.reset();

		cmd = "c3";
		System.out.println(cmd);

		fc.processRequest(cmd);
		System.err.print(es.toString());
		wait10ms();
		System.out.print(os.toString());

		String msg = os.toString("UTF8");
		String err = es.toString("UTF8");
		String sep = Helpers.getDecimalSeparator();

		assertEquals("insert coins: " + "[C0" + sep + "50  C1" + sep + "00  C2"
				+ sep + "00] or enter spot no: [S11  S12  S13]coin > ",
				replaceCRLF(msg));
		assertEquals("error-007: invalid coin inserted", replaceCRLF(err));

	}

	@Test
	public void stage7_testCorrectCoinInsert()
			throws UnsupportedEncodingException {
		String cmd = "s11";
		fc.processRequest(cmd);
		os.reset();

		cmd = "c1";
		System.out.println(cmd);

		fc.processRequest(cmd);
		System.err.print(es.toString());
		wait10ms();
		System.out.print(os.toString());

		String msg = os.toString("UTF8");
		String err = es.toString("UTF8");
		String sep = Helpers.getDecimalSeparator();

		assertEquals(
				"inserted SFr. 1.00 for spot 11 | remaining time 60 min insert coins: "
						+ "[C0" + sep + "50  C1" + sep + "00  C2" + sep
						+ "00] or enter spot no: [S11  S12  S13]coin > ",
				replaceCRLF(msg));
		assertEquals("", replaceCRLF(err));

	}

	@Test
	public void stage8_testMaxParkingTime() throws UnsupportedEncodingException {
		String cmd = "s11";
		fc.processRequest(cmd);
		cmd = "c1";
		fc.processRequest(cmd);
		os.reset();

		cmd = "c2";
		System.out.println(cmd);

		fc.processRequest(cmd);
		System.err.print(es.toString());
		wait10ms();
		System.out.print(os.toString());
		os.reset();

		cmd = "c2";
		System.out.println(cmd);

		fc.processRequest(cmd);
		System.err.print(es.toString());
		wait10ms();
		System.out.print(os.toString());

		String msg = os.toString("UTF8");
		String err = es.toString("UTF8");
		String sep = Helpers.getDecimalSeparator();

		assertEquals("insert coins: [C0" + sep + "50  C1" + sep + "00  C2"
				+ sep + "00] " + "or enter spot no: [S11  S12  S13]coin > ",
				replaceCRLF(msg));
		assertEquals(
				"error-009: the maximum parking time is exceeded - coin is not taken!",
				replaceCRLF(err));

	}

	private String replaceCRLF(String str) {
		String tmp = str.replace("\n", "");
		tmp = tmp.replace("\r", "");
		return tmp;

	}

	private void wait10ms() {
		try {
			Thread.sleep(10); // 10 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
