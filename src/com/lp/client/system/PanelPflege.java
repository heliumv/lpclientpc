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
package com.lp.client.system;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FibuFehlerDto;
import com.lp.util.Helper;

public class PanelPflege extends PanelBasis {

	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private PanelQueryFLR panelQueryFLRPreislisten = null;

	static final public String ACTION_PRUEFE_RECHNUNGSWERT = "ACTION_PRUEFE_RECHNUNGSWERT";
	static final public String ACTION_RABATTSATZ_NACHTRAGEN = "ACTION_RABATTSATZ_NACHTRAGEN";

	private static final String MENU_PFLEGE_BESTELLSTATI_PRUEFEN = "MENU_PFLEGE_BESTELLSTATI_PRUEFEN";
	private static final String MENU_PFLEGE_BESTELLPOSITION_PRUEFEN = "MENU_PFLEGE_BESTELLPOSITION_PRUEFEN";

	private final static String MENUE_ACTION_PFLEGE_PREISENEUBERECHNEN = "MENUE_ACTION_ER_PREISENEUBERECHNEN";

	private final static String MENUE_ACTION_PFLEGE_FEHLMENGEN_PRUEFEN = "menu_action_fehlmengen_pruefen";
	private final static String MENUE_ACTION_STUECKLISTE_NACHKALKULIEREN = "menu_action_stuecklisten_nachkalkulieren";

	private final String MENUE_PFLEGE_LAGERABGANGURSPRUNG = "MENUE_PFLEGE_LAGERABGANGURSPRUNG";
	private final String MENUE_PFLEGE_PRUEFE_VERBRAUCHTEMENGEN = "MENUE_PFLEGE_PRUEFE_VERBRAUCHTEMENGEN";
	private final String MENUE_PFLEGE_LAGERSTAENDE = "MENUE_PFLEGE_LAGERSTAENDE";
	private final String MENUE_PFLEGE_BELEGE_MIT_LAGERBEWEGUNGEN = "MENUE_PFLEGE_BELEGE_MIT_LAGERBEWEGUNGEN";
	private final String MENUE_PFLEGE_LAGERBEWEGUNGEN_MIT_BELEGE = "MENUE_PFLEGE_LAGERBEWEGUNGEN_MIT_BELEGE";
	private final String MENUE_PFLEGE_PRUEFE_VOLLSTAENDIG_VERBRAUCHT = "MENUE_PFLEGE_PRUEFE_VOLLSTAENDIG_VERBRAUCHT";
	private final String MENUE_PFLEGE_PRUEFE_RESERVIERUNGEN = "MENUE_PFLEGE_PRUEFE_RESERVIERUNGEN";
	private final String MENUE_PFLEGE_PRUEFE_BESTELLTLISTE = "MENUE_PFLEGE_PRUEFE_BESTELLTLISTE";
	private final String MENUE_PFLEGE_PRUEFE_RAHMENBEDARFE = "MENUE_PFLEGE_PRUEFE_RAHMENBEDARFE";
	private final String MENUE_PFLEGE_PRUEFE_AUFTRAGSERIENNUMMERN = "MENUE_PFLEGE_PRUEFE_AUFTRAGSERIENNUMMERN";
	private final String MENUE_PFLEGE_PRUEFE_VKPFSTAFFELMENGE = "MENUE_PFLEGE_PRUEFE_VKPFSTAFFELMENGE";
	private final String MENUE_PFLEGE_PRUEFE_VKPREISLAGERBEWEGUNG = "MENUE_PFLEGE_PRUEFE_VKPREISLAGERBEWEGUNG";
	private final String MENUE_PFLEGE_PRUEFE_I_ID_BUCHUNG = "MENUE_PFLEGE_PRUEFE_I_ID_BUCHUNG";
	private final String MENUE_PFLEGE_RELOAD_LP_TEXT = "MENUE_PFLEGE_RELOAD_LP_TEXT";
	private final String MENUE_PFLEGE_LAGERBEW_NACHTRAGEN = "MENUE_PFLEGE_LAGERBEW_NACHTRAGEN";
	private final String MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_LS_RE = "MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_LS_RE";
	private final String MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_LOS = "MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_LOS";
	private final String MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_BEST = "MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_BEST";
	private final String MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_HAND = "MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_HAND";
	private final String MENUE_PFLEGE_SI_WERTE_NACHTRAGEN = "MENUE_PFLEGE_SI_WERTE_NACHTRAGEN";
	private final String MENUE_PFLEGE_DIGIRASTER = "MENUE_PFLEGE_DIGIRASTER";
	private final String MENUE_PFLEGE_LOSE_IM_ZEITRAUM_NACHKALKULIEREN = "LOSE_IM_ZEITRAUM_NACHKALKULIEREN";
	
