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
package com.lp.client.partner;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedEventDrop;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.media.DropPanelSplit;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.partner.fastlanereader.generated.service.FLRPASelektionPK;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.KontaktDto;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerImportDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.csv.LPCSVReader;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um den Partner.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum xx.12.04</p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.27 $ Date $Date: 2013/01/17 10:44:59 $
 */
public class TabbedPanePartner extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BankDto bankDto = new BankDto();
	private PartnerDto partnerDto = new PartnerDto();
	private PASelektionDto pASelektionDto = new PASelektionDto();
	private PartnerbankDto partnerBankDto = new PartnerbankDto();
	private PartnerkommunikationDto partnerkommunikationDto = new PartnerkommunikationDto();
	private AnsprechpartnerDto ansprechpartnerDto = new AnsprechpartnerDto();
	public static final int IDX_PANEL_QP = 0;
	public static final int IDX_PANEL_D = 1;
	public static final int IDX_PANE_KOMMUNIKATION_SP = 2;
	public static final int IDX_PANE_BANK_SP = 3;
	public static final int IDX_PANE_ANREDEN_D = 4;
	public static final int IDX_PANE_PARTNERSELEKTION_SP = 5;
	public static final int IDX_PANE_KURZBRIEF_SP = 6;
	public static final int IDX_PANE_ANSPRECHPARTNER = 7;
	public static final int IDX_PANE_ANSPRECHPARTNER_VON = 8;
	public static final int IDX_PANE_REFERENZ = 9;
	public static final int IDX_PANE_KONTAKT = 10;

	private WrapperMenuBar wrapperMenuBar = null;

	private PanelQuery panelPartnerQP = null;
	private PanelBasis panelPartnerD = null;

	private PanelQuery panelKommunikationTopQP = null;
	private PanelBasis panelKommunikationBottomD = null;
	private PanelBasis panelKommunikationSP = null;

	private PanelQuery panelBankTopQP = null;
	private PanelBasis panelBankBottomD = null;
	private PanelSplit panelBankSP = null;

	private PanelPartnerAnreden panelPartnerAnredenD = null;

	private PanelQuery panelSelektionTopQP = null;
	private PanelPASelektion panelSelektionBottomD = null;
	private PanelSplit panelSelektionSP = null;

	private PanelPartnerKurzbrief panelKurzbriefBottomD = null;
	private PanelQuery panelKurzbriefTopQP = null;
	private PanelSplit panelKurzbriefSP = null;

	private PanelBasis panelAnsprechpartnerSP5 = null;
	private PanelQuery panelAnsprechpartnerTopQP5 = null;
	private PanelBasis panelAnsprechpartnerBottomD5 = null;

	private PanelBasis panelSplitKontakt = null;
	public PanelQuery panelQueryKontakt = null;
	private PanelBasis panelDetailKontakt = null;

	private PanelQuery panelAnsprechpartnerVonQP = null;
	private KurzbriefDto kurzbriefDto = new KurzbriefDto();

	private PanelQuery panelReferenzQP = null;

	private String rechtModulweit = null;

	private final String MENUE_ACTION_ADRESSETIKETT = "MENUE_ACTION_ADRESSETIKETT";
	private final String MENUE_ACTION_PARTNERZUSAMMENFUEHREN = "MENUE_ACTION_PARTNERZUSAMMENFUEHREN";
	private final String MENUE_ACTION_CSVIMPORT = "MENUE_ACTION_CSVIMPORT";
	private final String MENUE_ACTION_STAMMBLATT = "MENUE_ACTION_STAMMBLATT";
	private final String MENUE_ACTION_WIEDERVORLAGE = "MENUE_ACTION_WIEDERVORLAGE";
	private final String MENUE_ACTION_GEBURTSTAGSLISTE = "MENUE_ACTION_GEBURTSTAGSLISTE";
	private static final String ACTION_SPECIAL_NEW_EMAIL = "action_special_"
			+ PanelBasis.ALWAYSENABLED + "new_email_entry";

	public TabbedPanePartner(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"part.partner"));
		rechtModulweit = getInternalFrame().getRechtModulweit();
		jbInit();
		initComponents();

	}

	private void jbInit() throws Throwable {

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_QP);

		// 2 tab oben: D2 Partnerdeatil; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_D);

		// 3 tab oben; Splitpane Kommunikation; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kommunikation"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kommunikation"),
				IDX_PANE_KOMMUNIKATION_SP);

		// 4 tab oben; Splitpane Banken; lazy loading
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"part.kund.bankverbindung"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"part.kund.bankverbindung"), IDX_PANE_BANK_SP);

		// 5 tab oben; Detail Anreden; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.anreden"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.anreden"),
				IDX_PANE_ANREDEN_D);

		// 6 tab oben; Detail Partner Selektionslisten; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
				IDX_PANE_PARTNERSELEKTION_SP);

		// tab oben; Splitpane Kurzbrief
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kurzbrief"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kurzbrief"),
				IDX_PANE_KURZBRIEF_SP);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"label.ansprechpartner"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"label.ansprechpartner"), IDX_PANE_ANSPRECHPARTNER);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"part.partner.ansprechpartnervon"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"part.partner.ansprechpartnervon"),
				IDX_PANE_ANSPRECHPARTNER_VON);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.referenz"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.referenz"),
				IDX_PANE_REFERENZ);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_KONTAKTMANAGMENT)) {
			insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kontakt"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.kontakt"),
					IDX_PANE_KONTAKT);
		}

		// defaults
		// QP1 ist default.
		setSelectedComponent(panelPartnerQP);
		refreshPartnerQP1();

		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelPartnerQP.getSelectedId() == null) {
			getInternalFrame().enableAllPanelsExcept(false);
		}

		getPartnerDto().setIId((Integer) panelPartnerQP.getSelectedId());

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelPartnerQP,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
		/**
		 * @todo MB: das ist nicht schoen - bitte wie in den anderen modulen
		 *       behandeln
		 * 
		 *       JMenuBar jMenuBar = getJMenuBar();
		 *       getInternalFrame().setJMenuBar(jMenuBar);
		 *       HelperClient.setComponentNamesMenuBar(jMenuBar);
		 */
		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("part.partner") + "|"
						+ getPartnerDto().formatFixTitelName1Name2());
	}

	private void refreshReferenzQP(Integer iIdPartnerI) throws Throwable {
		if (panelReferenzQP == null) {

			panelReferenzQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_REFERENZ_ZU, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.referenz"), true);

			setComponentAt(IDX_PANE_REFERENZ, panelReferenzQP);
		}

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_I_ID, true, "" + iIdPartnerI + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;

		panelReferenzQP.setDefaultFilter(kriterien);

	}

	private void refreshAnsprechpartnerSP5(Integer iIdPartnerI)
			throws Throwable {

		if (panelAnsprechpartnerSP5 == null) {

			// die zusaetzlichen Buttons am PanelQuery anbringen
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKAnsprechpartner(iIdPartnerI);
			panelAnsprechpartnerTopQP5 = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_ANSPRECHPARTNER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.ansprechpartner"), true);

			panelAnsprechpartnerBottomD5 = new PanelPartnerAnsprechpartner(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.ansprechpartner"), null);

			panelAnsprechpartnerSP5 = new PanelSplit(getInternalFrame(),
					panelAnsprechpartnerBottomD5, panelAnsprechpartnerTopQP5,
					150);
			panelAnsprechpartnerTopQP5
					.befuellePanelFilterkriterienDirektUndVersteckte(
							PartnerFilterFactory.getInstance()
									.createFKDAnsprechpartnerPartnerName(),
							null, PartnerFilterFactory.getInstance()
									.createFKVAnsprechpartner());

			setComponentAt(IDX_PANE_ANSPRECHPARTNER, panelAnsprechpartnerSP5);
		} else {
			// filter refreshen.
			panelAnsprechpartnerTopQP5.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKAnsprechpartner(iIdPartnerI));
		}
	}

	public AnsprechpartnerDto getAnsprechpartnerDto() {
		return ansprechpartnerDto;
	}

	public void setAnsprechpartnerDto(AnsprechpartnerDto ansprechpartnerDto) {
		this.ansprechpartnerDto = ansprechpartnerDto;
	}

	private void refreshAnsprechpartnervonQP(Integer iIdPartnerI)
			throws Throwable {
		if (panelAnsprechpartnerVonQP == null) {

			panelAnsprechpartnerVonQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_ANSPRECHPARTNERPARTNER, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"part.partner.ansprechpartnervon"), true);

			setComponentAt(IDX_PANE_ANSPRECHPARTNER_VON,
					panelAnsprechpartnerVonQP);
		}

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_I_ID,
				true, "" + iIdPartnerI + "", FilterKriterium.OPERATOR_EQUAL,
				false);

		kriterien[0] = krit1;

		panelAnsprechpartnerVonQP.setDefaultFilter(kriterien);

	}

	private void refreshPartnerQP1() throws Throwable {
		if (panelPartnerQP == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelPartnerQP = new PanelQuery(PartnerFilterFactory.getInstance()
					.createQTPartnerart(), null, QueryParameters.UC_ID_PARTNER,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true,
					PartnerFilterFactory.getInstance().createFKVPartner(), null);

			
			panelPartnerQP.befuellePanelFilterkriterienDirekt(
					PartnerFilterFactory.getInstance().createFKDPartnerName(),
					PartnerFilterFactory.getInstance()
							.createFKDPartnerLandPLZOrt());

			panelPartnerQP.addDirektFilter(PartnerFilterFactory.getInstance()
					.createFKDPartnerErweiterteSuche());

			panelPartnerQP.addDirektFilter(PartnerFilterFactory.getInstance()
					.createFKDPartnersucheNachTelefonnummer());

			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
			boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
					.getCWertAsObject();

			if (!bSuchenInklusiveKbez) {
				panelPartnerQP.addDirektFilter(PartnerFilterFactory
						.getInstance().createFKDPartnerKurzbezeichnung());
			}

			setComponentAt(IDX_PANEL_QP, panelPartnerQP);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelPartnerQP) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				// Partner lesen
				if (key != null) {
					setPartnerDto(DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey((Integer) key));
					// key lock
					getInternalFrame().setKeyWasForLockMe(key + "");

					refreshPartnerD2((Integer) key);
					panelPartnerD.setKeyWhenDetailPanel(key);
				}

				// emptytable: TP: 3 zB. nach einem filtern ist alles leer.
				// jetzt alle (-) oberen Laschen wieder aktivieren.
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_QP, (key != null));
			} else if (eI.getSource() == panelKommunikationTopQP) {
				Integer iId = (Integer) panelKommunikationTopQP.getSelectedId();
				panelKommunikationBottomD.setKeyWhenDetailPanel(iId);
				panelKommunikationBottomD.eventYouAreSelected(false);
				// btnstate: 4b im QP die Buttons setzen.
				LockStateValue d = panelKommunikationBottomD
						.getLockedstateDetailMainKey();
				panelKommunikationTopQP.updateButtons(d);
			} else if (eI.getSource() == panelAnsprechpartnerTopQP5) {

				Integer iIdAnsprechpartner = (Integer) panelAnsprechpartnerTopQP5
						.getSelectedId();
				Integer iIdPartner = getPartnerDto().getIId();
				refreshAnsprechpartnerSP5(iIdPartner);
				panelAnsprechpartnerBottomD5
						.setKeyWhenDetailPanel(iIdAnsprechpartner);
				panelAnsprechpartnerBottomD5.eventYouAreSelected(false);
				panelAnsprechpartnerTopQP5
						.updateButtons(panelAnsprechpartnerBottomD5
								.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryKontakt) {

				Integer iIdAnsprechpartner = (Integer) panelQueryKontakt
						.getSelectedId();
				Integer iIdPartner = getPartnerDto().getIId();
				refreshAnsprechpartnerSP5(iIdPartner);
				panelDetailKontakt.setKeyWhenDetailPanel(iIdAnsprechpartner);
				panelDetailKontakt.eventYouAreSelected(false);
				panelQueryKontakt.updateButtons(panelDetailKontakt
						.getLockedstateDetailMainKey());
			}

			else if (eI.getSource() == panelBankTopQP) {
				Integer iId = (Integer) panelBankTopQP.getSelectedId();
				panelBankBottomD.setKeyWhenDetailPanel(iId);
				panelBankBottomD.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelBankTopQP.updateButtons();
			} else if (eI.getSource() == panelSelektionTopQP) {
				Object pASelektionPK = panelSelektionTopQP.getSelectedId();
				panelSelektionBottomD.setKeyWhenDetailPanel(pASelektionPK);
				panelSelektionBottomD.eventYouAreSelected(false);
				panelSelektionTopQP.updateButtons();
			} else if (eI.getSource() == panelKurzbriefTopQP) {
				Integer iIdPartner = (Integer) getPartnerDto().getIId();
				refreshKurzbriefSP9(iIdPartner);
				Integer iId = (Integer) panelKurzbriefTopQP.getSelectedId();
				panelKurzbriefBottomD.setKeyWhenDetailPanel(iId);
				panelKurzbriefBottomD.eventYouAreSelected(false);
				panelKurzbriefTopQP.updateButtons();
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelKommunikationBottomD) {
				// btnstate: 2 im QP die Buttons in den Zustand neu setzen.
				panelKommunikationTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBankBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBankTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelSelektionBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelSelektionTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelKurzbriefBottomD) {
				panelKurzbriefTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				panelKurzbriefBottomD.beEditMode(true);
			} else if (eI.getSource() == panelAnsprechpartnerBottomD5) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAnsprechpartnerTopQP5.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelDetailKontakt) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryKontakt.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelPartnerQP) {
				// ...im QP1 auf new gedrueckt.
				refreshPartnerD2(null);
				panelPartnerD.eventActionNew(eI, true, false);
				setSelectedComponent(panelPartnerD);
			} else if (eI.getSource() == panelKommunikationTopQP) {
				panelKommunikationBottomD.eventActionNew(eI, true, false);
				panelKommunikationBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelKommunikationSP);
			} else if (eI.getSource() == panelBankTopQP) {
				panelBankBottomD.eventActionNew(eI, true, false);
				panelBankBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelBankSP);
			} else if (eI.getSource() == panelSelektionTopQP) {
				panelSelektionBottomD.eventActionNew(eI, true, false);
				panelSelektionBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelSelektionSP);
			} else if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				panelAnsprechpartnerBottomD5.eventActionNew(eI, true, false);
				panelAnsprechpartnerBottomD5.eventYouAreSelected(false);
				setSelectedComponent(panelAnsprechpartnerSP5);
			} else if (eI.getSource() == panelKurzbriefTopQP) {
				panelKurzbriefBottomD.bePlain();
				panelKurzbriefBottomD.eventActionNew(eI, true, false);
				panelKurzbriefBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelKurzbriefSP);
			} else if (eI.getSource() == panelQueryKontakt) {
				panelDetailKontakt.eventActionNew(eI, true, false);
				panelDetailKontakt.eventYouAreSelected(false);
				setSelectedComponent(panelSplitKontakt);
			}
		}

		else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelPartnerQP) {
				// jetzt ab zu D2.
				setSelectedComponent(panelPartnerD);
			} else if (eI.getSource() instanceof PanelQuery) {
				if (((PanelQuery) eI.getSource()).getIdUsecase() == QueryParameters.UC_ID_WIEDERVORLAGE) {
					// Goto

					((PanelQuery) eI.getSource()).setCursor(new Cursor(
							Cursor.WAIT_CURSOR));

					Integer key = (Integer) ((PanelQuery) eI.getSource())
							.getSelectedId();
					KontaktDto kontaktDto = DelegateFactory.getInstance()
							.getPartnerDelegate().kontaktFindByPrimaryKey(key);

					getInternalFrame().geheZu(
							InternalFramePartner.IDX_PANE_PARTNER,
							TabbedPanePartner.IDX_PANE_KONTAKT,
							kontaktDto.getPartnerIId(), kontaktDto.getIId(),
							null);

					((PanelQuery) eI.getSource()).setCursor(new Cursor(
							Cursor.DEFAULT_CURSOR));
					getInternalFrame().hide();
					getInternalFrame().show();
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelPartnerD) {
				// ... QP1 selektieren.
				panelPartnerQP.eventYouAreSelected(false);
				getPartnerDto()
						.setIId((Integer) panelPartnerQP.getSelectedId());
				getInternalFrame().setKeyWasForLockMe(
						panelPartnerQP.getSelectedId() + "");
				setSelectedComponent(panelPartnerQP);
			} else if (eI.getSource() == panelBankBottomD) {
				Integer iIdPartner = (Integer) panelPartnerQP.getSelectedId();
				refreshBankSP4(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				if (panelBankBottomD.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelBankTopQP
							.getId2SelectAfterDelete();
					panelBankTopQP.setSelectedId(oNaechster);
				}
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKommunikationBottomD) {
				Integer iIdPartner = (Integer) panelPartnerQP.getSelectedId();
				refreshKommunikationSP3(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				if (panelKommunikationBottomD.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelKommunikationTopQP
							.getId2SelectAfterDelete();
					panelKommunikationTopQP.setSelectedId(oNaechster);
				}
				panelKommunikationSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSelektionBottomD) {
				FLRPASelektionPK fLRPASelektionPK = (FLRPASelektionPK) panelSelektionTopQP
						.getSelectedId();
				if (fLRPASelektionPK != null) {
					refreshSelektionSP6(fLRPASelektionPK.getPartner_i_id());
					getInternalFrame().setKeyWasForLockMe(
							fLRPASelektionPK.toString());
				}
				panelSelektionSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKurzbriefBottomD) {
				setKeyWasForLockMe();
				if (panelKurzbriefBottomD.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelKurzbriefTopQP
							.getId2SelectAfterDelete();
					panelKurzbriefTopQP.setSelectedId(oNaechster);
				} else {
					Integer iIdPartner = (Integer) getPartnerDto().getIId();
					refreshKurzbriefSP9(iIdPartner);
				}
				panelKurzbriefSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAnsprechpartnerBottomD5) {
				Integer iIdPartner = getPartnerDto().getIId();

				refreshAnsprechpartnerSP5(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				panelAnsprechpartnerSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKontakt) {
				Integer iIdPartner = getPartnerDto().getIId();

				refreshKontakt(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				panelSplitKontakt.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelPartnerD) {
				panelPartnerD.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n panel
			} else if (eI.getSource() == panelKommunikationBottomD) {
				panelKommunikationSP.eventYouAreSelected(false); // refresh auf
				// das
				// gesamte
				// 1:n panel
			} else if (eI.getSource() == panelBankBottomD) {
				panelBankSP.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n panel
			} else if (eI.getSource() == panelSelektionBottomD) {
				panelSelektionSP.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n
				// panel
			} else if (eI.getSource() == panelKurzbriefBottomD) {
				panelKurzbriefSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAnsprechpartnerBottomD5) {
				panelAnsprechpartnerSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKontakt) {
				panelSplitKontakt.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelKommunikationBottomD) {
				Object oKey = panelKommunikationBottomD.getKeyWhenDetailPanel();
				panelKommunikationTopQP.eventYouAreSelected(false);
				panelKommunikationTopQP.setSelectedId(oKey);
				panelKommunikationSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBankBottomD) {
				Object oKey = panelBankBottomD.getKeyWhenDetailPanel();
				panelBankTopQP.eventYouAreSelected(false);
				panelBankTopQP.setSelectedId(oKey);
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAnsprechpartnerBottomD5) {
				Object oKey = panelAnsprechpartnerBottomD5
						.getKeyWhenDetailPanel();
				panelAnsprechpartnerTopQP5.eventYouAreSelected(false);
				panelAnsprechpartnerTopQP5.setSelectedId(oKey);
				panelAnsprechpartnerSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKontakt) {
				Object oKey = panelDetailKontakt.getKeyWhenDetailPanel();
				panelQueryKontakt.eventYouAreSelected(false);
				panelQueryKontakt.setSelectedId(oKey);
				panelSplitKontakt.eventYouAreSelected(false);
			}

			else if (eI.getSource() == panelSelektionBottomD) {
				Object oKey = panelSelektionBottomD.getKeyWhenDetailPanel();
				panelSelektionTopQP.eventYouAreSelected(false);
				panelSelektionTopQP.setSelectedId(oKey);
				panelSelektionSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelPartnerD) {
				// MB 04.05.06 IMS 1676
				panelPartnerQP.clearDirektFilter();
				if (panelPartnerQP.getSelectedId() == null || // wenns der erste
						// ist ... oder:
						(panelPartnerQP.getSelectedId() != null && !(panelPartnerQP
								.getSelectedId(). // wenn ein falscher
								// selektiert ist
								equals(panelPartnerD.getKeyWhenDetailPanel())))) {
					Object key = panelPartnerD.getKeyWhenDetailPanel();
					panelPartnerQP.eventYouAreSelected(false);
					panelPartnerQP.setSelectedId(key);
					panelPartnerQP.eventYouAreSelected(false);
				}
			} else if (eI.getSource() == panelKurzbriefBottomD) {
				Object oKey = panelKurzbriefBottomD.getKeyWhenDetailPanel();
				panelKurzbriefTopQP.eventYouAreSelected(false);
				panelKurzbriefTopQP.setSelectedId(oKey);
				panelKurzbriefSP.eventYouAreSelected(false);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			// Einer der Knoepfe zur Reihung der Positionen auf einem PanelQuery
			// wurde gedrueckt
			if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				int iPos = panelAnsprechpartnerTopQP5.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelAnsprechpartnerTopQP5
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelAnsprechpartnerTopQP5
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getAnsprechpartnerDelegate()
							.vertauscheAnsprechpartner(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelAnsprechpartnerTopQP5.setSelectedId(iIdPosition);
					panelAnsprechpartnerSP5.eventYouAreSelected(true);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				int iPos = panelAnsprechpartnerTopQP5.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelAnsprechpartnerTopQP5.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelAnsprechpartnerTopQP5
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelAnsprechpartnerTopQP5
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getAnsprechpartnerDelegate()
							.vertauscheAnsprechpartner(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelAnsprechpartnerTopQP5.setSelectedId(iIdPosition);
					panelAnsprechpartnerSP5.eventYouAreSelected(true);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				panelAnsprechpartnerBottomD5.eventActionNew(eI, true, false);
				panelAnsprechpartnerBottomD5.eventYouAreSelected(false); // Buttons
				// schalten
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			if (eI.getSource() instanceof PanelQuery) {
				PanelQuery pq = (PanelQuery) eI.getSource();
				if (pq.getAspect().equals(ACTION_SPECIAL_NEW_EMAIL)) {
					panelKurzbriefBottomD.eventActionNew(eI, true, false);
					panelKurzbriefBottomD.eventYouAreSelected(false);
					setSelectedComponent(panelKurzbriefSP);
					panelKurzbriefBottomD.beHtml();
					panelKurzbriefBottomD.beEditMode(true);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DROP) {
			myLogger.info("Drop Changed on " + eI.getSource());
			if (eI.getSource() == panelKurzbriefBottomD) {
				if (eI instanceof ItemChangedEventDrop) {
					ItemChangedEventDrop dropEvent = (ItemChangedEventDrop) eI;
					Integer kbId = DelegateFactory
							.getInstance()
							.getEmailMediaDelegate()
							.createKurzbriefFromEmail(getPartnerDto().getIId(),
									LocaleFac.BELEGART_PARTNER,
									(MediaEmailMetaDto) dropEvent.getDropData());
					panelKurzbriefTopQP.setSelectedId(kbId);
					panelKurzbriefSP.eventYouAreSelected(false);
				}
			}
		}
	}

	private void refreshPartnerD2(Integer key) throws Throwable {

		if (panelPartnerD == null) {
			panelPartnerD = new PanelPartnerDetail(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.detail"), key, this);
			setComponentAt(IDX_PANEL_D, panelPartnerD);
		}
	}

	private void refreshPartnerAnredenD5(Integer key) throws Throwable {

		if (panelPartnerAnredenD == null) {
			panelPartnerAnredenD = new PanelPartnerAnreden(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.detail"), key,
					this);
			setComponentAt(IDX_PANE_ANREDEN_D, panelPartnerAnredenD);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();
		getInternalFrame().setRechtModulweit(rechtModulweit);

		if (!LPMain.getInstance().isLPAdmin()) {
			MandantDto[] mandanten = DelegateFactory.getInstance()
					.getMandantDelegate().mandantFindAll();
			for (int i = 0; i < mandanten.length; i++) {
				if (getPartnerDto() != null && getPartnerDto().getIId() != null) {
					if (getPartnerDto().getIId().equals(
							mandanten[i].getPartnerIId())) {
						getInternalFrame().setRechtModulweit(
								RechteFac.RECHT_MODULWEIT_READ);
						break;
					}
				}
			}
		}

		if (selectedIndex == IDX_PANEL_QP) {
			refreshPartnerQP1();
			panelPartnerQP.eventYouAreSelected(false);

			// btnstate: 3 QP alleine; im QP die Buttons setzen.
			panelPartnerQP.updateButtons();
			getInternalFrame()
					.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");
		}

		else if (selectedIndex == IDX_PANEL_D) {
			Integer iIdPartner = getPartnerDto().getIId();
			refreshPartnerD2(iIdPartner);
			panelPartnerD.eventYouAreSelected(false);
		}

		else if (selectedIndex == IDX_PANE_KOMMUNIKATION_SP) {
			Integer iIdPartner = getPartnerDto().getIId();
			refreshKommunikationSP3(iIdPartner);
			panelKommunikationSP.eventYouAreSelected(false);
			// btnstate: 4a im QP die Buttons setzen.
			LockStateValue d = panelKommunikationBottomD
					.getLockedstateDetailMainKey();
			panelKommunikationTopQP.updateButtons(d);
		}

		else if (selectedIndex == IDX_PANE_BANK_SP) {
			// gehe zu SP4
			Integer iIdPartner = getPartnerDto().getIId();
			refreshBankSP4(iIdPartner);
			panelBankSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelBankBottomD.getLockedstateDetailMainKey();
			panelBankTopQP.updateButtons(d);
		}

		else if (selectedIndex == IDX_PANE_ANREDEN_D) {
			// gehe zu D5
			Integer iIdPartner = getPartnerDto().getIId();
			refreshPartnerAnredenD5(iIdPartner);
			panelPartnerAnredenD.eventYouAreSelected(false);
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getInstance().getTextRespectUISPr("lp.anreden"));
		}

		else if (selectedIndex == IDX_PANE_PARTNERSELEKTION_SP) {
			// gehe zu SP6
			Integer iIdPartner = getPartnerDto().getIId();
			refreshSelektionSP6(iIdPartner);
			panelSelektionSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelSelektionBottomD
					.getLockedstateDetailMainKey();
			panelSelektionTopQP.updateButtons(d);
		}

		else if (selectedIndex == IDX_PANE_KURZBRIEF_SP) {

			boolean hatRechtKurzbriefCUD = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PART_KURZBRIEF_CUD);
			if (hatRechtKurzbriefCUD == true) {
				getInternalFrame().setRechtModulweit(
						RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			// gehe zu SP9
			Integer iIdPartner = getPartnerDto().getIId();

			refreshKurzbriefSP9(iIdPartner);
			panelKurzbriefSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelKurzbriefBottomD
					.getLockedstateDetailMainKey();
			panelKurzbriefTopQP.updateButtons(d);

		} else if (selectedIndex == IDX_PANE_ANSPRECHPARTNER) {
			// gehe zu SP5
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getInstance().getTextRespectUISPr(
							"label.ansprechpartner"));
			Integer iIdPartner = getPartnerDto().getIId();
			refreshAnsprechpartnerSP5(iIdPartner);
			panelAnsprechpartnerSP5.eventYouAreSelected(false);
			panelAnsprechpartnerTopQP5
					.updateButtons(panelAnsprechpartnerBottomD5
							.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANE_KONTAKT) {
			// gehe zu SP5
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getInstance().getTextRespectUISPr("lp.kontakt"));
			Integer iIdPartner = getPartnerDto().getIId();
			refreshKontakt(iIdPartner);
			panelSplitKontakt.eventYouAreSelected(false);
			panelQueryKontakt.updateButtons(panelDetailKontakt
					.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANE_ANSPRECHPARTNER_VON) {
			refreshAnsprechpartnervonQP(getPartnerDto().getIId());
			panelAnsprechpartnerVonQP.eventYouAreSelected(false);

			// btnstate: 3 QP alleine; im QP die Buttons setzen.
			panelAnsprechpartnerVonQP.updateButtons();

		} else if (selectedIndex == IDX_PANE_REFERENZ) {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getPartnerDto().formatFixTitelName1Name2());
			refreshReferenzQP(getPartnerDto().getIId());
			panelReferenzQP.eventYouAreSelected(false);

			// btnstate: 3 QP alleine; im QP die Buttons setzen.
			panelReferenzQP.updateButtons();

		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_ADRESSETIKETT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"part.report.adressetikett");
			getInternalFrame().showReportKriterien(
					new ReportAdressetikett(getInternalFrame(),
							getPartnerDto(), add2Title));
		}
		if (e.getActionCommand().equals(MENUE_ACTION_STAMMBLATT)) {
			if (getPartnerDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportPartnerstammblatt(getInternalFrame(),
								getPartnerDto().getIId(), getPartnerDto()
										.formatFixTitelName1Name2()));
			}
		}
		if (e.getActionCommand().equals(MENUE_ACTION_GEBURTSTAGSLISTE)) {
			if (getPartnerDto() != null) {
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"part.report.geburtstagsliste");
				getInternalFrame().showReportKriterien(
						new ReportGeburtstagsliste(getInternalFrame(),
								add2Title));
			}
		}
		if (e.getActionCommand().equals(MENUE_ACTION_PARTNERZUSAMMENFUEHREN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"part.partnerzusammenfuehren");
			PanelDialogPartnerZusammenfuehren d = new PanelDialogPartnerZusammenfuehren(
					getPartnerDto(), getInternalFrame(), add2Title);
			getInternalFrame().showPanelDialog(d);
			d.setVisible(true);
		}
		if (e.getActionCommand().equals(MENUE_ACTION_WIEDERVORLAGE)) {
			// Dialog'Wiedervorlage erstellen
			((InternalFramePartner) getInternalFrame()).dialogWiedervorlage = new DialogWiedervorlage(
					getInternalFrame());

			/*
			 * LPMain .getInstance() .getDesktop()
			 * .platziereDialogRechtsObenImFenster( ((InternalFramePartner)
			 * getInternalFrame()).dialogWiedervorlage);
			 */
			((InternalFramePartner) getInternalFrame()).dialogWiedervorlage
					.setVisible(true);

		}

		if (e.getActionCommand().equals(MENUE_ACTION_CSVIMPORT)) {

			// Dateiauswahldialog
			// PJ 14984
			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_CSV, false);
			File f = null;
			if (files != null && files.length > 0) {
				f = files[0];
			}
			if (f != null) {
				LPCSVReader reader = new LPCSVReader(new FileReader(f), ';');

				String[] sLine;
				// Erste Zeile Auslassen (Ueberschrift)
				sLine = reader.readNext();
				// zeilenweise einlesen.
				sLine = reader.readNext();
				ArrayList<PartnerImportDto> alDaten = new ArrayList<PartnerImportDto>();

				do {

					PartnerImportDto dtoTemp = new PartnerImportDto();
					try {
						dtoTemp.setAnrede(sLine[0]);
						dtoTemp.setTitel(sLine[1]);

						dtoTemp.setName1(sLine[2]);
						dtoTemp.setName2(sLine[3]);
						dtoTemp.setName3(sLine[4]);
						dtoTemp.setStrasse(sLine[5]);
						dtoTemp.setLand(sLine[6]);
						dtoTemp.setPlz(sLine[7]);
						dtoTemp.setOrt(sLine[8]);
						dtoTemp.setTelefon(sLine[9]);
						dtoTemp.setFax(sLine[10]);
						dtoTemp.setEmail(sLine[11]);
						dtoTemp.setHomepage(sLine[12]);
						dtoTemp.setBemerkung(sLine[13]);
						dtoTemp.setSelektion(sLine[14]);
						dtoTemp.setAnsprechpartnerFunktion(sLine[15]);
						dtoTemp.setAnsprechpartnerAnrede(sLine[16]);
						dtoTemp.setAnsprechpartnerTitel(sLine[17]);
						dtoTemp.setAnsprechpartnerVorname(sLine[18]);
						dtoTemp.setAnsprechpartnerNachname(sLine[19]);
						dtoTemp.setAnsprechpartnerTelefonDW(sLine[20]);
						dtoTemp.setAnsprechpartnerFaxDW(sLine[21]);
						dtoTemp.setAnsprechpartnerEmail(sLine[22]);

						if (sLine.length > 23) {
							dtoTemp.setAnsprechpartnerMobil(sLine[23]);
						}

						if (sLine.length > 24 && sLine[24].length() == 10) {
							Calendar c = Calendar.getInstance();
							c.set(Calendar.YEAR,
									new Integer(sLine[24].substring(6, 10)));
							c.set(Calendar.MONTH,
									new Integer(sLine[24].substring(3, 5)));
							c.set(Calendar.DAY_OF_MONTH,
									new Integer(sLine[24].substring(0, 2)));
							c.set(Calendar.HOUR, 0);
							c.set(Calendar.MINUTE, 0);
							c.set(Calendar.SECOND, 0);
							c.set(Calendar.MILLISECOND, 0);

							dtoTemp.setAnsprechpartnerGueltigab(new java.sql.Timestamp(
									c.getTimeInMillis()));

						}
						if (sLine.length > 25) {
							dtoTemp.setAnsprechpartnerBemerkung(sLine[25]);
						}
						if (sLine.length > 26) {
							dtoTemp.setAnsprechpartnerDirektfax(sLine[26]);
						}

						if (sLine.length > 27) {
							dtoTemp.setPartnerklasse(sLine[27]);
						}
						if (sLine.length > 28) {
							dtoTemp.setBranche(sLine[28]);
						}
						if (sLine.length > 29) {
							dtoTemp.setIln(sLine[29]);
						}
						if (sLine.length > 30) {
							dtoTemp.setFilialnr(sLine[30]);
						}
						if (sLine.length > 31) {
							dtoTemp.setUid(sLine[31]);
						}
						if (sLine.length > 32) {
							dtoTemp.setGerichtsstand(sLine[32]);
						}
						if (sLine.length > 33) {
							dtoTemp.setFirmenbuchnummer(sLine[33]);
						}
						if (sLine.length > 34) {
							dtoTemp.setPostfach(sLine[34]);
						}
						if (sLine.length > 35 && sLine[35].length() == 10) {
							Calendar c = Calendar.getInstance();
							c.set(Calendar.YEAR,
									new Integer(sLine[35].substring(6, 10)));
							c.set(Calendar.MONTH,
									new Integer(sLine[35].substring(3, 5)));
							c.set(Calendar.DAY_OF_MONTH,
									new Integer(sLine[35].substring(0, 2)));
							c.set(Calendar.HOUR, 0);
							c.set(Calendar.MINUTE, 0);
							c.set(Calendar.SECOND, 0);
							c.set(Calendar.MILLISECOND, 0);

							dtoTemp.setGeburtsdatumansprechpartner(new java.sql.Timestamp(
									c.getTimeInMillis()));

						}
						if (sLine.length > 36) {
							dtoTemp.setGmtversatz(sLine[36]);
						}
						if (sLine.length > 37) {
							dtoTemp.setKontonummer(sLine[37]);
						}
						alDaten.add(dtoTemp);
					} catch (java.lang.ArrayIndexOutOfBoundsException e1) {
						// dann wurde der Rest mir Leer aufgefuellt
						alDaten.add(dtoTemp);
					}

					sLine = reader.readNext();
				} while (sLine != null);

				PartnerImportDto[] dtos = new PartnerImportDto[alDaten.size()];

				dtos = (PartnerImportDto[]) alDaten.toArray(dtos);

				DialogPartnerImport d = new DialogPartnerImport(dtos, this);
				d.setSize(500, 500);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

			}

		}

	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	private void refreshKommunikationSP3(Integer iIdPartnerI) throws Throwable {

		if (panelKommunikationTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKPartnerKommunikationPartner(iIdPartnerI);

			panelKommunikationTopQP = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_PARTNERKOMMUNIKATION,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.uebersicht.detail"), true);

			panelKommunikationBottomD = new PanelPartnerKommunikation(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kommunikation"), null,
					this);

			panelKommunikationSP = new PanelSplit(getInternalFrame(),
					panelKommunikationBottomD, panelKommunikationTopQP, 200);
			setComponentAt(IDX_PANE_KOMMUNIKATION_SP, panelKommunikationSP);
		} else {
			// filter refreshen.
			panelKommunikationTopQP.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKPartnerKommunikationPartner(
							iIdPartnerI));
		}
	}

	private void refreshSelektionSP6(Integer iIdPartnerI) throws Throwable {

		if (panelSelektionTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKPASelektion(iIdPartnerI);

			panelSelektionTopQP = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_PARTNERSELEKTION,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.uebersicht.detail"), true);

			panelSelektionBottomD = new PanelPASelektion(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
					null, this);

			panelSelektionSP = new PanelSplit(getInternalFrame(),
					panelSelektionBottomD, panelSelektionTopQP, 200);
			setComponentAt(IDX_PANE_PARTNERSELEKTION_SP, panelSelektionSP);
		} else {
			// filter refreshen.
			panelSelektionTopQP.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKPASelektion(iIdPartnerI));
		}
	}

	private void refreshKurzbriefSP9(Integer iIdPartnerI) throws Throwable {

		if (panelKurzbriefSP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKKurzbriefpartner(iIdPartnerI,
							LocaleFac.BELEGART_PARTNER);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT)) {
				aWhichStandardButtonIUse = new String[] {
						PanelBasis.ACTION_NEW, ACTION_SPECIAL_NEW_EMAIL };
			}

			panelKurzbriefTopQP = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_PARTNERKURZBRIEF,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr("lp.kurzbrief"),
					true);

			if (aWhichStandardButtonIUse.length > 1) {

				panelKurzbriefTopQP.createAndSaveAndShowButton(
						"/com/lp/client/res/documentHtml.png", LPMain
								.getInstance()
								.getTextRespectUISPr("lp.newHtml"),
						ACTION_SPECIAL_NEW_EMAIL, KeyStroke.getKeyStroke('N',
								InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK),
						null);
			}

			panelKurzbriefBottomD = new PanelPartnerKurzbrief(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kurzbrief"), null);

			panelKurzbriefSP = new DropPanelSplit(getInternalFrame(),
					panelKurzbriefBottomD, panelKurzbriefTopQP, 150);

			if (aWhichStandardButtonIUse.length > 1) {
				panelKurzbriefSP.beOneTouchExpandable();
			}

			setComponentAt(IDX_PANE_KURZBRIEF_SP, panelKurzbriefSP);
		} else {
			// filter refreshen.
			panelKurzbriefTopQP.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKKurzbriefpartner(iIdPartnerI,
							LocaleFac.BELEGART_PARTNER));
		}
	}

	private void refreshKontakt(Integer iIdPartnerI) throws Throwable {

		if (panelSplitKontakt == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKKontakt(iIdPartnerI);
			;

			panelQueryKontakt = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_KONTAKT, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kontakt"), true);

			panelDetailKontakt = new PanelPartnerKontakt(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kontakt"),
					null);

			panelSplitKontakt = new PanelSplit(getInternalFrame(),
					panelDetailKontakt, panelQueryKontakt, 150);

			setComponentAt(IDX_PANE_KONTAKT, panelSplitKontakt);
		} else {
			// filter refreshen.
			panelQueryKontakt.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKKontakt(iIdPartnerI));
		}
	}

	private void refreshBankSP4(Integer iIdPartnerI) throws Throwable {

		if (panelBankTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKPartnerbank(iIdPartnerI);

			panelBankTopQP = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_PARTNERBANK,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"finanz.bankverbindung"), true);

			panelBankBottomD = new PanelPartnerpartnerbank(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"finanz.bankverbindung"), null);

			panelBankSP = new PanelSplit(getInternalFrame(), panelBankBottomD,
					panelBankTopQP, 200);
			setComponentAt(IDX_PANE_BANK_SP, panelBankSP);
		} else {
			// filter refreshen.
			panelBankTopQP.setDefaultFilter(PartnerFilterFactory.getInstance()
					.createFKPartnerbank(iIdPartnerI));
		}
	}

	/**
	 * Diese Methode setzt des aktuellen Partner aus der Auswahlliste als den zu
	 * lockenden Partner.
	 */
	public void setKeyWasForLockMe() {
		Object oKey = panelPartnerQP.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
			JMenu menuDatei = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);
			menuDatei.add(new JSeparator(), 0);

			// CSV-Import
			JMenuItem menuItemPartnerImport = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("part.csvimport"));
			menuItemPartnerImport.addActionListener(this);
			menuItemPartnerImport.setActionCommand(MENUE_ACTION_CSVIMPORT);
			menuDatei.add(menuItemPartnerImport, 0);

			JMenu menuInfo = new WrapperMenu("lp.info", this);
			wrapperMenuBar.addJMenuItem(menuInfo);
			JMenuItem menuItemadressetikett = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"part.report.adressetikett"));
			menuItemadressetikett.addActionListener(this);
			menuItemadressetikett.setActionCommand(MENUE_ACTION_ADRESSETIKETT);
			menuInfo.add(menuItemadressetikett);

			// Partnerstammblatt
			JMenuItem menuItemStammblatt = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("lp.stammblatt"));
			menuItemStammblatt.addActionListener(this);
			menuItemStammblatt.setActionCommand(MENUE_ACTION_STAMMBLATT);
			menuInfo.add(menuItemStammblatt);

			boolean bDarfPartnerZusammenfuehren = false;
			try {
				bDarfPartnerZusammenfuehren = DelegateFactory
						.getInstance()
						.getTheJudgeDelegate()
						.hatRecht(
								RechteFac.RECHT_PART_PARTNER_ZUSAMMENFUEHREN_ERLAUBT);
			} catch (Throwable ex) {
				handleException(ex, true);
			}
			if (bDarfPartnerZusammenfuehren) {
				JMenuItem menuItemPartnerZusammenfuehren = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"part.partnerzusammenfuehren"));
				menuItemPartnerZusammenfuehren.addActionListener(this);
				menuItemPartnerZusammenfuehren
						.setActionCommand(MENUE_ACTION_PARTNERZUSAMMENFUEHREN);
				HelperClient.setToolTipTextMitRechtToComponent(
						menuItemPartnerZusammenfuehren,
						RechteFac.RECHT_PART_PARTNER_ZUSAMMENFUEHREN_ERLAUBT);
				menuDatei.add(menuItemPartnerZusammenfuehren, 0);
			}
			JMenu journal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_KONTAKTMANAGMENT)) {
				JMenuItem menuItemWiedervorlage = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr("lp.wiedervorlage"));
				menuItemWiedervorlage.addActionListener(this);
				menuItemWiedervorlage
						.setActionCommand(MENUE_ACTION_WIEDERVORLAGE);

				journal.add(menuItemWiedervorlage);
			}
			JMenuItem menuItemGeburtstagsliste = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"part.report.geburtstagsliste"));
			menuItemGeburtstagsliste.addActionListener(this);
			menuItemGeburtstagsliste
					.setActionCommand(MENUE_ACTION_GEBURTSTAGSLISTE);

			journal.add(menuItemGeburtstagsliste);

		}
		return wrapperMenuBar;
	}

	public PanelQuery getPanelPartnerQP1() {
		this.setSelectedIndex(IDX_PANEL_QP);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.panelPartnerQP;
	}

	/**
	 * @return the panelPartnerD2
	 */
	public PanelBasis getPanelPartnerD2() {
		this.setSelectedIndex(IDX_PANEL_D);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.panelPartnerD;
	}

	/**
	 * @return the getPanelBankSP4
	 */
	public PanelSplit getPanelBankSP4() {
		this.setSelectedIndex(IDX_PANE_BANK_SP);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.panelBankSP;
	}

	/**
	 * @return the panelKommunikationTopQP3
	 */
	public PanelBasis getPanelKommunikationTopQP3() {
		this.setSelectedIndex(IDX_PANE_KOMMUNIKATION_SP);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.panelKommunikationSP;
	}

	/**
	 * @return the panelPartnerAnredenD5
	 */
	public PanelPartnerAnreden getPanelPartnerAnredenD5() {
		this.setSelectedIndex(IDX_PANE_ANREDEN_D);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.panelPartnerAnredenD;
	}

	/**
	 * @return the panelSelektionTopQP6
	 */
	public PanelSplit getPanelSelektionTopQP6() {
		this.setSelectedIndex(IDX_PANE_PARTNERSELEKTION_SP);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.panelSelektionSP;
	}

	public PASelektionDto getPASelektionDto() {
		return pASelektionDto;
	}

	public void setPASelektionDto(PASelektionDto pASelektionDto) {
		this.pASelektionDto = pASelektionDto;
	}

	public PartnerbankDto getPartnerBankDto() {
		return partnerBankDto;
	}

	public void setPartnerBankDto(PartnerbankDto partnerBankDto) {
		this.partnerBankDto = partnerBankDto;
	}

	public BankDto getBankDto() {
		return bankDto;
	}

	public void setBankDto(BankDto bankDto) {
		this.bankDto = bankDto;
	}

	public void setPartnerkommunikationDto(
			PartnerkommunikationDto partnerkommunikationDto) {
		this.partnerkommunikationDto = partnerkommunikationDto;
	}

	public PartnerkommunikationDto getPartnerkommunikationsDto() {
		return partnerkommunikationDto;
	}

	public void gotoDetail() throws Throwable {
		refreshPartnerD2(getPartnerDto().getIId());
		// setSelectedComponent(panelLandPlzOrtSP3);
		getInternalFrame().getContentPane().validate();
	}

	public PartnerDto getDto() {
		return partnerDto;
	}

	public KurzbriefDto getKurzbriefDto() {
		return kurzbriefDto;
	}

	public void setKurzbriefDto(KurzbriefDto kurzbriefDto) {
		this.kurzbriefDto = kurzbriefDto;
	}

}
