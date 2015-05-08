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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportStueckliste extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	InternalFrameStueckliste internalFrameStueckliste = null;
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";
	private Integer lagerIId = null;
	private PanelQueryFLR panelQueryFLRLager = null;
	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfLager = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRStueckliste = null;

	private Integer[] stuecklisteIId = null;
	private WrapperButton wbuStueckliste = new WrapperButton();
	private WrapperTextField wtStueckliste = new WrapperTextField();
	private ButtonGroup buttonGroupDruck = new ButtonGroup();
	private ButtonGroup buttonGroupUnterstklSortierung = new ButtonGroup();
	private WrapperRadioButton wrbAllgemein = new WrapperRadioButton();
	private WrapperRadioButton wrbAusgabestueckliste = new WrapperRadioButton();
	private WrapperRadioButton wrbAllgemeinMitPreis = new WrapperRadioButton();

	private WrapperRadioButton wrbUnterstklSortierungNachIdent = new WrapperRadioButton();
	private WrapperRadioButton wrbUnterstklSortierungPosition = new WrapperRadioButton();
	private WrapperRadioButton wrbUnterstklSortierungWieErfasst = new WrapperRadioButton();
	private WrapperCheckBox wcbUnterstklstrukturBelassen = new WrapperCheckBox();

	Map<Integer, String> mSortierungAlle = new LinkedHashMap<Integer, String>();
	Map<Integer, String> mSortierungNurArtikelNrBez = new LinkedHashMap<Integer, String>();

	private WrapperCheckBox wcbStuecklistenkommentar = new WrapperCheckBox();
	private WrapperCheckBox wcbPositionskommentar = new WrapperCheckBox();
	private WrapperCheckBox wcbGleichePositionenZusammenfassen = new WrapperCheckBox();
	private WrapperCheckBox wcbUnterstuecklistenEinbinden = new WrapperCheckBox();
	private WrapperCheckBox wcbSortiertNachAenderungsdatum = new WrapperCheckBox();
	private WrapperCheckBox wcbSortiertNachMontageart = new WrapperCheckBox();

	private WrapperCheckBox wcbNurAbbuchungslaeger = new WrapperCheckBox();

	static final public String ACTION_SPECIAL_STKL_FROM_LISTE = "ACTION_SPECIAL_STKL_FROM_LISTE";

	private WrapperComboBox wcoPreis = new WrapperComboBox();

	private WrapperComboBox wcoSortierung1 = new WrapperComboBox();
	private WrapperComboBox wcoSortierung2 = new WrapperComboBox();
	private WrapperComboBox wcoSortierung3 = new WrapperComboBox();

	private static int SORT_ARTIKELNUMMER = 0;
	private static int SORT_LFDNUMMER = 1;
	private static int SORT_POSITION = 2;
	private static int SORT_MONTAGEART = 3;
	private static int SORT_KOMMENTAR = 4;
	private static int SORT_WIE_ERFASST = 5;
	private static int SORT_ARTIKELBEZEICHNUNG = 6;

	private WrapperLabel wlaLosgroesse = new WrapperLabel();
	private WrapperNumberField wnfLosgroesse = new WrapperNumberField();

	private WrapperLabel wlaEinheit = new WrapperLabel();

	public ReportStueckliste(InternalFrameStueckliste internalFrame,
			String add2Title, Integer stuecklisteIId) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr("stkl.stueckliste");
		internalFrameStueckliste = internalFrame;
		this.stuecklisteIId = new Integer[] { stuecklisteIId };
		jbInit();
		initComponents();
		if (stuecklisteIId != null) {
			wtStueckliste.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
			StuecklisteDto dto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(stuecklisteIId);
			wtStueckliste.setText(dto.getArtikelDto()
					.formatArtikelbezeichnung());
			wnfLosgroesse.setBigDecimal(dto.getNLosgroesse());
			wlaEinheit.setText(dto.getArtikelDto().getEinheitCNr().trim());
		}
		LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.getHauptlagerDesMandanten();
		lagerIId = lagerDto.getIId();
		wtfLager.setText(lagerDto.getCNr());

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLager;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		internalFrameStueckliste.addItemChangedListener(this);
		wbuStueckliste.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.report.selektiertestueckliste"));

		wbuStueckliste.setActionCommand(ACTION_SPECIAL_STKL_FROM_LISTE);
		wbuStueckliste.addActionListener(this);

		wtStueckliste.setEditable(false);
		wtStueckliste.setMandatoryField(true);
		wtStueckliste.setSaveReportInformation(false);
		wtfLager.setMandatoryField(true);
		wrbAllgemein.setText("Allgemein");
		wrbAllgemein.addActionListener(this);
		wrbAllgemein.setSelected(true);

		wrbAusgabestueckliste.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.report.ausgabestueckliste"));
		wrbAllgemeinMitPreis.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.report.allgemeinmitpreis"));

		wrbAusgabestueckliste.addActionListener(this);
		wrbAllgemeinMitPreis.addActionListener(this);
		wcbUnterstuecklistenEinbinden.addActionListener(this);
		wcbNurAbbuchungslaeger.addActionListener(this);

		wrbUnterstklSortierungNachIdent.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.artikelnummer"));
		wcbUnterstklstrukturBelassen.setText(LPMain.getInstance()
				.getTextRespectUISPr("stkl.report.unterstklstrukturbelassen"));

		wrbUnterstklSortierungPosition.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.position"));
		wrbUnterstklSortierungWieErfasst.setText(LPMain.getInstance()
				.getTextRespectUISPr("stk.sort.wieerfasst"));
		wrbUnterstklSortierungNachIdent.setSelected(true);

		wcbGleichePositionenZusammenfassen
				.setText("gleiche Positionen zusammenfassen");
		wcbPositionskommentar.setText("Positionskommentar");
		wcbSortiertNachAenderungsdatum
				.setText("sortiert nach \u00C4nderungsdatum");
		wcbSortiertNachMontageart.setText("sortiert nach Montageart");
		wcbNurAbbuchungslaeger.setText(LPMain.getInstance()
				.getTextRespectUISPr("stk.report.nurabuchungslaeger"));

		wcbStuecklistenkommentar.setText(LPMain.getInstance()
				.getTextRespectUISPr("stkl.report.stuecklistenkommentar"));

		wcbUnterstuecklistenEinbinden.setText(LPMain.getInstance()
				.getTextRespectUISPr("stkl.report.unterstkleinbinden"));

		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wtfLager.setEditable(false);
		wbuLager.setActionCommand(this.ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);

		wcoSortierung1.setMandatoryField(true);
		wcoSortierung2.setMandatoryField(true);
		wcoSortierung3.setMandatoryField(true);

		wlaLosgroesse.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.losgroesse"));
		wlaLosgroesse.setHorizontalAlignment(wlaLosgroesse.LEFT);
		// ComboBox befuellen
		wcoPreis.setMandatoryField(true);
		Map<Integer, String> mBelegarten = new LinkedHashMap<Integer, String>();
		mBelegarten
				.put(new Integer(
						StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_GESTEHUNGSSPREIS),
						"Gestehungspreis");
		mBelegarten
				.put(new Integer(
						StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_VERKAUFSSPREIS),
						"VK-Preis");
		mBelegarten
				.put(new Integer(
						StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_PREIS_EINKAUFSPREIS),
						"EK-Preis");
		wcoPreis.setMap(mBelegarten);

		wcbUnterstklstrukturBelassen.setSelected(true);

		mSortierungAlle = new LinkedHashMap<Integer, String>();
		mSortierungAlle.put(new Integer(SORT_ARTIKELNUMMER), "Artikelnummer");
		mSortierungAlle.put(new Integer(SORT_ARTIKELBEZEICHNUNG),
				"Artikelbezeichnung");
		mSortierungAlle.put(new Integer(SORT_POSITION), LPMain.getInstance()
				.getTextRespectUISPr("lp.position"));
		mSortierungAlle.put(new Integer(SORT_LFDNUMMER), "LfdNr");
		mSortierungAlle.put(new Integer(SORT_MONTAGEART), LPMain.getInstance()
				.getTextRespectUISPr("stkl.montageart"));
		mSortierungAlle.put(new Integer(SORT_WIE_ERFASST), LPMain.getInstance()
				.getTextRespectUISPr("stk.sortierung.report.wieerfasst"));
		wcoSortierung1.setMap(mSortierungAlle);
		wcoSortierung2.setMap(mSortierungAlle);
		wcoSortierung3.setMap(mSortierungAlle);

		mSortierungNurArtikelNrBez = new LinkedHashMap<Integer, String>();
		mSortierungNurArtikelNrBez.put(new Integer(SORT_ARTIKELNUMMER),
				"Artikelnummer");
		mSortierungNurArtikelNrBez.put(new Integer(SORT_ARTIKELBEZEICHNUNG),
				"Artikelbezeichnung");

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuStueckliste, new GridBagConstraints(0, 0, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtStueckliste, new GridBagConstraints(1, 0, 2, 1, 1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, 1, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbAllgemein, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAllgemeinMitPreis, new GridBagConstraints(0, 3, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAusgabestueckliste, new GridBagConstraints(0, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaLosgroesse, new GridBagConstraints(0, 5, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfLosgroesse, new GridBagConstraints(0, 5, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 60, 2, 50), -300, 0));

		jpaWorkingOn.add(wlaEinheit, new GridBagConstraints(0, 5, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 30), -100, 0));

		jpaWorkingOn.add(wcbUnterstuecklistenEinbinden, new GridBagConstraints(
				0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbUnterstklSortierungNachIdent,
				new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wrbUnterstklSortierungPosition,
				new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wrbUnterstklSortierungWieErfasst,
				new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wcbUnterstklstrukturBelassen, new GridBagConstraints(
				0, 10, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoPreis, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbStuecklistenkommentar, new GridBagConstraints(1, 4,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		WrapperLabel wlaSortierung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.sortierung") + ":");
		wlaSortierung.setHorizontalAlignment(wlaLosgroesse.LEFT);

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 300, 0));

		jpaWorkingOn.add(wcbPositionskommentar, new GridBagConstraints(1, 5, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbGleichePositionenZusammenfassen,
				new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wcbNurAbbuchungslaeger, new GridBagConstraints(1, 7,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoSortierung1, new GridBagConstraints(2, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wcoSortierung2, new GridBagConstraints(2, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wcoSortierung3, new GridBagConstraints(2, 6, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		buttonGroupDruck.add(wrbAllgemein);
		buttonGroupDruck.add(wrbAllgemeinMitPreis);
		buttonGroupDruck.add(wrbAusgabestueckliste);

		buttonGroupUnterstklSortierung.add(wrbUnterstklSortierungNachIdent);
		buttonGroupUnterstklSortierung.add(wrbUnterstklSortierungPosition);
		buttonGroupUnterstklSortierung.add(wrbUnterstklSortierungWieErfasst);

		eventActionSpecial(new ActionEvent(wrbAllgemein, -1, ""));
	}

	public String getModul() {
		return StuecklisteReportFac.REPORT_MODUL;
	}

	public String getReportname() {

		if (wrbAllgemein.isSelected()) {
			return StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_OHNEPREIS;
		} else if (wrbAllgemeinMitPreis.isSelected()) {
			return StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEIN_MITPREIS;
		} else {
			return StuecklisteReportFac.REPORT_STUECKLISTE_AUSGABESTUECKLSITE;
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfLager.setText(lagerDto.getCNr());
				lagerIId = lagerDto.getIId();
				wtfLager.setText(lagerDto.getCNr());
			} else if (e.getSource() == panelQueryFLRStueckliste) {
				Object[] keys = panelQueryFLRStueckliste.getSelectedIds();

				stuecklisteIId = new Integer[keys.length];

				String stuecklisten = "";

				for (int i = 0; i < keys.length; i++) {
					StuecklisteDto dto = DelegateFactory.getInstance()
							.getStuecklisteDelegate()
							.stuecklisteFindByPrimaryKey((Integer) keys[i]);
					stuecklisteIId[i] = (Integer) keys[i];

					stuecklisten += dto.getArtikelDto()
							.formatArtikelbezeichnung() + ", ";

				}

				wtStueckliste.setText(stuecklisten);
				if (panelQueryFLRStueckliste.getDialog() != null) {
					panelQueryFLRStueckliste.getDialog().setVisible(false);
				}

			}
		}

	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(internalFrameStueckliste, lagerIId);

		new DialogQuery(panelQueryFLRLager);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_STKL_FROM_LISTE)) {
			Integer selectedId = null;
			if (stuecklisteIId != null && stuecklisteIId.length > 0) {
				selectedId = (Integer) stuecklisteIId[0];
			}

			panelQueryFLRStueckliste = StuecklisteFilterFactory.getInstance()
					.createPanelFLRStueckliste(getInternalFrame(), selectedId,
							false);

			if (wcbNurAbbuchungslaeger.isEnabled()
					&& wcbNurAbbuchungslaeger.isSelected()) {

			} else {
				panelQueryFLRStueckliste.setMultipleRowSelectionEnabled(true);
			}

			panelQueryFLRStueckliste.addButtonAuswahlBestaetigen(null);

			new DialogQuery(panelQueryFLRStueckliste);
		} else {

			if (!e.getSource().equals(wcbUnterstuecklistenEinbinden)) {
				wcbPositionskommentar.setEnabled(false);
				wcbSortiertNachAenderungsdatum.setEnabled(false);
				wcbSortiertNachMontageart.setEnabled(false);
				wcbStuecklistenkommentar.setEnabled(false);
				wcoPreis.setEnabled(false);
				wnfLosgroesse.setEditable(false);
				wnfLosgroesse.setMandatoryField(false);

				if (wcbUnterstuecklistenEinbinden.isSelected()) {

					if (wrbAusgabestueckliste.isSelected()) {
						wrbUnterstklSortierungNachIdent.setEnabled(false);
						wcbUnterstklstrukturBelassen.setEnabled(false);
						wrbUnterstklSortierungPosition.setEnabled(false);
						wrbUnterstklSortierungWieErfasst.setEnabled(false);
					} else {

						wrbUnterstklSortierungNachIdent.setEnabled(true);
						wcbUnterstklstrukturBelassen.setEnabled(true);
						wrbUnterstklSortierungPosition.setEnabled(true);
						wrbUnterstklSortierungWieErfasst.setEnabled(true);
					}
				} else {
					wrbUnterstklSortierungNachIdent.setEnabled(false);
					wcbUnterstklstrukturBelassen.setEnabled(false);
					wrbUnterstklSortierungPosition.setEnabled(false);
					wrbUnterstklSortierungWieErfasst.setEnabled(false);
				}

			}

			if (wcbUnterstuecklistenEinbinden.isSelected() == false) {
				wrbUnterstklSortierungNachIdent.setEnabled(false);
				wcbUnterstklstrukturBelassen.setEnabled(false);
				wrbUnterstklSortierungPosition.setEnabled(false);
				wrbUnterstklSortierungWieErfasst.setEnabled(false);
			}

			// SP2600
			if (e.getSource().equals(wrbAllgemein)
					|| e.getSource().equals(wrbAllgemeinMitPreis)
					|| e.getSource().equals(wrbAusgabestueckliste)) {

				System.out.println("XX");

				if (getParent() instanceof JPanel) {
					JPanel jpa = (JPanel) getParent();

					if (jpa.getParent() instanceof JSplitPane) {
						JSplitPane jsp = (JSplitPane) jpa.getParent();
						if (jsp.getParent() instanceof JPanel) {
							JPanel jpaworkingon = (JPanel) jsp.getParent();
							if (jpaworkingon.getParent() instanceof PanelReportKriterien) {
								PanelReportKriterien panelReportKriterien = (PanelReportKriterien) jpaworkingon
										.getParent();

								wrbAllgemein.removeActionListener(this);
								wrbAllgemeinMitPreis.removeActionListener(this);
								wrbAusgabestueckliste
										.removeActionListener(this);

								panelReportKriterien.refreshVarianten();

								wrbAllgemein.addActionListener(this);
								wrbAllgemeinMitPreis.addActionListener(this);
								wrbAusgabestueckliste.addActionListener(this);

							}
						}

					}

				}
			}

			if (e.getSource().equals(wrbAllgemein)) {
				wcbGleichePositionenZusammenfassen.setEnabled(true);
				wcbPositionskommentar.setEnabled(true);
				wcbNurAbbuchungslaeger.setEnabled(false);
				wcbStuecklistenkommentar.setEnabled(true);

				wcoSortierung1.setMap(mSortierungAlle);
				wcoSortierung2.setMap(mSortierungAlle);
				wcoSortierung3.setMap(mSortierungAlle);

				wbuStueckliste.setEnabled(false);
			} else if (e.getSource().equals(wrbAllgemeinMitPreis)) {
				wcbGleichePositionenZusammenfassen.setEnabled(true);
				wcbPositionskommentar.setEnabled(true);
				wcbNurAbbuchungslaeger.setEnabled(false);
				wcbStuecklistenkommentar.setEnabled(true);
				wcoPreis.setEnabled(true);

				wcoSortierung1.setMap(mSortierungAlle);
				wcoSortierung2.setMap(mSortierungAlle);
				wcoSortierung3.setMap(mSortierungAlle);

				wbuStueckliste.setEnabled(false);

			} else if (e.getSource().equals(wrbAusgabestueckliste)) {
				wcbStuecklistenkommentar.setEnabled(true);
				wcbNurAbbuchungslaeger.setEnabled(true);
				wnfLosgroesse.setEditable(true);
				wnfLosgroesse.setMandatoryField(true);

				wcoSortierung1.setMap(mSortierungNurArtikelNrBez);
				wcoSortierung2.setMap(mSortierungNurArtikelNrBez);
				wcoSortierung3.setMap(mSortierungNurArtikelNrBez);

				wbuStueckliste.setEnabled(true);

			} else if (e.getSource().equals(wcbUnterstuecklistenEinbinden)) {
				if (wcbUnterstuecklistenEinbinden.isSelected()) {

					wrbUnterstklSortierungNachIdent.setEnabled(true);
					wcbUnterstklstrukturBelassen.setEnabled(true);
					wrbUnterstklSortierungPosition.setEnabled(true);
					wrbUnterstklSortierungWieErfasst.setEnabled(true);

					if (wrbAusgabestueckliste.isSelected()) {
						wrbUnterstklSortierungNachIdent.setEnabled(false);
						wcbUnterstklstrukturBelassen.setEnabled(false);
						wrbUnterstklSortierungPosition.setEnabled(false);
						wrbUnterstklSortierungWieErfasst.setEnabled(false);
						wcoSortierung1.setEnabled(true);
						wcoSortierung2.setEnabled(true);
						wcoSortierung3.setEnabled(true);
					} else {
						wcoSortierung1.setEnabled(false);
						wcoSortierung2.setEnabled(false);
						wcoSortierung3.setEnabled(false);

					}

				} else {
					wrbUnterstklSortierungNachIdent.setEnabled(false);
					wcbUnterstklstrukturBelassen.setEnabled(false);
					wrbUnterstklSortierungPosition.setEnabled(false);
					wrbUnterstklSortierungWieErfasst.setEnabled(false);

					wcoSortierung1.setEnabled(true);
					wcoSortierung2.setEnabled(true);
					wcoSortierung3.setEnabled(true);

				}

			}

			if (wcbNurAbbuchungslaeger.isEnabled()
					&& wcbNurAbbuchungslaeger.isSelected()) {
				wbuLager.setEnabled(false);
			} else {
				wbuLager.setEnabled(true);
			}

		}
	}

	private int getSortierungDerSteuckliste(Integer iOption,
			boolean bDruckMitPreis) {
		int iSortierung = -1;

		if (bDruckMitPreis == true) {
			if (iOption == SORT_ARTIKELNUMMER) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL;
			} else if (iOption == SORT_KOMMENTAR) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR;
			} else if (iOption == SORT_LFDNUMMER) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_LFDNUMMER;
			} else if (iOption == SORT_MONTAGEART) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MONTAGEART;
			} else if (iOption == SORT_POSITION) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_POSITION;
			} else if (iOption == SORT_WIE_ERFASST) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_SORT;
			} else if (iOption == SORT_ARTIKELBEZEICHNUNG) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELBEZEICHNUNG;
			}
		} else {
			if (iOption == SORT_ARTIKELNUMMER) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL;
			} else if (iOption == SORT_KOMMENTAR) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSKOMMENTAR;
			} else if (iOption == SORT_LFDNUMMER) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_LFDNUMMER;
			} else if (iOption == SORT_MONTAGEART) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MONTAGEART;
			} else if (iOption == SORT_POSITION) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION;
			} else if (iOption == SORT_WIE_ERFASST) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_SORT;
			} else if (iOption == SORT_ARTIKELBEZEICHNUNG) {
				iSortierung = StuecklisteReportFac.REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELBEZEICHNUNG;
			}
		}
		return iSortierung;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		// Sortierung1
		int iSortierung1 = -1;
		int iSortierung2 = -1;
		int iSortierung3 = -1;

		if (wrbAllgemein.isSelected()) {
			iSortierung1 = getSortierungDerSteuckliste(
					(Integer) wcoSortierung1.getKeyOfSelectedItem(), false);
			iSortierung2 = getSortierungDerSteuckliste(
					(Integer) wcoSortierung2.getKeyOfSelectedItem(), false);
			iSortierung3 = getSortierungDerSteuckliste(
					(Integer) wcoSortierung3.getKeyOfSelectedItem(), false);

		} else if (wrbAllgemeinMitPreis.isSelected()) {
			iSortierung1 = getSortierungDerSteuckliste(
					(Integer) wcoSortierung1.getKeyOfSelectedItem(), true);
			iSortierung2 = getSortierungDerSteuckliste(
					(Integer) wcoSortierung2.getKeyOfSelectedItem(), true);
			iSortierung3 = getSortierungDerSteuckliste(
					(Integer) wcoSortierung3.getKeyOfSelectedItem(), true);
		}

		int iSortierungUnterstueckliste = -1;

		if (wcbUnterstuecklistenEinbinden.isSelected()) {
			if (wrbUnterstklSortierungNachIdent.isSelected()) {
				iSortierungUnterstueckliste = StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR;
			} else if (wrbUnterstklSortierungPosition.isSelected()) {
				iSortierungUnterstueckliste = StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION;
			} else if (wrbUnterstklSortierungWieErfasst.isSelected()) {
				iSortierungUnterstueckliste = StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE;
			}
		}

		try {
			if (wrbAllgemein.isSelected()) {

				return DelegateFactory
						.getInstance()
						.getStuecklisteReportDelegate()
						.printStuecklisteAllgemein(
								stuecklisteIId[0],
								Helper.short2boolean(wcbPositionskommentar
										.getShort()),
								Helper.short2boolean(wcbStuecklistenkommentar
										.getShort()),
								Helper.short2boolean(wcbUnterstuecklistenEinbinden
										.getShort()),
								Helper.short2boolean(wcbGleichePositionenZusammenfassen
										.getShort()),
								iSortierungUnterstueckliste,
								wcbUnterstklstrukturBelassen.isSelected(),
								iSortierung1, iSortierung2, iSortierung3);
			} else if (wrbAllgemeinMitPreis.isSelected()) {

				return DelegateFactory
						.getInstance()
						.getStuecklisteReportDelegate()
						.printStuecklisteAllgemeinMitPreis(
								stuecklisteIId[0],
								((Integer) wcoPreis.getKeyOfSelectedItem())
										.intValue(),
								Helper.short2boolean(wcbPositionskommentar
										.getShort()),
								Helper.short2boolean(wcbStuecklistenkommentar
										.getShort()),
								Helper.short2boolean(wcbUnterstuecklistenEinbinden
										.getShort()),
								Helper.short2boolean(wcbGleichePositionenZusammenfassen
										.getShort()),
								iSortierungUnterstueckliste,
								wcbUnterstklstrukturBelassen.isSelected(),
								iSortierung1, iSortierung2, iSortierung3);
			} else if (wrbAusgabestueckliste.isSelected()) {

				boolean bSortiertNachArtikelbezeichnung = false;

				Integer iSort = (Integer) wcoSortierung1.getKeyOfSelectedItem();
				if (iSort == SORT_ARTIKELBEZEICHNUNG) {
					bSortiertNachArtikelbezeichnung = true;
				}

				
				if(wcbNurAbbuchungslaeger.isSelected()){
					Integer stuecklisteIIdTemp=stuecklisteIId[0];
					stuecklisteIId=new Integer[]{stuecklisteIIdTemp};
				}
				
				return DelegateFactory
						.getInstance()
						.getStuecklisteReportDelegate()
						.printAusgabestueckliste(
								stuecklisteIId,
								lagerIId,
								Helper.short2boolean(wcbStuecklistenkommentar
										.getShort()),
								Helper.short2boolean(wcbUnterstuecklistenEinbinden
										.getShort()),
								Helper.short2boolean(wcbGleichePositionenZusammenfassen
										.getShort()),
								wnfLosgroesse.getBigDecimal(),
								bSortiertNachArtikelbezeichnung,wcbNurAbbuchungslaeger.isSelected());
			} else {
				return null;
			}
		} catch (ExceptionLP ex) {
			if (ex.getICode() == com.lp.util.EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN) {
				DialogFactory.showModalDialog("Fehler",
						"Losgr\u00F6sse zu klein");
			}

			else {
				// brauche ich
				handleException(ex, false);
			}

			return null;
		}
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
