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
package com.lp.client.artikel;

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
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
public class PanelArtikellagerplaetze extends PanelBasis {

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
	private InternalFrameArtikel internalFrameArtikel = null;
	private WrapperButton wbuLagerplatz = new WrapperButton();
	private WrapperTextField wtfLagerplatz = new WrapperTextField();

	private WrapperLabel wlaPaternoster = new WrapperLabel();
	private WrapperNumberField wnfPaternoster = new WrapperNumberField();

	private ArtikellagerplaetzeDto artikellagerplaetzeDto = null;
	private PanelQueryFLR panelQueryFLRLagerplatz = null;
	static final public String ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE = "action_lagerplatz_from_liste";

	public PanelArtikellagerplaetze(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfLagerplatz;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		artikellagerplaetzeDto = new ArtikellagerplaetzeDto();
		artikellagerplaetzeDto.setLagerplatzDto(new LagerplatzDto());

	}

	protected void dto2Components() throws Throwable {

		LagerplatzDto lagerplatzDto = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerplatzFindByPrimaryKey(
						artikellagerplaetzeDto.getLagerplatzIId());

		wtfLagerplatz.setText(lagerplatzDto.getCLagerplatz());
		wnfPaternoster.setBigDecimal(artikellagerplaetzeDto
				.getNLagerstandPaternoster());
	}

	protected void components2Dto() {
		artikellagerplaetzeDto.setArtikelIId(internalFrameArtikel
				.getArtikelDto().getIId());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLagerplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				LagerplatzDto lagerplatzDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerplatzFindByPrimaryKey((Integer) key);
				wtfLagerplatz.setText(lagerplatzDto.getCLagerplatz());
				artikellagerplaetzeDto.setLagerplatzIId(lagerplatzDto.getIId());

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_WARNUNG_WENN_LAGERPLATZ_BELEGT,
								ParameterFac.KATEGORIE_ARTIKEL,
								LPMain.getTheClient().getMandant());
				if ((Boolean) parameter.getCWertAsObject() == true) {
					String lagerplatzBereitsBelegt = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.istLagerplatzBereitsDurchAnderenArtikelBelegt(
									internalFrameArtikel.getArtikelDto()
											.getIId(), (Integer) key);

					if (lagerplatzBereitsBelegt != null) {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
						"lp.info"), LPMain.getInstance().getTextRespectUISPr(
						"artikel.lagerplaetze.warnung")+ " "+lagerplatzBereitsBelegt);
					}

				}

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

		wbuLagerplatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.lagerplatz"));
		wlaPaternoster.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.paternosterlagerstand"));
		wnfPaternoster.setActivatable(false);
		wbuLagerplatz
				.setActionCommand(this.ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE);
		wbuLagerplatz.addActionListener(this);
		wtfLagerplatz.setActivatable(false);

		wtfLagerplatz.setColumnsMax(ArtikelFac.MAX_LAGERPLATZ_NAME);
		wtfLagerplatz.setMandatoryField(true);
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
		jpaWorkingOn.add(wbuLagerplatz, new GridBagConstraints(0, 0, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLagerplatz, new GridBagConstraints(1, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaPaternoster, new GridBagConstraints(0, 1, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPaternoster, new GridBagConstraints(1, 1, 1, 1,
				0.1, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 20, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getLagerDelegate()
				.removeArtikellagerplaetze(artikellagerplaetzeDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void dialogQueryLagerplatzFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLagerplatz = ArtikelFilterFactory.getInstance()
				.createPanelFLRLagerplatz(getInternalFrame(), null, false);

		new DialogQuery(panelQueryFLRLagerplatz);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE)) {
			dialogQueryLagerplatzFromListe(e);
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
			artikellagerplaetzeDto = DelegateFactory.getInstance()
					.getLagerDelegate()
					.artikellagerplaetzeFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (artikellagerplaetzeDto.getIId() == null) {
				artikellagerplaetzeDto.setArtikelIId(internalFrameArtikel
						.getArtikelDto().getIId());
				artikellagerplaetzeDto.setIId(DelegateFactory.getInstance()
						.getLagerDelegate()
						.createArtikellagerplaetze(artikellagerplaetzeDto));
				setKeyWhenDetailPanel(artikellagerplaetzeDto.getIId());
			} else {
				DelegateFactory.getInstance().getLagerDelegate()
						.updateArtikellagerplaetze(artikellagerplaetzeDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getArtikelDto().getIId()
								.toString());
			}
			eventYouAreSelected(false);
		}
	}
}
