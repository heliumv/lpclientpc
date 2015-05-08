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
package com.lp.client.frame.component.durationfield;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.lp.client.frame.component.WrapperTextField;

public class WrapperDurationField extends WrapperTextField implements DocumentListener, FocusListener, KeyListener {

	private static final long serialVersionUID = -8181468993118007007L;
	
	private DurationFormat format = new DurationFormat();
	private long duration = 0;

	public WrapperDurationField() {
		this(DurationFormat.AUTO_FORMAT);
	}
	
	/**
	 * Use {@link DurationFormat#INDUSTRIAL_FORMAT}, {@link DurationFormat#NORMAL_FORMAT},
	 * {@link DurationFormat#AUTO_FORMAT} (Default) as Parameter.
	 * @param format das Format, in welchem die Darstellung erfolgt.
	 */
	public WrapperDurationField(int format) {
		this.format.setFormat(format); 
		
		getDocument().addDocumentListener(this);
		addFocusListener(this);
		addKeyListener(this);
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	
	protected void setValid(boolean valid) {
		setForeground(valid ? Color.BLACK : Color.RED);
	}
	
	protected void checkValid() {
		setValid(format.parse(getText())>-1);
	}

	/**
	 * setzt die Dauer und formatiert sie im gew&auml;hlten Format
	 * @param duration die Dauer in ms
	 */
	public void setDuration(long duration) {
		System.out.println(duration);
		this.duration = duration;
		setText(format.format(duration));
	}
	
	/**
	 * @return die letzte eingegebene Dauer in ms. -1 wenn die Eingabe ung&uuml;tig war.
	 */
	public long getDuration() {
		return duration;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {}



	@Override
	public void insertUpdate(DocumentEvent arg0) {
		checkValid();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		checkValid();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		selectAll();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		applyTheInput();
	}
	
	protected void applyTheInput() {
		long d = format.parse(getText());
		if(d == -1) d = duration;
		setDuration(d);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
			applyTheInput();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
