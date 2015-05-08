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
package com.lp.client.artikel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>In diesem Fenster wird eine VK-Staffelmenge fuer einen bestimmten Zeitraum
 * erfasst. <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 12.06.2006</p> <p> </p>
 * 
 * @author uli walch
 * 
 * @version $Revision: 1.25 $
 */
public class PanelVkpfStaffelmenge extends PanelBasis implements
		PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jpaWorkingOn = null;

	private InternalFrameArtikel internalFrameArtikel = null;

	private VkpfMengenstaffelDto vkpfStaffelmengeDto = null;

	private JButton wbuTagZurueck = new JButton();
	private JButton wbuNaechsterTag = new JButton();

	// Preisgueltigkeitsanzeige ab legt fest, welche Preise im Panel angezeigt
	// werden
	private WrapperLabel wlaPreisgueltigkeitsanzeigeab = null;
	private WrapperDateField wdfPreisgueltigkeitsanzeigeab =  new WrapperDateField();
	private Date datGueltigkeitsanzeigeab = null;

	private WrapperLabel wlaGueltigab = null;
	private WrapperDateField wdfGueltigab = new WrapperDateField();

	private WrapperDateField wdfGueltigbis =  new WrapperDateField();
	private WrapperLabel wlaGueltigbis = null;

	private WrapperLabel wlaGestehungspreis = null;
	private WrapperNumberField wnfGestehungspreis = null;
	private WrapperLabel wlaWaehrungGestehungspreis = null;
	private WrapperLabel wlaMinverkaufspreis = null;
	private WrapperNumberField wnfMinverkaufspreis = null;
	private WrapperLabel wlaWaehrungMinverkaufspreis = null;

	private WrapperComboBox wcoVkbasis = new WrapperComboBox();
	private WrapperNumberField wnfVkbasis = null;
	private WrapperLabel wlaVkbasiswaehrung = null;
	private WrapperLabel wlaVkbasisGueltigab = null;
	private WrapperDateField wdfVkbasisGueltigab = null;

	private WrapperLabel wlaMenge = null;
	private WrapperLabel wlaFixpreis = null;
	private WrapperLabel wlaRabattsatz = null;
	private WrapperLabel wlaBerechneterpreis = null;

	// diese Zeile traegt die Formatierung
	private WrapperNumberField wnfMenge = null;
	private WrapperLabel wlaMengeeinheit = null;
	private WrapperNumberField wnfFixpreis = null;
	private WrapperLabel wlaFixpreiswaehrung = null;
	private WrapperNumberField wnfRabattsatz = null;
	private WrapperLabel wlaRabattsatzprozent = null;
	private WrapperNumberField wnfBerechneterpreis = null;
	private WrapperLabel wlaBerechneterpreiswaehrung = null;

	/**
	 * Die Anzahl der Nachkommastellen fuer Preisfelder kommt aus
	 * Mandantparameter.
	 */
	private int iPreiseUINachkommastellen = -1;
	/**
	 * Die Anzahl der Nachkommastellen fuer Mengenfelder kommt aus
	 * Mandantparameter.
	 */
	private int iMengeUINachkommastellen = -1;
	/** Die Waehrung des Mandanten. */
	private String mandantenwaehrungCNr = null;

	private final boolean vkPreisBasisLief1Preis;
	private int preisbasisVerkauf = 0;

	public PanelVkpfStaffelmenge(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		internalFrameArtikel = (InternalFrameArtikel) internalFrame;

		ParametermandantDto p = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);
		if (p != null && p.getCWert().equals("1")) {
			vkPreisBasisLief1Preis = true;
		} else {
			vkPreisBasisLief1Preis = false;
		}

		p = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_PREISBASIS_VERKAUF);

		preisbasisVerkauf = (Integer) p.getCWertAsObject();

		initPanel();
		jbInit();
		initComponents();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout
		setLayout(new GridBagLayout());

		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = null;
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF)) {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD, };

		} else {
			aWhichButtonIUse = new String[] {};
		}

		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		jpaWorkingOn = new JPanel();
		jpaWorkingOn.setLayout(new GridBagLayout());

		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wlaPreisgueltigkeitsanzeigeab = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("vkpf.preisgueltigkeitsanzeigeab"));
		wdfPreisgueltigkeitsanzeigeab = new WrapperDateField();
		wdfPreisgueltigkeitsanzeigeab.setMandatoryField(true);
		wdfPreisgueltigkeitsanzeigeab.getDisplay().addPropertyChangeListener(
				this);
		wdfPreisgueltigkeitsanzeigeab.setActivatable(false);

		wlaGueltigab = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.gueltigab"));
		wdfGueltigab = new WrapperDateField();
		wdfGueltigab.setMandatoryField(true);
		wdfGueltigab.getDisplay().addPropertyChangeListener(this);
		wlaGueltigbis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.gueltigbis"));
		wdfGueltigbis = new WrapperDateField();

		wlaGestehungspreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.gestehungspreis"));
		wnfGestehungspreis = new WrapperNumberField();
		
		
		int iNachkommastellenGestpreis=Defaults.getInstance().getIUINachkommastellenPreiseVK();
		if(Defaults.getInstance().getIUINachkommastellenPreiseEK()>Defaults.getInstance().getIUINachkommastellenPreiseVK()){
			iNachkommastellenGestpreis=Defaults.getInstance().getIUINachkommastellenPreiseEK();
		}
		wnfGestehungspreis.setFractionDigits(iNachkommastellenGestpreis);
		
		
		wnfGestehungspreis.setActivatable(false);
		wlaWaehrungGestehungspreis = new WrapperLabel(mandantenwaehrungCNr);
		wlaWaehrungGestehungspreis
				.setHorizontalAlignment(SwingConstants.LEADING);

		wlaMinverkaufspreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.minverkaufspreisshort"));
		wnfMinverkaufspreis = new WrapperNumberField();
		wnfMinverkaufspreis.setActivatable(false);

		wlaWaehrungMinverkaufspreis = new WrapperLabel(mandantenwaehrungCNr);
		wlaWaehrungMinverkaufspreis
				.setHorizontalAlignment(SwingConstants.LEADING);
		wcoVkbasis.setMandatoryField(true);
		wcoVkbasis.setMap(DelegateFactory.getInstance()
				.getVkPreisfindungDelegate()
				.getAlleAktivenPreislistenMitVkPreisbasis());

		if (vkPreisBasisLief1Preis
				|| (preisbasisVerkauf == 0 || preisbasisVerkauf == 2)) {
			wcoVkbasis.setActivatable(false);
		} else {
			wcoVkbasis
					.addActionListener(new PanelVkpfStaffelmenge_wcoVkbasis_actionAdapter(
							this));
		}
		wnfVkbasis = new WrapperNumberField();
		
		if(vkPreisBasisLief1Preis){
			wnfVkbasis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		} else {
			wnfVkbasis.setFractionDigits(iPreiseUINachkommastellen);
		}
		
		
		wnfVkbasis.setActivatable(false);
		wlaVkbasiswaehrung = new WrapperLabel(mandantenwaehrungCNr);
		wlaVkbasiswaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		wlaVkbasisGueltigab = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("vkpf.vkbasisgueltigab"));
		wdfVkbasisGueltigab = new WrapperDateField();
		wdfVkbasisGueltigab.setActivatable(false);
		if (vkPreisBasisLief1Preis) {
			wlaVkbasisGueltigab.setVisible(false);
			wdfVkbasisGueltigab.setVisible(false);
		}

		wbuTagZurueck.setText("<");
		wbuTagZurueck
				.addActionListener(new PanelVkpfStaffelmenge_wbuTagZurueck_actionAdapter(
						this));
		wbuTagZurueck.setMaximumSize(new Dimension(25, 21));
		wbuTagZurueck.setMinimumSize(new Dimension(25, 21));
		wbuTagZurueck.setPreferredSize(new Dimension(25, 21));

		wbuNaechsterTag.setText(">");
		wbuNaechsterTag
				.addActionListener(new PanelVkpfStaffelmenge_wbuNaechsterTag_actionAdapter(
						this));
		wbuNaechsterTag.setMaximumSize(new Dimension(25, 21));
		wbuNaechsterTag.setMinimumSize(new Dimension(25, 21));
		wbuNaechsterTag.setPreferredSize(new Dimension(25, 21));

		wnfMenge = new WrapperNumberField();
		wnfMenge.setFractionDigits(iMengeUINachkommastellen);
		wnfMenge.setMandatoryField(true);
		// wnfMenge.setMinimumValue(new BigDecimal(0.001));
		wnfMenge.setMinimumValue(BigDecimal.valueOf(0.001));
		HelperClient.setDefaultsToComponent(wnfMenge, 60);
		wlaMengeeinheit = new WrapperLabel();

		wlaMengeeinheit.setHorizontalAlignment(SwingConstants.LEADING);
		HelperClient.setDefaultsToComponent(wlaMengeeinheit, 30);

		wlaMenge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.menge"));
		wlaMenge.setHorizontalAlignment(SwingConstants.LEADING);
		wlaMenge.setVerticalAlignment(SwingConstants.BOTTOM);
		wlaFixpreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.fixpreis"));
		wlaFixpreis.setHorizontalAlignment(SwingConstants.LEADING);
		wlaFixpreis.setVerticalAlignment(SwingConstants.BOTTOM);
		wlaRabattsatz = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.rabattsumme"));
		wlaRabattsatz.setHorizontalAlignment(SwingConstants.LEADING);
		wlaRabattsatz.setVerticalAlignment(SwingConstants.BOTTOM);
		wlaBerechneterpreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.berechneterpreis"));
		wlaBerechneterpreis.setHorizontalAlignment(SwingConstants.LEADING);
		wlaBerechneterpreis.setVerticalAlignment(SwingConstants.BOTTOM);

		wnfFixpreis = new WrapperNumberField();
		wnfFixpreis.setFractionDigits(iPreiseUINachkommastellen);
		wnfFixpreis.setDependenceField(true);
		wnfFixpreis
				.addFocusListener(new PanelVkpfStaffelmenge_fixpreis_focusAdapter(
						this));
		HelperClient.setDefaultsToComponent(wnfFixpreis, 60);

		wlaFixpreiswaehrung = new WrapperLabel(mandantenwaehrungCNr);
		HelperClient.setDefaultsToComponent(wlaFixpreiswaehrung, 25);
		wlaFixpreiswaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wnfRabattsatz = new WrapperNumberField();
		wnfRabattsatz.setFractionDigits(2); // 2 Nachkommastellen
		wnfRabattsatz.setMandatoryField(true);
		wnfRabattsatz.setDependenceField(true);
		wnfRabattsatz
				.addFocusListener(new PanelVkpfStaffelmenge_rabattsatz_focusAdapter(
						this));
		HelperClient.setDefaultsToComponent(wnfRabattsatz, 60);

		wlaRabattsatzprozent = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		HelperClient.setDefaultsToComponent(wlaRabattsatzprozent, 15);
		wlaRabattsatzprozent.setHorizontalAlignment(SwingConstants.LEADING);

		wnfBerechneterpreis = new WrapperNumberField();
		wnfBerechneterpreis.setFractionDigits(iPreiseUINachkommastellen);
		wnfBerechneterpreis.setActivatable(false);
		wnfBerechneterpreis.setDependenceField(true);
		wnfBerechneterpreis.setMandatoryField(true);
		HelperClient.setDefaultsToComponent(wnfBerechneterpreis, 60);

		wlaBerechneterpreiswaehrung = new WrapperLabel(mandantenwaehrungCNr);
		HelperClient.setDefaultsToComponent(wlaBerechneterpreiswaehrung, 25);
		wlaBerechneterpreiswaehrung
				.setHorizontalAlignment(SwingConstants.LEADING);

		JPanel gueltigkeitsanzeigePanel = new JPanel(new GridBagLayout());
		gueltigkeitsanzeigePanel.add(wlaPreisgueltigkeitsanzeigeab,
				new GridBagConstraints(0, 0, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		gueltigkeitsanzeigePanel.add(wbuTagZurueck,
				new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));
		gueltigkeitsanzeigePanel.add(wdfPreisgueltigkeitsanzeigeab,
				new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		gueltigkeitsanzeigePanel.add(wbuNaechsterTag,
				new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));
		
		jpaWorkingOn.add(gueltigkeitsanzeigePanel, new GridBagConstraints(0, iZeile, 8, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaGestehungspreis, new GridBagConstraints(0, iZeile, 2, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGestehungspreis, new GridBagConstraints(2, iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungGestehungspreis, new GridBagConstraints(3, iZeile, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMinverkaufspreis, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMinverkaufspreis, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungMinverkaufspreis, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcoVkbasis, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(wnfVkbasis, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaVkbasiswaehrung, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaVkbasisGueltigab, new GridBagConstraints(4, iZeile, 2, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVkbasisGueltigab, new GridBagConstraints(6, iZeile, 2, 1, 0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFixpreis, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaRabattsatz, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		if (preisbasisVerkauf != 2) {
			jpaWorkingOn.add(wlaBerechneterpreis, new GridBagConstraints(6, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		// die Formatierungszeile
		iZeile++;
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMengeeinheit, new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFixpreis, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFixpreiswaehrung, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfRabattsatz, new GridBagConstraints(4, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaRabattsatzprozent, new GridBagConstraints(5, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		if (preisbasisVerkauf != 2) {
			jpaWorkingOn.add(wnfBerechneterpreis, new GridBagConstraints(6, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wlaBerechneterpreiswaehrung,
					new GridBagConstraints(7, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;
		jpaWorkingOn.add(wlaGueltigab, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfGueltigab, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaGueltigbis, new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfGueltigbis, new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	private void initPanel() throws Throwable {
		iPreiseUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseVK();
		iMengeUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenMenge();
		mandantenwaehrungCNr = LPMain.getTheClient().getSMandantenwaehrung();
	}

	private void setDefaults() throws Throwable {
		vkpfStaffelmengeDto = new VkpfMengenstaffelDto();

		leereAlleFelder(this);

		datGueltigkeitsanzeigeab = Helper.cutDate(new Date(System
				.currentTimeMillis()));

		wdfGueltigab.setDate(datGueltigkeitsanzeigeab);
		wdfPreisgueltigkeitsanzeigeab.setDate(datGueltigkeitsanzeigeab);

		wnfRabattsatz.setDouble(new Double(0)); // wird mit 0 initialisiert

		wlaMengeeinheit.setText(internalFrameArtikel.getArtikelDto()
				.getEinheitCNr().trim());

		wcoVkbasis.setKeyOfSelectedItem(new Integer(-1));

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// bei Verlassen des Dialogs mit Strg+S
		preisSetzen();

		if (allMandatoryFieldsSetDlg()) {

			if (vkpfStaffelmengeDto.getTPreisgueltigab() != null) {
				if (Helper.cutDate(vkpfStaffelmengeDto.getTPreisgueltigab())
						.getTime() == Helper.cutDate(wdfGueltigab.getDate())
						.getTime()) {
					boolean b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"artikel.error.vkpreisaenderungmitselbemdatum"));
					if (b == false) {
						return;
					}
				}
			}

			components2Dto();
			
			boolean bZentralerArtikelstamm = LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM);
			if(bZentralerArtikelstamm){
				
				if(vkpfStaffelmengeDto.getVkpfartikelpreislisteIId()==null){
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.error"), LPMain.getInstance()
							.getTextRespectUISPr("artikel.vkmengenstaffel.zentralerartikelstamm.error"));
					return;
					
				}
				
				
			}
			
			

			if (vkpfStaffelmengeDto.getIId() == null) {
				Integer iId = DelegateFactory.getInstance()
						.getVkPreisfindungDelegate()
						.createVkpfMengenstaffel(vkpfStaffelmengeDto);
				vkpfStaffelmengeDto = DelegateFactory.getInstance()
						.getVkPreisfindungDelegate()
						.vkpfMengenstaffelFindByPrimaryKey(iId);
				setKeyWhenDetailPanel(iId);
			} else {
				DelegateFactory.getInstance().getVkPreisfindungDelegate()
						.updateVkpfMengenstaffel(vkpfStaffelmengeDto);
			}

			// buttons schalten
			super.eventActionSave(e, true);

			eventYouAreSelected(false);

			setPreisgueltigkeitsanzeigeab(true);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		resetPanel();

		setPreisgueltigkeitsanzeigeab(false);

	}

	private void setPreisgueltigkeitsanzeigeab(boolean enabled) {
		wbuNaechsterTag.setEnabled(enabled);
		wbuTagZurueck.setEnabled(enabled);
		wdfPreisgueltigkeitsanzeigeab.setEnabled(enabled);
	}

	private void resetPanel() throws Throwable {
		vkpfStaffelmengeDto = new VkpfMengenstaffelDto();
		leereAlleFelder(this);
		setDefaults();
	}

	private void dto2Components() throws Throwable {
		wnfMenge.setBigDecimal(vkpfStaffelmengeDto.getNMenge());
		wnfFixpreis.setBigDecimal(vkpfStaffelmengeDto.getNArtikelfixpreis());

		BigDecimal nBerechneterPreis = new BigDecimal(0);

		if (vkpfStaffelmengeDto.getNArtikelfixpreis() != null) {
			nBerechneterPreis = vkpfStaffelmengeDto.getNArtikelfixpreis();
		} else {
			// WH 21.06.06 Es gilt die VK-Basis zur Preisgueltigkeit. Damit kann
			// der
			// berechnete Preis von dem dargstellten in der FLR Liste abweichen,
			// der wird
			// fuer das Beginndatum der Mengenstaffel angezeigt
			BigDecimal nPreisbasis = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.ermittlePreisbasis(
							internalFrameArtikel.getArtikelDto().getIId(),
							wdfPreisgueltigkeitsanzeigeab.getDate(),
							vkpfStaffelmengeDto.getVkpfartikelpreislisteIId(),
							wlaFixpreiswaehrung.getText());

			VerkaufspreisDto vkpfDto = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.berechneVerkaufspreis(nPreisbasis,
							vkpfStaffelmengeDto.getFArtikelstandardrabattsatz());

			nBerechneterPreis = vkpfDto.nettopreis;
		}

		wnfBerechneterpreis.setBigDecimal(nBerechneterPreis);
		wnfRabattsatz.setDouble(vkpfStaffelmengeDto
				.getFArtikelstandardrabattsatz());
		wdfGueltigab.setDate(vkpfStaffelmengeDto.getTPreisgueltigab());
		wdfGueltigbis.setDate(vkpfStaffelmengeDto.getTPreisgueltigbis());

		if (vkpfStaffelmengeDto.getVkpfartikelpreislisteIId() != null) {
			wcoVkbasis.setKeyOfSelectedItem(vkpfStaffelmengeDto
					.getVkpfartikelpreislisteIId());
		} else if (vkpfStaffelmengeDto.getBAllepreislisten() == 1) {
			wcoVkbasis.setKeyOfSelectedItem(new Integer(-2));
		} else {
			wcoVkbasis.setKeyOfSelectedItem(new Integer(-1));
		}

	}

	private void components2Dto() throws ExceptionLP {
		vkpfStaffelmengeDto.setArtikelIId(internalFrameArtikel.getArtikelDto()
				.getIId());
		vkpfStaffelmengeDto.setNMenge(wnfMenge.getBigDecimal());
		vkpfStaffelmengeDto.setNArtikelfixpreis(wnfFixpreis.getBigDecimal());
		vkpfStaffelmengeDto.setFArtikelstandardrabattsatz(wnfRabattsatz
				.getDouble());
		vkpfStaffelmengeDto.setTPreisgueltigab(wdfGueltigab.getDate());
		vkpfStaffelmengeDto.setTPreisgueltigbis(wdfGueltigbis.getDate());

		if (((Integer) wcoVkbasis.getKeyOfSelectedItem()) > -1) {
			vkpfStaffelmengeDto
					.setVkpfartikelpreislisteIId((Integer) wcoVkbasis
							.getKeyOfSelectedItem());
		} else {
			vkpfStaffelmengeDto.setVkpfartikelpreislisteIId(null);
		}

		vkpfStaffelmengeDto.setBAllepreislisten(new Short((short) 0));

		if (((Integer) wcoVkbasis.getKeyOfSelectedItem()) == -2) {
			vkpfStaffelmengeDto.setBAllepreislisten(new Short((short) 1));
			vkpfStaffelmengeDto.setNArtikelfixpreis(null);
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, false);
		wbuNaechsterTag.setEnabled(false);
		wbuTagZurueck.setEnabled(false);

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			vkpfStaffelmengeDto = DelegateFactory.getInstance()
					.getVkPreisfindungDelegate()
					.vkpfMengenstaffelFindByPrimaryKey((Integer) oKey);

			dto2Components();
		}

		// die Artikeleigenschaften anzeigen
		artikelDto2comp(internalFrameArtikel.getArtikelDto());

		// die passende Verkaufspreisbasis anzeigen
		verkaufspreisbasisDto2comp(datGueltigkeitsanzeigeab);

		String sBezeichnung = "";
		if (internalFrameArtikel.getArtikelDto().getArtikelsprDto() != null) {
			sBezeichnung = internalFrameArtikel.getArtikelDto()
					.getArtikelsprDto().getCBez();
		}

		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_AS_I_LIKE,
				internalFrameArtikel.getArtikelDto().getCNr() + ", "
						+ sBezeichnung);

		aktualisiereStatusbar();
		setPreisgueltigkeitsanzeigeab(true);
	}

	private void verkaufspreisbasisDto2comp(Date gueltigAb) throws Throwable {

		wlaFixpreiswaehrung.setText(mandantenwaehrungCNr);
		wlaBerechneterpreiswaehrung.setText(mandantenwaehrungCNr);
		wlaVkbasiswaehrung.setText(mandantenwaehrungCNr);

		wnfBerechneterpreis.setMandatoryField(true);
		wnfFixpreis.setActivatable(true);
		if (getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME
				|| getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW) {
			wnfFixpreis.setEditable(true);
		}
		if (vkPreisBasisLief1Preis) {
			ArtikellieferantDto alDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.getArtikelEinkaufspreis(
							internalFrameArtikel.getArtikelDto().getIId(),
							null,
							new BigDecimal(1),
							LPMain.getInstance().getTheClient()
									.getSMandantenwaehrung(),
							wdfPreisgueltigkeitsanzeigeab.getDate());
			if (alDto != null && alDto.getLief1Preis() != null) {
				wnfVkbasis.setBigDecimal(alDto.getLief1Preis());
				wdfVkbasisGueltigab.setDate(alDto.getTPreisgueltigab());
			} else {
				wnfVkbasis.setBigDecimal(null);
				wdfVkbasisGueltigab.setDate(null);
			}
		} else {

			if (((Integer) wcoVkbasis.getKeyOfSelectedItem()) > -1) {
				// PJ 07/8670 Preisliste
				VkPreisfindungPreislisteDto preisPreislisteDto = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.getVkPreisfindungPreislisteFindByArtikelIIdPreislisteIIdTPreisgueltigab(
								internalFrameArtikel.getArtikelDto().getIId(),
								(Integer) wcoVkbasis.getKeyOfSelectedItem(),
								gueltigAb);

				VkpfartikelpreislisteDto preislisteDto = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.vkpfartikelpreislisteFindByPrimaryKey(
								(Integer) wcoVkbasis.getKeyOfSelectedItem());

				wlaFixpreiswaehrung.setText(preislisteDto.getWaehrungCNr());
				wlaBerechneterpreiswaehrung.setText(preislisteDto
						.getWaehrungCNr());
				wlaVkbasiswaehrung.setText(preislisteDto.getWaehrungCNr());

				if (preisPreislisteDto != null
						&& preisPreislisteDto.getIId() != null) {

					// werte anzeigen
					if (preisPreislisteDto.getNArtikelfixpreis() != null) {
						wnfVkbasis.setBigDecimal(preisPreislisteDto
								.getNArtikelfixpreis());
						wdfVkbasisGueltigab.setDate(preisPreislisteDto
								.getTPreisgueltigab());
					} else {
						// VK-Preisbasis
						VkPreisfindungEinzelverkaufspreisDto preisDto = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.getArtikeleinzelverkaufspreis(
										internalFrameArtikel.getArtikelDto()
												.getIId(),
										gueltigAb,
										LPMain.getTheClient()
												.getSMandantenwaehrung());
						if (preisDto != null && preisDto.getIId() != null
								&& preisDto.getNVerkaufspreisbasis() != null) {

							BigDecimal bdRabattsumme = preisDto
									.getNVerkaufspreisbasis()
									.multiply(
											preisPreislisteDto
													.getNArtikelstandardrabattsatz()
													.movePointLeft(2));
							BigDecimal bdBerechneterpreis = preisDto
									.getNVerkaufspreisbasis().subtract(
											bdRabattsumme);

							wnfVkbasis.setBigDecimal(bdBerechneterpreis);
							wdfVkbasisGueltigab.setDate(preisDto
									.getTVerkaufspreisbasisgueltigab());
						} else {
							// werte zuruecksetzen
							wnfVkbasis.setBigDecimal(null);
							wdfVkbasisGueltigab.setDate(null);
						}
					}

				} else {
					// werte zuruecksetzen
					wnfVkbasis.setBigDecimal(null);
					wdfVkbasisGueltigab.setDate(null);
				}
			} else {

				if (((Integer) wcoVkbasis.getKeyOfSelectedItem()) == -1) {

					// VK-Preisbasis
					VkPreisfindungEinzelverkaufspreisDto preisDto = DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.getArtikeleinzelverkaufspreis(
									internalFrameArtikel.getArtikelDto()
											.getIId(),
									gueltigAb,
									LPMain.getTheClient()
											.getSMandantenwaehrung());
					if (preisDto != null && preisDto.getIId() != null) {
						// werte anzeigen
						wnfVkbasis.setBigDecimal(preisDto
								.getNVerkaufspreisbasis());
						wdfVkbasisGueltigab.setDate(preisDto
								.getTVerkaufspreisbasisgueltigab());
					} else {
						// werte zuruecksetzen
						wnfVkbasis.setBigDecimal(null);
						wdfVkbasisGueltigab.setDate(null);
					}
				}
				if (((Integer) wcoVkbasis.getKeyOfSelectedItem()) == -2) {
					wnfVkbasis.setBigDecimal(null);
					wnfFixpreis.setBigDecimal(null);
					wnfFixpreis.setEditable(false);
					wnfFixpreis.setActivatable(false);
					wnfBerechneterpreis.setMandatoryField(false);
					wnfBerechneterpreis.setBigDecimal(null);
					wnfBerechneterpreis.setEditable(false);
				}
			}
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		clearStatusbar();
		setStatusbarPersonalIIdAendern(vkpfStaffelmengeDto
				.getPersonalIIdAendern());
		setStatusbarTAendern(vkpfStaffelmengeDto.getTAendern());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_VKPFMENGENSTAFFEL;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfMenge;
	}

	/**
	 * Fixpreis uebersteuert einen ev. vorhandenen Rabattsatz.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	private void fixpreis_focusLost(FocusEvent e) throws Throwable {
		if (e.getSource().equals(wnfFixpreis)) {
			if (wnfFixpreis.getText() != null
					&& wnfFixpreis.getText().length() > 0) {
				// Hinweis, falls es einen Rabattsatz gab, Rabattsatz auf 0
				// setzen
				if (wnfRabattsatz.getDouble().doubleValue() != 0) {
					wnfRabattsatz.setDouble(new Double(0));
				}
			}
		}

		berechneArtikelpreis(e);
	}

	/**
	 * Fixpreis uebersteuert einen ev. vorhandenen Rabattsatz.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	private void rabattsatz_focusLost(FocusEvent e) throws Throwable {
		if (e.getSource().equals(wnfRabattsatz)) {
			// Hinweis, falls es einen Fixpreis gibt, Rabattsatz auf 0 setzen
			if (wnfFixpreis.getDouble() != null
					&& wnfRabattsatz.getDouble().doubleValue() != 0) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.hint"),
						LPMain.getInstance().getTextRespectUISPr(
								"vkpf.hint.fixpreisueberschreibtrabattsatz"));

				wnfRabattsatz.setDouble(new Double(0));
			}
		}

		berechneArtikelpreis(e);
	}

	private void berechneArtikelpreis(FocusEvent e) throws Throwable {
		boolean bEventBehandelt = false;

		// Source ist wnfFixpreis oder wnfRabattsatz
		if (e.getSource() == wnfFixpreis) {
			preisSetzen();
			bEventBehandelt = true;
		}

		if (!bEventBehandelt) {
			if (e.getSource() == wnfRabattsatz) {
				preisSetzen();
				bEventBehandelt = true;
			}
		}
	}

	/**
	 * Aufgrund einer Benutzereingabe den Staffelpreis neu berechnen.
	 * 
	 * @throws Throwable
	 */
	private void preisSetzen() throws Throwable {

		if (((Integer) wcoVkbasis.getKeyOfSelectedItem()) != -2) {
			BigDecimal bdBerechneterpreis = null;
			try {
				// Artikelfixpreis zaehlt zuerst
				if (wnfFixpreis.getText() != null
						&& wnfFixpreis.getText().length() > 0) {
					bdBerechneterpreis = wnfFixpreis.getBigDecimal();
				} else {
					if (wnfRabattsatz.getBigDecimal() == null) {
						wnfRabattsatz.setBigDecimal(new BigDecimal(0));
					}

					if (wnfVkbasis.getBigDecimal() != null) {
						BigDecimal bdRabattsumme = wnfVkbasis.getBigDecimal()
								.multiply(
										wnfRabattsatz.getBigDecimal()
												.movePointLeft(2));
						bdBerechneterpreis = wnfVkbasis.getBigDecimal()
								.subtract(bdRabattsumme);
					}

				}

				wnfBerechneterpreis.setBigDecimal(bdBerechneterpreis);

				// wenn der berechnete Preis unter dem minimalen Verkaufspreis
				// liegt
				// ->
				// rote Markierung
				setColorBerechneterPreis();
			} catch (Throwable ex) {
				DialogFactory
						.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								LPMain.getInstance()
										.getTextRespectUISPr(
												"lp.error.preisberechnungfehlgeschlagen.pruefen"));
			}
		}
	}

	/**
	 * Wenn der berechnete Preis unter dem Min VK liegt, wird er rot
	 * gekennzeichnet.
	 * 
	 * @throws Throwable
	 */
	private void setColorBerechneterPreis() throws Throwable {
		if (wnfMinverkaufspreis.getBigDecimal() != null
				&& wnfBerechneterpreis != null
				&& wnfBerechneterpreis.getDouble().doubleValue() > 0) {
			double dMinverkaufspreis = wnfMinverkaufspreis.getDouble()
					.doubleValue();
			double dBerechneterPreis = wnfBerechneterpreis.getDouble()
					.doubleValue();

			if (dBerechneterPreis < dMinverkaufspreis) {
				wnfBerechneterpreis.setForeground(Color.red);
			} else {
				wnfBerechneterpreis.setForeground(Color.black);
			}
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		try {
			if (evt.getSource() == wdfGueltigab.getDisplay()
					&& evt.getPropertyName().equals("date")) {
				wdfGueltigbis.setMinimumValue(wdfGueltigab.getDate());

				// automatisch korrigieren
				if (wdfGueltigab.getDate() != null
						&& wdfGueltigbis.getDate() != null) {
					if (wdfGueltigab.getDate().getTime() > wdfGueltigbis
							.getDate().getTime()) {
						wdfGueltigbis.setDate(wdfGueltigab.getDate());
					}
				}
			} else if (evt.getSource() == wdfPreisgueltigkeitsanzeigeab
					.getDisplay() && evt.getPropertyName().equals("date")) {
				datGueltigkeitsanzeigeab = wdfPreisgueltigkeitsanzeigeab
						.getDate();
				if (wdfPreisgueltigkeitsanzeigeab.getDate() != null
						&& vkpfStaffelmengeDto.getIId() != null) {

					// die passende Verkaufspreisbasis anzeigen
					verkaufspreisbasisDto2comp(datGueltigkeitsanzeigeab);

					// den berechneten Preis aktualisieren
					BigDecimal nBerechneterPreis = new BigDecimal(0);

					if (vkpfStaffelmengeDto.getNArtikelfixpreis() == null) {
						// WH 21.06.06 Es gilt die VK-Basis zur
						// Preisgueltigkeit. Damit kann der
						// berechnete Preis von dem dargstellten in der FLR
						// Liste abweichen, der wird
						// fuer das Beginndatum der Mengenstaffel angezeigt
						BigDecimal nPreisbasis = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.ermittlePreisbasis(
										internalFrameArtikel.getArtikelDto()
												.getIId(),
										wdfPreisgueltigkeitsanzeigeab.getDate(),
										vkpfStaffelmengeDto
												.getVkpfartikelpreislisteIId(),
										wlaFixpreiswaehrung.getText());

						VerkaufspreisDto vkpfDto = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.berechneVerkaufspreis(
										nPreisbasis,
										vkpfStaffelmengeDto
												.getFArtikelstandardrabattsatz());

						nBerechneterPreis = vkpfDto.nettopreis;
					}

					wnfBerechneterpreis.setBigDecimal(nBerechneterPreis);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr(
							"vkpf.error.preisgueltigkeitsanzeigeab"));
		}
	}

	private void artikelDto2comp(ArtikelDto artikelDto) throws Throwable {
		if (artikelDto != null && artikelDto.getIId() != null) {
			// Gestehungspreis des Artikels am Hauptlager des Mandanten
			LagerDto hauptlagerDto = DelegateFactory.getInstance()
					.getLagerDelegate().getHauptlagerDesMandanten();

			BigDecimal nGestehungspreis = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getGemittelterGestehungspreisEinesLagers(
							artikelDto.getIId(), hauptlagerDto.getIId());

			wnfGestehungspreis.setBigDecimal(nGestehungspreis);

			// den minimalen Verkaufspreis (in Mandantenwaehrung) des Artikels
			// auf dem Hauptlager anzeigen
			BigDecimal bdMinverkaufspreis = new BigDecimal(0);

			try {
				bdMinverkaufspreis = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getMindestverkaufspreis(
								artikelDto.getIId(),
								DelegateFactory.getInstance()
										.getLagerDelegate()
										.getHauptlagerDesMandanten().getIId(),
								new BigDecimal(1));
			} catch (Throwable t) {
				// wenn der minVerkaufspreis nicht gefunden wird, z.B. der
				// Artikel liegt nicht am Hauptlager -> 0 anzeigen
			}

			wnfMinverkaufspreis.setBigDecimal(bdMinverkaufspreis);
		}
	}

	void wbuTagZurueck_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfPreisgueltigkeitsanzeigeab.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		wdfPreisgueltigkeitsanzeigeab.setTimestamp(new java.sql.Timestamp(c
				.getTimeInMillis()));
	}

	void wbuNaechsterTag_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfPreisgueltigkeitsanzeigeab.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		wdfPreisgueltigkeitsanzeigeab.setTimestamp(new java.sql.Timestamp(c
				.getTimeInMillis()));
	}

	void wcoVkbasis_actionPerformed(ActionEvent e) {
		try {
			verkaufspreisbasisDto2comp(datGueltigkeitsanzeigeab);
			preisSetzen();
		} catch (Throwable e1) {
			handleException(e1, true);
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getVkPreisfindungDelegate()
				.removeVkpfMengenstaffel(vkpfStaffelmengeDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
		// ueberschreiben
		wdfPreisgueltigkeitsanzeigeab.setEnabled(true);
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		wbuNaechsterTag.setEnabled(true);
		wbuTagZurueck.setEnabled(true);
		wdfPreisgueltigkeitsanzeigeab.setEnabled(true);

	}

	// Inner classes
	// -------------------------------------------------------------

	class PanelVkpfStaffelmenge_fixpreis_focusAdapter extends
			java.awt.event.FocusAdapter {
		PanelVkpfStaffelmenge adaptee;

		PanelVkpfStaffelmenge_fixpreis_focusAdapter(
				PanelVkpfStaffelmenge adaptee) {
			this.adaptee = adaptee;
		}

		public void focusLost(FocusEvent e) {
			try {
				adaptee.fixpreis_focusLost(e);
			} catch (Throwable ex) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"lp.error.preisberechnungfehlgeschlagen"));
			}
		}
	}

	class PanelVkpfStaffelmenge_rabattsatz_focusAdapter extends
			java.awt.event.FocusAdapter {
		PanelVkpfStaffelmenge adaptee;

		PanelVkpfStaffelmenge_rabattsatz_focusAdapter(
				PanelVkpfStaffelmenge adaptee) {
			this.adaptee = adaptee;
		}

		public void focusLost(FocusEvent e) {
			try {
				adaptee.rabattsatz_focusLost(e);
			} catch (Throwable ex) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"lp.error.preisberechnungfehlgeschlagen"));
			}
		}
	}

	class PanelVkpfStaffelmenge_wbuNaechsterTag_actionAdapter implements
			ActionListener {
		private PanelVkpfStaffelmenge adaptee;

		PanelVkpfStaffelmenge_wbuNaechsterTag_actionAdapter(
				PanelVkpfStaffelmenge adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.wbuNaechsterTag_actionPerformed(e);
		}
	}

	class PanelVkpfStaffelmenge_wbuTagZurueck_actionAdapter implements
			ActionListener {
		private PanelVkpfStaffelmenge adaptee;

		PanelVkpfStaffelmenge_wbuTagZurueck_actionAdapter(
				PanelVkpfStaffelmenge adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.wbuTagZurueck_actionPerformed(e);
		}
	}

	class PanelVkpfStaffelmenge_wcoVkbasis_actionAdapter implements
			ActionListener {
		private PanelVkpfStaffelmenge adaptee;

		PanelVkpfStaffelmenge_wcoVkbasis_actionAdapter(
				PanelVkpfStaffelmenge adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.wcoVkbasis_actionPerformed(e);
		}
	}

}
