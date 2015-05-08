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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JToolTip;

/**
 * Gewrapptes JLabel, Zeigt bei MouseOver den ganzen Text als ToolTip an, wenn
 * dieser gek&uuml;rzt wurde.<br>
 * <b>Derzeit noch experimentell!</b>
 * @author robert
 *
 */
public class WrapperLabelOverlay extends WrapperLabel {

	private static final long serialVersionUID = 1L;

	private boolean textIsShorted = false;
	private String customtoolTipText = null;
	private Rectangle overlay;

	public WrapperLabelOverlay() {
		setDefaults();
	}

	public WrapperLabelOverlay(String p0) {
		super(p0);
	}

	protected void setDefaults() {
		super.setDefaults();
		setUI(new WrapperLabelUI() {
			@Override
			protected String layoutCL(JLabel label, FontMetrics fontMetrics,
					String text, Icon icon, Rectangle viewR, Rectangle iconR,
					Rectangle textR) {
				overlay = fontMetrics.getStringBounds(text, getGraphics()).getBounds();
				overlay.setLocation(new Point());
				if(!isCutOffEnd()) {
					overlay.translate(viewR.width-overlay.width-5, 0);
				}
				String before = text;
				String s = super.layoutCL(label, fontMetrics, text, icon, viewR, iconR, textR);
				textIsShorted = !before.equals(s);
				setToolTipText(customtoolTipText);
				return s;
			}
		});
		setCutOffEnd(true);
	}
	
	@Override
	public void setCutOffEnd(boolean b) {
		// TODO Auto-generated method stub
		super.setCutOffEnd(b);
		setHorizontalAlignment(b ? LEFT : RIGHT);
	}
	
	@Override
	public JToolTip createToolTip() {
		if(customtoolTipText != null)
			return super.createToolTip();
		JToolTip toolTip = new JToolTip();
		WrapperLabel label = new WrapperLabel(this.getText());
		label.setHorizontalAlignment(getHorizontalAlignment());
//		toolTip.setPreferredSize(overlay.getSize());
//		toolTip.add(label);
//		label.setBounds(overlay.getBounds());
		
		toolTip.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
		toolTip.setBackground(getBackground());
		toolTip.setAlignmentX(0);
		toolTip.setVisible(true);
		return toolTip;
	}
	
	@Override
	public Point getToolTipLocation(MouseEvent event) {
		if(customtoolTipText != null)
			return super.getToolTipLocation(event);
		Point p = overlay.getLocation();
		p.translate(-4, 1);
		return p;
	}
	
	@Override
	public void setToolTipText(String text) {
		customtoolTipText = text;
		if(text != null)
			super.setToolTipText(text);
		else if(textIsShorted)
			super.setToolTipText(getText());
		else super.setToolTipText(null);
	}
	
	@Override
	public String getToolTipText() {
		return super.getToolTipText();
	}
}
