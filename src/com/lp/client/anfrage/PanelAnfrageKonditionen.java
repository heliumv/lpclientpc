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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelKonditionen;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTextmodul;
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
public class PanelAnfrageKonditionen extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAnfrage intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAnfrage tpAnfrage = null;

	private AnfragetextDto kopftextDto = null;
	private AnfragetextDto fusstextDto = null;

	protected JPanel jPanelWorkingOn = new JPanel();
	protected WrapperLabel wlaKopftext = new WrapperLabel();
	protected WrapperLabel wlaFusstext = new WrapperLabel();
	// wreditf: 1 deklaration mit null (wegen Designer)
	protected WrapperEditorField wefKopftext = null;
	protected WrapperEditorField wefFusstext = null;

	public PanelAnfrageKonditionen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAnfrage) internalFrame;
		tpAnfrage = intFrame.getTabbedPaneAnfrage();

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		this.setLayout(new GridBagLayout());

		jPanelWorkingOn.setLayout(new GridBagLayout());
		jPanelWorkingOn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
				10));

		// Actionpanel

		getInternalFrame().addItemChangedListener(this);

		this.add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
						2, 2, 2), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		wefFusstext = new WrapperEditorFieldTextmodul(getInternalFrame(),
				LPMain.getTextRespectUISPr("label.fusstext"));
		wefKopftext = new WrapperEditorFieldTextmodul(getInternalFrame(),
				LPMain.getTextRespectUISPr("label.kopftext"));
		wlaKopftext.setText(LPMain.getTextRespectUISPr("label.kopftext"));
		HelperClient.setDefaultsToComponent(wlaKopftext, 110);
		wlaFusstext.setText(LPMain.getTextRespectUISPr("label.fusstext"));

		jPanelWorkingOn.add(wlaKopftext, new GridBagConstraints(0, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 50, 0));
		jPanelWorkingOn.add(wefKopftext, new GridBagConstraints(1, 0, 5, 1,
				1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaFusstext, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefFusstext, new GridBagConstraints(1, 1, 5, 1,
				0.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

	}

	private void setDefaults() throws Throwable {
		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		// die Anfragetexte vorbelegen
		String localeCNr = LPMain.getTheClient().getLocUiAsString();

		if (tpAnfrage.getAnfrageDto().getArtCNr()
				.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)
				&& tpAnfrage.getLieferantDto().getPartnerDto()
						.getLocaleCNrKommunikation() != null) {
			localeCNr = tpAnfrage.getLieferantDto().getPartnerDto()
					.getLocaleCNrKommunikation();
		}

		kopftextDto = DelegateFactory
				.getInstance()
				.getAnfrageServiceDelegate()
				.anfragetextFindByMandantLocaleCNr(localeCNr,
						MediaFac.MEDIAART_KOPFTEXT);

		fusstextDto = DelegateFactory
				.getInstance()
				.getAnfrageServiceDelegate()
				.anfragetextFindByMandantLocaleCNr(localeCNr,
						MediaFac.MEDIAART_FUSSTEXT);
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_ANFRAGE;
	}

	public void dto2Components() throws Throwable {

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

		// Kopftext fuer diese Anfrage anzeigen
		if (tpAnfrage.getAnfrageDto().getXKopftextuebersteuert() != null) {
			wefKopftext.setText(tpAnfrage.getAnfrageDto()
					.getXKopftextuebersteuert());
		} else {
			if (tpAnfrage.getAnfrageDto().getArtCNr()
					.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
				wefKopftext.setText(DelegateFactory
						.getInstance()
						.getAnfrageServiceDelegate()
						.anfragetextFindByMandantLocaleCNr(
								tpAnfrage.getLieferantDto().getPartnerDto()
										.getLocaleCNrKommunikation(),
								MediaFac.MEDIAART_KOPFTEXT).getXTextinhalt());
			} else {
				wefKopftext.setText(DelegateFactory
						.getInstance()
						.getAnfrageServiceDelegate()
						.anfragetextFindByMandantLocaleCNr(
								LPMain.getTheClient().getLocUiAsString(),
								MediaFac.MEDIAART_KOPFTEXT).getXTextinhalt());
			}
		}

		// Fusstext fuer diese Anfrage in der Sprache des Lieferante anzeigen
		if (tpAnfrage.getAnfrageDto().getXFusstextuebersteuert() != null) {
			wefFusstext.setText(tpAnfrage.getAnfrageDto()
					.getXFusstextuebersteuert());
		} else {
			if (tpAnfrage.getAnfrageDto().getArtCNr()
					.equals(AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
				wefFusstext.setText(DelegateFactory
						.getInstance()
						.getAnfrageServiceDelegate()
						.anfragetextFindByMandantLocaleCNr(
								tpAnfrage.getLieferantDto().getPartnerDto()
										.getLocaleCNrKommunikation(),
								MediaFac.MEDIAART_FUSSTEXT).getXTextinhalt());
			} else {
				wefFusstext.setText(DelegateFactory
						.getInstance()
						.getAnfrageServiceDelegate()
						.anfragetextFindByMandantLocaleCNr(
								LPMain.getTheClient().getLocUiAsString(),
								MediaFac.MEDIAART_FUSSTEXT).getXTextinhalt());
			}
		}

		aktualisiereStatusbar();
	}

	private void components2Dto() throws Throwable {

		tpAnfrage.getAnfrageDto().setBelegtextIIdKopftext(kopftextDto.getIId());

		if (wefKopftext.getText() != null
				&& wefKopftext.getText().equals(kopftextDto.getXTextinhalt())) {
			tpAnfrage.getAnfrageDto().setXKopftextuebersteuert(null);
		} else {
			tpAnfrage.getAnfrageDto().setXKopftextuebersteuert(
					wefKopftext.getText());
		}

		tpAnfrage.getAnfrageDto().setBelegtextIIdFusstext(fusstextDto.getIId());

		// wenn der Fusstext nicht ueberschrieben wurde -> null setzen
		if (wefFusstext.getText() != null
				&& wefFusstext.getText().equals(fusstextDto.getXTextinhalt())) {
			tpAnfrage.getAnfrageDto().setXFusstextuebersteuert(null);
		} else {
			tpAnfrage.getAnfrageDto().setXFusstextuebersteuert(
					wefFusstext.getText());
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			boolean bUpdate = true;

			if (tpAnfrage.getAnfrageDto().getStatusCNr()
					.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
				if (DialogFactory
						.showMeldung(
								LPMain.getTextRespectUISPr("lp.hint.offennachangelegt"),
								LPMain.getTextRespectUISPr("lp.hint"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {
					bUpdate = false;
				}
			}

			if (bUpdate == true) {
				components2Dto();
				DelegateFactory.getInstance().getAnfrageDelegate()
						.updateAnfrageKonditionen(tpAnfrage.getAnfrageDto());

			}
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

		tpAnfrage.getAnfrageKonditionen().updateButtons(
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
							.equals(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)
					|| (tpAnfrage.getAnfrageDto().getStatusCNr()
							.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN) && tpAnfrage
							.getAnfrageDto().getCAngebotnummer() != null)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}

		}

		return lockStateValue;
	}
}
