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
package com.lp.client.angebot;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelKonditionen;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Fenster werden Konditionen zum Angebot erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 12.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version 1.0
 */
public class PanelAngebotKonditionen extends PanelKonditionen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAngebot intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAngebot tpAngebot = null;

	private WrapperLabel wlaVersteckterAufschlag = null;
	private WrapperNumberField wnfVersteckterAufschlag = null;
	private WrapperLabel wlaProzent3 = null;
	// Allgemeiner Rabattsatz aus PanelKonditionen
	private WrapperLabel wlaProjektierungsrabatt = null;
	private WrapperNumberField wnfProjektierungsrabatt = null;
	private WrapperLabel wlaProzent4 = null;

	private WrapperLabel wlaAngebotswertinangebotswaehrung = null;
	private WrapperLabel wlaAngebotswaehrung1 = null;
	private WrapperNumberField wnfAngebotswertinangebotswaehrung = null;

	private WrapperLabel wlaKorrekturbetrag = null;
	private WrapperNumberField wnfKorrekturbetrag = null;

	private WrapperCheckBox wcbMitZusammenfassung = new WrapperCheckBox();

	private PanelAngebotAkquisedaten panelAngebotAkquisedaten = null;

	public PanelAngebotKonditionen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAngebot) internalFrame;
		tpAngebot = intFrame.getTabbedPaneAngebot();

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		wtfLieferart.setMandatoryField(true);
		wtfZahlungsziel.setMandatoryField(true);
		wtfSpedition.setMandatoryField(true);

		wlaVersteckterAufschlag = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.versteckteraufschlag"));
		wnfVersteckterAufschlag = new WrapperNumberField();
		wlaProzent3 = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		wlaProzent3.setHorizontalAlignment(SwingConstants.LEFT);

		wlaProjektierungsrabatt = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("kond.label.projektRabatt"));
		wnfProjektierungsrabatt = new WrapperNumberField();
		wlaProzent4 = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		wlaProzent4.setHorizontalAlignment(SwingConstants.LEFT);

		wcbMitZusammenfassung.setText(LPMain
				.getTextRespectUISPr("angebot.mitzusammmenfassung"));

		wlaAngebotswertinangebotswaehrung = new WrapperLabel(LPMain
				.getInstance().getTextRespectUISPr("angb.angebotswert"));
		wnfAngebotswertinangebotswaehrung = new WrapperNumberField();

		wnfAngebotswertinangebotswaehrung.setFractionDigits(Defaults
				.getInstance().getIUINachkommastellenPreiseVK());

		wnfKorrekturbetrag = new WrapperNumberField();

		wnfKorrekturbetrag.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());
		wnfKorrekturbetrag.setActivatable(false);

		wlaKorrekturbetrag = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.korrekturbetrag"));

		wnfAngebotswertinangebotswaehrung.addFocusListener(this);
		wlaAngebotswaehrung1 = new WrapperLabel();
		// HelperClient.setDefaultsToComponent(wlaAngebotswaehrung1, 240);
		wlaAngebotswaehrung1.setHorizontalAlignment(SwingConstants.LEADING);

		iZeile = 4; // mein eigener Teil beginnt nach den ersten drei Zeilen des
					// BasisPanel

		jPanelWorkingOn.add(wlaVersteckterAufschlag, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfVersteckterAufschlag, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent3, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbMitZusammenfassung, new GridBagConstraints(3,
				iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaAllgemeinerRabatt, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfAllgemeinerRabatt, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent2, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaProjektierungsrabatt, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfProjektierungsrabatt, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent4, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaAngebotswertinangebotswaehrung,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 10, 2), 0, 0));
		jPanelWorkingOn.add(wnfAngebotswertinangebotswaehrung,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 10, 2), 0, 0));
		jPanelWorkingOn.add(wlaAngebotswaehrung1, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 10, 2), 0, 0));
		jPanelWorkingOn.add(wlaKorrekturbetrag, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 10, 2), 0, 0));

		jPanelWorkingOn.add(wnfKorrekturbetrag, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// review JO
		iZeile++;
		panelAngebotAkquisedaten = new PanelAngebotAkquisedaten(intFrame,
				LPMain.getInstance().getTextRespectUISPr("angb.akquisedaten"));
		panelAngebotAkquisedaten.addAkquisedaten(jPanelWorkingOn, iZeile);
	}

	private void setDefaults() throws Throwable {
		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		panelAngebotAkquisedaten.setDefaults();

		wlaAngebotswaehrung1
				.setText(tpAngebot.getAngebotDto().getWaehrungCNr());
		wlaAngebotswaehrung1
				.setText(tpAngebot.getAngebotDto().getWaehrungCNr());

		wefKopftext.setDefaultText(tpAngebot.getKopftextDto().getXTextinhalt());
		wefFusstext.setDefaultText(tpAngebot.getFusstextDto().getXTextinhalt());
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_ANGEBOT;
	}

	public void dto2Components() throws Throwable {
		holeLieferart(tpAngebot.getAngebotDto().getLieferartIId());
		holeZahlungsziel(tpAngebot.getAngebotDto().getZahlungszielIId());
		holeSpediteur(tpAngebot.getAngebotDto().getSpediteurIId());

		wnfVersteckterAufschlag.setDouble(tpAngebot.getAngebotDto()
				.getFVersteckterAufschlag());
		wnfAllgemeinerRabatt.setDouble(tpAngebot.getAngebotDto()
				.getFAllgemeinerRabattsatz());
		wnfProjektierungsrabatt.setDouble(tpAngebot.getAngebotDto()
				.getFProjektierungsrabattsatz());
		wcbMitZusammenfassung.setShort(tpAngebot.getAngebotDto()
				.getBMitzusammenfassung());
		// die Werte des Angebots anzeigen
		BigDecimal bdAngebotswertinanfragewaehrung = tpAngebot.getAngebotDto()
				.getNGesamtwertinbelegwaehrung();

		if (bdAngebotswertinanfragewaehrung == null
				|| tpAngebot.getAngebotDto().getStatusCNr()
						.equals(AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
			bdAngebotswertinanfragewaehrung = DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.berechneNettowertGesamt(tpAngebot.getAngebotDto().getIId());
			if (tpAngebot.getAngebotDto().getNKorrekturbetrag() != null) {
				bdAngebotswertinanfragewaehrung = bdAngebotswertinanfragewaehrung
						.add(tpAngebot.getAngebotDto().getNKorrekturbetrag());
			}
		}

		wnfAngebotswertinangebotswaehrung
				.setBigDecimal(bdAngebotswertinanfragewaehrung);
		wnfKorrekturbetrag.setBigDecimal(tpAngebot.getAngebotDto()
				.getNKorrekturbetrag());

		wtfLieferartort.setText(tpAngebot.getAngebotDto().getCLieferartort());

		panelAngebotAkquisedaten.wdfNachfasstermin.setDate(tpAngebot
				.getAngebotDto().getTNachfasstermin());
		panelAngebotAkquisedaten.wdfRealisierungstermin.setDate(tpAngebot
				.getAngebotDto().getTRealisierungstermin());
		panelAngebotAkquisedaten.wnfAuftragswahrscheinlichkeit
				.setDouble(tpAngebot.getAngebotDto()
						.getFAuftragswahrscheinlichkeit());
		panelAngebotAkquisedaten.wtfAblageort.setText(tpAngebot.getAngebotDto()
				.getXAblageort());

		// Kopftext fuer dieses Angebot in der Sprache des Kunden anzeigen
		if (tpAngebot.getAngebotDto().getXKopftextuebersteuert() != null) {
			wefKopftext.setText(tpAngebot.getAngebotDto()
					.getXKopftextuebersteuert());
		} else {
			wefKopftext.setText(DelegateFactory
					.getInstance()
					.getAngebotServiceDelegate()
					.getAngebotkopfDefault(
							tpAngebot.getKundeDto().getPartnerDto()
									.getLocaleCNrKommunikation())
					.getXTextinhalt());
		}

		// Fusstext fuer dieses Angebot in der Sprache des Lieferante anzeigen
		if (tpAngebot.getAngebotDto().getXFusstextuebersteuert() != null) {
			wefFusstext.setText(tpAngebot.getAngebotDto()
					.getXFusstextuebersteuert());
		} else {
			wefFusstext.setText(DelegateFactory
					.getInstance()
					.getAngebotServiceDelegate()
					.getAngebotfussDefault(
							tpAngebot.getKundeDto().getPartnerDto()
									.getLocaleCNrKommunikation())
					.getXTextinhalt());
		}

		aktualisiereStatusbar();
	}

	public void focusGained(FocusEvent e) {

	}

	public void focusLost(FocusEvent e) {

		try {
			BigDecimal bdAngebotswertinanfragewaehrungVorhanden = DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.berechneNettowertGesamt(tpAngebot.getAngebotDto().getIId());

			if (wnfAngebotswertinangebotswaehrung.getBigDecimal() != null
					&& bdAngebotswertinanfragewaehrungVorhanden != null
					&& !bdAngebotswertinanfragewaehrungVorhanden
							.equals(wnfAngebotswertinangebotswaehrung
									.getBigDecimal())) {
				// Versteckten Aufschlag neu berechnen, wenn im editieren-Modus

				BigDecimal bdFaktor = new BigDecimal(1 + (tpAngebot
						.getAngebotDto().getFVersteckterAufschlag() / 100));
				if (bdFaktor.doubleValue() == 0) {

					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.error"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"lp.error.berechnung.versteckter.aufschlag"));
				} else {

					BigDecimal bdWertOhneVerstAufschlag = bdAngebotswertinanfragewaehrungVorhanden
							.divide(bdFaktor, BigDecimal.ROUND_HALF_EVEN,
									Defaults.getInstance()
											.getIUINachkommastellenPreiseVK());
					if (bdWertOhneVerstAufschlag.doubleValue() != 0) {
						BigDecimal bd = bdWertOhneVerstAufschlag
								.subtract(wnfAngebotswertinangebotswaehrung
										.getBigDecimal());

						bd = bd.divide(bdWertOhneVerstAufschlag,
								BigDecimal.ROUND_HALF_EVEN, Defaults
										.getInstance()
										.getIUINachkommastellenPreiseVK());
						bd = bd.multiply(new BigDecimal(100));

						wnfVersteckterAufschlag.setBigDecimal(bd.negate());

						// Eigentlich muss nun das Angebot gespeichert werden ->
						tpAngebot.getAngebotDto().setNKorrekturbetrag(null);
						tpAngebot.getAngebotDto().setFVersteckterAufschlag(
								wnfVersteckterAufschlag.getDouble());
						DelegateFactory.getInstance().getAngebotDelegate()
								.updateAngebot(tpAngebot.getAngebotDto(), null);
						DelegateFactory
								.getInstance()
								.getAngebotDelegate()
								.updateAngebotKonditionen(
										tpAngebot.getAngebotDto().getIId());

						BigDecimal bdWertBerechnetNeu = DelegateFactory
								.getInstance()
								.getAngebotDelegate()
								.berechneNettowertGesamt(
										tpAngebot.getAngebotDto().getIId());

						// Korrekturbetrag
						BigDecimal bdKorrektur = wnfAngebotswertinangebotswaehrung
								.getBigDecimal().subtract(bdWertBerechnetNeu);
						wnfKorrekturbetrag.setBigDecimal(bdKorrektur);

						tpAngebot.getAngebotDto().setNKorrekturbetrag(
								bdKorrektur);
						DelegateFactory.getInstance().getAngebotDelegate()
								.updateAngebot(tpAngebot.getAngebotDto(), null);

					}
				}

			}
		} catch (Throwable e1) {
			getInternalFrame().handleException(e1, true);
		}

	}

	private void components2Dto() throws Throwable {
		tpAngebot.getAngebotDto().setLieferartIId(lieferartDto.getIId());
		tpAngebot.getAngebotDto().setZahlungszielIId(zahlungszielDto.getIId());
		tpAngebot.getAngebotDto().setSpediteurIId(spediteurDto.getIId());

		tpAngebot.getAngebotDto().setFVersteckterAufschlag(
				wnfVersteckterAufschlag.getDouble());
		tpAngebot.getAngebotDto().setFAllgemeinerRabattsatz(
				wnfAllgemeinerRabatt.getDouble());
		tpAngebot.getAngebotDto().setFProjektierungsrabattsatz(
				wnfProjektierungsrabatt.getDouble());
		tpAngebot.getAngebotDto().setCLieferartort(wtfLieferartort.getText());

		tpAngebot.getAngebotDto().setBMitzusammenfassung(
				wcbMitZusammenfassung.getShort());
		// Angebotswert wird nur angezeigt, aber nicht eingegeben und
		// abgespeichert

		tpAngebot.getAngebotDto().setTNachfasstermin(
				panelAngebotAkquisedaten.wdfNachfasstermin.getTimestamp());
		tpAngebot.getAngebotDto().setTRealisierungstermin(
				panelAngebotAkquisedaten.wdfRealisierungstermin.getTimestamp());
		tpAngebot.getAngebotDto().setFAuftragswahrscheinlichkeit(
				panelAngebotAkquisedaten.wnfAuftragswahrscheinlichkeit
						.getDouble());
		tpAngebot.getAngebotDto().setXAblageort(
				panelAngebotAkquisedaten.wtfAblageort.getText());

		tpAngebot.getAngebotDto().setBelegtextIIdKopftext(
				tpAngebot.getKopftextDto().getIId());

		tpAngebot.getAngebotDto().setNKorrekturbetrag(
				wnfKorrekturbetrag.getBigDecimal());
		// wenn der Kopftext nicht ueberschrieben wurde -> null setzen
		if (wefKopftext.getText() != null
				&& wefKopftext.getText().equals(
						DelegateFactory
								.getInstance()
								.getAngebotServiceDelegate()
								.getAngebotkopfDefault(
										tpAngebot.getKundeDto().getPartnerDto()
												.getLocaleCNrKommunikation())
								.getXTextinhalt())) {
			tpAngebot.getAngebotDto().setXKopftextuebersteuert(null);
		} else {
			tpAngebot.getAngebotDto().setXKopftextuebersteuert(
					wefKopftext.getText());
		}

		tpAngebot.getAngebotDto().setBelegtextIIdFusstext(
				tpAngebot.getFusstextDto().getIId());

		// wenn der Fusstext nicht ueberschrieben wurde -> null setzen
		if (wefFusstext.getText() != null
				&& wefFusstext.getText().equals(
						DelegateFactory
								.getInstance()
								.getAngebotServiceDelegate()
								.getAngebotkopfDefault(
										tpAngebot.getKundeDto().getPartnerDto()
												.getLocaleCNrKommunikation())
								.getXTextinhalt())) {
			tpAngebot.getAngebotDto().setXFusstextuebersteuert(null);
		} else {
			tpAngebot.getAngebotDto().setXFusstextuebersteuert(
					wefFusstext.getText());
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			DelegateFactory.getInstance().getAngebotDelegate()
					.updateAngebot(tpAngebot.getAngebotDto(), null);

			DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.updateAngebotKonditionen(
							tpAngebot.getAngebotDto().getIId());

			super.eventActionSave(e, false); // buttons schalten

			eventYouAreSelected(false);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAngebot.istAktualisierenAngebotErlaubt()) {
			super.eventActionUpdate(aE, false);
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpAngebot.getAngebotDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		tpAngebot.setAngebotDto(DelegateFactory.getInstance()
				.getAngebotDelegate().angebotFindByPrimaryKey((Integer) oKey));
		dto2Components();

		tpAngebot.setTitleAngebot(LPMain.getInstance().getTextRespectUISPr(
				"angb.panel.konditionen"));
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAngebot.getAngebotDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAngebot.getAngebotDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAngebot.getAngebotDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAngebot.getAngebotDto().getTAendern());
		setStatusbarStatusCNr(tpAngebot.getAngebotStatus());
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_FORMAT_NUMBER:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.belegwerte"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}
}
