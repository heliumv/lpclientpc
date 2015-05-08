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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
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
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportAuftragstatistik extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbAuftragsnummer = new WrapperRadioButton();
	private WrapperRadioButton wrbRechnungsadresse = new WrapperRadioButton();
	private WrapperRadioButton wrbProjekt = new WrapperRadioButton();
	private WrapperRadioButton wrbKunde = new WrapperRadioButton();
	private WrapperRadioButton wrbBestellnummer = new WrapperRadioButton();
	private WrapperRadioButton wrbFuehrenderArtikel = new WrapperRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperRadioButton wrbAuftragdatum = new WrapperRadioButton();
	private WrapperRadioButton wrbErledigtDatum = new WrapperRadioButton();
	private ButtonGroup buttonGroupDatum = new ButtonGroup();

	private WrapperCheckBox wcbArbeitszeitDetailliert = new WrapperCheckBox();

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperButton wbuRechnungsadresse = new WrapperButton();
	private WrapperButton wbuKunde = new WrapperButton();
	private WrapperButton wbuPartnerklassen = new WrapperButton();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperTextField wtfPartnerklassen = new WrapperTextField();
	private WrapperTextField wtfProjekt = new WrapperTextField();
	private WrapperTextField wtfRechnungsadresse = new WrapperTextField();
	private WrapperButton wbuProjekt = null;

	protected JPanel jpaWorkingOn = new JPanel();

	private WrapperLabel wlaBestellnummer = new WrapperLabel();
	private WrapperTextField wtfBestellnummer = new WrapperTextField();

	private WrapperDateRangeController wdrBereich = null;

	private Integer kundeIId_Rechnungsadresse = null;
	private Integer kundeIId = null;
	private PanelQueryFLR panelQueryFLRProjekt = null;
	private Integer projektIId = null;

	static final public String ACTION_SPECIAL_RECHNUNGSADRESSE_FROM_LISTE = "ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE";
	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "ACTION_SPECIAL_KUNDE_FROM_LISTE";
	static final public String ACTION_SPECIAL_PARTNERKLASSEN_FROM_LISTE = "ACTION_SPECIAL_PARTNERKLASSEN_FROM_LISTE";
	private final static String ACTION_SPECIAL_PROJEKT = "action_special_projekt";
	private PanelQueryFLR panelQueryFLRRechnungsadresse = null;

	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRPartnerliste = null;

	public ReportAuftragstatistik(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame(), null, false);
		
		new DialogQuery(panelQueryFLRProjekt);

	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn.setLayout(new GridBagLayout());
		wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
		wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.sortierung"));
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		boolean bProjektklammer = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER);

		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));
		wrbAuftragdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.belegdatum"));
		wrbErledigtDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.erledigungsdatum"));
		buttonGroupDatum.add(wrbAuftragdatum);
		buttonGroupDatum.add(wrbErledigtDatum);
		wrbAuftragdatum.setSelected(true);

		wcbArbeitszeitDetailliert.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.nachkalkulation.zeitendetailliert"));
		wcbArbeitszeitDetailliert.setSelected(false);
		wlaProjekt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));
		wlaBestellnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.bestellnummer"));
		wbuRechnungsadresse.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.rechnungsadresse")
				+ "...");
		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wbuPartnerklassen.setText(LPMain.getInstance().getTextRespectUISPr(
				"auftrag.partnerklassen"));
		wrbAuftragsnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.auftragnummer"));
		wrbFuehrenderArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"auftragsstatistik.sortierung.fuehrenderartikel"));

		wrbRechnungsadresse.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.rechnungsadresse"));
		wrbProjekt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));
		wrbKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kunde.modulname"));

		wbuRechnungsadresse
				.setActionCommand(ACTION_SPECIAL_RECHNUNGSADRESSE_FROM_LISTE);
		wbuRechnungsadresse.addActionListener(this);
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);
		wbuPartnerklassen
				.setActionCommand(ACTION_SPECIAL_PARTNERKLASSEN_FROM_LISTE);
		wbuPartnerklassen.addActionListener(this);

		buttonGroupSortierung.add(wrbAuftragsnummer);
		buttonGroupSortierung.add(wrbRechnungsadresse);
		buttonGroupSortierung.add(wrbProjekt);
		buttonGroupSortierung.add(wrbKunde);
		buttonGroupSortierung.add(wrbBestellnummer);
		buttonGroupSortierung.add(wrbFuehrenderArtikel);

		wtfKunde.setActivatable(false);
		wtfPartnerklassen.setActivatable(false);
		wtfRechnungsadresse.setActivatable(false);

		wrbAuftragsnummer.setSelected(true);

		wrbBestellnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.bestellnummer"));

		wbuProjekt = new WrapperButton(
				LPMain.getTextRespectUISPr("auft.report.projekte"));
		wbuProjekt.setActionCommand(ACTION_SPECIAL_PROJEKT);
		wbuProjekt.addActionListener(this);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

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
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAuftragdatum, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbErledigtDatum, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbArbeitszeitDetailliert, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAuftragsnummer, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		if (bProjektklammer == true) {
			wtfProjekt.setActivatable(false);
			jpaWorkingOn.add(wbuProjekt,
					new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		} else {
			jpaWorkingOn.add(wlaProjekt,
					new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbProjekt, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuPartnerklassen, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPartnerklassen, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbFuehrenderArtikel, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbKunde, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuRechnungsadresse, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfRechnungsadresse, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbRechnungsadresse, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBestellnummer, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBestellnummer, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBestellnummer, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(
				ACTION_SPECIAL_RECHNUNGSADRESSE_FROM_LISTE)) {
			dialogQueryRechnungsadresseFromListe(e);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PARTNERKLASSEN_FROM_LISTE)) {
			dialogQueryPartnerliste(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PROJEKT)) {
			dialogQueryProjektFromListe(e);
		}

	}

	private void dialogQueryPartnerliste(ActionEvent e) throws Throwable {
		panelQueryFLRPartnerliste = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame());

		new DialogQuery(panelQueryFLRPartnerliste);
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), false, true, kundeIId);

		new DialogQuery(panelQueryFLRKunde);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRRechnungsadresse) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(iId);
				kundeIId_Rechnungsadresse = kundeDto.getIId();
				wtfRechnungsadresse.setText(kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
			}

			else if (e.getSource() == panelQueryFLRKunde) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(iId);
				kundeIId = kundeDto.getIId();
				wtfKunde.setText(kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
			} else if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) panelQueryFLRProjekt.getSelectedId();
				if (key != null) {

					ProjektDto pDto = DelegateFactory.getInstance()
							.getProjektDelegate().projektFindByPrimaryKey(key);

					projektIId=key;
					wtfProjekt.setText(pDto.getCNr());
				}
			} else if (e.getSource() == panelQueryFLRPartnerliste) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate().partnerFindByPrimaryKey(iId);
				wtfPartnerklassen
						.setText(partnerDto.formatFixTitelName1Name2());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRRechnungsadresse) {
				kundeIId_Rechnungsadresse = null;
				wtfRechnungsadresse.setText(null);
			} else if (e.getSource() == panelQueryFLRKunde) {
				kundeIId = null;
				wtfKunde.setText(null);
			} else if (e.getSource() == panelQueryFLRProjekt) {
				kundeIId = null;
				wtfProjekt.setText(null);
			}
		}
	}

	void dialogQueryRechnungsadresseFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRRechnungsadresse = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), false, true,
						kundeIId_Rechnungsadresse);
		new DialogQuery(panelQueryFLRRechnungsadresse);

	}

	public String getModul() {
		return ZeiterfassungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AuftragReportFac.REPORT_AUFTRAGSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(wdfBis
				.getTimestamp().getTime() + 24 * 3600000);

		int iOptionsortierung = -1;

		if (wrbProjekt.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_PROJEKT;
		} else if (wrbKunde.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_AUFTRAGSADRESSE;
		} else if (wrbRechnungsadresse.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_RECHNUNGSADRESSE;
		} else if (wrbAuftragsnummer.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_AUFTRAG;
		} else if (wrbBestellnummer.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_BESTELLNUMMER;
		} else if (wrbFuehrenderArtikel.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_FUEHRENDER_ARTIKEL;
		}

		String projektBezeichnung=null;
		if(projektIId==null){
			projektBezeichnung=wtfProjekt.getText();
		}
		
		jasperPrint = DelegateFactory
				.getInstance()
				.getAuftragReportDelegate()
				.printAuftragstatistik(wdfVon.getTimestamp(), wdfBisTemp,
						wrbAuftragdatum.isSelected(), kundeIId,
						kundeIId_Rechnungsadresse, null, projektBezeichnung,
						wtfBestellnummer.getText(), iOptionsortierung,
						!wcbArbeitszeitDetailliert.isSelected(), null,
						projektIId);

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
