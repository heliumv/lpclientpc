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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.util.Helper;

/**
 * <p>
 * Panel zum Bearbeiten der Materialdaten eines Loses
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>09. 10. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.22 $
 */
public class PanelLosZeitdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneLos tabbedPaneLos = null;
	private LossollarbeitsplanDto loszeitDto = null;

	private JPanel jPanelWorkingOn = new JPanel(new GridBagLayout());

	private WrapperIdentField wifArtikel = null;

	private WrapperLabel wlaArbeitsgang = null;
	private WrapperNumberField wnfArbeitsgang = null;
	int iAnzeigeArbeitszeit = 0;
	private WrapperLabel wlaRuestzeit = null;
	private WrapperSpinner wspRuestzeitStunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(50000), new Integer(1));
	private WrapperSpinner wspRuestzeitMinuten = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));
	private WrapperSpinner wspRuestzeitSekunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));

	private WrapperComboBox wcoAgart = new WrapperComboBox();

	private WrapperSelectField wsfPersonalZugeordneter = null;

	private WrapperLabel wlaStueckzeit = null;
	private WrapperSpinner wspStueckzeitStunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(50000), new Integer(1));
	private WrapperSpinner wspStueckzeitMinuten = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));
	private WrapperSpinner wspStueckzeitSekunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));

	private WrapperSpinner wspMillisekundenStueckzeit = null;

	private WrapperSpinner wspMillisekundenRuestzeit = null;
	private WrapperSpinner wspTageRuestzeit = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(999), new Integer(1));
	private WrapperSpinner wspTageStueckzeit = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(999), new Integer(1));
	
	private WrapperLabel wlaUeberschriftZeitTage = null;
	private WrapperLabel wlaUeberschriftZeitStd = null;
	private WrapperLabel wlaUeberschriftZeitMin = null;
	private WrapperLabel wlaUeberschriftZeitSek = null;
	private WrapperLabel wlaUeberschriftZeitMillis = null;

	private WrapperLabel wlaGesamtzeit = null;

	private WrapperLabel wlaKommentar = null;
	private WrapperTextField wtfKommentar = null;

	private WrapperLabel wlaText = null;
	private WrapperEditorField wefText = null;

	private WrapperCheckBox wcbFertig = new WrapperCheckBox();
	private WrapperCheckBox wcbAutoEndeBeiGeht = new WrapperCheckBox();
	private WrapperCheckBox wcbNurMaschinenzeit = new WrapperCheckBox();
	private WrapperNumberField wnfAufspannung = new WrapperNumberField();

	private WrapperNumberField wnfUnterarbeitsgang = new WrapperNumberField();
	private WrapperLabel wlaMaschinenversatztage = new WrapperLabel();
	private WrapperDateField wdfMaschinenversatztage = new WrapperDateField();
	private WrapperLabel wlaLosgroesse = null;

	private WrapperButton wbuMaterial = new WrapperButton();
	private WrapperTextField wtfMaterial = new WrapperTextField();

	private WrapperLabel wlaKosten = new WrapperLabel();
	private WrapperLabel wlaKostenAG = new WrapperLabel();
	static final public String ACTION_SPECIAL_SOLLMATERIAL_FROM_LISTE = "action_sollmaterial_from_liste";
	private PanelQueryFLR panelQueryFLRSollmaterial = null;
	static final public String ACTION_SPECIAL_MASCHINE_FROM_LISTE = "action_maschine_from_liste";
	private PanelQueryFLR panelQueryFLRMaschine = null;

	private WrapperButton wbuMaschine = new WrapperButton();
	private WrapperTextField wtfMaschine = new WrapperTextField();

	boolean bTheoretischeIstZeit = false;

	public PanelLosZeitdaten(InternalFrame internalFrame, String add2TitleI,
			Object key, TabbedPaneLos tabbedPaneLos) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneLos = tabbedPaneLos;
		jbInit();
		initComponents();
	}

	private TabbedPaneLos getTabbedPaneLos() {
		return tabbedPaneLos;
	}

	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);

		this.setLayout(new GridBagLayout());

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);
		wlaArbeitsgang = new WrapperLabel();
		wnfArbeitsgang = new WrapperNumberField();
		wlaRuestzeit = new WrapperLabel();
		wlaStueckzeit = new WrapperLabel();
		wlaGesamtzeit = new WrapperLabel();
		wlaKosten = new WrapperLabel();
		wlaKommentar = new WrapperLabel();
		wtfKommentar = new WrapperTextField();
		wlaText = new WrapperLabel();
		wlaLosgroesse = new WrapperLabel();
		wefText = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("label.text"));
		wlaUeberschriftZeitTage = new WrapperLabel(LPMain.getTextRespectUISPr("stkl.arbeitsplan.tage"));
		wlaUeberschriftZeitStd = new WrapperLabel(LPMain.getTextRespectUISPr("stkl.arbeitsplan.std"));
		wlaUeberschriftZeitMin = new WrapperLabel(LPMain.getTextRespectUISPr("stkl.arbeitsplan.min"));
		wlaUeberschriftZeitSek = new WrapperLabel(LPMain.getTextRespectUISPr("stkl.arbeitsplan.sek"));
		wlaUeberschriftZeitMillis = new WrapperLabel(LPMain.getTextRespectUISPr("stkl.arbeitsplan.millisek"));
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (((Boolean) parameter.getCWertAsObject()) == true) {
			bTheoretischeIstZeit = true;
		}
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ANZEIGE_IN_ARBEITSPLAN,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());

		iAnzeigeArbeitszeit = (Integer) parameter.getCWertAsObject();
		Map<String, String> m = new TreeMap<String, String>();
		m.put(StuecklisteFac.AGART_LAUFZEIT, StuecklisteFac.AGART_LAUFZEIT);
		m.put(StuecklisteFac.AGART_UMSPANNZEIT,
				StuecklisteFac.AGART_UMSPANNZEIT);
		wcoAgart.setMap(m);
		wcoAgart.addActionListener(this);
		wcbFertig.setText(LPMain.getTextRespectUISPr("lp.fertig"));
		wcbAutoEndeBeiGeht.setText(LPMain
				.getTextRespectUISPr("fert.loszeitdaten.autoendebeigeht"));
		wcbNurMaschinenzeit.setText(LPMain
				.getTextRespectUISPr("fert.loszeitdaten.nurmaschinenzeit"));

		wnfAufspannung.setFractionDigits(0);
		wnfAufspannung.setMinimumValue(1);
		wspMillisekundenStueckzeit = new WrapperSpinner(new Integer(0),
				new Integer(0), new Integer(999), new Integer(1));

		wspMillisekundenRuestzeit = new WrapperSpinner(new Integer(0),
				new Integer(0), new Integer(999), new Integer(1));
		wlaKostenAG.setHorizontalAlignment(SwingConstants.LEFT);

		wifArtikel.getWbuArtikel().setText(
				LPMain.getTextRespectUISPr("button.taetigkeit"));
		wifArtikel.getWbuArtikel().setToolTipText(
				LPMain.getTextRespectUISPr("button.taetigkeit.tooltip"));
		wlaArbeitsgang.setText(LPMain
				.getTextRespectUISPr("stkl.arbeitsplan.arbeitsgang"));
		wlaRuestzeit.setText(LPMain
				.getTextRespectUISPr("stkl.arbeitsplan.ruestzeit"));
		wlaStueckzeit.setText(LPMain
				.getTextRespectUISPr("stkl.arbeitsplan.stueckzeit"));
		wlaGesamtzeit.setText(LPMain.getTextRespectUISPr("fert.gesamtzeit"));
		wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));
		wlaText.setText(LPMain.getTextRespectUISPr("label.text"));

		wlaMaschinenversatztage.setText(LPMain
				.getTextRespectUISPr("fert.arbeitsplan.maschinenversatztage"));

		wbuMaterial.setText(LPMain.getTextRespectUISPr("lp.material") + "...");
		wbuMaterial.setActionCommand(ACTION_SPECIAL_SOLLMATERIAL_FROM_LISTE);
		wbuMaterial.addActionListener(this);
		wtfMaterial.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfMaterial.setActivatable(false);

		wsfPersonalZugeordneter = new WrapperSelectField(
				WrapperSelectField.PERSONAL, getInternalFrame(), true);

		wnfArbeitsgang.setMandatoryFieldDB(true);

		wnfUnterarbeitsgang.setFractionDigits(0);
		wnfUnterarbeitsgang.setMaximumValue(0);
		wnfUnterarbeitsgang.setMaximumValue(9);

		wnfArbeitsgang.setFractionDigits(0);

		wbuMaschine.setText(LPMain.getTextRespectUISPr("lp.maschine") + "...");
		wbuMaschine.setActionCommand(ACTION_SPECIAL_MASCHINE_FROM_LISTE);
		wbuMaschine.addActionListener(this);
		wtfMaschine.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfMaschine.setActivatable(false);

		jPanelWorkingOn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
				10));
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		wifArtikel.getWbuArtikel().setMinimumSize(
				new Dimension(100, Defaults.getInstance().getControlHeight()));
		wifArtikel.getWbuArtikel().setPreferredSize(
				new Dimension(100, Defaults.getInstance().getControlHeight()));
		wifArtikel.getWtfIdent().setMinimumSize(
				new Dimension(100, Defaults.getInstance().getControlHeight()));
		wifArtikel.getWtfIdent().setPreferredSize(
				new Dimension(100, Defaults.getInstance().getControlHeight()));

		wspMillisekundenRuestzeit.setMinimumSize(new Dimension(50, Defaults
				.getInstance().getControlHeight()));
		wspMillisekundenRuestzeit.setPreferredSize(new Dimension(50, Defaults
				.getInstance().getControlHeight()));

		wspMillisekundenStueckzeit.setMinimumSize(new Dimension(50, Defaults
				.getInstance().getControlHeight()));
		wspMillisekundenStueckzeit.setPreferredSize(new Dimension(50, Defaults
				.getInstance().getControlHeight()));

		wifArtikel.setDefaultFilter(ArtikelFilterFactory.getInstance()
				.createFKArtikellisteNurArbeitszeit());

		jPanelWorkingOn.setLayout(new MigLayout("wrap 8", "[15%][10%][3%][15%][15%][15%][15%][13%]"));
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanelWorkingOn.add(wifArtikel.getWbuArtikel(), "growx");
		jPanelWorkingOn.add(wifArtikel.getWtfIdent(), "growx, span 2");
		jPanelWorkingOn.add(wifArtikel.getWtfBezeichnung(), "growx, span");
		
		jPanelWorkingOn.add(wlaArbeitsgang, "growx");
		jPanelWorkingOn.add(wnfArbeitsgang, "growx, span 2");
		jPanelWorkingOn.add(new WrapperLabel("."), "growx 25, split 2");

		jPanelWorkingOn.add(wnfUnterarbeitsgang, "growx 75");
		jPanelWorkingOn.add(wlaKosten, "growx, span");

		JPanel zeiten = new JPanel(new MigLayout("flowy, wrap 3", iAnzeigeArbeitszeit == 1 ? "[33%][33%][33%]" : "[25%][25%][25%][25%]"));
		if (iAnzeigeArbeitszeit == 1) {
			zeiten.add(wlaUeberschriftZeitTage, "growx");
			zeiten.add(wspTageRuestzeit, "growx");
			zeiten.add(wspTageStueckzeit, "growx");
		}
		zeiten.add(wlaUeberschriftZeitStd, "growx");
		zeiten.add(wspRuestzeitStunden, "growx");
		zeiten.add(wspStueckzeitStunden, "growx");
		
		zeiten.add(wlaUeberschriftZeitMin, "growx");
		zeiten.add(wspRuestzeitMinuten, "growx");
		zeiten.add(wspStueckzeitMinuten, "growx");
		
		if (iAnzeigeArbeitszeit == 0) {
			zeiten.add(wlaUeberschriftZeitSek, "growx");
			zeiten.add(wspRuestzeitSekunden, "growx");
			zeiten.add(wspStueckzeitSekunden, "growx");
			
			zeiten.add(wlaUeberschriftZeitMillis, "growx");
			zeiten.add(wspMillisekundenRuestzeit, "growx");
			zeiten.add(wspMillisekundenStueckzeit, "growx");
		}
		jPanelWorkingOn.add(zeiten, "skip, growx, span 3 3");

		jPanelWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("stkl.agart")), "growx");
		jPanelWorkingOn.add(wcoAgart, "growx");
		jPanelWorkingOn.add(wcbFertig, "growx, wrap");

		jPanelWorkingOn.add(wlaRuestzeit, "growx");

		jPanelWorkingOn.add(wlaMaschinenversatztage, "growx");
		jPanelWorkingOn.add(wdfMaschinenversatztage, "growx, wrap");

		jPanelWorkingOn.add(wlaStueckzeit, "growx");

		jPanelWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("stkl.aufspannung")), "growx");
		jPanelWorkingOn.add(wnfAufspannung, "growx, wrap");

		jPanelWorkingOn.add(wlaGesamtzeit, "growx");
		jPanelWorkingOn.add(wlaKostenAG, "growx, span 3");
		jPanelWorkingOn.add(wlaLosgroesse, "growx, span 2");

		jPanelWorkingOn.add(wsfPersonalZugeordneter.getWrapperButton(), "growx");

		jPanelWorkingOn.add(wsfPersonalZugeordneter.getWrapperTextField(), "growx, wrap");

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {
			jPanelWorkingOn.add(wbuMaschine, "growx");

			jPanelWorkingOn.add(wtfMaschine, "growx, span 4");

			jPanelWorkingOn.add(wcbAutoEndeBeiGeht, "growx, span, split 2");

			jPanelWorkingOn.add(wcbNurMaschinenzeit, "growx");
		}
		
		jPanelWorkingOn.add(wlaKommentar, "growx");
		jPanelWorkingOn.add(wtfKommentar, "growx, span 4");
		jPanelWorkingOn.add(wbuMaterial, "growx");
		jPanelWorkingOn.add(wtfMaterial, "growx, span");
		
		jPanelWorkingOn.add(wlaText, "growx");
		jPanelWorkingOn.add(wefText, "growx, span");
	}

	void dialogQueryLossollarbeitsplanFromListe() throws Throwable {
		panelQueryFLRSollmaterial = FertigungFilterFactory.getInstance()
				.createPanelFLRLossollmaterial(getInternalFrame(),
						getTabbedPaneLos().getLosDto().getIId(),
						loszeitDto.getLossollmaterialIId(), true);
		new DialogQuery(panelQueryFLRSollmaterial);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		wifArtikel.validate();
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (loszeitDto != null) {
				LossollarbeitsplanDto savedDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.updateLossollarbeitsplan(loszeitDto);
				this.loszeitDto = savedDto;
				setKeyWhenDetailPanel(loszeitDto.getIId());
				super.eventActionSave(e, true);
				// jetz den anzeigen
				eventYouAreSelected(false);
			}
		}
	}

	/**
	 * Die eingegebenen Daten in ein Dto schreiben
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		// den bestehenden Dto verwenden
		loszeitDto.setLosIId(getTabbedPaneLos().getLosDto().getIId());
		loszeitDto.setBAutoendebeigeht(Helper.boolean2Short(true));
		loszeitDto.setArtikelIIdTaetigkeit(wifArtikel.getArtikelDto().getIId());
		loszeitDto.setBNachtraeglich(Helper.boolean2Short(true));
		loszeitDto.setCKomentar(wtfKommentar.getText());
		loszeitDto.setIArbeitsgangnummer(wnfArbeitsgang.getInteger());
		loszeitDto.setIUnterarbeitsgang(wnfUnterarbeitsgang.getInteger());
		long rTage = wspTageRuestzeit.getInteger().longValue() * (3600000 * 24);
		long rStunden = wspRuestzeitStunden.getInteger().longValue() * 3600000;
		long rMinuten = wspRuestzeitMinuten.getInteger().longValue() * 60000;
		long rSekunden = wspRuestzeitSekunden.getInteger().longValue() * 1000;
		long rMsekunden = wspMillisekundenRuestzeit.getInteger().longValue();
		long lRuestzeit = rTage + rStunden + rMinuten + rSekunden + rMsekunden;

		long sTage = wspTageStueckzeit.getInteger().longValue()
				* (3600000 * 24);
		long sStunden = wspStueckzeitStunden.getInteger().longValue() * 3600000;
		long sMinuten = wspStueckzeitMinuten.getInteger().longValue() * 60000;
		long sSekunden = wspStueckzeitSekunden.getInteger().longValue() * 1000;
		long sMsekunden = wspMillisekundenStueckzeit.getInteger().longValue();
		long lStueckzeit = sTage + sStunden + sMinuten + sSekunden + sMsekunden;

		loszeitDto.setLRuestzeit(new Long(lRuestzeit));
		loszeitDto.setLStueckzeit(new Long(lStueckzeit));
		loszeitDto.setIAufspannung(wnfAufspannung.getInteger());
		loszeitDto.setAgartCNr((String) wcoAgart.getKeyOfSelectedItem());
		loszeitDto.setBFertig(wcbFertig.getShort());
		loszeitDto.setBAutoendebeigeht(wcbAutoEndeBeiGeht.getShort());
		loszeitDto.setBNurmaschinenzeit(wcbNurMaschinenzeit.getShort());
		loszeitDto
				.setPersonalIIdZugeordneter(wsfPersonalZugeordneter.getIKey());

		if (wdfMaschinenversatztage.getTimestamp() != null) {

			int i = Helper.ermittleTageEinesZeitraumes(getTabbedPaneLos()
					.getLosDto().getTProduktionsbeginn(),
					wdfMaschinenversatztage.getDate());

			loszeitDto.setIMaschinenversatztage(i);
		} else {
			loszeitDto.setIMaschinenversatztage(null);
		}

		/**
		 * @todo gesamtzeit bei focuslost PJ 5010
		 */
		loszeitDto.setNGesamtzeit(new BigDecimal(0));

		loszeitDto.setXText(wefText.getText());

	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		if (loszeitDto != null) {
			holeArtikel(loszeitDto.getArtikelIIdTaetigkeit());
			wtfKommentar.setText(loszeitDto.getCKomentar());
			wnfArbeitsgang.setInteger(loszeitDto.getIArbeitsgangnummer());
			wnfUnterarbeitsgang.setInteger(loszeitDto.getIUnterarbeitsgang());

			wnfAufspannung.setInteger(loszeitDto.getIAufspannung());
			wcoAgart.setKeyOfSelectedItem(loszeitDto.getAgartCNr());
			wcbFertig.setShort(loszeitDto.getBFertig());
			wcbAutoEndeBeiGeht.setShort(loszeitDto.getBAutoendebeigeht());
			wcbNurMaschinenzeit.setShort(loszeitDto.getBNurmaschinenzeit());
			wsfPersonalZugeordneter.setKey(loszeitDto
					.getPersonalIIdZugeordneter());

			long dRuestzeit = 0;
			long dStueckzeit = 0;

			dRuestzeit = (loszeitDto.getLRuestzeit().longValue());
			if (dRuestzeit > 0) {
				if (iAnzeigeArbeitszeit != 0) {
					double rTage = dRuestzeit / (3600000 * 24);
					if (rTage != 0) {
						dRuestzeit = dRuestzeit % (3600000 * 24);
					}
					wspTageRuestzeit.setInteger(new Integer((int) rTage));
				} else {
					wspTageRuestzeit.setInteger(0);
				}
				long rStunden = dRuestzeit / 3600000;
				if (rStunden != 0) {
					dRuestzeit = dRuestzeit % 3600000;
				}
				long rMinuten = dRuestzeit / 60000;
				if (rMinuten != 0) {
					dRuestzeit = dRuestzeit % 60000;
				}
				long rSekunden = dRuestzeit / 1000;
				if (rSekunden != 0) {
					dRuestzeit = dRuestzeit % 1000;
				}
				long rMsekunden = dRuestzeit;

				wspRuestzeitStunden.setInteger(new Integer((int) rStunden));
				wspRuestzeitMinuten.setInteger(new Integer((int) rMinuten));
				wspRuestzeitSekunden.setInteger(new Integer((int) rSekunden));
				wspMillisekundenRuestzeit.setInteger(new Integer(
						(int) rMsekunden));
			} else {
				wspTageRuestzeit.setInteger(new Integer(0));
				wspRuestzeitStunden.setInteger(new Integer(0));
				wspRuestzeitMinuten.setInteger(new Integer(0));
				wspRuestzeitSekunden.setInteger(new Integer(0));
				wspMillisekundenRuestzeit.setInteger(new Integer(0));

			}

			dStueckzeit = (loszeitDto.getLStueckzeit().longValue());
			if (dStueckzeit > 0) {
				if (iAnzeigeArbeitszeit != 0) {
					double sTage = dStueckzeit / (3600000 * 24);
					if (sTage != 0) {
						dStueckzeit = dStueckzeit % (3600000 * 24);
					}
					wspTageStueckzeit.setInteger(new Integer((int) sTage));
				} else {
					wspTageStueckzeit.setInteger(0);
				}
				long sStunden = dStueckzeit / 3600000;
				if (sStunden != 0) {
					dStueckzeit = dStueckzeit % 3600000;
				}
				long sMinuten = dStueckzeit / 60000;
				if (sMinuten != 0) {
					dStueckzeit = dStueckzeit % 60000;
				}
				long sSekunden = dStueckzeit / 1000;
				if (sSekunden != 0) {
					dStueckzeit = dStueckzeit % 1000;
				}
				long sMsekunden = dStueckzeit;

				wspStueckzeitStunden.setInteger(new Integer((int) sStunden));
				wspStueckzeitMinuten.setInteger(new Integer((int) sMinuten));
				wspStueckzeitSekunden.setInteger(new Integer((int) sSekunden));
				wspMillisekundenStueckzeit.setInteger(new Integer(
						(int) sMsekunden));
			} else {
				wspTageStueckzeit.setInteger(new Integer(0));
				wspStueckzeitStunden.setInteger(new Integer(0));
				wspStueckzeitMinuten.setInteger(new Integer(0));
				wspStueckzeitSekunden.setInteger(new Integer(0));
				wspMillisekundenStueckzeit.setInteger(new Integer(0));

			}

			if (getTabbedPaneLos().getLosDto().getStuecklisteIId() != null) {
				StuecklisteDto stkDto = DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(
								getTabbedPaneLos().getLosDto()
										.getStuecklisteIId());
				wlaLosgroesse.setText(LPMain
						.getTextRespectUISPr("label.losgroesse")
						+ " = "
						+ Helper.formatZahl(getTabbedPaneLos().getLosDto()
								.getNLosgroesse(), 0, LPMain.getInstance()
								.getUISprLocale())
						+ " "
						+ stkDto.getArtikelDto().getEinheitCNr().trim());
			} else {
				wlaLosgroesse.setText(LPMain
						.getTextRespectUISPr("label.losgroesse")
						+ " = "
						+ Helper.formatZahl(getTabbedPaneLos().getLosDto()
								.getNLosgroesse(), 0, LPMain.getInstance()
								.getUISprLocale()));
			}

			if (loszeitDto.getMaschineIId() != null) {
				MaschineDto maschineDto = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.maschineFindByPrimaryKey(loszeitDto.getMaschineIId());
				wtfMaschine.setText(maschineDto.getBezeichnung());
			} else {
				wtfMaschine.setText("");
			}

			if (loszeitDto.getLossollmaterialIId() != null) {

				LossollmaterialDto lossollmaterialDto = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(
								loszeitDto.getLossollmaterialIId());
				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								lossollmaterialDto.getArtikelIId());
				wtfMaterial.setText(artikelDto.formatArtikelbezeichnung());

			} else {
				wtfMaterial.setText("");
			}

			// Maschine+Personalkosten
			String sKosten = "";
			BigDecimal kostenAG = new BigDecimal(0);

			if (Helper.short2Boolean(loszeitDto.getBNurmaschinenzeit()) == false) {
				BigDecimal preis = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
								loszeitDto.getArtikelIIdTaetigkeit());

				if (preis == null) {
					preis = new BigDecimal(0);
				}

				if (loszeitDto.getIAufspannung() != null
						&& loszeitDto.getIAufspannung() >= 1) {
					preis = preis.divide(
							new BigDecimal(loszeitDto.getIAufspannung()), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}
				kostenAG = kostenAG.add(preis.multiply(loszeitDto
						.getNGesamtzeit()));
				sKosten = LPMain
						.getTextRespectUISPr("fert.loszeitdaten.personalkosten")
						+ " "

						+ Helper.formatZahl(preis, 2, LPMain.getTheClient()
								.getLocUi())
						+ " "
						+ LPMain.getTheClient().getSMandantenwaehrung();
				if (loszeitDto.getMaschineIId() != null) {
					sKosten += ", ";
				}
			}

			if (loszeitDto.getMaschineIId() != null) {
				BigDecimal bdKosten = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.getMaschinenKostenZumZeitpunkt(
								loszeitDto.getMaschineIId());

				if (loszeitDto.getIAufspannung() != null
						&& loszeitDto.getIAufspannung() >= 1) {
					bdKosten = bdKosten.divide(
							new BigDecimal(loszeitDto.getIAufspannung()), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}

				if (bdKosten == null) {
					bdKosten = new BigDecimal(0);
				}

				kostenAG = kostenAG.add(bdKosten.multiply(loszeitDto
						.getNGesamtzeit()));

				sKosten += LPMain
						.getTextRespectUISPr("fert.loszeitdaten.maschinenkosten")
						+ " "

						+ Helper.formatZahl(bdKosten, 2, LPMain.getTheClient()
								.getLocUi())
						+ " "
						+ LPMain.getTheClient().getSMandantenwaehrung();

			}

			wlaKostenAG.setText(Helper.formatZahl(loszeitDto.getNGesamtzeit(),
					4, LPMain.getTheClient().getLocUi())
					+ " "
					+ SystemFac.EINHEIT_STUNDE.trim()
					+ "/ "
					+ LPMain.getTextRespectUISPr("lp.kosten")
					+ ": "
					+ Helper.formatZahl(kostenAG, 2, LPMain.getTheClient()
							.getLocUi())
					+ " "
					+ LPMain.getTheClient().getSMandantenwaehrung());

			if (loszeitDto.getIMaschinenversatztage() != null) {

				wdfMaschinenversatztage.setDate(Helper.addiereTageZuDatum(
						getTabbedPaneLos().getLosDto().getTProduktionsbeginn(),
						loszeitDto.getIMaschinenversatztage()));

				int i = Helper.ermittleTageEinesZeitraumes(getTabbedPaneLos()
						.getLosDto().getTProduktionsbeginn(),
						wdfMaschinenversatztage.getDate());

				loszeitDto.setIMaschinenversatztage(i);
			} else {
				wdfMaschinenversatztage.setDate(getTabbedPaneLos().getLosDto()
						.getTProduktionsbeginn());
			}

			wlaKosten.setText(sKosten);

			wefText.setText(loszeitDto.getXText());
			wefText.setDefaultText(loszeitDto.getXText());
			// Statusleiste
			this.setStatusbarPersonalIIdAendern(loszeitDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(loszeitDto.getTAendern());
			this.setStatusbarStatusCNr(getTabbedPaneLos().getLosDto()
					.getStatusCNr());
		}
	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, false, false);
		loszeitDto = new LossollarbeitsplanDto();
		this.leereAlleFelder(this);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.loszeitDto != null) {
			if (loszeitDto.getIId() != null) {
				if (!isLockedDlg()) {
					DelegateFactory.getInstance().getFertigungDelegate()
							.removeLossollarbeitsplan(loszeitDto);
					this.loszeitDto = null;
					this.leereAlleFelder(this);
					this.setKeyWhenDetailPanel(null);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_LOS;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MASCHINE_FROM_LISTE)) {
			dialogQueryMaschineFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_SOLLMATERIAL_FROM_LISTE)) {
			dialogQueryLossollarbeitsplanFromListe();
		}

		if (e.getSource().equals(wcoAgart)) {
			if (getLockedstateDetailMainKey() != null
					&& getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME)

				if (bTheoretischeIstZeit == true) {
					if (wcoAgart.getKeyOfSelectedItem() == null) {
						wspStueckzeitMinuten.setEnabled(false);
						wspStueckzeitSekunden.setEnabled(false);
						wspStueckzeitStunden.setEnabled(false);
						wspMillisekundenStueckzeit.setEnabled(false);
						wspStueckzeitMinuten.setValue(0);
						wspStueckzeitSekunden.setValue(0);
						wspStueckzeitStunden.setValue(0);
						wspMillisekundenStueckzeit.setValue(0);

						wspRuestzeitMinuten.setEnabled(true);
						wspRuestzeitSekunden.setEnabled(true);
						wspRuestzeitStunden.setEnabled(true);
						wspMillisekundenRuestzeit.setEnabled(true);

					} else {
						wspRuestzeitMinuten.setEnabled(false);
						wspRuestzeitSekunden.setEnabled(false);
						wspRuestzeitStunden.setEnabled(false);
						wspMillisekundenRuestzeit.setEnabled(false);

						wspRuestzeitMinuten.setValue(0);
						wspRuestzeitSekunden.setValue(0);
						wspRuestzeitStunden.setValue(0);
						wspMillisekundenRuestzeit.setValue(0);

						wspStueckzeitMinuten.setEnabled(true);
						wspStueckzeitSekunden.setEnabled(true);
						wspStueckzeitStunden.setEnabled(true);
						wspMillisekundenStueckzeit.setEnabled(true);
					}
				}
		}

	}

	private void dialogQueryMaschineFromListe() throws Throwable {
		panelQueryFLRMaschine = ZeiterfassungFilterFactory.getInstance()
				.createPanelFLRMaschinen(getInternalFrame(),
						loszeitDto.getMaschineIId());
		new DialogQuery(panelQueryFLRMaschine);

	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRMaschine) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				MaschineDto maschineDto = null;
				maschineDto = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.maschineFindByPrimaryKey(key);
				loszeitDto.setMaschineIId(maschineDto.getIId());

				wcbAutoEndeBeiGeht.setShort(maschineDto.getBAutoendebeigeht());
				wtfMaschine.setText(maschineDto.getBezeichnung());
			} else if (e.getSource() == panelQueryFLRSollmaterial) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LossollmaterialDto lossollmaterialDto = DelegateFactory
						.getInstance().getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(key);

				loszeitDto.setLossollmaterialIId(key);

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								lossollmaterialDto.getArtikelIId());
				wtfMaterial.setText(artikelDto.formatArtikelbezeichnung());

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRMaschine) {
				wtfMaschine.setText(null);
				loszeitDto.setMaschineIId(null);
			} else if (e.getSource() == panelQueryFLRSollmaterial) {
				wtfMaterial.setText(null);
				loszeitDto.setLossollmaterialIId(null);
			}
		}
	}

	private void holeArtikel(Object key) throws Throwable {
		ArtikelDto artikelDto;
		if (key != null) {
			artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey((Integer) key);
		} else {
			artikelDto = null;
		}
		wifArtikel.setArtikelDto(artikelDto);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {

			Object key = getKeyWhenDetailPanel();
			if (key == null || key.equals(LPMain.getLockMeForNew())) {
				// einen neuen Eintrag anlegen oder die letzte Position wurde
				// geloescht.
				leereAlleFelder(this);
				clearStatusbar();
				// die naechste Arbeitsgangnummer vorbesetzen
				Integer i = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.getNextArbeitsgang(
								getTabbedPaneLos().getLosDto().getIId());
				if (i != null) {
					wnfArbeitsgang.setInteger(i);
				} else {
					wnfArbeitsgang.setInteger(new Integer(10));
				}
				wdfMaschinenversatztage.setMinimumValue(getTabbedPaneLos()
						.getLosDto().getTProduktionsbeginn());
				wdfMaschinenversatztage.setDate(getTabbedPaneLos().getLosDto()
						.getTProduktionsbeginn());

				// PJ 16296
				eventActionSpecial(new ActionEvent(wcoAgart, 1, ""));

			} else {
				// einen alten Eintrag laden.
				loszeitDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.lossollarbeitsplanFindByPrimaryKey((Integer) key);
				dto2Components();
				wdfMaschinenversatztage.setMinimumValue(getTabbedPaneLos()
						.getLosDto().getTProduktionsbeginn());
			}

		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		if (bTheoretischeIstZeit == true) {
			if (wcoAgart.getKeyOfSelectedItem() == null) {
				wspStueckzeitMinuten.setEnabled(false);
				wspStueckzeitSekunden.setEnabled(false);
				wspStueckzeitStunden.setEnabled(false);
				wspMillisekundenStueckzeit.setEnabled(false);

				wspStueckzeitMinuten.setValue(0);
				wspStueckzeitSekunden.setValue(0);
				wspStueckzeitStunden.setValue(0);
				wspMillisekundenStueckzeit.setValue(0);

			} else {
				wspRuestzeitMinuten.setEnabled(false);
				wspRuestzeitSekunden.setEnabled(false);
				wspRuestzeitStunden.setEnabled(false);
				wspMillisekundenRuestzeit.setEnabled(false);

				wspRuestzeitMinuten.setValue(0);
				wspRuestzeitSekunden.setValue(0);
				wspRuestzeitStunden.setValue(0);
				wspMillisekundenRuestzeit.setValue(0);
			}
		}

	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		return false;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wifArtikel.getWbuArtikel();
	}
}
