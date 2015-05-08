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
package com.lp.client.bestellung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCHNRField;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich WEPOS.</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>18.02.05</I></p>
 * 
 * @author josef erlinger
 * 
 * @version $Revision: 1.33 $
 */
public class PanelBestellungWEP extends PanelBasis {
	private static final long serialVersionUID = 1L;

	private final TabbedPaneBestellung tpBestellung;
	private BestellpositionDto bestellpositionDto = null;

	private JPanel jpaWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;

	private WrapperLabel wlaGelieferteMenge = new WrapperLabel();
	public WrapperNumberField wnfGelieferteMenge = null;

	private WrapperLabel wlaGewicht = new WrapperLabel();
	protected WrapperNumberField wnfGewichtbestellmenge = null;
	private WrapperLabel wlaGewichtEinheit = new WrapperLabel();
	private WrapperLabel wlaGewichtPreis = new WrapperLabel();
	protected WrapperNumberField wnfGewichtPreis = null;
	private WrapperLabel wlaGewichtWaehrung = null;

	private WrapperEditorField wefKommentar = null;

	private WrapperLabel wlaGelieferterPreis = new WrapperLabel();
	protected WrapperNumberField wnfGelieferterPreis = new WrapperNumberField();

	private WrapperLabel wlaGelieferterWert = new WrapperLabel();
	protected WrapperNumberField wnfGelieferterWert = new WrapperNumberField();

	private WrapperLabel wlaEinstandspreis = new WrapperLabel();
	private WrapperNumberField wnfEinstandspreis = new WrapperNumberField();

	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	private WrapperSnrChnrField wtfSeriennr = null;

	private WrapperLabel wlaNettoeinzelpreis = null;
	private WrapperNumberField wnfNettoeinzelpreis = null;

	private WrapperLabel wlaBSWaehrungBS1 = null;
	private WrapperLabel wlaBSWaehrungBS2 = null;
	private WrapperLabel wlaBSWaehrungBS3 = null;
	private WrapperLabel wlaBSWaehrungBS4 = null;
	private WrapperLabel wlaNettoeinzelpreisWaehrung = null;
	private WrapperLabel wlaWEPOSEinheit = null;
	private WrapperLabel wlaZuschlag = null;

	private WrapperLabel wlaArtikel = new WrapperLabel();
	private WrapperLabel wlaArtikelZBez2 = new WrapperLabel();
	private WrapperLabel wlaArtikelZbez = new WrapperLabel();
	private WrapperTextField wtfArtikel = new WrapperTextField();
	private WrapperTextField wtfArtikelZBez = new WrapperTextField();
	private WrapperTextField wtfArtikelZBez2 = new WrapperTextField();
	private WrapperNumberField wnfZuschlag = new WrapperNumberField();

	private WrapperIdentField wifArtikelauswahl = null;
	private WrapperLabel wlaLFBez = new WrapperLabel();
	private WrapperLabel wlaLFArtnr = new WrapperLabel();
	private WrapperTextField wtfLFBez = new WrapperTextField();
	private WrapperTextField wtfLFArtnr = new WrapperTextField();

	private WrapperButton wbuHandartikelAnlegen = new WrapperButton();

	private WrapperKeyValueField wkvKunde = new WrapperKeyValueField(Defaults
			.getInstance().bySizeFactor(40));
	private WrapperLabel wlaWareneingang = new WrapperLabel("");

	private WrapperLabel wlaFixkosten = null;
	private WrapperNumberField wnfFixkosten = null;

	private WrapperCheckBox wcbPreiseErfasst = null;

	private WrapperSelectField wsfHersteller = null;
	private WrapperSelectField wsfLand = null;

	static final private String ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN = "action_special_positionmanuellerledigen";
	static final private String ACTION_SPECIAL_ETIKETTDRUCKEN = "action_special_etikettdrucken";

	static final private String ACTION_SPECIAL_HANDARTIKEL_ERZEUGEN = "action_special_handartikel_erzeugen";

	private static final String ACTION_SPECIAL_EINZELPREIS_RUECKPFLEGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_EINZELPREIS_RUECKPFLEGEN";
	private static final String ACTION_SPECIAL_STAFFELPREIS_RUECKPFLEGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_STAFFELPREIS_RUECKPFLEGEN";

	private ArtikelDto artikelDto = null;
	private ArtikellieferantDto artikellieferantDto = null;

	public PanelBestellungWEP(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		tpBestellung = getInternalFrameBestellung().getTabbedPaneBestellung();
		jbInit();
		initPanel();
		initComponents();
	}

