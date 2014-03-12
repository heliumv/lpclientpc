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
package com.lp.client.partner;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.KommunikationsartDto;
import com.lp.server.partner.service.PartnerartDto;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.partner.service.SelektionDto;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um den Partner.</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Organisation: </p>
 *
 * @author $Author: christian $
 * @version $Revision: 1.5 $
 */
public class InternalFramePartner extends InternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int IDX_PANE_PARTNER = 0;
	public static int IDX_PANE_BANK = 1;
	public static int IDX_PANE_SERIENBRIEF = 2;
	public static int IDX_PANE_GRUNDDATEN = 3;

	private AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = new AnsprechpartnerfunktionDto();
	private BrancheDto brancheDto = new BrancheDto();
	private PartnerklasseDto partnerklasseDto = new PartnerklasseDto();
	private TabbedPanePartner tpPartner = null;
	private TabbedPaneBank tpBank = null;
	private TabbedPaneGrunddatenPartner tpGrunddaten = null;
	private PartnerartDto partnerartDto = new PartnerartDto();
	private KommunikationsartDto kommunikationsartDto = new KommunikationsartDto();
	private SelektionDto SelektionDto = new SelektionDto();

	private String rechtModulweit = null;

	public DialogWiedervorlage dialogWiedervorlage = null;

	private TabbedPaneSerienbrief tpSerienbrief = null;

	public InternalFramePartner(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {

		super(title, belegartCNr, sRechtModulweitI);
		rechtModulweit = sRechtModulweitI;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		// 1 unteres tab: Partner; lazy loading; ist auch default.
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr("part.partner"), null,
				tpPartner,
				LPMain.getInstance().getTextRespectUISPr("part.partner"),
				IDX_PANE_PARTNER);

		// 2 unteres tab: Bank; lazy loading.
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr("part.kund.banken"),
				null, tpBank,
				LPMain.getInstance().getTextRespectUISPr("part.kund.banken"),
				IDX_PANE_BANK);

		// 3 tab unten: Serienbrief
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.serienbrief"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.serienbrief"),
				IDX_PANE_SERIENBRIEF);

		// 4 tab unten: Grunddaten
		// nur anzeigen wenn Benutzer Recht dazu hat
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.grunddaten"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.grunddaten"), IDX_PANE_GRUNDDATEN);
		}

		// Defaulttabbedpane setzen.
		refreshPartnerTP();
		tpPartner.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tpPartner);

		// ich selbst moechte informiert werden.
		addItemChangedListener(this);
		// awt: listener bin auch ich.
		registerChangeListeners();

		// dem frame das icon setzen
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/businessmen16x16.png"));
		setFrameIcon(iicon);
		// Menue
		// setJMenuBar(new WrapperMenuBar(this));
	}

	// TODO-AGILCHANGES
	/**
	 * AGILPRO CHANGES Changed visiblity from protected to public
	 * 
	 * @author Lukas Lisowski
	 * @param e
	 *            EventObject
	 * @throws Throwable
	 */
	public void lPStateChanged(EventObject e) throws Throwable {

		setRechtModulweit(rechtModulweit);

		// TODO-AGILCHANGES
		/**
		 * AGILPRO CHANGES BEGIN
		 * 
		 * @author Lukas Lisowski
		 */
		int selectedCur = 0;

		try {
			selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();
		} catch (Exception ex) {

			selectedCur = ((Desktop) e.getSource()).getSelectedIndex();
		}

		if (selectedCur == IDX_PANE_PARTNER) {
			refreshPartnerTP();
			tpPartner.lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN, LPMain
					.getInstance().getTextRespectUISPr("part.partner"));
		}

		else if (selectedCur == IDX_PANE_BANK) {
			refreshBankTP();
			tpBank.lPEventObjectChanged(null);
			// emptytable: TP: 0 jetzt ist die tpBank angelegt.
			tpBank.lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN, LPMain
					.getInstance().getTextRespectUISPr("lp.bank"));
		}

		else if (selectedCur == IDX_PANE_GRUNDDATEN) {
			refreshGrunddatenTP();
			tabbedPaneRoot.setSelectedComponent(tpGrunddaten);
			tpGrunddaten.lPEventObjectChanged(null);
			setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
					LPMain.getInstance().getTextRespectUISPr(
							"bes.title.panel.bestellunggrunddaten"));
		}

		else if (selectedCur == IDX_PANE_SERIENBRIEF) {
			refreshSerienbriefTP();
			tabbedPaneRoot.setSelectedComponent(tpSerienbrief);
			tpSerienbrief.lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN, LPMain
					.getInstance().getTextRespectUISPr("lp.serienbrief"));
		}

	}

	/**
	 * refreshSerienbriefTP
	 * 
	 * @throws Throwable
	 */
	private void refreshSerienbriefTP() throws Throwable {
		if (tpSerienbrief == null) {
			// lazy loading
			tpSerienbrief = new TabbedPaneSerienbrief(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_SERIENBRIEF, tpSerienbrief);
			initComponents();
		}
	}

	private void refreshGrunddatenTP() throws Throwable {
		if (tpGrunddaten == null) {
			// lazy loading
			tpGrunddaten = new TabbedPaneGrunddatenPartner(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_GRUNDDATEN, tpGrunddaten);
			initComponents();
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) {
		// nothing here
	}

	public AnsprechpartnerfunktionDto getAnsprechpartnerfunktionDto() {
		return ansprechpartnerfunktionDto;
	}

	public BrancheDto getBrancheDto() {
		return brancheDto;
	}

	public PartnerklasseDto getPartnerklasseDto() {
		return partnerklasseDto;
	}

	public PartnerartDto getPartnerartDto() {
		return partnerartDto;
	}

	public KommunikationsartDto getKommunikationsartDto() {
		return kommunikationsartDto;
	}

	public TabbedPanePartner getTpPartner() {
		return tpPartner;
	}

	public SelektionDto getSelektionDto() {
		return SelektionDto;
	}

	private void refreshPartnerTP() throws Throwable {
		if (tpPartner == null) {
			tpPartner = new TabbedPanePartner(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_PARTNER, tpPartner);
			initComponents();
		}
	}

	private void refreshBankTP() throws Throwable {
		if (tpBank == null) {
			tpBank = new TabbedPaneBank(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_BANK, tpBank);
			initComponents();
		}
	}

	public void setAnsprechpartnerfunktionDto(
			AnsprechpartnerfunktionDto ansprechpartnerfunktionDto) {
		this.ansprechpartnerfunktionDto = ansprechpartnerfunktionDto;
	}

	public void setBrancheDto(BrancheDto brancheDto) {
		this.brancheDto = brancheDto;
	}

	public void setPartnerklasseDto(PartnerklasseDto partnerklasseDto) {
		this.partnerklasseDto = partnerklasseDto;
	}

	public void setPartnerartDto(PartnerartDto partnerartDto) {
		this.partnerartDto = partnerartDto;
	}

	public void setKommunikationsartDto(
			KommunikationsartDto kommunikationsartDto) {
		this.kommunikationsartDto = kommunikationsartDto;
	}

	public void setSelektionDto(SelektionDto SelektionDto) {
		this.SelektionDto = SelektionDto;
	}

	protected void menuActionPerformed(ActionEvent e) throws Throwable {

	}

	public PropertyVetoException vetoableChangeLP() throws Throwable {
		if (dialogWiedervorlage != null) {
			dialogWiedervorlage.dispose();
			dialogWiedervorlage = null;
		}

		return super.vetoableChangeLP();
	}

}
