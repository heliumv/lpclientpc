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
package com.lp.client.partner;

import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.mengenstaffel.PanelMengenstaffel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Fenster wird eine Kundesokomengenstaffel erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 07.07.2006</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelKundesokomengenstaffel extends PanelMengenstaffel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected InternalFrameKunde internalFrameKunde = null;

	private KundesokoDto kundesokoDto = null;
	private KundesokomengenstaffelDto mengenstaffelDto = null;

	public PanelKundesokomengenstaffel(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key, false);

		internalFrameKunde = (InternalFrameKunde) internalFrame;

		initComponents();
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();

		setComponentsEnabled(false);

		mengenstaffelDto = new KundesokomengenstaffelDto();

		// den Artikel bzw. die Artikelgruppe bei einer neuen Mengenstaffel
		// initialisieren
		kundesokoDto = internalFrameKunde.getTpKunde().getPanelKundesoko()
				.getKundesokoDto();

		if (kundesokoDto.getArtikelIId() != null) {
			wcoArt.setKeyOfSelectedItem(ART_ARTIKEL);

			panelArtikel.getWifArtikel().setArtikelDto(
					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									kundesokoDto.getArtikelIId()));

			panelArtikel.artikelDto2Components(panelArtikel.getWifArtikel()
					.getArtikelDto());

			panelArtikel.getWdfGueltigab().setDate(
					kundesokoDto.getTPreisgueltigab());
			panelArtikel.getWdfGueltigbis().setDate(
					kundesokoDto.getTPreisgueltigbis());

			panelArtikel.wcbWirktNichtInVerkaufspreisfindung.setVisible(false);

		} else {
			wcoArt.setKeyOfSelectedItem(ART_ARTIKELGRUPPE);

			panelArtgru.artgruDto2components(kundesokoDto.getArtgruIId());

			panelArtgru.getWdfGueltigab().setDate(
					kundesokoDto.getTPreisgueltigab());
			panelArtgru.getWdfGueltigbis().setDate(
					kundesokoDto.getTPreisgueltigbis());
		}
	}

	private void setComponentsEnabled(boolean bEnabled) {
		wcoArt.setActivatable(bEnabled);

		panelArtikel.getWifArtikel().getWbuArtikel().setActivatable(bEnabled);
		panelArtikel.getWifArtikel().getWtfIdent().setActivatable(bEnabled);
		panelArtikel.getWifArtikel().getWtfBezeichnung()
				.setActivatable(bEnabled);
		panelArtikel.getWdfGueltigab().setActivatable(bEnabled);
		panelArtikel.getWdfGueltigbis().setActivatable(bEnabled);

		panelArtgru.getWbuArtikelgruppe().setActivatable(bEnabled);
		panelArtgru.getWtfArtikelgruppe().setActivatable(bEnabled);
		panelArtgru.getWtfArtgruppespr().setActivatable(bEnabled);
		panelArtgru.getWdfGueltigab().setActivatable(bEnabled);
		panelArtgru.getWdfGueltigbis().setActivatable(bEnabled);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			mengenstaffelDto.setKundesokoIId(kundesokoDto.getIId());

			if (wcoArt.getKeyOfSelectedItem().equals(ART_ARTIKEL)) {
				// bei Verlassen des Dialogs mit Strg+S
				panelArtikel.preisSetzen();

				mengenstaffelDto = panelArtikel
						.components2mengenstaffelDto(mengenstaffelDto);
			} else { // Artikelgruppe
				mengenstaffelDto = panelArtgru
						.components2mengenstaffelDto(mengenstaffelDto);
			}

			if (mengenstaffelDto.getIId() == null) {
				Integer mengenstaffelIId = DelegateFactory.getInstance()
						.getKundesokoDelegate()
						.createKundesokomengenstaffel(mengenstaffelDto);
				mengenstaffelDto = DelegateFactory
						.getInstance()
						.getKundesokoDelegate()
						.kundesokomengenstaffelFindByPrimaryKey(
								mengenstaffelIId);

				setKeyWhenDetailPanel(mengenstaffelDto.getIId());
			} else {
				DelegateFactory.getInstance().getKundesokoDelegate()
						.updateKundesokomengenstaffel(mengenstaffelDto);
			}

			// buttons schalten
			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		setDefaults();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// neu einlesen, ausloeser war ev. ein refresh
		Object key = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {
			mengenstaffelDto = DelegateFactory.getInstance()
					.getKundesokoDelegate()
					.kundesokomengenstaffelFindByPrimaryKey((Integer) key);

			kundesokoDto = DelegateFactory
					.getInstance()
					.getKundesokoDelegate()
					.kundesokoFindByPrimaryKey(
							mengenstaffelDto.getKundesokoIId());

			if (kundesokoDto.getArtikelIId() != null) {
				wcoArt.setKeyOfSelectedItem(ART_ARTIKEL);

				panelArtikel.mengenstaffelDto2components(mengenstaffelDto);
				panelArtikel.artikelDto2Components(DelegateFactory
						.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(kundesokoDto.getArtikelIId()));
			} else {
				wcoArt.setKeyOfSelectedItem(ART_ARTIKELGRUPPE);

				panelArtgru.mengenstaffelDto2components(mengenstaffelDto);
				panelArtgru.artgruDto2components(kundesokoDto.getArtgruIId());
			}

			aktualisiereStatusbar();
		}

		String sTitel = internalFrameKunde.getKundeDto().getPartnerDto()
				.formatFixTitelName1Name2();

		if (wcoArt.getKeyOfSelectedItem().equals(ART_ARTIKEL)) {
			sTitel += panelArtikel.baueTitel();
		} else {
			sTitel += panelArtgru.baueTitel();
		}

		if (mengenstaffelDto.getIId() != null) {
			sTitel += " | " + mengenstaffelDto.getNMenge();
		}

		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitel);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_KUNDESOKOMENGENSTAFFEL;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		// die Default Mengenstaffel kann nicht geloescht werden
		if (internalFrameKunde.getTpKunde()
				.getPanelKundesokomengenstaffelQP11().getTable().getRowCount() == 1) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
					.getTextRespectUISPr("part.hint.defaultmengenstaffel"));
		} else {
			if (mengenstaffelDto != null && mengenstaffelDto.getIId() != null) {
				DelegateFactory.getInstance().getKundesokoDelegate()
						.removeKundesokomengenstaffel(mengenstaffelDto);
			}

			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
														// ueberschreiben
		}
	}

	protected void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAendern(kundesokoDto.getPersonalIIdAendern());
		setStatusbarTAendern(kundesokoDto.getTAendern());
	}

	/**
	 * Auswahl der Art der SOKO ist erfolgt.
	 * 
	 * @param e
	 *            ActionEvent
	 */
	void wcoArt_actionPerformed(ActionEvent e) {
		Object currentArt = wcoArt.getKeyOfSelectedItem();

		if (currentArt.equals(ART_ARTIKEL)) {
			panelArtikel.setVisible(true);
			panelArtikel.setMandatoryFields(true);

			panelArtgru.setVisible(false);
			panelArtgru.setMandatoryFields(false);
		} else if (currentArt.equals(ART_ARTIKELGRUPPE)) {
			panelArtikel.setVisible(false);
			panelArtikel.setMandatoryFields(false);

			panelArtgru.setVisible(true);
			panelArtgru.setMandatoryFields(true);
		}
	}
}

class PanelKundesokomengenstaffel_wcoArt_actionAdapter implements
		java.awt.event.ActionListener {
	PanelKundesokomengenstaffel adaptee;

	PanelKundesokomengenstaffel_wcoArt_actionAdapter(
			PanelKundesokomengenstaffel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoArt_actionPerformed(e);
	}
}