	private void initPanel() {
		// Wenn der Benutzer keine Preise sehen darf, sind einige Felder nicht
		// sichtbar.
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaGewichtPreis.setVisible(false);
			wnfGewichtPreis.setVisible(false);
			wlaGewichtWaehrung.setVisible(false);
			wlaGelieferterPreis.setVisible(false);
			wnfGelieferterPreis.setVisible(false);
			wlaGelieferterWert.setVisible(false);
			wnfGelieferterWert.setVisible(false);
			wlaBSWaehrungBS1.setVisible(false);
			wlaFixkosten.setVisible(false);
			wnfFixkosten.setVisible(false);
			wlaBSWaehrungBS2.setVisible(false);
			wlaBSWaehrungBS4.setVisible(false);
			wlaEinstandspreis.setVisible(false);
			wnfEinstandspreis.setVisible(false);
			wlaBSWaehrungBS3.setVisible(false);
			wlaNettoeinzelpreis.setVisible(false);
			wlaNettoeinzelpreisWaehrung.setVisible(false);
			wlaZuschlag.setVisible(false);
			wnfZuschlag.setVisible(false);
			wnfNettoeinzelpreis.setVisible(false);
			wcbPreiseErfasst.setVisible(false);
		}
	}

	private BestellpositionDto getBestellpositionDto() {
		return bestellpositionDto;
	}

	private void setBestellpositionDto(BestellpositionDto bestellpositionDto) {
		this.bestellpositionDto = bestellpositionDto;
	}

	protected void setDefaults() throws Throwable {
		leereAlleFelder(this);
		// nothing here
	}

	private InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		if (tpBestellung.getWareneingangDto().getEingangsrechnungIId() != null) {

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("bes.wep.er.vorhanden"));
			return;
		}

		if (!tpBestellung.istAktualisierenBestellungErlaubt()) {
			return;
		}

		super.eventActionNew(eventObject, true, false);

		setArtikelDto(null);
		tpBestellung
				.setWareneingangspositionenDto(new WareneingangspositionDto());
		tpBestellung.getWareneingangspositionenDto().setIId(null);
		wkvKunde.setValue("");
		leereAlleFelder(this);

	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);

		refreshMyComponents();

		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);

		if (tpBestellung.getWareneingangspositionenDto() == null) {
			wnfGelieferteMenge.setBigDecimal(null);
			wnfGelieferterPreis.setBigDecimal(null);
		}
	}

	protected void eventActionUpdate(ActionEvent e, boolean bNeedNoUpdateI)
			throws Throwable {
		boolean bUpdate = true;

		if (tpBestellung.getWareneingangDto().getEingangsrechnungIId() != null) {

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("bes.wep.er.vorhanden"));
			return;
		}

		// Artikelhinweise anzeigen
		// SK: von EventActionSave hierhin verschoben
		String[] hinweise = DelegateFactory
				.getInstance()
				.getArtikelkommentarDelegate()
				.getArtikelhinweise(artikelDto.getIId(),
						LocaleFac.BELEGART_WARENEINGANG);

		if (hinweise != null) {
			for (int i = 0; i < hinweise.length; i++) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hinweis"), Helper
						.strippHTML(hinweise[i]));
			}
		}

		// PJ 14648 Setartikel
		if (artikelDto != null && artikelDto.getIId() != null
				&& getBestellpositionDto().getPositioniIdArtikelset() != null) {

			// Es kann nur das Artikel-Set geaendert werden
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("ls.warning.setartikelnichtaenderbar"));
			return;
		}

		ArrayList<byte[]> bilder = DelegateFactory
				.getInstance()
				.getArtikelkommentarDelegate()
				.getArtikelhinweiseBild(artikelDto.getIId(),
						LocaleFac.BELEGART_WARENEINGANG);

		if (bilder != null && bilder.size() > 0) {
			DialogFactory.showArtikelHinweisBild(bilder, getInternalFrame());
		}

		if (getBestellpositionDto().getTManuellvollstaendiggeliefert() != null) {
			// die Position wurde manuell auf vollstaendig geliefert gesetzt
			boolean bAnswer = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("bes.frage.positionwurdemanuellaufgeliefertgesetzt"),
							LPMain.getTextRespectUISPr("lp.frage"));
			if (bAnswer) {
				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellpositionmanuellErledigungAufheben(
								getBestellpositionDto().getIId());
				getTPBes().getPanelBestellungWepQP5().eventYouAreSelected(true);
			} else {
				bUpdate = false;
			}
		}
		WareneingangspositionDto weposDto = tpBestellung
				.getWareneingangspositionenDto();
		if (weposDto.getIId() != null) {
			// die WEPOS existiert bereits
			wnfGelieferterPreis.setBigDecimal(weposDto.getNGelieferterpreis());
			// Die Fixkosten wurden schon mal eingegeben.
			wnfFixkosten.setBigDecimal(getBestellpositionDto()
					.getNFixkostengeliefert());
		} else {
			// Die erste Anlieferung dieser Bestellposition in diesem WE.
			// daher die geplanten Fixkosten vorbelegen.
			wnfFixkosten.setBigDecimal(getBestellpositionDto().getNFixkosten());
			// Den Preis aus der Bestellposition vorschlagen.
			wnfGelieferterPreis.setBigDecimal(getBestellpositionDto()
					.getNNettogesamtpreis());
			if (getBestellpositionDto().getNNettogesamtpreis() != null
					&& getBestellpositionDto().getNMenge() != null) {
				wnfGelieferterWert.setBigDecimal(getBestellpositionDto()
						.getNNettogesamtpreis().multiply(
								getBestellpositionDto().getNMenge()));
			}
			wcbPreiseErfasst.setSelected(false);

		}
		if (bUpdate) {
			super.eventActionUpdate(e, bNeedNoUpdateI);
		}

		if (weposDto.getIId() == null && getArtikelDto() != null) {
			wsfHersteller.setKey(getArtikelDto().getHerstellerIId());
			wsfLand.setKey(getArtikelDto().getLandIIdUrsprungsland());
		}
		wefKommentar.setActivatable(false);
		wifArtikelauswahl.getWbuArtikel().setEnabled(false);
		wbuHandartikelAnlegen.setEnabled(false);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		boolean bDelete = true;
		if (getBestellpositionDto().getTManuellvollstaendiggeliefert() != null) {
			// die Position wurde manuell auf vollstaendig geliefert gesetzt
			boolean bAnswer = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("bes.frage.positionwurdemanuellaufgeliefertgesetzt"),
							LPMain.getTextRespectUISPr("lp.frage"));
			if (bAnswer) {
				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellpositionmanuellErledigungAufheben(
								getBestellpositionDto().getIId());
				getTPBes().getPanelBestellungWepQP5().eventYouAreSelected(true);
			} else {
				bDelete = false;
			}
		}
		if (bDelete) {
			WareneingangspositionDto wepDto = tpBestellung
					.getWareneingangspositionenDto();

			if (wepDto != null) {
				if (!tpBestellung.getWareneingangDto().getIId()
						.equals(wepDto.getWareneingangIId())) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.warning"),
									LPMain.getTextRespectUISPr("bes.wepnichtloeschbar"));
					return;
				}

				if (tpBestellung.getBesDto().getStatusCNr()
						.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
						&& !(getBestellpositionDto()
								.getTManuellvollstaendiggeliefert() == null)) {

					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("bes.bestellunggeliefert"));

					String sStatus = null;
					sStatus = HelperBestellung
							.showBestellstatusWennVonGeliefertAufOffenOderBestaetigt(
									tpBestellung.getBesDto(),
									this.getInternalFrameBestellung());

					mf.setLocale(LPMain.getTheClient().getLocUi());

					Object pattern[] = { " " + sStatus };
					String sMsg = mf.format(pattern);

					boolean bAnswer = DialogFactory.showModalJaNeinDialog(
							getInternalFrame(), sMsg,
							LPMain.getTextRespectUISPr("lp.warning"));
					if (bAnswer) {
						DelegateFactory
								.getInstance()
								.getWareneingangDelegate()
								.removeWareneingangsposition(
										tpBestellung
												.getWareneingangspositionenDto());
						// hier Rahmenbes ruecksetzen wenn Abrufbes verandert
						setRahmenbestellungAufOffenIfAbrufbestellungChanged(tpBestellung
								.getBesDto());

						super.eventActionDelete(e, false, false);
					}
				} else if (tpBestellung.getBesDto().getStatusCNr()
						.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
						&& (tpBestellung.getBesDto().getTManuellGeliefert() != null)) {

					tpBestellung.istAktualisierenBestellungErlaubt();
				} else {
					DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.removeWareneingangsposition(
									tpBestellung
											.getWareneingangspositionenDto());
					super.eventActionDelete(e, false, false);
				}
			}
		}
		refreshMyComponents();
	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		boolean bHandled = false;
		if (exfc.getICode() == EJBExceptionLP.FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("bes.mengenreduzierung"));
			bHandled = true;
		} else if (exfc.getICode() == EJBExceptionLP.LAGER_SERIENNUMMER_SCHON_VORHANDEN) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("bes.seriennummer"));
			bHandled = true;
		} else if (exfc.getICode() == EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("bes.artikelseriennummerzumengeungleich"));
			bHandled = true;
		}
		return bHandled;
	}

	private void setRahmenbestellungAufOffenIfAbrufbestellungChanged(
			BestellungDto besDto) throws Throwable {
		// wenn es eine Abrufbestellung ist so wird die zugehoerige
		// Rahmenbestellung auch auf offen gesetzt
		if (besDto.getBestellungartCNr().equals(
				BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

			if (besDto.getIBestellungIIdRahmenbestellung() != null) {
				Integer rahmenId = besDto.getIBestellungIIdRahmenbestellung();

				// ueberschreiben des Dto mit RahmenDto
				besDto = DelegateFactory.getInstance().getBestellungDelegate()
						.bestellungFindByPrimaryKey(rahmenId);

				if (besDto != null
						&& besDto.getStatusCNr().equals(
								BestellungFac.BESTELLSTATUS_GELIEFERT)) {
					/**
					 * @todo so nicht
					 */
					besDto.setStatusCNr(BestellungFac.BESTELLSTATUS_ABGERUFEN);
					DelegateFactory.getInstance().getBestellungDelegate()
							.updateBestellung(besDto);
				}
			}
		}
	}

	protected void components2Dto() throws Throwable {
		WareneingangspositionDto wepDto = tpBestellung
				.getWareneingangspositionenDto();
		if (wepDto == null) {
			// neu anlegen
			wepDto = new WareneingangspositionDto();
		}
		wepDto.setBPreiseErfasst(wcbPreiseErfasst.isSelected());
		wepDto.setWareneingangIId(tpBestellung.getWareneingangDto().getIId());
		wepDto.setBestellpositionIId(getBestellpositionDto().getIId());
		wepDto.setNGeliefertemenge(wnfGelieferteMenge.getBigDecimal());
		wepDto.setNGelieferterpreis(wnfGelieferterPreis.getBigDecimal());
		wepDto.setNEinstandspreis(wnfEinstandspreis.getBigDecimal());
		wepDto.setNFixkosten4Bestellposition(wnfFixkosten.getBigDecimal());

		wepDto.setHerstellerIId(wsfHersteller.getIKey());
		wepDto.setLandIId(wsfLand.getIKey());

		// ueberpruefen ob artikel serien oder chargenbehaftet
		if (getArtikelDto() != null && getArtikelDto().getIId() != null) {
			artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(getArtikelDto().getIId());

			if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
					|| Helper.short2boolean(artikelDto.getBChargennrtragend())) {
				wepDto.setSeriennrChargennrMitMenge(wtfSeriennr
						.getSeriennummern());
			} else {
				wepDto.setSeriennrChargennrMitMenge(null);
			}
		}
	}

	protected void dto2Components() throws Throwable {
		// Lieferantenbezeichnung, falls vorhanden.
		
		
		artikellieferantDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.getArtikelEinkaufspreis(
						getArtikelDto().getIId(),
						getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto().getLieferantIIdBestelladresse(),BigDecimal.ONE,getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto().getWaehrungCNr(),getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto().getDBelegdatum());

		if (artikellieferantDto != null) {
			wtfLFArtnr.setText(artikellieferantDto.getCArtikelnrlieferant());
			wtfLFBez.setText(artikellieferantDto.getCBezbeilieferant());
		} else {
			wtfLFArtnr.setText(null);
			wtfLFBez.setText(null);
		}

		WareneingangspositionDto weposDto = tpBestellung
				.getWareneingangspositionenDto();
		aktualisiereFelderSnrChnr(true);
		if (weposDto.getBPreiseErfasst() != null) {
			wcbPreiseErfasst.setSelected(weposDto.getBPreiseErfasst());
		}
		wnfGelieferteMenge.setBigDecimal(weposDto.getNGeliefertemenge());

		if (bestellpositionDto.getKundeIId() != null) {
			KundeDto kundeDto = DelegateFactory.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(bestellpositionDto.getKundeIId());

			wkvKunde.setValue(kundeDto.getPartnerDto().formatAnrede());
		} else {
			wkvKunde.setValue("");
		}

		wnfGelieferterPreis.setBigDecimal(weposDto.getNGelieferterpreis());
		wnfFixkosten.setBigDecimal(bestellpositionDto.getNFixkostengeliefert());

		wsfHersteller.setKey(weposDto.getHerstellerIId());
		wsfLand.setKey(weposDto.getLandIId());

		/**
		 * @todo wieso nicht auf der bestelleinheit? -> umbessern
		 */
		if (getArtikelDto().getEinheitCNrBestellung() != null) {
			wnfGewichtbestellmenge.setVisible(true);
			wnfGewichtPreis.setVisible(true);
			wlaGewichtPreis.setVisible(true);
			wlaGewichtWaehrung.setVisible(true);
			wlaGewicht.setVisible(true);

			wefKommentar.setVisible(false);
			revalidate();

			if (Helper.short2boolean(getArtikelDto()
					.getbBestellmengeneinheitInvers())) {
				wnfGewichtbestellmenge.setBigDecimal(weposDto
						.getNGeliefertemenge().divide(
								getArtikelDto().getNUmrechnungsfaktor(),
								Defaults.getInstance()
										.getIUINachkommastellenPreiseEK(),
								BigDecimal.ROUND_HALF_EVEN));
			} else {
				wnfGewichtbestellmenge.setBigDecimal(weposDto
						.getNGeliefertemenge().multiply(
								getArtikelDto().getNUmrechnungsfaktor()));
			}

			wlaGewichtEinheit
					.setText(getArtikelDto().getEinheitCNrBestellung());
			if (wnfGelieferterPreis.getBigDecimal() != null
					&& getArtikelDto().getNUmrechnungsfaktor() != null) {
				BigDecimal nPreisPerEinheit = null;
				if (Helper.short2boolean(getArtikelDto()
						.getbBestellmengeneinheitInvers())) {
					nPreisPerEinheit = (wnfGelieferterPreis.getBigDecimal()
							.multiply(getArtikelDto().getNUmrechnungsfaktor()));
				} else {
					nPreisPerEinheit = (wnfGelieferterPreis.getBigDecimal()
							.divide(getArtikelDto().getNUmrechnungsfaktor(),
									Defaults.getInstance()
											.getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN));
				}

				wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
			}
		} else {
			wlaGewichtEinheit.setText(null);

			wnfGewichtbestellmenge.setVisible(false);
			wnfGewichtPreis.setVisible(false);
			wlaGewichtPreis.setVisible(false);
			wlaGewichtWaehrung.setVisible(false);
			wlaGewicht.setVisible(false);
			wefKommentar.setVisible(true);
			revalidate();

		}

		wnfEinstandspreis.setBigDecimal(weposDto.getNEinstandspreis());

		wlaBSWaehrungBS1.setText(tpBestellung.getBesDto().getWaehrungCNr());
		wlaBSWaehrungBS2.setText(tpBestellung.getBesDto().getWaehrungCNr());
		wlaBSWaehrungBS3.setText(tpBestellung.getBesDto().getWaehrungCNr());
		wlaBSWaehrungBS4.setText(tpBestellung.getBesDto().getWaehrungCNr());

		String sJeEinheit = "";
		if (getArtikelDto().getEinheitCNrBestellung() != null) {
			sJeEinheit = "/" + getArtikelDto().getEinheitCNrBestellung().trim();
		}

		wifArtikelauswahl.setArtikelDto(getArtikelDto());
		wlaGewichtWaehrung.setText(tpBestellung.getBesDto().getWaehrungCNr()
				+ sJeEinheit);

		wlaNettoeinzelpreisWaehrung.setText(tpBestellung.getBesDto()
				.getWaehrungCNr());

		EinheitDto einheitDto = DelegateFactory.getInstance()
				.getSystemDelegate()
				.einheitFindByPrimaryKey(getArtikelDto().getEinheitCNr());
		wlaWEPOSEinheit.setText(einheitDto.formatBez());
		wlaWEPOSEinheit.setHorizontalAlignment(SwingConstants.LEADING);

		wlaNettoeinzelpreis.setText(LPMain
				.getTextRespectUISPr("bes.nettopreis"));

		BigDecimal zuschlag = DelegateFactory
				.getInstance()
				.getMaterialDelegate()
				.getMaterialzuschlagEKInZielwaehrung(
						getArtikelDto().getIId(),
						tpBestellung.getBesDto()
								.getLieferantIIdBestelladresse(),
						new java.sql.Date(tpBestellung.getWareneingangDto()
								.getTLieferscheindatum().getTime()),
						tpBestellung.getBesDto().getWaehrungCNr());

		BigDecimal bdPreiMitAktuellemZusschlag = getBestellpositionDto()
				.getNNettogesamtpreis();

		if (bdPreiMitAktuellemZusschlag != null
				&& getBestellpositionDto().getNMaterialzuschlag() != null) {
			bdPreiMitAktuellemZusschlag = bdPreiMitAktuellemZusschlag
					.subtract(getBestellpositionDto().getNMaterialzuschlag());
			if (zuschlag != null) {
				bdPreiMitAktuellemZusschlag = bdPreiMitAktuellemZusschlag
						.add(zuschlag);
			}

		}

		wnfNettoeinzelpreis.setBigDecimal(bdPreiMitAktuellemZusschlag);

		wnfZuschlag.setBigDecimal(zuschlag);

		if (getBestellpositionDto().getNMaterialzuschlag() != null
				&& getBestellpositionDto().getNMaterialzuschlag().doubleValue() != 0) {
			wlaZuschlag.setVisible(true);
			wnfZuschlag.setVisible(true);
		} else {
			wlaZuschlag.setVisible(false);
			wnfZuschlag.setVisible(false);
		}

		BestellpositionDto besPosDto = getBestellpositionDto();

		// die Artikelbezeichnung kann uebersteuert sein
		if (besPosDto.getCBez() == null) {
			if (getArtikelDto().getArtikelsprDto() != null) {
				wtfBezeichnung.setText(getArtikelDto().getArtikelsprDto()
						.getCBez());
			}
		} else {
			wtfBezeichnung.setText(besPosDto.getCBez());
		}

		// die Artikelzusatzbezeichnung ist nie uebersteuert
		if (getArtikelDto().getArtikelsprDto() != null
				&& !Helper.short2Boolean(besPosDto
						.getBArtikelbezeichnunguebersteuert())) {
			wtfArtikelZBez.setText(getArtikelDto().getArtikelsprDto()
					.getCZbez());
		} else {
			wtfArtikelZBez.setText(besPosDto.getCZusatzbez());
		}

		wtfArtikel.setText(getArtikelDto().getCNr());
		if (getArtikelDto().getArtikelsprDto() != null) {
			wtfArtikelZBez2.setText(getArtikelDto().getArtikelsprDto()
					.getCZbez2());
		} else {
			wtfArtikelZBez2.setText(null);
		}

		setStatusbarSpalte5(LPMain
				.getTextRespectUISPr("bes.wareneingang.wertsumme")
				+ ": "
				+ tpBestellung.getWareneingangWertsumme(tpBestellung
						.getWareneingangDto()));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			if (wnfGelieferteMenge.getBigDecimal().compareTo(new BigDecimal(0)) <= 0) {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.hinweis"), LPMain
						.getTextRespectUISPr("bes.wemengeaufnullreduzieren"));
				return;
			}
			if (checkAndSetGelPreisDlg() == JOptionPane.YES_OPTION) {
				if (ueberliefernDlg()) {
					WareneingangspositionDto weposDto2BSPOSFound = null;
					// ueberprueft ob es schon eine WEPOS mit zugehoerigen BSPOS
					// gibt
					WareneingangspositionDto[] aWEPOSDto = DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.wareneingangspositionFindByWareneingangIId(
									tpBestellung.getWareneingangDto().getIId());

					BigDecimal mengeVorher = new BigDecimal(0);

					if (aWEPOSDto.length >= 1) {
						Integer iIdBSPOS = getBestellpositionDto().getIId();
						for (int i = 0; i < aWEPOSDto.length; i++) {
							if (aWEPOSDto[i].getBestellpositionIId().equals(
									iIdBSPOS)) {
								weposDto2BSPOSFound = aWEPOSDto[i];
								mengeVorher = weposDto2BSPOSFound
										.getNGeliefertemenge();
								break;
							} else {
								weposDto2BSPOSFound = null;
							}
						}
					}

					// PJ 16966

					Integer artikelIId = null;

					if (getArtikelDto() != null) {
						artikelIId = getArtikelDto().getIId();
					}
					boolean bZertifiziert = pruefeObZertifiziert(artikelIId,
							tpBestellung.getLieferantDto());

					if (bZertifiziert == false) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("part.lieferant.nichtzertifiziert"));

						return;

					}

					if (weposDto2BSPOSFound == null) {
						// WE der BS ist ungleich WEPOS

						components2Dto();

						if (tpBestellung.getWareneingangspositionenDto()
								.getIId() == null) {
							tpBestellung.getWareneingangspositionenDto()
									.setArtikelIIdFuerNeuAnlageAusWEP(
											artikelIId);
							// Create.
							Integer iId = DelegateFactory
									.getInstance()
									.getWareneingangDelegate()
									.createWareneingangsposition(
											tpBestellung
													.getWareneingangspositionenDto());

							// Nun neu setzen, falls die Best-Pos neu angelegt
							// wurde
							WareneingangspositionDto weposDto = DelegateFactory
									.getInstance().getWareneingangDelegate()
									.wareneingangspositionFindByPrimaryKey(iId);

							tpBestellung
									.setWareneingangspositionenDto(weposDto);

							BestellpositionDto besPosDto = DelegateFactory
									.getInstance()
									.getBestellungDelegate()
									.bestellpositionFindByPrimaryKey(
											weposDto.getBestellpositionIId());

							setBestellpositionDto(besPosDto);
							getTPBes().getPanelBestellungWepQP5()
									.setSelectedId(
											weposDto.getBestellpositionIId());
							// diesem panel den key setzen.
							setKeyWhenDetailPanel(weposDto
									.getBestellpositionIId());

						}
					} else {
						// Update WE der BS ist gleich WEPOS
						/**
						 * @todo
						 */
						tpBestellung
								.setWareneingangspositionenDto(weposDto2BSPOSFound);
						components2Dto();
						DelegateFactory
								.getInstance()
								.getWareneingangDelegate()
								.updateWareneingangsposition(
										tpBestellung
												.getWareneingangspositionenDto());
					}
					// Artikelhinweise anzeigen
					// SK: verschoben nach eventActionUpdate damit kommentar
					// beim editieren statt beim speichern kommt.
					/*
					 * String[] hinweise =
					 * DelegateFactory.getInstance().getArtikelkommentarDelegate
					 * ().getArtikelhinweise(artikelDto.getIId(),
					 * LocaleFac.BELEGART_WARENEINGANG); if(hinweise != null){
					 * for (int i = 0; i < hinweise.length; i++) {
					 * DialogFactory.
					 * showModalDialog(LPMain.getInstance().getTextRespectUISPr(
					 * "lp.hinweis"), Helper.strippHTML(hinweise[i])); } }
					 */

					// SK Er Hinweise beim speichern 14536
					if (tpBestellung.getWareneingangspositionenDto()
							.getBPreiseErfasst()) {
						tpBestellung.zeigeArtikelhinweiseEingangsrechnung(
								new Integer[] { artikelDto.getIId() }, false);
					}

					ParametermandantDto parameterWEP = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_WEP_PREIS_ZURUECKSCHREIBEN,
									ParameterFac.KATEGORIE_BESTELLUNG,
									LPMain.getTheClient().getMandant());
					int iPreisRueckpflegen = ((Integer) parameterWEP
							.getCWertAsObject());
					if (iPreisRueckpflegen == 1
							&& getBestellpositionDto() != null
							&& getBestellpositionDto().getIId() != null) {
						preisInArtikellieferantRueckpflegen(true,
								getBestellpositionDto().getIId(), false);
					}

					super.eventActionSave(e, false);

					eventYouAreSelected(false);

					if (mengeVorher.doubleValue() != wnfGelieferteMenge
							.getBigDecimal().doubleValue()) {
						if (wnfGelieferteMenge.getBigDecimal()
								.subtract(mengeVorher).doubleValue() > 0) {

							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
									.getInstance()
									.getParameterDelegate()
									.getParametermandant(
											ParameterFac.PARAMETER_FEHLMENGEN_LOSBEZOGEN_SOFORT_AUFLOESEN,
											ParameterFac.KATEGORIE_FERTIGUNG,
											LPMain.getTheClient().getMandant());
							boolean bFehlmengenLosbezogenSofortAufloesen = ((Boolean) parameter
									.getCWertAsObject());

							if (bFehlmengenLosbezogenSofortAufloesen == true
									&& getBestellpositionDto() != null
									&& getBestellpositionDto()
											.getLossollmaterialIId() != null) {

								FehlmengenAufloesen
										.fehlmengenAufloesenMitLosBezug(
												getInternalFrame(),
												getArtikelDto().getIId(),
												getTPBes().getWareneingangDto()
														.getLagerIId(),
												wtfSeriennr.getSeriennummern(),
												wnfGelieferteMenge
														.getBigDecimal()
														.subtract(mengeVorher),
												getBestellpositionDto()
														.getLossollmaterialIId());
							} else {
								FehlmengenAufloesen.fehlmengenAufloesen(
										getInternalFrame(), getArtikelDto()
												.getIId(), getTPBes()
												.getWareneingangDto()
												.getLagerIId(), wtfSeriennr
												.getSeriennummern(),
										wnfGelieferteMenge.getBigDecimal()
												.subtract(mengeVorher));
							}

						}
					}
				}
			}
			// hier ueberpruefen ob alle Abrufbestellungen auf geliefert sind,
			// wenn ja, Rahmenbestellung auf geliefert setzen
			setRahmenbestellungAufGeliefertWennAlleAbrufbestellungenAufGeliefert();
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		Integer besposIId = (Integer) getTPBes().getPanelBestellungWepQP5()
				.getSelectedId();
		if (besposIId != null) {
			if (e.getActionCommand().equals(
					ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN)) {
				// Vollstaendig Geliefert-Status setzen
				if (getBestellpositionDto().getTManuellvollstaendiggeliefert() != null) {
					// die Position wurde manuell auf vollstaendig geliefert
					// gesetzt
					boolean bAnswer = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("bes.menu.bearbeiten.erledigungaufheben")
											+ " ?", LPMain
											.getTextRespectUISPr("lp.frage"));
					if (bAnswer) {
						DelegateFactory
								.getInstance()
								.getBestellungDelegate()
								.bestellpositionmanuellErledigungAufheben(
										besposIId);
						getTPBes().getPanelBestellungWepQP5()
								.eventYouAreSelected(true);
					}
				} else {
					// Wenn sie nicht eh schon geliefert oder erledigt ist
					if (!getBestellpositionDto()
							.getBestellpositionstatusCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT)
							&& !getBestellpositionDto()
									.getBestellpositionstatusCNr()
									.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
						boolean bAnswer = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("bes.frage.positionmanuellaufgeliefertsetzen"),
										LPMain.getTextRespectUISPr("lp.frage"));
						if (bAnswer) {
							DelegateFactory
									.getInstance()
									.getBestellungDelegate()
									.bestellpositionManuellAufVollstaendigGeliefertSetzen(
											besposIId);
							getTPBes().getPanelBestellungWepQP5()
									.eventYouAreSelected(true);
						}
					}
				}
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_ETIKETTDRUCKEN)) {
				ReportWepEtikett2 reportEtikett = new ReportWepEtikett2(
						getInternalFrame(), tpBestellung.getWareneingangDto(),
						tpBestellung.getWareneingangspositionenDto(),
						getBestellpositionDto(), getArtikelDto(),
						getArtikellieferantDto(), getArtikelDto()
								.formatArtikelbezeichnung());
				reportEtikett.eventYouAreSelected(false);
				reportEtikett.setKeyWhenDetailPanel(tpBestellung
						.getWareneingangspositionenDto().getIId());
				getInternalFrame().showReportKriterien(reportEtikett, false);
				reportEtikett.updateButtons();
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_EINZELPREIS_RUECKPFLEGEN)) {

				preisInArtikellieferantRueckpflegen(true, besposIId, true);
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_STAFFELPREIS_RUECKPFLEGEN)) {

				preisInArtikellieferantRueckpflegen(false, besposIId, true);
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_HANDARTIKEL_ERZEUGEN)) {

				// Handartikel anlegen

				DialogHandartikelAnlegen d = new DialogHandartikelAnlegen(this);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

				Integer artikelIIdHandeingabe = d.artikelIId;
				if (artikelIIdHandeingabe != null) {
					befuelleArtikel(artikelIIdHandeingabe);
				}

			}
		}
	}

	private void preisInArtikellieferantRueckpflegen(boolean bEinzelpreis,
			Integer besposIId, boolean bNullPreiseZurueckpflegen)
			throws ExceptionLP, Throwable {
		if (wnfGelieferterPreis.getBigDecimal() != null) {
			BestellpositionDto besPosDto = DelegateFactory.getInstance()
					.getBestellungDelegate()
					.bestellpositionFindByPrimaryKey(besposIId);

			besPosDto.setNNettogesamtpreis(wnfGelieferterPreis.getBigDecimal());
			
			//SP3382
			if(besPosDto.getNNettoeinzelpreis()!=null && besPosDto.getNNettoeinzelpreis().doubleValue() == 0){
				besPosDto.setNNettoeinzelpreis(wnfGelieferterPreis.getBigDecimal());
			}
			

			// Rabatt berechnen

			// Rabatt berechnen
			BigDecimal rabattsatz = null;
			if (wnfGelieferterPreis.getBigDecimal().doubleValue() != 0) {

				if (besPosDto.getNNettoeinzelpreis() == null
						|| besPosDto.getNNettoeinzelpreis().doubleValue() == 0) {
					rabattsatz = new BigDecimal(0);
				} else {
					rabattsatz = new BigDecimal(1).subtract(wnfGelieferterPreis
							.getBigDecimal().divide(
									besPosDto.getNNettoeinzelpreis(), 4,
									BigDecimal.ROUND_HALF_EVEN));
					rabattsatz = rabattsatz.multiply(new BigDecimal(100));
				}

			} else {
				rabattsatz = new BigDecimal(100);
			}

			besPosDto.setDRabattsatz(rabattsatz.doubleValue());

			besPosDto.setNFixkosten(wnfFixkosten.getBigDecimal());

			if (bEinzelpreis) {

				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.preispflege(
								besPosDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_EINZELPREIS_RUECKPFLEGEN,
								null, bNullPreiseZurueckpflegen);
			} else {

				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.preispflege(
								besPosDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_STAFFELPREIS_RUECKPFLEGEN,
								null, bNullPreiseZurueckpflegen);
			}

		}
	}

	private void setRahmenbestellungAufGeliefertWennAlleAbrufbestellungenAufGeliefert()
			throws Throwable {
		/**
		 * @todo gehoert das nicht auf den server?
		 */
		if (tpBestellung.getBesDto().getBestellungartCNr()
				.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

			BestellungDto besDto[] = null;
			besDto = DelegateFactory
					.getInstance()
					.getBestellungDelegate()
					.abrufBestellungenfindByRahmenbestellung(
							tpBestellung.getBesDto()
									.getIBestellungIIdRahmenbestellung());

			int countBestellungenAufGeliefert = 0;
			for (int i = 0; i < besDto.length; i++) {
				if (besDto[i].getStatusCNr().equals(
						BestellungFac.BESTELLSTATUS_GELIEFERT)) {
					countBestellungenAufGeliefert++;
				}
			}
			if (countBestellungenAufGeliefert == besDto.length) {
				BestellungDto rahmenBesDto = new BestellungDto();
				rahmenBesDto = DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellungFindByPrimaryKey(
								tpBestellung.getBesDto()
										.getIBestellungIIdRahmenbestellung());

				if (rahmenBesDto.getStatusCNr().equals(
						BestellungFac.BESTELLSTATUS_ABGERUFEN)) {
					rahmenBesDto
							.setStatusCNr(BestellungFac.BESTELLSTATUS_GELIEFERT);
				}
				DelegateFactory.getInstance().getBestellungDelegate()
						.updateBestellung(rahmenBesDto);
			}
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == wifArtikelauswahl.getPanelQueryFLRArtikel()) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				befuelleArtikel(key);

			}
		}
	}

	private void befuelleArtikel(Object key) throws ExceptionLP, Throwable {

		artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey((Integer) key);
		setArtikelDto(artikelDto);

		if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
				|| Helper.short2boolean(artikelDto.getBChargennrtragend())) {
			tpBestellung.getWareneingangspositionenDto()
					.setSeriennrChargennrMitMenge(
							wtfSeriennr.getSeriennummern());
		} else {
			tpBestellung.getWareneingangspositionenDto()
					.setSeriennrChargennrMitMenge(null);

		}
		wtfArtikel.setText(getArtikelDto().getCNr());
		if (getArtikelDto().getArtikelsprDto() != null) {
			wtfArtikelZBez.setText(getArtikelDto().getArtikelsprDto()
					.getCZbez());
			wtfArtikelZBez2.setText(getArtikelDto().getArtikelsprDto()
					.getCZbez2());
			wtfBezeichnung
					.setText(getArtikelDto().getArtikelsprDto().getCBez());

		} else {
			wtfArtikelZBez.setText(null);
			wtfArtikelZBez2.setText(null);
			wtfBezeichnung.setText(null);
		}

		aktualisiereFelderSnrChnr(true);
		setzeSichtbarkeitSnrChnr();

		if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
				|| Helper.short2boolean(artikelDto.getBChargennrtragend())) {
			wtfSeriennr.getButtonSnrAuswahl().setEnabled(true);
		} else {
			wnfGelieferteMenge.setEditable(true);
		}
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		this.createAndSaveAndShowButton(
				"/com/lp/client/res/box_preferences.png",
				LPMain.getTextRespectUISPr("bes.tooltip.manuellerledigen"),
				ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN,
				RechteFac.RECHT_BES_BESTELLUNG_CUD);

		this.createAndSaveAndShowButton(
				"/com/lp/client/res/printer216x16.png",
				LPMain.getTextRespectUISPr("artikel.report.etikett.shortcut"),
				ACTION_SPECIAL_ETIKETTDRUCKEN,
				KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK
						| InputEvent.SHIFT_DOWN_MASK),
				RechteFac.RECHT_BES_BESTELLUNG_CUD);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		wkvKunde.setKey(LPMain.getInstance().getTextRespectUISPr("label.kunde"));

		wlaGelieferteMenge.setText(LPMain
				.getTextRespectUISPr("bes.geliefertemenge"));
		wnfGelieferteMenge = new WrapperNumberField();
		wnfGelieferteMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfGelieferteMenge.setMandatoryField(true);
		wnfGelieferteMenge.addFocusListener(new WnfGelieferteMengeFocusAdapter(
				this));
		wnfGelieferteMenge.setMinimumValue(0);

		wlaGewicht.setText(LPMain.getTextRespectUISPr("lp.gewicht"));
		wlaGewichtEinheit.setHorizontalAlignment(SwingConstants.LEADING);
		wnfGewichtbestellmenge = new WrapperNumberField();
		wnfGewichtbestellmenge.addFocusListener(new FocusAdapterGewicht(this));

		wefKommentar = new WrapperEditorField(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.kommentar"), true);
		wefKommentar.setActivatable(false);

		wefKommentar.setVisible(false);

		wlaGewichtPreis = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.preis"));
		wnfGewichtPreis = new WrapperNumberField();
		wnfGewichtPreis.addFocusListener(new FocusAdapterGewichtPreis(this));
		wnfGewichtPreis.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wlaGewichtWaehrung = new WrapperLabel();
		wlaGewichtWaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wlaZuschlag = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.materialzuschlag"));

		wlaArtikel.setText(LPMain.getTextRespectUISPr("artikel.artikelnummer"));
		wlaArtikelZBez2.setText(LPMain
				.getTextRespectUISPr("lp.zusatzbezeichnung") + " 2");
		wlaArtikelZbez.setText(LPMain
				.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wtfArtikel.setActivatable(false);
		wtfArtikel.setMandatoryField(true);
		wifArtikelauswahl = new WrapperIdentField(getInternalFrame(), this);
		// wifArtikelauswahl.getWbuArtikel().setActivatable(false);
		wifArtikelauswahl.setBelegartCNr(LocaleFac.BELEGART_WARENEINGANG);

		wtfArtikelZBez.setActivatable(false);
		wtfArtikelZBez2.setActivatable(false);
		wlaGelieferterPreis.setText(LPMain
				.getTextRespectUISPr("bes.gelieferterpreis"));
		wnfGelieferterPreis.setMandatoryField(true);
		wnfGelieferterPreis.setEditable(true);
		wnfGelieferterPreis.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfGelieferterPreis
				.addFocusListener(new WnfGelieferterPreisFocusAdapter(this));
		wnfGelieferterPreis.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());

		wlaGelieferterWert.setText(LPMain
				.getTextRespectUISPr("bes.gelieferterwert"));
		wnfGelieferterWert.setMandatoryField(true);
		wnfGelieferterWert.setEditable(true);

		wnfGelieferterWert.addFocusListener(new WnfGelieferterWertFocusAdapter(
				this));

		// SP2712 Wert=Positionssumme hat lt. WH immer 2 Nachkommastellen
		wnfGelieferterWert.setFractionDigits(2);

		wlaEinstandspreis.setText(LPMain
				.getTextRespectUISPr("bes.einstandspreis"));
		wlaLFArtnr.setText(LPMain.getTextRespectUISPr("bes.lfartikelnummer"));
		wlaLFBez.setText(LPMain.getTextRespectUISPr("bes.lfbezeichnung"));

		wnfEinstandspreis.setActivatable(false);
		wnfEinstandspreis.setMandatoryField(false);
		wnfEinstandspreis.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setActivatable(false);
		wtfLFArtnr.setActivatable(false);
		wtfLFBez.setActivatable(false);

		wtfBezeichnung.setMandatoryField(false);

		wtfSeriennr = new WrapperSnrChnrField(getInternalFrame(), true);
		wtfSeriennr.setActivatable(false);

		wtfSeriennr.setWnfBelegMenge(wnfGelieferteMenge);

		wlaNettoeinzelpreis = new WrapperLabel();
		wlaBSWaehrungBS1 = new WrapperLabel();
		wlaBSWaehrungBS2 = new WrapperLabel();
		wlaBSWaehrungBS3 = new WrapperLabel();
		wlaBSWaehrungBS4 = new WrapperLabel();
		wlaNettoeinzelpreisWaehrung = new WrapperLabel();
		wlaWEPOSEinheit = new WrapperLabel();

		wlaNettoeinzelpreisWaehrung
				.setHorizontalAlignment(SwingConstants.LEADING);

		wnfNettoeinzelpreis = new WrapperNumberField();
		wnfNettoeinzelpreis.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfNettoeinzelpreis.setActivatable(false);

		wnfZuschlag.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfZuschlag.setActivatable(false);

		wlaBSWaehrungBS1.setHorizontalAlignment(SwingConstants.LEADING);
		wlaBSWaehrungBS2.setHorizontalAlignment(SwingConstants.LEADING);
		wlaBSWaehrungBS3.setHorizontalAlignment(SwingConstants.LEADING);
		wlaBSWaehrungBS4.setHorizontalAlignment(SwingConstants.LEADING);

		wlaFixkosten = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.fixkosten"));
		wnfFixkosten = new WrapperNumberField();
		wnfFixkosten.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());

		wcbPreiseErfasst = new WrapperCheckBox();
		wcbPreiseErfasst.setText(LPMain
				.getTextRespectUISPr("bes.preiseerfasst"));

		wifArtikelauswahl.getWbuArtikel().setMinimumSize(
				new Dimension(120, Defaults.getInstance().getControlHeight()));
		wifArtikelauswahl.getWbuArtikel().setPreferredSize(
				new Dimension(120, Defaults.getInstance().getControlHeight()));

		wlaLFArtnr.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaLFArtnr.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));

		wlaWEPOSEinheit.setMinimumSize(new Dimension(70, Defaults.getInstance()
				.getControlHeight()));
		wlaWEPOSEinheit.setPreferredSize(new Dimension(70, Defaults
				.getInstance().getControlHeight()));

		wlaBSWaehrungBS1.setMinimumSize(new Dimension(30, Defaults
				.getInstance().getControlHeight()));
		wlaBSWaehrungBS1.setPreferredSize(new Dimension(30, Defaults
				.getInstance().getControlHeight()));

		wsfHersteller = new WrapperSelectField(WrapperSelectField.HERSTELLER,
				getInternalFrame(), true);
		wsfLand = new WrapperSelectField(WrapperSelectField.LAND,
				getInternalFrame(), true);
		wsfLand.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.ursprungsland")
				+ "...");

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LAGERBEWEGUNG_MIT_URSPRUNG,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		boolean bHerstellerUrsprungsland = ((Boolean) parameter
				.getCWertAsObject());

		jpaWorkingOn = new JPanel(new GridBagLayout());
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		if (!getInternalFrame().bRechtDarfPreiseAendernEinkauf) {
			wnfGelieferterPreis.setActivatable(false);
			wnfGelieferterWert.setActivatable(false);
			wnfFixkosten.setActivatable(false);
			wcbPreiseErfasst.setActivatable(false);
		}

		wbuHandartikelAnlegen.setIcon(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/notebook.png")));
		wbuHandartikelAnlegen
				.setActionCommand(ACTION_SPECIAL_HANDARTIKEL_ERZEUGEN);
		wbuHandartikelAnlegen.addActionListener(this);

		iZeile++;
		/*
		 * jpaWorkingOn.add(wlaArtikel, new GridBagConstraints(0, iZeile, 1, 1,
		 * 0.0, 0.0 , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
		 * new Insets(2, 2, 2, 2), 0, 0));
		 */

		jpaWorkingOn.add(wifArtikelauswahl.getWbuArtikel(),
				new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2),
						0, 0));

		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, iZeile, 2, 1,
				1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 25), 0, 0));

		jpaWorkingOn.add(wbuHandartikelAnlegen, new GridBagConstraints(2,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(1, 30, 1, 2), 10, 0));

		if (bHerstellerUrsprungsland == true) {

			jpaWorkingOn.add(wsfHersteller.getWrapperButton(),
					new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(1, 2, 1, 2), 0, 0));
			jpaWorkingOn.add(wsfHersteller.getWrapperTextField(),
					new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(1, 2, 1, 2), 0, 0));
		}

		iZeile++;

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wlaLFArtnr, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wtfLFArtnr, new GridBagConstraints(4, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaArtikelZbez, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wtfArtikelZBez, new GridBagConstraints(1, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wlaLFBez, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wtfLFBez, new GridBagConstraints(4, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaArtikelZBez2, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wtfArtikelZBez2, new GridBagConstraints(1, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wcbPreiseErfasst, new GridBagConstraints(4, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaZuschlag, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfZuschlag, new GridBagConstraints(4, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGelieferteMenge, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfGelieferteMenge, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 160, 0));
		jpaWorkingOn.add(wlaWEPOSEinheit, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wlaGelieferterPreis, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfGelieferterPreis, new GridBagConstraints(4, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 160, 0));
		jpaWorkingOn.add(wlaBSWaehrungBS2, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGelieferterWert, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfGelieferterWert, new GridBagConstraints(4, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 160, 0));
		jpaWorkingOn.add(wlaBSWaehrungBS4, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfGewichtbestellmenge, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wlaGewichtEinheit, new GridBagConstraints(2, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wlaFixkosten, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfFixkosten, new GridBagConstraints(4, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wlaBSWaehrungBS3, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));

		jpaWorkingOn.add(wefKommentar, new GridBagConstraints(0, iZeile, 3, 3,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaGewichtPreis, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfGewichtPreis, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wlaGewichtWaehrung, new GridBagConstraints(2, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wlaEinstandspreis, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfEinstandspreis, new GridBagConstraints(4, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wlaBSWaehrungBS1, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaNettoeinzelpreis, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wnfNettoeinzelpreis, new GridBagConstraints(4, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wlaNettoeinzelpreisWaehrung, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		if (bHerstellerUrsprungsland == true) {

			jpaWorkingOn.add(wsfLand.getWrapperButton(),
					new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(1, 2, 1, 2), 0, 0));
			jpaWorkingOn.add(wsfLand.getWrapperTextField(),
					new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(1, 2, 1, 2), 0, 0));

		}

		createAndSaveAndShowButton("/com/lp/client/res/cube_green_add.png",
				LPMain.getTextRespectUISPr("bes.einzelpreis.rueckpflegen"),
				ACTION_SPECIAL_EINZELPREIS_RUECKPFLEGEN,
				RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);

		createAndSaveAndShowButton("/com/lp/client/res/cube_green.png",
				LPMain.getTextRespectUISPr("bes.staffelpreis.rueckpflegen"),
				ACTION_SPECIAL_STAFFELPREIS_RUECKPFLEGEN,
				RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);

		wlaWareneingang.setMinimumSize(new Dimension(400, 23));
		wlaWareneingang.setPreferredSize(new Dimension(400, 23));
		getToolBar().getToolsPanelRight().add(wlaWareneingang);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_KUNDE_JE_BESTELLPOSITION)) {

			wkvKunde.setMinimumSize(new Dimension(228, 23));
			wkvKunde.setPreferredSize(new Dimension(228, 23));

			getToolBar().getToolsPanelRight().add(wkvKunde);
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		setDefaults();
		// da steht NICHT die WEPOS drin sondern die BSPOS
		Object oBSPOS = getKeyWhenDetailPanel();
		getTPBes().getPanelBestellungWepQP5().setUebersteuerteId(null);
		// Assert
		if (oBSPOS == null
				|| (oBSPOS != null && oBSPOS.equals(LPMain.getLockMeForNew()))) {
			// SK auskommentiert, da durch FIllter null sein darf
			// throw new Exception("eventYouAreSelected no new!");
			setBestellpositionDto(new BestellpositionDto());
		} else {

			// BSPOS holen
			setBestellpositionDto(DelegateFactory.getInstance()
					.getBestellungDelegate()
					.bestellpositionFindByPrimaryKey((Integer) oBSPOS));
			if (getBestellpositionDto().getArtikelIId() == null) {
				return;
			}

			// ArtikelDto holen
			setArtikelDto(DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							getBestellpositionDto().getArtikelIId()));

			// die WEPOS der BSPOS suchen
			// alle WPPOS mit allen WEs von der BSPOS holen
			WareneingangspositionDto[] aWEPOSDto = DelegateFactory
					.getInstance()
					.getWareneingangDelegate()
					.wareneingangspositionFindByBestellpositionIId(
							getBestellpositionDto().getIId());

			if (aWEPOSDto.length > 0) {
				Integer iIdWE = tpBestellung.getWareneingangDto().getIId();

				int xWEPOSZuBSPOSFound = -1;
				for (int xWEPOSDto = 0; xWEPOSDto < aWEPOSDto.length; xWEPOSDto++) {
					if ((aWEPOSDto[xWEPOSDto].getWareneingangIId())
							.equals(iIdWE)) {
						xWEPOSZuBSPOSFound = xWEPOSDto;
						getTPBes().getPanelBestellungWepQP5()
								.setUebersteuerteId(
										aWEPOSDto[xWEPOSDto].getIId());
						break;
					}
				}
				if (xWEPOSZuBSPOSFound > -1) {
					// WEPOS zu BSPOS gefunden
					tpBestellung
							.setWareneingangspositionenDto(aWEPOSDto[xWEPOSZuBSPOSFound]);

				} else {
					// WEPOS zu BSPOS NICHT gefunden
					setForNew();
				}
			} else {
				// keine WEPOS zur BSPOS gefunden
				setForNew();
			}

			dto2Components();

			setzeSichtbarkeitSnrChnr();

			setStatusbar();

			refreshMyComponents();
			tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
			tpBestellung.setTitle();
			// Status setzen
			getPanelStatusbar().setStatusCNr(
					tpBestellung.getBesDto().getStatusCNr());

		}

		MessageFormat mf = new MessageFormat(
				LPMain.getTextRespectUISPr("best.wareneingangspostiotn.selektierterwareneingang"));
		mf.setLocale(LPMain.getTheClient().getLocUi());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tpBestellung.getWareneingangDto()
				.getTLieferscheindatum().getTime());

		String lsDatum = Helper.formatDatum(c.getTime(), LPMain.getTheClient()
				.getLocUi());

		c.setTimeInMillis(tpBestellung.getWareneingangDto()
				.getTWareneingangsdatum().getTime());
		String weDatum = Helper.formatDatum(c.getTime(), LPMain.getTheClient()
				.getLocUi());

		String lager = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByPrimaryKey(
						tpBestellung.getWareneingangDto().getLagerIId())
				.getCNr();

		Object pattern[] = {
				tpBestellung.getWareneingangDto().getCLieferscheinnr(),
				lsDatum, weDatum, lager };

		wlaWareneingang.setText(mf.format(pattern));
		wlaWareneingang.setToolTipText(mf.format(pattern));

	}

	private void setForNew() throws Throwable {
		WareneingangspositionDto weposDto = new WareneingangspositionDto();
		BigDecimal bdOffeneMenge = DelegateFactory.getInstance()
				.getBestellungDelegate()
				.berechneOffeneMengePosition(getBestellpositionDto().getIId());
		weposDto.setNGeliefertemenge((bdOffeneMenge.doubleValue() <= 0) ? new BigDecimal(
				0) : bdOffeneMenge);

		BigDecimal zuschlag = DelegateFactory
				.getInstance()
				.getMaterialDelegate()
				.getMaterialzuschlagEKInZielwaehrung(
						getArtikelDto().getIId(),
						tpBestellung.getBesDto()
								.getLieferantIIdBestelladresse(),
						new java.sql.Date(tpBestellung.getWareneingangDto()
								.getTLieferscheindatum().getTime()),
						tpBestellung.getBesDto().getWaehrungCNr());

		BigDecimal bdPreiMitAktuellemZusschlag = getBestellpositionDto()
				.getNNettogesamtpreis();

		if (bdPreiMitAktuellemZusschlag != null
				&& getBestellpositionDto().getNMaterialzuschlag() != null) {
			bdPreiMitAktuellemZusschlag = bdPreiMitAktuellemZusschlag
					.subtract(getBestellpositionDto().getNMaterialzuschlag());
			if (zuschlag != null) {
				bdPreiMitAktuellemZusschlag = bdPreiMitAktuellemZusschlag
						.add(zuschlag);
			}

		}

		weposDto.setNGelieferterpreis(bdPreiMitAktuellemZusschlag);

		if (bdPreiMitAktuellemZusschlag != null
				&& getBestellpositionDto().getNMenge() != null) {
			wnfGelieferterWert.setBigDecimal(getBestellpositionDto()
					.getNNettogesamtpreis().multiply(
							getBestellpositionDto().getNMenge()));
		}

		tpBestellung.setWareneingangspositionenDto(weposDto);
	}

	protected boolean ueberliefernDlg() throws Throwable {
		double dGelieferteMenge = wnfGelieferteMenge.getBigDecimal()
				.doubleValue();
		if (dGelieferteMenge != 0 && getBestellpositionDto().getIId() != null) {
			double ddBSPOSMenge = getBestellpositionDto().getNMenge()
					.doubleValue();

			BigDecimal bdOffeneMenge = DelegateFactory
					.getInstance()
					.getBestellungDelegate()
					.berechneOffeneMengePosition(
							getBestellpositionDto().getIId());

			if (dGelieferteMenge > ddBSPOSMenge) {
				boolean answer = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("bes.ueberlieferung"),
						LPMain.getTextRespectUISPr("lp.warning"));
				if (!answer) {
					wnfGelieferteMenge.setBigDecimal((bdOffeneMenge
							.doubleValue() <= 0) ? new BigDecimal(0)
							: bdOffeneMenge);
				}
			}
		}
		return true;
	}

	private void setzeSichtbarkeitSnrChnr() {
		if (getArtikelDto() != null) {
			if (Helper.short2boolean(getArtikelDto().getBChargennrtragend())
					|| Helper.short2boolean(getArtikelDto()
							.getBSeriennrtragend())) {
				wtfSeriennr.setMandatoryField(true);
				wnfGelieferteMenge.setActivatable(false);
			} else {
				wtfSeriennr.setMandatoryField(false);
				wnfGelieferteMenge.setActivatable(true);

			}
		}
	}

	private void aktualisiereFelderSnrChnr(boolean bEnableField)
			throws Throwable {
		if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
				|| Helper.short2boolean(artikelDto.getBChargennrtragend())) {
			// Behandlung der Seriennummer

			wtfSeriennr.setSeriennummern(tpBestellung
					.getWareneingangspositionenDto()
					.getSeriennrChargennrMitMenge(), artikelDto.getIId(),
					getTPBes().getWareneingangDto().getLagerIId());
			jpaWorkingOn.add(wtfSeriennr.getButtonSnrAuswahl(),
					new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(1, 2, 1, 2), 0, 0));
			jpaWorkingOn.add(wtfSeriennr, new GridBagConstraints(1, iZeile, 1,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		} else {
			jpaWorkingOn.remove(wtfSeriennr);
			jpaWorkingOn.remove(wtfSeriennr.getButtonSnrAuswahl());
		}
	}

	protected int checkAndSetGelPreisDlg() throws ExceptionLP, Throwable {
		int iDoSave = JOptionPane.YES_OPTION;
		if (getBestellpositionDto().getIId() != null
				&& wnfGelieferterPreis.getBigDecimal() != null) {
			BigDecimal bestellposPreis = DelegateFactory
					.getInstance()
					.getBestellungDelegate()
					.bestellpositionFindByPrimaryKey(
							getBestellpositionDto().getIId())
					.getNNettogesamtpreis();
			BigDecimal bdEKAbweichung = Helper.calculateRatioInDecimal(
					wnfGelieferterPreis.getBigDecimal(), bestellposPreis);
			ParametermandantDto pmEKPreisabweichung = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_EINKAUSPREIS_ABWEICHUNG);
			BigDecimal bdEKPreisabweichung = new BigDecimal(
					pmEKPreisabweichung.getCWert());
			if ((bdEKPreisabweichung.compareTo(bdEKAbweichung)) < 0) {
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("bes.preisspanne"));
				mf.setLocale(LPMain.getTheClient().getLocUi());
				Object pattern[] = { pmEKPreisabweichung.getCWert(),
						bdEKAbweichung.toString() };
				String sMsg = mf.format(pattern);

				iDoSave = DialogFactory.showMeldung(sMsg,
						LPMain.getTextRespectUISPr("lp.warning"),
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (iDoSave == JOptionPane.NO_OPTION) {
					// Gel. Preis nicht erfassen->mit BSPOS-Preis
					// ueberschreiben.
					wnfGelieferterPreis.setBigDecimal(bestellposPreis);
					iDoSave = JOptionPane.YES_OPTION;
				}
			}
		}
		return iDoSave;
	}

	protected ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	private void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	private ArtikellieferantDto getArtikellieferantDto() {
		return artikellieferantDto;
	}

	private void setArtikellieferantDto(ArtikellieferantDto artikellieferantDto) {
		this.artikellieferantDto = artikellieferantDto;
	}

	protected void setStatusbar() throws Throwable {
		WareneingangspositionDto weposDTO = tpBestellung
				.getWareneingangspositionenDto();
		if (weposDTO != null) {
			setStatusbarPersonalIIdAnlegen(weposDTO.getPersonalIIdAnlegen());
			setStatusbarTAnlegen(weposDTO.getTAnlegen());
			setStatusbarPersonalIIdAendern(weposDTO.getPersonalIIdAendern());
			setStatusbarTAendern(weposDTO.getTAendern());
		}
	}

	private TabbedPaneBestellung getTPBes() {
		return getInternalFrameBestellung().getTabbedPaneBestellung();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfGelieferteMenge;
	}

	private void refreshMyComponents() throws Throwable {
		WareneingangDto we = tpBestellung.getWareneingangDto();
		WareneingangspositionDto wepos = tpBestellung
				.getWareneingangspositionenDto();

		// PJ17904

		if (wepos != null) {
			wefKommentar.setText(wepos.getXInternerKommentar());
			wefKommentar.revalidate();
		} else {
			wefKommentar.setText("");
		}

		if (getTPBes().getInternalFrame().getRechtModulweit()
				.equals(RechteFac.RECHT_MODULWEIT_UPDATE)) {

			// Loesche-Button
			boolean bEnableLoesche = (wepos != null)
					&& (wepos.getWareneingangIId() != null)
					&& (we.getIId() != null)
					&& (wepos.getWareneingangIId().intValue() == we.getIId()
							.intValue())
					&& !getBestellpositionDto()
							.getBestellpositionstatusCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)
					&& !tpBestellung.getBesDto().getStatusCNr()
							.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT);

			int lockstate = getLockedstateDetailMainKey().getIState();
			if (lockstate == LOCK_IS_LOCKED_BY_ME
					|| lockstate == LOCK_IS_NOT_LOCKED) {
				LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
						PanelBasis.ACTION_DELETE);
				item.getButton().setEnabled(bEnableLoesche);
			}

			boolean bEnable = (lockstate == LOCK_FOR_NEW);

			String cArt = getTPBes().getBesDto().getBestellungartCNr();
			bEnable = cArt
					.equals(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR)
					|| cArt.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR);
			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
					ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN);
			item.getButton().setEnabled(bEnable);

			boolean bEnableEtikket = (wepos != null)
					&& (wepos.getWareneingangIId() != null);
			LPButtonAction itemEtikket = (LPButtonAction) getHmOfButtons().get(
					ACTION_SPECIAL_ETIKETTDRUCKEN);
			itemEtikket.getButton().setEnabled(bEnableEtikket);
		}
	}

}

