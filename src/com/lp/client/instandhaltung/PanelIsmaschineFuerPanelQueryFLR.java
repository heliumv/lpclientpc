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
package com.lp.client.instandhaltung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.instandhaltung.service.AnlageDto;
import com.lp.server.instandhaltung.service.IsmaschineDto;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>18.02.05</I></p>
 *
 * <p> </p>
 *
 * @author josef erlinger
 * @version $Revision: 1.1 $
 */
public class PanelIsmaschineFuerPanelQueryFLR extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IsmaschineDto ismaschineDto = null;

	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private JDialog owner = null;

	InternalFrame internalFrame = null;
	Integer anlageIId = null;
	public PanelIsmaschineFuerPanelQueryFLR(
			InternalFrame internalFrame, String add2TitleI,
			JDialog owner, Integer anlageIId) throws Throwable {
		super(internalFrame, add2TitleI, null);
		this.anlageIId=anlageIId;
		jbInit();
		initComponents();
		this.owner = owner;
		this.internalFrame = internalFrame;
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		setKeyWhenDetailPanel(null);
		owner.setVisible(false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		
		// neue Tabelle

	}

	protected void components2Dto() throws Throwable {
		if (ismaschineDto == null) {
			ismaschineDto = new IsmaschineDto();
		}
		ismaschineDto.setMandantCNr(LPMain.getTheClient().getMandant());
		ismaschineDto.setCBez(wtfBezeichnung.getText());
		ismaschineDto.setAnlageIId(anlageIId);

	}

	protected void dto2Components() throws Throwable {
		wtfBezeichnung.setText(ismaschineDto.getCBez());


	

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			// create.
			Integer iIdLandPlzOrt = DelegateFactory.getInstance()
					.getInstandhaltungDelegate().createIsmaschine(ismaschineDto);

			// diesem panel den key setzen.
			setKeyWhenDetailPanel(iIdLandPlzOrt);
			owner.setVisible(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		String[] aWhichButtonIUse = { ACTION_SAVE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));

		wtfBezeichnung.setColumnsMax(SystemFac.MAX_PLZ);
		wtfBezeichnung.setMandatoryFieldDB(true);

		

	
		// jetzt meine felder
		jPanelWorkingOn = new JPanel(new GridBagLayout());
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 1, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LANDPLZORT;
	}

	private void setDefaults() throws Throwable {

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			// vorbesetzen
			setDefaults();
		} else {
			ismaschineDto = DelegateFactory.getInstance()
					.getInstandhaltungDelegate()
					.ismaschineFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	public IsmaschineDto getIsmaschineDto() {
		return ismaschineDto;
	}

	protected JComponent getFirstFocusableComponent() {
		return wtfBezeichnung;
	}

	public void setIsmaschineDto(IsmaschineDto ismaschineDto) {
		this.ismaschineDto = ismaschineDto;
	}

}
