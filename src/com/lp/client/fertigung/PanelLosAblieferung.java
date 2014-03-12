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
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Panel zum Bearbeiten der Ablieferungen eines Loses</p> <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum <I>11. 10.
 * 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.37 $
 */
public class PanelLosAblieferung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final private String ACTION_SPECIAL_ETIKETTDRUCKEN = "action_special_etikettdrucken";

	private TabbedPaneLos tabbedPaneLos = null;

	private LosablieferungDto losablieferungDto = null;

	private JPanel jPanelWorkingOn = null;
	private Border border1;

	private WrapperLabel wlaMenge = null;
	private WrapperNumberField wnfMenge = null;
	private WrapperLabel wlaEinheit1 = null;
	private WrapperLabel wlaLosgroesse = null;
	private WrapperNumberField wnfLosgroesse = null;
	private WrapperLabel wlaEinheit2 = null;
	private WrapperLabel wlaOffen = null;
	private WrapperNumberField wnfOffen = null;
	private WrapperLabel wlaEinheit3 = null;
	private WrapperLabel wlaDatum = null;
	private WrapperDateField wdfDatum = null;
	private WrapperSnrChnrField wsfSerieCharge = null;

	private WrapperLabel wlaGestehungspreis = null;
	private WrapperNumberField wnfGestehungspreis = null;
	private WrapperLabel wlaWaehrung1 = null;
	private WrapperLabel wlaArbeitszeitwert = null;
	private WrapperNumberField wnfArbeitszeitwert = null;
	private WrapperLabel wlaWaehrung2 = null;
	private WrapperLabel wlaMaterialwert = null;
	private WrapperNumberField wnfMaterialwert = null;
	private WrapperLabel wlaWaehrung3 = null;
	WrapperLabel wlagGsnr = null;
	private JScrollPane jScrollPaneGsnr = new JScrollPane();
	JEditorPane textPanelGsnr = new JEditorPane();

	private BigDecimal bdVorherigeMenge = null;
	private boolean bGeraeteseriennummern = false;

	private WrapperCheckBox wcbErledigt = null;
	private WrapperCheckBox wcbMaterialzurueckgeben = new WrapperCheckBox();

	private WrapperLabel wlaVersion = null;
	private WrapperTextField wtfVersion = null;

	// private WrapperCheckBox wcbMaterialZurueckbuchen = new WrapperCheckBox();

	public PanelLosAblieferung(InternalFrame internalFrame, String add2TitleI,
			Object key, TabbedPaneLos tabbedPaneLos) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneLos = tabbedPaneLos;
		jbInit();
		initComponents();
		initPanel();
	}

	private void initPanel() throws Throwable {
		String sMandantWaehrung = LPMain.getTheClient().getSMandantenwaehrung();
		wlaWaehrung1.setText(sMandantWaehrung);
		wlaWaehrung2.setText(sMandantWaehrung);
		wlaWaehrung3.setText(sMandantWaehrung);
	}

	private TabbedPaneLos getTabbedPaneLos() {
		return tabbedPaneLos;
	}

	private void jbInit() throws Throwable {
		jPanelWorkingOn = new JPanel();

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(new GridBagLayout());

		jPanelWorkingOn.setBorder(border1);
		jPanelWorkingOn.setLayout(new GridBagLayout());
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN)) {
			bGeraeteseriennummern = true;
		}

		// controls
		wlaMenge = new WrapperLabel();
		wnfMenge = new WrapperNumberField();
		wlaEinheit1 = new WrapperLabel();
		wlaLosgroesse = new WrapperLabel();
		wnfLosgroesse = new WrapperNumberField();
		wlaEinheit2 = new WrapperLabel();
		wlaOffen = new WrapperLabel();
		wnfOffen = new WrapperNumberField();
		wlaEinheit3 = new WrapperLabel();
		wlaDatum = new WrapperLabel();
		wdfDatum = new WrapperDateField();
		wsfSerieCharge = new WrapperSnrChnrField(getInternalFrame());
		wsfSerieCharge.setWnfBelegMenge(wnfMenge);
		wsfSerieCharge.setActivatable(false);
		wlaGestehungspreis = new WrapperLabel();
		wnfGestehungspreis = new WrapperNumberField();
		wlaWaehrung1 = new WrapperLabel();
		wlaArbeitszeitwert = new WrapperLabel();
		wnfArbeitszeitwert = new WrapperNumberField();
		wlaWaehrung2 = new WrapperLabel();
		wlaMaterialwert = new WrapperLabel();
		wnfMaterialwert = new WrapperNumberField();
		wlaWaehrung3 = new WrapperLabel();
		wcbErledigt = new WrapperCheckBox();
		wlaVersion = new WrapperLabel();
		wtfVersion = new WrapperTextField();

		wlaMenge.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaMenge.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit1.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit1.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaDatum.setMinimumSize(new Dimension(50, Defaults.getInstance()
				.getControlHeight()));
		wlaDatum.setPreferredSize(new Dimension(50, Defaults.getInstance()
				.getControlHeight()));
		wlaMenge.setText(LPMain.getTextRespectUISPr("lp.menge"));
		wlaLosgroesse.setText(LPMain.getTextRespectUISPr("label.losgroesse"));
		wlaOffen.setText(LPMain.getTextRespectUISPr("label.offen"));
		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wlaVersion.setText(LPMain.getTextRespectUISPr("lp.version"));

		wlaGestehungspreis.setText(LPMain
				.getTextRespectUISPr("fert.ablieferung.ablieferpreis"));
		wlaArbeitszeitwert.setText(LPMain
				.getTextRespectUISPr("fert.ablieferung.arbeitszeitanteil"));
		wlaMaterialwert.setText(LPMain
				.getTextRespectUISPr("fert.ablieferung.materialanteil"));

		wcbErledigt.setText(LPMain
				.getTextRespectUISPr("fert.losablieferung.erledigt"));

		wcbMaterialzurueckgeben
				.setText(LPMain
						.getTextRespectUISPr("fert.losablieferung.ueberzaehliegesmaterialzurueckbuchen"));
		wcbMaterialzurueckgeben.setSelected(true);

		wlaEinheit1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheit2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheit3.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);

		wdfDatum.setActivatable(false);
		wnfLosgroesse.setActivatable(false);
		wnfOffen.setActivatable(false);
		wnfGestehungspreis.setActivatable(false);
		wnfArbeitszeitwert.setActivatable(false);
		wnfMaterialwert.setActivatable(false);
		wnfMenge.setMandatoryFieldDB(true);
		wdfDatum.setMandatoryFieldDB(true);

		wnfMenge.setMinimumValue(0);
		wnfMenge.addFocusListener(this);

		textPanelGsnr.setFont(new java.awt.Font("monospaced", 0, 12));
		textPanelGsnr.setEnabled(false);
		jScrollPaneGsnr.getViewport().add(textPanelGsnr);
		wlagGsnr = new WrapperLabel(
				LPMain.getTextRespectUISPr("fert.geraeteseriennummern") + ":");
		wlagGsnr.setHorizontalAlignment(SwingConstants.LEFT);
		wnfMenge.setFractionDigits(0);
		wnfLosgroesse.setFractionDigits(0);
		wnfOffen.setFractionDigits(0);

		wnfGestehungspreis.setFractionDigits(4);
		wnfArbeitszeitwert.setFractionDigits(4);
		wnfMaterialwert.setFractionDigits(4);

		// wcbMaterialZurueckbuchen.setText(LPMain.getTextRespectUISPr(
		// "fert.losablieferung.ueberzaehliegesmaterialzurueckbuchen"));

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaEinheit1, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 0));
		jPanelWorkingOn.add(wlaDatum, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfDatum, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaLosgroesse, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfLosgroesse, new GridBagConstraints(1, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaEinheit2, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlagGsnr, new GridBagConstraints(3, iZeile, 2, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaOffen, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfOffen, new GridBagConstraints(1, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaEinheit3, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jScrollPaneGsnr.setVisible(false);
		jPanelWorkingOn.add(jScrollPaneGsnr, new GridBagConstraints(3, iZeile,
				2, 6, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 300, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaGestehungspreis, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfGestehungspreis, new GridBagConstraints(1,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrung1, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaMaterialwert, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMaterialwert, new GridBagConstraints(1, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrung2, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaArbeitszeitwert, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfArbeitszeitwert, new GridBagConstraints(1,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrung3, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wsfSerieCharge.getButtonSnrAuswahl(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wsfSerieCharge, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaVersion, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfVersion, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FERT_DARF_LOS_ERLEDIGEN)) {
			iZeile++;
			jPanelWorkingOn.add(wcbErledigt, new GridBagConstraints(1, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jPanelWorkingOn.add(wcbMaterialzurueckgeben, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		wcbMaterialzurueckgeben.setVisible(false);

		this.createAndSaveAndShowButton("/com/lp/client/res/printer216x16.png",
				LPMain.getTextRespectUISPr("artikel.report.etikett"),
				ACTION_SPECIAL_ETIKETTDRUCKEN, null);

	}

	public void istStuecklisteDokumentenpflichtig() throws Throwable {
		wcbErledigt.setActivatable(true);
		wcbErledigt.setText(LPMain
				.getTextRespectUISPr("fert.losablieferung.erledigt"));
		if (getTabbedPaneLos().getLosDto().getStuecklisteIId() != null) {

			StuecklisteDto stklDto = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(
							getTabbedPaneLos().getLosDto().getStuecklisteIId());
			ArtikelDto aDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(stklDto.getArtikelIId());
			if (Helper.short2boolean(aDto.getBDokumentenpflicht())) {
				wcbErledigt.setSelected(false);
				wcbErledigt.setText(LPMain
						.getTextRespectUISPr("fert.stkl.dokumentenpflichtig"));
				wcbErledigt.setActivatable(false);
			}
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		// In Delegate-Methode auslagern, wenn wieder etwas geaendert wird
		// Betrifft auch "Losablieferung per Auftragsnummer"

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (losablieferungDto != null) {
				if (losablieferungDto.getNMenge().doubleValue() <= 0.0) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("rechnung.hint.mengegroessernull"));
					return;
				}
				boolean bSpeichern;

				if (losablieferungDto.getIId() == null) {
					// PJ 16622
					if (bGeraeteseriennummern == true) {

						if (getTabbedPaneLos().getLosDto().getStuecklisteIId() != null) {

							StuecklisteDto stklDto = DelegateFactory
									.getInstance()
									.getStuecklisteDelegate()
									.stuecklisteFindByPrimaryKey(
											getTabbedPaneLos().getLosDto()
													.getStuecklisteIId());
							ArtikelDto aDto = DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikelFindByPrimaryKey(
											stklDto.getArtikelIId());
							if (Helper
									.short2boolean(aDto.getBSeriennrtragend())) {

								DialogGeraeteseriennummern d = new DialogGeraeteseriennummern(
										LPMain.getTextRespectUISPr("fert.geraeteseriennummern"),
										losablieferungDto.getLosIId());

								if (wnfMenge.getBigDecimal().doubleValue() != 1
										&& d.bSnrBehafteteArtikelVorhanden == true) {
									// Fehler-> Menge muss bei
									// Geraeteseriennummern
									// immer = 1 sein
									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.hint"),
													LPMain.getTextRespectUISPr("fert.geraeteseriennummern.error2"));
									return;
								}

								if (d.bSnrBehafteteArtikelVorhanden == true) {
									LPMain.getInstance()
											.getDesktop()
											.platziereDialogInDerMitteDesFensters(
													d);
									d.setVisible(true);
									if (d.bAbgebrochen == false) {
										losablieferungDto
												.getSeriennrChargennrMitMenge()
												.get(0)
												.setAlGeraetesnr(d.alGeraetesnr);
									} else {
										return;
									}
								}
							}
						}
					}

					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG,
									ParameterFac.KATEGORIE_FERTIGUNG,
									LPMain.getInstance().getTheClient()
											.getMandant());
					if (((Boolean) parameter.getCWertAsObject()) == false) {

						parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN,
										ParameterFac.KATEGORIE_FERTIGUNG,
										LPMain.getInstance().getTheClient()
												.getMandant());

						if ((Boolean) parameter.getCWertAsObject()) {
							DelegateFactory
									.getInstance()
									.getFertigungDelegate()
									.bucheMaterialAufLos(
											getTabbedPaneLos().getLosDto(),
											losablieferungDto.getNMenge(),
											false,
											false,
											true,
											getTabbedPaneLos()
													.getAbzubuchendeSeriennrChargen(
															losablieferungDto
																	.getNMenge()));

						} else {
							if (getTabbedPaneLos().getLosDto()
									.getStuecklisteIId() != null) {
								StuecklisteDto stklDto = DelegateFactory
										.getInstance()
										.getStuecklisteDelegate()
										.stuecklisteFindByPrimaryKey(
												getTabbedPaneLos().getLosDto()
														.getStuecklisteIId());
								if (Helper.short2Boolean(stklDto
										.getBMaterialbuchungbeiablieferung()) == true) {
									DelegateFactory
											.getInstance()
											.getFertigungDelegate()
											.bucheMaterialAufLos(
													getTabbedPaneLos()
															.getLosDto(),
													losablieferungDto
															.getNMenge(),
													false,
													false,
													true,
													getTabbedPaneLos()
															.getAbzubuchendeSeriennrChargen(
																	losablieferungDto
																			.getNMenge()));
								}
							}
						}
					}
				}

				// auf Ueberlieferung pruefen
				BigDecimal bdBereitseErledigt = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.getErledigteMenge(losablieferungDto.getLosIId());
				BigDecimal bdGesamt = null;

				if (losablieferungDto.getIId() == null) {
					bdGesamt = bdBereitseErledigt.add(losablieferungDto
							.getNMenge());
				} else {
					bdGesamt = bdBereitseErledigt.add(losablieferungDto
							.getNMenge().subtract(bdVorherigeMenge));
				}

				if (bdGesamt.compareTo(getTabbedPaneLos().getLosDto()
						.getNLosgroesse()) > 0) {
					String[] optionen = null;

					boolean bLosUeberlieferbar = false;
					if (getTabbedPaneLos().getLosDto().getStuecklisteIId() != null) {
						StuecklisteDto stklDto = DelegateFactory
								.getInstance()
								.getStuecklisteDelegate()
								.stuecklisteFindByPrimaryKey(
										getTabbedPaneLos().getLosDto()
												.getStuecklisteIId());
						if (Helper.short2Boolean(stklDto.getBUeberlieferbar()) == true) {
							bLosUeberlieferbar = true;
						}
					}

					if (bLosUeberlieferbar == true) {
						optionen = new String[3];
						optionen[0] = LPMain.getTextRespectUISPr("lp.ja");
						optionen[1] = LPMain.getTextRespectUISPr("lp.nein");
						optionen[2] = LPMain
								.getTextRespectUISPr("fert.los.abliefern.option.losgroesseaendern");

					} else {
						optionen = new String[2];
						optionen[0] = LPMain.getTextRespectUISPr("lp.ja");
						optionen[1] = LPMain.getTextRespectUISPr("lp.nein");
					}

					String sText = LPMain
							.getTextRespectUISPr("fert.losueberliefern");
					int choice = JOptionPane.showOptionDialog(this, sText,
							LPMain.getTextRespectUISPr("lp.frage"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, optionen,
							optionen[1]);

					switch (choice) {
					case JOptionPane.YES_OPTION: {
						bSpeichern = true;
					}
						break;
					case JOptionPane.NO_OPTION: {
						return;
					}

					case 2: {

						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.aendereLosgroesse(
										getTabbedPaneLos().getLosDto().getIId(),
										bdGesamt.intValue(), false);

						bSpeichern = true;
					}
						break;
					default: {
						return;
					}
					}
				} else {
					bSpeichern = true;
				}

				// Sollsatzgroessen pruefen
				try {
					if (losablieferungDto.getIId() != null) {
						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.pruefePositionenMitSollsatzgroesseUnterschreitung(
										getTabbedPaneLos().getLosDto().getIId(),
										losablieferungDto.getNMenge().subtract(
												bdVorherigeMenge));
					} else {
						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.pruefePositionenMitSollsatzgroesseUnterschreitung(
										getTabbedPaneLos().getLosDto().getIId(),
										losablieferungDto.getNMenge());
					}

					bSpeichern = true;
				} catch (ExceptionLP ex1) {
					if (ex1.getICode() == EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN) {
						if (DelegateFactory
								.getInstance()
								.getTheJudgeDelegate()
								.hatRecht(
										RechteFac.RECHT_FERT_ABLIEFERUNG_UNTER_SOLLSATZGROESSE_ERLAUBT)) {

							StringBuffer sText = (StringBuffer) ex1
									.getAlInfoForTheClient().get(0);

							sText.append("\r\n")
									.append(LPMain
											.getTextRespectUISPr("fert.frage.trotzdemabliefern"));
							JTextArea area = new JTextArea(sText.toString());
							area.setRows(30);
							area.setColumns(50);
							area.setLineWrap(true);
							JScrollPane pane = new JScrollPane(area);
							//
							int choice = JOptionPane.showConfirmDialog(null,
									pane, "Test", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);

							switch (choice) {
							case JOptionPane.YES_OPTION: {
								bSpeichern = true;
							}
								break;
							case JOptionPane.NO_OPTION: {
								bSpeichern = false;
							}
								break;
							default: {
								bSpeichern = false;
							}
							}

						} else {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("fert.sollsatzgroesseunterschritten.error"));
							bSpeichern = false;
						}
					} else {
						handleException(ex1, false);
						return;
					}
				}

				if (bSpeichern) {

					if (!DelegateFactory.getInstance().getTheJudgeDelegate()
							.hatRecht(RechteFac.RECHT_FERT_DARF_LOS_ERLEDIGEN)) {
						wcbErledigt.setSelected(false);
					}

					if (wcbErledigt.isSelected()) {

						String s = DelegateFactory
								.getInstance()
								.getZeiterfassungDelegate()
								.istBelegGeradeInBearbeitung(
										LocaleFac.BELEGART_LOS,
										losablieferungDto.getLosIId());

						if (s != null) {
							boolean b = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr("fert.los.erledigen.losgeradeinbearbeitung")
													+ s
													+ LPMain.getTextRespectUISPr("fert.los.erledigen.losgeradeinbearbeitung1"));

							if (b == false) {
								return;
							}

						}

						// PJ 17831
						if (bdGesamt.doubleValue() < getTabbedPaneLos()
								.getLosDto().getNLosgroesse().doubleValue()) {

							String[] optionen = new String[3];
							optionen[0] = LPMain.getTextRespectUISPr("lp.ja");
							optionen[1] = LPMain.getTextRespectUISPr("lp.nein");
							optionen[2] = LPMain
									.getTextRespectUISPr("fert.los.abliefern.option.losgroessereduzieren");

							String sText = LPMain
									.getTextRespectUISPr("fert.losunterliefern");
							int choice = JOptionPane.showOptionDialog(this,
									sText,
									LPMain.getTextRespectUISPr("lp.frage"),
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									optionen, optionen[1]);

							switch (choice) {
							case JOptionPane.YES_OPTION: {
								// WEITER
							}
								break;
							case JOptionPane.NO_OPTION: {
								return;
							}

							case 2: {

								DelegateFactory
										.getInstance()
										.getFertigungDelegate()
										.aendereLosgroesse(
												getTabbedPaneLos().getLosDto()
														.getIId(),
												bdGesamt.intValue(), true);

							}
								break;

							}
						}

					}

					if (losablieferungDto.getIId() == null) {

						LosablieferungDto savedDto = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.createLosablieferung(losablieferungDto,
										wcbErledigt.isSelected());
						DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.versionInLagerbewegungUpdaten(
								LocaleFac.BELEGART_LOSABLIEFERUNG,
								savedDto.getIId(), wtfVersion.getText());
						this.losablieferungDto = savedDto;
						setKeyWhenDetailPanel(losablieferungDto.getIId());
						super.eventActionSave(e, true);
						// los refresh
						getTabbedPaneLos().reloadLosDto();
						// jetz den anzeigen

					

						eventYouAreSelected(false);

						// Fehlmengen aufloesen, wenn keine Materialliste.
						if (getTabbedPaneLos().getStuecklisteDto() != null) {

							FehlmengenAufloesen.fehlmengenAufloesen(
									getInternalFrame(), getTabbedPaneLos()
											.getStuecklisteDto()
											.getArtikelIId(),
									getTabbedPaneLos().getLosDto()
											.getLagerIIdZiel(), wsfSerieCharge
											.getSeriennummern(),
									losablieferungDto.getNMenge());
						}
					} else {
						boolean bMaterialZurueckgeben = false;
						ParametermandantDto parameterMzu = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN,
										ParameterFac.KATEGORIE_FERTIGUNG,
										LPMain.getInstance().getTheClient()
												.getMandant());

						if ((Boolean) parameterMzu.getCWertAsObject()) {
							if (wcbMaterialzurueckgeben.isSelected()) {
								bMaterialZurueckgeben = true;
							}
						}

						this.losablieferungDto = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.updateLosablieferung(losablieferungDto,
										bMaterialZurueckgeben);
						DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.versionInLagerbewegungUpdaten(
								LocaleFac.BELEGART_LOSABLIEFERUNG,
								losablieferungDto.getIId(),
								wtfVersion.getText());
						setKeyWhenDetailPanel(losablieferungDto.getIId());
						super.eventActionSave(e, true);
						// los refresh
						getTabbedPaneLos().reloadLosDto();
						// jetz den anzeigen

						

						eventYouAreSelected(false);
						wcbErledigt.setVisible(true);
						wcbMaterialzurueckgeben.setVisible(false);

					}
				}
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
		if (losablieferungDto == null) {
			losablieferungDto = new LosablieferungDto();
			losablieferungDto
					.setLosIId(getTabbedPaneLos().getLosDto().getIId());
		}
		// Materialliste?
		if (getTabbedPaneLos().getStuecklisteDto() != null) {
			ArtikelDto artikelDto = getTabbedPaneLos().getStuecklisteDto()
					.getArtikelDto();
			if (Helper.short2boolean(artikelDto.getBChargennrtragend())
					|| Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
				if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
					wnfMenge.setBigDecimal(wsfSerieCharge.getMenge());
				}

				losablieferungDto.setSeriennrChargennrMitMenge(wsfSerieCharge
						.getSeriennummern());
			} else {
				losablieferungDto.setSeriennrChargennrMitMenge(null);
			}
		} else {
			losablieferungDto.setSeriennrChargennrMitMenge(null);
		}
		losablieferungDto.setNMenge(wnfMenge.getBigDecimal());
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		if (losablieferungDto != null) {
			wnfMenge.setBigDecimal(losablieferungDto.getNMenge());
			wnfArbeitszeitwert.setBigDecimal(losablieferungDto
					.getNArbeitszeitwert());
			wnfGestehungspreis.setBigDecimal(losablieferungDto
					.getNGestehungspreis());
			wnfMaterialwert.setBigDecimal(losablieferungDto.getNMaterialwert());
			wdfDatum.setDate(losablieferungDto.getTAendern());

			Integer artikelIId = null;
			if (getTabbedPaneLos().getLosDto().getStuecklisteIId() != null) {

				StuecklisteDto stklDto = DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(
								getTabbedPaneLos().getLosDto()
										.getStuecklisteIId());
				ArtikelDto aDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(stklDto.getArtikelIId());
				artikelIId = aDto.getIId();
			}

			wsfSerieCharge.setSeriennummern(losablieferungDto
					.getSeriennrChargennrMitMenge(), artikelIId,
					getTabbedPaneLos().getLosDto().getLagerIIdZiel());
			setStatusbarPersonalIIdAendern(losablieferungDto
					.getPersonalIIdAendern());
			setStatusbarTAendern(losablieferungDto.getTAendern());
			wcbErledigt.setSelected(getTabbedPaneLos().getLosDto()
					.getStatusCNr().equals(FertigungFac.STATUS_ERLEDIGT));
			bdVorherigeMenge = losablieferungDto.getNMenge();

			if (bGeraeteseriennummern
					&& losablieferungDto.getSeriennrChargennrMitMenge() != null
					&& losablieferungDto.getSeriennrChargennrMitMenge().size() > 0) {
				GeraetesnrDto[] gsnrDtos = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getGeraeteseriennummerEinerLagerbewegung(
								LocaleFac.BELEGART_LOSABLIEFERUNG,
								losablieferungDto.getIId(),
								losablieferungDto
										.getSeriennrChargennrMitMenge().get(0)
										.getCSeriennrChargennr());

				if (gsnrDtos.length > 0) {
					String s = "";
					byte[] CRLFAscii = { 13, 10 };

					for (int i = 0; i < gsnrDtos.length; i++) {

						ArtikelDto artikelDto = DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.artikelFindByPrimaryKey(
										gsnrDtos[i].getArtikelIId());

						s += Helper.fitString2Length(
								artikelDto.formatArtikelbezeichnung(), 25, ' ')
								+ " = "
								+ gsnrDtos[i].getCSnr()
								+ new String(CRLFAscii);

						textPanelGsnr.setText(s);
					}

				} else {
					textPanelGsnr.setText("");
				}

				// Erste gefundene Version anzeigen
				LagerbewegungDto[] lDto = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.lagerbewegungFindByBelegartCNrBelegartPositionIId(
								LocaleFac.BELEGART_LOSABLIEFERUNG,
								losablieferungDto.getIId());
				if (lDto != null && lDto.length > 0) {
					wtfVersion.setText(lDto[0].getCVersion());
				}

			}
		} else {
			textPanelGsnr.setText("");
			wsfSerieCharge.setSeriennummern(null, (Integer) null, null);
		}
		setStatusbarStatusCNr(getTabbedPaneLos().getLosDto().getStatusCNr());
	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		losablieferungDto = null;
		this.leereAlleFelder(this);
		clearStatusbar();
		// heute
		wdfDatum.setDate(new java.sql.Date(System.currentTimeMillis()));
		// mit offener menge vorbesetzen
		BigDecimal bdErledigt = DelegateFactory.getInstance()
				.getFertigungDelegate()
				.getErledigteMenge(getTabbedPaneLos().getLosDto().getIId());

		// Wenn ueberliefert, dann kein Vorschlag
		if (getTabbedPaneLos().getLosDto().getNLosgroesse()
				.subtract(bdErledigt).doubleValue() > 0) {

			wnfMenge.setBigDecimal(getTabbedPaneLos().getLosDto()
					.getNLosgroesse().subtract(bdErledigt));
		}

		wcbErledigt.setSelected(true);

		if (getTabbedPaneLos().getStuecklisteDto() != null) {
			ArtikelDto artikelDto = getTabbedPaneLos().getStuecklisteDto()
					.getArtikelDto();
			if (Helper.short2boolean(artikelDto.getBChargennrtragend())
					|| Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
				wcbErledigt.setSelected(false);
			}
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (getTabbedPaneLos().getPanelQueryAblieferung(true).getSelectedIds() != null) {

			if (!isLockedDlg()) {
				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.removeLosablieferung(
								getTabbedPaneLos().getPanelQueryAblieferung(
										true).getSelectedIds());
				this.losablieferungDto = null;
				this.leereAlleFelder(this);
				super.eventActionDelete(e, false, false);
			}

		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_LOS;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ETIKETTDRUCKEN)) {
			ReportAblieferetikett reportEtikett = new ReportAblieferetikett(
					getInternalFrame(), losablieferungDto.getIId(), "");
			getInternalFrame().showReportKriterien(reportEtikett, false);
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
		// ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {

			wlaVersion.setVisible(false);
			wtfVersion.setVisible(false);
			Object key = getKeyWhenDetailPanel();
			if (key == null || key.equals(LPMain.getLockMeForNew())) {
				// einen neuen Eintrag anlegen oder die letzte Position wurde
				// geloescht.
			} else {
				// einen alten Eintrag laden.
				losablieferungDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losablieferungFindByPrimaryKey((Integer) key, true);
				dto2Components();
			}
			wnfLosgroesse.setBigDecimal(getTabbedPaneLos().getLosDto()
					.getNLosgroesse());
			// offene menge
			BigDecimal bdErledigt = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.getErledigteMenge(getTabbedPaneLos().getLosDto().getIId());
			wnfOffen.setBigDecimal(getTabbedPaneLos().getLosDto()
					.getNLosgroesse().subtract(bdErledigt));

			if (getTabbedPaneLos().getStuecklisteDto() != null) {
				if (key != null && key.equals(LPMain.getLockMeForNew())) {
					wsfSerieCharge.setSeriennummern(null, getTabbedPaneLos()
							.getStuecklisteDto().getArtikelDto(),
							getTabbedPaneLos().getLosDto().getLagerIIdZiel());
				}

				String sEinheit = getTabbedPaneLos().getStuecklisteDto()
						.getArtikelDto().getEinheitCNr().trim();
				wlaEinheit1.setText(sEinheit);
				wlaEinheit2.setText(sEinheit);
				wlaEinheit3.setText(sEinheit);

				String sMandantWaehrung = LPMain.getTheClient()
						.getSMandantenwaehrung();
				wlaWaehrung1.setText(sMandantWaehrung + "/ " + sEinheit);
				wlaWaehrung2.setText(sMandantWaehrung + "/ " + sEinheit);
				wlaWaehrung3.setText(sMandantWaehrung + "/ " + sEinheit);

				if (Helper.short2boolean(getTabbedPaneLos().getStuecklisteDto()
						.getArtikelDto().getBChargennrtragend())) {
					wsfSerieCharge.getButtonSnrAuswahl().setVisible(true);
					wsfSerieCharge
							.getButtonSnrAuswahl()
							.setText(
									LPMain.getTextRespectUISPr("lp.chargennummer_lang"));
					wsfSerieCharge.setVisible(true);
					wsfSerieCharge.setMandatoryField(true);
					wnfMenge.setActivatable(false);
				} else if (Helper.short2boolean(getTabbedPaneLos()
						.getStuecklisteDto().getArtikelDto()
						.getBSeriennrtragend())) {
					wnfMenge.setActivatable(false);
					wsfSerieCharge.getButtonSnrAuswahl().setVisible(true);
					wsfSerieCharge
							.getButtonSnrAuswahl()
							.setText(
									LPMain.getTextRespectUISPr("bes.seriennummer_short"));
					wsfSerieCharge.setVisible(true);
					wsfSerieCharge.setMandatoryField(true);
					if (bGeraeteseriennummern == true) {

						wlagGsnr.setVisible(true);
						jScrollPaneGsnr.setVisible(true);
					} else {
						wlagGsnr.setVisible(false);
						jScrollPaneGsnr.setVisible(false);
					}

					wlaVersion.setVisible(true);
					wtfVersion.setVisible(true);

					// PJ 16250 Seriennummer vorschlagen
					if (key != null && key.equals(LPMain.getLockMeForNew())) {
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_SERIENNUMMERNGENERATOR,
										ParameterFac.KATEGORIE_FERTIGUNG,
										LPMain.getInstance().getTheClient()
												.getMandant());
						if (((Integer) parameter.getCWertAsObject()) == 1) {
							try {

								wsfSerieCharge.setText(DelegateFactory
										.getInstance()
										.getLagerDelegate()
										.getNaechsteSeriennummer(
												getTabbedPaneLos()
														.getStuecklisteDto()
														.getArtikelDto()
														.getIId(),
												getTabbedPaneLos().getLosDto()
														.getLagerIIdZiel()));
								wnfMenge.setBigDecimal(new BigDecimal(1));
								focusLost(new FocusEvent(wnfMenge, 0));

							} catch (ExceptionLP e) {

								if (e.getICode() == EJBExceptionLP.FEHLER_SERIENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN) {
									String sMsg = LPMain
											.getTextRespectUISPr("fert.seriennummerngenerator.error")
											+ ": "
											+ e.getAlInfoForTheClient().get(0);
									DialogFactory.showModalDialog(LPMain
											.getTextRespectUISPr("lp.error"),
											sMsg);
								} else {
									throw e;
								}

							}
						}
					}

				} else {
					wnfMenge.setActivatable(true);
					wsfSerieCharge.getButtonSnrAuswahl().setVisible(false);
					wsfSerieCharge.setVisible(false);

					wsfSerieCharge.setMandatoryField(false);
					wlagGsnr.setVisible(false);
					jScrollPaneGsnr.setVisible(false);
				}
			} else {
				wnfMenge.setActivatable(true);
				wsfSerieCharge.getButtonSnrAuswahl().setVisible(false);
				wsfSerieCharge.setVisible(false);

				wsfSerieCharge.setMandatoryField(false);
				wlagGsnr.setVisible(false);
				jScrollPaneGsnr.setVisible(false);
			}

		}

		istStuecklisteDokumentenpflichtig();

	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		super.eventActionUnlock(e);

		wcbMaterialzurueckgeben.setVisible(false);
		wcbErledigt.setVisible(true);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		if (getTabbedPaneLos().getLosDto().getStatusCNr()
				.equals(FertigungFac.STATUS_STORNIERT)) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception(getTabbedPaneLos().getLosDto().getCNr()
							+ " ist storniert"));
		}
		if (getTabbedPaneLos().getLosDto().getStatusCNr()
				.equals(FertigungFac.STATUS_ANGELEGT)) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					new Exception(getTabbedPaneLos().getLosDto().getCNr()
							+ " ist noch nicht ausgegeben"));
		}
		if (getTabbedPaneLos().getLosDto().getStatusCNr()
				.equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception(getTabbedPaneLos().getLosDto().getCNr()
							+ " ist erledigt"));
		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_BEI_LOS_ERLEDIGEN_MATERIAL_NACHBUCHEN,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getInstance().getTheClient().getMandant());
		wcbMaterialzurueckgeben.setSelected(true);
		if ((Boolean) parameter.getCWertAsObject()) {
			wcbMaterialzurueckgeben.setVisible(true);
		} else {
			wcbMaterialzurueckgeben.setVisible(false);
		}

	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		if (exfc.getICode() == EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getMsg(exfc));
			return true;
		} else {
			return false;
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfMenge;
	}

	public void focusLost(FocusEvent e) {
		if (e.getSource().equals(wnfMenge)) {
			try {
				if (wnfMenge.getBigDecimal() != null) {
					if (getTabbedPaneLos().getLosDto().getStatusCNr()
							.equals(FertigungFac.STATUS_ERLEDIGT)
							|| wnfMenge.getBigDecimal().compareTo(
									wnfOffen.getBigDecimal()) >= 0) {
						wcbErledigt.setSelected(true);
					} else {
						wcbErledigt.setSelected(false);
					}
				} else {
					wcbErledigt.setSelected(false);
				}
			} catch (ExceptionLP e1) {
				handleException(e1, true);
			}
		}
	}
}
