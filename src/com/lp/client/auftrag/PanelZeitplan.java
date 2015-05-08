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
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.ZeitplanDto;
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
public class PanelZeitplan extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;

	private ZeitplanDto zeitplanDto = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	WrapperLabel wlaTermin = new WrapperLabel();
	WrapperDateField wdfTermin = new WrapperDateField();

	WrapperLabel wlaMaterial = new WrapperLabel();
	WrapperNumberField wnfMaterial = new WrapperNumberField();

	WrapperLabel wlaMaterialUrsprung = new WrapperLabel();
	WrapperNumberField wnfMaterialUrsprung = new WrapperNumberField();

	WrapperLabel wlaDauer = new WrapperLabel();
	WrapperNumberField wnfDauer = new WrapperNumberField();
	WrapperLabel wlaDauerUrsprung = new WrapperLabel();
	WrapperNumberField wnfDauerUrsprung = new WrapperNumberField();

	WrapperLabel wlaEinheitDauer = new WrapperLabel();
	WrapperLabel wlaWaehrungMaterial = new WrapperLabel();

	WrapperLabel wlaKommentar = new WrapperLabel();
	WrapperTextField wtfKommentar = new WrapperTextField();

	WrapperLabel wlaKommentarLang = new WrapperLabel();
	WrapperEditorField wefKommentarLang = new WrapperEditorField(
			getInternalFrame(), "");

	public PanelZeitplan(InternalFrame internalFrame, String add2TitleI,
			Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();

		jbInit();
		setDefaults();
		initComponents();
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

		wlaTermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.termin"));
		wlaDauer.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.dauer"));
		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.kommentar"));
		wlaMaterial.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.material"));
		wlaKommentarLang.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.kommentar"));

		wlaDauerUrsprung.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.dauer.ursprung"));
		wlaMaterialUrsprung.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplan.material.ursprung"));

		wtfKommentar.setColumnsMax(300);

		wdfTermin.setMandatoryField(true);
		wnfMaterial.setMandatoryField(true);
		wnfDauer.setMandatoryField(true);

		wnfMaterialUrsprung.setActivatable(false);
		wnfDauerUrsprung.setActivatable(false);

		wlaWaehrungMaterial.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitDauer.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitDauer.setText("h");

		int iZeile = 0;

		jPanelWorkingOn.add(wlaTermin, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wdfTermin, new GridBagConstraints(1, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaMaterial, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMaterial, new GridBagConstraints(1, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrungMaterial, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 40, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaMaterialUrsprung, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMaterialUrsprung, new GridBagConstraints(1,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jPanelWorkingOn.add(wlaDauer, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfDauer, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaEinheitDauer, new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaDauerUrsprung, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfDauerUrsprung, new GridBagConstraints(1, iZeile,
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

	}

	private void setDefaults() throws Throwable {
		leereAlleFelder(this);

		wdfTermin.setMaximumValue(tpAuftrag.getAuftragDto().getDLiefertermin());
		wlaWaehrungMaterial.setText(tpAuftrag.getAuftragDto()
				.getCAuftragswaehrung());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (zeitplanDto.getIId() == null) {

				Integer pkTeilnehmer = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.createZeitplan(zeitplanDto);
				zeitplanDto = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.zeitplanFindByPrimaryKey(pkTeilnehmer);
				this.setKeyWhenDetailPanel(pkTeilnehmer);

				// @todo in der Folge wird am Server fuer den entsprechenden
				// Teilnehmer eine Aufgabe erzeugt PJ 4818
			} else {
				DelegateFactory.getInstance().getAuftragServiceDelegate()
						.updateZeitplan(this.zeitplanDto);
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
					.removeZeitplan(zeitplanDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
														// ueberschreiben
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	private void resetPanel() throws Throwable {
		zeitplanDto = new ZeitplanDto();
		leereAlleFelder(this);
		setDefaults();
	}

	private void dto2Components() throws Throwable {

		wtfKommentar.setText(zeitplanDto.getCKommentar());
		wefKommentarLang.setText(zeitplanDto.getXText());
		wnfDauer.setBigDecimal(zeitplanDto.getNDauer());
		wnfDauerUrsprung.setBigDecimal(zeitplanDto.getNDauerUrsprung());

		wnfMaterial.setBigDecimal(zeitplanDto.getNMaterial());
		wnfMaterialUrsprung.setBigDecimal(zeitplanDto.getNMaterialUrsprung());

		wtfKommentar.setText(zeitplanDto.getCKommentar());

		wdfTermin.setDate(Helper.addiereTageZuDatum(tpAuftrag.getAuftragDto()
				.getDLiefertermin(), -zeitplanDto.getITerminVorLiefertermin()));

	}

	private void components2Dto() throws Throwable {
		if (zeitplanDto.getIId() == null) {
			zeitplanDto.setAuftragIId(tpAuftrag.getAuftragDto().getIId());

		}

		int i = Helper.ermittleTageEinesZeitraumes(wdfTermin.getDate(),
				new java.sql.Date(tpAuftrag.getAuftragDto().getDLiefertermin()
						.getTime()));

		zeitplanDto.setITerminVorLiefertermin(i);
		zeitplanDto.setNDauer(wnfDauer.getBigDecimal());
		zeitplanDto.setNMaterial(wnfMaterial.getBigDecimal());

		zeitplanDto.setCKommentar(wtfKommentar.getText());
		zeitplanDto.setXText(wefKommentarLang.getText());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			zeitplanDto = DelegateFactory.getInstance()
					.getAuftragServiceDelegate()
					.zeitplanFindByPrimaryKey((Integer) oKey);
			dto2Components();

		}

		tpAuftrag.setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.teilnehmer"));

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
