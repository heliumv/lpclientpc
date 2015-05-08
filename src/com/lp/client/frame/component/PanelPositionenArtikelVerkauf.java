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
package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.EventObject;

import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.PanelDialogPreisvorschlag;
import com.lp.client.frame.PanelDialogPreisvorschlagKundesokomengenstaffel;
import com.lp.client.frame.PanelDialogPreisvorschlagPreisliste;
import com.lp.client.frame.PanelDialogPreisvorschlagVkmengenstaffel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Basisfenster fuer LP5 Positionen.</p> <p>Copyright Logistik Pur Software
 * GmbH (c) 2004-2008</p> <p>Erstellungsdatum 2005-02-11</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.54 $
 */
public class PanelPositionenArtikelVerkauf extends PanelPositionenPreiseingabe {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Den ermittelten Verkaufspreis in Zielwaehrung hinterlegen. */
	public VerkaufspreisDto verkaufspreisDtoInZielwaehrung = null;

	/** Die Ergebnisse der VK Preisfindung hinterlegen. */
	private VkpreisfindungDto vkpreisfindungDto = null;

	/** Den Kunden mit seiner Standardpreisliste hinterlegen. */
	private KundeDto kundeDto = null;
	/** Das Gueltigkeitsdatum fuer den Artikeleinzelverkaufspreis hinterlegen. */
	private Date datGueltigkeitsdatumArtikeleinzelverkaufspreis = null;
	/** Die Waehrung fuer die Berechnung der Preise hinterlegen. */
	private String cNrWaehrung = null;
	/** Den Wechselkurs fuer die Berechnung der Preise hinterlegen. */
	private Double ddWechselkurs = null;
	/**
	 * Wenn eine Preisliste ueber PanelDialogPreisvorschlag geawehlt wurde, wird
	 * sie hier hinterlegt.
	 */
	private Integer iIdPreislisteGewaehlt = null;
	/**
	 * Wenn eine VK-Staffelmenge ueber PanelDialogPreisvorschlag gewaehlt wurde,
	 * wird sie hier hinterlegt.
	 */
	private Integer vkStaffelmengeIId = null;
	/**
	 * Wenn eine Kundensonderkondition ueber PanelDialogPreisvorschlag gewaehlt
	 * wurde, wird sie hier hinterlegt.
	 */
	private Integer kundesokostaffelmengeIId = null;
	/** Das Lager, das dem Beleg zugeordnet ist. */
	protected Integer iIdLager = null;

	/**
	 * Den Vorschlagswert fuer den Mwstsatz hinterlegen, damit man sehen kann,
	 * ob manipuliert wurde.
	 */
	public Integer iIdMwstsatzDefault = null;

	/**
	 * Wurde der Vorschlagswert fuer den Rabattsatz in diesem Fenster manuell
	 * uebersteuert?
	 */
	public boolean bIstRabattsatzDefaultUebersteuert = false;
	/**
	 * Wurde der Vorschlagswert fuer den Mwstsatz in diesem Fenster manuell
	 * uebersteuert?
	 */
	public boolean bIstMwstsatzDefaultUebersteuert = false;
	/**
	 * Wurde der Vorschlagswert fuer den Nettoeinzelpreis in diesem Fenster
	 * manuell uebersteuert?
	 */
	public boolean bIstNettoeinzelpreisUebersteuert = false;
	/**
	 * Wurde der Vorschlagswert fuer den Nettogesamtpreis in diesem Fenster
	 * manuell uebersteuert?
	 */
	public boolean bIstNettogesamtpreisUebersteuert = false;

	private int iYGridBagNext = 0;

	// einfache Preisauswahl ueber Dialog
	public WrapperButton wbuPreisauswahl = null;
	public PanelDialogPreisvorschlag pdPreisvorschlag = null;

	static final public String ACTION_SPECIAL_PREISAUSWAHL_FROM_DIALOG = "action_special_preisauswahl_from_dialog";

	/** Ableitende Panels muessen den FocusListener wieder entfernen koennen. */
	public FocusListener wnfMengeFocusListener = null;

	/** Der Fixpreis des Artikels, ist der Nettopreis. */
	private BigDecimal nFixPreis = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param sLockMeWer
	 *            String
	 * @param iSpaltenbreite1I
	 *            die Breite der ersten Spalte
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelPositionenArtikelVerkauf(InternalFrame internalFrame,
			String add2TitleI, Object key, String sLockMeWer,
			int iSpaltenbreite1I) throws Throwable {
		super(internalFrame, add2TitleI, key, sLockMeWer,
				internalFrame.bRechtDarfPreiseSehenVerkauf,
				internalFrame.bRechtDarfPreiseAendernVerkauf, iSpaltenbreite1I);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		setLayout(new GridBagLayout());

		// Zeile 1 + 2 ist der Artikelblock
		addArtikelblock(this, iYGridBagNext);

		// Zeile
		iYGridBagNext++;
		iYGridBagNext++;
		addFormatierungszeileNettoeinzelpreis(this, iYGridBagNext);

		// Zeile
		iYGridBagNext++;
		addZeileRabattsumme(this, iYGridBagNext);

		// poszusatzrabatt: 1 Zeile fuer Zusatzrabattsumme auf Panel hinzufuegen
		iYGridBagNext++;
		addZeileZusatzrabattsumme(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileMaterialzuschlag(this, iYGridBagNext);

		// Zeile
		iYGridBagNext++;
		addZeileNettogesamtpreis(this, iYGridBagNext, true);

		// Zeile
		iYGridBagNext++;
		addZeileMwstsumme(this, iYGridBagNext);

		// Zeile
		iYGridBagNext++;
		addZeileBruttogesamtpreis(this, iYGridBagNext);

		// einfache Preisauswahl ueber Dialog
		remove(wlaEinzelpreis);
		wbuPreisauswahl = new WrapperButton();
		HelperClient.setDefaultsToComponent(wbuPreisauswahl, 70);
		wbuPreisauswahl
				.setActionCommand(ACTION_SPECIAL_PREISAUSWAHL_FROM_DIALOG);
		wbuPreisauswahl.setText(LPMain.getTextRespectUISPr("button.preis"));
		wbuPreisauswahl.addActionListener(this);
		// darf Preis sehen Recht, keinen Button zeigen, wenn nicht erlaubt
		if (!bRechtDarfPreiseSehen) {
			wbuPreisauswahl.setVisible(false);
		}
		if (bRechtDarfPreiseAendern == false) {
			wbuPreisauswahl.setEnabled(false);
		}

		add(wbuPreisauswahl, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
						2, 2, 2), 0, 0));

