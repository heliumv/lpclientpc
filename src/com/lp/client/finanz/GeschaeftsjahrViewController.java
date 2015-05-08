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
package com.lp.client.finanz;

import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.system.service.TheClientDto;

public class GeschaeftsjahrViewController implements
		IGeschaeftsjahrViewController {

	private static final String MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG = "MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG";

	private Map<?, ?> allGeschaeftsjahre ;
	private InternalFrameFinanz internalFinanzFrame ;
	private TheClientDto theClientDto ;
	private JMenu menu;
	
	public GeschaeftsjahrViewController(InternalFrameFinanz finanzFrame, TheClientDto clientDto) {
		internalFinanzFrame = finanzFrame ;
		theClientDto = clientDto ;
	}
	
	protected Map<?, ?> getAllGeschaeftsJahre() throws Throwable {
		if(null == allGeschaeftsjahre) {
			allGeschaeftsjahre = DelegateFactory.getInstance().getSystemDelegate().getAllGeschaeftsjahr();
		}
		return allGeschaeftsjahre ;
	}
	
	@Override
	public JMenu getGeschaeftsJahreMenue(
			TabbedPane tabbedPane, ActionListener actionListener) throws Throwable {
		if(menu != null) {
			for(int i = 0; i < menu.getItemCount(); i++) {
				//Damit er nicht mehrfach geadded wird vorher einen remove
				menu.getItem(i).removeActionListener(actionListener);
				menu.getItem(i).addActionListener(actionListener);
			}
			return menu;
		}
	   	menu = new WrapperMenu("fb.menu.geschaeftsjahr", tabbedPane);
		Map<?, ?> mapGJ = getAllGeschaeftsJahre() ;
		ButtonGroup bgGJ = new ButtonGroup();
		for (Iterator<?> iter = mapGJ.keySet().iterator(); iter
				.hasNext();) {
			Integer item = (Integer) iter.next();
			String itemString = item.toString() ;

			JRadioButtonMenuItem menueItem = new JRadioButtonMenuItem(itemString);
			menueItem.addActionListener(actionListener);
			menueItem.setActionCommand(buildCommand(itemString)) ;
 			if(iter.hasNext() == false){
				menueItem.setSelected(true);
				geschaeftsJahrChanged(itemString) ;
			}

			menu.add(menueItem, 0);
			bgGJ.add(menueItem);
		}
		
		return menu ;
	}

	protected String buildCommand(String itemString) {
		return MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG + itemString ;
	}

	@Override
	public String getSelectedGeschaeftsjahr(String command) throws Throwable {
		Map<?, ?> mapGJ = getAllGeschaeftsJahre() ;
		for (Iterator<?> iter = mapGJ.keySet().iterator(); iter.hasNext();) {
			Integer item = (Integer) iter.next();
			String itemString = item.toString() ;
			if (command.equals(buildCommand(itemString))) {
				geschaeftsJahrChanged(itemString) ;
				return itemString ;
			}
		}

		return null ;
	}

	@Override
	public void geschaeftsJahrChanged(String newGeschaeftsJahr) {
		internalFinanzFrame.setAktuellesGeschaeftsjahr(newGeschaeftsJahr) ;
		theClientDto.setGeschaeftsJahr(new Integer(newGeschaeftsJahr)) ;
	}
}
