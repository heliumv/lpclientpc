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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicLabelUI;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;

/**
 * <p>
 * Gewrapptes JLabel<br/>
 * </p>
 * 
 * implementiert IControl nicht, da activatable und mandatory hier keine rolle
 * spielen.
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class WrapperLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean cutOffEnd = true;

	public WrapperLabel() {
		setDefaults();
	}

	public WrapperLabel(String p0) {
		super(p0);
		setDefaults();
	}

	public WrapperLabel(String p0, int p1) {
		super(p0, p1);
		setDefaults();
	}

	public WrapperLabel(Icon p0) {
		super(p0);
		setDefaults();
	}

	public WrapperLabel(Icon p0, int p1) {
		super(p0, p1);
		setDefaults();
	}

	public WrapperLabel(String p0, Icon p1, int p2) {
		super(p0, p1, p2);
		setDefaults();
	}
	
	/**
	 * Wenn ein Text zu lang ist wird er gek&uuml;rzt und '...'
	 * anstatt des entfernten Textes angef&uuml;gt.<br>
	 * Mit dieser Methode kann man bestimmen, ob der Text am Anfang
	 * oder am Ende gek&uuml;rzt werden soll.
	 * @param b <code>true</code> am Ende k&uuml;rzen, <code>false</code> am Anfang 
	 */
	public void setCutOffEnd(boolean b) {
		cutOffEnd = b;
	}
	
	protected boolean isCutOffEnd() {
		return cutOffEnd;
	}

	protected void setDefaults() {
		setUI(new WrapperLabelUI());
		HelperClient.setDefaultsToComponent(this);
		setHorizontalAlignment(SwingConstants.RIGHT);
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
	public void updateUI() {
		super.updateUI();
		setUI(new WrapperLabelUI());
	}
	
	protected class WrapperLabelUI extends BasicLabelUI {
		
		@Override
		protected String layoutCL(JLabel label, FontMetrics fontMetrics,
				String text, Icon icon, Rectangle viewR, Rectangle iconR,
				Rectangle textR) {
			text = cutOffEnd ? text : new StringBuilder(text).reverse().toString();
			String s = super.layoutCL(label, fontMetrics, text, icon, viewR, iconR, textR);
			return cutOffEnd ? s : new StringBuilder(s).reverse().toString();
		}
	}
}
