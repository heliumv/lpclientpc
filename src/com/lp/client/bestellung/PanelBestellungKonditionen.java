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
package com.lp.client.bestellung;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelKonditionen;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um die Konditionen einer Bestellung</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>29.03.05</I></p>
 * 
 * <p> </p>
 * 
 * @author Josef Erlinger
 * 
 * @version 1.0
 */
public class PanelBestellungKonditionen extends PanelKonditionen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameBestellung intFrame = null;
	private TabbedPaneBestellung tpBestellung = null;

	private WrapperLabel wlaBestellGesamtwert = null;
	private WrapperNumberField wnfBestellGesamtwert = null;
	private WrapperLabel wlaBestellwaehrung = null;

	public PanelBestellungKonditionen(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneBestellung tabbedPaneBestellung) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameBestellung) internalFrame;
		tpBestellung = tabbedPaneBestellung;

		jbInit();
		initPanel();
		initComponents();
	}

	private void initPanel() {
		wlaBestellwaehrung.setText(tpBestellung.getBesDto()
				.getWaehrungCNr());

		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaBestellGesamtwert.setVisible(false);
			wnfBestellGesamtwert.setVisible(false);
			wlaBestellwaehrung.setVisible(false);

			setVisibleAllgemeinerRabatt(false);
		}
	}

	private void jbInit() throws Throwable {

		wlaBestellGesamtwert = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("bes.bestellgesamtwert"));
		wnfBestellGesamtwert = new WrapperNumberField();
		wnfBestellGesamtwert.setActivatable(false);
		wnfBestellGesamtwert.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());

		wlaBestellwaehrung = new WrapperLabel();
		wlaBestellwaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		wtfLieferart.setMandatoryField(true);
		wtfSpedition.setMandatoryField(true);
		wtfZahlungsziel.setMandatoryField(true);

		wlaAllgemeinerRabattText.setForeground(Color.RED);

		if (tpBestellung.getLieferantDto() != null) {
			jPanelWorkingOn.add(wlaBestellGesamtwert, new GridBagConstraints(0,
					iZeile, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			jPanelWorkingOn.add(wnfBestellGesamtwert,
					new GridBagConstraints(1, iZeile, 1, 1, 0.06, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jPanelWorkingOn.add(wlaBestellwaehrung, new GridBagConstraints(2,
					iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jPanelWorkingOn.add(new WrapperLabel(""), new GridBagConstraints(3,
					iZeile, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
	}

	protected void components2Dto() throws Throwable {
		BestellungDto bestellungDto = tpBestellung.getBesDto();

		if (lieferartDto != null) {
			bestellungDto.setLieferartIId(lieferartDto.getIId());
		}

		if (zahlungszielDto != null) {
			bestellungDto.setZahlungszielIId(zahlungszielDto.getIId());
		}

		if (spediteurDto != null) {
			bestellungDto.setSpediteurIId(spediteurDto.getIId());
		}

		bestellungDto.setCLieferartort(wtfLieferartort.getText());
		
		bestellungDto.setFAllgemeinerRabattsatz(wnfAllgemeinerRabatt
				.getBigDecimal().doubleValue());
		// der Bestellwert wird nie vom UI genommen !

		// Kopf- und Fusstext mit den Konditionen abspeichern
		tpBestellung.getBesDto().setBestelltextIIdKopftext(
				tpBestellung.getKopftextDto().getIId());

		// wenn der Kopftext nicht ueberschrieben wurde -> null setzen
		if (wefKopftext.getText() != null
				&& wefKopftext.getText().equals(
						DelegateFactory.getInstance()
								.getBestellungServiceDelegate()
								.getBestellungkopfDefault(
										tpBestellung.getLieferantDto()
												.getPartnerDto()
												.getLocaleCNrKommunikation())
								.getXTextinhalt())) {
			tpBestellung.getBesDto().setCKopftextUebersteuert(null);
		} else {
			tpBestellung.getBesDto().setCKopftextUebersteuert(
					wefKopftext.getText());
		}
		tpBestellung.getBesDto().setBestelltextIIdFusstext(
				tpBestellung.getFusstextDto().getIId());
		// wenn der Fusstext nicht ueberschrieben wurde -> null setzen
		if (wefFusstext.getText() != null
				&& wefFusstext.getText().equals(
						DelegateFactory.getInstance()
								.getBestellungServiceDelegate()
								.getBestellungfussDefault(
										tpBestellung.getLieferantDto()
												.getPartnerDto()
												.getLocaleCNrKommunikation())
								.getXTextinhalt())) {
			tpBestellung.getBesDto().setCFusstextUebersteuert(null);
		} else {
			tpBestellung.getBesDto().setCFusstextUebersteuert(
					wefFusstext.getText());
		}
	}

	protected void dto2Components() throws Throwable {

		holeZahlungsziel(tpBestellung.getBesDto().getZahlungszielIId());

		holeLieferart(tpBestellung.getBesDto().getLieferartIId());

		holeSpediteur(tpBestellung.getBesDto().getSpediteurIId());

		wnfAllgemeinerRabatt.setBigDecimal(new BigDecimal(tpBestellung.getBesDto()
				.getFAllgemeinerRabattsatz()));

		wtfLieferartort.setText(tpBestellung.getBesDto().getCLieferartort());
		
		if (DelegateFactory.getInstance().getBestellungDelegate()
				.enthaeltBestellungMaterialzuschlaege(
						tpBestellung.getBesDto().getIId())) {
			wlaAllgemeinerRabattText.setText(LPMain.getInstance()
					.getTextRespectUISPr("bes.label.allgrabattwarnung"));
		} else {
			wlaAllgemeinerRabattText.setText("");
		}

		refreshBestellwert();

		// Kopftext fuer diese Bestellung in der Sprache des Lieferanten
		// anzeigen
		if (tpBestellung.getBesDto().getCKopftextUebersteuert() != null) {
			wefKopftext.setText(tpBestellung.getBesDto()
					.getCKopftextUebersteuert());
		} else {
			wefKopftext.setText(DelegateFactory.getInstance()
					.getBestellungServiceDelegate().getBestellungkopfDefault(
							tpBestellung.getLieferantDto().getPartnerDto()
									.getLocaleCNrKommunikation())
					.getXTextinhalt());
		}

		// Fusstext fuer diese Bestellung in der Sprache des Lieferanten
		// anzeigen
		if (tpBestellung.getBesDto().getCFusstextUebersteuert() != null) {
			wefFusstext.setText(tpBestellung.getBesDto()
					.getCFusstextUebersteuert());
		} else {
			wefFusstext.setText(DelegateFactory.getInstance()
					.getBestellungServiceDelegate().getBestellungfussDefault(
							tpBestellung.getLieferantDto().getPartnerDto()
									.getLocaleCNrKommunikation())
					.getXTextinhalt());
		}
	}

	protected void refreshBestellwert() throws ExceptionLP, Throwable {

		BigDecimal bdBestellwertInBestellwaehrung = null;
		// tpBestellung.getBestellungDto().getNBestellwert();

		/** @todo JO->WH PJ 4859 */
		// if (bdBestellwertInBestellwaehrung == null ||
		// tpBestellung.getBestellungDto().getBestellungsstatusCNr().equals(
		// BestellungFac.BESTELLSTATUS_STORNIERT)) {
		bdBestellwertInBestellwaehrung = DelegateFactory.getInstance()
				.getBestellungDelegate().berechneNettowertGesamt(
						tpBestellung.getBesDto().getIId());

		// }
		wnfBestellGesamtwert.setBigDecimal(bdBestellwertInBestellwaehrung);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			DelegateFactory.getInstance().getBestellungDelegate()
					.updateBestellung(tpBestellung.getBesDto());

			DelegateFactory.getInstance().getBestellungDelegate()
					.updateBestellungKonditionen(
							tpBestellung.getBesDto().getIId());

			super.eventActionSave(e, false);

			eventYouAreSelected(false);
		}
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);
		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
	}

	// getter Methoden
	public InternalFrameBestellung getInternalFrameBestellung() {
		return intFrame;
	}

	public TabbedPaneBestellung getTabbedPaneBestellung() {
		return getInternalFrameBestellung().getTabbedPaneBestellung();
	}

	private void setDefaults() throws Throwable {

		// belegartkonditionen: 8 die Default Texte zum Ruecksetzen hinterlegen
		wefKopftext.setDefaultText(tpBestellung.getKopftextDto()
				.getXTextinhalt());
		wefFusstext.setDefaultText(tpBestellung.getFusstextDto()
				.getXTextinhalt());

	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);

		refreshMyComponents();

		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false) ;

		// Waehrung belegen
		initPanel();
		setDefaults();
		Object oKey = tpBestellung.getBesDto().getIId();

//		if((getKeyWhenDetailPanel() == null) || (getKeyWhenDetailPanel().equals(LPMain.getInstance().getLockMeForNew()))) {
//		if (getKeyWhenDetailPanel().equals(
//				LPMain.getInstance().getLockMeForNew())
//				|| getKeyWhenDetailPanel() == null) {
			// Create.
		if(oKey == null) {
			leereAlleFelder(this);
		} else {
			// Update.
			tpBestellung.setBestellungDto(DelegateFactory.getInstance()
					.getBestellungDelegate().bestellungFindByPrimaryKey(
							(Integer) oKey));

			dto2Components();
			aktualisiereStatusbar();

			refreshMyComponents();

			tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
		}		
		
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpBestellung.getBesDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpBestellung.getBesDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpBestellung.getBesDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpBestellung.getBesDto().getTAendern());
		setStatusbarStatusCNr(tpBestellung.getBesDto()
				.getStatusCNr());
	}

	private void refreshMyComponents() throws Throwable {

		String cNrStatus = tpBestellung.getBesDto().getStatusCNr();

		boolean bEnable = (cNrStatus
				.equals(BestellungFac.BESTELLSTATUS_ANGELEGT) || cNrStatus
				.equals(BestellungFac.BESTELLSTATUS_OFFEN))
				&& (DelegateFactory.getInstance().getWareneingangDelegate()
						.getAnzahlWE(tpBestellung.getBesDto().getIId())
						.intValue() <= 0) && !isLocked();

		// Update-Button
		LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
				PanelBasis.ACTION_UPDATE);
		item.getButton().setEnabled(bEnable);

		// Loesche-Button
		item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_DELETE);
		item.getButton().setEnabled(bEnable);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		Integer nWEs = null;
		if (tpBestellung.getBesDto().getIId() != null) {
			nWEs = DelegateFactory.getInstance().getWareneingangDelegate()
					.getAnzahlWE(tpBestellung.getBesDto().getIId());
			if (nWEs != null && nWEs.intValue() == 0) {
				tpBestellung.getBesDto().setTGedruckt(null);
				tpBestellung.getBesDto().setTStorniert(null);
				tpBestellung.getBesDto().setStatusCNr(
						BestellungFac.BESTELLSTATUS_ANGELEGT);
				tpBestellung.getBesDto().setTVersandzeitpunkt(null);
				tpBestellung.getBesDto().setStatusCNr(
						BestellungFac.BESTELLSTATUS_ANGELEGT);
				DelegateFactory.getInstance().getBestellungDelegate()
						.updateBestellung(tpBestellung.getBesDto());
				// auch im Zwischenpuffer setzen
				tpBestellung.getBesDto().setStatusCNr(
						BestellungFac.BESTELLSTATUS_ANGELEGT);
			}
		}
		refreshMyComponents();

		super.eventActionUpdate(aE, false);
	}
}
