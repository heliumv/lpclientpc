/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;

/**
 * <p>
 * Diese Klasse ist ein kleines Panel und dient zur Anzeige von Schluessel/Wert
 * - Paaren
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 31.07.06
 * </p>
 * 
 * <p>
 * @author $Author: robert $
 * </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:13 $
 */
public class WrapperKeyValueField extends JPanel implements IDirektHilfe {

	private static final long serialVersionUID = -2478663718545384397L;
	private WrapperLabel wlaKey = null;
	private WrapperLabel wlaValue = null;
	private JPanel jpaWorkingOn = null;
	private CornerInfoButton cib = null;
	
	private int iDividerLocation;

	public WrapperKeyValueField(int iDividerLocation) throws Throwable {
		super(new GridBagLayout());
		this.iDividerLocation = iDividerLocation;
		jbInit();
		setDefaults();
	}

	private void jbInit() throws Throwable {
		wlaKey = new WrapperLabel();
		wlaValue = new WrapperLabel();

		cib = new CornerInfoButton(this);
		
		// Werte erchtsbuendig anzeigen.
		wlaValue.setHorizontalAlignment(SwingConstants.RIGHT);
		// kleiner Rahmen
		wlaValue.setBorder(BorderFactory.createLoweredBevelBorder());

		wlaKey.setMinimumSize(new Dimension(iDividerLocation, Defaults
				.getInstance().getControlHeight()));
		wlaKey.setPreferredSize(new Dimension(iDividerLocation, Defaults
				.getInstance().getControlHeight()));

		// wlaValue.setMinimumSize(new
		// Dimension(Defaults.getInstance().getControlHeight(),
		// Defaults.getInstance().getControlHeight()));
		// wlaValue.setPreferredSize(new
		// Dimension(Defaults.getInstance().getControlHeight(),
		// Defaults.getInstance().getControlHeight()));
		jpaWorkingOn = new JPanel(new GridBagLayout());
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaKey, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaValue, new GridBagConstraints(1, 0, 1, 1, 0.01,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public WrapperLabel getWlaKey() {
		return wlaKey;
	}

	public Double getValue() {
		Double d;
		try {
			NumberFormat nf = NumberFormat.getInstance();
			Number n = nf.parse(wlaValue.getText());
			d = n.doubleValue();
		} catch (Exception e) {
			return (double) 0;
		}

		return d;
	}

	public BigDecimal getValueBigDecimal(int dezimalstellen) {
		BigDecimal d;
		d = new BigDecimal(this.getValue());
		d = d.setScale(dezimalstellen, BigDecimal.ROUND_HALF_EVEN);
		return d;
	}
	
	public WrapperLabel getWlaValue() {
		return wlaValue;
	}

	public void setKey(String sKey) {
		wlaKey.setText(sKey);
	}

	public void setValue(String sValue) {
		wlaValue.setText(sValue);
	}

	private void setDefaults() throws Throwable {
		HelperClient.setDefaultsToComponent(this);
	}

	public void setMinimumSize(Dimension d) {
		super.setMinimumSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	public void setMaximumSize(Dimension d) {
		super.setMaximumSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	@Override
	public void setToken(String token) {
		cib.setToolTipToken(token);
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(this);
	}

	@Override
	public void removeCib() {
		cib = null;
	}

	@Override
	public String getToken() {
		return cib.getToolTipToken();
	}
}
