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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.MeilensteinDto;
import com.lp.server.auftrag.service.ZahlungsplanmeilensteinDto;
import com.lp.server.auftrag.service.ZeitplanDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Fenster werden Auftragteilnehmer erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-09-29</p>
 * <p> </p>
 * @author uli walch
 * @version $Revision: 1.4 $
 */
public class PanelZahlungsplanmeilenstein extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;

	private ZahlungsplanmeilensteinDto zahlungsplanmeilensteinDto = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	static final public String ACTION_SPECIAL_MEILENSTEIN = "action_special_meilenstein";

	static final public String ACTION_SPECIAL_TOGGLE_ERLEDIGT = "action_special_toggle_erledigt";

	WrapperButton wbuMeilenstein = new WrapperButton();
	WrapperTextField wtfMeilenstein = new WrapperTextField();

	WrapperLabel wlaKommentar = new WrapperLabel();
	WrapperTextField wtfKommentar = new WrapperTextField();

	WrapperLabel wlaErledigt = new WrapperLabel();

	WrapperLabel wlaKommentarLang = new WrapperLabel();
	WrapperEditorField wefKommentarLang = new WrapperEditorField(
			getInternalFrame(), "");

	private PanelQueryFLR panelQueryFLRMeilenstein = null;

	public PanelZahlungsplanmeilenstein(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();

		jbInit();
		setDefaults();
		initComponents();
	}

	void dialogQueryMeilenstein(ActionEvent e) throws Throwable {
		QueryType[] qt = null;
		FilterKriterium[] fk = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		panelQueryFLRMeilenstein = new PanelQueryFLR(qt, fk,
				QueryParameters.UC_ID_MEILENSTEIN, null, intFrame, LPMain
						.getInstance().getTextRespectUISPr("auft.meilenstein"),
				zahlungsplanmeilensteinDto.getMeilensteinId());

		new DialogQuery(panelQueryFLRMeilenstein);
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		wbuMeilenstein.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.meilenstein"));
		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.kommentar"));

		wlaKommentarLang.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.kommentar"));

		wlaErledigt.setHorizontalAlignment(SwingConstants.LEFT);

		wtfKommentar.setColumnsMax(300);

		wtfMeilenstein.setMandatoryField(true);
		wtfMeilenstein.setActivatable(false);
		wtfMeilenstein.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuMeilenstein.addActionListener(this);
		wbuMeilenstein.setActionCommand(ACTION_SPECIAL_MEILENSTEIN);

		int iZeile = 0;

		iZeile++;

		jPanelWorkingOn.add(wbuMeilenstein, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 80, 0));
		jPanelWorkingOn.add(wtfMeilenstein, new GridBagConstraints(1, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaKommentarLang, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefKommentarLang, new GridBagConstraints(1, iZeile,
				1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaErledigt, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		createAndSaveAndShowButton(
				"/com/lp/client/res/document_check16x16.png",
				LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein.toggleerledigt"),
				ACTION_SPECIAL_TOGGLE_ERLEDIGT, null);

	}

	private void setDefaults() throws Throwable {
		leereAlleFelder(this);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRMeilenstein) {
				Integer meilensteinIId = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();

				MeilensteinDto meilensteinDto = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.meilensteinFindByPrimaryKey(meilensteinIId);

				wtfMeilenstein.setText(meilensteinDto.getBezeichnung());
				zahlungsplanmeilensteinDto.setMeilensteinId(meilensteinIId);
			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (zahlungsplanmeilensteinDto.getIId() == null) {

				Integer pk = DelegateFactory
						.getInstance()
						.getAuftragServiceDelegate()
						.createZahlungsplanmeilenstein(
								zahlungsplanmeilensteinDto);
				zahlungsplanmeilensteinDto = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.zahlungsplanmeilensteinFindByPrimaryKey(pk);
				this.setKeyWhenDetailPanel(pk);

			} else {
				DelegateFactory
						.getInstance()
						.getAuftragServiceDelegate()
						.updateZahlungsplanmeilenstein(
								this.zahlungsplanmeilensteinDto);
			}

			// buttons schalten
			super.eventActionSave(e, true);

			eventYouAreSelected(false);

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {

			super.eventActionNew(eventObject, true, false);

			this.resetPanel();

		} else {
			tpAuftrag.getAuftragTeilnehmerTop().updateButtons(
					tpAuftrag.getAuftragTeilnehmerBottom()
							.getLockedstateDetailMainKey());
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {
			super.eventActionUpdate(aE, false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {
			DelegateFactory.getInstance().getAuftragServiceDelegate()
					.removeZahlungsplanmeilenstein(zahlungsplanmeilensteinDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
														// ueberschreiben
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MEILENSTEIN)) {
			dialogQueryMeilenstein(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_TOGGLE_ERLEDIGT)) {
			DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.toggleZahlungplanmeilensteinErledigt(
							zahlungsplanmeilensteinDto.getIId());

			tpAuftrag.getAuftragZahlungsplanmeilensteinTop()
					.eventYouAreSelected(false);

			eventYouAreSelected(false);
		}
	}

	private void resetPanel() throws Throwable {
		zahlungsplanmeilensteinDto = new ZahlungsplanmeilensteinDto();
		leereAlleFelder(this);
		setDefaults();
	}

	private void dto2Components() throws Throwable {

		MeilensteinDto meilensteinDto = DelegateFactory
				.getInstance()
				.getAuftragServiceDelegate()
				.meilensteinFindByPrimaryKey(
						zahlungsplanmeilensteinDto.getMeilensteinId());

		wtfMeilenstein.setText(meilensteinDto.getBezeichnung());

		wtfKommentar.setText(zahlungsplanmeilensteinDto.getCKommentar());
		wefKommentarLang.setText(zahlungsplanmeilensteinDto.getXText());

		if (zahlungsplanmeilensteinDto.getPersonalIIdErledigt() != null) {
			PersonalDto personalDtoVerrechnen = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							zahlungsplanmeilensteinDto.getPersonalIIdErledigt());

			wlaErledigt
					.setText(LPMain
							.getTextRespectUISPr("auft.zahlungsplanmeilenstein.erledigtam")
							+ " "
							+ Helper.formatDatum(
									zahlungsplanmeilensteinDto.getTErledigt(),
									LPMain.getTheClient().getLocUi())
							+ ", "
							+ personalDtoVerrechnen.formatAnrede());

		} else {
			wlaErledigt.setText("");
		}

	}

	private void components2Dto() throws Throwable {
		if (zahlungsplanmeilensteinDto.getIId() == null) {
			zahlungsplanmeilensteinDto.setZahlungsplaniId((Integer) tpAuftrag
					.getAuftragZahlungsplanTop().getSelectedId());

		}

		zahlungsplanmeilensteinDto.setCKommentar(wtfKommentar.getText());
		zahlungsplanmeilensteinDto.setXText(wefKommentarLang.getText());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			zahlungsplanmeilensteinDto = DelegateFactory.getInstance()
					.getAuftragServiceDelegate()
					.zahlungsplanmeilensteinFindByPrimaryKey((Integer) oKey);
			dto2Components();

		}

		tpAuftrag.setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
				"auft.meilenstein"));

		tpAuftrag.enablePanelsNachBitmuster();

		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAuftrag.getAuftragDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAuftrag.getAuftragDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAuftrag.getAuftragDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAuftrag.getAuftragDto().getTAendern());
		setStatusbarStatusCNr(tpAuftrag.getAuftragStatus());
	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		tpAuftrag.enablePanelsNachBitmuster();
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUFTRAG;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		tpAuftrag.enablePanelsNachBitmuster();
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lsv = super.getLockedstateDetailMainKey();

		if (tpAuftrag.getAuftragDto().getIId() != null) {
			if (tpAuftrag.getAuftragDto().getStatusCNr()
					.equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)
					|| tpAuftrag
							.getAuftragDto()
							.getStatusCNr()
							.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
					|| tpAuftrag.getAuftragDto().getStatusCNr()
							.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				lsv = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lsv;
	}
}
