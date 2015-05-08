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
package com.lp.client.anfrage;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelKonditionen;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragetextDto;
import com.lp.server.system.service.MediaFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * In diesem Fenster werden Konditionen zur Anfrage erfasst.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 09.06.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */
public class PanelAnfrageLieferkonditionen extends PanelKonditionen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAnfrage intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAnfrage tpAnfrage = null;

	private WrapperLabel wlaAnfragewertinanfragewaehrung = null;
	private WrapperLabel wlaAnfragewaehrung1 = null;
	private WrapperNumberField wnfAnfragewertinanfragewaehrung = null;

	private WrapperLabel wlaTransportkosteninanfragewaehrung = null;
	private WrapperLabel wlaAnfragewaehrung2 = null;
	private WrapperNumberField wnfTransportkosteninanfragewaehrung = null;

	private WrapperLabel wlaAngebotnummer = null;
	private WrapperTextField wtfAngebotnummer = null;

	private WrapperLabel wlaAngebotsdatum = null;
	private WrapperDateField wdfAngebotsdatum = null;
	private WrapperLabel wlaAngebotsgueltigkeit = null;
	private WrapperDateField wdfAngebotsgueltigkeit = null;

	public PanelAnfrageLieferkonditionen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAnfrage) internalFrame;
		tpAnfrage = intFrame.getTabbedPaneAnfrage();

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		wtfLieferart.setMandatoryField(true);
		wtfZahlungsziel.setMandatoryField(true);
		wtfSpedition.setMandatoryField(true);

		LPMain.getInstance();
		wlaAnfragewertinanfragewaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("anf.anfragewert"));

		wnfAnfragewertinanfragewaehrung = new WrapperNumberField();
		wnfAnfragewertinanfragewaehrung.setToken("anf.anfragewert");
		wnfAnfragewertinanfragewaehrung.setActivatable(false);
		wnfAnfragewertinanfragewaehrung.setFractionDigits(Defaults
				.getInstance().getIUINachkommastellenPreiseEK());

		wlaAnfragewaehrung1 = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaAnfragewaehrung1, 240);
		wlaAnfragewaehrung1.setHorizontalAlignment(SwingConstants.LEADING);

		LPMain.getInstance();
		wlaTransportkosteninanfragewaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.transportkosten"));

		wefKopftext.setVisible(false);
		wefFusstext.setVisible(false);
		wlaKopftext.setVisible(false);
		wlaFusstext.setVisible(false);

		wnfTransportkosteninanfragewaehrung = new WrapperNumberField();
		wnfTransportkosteninanfragewaehrung.setToken("bes.transportkosten");
		wnfTransportkosteninanfragewaehrung.setFractionDigits(Defaults
				.getInstance().getIUINachkommastellenPreiseEK());

		wlaAnfragewaehrung2 = new WrapperLabel();
		wlaAnfragewaehrung2.setHorizontalAlignment(SwingConstants.LEADING);

		LPMain.getInstance();
		wlaAngebotnummer = new WrapperLabel(
				LPMain.getTextRespectUISPr("anf.angebotnummer"));
		wtfAngebotnummer = new WrapperTextField();
		wtfAngebotnummer.setToken("anf.angebotnummer");
		wtfAngebotnummer.setColumnsMax(40);
		wtfAngebotnummer.setMandatoryField(true);

		wlaAngebotsdatum = new WrapperLabel(
				LPMain.getTextRespectUISPr("anfr.angebotsdatum"));
		wdfAngebotsdatum = new WrapperDateField();
		// wdfAngebotsdatum.setToken("anfr.angebotsdatum");
		wlaAngebotsgueltigkeit = new WrapperLabel(
				LPMain.getTextRespectUISPr("anfr.angebotguelitgbis"));
		wdfAngebotsgueltigkeit = new WrapperDateField();

		iZeile = 5; // mein eigener Teil beginnt nach den ersten drei Zeilen des
					// BasisPanel

		// Zeile
		jPanelWorkingOn.add(wlaAnfragewertinanfragewaehrung,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 150, 0));
		jPanelWorkingOn.add(wnfAnfragewertinanfragewaehrung,
				new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaAnfragewaehrung1, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaTransportkosteninanfragewaehrung,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfTransportkosteninanfragewaehrung,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaAnfragewaehrung2, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaAngebotnummer, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAngebotnummer, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaAngebotsdatum, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfAngebotsdatum, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaAngebotsgueltigkeit, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfAngebotsgueltigkeit, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);

	}

	private void setDefaults() throws Throwable {
		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		wlaAnfragewaehrung1.setText(tpAnfrage.getAnfrageDto().getWaehrungCNr());
		wlaAnfragewaehrung2.setText(tpAnfrage.getAnfrageDto().getWaehrungCNr());

		// die Anfragetexte vorbelegen
		String localeCNr = LPMain.getTheClient().getLocUiAsString();

		if (tpAnfrage.getAnfrageDto().getArtCNr()
				.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)
				&& tpAnfrage.getLieferantDto().getPartnerDto()
						.getLocaleCNrKommunikation() != null) {
			localeCNr = tpAnfrage.getLieferantDto().getPartnerDto()
					.getLocaleCNrKommunikation();
		}

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_ANFRAGE;
	}

	public void dto2Components() throws Throwable {
		holeLieferart(tpAnfrage.getAnfrageDto().getLieferartIId());
		holeZahlungsziel(tpAnfrage.getAnfrageDto().getZahlungszielIId());
		holeSpediteur(tpAnfrage.getAnfrageDto().getSpediteurIId());

		wnfAllgemeinerRabatt.setDouble(tpAnfrage.getAnfrageDto()
				.getFAllgemeinerRabattsatz());
		wtfLieferartort.setText(tpAnfrage.getAnfrageDto().getCLieferartort());

		// die Werte der Anfrage
		BigDecimal bdAnfragewertinanfragewaehrung = tpAnfrage.getAnfrageDto()
				.getNGesamtwertinbelegwaehrung();

		if (bdAnfragewertinanfragewaehrung == null
				|| tpAnfrage.getAnfrageDto().getStatusCNr()
						.equals(AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)) {
			bdAnfragewertinanfragewaehrung = DelegateFactory
					.getInstance()
					.getAnfrageDelegate()
					.berechneNettowertGesamt(tpAnfrage.getAnfrageDto().getIId());
		}

		wnfAnfragewertinanfragewaehrung
				.setBigDecimal(bdAnfragewertinanfragewaehrung);
		wnfTransportkosteninanfragewaehrung.setBigDecimal(tpAnfrage
				.getAnfrageDto().getNTransportkosteninanfragewaehrung());

		wtfAngebotnummer.setText(tpAnfrage.getAnfrageDto().getCAngebotnummer());

		wdfAngebotsdatum.setTimestamp(tpAnfrage.getAnfrageDto()
				.getTAngebotdatum());
		wdfAngebotsgueltigkeit.setTimestamp(tpAnfrage.getAnfrageDto()
				.getTAngebotgueltigbis());

		aktualisiereStatusbar();
	}

	private void components2Dto() throws Throwable {
		tpAnfrage.getAnfrageDto().setFAllgemeinerRabattsatz(
				wnfAllgemeinerRabatt.getDouble());
		tpAnfrage.getAnfrageDto().setLieferartIId(lieferartDto.getIId());

		tpAnfrage.getAnfrageDto().setZahlungszielIId(zahlungszielDto.getIId());
		tpAnfrage.getAnfrageDto().setSpediteurIId(spediteurDto.getIId());

		tpAnfrage.getAnfrageDto().setTAngebotdatum(
				Helper.cutTimestamp(wdfAngebotsdatum.getTimestamp()));
		tpAnfrage.getAnfrageDto().setTAngebotgueltigbis(
				Helper.cutTimestamp(wdfAngebotsgueltigkeit.getTimestamp()));

		tpAnfrage.getAnfrageDto().setCLieferartort(wtfLieferartort.getText());
		tpAnfrage.getAnfrageDto().setNTransportkosteninanfragewaehrung(
				wnfTransportkosteninanfragewaehrung.getBigDecimal());

		tpAnfrage.getAnfrageDto().setCAngebotnummer(wtfAngebotnummer.getText());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		// Bei Delete Angebotsnummer entfernen (Wenn noch keine Lieferdaten
		// erfasst sind)
		tpAnfrage.getAnfrageDto().setCAngebotnummer(null);

		DelegateFactory.getInstance().getAnfrageDelegate()
				.updateAnfrage(tpAnfrage.getAnfrageDto(),false, null);

		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			DelegateFactory.getInstance().getAnfrageDelegate()
					.updateAnfrage(tpAnfrage.getAnfrageDto(),false, null);

			DelegateFactory
					.getInstance()
					.getAnfrageDelegate()
					.updateAnfrageLieferKonditionen(
							tpAnfrage.getAnfrageDto().getIId());

			super.eventActionSave(e, false); // buttons schalten

			eventYouAreSelected(false);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpAnfrage.getAnfrageDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		tpAnfrage.setAnfrageDto(DelegateFactory.getInstance()
				.getAnfrageDelegate().anfrageFindByPrimaryKey((Integer) oKey));
		dto2Components();

		LPMain.getInstance();
		tpAnfrage.setTitleAnfrage(LPMain
				.getTextRespectUISPr("anf.panel.konditionen"));

		tpAnfrage.getAnfrageLieferKonditionen().updateButtons(
				getLockedstateDetailMainKey());
		tpAnfrage.enableLieferdaten();
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAnfrage.istAktualisierenAnfrageErlaubt()) {
			super.eventActionUpdate(aE, false); // Buttons schalten
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAnfrage.getAnfrageDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAnfrage.getAnfrageDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAnfrage.getAnfrageDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAnfrage.getAnfrageDto().getTAendern());
		setStatusbarStatusCNr(tpAnfrage.getAnfrageStatus());
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

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (tpAnfrage.getAnfrageDto().getIId() != null) {
			if (tpAnfrage.getAnfrageDto().getStatusCNr()
					.equals(AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)
					|| tpAnfrage.getAnfrageDto().getStatusCNr()
							.equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)
					|| tpAnfrage.getAnfrageDto().getStatusCNr()
							.equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)
					|| tpAnfrage.getAnfrageDto().getStatusCNr()
							.equals(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}
}
