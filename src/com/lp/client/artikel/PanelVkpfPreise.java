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
import java.text.MessageFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

/*
 * <p>In diesem Fenster werden Preisinformationen zu einem Artikel erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * 2004-09-29</p> <p> </p>
 * 
 * @author uli walch
 * 
 * @version $Revision: 1.33 $
 */
public class PanelVkpfPreise extends PanelBasis implements
		PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;

	// Formatierung des Panels

	private JButton wbuTagZurueck = new JButton();
	private JButton wbuNaechsterTag = new JButton();

	/** Clone der Verkaufspreisbasis des aktuellen Artikels. */
	private VkPreisfindungEinzelverkaufspreisDto verkaufspreisbasisDto = null;
	/** Alle Preislistennamen fuer diesen Mandanten. */
	private VkpfartikelpreislisteDto[] aVkpfartikelpreislisteDtos = null;
	/** Alle Preise fuer diesen Artikel. */
	private VkPreisfindungPreislisteDto[] preislisteDtos = null;

	// Preisgueltigkeitsanzeige ab legt fest, welche Preise im Panel angezeigt
	// werden
	private WrapperLabel wlaPreisgueltigkeitsanzeigeab = null;
	private WrapperDateField wdfPreisgueltigkeitsanzeigeab = null;
	private Date datGueltigkeitsanzeigeab = null;

	// Der Artikel
	private WrapperLabel wlaIdent = null;
	private WrapperTextField wtfIdent = null;
	private WrapperLabel wlaBezeichnung = null;
	private WrapperTextField wtfBezeichnung = null;
	private WrapperLabel wlaZusatzbezeichnung = null;
	private WrapperTextField wtfZusatzbezeichnung = null;
	private WrapperLabel wlaZusatzbezeichnung2 = null;
	private WrapperTextField wtfZusatzbezeichnung2 = null;

	private WrapperNumberField wnfGestehungspreis = null;
	
	private WrapperNumberField wnfMinverkaufspreis = null;
	
	private WrapperNumberField wnfLief1Preis = null;
	
	private WrapperNumberField wnfMaterialzuschlag = null;
	private WrapperLabel wlaMaterialzuschlag = null;
	private WrapperLabel wlaMaterialzuschlagWaehrung = null;

	private WrapperLabel wlaEinzelvkp = null;
	private WrapperNumberField wnfEinzelvkp = null;
	private WrapperLabel wlaWaehrung1 = null;
	private WrapperLabel wlaEinzelvkpGueltigab = null;
	private WrapperDateField wdfEinzelvkpGueltigab = null;

	private WrapperLabel wlaFixpreis = null;
	private WrapperLabel wlaRabattsatz = null;
	private WrapperLabel wlaBerechneterpreis = null;

	private WrapperLabel[] wlaPreislistenname = null;
	private WrapperNumberField[] wnfArtikelfixpreis = null;
	private WrapperLabel[] wlaWaehrung = null;
	private WrapperNumberField[] wnfStandardrabattsatz = null;
	private WrapperLabel[] wlaProzent = null;
	private WrapperNumberField[] wnfBerechneterPreis = null;
	private WrapperDateField[] wdfPreisGueltigab = null;
	
	private boolean bWaehrungsfehlerBereitsEinmalAngezeigt = false;

	private WrapperLabel wlaNichtRabattierbar = null;


	private final int iPreiseUINachkommastellen;

	private final boolean vkPreisBasisLief1Preis;

	public PanelVkpfPreise(InternalFrame internalFrame, String add2TitleI,
			Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		iPreiseUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseVK();

		ParametermandantDto p = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);
		if (p != null && p.getCWert().equals("1")) {
			vkPreisBasisLief1Preis = true;
		} else {
			vkPreisBasisLief1Preis = false;
		}
		jbInit();
		initComponents();
		initPreislisten();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// ohne InnerBorder, den Platz brauche ich

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = null;

		wbuTagZurueck.setText("<");
		wbuTagZurueck
				.addActionListener(new PanelVkpfPreise_wbuTagZurueck_actionAdapter(
						this));
		wbuTagZurueck.setMaximumSize(new Dimension(25, 21));
		wbuTagZurueck.setMinimumSize(new Dimension(25, 21));
		wbuTagZurueck.setPreferredSize(new Dimension(25, 21));

		wbuNaechsterTag.setText(">");
		wbuNaechsterTag
				.addActionListener(new PanelVkpfPreise_wbuNaechsterTag_actionAdapter(
						this));
		wbuNaechsterTag.setMaximumSize(new Dimension(25, 21));
		wbuNaechsterTag.setMinimumSize(new Dimension(25, 21));
		wbuNaechsterTag.setPreferredSize(new Dimension(25, 21));

		// zusaetzliche buttons
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF)) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_UPDATE,
					PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD,
					PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT };

		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_PREVIOUS,
					PanelBasis.ACTION_NEXT };
		}

		enableToolsPanelButtons(aWhichButtonIUse);


		wlaPreisgueltigkeitsanzeigeab = new WrapperLabel(LPMain.getTextRespectUISPr("vkpf.preisgueltigkeitsanzeigeab"));
		wdfPreisgueltigkeitsanzeigeab = new WrapperDateField();
		wdfPreisgueltigkeitsanzeigeab.setMandatoryField(true);
		datGueltigkeitsanzeigeab = Helper.cutDate(new Date(System
				.currentTimeMillis()));
		wdfPreisgueltigkeitsanzeigeab.setDate(datGueltigkeitsanzeigeab);
		wdfPreisgueltigkeitsanzeigeab.getDisplay().addPropertyChangeListener(
				this);
		wdfPreisgueltigkeitsanzeigeab.setActivatable(false);

		wlaIdent = new WrapperLabel(LPMain.getTextRespectUISPr(
				"artikel.artikelnummer"));
		wtfIdent = new WrapperTextField();
		wtfIdent.setActivatable(false);

		wlaBezeichnung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung.setActivatable(false);

		wlaZusatzbezeichnung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wtfZusatzbezeichnung = new WrapperTextField();
		wtfZusatzbezeichnung.setActivatable(false);

		wlaZusatzbezeichnung2 = new WrapperLabel(LPMain.getTextRespectUISPr("lp.zusatzbez2")); // wird ungekuerzt zu
		// breit
		
		wtfZusatzbezeichnung2 = new WrapperTextField();
		wtfZusatzbezeichnung2.setActivatable(false);

		WrapperLabel wlaGestehungspreis = null;
		WrapperLabel wlaGestehungspreisWaehrung = null;
		WrapperLabel wlaMinverkaufspreis = null;
		WrapperLabel wlaMinverkaufspreisWaehrung = null;
		WrapperLabel wlaLief1Preis = null;
		WrapperLabel wlaLief1PreisWaehrung = null;
		
		wlaGestehungspreis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.gestehungspreis"));
		wnfGestehungspreis = new WrapperNumberField();
		wnfGestehungspreis.setActivatable(false);
		
		
		int iNachkommastellenGestpreis=Defaults.getInstance().getIUINachkommastellenPreiseVK();
		if(Defaults.getInstance().getIUINachkommastellenPreiseEK()>Defaults.getInstance().getIUINachkommastellenPreiseVK()){
			iNachkommastellenGestpreis=Defaults.getInstance().getIUINachkommastellenPreiseEK();
		}
		wnfGestehungspreis.setFractionDigits(iNachkommastellenGestpreis);
		wlaGestehungspreisWaehrung = new WrapperLabel(LPMain.getTheClient().getSMandantenwaehrung());
		wlaGestehungspreisWaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wlaLief1Preis = new WrapperLabel(LPMain.getTextRespectUISPr("artikel.lief1preis"));
		wnfLief1Preis = new WrapperNumberField();
		wnfLief1Preis.setActivatable(false);
		wnfLief1Preis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wlaLief1PreisWaehrung = new WrapperLabel(LPMain.getTheClient().getSMandantenwaehrung());
		wlaLief1PreisWaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		
		wlaMaterialzuschlag = new WrapperLabel(LPMain.getTextRespectUISPr("lp.materialzuschlag"));
		wnfMaterialzuschlag = new WrapperNumberField();
		wnfMaterialzuschlag.setActivatable(false);
		wnfMaterialzuschlag.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wlaMaterialzuschlagWaehrung = new WrapperLabel(LPMain.getTheClient().getSMandantenwaehrung());
		wlaMaterialzuschlagWaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wlaMinverkaufspreis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.minverkaufspreisshort"));
		wnfMinverkaufspreis = new WrapperNumberField();
		wnfMinverkaufspreis.setActivatable(false);
		wnfMinverkaufspreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wlaMinverkaufspreisWaehrung = new WrapperLabel(LPMain.getTheClient().getSMandantenwaehrung());
		wlaMinverkaufspreisWaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		
		wlaFixpreis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.fixpreis"));
		wlaFixpreis.setHorizontalAlignment(SwingConstants.CENTER);
		wlaRabattsatz = new WrapperLabel(LPMain.getTextRespectUISPr("label.rabattsumme"));
		wlaRabattsatz.setHorizontalAlignment(SwingConstants.CENTER);
		wlaBerechneterpreis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.berechneterpreis"));
		wlaBerechneterpreis.setHorizontalAlignment(SwingConstants.CENTER);

		wlaEinzelvkp = new WrapperLabel(LPMain.getTextRespectUISPr("artikel.label.einzelverkaufspreis"));

		wnfEinzelvkp = new WrapperNumberField();
		wnfEinzelvkp.setFractionDigits(iPreiseUINachkommastellen);
		wlaWaehrung1 = new WrapperLabel();
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEADING);

		wlaEinzelvkpGueltigab = new WrapperLabel(LPMain.getTextRespectUISPr("vkpf.vkbasisgueltigab"));

		wdfEinzelvkpGueltigab = new WrapperDateField();
		wdfEinzelvkpGueltigab.setDate(datGueltigkeitsanzeigeab);
		if (vkPreisBasisLief1Preis) {
			wlaEinzelvkp.setVisible(false);
			wnfEinzelvkp.setVisible(false);
			wlaWaehrung1.setVisible(false);
			wlaEinzelvkpGueltigab.setVisible(false);
			wdfEinzelvkpGueltigab.setVisible(false);
			wnfLief1Preis.setBorder(Defaults.getInstance()
					.getMandatoryFieldBorder());
		} else {
			wnfEinzelvkp.setDependenceField(true);
			wnfEinzelvkp.setMandatoryField(true);
			wnfEinzelvkp
					.addFocusListener(new PanelVkpfPreise_wnfEinzelvkp_focusAdapter(
							this));
			wdfEinzelvkpGueltigab.setMandatoryField(true);
		}

		wlaNichtRabattierbar = new WrapperLabel();
		wlaNichtRabattierbar.setForeground(Color.RED);
		
		// Workingpanel
		jPanelWorkingOn = new JPanel(new MigLayout("wrap 8, hidemode 2",
				"[fill,20%|fill,12.5%|fill,5%|fill,22.5%|fill,5%|fill,15%|fill,5%|fill,3%]"));
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		JPanel gueltigkeitsanzeigePanel = new JPanel(new GridBagLayout());
		gueltigkeitsanzeigePanel.add(wlaPreisgueltigkeitsanzeigeab,
				new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		gueltigkeitsanzeigePanel.add(wbuTagZurueck, new GridBagConstraints(1, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));
		gueltigkeitsanzeigePanel.add(wdfPreisgueltigkeitsanzeigeab,
				new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		gueltigkeitsanzeigePanel.add(wbuNaechsterTag, new GridBagConstraints(3, 0,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));
		
		jPanelWorkingOn.add(gueltigkeitsanzeigePanel, "span");

		jPanelWorkingOn.add(wlaIdent);
		jPanelWorkingOn.add(wtfIdent, "span 2");
		jPanelWorkingOn.add(wlaNichtRabattierbar, "span");

		jPanelWorkingOn.add(wlaBezeichnung);
		jPanelWorkingOn.add(wtfBezeichnung, "span 4, wrap");

		jPanelWorkingOn.add(wlaZusatzbezeichnung);
		jPanelWorkingOn.add(wtfZusatzbezeichnung, "span 4, wrap");

		jPanelWorkingOn.add(wlaZusatzbezeichnung2);
		jPanelWorkingOn.add(wtfZusatzbezeichnung2, "span 4");
		jPanelWorkingOn.add(wlaMaterialzuschlag, "span 2");
		jPanelWorkingOn.add(wnfMaterialzuschlag, "split, span, w 10%!");
		jPanelWorkingOn.add(wlaMaterialzuschlagWaehrung, "wrap, w 5%!");

		jPanelWorkingOn.add(wlaLief1Preis);
		jPanelWorkingOn.add(wnfLief1Preis);
		jPanelWorkingOn.add(wlaLief1PreisWaehrung);
		jPanelWorkingOn.add(wlaGestehungspreis, "split 2, span 2, right, w 15%!");
		jPanelWorkingOn.add(wnfGestehungspreis, " w 10%!");
		jPanelWorkingOn.add(wlaGestehungspreisWaehrung, "split 2, span 2, w 5%!");		
		jPanelWorkingOn.add(wlaMinverkaufspreis);
		jPanelWorkingOn.add(wnfMinverkaufspreis, "split, span, w 10%!");
		jPanelWorkingOn.add(wlaMinverkaufspreisWaehrung, "wrap, w 5%!");

		jPanelWorkingOn.add(wlaEinzelvkp);
		jPanelWorkingOn.add(wnfEinzelvkp);
		jPanelWorkingOn.add(wlaWaehrung1);
		jPanelWorkingOn.add(wlaEinzelvkpGueltigab, "span 3");
		jPanelWorkingOn.add(wdfEinzelvkpGueltigab, "span");

		jPanelWorkingOn.add(wlaFixpreis, "skip");
		jPanelWorkingOn.add(wlaRabattsatz, "span 2");
		jPanelWorkingOn.add(wlaBerechneterpreis, "span 3, wrap");
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfEinzelvkp;
	}

	/**
	 * Saemtliche Preisanzeigefelder aktualisiert werden.
	 * 
	 * @throws Throwable
	 */
	private void refreshPreise() throws Throwable {
		ArtikelDto artikelDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(
						((InternalFrameArtikel) getInternalFrame())
								.getArtikelDto().getIId());

		// die gewuenschte Verkaufspreisbasis anzeigen
		verkaufspreisbasisDto = DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.getArtikeleinzelverkaufspreis(artikelDto.getIId(),
						datGueltigkeitsanzeigeab,
						LPMain.getTheClient().getSMandantenwaehrung());

		verkaufspreisbasisDto2comp(verkaufspreisbasisDto);

		wlaWaehrung1.setText(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getTheClient().getMandant())
				.getWaehrungCNr());

		// Preisinformationen fuer Preislisten anzeigen
		preislisteDtos = new VkPreisfindungPreislisteDto[aVkpfartikelpreislisteDtos.length];

		for (int i = 0; i < preislisteDtos.length; i++) {

			// angezeigt werden die aktuell gewuenschten Preise
			preislisteDtos[i] = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.getAktuellePreislisteByArtikelIIdPreislisteIId(
							artikelDto.getIId(),
							aVkpfartikelpreislisteDtos[i].getIId(),
							datGueltigkeitsanzeigeab,
							aVkpfartikelpreislisteDtos[i].getWaehrungCNr());

			// das Gultiegkeitsdatum der Verkaufspreisbasis schraenkt das
			// moeglich gueltig ab ein
			// wdfPreisGueltigab[i].setMinimumValue(verkaufspreisbasisDto.
			// getTVerkaufspreisbasisgueltigab());
		}

		preislisteDtos2com(preislisteDtos);

	}

	private void initPreislisten() throws Throwable {
		aVkpfartikelpreislisteDtos = DelegateFactory.getInstance()
				.getVkPreisfindungDelegate().getAlleAktivenPreislisten();

		// alle vorhandenen preislisten anzeigen
		wlaPreislistenname = new WrapperLabel[aVkpfartikelpreislisteDtos.length];
		wnfArtikelfixpreis = new WrapperNumberField[aVkpfartikelpreislisteDtos.length];
		wlaWaehrung = new WrapperLabel[aVkpfartikelpreislisteDtos.length];
		wnfStandardrabattsatz = new WrapperNumberField[aVkpfartikelpreislisteDtos.length];
		wlaProzent = new WrapperLabel[aVkpfartikelpreislisteDtos.length];
		wnfBerechneterPreis = new WrapperNumberField[aVkpfartikelpreislisteDtos.length];
		wdfPreisGueltigab = new WrapperDateField[aVkpfartikelpreislisteDtos.length];

		String mw = LPMain.getTheClient().getSMandantenwaehrung();
		for (int i = 0; i < aVkpfartikelpreislisteDtos.length; i++) {

			// die Nummerierung der Preislisten beginnt mit 1 und ist
			// fortlaufend
			int iNummer = i + 1;
			boolean isFremdWaehrung = !(aVkpfartikelpreislisteDtos[i]
					.getWaehrungCNr().equals(mw));

			wlaPreislistenname[i] = new WrapperLabel(iNummer + " "
					+ aVkpfartikelpreislisteDtos[i].getCNr());
			wlaPreislistenname[i]
					.setHorizontalAlignment(SwingConstants.LEADING);

			jPanelWorkingOn.add(wlaPreislistenname[i]);

			wnfArtikelfixpreis[i] = new WrapperNumberField(); // kommt im UI mit
			// der 15,4
			// Maske daher
			wnfArtikelfixpreis[i].setMaximumIntegerDigits(6); // 6
			// Vorkommastellen
			wnfArtikelfixpreis[i].setFractionDigits(iPreiseUINachkommastellen); // UI
			// Nachkommastellen
			wnfArtikelfixpreis[i].setMandatoryField(isFremdWaehrung);
			wnfArtikelfixpreis[i].setDependenceField(true);
			wnfArtikelfixpreis[i]
					.addFocusListener(new PanelVkpfPreise_artikelfixpreis_focusAdapter(
							this));

			jPanelWorkingOn.add(wnfArtikelfixpreis[i]);

			wnfStandardrabattsatz[i] = new WrapperNumberField(); // kommt im UI
			// mit der
			// 15,4
			// Maske
			// daher
			wnfStandardrabattsatz[i].setMaximumIntegerDigits(6); // 6
			// Vorkommastellen
			wnfStandardrabattsatz[i].setFractionDigits(2); // 2 Nachkommastellen
			// PJ 15081
			// wnfStandardrabattsatz[i].setDouble(new Double(0)); // wird mit 0
			// initialisiert
			wnfStandardrabattsatz[i].setMandatoryField(!isFremdWaehrung);
			wnfStandardrabattsatz[i].setDependenceField(true);
			// wnfStandardrabattsatz[i].setEditable(!isFremdWaehrung);
			wnfStandardrabattsatz[i]
					.addFocusListener(new PanelVkpfPreise_standardrabattsatz_focusAdapter(
							this));
			jPanelWorkingOn.add(wnfStandardrabattsatz[i], "span 2");

			wlaProzent[i] = new WrapperLabel(LPMain.getTextRespectUISPr("label.prozent"));
			wlaProzent[i].setHorizontalAlignment(SwingConstants.LEADING);

			jPanelWorkingOn.add(wlaProzent[i]);

			wnfBerechneterPreis[i] = new WrapperNumberField(); // kommt im UI
			// mit der 15,4
			// Maske daher
			wnfBerechneterPreis[i].setMaximumIntegerDigits(6); // 6
			// Vorkommastellen
			wnfBerechneterPreis[i].setFractionDigits(iPreiseUINachkommastellen); // UI
			// Preise
			// Nachkommastellen
			wnfBerechneterPreis[i].setActivatable(false);
			wnfBerechneterPreis[i].setDependenceField(true);

			jPanelWorkingOn.add(wnfBerechneterPreis[i]);

			wlaWaehrung[i] = new WrapperLabel(
					aVkpfartikelpreislisteDtos[i].getWaehrungCNr());
			wlaWaehrung[i].setHorizontalAlignment(SwingConstants.LEADING);

			jPanelWorkingOn.add(wlaWaehrung[i]);

			wdfPreisGueltigab[i] = new WrapperDateField();
			wdfPreisGueltigab[i].setMandatoryField(true);
			wdfPreisGueltigab[i].setDate(datGueltigkeitsanzeigeab);

			jPanelWorkingOn.add(wdfPreisGueltigab[i], "w 15%!, wrap");
		}

		refreshPreise();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		setPreisgueltigkeitsanzeigeab(true);
		initRabatt(false);
		berechnePreis();
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			boolean bSpeichern = true;

			if (!vkPreisBasisLief1Preis) { // PJ 15000

				// PJ 08/14231
				if (verkaufspreisbasisDto != null
						&& verkaufspreisbasisDto.getIId() != null) {
					if (verkaufspreisbasisDto.getTVerkaufspreisbasisgueltigab() != null) {
						if (Helper.cutDate(
								verkaufspreisbasisDto
										.getTVerkaufspreisbasisgueltigab())
								.getTime() == Helper.cutDate(
								wdfEinzelvkpGueltigab.getDate()).getTime()) {
							boolean b = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr(
															"artikel.error.vkpreisaenderungmitselbemdatum"));
							if (b == false) {
								return;
							}
						}
					}

				}
			}
			// Artikelverkaufspreisbasis pruefen
			verkaufspreisbasisDto = comp2verkaufspreisbasisDto();

			// gibt es zum gewaehlten Gueltigkeitsdatum bereits eine
			// Artikelverkaufspreisbasis (UK Constraint pruefen)?
			VkPreisfindungEinzelverkaufspreisDto checkVerkaufspreisbasisDto = null;

			try {
				checkVerkaufspreisbasisDto = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.einzelverkaufspreisFindByUniqueyKey(
								verkaufspreisbasisDto.getArtikelIId(),
								new Date(verkaufspreisbasisDto
										.getTVerkaufspreisbasisgueltigab()
										.getTime()));
			} catch (Throwable t) {
				// continue
			}

			if (checkVerkaufspreisbasisDto != null
					&& verkaufspreisbasisDto.getIId() != null
					&& checkVerkaufspreisbasisDto.getIId().intValue() != verkaufspreisbasisDto
							.getIId().intValue()) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain.getTextRespectUISPr("vkpf.error.gueltigabbelegt"));

				bSpeichern = false;
			}

			// fuer alle Preislisten: gibt es zum gewaehlten Gueltigkeitsdatum
			// bereits einen Artikelpreis?
			for (int i = 0; i < preislisteDtos.length; i++) {
				// wenn das Datum nicht leer ist, werden die Artikelpreise
				// behandelt
				if (wdfPreisGueltigab[i].getDate() != null) {

					// gibt es den unique key schon
					VkPreisfindungPreislisteDto checkPreisDto = null;

					try {
						checkPreisDto = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.preislisteFindByUniqueyKey(
										preislisteDtos[i]
												.getVkpfartikelpreislisteIId(),
										preislisteDtos[i].getArtikelIId(),
										wdfPreisGueltigab[i].getDate());
					} catch (Throwable t) {
						// continue
					}

					if (checkPreisDto != null
							&& checkPreisDto.getIId().intValue() != preislisteDtos[i]
									.getIId().intValue()) {
						MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr(
										"vkpf.error.preisgueltigabbelegt"));

						mf.setLocale(LPMain.getTheClient()
								.getLocUi());

						Object pattern[] = { wlaPreislistenname[i].getText() };

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), mf
								.format(pattern));

						bSpeichern = false;
					}
				}
			}

			if (bSpeichern) {
				if (!vkPreisBasisLief1Preis) { // PJ 15000
					if (verkaufspreisbasisDto.getIId() == null
							|| checkVerkaufspreisbasisDto == null) {
						Integer pkEinzelvkp = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.createVkPreisfindungEinzelverkaufspreis(
										verkaufspreisbasisDto);
						verkaufspreisbasisDto.setIId(pkEinzelvkp);
					} else {
						DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.updateVkPreisfindungEinzelverkaufspreis(
										verkaufspreisbasisDto);
					}
				}

				// die Preise des Artikels in der jweiligen Preisliste
				preislisteDtos = comp2preislisteDtos();

				for (int i = 0; i < preislisteDtos.length; i++) {
					// wenn das Datum nicht leer ist, werden die Artikelpreise
					// behandelt
					if (wdfPreisGueltigab[i].getDate() != null) {

						// gibt es den unique key schon
						VkPreisfindungPreislisteDto checklisteDto = null;

						try {
							checklisteDto = DelegateFactory
									.getInstance()
									.getVkPreisfindungDelegate()
									.preislisteFindByUniqueyKey(
											preislisteDtos[i]
													.getVkpfartikelpreislisteIId(),
											preislisteDtos[i].getArtikelIId(),
											new Date(preislisteDtos[i]
													.getTPreisgueltigab()
													.getTime()));
						} catch (Throwable t) {
							// continue
						}

						if (preislisteDtos[i].getIId() == null
								|| checklisteDto == null) {
							// einen neuen Preis anlegen
							Integer pkPreisliste = DelegateFactory
									.getInstance()
									.getVkPreisfindungDelegate()
									.createVkPreisfindungPreisliste(
											preislisteDtos[i]);
							preislisteDtos[i].setIId(pkPreisliste);
						} else {
							DelegateFactory
									.getInstance()
									.getVkPreisfindungDelegate()
									.updateVkPreisfindungPreisliste(
											preislisteDtos[i]);
						}

					}
				}

				// PJ 14400
				VkpfMengenstaffelDto[] vkpfMengenstaffelDtos = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
								verkaufspreisbasisDto.getArtikelIId(),
								wdfPreisgueltigkeitsanzeigeab.getDate(), null);

				if (vkpfMengenstaffelDtos != null
						&& vkpfMengenstaffelDtos.length > 0) {
					boolean bMeldungMussangezeigtWerden = false;
					for (int i = 0; i < vkpfMengenstaffelDtos.length; i++) {
						if (vkpfMengenstaffelDtos[i].getNArtikelfixpreis() != null) {
							bMeldungMussangezeigtWerden = true;
						}
					}

					if (bMeldungMussangezeigtWerden) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr(
										"lp.hinweis"),
								LPMain.getTextRespectUISPr(
										"artikel.error.staffelpreiseaendern"));
					}

				}

				super.eventActionSave(e, true);

				eventYouAreSelected(false);

				setPreisgueltigkeitsanzeigeab(true);
			}
		}
	}

	private void setPreisgueltigkeitsanzeigeab(boolean enabled) {
		wbuNaechsterTag.setEnabled(enabled);
		wbuTagZurueck.setEnabled(enabled);
		wdfPreisgueltigkeitsanzeigeab.setEnabled(enabled);
	}

	/**
	 * Diese Methode baut ein gueltiges VkPreisfindungEinzelverkaufspreisDto
	 * zusammen.
	 * 
	 * @return VkPreisfindungEinzelverkaufspreisDto
	 * @throws Throwable
	 */
	private VkPreisfindungEinzelverkaufspreisDto comp2verkaufspreisbasisDto()
			throws Throwable {
		if (verkaufspreisbasisDto == null
				|| verkaufspreisbasisDto.getIId() == null) {
			verkaufspreisbasisDto = new VkPreisfindungEinzelverkaufspreisDto();
			verkaufspreisbasisDto.setMandantCNr(LPMain.getTheClient().getMandant());
			verkaufspreisbasisDto
					.setArtikelIId(((InternalFrameArtikel) getInternalFrame())
							.getArtikelDto().getIId());
		}

		verkaufspreisbasisDto.setNVerkaufspreisbasis(this.wnfEinzelvkp
				.getBigDecimal());
		verkaufspreisbasisDto
				.setTVerkaufspreisbasisgueltigab(this.wdfEinzelvkpGueltigab
						.getDate());

		return verkaufspreisbasisDto;
	}

	/**
	 * Diese Methode baut die Preise fuer den Artikel in der jeweiligen
	 * Preisliste zusammen.
	 * 
	 * @return VkPreisfindungPreislisteDto[]
	 * @throws Throwable
	 */
	private VkPreisfindungPreislisteDto[] comp2preislisteDtos()
			throws Throwable {
		for (int i = 0; i < preislisteDtos.length; i++) {
			if (this.preislisteDtos[i] == null
					|| preislisteDtos[i].getIId() == null) {
				preislisteDtos[i] = new VkPreisfindungPreislisteDto();
				preislisteDtos[i]
						.setArtikelIId(((InternalFrameArtikel) getInternalFrame())
								.getArtikelDto().getIId());
				preislisteDtos[i]
						.setVkpfartikelpreislisteIId(this.aVkpfartikelpreislisteDtos[i]
								.getIId());
			}

			preislisteDtos[i].setNArtikelfixpreis(wnfArtikelfixpreis[i]
					.getBigDecimal());
			preislisteDtos[i]
					.setNArtikelstandardrabattsatz(wnfStandardrabattsatz[i]
							.getBigDecimal());

			// preislisteDtos[i].setDGueltigab(wdfPreisGueltigab[i].getDate());
			preislisteDtos[i].setTPreisgueltigab(Helper
					.cutDate(wdfPreisGueltigab[i].getDate()));
		}

		return preislisteDtos;
	}

	private void artikelDto2comp(ArtikelDto artikelDto) throws Throwable {
		if (artikelDto != null && artikelDto.getIId() != null) {

			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {

				ArtikellieferantDto alDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.getArtikelEinkaufspreis(
								artikelDto.getIId(),
								null,
								new BigDecimal(0),
								LPMain.getTheClient()
										.getSMandantenwaehrung(),
								wdfPreisgueltigkeitsanzeigeab.getDate());
				if (alDto != null && alDto.getLief1Preis() != null) {
					wnfLief1Preis.setBigDecimal(alDto.getLief1Preis());

				} else {
					wnfLief1Preis.setBigDecimal(BigDecimal.ZERO);
				}
			}

			if (artikelDto.getMaterialIId() != null) {
				wnfMaterialzuschlag.setVisible(true);
				wnfMaterialzuschlag.setBigDecimal(
								DelegateFactory
										.getInstance()
										.getMaterialDelegate()
										.getMaterialzuschlagVKInZielwaehrung(
												artikelDto.getIId(),
												datGueltigkeitsanzeigeab,
												LPMain.getTheClient()
														.getSMandantenwaehrung()));
				wlaMaterialzuschlag.setVisible(true);
				wlaMaterialzuschlagWaehrung.setVisible(true);
			} else {
				wnfMaterialzuschlag.setVisible(false);
				wlaMaterialzuschlag.setVisible(false);
				wlaMaterialzuschlagWaehrung.setVisible(false);
			}
			wtfIdent.setText(artikelDto.getCNr());
			if (Helper.short2Boolean(artikelDto.getBRabattierbar())) {
				wlaNichtRabattierbar.setText("");
			} else {
				wlaNichtRabattierbar.setText(LPMain.getTextRespectUISPr("artikel.nichtrabattierbar"));
			}

			if (artikelDto.getArtikelsprDto() != null) {
				wtfBezeichnung.setText(artikelDto.getArtikelsprDto().getCBez());
				wtfZusatzbezeichnung.setText(artikelDto.getArtikelsprDto()
						.getCZbez());
				wtfZusatzbezeichnung2.setText(artikelDto.getArtikelsprDto()
						.getCZbez2());

				// Gestehungspreis des Artikels am Hauptlager des Mandanten
				LagerDto hauptlagerDto = DelegateFactory.getInstance()
						.getLagerDelegate().getHauptlagerDesMandanten();

				BigDecimal nGestehungspreis = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						. // check WH
						getGemittelterGestehungspreisEinesLagers(
								artikelDto.getIId(), hauptlagerDto.getIId());

				nGestehungspreis = Helper
						.rundeKaufmaennisch(nGestehungspreis, Defaults
								.getInstance().getIUINachkommastellenPreiseVK());

				wnfGestehungspreis.setBigDecimal(nGestehungspreis);

				// den minimalen Verkaufspreis (in Mandantenwaehrung) des
				// Artikels auf dem Hauptlager anzeigen
				BigDecimal bdMinverkaufspreis = BigDecimal.ZERO;

				try {
					bdMinverkaufspreis = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getMindestverkaufspreis(
									artikelDto.getIId(),
									DelegateFactory.getInstance()
											.getLagerDelegate()
											.getHauptlagerDesMandanten()
											.getIId(), new BigDecimal(1));
					bdMinverkaufspreis = Helper.rundeKaufmaennisch(
							bdMinverkaufspreis, Defaults.getInstance()
									.getIUINachkommastellenPreiseVK());

				} catch (Throwable t) {
					// wenn der minVerkaufspreis nicht gefunden wird, z.B. der
					// Artikel liegt nicht am Hauptlager -> 0 anzeigen
				}
				wnfMinverkaufspreis.setBigDecimal(bdMinverkaufspreis);
			}
		}
	}

	private void verkaufspreisbasisDto2comp(
			VkPreisfindungEinzelverkaufspreisDto preisDto) throws Throwable {
		if (preisDto != null && preisDto.getIId() != null) {
			// werte anzeigen
			wnfEinzelvkp.setBigDecimal(preisDto.getNVerkaufspreisbasis());
			wdfEinzelvkpGueltigab.setDate(preisDto
					.getTVerkaufspreisbasisgueltigab());

			this.setStatusbarPersonalIIdAendern(preisDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(preisDto.getTAendern());

		} else {
			// werte zuruecksetzen
			wnfEinzelvkp.setBigDecimal(null);
			wdfEinzelvkpGueltigab.setDate(datGueltigkeitsanzeigeab);
			clearStatusbar();
		}
	}

	/**
	 * Diese Methode zeigt die Preise eines Artikels in einer Preisliste an.
	 * 
	 * @param dtos
	 *            VkPreisfindungPreislisteDto[]
	 * @throws Throwable
	 */
	private void preislisteDtos2com(VkPreisfindungPreislisteDto[] dtos)
			throws Throwable {
		if (dtos != null && dtos.length > 0) {
			for (int i = 0; i < dtos.length; i++) {
				if (dtos[i] != null && dtos[i].getIId() != null) {
					// werte anzeigen
					wnfArtikelfixpreis[i].setBigDecimal(dtos[i]
							.getNArtikelfixpreis());
					wnfStandardrabattsatz[i].setBigDecimal(dtos[i]
							.getNArtikelstandardrabattsatz());

					// hier den berechneten Preis pro Artikel und Preisliste
					// anzeigen
					berechnePreis();

					wdfPreisGueltigab[i].setDate(dtos[i].getTPreisgueltigab());
				} else {
					// werte zuruecksetzen
					wnfArtikelfixpreis[i].setBigDecimal(null);
					// PJ 15081
					// wnfStandardrabattsatz[i].setDouble(new Double(0));

					// PJ 17178
					wnfStandardrabattsatz[i].setDouble(null);
					wnfBerechneterPreis[i].setBigDecimal(BigDecimal.ZERO);
					wdfPreisGueltigab[i].setDate(datGueltigkeitsanzeigeab);
				}
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		Object oKey = ((InternalFrameArtikel) getInternalFrame())
				.getArtikelDto().getIId();

		if (oKey != null) {
			ArtikelDto artikelDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							((InternalFrameArtikel) getInternalFrame())
									.getArtikelDto().getIId());
			artikelDto2comp(artikelDto);

			refreshPreise();
		}
		setPreisgueltigkeitsanzeigeab(true);
	}

	public void eventActionLock(ActionEvent e) throws Throwable {
		super.eventActionLock(e);
		try {
			initRabatt(true);
			berechnePreis();
		} catch (Throwable t) {
			//
		}
	}

	/**
	 * Es wird immer der gesamte Artikel gelockt.
	 * 
	 * @throws Exception
	 * @return LockMeDto
	 */
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	/**
	 * Wenn dieses Feld verlassen wird, muessen die Preisfelder neu berechnet
	 * werden.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	void wnfEinzelvkp_focusLost(FocusEvent e) {
		try {
			berechnePreis();
		} catch (Throwable ex) {
			handleException(ex, false);
		}
	}

	void artikelfixpreis_focusLost(FocusEvent e) {
		try {
			for (int i = 0; i < wnfArtikelfixpreis.length; i++) {
				if (e.getSource().equals(wnfArtikelfixpreis[i])) {
					// Hinweis, falls es einen Rabattsatz gab, Rabattsatz auf 0
					// setzen
					if (wnfStandardrabattsatz[i].getDouble().doubleValue() != 0) {
						wnfStandardrabattsatz[i].setDouble(new Double(0));
					}
				}
			}

			berechneArtikelpreis(e);
		} catch (Throwable ex) {
			handleException(ex, false);
		}
	}

	void standardrabattsatz_focusLost(FocusEvent e) throws Throwable {
		for (int i = 0; i < wnfStandardrabattsatz.length; i++) {
			if (e.getSource().equals(wnfStandardrabattsatz[i])) {
				// Hinweis, falls es einen Fixpreis gibt, Rabattsatz auf 0
				// setzen
				if (wnfArtikelfixpreis[i].getDouble() != null
						&& wnfStandardrabattsatz[i].getDouble().doubleValue() != 0) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr(
											"lp.hint"),
									LPMain.getTextRespectUISPr(
													"vkpf.hint.fixpreisueberschreibtrabattsatz"));

					wnfStandardrabattsatz[i].setDouble(new Double(0));
				}
			}
		}
		berechneArtikelpreis(e);
	}

	private void berechneArtikelpreis(FocusEvent e) throws Throwable {
		boolean bEventBehandelt = false;

		// Source ist wnfArtikelfixpreis[i] oder wnfStandardrabattsatz[i]
		for (int i = 0; i < wnfArtikelfixpreis.length; i++) {
			if (e.getSource() == wnfArtikelfixpreis[i]) {
				preisSetzen(i);
				bEventBehandelt = true;
			}
		}

		if (!bEventBehandelt) {
			for (int i = 0; i < wnfStandardrabattsatz.length; i++) {
				if (e.getSource() == wnfStandardrabattsatz[i]) {
					preisSetzen(i);
					bEventBehandelt = true;
				}
			}
		}
	}

	private void preisSetzen(int iPreislisteI) throws Throwable {
		BigDecimal preisBasis;
		if (vkPreisBasisLief1Preis)
			preisBasis = wnfLief1Preis.getBigDecimal();
		else
			preisBasis = wnfEinzelvkp.getBigDecimal();
		preisBasis = preisBasis == null ? BigDecimal.ZERO : preisBasis;

		if (Helper.short2boolean(aVkpfartikelpreislisteDtos[iPreislisteI]
				.getBPreislisteaktiv())) {
			BigDecimal bdBerechneterpreis = null;

			// Artikelfixpreis zaehlt zuerst
			if (wnfArtikelfixpreis[iPreislisteI].getText() != null
					&& wnfArtikelfixpreis[iPreislisteI].getText().length() > 0) {
				bdBerechneterpreis = wnfArtikelfixpreis[iPreislisteI]
						.getBigDecimal();
			} else {
				if (wnfStandardrabattsatz[iPreislisteI].getBigDecimal() != null) {
					BigDecimal bdRabattsumme = preisBasis
							.multiply(wnfStandardrabattsatz[iPreislisteI]
									.getBigDecimal().movePointLeft(2));
					bdBerechneterpreis = preisBasis.subtract(bdRabattsumme);
				}
			}

			wnfBerechneterPreis[iPreislisteI].setBigDecimal(bdBerechneterpreis == null ? BigDecimal.ZERO : bdBerechneterpreis);
			if (aVkpfartikelpreislisteDtos[iPreislisteI].getWaehrungCNr()
					.equals(LPMain.getTheClient()
							.getSMandantenwaehrung())) {

				if (vkPreisBasisLief1Preis) {
					wnfBerechneterPreis[iPreislisteI]
							.setCompareValue(preisBasis);
				} else {
					wnfBerechneterPreis[iPreislisteI]
							.setCompareValue(wnfMinverkaufspreis
									.getBigDecimal());
				}

			} else {
				BigDecimal wk = DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.getWechselkurs2(
								LPMain.getTheClient()
										.getSMandantenwaehrung(),
								aVkpfartikelpreislisteDtos[iPreislisteI]
										.getWaehrungCNr());

				if (wk != null) {

					if (vkPreisBasisLief1Preis) {
						wnfBerechneterPreis[iPreislisteI]
								.setCompareValue(preisBasis.multiply(wk)
										.setScale(iPreiseUINachkommastellen,
												BigDecimal.ROUND_HALF_EVEN));
					} else {
						if(wnfMinverkaufspreis.getBigDecimal() == null)
							wnfMinverkaufspreis.setBigDecimal(BigDecimal.ZERO);
						wnfBerechneterPreis[iPreislisteI]
								.setCompareValue(wnfMinverkaufspreis
										.getBigDecimal()
										.multiply(wk)
										.setScale(iPreiseUINachkommastellen,
												BigDecimal.ROUND_HALF_EVEN));
					}
				} else {

					if (bWaehrungsfehlerBereitsEinmalAngezeigt == false) {

						// PJ17174
						MessageFormat mf = new MessageFormat(
								LPMain.getTextRespectUISPr(
												"artikel.error.keinwechselkurshinterlegt"));

						Object pattern[] = { aVkpfartikelpreislisteDtos[iPreislisteI]
								.getCNr()
								+ " ("
								+ aVkpfartikelpreislisteDtos[iPreislisteI]
										.getWaehrungCNr() + ")" };

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), mf
								.format(pattern));
						bWaehrungsfehlerBereitsEinmalAngezeigt = true;
					}

				}

			}

			// wenn der berechnete Preis unter dem minimalen Verkaufspreis liegt
			// -> rote Markierung
			// setColorBerechneterPreis(iPreislisteI);

			if (wdfPreisGueltigab[iPreislisteI].getDate() == null) {
				wdfPreisGueltigab[iPreislisteI].setDate(new Date(System
						.currentTimeMillis()));
			}
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

	public void propertyChange(PropertyChangeEvent evt) {
		try {
			if (evt.getSource() == wdfPreisgueltigkeitsanzeigeab.getDisplay()
					&& evt.getPropertyName().equals("date")) {
				datGueltigkeitsanzeigeab = wdfPreisgueltigkeitsanzeigeab
						.getDate();

				// gesamte Anzeige aktualisieren
				refreshPreise();
			}
		} catch (Throwable t) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr(
							"vkpf.error.preisgueltigkeitsanzeigeab"));
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (wnfEinzelvkp.getText() == null
				|| wnfEinzelvkp.getText().length() == 0) {
			wnfEinzelvkp.setBigDecimal(new BigDecimal(0));
		}

		super.eventActionUpdate(aE, false);
		setPreisgueltigkeitsanzeigeab(false);

	}

	/**
	 * Wenn eines der abhaengigen Felder geaendert wurde, dann muss der
	 * angezeigte Preis geaendert werden.
	 * 
	 * @throws Throwable
	 */
	private void berechnePreis() throws Throwable {
		BigDecimal preisBasis;
		if (vkPreisBasisLief1Preis)
			preisBasis = wnfLief1Preis.getBigDecimal();
		else
			preisBasis = wnfEinzelvkp.getBigDecimal();

		if (preisBasis != null) {
			for (int i = 0; i < wlaPreislistenname.length; i++) {
				preisSetzen(i);
			}
		}
	}

	// PJ 15081
	private void initRabatt(boolean initNulls) throws Throwable {
		for (int i = 0; i < wlaPreislistenname.length; i++) {
			if (wnfStandardrabattsatz[i].getBigDecimal() == null) {
				if (initNulls
						&& aVkpfartikelpreislisteDtos[i]
								.getNStandardrabattsatz() == null) {
					wnfStandardrabattsatz[i].setDouble(new Double(0));
				} else {
					wnfStandardrabattsatz[i]
							.setBigDecimal(aVkpfartikelpreislisteDtos[i]
									.getNStandardrabattsatz());
				}
			}
		}
	}
	/*
	 * private void setColorBerechneterPreis(int index) throws Throwable {
	 * 
	 * BigDecimal bdMinverkaufspreis = new BigDecimal(0);
	 * 
	 * try { bdMinverkaufspreis = DelegateFactory.getInstance()
	 * .getLagerDelegate().getMindestverkaufspreis( ((InternalFrameArtikel)
	 * getInternalFrame()) .getArtikelDto().getIId(),
	 * DelegateFactory.getInstance() .getLagerDelegate()
	 * .getHauptlagerDesMandanten() .getIId()); bdMinverkaufspreis =
	 * Helper.rundeKaufmaennisch( bdMinverkaufspreis, Defaults.getInstance()
	 * .getIUINachkommastellenPreise());
	 * 
	 * } catch (Throwable t) { // wenn der minVerkaufspreis nicht gefunden wird,
	 * z.B. der // Artikel liegt nicht am Hauptlager -> 0 anzeigen }
	 * 
	 * 
	 * if (bdMinverkaufspreis != null && wnfBerechneterPreis != null &&
	 * wnfBerechneterPreis.length > 0) { double dMinverkaufspreis =
	 * bdMinverkaufspreis .doubleValue(); Double dBerechneterPreis =
	 * wnfBerechneterPreis[index].getDouble();
	 * 
	 * if (dBerechneterPreis < dMinverkaufspreis) {
	 * wnfBerechneterPreis[index].setForeground(Color.red); } else {
	 * wnfBerechneterPreis[index].setForeground(Color.black); } } }
	 */
}

class PanelVkpfPreise_artikelfixpreis_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelVkpfPreise adaptee;

	PanelVkpfPreise_artikelfixpreis_focusAdapter(PanelVkpfPreise adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.artikelfixpreis_focusLost(e);
	}
}

class PanelVkpfPreise_standardrabattsatz_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelVkpfPreise adaptee;

	PanelVkpfPreise_standardrabattsatz_focusAdapter(PanelVkpfPreise adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.standardrabattsatz_focusLost(e);
		} catch (Throwable ex) {
			ex.printStackTrace();
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class PanelVkpfPreise_wnfEinzelvkp_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelVkpfPreise adaptee;

	PanelVkpfPreise_wnfEinzelvkp_focusAdapter(PanelVkpfPreise adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfEinzelvkp_focusLost(e);
	}
}

class PanelVkpfPreise_wbuNaechsterTag_actionAdapter implements ActionListener {
	private PanelVkpfPreise adaptee;

	PanelVkpfPreise_wbuNaechsterTag_actionAdapter(PanelVkpfPreise adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuNaechsterTag_actionPerformed(e);
	}
}

class PanelVkpfPreise_wbuTagZurueck_actionAdapter implements ActionListener {
	private PanelVkpfPreise adaptee;

	PanelVkpfPreise_wbuTagZurueck_actionAdapter(PanelVkpfPreise adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuTagZurueck_actionPerformed(e);
	}
}
