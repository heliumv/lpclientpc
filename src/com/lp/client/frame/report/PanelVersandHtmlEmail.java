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
package com.lp.client.frame.report;

import java.awt.Component;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.IHvValueHolderListener;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperHtmlField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.util.Helper;

public class PanelVersandHtmlEmail extends PanelVersandEmail {
	private static final long serialVersionUID = 1409529263005103286L;
	private WrapperHtmlField whtmlField ;
	private String cachedDefaultText ;
	
	public PanelVersandHtmlEmail(InternalFrame internalFrame,
			MailtextDto mailtextDto, String belegartCNr, Integer belegIId,
			PanelReportIfJRDS jpaPanelReportIf,
			PanelReportKriterien panelReportKriterien,
			PartnerDto partnerDtoEmpfaenger, WrapperHtmlField htmlField) throws Throwable {
		super(internalFrame, mailtextDto, belegartCNr, belegIId, jpaPanelReportIf, panelReportKriterien, partnerDtoEmpfaenger) ;
		this.whtmlField = htmlField ;
		/*
		 * Hintergrund:
		 * Im Constructor von PanelversandEmail wird bereits alles zusammengebaut und dann auch gleich setDefaultText aufgerufen
		 * Zu diesem Zeitpunkt ist aber whtmlField noch gar nicht gesetzt. hint: Es gibt schon einen Grund, warum man
		 * "Composition" der "Inheritance" vorzieht. Mir ist bewusst, dass das alles grausam ist. (ghp)
		 */
		if(cachedDefaultText != null) {
			whtmlField.setText(cachedDefaultText);
		}
		
		wtfEmpfaenger.addValueChangedListener(new EmpfaengerChanged());
	}

	protected void setDefaultText() throws Throwable {
		cachedDefaultText = DelegateFactory.getInstance()
				.getVersandDelegate().getDefaultTextForBelegHtmlEmail(getMailtextDto());
		if(whtmlField != null) {
			whtmlField.setText(cachedDefaultText);			
		}
	}
	
	@Override
	protected void setDefaultsPanel() throws Throwable {
		super.setDefaultsPanel() ;
		
		if(!Helper.isStringEmpty(getMailtextDto().getMailBetreff())) {
			setBetreff(getMailtextDto().getMailBetreff());
		}
	}

	
	public class EmpfaengerChanged implements IHvValueHolderListener {
		@Override
		public void valueChanged(Component reference, Object oldValue, Object newValue) {
			System.out.println("empfaenger value changed from <" + oldValue + "> to <" + newValue + ">") ;
			if(newValue == null) return ;
			
			System.out.println("new partner id " + partnerDtoEmpfaenger.getIId()) ;
			System.out.println("new ansprechpartner id " + ansprechpartnerIId) ;
			if(!HelperClient.nullableEquals(getMailtextDto().getMailPartnerIId(), partnerDtoEmpfaenger.getIId()) ||
				!HelperClient.nullableEquals(getMailtextDto().getMailAnprechpartnerIId(), ansprechpartnerIId)) {
				getMailtextDto().setMailPartnerIId(partnerDtoEmpfaenger.getIId());
				getMailtextDto().setMailAnprechpartnerIId(ansprechpartnerIId) ;
				try {
				 setDefaultText() ;
				} catch(Throwable t) {
					System.out.println("Throwable t " + t.getMessage()) ;
				}
			}
		}	
	}	
}
