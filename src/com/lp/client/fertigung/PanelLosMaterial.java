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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

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
 * @version $Revision: 1.13 $
 */
public class PanelLosMaterial extends PanelBasis implements
		ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneLos tabbedPaneLos = null;

	private LagerDto lagerDto = null;
	private MontageartDto montageartDto = null;
	// identfield: 10 identfield als member
	// identfield: 11 wbuArtikel etc. werden nicht mehr benoetigt
	private WrapperIdentField wifArtikel = null;

	private LossollmaterialDto losmatDto = null;

	private static final String ACTION_SPECIAL_LAGER = "action_special_los_lager";
	private static final String ACTION_SPECIAL_MONTAGEART = "action_special_los_montageart";

	private PanelQueryFLR panelQueryFLRLager = null;
	private PanelQueryFLR panelQueryFLRMontageart = null;

	boolean bDarfGestpreiseaendern = false;

	private boolean bFruehzeitigeBeschaffung = false;

	Integer letzterArtikelIId = null;

	private WrapperLabel wlaBeginnterminOffset = new WrapperLabel();
	private WrapperNumberField wnfBeginnterminOffset = new WrapperNumberField();

	private WrapperCheckBox wcbSofortigeBestellung = new WrapperCheckBox();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanelWorkingOn = new JPanel();

	private WrapperTextField wtfLager = null;
	private WrapperButton wbuLager = null;

	private WrapperLabel wlaMenge = null;
	private WrapperNumberField wnfMenge = null;
	private WrapperLabel wlaSollmenge = null;
	private WrapperNumberField wnfSollmenge = null;
	private WrapperLabel wlaEinheit1 = null;
	private WrapperLabel wlaEinheit2 = null;
	private WrapperLabel wlaAusgegeben = null;
	private WrapperNumberField wnfAusgegeben = null;
	private WrapperLabel wlaEinheit3 = null;

	private WrapperLabel wlaSollpreis = null;
	private WrapperNumberField wnfSollpreis = null;
	private WrapperLabel wlaWaehrung1 = null;
	private WrapperLabel wlaIstpreis = null;
	private WrapperNumberField wnfIstpreis = null;
	private WrapperLabel wlaWaehrung2 = null;

	private WrapperSNRField wsnrfSerienchargennummer = null;

	private WrapperButton wbuMontageart = null;
	private WrapperTextField wtfMontageart = null;

	private WrapperLabel wlaDifferenzMenge = null;
	private WrapperNumberField wnfDifferenzMenge = null;
	private WrapperLabel wlaEinheit4 = null;

	private WrapperLabel wlaDifferenzWert = null;
	private WrapperNumberField wnfDifferenzWert = null;
	private WrapperLabel wlaWaehrung3 = null;

	private WrapperLabel wlaKommentar = null;
	private WrapperTextField wtfKommentar = null;

	private MontageartDto defaultMontageartDto = null;

	JList list = null;
	Map<?, ?> mBestellungen = new TreeMap<Object, Object>();
	protected WrapperGotoButton wbuBestellung = new WrapperGotoButton(
			WrapperGotoButton.GOTO_BESTELLUNG_POSITION);

	public PanelLosMaterial(InternalFrame internalFrame, String add2TitleI,
			Object key, TabbedPaneLos tabbedPaneLos) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneLos = tabbedPaneLos;

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_FRUEHZEITIGE_BESCHAFFUNG,
						ParameterFac.KATEGORIE_STUECKLISTE,
						LPMain.getTheClient().getMandant());
		bFruehzeitigeBeschaffung = (Boolean) parameter.getCWertAsObject();

		jbInit();

		initComponents();
		initPanel();
	}

	private void initPanel() throws Throwable {
		String sMandantenWaehrung = LPMain.getTheClient()
				.getSMandantenwaehrung();
		wlaWaehrung1.setText(sMandantenWaehrung);
		wlaWaehrung2.setText(sMandantenWaehrung);
		wlaWaehrung3.setText(sMandantenWaehrung);

		MontageartDto[] dtos = DelegateFactory.getInstance()
				.getStuecklisteDelegate().montageartFindByMandantCNr();

		if (dtos != null && dtos.length > 0) {
			defaultMontageartDto = dtos[0];
		}

	}

	private TabbedPaneLos getTabbedPaneLos() {
		return tabbedPaneLos;
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		String add2Title = LPMain
				.getTextRespectUISPr("fert.report.materialliste");
		getInternalFrame().showReportKriterien(
				new ReportMaterialliste((InternalFrameFertigung) tabbedPaneLos
						.getInternalFrame(), add2Title));
	}

	private void jbInit() throws Throwable {

		boolean bDarfCUD = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FERT_DARF_SOLLMATERIAL_CUD);

		String[] aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT };

		boolean hatRecht = DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						RechteFac.RECHT_FERT_LOS_DARF_ISTMATERIAL_MANUELL_NACHBUCHEN);

		if (getTabbedPaneLos().getRechtModulweit().equals(
				RechteFac.RECHT_MODULWEIT_READ)
				&& hatRecht == true) {
			aWhichButtonIUse = new String[] {};
		}

		this.enableToolsPanelButtons(aWhichButtonIUse);
		wtfLager = new WrapperTextField();
		wbuLager = new WrapperButton();
		wlaMenge = new WrapperLabel();
		wnfMenge = new WrapperNumberField();
		wlaAusgegeben = new WrapperLabel();
		wnfAusgegeben = new WrapperNumberField();
		wlaEinheit1 = new WrapperLabel();
		wlaEinheit2 = new WrapperLabel();
		wlaEinheit3 = new WrapperLabel();
		wlaSollpreis = new WrapperLabel();
		wnfSollpreis = new WrapperNumberField();
		wlaWaehrung1 = new WrapperLabel();
		wlaIstpreis = new WrapperLabel();
		wnfIstpreis = new WrapperNumberField();
		wlaWaehrung2 = new WrapperLabel();
		wsnrfSerienchargennummer = new WrapperSNRField();
		wlaSollmenge = new WrapperLabel();
		wnfSollmenge = new WrapperNumberField();

		// PJ16379
		if (!bDarfCUD) {
			wnfMenge.setActivatable(false);
			wnfSollpreis.setActivatable(false);
		}

		wtfMontageart = new WrapperTextField();
		wbuMontageart = new WrapperButton();
		wlaDifferenzMenge = new WrapperLabel();
		wnfDifferenzMenge = new WrapperNumberField();
		wlaEinheit4 = new WrapperLabel();

		wlaDifferenzWert = new WrapperLabel();
		wnfDifferenzWert = new WrapperNumberField();
		wlaWaehrung3 = new WrapperLabel();
		// identfield: 12 new() in jbInit
		wifArtikel = new WrapperIdentField(getInternalFrame(), this);

		wnfMenge.setFractionDigits(3);
		wnfAusgegeben.setFractionDigits(3);
		wnfSollmenge.setFractionDigits(3);
		wnfDifferenzMenge.setFractionDigits(3);
		wnfSollpreis.setFractionDigits(4);
		wnfIstpreis.setFractionDigits(4);
		wnfDifferenzWert.setFractionDigits(4);

		this.setLayout(gridBagLayout1);
		wbuLager.setText(LPMain.getTextRespectUISPr("button.lager"));
		wbuLager.setToolTipText(LPMain
				.getTextRespectUISPr("button.lager.tooltip"));
		wbuMontageart.setText(LPMain.getTextRespectUISPr("button.montageart"));
		wbuMontageart.setToolTipText(LPMain
				.getTextRespectUISPr("button.montageart.tooltip"));
		wlaMenge.setText(LPMain.getTextRespectUISPr("fert.sollmenge"));
		wlaAusgegeben.setText(LPMain.getTextRespectUISPr("fert.ausgegeben"));
		wlaSollmenge.setText(LPMain.getTextRespectUISPr("fert.sollsatzmenge"));
		wlaSollpreis.setText(LPMain.getTextRespectUISPr("fert.sollpreis"));
		wlaIstpreis.setText(LPMain.getTextRespectUISPr("fert.istpreis"));
		wlaDifferenzMenge.setText(LPMain.getTextRespectUISPr("lp.differenz"));

		wlaKommentar = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.kommentar"));
		wtfKommentar = new WrapperTextField(80);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		wbuLager.addActionListener(this);
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER);
		wbuMontageart.addActionListener(this);
		wbuMontageart.setActionCommand(ACTION_SPECIAL_MONTAGEART);
		wnfMenge.addFocusListener(new PanelLosMaterial_wnfMenge_focusAdapter(
				this));

		wlaEinheit1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheit2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheit3.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheit4.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);

		// identfield: 13 alle labels, die die einheit anzeigen sollen,
		// hinzufuegen
		wifArtikel.addEinheitLabel(wlaEinheit1);
		wifArtikel.addEinheitLabel(wlaEinheit2);
		wifArtikel.addEinheitLabel(wlaEinheit3);
		wifArtikel.addEinheitLabel(wlaEinheit4);

		wtfLager.setActivatable(false);
		wnfSollmenge.setActivatable(false);
		wnfAusgegeben.setActivatable(false);

		bDarfGestpreiseaendern = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_WW_ARTIKEL_GESTPREISE_CU);

		if (!bDarfGestpreiseaendern) {
			wnfSollpreis.setActivatable(false);

		}

		wnfIstpreis.setActivatable(false);
		wnfDifferenzMenge.setActivatable(false);
		wnfDifferenzWert.setActivatable(false);
		wtfMontageart.setActivatable(false);

		wsnrfSerienchargennummer.setActivatable(false);

		wtfMontageart.setMandatoryFieldDB(true);
		wnfMenge.setMandatoryFieldDB(true);

		// identfield: 14 optional: dimensions so setzen
		wifArtikel.getWbuArtikel().setMinimumSize(
				new Dimension(110, Defaults.getInstance().getControlHeight()));
		wifArtikel.getWbuArtikel().setPreferredSize(
				new Dimension(110, Defaults.getInstance().getControlHeight()));
		wifArtikel.getWtfIdent().setMinimumSize(
				new Dimension(100, Defaults.getInstance().getControlHeight()));
		wifArtikel.getWtfIdent().setPreferredSize(
				new Dimension(100, Defaults.getInstance().getControlHeight()));

		wlaAusgegeben.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wlaAusgegeben.setPreferredSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wnfAusgegeben.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wnfAusgegeben.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaEinheit1.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit1.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit2.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit2.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));

		jPanelWorkingOn.setLayout(new MigLayout("wrap 9", "[15%][20%][5%][15%][10%][5%][15%][10%][5%]"));
		
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		// identfield: 15 die komponenten ins panel haengen
		jPanelWorkingOn.add(wifArtikel.getWbuArtikel(), "growx");
		jPanelWorkingOn.add(wifArtikel.getWtfIdent(), "growx");
		jPanelWorkingOn.add(wifArtikel.getWtfBezeichnung(), "growx, span");
		
		jPanelWorkingOn.add(wifArtikel.getWtfZusatzBezeichnung(), "skip 2, growx, span, wrap 20");

		jPanelWorkingOn.add(wlaKommentar, "growx");
		jPanelWorkingOn.add(wtfKommentar, "growx, span");
		
		jPanelWorkingOn.add(wbuMontageart, "growx");
		jPanelWorkingOn.add(wtfMontageart, "growx, wrap");
		
		jPanelWorkingOn.add(wlaSollmenge, "growx");
		jPanelWorkingOn.add(wnfSollmenge, "growx");
		jPanelWorkingOn.add(wlaEinheit3, "growx, wrap");
		
		jPanelWorkingOn.add(wlaMenge, "growx");
		jPanelWorkingOn.add(wnfMenge, "growx");
		jPanelWorkingOn.add(wlaEinheit1, "growx");
		jPanelWorkingOn.add(wlaAusgegeben, "growx");
		jPanelWorkingOn.add(wnfAusgegeben, "growx");
		jPanelWorkingOn.add(wlaEinheit2, "growx");
		jPanelWorkingOn.add(wlaDifferenzMenge, "growx");
		jPanelWorkingOn.add(wnfDifferenzMenge, "growx");
		jPanelWorkingOn.add(wlaEinheit4, "growx, wrap");

		jPanelWorkingOn.add(wlaSollpreis, "growx");
		jPanelWorkingOn.add(wnfSollpreis, "growx");
		jPanelWorkingOn.add(wlaWaehrung1, "growx");
		jPanelWorkingOn.add(wlaIstpreis, "growx");
		jPanelWorkingOn.add(wnfIstpreis, "growx");
		jPanelWorkingOn.add(wlaWaehrung2, "growx");
		jPanelWorkingOn.add(wlaDifferenzWert, "growx");
		jPanelWorkingOn.add(wnfDifferenzWert, "growx");
		jPanelWorkingOn.add(wlaWaehrung3, "growx, wrap");

		if (bFruehzeitigeBeschaffung == true) {
			wcbSofortigeBestellung.setText(LPMain
					.getTextRespectUISPr("stk.positionen.sofortigebestellung"));
			jPanelWorkingOn.add(wcbSofortigeBestellung, "skip 4, growx, span, wrap");
		} else {
			wlaBeginnterminOffset.setText(LPMain
					.getTextRespectUISPr("stk.positionen.beginnterminoffset"));
			jPanelWorkingOn.add(wlaBeginnterminOffset, "skip 5, growx, span 2");
			wnfBeginnterminOffset.setMandatoryField(true);
			wnfBeginnterminOffset.setFractionDigits(0);
			jPanelWorkingOn.add(wnfBeginnterminOffset, "growx, wrap");
		}

		list = new JList();
		list.setFont(new java.awt.Font("monospaced", 0, 12));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		list.addListSelectionListener(this);
		wbuBestellung.setText(LPMain
				.getTextRespectUISPr("bes.bestellung.tooltip"));
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_VERKNUEPFTE_BESTELLDETAILS_ANZEIGEN,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameter.getCWertAsObject()) {

			WrapperLabel wla = new WrapperLabel(
					LPMain.getTextRespectUISPr("fert.los.material.verknuepftebestellungen"));
			wla.setHorizontalAlignment(SwingConstants.LEFT);

			jPanelWorkingOn.add(wla, "growx, span 2, wrap");

			jPanelWorkingOn.add(wbuBestellung, "growx");
			jPanelWorkingOn.add(listScroller, "height min:1000:1000, grow, span");
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				wbuBestellung.setOKey(null);

			} else {
				Iterator<?> it = mBestellungen.keySet().iterator();
				int i = 0;
				while (it.hasNext()) {

					Integer key = (Integer) it.next();

					if (i == list.getSelectedIndex()) {
						wbuBestellung.setOKey(key);
						break;
					}
					i++;
				}

			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// identfield: 20 die Eingabe muss vor dem speichern geprueft werden
		wifArtikel.validate();
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (losmatDto != null) {

				letzterArtikelIId = losmatDto.getArtikelIId();

				LossollmaterialDto savedDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.updateLossollmaterial(losmatDto);
				this.losmatDto = savedDto;
				setKeyWhenDetailPanel(losmatDto.getIId());
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
		if (losmatDto == null) {
			losmatDto = new LossollmaterialDto();
			losmatDto.setLosIId(getTabbedPaneLos().getLosDto().getIId());
		}
		// identfield: 16 beim speichern artikeldaten auslesen
		losmatDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());
		if (losmatDto.getEinheitCNr() == null) {
			losmatDto.setEinheitCNr(wifArtikel.getArtikelDto().getEinheitCNr());
		}
		losmatDto.setMontageartIId(montageartDto.getIId());
		losmatDto.setNMenge(wnfMenge.getBigDecimal());

		if (bFruehzeitigeBeschaffung == true) {
			if (wcbSofortigeBestellung.isSelected()) {
				losmatDto.setIBeginnterminoffset(-999);
			} else {
				losmatDto.setIBeginnterminoffset(0);
			}
		} else {
			losmatDto
					.setIBeginnterminoffset(wnfBeginnterminOffset.getInteger());
		}

		BigDecimal sollPreis = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.getGemittelterGestehungspreisEinesLagers(
						wifArtikel.getArtikelDto().getIId(),
						DelegateFactory.getInstance().getLagerDelegate()
								.getHauptlagerDesMandanten().getIId());

		if (wnfSollpreis.getBigDecimal() != null && bDarfGestpreiseaendern) {
			sollPreis = wnfSollpreis.getBigDecimal();
		}
		losmatDto.setNSollpreis(sollPreis);

		losmatDto.setISort(new Integer(0));
		losmatDto.setCKommentar(wtfKommentar.getText());

	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		if (losmatDto != null) {
			holeArtikel(losmatDto.getArtikelIId());
			holeMontageart(losmatDto.getMontageartIId());
			wtfKommentar.setText(losmatDto.getCKommentar());
			losmatDto.getCPosition();
			/** @todo anzeigen PJ 5009 */
			wlaEinheit1.setText(losmatDto.getEinheitCNr().trim());
			wlaEinheit2.setText(losmatDto.getEinheitCNr().trim());
			wlaEinheit3.setText(losmatDto.getEinheitCNr().trim());
			wlaEinheit4.setText(losmatDto.getEinheitCNr().trim());
			losmatDto.getFDimension1();
			/** @todo anzeigen PJ 5009 */
			losmatDto.getFDimension2();
			/** @todo anzeigen PJ 5009 */
			losmatDto.getFDimension3();
			/** @todo anzeigen PJ 5009 */
			losmatDto.getILfdnummer();
			/** @todo anzeigen PJ 5009 */
			losmatDto.getISort();
			/** @todo anzeigen PJ 5009 */
			losmatDto.getMontageartIId();
			/** @todo anzeigen PJ 5009 */

			if (bFruehzeitigeBeschaffung == true) {

				if (losmatDto.getIBeginnterminoffset() == -999) {
					wcbSofortigeBestellung.setSelected(true);
				} else {
					wcbSofortigeBestellung.setSelected(false);
				}
			} else {
				wnfBeginnterminOffset.setInteger(losmatDto
						.getIBeginnterminoffset());
			}

			wnfMenge.setBigDecimal(losmatDto.getNMenge());
			focusLostWnfMenge();

			wnfSollpreis.setBigDecimal(losmatDto.getNSollpreis());

			wnfAusgegeben.setBigDecimal(DelegateFactory.getInstance()
					.getFertigungDelegate()
					.getAusgegebeneMenge(losmatDto.getIId()));
			wnfIstpreis.setBigDecimal(DelegateFactory.getInstance()
					.getFertigungDelegate()
					.getAusgegebeneMengePreis(losmatDto.getIId()));

			mBestellungen = DelegateFactory.getInstance()
					.getBestellungDelegate()
					.getListeDerVerknuepftenBestellungen(losmatDto.getIId());

			list.removeAll();
			wbuBestellung.setOKey(null);

			Object[] tempZeilen = mBestellungen.values().toArray();
			list.setListData(tempZeilen);

			// Statusleiste
			this.setStatusbarPersonalIIdAendern(losmatDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(losmatDto.getTAendern());
			this.setStatusbarStatusCNr(getTabbedPaneLos().getLosDto()
					.getStatusCNr());
		}
	}

	private void dto2ComponentsLager() {
		if (lagerDto != null) {
			wtfLager.setText(lagerDto.getCNr());
		} else {
			wtfLager.setText(null);
		}
	}

	private void dto2ComponentsMontageart() {
		if (montageartDto != null) {
			wtfMontageart.setText(montageartDto.getCBez());
		} else {
			wtfMontageart.setText(null);
		}
	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, false, false);
		losmatDto = null;
		// identfield: 17 bei new artikelDto=null setzen
		wifArtikel.setArtikelDto(null);
		this.lagerDto = null;
		this.leereAlleFelder(this);
		lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.getHauptlagerDesMandanten();
		dto2ComponentsLager();
	}

	/**
	 * Stornieren einer Rechnung bzw Gutschrift
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.losmatDto != null) {
			if (losmatDto.getIId() != null) {
				if (!isLockedDlg()) {
					// PJ 17231 Zuerst pruefen obs Verknuepfungen gibt
					// Im Sollarbeitsplan

					// In der Bestellposition
					String s = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.verknuepfungZuBestellpositionUndArbeitsplanDarstellen(
									losmatDto.getIId());
					if (s != null && !s.equals("")) {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("fert.sollmat.loeschen.abhaengigkeiten")
												+ s);
						return;
					} else {

						DelegateFactory.getInstance().getFertigungDelegate()
								.removeLossollmaterial(losmatDto);
					}
					this.losmatDto = null;
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
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER)) {
			dialogQueryLager();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MONTAGEART)) {
			dialogQueryMontageart();
		}
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
			if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeLager((Integer) key);
			} else if (e.getSource() == panelQueryFLRMontageart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeMontageart((Integer) key);
			}
		}
	}

	private void holeLager(Integer key) throws Throwable {
		if (key != null) {
			lagerDto = DelegateFactory.getInstance().getLagerDelegate()
					.lagerFindByPrimaryKey(key);
		} else {
			lagerDto = null;
		}
		dto2ComponentsLager();
	}

	private void holeArtikel(Integer key) throws Throwable {
		ArtikelDto artikelDto;
		if (key != null) {
			artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(key);
		} else {
			artikelDto = null;
		}
		// identfield: 18 fuer dto2components dto reinsetzen
		wifArtikel.setArtikelDto(artikelDto);
	}

	private void holeMontageart(Integer key) throws Throwable {
		if (key != null) {
			montageartDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate().montageartFindByPrimaryKey(key);
		} else {
			montageartDto = null;
		}
		dto2ComponentsMontageart();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
				// einen neuen Eintrag anlegen oder die letzte Position wurde
				// geloescht.
				leereAlleFelder(this);
				clearStatusbar();

				// CK: Bei NEU Default-Montageart vorbesetzen
				if (defaultMontageartDto != null) {
					montageartDto = defaultMontageartDto;
					dto2ComponentsMontageart();
				}

				if (bFruehzeitigeBeschaffung == false) {
					wnfBeginnterminOffset.setInteger(0);
				}

				if (letzterArtikelIId != null) {
					wifArtikel.setArtikelDto(DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(letzterArtikelIId));
				}

			} else {
				// einen alten Eintrag laden.
				losmatDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey((Integer) key);
				dto2Components();
			}
			// Differenz
			if (wnfMenge.getBigDecimal() != null
					&& wnfAusgegeben.getBigDecimal() != null) {
				wnfDifferenzMenge.setBigDecimal(wnfMenge.getBigDecimal()
						.subtract(wnfAusgegeben.getBigDecimal()));
				BigDecimal bdSoll = wnfMenge.getBigDecimal().multiply(
						wnfSollpreis.getBigDecimal());
				BigDecimal bdIst = wnfAusgegeben.getBigDecimal().multiply(
						wnfIstpreis.getBigDecimal());
				wnfDifferenzWert.setBigDecimal(bdSoll.subtract(bdIst));
			} else {
				wnfDifferenzMenge.setBigDecimal(new BigDecimal(0));
				wnfDifferenzWert.setBigDecimal(new BigDecimal(0));
			}

		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		return false;
	}

	private void dialogQueryLager() throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(), lagerDto.getIId());
		if (lagerDto != null) {
			panelQueryFLRLager.setSelectedId(lagerDto.getIId());
		}
		new DialogQuery(panelQueryFLRLager);
	}

	private void dialogQueryMontageart() throws Throwable {
		panelQueryFLRMontageart = StuecklisteFilterFactory.getInstance()
				.createPanelFLRMontageart(getInternalFrame(), null);
		new DialogQuery(panelQueryFLRMontageart);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		// identfield: 19 optional: focus fuer die tastaturbedienung
		return wifArtikel.getWbuArtikel();
	}

	protected void focusLostWnfMenge() throws Throwable {
		if (wnfMenge.getBigDecimal() != null) {
			wnfSollmenge.setBigDecimal(wnfMenge.getBigDecimal().divide(
					getTabbedPaneLos().getLosDto().getNLosgroesse(),
					BigDecimal.ROUND_HALF_EVEN));
		} else {
			wnfSollmenge.setBigDecimal(null);
		}
	}

	public LossollmaterialDto getLossollmaterialDto() {
		return this.losmatDto;
	}
}

class PanelLosMaterial_wnfMenge_focusAdapter implements
		java.awt.event.FocusListener {
	private PanelLosMaterial adaptee;

	PanelLosMaterial_wnfMenge_focusAdapter(PanelLosMaterial adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.focusLostWnfMenge();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}
