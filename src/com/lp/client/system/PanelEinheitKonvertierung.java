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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.EinheitKonvertierungDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich ...</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>18.02.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author josef erlinger
 * @version $Revision: 1.5 $
 */
public class PanelEinheitKonvertierung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperLabel wla1 = null;
	private WrapperComboBox wcoEinheitVon = null;

	private WrapperComboBox wcoEinheitZu = null;

	private WrapperLabel wlaIst = null;
	private WrapperNumberField wnfFaktor = null;

	public PanelEinheitKonvertierung(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	private InternalFrameSystem getInternalFrameSystem() {
		return (InternalFrameSystem) getInternalFrame();
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		// hier wird Id null gesetzt fuer update
		getInternalFrameSystem().getEinheitKonvertierungDto().setIId(null);
		leereAlleFelder(this);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getSystemDelegate()
				.removeEinheitKonvertierung(
						getInternalFrameSystem().getEinheitKonvertierungDto());
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		// nothing here.
	}

	protected void components2Dto() throws Throwable {
		getInternalFrameSystem().getEinheitKonvertierungDto().setEinheitCNrVon(
				(String) wcoEinheitVon.getKeyOfSelectedItem());
		getInternalFrameSystem().getEinheitKonvertierungDto().setEinheitCNrZu(
				(String) wcoEinheitZu.getKeyOfSelectedItem());

		getInternalFrameSystem().getEinheitKonvertierungDto().setNFaktor(
				wnfFaktor.getBigDecimal());
	}

	protected void dto2Components() throws ExceptionLP {
		wcoEinheitVon.setKeyOfSelectedItem(getInternalFrameSystem()
				.getEinheitKonvertierungDto().getEinheitCNrVon());

		wcoEinheitZu.setKeyOfSelectedItem(getInternalFrameSystem()
				.getEinheitKonvertierungDto().getEinheitCNrZu());

		wnfFaktor.setBigDecimal(getInternalFrameSystem()
				.getEinheitKonvertierungDto().getNFaktor());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			Integer iIdEinheitenKonvert = null;
			try {

				components2Dto();

				if (wnfFaktor.getBigDecimal().doubleValue() == 0) {
					DialogFactory.showModalDialog(LPMain
							.getTextRespectUISPr("lp.hint"), LPMain
							.getTextRespectUISPr("system.faktor.null"));
					return;
				}

				if (getInternalFrameSystem().getEinheitKonvertierungDto()
						.getIId() == null) {

					EinheitDto einheitVon = DelegateFactory.getInstance()
							.getSystemDelegate().einheitFindByPrimaryKey(
									(String) wcoEinheitVon
											.getKeyOfSelectedItem());
					EinheitDto einheitZu = DelegateFactory.getInstance()
							.getSystemDelegate().einheitFindByPrimaryKey(
									(String) wcoEinheitZu
											.getKeyOfSelectedItem());

					if (!einheitVon.getIDimension().equals(
							einheitZu.getIDimension())) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hint"),
										LPMain
												.getTextRespectUISPr("lp.einheitkonvertierunggleichedimension"));
					} else {
						// create.
						iIdEinheitenKonvert = DelegateFactory.getInstance()
								.getSystemDelegate()
								.createEinheitKonvertierung(
										getInternalFrameSystem()
												.getEinheitKonvertierungDto());

						// dem dto den key setzen.
						DelegateFactory.getInstance().getSystemDelegate()
								.einheitKonvertierungFindByPrimaryKey(
										iIdEinheitenKonvert);

						// diesem panel den key setzen.
						setKeyWhenDetailPanel(iIdEinheitenKonvert);
					}
				} else {
					// update
					DelegateFactory.getInstance().getSystemDelegate()
							.updateEinheitKonvertierung(
									getInternalFrameSystem()
											.getEinheitKonvertierungDto());

				}

				super.eventActionSave(e, true);

				eventYouAreSelected(false);

			} catch (ExceptionLP t) {
				if (t.getICode() == EJBExceptionLP.FEHLER_SYSTEM_EINHEITKONVERTIERUNG_SCHON_VORHANDEN) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain
											.getTextRespectUISPr("lp.einheitkonvertierungschonangelegt"));
				} else if (t.getICode() == EJBExceptionLP.FEHLER_SYSTEM_EINHEITKONVERTIERUNG_GLEICHE_EINHEITEN) {
					DialogFactory.showModalDialog(LPMain
							.getTextRespectUISPr("lp.hint"), LPMain
							.getTextRespectUISPr("lp.einheitensinddieselben"));
				}

				return;
			} catch (Throwable t) {
				handleException(t, true);
			}
		}
	}

	protected void eventItemchanged(ItemChangedEvent eI) throws Throwable {
		// nothing here.
	}

	protected void setDefaults() throws Throwable {
		Map<?, ?> mEinheit = DelegateFactory.getInstance().getSystemDelegate()
				.getAllEinheiten();
		wcoEinheitVon.setMap(mEinheit);
		wcoEinheitZu.setMap(mEinheit);
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		wla1 = new WrapperLabel();
		wcoEinheitVon = new WrapperComboBox();

		wcoEinheitZu = new WrapperComboBox();

		wlaIst = new WrapperLabel("=");
		wnfFaktor = new WrapperNumberField();
		wnfFaktor.setMandatoryField(true);

		wla1.setText("1");
		wcoEinheitVon.setMandatoryField(true);

		wcoEinheitZu.setMandatoryField(true);

		// jetzt meine felder
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wla1, new GridBagConstraints(0, 0, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 10, 0));
		jPanelWorkingOn.add(wcoEinheitVon, new GridBagConstraints(1, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaIst, new GridBagConstraints(2, 0, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 10, 0));
		jPanelWorkingOn.add(wnfFaktor, new GridBagConstraints(3, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcoEinheitZu, new GridBagConstraints(4, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINHEITKONVERTIERUNG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
		} else {
			getInternalFrameSystem()
					.setEinheitKonvertierungDto(
							DelegateFactory.getInstance().getSystemDelegate()
									.einheitKonvertierungFindByPrimaryKey(
											(Integer) key));
			setStatusbar();
			dto2Components();
		}
	}

	protected void setStatusbar() throws Throwable {
		setStatusbarTAendern(getInternalFrameSystem()
				.getEinheitKonvertierungDto().getTAendern());
		setStatusbarTAnlegen(getInternalFrameSystem()
				.getEinheitKonvertierungDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(getInternalFrameSystem()
				.getEinheitKonvertierungDto().getPersonalIIdAendern());
		setStatusbarPersonalIIdAnlegen(getInternalFrameSystem()
				.getEinheitKonvertierungDto().getPersonalIIdAnlegen());
	}

}
