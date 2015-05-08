/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.frame.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import com.lp.client.pc.LPMain;


/**
 * A message dialog that closes after a timeout.
 * 
 * @author clap
 * @author roel
 * 
 * Inspired by http://forum.java.sun.com/thread.jspa?threadID=5148811&messageID=9556308
 * 
 */
public class WrapperTimedOptionPane {

	protected static int UPDATE_PERIOD = 1000;

	/**
	 * Show a dialogBox.
	 * 
	 * @param f
	 *            the owner
	 * @param message
	 *            the message to display
	 * @param timeout
	 *            in milliseconds
	 * @param title
	 *            title of the dialog box
	 * @param timeoutMessage
	 *            text showing remaining seconds
	 *            
	 * @param messageType
	 *            JOptionPAne messagetype
	 * @param optionType
	 *            JoptionPane optiontype
	 * @return {@link JOptionPane#YES_OPTION}, when Yes is clicked, {@link JOptionPane#NO_OPTION}
	 *         if NO is clicked, or {@link JOptionPane#CANCEL_OPTION} on timeout or window closed
	 *         event.
	 */
	public final static int showTimedOptionPane(Frame f, String message, String title,
			String timeoutMessage, final long timeout, int messageType, int optionType) 
	{
		final Message messageComponent = new Message(message, timeout,
				timeoutMessage);
		final JOptionPane pane = new JOptionPane(messageComponent, messageType,
				optionType);
		Object result = showTimedOptionPane(f, title, messageComponent, pane);

		String valueString = result.toString();
		if (JOptionPane.UNINITIALIZED_VALUE.equals(valueString)) 
		{
			return JOptionPane.CLOSED_OPTION;
		}
		return ((Integer)result).intValue();  
	}

	// Same as above, only with custom button options that need some conversion
	public static int showTimedOptionPane(Frame f, String message, String title,
			String timeoutMessage, final long timeout, int messageType, int optionType, Icon icon, Object[] options,
			Object defaultOption) 
	{

		final Message messageComponent = new Message(message, timeout,
				timeoutMessage);
		final JOptionPane pane = new JOptionPane(messageComponent, messageType,
				optionType, icon, options, defaultOption);
		Object result =  showTimedOptionPane(f, title, messageComponent, pane);
		int intResult = JOptionPane.CLOSED_OPTION;
		// convert custom button to int result
		if(options != null){
			for (int i = 0; i < options.length; i++)
			{
				if (result.equals(options[i]))
				{
					intResult = i;
				}
			}
		}
		return intResult;
	}

	public static Object showTimedOptionPane(Frame f, String title, final Message messageComponent, final JOptionPane pane)
	{
		final JDialog dialog = new JDialog(f, title, true);
		dialog.setResizable(false);

		dialog.setContentPane(pane);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Timer timer = new Timer(UPDATE_PERIOD, new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				messageComponent.addEllapsedMilliseconds(UPDATE_PERIOD);
				if (messageComponent.isOver()) 
				{
					dialog.dispose();
				}
			}
		});
		timer.start();
		pane.addPropertyChangeListener(new PropertyChangeListener() 
		{
			public void propertyChange(PropertyChangeEvent e) 
			{
				String prop = e.getPropertyName();
				if (dialog.isVisible() && (e.getSource() == pane)
						&& (JOptionPane.VALUE_PROPERTY.equals(prop))) 
				{
					dialog.setVisible(false);
				}
			}
		});

		dialog.pack();
		dialog.setLocationRelativeTo(f);
		dialog.setVisible(true);

		return pane.getValue();
	}

	/**
	 * Content of the {@link JOptionPane}.
	 * 
	 * @author clap
	 * @author roel
	 * 
	 */
	static class Message extends JPanel 
	{

		private final static String RS = LPMain.getTextRespectUISPr("lp.dialog.automatischschliessen");
		JLabel remaining;
		private long timeout;
		private long ellapsed = 0;

		/**
		 * Build content panel.
		 * 
		 * @param message
		 *            the message to show
		 * @param milliseconds
		 *            timeout in milliseconds
		 * @param timeoutMessage
		 *            text showing remaining seconds
		 */
		protected Message(String message, long milliseconds, String timeoutMessage) 
		{
			super(new BorderLayout());
			//JLabel messageLabel;
			JPanel messagePanel = new JPanel();
			GridLayout gridLayout = new GridLayout(0,1);
			messagePanel.setLayout(gridLayout);

			Font SMALL_FONT = new Font("Dialog",0,10);
			this.timeout = milliseconds;
			// tokenize message on "\n"
			StringTokenizer tokenizer = new StringTokenizer(message, "\n");
			while (tokenizer.hasMoreTokens())
			{
				String token = tokenizer.nextToken();
				JLabel messageLabel = new JLabel(token);
				messagePanel.add(messageLabel);   
			}


			this.add(messagePanel);

			this.remaining = new JLabel(formatRemainingSeconds(milliseconds));
			this.remaining.setFont(SMALL_FONT);
			add(this.remaining, BorderLayout.SOUTH);
		}

		private String formatRemainingSeconds(long ms) 
		{
			return RS + " " + (ms / 1000) + "s.";
		}

		/**
		 * 
		 * @return true if the timeout has been reached.
		 */
		protected boolean isOver() 
		{
			return ellapsed >= timeout;
		}

		/**
		 * Indicates that some milliseconds has occured.
		 * 
		 * @param milliseconds
		 */
		protected void addEllapsedMilliseconds(long milliseconds) 
		{
			ellapsed += milliseconds;
			remaining.setText(formatRemainingSeconds(timeout - ellapsed));
			repaint();
		}
	}


}