	private final String MENUE_PFLEGE_EKPREISE_IM_ZEITRAUM_RUECKPFLEGEN = "EKPREISE_IM_ZEITRAUM_RUECKPFLEGEN";
	

	public PanelPflege(InternalFrame internalFrame, String add2TitleI)
			throws Throwable {
		super(internalFrame, add2TitleI, null);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRPreislisten) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				java.sql.Date datum = DialogFactory.showDatumseingabe();
				if (datum != null) {
					DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.pflegeRabattsaetzeNachpflegen(iId,
									Helper.cutDate(datum));
					DialogFactory.showModalDialog("Hinweis",
							"Rabatts\u00E4tze nachgetragen.");
				}

			}
		}
	}

	private void deaktiviereWennNichtLPAdmin(JButton button) {
		if (!LPMain.getInstance().isLPAdmin()) {
			button.setEnabled(false);
		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);
		// jetzt meine felder
		jpaWorkingOn = new JPanel(new MigLayout("wrap 3",
				"[fill,60%|fill,20%|fill,20%]",
				"[fill,33%][fill,33%][fill,33%]"));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 1, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		JPanel artikel = new JPanel(new MigLayout("wrap 2",
				"[fill,50%|fill,50%]", "[fill]"));
		artikel.setBorder(BorderFactory.createTitledBorder("Artikel"));
		jpaWorkingOn.add(artikel, "span 1 3");

		JPanel allgemein = new JPanel(new MigLayout("wrap 1", "[fill,100%]"));
		allgemein.setBorder(BorderFactory.createTitledBorder("Allgemein"));
		jpaWorkingOn.add(allgemein);
		JPanel best = new JPanel(new MigLayout("wrap 1", "[fill,100%]"));
		best.setBorder(BorderFactory.createTitledBorder("Bestellung"));
		jpaWorkingOn.add(best);

		JPanel rechnung = new JPanel(new MigLayout("wrap 1", "[fill,100%]"));
		rechnung.setBorder(BorderFactory.createTitledBorder("Rechnung"));
		jpaWorkingOn.add(rechnung);
		JPanel eingangsrechnung = new JPanel(new MigLayout("wrap 1",
				"[fill,100%]"));
		eingangsrechnung.setBorder(BorderFactory
				.createTitledBorder("Eingangsrechnung"));
		jpaWorkingOn.add(eingangsrechnung);

		JPanel fertigung = new JPanel(new MigLayout("wrap 1", "[fill,100%]"));
		fertigung.setBorder(BorderFactory.createTitledBorder("Fertigung"));
		jpaWorkingOn.add(fertigung);

		JPanel hinweis = new JPanel(new MigLayout("wrap 1"));
		hinweis.setBorder(BorderFactory.createTitledBorder("Hinweis"));
		jpaWorkingOn.add(hinweis);

		JButton btnPruefeRechnungswert = new AutoWrapButton(
				"Pr\u00FCfe Rechnungswert");
		btnPruefeRechnungswert.setActionCommand(ACTION_PRUEFE_RECHNUNGSWERT);
		btnPruefeRechnungswert.addActionListener(this);
		deaktiviereWennNichtLPAdmin(btnPruefeRechnungswert);
		rechnung.add(btnPruefeRechnungswert);

		JButton btnRabattsaetzeNachtragen = new AutoWrapButton(
				"Rabatts\u00E4tze einer Preisliste nachtragen");
		btnRabattsaetzeNachtragen
				.setActionCommand(ACTION_RABATTSATZ_NACHTRAGEN);
		btnRabattsaetzeNachtragen.addActionListener(this);
		artikel.add(btnRabattsaetzeNachtragen);
		deaktiviereWennNichtLPAdmin(btnRabattsaetzeNachtragen);

		JButton btnPflegeBestellStati = new AutoWrapButton(
				LPMain.getTextRespectUISPr("bes.menu.pflege.bestellstati"));
		btnPflegeBestellStati
				.setActionCommand(MENU_PFLEGE_BESTELLSTATI_PRUEFEN);
		btnPflegeBestellStati.addActionListener(this);
		best.add(btnPflegeBestellStati);
		deaktiviereWennNichtLPAdmin(btnPflegeBestellStati);

		JButton btnPflegeBestellposStati = new AutoWrapButton(
				LPMain.getTextRespectUISPr("bes.menu.pflege.bestellposstati"));
		btnPflegeBestellposStati
				.setActionCommand(MENU_PFLEGE_BESTELLPOSITION_PRUEFEN);
		btnPflegeBestellposStati.addActionListener(this);
		best.add(btnPflegeBestellposStati);
		deaktiviereWennNichtLPAdmin(btnPflegeBestellposStati);

		JButton btnERPReispflege = new AutoWrapButton(
				LPMain.getTextRespectUISPr("er.pflege.preiseneuberechnen"));
		btnERPReispflege
				.setActionCommand(MENUE_ACTION_PFLEGE_PREISENEUBERECHNEN);
		btnERPReispflege.addActionListener(this);
		eingangsrechnung.add(btnERPReispflege);

		// SP1902
		// deaktiviereWennNichtLPAdmin(btnERPReispflege);

		JButton btnPflegeFehlmengen = new AutoWrapButton(
				LPMain.getTextRespectUISPr("fert.menu.pruefefehlmengen"));
		btnPflegeFehlmengen
				.setActionCommand(MENUE_ACTION_PFLEGE_FEHLMENGEN_PRUEFEN);
		btnPflegeFehlmengen.addActionListener(this);
		fertigung.add(btnPflegeFehlmengen);

		JButton btnPflegeNachkalkulatnio = new AutoWrapButton(
				"<html> Lose einer St\u00FCckliste <br> nachkalkulieren </html>");
		btnPflegeNachkalkulatnio
				.setActionCommand(MENUE_ACTION_STUECKLISTE_NACHKALKULIEREN);
		btnPflegeNachkalkulatnio.addActionListener(this);
		fertigung.add(btnPflegeNachkalkulatnio);
		deaktiviereWennNichtLPAdmin(btnPflegeNachkalkulatnio);

		JButton btnPflegeLoseNachkalkulieren = new AutoWrapButton(
				LPMain.getTextRespectUISPr("fert.pflege.loseimzeitraum.nachkalkulieren"));
		btnPflegeLoseNachkalkulieren
				.setActionCommand(MENUE_PFLEGE_LOSE_IM_ZEITRAUM_NACHKALKULIEREN);
		btnPflegeLoseNachkalkulieren.addActionListener(this);
		fertigung.add(btnPflegeLoseNachkalkulieren);

		JButton btnPflegeLagerabgangursprung = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.pruefelagerabgangursprung"));
		btnPflegeLagerabgangursprung
				.setActionCommand(MENUE_PFLEGE_LAGERABGANGURSPRUNG);
		btnPflegeLagerabgangursprung.addActionListener(this);
		artikel.add(btnPflegeLagerabgangursprung);
		deaktiviereWennNichtLPAdmin(btnPflegeLagerabgangursprung);

		JButton btnPflegeLagerstaende = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.pruefelagerstaende"));
		btnPflegeLagerstaende.setActionCommand(MENUE_PFLEGE_LAGERSTAENDE);
		btnPflegeLagerstaende.addActionListener(this);
		artikel.add(btnPflegeLagerstaende);
		deaktiviereWennNichtLPAdmin(btnPflegeLagerstaende);

		JButton btnPflegeBelege = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.pruefebelegegegenlagerbewegungenaufkonsistenz"));
		btnPflegeBelege
				.setActionCommand(MENUE_PFLEGE_BELEGE_MIT_LAGERBEWEGUNGEN);
		btnPflegeBelege.addActionListener(this);
		artikel.add(btnPflegeBelege);
		deaktiviereWennNichtLPAdmin(btnPflegeBelege);

		JButton btnPflegeBelege2 = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.pruefelagerbewegungengegenbelegpositionenaufkonsistenz"));
		btnPflegeBelege2
				.setActionCommand(MENUE_PFLEGE_LAGERBEWEGUNGEN_MIT_BELEGE);
		btnPflegeBelege2.addActionListener(this);
		artikel.add(btnPflegeBelege2);
		deaktiviereWennNichtLPAdmin(btnPflegeBelege2);

		JButton btnPflegeVerbraucht = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.pruefevollstaendigverbraucht"));
		btnPflegeVerbraucht
				.setActionCommand(MENUE_PFLEGE_PRUEFE_VOLLSTAENDIG_VERBRAUCHT);
		btnPflegeVerbraucht.addActionListener(this);
		artikel.add(btnPflegeVerbraucht);
		deaktiviereWennNichtLPAdmin(btnPflegeVerbraucht);

		JButton btnPruefeReservierungen = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.pruefereservierungen"));
		btnPruefeReservierungen
				.setActionCommand(MENUE_PFLEGE_PRUEFE_RESERVIERUNGEN);
		btnPruefeReservierungen.addActionListener(this);
		artikel.add(btnPruefeReservierungen);

		JButton btnPruefeBestelltliste = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.pruefebestelltliste"));
		btnPruefeBestelltliste
				.setActionCommand(MENUE_PFLEGE_PRUEFE_BESTELLTLISTE);
		btnPruefeBestelltliste.addActionListener(this);
		artikel.add(btnPruefeBestelltliste);

		JButton btnPruefeRahmenbedarfe = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.prueferahmenbedarfe"));
		btnPruefeRahmenbedarfe
				.setActionCommand(MENUE_PFLEGE_PRUEFE_RAHMENBEDARFE);
		btnPruefeRahmenbedarfe.addActionListener(this);
		artikel.add(btnPruefeRahmenbedarfe);

		JButton btnPruefeAuftragseriennummern = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.auftragseriennummern"));
		btnPruefeAuftragseriennummern
				.setActionCommand(MENUE_PFLEGE_PRUEFE_AUFTRAGSERIENNUMMERN);
		btnPruefeAuftragseriennummern.addActionListener(this);
		artikel.add(btnPruefeAuftragseriennummern);
		deaktiviereWennNichtLPAdmin(btnPruefeAuftragseriennummern);

		JButton btnPruefeVkpfms = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.report.pruefevkpfstaffelmenge"));
		btnPruefeVkpfms.setActionCommand(MENUE_PFLEGE_PRUEFE_VKPFSTAFFELMENGE);
		btnPruefeVkpfms.addActionListener(this);
		artikel.add(btnPruefeVkpfms);
		deaktiviereWennNichtLPAdmin(btnPruefeVkpfms);

		JButton btnPruefeVkpreislb = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.pflege.pruefevkpreislagerbewegung"));
		btnPruefeVkpreislb
				.setActionCommand(MENUE_PFLEGE_PRUEFE_VKPREISLAGERBEWEGUNG);
		btnPruefeVkpreislb.addActionListener(this);
		artikel.add(btnPruefeVkpreislb);
		deaktiviereWennNichtLPAdmin(btnPruefeVkpreislb);

		JButton btnPruefeVerbrauchteMengen = new AutoWrapButton(
				LPMain.getTextRespectUISPr("artikel.pflege.pruefeverkauftemengen"));
		btnPruefeVerbrauchteMengen
				.setActionCommand(MENUE_PFLEGE_PRUEFE_VERBRAUCHTEMENGEN);
		btnPruefeVerbrauchteMengen.addActionListener(this);
		artikel.add(btnPruefeVerbrauchteMengen);
		deaktiviereWennNichtLPAdmin(btnPruefeVerbrauchteMengen);

		JButton btnPruefeIIdBuchung = new AutoWrapButton(
				"Pr\u00FCfe I_ID_BUCHUNG");
		btnPruefeIIdBuchung.setActionCommand(MENUE_PFLEGE_PRUEFE_I_ID_BUCHUNG);
		btnPruefeIIdBuchung.addActionListener(this);
		artikel.add(btnPruefeIIdBuchung);
		deaktiviereWennNichtLPAdmin(btnPruefeIIdBuchung);

		JButton btnLagerbewNachtragen = new AutoWrapButton(
				"Lagerbewegungen ab. Datum nachtragen");
		btnLagerbewNachtragen
				.setActionCommand(MENUE_PFLEGE_LAGERBEW_NACHTRAGEN);
		btnLagerbewNachtragen.addActionListener(this);
		artikel.add(btnLagerbewNachtragen);
		deaktiviereWennNichtLPAdmin(btnLagerbewNachtragen);

		JButton btnLagerbewKonstruierenausHAND = new AutoWrapButton(
				"Lagerbewegungen aus HAND nachtragen");
		btnLagerbewKonstruierenausHAND
				.setActionCommand(MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_HAND);
		btnLagerbewKonstruierenausHAND.addActionListener(this);
		artikel.add(btnLagerbewKonstruierenausHAND);
		deaktiviereWennNichtLPAdmin(btnLagerbewKonstruierenausHAND);
		
		JButton btnLagerbewKonstruierenausLSRE = new AutoWrapButton(
				"Lagerbewegungen aus LS/RE nachtragen");
		btnLagerbewKonstruierenausLSRE
				.setActionCommand(MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_LS_RE);
		btnLagerbewKonstruierenausLSRE.addActionListener(this);
		artikel.add(btnLagerbewKonstruierenausLSRE);
		deaktiviereWennNichtLPAdmin(btnLagerbewKonstruierenausLSRE);

		JButton btnLagerbewKonstruierenausLOS = new AutoWrapButton(
				"Lagerbewegungen aus LOS nachtragen");
		btnLagerbewKonstruierenausLOS
				.setActionCommand(MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_LOS);
		btnLagerbewKonstruierenausLOS.addActionListener(this);
		artikel.add(btnLagerbewKonstruierenausLOS);
		deaktiviereWennNichtLPAdmin(btnLagerbewKonstruierenausLOS);

		JButton btnLagerbewKonstruierenausBEST = new AutoWrapButton(
				"Lagerbewegungen aus BESTELLUNG nachtragen");
		btnLagerbewKonstruierenausBEST
				.setActionCommand(MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_BEST);
		btnLagerbewKonstruierenausBEST.addActionListener(this);
		artikel.add(btnLagerbewKonstruierenausBEST);
		deaktiviereWennNichtLPAdmin(btnLagerbewKonstruierenausBEST);

		

		JButton btnSiWerteNachtragen = new AutoWrapButton("SI-Werte nachtragen");
		btnSiWerteNachtragen.setActionCommand(MENUE_PFLEGE_SI_WERTE_NACHTRAGEN);
		btnSiWerteNachtragen.addActionListener(this);
		artikel.add(btnSiWerteNachtragen);
		
		
		JButton btngelifertPreiseNachtragen = new AutoWrapButton("Geliefert-Preis aus WEP als EK-Preis zurückpflegen");
		btngelifertPreiseNachtragen.setActionCommand(MENUE_PFLEGE_EKPREISE_IM_ZEITRAUM_RUECKPFLEGEN);
		btngelifertPreiseNachtragen.addActionListener(this);
		best.add(btngelifertPreiseNachtragen);

		/*
		 * JButton btnDigiraster = new AutoWrapButton( "DIGIRASTER");
		 * btnDigiraster .setActionCommand(MENUE_PFLEGE_DIGIRASTER);
		 * btnDigiraster.addActionListener(this); artikel.add(btnDigiraster);
		 */

		JButton btnReloadLPTEXT = new AutoWrapButton("LP_TEXT neu laden");
		btnReloadLPTEXT.setActionCommand(MENUE_PFLEGE_RELOAD_LP_TEXT);
		btnReloadLPTEXT.addActionListener(this);
		allgemein.add(btnReloadLPTEXT);

		// Hinweis

		JLabel achtung = new JLabel(
				"<html><font size=\"6\" color=\"#FF0000\">!!!!!!!!!!!!!!!!!</font></html>");

		JLabel lblHinweis1 = new JLabel(
				LPMain.getTextRespectUISPr("lp.pflege.hinweis"));
		lblHinweis1.setForeground(Color.RED);

		JLabel lblHinweis2 = new JLabel(
				LPMain.getTextRespectUISPr("lp.pflege.hinweis1"));
		lblHinweis2.setForeground(Color.RED);
		JLabel lblHinweis3 = new JLabel(
				LPMain.getTextRespectUISPr("lp.pflege.hinweis2"));
		lblHinweis3.setForeground(Color.RED);
		JLabel lblHinweis4 = new JLabel(
				LPMain.getTextRespectUISPr("lp.pflege.hinweis3"));
		lblHinweis4.setForeground(Color.RED);

		hinweis.add(achtung);
		hinweis.add(lblHinweis1);
		hinweis.add(lblHinweis2);
		hinweis.add(lblHinweis3);
		hinweis.add(lblHinweis4);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MATERIAL;
	}

	protected void components2Dto() {

	}

	private File getLogFile(String baseFilename) {
		return new File("log", baseFilename);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_PRUEFE_RECHNUNGSWERT)) {
			DelegateFactory.getInstance().getRechnungDelegate()
					.pruefeRechnungswert();

			DialogFactory
					.showModalDialog(
							"Hinweis",
							"Pr\u00FCfung durchgef\u00FChrt! Sie finden das Pr\u00FCfergebnis in der Tabelle LP_PROTOKOLL");

		} else if (e.getActionCommand().equals(ACTION_RABATTSATZ_NACHTRAGEN)) {

			panelQueryFLRPreislisten = ArtikelFilterFactory.getInstance()
					.createPanelFLRPreisliste(getInternalFrame(), null);
			new DialogQuery(panelQueryFLRPreislisten);

		} else if (e.getActionCommand().equals(MENUE_PFLEGE_RELOAD_LP_TEXT)) {
			DelegateFactory.getInstance().getBenutzerServicesDelegate()
					.reloadUebersteuertenText();
			Defaults.getInstance().reloadSpezifischeTexte();
		} else if (e.getActionCommand().equals(
				MENU_PFLEGE_BESTELLPOSITION_PRUEFEN)) {
			boolean doIt = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("bes.pflege.bestellstatigeprueft"));
			if (doIt) {
				String sForFile = DelegateFactory.getInstance()
						.getBestellungDelegate().checkBestellpositionStati();
				File file = new File("Bestellpositionstati_pruefen.log");
				if (!file.exists()) {
					file.createNewFile();
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(sForFile);
				bw.close();
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hinweis"),
						LPMain.getTextRespectUISPr("lp.log.gespeichert") + " "
								+ file.getAbsolutePath());
			}
		} else if (e.getActionCommand()
				.equals(MENUE_PFLEGE_LAGERABGANGURSPRUNG)) {
			String s = DelegateFactory.getInstance().getLagerDelegate()
					.pruefeLagerabgangurpsrung();

			// java.io.File ausgabedatei = new
			// java.io.File("c:/lagerabgangursprung.txt");
			java.io.File ausgabedatei = getLogFile("lagerabgangursprung.txt");
			java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
			bw.write(s);
			bw.close();

		} else if (e.getActionCommand().equals(MENUE_PFLEGE_LAGERSTAENDE)) {

			String artikelId = (String) JOptionPane.showInputDialog(this,
					"Bitte artikelIId angeben: ", "Eingabe",
					JOptionPane.PLAIN_MESSAGE);

			Integer artikelIID = null;

			if (artikelId != null && artikelId.length() > 0) {
				artikelIID = new Integer(artikelId);
			}

			String s = DelegateFactory.getInstance().getLagerDelegate()
					.pruefeQuickLagerstandGegenEchtenLagerstand(artikelIID);

			// java.io.File ausgabedatei = new
			// java.io.File("c:/lagerstaende.txt");
			java.io.File ausgabedatei = getLogFile("lagerstaende.txt");
			java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
			bw.write(s);
			bw.close();

		} else if (e.getActionCommand()
				.equals(MENUE_PFLEGE_PRUEFE_I_ID_BUCHUNG)) {

			String artikelId = (String) JOptionPane.showInputDialog(this,
					"Bitte artikelIId angeben: ", "Eingabe",
					JOptionPane.PLAIN_MESSAGE);

			Integer artikelIID = null;

			if (artikelId != null && artikelId.length() > 0) {
				artikelIID = new Integer(artikelId);
			}

			String s = DelegateFactory.getInstance().getLagerDelegate()
					.pruefeIIdBuchungen(artikelIID);

			// java.io.File ausgabedatei = new
			// java.io.File("c:/i_id_buchung.txt");
			java.io.File ausgabedatei = getLogFile("i_id_buchung.txt");
			java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
			bw.write(s);
			bw.close();

		} else if (e.getActionCommand()
				.equals(MENUE_PFLEGE_LAGERBEW_NACHTRAGEN)) {

			java.sql.Date datum = DialogFactory
					.showDatumseingabe("Ab welchem Datum:");
			if (datum != null) {

				String s = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.fehlendeAbbuchungenNachtragen(
								new java.sql.Timestamp(datum.getTime()));

				java.io.File ausgabedatei = getLogFile("laberbew_nachtragen.txt");
				java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
				java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
				bw.write(s);
				bw.close();
			}

		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_LS_RE)) {

			DelegateFactory.getInstance().getLagerDelegate()
					.konstruiereLagergewegungenLSREAusBelegen();

		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_LOS)) {

			DelegateFactory.getInstance().getLagerDelegate()
					.konstruiereLagergewegungenLOSAusBelegen();

		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_BEST)) {

			DelegateFactory.getInstance().getLagerDelegate()
					.konstruiereLagergewegungenBESTAusBelegen();

		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_KONSTRUIERE_LAGERBEW_HAND)) {

			DelegateFactory.getInstance().getLagerDelegate()
					.konstruiereLagergewegungenHAND();

		} else if (e.getActionCommand()
				.equals(MENUE_PFLEGE_SI_WERTE_NACHTRAGEN)) {

			DelegateFactory.getInstance().getArtikelDelegate()
					.alleSIwerteNachtragen();

		} else if (e.getActionCommand().equals(MENUE_PFLEGE_DIGIRASTER)) {

			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_XLS, false);
			if (files == null || files.length < 1 || files[0] == null) {
				return;
			}
			File f = files[0];

			ByteArrayOutputStream ous = null;
			InputStream ios = null;
			try {
				byte[] buffer = new byte[4096];
				ous = new ByteArrayOutputStream();
				ios = new FileInputStream(f);
				int read = 0;
				while ((read = ios.read(buffer)) != -1) {
					ous.write(buffer, 0, read);
				}
			} finally {
				try {
					if (ous != null)
						ous.close();
				} catch (IOException ex) {
				}

				try {
					if (ios != null)
						ios.close();
				} catch (IOException ex) {
				}
			}

			DelegateFactory.getInstance().getArtikelDelegate()
					.importiereDigiraster(ous.toByteArray());

		} else if (e.getActionCommand()
				.equals(MENU_PFLEGE_BESTELLSTATI_PRUEFEN)) {
			String sForFile = DelegateFactory.getInstance()
					.getBestellungDelegate().checkBestellStati();
			File file = new File("Bestellstati_pruefen.log");
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.append(sForFile);
			bw.close();
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hinweis"),
					LPMain.getTextRespectUISPr("lp.log.gespeichert") + " "
							+ file.getAbsolutePath());
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_PFLEGE_PREISENEUBERECHNEN)) {
			/*
			 * DelegateFactory.getInstance().getEingangsrechnungDelegate()
			 * .pruefePreise();
			 */
			Map<?, ?> geschaeftsjahre = DelegateFactory.getInstance()
					.getSystemDelegate().getAllGeschaeftsjahr();
			ArrayList<Object> optionen = DialogFactory
					.showBelegKurspruefungOptionen(geschaeftsjahre);
			Integer geschaeftsjahr = (Integer) optionen.get(0);
			if (geschaeftsjahr != null) {
				Boolean nurPruefen = (Boolean) optionen.get(1);
				ArrayList<FibuFehlerDto> fibufehler = DelegateFactory
						.getInstance().getFinanzServiceDelegate()
						.pruefeBelegeKurs(geschaeftsjahr, nurPruefen);
				if (fibufehler != null)
					if (fibufehler.size() > 0)
						DialogFactory.showBelegPruefergebnis(
								getInternalFrame(), fibufehler, "Kursfehler");
					else if (nurPruefen)
						JOptionPane.showMessageDialog(getInternalFrame(),
								"Kurspr\u00FCfung ohne Fehler abgeschlossen.");
					else
						JOptionPane
								.showMessageDialog(getInternalFrame(),
										"Kurskorrektur abgeschlossen. Korrekturen entnehmen Sie dem Server-Logfile.");
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_PFLEGE_FEHLMENGEN_PRUEFEN)) {
			DelegateFactory.getInstance().getFehlmengeDelegate()
					.pruefeFehlmengen();
		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_LOSE_IM_ZEITRAUM_NACHKALKULIEREN)) {
			java.sql.Date[] datum = DialogFactory
					.showDatumseingabeVonBis(LPMain
							.getTextRespectUISPr("fert.pflege.loseimzeitraum.nachkalkulieren.bereich"));

			if (datum[0] != null && datum[1] != null) {

				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.erledigteLoseImZeitraumNachkalkulieren(datum[0],
								datum[1]);
			}

		}  else if (e.getActionCommand().equals(
				MENUE_PFLEGE_EKPREISE_IM_ZEITRAUM_RUECKPFLEGEN)) {
			java.sql.Date[] datum = DialogFactory
					.showDatumseingabeVonBis(LPMain
							.getTextRespectUISPr("fert.pflege.ekpreisezurueckpflegen.bereich"));

			if (datum[0] != null && datum[1] != null) {

				DelegateFactory
						.getInstance()
						.getWareneingangDelegate()
						.geliefertPreiseAllerWEPRueckpflegen(datum[0],
								datum[1]);
			}

		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_BELEGE_MIT_LAGERBEWEGUNGEN)) {

			String s = DelegateFactory.getInstance().getLagerDelegate()
					.pruefeBelegeMitLagerbewegungen();

			// java.io.File ausgabedatei = new
			// java.io.File("c:/lagerbewegungbelege.txt");
			java.io.File ausgabedatei = getLogFile("lagerbewegungbelege.txt");
			java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
			bw.write(s);
			bw.close();

		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_LAGERBEWEGUNGEN_MIT_BELEGE)) {
			String s = DelegateFactory.getInstance().getLagerDelegate()
					.pruefeLagerbewegungenMitBelege();

			// java.io.File ausgabedatei = new
			// java.io.File("c:/belegelagerbewegung.txt");
			java.io.File ausgabedatei = getLogFile("belegelagerbewegung.txt");
			java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
			bw.write(s);
			bw.close();

		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_PRUEFE_VOLLSTAENDIG_VERBRAUCHT)) {

			String artikelId = (String) JOptionPane.showInputDialog(this,
					"Bitte artikelIId angeben: ", "Eingabe",
					JOptionPane.PLAIN_MESSAGE);

			Integer artikelIID = null;

			if (artikelId != null && artikelId.length() > 0) {
				artikelIID = new Integer(artikelId);
			}

			boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					"Sollen die Fehler korrigiert werden?");

			String s = DelegateFactory.getInstance().getLagerDelegate()
					.pruefeVollstaendigVerbraucht(artikelIID, b);

			// java.io.File ausgabedatei = new
			// java.io.File("c:/vollstverbraucht.txt");
			java.io.File ausgabedatei = getLogFile("vollstverbraucht.txt");
			java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
			bw.write(s);
			bw.close();

		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_PRUEFE_VERBRAUCHTEMENGEN)) {

			String s = DelegateFactory.getInstance().getLagerDelegate()
					.pruefeVerbrauchteMenge();

			// java.io.File ausgabedatei = new
			// java.io.File("c:/verbrauchtemengen.txt");
			java.io.File ausgabedatei = getLogFile("verbrauchtemengen.txt");
			java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
			bw.write(s);
			bw.close();

		}

		else if (e.getActionCommand()
				.equals(MENUE_PFLEGE_PRUEFE_RESERVIERUNGEN)) {
			DelegateFactory.getInstance().getReservierungDelegate()
					.pruefeReservierungen();
		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_PRUEFE_BESTELLTLISTE)) {
			DelegateFactory.getInstance().getArtikelbestelltDelegate()
					.pruefeBestelltliste();
		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_PRUEFE_RAHMENBEDARFE)) {
			DelegateFactory.getInstance().getRahmenbedarfeDelegate()
					.aktualisiereAlleRahmenauftaegeEinesMandanten();
		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_PRUEFE_AUFTRAGSERIENNUMMERN)) {
			DelegateFactory.getInstance().getAuftragpositionDelegate()
					.pruefeAuftragseriennumern();
		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_PRUEFE_VKPFSTAFFELMENGE)) {
			DelegateFactory.getInstance().getVkPreisfindungDelegate()
					.pruefeVkpfStaffelmenge();
		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_PRUEFE_VKPREISLAGERBEWEGUNG)) {

			boolean b = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							"Wenn Sie diese Pflegefunktion ausf\u00FChren, sind Preiskorrekturen auf den Belegen nicht mehr sichtbar. Wollen Sie diese Pflegefunktion wirklich ausf\u00FChren?");

			if (b == true) {
				DelegateFactory.getInstance().getLieferscheinpositionDelegate()
						.pruefeVKPreisAufLagerbewegung();
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_STUECKLISTE_NACHKALKULIEREN)) {

			String response = JOptionPane
					.showInputDialog(
							null,
							"Bitte Artikelnummer eingeben, oder @, um eine Textdatei einzulesen:",
							"", JOptionPane.QUESTION_MESSAGE);

			if (response != null) {
				String artikelnummer = response;

				response = JOptionPane
						.showInputDialog(
								null,
								"Bitte das Datum, ab dem die Lose neu berechnet werden sollen (im Format TT/MM/JJJJ):",
								"", JOptionPane.QUESTION_MESSAGE);

				if (artikelnummer.length() > 0) {

					if (artikelnummer.equals("@")) {

						JFileChooser fc = new JFileChooser();
						fc.setFileFilter(new FileFilter() {
							public boolean accept(File f) {
								return f.getName().toLowerCase()
										.endsWith("csv")
										|| f.isDirectory();
							}

							public String getDescription() {
								return "CSV-Dateien";
							}
						});
						int returnVal = fc.showOpenDialog(LPMain.getInstance()
								.getDesktop());
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fc.getSelectedFile();

							try {
								BufferedReader in = new BufferedReader(
										new FileReader(file));
								String zeile = null;
								while ((zeile = in.readLine()) != null) {
									DelegateFactory
											.getInstance()
											.getFertigungDelegate()
											.alleLoseEinerStuecklisteNachkalkulieren(
													zeile, response);
								}
								in.close();
							} catch (IOException eh) {
								eh.printStackTrace();
							}

						}

					} else {
						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.alleLoseEinerStuecklisteNachkalkulieren(
										artikelnummer, response);
					}

				}

			}

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

	}

	private class AutoWrapButton extends JButton {
		private static final long serialVersionUID = -1988326226807369144L;

		public AutoWrapButton() {
			Insets i = getMargin();
			setMargin(new Insets(i.top, 2, i.bottom, 2));
		}

		public AutoWrapButton(String text) {
			this();
			setText(text);
		}

		@Override
		public void setText(String text) {
			text = text.replaceAll("<\\s*/?(html|p|br)\\s*>", ""); // alle html
																	// und
																	// paragraphen
																	// tags
																	// entfernen

			super.setText("<html><center><p>" + text + "</p></center></html>");
		}
	}

}
