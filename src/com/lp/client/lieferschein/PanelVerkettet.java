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
package com.lp.client.lieferschein;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

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
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.VerkettetDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
public class PanelVerkettet extends PanelBasis {

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
	private InternalFrameLieferschein internalFrameLieferschein = null;
	private WrapperButton wbuLieferschein = new WrapperButton();
	private WrapperTextField wtfLieferschein = new WrapperTextField();

	private VerkettetDto verkettetDto = null;
	private PanelQueryFLR panelQueryFLRLieferschein = null;
	static final public String ACTION_SPECIAL_LIEFERSCHEIN_FROM_LISTE = "action_lagerplatz_from_liste";

	public PanelVerkettet(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameLieferschein = (InternalFrameLieferschein) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLieferschein;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		verkettetDto = new VerkettetDto();

	}

	protected void dto2Components() throws Throwable {

		LieferscheinDto lsDto = DelegateFactory
				.getInstance()
				.getLsDelegate()
				.lieferscheinFindByPrimaryKey(
						verkettetDto.getLieferscheinIIdVerkettet());

		wtfLieferschein.setText(lsDto.getCNr());

	}

	protected void components2Dto() {
		verkettetDto.setLieferscheinIId(internalFrameLieferschein
				.getTabbedPaneLieferschein().getLieferscheinDto().getIId());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferschein) {

				Object key = panelQueryFLRLieferschein.getSelectedId();

				LieferscheinDto lieferscheinDto = DelegateFactory.getInstance()
						.getLsDelegate()
						.lieferscheinFindByPrimaryKey((Integer) key);
				wtfLieferschein.setText(lieferscheinDto.getCNr());
				verkettetDto.setLieferscheinIIdVerkettet(lieferscheinDto
						.getIId());

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

		wbuLieferschein.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferschein"));

		wbuLieferschein
				.setActionCommand(this.ACTION_SPECIAL_LIEFERSCHEIN_FROM_LISTE);
		wbuLieferschein.addActionListener(this);
		wtfLieferschein.setActivatable(false);

		wtfLieferschein.setColumnsMax(ArtikelFac.MAX_LAGERPLATZ_NAME);
		wtfLieferschein.setMandatoryField(true);
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
		jpaWorkingOn.add(wbuLieferschein, new GridBagConstraints(0, 0, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferschein, new GridBagConstraints(1, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE ,ACTION_SAVE, ACTION_DELETE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getLieferscheinServiceDelegate()
				.removeVerkettet(verkettetDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void dialogQueryLieferscheinFromListe(ActionEvent e)
			throws Throwable {

		FilterKriterium[] filters = null;

		filters = new FilterKriterium[6];
		filters[0] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_MANDANT_C_NR, true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		filters[1] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_LIEFERADRESSE,
				true, internalFrameLieferschein.getTabbedPaneLieferschein()
						.getLieferscheinDto().getKundeIIdLieferadresse()
						+ "", FilterKriterium.OPERATOR_EQUAL, false);
		filters[2] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_RECHNUNGSADRESSE,
				true, internalFrameLieferschein.getTabbedPaneLieferschein()
						.getLieferscheinDto().getKundeIIdRechnungsadresse()
						+ "", FilterKriterium.OPERATOR_EQUAL, false);
		filters[3] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_WAEHRUNG_C_NR_LIEFERSCHEINWAEHRUNG,
				true, "'"
						+ internalFrameLieferschein.getTabbedPaneLieferschein()
								.getLieferscheinDto().getWaehrungCNr() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		filters[4] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_I_ID,
				true, internalFrameLieferschein.getTabbedPaneLieferschein()
						.getLieferscheinDto().getIId()
						+ "", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		// Bereits verkettete nicht mehr anzeigen

		filters[5] = new FilterKriterium(
				LieferscheinServiceFac.LS_HANDLER_OHNE_VERKETTETE, true, "",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		String sTitle = LPMain.getTextRespectUISPr("ls.title.tooltip.auswahl");

		panelQueryFLRLieferschein = LieferscheinFilterFactory.getInstance()
				.createPanelQueryFLRLieferschein(getInternalFrame(), filters,
						sTitle, null);

		new DialogQuery(panelQueryFLRLieferschein);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERSCHEIN_FROM_LISTE)) {
			dialogQueryLieferscheinFromListe(e);
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			verkettetDto = DelegateFactory.getInstance()
					.getLieferscheinServiceDelegate()
					.verkettetFindByPrimaryKey((Integer) key);
			dto2Components();
		}

		internalFrameLieferschein.getTabbedPaneLieferschein()
				.setTitleLieferschein(
						LPMain.getInstance()
								.getTextRespectUISPr("ls.verkettet"));

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (verkettetDto.getIId() == null) {

				verkettetDto.setIId(DelegateFactory.getInstance()
						.getLieferscheinServiceDelegate()
						.createVerkettet(verkettetDto));
				setKeyWhenDetailPanel(verkettetDto.getIId());
			} else {
				DelegateFactory.getInstance().getLieferscheinServiceDelegate()
						.updateVerkettet(verkettetDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameLieferschein.getTabbedPaneLieferschein()
								.getLieferscheinDto().getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}
}
