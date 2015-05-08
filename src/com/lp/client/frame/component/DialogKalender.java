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
 *******************************************************************************/
package com.lp.client.frame.component;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import com.lp.client.frame.component.calendar.JCalendar;

public class DialogKalender extends JDialog implements PropertyChangeListener {

	private JCalendar jcalendar;
	private Locale locale = null;
	private java.util.Date selectedDate = null;

	Date dMaximum = null;
	Date dMinimum = null;

	boolean dateSelected = false;

	public java.util.Date returnDate = null;

	private static final long serialVersionUID = 1L;

	public DialogKalender(Locale locale, java.util.Date selectedDate,
			Date dMaximum, Date dMinimum) {
		super((Frame) null, "", true);
		this.locale = locale;
		this.selectedDate = selectedDate;
		this.dMaximum = dMaximum;
		this.dMinimum = dMinimum;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setUndecorated(true);
		addEscapeListener(this);
		jbInit();
		pack();

	}

	public static void addEscapeListener(final JDialog dialog) {
		ActionListener escListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		};

		dialog.getRootPane().registerKeyboardAction(escListener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	private void jbInit() {

		jcalendar = new JCalendar(null, locale);
		jcalendar.setTodayButtonVisible(true);
		jcalendar.setYesterdayButtonVisible(true);
		jcalendar.getDayChooser().addPropertyChangeListener("day", this);
		jcalendar.getMonthChooser().addPropertyChangeListener("month", this);

		jcalendar.setMaxSelectableDate(dMaximum);
		jcalendar.setMinSelectableDate(dMinimum);

		jcalendar.getDayChooser().setAlwaysFireDayProperty(true);

		this.getContentPane().setLayout(new GridBagLayout());

		this.getContentPane().add(jcalendar);

		Calendar calendar = Calendar.getInstance();
		Date date = selectedDate;

		if (date != null) {
			calendar.setTime(date);
		}
		jcalendar.setCalendar(calendar);

		dateSelected = false;

		jcalendar.updateUI();
		jcalendar.getDayChooser().updateUI();

		this.setSize(jcalendar.getSize());

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("month")) {
			if (isVisible()) {
				dateSelected = false;
			}

		} else if (evt.getPropertyName().equals("day")) {
			if (isVisible()) {
				returnDate = jcalendar.getCalendar().getTime();
				dateSelected = (Integer.valueOf(evt.getOldValue().toString()) == 0);
				if (dateSelected == true) {
					this.setVisible(false);
				}
			}
		} else if (evt.getPropertyName().equals("date")) {
			returnDate = (Date) evt.getNewValue();
			this.setVisible(false);
		}

	}

}