		wnfMengeFocusListener = this;
		wnfMenge.addFocusListener(wnfMengeFocusListener);
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();

		setArtikelDto(new ArtikelDto());
		verkaufspreisDtoInZielwaehrung = new VerkaufspreisDto();
		vkpreisfindungDto = new VkpreisfindungDto(LPMain.getInstance()
				.getTheClient().getLocUi());

		wtfZusatzbezeichnung.setActivatable(false);

		KundeDto kdDtoFuerMwst = kundeDto;
		// PJ17455 Wenn Lieferschein, dann kommt MWST aus Lieferadresse
		if (getInternalFrame() instanceof InternalFrameLieferschein) {
			InternalFrameLieferschein ifL = (InternalFrameLieferschein) getInternalFrame();
			kdDtoFuerMwst = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							ifL.getTabbedPaneLieferschein()
									.getLieferscheinDto()
									.getKundeIIdLieferadresse());

		}

		if (!getBDefaultMwstsatzAusArtikel()) {
			if (kdDtoFuerMwst != null) {
				Integer iIdMwstsatzbez = kdDtoFuerMwst.getMwstsatzbezIId();

				// Aktuellen MWST-Satz uebersetzen.
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(iIdMwstsatzbez);
				if (wcoMwstsatz != null) {
					wcoMwstsatz.setKeyOfSelectedItem(mwstsatzDtoAktuell
							.getIId());
				}
				iIdMwstsatzDefault = mwstsatzDtoAktuell.getIId();
			}
		} else {
			iIdMwstsatzDefault = null; // der Artikel bringt seine Mwst mit

			if (kdDtoFuerMwst != null) {
				// Ausser der Kunde hat MWST-Satz mit 0%, dann muss dieser
				// verwendet werden
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								kdDtoFuerMwst.getMwstsatzbezIId());

				if (mwstsatzDtoAktuell.getFMwstsatz().doubleValue() == 0) {
					iIdMwstsatzDefault = mwstsatzDtoAktuell.getIId();
					if (wcoMwstsatz != null) {
						wcoMwstsatz.setKeyOfSelectedItem(iIdMwstsatzDefault);
					}
				}

			}

		}
		if (getInternalFrame().bRechtDarfPreiseAendernVerkauf == false) {
			wbuPreisauswahl.setEnabled(false);
		} else {
			wbuPreisauswahl.setEnabled(true);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		super.eventItemchanged(eI);

		if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			// Source ist mein Dialog zum Preisvorschlag
			if (e.getSource() == pdPreisvorschlag) {
				iIdPreislisteGewaehlt = pdPreisvorschlag
						.getIIdPreislisteGewaehlt();
				kundesokostaffelmengeIId = pdPreisvorschlag
						.getIIdKundesokomengenstaffelZuletztGewaehlt();
				vkStaffelmengeIId = pdPreisvorschlag
						.getIIdVkmengenstaffelZuletztGewaehlt();
				verkaufspreisDtoInZielwaehrung = pdPreisvorschlag
						.getVerkaufspreisDto();
				pruefeNettoPreis();

				if (verkaufspreisDtoInZielwaehrung != null
						&& verkaufspreisDtoInZielwaehrung.mwstsatzIId != null
						&& !verkaufspreisDtoInZielwaehrung.mwstsatzIId
								.equals(wcoMwstsatz.getKeyOfSelectedItem())) {
					// 15409 mit WH besprochen: Meldung anzeigen
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.info"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.warn.mwstsatzzurueckgesetzt"));

				}

				verkaufspreisDto2components();

			}
		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == wifArtikelauswahl) {
				if (wifArtikelauswahl.getArtikelDto() != null) {
					if (getInternalFrame().bRechtDarfPreiseAendernVerkauf == true) {
						wbuPreisauswahl.setEnabled(true);
					} else {
						wbuPreisauswahl.setEnabled(false);
					}
					pruefeNettoPreis();

					// PJ 17176
					if (getTBelegdatumMwstsatz() != null) {
						KundesokoDto kundeSokoDto_gueltig = DelegateFactory
								.getInstance()
								.getKundesokoDelegate()
								.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
										kundeDto.getIId(),
										wifArtikelauswahl.getArtikelDto()
												.getIId(),
										new java.sql.Date(
												getTBelegdatumMwstsatz()
														.getTime()));
						if (kundeSokoDto_gueltig != null) {
							if (kundeSokoDto_gueltig.getCKundeartikelbez() != null) {
								wtfBezeichnung.setText(kundeSokoDto_gueltig
										.getCKundeartikelbez());
							}
							if (kundeSokoDto_gueltig.getCKundeartikelzbez() != null) {
								wtfZusatzbezeichnung
										.setText(kundeSokoDto_gueltig
												.getCKundeartikelzbez());
							}

						}
					}

				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {
			wlaMinus.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.minus"));
			wlaMinus.setToolTipText("");
			wlaMinus.setForeground(Color.BLACK);
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			pruefeGueltigBisArtikellieferant();
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false); // LockMeForNew setzen

		setDefaults();

		setArtikelEingabefelderEditable(false);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand()
				.equals(ACTION_SPECIAL_PREISAUSWAHL_FROM_DIALOG)) {
			dialogPreisvorschlag();
		}
	}

	private void dialogPreisvorschlag() throws Throwable {
		if (getArtikelDto().getIId() != null) {
			// PJ 15270
			// Wenn Fixpreis gewaehlt wurde dann nFixPreis initilisieren, da
			// dies
			// sonst nur bei Preisfindung erfolgt

			// PJ 15343 immer den Nettopreis vorschlagen

			BigDecimal nMaterialzuschlag = new BigDecimal(0);
			if (wnfMaterialzuschlag != null
					&& wnfMaterialzuschlag.getBigDecimal() != null) {
				nMaterialzuschlag = wnfMaterialzuschlag.getBigDecimal();
			}

			nFixPreis = wnfNettopreis.getBigDecimal();
			if (iIdPreislisteGewaehlt == null)

				iIdPreislisteGewaehlt = kundeDto
						.getVkpfArtikelpreislisteIIdStdpreisliste();
			nFixPreis = wnfNettopreis.getBigDecimal();

			// den Aufbau des Auswahldialogs bestimmen

			if (iIdMwstsatzDefault == null) {
				iIdMwstsatzDefault = (Integer) wcoMwstsatz
						.getKeyOfSelectedItem();
			}

			vkpreisfindungDto = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.verkaufspreisfindung(getArtikelDto().getIId(),
							kundeDto.getIId(), wnfMenge.getBigDecimal(),
							getGueltigkeitsdatumArtikeleinzelverkaufspreis(),
							iIdPreislisteGewaehlt, iIdMwstsatzDefault,
							getWaehrungCNr());

			PanelDialogPreisvorschlagDto preisvorschlagDto = new PanelDialogPreisvorschlagDto(
					getArtikelDto(), wnfMenge.getBigDecimal(), iIdLager,
					getGueltigkeitsdatumArtikeleinzelverkaufspreis(), kundeDto,
					iIdPreislisteGewaehlt, kundesokostaffelmengeIId,
					vkStaffelmengeIId, cNrWaehrung, ddWechselkurs,
					verkaufspreisDtoInZielwaehrung, vkpreisfindungDto,
					nFixPreis, nMaterialzuschlag, true, wnfNettopreis
							.getWrbFixNumber().isSelected());

			if (vkpreisfindungDto.getVkpreisberechnetStufe() != null
					&& vkpreisfindungDto.getVkpreisberechnetStufe().equals(
							VkpreisfindungDto.VKPFSTUFE3)) {
				pdPreisvorschlag = new PanelDialogPreisvorschlagKundesokomengenstaffel(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.preisvorschlag.kundesokomengenstaffel"),
						preisvorschlagDto);
			} else if (vkpreisfindungDto.getVkpreisberechnetStufe() != null
					&& vkpreisfindungDto.getVkpreisberechnetStufe().equals(
							VkpreisfindungDto.VKPFSTUFE2)) {
				pdPreisvorschlag = new PanelDialogPreisvorschlagVkmengenstaffel(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.preisvorschlag.vkmengenstaffel"),
						preisvorschlagDto);
			} else {
				pdPreisvorschlag = new PanelDialogPreisvorschlagPreisliste(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.preisvorschlag.kundepreisliste"),
						preisvorschlagDto);
			}

			getInternalFrame().showPanelDialog(pdPreisvorschlag);
		} else {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("lp.hint.artikelwaehlen"));
		}
	}

	/**
	 * Bestimmen, ob fuer einen bestimmten Artikel eine Verkaufspreisbasis
	 * hinterlegt ist.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param datGueltigkeitsdatumI
	 *            Gueltigkeitsdatum des Artikels
	 * @return boolean true, wenn eine Verkaufspreisbasis hinterlegt ist
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public boolean istArtikelverkaufspreisbasisHinterlegt(Integer iIdArtikelI,
			java.sql.Date datGueltigkeitsdatumI) throws Throwable {
		boolean bIstHinterlegt = false;

		try {
			VkPreisfindungEinzelverkaufspreisDto oEinzelverkaufspreisDto = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.getArtikeleinzelverkaufspreis(iIdArtikelI,
							datGueltigkeitsdatumI,
							LPMain.getTheClient().getSMandantenwaehrung());

			if (oEinzelverkaufspreisDto.getNVerkaufspreisbasis() != null) {
				bIstHinterlegt = true;
			}
		} catch (Throwable t) {
			MessageFormat mf = new MessageFormat(LPMain.getInstance()
					.getTextRespectUISPr(
							"lp.error.artikelhatkeineneinzelvkphinterlegt"));

			mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

			VkpfartikelpreislisteDto oVkpfartikelpreislisteDto = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.vkpfartikelpreislisteFindByPrimaryKey(
							kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste());

			Object pattern[] = { kundeDto.getPartnerDto().formatTitelAnrede(),
					oVkpfartikelpreislisteDto.getCNr() };

			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), mf.format(pattern));
		}

		return bIstHinterlegt;
	}

	/**
	 * Durch die Verkaufspresifindung Stufe 1 wurden die entsprechenden Werte
	 * berechnet. Diese werden jetzt zur Anzeige gebracht. <br>
	 * Die Anzeige erfolgt in Fremdwaehrung.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void verkaufspreisDto2components() throws Throwable {
		wnfEinzelpreis
				.setBigDecimal(verkaufspreisDtoInZielwaehrung.einzelpreis);
		wnfRabattsumme
				.setBigDecimal(verkaufspreisDtoInZielwaehrung.rabattsumme);

		getWnfRabattsatz().setDouble(verkaufspreisDtoInZielwaehrung.rabattsatz);
		getWnfZusatzrabattsatz().setDouble(
				verkaufspreisDtoInZielwaehrung.getDdZusatzrabattsatz());
		getWnfZusatzrabattsumme().setBigDecimal(
				verkaufspreisDtoInZielwaehrung.getNZusatzrabattsumme());

		if (wnfMaterialzuschlag != null
				&& verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag != null) {
			wnfMaterialzuschlag
					.setBigDecimal(verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag);
		}

		wnfNettopreis.setBigDecimal(verkaufspreisDtoInZielwaehrung.nettopreis);
		wnfMwstsumme.setBigDecimal(verkaufspreisDtoInZielwaehrung.mwstsumme);
		wnfBruttopreis
				.setBigDecimal(verkaufspreisDtoInZielwaehrung.bruttopreis);
		// die MwstsatzIId hier nicht ueberschreiben! WARUM NICHT??? Das ist
		// falsch ...

		// falls der Mwstsatz aus der ComboBox durch den Preisdialog
		// uebersteuert wurde
		wcoMwstsatz
				.setKeyOfSelectedItem(verkaufspreisDtoInZielwaehrung.mwstsatzIId);
	}

	/**
	 * Den gesamten Verkaufspreis wie er momentan im Panel angezeigt wird in ein
	 * Dto packen.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void components2VerkaufspreisDto() throws Throwable {
		verkaufspreisDtoInZielwaehrung.einzelpreis = wnfEinzelpreis
				.getBigDecimal();
		verkaufspreisDtoInZielwaehrung.rabattsumme = wnfRabattsumme
				.getBigDecimal();
		verkaufspreisDtoInZielwaehrung.nettopreis = wnfNettopreis
				.getBigDecimal();
		verkaufspreisDtoInZielwaehrung.mwstsumme = wnfMwstsumme.getBigDecimal();
		verkaufspreisDtoInZielwaehrung.bruttopreis = wnfBruttopreis
				.getBigDecimal();

		verkaufspreisDtoInZielwaehrung.rabattsatz = getWnfRabattsatz()
				.getDouble();
		verkaufspreisDtoInZielwaehrung.mwstsatzIId = (Integer) wcoMwstsatz
				.getKeyOfSelectedItem();
	}

	public void berechneVerkaufspreis(boolean bMitDialogPreisvorschlag)
			throws Throwable {
		berechneVerkaufspreis((Integer) wcoMwstsatz.getKeyOfSelectedItem(),
				ddWechselkurs, bMitDialogPreisvorschlag);
	}

	/**
	 * Den Verkaufspreis berechnen.
	 * 
	 * @param iIdMwstsatzI
	 *            PK des Mwstsatzes
	 * @param ddWechselkursI
	 *            Wechselkurs von Mandantenwaehrung zu Fremdwaehrung
	 * @throws Throwable
	 */
	public void berechneVerkaufspreis(Integer iIdMwstsatzI,
			Double ddWechselkursI, boolean bMitDialogPreisvorschlag)
			throws Throwable {

		// PJ 15845
		if (!DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF)) {
			bMitDialogPreisvorschlag = false;
		}

		nFixPreis = wnfNettopreis.getBigDecimal();
		// VKPF Stufe 1: Standardpreisliste des Kunden bestimmen, ist eine
		// Eigenschaft des Kunden und darf nicht null sein
		Integer kundeStandardpreisliste = kundeDto
				.getVkpfArtikelpreislisteIIdStdpreisliste();

		if (kundeStandardpreisliste == null) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_KUNDE_HAT_KEINE_STANDARDPREISLISTE_HINTERLEGT,
					new Exception("kundeStandardpreisliste == null"));
		}

		String vkpfWaehrung = DelegateFactory.getInstance()
				.getVkPreisfindungDelegate()
				.vkpfartikelpreislisteFindByPrimaryKey(kundeStandardpreisliste)
				.getWaehrungCNr();
		if (kundeDto.getCWaehrung().compareTo(vkpfWaehrung) != 0) {

			if (!vkpfWaehrung.equals(LPMain.getInstance().getTheClient()
					.getSMandantenwaehrung())) {

				throw new ExceptionLP(
						EJBExceptionLP.FEHLER_KUNDE_STANDARDPREISLISTE_HAT_FALSCHE_WAEHRUNG,
						new Exception(
								"kundeStandardpreisliste falsche Waehrung ("
										+ vkpfWaehrung + ")"));
			}
		}
		iIdPreislisteGewaehlt = kundeStandardpreisliste;

		// VKPF Stufe 2: die passende Mengenstaffel vorbelegen
		VkpfMengenstaffelDto[] aVkpfMengenstaffelDtos = // alle
														// Mengenstaffeln
		DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
						getArtikelDto().getIId(),
						datGueltigkeitsdatumArtikeleinzelverkaufspreis,
						getKundeDto()
								.getVkpfArtikelpreislisteIIdStdpreisliste());

		// die passende Mengenstaffel
		VkpfMengenstaffelDto vkpfMengenstaffelDtoPassend = DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
						getArtikelDto().getIId(),
						wnfMenge.getBigDecimal(),
						datGueltigkeitsdatumArtikeleinzelverkaufspreis,
						getKundeDto()
								.getVkpfArtikelpreislisteIIdStdpreisliste());
		// PJ 09/0013865

		if (vkpfMengenstaffelDtoPassend != null)
			if (vkpfMengenstaffelDtoPassend.getNArtikelfixpreis() != null) {
				wnfNettopreis.getWrbFixNumber().setSelected(true);
			}

		if (aVkpfMengenstaffelDtos != null && aVkpfMengenstaffelDtos.length > 0
				&& vkpfMengenstaffelDtoPassend != null) {
			for (int i = 0; i < aVkpfMengenstaffelDtos.length; i++) {
				if (aVkpfMengenstaffelDtos[i].getIId().intValue() == vkpfMengenstaffelDtoPassend
						.getIId().intValue()) {
					vkStaffelmengeIId = aVkpfMengenstaffelDtos[i].getIId();
				}
			}
		}

		// VKPF Stufe 3: Die passende Kundesokomengenstaffel vorbelegen
		KundesokomengenstaffelDto mengenstaffelDtoPassend = DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.ermittleKundesokomengenstaffel(getArtikelDto(),
						kundeDto.getIId(), wnfMenge.getBigDecimal(),
						datGueltigkeitsdatumArtikeleinzelverkaufspreis);

		if (mengenstaffelDtoPassend != null) {
			kundesokostaffelmengeIId = mengenstaffelDtoPassend.getIId();
		}

		vkpreisfindungDto = DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.verkaufspreisfindung(getArtikelDto().getIId(),
						kundeDto.getIId(), wnfMenge.getBigDecimal(),
						datGueltigkeitsdatumArtikeleinzelverkaufspreis,
						kundeStandardpreisliste, iIdMwstsatzI, getWaehrungCNr());

		try {
			verkaufspreisDtoInZielwaehrung = Helper
					.getVkpreisBerechnet(vkpreisfindungDto);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		if (verkaufspreisDtoInZielwaehrung == null) {
			// Message anzeigen

			// PJ 17580
			boolean bMeldungAnzeigen = true;
			if (getArtikelDto().getArtgruIId() != null
					&& getInternalFrame() instanceof InternalFrameLieferschein) {
				ArtgruDto agDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artgruFindByPrimaryKey(getArtikelDto().getArtgruIId());

				if (Helper.short2boolean(agDto.getBKeinevkwarnmeldungimls()) == true) {
					bMeldungAnzeigen = false;
				}

			}

			if (bMeldungAnzeigen == true) {

				if (!Helper.short2boolean(getArtikelDto().getBKalkulatorisch())) {
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("lp.error.artikelhatkeineneinzelvkphinterlegt"));

					mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

					VkpfartikelpreislisteDto oVkpfartikelpreislisteDto = DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.vkpfartikelpreislisteFindByPrimaryKey(
									iIdPreislisteGewaehlt);

					Object pattern[] = {
							kundeDto.getPartnerDto().formatTitelAnrede(),
							oVkpfartikelpreislisteDto.getCNr() };

					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.warning"), mf
							.format(pattern));
				}
			}

			// die Preise koennen auch per Hand eingegeben werden
			verkaufspreisDtoInZielwaehrung = new VerkaufspreisDto();

			// SP2952 Materialzuschlag gibt es jedoch trotzdem
			if (getArtikelDto().getMaterialIId() != null) {
				BigDecimal materialzuschlag = DelegateFactory
						.getInstance()
						.getMaterialDelegate()
						.getMaterialzuschlagVKInZielwaehrung(
								getArtikelDto().getIId(),
								datGueltigkeitsdatumArtikeleinzelverkaufspreis,
								getWaehrungCNr());
				if (materialzuschlag != null) {
					verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag = materialzuschlag;
					verkaufspreisDtoInZielwaehrung.nettopreis = materialzuschlag;
				}
			}

			wnfEinzelpreis.requestFocus();
		}

		if (nFixPreis.compareTo(verkaufspreisDtoInZielwaehrung.einzelpreis) != 0) {
			// VF PJ 08/0013026
			LockStateValue lv = this.getLockedstateDetailMainKey();
			if (lv.getIState() != LOCK_FOR_EMPTY
					&& bMitDialogPreisvorschlag == true)
				dialogPreisvorschlag();
		}
		// Preise in der Zielwaehrung anzeigen
		verkaufspreisDto2components();

		pruefeGueltigBisArtikellieferant();

	}

	private void pruefeGueltigBisArtikellieferant() throws Throwable {
		// PJ15259

		if (getArtikelDto() != null && getArtikelDto().getIId() != null
				&& wnfMenge.getBigDecimal() != null && getCNrWaehrung() != null) {

			ArtikellieferantDto alDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.getArtikelEinkaufspreis(getArtikelDto().getIId(),
							wnfMenge.getBigDecimal(), getCNrWaehrung());

			Timestamp tPruefDatum = getTBelegdatumMwstsatz();

			if (getInternalFrame() instanceof InternalFrameAngebot) {

				InternalFrameAngebot ifa = (InternalFrameAngebot) getInternalFrame();
				tPruefDatum = ifa.getTabbedPaneAngebot().getAngebotDto()
						.getTAngebotsgueltigkeitbis();

			}

			if (alDto != null && alDto.getTPreisgueltigbis() != null
					&& tPruefDatum != null
					&& alDto.getTPreisgueltigbis().before(tPruefDatum)) {

				wlaMinus.setText(LPMain.getInstance().getTextRespectUISPr(
						"lp.ekpreis.abgelaufen")
						+ "  "
						+ LPMain.getInstance().getTextRespectUISPr("lp.minus"));
				wlaMinus.setToolTipText(LPMain.getInstance()
						.getTextRespectUISPr("lp.ekpreis.abgelaufen.tooltip"));
				wlaMinus.setForeground(Color.RED);

			} else {
				wlaMinus.setText(LPMain.getInstance().getTextRespectUISPr(
						"lp.minus"));
				wlaMinus.setToolTipText("");
				wlaMinus.setForeground(Color.BLACK);
			}
		}
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
		if (kundeDto != null) {
			wifArtikelauswahl.setKundeIId(kundeDto.getIId());
		} else {
			wifArtikelauswahl.setKundeIId(null);
		}

	}

	public Double getWechselkurs() {
		return ddWechselkurs;
	}

	public void setWechselkurs(Double ddWechselkursI) {
		ddWechselkurs = ddWechselkursI;
	}

	public String getCNrWaehrung() {
		return cNrWaehrung;
	}

	public void setCNrWaehrung(String cNrWaehrungI) {
		this.cNrWaehrung = cNrWaehrungI;
	}

	public java.sql.Date getGueltigkeitsdatumArtikeleinzelverkaufspreis() {
		return datGueltigkeitsdatumArtikeleinzelverkaufspreis;
	}

	/**
	 * Hier den Wert fuer datGueltigkeitsdatumArtikeleinzelverkaufspreis
	 * hinterlegen.
	 * 
	 * @param datDatumI
	 *            das Gueltigkeitsdatum
	 */
	public void setGueltigkeitsdatumArtikeleinzelverkaufspreis(
			java.sql.Date datDatumI) {
		datGueltigkeitsdatumArtikeleinzelverkaufspreis = datDatumI;
	}

	public void artikelDto2components() throws Throwable {
		super.artikelDto2components();

		if (getBDefaultMwstsatzAusArtikel()) {
			// der Artikel bestimmt den Vorschlagswert im Mwstsatz
			// Auf den aktuellen MWST-Satz uebersetzen.

			MwstsatzDto mwstsatzDtoAktuell = null;

			KundeDto kdDtoFuerMwst = kundeDto;
			// PJ17455 Wenn Lieferschein, dann kommt MWST aus Lieferadresse
			if (getInternalFrame() instanceof InternalFrameLieferschein) {
				InternalFrameLieferschein ifL = (InternalFrameLieferschein) getInternalFrame();
				kdDtoFuerMwst = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								ifL.getTabbedPaneLieferschein()
										.getLieferscheinDto()
										.getKundeIIdLieferadresse());

			}

			if (kdDtoFuerMwst != null) {
				// Ausser der Kunde hat MWST-Satz mit 0%, dann muss dieser
				// verwendet werden
				MwstsatzDto mwstsatzDtoKunde = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								kdDtoFuerMwst.getMwstsatzbezIId());

				if (mwstsatzDtoKunde.getFMwstsatz().doubleValue() == 0) {
					mwstsatzDtoAktuell = mwstsatzDtoKunde;
				}

			}

			if (mwstsatzDtoAktuell == null) {
				mwstsatzDtoAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								getArtikelDto().getMwstsatzbezIId());
			}

			if (mwstsatzDtoAktuell != null) {
				iIdMwstsatzDefault = mwstsatzDtoAktuell.getIId();
				wcoMwstsatz.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
			} else
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
						.getTextRespectUISPr("lp.error.mwstsatzartikel"));
		}
		// VF PJ 08/0013026
		LockStateValue lv = this.getLockedstateDetailMainKey();
		if (lv.getIState() != LOCK_IS_LOCKED_BY_ME)
			berechneVerkaufspreis((Integer) wcoMwstsatz.getKeyOfSelectedItem(),
					ddWechselkurs, true);
	}

	public Integer getIIdPreislisteGewaehlt() {
		return iIdPreislisteGewaehlt;
	}

	public void setIIdPreislisteGewaehlt(Integer iIdPreislisteI) {
		iIdPreislisteGewaehlt = iIdPreislisteI;
	}

	public Integer getVkStaffelmengeIId() {
		return vkStaffelmengeIId;
	}

	public void setVkStaffelmengeIId(Integer vkStaffelmengeIIdI) {
		vkStaffelmengeIId = vkStaffelmengeIIdI;
	}

	public Integer getKundesokostaffelmengeIId() {
		return kundesokostaffelmengeIId;
	}

	public void setKundesokostaffelmengeIId(Integer kundesokostaffelmengeIIdI) {
		kundesokostaffelmengeIId = kundesokostaffelmengeIIdI;
	}

	public Integer getIIdLager() {
		return this.iIdLager;
	}

	public void setIIdLager(Integer iIdLagerI) {
		this.iIdLager = iIdLagerI;
	}

	/**
	 * Hinterlegen der Information, ob die Default MwstsatzIId manuell
	 * uebersteuert wurde.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected void bestimmeDefaultMwstsatzIIdUebersteuert() throws Throwable {
		if (iIdMwstsatzDefault != null
				&& wcoMwstsatz.getKeyOfSelectedItem() != null) {

			if (((Integer) wcoMwstsatz.getKeyOfSelectedItem()).intValue() != iIdMwstsatzDefault
					.intValue()) {
				bIstMwstsatzDefaultUebersteuert = true;

				// im PanelDialogPreisauswahl muss Fixpreis/Rabattsatz angezeigt
				// werden
				iIdPreislisteGewaehlt = null;

				components2VerkaufspreisDto();
			} else {
				bIstMwstsatzDefaultUebersteuert = false;
			}
		}
		// MB 16.10.06 IMS 2420
		else {
			iIdMwstsatzDefault = (Integer) wcoMwstsatz.getKeyOfSelectedItem();
			bIstMwstsatzDefaultUebersteuert = false;
		}
	}

	/**
	 * Feststellen, ob der Rabattsatz aus der Vkpf manuell uebersteuert wurde.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected void bestimmeRabattsatzAusVkpfUebersteuert() throws Throwable {
		if (verkaufspreisDtoInZielwaehrung != null) {

			if (getWnfRabattsatz().getDouble() != null
					&& verkaufspreisDtoInZielwaehrung.rabattsatz != null) {
				if (getWnfRabattsatz().getDouble().doubleValue() != verkaufspreisDtoInZielwaehrung.rabattsatz
						.doubleValue()) {
					bIstRabattsatzDefaultUebersteuert = true;

					// im PanelDialogPreisauswahl muss Fixpreis/Rabattsatz
					// angezeigt werden
					iIdPreislisteGewaehlt = null;

					components2VerkaufspreisDto();
				} else {
					bIstRabattsatzDefaultUebersteuert = false;
				}
			}
		}
	}

	/**
	 * Feststellen, ob der Nettoeinzelpreis aus der Vkpf manuell uebersteuert
	 * wurde.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected void bestimmeNettoeinzelpreisAusVkpfUebersteuert()
			throws Throwable {
		if (verkaufspreisDtoInZielwaehrung != null) {

			if (wnfEinzelpreis.getBigDecimal() != null
					&& verkaufspreisDtoInZielwaehrung.einzelpreis != null) {
				if (wnfEinzelpreis.getBigDecimal().compareTo(
						verkaufspreisDtoInZielwaehrung.einzelpreis) != 0) {
					bIstNettoeinzelpreisUebersteuert = true;

					// im PanelDialogPreisauswahl muss Fixpreis/Rabattsatz
					// angezeigt werden
					iIdPreislisteGewaehlt = null;

					components2VerkaufspreisDto();
				} else {
					bIstNettoeinzelpreisUebersteuert = false;
				}
			}
		}
	}

	/**
	 * Feststellen, ob der bestimmeNettogesamtpreis aus der Vkpf manuell
	 * uebersteuert wurde.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected void bestimmeNettogesamtpreisAusVkpfUebersteuert()
			throws Throwable {
		if (verkaufspreisDtoInZielwaehrung != null) {

			if (wnfNettopreis.getBigDecimal() != null
					&& verkaufspreisDtoInZielwaehrung.nettopreis != null) {
				if (wnfNettopreis.getBigDecimal().compareTo(
						verkaufspreisDtoInZielwaehrung.nettopreis) != 0) {
					bIstNettogesamtpreisUebersteuert = true;
					// im PanelDialogPreisauswahl muss Fixpreis/Rabattsatz
					// angezeigt werden
					iIdPreislisteGewaehlt = null;

					components2VerkaufspreisDto();
				} else {
					bIstNettogesamtpreisUebersteuert = false;
				}
			}
		}
	}

	/**
	 * Wenn dieses Feld verlassen wird, muessen die Preisfelder neu berechnet
	 * werden.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	public void wnfRabattsatz_focusLost(FocusEvent e) throws Throwable {
		super.wnfRabattsatz_focusLost(e);
		bestimmeRabattsatzAusVkpfUebersteuert();
	}

	/**
	 * Es wurde ein neuer Mwsatsatz gewaehlt.
	 * 
	 * @param e
	 *            enthaelt den neuen Mwstsatz
	 * @throws Throwable
	 */
	protected void jComboBoxMwstsatz_itemStateChanged(ItemEvent e)
			throws Throwable {
		super.jComboBoxMwstsatz_itemStateChanged(e);
		bestimmeDefaultMwstsatzIIdUebersteuert();
	}

	/**
	 * Wenn dieses Feld verlassen wird, muessen die Preisfelder neu berechnet
	 * werden.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	public void wnfEinzelpreis_focusLost(FocusEvent e) throws Throwable {
		super.wnfEinzelpreis_focusLost(e);
		bestimmeNettoeinzelpreisAusVkpfUebersteuert();
	}

	/**
	 * Nettopreis und Einzelpreis bestimmen den Rabattsatz.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	void wnfNettopreis_focusLost(FocusEvent e) throws Throwable {
		super.wnfNettopreis_focusLost(e);
		bestimmeNettogesamtpreisAusVkpfUebersteuert();
		pruefeNettoPreis();
	}

	/**
	 * Wenn eine Verkaufsposition abgespeichert werden soll, muss
	 * moeglicherweise der Nettogesamtpreis darauf ueberpruft werden, ob er
	 * unter dem StandardDB des Artikels liegt (StandardDB = Gestehungspreis +
	 * Aufschlag MindestDB). Ob diese Pruefung notwendig ist, bestimmt der
	 * Mandantenparameter DEFAULT_ANGEBOT_PRUEFESTANDARDDBARTIKEL.
	 * 
	 * @return boolean true, wenn der Parameter auf 1 steht
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public boolean pruefeStandardDBArtikel() throws Throwable {
		boolean bPruefeStandardDB = false;

		// Soll der StandardDB geprueft werden?
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ANGEBOT,
						ParameterFac.PARAMETER_DEFAULT_ANGEBOT_PRUEFESTANDARDDBARTIKEL);

		bPruefeStandardDB = ((Boolean) parametermandantDto.getCWertAsObject())
				.booleanValue();

		return bPruefeStandardDB;
	}

	public void pruefeNettoPreis() throws Throwable {

		int iPreiseUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseVK();
		BigDecimal nEinzelpreisMinusRabattsumme = verkaufspreisDtoInZielwaehrung.einzelpreis;
		nEinzelpreisMinusRabattsumme = Helper.rundeKaufmaennisch(
				nEinzelpreisMinusRabattsumme, iPreiseUINachkommastellen);

		if (verkaufspreisDtoInZielwaehrung.rabattsatz.doubleValue() != 0) {
			nEinzelpreisMinusRabattsumme = Helper.getWertPlusProzent(
					verkaufspreisDtoInZielwaehrung.einzelpreis, new BigDecimal(
							wnfRabattsatz.getDouble()),
					iPreiseUINachkommastellen);
		}
		boolean bUebersteuert = (nEinzelpreisMinusRabattsumme.compareTo(Helper
				.rundeKaufmaennisch(verkaufspreisDtoInZielwaehrung.nettopreis,
						iPreiseUINachkommastellen)) != 0);
		if (bUebersteuert) {
			wnfNettopreis.getWrbFixNumber().setSelected(true);
		} else {
			wnfRabattsumme.getWrbFixNumber().setSelected(true);
		}

		if (bVkpreiseingabeNurNetto == true) {
			wnfNettopreis.getWrbFixNumber().setSelected(true);
		}
	}

	public void focusLost(FocusEvent e) {
		super.focusLost(e);

		try {
			// focusLost am Ident-Feld, aber ich bleibe in HeliumV
			if (e.getSource() == wtfArtikel) {
				if (wtfArtikel.getText() != null) {
					wbuPreisauswahl.setEnabled(true);
				}
			} else if (e.getSource() == wnfMenge) {
				// eventuell wurde eine neue Staffelmenge erreicht
				if (wnfMenge.getText() != null
						&& wnfMenge.getText().trim().length() > 0) {
					if (getArtikelDto() != null
							&& getArtikelDto().getIId() != null) {
						berechneVerkaufspreis(
								(Integer) wcoMwstsatz.getKeyOfSelectedItem(),
								ddWechselkurs, true);
					} else {
						if (wtfArtikel.getText() != null) {
							ArtikelDto aDto = null;
							try {
								aDto = DelegateFactory.getInstance()
										.getArtikelDelegate()
										.artikelFindByCNr(wtfArtikel.getText());
							} catch (ExceptionLP ex) {
								switch (ex.getICode()) {
								case EJBExceptionLP.FEHLER_BEI_FIND: {
									// nothing here
								}
									break;
								default: {
									throw ex;
								}
								}
							}
							if (aDto != null) {
								// MB 09.05.06 Die Artikeldaten muessen
								// (duerfen) nur dann neu angezeigt werden ...
								boolean bRefreshArtikeldaten = false;
								// wenn a) vorher noch kein artikel ausgewaehlt
								// war
								if (getArtikelDto() == null) {
									bRefreshArtikeldaten = true;
								} else {
									// wenn b) ein anderer artikel ausgewaehlt
									// wurde
									if (getArtikelDto().getIId() == null
											|| !getArtikelDto().getIId()
													.equals(aDto.getIId())) {
										bRefreshArtikeldaten = true;
									}
								}
								setArtikelDto(aDto);
								if (bRefreshArtikeldaten) {
									artikelDto2components();
								}
								berechneVerkaufspreis(
										(Integer) wcoMwstsatz
												.getKeyOfSelectedItem(),
										ddWechselkurs, true);
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	/**
	 * Wenn die VK-Preisfindung nicht den guenstigsten moeglichen Preis
	 * ermittelt, erhaelt der Benutzer die Moeglichkeit, diesen trotzdem zu
	 * waehlen.
	 * 
	 * @param vkpreisfindungDtoI
	 *            das Ergebnis der Preisfindung
	 * @return boolean true, wenn der guenstigst moegliche Preis uebernommen
	 *         werden soll
	 * @throws Throwable
	 */
	public boolean showVkphint(VkpreisfindungDto vkpreisfindungDtoI)
			throws Throwable {
		boolean bVkpminimal = true;

		MessageFormat mf = new MessageFormat(LPMain.getInstance()
				.getTextRespectUISPr("vkpf.vkhint"));

		mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

		VerkaufspreisDto vkpBerechnetInZielwaehrungDto = Helper
				.getVkpreisBerechnet(vkpreisfindungDtoI);

		VerkaufspreisDto vkpMinimalInZielwaehrungDto = DelegateFactory
				.getInstance().getVkPreisfindungDelegate()
				.getVkpreisMinimal(vkpreisfindungDtoI);

		if (vkpMinimalInZielwaehrungDto.nettopreis != null) {
			vkpMinimalInZielwaehrungDto.nettopreis = Helper.rundeKaufmaennisch(
					vkpMinimalInZielwaehrungDto.nettopreis, Defaults
							.getInstance().getIUINachkommastellenPreiseVK());
		}
		if (vkpBerechnetInZielwaehrungDto.nettopreis != null) {
			vkpBerechnetInZielwaehrungDto.nettopreis = Helper
					.rundeKaufmaennisch(
							vkpBerechnetInZielwaehrungDto.nettopreis, Defaults
									.getInstance()
									.getIUINachkommastellenPreiseVK());
		}
		Object pattern[] = {
				vkpreisfindungDtoI.getVkpreisminimalStufe(),
				vkpreisfindungDtoI.getVkpreisberechnetStufe(),
				vkpMinimalInZielwaehrungDto.getNettpreisOhneMaterialzuschlag()
						+ " " + cNrWaehrung,
				vkpBerechnetInZielwaehrungDto
						.getNettpreisOhneMaterialzuschlag() + " " + cNrWaehrung };

		if (DialogFactory.showMeldung(mf.format(pattern), LPMain.getInstance()
				.getTextRespectUISPr("lp.hint"),
				javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {
			bVkpminimal = false;
		}

		return bVkpminimal;
	}

	/**
	 * Der Benutzer kann einen guenstigeren VK-Preis uebernehmen, als den, der
	 * durch die VK-Preisfindung ermittelt wurde.
	 * 
	 * @throws Throwable
	 */
	public void checkVkp() throws Throwable {
		if (vkpreisfindungDto.getVkpreisminimalStufe() != null) {
			VerkaufspreisDto vkpBerechnetDto = Helper
					.getVkpreisBerechnet(vkpreisfindungDto);

			VerkaufspreisDto vkpMinimalDto = DelegateFactory.getInstance()
					.getVkPreisfindungDelegate()
					.getVkpreisMinimal(vkpreisfindungDto);

			if (vkpBerechnetDto != null && vkpMinimalDto != null) {
				if (vkpBerechnetDto.getNettpreisOhneMaterialzuschlag()
						.doubleValue() > vkpMinimalDto
						.getNettpreisOhneMaterialzuschlag().doubleValue()) {
					boolean bVkpminimal = showVkphint(vkpreisfindungDto);

					if (bVkpminimal) {
						// @todo die letzte gewaehlte ... setzen PJ 5030
						setIIdPreislisteGewaehlt(null);
						setKundesokostaffelmengeIId(null);
						setVkStaffelmengeIId(null);

						VerkaufspreisDto vkpInMandantwaehrungDto = vkpMinimalDto;

						VerkaufspreisDto vkpInZielwaehrung = vkpInMandantwaehrungDto;

						setVerkaufspreisDtoInZielwaehrung(vkpInZielwaehrung);
						verkaufspreisDto2components();
					}
				}
			}
		}
	}

	protected void wnfMenge_inputVerifier() {
		// eventuell wurde eine neue Staffelmenge erreicht
		if (wnfMenge.getText() != null) {
			if (getArtikelDto() != null && getArtikelDto().getIId() != null) {
				try {
					berechneVerkaufspreis(
							(Integer) wcoMwstsatz.getKeyOfSelectedItem(),
							ddWechselkurs, true);
				} catch (Throwable e) {
				}
			}
		}
	}

	protected void wnfNettopreis_inputVerifier() {
		try {
			wnfNettopreis_focusLost(null);
		} catch (Throwable e) {
		}
	}

	public VkpreisfindungDto getVkpreisfindungDto() {
		return vkpreisfindungDto;
	}

	public void setVerkaufspreisDtoInZielwaehrung(
			VerkaufspreisDto verkaufspreisDtoInZielwaehrungI) {
		verkaufspreisDtoInZielwaehrung = verkaufspreisDtoInZielwaehrungI;
	}
}
