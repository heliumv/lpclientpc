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
package com.lp.client.finanz;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PanelPartnerDetail;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.MandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um den Partnerteil des Finanzamts</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 11.04.05
 * </p>
 * 
 * @author $Author: adi $
 * @version $Revision: 1.6 $
 */

public class PanelFinanzFinanzamtKopfdaten extends PanelPartnerDetail {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TabbedPaneFinanzamt tabbedPaneFinanzamt;
	private WrapperTextField wtfReferat = new WrapperTextField();
	private WrapperLabel wlaReferat = new WrapperLabel();
	private WrapperTextField wtfSteuerNr = new WrapperTextField();
	private WrapperLabel wlaSteuerNr = new WrapperLabel();

	private WrapperNumberField wtfFormularNr = new WrapperNumberField();
	private WrapperLabel wlaFormularNr = new WrapperLabel();

	private WrapperLabel wlaUmsatzRunden = new WrapperLabel();
	private WrapperCheckBox wcbUmsatzRunden = new WrapperCheckBox();
	
	public PanelFinanzFinanzamtKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object keyI,
			TabbedPaneFinanzamt tabbedPaneFinanzamt) throws Throwable {
		super(internalFrame, add2TitleI, keyI);
		this.tabbedPaneFinanzamt = tabbedPaneFinanzamt;
		jbInitPanel();
		initComponents();
	}

	private TabbedPaneFinanzamt getTabbedPaneFinanzamt() {
		return tabbedPaneFinanzamt;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(true);
		Object key = null;
		if (getTabbedPaneFinanzamt().getFinanzamtDto() != null) {
			key = getTabbedPaneFinanzamt().getFinanzamtDto().getPartnerIId();
		}
		if (key == null) {
			leereAlleFelder(this);
			// setDefaults();
			clearStatusbar();
		} else {
			// bestehender; einlesen; wenn bereits geloescht: exception nach
			// oben.
			getTabbedPaneFinanzamt().setFinanzamtDto(
					DelegateFactory
							.getInstance()
							.getFinanzDelegate()
							.finanzamtFindByPrimaryKey((Integer) key,
									LPMain.getTheClient().getMandant()));

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getTabbedPaneFinanzamt().getFinanzamtDto().getPartnerDto()
							.getCName1nachnamefirmazeile1());
			updateComponentsPanel();
			dto2Components();
			setStatusbar();
		}
	}

	private void jbInitPanel() throws Throwable {
		// buttons.
		resetToolsPanel();
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);
		wtfReferat = new WrapperTextField();
		wlaReferat = new WrapperLabel();
		wtfSteuerNr = new WrapperTextField();
		wlaSteuerNr = new WrapperLabel();
		wlaReferat.setText(LPMain.getTextRespectUISPr("fb.referat"));
		wlaSteuerNr.setText(LPMain.getTextRespectUISPr("fb.steuernummer"));
		wtfSteuerNr.setColumnsMax(FinanzFac.MAX_FINANZAMT_STEUERNUMMER);
		wcoPartnerart.setActivatable(false);
		wtfFormularNr.setFractionDigits(0);
		wlaFormularNr.setText(LPMain.getTextRespectUISPr("fb.formularnummer"));
		wlaUmsatzRunden.setText(LPMain.getTextRespectUISPr("fb.umsatzrunden"));
		
		// ab hier einhaengen.
		iZeile++;
		jpaWorkingOn.add(wlaSteuerNr, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfSteuerNr, new GridBagConstraints(1, iZeile, 2, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaUmsatzRunden, new GridBagConstraints(3, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbUmsatzRunden, new GridBagConstraints(5, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaReferat, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfReferat, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFormularNr, new GridBagConstraints(3, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfFormularNr, new GridBagConstraints(5, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
	}

	protected void components2Dto() throws Throwable {

		super.components2Dto();
		getFinanzamtDto().setCReferat(wtfReferat.getText());
		getFinanzamtDto().setCSteuernummer(wtfSteuerNr.getText());
		getFinanzamtDto().setIFormularnummer(wtfFormularNr.getInteger());
		getFinanzamtDto().setBUmsatzRunden(Helper.boolean2Short(wcbUmsatzRunden.isSelected()));
		getFinanzamtDto().setPartnerDto(getPartnerDto());
		getPartnerDto().setPartnerartCNr(PartnerFac.PARTNERART_SONSTIGES);
	}

	/**
	 * Behandle Ereignis Save.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			FinanzamtDto updatedFA = DelegateFactory.getInstance()
					.getFinanzDelegate().updateFinanzamt(getFinanzamtDto());
			setKeyWhenDetailPanel(updatedFA.getPartnerIId());
			getTabbedPaneFinanzamt().setFinanzamtDto(updatedFA);
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				// der erste eintrag wurde angelegt
				getInternalFrame().setKeyWasForLockMe(
						getKeyWhenDetailPanel() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (getLockedstateDetailMainKey().getIState() != PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			DelegateFactory.getInstance().getFinanzDelegate()
					.removeFinanzamt(getFinanzamtDto());
			getTabbedPaneFinanzamt().setFinanzamtDto(new FinanzamtDto());
			super.eventActionDelete(e, true, true);
		}
	}

	protected void dto2Components() throws Throwable {
		super.dto2Components();

		wtfSteuerNr.setText(getFinanzamtDto().getCSteuernummer());
		wtfReferat.setText(getFinanzamtDto().getCReferat());
		wtfFormularNr.setInteger(getFinanzamtDto().getIFormularnummer());
		wcbUmsatzRunden.setSelected(Helper.short2boolean(getFinanzamtDto().getBUmsatzRunden()));
		setStatusbarModification(getFinanzamtDto()) ;
	}

	/**
	 * eventActionNew
	 * 
	 * @param eventObject
	 *            der event
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, true);

		if (!bNeedNoNewI) {
			getTabbedPaneFinanzamt().setFinanzamtDto(new FinanzamtDto());
			setPartnerDto(new PartnerDto());
			setDefaults();
		}
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorBekannt = true;
		String sMsg = null;
		int code = exfc.getICode();
		switch (code) {
		case EJBExceptionLP.FEHLER_PARTNER_LIEFERART: {
			sMsg = LPMain.getTextRespectUISPr("part.mandant.lieferart_fehlt");
			break;
		}
		case EJBExceptionLP.FEHLER_PARTNER_SPEDITEUR: {
			sMsg = LPMain.getTextRespectUISPr("part.mandant.spediteur_fehlt");
			break;
		}
		case EJBExceptionLP.FEHLER_PARTNER_VKPREISFINDUNGPREISLISTENNAME: {
			sMsg = LPMain
					.getTextRespectUISPr("part.mandant.vkpreislistenname_fehlt");
			break;
		}
		case EJBExceptionLP.FEHLER_PARTNER_ZAHLUNGSZIEL: {
			sMsg = LPMain
					.getTextRespectUISPr("part.mandant.zahlungsziel_fehlt");
			break;
		}
		case EJBExceptionLP.FEHLER_PARTNER_MWST: {
			sMsg = LPMain.getTextRespectUISPr("part.mandant.mwst_fehlt");
			break;
		}
		default:
			bErrorBekannt = false;
			break;
		}
		if (bErrorBekannt) {
			JOptionPane pane = InternalFrame
					.getNarrowOptionPane(com.lp.client.pc.Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
			pane.setMessage(sMsg);
			pane.setMessageType(JOptionPane.ERROR_MESSAGE);
			JDialog dialog = pane.createDialog(this, "");
			dialog.setVisible(true);
		}
		return bErrorBekannt;
	}

	/**
	 * Setze alle Defaultwerte.
	 * 
	 * @throws Throwable
	 */
	protected void setDefaults() throws Throwable {
		getFinanzamtDto().setMandantCNr(LPMain.getTheClient().getMandant());
		defaultFieldIsNullDlg();
		super.setDefaults();
	}

	private void defaultFieldIsNullDlg() throws Throwable {
		MandantDto mandantDto = DelegateFactory.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(getFinanzamtDto().getMandantCNr());
		if (mandantDto.getLieferartIIdKunde() == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARTNER_LIEFERART, null);
		} else if (mandantDto.getVkpfArtikelpreislisteIId() == null) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_PARTNER_VKPREISFINDUNGPREISLISTENNAME,
					null);
		} else if (mandantDto.getSpediteurIIdKunde() == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARTNER_SPEDITEUR, null);
		} else if (mandantDto.getZahlungszielIIdKunde() == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZAHLUNGSZIEL,
					null);
		}
	}

	protected FinanzamtDto getFinanzamtDto() {
		return getTabbedPaneFinanzamt().getFinanzamtDto();
	}

	protected PartnerDto getPartnerDto() {
		return getTabbedPaneFinanzamt().getFinanzamtDto().getPartnerDto();
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		getTabbedPaneFinanzamt().getFinanzamtDto().setPartnerDto(partnerDto);
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_FINANZ_FINANZAMT;
	}

	/**
	 * Update alle Components.
	 * 
	 * @throws Throwable
	 */
	private void updateComponentsPanel() throws Throwable {

		wcoPartnerart.setActivatable(false);

		wlaGebDatumAnsprechpartner.setVisible(false);
		wdfGebDatumAnsprechpartner.setVisible(false);
		wdfGebDatumAnsprechpartner.setDate(null);
	}

}
