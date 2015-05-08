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
package com.lp.client.benutzer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RollerechtDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelRollerecht extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameBenutzer internalFrameBenutzer = null;
	private WrapperButton wbuRecht = new WrapperButton();
	private RollerechtDto rollerechtDto = null;
	private WrapperTextField wtfRecht = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRRechte = null;

	static final public String ACTION_SPECIAL_RECHT_FROM_LISTE = "action_recht_from_liste";

	public PanelRollerecht(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameBenutzer = (InternalFrameBenutzer) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuRecht;
	}

	protected void setDefaults() {

	}

	void dialogQueryRechtFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(
				"c_nr",
				true,
				"(select rollerecht.flrrecht.c_nr from FLRRollerecht as rollerecht WHERE rollerecht.flrsystemrolle.i_id="
						+ internalFrameBenutzer.getSystemrolleDto().getIId()
						+ " )", FilterKriterium.OPERATOR_NOT_IN, false);

		panelQueryFLRRechte = new PanelQueryFLR(null, kriterien,
				QueryParameters.UC_ID_RECHT, aWhichButtonIUse,
				internalFrameBenutzer, LPMain.getInstance()
						.getTextRespectUISPr("title.rechtauswahlliste"));

		panelQueryFLRRechte.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("c_nr", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.rechte"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT), null);

		panelQueryFLRRechte.setSelectedId(rollerechtDto.getRechtCNr());

		new DialogQuery(panelQueryFLRRechte);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		rollerechtDto = new RollerechtDto();
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wtfRecht.setText(rollerechtDto.getRechtCNr().trim());

		this.setStatusbarPersonalIIdAendern(rollerechtDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(rollerechtDto.getTAendern());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			rollerechtDto = DelegateFactory.getInstance().getRechteDelegate()
					.rollerechtFindByPrimaryKey((Integer) key);
			dto2Components();

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRRechte) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				wtfRecht.setText(((String) key).trim());
				rollerechtDto.setRechtCNr((String) key);
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_RECHT_FROM_LISTE)) {
			dialogQueryRechtFromListe(e);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (rollerechtDto.getIId() == null) {
				rollerechtDto.setSystemrolleIId(internalFrameBenutzer
						.getSystemrolleDto().getIId());
				rollerechtDto.setIId(DelegateFactory.getInstance()
						.getRechteDelegate().createRollerecht(rollerechtDto));
				setKeyWhenDetailPanel(rollerechtDto.getIId());
			} else {
				DelegateFactory.getInstance().getRechteDelegate()
						.updateRollerecht(rollerechtDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameBenutzer.getSystemrolleDto().getIId()
								.toString());
			}
			eventYouAreSelected(false);
		}
	}

	protected void components2Dto() {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		RollerechtDto dto = new RollerechtDto();
		dto.setIId((Integer) getKeyWhenDetailPanel());
		DelegateFactory.getInstance().getRechteDelegate().removeRollerecht(dto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
		rollerechtDto = new RollerechtDto();
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wbuRecht.setText(LPMain.getInstance().getTextRespectUISPr("lp.rechte")
				+ "...");
		wbuRecht
				.setActionCommand(PanelRollerecht.ACTION_SPECIAL_RECHT_FROM_LISTE);
		wbuRecht.addActionListener(this);

		getInternalFrame().addItemChangedListener(this);

		wtfRecht.setMandatoryField(true);
		wtfRecht.setActivatable(false);
		wtfRecht.setColumnsMax(100);
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
		jpaWorkingOn.add(wbuRecht, new GridBagConstraints(0, 0, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfRecht, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SYSTEMROLLE;
	}

}
