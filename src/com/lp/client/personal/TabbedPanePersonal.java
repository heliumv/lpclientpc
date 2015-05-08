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
package com.lp.client.personal;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PanelPartnerKurzbrief;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class TabbedPanePersonal extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryPersonal = null;
	private PanelBasis panelDetailPersonal = null;
	private PanelBasis panelDetailDaten = null;

	private PanelQuery panelQueryAngehoerige = null;
	private PanelBasis panelBottomAngehoerige = null;
	private PanelSplit panelSplitAngehoerige = null;

	private PanelQuery panelQueryBankverbindung = null;
	private PanelBasis panelBottomBankverbindung = null;
	private PanelSplit panelSplitBankverbindung = null;

	private PanelQuery panelQueryEintrittaustritt = null;
	private PanelBasis panelBottomEintrittaustritt = null;
	private PanelSplit panelSplitEintrittaustritt = null;

	private PanelQuery panelQueryPersonalzeiten = null;
	private PanelBasis panelBottomPersonalzeiten = null;
	private PanelSplit panelSplitPersonalzeiten = null;

	private PanelQuery panelQueryPersonalzeitmodell = null;
	private PanelBasis panelBottomPersonalzeitmodell = null;
	private PanelSplit panelSplitPersonalzeitmodel = null;

	private PanelQuery panelQuerySchichtzeitmodell = null;
	private PanelBasis panelBottomSchichtzeitmodell = null;
	private PanelSplit panelSplitSchichtzeitmodel = null;

	private PanelQuery panelQueryPersonalzutrittsklasse = null;
	private PanelBasis panelBottomPersonalzutrittsklasse = null;
	private PanelSplit panelSplitPersonalzutrittsklasse = null;

	private PanelQuery panelQueryUrlaubsanspruch = null;
	private PanelBasis panelBottomUrlaubsanspruch = null;
	private PanelSplit panelSplitUrlaubsanspruch = null;

	private PanelQuery panelQueryGleitzeitsaldo = null;
	private PanelBasis panelBottomGleitzeitsaldo = null;
	private PanelSplit panelSplitGleitzeitsaldo = null;

	private PanelQuery panelQueryStundenabrechnung = null;
	private PanelBasis panelBottomStundenabrechnung = null;
	private PanelSplit panelSplitStundenabrechnung = null;

	private PanelQuery panelQueryPersonalgehalt = null;
	private PanelBasis panelBottomPersonalgehalt = null;
	private PanelSplit panelSplitPersonalgehalt = null;

	private PanelQuery panelQueryPersonalverfuegbarkeit = null;
	private PanelBasis panelBottomPersonalverfuegbarkeit = null;
	private PanelSplit panelSplitPersonalverfuegbarkeit = null;

	private PanelQuery panelQueryPersonalkurzbrief = null;
	private PanelBasis panelBottomPersonalkurzbrief = null;
	private PanelSplit panelSplitPersonalkurzbrief = null;

	private PanelQuery panelQueryPersonalfinger = null;
	private PanelBasis panelBottomPersonalfinger = null;
	private PanelSplit panelSplitPersonalfinger = null;

	private PanelDialogPersonalKommentar pdPersonalKommentar = null;

	private static final String MENUE_ACTION_PERSONALLISTE = "MENUE_ACTION_PERSONALLISTE";

	private static final String MENU_BEARBEITEN_KOMMENTAR = "MENU_BEARBEITEN_KOMMENTAR";

	private final static String EXTRA_SCHICHTZEIT = "EXTRA_SCHICHTZEIT";

	private int IDX_PANEL_AUSWAHL = -1;
	private int IDX_PANEL_DETAIL = -1;
	private int IDX_PANEL_DATEN = -1;
	private int IDX_PANEL_ANGEHOERIGE = -1;
	private int IDX_PANEL_BANKVERBINDUNG = -1;
	private int IDX_PANEL_EINTRITTAUSTRITT = -1;
	private int IDX_PANEL_ZEITEN = -1;
	private int IDX_PANEL_PERSONALZEITMODELL = -1;
	private int IDX_PANEL_SCHICHTZEITMODELL = -1;
	private int IDX_PANEL_PERSONALZUTRITTSKLASSE = -1;
	private int IDX_PANEL_URLAUBSANSPRUCH = -1;
	private int IDX_PANEL_GLEITZEITSALDO = -1;
	private int IDX_PANEL_STUNDENABRECHNUNG = -1;
	private int IDX_PANEL_PERSONALGEHALT = -1;
	private int IDX_PANEL_PERSONALVERFUEGBARKEIT = -1;
	private int IDX_PANEL_PERSONALKURZBRIEF = -1;
	private int IDX_PANEL_PERSONALFINGER = -1;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPanePersonal(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"menueentry.personal"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryPersonal() {
		return panelQueryPersonal;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryPersonal == null) {
			// MB 08.06.06 IMS 2173
			boolean bDarfAlleSehen = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE);
			String[] aWhichButtonIUse;
			// nur wenn er alle sehen darf, darf er auch neue anlegen.
			if (bDarfAlleSehen) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW,
						PanelBasis.ACTION_FILTER };
			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
			}
			panelQueryPersonal = new PanelQuery(PersonalFilterFactory
					.getInstance().createQTPersonal(),
					PersonalFilterFactory.getInstance()
							.createFKDPersonalauswahl(getInternalFrame()),
					QueryParameters.UC_ID_PERSONAL, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.title.panel.auswahl"),
					true);

			panelQueryPersonal
					.befuellePanelFilterkriterienDirektUndVersteckte(
							PersonalFilterFactory.getInstance()
									.createFKDPersonalname(),
							PersonalFilterFactory.getInstance()
									.createFKDPersonalnummer(),
							PersonalFilterFactory.getInstance()
									.createFKVPersonal());
			panelQueryPersonal.addDirektFilter(PersonalFilterFactory
					.getInstance().createFKDAusweis());
			panelQueryPersonal.eventYouAreSelected(false);
			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryPersonal);
			try {
				panelQueryPersonal.setSelectedId(LPMain.getInstance()
						.getTheClient().getIDPersonal());
			} catch (Throwable ex) {
				// nothing here
			}
		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailPersonal == null) {
			panelDetailPersonal = new PanelPersonal(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailPersonal);
		}
	}

	private void createDaten(Integer key) throws Throwable {
		if (panelDetailDaten == null) {
			panelDetailDaten = new PanelPersonaldaten(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.daten"), key);
			setComponentAt(IDX_PANEL_DATEN, panelDetailDaten);
		}
	}

	private void createKurzbrief(Integer iIdPartnerI) throws Throwable {

		if (panelSplitPersonalkurzbrief == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKKurzbriefpartner(iIdPartnerI,
							LocaleFac.BELEGART_PERSONAL);

			panelQueryPersonalkurzbrief = new PanelQuery(null, filters,
					QueryParameters.UC_ID_PARTNERKURZBRIEF,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr("lp.kurzbrief"),
					true);

			panelBottomPersonalkurzbrief = new PanelPartnerKurzbrief(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kurzbrief"), null);

			panelSplitPersonalkurzbrief = new PanelSplit(getInternalFrame(),
					panelBottomPersonalkurzbrief, panelQueryPersonalkurzbrief,
					150);

			setComponentAt(IDX_PANEL_PERSONALKURZBRIEF,
					panelSplitPersonalkurzbrief);
		} else {
			// filter refreshen.
			panelQueryPersonalkurzbrief.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKKurzbriefpartner(iIdPartnerI,
							LocaleFac.BELEGART_PERSONAL));
		}
	}

	private void createFinger(Integer iIdPartnerI) throws Throwable {

		if (panelSplitPersonalfinger == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PersonalFilterFactory.getInstance()
					.createFKPersonal(iIdPartnerI);

			panelQueryPersonalfinger = new PanelQuery(null, filters,
					QueryParameters.UC_ID_PERSONALFINGER,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.zutritt.finger"), true);

			panelBottomPersonalfinger = new PanelFinger(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.zutritt.finger"), null);

			panelSplitPersonalfinger = new PanelSplit(getInternalFrame(),
					panelBottomPersonalfinger, panelQueryPersonalfinger, 150);

			setComponentAt(IDX_PANEL_PERSONALFINGER, panelSplitPersonalfinger);
		} else {
			// filter refreshen.
			panelQueryPersonalfinger.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(iIdPartnerI));
		}
	}

	private void createAngehoerige(Integer key) throws Throwable {

		if (panelQueryAngehoerige == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryAngehoerige = new PanelQuery(null, PersonalFilterFactory
					.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_PERSONALANGEHOERIGE,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.title.tab.angehoerige"), true);
			panelBottomAngehoerige = new PanelPersonalangehoerige(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.title.tab.angehoerige"),
					null);

			panelSplitAngehoerige = new PanelSplit(getInternalFrame(),
					panelBottomAngehoerige, panelQueryAngehoerige, 300);

			setComponentAt(IDX_PANEL_ANGEHOERIGE, panelSplitAngehoerige);
		} else {
			// filter refreshen.
			panelQueryAngehoerige.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createEintrittAustritt(Integer key) throws Throwable {

		if (panelQueryEintrittaustritt == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryEintrittaustritt = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_EINTRITTAUSTRITT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.title.tab.eintrittaustritt"), true);
			panelBottomEintrittaustritt = new PanelEintrittaustritt(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.eintrittaustritt"), null);

			panelSplitEintrittaustritt = new PanelSplit(getInternalFrame(),
					panelBottomEintrittaustritt, panelQueryEintrittaustritt,
					370);

			setComponentAt(IDX_PANEL_EINTRITTAUSTRITT,
					panelSplitEintrittaustritt);
		} else {
			// filter refreshen.
			panelQueryEintrittaustritt.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createZeiten(Integer key) throws Throwable {

		if (panelQueryPersonalzeiten == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryPersonalzeiten = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_PERSONALZEITEN,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.title.tab.zeiten"), true);
			panelBottomPersonalzeiten = new PanelPersonalzeiten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.title.tab.zeiten"), null);

			panelSplitPersonalzeiten = new PanelSplit(getInternalFrame(),
					panelBottomPersonalzeiten, panelQueryPersonalzeiten, 370);

			setComponentAt(IDX_PANEL_ZEITEN, panelSplitPersonalzeiten);
		} else {
			// filter refreshen.
			panelQueryPersonalzeiten.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createPersonalzeitmodell(Integer key) throws Throwable {

		if (panelQueryPersonalzeitmodell == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryPersonalzeitmodell = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_PERSONALZEITMODELL,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.title.tab.personalzeitmodell"), true);
			panelBottomPersonalzeitmodell = new PanelPersonalzeitmodell(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.personalzeitmodell"), null);

			panelSplitPersonalzeitmodel = new PanelSplit(getInternalFrame(),
					panelBottomPersonalzeitmodell,
					panelQueryPersonalzeitmodell, 370);

			// Hier den zusaetzlichen Button aufs Panel bringen
			panelQueryPersonalzeitmodell.createAndSaveAndShowButton(
					"/com/lp/client/res/heavy_operation.png",
					LPMain.getInstance()
							.getTextRespectUISPr("pers.schichtzeit"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_SCHICHTZEIT, null);

			setComponentAt(IDX_PANEL_PERSONALZEITMODELL,
					panelSplitPersonalzeitmodel);
		} else {
			// filter refreshen.
			panelQueryPersonalzeitmodell.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createSchichtzeitmodell(Integer key) throws Throwable {

		if (panelQuerySchichtzeitmodell == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQuerySchichtzeitmodell = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_SCHICHTZEITMODELL,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.title.tab.schichtzeitmodell"), true);
			panelBottomSchichtzeitmodell = new PanelSchichtzeitmodell(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.schichtzeitmodell"), null);

			panelSplitSchichtzeitmodel = new PanelSplit(getInternalFrame(),
					panelBottomSchichtzeitmodell, panelQuerySchichtzeitmodell,
					370);

			setComponentAt(IDX_PANEL_SCHICHTZEITMODELL,
					panelSplitSchichtzeitmodel);
		} else {
			// filter refreshen.
			panelQuerySchichtzeitmodell.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createPersonalzutrittsklasse(Integer key) throws Throwable {

		if (panelQueryPersonalzutrittsklasse == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryPersonalzutrittsklasse = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_PERSONALZUTRITTSKLASSE,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.zutritt.zutrittsklasse"), true);
			panelBottomPersonalzutrittsklasse = new PanelPersonalzutrittsklasse(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.zutritt.zutrittsklasse"), null);

			panelSplitPersonalzutrittsklasse = new PanelSplit(
					getInternalFrame(), panelBottomPersonalzutrittsklasse,
					panelQueryPersonalzutrittsklasse, 370);

			setComponentAt(IDX_PANEL_PERSONALZUTRITTSKLASSE,
					panelSplitPersonalzutrittsklasse);
		} else {
			// filter refreshen.
			panelQueryPersonalzutrittsklasse
					.setDefaultFilter(PersonalFilterFactory.getInstance()
							.createFKPersonal(key));
		}
	}

	private void createUrlaubsanspruch(Integer key) throws Throwable {

		if (panelQueryUrlaubsanspruch == null) {

			panelQueryUrlaubsanspruch = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_URLAUBSANSPRUCH,
					new String[] { PanelBasis.ACTION_NEW }, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.urlaubsanspruch"), true);
			panelBottomUrlaubsanspruch = new PanelUrlaubsanspruch(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.urlaubsanspruch"), null);

			panelSplitUrlaubsanspruch = new PanelSplit(getInternalFrame(),
					panelBottomUrlaubsanspruch, panelQueryUrlaubsanspruch, 230);

			setComponentAt(IDX_PANEL_URLAUBSANSPRUCH, panelSplitUrlaubsanspruch);
		} else {
			// filter refreshen.
			panelQueryUrlaubsanspruch.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createGleitzeitsaldo(Integer key) throws Throwable {

		if (panelQueryGleitzeitsaldo == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryGleitzeitsaldo = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_GLEITZEITSALDO,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.title.tab.gleitzeitsaldo"), true);
			panelBottomGleitzeitsaldo = new PanelGleitzeitsaldo(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.gleitzeitsaldo"), null);

			panelSplitGleitzeitsaldo = new PanelSplit(getInternalFrame(),
					panelBottomGleitzeitsaldo, panelQueryGleitzeitsaldo, 180);

			setComponentAt(IDX_PANEL_GLEITZEITSALDO, panelSplitGleitzeitsaldo);
		} else {
			// filter refreshen.
			panelQueryGleitzeitsaldo.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createBankverbindung(Integer iIdPartnerI) throws Throwable {

		if (panelQueryBankverbindung == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKPartnerbank(iIdPartnerI);

			panelQueryBankverbindung = new PanelQuery(null, filters,
					QueryParameters.UC_ID_PARTNERBANK,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"finanz.bankverbindung"), true);

			panelBottomBankverbindung = new PanelPersonalpartnerbank(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("finanz.bankverbindung"), null);

			panelSplitBankverbindung = new PanelSplit(getInternalFrame(),
					panelBottomBankverbindung, panelQueryBankverbindung, 200);
			setComponentAt(IDX_PANEL_BANKVERBINDUNG, panelSplitBankverbindung);
		} else {
			// filter refreshen.
			panelQueryBankverbindung.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKPartnerbank(iIdPartnerI));
		}
	}

	private void createStundenabrechnung(Integer key) throws Throwable {

		if (panelQueryStundenabrechnung == null) {
			panelQueryStundenabrechnung = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_STUNDENABRECHNUNG,
					new String[] { PanelBasis.ACTION_NEW }, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.stundenabrechnung"), true);
			panelBottomStundenabrechnung = new PanelStundenabrechnung(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.stundenabrechnung"), null);

			panelSplitStundenabrechnung = new PanelSplit(getInternalFrame(),
					panelBottomStundenabrechnung, panelQueryStundenabrechnung,
					150);

			setComponentAt(IDX_PANEL_STUNDENABRECHNUNG,
					panelSplitStundenabrechnung);
		} else {
			// filter refreshen.
			panelQueryStundenabrechnung.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createPersonalgehalt(Integer key) throws Throwable {

		if (panelQueryPersonalgehalt == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryPersonalgehalt = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_PERSONALGEHALT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.title.tab.personalgehalt"), true);
			panelBottomPersonalgehalt = new PanelPersonalgehalt(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.personalgehalt"), null);

			panelSplitPersonalgehalt = new PanelSplit(getInternalFrame(),
					panelBottomPersonalgehalt, panelQueryPersonalgehalt, 130);
			panelBottomPersonalgehalt.setMinimumSize(new Dimension(100, 350));


			setComponentAt(IDX_PANEL_PERSONALGEHALT, panelSplitPersonalgehalt);
		} else {
			// filter refreshen.
			panelQueryPersonalgehalt.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPersonal(key));
		}
	}

	private void createPersonalverfuegbarkeit(Integer key) throws Throwable {

		if (panelQueryPersonalverfuegbarkeit == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryPersonalverfuegbarkeit = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_PERSONALVERFUEGBARKEIT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"pers.personalgehalt.verfuegbarkeit"), true);
			panelBottomPersonalverfuegbarkeit = new PanelPersonalverfuegbarkeit(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.personalgehalt.verfuegbarkeit"), null);

			panelSplitPersonalverfuegbarkeit = new PanelSplit(
					getInternalFrame(), panelBottomPersonalverfuegbarkeit,
					panelQueryPersonalverfuegbarkeit, 320);

			setComponentAt(IDX_PANEL_PERSONALVERFUEGBARKEIT,
					panelSplitPersonalverfuegbarkeit);
		} else {
			// filter refreshen.
			panelQueryPersonalverfuegbarkeit
					.setDefaultFilter(PersonalFilterFactory.getInstance()
							.createFKPersonal(key));
		}
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {

		int tabIndex = 0;
		IDX_PANEL_AUSWAHL = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		tabIndex++;
		IDX_PANEL_DETAIL = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_DETAIL);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_DARF_PERSONALDETAILDATEN_SEHEN)) {

			tabIndex++;
			IDX_PANEL_DATEN = tabIndex;
			insertTab(LPMain.getInstance().getTextRespectUISPr("lp.daten"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.daten"),
					IDX_PANEL_DATEN);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PERSONAL)) {
				tabIndex++;
				IDX_PANEL_ANGEHOERIGE = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.angehoerige"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.angehoerige"),
						IDX_PANEL_ANGEHOERIGE);

				tabIndex++;
				IDX_PANEL_BANKVERBINDUNG = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"finanz.bankverbindung"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"finanz.bankverbindung"),
						IDX_PANEL_BANKVERBINDUNG);

				tabIndex++;
				IDX_PANEL_EINTRITTAUSTRITT = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.eintrittaustritt"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.eintrittaustritt"),
						IDX_PANEL_EINTRITTAUSTRITT);

				tabIndex++;
				IDX_PANEL_ZEITEN = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.zeiten"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.zeiten"), IDX_PANEL_ZEITEN);

				tabIndex++;
				IDX_PANEL_PERSONALZEITMODELL = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.personalzeitmodell"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.personalzeitmodell"),
						IDX_PANEL_PERSONALZEITMODELL);
				tabIndex++;
				IDX_PANEL_SCHICHTZEITMODELL = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.schichtzeitmodell"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.schichtzeitmodell"),
						IDX_PANEL_SCHICHTZEITMODELL);

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufModulZugreifen(
								LocaleFac.BELEGART_ZUTRITT)) {
					tabIndex++;
					IDX_PANEL_PERSONALZUTRITTSKLASSE = tabIndex;
					insertTab(
							LPMain.getInstance().getTextRespectUISPr(
									"pers.zutritt.zutrittsklasse"),
							null,
							null,
							LPMain.getInstance().getTextRespectUISPr(
									"pers.zutritt.zutrittsklasse"),
							IDX_PANEL_PERSONALZUTRITTSKLASSE);
				}

				tabIndex++;
				IDX_PANEL_URLAUBSANSPRUCH = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.urlaubsanspruch"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.urlaubsanspruch"),
						IDX_PANEL_URLAUBSANSPRUCH);

				tabIndex++;
				IDX_PANEL_GLEITZEITSALDO = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.gleitzeitsaldo"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.gleitzeitsaldo"),
						IDX_PANEL_GLEITZEITSALDO);

				tabIndex++;
				IDX_PANEL_STUNDENABRECHNUNG = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.stundenabrechnung"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.stundenabrechnung"),
						IDX_PANEL_STUNDENABRECHNUNG);

				tabIndex++;
				IDX_PANEL_PERSONALGEHALT = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.personalgehalt"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.title.tab.personalgehalt"),
						IDX_PANEL_PERSONALGEHALT);

				tabIndex++;
				IDX_PANEL_PERSONALVERFUEGBARKEIT = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"pers.personalgehalt.verfuegbarkeit"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.personalgehalt.verfuegbarkeit"),
						IDX_PANEL_PERSONALVERFUEGBARKEIT);

				tabIndex++;
				IDX_PANEL_PERSONALKURZBRIEF = tabIndex;
				insertTab(
						LPMain.getInstance()
								.getTextRespectUISPr("lp.kurzbrief"), null,
						null,
						LPMain.getInstance()
								.getTextRespectUISPr("lp.kurzbrief"),
						IDX_PANEL_PERSONALKURZBRIEF);

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_FINGERPRINT)) {
					tabIndex++;
					IDX_PANEL_PERSONALFINGER = tabIndex;
					insertTab(
							LPMain.getInstance().getTextRespectUISPr(
									"pers.zutritt.finger"),
							null,
							null,
							LPMain.getInstance().getTextRespectUISPr(
									"pers.zutritt.finger"),
							IDX_PANEL_PERSONALFINGER);

				}
			}
		}
		createAuswahl();

		if ((Integer) panelQueryPersonal.getSelectedId() != null) {
			getInternalFramePersonal().setPersonalDto(
					DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(
									(Integer) panelQueryPersonal
											.getSelectedId()));
		}

		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelQueryPersonal.getSelectedId() == null) {
			getInternalFrame().enableAllPanelsExcept(false);
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryPersonal,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryPersonal.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void refreshTitle() {

		getInternalFrame()
				.setLpTitle(
						InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
						LPMain.getInstance().getTextRespectUISPr(
								"menueentry.personal"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFramePersonal().getPersonalDto() != null) {
			String sNachname = getInternalFramePersonal().getPersonalDto()
					.getPartnerDto().getCName1nachnamefirmazeile1();
			if (sNachname == null) {
				sNachname = "";
			}
			String sVorname = getInternalFramePersonal().getPersonalDto()
					.getPartnerDto().getCName2vornamefirmazeile2();
			if (sVorname == null) {
				sVorname = "";
			}
			if (getInternalFramePersonal().getPersonalDto().getCPersonalnr() != null) {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFramePersonal().getPersonalDto()
								.getCPersonalnr()
								+ ", "
								+ sVorname
								+ " "
								+ sNachname);
			} else {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE, "");
			}
		}
	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENUE_ACTION_PERSONALLISTE)) {
			String add2Title = "Personalliste";
			getInternalFrame().showReportKriterien(
					new ReportPersonalliste(getInternalFramePersonal(),
							add2Title));

		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_KOMMENTAR)) {
			if (pruefeAktuellesPersonal()) {

				if (!getPersonalDetail().isLockedDlg()) {
					refreshPdKommentar();
					getInternalFrame().showPanelDialog(pdPersonalKommentar);

				}
			}
		}
	}

	private void refreshPdKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdPersonalKommentar = new PanelDialogPersonalKommentar(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.kommentar"), true);
	}

	private PanelBasis getPersonalDetail() throws Throwable {
		Integer iIdPersonal = null;

		if (panelDetailPersonal == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdPersonal = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			panelDetailPersonal = new PanelPersonal(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.detail"),
					iIdPersonal); // empty bei leerer angebotsliste

			setComponentAt(IDX_PANEL_DETAIL, panelDetailPersonal);
		}

		return panelDetailPersonal;
	}

	public boolean pruefeAktuellesPersonal() {
		boolean bIstGueltig = true;

		if (getInternalFramePersonal().getPersonalDto() == null
				|| getInternalFramePersonal().getPersonalDto().getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("pers.warning.keinpersonal"));
		}

		return bIstGueltig;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryPersonal) {
				Integer iId = (Integer) panelQueryPersonal.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailPersonal);
					panelDetailPersonal.eventYouAreSelected(false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailPersonal) {
				panelQueryPersonal.clearDirektFilter();
				Object oKey = panelDetailPersonal.getKeyWhenDetailPanel();

				panelQueryPersonal.setSelectedId(oKey);
			} else if (e.getSource() == panelBottomAngehoerige) {
				Object oKey = panelBottomAngehoerige.getKeyWhenDetailPanel();
				panelQueryAngehoerige.eventYouAreSelected(false);
				panelQueryAngehoerige.setSelectedId(oKey);
				panelSplitAngehoerige.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomBankverbindung) {
				Object oKey = panelBottomBankverbindung.getKeyWhenDetailPanel();
				panelQueryBankverbindung.eventYouAreSelected(false);
				panelQueryBankverbindung.setSelectedId(oKey);
				panelSplitBankverbindung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzeiten) {
				Object oKey = panelBottomPersonalzeiten.getKeyWhenDetailPanel();
				panelQueryPersonalzeiten.eventYouAreSelected(false);
				panelQueryPersonalzeiten.setSelectedId(oKey);
				panelSplitPersonalzeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomEintrittaustritt) {
				Object oKey = panelBottomEintrittaustritt
						.getKeyWhenDetailPanel();
				panelQueryEintrittaustritt.eventYouAreSelected(false);
				panelQueryEintrittaustritt.setSelectedId(oKey);
				panelSplitEintrittaustritt.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzeitmodell) {
				Object oKey = panelBottomPersonalzeitmodell
						.getKeyWhenDetailPanel();
				panelQueryPersonalzeitmodell.eventYouAreSelected(false);
				panelQueryPersonalzeitmodell.setSelectedId(oKey);
				panelSplitPersonalzeitmodel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchichtzeitmodell) {
				Object oKey = panelBottomSchichtzeitmodell
						.getKeyWhenDetailPanel();
				panelQuerySchichtzeitmodell.eventYouAreSelected(false);
				panelQuerySchichtzeitmodell.setSelectedId(oKey);
				panelSplitSchichtzeitmodel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzutrittsklasse) {
				Object oKey = panelBottomPersonalzutrittsklasse
						.getKeyWhenDetailPanel();
				panelQueryPersonalzutrittsklasse.eventYouAreSelected(false);
				panelQueryPersonalzutrittsklasse.setSelectedId(oKey);
				panelSplitPersonalzutrittsklasse.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomUrlaubsanspruch) {
				Object oKey = panelBottomUrlaubsanspruch
						.getKeyWhenDetailPanel();
				panelQueryUrlaubsanspruch.eventYouAreSelected(false);
				panelQueryUrlaubsanspruch.setSelectedId(oKey);
				panelSplitUrlaubsanspruch.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGleitzeitsaldo) {
				Object oKey = panelBottomGleitzeitsaldo.getKeyWhenDetailPanel();
				panelQueryGleitzeitsaldo.eventYouAreSelected(false);
				panelQueryGleitzeitsaldo.setSelectedId(oKey);
				panelSplitGleitzeitsaldo.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStundenabrechnung) {
				Object oKey = panelBottomStundenabrechnung
						.getKeyWhenDetailPanel();
				panelQueryStundenabrechnung.eventYouAreSelected(false);
				panelQueryStundenabrechnung.setSelectedId(oKey);
				panelSplitStundenabrechnung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalgehalt) {
				Object oKey = panelBottomPersonalgehalt.getKeyWhenDetailPanel();
				panelQueryPersonalgehalt.eventYouAreSelected(false);
				panelQueryPersonalgehalt.setSelectedId(oKey);
				panelSplitPersonalgehalt.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalverfuegbarkeit) {
				Object oKey = panelBottomPersonalverfuegbarkeit
						.getKeyWhenDetailPanel();
				panelQueryPersonalverfuegbarkeit.eventYouAreSelected(false);
				panelQueryPersonalverfuegbarkeit.setSelectedId(oKey);
				panelSplitPersonalverfuegbarkeit.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalkurzbrief) {
				Object oKey = panelBottomPersonalkurzbrief
						.getKeyWhenDetailPanel();
				panelQueryPersonalkurzbrief.eventYouAreSelected(false);
				panelQueryPersonalkurzbrief.setSelectedId(oKey);
				panelSplitPersonalkurzbrief.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalfinger) {
				Object oKey = panelBottomPersonalfinger.getKeyWhenDetailPanel();
				panelQueryPersonalfinger.eventYouAreSelected(false);
				panelQueryPersonalfinger.setSelectedId(oKey);
				panelSplitPersonalfinger.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryPersonal) {
				if (panelQueryPersonal.getSelectedId() != null) {
					getInternalFramePersonal().setKeyWasForLockMe(
							panelQueryPersonal.getSelectedId() + "");
					createDetail((Integer) panelQueryPersonal.getSelectedId());
					panelDetailPersonal
							.setKeyWhenDetailPanel(panelQueryPersonal
									.getSelectedId());

					getInternalFramePersonal().setPersonalDto(
							DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.personalFindByPrimaryKey(
											(Integer) panelQueryPersonal
													.getSelectedId()));
					String sNachname = getInternalFramePersonal()
							.getPersonalDto().getPartnerDto()
							.getCName1nachnamefirmazeile1();
					if (sNachname == null) {
						sNachname = "";
					}
					String sVorname = getInternalFramePersonal()
							.getPersonalDto().getPartnerDto()
							.getCName2vornamefirmazeile2();
					if (sVorname == null) {
						sVorname = "";
					}
					if (getInternalFramePersonal().getPersonalDto() != null) {
						getInternalFrame().setLpTitle(
								InternalFrame.TITLE_IDX_AS_I_LIKE,
								getInternalFramePersonal().getPersonalDto()
										.getCPersonalnr()
										+ ", "
										+ sVorname
										+ " " + sNachname);
					}

					getInternalFrame()
							.enableAllOberePanelsExceptMe(
									this,
									IDX_PANEL_AUSWAHL,
									((ISourceEvent) e.getSource())
											.getIdSelected() != null);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

			} else if (e.getSource() == panelQueryAngehoerige) {
				Integer iId = (Integer) panelQueryAngehoerige.getSelectedId();
				panelBottomAngehoerige.setKeyWhenDetailPanel(iId);
				panelBottomAngehoerige.eventYouAreSelected(false);
				panelQueryAngehoerige.updateButtons();
			} else if (e.getSource() == panelQueryBankverbindung) {
				Integer iId = (Integer) panelQueryBankverbindung
						.getSelectedId();
				panelBottomBankverbindung.setKeyWhenDetailPanel(iId);
				panelBottomBankverbindung.eventYouAreSelected(false);
				panelQueryBankverbindung.updateButtons();
			} else if (e.getSource() == panelQueryEintrittaustritt) {
				Integer iId = (Integer) panelQueryEintrittaustritt
						.getSelectedId();
				panelBottomEintrittaustritt.setKeyWhenDetailPanel(iId);
				panelBottomEintrittaustritt.eventYouAreSelected(false);
				panelQueryEintrittaustritt.updateButtons();
			} else if (e.getSource() == panelQueryPersonalzeiten) {
				Integer iId = (Integer) panelQueryPersonalzeiten
						.getSelectedId();
				panelBottomPersonalzeiten.setKeyWhenDetailPanel(iId);
				panelBottomPersonalzeiten.eventYouAreSelected(false);
				panelQueryPersonalzeiten.updateButtons();
			} else if (e.getSource() == panelQueryPersonalzeitmodell) {
				Integer iId = (Integer) panelQueryPersonalzeitmodell
						.getSelectedId();
				panelBottomPersonalzeitmodell.setKeyWhenDetailPanel(iId);
				panelBottomPersonalzeitmodell.eventYouAreSelected(false);
				panelQueryPersonalzeitmodell.updateButtons();
			} else if (e.getSource() == panelQuerySchichtzeitmodell) {
				Integer iId = (Integer) panelQuerySchichtzeitmodell
						.getSelectedId();
				panelBottomSchichtzeitmodell.setKeyWhenDetailPanel(iId);
				panelBottomSchichtzeitmodell.eventYouAreSelected(false);
				panelQuerySchichtzeitmodell.updateButtons();
			} else if (e.getSource() == panelQueryPersonalzutrittsklasse) {
				Integer iId = (Integer) panelQueryPersonalzutrittsklasse
						.getSelectedId();
				panelBottomPersonalzutrittsklasse.setKeyWhenDetailPanel(iId);
				panelBottomPersonalzutrittsklasse.eventYouAreSelected(false);
				panelQueryPersonalzutrittsklasse.updateButtons();
			} else if (e.getSource() == panelQueryUrlaubsanspruch) {
				Integer iId = (Integer) panelQueryUrlaubsanspruch
						.getSelectedId();
				panelBottomUrlaubsanspruch.setKeyWhenDetailPanel(iId);
				panelBottomUrlaubsanspruch.eventYouAreSelected(false);
				panelQueryUrlaubsanspruch.updateButtons();
			} else if (e.getSource() == panelQueryGleitzeitsaldo) {
				Integer iId = (Integer) panelQueryGleitzeitsaldo
						.getSelectedId();
				panelBottomGleitzeitsaldo.setKeyWhenDetailPanel(iId);
				panelBottomGleitzeitsaldo.eventYouAreSelected(false);
				panelQueryGleitzeitsaldo.updateButtons();
			} else if (e.getSource() == panelQueryStundenabrechnung) {
				Integer iId = (Integer) panelQueryStundenabrechnung
						.getSelectedId();
				panelBottomStundenabrechnung.setKeyWhenDetailPanel(iId);
				panelBottomStundenabrechnung.eventYouAreSelected(false);
				panelQueryStundenabrechnung.updateButtons();
			} else if (e.getSource() == panelQueryPersonalgehalt) {
				Integer iId = (Integer) panelQueryPersonalgehalt
						.getSelectedId();
				panelBottomPersonalgehalt.setKeyWhenDetailPanel(iId);
				panelBottomPersonalgehalt.eventYouAreSelected(false);
				panelQueryPersonalgehalt.updateButtons();
			} else if (e.getSource() == panelQueryPersonalverfuegbarkeit) {
				Integer iId = (Integer) panelQueryPersonalverfuegbarkeit
						.getSelectedId();
				panelBottomPersonalverfuegbarkeit.setKeyWhenDetailPanel(iId);
				panelBottomPersonalverfuegbarkeit.eventYouAreSelected(false);
				panelQueryPersonalverfuegbarkeit.updateButtons();
			} else if (e.getSource() == panelQueryPersonalkurzbrief) {
				Integer iId = (Integer) panelQueryPersonalkurzbrief
						.getSelectedId();
				panelBottomPersonalkurzbrief.setKeyWhenDetailPanel(iId);
				panelBottomPersonalkurzbrief.eventYouAreSelected(false);
				panelQueryPersonalkurzbrief.updateButtons();
			} else if (e.getSource() == panelQueryPersonalfinger) {
				Integer iId = (Integer) panelQueryPersonalfinger
						.getSelectedId();
				panelBottomPersonalfinger.setKeyWhenDetailPanel(iId);
				panelBottomPersonalfinger.eventYouAreSelected(false);
				panelQueryPersonalfinger.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomAngehoerige) {
				panelQueryAngehoerige.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomEintrittaustritt) {
				panelQueryEintrittaustritt.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomGleitzeitsaldo) {
				panelQueryGleitzeitsaldo.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomBankverbindung) {
				panelQueryBankverbindung.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomPersonalgehalt) {
				panelQueryPersonalgehalt.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomPersonalverfuegbarkeit) {
				panelQueryPersonalverfuegbarkeit
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomPersonalzeiten) {
				panelQueryPersonalzeiten.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomPersonalzeitmodell) {
				panelQueryPersonalzeitmodell.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomSchichtzeitmodell) {
				panelQuerySchichtzeitmodell.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomPersonalzutrittsklasse) {
				panelQueryPersonalzutrittsklasse
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomStundenabrechnung) {
				panelQueryStundenabrechnung.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomUrlaubsanspruch) {
				panelQueryUrlaubsanspruch.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomPersonalkurzbrief) {
				panelQueryPersonalkurzbrief.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomPersonalfinger) {
				panelQueryPersonalfinger.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailPersonal) {
				panelQueryPersonal.eventYouAreSelected(false);
				getInternalFramePersonal().getPersonalDto().setIId(
						(Integer) panelQueryPersonal.getSelectedId());
				getInternalFrame().setKeyWasForLockMe(
						panelQueryPersonal.getSelectedId() + "");
				this.setSelectedComponent(panelQueryPersonal);
			} else if (e.getSource() == panelBottomAngehoerige) {
				setKeyWasForLockMe();
				if (panelBottomAngehoerige.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAngehoerige
							.getId2SelectAfterDelete();
					panelQueryAngehoerige.setSelectedId(oNaechster);
				}

				panelSplitAngehoerige.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomBankverbindung) {
				setKeyWasForLockMe();
				if (panelBottomBankverbindung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBankverbindung
							.getId2SelectAfterDelete();
					panelQueryBankverbindung.setSelectedId(oNaechster);
				}

				panelSplitBankverbindung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomEintrittaustritt) {
				setKeyWasForLockMe();
				if (panelBottomEintrittaustritt.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryEintrittaustritt
							.getId2SelectAfterDelete();
					panelQueryEintrittaustritt.setSelectedId(oNaechster);
				}

				panelSplitEintrittaustritt.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzeiten) {
				setKeyWasForLockMe();
				if (panelBottomPersonalzeiten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPersonalzeiten
							.getId2SelectAfterDelete();
					panelQueryPersonalzeiten.setSelectedId(oNaechster);
				}

				panelSplitPersonalzeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzeitmodell) {
				setKeyWasForLockMe();
				if (panelBottomPersonalzeitmodell.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPersonalzeitmodell
							.getId2SelectAfterDelete();
					panelQueryPersonalzeitmodell.setSelectedId(oNaechster);
				}

				panelSplitPersonalzeitmodel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchichtzeitmodell) {
				setKeyWasForLockMe();
				if (panelBottomSchichtzeitmodell.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySchichtzeitmodell
							.getId2SelectAfterDelete();
					panelQuerySchichtzeitmodell.setSelectedId(oNaechster);
				}

				panelSplitSchichtzeitmodel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzutrittsklasse) {
				setKeyWasForLockMe();
				if (panelBottomPersonalzutrittsklasse.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPersonalzutrittsklasse
							.getId2SelectAfterDelete();
					panelQueryPersonalzutrittsklasse.setSelectedId(oNaechster);
				}

				panelSplitPersonalzutrittsklasse.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomUrlaubsanspruch) {
				setKeyWasForLockMe();
				if (panelBottomUrlaubsanspruch.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryUrlaubsanspruch
							.getId2SelectAfterDelete();
					panelQueryUrlaubsanspruch.setSelectedId(oNaechster);
				}

				panelSplitUrlaubsanspruch.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGleitzeitsaldo) {
				setKeyWasForLockMe();
				if (panelBottomGleitzeitsaldo.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryGleitzeitsaldo
							.getId2SelectAfterDelete();
					panelQueryGleitzeitsaldo.setSelectedId(oNaechster);
				}

				panelSplitGleitzeitsaldo.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStundenabrechnung) {
				setKeyWasForLockMe();
				if (panelBottomStundenabrechnung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryStundenabrechnung
							.getId2SelectAfterDelete();
					panelQueryStundenabrechnung.setSelectedId(oNaechster);
				}

				panelSplitStundenabrechnung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalgehalt) {
				setKeyWasForLockMe();
				if (panelBottomPersonalgehalt.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPersonalgehalt
							.getId2SelectAfterDelete();
					panelQueryPersonalgehalt.setSelectedId(oNaechster);
				}

				panelSplitPersonalgehalt.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalverfuegbarkeit) {
				setKeyWasForLockMe();
				if (panelBottomPersonalverfuegbarkeit.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPersonalverfuegbarkeit
							.getId2SelectAfterDelete();
					panelQueryPersonalverfuegbarkeit.setSelectedId(oNaechster);
				}

				panelSplitPersonalverfuegbarkeit.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalkurzbrief) {
				setKeyWasForLockMe();
				if (panelBottomPersonalkurzbrief.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPersonalkurzbrief
							.getId2SelectAfterDelete();
					panelQueryPersonalkurzbrief.setSelectedId(oNaechster);
				}

				panelSplitPersonalkurzbrief.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalfinger) {
				setKeyWasForLockMe();
				if (panelBottomPersonalfinger.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPersonalfinger
							.getId2SelectAfterDelete();
					panelQueryPersonalfinger.setSelectedId(oNaechster);
				}

				panelSplitPersonalfinger.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomAngehoerige) {
				panelSplitAngehoerige.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomBankverbindung) {
				panelSplitBankverbindung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzeiten) {
				panelSplitPersonalzeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomEintrittaustritt) {
				panelSplitEintrittaustritt.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzeitmodell) {
				panelSplitPersonalzeitmodel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchichtzeitmodell) {
				panelSplitSchichtzeitmodel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalzutrittsklasse) {
				panelSplitPersonalzutrittsklasse.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomUrlaubsanspruch) {
				panelSplitUrlaubsanspruch.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGleitzeitsaldo) {
				panelSplitGleitzeitsaldo.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStundenabrechnung) {
				panelSplitStundenabrechnung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalgehalt) {
				panelSplitPersonalgehalt.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalverfuegbarkeit) {
				panelSplitPersonalverfuegbarkeit.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalkurzbrief) {
				panelSplitPersonalkurzbrief.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPersonalfinger) {
				panelSplitPersonalfinger.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryPersonal) {

				createDetail(null);
				panelDetailPersonal.eventActionNew(e, true, false);
				this.setSelectedComponent(panelDetailPersonal);

			} else if (e.getSource() == panelQueryAngehoerige) {

				panelBottomAngehoerige.eventActionNew(e, true, false);
				panelBottomAngehoerige.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitAngehoerige);

			} else if (e.getSource() == panelQueryBankverbindung) {

				panelBottomBankverbindung.eventActionNew(e, true, false);
				panelBottomBankverbindung.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitBankverbindung);

			} else if (e.getSource() == panelQueryEintrittaustritt) {

				panelBottomEintrittaustritt.eventActionNew(e, true, false);
				panelBottomEintrittaustritt.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitEintrittaustritt);
			} else if (e.getSource() == panelQueryPersonalzeiten) {

				panelBottomPersonalzeiten.eventActionNew(e, true, false);
				panelBottomPersonalzeiten.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPersonalzeiten);

			} else if (e.getSource() == panelQueryPersonalzeitmodell) {

				panelBottomPersonalzeitmodell.eventActionNew(e, true, false);
				panelBottomPersonalzeitmodell.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPersonalzeitmodel);

			} else if (e.getSource() == panelQuerySchichtzeitmodell) {

				panelBottomSchichtzeitmodell.eventActionNew(e, true, false);
				panelBottomSchichtzeitmodell.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitSchichtzeitmodel);

			} else if (e.getSource() == panelQueryPersonalzutrittsklasse) {

				panelBottomPersonalzutrittsklasse
						.eventActionNew(e, true, false);
				panelBottomPersonalzutrittsklasse.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPersonalzutrittsklasse);

			} else if (e.getSource() == panelQueryUrlaubsanspruch) {

				panelBottomUrlaubsanspruch.eventActionNew(e, true, false);
				panelBottomUrlaubsanspruch.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitUrlaubsanspruch);

			} else if (e.getSource() == panelQueryGleitzeitsaldo) {

				panelBottomGleitzeitsaldo.eventActionNew(e, true, false);
				panelBottomGleitzeitsaldo.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitGleitzeitsaldo);

			} else if (e.getSource() == panelQueryStundenabrechnung) {

				panelBottomStundenabrechnung.eventActionNew(e, true, false);
				panelBottomStundenabrechnung.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitStundenabrechnung);

			} else if (e.getSource() == panelQueryPersonalgehalt) {

				panelBottomPersonalgehalt.eventActionNew(e, true, false);
				panelBottomPersonalgehalt.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPersonalgehalt);

			} else if (e.getSource() == panelQueryPersonalverfuegbarkeit) {

				panelBottomPersonalverfuegbarkeit
						.eventActionNew(e, true, false);
				panelBottomPersonalverfuegbarkeit.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPersonalverfuegbarkeit);

			} else if (e.getSource() == panelQueryPersonalkurzbrief) {

				panelBottomPersonalkurzbrief.eventActionNew(e, true, false);
				panelBottomPersonalkurzbrief.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPersonalkurzbrief);

			} else if (e.getSource() == panelQueryPersonalfinger) {

				panelBottomPersonalfinger.eventActionNew(e, true, false);
				panelBottomPersonalfinger.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPersonalfinger);

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			if (e.getSource() == panelQueryPersonalzeitmodell) {
				DialogSchichtzeit d = new DialogSchichtzeit(
						getInternalFramePersonal(),
						(Integer) getPanelQueryPersonal().getSelectedId());
				d.setVisible(true);
				panelQueryPersonalzeitmodell.eventYouAreSelected(false);
			}

		}

	}

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryPersonal.eventYouAreSelected(false);
			panelQueryPersonal.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			createDetail((Integer) panelQueryPersonal.getSelectedId());
			panelDetailPersonal.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_DATEN) {
			createDaten(getInternalFramePersonal().getPersonalDto().getIId());
			panelDetailDaten.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_ANGEHOERIGE) {
			createAngehoerige(getInternalFramePersonal().getPersonalDto()
					.getIId());
			panelSplitAngehoerige.eventYouAreSelected(false);
			panelQueryAngehoerige.updateButtons();
		} else if (selectedIndex == IDX_PANEL_BANKVERBINDUNG) {
			createBankverbindung(getInternalFramePersonal().getPersonalDto()
					.getPartnerDto().getIId());
			panelSplitBankverbindung.eventYouAreSelected(false);
			panelQueryBankverbindung.updateButtons();
		} else if (selectedIndex == IDX_PANEL_EINTRITTAUSTRITT) {
			createEintrittAustritt(getInternalFramePersonal().getPersonalDto()
					.getIId());
			panelSplitEintrittaustritt.eventYouAreSelected(false);
			panelQueryEintrittaustritt.updateButtons();

		} else if (selectedIndex == IDX_PANEL_ZEITEN) {
			createZeiten(getInternalFramePersonal().getPersonalDto().getIId());
			panelSplitPersonalzeiten.eventYouAreSelected(false);
			panelQueryPersonalzeiten.updateButtons();

		} else if (selectedIndex == IDX_PANEL_PERSONALZEITMODELL) {
			createPersonalzeitmodell(getInternalFramePersonal()
					.getPersonalDto().getIId());
			panelSplitPersonalzeitmodel.eventYouAreSelected(false);
			panelQueryPersonalzeitmodell.updateButtons();

		} else if (selectedIndex == IDX_PANEL_SCHICHTZEITMODELL) {
			createSchichtzeitmodell(getInternalFramePersonal().getPersonalDto()
					.getIId());
			panelSplitSchichtzeitmodel.eventYouAreSelected(false);
			panelQuerySchichtzeitmodell.updateButtons();

		} else if (selectedIndex == IDX_PANEL_PERSONALZUTRITTSKLASSE) {
			createPersonalzutrittsklasse(getInternalFramePersonal()
					.getPersonalDto().getIId());
			panelSplitPersonalzutrittsklasse.eventYouAreSelected(false);
			panelQueryPersonalzutrittsklasse.updateButtons();

		} else if (selectedIndex == IDX_PANEL_URLAUBSANSPRUCH) {
			createUrlaubsanspruch(getInternalFramePersonal().getPersonalDto()
					.getIId());
			panelSplitUrlaubsanspruch.eventYouAreSelected(false);
			panelQueryUrlaubsanspruch.updateButtons();

		} else if (selectedIndex == IDX_PANEL_GLEITZEITSALDO) {
			createGleitzeitsaldo(getInternalFramePersonal().getPersonalDto()
					.getIId());
			panelSplitGleitzeitsaldo.eventYouAreSelected(false);
			panelQueryGleitzeitsaldo.updateButtons();

		} else if (selectedIndex == IDX_PANEL_STUNDENABRECHNUNG) {
			createStundenabrechnung(getInternalFramePersonal().getPersonalDto()
					.getIId());
			panelSplitStundenabrechnung.eventYouAreSelected(false);
			panelQueryStundenabrechnung.updateButtons();

		} else if (selectedIndex == IDX_PANEL_PERSONALGEHALT) {
			createPersonalgehalt(getInternalFramePersonal().getPersonalDto()
					.getIId());
			panelSplitPersonalgehalt.eventYouAreSelected(false);
			panelQueryPersonalgehalt.updateButtons();

		} else if (selectedIndex == IDX_PANEL_PERSONALVERFUEGBARKEIT) {
			createPersonalverfuegbarkeit(getInternalFramePersonal()
					.getPersonalDto().getIId());
			panelSplitPersonalverfuegbarkeit.eventYouAreSelected(false);
			panelQueryPersonalverfuegbarkeit.updateButtons();

		} else if (selectedIndex == IDX_PANEL_PERSONALKURZBRIEF) {
			createKurzbrief(getInternalFramePersonal().getPersonalDto()
					.getPartnerDto().getIId());
			panelSplitPersonalkurzbrief.eventYouAreSelected(false);
			panelQueryPersonalkurzbrief.updateButtons();

		} else if (selectedIndex == IDX_PANEL_PERSONALFINGER) {
			createFinger(getInternalFramePersonal().getPersonalDto().getIId());
			panelSplitPersonalfinger.eventYouAreSelected(false);
			panelQueryPersonalfinger.updateButtons();

		}
		refreshTitle();
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			try {
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE)) {

					JMenuItem menuItemPersonalliste = new JMenuItem(LPMain
							.getInstance().getTextRespectUISPr(
									"pers.report.personalliste"));

					menuItemPersonalliste.addActionListener(this);

					menuItemPersonalliste
							.setActionCommand(MENUE_ACTION_PERSONALLISTE);
					JMenu jmJournal = (JMenu) wrapperMenuBar
							.getComponent(WrapperMenuBar.MENU_JOURNAL);
					jmJournal.add(menuItemPersonalliste);

				}
			} catch (Throwable ex) {
				handleException(ex, false);
			}

			// Menue Bearbeiten
			JMenu jmBearbeiten = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

			JMenuItem menuItemBearbeitenExternerKommentar = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));
			menuItemBearbeitenExternerKommentar.addActionListener(this);
			menuItemBearbeitenExternerKommentar
					.setActionCommand(MENU_BEARBEITEN_KOMMENTAR);
			jmBearbeiten.add(menuItemBearbeitenExternerKommentar, 0);

		}

		return wrapperMenuBar;
	}

}
