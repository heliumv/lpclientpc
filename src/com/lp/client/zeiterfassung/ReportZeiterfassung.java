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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public abstract class ReportZeiterfassung extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JPanel jpaWorkingOn;
	private WrapperLabel wlaPersonal = new WrapperLabel();
	private WrapperTextField wtfPersonal = new WrapperTextField();
	private ButtonGroup buttonGroupAuswertung = new ButtonGroup();
	private Integer personalIId = null;
	private JRadioButton wrbSelektierteperson = new JRadioButton();
	private JRadioButton wrbAllearbeiter = new JRadioButton();
	private JRadioButton wrbAlleangestellte = new JRadioButton();
	private JRadioButton wrbAllepersonen = new JRadioButton();
	private JRadioButton wrbMeineAbteilung = new JRadioButton();
	private PersonalDto personalDto = null;

	protected ButtonGroup buttonGroupSortierung = new ButtonGroup();
	protected WrapperLabel wlaSortierung = new WrapperLabel();
	protected JRadioButton wrbSortPersonalnummer = new JRadioButton();
	protected JRadioButton wrbSortNameVorname = new JRadioButton();
	protected JRadioButton wrbSortAbteilungNameVorname = new JRadioButton();
	protected JRadioButton wrbSortKostenstelleNameVorname = new JRadioButton();
	protected JRadioButton wrbSortAbteilungKostenstelleNameVorname = new JRadioButton();

	protected WrapperLabel wlaZeitraum;
	protected WrapperLabel wlaVon;
	protected WrapperDateField wdfVon;
	protected WrapperLabel wlaBis;
	protected WrapperDateField wdfBis;
	protected WrapperDateRangeController wdrBereich = null;

	private WrapperCheckBox wcbPlusVersteckte = new WrapperCheckBox();
	private WrapperCheckBox wcbNurAnwesende = new WrapperCheckBox();

	public ReportZeiterfassung(InternalFrame internalFrame,
			Integer personalIId, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		this.personalIId = personalIId;
		if (personalIId != null) {

			personalDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(personalIId);

			wtfPersonal.setText(personalDto.formatAnrede());
		}
		jbInit();
		initComponents();

	}

	protected boolean showSorting() {
		return true;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public boolean mitVersteckten() {
		return wcbPlusVersteckte.isSelected();
	}

	public boolean nurAnwesende() {
		return wcbNurAnwesende.isSelected();
	}

	public Integer getPersonAuswahl() {
		int iReturn = -1;
		if (wrbSelektierteperson.isSelected()) {
			iReturn = ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON;
		} else if (wrbAllepersonen.isSelected()) {
			iReturn = ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN;

		} else if (wrbAlleangestellte.isSelected()) {
			iReturn = ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE;

		} else if (wrbAllearbeiter.isSelected()) {
			iReturn = ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER;

		} else if (wrbMeineAbteilung.isSelected()) {
			iReturn = ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_MEINE_ABTEILUNG;
		}
		return iReturn;
	}

	protected abstract JComponent getFirstFocusableComponent() throws Exception;

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		wlaPersonal
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.selektierteperson")
						+ ": ");
		wtfPersonal.setEditable(false);
		wtfPersonal.setActivatable(false);
		wtfPersonal.setSaveReportInformation(false);
		wrbSelektierteperson.setSelected(true);

		wrbSelektierteperson
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.selektierteperson"));
		wrbAllearbeiter.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.report.allearbeiter"));
		wrbAlleangestellte.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.report.alleangestellte"));
		wrbAllepersonen.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.report.allepersonen"));
		wcbPlusVersteckte.setText(LPMain.getTextRespectUISPr("lp.versteckte"));
		wcbNurAnwesende.setText(LPMain
				.getTextRespectUISPr("pers.zeiterfassung.report.nuranwesende"));
		wrbMeineAbteilung.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.report.meineabteilung"));

		buttonGroupAuswertung.add(wrbSelektierteperson);
		buttonGroupAuswertung.add(wrbAllepersonen);
		buttonGroupAuswertung.add(wrbAllearbeiter);
		buttonGroupAuswertung.add(wrbAlleangestellte);
		buttonGroupAuswertung.add(wrbMeineAbteilung);

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));
		wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);
		wrbSortAbteilungNameVorname
				.setText(LPMain
						.getTextRespectUISPr("pers.monatsabrechnung.sortierung.abteilungnamevorname"));
		wrbSortNameVorname
				.setText(LPMain
						.getTextRespectUISPr("pers.monatsabrechnung.sortierung.namevorname"));
		wrbSortPersonalnummer.setText(LPMain
				.getTextRespectUISPr("pers.personal.personalnummer"));

		wrbSortKostenstelleNameVorname
				.setText(LPMain
						.getTextRespectUISPr("pers.monatsabrechnung.sortierung.kostenstellenamevorname"));
		wrbSortAbteilungKostenstelleNameVorname
				.setText(LPMain
						.getTextRespectUISPr("pers.monatsabrechnung.sortierung.abteilungkostenstellenamevorname"));

		buttonGroupSortierung.add(wrbSortAbteilungNameVorname);
		buttonGroupSortierung.add(wrbSortNameVorname);
		buttonGroupSortierung.add(wrbSortPersonalnummer);
		buttonGroupSortierung.add(wrbSortKostenstelleNameVorname);
		buttonGroupSortierung.add(wrbSortAbteilungKostenstelleNameVorname);

		wrbSortAbteilungNameVorname.setVisible(showSorting());
		wrbSortNameVorname.setVisible(showSorting());
		wrbSortPersonalnummer.setVisible(showSorting());
		wrbSortKostenstelleNameVorname.setVisible(showSorting());
		wrbSortAbteilungKostenstelleNameVorname.setVisible(showSorting());
		wlaSortierung.setVisible(showSorting());
		wrbSortPersonalnummer.setSelected(true);

		boolean sichtbarkeitAlle = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE);

		jpaWorkingOn = new JPanel(new MigLayout("wrap 4, hidemode 2",
				"[25%,fill|25%,fill|20%,fill]5%[30%,fill]"));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaPersonal);
		jpaWorkingOn.add(wtfPersonal, "span 2");
		jpaWorkingOn.add(wlaSortierung);

		jpaWorkingOn.add(wrbSelektierteperson, "newline, skip");
		if (sichtbarkeitAlle
				|| DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ABTEILUNG)) {
			jpaWorkingOn.add(wrbMeineAbteilung);
			if (personalDto == null
					|| personalDto.getKostenstelleIIdAbteilung() == null) {
				wrbMeineAbteilung.setEnabled(false);
			}
		}
		jpaWorkingOn.add(wrbSortPersonalnummer, "cell 3 1");

		if (sichtbarkeitAlle)
			jpaWorkingOn.add(wrbAllepersonen, "newline, skip");
		jpaWorkingOn.add(wrbSortNameVorname, "cell 3 2");

		if (sichtbarkeitAlle) {
			jpaWorkingOn.add(wrbAllearbeiter, "newline, skip");
			jpaWorkingOn.add(wrbAlleangestellte);
		}
		jpaWorkingOn.add(wrbSortAbteilungNameVorname, "cell 3 3");

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbPlusVersteckte, "newline, skip");
			jpaWorkingOn.add(wcbNurAnwesende);
			
			wcbNurAnwesende.addActionListener(this);

		} else {
			jpaWorkingOn.add(wcbNurAnwesende, "newline, skip");
		}
		jpaWorkingOn.add(wrbSortKostenstelleNameVorname, "cell 3 4");
		jpaWorkingOn.add(wrbSortAbteilungKostenstelleNameVorname,
				"cell 3 5, wrap");
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if(wcbNurAnwesende.isSelected()){
			wcbPlusVersteckte.setSelected(true);
			wcbPlusVersteckte.setEnabled(false);
		
		} else {
			wcbPlusVersteckte.setEnabled(true);
		}
	}
	
	protected void addZeitraumAuswahl() {

		wlaZeitraum = new WrapperLabel();
		wlaVon = new WrapperLabel();
		wdfVon = new WrapperDateField();
		wlaBis = new WrapperLabel();
		wdfBis = new WrapperDateField();

		wlaZeitraum.setText(LPMain.getTextRespectUISPr("lp.zeitraum") + ":");
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		jpaWorkingOn.add(wlaZeitraum, "newline");
		jpaWorkingOn.add(wlaVon, "span, split, growx 30");
		jpaWorkingOn.add(wdfVon, "growx");
		jpaWorkingOn.add(wlaBis, "growx 30");
		jpaWorkingOn.add(wdfBis, "growx");
		jpaWorkingOn.add(wdrBereich, "growy");
	}

	public abstract String getModul();

	public abstract String getReportname();

	public abstract JasperPrintLP getReport(String sDrucktype) throws Throwable;

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
