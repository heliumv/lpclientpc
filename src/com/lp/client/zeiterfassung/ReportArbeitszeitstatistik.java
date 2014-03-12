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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.report.JasperPrintLP;

public class ReportArbeitszeitstatistik extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaZeitraum = new WrapperLabel();
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbTaetigkeit = new WrapperRadioButton();
	private WrapperRadioButton wrbPersonal = new WrapperRadioButton();
	private WrapperRadioButton wrbBeleg = new WrapperRadioButton();
	private WrapperRadioButton wrbKunde = new WrapperRadioButton();
	private WrapperRadioButton wrbArtikelgruppe = new WrapperRadioButton();
	private WrapperRadioButton wrbArtikelklasse = new WrapperRadioButton();
	private WrapperRadioButton wrbKostenstelle = new WrapperRadioButton();

	private WrapperComboBox wcoBeleg = new WrapperComboBox();
	private WrapperButton wbuBeleg = new WrapperButton();
	private WrapperButton wbuPerson = new WrapperButton();
	private WrapperButton wbuTaetigkeit = new WrapperButton();
	private WrapperButton wbuKunde = new WrapperButton();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperTextField wtfPerson = new WrapperTextField();
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private WrapperTextField wtfTaetigkeit = new WrapperTextField();

	protected JPanel jpaWorkingOn = new JPanel();

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private WrapperCheckBox wcbVerdichtet = new WrapperCheckBox();

	private WrapperButton wbuArtikelgruppeFLR = null;
	private WrapperTextField wtfArtikelgruppe = new WrapperTextField();
	private WrapperButton wbuArtikelklasseFLR = null;
	private WrapperTextField wtfArtikelklasse = new WrapperTextField();
	private WrapperDateRangeController wdrBereich = null;

	private Integer artikelIId = null;
	private Integer belegartIId = null;
	private Integer personalIId = null;
	private Integer partnerIId = null;
	Integer artikelgruppeIId = null;
	Integer artikelklasseIId = null;

	static final public String ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE = "ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE";
	static final public String ACTION_SPECIAL_BELEG_FROM_LISTE = "ACTION_SPECIAL_BELEG_FROM_LISTE";
	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "ACTION_SPECIAL_KUNDE_FROM_LISTE";
	static final public String ACTION_SPECIAL_PERSONAL_FROM_LISTE = "ACTION_SPECIAL_PERSONAL_FROM_LISTE";
	static final private String ACTION_SPECIAL_FLR_ARTIKELGRUPPE = "ACTION_SPECIAL_FLR_ARTIKELGRUPPE";
	static final private String ACTION_SPECIAL_FLR_ARTIKELKLASSE = "ACTION_SPECIAL_FLR_ARTIKELKLASSE";
	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRPersonal = null;

	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRProjekt = null;
	private PanelQueryFLR panelQueryFLRAngebot = null;
	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private PanelQueryFLR panelQueryFLRArtikelklasse = null;

	public ReportArbeitszeitstatistik(InternalFrameZeiterfassung internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn.setLayout(new GridBagLayout());
		wlaZeitraum.setText(LPMain.getTextRespectUISPr("lp.zeitraum") + ":");
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaSortierung.setText(LPMain.getTextRespectUISPr("lp.sortierung"));
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		wbuBeleg.setText(LPMain.getTextRespectUISPr("lp.beleg"));
		wbuPerson.setText(LPMain.getTextRespectUISPr("lp.personal"));
		wbuTaetigkeit.setText(LPMain.getTextRespectUISPr("button.taetigkeit"));
		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wrbTaetigkeit.setText(LPMain.getTextRespectUISPr("lp.taetigkeit"));
		wrbPersonal.setText(LPMain
				.getTextRespectUISPr("button.personal.tooltip"));
		wrbBeleg.setText(LPMain.getTextRespectUISPr("pers.arbeitszeitstatistik.sortierung.belegpersonal"));
		wrbKunde.setText(LPMain.getTextRespectUISPr("pers.arbeitszeitstatistik.sortierung.kundepersonal"));
		wrbKostenstelle.setText(LPMain
				.getTextRespectUISPr("label.kostenstelle"));

		wbuTaetigkeit.setActionCommand(ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE);
		wbuTaetigkeit.addActionListener(this);
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);
		wbuPerson.setActionCommand(ACTION_SPECIAL_PERSONAL_FROM_LISTE);
		wbuPerson.addActionListener(this);
		wbuBeleg.setActionCommand(ACTION_SPECIAL_BELEG_FROM_LISTE);
		wbuBeleg.addActionListener(this);

		wbuArtikelgruppeFLR = new WrapperButton();
		wbuArtikelgruppeFLR.setText(LPMain
				.getTextRespectUISPr("button.artikelgruppe"));
		wbuArtikelgruppeFLR.setMandatoryField(true);
		wbuArtikelgruppeFLR.setActionCommand(ACTION_SPECIAL_FLR_ARTIKELGRUPPE);
		wbuArtikelgruppeFLR.addActionListener(this);

		wbuArtikelklasseFLR = new WrapperButton();
		wbuArtikelklasseFLR.setText(LPMain
				.getTextRespectUISPr("button.artikelklasse"));
		wbuArtikelklasseFLR.setMandatoryField(true);
		wbuArtikelklasseFLR.setActionCommand(ACTION_SPECIAL_FLR_ARTIKELKLASSE);
		wbuArtikelklasseFLR.addActionListener(this);

		wcoBeleg.addActionListener(this);

		buttonGroupSortierung.add(wrbTaetigkeit);
		buttonGroupSortierung.add(wrbPersonal);
		buttonGroupSortierung.add(wrbBeleg);
		buttonGroupSortierung.add(wrbKunde);
		buttonGroupSortierung.add(wrbArtikelgruppe);
		buttonGroupSortierung.add(wrbArtikelklasse);
		buttonGroupSortierung.add(wrbKostenstelle);

		wtfKunde.setActivatable(false);
		wtfPerson.setActivatable(false);
		wtfBeleg.setActivatable(false);
		wtfTaetigkeit.setActivatable(false);

		boolean bHatAngebotszeiterfassung = false;
		boolean bHatProjektzeiterfassung = false;

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)
				&& LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG)) {
			bHatProjektzeiterfassung = true;
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)
				&& LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG)) {
			bHatAngebotszeiterfassung = true;
		}

		Map<String, String> mBelegarten = new LinkedHashMap<String, String>();
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {
			mBelegarten.put(LocaleFac.BELEGART_AUFTRAG,
					LocaleFac.BELEGART_AUFTRAG);
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)) {
			mBelegarten.put(LocaleFac.BELEGART_LOS, LocaleFac.BELEGART_LOS);
		}
		if (bHatAngebotszeiterfassung) {
			mBelegarten.put(LocaleFac.BELEGART_ANGEBOT,
					LocaleFac.BELEGART_ANGEBOT);
		}
		if (bHatProjektzeiterfassung == true) {
			mBelegarten.put(LocaleFac.BELEGART_PROJEKT,
					LocaleFac.BELEGART_PROJEKT);
		}

		wcoBeleg.setMap(mBelegarten);
		wrbTaetigkeit.setSelected(true);

		wcbVerdichtet.setText(LPMain.getTextRespectUISPr("lp.verdichtet"));

		wrbArtikelgruppe
				.setText(LPMain.getTextRespectUISPr("lp.artikelgruppe"));
		wrbArtikelklasse
				.setText(LPMain.getTextRespectUISPr("lp.artikelklasse"));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbTaetigkeit, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBeleg, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuPerson, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPerson, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbKunde, new GridBagConstraints(2, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbPersonal, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbKostenstelle, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbArtikelgruppe, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuTaetigkeit, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfTaetigkeit, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbArtikelklasse, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuArtikelgruppeFLR, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelgruppe, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuArtikelklasseFLR, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelklasse, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcoBeleg, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuBeleg, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBeleg, new GridBagConstraints(2, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wcoBeleg)) {
			wtfBeleg.setText("");
			belegartIId = null;
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONAL_FROM_LISTE)) {
			dialogQueryPersonalFromListe(e);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FLR_ARTIKELGRUPPE)) {
			panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelgruppe(getInternalFrame(),
							artikelgruppeIId);
			new DialogQuery(panelQueryFLRArtikelgruppe);

		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FLR_ARTIKELKLASSE)) {
			panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelklasse(getInternalFrame(),
							artikelklasseIId);
			new DialogQuery(panelQueryFLRArtikelklasse);

		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BELEG_FROM_LISTE)) {
			if (wcoBeleg.getKeyOfSelectedItem() != null) {
				if (wcoBeleg.getKeyOfSelectedItem().equals(
						LocaleFac.BELEGART_LOS)) {
					dialogQueryLosFromListe(e);
				} else if (wcoBeleg.getKeyOfSelectedItem().equals(
						LocaleFac.BELEGART_AUFTRAG)) {
					dialogQueryAuftragFromListe(e);
				} else if (wcoBeleg.getKeyOfSelectedItem().equals(
						LocaleFac.BELEGART_PROJEKT)) {
					dialogQueryProjektFromListe(e);
				} else if (wcoBeleg.getKeyOfSelectedItem().equals(
						LocaleFac.BELEGART_ANGEBOT)) {
					dialogQueryAngebotFromListe(e);
				}
			}
		}
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame(), partnerIId, true);

		new DialogQuery(panelQueryFLRKunde);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				artikelIId = artikelDto.getIId();
				wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRPersonal) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				PersonalDto personalDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey((Integer) key);
				wtfPerson.setText(personalDto.formatAnrede());
				personalIId = ((Integer) key);
			} else if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtgruDto artgruDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artgruFindByPrimaryKey((Integer) key);
				wtfArtikelgruppe.setText(artgruDto.getCNr());
				artikelgruppeIId = (Integer) key;
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtklaDto artklaDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artklaFindByPrimaryKey((Integer) key);
				wtfArtikelklasse.setText(artklaDto.getCNr());
				artikelklasseIId = (Integer) key;

			}

			else if (e.getSource() == panelQueryFLRLos) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				LosDto losDto = null;
				losDto = DelegateFactory.getInstance().getFertigungDelegate()
						.losFindByPrimaryKey(key);
				String projBez = losDto.getCProjekt();
				if (projBez == null) {
					projBez = "";
				}

				wtfBeleg.setText(losDto.getCNr() + ", " + projBez);

				belegartIId = losDto.getIId();

			} else if (e.getSource() == panelQueryFLRAngebot) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AngebotDto angebotDto = null;
				angebotDto = DelegateFactory.getInstance().getAngebotDelegate()
						.angebotFindByPrimaryKey(key);
				belegartIId = angebotDto.getIId();

				String projBez = angebotDto.getCBez();
				if (projBez == null) {
					projBez = "";
				}

				wtfBeleg.setText(angebotDto.getCNr() + ", " + projBez);
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AuftragDto auftragDto = null;
				auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(key);
				belegartIId = auftragDto.getIId();

				String projBez = ", " + auftragDto.getCBezProjektbezeichnung();

				String kunde = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdAuftragsadresse())
						.getPartnerDto().formatTitelAnrede();

				wtfBeleg.setText(auftragDto.getCNr() + projBez + ", " + kunde);
			} else if (e.getSource() == panelQueryFLRKunde) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate().partnerFindByPrimaryKey(iId);
				partnerIId = partnerDto.getIId();
				wtfKunde.setText(partnerDto.formatFixTitelName1Name2());
			} else if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				ProjektDto projektDto = null;
				projektDto = DelegateFactory.getInstance().getProjektDelegate()
						.projektFindByPrimaryKey(key);
				belegartIId = projektDto.getIId();

				String projBez = projektDto.getCTitel();
				if (projBez == null) {
					projBez = "";
				}

				wtfBeleg.setText(projektDto.getIId() + ", " + projBez);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikel) {
				artikelIId = null;
				wtfTaetigkeit.setText(null);
			} else if (e.getSource() == panelQueryFLRAuftrag
					|| e.getSource() == panelQueryFLRAngebot
					|| e.getSource() == panelQueryFLRLos
					|| e.getSource() == panelQueryFLRProjekt) {
				belegartIId = null;
				wtfBeleg.setText(null);
			} else if (e.getSource() == panelQueryFLRPersonal) {
				personalIId = null;
				wtfPerson.setText(null);
			} else if (e.getSource() == panelQueryFLRKunde) {
				partnerIId = null;
				wtfKunde.setText(null);
			} else if (e.getSource() == panelQueryFLRArtikelgruppe) {
				artikelgruppeIId = null;
				wtfArtikelgruppe.setText("");
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				artikelklasseIId = null;
				wtfArtikelklasse.setText("");
			}

		}
	}

	void dialogQueryPersonalFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRPersonal = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), false, true,
						personalIId);

		new DialogQuery(panelQueryFLRPersonal);

	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance()
				.createPanelFLRLose(getInternalFrame(), null, false);
		panelQueryFLRLos.setSelectedId(belegartIId);

		new DialogQuery(panelQueryFLRLos);

	}

	void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRAngebot = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebot(getInternalFrame(), false, false,
						SystemFilterFactory.getInstance().createFKMandantCNr(),
						belegartIId);
		new DialogQuery(panelQueryFLRAngebot);

	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame(), belegartIId, false);
		new DialogQuery(panelQueryFLRProjekt);

	}

	void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), false, true, null,
						belegartIId);

		new DialogQuery(panelQueryFLRAuftrag);

	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), true);
		new DialogQuery(panelQueryFLRArtikel);

	}

	public String getModul() {
		return ZeiterfassungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(wdfBis
				.getTimestamp().getTime() + 24 * 3600000);

		int iOptionsortierung = -1;

		if (wrbBeleg.isSelected()) {
			iOptionsortierung = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG;
		} else if (wrbKunde.isSelected()) {
			iOptionsortierung = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE;

		} else if (wrbPersonal.isSelected()) {
			iOptionsortierung = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL;

		} else if (wrbTaetigkeit.isSelected()) {
			iOptionsortierung = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL;

		} else if (wrbKostenstelle.isSelected()) {
			iOptionsortierung = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE;

		} else if (wrbArtikelgruppe.isSelected()) {
			iOptionsortierung = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE;

		} else if (wrbArtikelklasse.isSelected()) {
			iOptionsortierung = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE;

		}

		jasperPrint = DelegateFactory
				.getInstance()
				.getZeiterfassungReportDelegate()
				.printArbeitszeitstatistik(wdfVon.getTimestamp(), wdfBisTemp,
						iOptionsortierung,
						(String) wcoBeleg.getKeyOfSelectedItem(), belegartIId,
						personalIId, artikelIId, partnerIId, artikelgruppeIId,
						artikelklasseIId, wcbVerdichtet.isSelected());

		return jasperPrint;

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
