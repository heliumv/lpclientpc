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
package com.lp.client.bestellung;

import java.awt.HeadlessException;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BSMahnlaufDto;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>10.03.05</>I></p>
 * 
 * <p> </p>
 * 
 * @author Josef Erlinger
 * 
 * @version $Revision: 1.15 $
 */
public class TabbedPaneBESMahnwesen extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelQuery panelBSMahnlaufTopQP1 = null;

	private PanelQuery panelBSMahnungenTopQP2 = null;
	private PanelSplit panelBSMahnungenSP2 = null;
	private PanelBestellungMahnungKopfdaten panelBSMahnungenBottomD2 = null;

	private PanelDialogKriterienBSMahnwesen panelBSMahnwesen = null;

	private static final int IDX_PANEL_BSMAHNLAUF = 0;
	private static final int IDX_PANEL_BSMAHNUNGEN = 1;

	private BSMahnlaufDto bsmahnlaufDto = null;

	private String whichFilterAusBSMahnwesen = null;

	private final static String ACTION_SPECIAL_REMOVE_MAHNLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_remove";
	private final static String ACTION_SPECIAL_PRINT_MAHNLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_print";
	private final static String ACTION_SPECIAL_FAX_MAHNLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_fax";
	private final static String ACTION_SPECIAL_MAIL_MAHNLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_mail";
	private final static String ACTION_SPECIAL_MAHNLAUF_ZURUECKNEHMEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_mahnlauf_zuruecknehmen";
	private final static String ACTION_SPECIAL_AUSWAHL_LOESCHEN = PanelBasis.LEAVEALONE
			+ "action_special_auswahl_loeschen";

	public TabbedPaneBESMahnwesen(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("bes.title.panel.mahnwesen"));
		jbInit();
		initComponents();
	}

	public InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("bes.mahnlauf"), null, null,
				LPMain.getTextRespectUISPr("bes.tooltip.mahnlauf"),
				IDX_PANEL_BSMAHNLAUF);

		insertTab(LPMain.getTextRespectUISPr("bes.mahnungen"), null, null,
				LPMain.getTextRespectUISPr("bes.tooltip.mahnungen"),
				IDX_PANEL_BSMAHNUNGEN);

		refreshPanelBSQueryMahnlauf();
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * Einen ausgewaehlten Mahnlauf holen und die Panels aktualisieren
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeBSMahnlaufDto(Object key) throws Throwable {
		if (key != null) {
			BSMahnlaufDto bsmahnlaufDto = DelegateFactory.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahnlaufFindByPrimaryKey((Integer) key);
			setBSMahnlaufDto(bsmahnlaufDto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
		} else {
			setBSMahnlaufDto(null);
		}
	}

	protected void setwhichFilterAusBSMahnwesen(String whichFilterAusBSMahnwesen) {
		this.whichFilterAusBSMahnwesen = whichFilterAusBSMahnwesen;
	}

	protected String getwhichFilterAusBSMahnwesen() {
		return whichFilterAusBSMahnwesen;
	}

	/**
	 * changed
	 * 
	 * @param eI
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == refreshPanelBSQueryMahnlauf()) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				holeBSMahnlaufDto(key);
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_BSMAHNLAUF, false);
					refreshPanelBSQueryMahnlauf().updateButtons(
							new LockStateValue(PanelBasis.LOCK_FOR_EMPTY));
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_BSMAHNLAUF, true);
					refreshPanelBSQueryMahnlauf()
							.updateButtons(
									new LockStateValue(
											(PanelBasis.LOCK_IS_NOT_LOCKED)));
				}
			}

			else if (e.getSource() == this.panelBSMahnungenTopQP2) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				refreshPanelBSDetailMahnung().setKeyWhenDetailPanel(key);
				refreshPanelBSDetailMahnung().eventYouAreSelected(false);
				if (panelBSMahnungenTopQP2.getSelectedIds() != null
						&& panelBSMahnungenTopQP2.getSelectedIds().length > 0) {
					panelBSMahnungenTopQP2.enableToolsPanelLeaveAloneButtons(
							new String[] { ACTION_SPECIAL_AUSWAHL_LOESCHEN },
							true);
				} else {
					panelBSMahnungenTopQP2.enableToolsPanelLeaveAloneButtons(
							new String[] { ACTION_SPECIAL_AUSWAHL_LOESCHEN },
							false);
				}

				// // im QP die Buttons in den Zustand nolocking/save setzen.
				// panelBSMahnungenTopQP2.updateButtons(panelBSMahnungenBottomD2
				// .getLockedstateDetailMainKey());

				if (panelBSMahnungenTopQP2.getSelectedIds() != null) {
					if (panelBSMahnungenTopQP2.getSelectedIds().length != 1) {
						panelBSMahnungenTopQP2.enableToolsPanelButtons(false,
								PanelBasis.ACTION_PRINT);
					} else {
						panelBSMahnungenTopQP2.enableToolsPanelButtons(true,
								PanelBasis.ACTION_PRINT);
					}
				} else {
					panelBSMahnungenTopQP2.enableToolsPanelButtons(false,
							PanelBasis.ACTION_PRINT);
				}

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			if (eI.getSource() == refreshPanelBSQueryMahnlauf()) {
				Integer key = (Integer) ((ISourceEvent) eI.getSource())
						.getIdSelected();
				if (key != null) {
					DelegateFactory.getInstance().getBSMahnwesenDelegate()
							.mahneBSMahnlaufRueckgaengig(key);
					refreshPanelBSQueryMahnlauf().eventYouAreSelected(false);
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.

			if (eI.getSource() == panelBSMahnungenBottomD2) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBSMahnungenTopQP2.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == refreshPanelBSDetailMahnung()) {
				panelBSMahnungenSP2.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelBSMahnungenBottomD2) {
				Object oKey = panelBSMahnungenBottomD2.getKeyWhenDetailPanel();
				panelBSMahnungenTopQP2.eventYouAreSelected(false);
				panelBSMahnungenTopQP2.setSelectedId(oKey);
				panelBSMahnungenSP2.eventYouAreSelected(false);
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == refreshPanelBSDetailMahnung()) {
				setKeyWasForLockMe();
				refreshPanelBSSplitMahnung().eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == refreshPanelBSQueryMahnlauf()) {
				getInternalFrame().showPanelDialog(
						refreshPanelDialogKriterienBSMahnwesen());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (eI.getSource() == refreshPanelBSQueryMahnlauf()) {
				if (ACTION_SPECIAL_REMOVE_MAHNLAUF.equals(sAspectInfo)) {
					if (getBSMahnlaufDto() != null) {
						DelegateFactory.getInstance().getBSMahnwesenDelegate()
								.removeBSMahnlauf(getBSMahnlaufDto());
						setBSMahnlaufDto(null);
					}
				} else if (ACTION_SPECIAL_FAX_MAHNLAUF.equals(sAspectInfo)) {
					DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.sendMahnlauf(PartnerFac.KOMMUNIKATIONSART_FAX,
									getBSMahnlaufDto());
				} else if (ACTION_SPECIAL_MAIL_MAHNLAUF.equals(sAspectInfo)) {
					DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.sendMahnlauf(PartnerFac.KOMMUNIKATIONSART_EMAIL,
									getBSMahnlaufDto());
				}
				refreshPanelBSQueryMahnlauf().eventYouAreSelected(false);
			} else if (eI.getSource() == refreshPanelBSQueryMahnung()) {
				if (ACTION_SPECIAL_AUSWAHL_LOESCHEN.equals(sAspectInfo)) {
					Object[] iIds = refreshPanelBSQueryMahnung()
							.getSelectedIds();
					if (iIds != null) {
						for (Object id : iIds) {
							DelegateFactory.getInstance()
									.getBSMahnwesenDelegate()
									.removeBSMahnung((Integer) id);
						}
					}
				}
				refreshPanelBSQueryMahnung().eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (eI.getSource() == refreshPanelBSQueryMahnlauf()) {
				printAlleBSMahnungen();
			} else if (eI.getSource() == refreshPanelBSQueryMahnung()) {
				printBSMahnung();
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (eI.getSource() == this.panelBSMahnwesen) {
				this.newBSMahnlauf(getwhichFilterAusBSMahnwesen());
			}
			refreshPanelBSQueryMahnung().eventYouAreSelected(false);
		}
	}

	private PanelDialogKriterien refreshPanelDialogKriterienBSMahnwesen()
			throws HeadlessException, Throwable {

		panelBSMahnwesen = new PanelDialogKriterienBSMahnwesen(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"bes.mahnkriterien"), this);

		return panelBSMahnwesen;
	}

	private void printBSMahnung() throws Throwable {
		Integer bsmahnungIId = (Integer) panelBSMahnungenTopQP2.getSelectedId();
		if (bsmahnungIId != null) {
			getInternalFrame().showReportKriterien(
					new ReportBestellungMahnung(getInternalFrame(),
							getAktuellesPanel(), bsmahnungIId,
							"Mahnung drucken"));
			panelBSMahnungenTopQP2.eventYouAreSelected(false);
		}
	}

	private void printAlleBSMahnungen() throws Throwable {
		if (getBSMahnlaufDto() != null) {
			Integer mahnlaufIId = getBSMahnlaufDto().getIId();
			if (mahnlaufIId != null) {
				DelegateFactory.getInstance().getBSMahnwesenDelegate()
						.mahneBSMahnlauf(mahnlaufIId);
				panelBSMahnungenTopQP2.eventYouAreSelected(false);
				JasperPrintLP[] prints = DelegateFactory.getInstance()
						.getBestellungDelegate()
						.printBSSammelMahnung(mahnlaufIId, false);
				for (int i = 0; i < prints.length; i++) {

					Integer lieferantIId = (Integer) prints[i]
							.getAdditionalInformation("lieferantIId");

					Integer anspIId = null;
					PartnerDto pDto = null;
					if (lieferantIId != null) {
						LieferantDto lDto = DelegateFactory.getInstance()
								.getLieferantDelegate()
								.lieferantFindByPrimaryKey(lieferantIId);
						pDto = lDto.getPartnerDto();
						anspIId = (Integer) prints[i]
								.getAdditionalInformation("ansprechpartnerIId");
					}

					getInternalFrame().showReportKriterien(
							new ReportBSSammelmahnung(getInternalFrame(),
									prints[i], lieferantIId, "Sammelmahnung"),
							pDto, anspIId);
				}
			}
		}
	}

	/**
	 * Diese Methode setzt der aktuellen Bestellung aus der Auswahlliste als den
	 * zu lockenden Bestellung.
	 * 
	 * @throws Throwable
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = refreshPanelBSQueryMahnlauf().getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private PanelSplit refreshPanelBSSplitMahnung() throws Throwable {
		if (panelBSMahnungenSP2 == null) {
			panelBSMahnungenSP2 = new PanelSplit(getInternalFrame(),
					refreshPanelBSDetailMahnung(),
					refreshPanelBSQueryMahnung(), 300);
			setComponentAt(IDX_PANEL_BSMAHNUNGEN, panelBSMahnungenSP2);
		}
		return panelBSMahnungenSP2;
	}

	/**
	 * getPanelQueryMahnung.
	 * 
	 * @return PanelBasis
	 * @throws Throwable
	 */
	private PanelBestellungMahnungKopfdaten refreshPanelBSDetailMahnung()
			throws Throwable {
		if (panelBSMahnungenBottomD2 == null) {
			panelBSMahnungenBottomD2 = new PanelBestellungMahnungKopfdaten(
					getInternalFrame(), "Mahnung", null, this);
		}
		return panelBSMahnungenBottomD2;
	}

	private void newBSMahnlauf(String whichFilterAusBSMahnwesen)
			throws Throwable {
		Boolean b = DelegateFactory.getInstance().getBSMahnwesenDelegate()
				.bGibtEsEinenOffenenBSMahnlauf();
		if (b.booleanValue()) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
					.getTextRespectUISPr("bes.mahnlauf.offen"));
		} else {
			BSMahnlaufDto newMahnlauf = null;
			if (whichFilterAusBSMahnwesen.equals(new String(
					BSMahnwesenFac.MAHNART_LIEFER_MAHNUNGEN))) {
				// echteLiefermahnungen
				newMahnlauf = DelegateFactory.getInstance()
						.getBSMahnwesenDelegate()
						.createBSMahnlaufEchteLiefermahnungen();
			} else if (whichFilterAusBSMahnwesen.equals(new String(
			// AB-Mahnungen
					BSMahnwesenFac.MAHNART_LIEFERERINNERUNG))) {
				newMahnlauf = DelegateFactory.getInstance()
						.getBSMahnwesenDelegate()
						.createBSMahnlaufLiefererinnerung();
			} else if (whichFilterAusBSMahnwesen.equals(new String(
			// AB-Mahnungen
					BSMahnwesenFac.MAHNART_AB_MAHNUNGEN))) {
				newMahnlauf = DelegateFactory.getInstance()
						.getBSMahnwesenDelegate().createBSMahnlaufABMahnungen();
			} else if (whichFilterAusBSMahnwesen.equals(new String(
					BSMahnwesenFac.MAHNART_AB_UND_LIEFER_MAHNUNGEN))) {
				// AB und LieferMahnungen
				newMahnlauf = DelegateFactory
						.getInstance()
						.getBSMahnwesenDelegate()
						.createABMahnungenUndLieferMahnungenUndLiefererinnerungen();
			}
			// refresh auf panels
			if (newMahnlauf != null) {
				refreshPanelBSQueryMahnlauf().setSelectedId(
						newMahnlauf.getIId());
				refreshPanelBSQueryMahnlauf().eventYouAreSelected(false);
				this.setSelectedComponent(panelBSMahnungenSP2);
			} else {

				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("bes.mahnwesen.keinneuermahnlauf"));
				return;
			}

		}
	}

	/**
	 * eventStateChanged
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_BSMAHNLAUF:
			refreshPanelBSQueryMahnlauf();
			panelBSMahnlaufTopQP1.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelBSMahnlaufTopQP1.updateButtons(panelBSMahnlaufTopQP1
					.getLockedstateDetailMainKey());
			break;
		}
		switch (selectedIndex) {
		case IDX_PANEL_BSMAHNUNGEN:
			this.refreshPanelBSSplitMahnung();
			panelBSMahnungenTopQP2.eventYouAreSelected(false);

			panelBSMahnungenTopQP2.updateButtons(panelBSMahnungenBottomD2
					.getLockedstateDetailMainKey());
			break;
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	public BSMahnlaufDto getBSMahnlaufDto() {
		return bsmahnlaufDto;
	}

	public void setBSMahnlaufDto(BSMahnlaufDto bsmahnlaufDto) throws Throwable {
		this.bsmahnlaufDto = bsmahnlaufDto;
		refreshPanelBSQueryMahnung().setDefaultFilter(
				BestellungFilterFactory.getInstance().createFKMahnung(
						getBSMahnlaufDto()));
		String sTitle = null;
		if (getBSMahnlaufDto() != null) {
			sTitle = Helper.formatDatum(new java.sql.Date(getBSMahnlaufDto()
					.getTAnlegen().getTime()), LPMain.getInstance()
					.getTheClient().getLocUi());
			LPButtonAction item1 = (LPButtonAction) refreshPanelBSQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_REMOVE_MAHNLAUF);
			item1.getButton().setEnabled(true);
			LPButtonAction item2 = (LPButtonAction) refreshPanelBSQueryMahnlauf()
					.getHmOfButtons().get(
							this.ACTION_SPECIAL_MAHNLAUF_ZURUECKNEHMEN);
			item2.getButton().setEnabled(true);
			LPButtonAction item3 = (LPButtonAction) refreshPanelBSQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_FAX_MAHNLAUF);
			item3.getButton().setEnabled(true);
			LPButtonAction item4 = (LPButtonAction) refreshPanelBSQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_MAIL_MAHNLAUF);
			item4.getButton().setEnabled(true);
		} else {
			sTitle = "";
			LPButtonAction item1 = (LPButtonAction) refreshPanelBSQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_REMOVE_MAHNLAUF);
			item1.getButton().setEnabled(false);
			LPButtonAction item2 = (LPButtonAction) refreshPanelBSQueryMahnlauf()
					.getHmOfButtons().get(
							this.ACTION_SPECIAL_MAHNLAUF_ZURUECKNEHMEN);
			item2.getButton().setEnabled(false);
			LPButtonAction item3 = (LPButtonAction) refreshPanelBSQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_FAX_MAHNLAUF);
			item3.getButton().setEnabled(false);
			LPButtonAction item4 = (LPButtonAction) refreshPanelBSQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_MAIL_MAHNLAUF);
			item4.getButton().setEnabled(false);
		}
		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	/**
	 * getPanelQueryMahnung.
	 * 
	 * @return PanelQuery
	 * @throws Throwable
	 */
	private PanelQuery refreshPanelBSQueryMahnung() throws Throwable {
		if (panelBSMahnungenTopQP2 == null) {
			String[] aWhichButtonIUseMahnung = { PanelBasis.ACTION_PRINT };
			FilterKriterium[] filtersMahnung = BestellungFilterFactory
					.getInstance().createFKMahnung(getBSMahnlaufDto());

			panelBSMahnungenTopQP2 = new PanelQuery(null, filtersMahnung,
					QueryParameters.UC_ID_BESMAHNUNG, aWhichButtonIUseMahnung,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("finanz.mahnungen"), true);
			panelBSMahnungenTopQP2.setMultipleRowSelectionEnabled(true);
			panelBSMahnungenTopQP2.createAndSaveAndShowButton(
					"/com/lp/client/res/delete2.png",
					LPMain.getTextRespectUISPr("lp.auswahl.loeschen"),
					ACTION_SPECIAL_AUSWAHL_LOESCHEN,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
		}
		return panelBSMahnungenTopQP2;
	}

	private PanelQuery refreshPanelBSQueryMahnlauf() throws Throwable {
		if (panelBSMahnlaufTopQP1 == null) {
			String[] aWhichButtonIUseMahnlauf = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_PRINT };
			FilterKriterium[] filtersMahnlauf = SystemFilterFactory
					.getInstance().createFKMandantCNr();

			panelBSMahnlaufTopQP1 = new PanelQuery(null, filtersMahnlauf,
					QueryParameters.UC_ID_BESMAHNLAUF,
					aWhichButtonIUseMahnlauf, getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.mahnlauf"), true);

			panelBSMahnlaufTopQP1.createAndSaveAndShowButton(
					"/com/lp/client/res/mail.png",
					LPMain.getTextRespectUISPr("finanz.tooltip.mahnlaufmail"),
					ACTION_SPECIAL_MAIL_MAHNLAUF,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelBSMahnlaufTopQP1.createAndSaveAndShowButton(
					"/com/lp/client/res/fax.png",
					LPMain.getTextRespectUISPr("finanz.tooltip.mahnlauffax"),
					ACTION_SPECIAL_FAX_MAHNLAUF,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelBSMahnlaufTopQP1.createAndSaveAndShowButton(
					"/com/lp/client/res/delete2.png",
					LPMain.getTextRespectUISPr("finanz.mahnlaufloeschen"),
					ACTION_SPECIAL_REMOVE_MAHNLAUF,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelBSMahnlaufTopQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/leeren.png",
							LPMain.getTextRespectUISPr("finanz.tooltip.mahnlaufzuruecknehmen"),
							ACTION_SPECIAL_MAHNLAUF_ZURUECKNEHMEN,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);

			setComponentAt(IDX_PANEL_BSMAHNLAUF, panelBSMahnlaufTopQP1);
		}
		return panelBSMahnlaufTopQP1;
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	public Object getDto() {
		return bsmahnlaufDto;
	}
}
