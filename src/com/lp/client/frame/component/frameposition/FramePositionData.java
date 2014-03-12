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
package com.lp.client.frame.component.frameposition;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.Serializable;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.Desktop;

public class FramePositionData implements Serializable {
	
	private static final long serialVersionUID = 8549692800870893290L;
	
	private Dimension size;
	private Point location;
	private Dimension iconSize;
	private Point iconLocation;
	private boolean isIconified;
	private boolean isMaximized;
	
	public FramePositionData() {
		
	}

	public FramePositionData(InternalFrame iFrame) {
		Rectangle bounds = iFrame.getBounds();
		setSize(bounds.getSize());
		setLocation(bounds.getLocation());
		setIconSize(iFrame.getDesktopIcon().getSize());
		setIconLocation(iFrame.getDesktopIcon().getLocation());
		setIconified(iFrame.isIcon());
		setMaximized(iFrame.isMaximum());
	}
	
	public FramePositionData(Desktop desktop) {
		setSize(desktop.getSize());
		setLocation(desktop.getLocation());
		setIconified(false);
		setMaximized(desktop.getExtendedState() == Frame.MAXIMIZED_BOTH);
	}

	public void applyto(InternalFrame iFrame) {
		iFrame.setBounds(new Rectangle(getLocation(), getSize()));
//		iFrame.setSize(getSize());
//		iFrame.setLocation(getLocation());
		try {
			iFrame.setIcon(isIconified());
			iFrame.setMaximum(isMaximized());
			iFrame.moveToFront();
		} catch (PropertyVetoException e) {
			//TODO: was machen wenn das nicht klappt?
			//vorerst Pech gehabt
		}
		if(getIconSize() != null)
			iFrame.getDesktopIcon().setSize(getIconSize());
		if(getIconLocation() != null)
			iFrame.getDesktopIcon().setLocation(getIconLocation());
		
	}
	
	public void applyto(Desktop desktop) {
		desktop.setSize(getSize());
		desktop.setLocation(getLocation());
		desktop.setExtendedState( isIconified() ? Frame.ICONIFIED :
			(isMaximized() ? Frame.MAXIMIZED_BOTH : Frame.NORMAL));
	}
	
	// Getters
	public Dimension getSize() {
		return size;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public Dimension getIconSize() {
		return iconSize;
	}
	
	public Point getIconLocation() {
		return iconLocation;
	}
	
	public boolean isIconified() {
		return isIconified;
	}
	
	public boolean isMaximized() {
		return isMaximized;
	}
	
	// Setters
	public void setSize(Dimension size) {
		this.size = size;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public void setIconSize(Dimension iconSize) {
		this.iconSize = iconSize;
	}
	
	public void setIconLocation(Point iconLocation) {
		this.iconLocation = iconLocation;
	}
	
	public void setIconified(boolean isIconified) {
		this.isIconified = isIconified;
	}
	
	public void setMaximized(boolean isMaximized) {
		this.isMaximized = isMaximized;
	}
}
