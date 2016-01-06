package ch.zhaw.pm.fe.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * This class represents a line on the display.
 * Special about this display-line is that the text will be moved steadily to the left if necessary (e.g. text to long)
 * @author Manuel
 *
 */
public class DisplayTextLine extends JTextField implements ActionListener {

	private static final long serialVersionUID = 4152872187352930966L;
	private int maxChars;
	private String text;
	private int currentOffset = 0;
	private Timer timer;
	
	private static final int TIMER_START_DELAY = 2000;
	private static final int TIMER_TICK_DELAY = 500;
	
	private static final Font defaultFont = new Font(Font.MONOSPACED, Font.BOLD, 12);
	
	/**
	 * Constructor
	 * @param maxChars An <code>int</code> containing the maximum number of chars this line can display
	 */
	public DisplayTextLine(int maxChars) {
		super();
		this.maxChars = maxChars;
		initField();
	}

	private void initField() {
		setMargin(new Insets(0, 5, 0, 5));
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
		attributes.put(TextAttribute.TRACKING, 0.12);
		setFont(defaultFont.deriveFont(attributes));

		timer = new Timer(TIMER_TICK_DELAY, this);
		timer.setInitialDelay(TIMER_START_DELAY);
		timer.setActionCommand("moveTick");

		setEditable(false);
		
	}

	/**
	 * Set text in display line and start timer for text moving if necessary
	 * @param t A <code>String</code> containing the text to display
	 */
	@Override
	public void setText(String t) {
		this.text = t.toUpperCase();
		timer.stop();
		currentOffset = 0;

		if (t.length() > maxChars) {
			super.setText(text.substring(currentOffset, maxChars
					+ currentOffset));
			timer.start();
		} else {
			super.setText(text);
		}
	}

	/**
	 * Moves the text to the left and jumps back to the beginning if necessary
	 */
	private void moveText() {
		currentOffset += 1;

		if (maxChars + currentOffset == text.length()) {
			timer.stop();
			timer.start();
		} else if (maxChars + currentOffset > text.length()) {
			currentOffset = 0;
			timer.stop();
			timer.start();
		}
		super.setText(text.substring(currentOffset, maxChars + currentOffset));

	}

	/**
	 * Handles Timer actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("moveTick")) {
			moveText();
		}

	}
}
