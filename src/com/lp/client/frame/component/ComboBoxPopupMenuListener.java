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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;

public class ComboBoxPopupMenuListener implements PopupMenuListener {

	private boolean popupWider;
	private int maximumWidth = -1;
	private JScrollPane scrollPane;

	public ComboBoxPopupMenuListener() {
		this(true, -1);
	}

	public ComboBoxPopupMenuListener(boolean popupWider, int maximumWidth) {
		setPopupWider( popupWider );
		setMaximumWidth( maximumWidth );
	}

	public void setMaximumWidth(int maximumWidth) {
		this.maximumWidth = maximumWidth;
	}

	public void setPopupWider(boolean popupWider) {
		this.popupWider = popupWider;
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		JComboBox comboBox = (JComboBox)e.getSource();

		if (comboBox.getItemCount() == 0) return;

		final Object child = comboBox.getAccessibleContext().getAccessibleChild(0);

		if (child instanceof BasicComboPopup) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					customizePopup((BasicComboPopup)child);
				}
			});
		}
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		// TODO Auto-generated method stub

	}

	protected void customizePopup(BasicComboPopup popup) {
		scrollPane = getScrollPane(popup);

		if (popupWider)
			setPopupWidth( popup );

		Component comboBox = popup.getInvoker();
		Point location = comboBox.getLocationOnScreen();

		int height = comboBox.getPreferredSize().height;
		popup.setLocation(location.x, location.y + height - 1);

	}

	protected void setPopupWidth(BasicComboPopup popup)	{

		int popupWidth = popup.getList().getPreferredSize().width
					   + 5  // damit horizontale Scrollbar nicht erscheint
					   + getScrollBarWidth(popup, scrollPane)
					   ;

		if (maximumWidth != -1)
		{
			popupWidth = Math.min(popupWidth, maximumWidth);
		}

		Dimension scrollPaneSize = scrollPane.getPreferredSize();
		popupWidth = Math.max(popupWidth, scrollPaneSize.width);

		scrollPaneSize.width = popupWidth;
		scrollPane.setPreferredSize(scrollPaneSize);
		scrollPane.setMaximumSize(scrollPaneSize);
	}

	protected JScrollPane getScrollPane(BasicComboPopup popup) {
		return (JScrollPane)SwingUtilities.getAncestorOfClass(JScrollPane.class, popup.getList());
	}

	protected int getScrollBarWidth(BasicComboPopup popup, JScrollPane scrollPane) {

		int scrollBarWidth = 0;
		JComboBox comboBox = (JComboBox)popup.getInvoker();

		if (comboBox.getItemCount() > comboBox.getMaximumRowCount()) {
			JScrollBar vertical = scrollPane.getVerticalScrollBar();
			scrollBarWidth = vertical.getPreferredSize().width;
		}

		return scrollBarWidth;
	}

}