class FocusAdapterGewichtPreis implements FocusListener {
	private PanelBestellungWEP adaptee = null;

	FocusAdapterGewichtPreis(PanelBestellungWEP adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {
			if (adaptee.wnfGewichtPreis.getBigDecimal() != null) {
				if (adaptee.getArtikelDto().getNUmrechnungsfaktor() != null) {
					BigDecimal umrechnung = new BigDecimal(0);
					BigDecimal preis = adaptee.wnfGewichtPreis.getBigDecimal();
					BigDecimal faktor = adaptee.getArtikelDto()
							.getNUmrechnungsfaktor();
					if (preis != null || faktor != null) {
						if (Helper.short2boolean(adaptee.getArtikelDto()
								.getbBestellmengeneinheitInvers())) {
							umrechnung = preis.divide(faktor, Defaults
									.getInstance()
									.getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							umrechnung = preis.multiply(faktor);
						}
						adaptee.wnfGelieferterPreis.setBigDecimal(umrechnung);
						if (adaptee.wnfGelieferteMenge.getBigDecimal() != null) {
							adaptee.wnfGelieferterWert.setBigDecimal(umrechnung
									.multiply(adaptee.wnfGelieferteMenge
											.getBigDecimal()));
						}
					}
				}
			}

		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class FocusAdapterGewicht implements FocusListener {
	private PanelBestellungWEP adaptee = null;

	FocusAdapterGewicht(PanelBestellungWEP adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {

			if (adaptee.wnfGewichtbestellmenge.getBigDecimal() != null) {
				if (adaptee.getArtikelDto().getNUmrechnungsfaktor() != null) {
					BigDecimal umrechnung = new BigDecimal(0);
					BigDecimal gewicht = adaptee.wnfGewichtbestellmenge
							.getBigDecimal();
					BigDecimal faktor = adaptee.getArtikelDto()
							.getNUmrechnungsfaktor();
					if (gewicht != null || faktor != null) {

						if (Helper.short2boolean(adaptee.getArtikelDto()
								.getbBestellmengeneinheitInvers())) {
							umrechnung = gewicht.multiply(faktor);
						} else {
							umrechnung = gewicht.divide(faktor, Defaults
									.getInstance()
									.getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN);
						}

						adaptee.wnfGelieferteMenge.setBigDecimal(umrechnung);
					}
				}
			}
		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class WnfGelieferteMengeFocusAdapter extends java.awt.event.FocusAdapter {
	private PanelBestellungWEP adaptee;

	WnfGelieferteMengeFocusAdapter(PanelBestellungWEP adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		if (adaptee.getArtikelDto() != null
				&& adaptee.getArtikelDto().getNUmrechnungsfaktor() != null) {
			try {
				if (adaptee.wnfGelieferteMenge.getBigDecimal() != null) {
					if (Helper.short2boolean(adaptee.getArtikelDto()
							.getbBestellmengeneinheitInvers())) {

						adaptee.wnfGewichtbestellmenge
								.setBigDecimal(adaptee.wnfGelieferteMenge
										.getBigDecimal()
										.divide(adaptee.getArtikelDto()
												.getNUmrechnungsfaktor(),
												Defaults.getInstance()
														.getIUINachkommastellenPreiseEK(),
												BigDecimal.ROUND_HALF_EVEN));
					} else {
						adaptee.wnfGewichtbestellmenge
								.setBigDecimal(adaptee.wnfGelieferteMenge
										.getBigDecimal()
										.multiply(
												adaptee.getArtikelDto()
														.getNUmrechnungsfaktor()));
					}

				}
			} catch (Throwable tDummy) {
				LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
			}
		}
		try {
			if (adaptee.wnfGelieferteMenge.getBigDecimal() != null
					&& adaptee.wnfGelieferterPreis.getBigDecimal() != null) {
				adaptee.wnfGelieferterWert
						.setBigDecimal(adaptee.wnfGelieferterPreis
								.getBigDecimal().multiply(
										adaptee.wnfGelieferteMenge
												.getBigDecimal()));
			}
		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}

	}
}

class WnfGelieferterPreisFocusAdapter extends java.awt.event.FocusAdapter {
	private PanelBestellungWEP adaptee;

	WnfGelieferterPreisFocusAdapter(PanelBestellungWEP adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			if (adaptee.wnfGelieferterPreis.getBigDecimal() != null
					&& adaptee.getArtikelDto() != null
					&& adaptee.getArtikelDto().getNUmrechnungsfaktor() != null) {

				BigDecimal nPreisPerEinheit = null;
				if (Helper.short2boolean(adaptee.getArtikelDto()
						.getbBestellmengeneinheitInvers())) {
					nPreisPerEinheit = (adaptee.wnfGelieferterPreis
							.getBigDecimal().multiply(adaptee.getArtikelDto()
							.getNUmrechnungsfaktor()));
				} else {
					nPreisPerEinheit = (adaptee.wnfGelieferterPreis
							.getBigDecimal().divide(adaptee.getArtikelDto()
							.getNUmrechnungsfaktor(), Defaults.getInstance()
							.getIUINachkommastellenPreiseEK(),
							BigDecimal.ROUND_HALF_EVEN));
				}

				adaptee.wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);

			}

			if (adaptee.wnfGelieferterPreis.getBigDecimal() != null
					&& adaptee.wnfGelieferteMenge.getBigDecimal() != null) {
				adaptee.wnfGelieferterWert
						.setBigDecimal(adaptee.wnfGelieferterPreis
								.getBigDecimal().multiply(
										adaptee.wnfGelieferteMenge
												.getBigDecimal()));
			}
		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class WnfGelieferterWertFocusAdapter extends java.awt.event.FocusAdapter {
	private PanelBestellungWEP adaptee;

	WnfGelieferterWertFocusAdapter(PanelBestellungWEP adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			if (adaptee.wnfGelieferterWert.getBigDecimal() != null) {

				if (adaptee.wnfGelieferteMenge.getBigDecimal() != null
						&& adaptee.wnfGelieferteMenge.getBigDecimal()
								.doubleValue() != 0) {

					BigDecimal bdGelPreis = adaptee.wnfGelieferterWert
							.getBigDecimal().divide(
									adaptee.wnfGelieferteMenge.getBigDecimal(),
									4, BigDecimal.ROUND_HALF_EVEN);

					adaptee.wnfGelieferterPreis.setBigDecimal(bdGelPreis);
				} else {
					adaptee.wnfGelieferterPreis.setBigDecimal(BigDecimal.ZERO);
				}

				//
				if (adaptee.wnfGelieferterPreis.getBigDecimal() != null
						&& adaptee.getArtikelDto() != null
						&& adaptee.getArtikelDto().getNUmrechnungsfaktor() != null) {

					BigDecimal nPreisPerEinheit = null;
					if (Helper.short2boolean(adaptee.getArtikelDto()
							.getbBestellmengeneinheitInvers())) {
						nPreisPerEinheit = (adaptee.wnfGelieferterPreis
								.getBigDecimal().multiply(adaptee
								.getArtikelDto().getNUmrechnungsfaktor()));
					} else {
						nPreisPerEinheit = (adaptee.wnfGelieferterPreis
								.getBigDecimal().divide(adaptee.getArtikelDto()
								.getNUmrechnungsfaktor(),
								Defaults.getInstance()
										.getIUINachkommastellenPreiseEK(),
								BigDecimal.ROUND_HALF_EVEN));
					}

					adaptee.wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);

					if (adaptee.wnfGelieferteMenge.getBigDecimal() != null) {
						adaptee.wnfGelieferterWert
								.setBigDecimal(adaptee.wnfGelieferterPreis
										.getBigDecimal().multiply(
												adaptee.wnfGelieferteMenge
														.getBigDecimal()));
					}
				}

			}
		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}
