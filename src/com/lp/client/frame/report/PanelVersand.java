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
package com.lp.client.frame.report;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandauftragDto;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>11.03.05</I></p>
 *
 * <p> </p>
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.5 $
 */
public abstract class PanelVersand extends JPanel implements ActionListener,
		ItemChangedListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int iZeile = 0;
	protected JPanel jpaWorkingOn = null;
	protected WrapperTimestampField wtsfSendeZeitpunkt = null;
	private WrapperLabel wlaSendeZeitpunkt = null;
	// private WrapperCheckBox wcbBestaetigung=null;
	private GridBagLayout gridbagLayoutAll = null;
	private GridBagLayout gridbagLayoutWorkingOn = null;
	protected WrapperTextField wtfEmpfaenger = null;
	protected WrapperLabel wlaEmpfaenger = null;
	private WrapperButton wbuEmpfaenger = null;
	private WrapperButton wbuEmpfaengerAnsp = null;
	private InternalFrame internalFrame = null;
	protected WrapperButton wbuSenden = null;
	protected WrapperLabel wlaBetreff = null;
	protected WrapperTextField wtfBetreff = null;
	protected PanelQueryFLR panelQueryFLRPartner = null;
	protected PanelQueryFLR panelQueryFLRAnsprechpartner = null;


	protected static final String ACTION_SPECIAL_PARTNER = "action_special_partner";
	protected static final String ACTION_SPECIAL_ANSPRECHPARTNER = "action_special_ansprechpartner";

	protected PartnerDto partnerDtoEmpfaenger = null;
	protected Integer ansprechpartnerIId = null;
	public static final String ACTION_SPECIAL_SENDEN = "action_special_senden";
	private String belegartCNr = null;
	private Integer belegIId = null;

	public PanelVersand(InternalFrame internalFrame, String belegartCNr,
			Integer belegIId,PartnerDto partnerDtoEmpfaenger) throws Throwable {
		this.internalFrame = internalFrame;
		this.belegartCNr = belegartCNr;
		this.belegIId = belegIId;
		this.partnerDtoEmpfaenger=partnerDtoEmpfaenger;
		jbInit();
		setDefaults();
	}

	private void setDefaults() throws Throwable {
		wtfBetreff.setText(DelegateFactory.getInstance().getVersandDelegate()
				.getDefaultDateinameForBelegEmail(belegartCNr, belegIId));
	}

	protected InternalFrame getInternalFrame() {
		return this.internalFrame;
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		gridbagLayoutAll = new GridBagLayout();
		this.setLayout(gridbagLayoutAll);
		jpaWorkingOn = new JPanel();
		gridbagLayoutWorkingOn = new GridBagLayout();
		jpaWorkingOn.setLayout(gridbagLayoutWorkingOn);
		wbuSenden = new WrapperButton(LPMain.getInstance().getTextRespectUISPr(
				"lp.senden"));
		wbuSenden.setActionCommand(ACTION_SPECIAL_SENDEN);
		wlaSendeZeitpunkt = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.sendezeitpunkt"));
		wlaSendeZeitpunkt.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaSendeZeitpunkt.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wtsfSendeZeitpunkt = new WrapperTimestampField();
		wtsfSendeZeitpunkt.setMinimumSize(new Dimension(200, Defaults
				.getInstance().getControlHeight()));
		wtsfSendeZeitpunkt.setPreferredSize(new Dimension(200, Defaults
				.getInstance().getControlHeight()));
		wtfEmpfaenger = new WrapperTextField(VersandFac.MAX_EMPFAENGER);
		wtfEmpfaenger.setMandatoryField(true);
		wbuEmpfaenger = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("lp.versand.partner"));
		wbuEmpfaenger.setActionCommand(ACTION_SPECIAL_PARTNER);
		wbuEmpfaenger.addActionListener(this);

		wbuEmpfaengerAnsp = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("lp.versand.ansprechpartner"));
		wbuEmpfaengerAnsp.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER);
		wbuEmpfaengerAnsp.addActionListener(this);

		wlaEmpfaenger = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.versand.an"));
		wlaBetreff = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.betreff"));
		wtfBetreff = new WrapperTextField();
		wtfBetreff.setColumnsMax(100);
		wtfBetreff.setMandatoryField(true);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaSendeZeitpunkt, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtsfSendeZeitpunkt, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuSenden, new GridBagConstraints(2, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuEmpfaenger, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 70, 0));

		if (partnerDtoEmpfaenger != null) {

			jpaWorkingOn.add(wbuEmpfaengerAnsp, new GridBagConstraints(0,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 85, 2, 2), 70, 0));
		}
		jpaWorkingOn.add(wlaEmpfaenger, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 150, 2, 2), 30, 0));
		jpaWorkingOn.add(wtfEmpfaenger, new GridBagConstraints(1, iZeile, 2, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
	}

	public VersandauftragDto getVersandauftragDto() throws Throwable {
		VersandauftragDto dto = new VersandauftragDto();
		dto.setCEmpfaenger(wtfEmpfaenger.getText());
		dto.setTSendezeitpunktwunsch(wtsfSendeZeitpunkt.getTimestamp());
		dto.setCBetreff(wtfBetreff.getText());
		return dto;
	}

	public void setSendezeitpunkt(Timestamp tSendezeitpunkt) {
		wtsfSendeZeitpunkt.setTimestamp(tSendezeitpunkt);
	}

	public void setDefaultAbsender(PartnerDto partnerDtoEmpfaenger,
			Integer ansprechpartnerIId) throws Throwable {
		this.partnerDtoEmpfaenger = partnerDtoEmpfaenger;
		this.ansprechpartnerIId = ansprechpartnerIId;
		setVorschlag();
	}

	public void setEmpfaenger(String sEmpfaenger) {
		wtfEmpfaenger.setText(sEmpfaenger);
	}

	/**
	 * Vorschlagswert fuer den Empfaenger setzen.
	 * 
	 * @throws Throwable
	 */
	protected abstract void setVorschlag() throws Throwable;

	protected WrapperButton getWbuSenden() {
		return wbuSenden;
	}

	protected String getCBetreff() {
		return wtfBetreff.getText();
	}

	public void setBetreff(String sBetreff) {
		wtfBetreff.setText(sBetreff);
	}

	public String getbelegartCNr() {
		return belegartCNr;
	}

	public Integer getbelegIId() {
		return belegIId;
	}
}
