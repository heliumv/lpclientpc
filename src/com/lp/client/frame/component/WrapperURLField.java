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


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;



/**
 * <p> Diese Klasse besteht aus einem TextField mit einem Button.
 * Bei Buttonklick wird die URL des TextFields im Browser ge&ouml;ffnet.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004 - 2007</p>
 *
 * <p>Erstellung: Martin Bluehweis; 28.07.07</p>
 *
 * <p>@author $Author: robert $</p>
 *
 * @version not attributable Date $Date: 2012/09/24 14:59:59 $
 */


public class WrapperURLField extends WrapperTextFieldWithIconButton
{
	private final static long serialVersionUID = 5232388859414464532L;

	public WrapperURLField() throws Throwable {
		super(new ImageIcon(WrapperURLField.class.getResource("/com/lp/client/res/earth_view.png")),
				"lp.websiteanzeigen");
	}

	@Override
	protected void actionEventImpl(ActionEvent e) {
		try {
		    if(wtfText.getText()!=null && wtfText.getText().trim().length()>0) {
		    	int i= wtfText.getText().indexOf("://");
		    	URI uri = new URI( (i < 0 ? "http://" : "") + wtfText.getText().trim()) ;
	    		java.awt.Desktop.getDesktop().browse(uri);
		    }
		} catch (URISyntaxException ex1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),LPMain.getTextRespectUISPr("lp.fehlerhafteurl"));
		} catch (IOException ex1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),ex1.getMessage());
		}
	}
}
