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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;

@SuppressWarnings("static-access")
/**
 *
 * <p> Diese Klasse kuemmert sich die Partnerkommunikationen.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; dd.mm.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/08/29 14:29:29 $
 */
public class PanelPartnerKommunikation extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPanePartner tpPartner = null;

	private JPanel jpaWorking = null;
	private GridBagLayout gblWorkingPanel = null;
	private Border border = null;
	private GridBagLayout gblAussen = null;

	private WrapperLabel wlaKommunikationsart = null;
	private WrapperComboBox wcoKommunikationsart = null;
	private WrapperLabel wlaInhalt = null;
	private WrapperTextField wtfBezeichnung = null;
	private WrapperLabel wlaBezeichnung = null;
	private WrapperTextField wtfInhalt = null;
	private Map<?, ?> tmKommunikationsarten = null;

	// private WrapperCheckBox wcbGehoertZuMandant = null;

	public PanelPartnerKommunikation(InternalFrame internalFrame,
			String add2TitleI, Object keyI, TabbedPanePartner tpPartner)
			throws Throwable {

		super(internalFrame, add2TitleI, keyI);
		this.tpPartner = tpPartner;
		jbInit();
		initComponents();
		initPanel();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// buttons.
		resetToolsPanel();

		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		// actionpanel von Oberklasse holen und anhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Workingpanel
		jpaWorking = new JPanel();
		gblWorkingPanel = new GridBagLayout();
		jpaWorking.setLayout(gblWorkingPanel);
		add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		wlaKommunikationsart = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.kommunikationsart"));
		wcoKommunikationsart = new WrapperComboBox();
		wcoKommunikationsart.setMandatoryField(true);

		wlaInhalt = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.inhalt"));
		wtfInhalt = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);
		wtfInhalt.setMandatoryField(true);

		wlaBezeichnung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung = new WrapperTextField(
				PartnerFac.MAX_KOMMART_BEZEICHNUNG);
		wtfBezeichnung.setMandatoryField(true);

		// wcbGehoertZuMandant = new WrapperCheckBox(
		// LPMain.getInstance().getTextRespectUISPr("part.komm.mandant")
		// + " "
		// + LPMain.getInstance().getTheClient().getMandant());

		// Ab hier einhaengen.
		// Zeile
		jpaWorking.add(wlaKommunikationsart, new GridBagConstraints(0, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wcoKommunikationsart, new GridBagConstraints(1, iZeile,
				1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorking.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 2, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorking.add(wlaInhalt, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfInhalt, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// Neu.
			leereAlleFelder(this);

			clearStatusbar();

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getTabbedPanePartner().getPartnerDto()
							.formatFixTitelName1Name2());
		} else {
			// Update.
			getTabbedPanePartner()
					.setPartnerkommunikationDto(
							DelegateFactory
									.getInstance()
									.getPartnerDelegate()
									.partnerkommunikationFindByPrimaryKey(
											(Integer) key));

			getInternalFrame()
					.setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							getTabbedPanePartner().getPartnerDto()
									.formatFixTitelName1Name2()
									+ " | "
									+ getTabbedPanePartner()
											.getPartnerkommunikationsDto()
											.getCInhalt());
			dto2Components();
		}
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		wcoKommunikationsart.setKeyOfSelectedItem(getTabbedPanePartner()
				.getPartnerkommunikationsDto().getKommunikationsartCNr());

		wtfInhalt.setText(getTabbedPanePartner().getPartnerkommunikationsDto()
				.getCInhalt());

		wtfBezeichnung.setText(getTabbedPanePartner()
				.getPartnerkommunikationsDto().getCBez());

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
			checkLockedDlg();
			Integer iIdPartnerkommunikation = null;
			components2Dto();
			if (getTabbedPanePartner().getPartnerkommunikationsDto().getIId() == null) {
				// Create.
				iIdPartnerkommunikation = DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.createPartnerkommunikation(
								getTabbedPanePartner()
										.getPartnerkommunikationsDto());
				getTabbedPanePartner().getPartnerkommunikationsDto().setIId(
						iIdPartnerkommunikation);
				setKeyWhenDetailPanel(iIdPartnerkommunikation);
			} else {
				// Update.
				DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.updatePartnerkommunikation(
								getTabbedPanePartner()
										.getPartnerkommunikationsDto());
			}
			super.eventActionSave(e, false);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.removePartnerkommunikation(
							getTabbedPanePartner()
									.getPartnerkommunikationsDto().getIId());

			getTabbedPanePartner().setPartnerkommunikationDto(
					new PartnerkommunikationDto());
			this.setKeyWhenDetailPanel(null);

			super.eventActionDelete(e, false, false);
		}
	}

	private void components2Dto() throws Throwable {

		getTabbedPanePartner().getPartnerkommunikationsDto()
				.setKommunikationsartCNr(
						(String) wcoKommunikationsart.getKeyOfSelectedItem());

		getTabbedPanePartner().getPartnerkommunikationsDto().setCInhalt(
				wtfInhalt.getText());

		getTabbedPanePartner().getPartnerkommunikationsDto().setCBez(
				wtfBezeichnung.getText());

		// String cNrMandant = null;
		// if (Helper.short2boolean(wcbGehoertZuMandant.getShort())) {
		// cNrMandant = LPMain.getInstance().getTheClient().getMandant();
		// }
		// if (getInternalFramePartner().getPartnerkommunikationsDto() == null)
		// {
		// cNrMandant = LPMain.getInstance().getTheClient().getMandant();
		// getInternalFramePartner().getPartnerkommunikationsDto().setCNrMandant(cNrMandant);
		// }
	}

	/**
	 * Fuelle die Helpercontrols, jene aus denen ausgewaehlt werden kann.
	 * 
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {
		tmKommunikationsarten = DelegateFactory
				.getInstance()
				.getPartnerServicesDelegate()
				.getAllKommunikationsArten(
						LPMain.getInstance().getTheClient().getLocUiAsString());
		wcoKommunikationsart.setMap(tmKommunikationsarten);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	private TabbedPanePartner getTabbedPanePartner() {
		return tpPartner;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		if (!bNeedNoNewI) {
			getTabbedPanePartner().setPartnerkommunikationDto(
					new PartnerkommunikationDto());
			setDefaults();
		}
	}

	/**
	 * setDefaults
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		getTabbedPanePartner().getPartnerkommunikationsDto().setPartnerIId(
				getTabbedPanePartner().getPartnerDto().getIId());
		getTabbedPanePartner().getPartnerkommunikationsDto().setCNrMandant(
				LPMain.getInstance().getTheClient().getMandant());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

}
