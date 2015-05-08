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

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.AnsprechpartnerDto;

/*
 * <p> Diese Klasse beinhaltet ein TextField mit Button.
 * Bei Buttonklick wird die E-Mail Adresse im TextField an das
 * Standard-EMail programm geleitet.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004 - 2007</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 28.07.07</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2012/10/16 07:12:06 $
 */
public class WrapperEmailField extends WrapperTextFieldWithIconButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6546632209234418813L;

	private AnsprechpartnerDto ansprechpartnerDto = null;

	public WrapperEmailField() throws Throwable {
		super(new ImageIcon(WrapperEmailField.class
				.getResource("/com/lp/client/res/mail.png")),
				"lp.emailversenden");
		wtfText.setColumnsMax(80);
	}

	public void setEmail(String adresse, AnsprechpartnerDto ansprechpartnerDto) {
		this.ansprechpartnerDto = ansprechpartnerDto;

		wtfText.setText(adresse);
	}

	public void setColumnsMax(int columnsMax) {
		wtfText.setColumnsMax(columnsMax);
	}

	@Override
	protected void actionEventImpl(ActionEvent e) {
		if (wtfText.getText() != null && wtfText.getText().length() > 0) {
			String body = "";
			if (ansprechpartnerDto != null) {
				try {
					body = DelegateFactory.getInstance()
							.getPartnerServicesDelegate()
							.getBriefanredeFuerBeleg(
									ansprechpartnerDto.getIId(),
									ansprechpartnerDto.getPartnerIId());
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			} else {
				body = LPMain
						.getTextRespectUISPr("lp.anrede.sehrgeehrtedamenundherren");
			}
			try {
				java.awt.Desktop.getDesktop().mail(
						new URI("mailto", wtfText.getText().trim() + "?BODY="
								+ body, null));
			} catch (IOException e1) {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), e1.getMessage());
			} catch (URISyntaxException e1) {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), e1.getMessage());
			}

		}
	}
}
