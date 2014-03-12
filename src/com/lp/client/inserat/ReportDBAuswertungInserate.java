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
package com.lp.client.inserat;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
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
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.inserat.service.InseratReportFac;
import com.lp.server.inserat.service.ReportJournalInseratDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;

public class ReportDBAuswertungInserate extends PanelBasis implements
		PropertyChangeListener, PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final static String ACTION_SPECIAL_SORTIERUNG_BEREICH_DATUM = "action_special_sortierung_bereich_datum";
	protected final static String ACTION_SPECIAL_SORTIERUNG_BEREICH_NUMMER = "action_special_sortierung_bereich_nummer";

	protected WrapperSelectField wsfVertreter = new WrapperSelectField(
			WrapperSelectField.PERSONAL, getInternalFrame(), true);

	protected WrapperSelectField wsfKunde = new WrapperSelectField(
			WrapperSelectField.KUNDE, getInternalFrame(), true);
	protected WrapperSelectField wsfLieferant = new WrapperSelectField(
			WrapperSelectField.LIEFERANT, getInternalFrame(), true);

	private final static int BREITE_SPALTE2 = 80;
	protected final static int BREITE_BUTTONS = 110;

	protected KundeDto kundeDto = null;

	private Border border1;
	private WrapperLabel wlaSortierung = new WrapperLabel();

	private WrapperLabel wlaBereich = new WrapperLabel();
	protected ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private ButtonGroup buttonGroupBereich = new ButtonGroup();

	protected WrapperLabel wlaVon1 = new WrapperLabel();
	protected WrapperLabel wlaVon2 = new WrapperLabel();
	protected WrapperLabel wlaBis1 = new WrapperLabel();
	protected WrapperLabel wlaBis2 = new WrapperLabel();

	protected JPanel jpaPartner = null;

	protected WrapperDateField wdfVon = new WrapperDateField();
	protected WrapperDateField wdfBis = new WrapperDateField();
	protected WrapperTextField wtfVon = new WrapperTextField();
	protected WrapperTextField wtfBis = new WrapperTextField();

	protected WrapperRadioButton wrbSortierungBelegnummer = new WrapperRadioButton();
	protected WrapperRadioButton wrbSortierungKunde = new WrapperRadioButton();
	protected WrapperRadioButton wrbSortierungLieferant = new WrapperRadioButton();
	protected WrapperRadioButton wrbSortierungVertreter = new WrapperRadioButton();

	protected JPanel jpaWorkingOn = new JPanel();

	protected WrapperRadioButton wrbBereichDatum = new WrapperRadioButton();
	protected WrapperRadioButton wrbBereichNummer = new WrapperRadioButton();

	protected WrapperDateRangeController wdrBereich = null;

	private WrapperRadioButton wrbBezahltdatum = new WrapperRadioButton();
	private WrapperRadioButton wrbVerrechnungsdatum = new WrapperRadioButton();

	protected JPanel jpaBereichDatum = null;

	public ReportDBAuswertungInserate(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		initPanel();
		setDefaults();
		initComponents();
	}

	private final void initPanel() throws Throwable {
		// rechte

	}

	private final void setDefaults() {

		wrbSortierungBelegnummer.setSelected(true);

		// datumseinschraenkung ist default sichtbar
		wrbBereichDatum.setSelected(true);
		wlaVon2.setVisible(false);
		wlaBis2.setVisible(false);
		wtfVon.setVisible(false);
		wtfBis.setVisible(false);
		// default alle Partner

		// wdrc: 4 default ist z.b. das vormonat
		wdrBereich.doClickUp();
	}

	private final void jbInit() throws Throwable {
		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(new GridBagLayout());

		wrbBereichDatum.setMinimumSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));
		wrbBereichDatum.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));

		jpaWorkingOn.setLayout(new GridBagLayout());

		wlaVon1.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaVon2.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis1.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaBis2.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaVon1.setMinimumSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaVon1.setPreferredSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaVon2.setMinimumSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaVon2.setPreferredSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaBis1.setMinimumSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaBis1.setPreferredSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaBis2.setMinimumSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaBis2.setPreferredSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));

		wlaSortierung.setMinimumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wlaSortierung.setPreferredSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);
		wlaSortierung.setText(LPMain.getTextRespectUISPr("lp.sortierung"));
		wlaBereich.setHorizontalAlignment(SwingConstants.LEFT);
		wlaBereich.setText(LPMain.getTextRespectUISPr("label.bereich"));
		jpaWorkingOn.setBorder(border1);

		wrbSortierungBelegnummer.setText(LPMain
				.getTextRespectUISPr("label.belegnummer"));

		wrbBereichDatum.setText(LPMain
				.getTextRespectUISPr("iv.dbauswertung.belegdatum"));
		wrbVerrechnungsdatum.setText(LPMain
				.getTextRespectUISPr("iv.dbauswertung.verrechnungsdatum"));
		wrbBereichNummer.setText(LPMain
				.getTextRespectUISPr("label.belegnummer"));

		wrbBezahltdatum.setText(LPMain
				.getTextRespectUISPr("iv.dbauswertung.bezahltdatum"));

		wrbBereichDatum
				.setActionCommand(ACTION_SPECIAL_SORTIERUNG_BEREICH_DATUM);
		wrbVerrechnungsdatum
				.setActionCommand(ACTION_SPECIAL_SORTIERUNG_BEREICH_DATUM);
		wrbBezahltdatum
				.setActionCommand(ACTION_SPECIAL_SORTIERUNG_BEREICH_DATUM);
		wrbBereichNummer
				.setActionCommand(ACTION_SPECIAL_SORTIERUNG_BEREICH_NUMMER);

		wrbSortierungBelegnummer.addActionListener(this);

		wrbBezahltdatum.addActionListener(this);
		wrbBereichDatum.addActionListener(this);
		wrbVerrechnungsdatum.addActionListener(this);
		wrbBereichNummer.addActionListener(this);

		// wdfVon.getDisplay().addPropertyChangeListener(this);
		// wdfBis.getDisplay().addPropertyChangeListener(this);

		// wdrc: 2 der DateRangeController muss die beiden DateFields kennen
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		// PJ 14057
		wdfVon.setName("wdfVon");
		wdfBis.setName("wdfBis");

		wrbBereichDatum.setName("wrbBereichDatum");
		wrbBereichNummer.setName("wrbBereichDatum");

		wrbSortierungBelegnummer.setName("wrbSortierungBelegnummer");

		// wegen dialogFLR
		getInternalFrame().addItemChangedListener(this);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBereich, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortierungBelegnummer, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBereichDatum, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbVerrechnungsdatum, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBezahltdatum, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(getJPanelBereichDatum(), new GridBagConstraints(5,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbBereichNummer, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaVon2, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVon, new GridBagConstraints(3, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis2, new GridBagConstraints(4, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBis, new GridBagConstraints(5, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortierungKunde, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wsfKunde.getWrapperButton(), new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 2), 100, 0));
		jpaWorkingOn.add(wsfKunde.getWrapperTextField(),
				new GridBagConstraints(2, iZeile, 4, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortierungLieferant, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wsfLieferant.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 30, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperTextField(),
				new GridBagConstraints(2, iZeile, 4, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbSortierungVertreter, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		wsfVertreter.getWrapperButton().setText(
				LPMain.getTextRespectUISPr("button.vertreter"));

		jpaWorkingOn.add(wsfVertreter.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 30, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfVertreter.getWrapperTextField(),
				new GridBagConstraints(2, iZeile, 4, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		buttonGroupSortierung.add(wrbSortierungBelegnummer);
		buttonGroupSortierung.add(wrbSortierungKunde);
		buttonGroupSortierung.add(wrbSortierungLieferant);
		buttonGroupSortierung.add(wrbSortierungVertreter);

		buttonGroupBereich.add(wrbBereichDatum);
		buttonGroupBereich.add(wrbVerrechnungsdatum);
		buttonGroupBereich.add(wrbBezahltdatum);
		buttonGroupBereich.add(wrbBereichNummer);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand()
				.equals(ACTION_SPECIAL_SORTIERUNG_BEREICH_DATUM)) {
			wlaVon1.setVisible(true);
			wlaBis1.setVisible(true);
			wdfVon.setVisible(true);
			wdfBis.setVisible(true);
			wdrBereich.setVisible(true);
			wlaVon2.setVisible(false);
			wlaBis2.setVisible(false);
			wtfVon.setVisible(false);
			wtfBis.setVisible(false);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_SORTIERUNG_BEREICH_NUMMER)) {
			wlaVon1.setVisible(false);
			wlaBis1.setVisible(false);
			wdfVon.setVisible(false);
			wdfBis.setVisible(false);
			wdrBereich.setVisible(false);
			wlaVon2.setVisible(true);
			wlaBis2.setVisible(true);
			wtfVon.setVisible(true);
			wtfBis.setVisible(true);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == wdfVon.getDisplay()
				&& evt.getPropertyName().equals("date")) {
			wdfBis.setMinimumValue(wdfVon.getDate());
		} else if (evt.getSource() == wdfBis.getDisplay()
				&& evt.getPropertyName().equals("date")) {
			wdfVon.setMaximumValue(wdfBis.getDate());
		}
	}

	protected void befuelleKriterien(ReportJournalInseratDto krit) {

		if (wrbBereichDatum.isSelected() || wrbVerrechnungsdatum.isSelected() || wrbBezahltdatum.isSelected()) {
			krit.dVon = wdfVon.getDate();
			krit.dBis = wdfBis.getDate();
		} else if (wrbBereichNummer.isSelected()) {
			krit.sBelegnummerVon = wtfVon.getText();
			krit.sBelegnummerBis = wtfBis.getText();
		}
		if (wrbSortierungBelegnummer.isSelected()) {
			krit.iSortierung = ReportJournalInseratDto.KRIT_SORT_NACH_BELEGNUMMER;
		} else if (wrbSortierungKunde.isSelected()) {
			krit.iSortierung = ReportJournalInseratDto.KRIT_SORT_NACH_KUNDE;
		} else if (wrbSortierungLieferant.isSelected()) {
			krit.iSortierung = ReportJournalInseratDto.KRIT_SORT_NACH_LIEFERANT;
		} else if (wrbSortierungVertreter.isSelected()) {
			krit.iSortierung = ReportJournalInseratDto.KRIT_SORT_NACH_VERTRETER;
		}

		krit.personalIId = wsfVertreter.getIKey();
		krit.kundeIId = wsfKunde.getIKey();
		krit.lieferantIId = wsfLieferant.getIKey();

	}

	protected void setEinschraenkungDatumBelegnummerSichtbar(boolean bSichtbar) {
		wrbBereichDatum.setVisible(bSichtbar);
		wrbBereichNummer.setVisible(bSichtbar);
		wlaBis1.setVisible(bSichtbar);
		wlaVon1.setVisible(bSichtbar);
		wlaBis2.setVisible(bSichtbar);
		wlaVon2.setVisible(bSichtbar);
		wdfBis.setVisible(bSichtbar);
		wdfVon.setVisible(bSichtbar);
		wtfBis.setVisible(bSichtbar);
		wtfVon.setVisible(bSichtbar);
		wdrBereich.setVisible(bSichtbar);
		wlaBereich.setVisible(bSichtbar);
		if (!bSichtbar) {
			wdfBis.setDate(null);
			wdfVon.setDate(null);
			wtfBis.setText(null);
			wtfVon.setText(null);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private JPanel getJPanelBereichDatum() {
		jpaBereichDatum = new JPanel(new GridBagLayout());
		jpaBereichDatum.add(wlaVon1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaBereichDatum.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaBereichDatum.add(wlaBis1, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaBereichDatum.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// wdrc: 3 position rechts neben den datefields
		jpaBereichDatum.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// rechts mit einem Label auffuellen, damit alles links ausgerichtet ist
		jpaBereichDatum.add(new JLabel(), new GridBagConstraints(5, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		return jpaBereichDatum;
	}

	public String getModul() {
		return InseratReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return InseratReportFac.REPORT_INSERATE_DBAUSWERTUNG;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		ReportJournalInseratDto krit = new ReportJournalInseratDto();
		befuelleKriterien(krit);

		int iOptionDatum = InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_KEINE;

		if (wrbVerrechnungsdatum.isSelected()) {
			iOptionDatum = InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_VERRECHNETDATUM;
		} else if (wrbBezahltdatum.isSelected()) {
			iOptionDatum = InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_BEZAHLTDATUM;
		} else if (wrbBereichDatum.isSelected()) {
			iOptionDatum = InseratReportFac.REPORT_DBAUSWERTUNG_OPTION_DATUM_BELEGDATUM;
		}

		return DelegateFactory.getInstance().getInseratDelegate()
				.printDBAuswertung(krit, iOptionDatum);
	}

}
