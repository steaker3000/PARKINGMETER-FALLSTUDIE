package ch.zhaw.pm.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.zhaw.pm.domain.exception.DomainException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParkingMeterDomainServiceStubTest {

	ParkingMeterDomainService domainService;

	@Before
	public void setUp() {
		domainService = new ParkingMeterDomainServiceStub(null, null, null);
	}

	@Test
	public void stage1_testCoinValues() {

		assertEquals("[0.5, 1.0, 2.0]", domainService.getCoinValues()
				.toString());

	}

	@Test
	public void stage3_testParkingSpots() {

		assertEquals("[11, 12, 13]", domainService.getParkingSpots().toString());

	}

	@Test
	public void stage4_testCurrencyString() {

		assertEquals("CHF", domainService.getCurrencyString());

	}

	@Test
	public void stage5_testLocale() {

		assertEquals("de_CH", domainService.getLocale().toString());

	}

	@Test
	public void stage6_testMaintenanceCode() {

		assertEquals("exit", domainService.getMaintenanceCode("9999"));
		assertEquals("allinfo", domainService.getMaintenanceCode("1234"));

	}

	@Test
	public void stage7_testIsValidCoin() {

		assertTrue(domainService.isValidCoin(0.5));
		assertFalse(domainService.isValidCoin(0.4));

	}

	@Test(expected = DomainException.class)
	public void stage8_testParkingTime() throws DomainException {

		String spotNo = "11";
		assertEquals(0, domainService.getParkingTime(spotNo));
		domainService.processCoinInsert(0.5, spotNo);
		assertEquals(30, domainService.getParkingTime(spotNo));
		domainService.processCoinInsert(1, spotNo);
		assertEquals(90, domainService.getParkingTime(spotNo));
		domainService.processCoinInsert(1, spotNo);
		assertEquals(150, domainService.getParkingTime(spotNo));
		domainService.processCoinInsert(1, spotNo);
		assertEquals(210, domainService.getParkingTime(spotNo));

		assertFalse(domainService.isMoreThenMaxParkingTime(spotNo, 0.5));
		assertTrue(domainService.isMoreThenMaxParkingTime(spotNo, 1));

		domainService.processCoinInsert(1, spotNo);
		assertEquals(240, domainService.getParkingTime(spotNo));
	}

}
