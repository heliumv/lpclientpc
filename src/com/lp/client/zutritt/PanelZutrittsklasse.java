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
package com.lp.client.zutritt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZutrittsklasseDto;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
public class PanelZutrittsklasse extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private InternalFrameZutritt internalFrameZutritt = null;

	private ZutrittsklasseDto zutrittsklasseDto = null;

	public PanelZutrittsklasse(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameZutritt = (InternalFrameZutritt) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		zutrittsklasseDto = new ZutrittsklasseDto();

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getZutrittDelegate()
				.removeZutrittsklasse(zutrittsklasseDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		zutrittsklasseDto.setCBez(wtfBezeichnung.getText());
		zutrittsklasseDto.setCNr(wtfKennung.getText());

	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(zutrittsklasseDto.getCNr());
		wtfBezeichnung.setText(zutrittsklasseDto.getCBez());

		// MandantDto mandantDto = DelegateFactory.getInstance()
		// .getMandantDelegate()
		// .mandantFindByPrimaryKey(zutrittsklasseDto.getMandantCNr());
		// String kBez = "";
		// if (mandantDto.getCKbez() != null) {
		// kBez = "- " + mandantDto.getCKbez();
		// }
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			try {

				if (zutrittsklasseDto.getIId() == null) {
					zutrittsklasseDto.setCBez(wtfBezeichnung.getText());
					zutrittsklasseDto.setIId(DelegateFactory.getInstance()
							.getZutrittDelegate()
							.createZutrittsklasse(zutrittsklasseDto));
					setKeyWhenDetailPanel(zutrittsklasseDto.getIId());
					internalFrameZutritt
							.setZutrittsklasseDto(zutrittsklasseDto);
				} else {
					DelegateFactory.getInstance().getZutrittDelegate()
							.updateZutrittsklasse(zutrittsklasseDto);
				}
				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							zutrittsklasseDto.getIId() + "");
				}
				eventYouAreSelected(false);
			} catch (ExceptionLP ex) {
				if (ex.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
					String msg = "Die Kennung dieser Zutrittsklasse wird bereits (von einem anderen Mandanten) verwendet. Bitte verwenden Sie eine andere Kennung.";
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.error"), msg);

				} else {
					handleException(ex, true);
				}
			}
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		eI = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kennung"));
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfKennung.setMandatoryField(true);
		wtfKennung.setColumnsMax(3);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, 0, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, 0, 1, 1, 0.5,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 1, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZUTRITTSKLASSE;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();

			zutrittsklasseDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
		} else {
			zutrittsklasseDto = DelegateFactory.getInstance()
					.getZutrittDelegate()
					.zutrittsklasseFindByPrimaryKey((Integer) key);

			dto2Components();
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					internalFrameZutritt.getZutrittsklasseDto()
							.getBezeichnung());
		}
	}

}
