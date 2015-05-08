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

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.NachrichtartDto;
import com.lp.server.benutzer.service.ThemaDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelNachrichtart extends PanelBasis {

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
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private NachrichtartDto nachrichtartDto = null;
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperButton wbuThema = new WrapperButton();
	private WrapperTextField wtfThema = new WrapperTextField();
	private WrapperCheckBox wcbPopup = new WrapperCheckBox();
	private WrapperCheckBox wcbArchivieren = new WrapperCheckBox();

	private PanelQueryFLR panelQueryFLRThema = null;

	static final public String ACTION_SPECIAL_THEMA_FROM_LISTE = "action_thema_from_liste";

	public PanelNachrichtart(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		nachrichtartDto = new NachrichtartDto();
		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_THEMA_FROM_LISTE)) {
			dialogQueryThemaFromListe(e);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getBenutzerDelegate().removeNachrichtart(
				nachrichtartDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		nachrichtartDto.setBArchivieren(wcbArchivieren.getShort());
		nachrichtartDto.setBPopup(wcbPopup.getShort());

		nachrichtartDto.setCNr(wtfKennung.getText().trim());

		nachrichtartDto.setCBez(wtfBezeichnung.getText());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wcbArchivieren.setShort(nachrichtartDto.getBArchivieren());
		wcbPopup.setShort(nachrichtartDto.getBPopup());
		wtfKennung.setText(nachrichtartDto.getCNr());
		wtfBezeichnung.setText(nachrichtartDto.getCBez());
		ThemaDto themaDto = DelegateFactory.getInstance().getBenutzerDelegate()
				.themaFindByPrimaryKey(nachrichtartDto.getThemaCNr());
		wtfThema.setText(themaDto.getBezeichnung());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (nachrichtartDto.getIId() == null) {
				nachrichtartDto.setIId(DelegateFactory.getInstance()
						.getBenutzerDelegate().createNachrichtart(nachrichtartDto));
				setKeyWhenDetailPanel(nachrichtartDto.getIId());
			} else {
				DelegateFactory.getInstance().getBenutzerDelegate()
						.updateNachrichtart(nachrichtartDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame()
						.setKeyWasForLockMe(nachrichtartDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	void dialogQueryThemaFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRThema = BenutzerFilterFactory.getInstance()
				.createPanelFLRThema(getInternalFrame(),
						nachrichtartDto.getThemaCNr(), false);

		new DialogQuery(panelQueryFLRThema);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRThema) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				ThemaDto themaDto = DelegateFactory.getInstance()
						.getBenutzerDelegate().themaFindByPrimaryKey(key + "");
				wtfThema.setText(themaDto.getBezeichnung());
				nachrichtartDto.setThemaCNr(themaDto.getCNr());
			}

		}
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

		wtfKennung.setColumnsMax(BenutzerFac.MAX_BENUTZER_KENNUNG);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);
		wtfKennung.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kennung"));
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wbuThema.setText(LPMain.getInstance().getTextRespectUISPr("ben.thema")
				+ "...");
		wbuThema.setActionCommand(ACTION_SPECIAL_THEMA_FROM_LISTE);
		wbuThema.addActionListener(this);

		wtfThema.setActivatable(false);
		wtfThema.setMandatoryField(true);

		wtfThema.setText("");
		wcbPopup.setText(LPMain.getInstance().getTextRespectUISPr("ben.popup"));
		wcbArchivieren.setText(LPMain.getInstance().getTextRespectUISPr(
				"ben.archivieren"));

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

		iZeile++;

		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1,
				1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuThema, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfThema, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wcbPopup, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbArchivieren, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				 ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_NACHRICHTART;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			nachrichtartDto = DelegateFactory.getInstance().getBenutzerDelegate()
					.nachrichtartFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

	}

}
