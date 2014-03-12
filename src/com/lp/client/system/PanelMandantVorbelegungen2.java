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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperBildField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um die Mandantdaten.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>08.03.05</I></p>
 * 
 * @author $Author: christoph $
 * 
 * @version $Revision: 1.5 $
 */
public class PanelMandantVorbelegungen2 extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = null;
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperBildField wmcBild = new WrapperBildField(getInternalFrame(),
			"");

	private WrapperLabel wlaStueckliste = null;
	private WrapperLabel wlaBild = null;

	private static final String ACTION_SPECIAL_STKLKUNDE = "action_special_kunde";
	private static final String ACTION_SPECIAL_FINANZAMT = "action_special_konto_finanzamt";
	private PanelQueryFLR panelQueryFLRFinanzamt = null;
	private WrapperButton wbuStklKunde = null;
	private WrapperTextField wtfStklKunde = null;
	private PanelQueryFLR panelQueryStklKunde = null;
	private WrapperButton wbuFinanzamt = new WrapperButton();
	private WrapperTextField wtfFinanzamt = new WrapperTextField();
	private WrapperLabel wlaFibu = null;

	public PanelMandantVorbelegungen2(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {
		super(internalFrame, add2TitleI, keyI);
		jbInit();
		initComponents();
	}

	private void initPanel() throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryStklKunde) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(iId);
				wtfStklKunde.setText(kundeDto.getPartnerDto().formatAnrede());

				getMandantDto().setKundeIIdStueckliste(iId);
			} else if (e.getSource() == panelQueryFLRFinanzamt) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate().partnerFindByPrimaryKey(iId);
				getMandantDto().setPartnerIIdFinanzamt(iId);
				wtfFinanzamt.setText(partnerDto.formatFixTitelName1Name2());

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryStklKunde) {
				wtfStklKunde.setText(null);
				getMandantDto().setKundeIIdStueckliste(null);
			} else if (e.getSource() == panelQueryFLRFinanzamt) {
				wtfFinanzamt.setText(null);
				getMandantDto().setPartnerIIdFinanzamt(null);
			}
		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();

		wbuFinanzamt.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.finanzamt"));
		wbuFinanzamt.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.finanzamt.tooltip"));
		wbuFinanzamt.addActionListener(this);
		wbuFinanzamt.setActionCommand(ACTION_SPECIAL_FINANZAMT);

		wlaFibu = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"finanz.modulname"));

		wtfFinanzamt.setActivatable(false);

		add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// jetzt meine Felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		wbuStklKunde = new WrapperButton();
		wbuStklKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wbuStklKunde.setActionCommand(ACTION_SPECIAL_STKLKUNDE);
		wbuStklKunde.addActionListener(this);

		wtfStklKunde = new WrapperTextField();
		wtfStklKunde.setActivatable(false);

		wlaStueckliste = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("stkl.stueckliste"));
		wlaBild = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant.bild"));
		wlaBild.setHorizontalAlignment(SwingConstants.LEFT);
		// ab hier einhaengen.

		// Zeile.

		iZeile++;
		jpaWorkingOn.add(wlaStueckliste, new GridBagConstraints(0, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuStklKunde, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfStklKunde, new GridBagConstraints(1, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaBild, new GridBagConstraints(0, iZeile, 2, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wmcBild, new GridBagConstraints(0, iZeile, 2, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 5));
		iZeile++;
		jpaWorkingOn.add(wlaFibu, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuFinanzamt, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wtfFinanzamt, new GridBagConstraints(1, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
	}

	protected void dto2Components() throws Throwable {
		wmcBild.setImage(getMandantDto().getPartnerDto().getOBild());
		if (getMandantDto().getKundeIIdStueckliste() != null) {
			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							getMandantDto().getKundeIIdStueckliste());
			wtfStklKunde.setText(kundeDto.getPartnerDto().formatAnrede());

		}
		if (getMandantDto().getPartnerIIdFinanzamt() != null) {
			// Partner holen
			PartnerDto partnerDto = DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(
							getMandantDto().getPartnerIIdFinanzamt());
			wtfFinanzamt.setText(partnerDto.formatFixTitelName1Name2());
		} else {
			wtfFinanzamt.setText(null);
		}

	}

	protected void components2Dto() throws Throwable {
		getMandantDto().getPartnerDto().setOBild(
				Helper.imageToByteArray((BufferedImage) wmcBild.getImage()));
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);

		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance()
				.getTheClient().getMandant(), getMandantDto().getCNr());

		if (!bNeedNoYouAreSelectedI) {

			String cNr = getMandantDto().getCNr();

			if (cNr == null) {
				throw new Exception("key == null");
			}

			getInternalFrameSystem().setMandantDto(
					DelegateFactory.getInstance().getMandantDelegate()
							.mandantFindByPrimaryKey(cNr));

			initPanel();

			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameSystem().getMandantDto().getCNr());

			setStatusbar();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getMandantDelegate()
					.updateMandant(getMandantDto());

			super.eventActionSave(e, true);

			eventYouAreSelected(false);

			dto2Components();
		}

	}

	void dialogQueryFinanzamt(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		// Filter nach Mandant
		FilterKriterium[] filters = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		panelQueryFLRFinanzamt = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZAMT, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"finanz.liste.finanzaemter"));
		if (getMandantDto().getPartnerIIdFinanzamt() != null) {
			panelQueryFLRFinanzamt.setSelectedId(getMandantDto()
					.getPartnerIIdFinanzamt());
		}
		new DialogQuery(panelQueryFLRFinanzamt);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_STKLKUNDE)) {
			panelQueryStklKunde = PartnerFilterFactory.getInstance()
					.createPanelFLRKunde(getInternalFrame(), true, true,
							getMandantDto().getLieferartIIdKunde());
			new DialogQuery(panelQueryStklKunde);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FINANZAMT)) {
			dialogQueryFinanzamt(e);
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MANDANT;
	}

	protected void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getMandantDto().getIAnlegen());
		setStatusbarPersonalIIdAendern(getMandantDto().getIAendern());
		setStatusbarTAendern(getMandantDto().getTAendern());
		setStatusbarTAnlegen(getMandantDto().getTAnlegen());
	}

	protected MandantDto getMandantDto() {
		return getInternalFrameSystem().getMandantDto();
	}

	private InternalFrameSystem getInternalFrameSystem() {
		return (InternalFrameSystem) getInternalFrame();
	}

	protected void eventActionRefresh(ActionEvent aE, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(aE, bNeedNoRefreshI);
		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance()
				.getTheClient().getMandant(), getMandantDto().getCNr());
	}

	/**
	 * 
	 * @param loggedinMandant
	 *            String
	 * @param selectedMandant
	 *            String
	 * @throws Throwable
	 */
	private void checkMandantLoggedInEqualsMandantSelected(
			String loggedinMandant, String selectedMandant) throws Throwable {

		if (!loggedinMandant.equals(selectedMandant)) {

			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
					PanelBasis.ACTION_UPDATE);

			item.getButton().setEnabled(false);
			getPanelStatusbar().setLockField(
					LPMain.getInstance().getTextRespectUISPr(
							"system.nurleserecht"));
		}
	}

}
