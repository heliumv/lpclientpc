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
package com.lp.client.frame.report;

import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ImageIcon;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>11.03.05</I></p>
 * 
 * <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.9 $
 */
public class PanelVersandFax extends PanelVersand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelReportIfJRDS jpaPanelReportIf = null;

	private PanelReportKriterien panelReportKriterien = null;

	public PanelVersandFax(InternalFrame internalFrame, String belegartCNr,
			Integer belegIId, PanelReportIfJRDS jpaPanelReportIf,
			PanelReportKriterien panelReportKriterien,
			PartnerDto partnerDtoEmpfaenger) throws Throwable {
		super(internalFrame, belegartCNr, belegIId, partnerDtoEmpfaenger);
		this.jpaPanelReportIf = jpaPanelReportIf;
		this.panelReportKriterien = panelReportKriterien;
		jbInit();
	}

	private void jbInit() throws Throwable {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/fax.png"));
		wbuSenden.setIcon(imageIcon);
		getInternalFrame().addItemChangedListener(this);
		iZeile++;
		jpaWorkingOn.add(wlaBetreff, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBetreff, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	public VersandauftragDto getVersandauftragDto() throws Throwable {
		VersandauftragDto dto = super.getVersandauftragDto();
		// Faxnummer checken
		DelegateFactory.getInstance().getSystemDelegate()
				.validateFaxnummer(super.wtfEmpfaenger.getText());
		//
		StringBuffer sFaxnummer = new StringBuffer();
		// Amtsleitungsvorwahl vorne anhaengen
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_AMTSLEITUNGSVORWAHL);
		sFaxnummer.append(parameter.getCWert()).append(" ");
		sFaxnummer.append(super.wtfEmpfaenger.getText());
		dto.setCEmpfaenger(sFaxnummer.toString());

		// MB 17.01.07 in das Absenderfeld kommt auch beim Fax die Email-Adresse
		// des Benutzers.
		PersonalDto personalDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getIDPersonal());
		if (personalDto.getCEmail() != null) {
			dto.setCAbsenderadresse(personalDto.getCEmail());
		}

		return dto;
	}

	/**
	 * Vorschlagswert fuer den Empfaenger setzen.
	 * 
	 * @throws Throwable
	 */
	protected void setVorschlag() throws Throwable {
		if (partnerDtoEmpfaenger != null) {
			Integer partnerIIdAnsprechpartner = null;
			if (ansprechpartnerIId != null) {
				partnerIIdAnsprechpartner = DelegateFactory.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId)
						.getPartnerIIdAnsprechpartner();
			}

			String report = jpaPanelReportIf.getReportname();

			if (panelReportKriterien.getPanelStandardDrucker().getVariante() != null) {
				ReportvarianteDto varDto = DelegateFactory
						.getInstance()
						.getDruckerDelegate()
						.reportvarianteFindByPrimaryKey(
								panelReportKriterien.getPanelStandardDrucker()
										.getVariante());
				report = varDto.getCReportnamevariante();
			}

			String ubersteuerterEmpfaenger = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.getUebersteuerteEmpfaenger(partnerDtoEmpfaenger, report,
							false);

			if (ubersteuerterEmpfaenger != null
					&& ubersteuerterEmpfaenger.length() > 0) {
				setEmpfaenger(ubersteuerterEmpfaenger);
			} else {
				String p = DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.partnerkommFindOhneExec(
								partnerDtoEmpfaenger.getIId(),
								partnerIIdAnsprechpartner,
								PartnerFac.KOMMUNIKATIONSART_FAX,
								LPMain.getInstance().getTheClient()
										.getMandant());

				String pDirektFax = DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.partnerkommFindOhneExec(
								partnerDtoEmpfaenger.getIId(),
								partnerIIdAnsprechpartner,
								PartnerFac.KOMMUNIKATIONSART_DIREKTFAX,
								LPMain.getInstance().getTheClient()
										.getMandant());

				if (pDirektFax != null && pDirektFax.length() > 0) {
					setEmpfaenger(Helper
							.befreieFaxnummerVonSonderzeichen(pDirektFax));
				} else {
					if (p != null) {
						setEmpfaenger(Helper
								.befreieFaxnummerVonSonderzeichen(p));
					}

				}
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		try {
			panelQueryFLRPartner = PartnerFilterFactory.getInstance()
					.createPanelFLRPartner(getInternalFrame());
			new DialogQuery(panelQueryFLRPartner);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		try {
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				wtfEmpfaenger.setText(null);
				if (e.getSource() == panelQueryFLRPartner) {
					ansprechpartnerIId = null;
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();

					partnerDtoEmpfaenger = DelegateFactory.getInstance()
							.getPartnerDelegate().partnerFindByPrimaryKey(key);

					AnsprechpartnerDto[] dtos = DelegateFactory.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPartnerIIdOhneExc(key);

					if (dtos != null && dtos.length > 0) {
						panelQueryFLRAnsprechpartner = PartnerFilterFactory
								.getInstance().createPanelFLRAnsprechpartner(
										getInternalFrame(), key, null, false,
										true);

						new DialogQuery(panelQueryFLRAnsprechpartner);

					} else {
						setVorschlag();
					}

				} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();
					ansprechpartnerIId = key;

					setVorschlag();
				}

			}

			else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == panelQueryFLRAnsprechpartner) {
					setVorschlag();
				}
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}
}
