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
package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.bestellung.HelperBestellung;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Basisklasse fuer Bestellvorschlag.</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>21.10.05</I></p>
 * 
 * <p> </p>
 * 
 * @author Josef Erlinger
 * 
 * @version $Revision: 1.15 $
 */
public class PanelPositionenBestellvorschlag extends
		PanelPositionenPreiseingabe {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// eigenes Panel fuer die Felder ist wegen der Positionierung notwendig
	private JPanel jPanel = null;

	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;

	private WrapperGotoButton wbuArtikellieferant = null;
	private WrapperButton wbuLieferant = null;
	private WrapperTextField wtfLieferant = null;

	private WrapperLabel wlaMaterialzuschlagInfo = null;
	private WrapperNumberField wnfMaterialzuschlagInfo = null;
	private WrapperLabel wlaMaterialzuschlagWaehrung = null;

	private PanelQueryFLR panelQueryFLRArtikelLieferant = null;
	private PanelQueryFLR panelQueryFLRLieferant = null;

	private WrapperSelectField wsfProjekt = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), true);

	public final static String ACTION_SPECIAL_ARTIKELLIEFERANT = "action_special_artikellieferant";
	public final static String ACTION_SPECIAL_LIEFERANT_BESTELLUNG = "action_special_lieferant_bestellung";

	private WrapperLabel wlaLieferantArtikelBezeichnung = null;
	private WrapperTextField wtfLieferantArtikelBezeichnung = null;

	private BestellvorschlagDto bestellvorschlagDto = null;
	private LieferantDto lieferantDto = null;

	public WrapperButton wbuPreisauswahl = null;
	static final public String ACTION_SPECIAL_EK_PREIS_HOLEN = "action_special_ek_preis_holen";

	// JE: auf Staffelmengen entweder bei focusLost oder spaetestens beim
	// Speichern pruefen, aber nicht mehrfach
	private boolean checkMenge = true;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param iSpaltenbreite1I
	 *            die Breite der ersten Spalte
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelPositionenBestellvorschlag(InternalFrame internalFrame,
			String add2TitleI, Object key, int iSpaltenbreite1I)
			throws Throwable {
		super(internalFrame, add2TitleI, key,
				HelperClient.LOCKME_BESTELLVORSCHLAG,
				internalFrame.bRechtDarfPreiseSehenEinkauf,
				internalFrame.bRechtDarfPreiseAendernEinkauf, iSpaltenbreite1I);
		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));

		// Zeile - die Toolbar
		add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// ohne Button new
		resetToolsPanel();

		// zusaetzliche Buttons setzen
		String[] aWhichButtonIUse = {
				ACTION_UPDATE,
				ACTION_SAVE,
				(getInternalFrame() instanceof InternalFrameBestellung) ? ACTION_DELETE
						: null, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

		// hier kommen die Felder drauf

		jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());

		iZeile++;
		add(jPanel, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// alle speziellen Felder
		wlaLiefertermin = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.liefertermin"));
		wdfLiefertermin = new WrapperDateField();

		wbuArtikellieferant = new WrapperGotoButton(
				LPMain.getTextRespectUISPr("label.lieferantendesartikels"),
				WrapperGotoButton.GOTO_LIEFERANT_AUSWAHL);
		wbuArtikellieferant.setActionCommand(ACTION_SPECIAL_ARTIKELLIEFERANT);
		wbuArtikellieferant.addActionListener(this);

		wbuLieferant = new WrapperButton(
				LPMain.getTextRespectUISPr("label.lieferant"));
		wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT_BESTELLUNG);
		wbuLieferant.addActionListener(this);

		wtfLieferant = new WrapperTextField();
		wtfLieferant.setActivatable(false);
		wtfLieferant.setMandatoryField(true);
		wtfLieferant.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaMaterialzuschlagInfo = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.materialzuschlag"));
		wlaMaterialzuschlagWaehrung = new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung());
		wlaMaterialzuschlagWaehrung
				.setHorizontalAlignment(SwingConstants.LEADING);
		wnfMaterialzuschlagInfo = new WrapperNumberField();
		wnfMaterialzuschlagInfo.setActivatable(false);

		wlaLieferantArtikelBezeichnung = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.artikelbezeichnungbeimlieferanten"));
		wtfLieferantArtikelBezeichnung = new WrapperTextField();
		wtfLieferantArtikelBezeichnung.setActivatable(false);
		wtfLieferantArtikelBezeichnung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		// Zeile 1 + 2 ist der Artikelblock
		int iGridBagLayout = 0;
		addArtikelblock(jPanel, iGridBagLayout);

		iGridBagLayout++;
		iGridBagLayout++;
		addFormatierungszeileNettoeinzelpreis(jPanel, iGridBagLayout);

		iGridBagLayout++;
		addZeileRabattsumme(jPanel, iGridBagLayout);

		iGridBagLayout++;
		addZeileMaterialzuschlag(jPanel, iGridBagLayout + 2);

		jPanel.add(wlaMaterialzuschlagInfo, new GridBagConstraints(3,
				iGridBagLayout + 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		jPanel.add(wnfMaterialzuschlagInfo, new GridBagConstraints(6,
				iGridBagLayout + 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jPanel.add(wlaMaterialzuschlagWaehrung, new GridBagConstraints(7,
				iGridBagLayout + 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		//

		iGridBagLayout++;
		addZeileNettogesamtpreis(jPanel, iGridBagLayout, false);

		wbuPreisauswahl = new WrapperButton();
		HelperClient.setDefaultsToComponent(wbuPreisauswahl, 70);

		wbuPreisauswahl.setActionCommand(ACTION_SPECIAL_EK_PREIS_HOLEN);
		wbuPreisauswahl.addActionListener(this);
		wbuPreisauswahl.setText(LPMain.getTextRespectUISPr("button.preis"));

		// darf Preis sehen Recht, keinen Button zeigen, wenn nicht erlaubt
		if (!bRechtDarfPreiseSehen) {
			wbuPreisauswahl.setVisible(false);
		} else {
			wlaEinzelpreis.setVisible(false);
		}

		jPanel.add(wbuPreisauswahl, new GridBagConstraints(5, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		wtfArtikel.setMandatoryField(true);
		wnfMenge.addFocusListener(new WnfMengeVorschlagFocusAdapter(this));
		wtfBezeichnung.setActivatable(false);
		wcoEinheit.setActivatable(false);

		// jetzt alle zusaetzlichen Felder
		iGridBagLayout++;
		jPanel.add(wlaLiefertermin, new GridBagConstraints(0, iGridBagLayout,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanel.add(wdfLiefertermin, new GridBagConstraints(1, iGridBagLayout,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iGridBagLayout++;
		jPanel.add(wbuArtikellieferant, new GridBagConstraints(0,
				iGridBagLayout, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanel.add(wbuLieferant, new GridBagConstraints(3, iGridBagLayout, 5,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iGridBagLayout++;
		jPanel.add(wtfLieferant, new GridBagConstraints(0, iGridBagLayout, 8,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iGridBagLayout++;
		jPanel.add(wlaLieferantArtikelBezeichnung, new GridBagConstraints(0,
				iGridBagLayout, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanel.add(wtfLieferantArtikelBezeichnung, new GridBagConstraints(3,
				iGridBagLayout, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			iGridBagLayout++;
			jPanel.add(wsfProjekt.getWrapperGotoButton(),
					new GridBagConstraints(0, iGridBagLayout, 3, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
			jPanel.add(wsfProjekt.getWrapperTextField(),
					new GridBagConstraints(3, iGridBagLayout, 5, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
		}
	}

	private void initPanel() throws Throwable {
		setWaehrungCNr(LPMain.getTheClient().getSMandantenwaehrung());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELLIEFERANT)) {
			panelQueryFLRArtikelLieferant = new PanelQueryFLR(null,
					BestellungFilterFactory.getInstance()
							.createArtikellieferantForThisArtikel(
									getArtikelDto().getIId()),
					QueryParameters.UC_ID_ARTIKELLIEFERANT, null,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("title.artikellieferantliste"));

			panelQueryFLRArtikelLieferant.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance()
							.createFKDArtikellieferantPartner(),
					ArtikelFilterFactory.getInstance()
							.createFKDArtikellieferantPartnerOrt());

			new DialogQuery(panelQueryFLRArtikelLieferant);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LIEFERANT_BESTELLUNG)) {
			panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
					.createPanelFLRLieferantGoto(
							getInternalFrame(),
							(lieferantDto != null) ? lieferantDto.getIId()
									: null, true, false);
			new DialogQuery(panelQueryFLRLieferant);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_EK_PREIS_HOLEN)) {
			wnfMengeVorschlagFocusLost(null);
		}
	}

	protected void setDefaults() throws Throwable {
		leereAlleFelder(this);

		super.setDefaults();

		bestellvorschlagDto = new BestellvorschlagDto();
		lieferantDto = new LieferantDto();

		wdfLiefertermin.setTimestamp(new Timestamp(System.currentTimeMillis()));
		wnfRabattsumme.getWrbFixNumber().setSelected(true);
	}

	protected void components2Dto() throws Throwable {
		bestellvorschlagDto.setCMandantCNr(LPMain.getInstance().getTheClient()
				.getMandant());
		bestellvorschlagDto.setIArtikelId(getArtikelDto().getIId());
		bestellvorschlagDto.setNZubestellendeMenge(wnfMenge.getBigDecimal());
		bestellvorschlagDto.setTLiefertermin(wdfLiefertermin.getTimestamp());
		bestellvorschlagDto.setCBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
		bestellvorschlagDto.setIBelegartId(null);
		bestellvorschlagDto.setILieferantId(lieferantDto.getIId());

		// Bestellvorschlag in Mandantenwaehrung == UI-Anzeige
		bestellvorschlagDto
				.setNNettoeinzelpreis(wnfEinzelpreis.getBigDecimal());
		bestellvorschlagDto.setDRabattsatz(getWnfRabattsatz().getDouble());
		bestellvorschlagDto.setNRabattbetrag(wnfRabattsumme.getBigDecimal());
		// UW->JE Nettogesamtpreisminusrabatte -> Spalte entfernen?
		bestellvorschlagDto.setNNettogesamtpreis(wnfEinzelpreis.getBigDecimal()
				.subtract(wnfRabattsumme.getBigDecimal()));

		bestellvorschlagDto.setBNettopreisuebersteuert(Helper
				.boolean2Short(wnfNettopreis.getWrbFixNumber().isSelected()));
		bestellvorschlagDto.setProjektIId(wsfProjekt.getIKey());

		// @todo UW->JE koennte man am Server machen? JE ja PJ 5038
		checkArtikellieferantAndCreateOrUpdate();
	}

	protected void dto2Components() throws Throwable {
		artikelDto2components();

		// die Bezeichnung kann durch die Positionen uebersteuert werden
		String cBezeichnungUebersteuert = null;

		if (bestellvorschlagDto.getIBelegartId() != null) {
			if (bestellvorschlagDto.getCBelegartCNr().equals(
					LocaleFac.BELEGART_AUFTRAG)) {
				AuftragpositionDto auftragpositionDto = DelegateFactory
						.getInstance()
						.getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey(
								bestellvorschlagDto.getIBelegartpositionid());

				if (auftragpositionDto.getCBez() != null
						&& auftragpositionDto.getCBez().length() > 0) {
					cBezeichnungUebersteuert = auftragpositionDto.getCBez();
				}
			} else if (bestellvorschlagDto.getCBelegartCNr().equals(
					LocaleFac.BELEGART_BESTELLUNG)) {
				BestellpositionDto bestellpositionDto = DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellpositionFindByPrimaryKey(
								bestellvorschlagDto.getIBelegartpositionid());

				if (bestellpositionDto.getCBez() != null
						&& bestellpositionDto.getCBez().length() > 0) {
					cBezeichnungUebersteuert = bestellpositionDto.getCBez();
				}
			}
		}

		if (cBezeichnungUebersteuert != null) {
			wtfBezeichnung.setText(cBezeichnungUebersteuert);
		}

		wnfMenge.setBigDecimal(bestellvorschlagDto.getNZubestellendeMenge());
		wdfLiefertermin.setTimestamp(bestellvorschlagDto.getTLiefertermin());

		if (lieferantDto.getIId() != null) {
			wbuArtikellieferant.setOKey(lieferantDto.getIId());
			wtfLieferant.setText(lieferantDto.getPartnerDto()
					.formatTitelAnrede()); // abgespeichert ist der
			// Lieferant

			String cArtikelBez = null;

			ArtikellieferantDto artikellieferantDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
							getArtikelDto().getIId(), lieferantDto.getIId());

			if (artikellieferantDto != null) {
				cArtikelBez = artikellieferantDto.getCBezbeilieferant(); // nur
				// Anzeige
			}

			wtfLieferantArtikelBezeichnung.setText(cArtikelBez);
		} else {
			wbuArtikellieferant.setOKey(null);
			wtfLieferant.setText("");
			wtfLieferantArtikelBezeichnung.setText("");
		}

		// PJ18006 Materialzuschlag nur als Info anzeigen
		setVisibleZeileMaterialzuschlag(false);
		wnfMaterialzuschlag.setBigDecimal(null);
		// der Bestellvorschlag wird in Mandantenwaehrung abgespeichert und
		// angezeigt UW->JE
		if (Helper.short2boolean(bestellvorschlagDto
				.getBNettopreisuebersteuert())) {
			wnfNettopreis.getWrbFixNumber().setSelected(true);
		} else {
			wnfRabattsumme.getWrbFixNumber().setSelected(true);
		}

		wnfEinzelpreis
				.setBigDecimal(bestellvorschlagDto.getNNettoeinzelpreis());
		getWnfRabattsatz().setDouble(bestellvorschlagDto.getDRabattsatz());
		wnfRabattsumme.setBigDecimal(bestellvorschlagDto.getNRabattbetrag());
		wnfNettopreis.setBigDecimal(bestellvorschlagDto.getNNettogesamtpreis());
		// UW->JE Nettogesamtpreisminusrabatte -> Spalte entfernen?
		wnfMaterialzuschlagInfo.setVisible(false);
		wlaMaterialzuschlagInfo.setVisible(false);
		wlaMaterialzuschlagWaehrung.setVisible(false);
		wnfMaterialzuschlagInfo.setBigDecimal(new BigDecimal(0));

		if (getArtikelDto().getMaterialIId() != null
				&& lieferantDto.getIId() != null) {

			wnfMaterialzuschlagInfo.setVisible(true);
			wlaMaterialzuschlagInfo.setVisible(true);
			wlaMaterialzuschlagWaehrung.setVisible(true);

			BigDecimal zuschlag = DelegateFactory
					.getInstance()
					.getMaterialDelegate()
					.getMaterialzuschlagEKInZielwaehrung(
							getArtikelDto().getIId(), lieferantDto.getIId(),
							new java.sql.Date(System.currentTimeMillis()),
							LPMain.getTheClient().getSMandantenwaehrung());

			wnfMaterialzuschlagInfo.setBigDecimal(zuschlag);
		}
		
		wsfProjekt.setKey(bestellvorschlagDto.getProjektIId());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged((ItemChangedEvent) eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferant) {
				Integer iIdLieferant = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (iIdLieferant != null) {
					lieferantDto = DelegateFactory.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(iIdLieferant);
					wbuArtikellieferant.setOKey(lieferantDto.getIId());
					wtfLieferant.setText(lieferantDto.getPartnerDto()
							.formatFixTitelName1Name2());

					if (bestellvorschlagDto.getIArtikelId() != null) {
						// MB 17.05.06 mit WH ausgemacht:
						// 1. schaun, obs einen artikellieferant-eintrag zu
						// diesem lieferanten gibt
						ArtikellieferantDto alDto = DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
										bestellvorschlagDto.getIArtikelId(),
										iIdLieferant);

						// 2. wenns einen gibt, dann gleiches Verhalten wie bei
						// Artikellieferant-Auswahl
						if (alDto != null) {
							// artikellieferantDto in Mandantenwaehrung holen
							ArtikellieferantDto artikellieferantDto = DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(
											bestellvorschlagDto.getIArtikelId(),
											iIdLieferant,
											LPMain.getInstance().getTheClient()
													.getSMandantenwaehrung());
							if (artikellieferantDto != null) {

								wtfLieferantArtikelBezeichnung
										.setText(artikellieferantDto
												.getCBezbeilieferant());

								wnfEinzelpreis
										.setBigDecimal(artikellieferantDto
												.getNEinzelpreis());
								getWnfRabattsatz().setDouble(
										artikellieferantDto.getFRabatt());

								BigDecimal nettopreis = artikellieferantDto
										.getNNettopreis();

								if (wnfMaterialzuschlag != null
										&& wnfMaterialzuschlag.getBigDecimal() != null) {
									nettopreis = nettopreis
											.add(wnfMaterialzuschlag
													.getBigDecimal());
								}

								wnfNettopreis.setBigDecimal(nettopreis);

								if (artikellieferantDto.getNNettopreis() != null
										&& artikellieferantDto
												.getNEinzelpreis() != null) {
									wnfRabattsumme
											.setBigDecimal(artikellieferantDto
													.getNEinzelpreis()
													.subtract(
															artikellieferantDto
																	.getNNettopreis()));
								}
							}
						} else {
							// 3. sonst alles unveraendert lassen
						}
					}
				} else {
					wbuArtikellieferant.setOKey(null);
				}
			} else if (e.getSource() == wifArtikelauswahl
					.getPanelQueryFLRArtikel()) {
				Integer artikelIId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				ArtikellieferantDto[] dtos = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikellieferantFindByArtikelIId(artikelIId);

				if (dtos.length > 0) {
					holeArtikellieferant(dtos[0].getIId());
				}
			}

			else if (e.getSource() == panelQueryFLRArtikelLieferant) {
				Integer iIdArtikelLieferant = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				if (iIdArtikelLieferant != null) {
					holeArtikellieferant(iIdArtikelLieferant);
				} else {
					wbuArtikellieferant.setOKey(null);
				}
			}
		}
	}

	private void holeArtikellieferant(Integer iIdArtikelLieferant)
			throws ExceptionLP, Throwable {

		ArtikellieferantDto alDto = DelegateFactory.getInstance()
				.getArtikelDelegate()
				.artikellieferantFindByPrimaryKey(iIdArtikelLieferant);
		// nun den Preis in Mandantenwaehrung holen
		ArtikellieferantDto artikellieferantDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikellieferantFindByIIdInWunschwaehrung(
						alDto.getIId(),
						LPMain.getInstance().getTheClient()
								.getSMandantenwaehrung());
		if (artikellieferantDto != null) {
			lieferantDto = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							artikellieferantDto.getLieferantIId());
			wbuArtikellieferant.setOKey(lieferantDto.getIId());
			wtfLieferant.setText(lieferantDto.getPartnerDto()
					.formatFixTitelName1Name2());

			wtfLieferantArtikelBezeichnung.setText(artikellieferantDto
					.getCBezbeilieferant());

			wnfEinzelpreis.setBigDecimal(artikellieferantDto.getNEinzelpreis());
			getWnfRabattsatz().setDouble(artikellieferantDto.getFRabatt());

			BigDecimal nettopreis = artikellieferantDto.getNNettopreis();

			if (wnfMaterialzuschlag != null
					&& wnfMaterialzuschlag.getBigDecimal() != null) {
				nettopreis = nettopreis
						.add(wnfMaterialzuschlag.getBigDecimal());
			}

			wnfNettopreis.setBigDecimal(nettopreis);

			if (artikellieferantDto.getNNettopreis() != null
					&& artikellieferantDto.getNEinzelpreis() != null) {
				wnfRabattsumme.setBigDecimal(artikellieferantDto
						.getNEinzelpreis().subtract(
								artikellieferantDto.getNNettopreis()));
			}
		}
		// @todo UW->JE hier muessen auch die hinterlegten Preise angezeigt
		// werden! PJ 5039

	}

	protected PropertyVetoException eventActionVetoableChangeLP()
			throws Throwable {
		PropertyVetoException pve = super.eventActionVetoableChangeLP();
		DelegateFactory.getInstance().getBestellvorschlagDelegate()
				.removeLockDesBestellvorschlagesWennIchIhnSperre();
		return pve;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// zuerst alles zuruecksetzen, Ausloeser war ev. Button Discard
		setDefaults();

		// Position neu einlesen, ausloeser war ev. Button Refresh oder Discard
		Object key = getKeyWhenDetailPanel();

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {
			bestellvorschlagDto = DelegateFactory.getInstance()
					.getBestellvorschlagDelegate()
					.bestellvorschlagFindByPrimaryKey((Integer) key);

			if (bestellvorschlagDto.getILieferantId() != null) {
				lieferantDto = DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								bestellvorschlagDto.getILieferantId());
			}

			if (bestellvorschlagDto.getIArtikelId() != null) {
				setArtikelDto(DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								bestellvorschlagDto.getIArtikelId()));
			}
			dto2Components();
			/**
			 * @todo Titel und Statusbar PJ 5040
			 */
		}
	}

	/**
	 * Fuer einen Lieferanten pruefen, ob er fuer einen gewaehlten Artikel
	 * Artikellieferant ist. Wenn er kein Artikellieferant ist, wird er neu
	 * angelegt. Die Preise muessen dabei in der Waehrung des Lieferanten
	 * abgespeichert werden. Wenn der Artikellieferant bereits existiert,
	 * muessen die Preis aktualisiert werden.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void checkArtikellieferantAndCreateOrUpdate() throws Throwable {
		if (lieferantDto.getIId() != null && getArtikelDto().getIId() != null) {
			// Suchen, ob ein Lieferant schon Artikellieferant ist
			ArtikellieferantDto artikellieferantDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
							getArtikelDto().getIId(), lieferantDto.getIId());

			// Wenn der Lieferant noch kein Artikellieferant ist, neu anlegen
			if (artikellieferantDto == null) {
				artikellieferantDto = new ArtikellieferantDto();
				artikellieferantDto.setArtikelIId(getArtikelDto().getIId());
				artikellieferantDto.setLieferantIId(lieferantDto.getIId());
				artikellieferantDto.setMandantCNr(LPMain.getInstance()
						.getTheClient().getMandant());
				artikellieferantDto.setTPreisgueltigab(wdfLiefertermin
						.getTimestamp());
				artikellieferantDto.setBRabattbehalten(wnfRabattsumme
						.getWrbFixNumber().getShort());

				// die Anzeige ist in Mandantenwaehrung
				/**
				 * @todo Nachkommastellen nicht hart codiert
				 */
				artikellieferantDto
						.setNEinzelpreis(wnfEinzelpreis
								.getBigDecimal()
								.divide(getWechselKursVonMandantNachLieferantWaehrung(),
										2, BigDecimal.ROUND_HALF_EVEN));
				artikellieferantDto
						.setNNettopreis(wnfNettopreis
								.getBigDecimal()
								.divide(getWechselKursVonMandantNachLieferantWaehrung(),
										2, BigDecimal.ROUND_HALF_EVEN));
				artikellieferantDto.setFRabatt(getWnfRabattsatz().getDouble());
			} else {
				// update Artikellieferant wegen Preisen, vielleicht gab es
				// aufgrund der Staffelmengen eine Aenderung
				artikellieferantDto
						.setNEinzelpreis(wnfEinzelpreis
								.getBigDecimal()
								.multiply(
										getWechselKursVonMandantNachLieferantWaehrung()));
				artikellieferantDto
						.setNNettopreis(wnfNettopreis
								.getBigDecimal()
								.multiply(
										getWechselKursVonMandantNachLieferantWaehrung()));
				artikellieferantDto.setFRabatt(getWnfRabattsatz().getDouble());

				DelegateFactory.getInstance().getArtikelDelegate()
						.updateArtikellieferant(artikellieferantDto);

				// Staffelpreise nachpflegen
				ArtikellieferantstaffelDto[] artliefstaffelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikellieferantstaffelFindByArtikellieferantIId(
								artikellieferantDto.getIId());

				for (int i = 0; i < artliefstaffelDto.length; i++) {
					BigDecimal differenz = artikellieferantDto
							.getNEinzelpreis().multiply(
									new BigDecimal(artliefstaffelDto[i]
											.getFRabatt().doubleValue())
											.movePointLeft(2));
					artliefstaffelDto[i].setNNettopreis(artikellieferantDto
							.getNEinzelpreis().subtract(differenz));

					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.updateArtikellieferantstaffel(artliefstaffelDto[i]);
				}
			}
		}
	}

	private BigDecimal getWechselKursVonMandantNachLieferantWaehrung()
			throws Throwable {
		BigDecimal bdWechselkurs = null;
		// Die originale lieferantDto ueberschreibt die Modulweit geltende
		// lieferantDto (ghp)
		// LieferantDto lieferantDto = null;

		LieferantDto helperLieferantDto = null;

		// if (bestellvorschlagDto != null
		// && bestellvorschlagDto.getILieferantId() != null) {
		// helperLieferantDto = DelegateFactory
		// .getInstance()
		// .getLieferantDelegate()
		// .lieferantFindByPrimaryKey(
		// bestellvorschlagDto.getILieferantId());
		// } else {
		// helperLieferantDto =
		// DelegateFactory.getInstance().getLieferantDelegate()
		// .lieferantFindByPrimaryKey(lieferantDto.getIId());
		// }

		Integer lieferantId;
		if (bestellvorschlagDto != null
				&& bestellvorschlagDto.getILieferantId() != null) {
			lieferantId = bestellvorschlagDto.getILieferantId();
		} else {
			lieferantId = lieferantDto.getIId();
		}

		helperLieferantDto = DelegateFactory.getInstance()
				.getLieferantDelegate().lieferantFindByPrimaryKey(lieferantId);

		String sMandantenwaehrung = LPMain.getInstance().getTheClient()
				.getSMandantenwaehrung();
		if (!helperLieferantDto.getWaehrungCNr().equals(sMandantenwaehrung)) {
			bdWechselkurs = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.getWechselkurs2(sMandantenwaehrung,
							helperLieferantDto.getWaehrungCNr());
		} else {
			bdWechselkurs = new BigDecimal(1);
		}

		return bdWechselkurs;
	}

	/*
	 * private Float getWechselKursVonLieferantNachMandantWaehrung() throws
	 * Throwable { Float wechselkurs = null; LieferantDto lieferantDto = null;
	 * 
	 * if (bestellvorschlagDto != null && bestellvorschlagDto.getILieferantId()
	 * != null) { lieferantDto = getInternalFrame().getLieferantDelegate().
	 * lieferantFindByPrimaryKey(bestellvorschlagDto. getILieferantId()); } else
	 * { lieferantDto = getInternalFrame().getLieferantDelegate().
	 * lieferantFindByPrimaryKey(lieferantDto.getIId()); }
	 * 
	 * MandantDto mandantDto = getInternalFrame().getMandantDelegate().
	 * mandantFindByPrimaryKey
	 * (LPMain.getInstance().getTheClient().getMandant());
	 * 
	 * if (!lieferantDto.getWaehrungCNr().equals(mandantDto.getWaehrungCNr())) {
	 * // UW->JE != geht nicht wechselkurs =
	 * getInternalFrame().getLocaleDelegate().
	 * getWechselkurs(lieferantDto.getWaehrungCNr(),
	 * mandantDto.getWaehrungCNr()); } else { wechselkurs = new Float(1); }
	 * 
	 * return wechselkurs; }
	 */

	/**
	 * Jedes Mal, wenn die Menge geaendert wird, muss gepreuft werden, ob es
	 * hinterlegte Staffelpreise gibt, wenn ja in Mandantenwaehrung anzeigen.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	protected void wnfMengeVorschlagFocusLost(FocusEvent e) throws ExceptionLP,
			Throwable {
		// diese Logik wird in mehreren Panels verwendet!

		// waehrung mitgeben damit in Methode
		// HelperBestellung.setzePreisfelder() die Wahrung in
		// Mandantwaehrung umgerechnet wird
		MandantDto mandantDto = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant());

		checkMenge = HelperBestellung.getInstance()
				.checkStaffelpreiseUndMengen(getArtikelDto(),
						getInternalFrame(), checkMenge, wnfMenge,
						wnfNettopreis, getWnfRabattsatz(), wnfRabattsumme,
						wnfEinzelpreis, lieferantDto,
						mandantDto.getWaehrungCNr(), true);

		// Preise muessen durch HelperBestellung in Lieferantenwaehrung
		// angezeigt worden sein!
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false); // LockMeForNew setzen

		setDefaults();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		setArtikelEingabefelderEditable(false);
		checkMenge = true;
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, false);

		setArtikelEingabefelderEditable(true);

		// wenn es noch keine Preise gibt, die Preisfelder mit 0.0 vorbelegen
		if (bestellvorschlagDto.getNNettoeinzelpreis() == null) {
			BigDecimal nDefault = new BigDecimal(0);

			wnfEinzelpreis.setBigDecimal(nDefault);
			getWnfRabattsatz().setBigDecimal(nDefault);
			wnfRabattsumme.setBigDecimal(nDefault);
			wnfNettopreis.setBigDecimal(nDefault);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			// checken ob mindestbestellmenge unterschritten wurde nur wenn
			// checkmenge false
			if (checkMenge) {
				boolean answer = HelperBestellung.getInstance()
						.checkMindestbestellmenge(getInternalFrame(),
								getArtikelDto(), wnfMenge, wnfNettopreis,
								getWnfRabattsatz(), wnfRabattsumme,
								wnfEinzelpreis, lieferantDto);

				if (!answer) {
					return;
				}
			}
			checkMenge = true;
			components2Dto();

			if (bestellvorschlagDto.getIId() == null) {
				Integer bestellvorschlagIId = DelegateFactory.getInstance()
						.getBestellvorschlagDelegate()
						.createBestellvorschlag(bestellvorschlagDto);

				bestellvorschlagDto = DelegateFactory.getInstance()
						.getBestellvorschlagDelegate()
						.bestellvorschlagFindByPrimaryKey(bestellvorschlagIId);

				setKeyWhenDetailPanel(bestellvorschlagIId);
			} else {
				DelegateFactory.getInstance().getBestellvorschlagDelegate()
						.updateBestellvorschlag(bestellvorschlagDto);
			}

			super.eventActionSave(e, true);

			if (wdfLiefertermin.getDate() != null) {

				ArtikellieferantDto artLiefDtoBilliger = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.getGuenstigstenEKPreis(
								getArtikelDto().getIId(),
								wnfMenge.getBigDecimal(),
								wdfLiefertermin.getDate(),
								LPMain.getInstance().getTheClient()
										.getSMandantenwaehrung(),
								bestellvorschlagDto.getILieferantId());

				if (artLiefDtoBilliger != null
						&& artLiefDtoBilliger.getNNettopreis() != null
						&& artLiefDtoBilliger.getLieferantIId() != null
						&& artLiefDtoBilliger.getNNettopreis().doubleValue() < wnfNettopreis
								.getBigDecimal().doubleValue()) {

					LieferantDto liefBilliger = DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(
									artLiefDtoBilliger.getLieferantIId());

					StringBuffer sb = new StringBuffer();
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("bes.lief.billiger"));
					mf.setLocale(LPMain.getTheClient().getLocUi());
					Object pattern[] = {
							liefBilliger.getPartnerDto()
									.formatFixTitelName1Name2(),
							Helper.formatZahl(artLiefDtoBilliger
									.getNNettopreis(), LPMain.getTheClient()
									.getLocUi())
									+ " "
									+ LPMain.getInstance().getTheClient()
											.getSMandantenwaehrung() };
					sb.append(mf.format(pattern));

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.info"),
							sb.toString());
				}
			}
			eventYouAreSelected(true);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (getInternalFrame() instanceof InternalFrameBestellung)
			((InternalFrameBestellung) getInternalFrame())
					.getTabbedPaneBestellvorschlag().deleteAuswahl();
		// DelegateFactory.getInstance().getBestellvorschlagDelegate()
		// .removeBestellvorschlag(bestellvorschlagDto);
		// this.setKeyWhenDetailPanel(null);
		// super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
		// ueberschreiben
	}

	public BestellvorschlagDto getBestellvorschlagDto() {
		return bestellvorschlagDto;
	}
}

@SuppressWarnings("static-access")
class WnfMengeVorschlagFocusAdapter extends java.awt.event.FocusAdapter {
	PanelPositionenBestellvorschlag adaptee;

	WnfMengeVorschlagFocusAdapter(PanelPositionenBestellvorschlag adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfMengeVorschlagFocusLost(e);
		} catch (Throwable t) {
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}
