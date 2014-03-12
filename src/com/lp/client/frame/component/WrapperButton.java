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
import java.awt.Insets;
import java.awt.Point;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;

/**
 * <p>
 * Gewrappter JButton<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.6 $
 */
public class WrapperButton extends JButton implements IControl, IDirektHilfe {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isActivatable = true;
	private boolean isMandatoryField = false;
	private boolean heavyOperation = false;
	private boolean isNeuAusButton = false;
	private CornerInfoButton cib = null;

	private ImageIcon imageIconHeavyOperation = null;
	private ImageIcon imageIconNeuAus = null;

	public WrapperButton() {
		setDefaults();
	}

	public WrapperButton(String p0) {
		super(p0);
		setDefaults();
	}

	public WrapperButton(Action p0) {
		super(p0);
		setDefaults();
	}

	public WrapperButton(Icon p0) {
		super(p0);
		setDefaults();
	}

	public WrapperButton(String p0, Icon p1) {
		super(p0, p1);
		setDefaults();
	}

	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
	}

	public boolean isActivatable() {
		return isActivatable;
	}

	public void setMandatoryField(boolean isMandatoryField) {
		this.isMandatoryField = isMandatoryField;
	}

	public boolean isMandatoryField() {
		return isMandatoryField;
	}

	public void removeContent() {
		// nix tun MB
	}

	protected void setDefaults() {
		cib = new PaintingCornerInfoButton(this);
		HelperClient.setDefaultsToComponent(this);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		Insets s = getMargin();
		setMargin(new Insets(s.top, 1, s.bottom, 1));
	}

	private ImageIcon getImageIconHeavyOperation() {
		if (imageIconHeavyOperation == null) {
			imageIconHeavyOperation = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/heavy_operation.png"));
		}
		return imageIconHeavyOperation;
	}

	private ImageIcon getImageIconNeuAus() {
		if (imageIconNeuAus == null) {
			imageIconNeuAus = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/document.png"));
		}
		return imageIconNeuAus;
	}

	public boolean isHeavyOperation() {
		return heavyOperation;
	}

	public void setHeavyOperation(boolean heavyOperation) {
		this.heavyOperation = heavyOperation;
		if (heavyOperation) {
			this.setIcon(getImageIconHeavyOperation());
		} else {
			this.setIcon(null);
		}
	}

	public boolean isNeuAusButton() {
		return isNeuAusButton;
	}

	public void setNeuAusButton(boolean isNeuAusButton) {
		this.isNeuAusButton = isNeuAusButton;
		if (isNeuAusButton) {
			this.setIcon(getImageIconNeuAus());
		} else {
			this.setIcon(null);
		}
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

	public void setEnabled(boolean bEnabled) {
		super.setEnabled(bEnabled);
	}

	public void setRechtCNr(String rechtCNr) {
		HelperClient.setToolTipTextMitRechtToComponent(this, rechtCNr);
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

	@Override
	public boolean hasContent() throws Throwable {
		return true;
	}
}
