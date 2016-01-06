package ch.zhaw.pm.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.zhaw.pm.domain.model.SpotSelectionState;
import ch.zhaw.pm.domain.model.SpotSelectionTimer;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpotSelectionTimerTest implements Observer {

	private SpotSelectionTimer spotSelectionTimer;
	private final int spotSelectionTimeInSeconds = 10;
	private String spotNo = "";
	private long transactionCode = 0;

	@Before
	public void setUp() throws Exception {
		spotSelectionTimer = new SpotSelectionTimer(spotSelectionTimeInSeconds);
		spotSelectionTimer.addObserver(this);
	}

	@Test
	public void stage1_testPreConditions() {
		assertEquals(SpotSelectionState.SPOTNOTSELECTED,
				spotSelectionTimer.getSpotSelectionState());
		assertEquals(spotSelectionTimeInSeconds,
				spotSelectionTimer.getSelectionTimeInSeconds());
		assertEquals(0, spotSelectionTimer.getTransactionCode());
		assertEquals("", spotSelectionTimer.getSpotNo());
	}

	@Test
	public void stage2_testSpotSelection() {
		spotNo = "11";
		spotSelectionTimer.rescheduleTimer(spotNo);
		assertEquals(SpotSelectionState.SPOTISSELECTED,
				spotSelectionTimer.getSpotSelectionState());
		transactionCode = spotSelectionTimer.getTransactionCode();
		spotNo = spotSelectionTimer.getSpotNo();

		// reschedule with same spotNo
		wait10ms();
		spotSelectionTimer.rescheduleTimer(spotNo);
		assertEquals(transactionCode, spotSelectionTimer.getTransactionCode());
		assertEquals(spotNo, spotSelectionTimer.getSpotNo());

		// reschedule with different spotNo - transaction code must change
		spotNo = "12";
		spotSelectionTimer.rescheduleTimer(spotNo);
		assertEquals(spotNo, spotSelectionTimer.getSpotNo());
		assertNotEquals(transactionCode,
				spotSelectionTimer.getTransactionCode());

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (arg instanceof SpotSelectionTimer) {
			SpotSelectionTimer sst = (SpotSelectionTimer) arg;
			assertEquals(spotNo, sst.getSpotNo());
		}

	}

	private void wait10ms() {
		try {
			Thread.sleep(10); // 10 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
