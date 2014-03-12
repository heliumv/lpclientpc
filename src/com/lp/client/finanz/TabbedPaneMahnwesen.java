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
package com.lp.client.finanz;

import java.sql.Timestamp;
import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import net.sf.jasperreports.engine.JasperExportManager;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um das Mahnwesen.</I> </p> <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>06.01.05</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.14 $
 */
public class TabbedPaneMahnwesen extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryMahnlauf = null;
	// private PanelFinanzMahnlaufKopfdaten panelDetailMahnlauf = null;
	// private PanelSplit panelSplitMahnlauf = null;
	private PanelQuery panelQueryMahnung = null;
	private PanelFinanzMahnungKopfdaten panelDetailMahnung = null;
	private PanelSplit panelSplitMahnung = null;

	private PanelQuery mahnsperren = null;

	private final static int IDX_0_MAHNLAUF = 0;
	private final static int IDX_1_MAHNUNGEN = 1;
	private final static int IDX_2_MAHNSPERREN = 2;

	private MahnlaufDto mahnlaufDto = null;

	private final static String ACTION_SPECIAL_REMOVE_MAHNLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_remove";
	private final static String ACTION_SPECIAL_MAHNLAUF_ZURUECKNEHMEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_mahnlauf_zuruecknehmen";
	private final static String MENU_ACTION_PRINT_MAHNLAUF = "menu_action_special_print";
	private final static String ACTION_SPECIAL_FAX_MAHNLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_fax";
	private final static String ACTION_SPECIAL_MAIL_MAHNLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_mail";

	public TabbedPaneMahnwesen(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"finanz.tab.unten.mahnwesen.title"));
		jbInit();
		initComponents();
	}

	public MahnlaufDto getMahnlaufDto() {
		return mahnlaufDto;
	}

	public void setMahnlaufDto(MahnlaufDto mahnlaufDto) throws Throwable {
		this.mahnlaufDto = mahnlaufDto;
		getPanelQueryMahnung().setDefaultFilter(
				FinanzFilterFactory.getInstance().createFKMahnung(
						getMahnlaufDto()));
		String sTitle = null;
		if (getMahnlaufDto() != null) {
			sTitle = Helper.formatTimestamp(getMahnlaufDto().getTAnlegen(),
					LPMain.getInstance().getTheClient().getLocUi());
			LPButtonAction item1 = (LPButtonAction) getPanelQueryMahnlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_REMOVE_MAHNLAUF);
			item1.getButton().setEnabled(true);
			LPButtonAction item2 = (LPButtonAction) getPanelQueryMahnlauf()
					.getHmOfButtons()
					.get(ACTION_SPECIAL_MAHNLAUF_ZURUECKNEHMEN);
			item2.getButton().setEnabled(true);
			LPButtonAction item3 = (LPButtonAction) getPanelQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_FAX_MAHNLAUF);
			item3.getButton().setEnabled(true);
			LPButtonAction item4 = (LPButtonAction) getPanelQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_MAIL_MAHNLAUF);
			item4.getButton().setEnabled(true);
		} else {
			sTitle = "";
			LPButtonAction item1 = (LPButtonAction) getPanelQueryMahnlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_REMOVE_MAHNLAUF);
			item1.getButton().setEnabled(false);
			LPButtonAction item2 = (LPButtonAction) getPanelQueryMahnlauf()
					.getHmOfButtons()
					.get(ACTION_SPECIAL_MAHNLAUF_ZURUECKNEHMEN);
			item2.getButton().setEnabled(false);
			LPButtonAction item3 = (LPButtonAction) getPanelQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_FAX_MAHNLAUF);
			item3.getButton().setEnabled(false);
			LPButtonAction item4 = (LPButtonAction) getPanelQueryMahnlauf()
					.getHmOfButtons().get(this.ACTION_SPECIAL_MAIL_MAHNLAUF);
			item4.getButton().setEnabled(false);
		}
		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	/**
	 * jbInit.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// 1 tab oben: Mahnlaeufe; lazy loading
		insertTab(
				LPMain.getTextRespectUISPr("finanz.tab.oben.mahnlaeufe.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.mahnlaeufe.tooltip"),
				IDX_0_MAHNLAUF);
		// 2 tab oben: Mahnungen; lazy loading
		insertTab(
				LPMain.getTextRespectUISPr("finanz.tab.oben.mahnungen.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.mahnungen.tooltip"),
				IDX_1_MAHNUNGEN);
		insertTab(LPMain.getTextRespectUISPr("finanz.mahnsperren"), null, null,
				LPMain.getTextRespectUISPr("finanz.mahnsperren"),
				IDX_2_MAHNSPERREN);
		// default selektierung
		setSelectedComponent(getPanelQueryMahnlauf());
		// refresh
		getPanelQueryMahnlauf().eventYouAreSelected(false);
		// Listener
		// damit gleich eine selektiert ist

		ItemChangedEvent it = new ItemChangedEvent(getPanelQueryMahnlauf(),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public PanelQuery getPanelQueryMahnsperren() throws Throwable {
		if (mahnsperren == null) {
			QueryType[] qtPos = null;
			FilterKriterium[] filtersPos = RechnungFilterFactory.getInstance()
					.createFKMahnsperren(
							new java.sql.Date(getMahnlaufDto().getTAnlegen()
									.getTime()));

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			mahnsperren = new PanelQuery(qtPos, filtersPos,
					QueryParameters.UC_ID_MAHNSPERRE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("finanz.mahnsperren"), true);
			setComponentAt(IDX_2_MAHNSPERREN, mahnsperren);
		} else {
			mahnsperren.setDefaultFilter(RechnungFilterFactory.getInstance()
					.createFKMahnsperren(
							new java.sql.Date(getMahnlaufDto().getTAnlegen()
									.getTime())));
		}

		return mahnsperren;
	}

	private PanelSplit getPanelSplitMahnung() throws Throwable {
		if (panelSplitMahnung == null) {
			panelSplitMahnung = new PanelSplit(getInternalFrame(),
					getPanelDetailMahnung(), getPanelQueryMahnung(), 300);
			setComponentAt(IDX_1_MAHNUNGEN, panelSplitMahnung);
		}
		return panelSplitMahnung;
	}

	private PanelQuery getPanelQueryMahnlauf() throws Throwable {
		if (panelQueryMahnlauf == null) {
			String[] aWhichButtonIUseMahnlauf = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_PRINT };
			FilterKriterium[] filtersMahnlauf = SystemFilterFactory
					.getInstance().createFKMandantCNr();

			panelQueryMahnlauf = new PanelQuery(null, filtersMahnlauf,
					QueryParameters.UC_ID_MAHNLAUF, aWhichButtonIUseMahnlauf,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.mahnlauf"), true);

			panelQueryMahnlauf.createAndSaveAndShowButton(
					"/com/lp/client/res/mail.png",
					LPMain.getTextRespectUISPr("finanz.tooltip.mahnlaufmail"),
					ACTION_SPECIAL_MAIL_MAHNLAUF,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelQueryMahnlauf.createAndSaveAndShowButton(
					"/com/lp/client/res/fax.png",
					LPMain.getTextRespectUISPr("finanz.tooltip.mahnlauffax"),
					ACTION_SPECIAL_FAX_MAHNLAUF,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);
			panelQueryMahnlauf.createAndSaveAndShowButton(
					"/com/lp/client/res/delete2.png",
					LPMain.getTextRespectUISPr("finanz.mahnlaufloeschen"),
					ACTION_SPECIAL_REMOVE_MAHNLAUF,
					RechteFac.RECHT_FB_FINANZ_CUD);
			panelQueryMahnlauf
					.createAndSaveAndShowButton(
							"/com/lp/client/res/leeren.png",
							LPMain.getTextRespectUISPr("finanz.tooltip.mahnlaufzuruecknehmen"),
							ACTION_SPECIAL_MAHNLAUF_ZURUECKNEHMEN,
							RechteFac.RECHT_FB_FINANZ_CUD);

			setComponentAt(IDX_0_MAHNLAUF, panelQueryMahnlauf);
		}
		return panelQueryMahnlauf;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == getPanelQueryMahnlauf()) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				holeMahnlaufDto(key);
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_0_MAHNLAUF, false);
					getPanelQueryMahnlauf().updateButtons(
							new LockStateValue(PanelBasis.LOCK_FOR_EMPTY));
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_0_MAHNLAUF, true);
					getPanelQueryMahnlauf()
							.updateButtons(
									new LockStateValue(
											(PanelBasis.LOCK_IS_NOT_LOCKED)));
				}
			} else if (eI.getSource() == getPanelQueryMahnung()) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				getPanelDetailMahnung().setKeyWhenDetailPanel(key);
				getPanelDetailMahnung().eventYouAreSelected(false);
				getPanelQueryMahnung().updateButtons();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == getPanelQueryMahnlauf()) {
				newMahnlauf();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			if (eI.getSource() == getPanelQueryMahnlauf()) {
				Integer key = (Integer) ((ISourceEvent) eI.getSource())
						.getIdSelected();
				if (key != null) {
					DelegateFactory.getInstance().getMahnwesenDelegate()
							.mahneMahnlaufRueckgaengig(key);
					getPanelQueryMahnlauf().eventYouAreSelected(false);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			if (eI.getSource() == getPanelQueryMahnlauf()) {
				String sAspectInfo = ((ISourceEvent) eI.getSource())
						.getAspect();
				if (ACTION_SPECIAL_REMOVE_MAHNLAUF.equals(sAspectInfo)) {
					if (getMahnlaufDto() != null) {
						DelegateFactory.getInstance().getMahnwesenDelegate()
								.removeMahnlauf(getMahnlaufDto());
						setMahnlaufDto(null);
					}
				} else if (ACTION_SPECIAL_FAX_MAHNLAUF.equals(sAspectInfo)) {
					sendMahnlauf(PartnerFac.KOMMUNIKATIONSART_FAX);
				} else if (ACTION_SPECIAL_MAIL_MAHNLAUF.equals(sAspectInfo)) {
					sendMahnlauf(PartnerFac.KOMMUNIKATIONSART_EMAIL);
				}
				getPanelQueryMahnlauf().eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (eI.getSource() == getPanelQueryMahnlauf()) {
				printAlleMahnungen();
				getPanelQueryMahnlauf().eventYouAreSelected(false);
			} else if (eI.getSource() == getPanelQueryMahnung()) {
				printMahnung();
				getPanelQueryMahnung().eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == getPanelQueryMahnlauf()) {
				setSelectedComponent(panelSplitMahnung);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == getPanelDetailMahnung()) {

				setKeyWasForLockMe();
				if (getPanelDetailMahnung().getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryMahnung()
							.getId2SelectAfterDelete();
					getPanelQueryMahnung().setSelectedId(oNaechster);
				}
				getPanelSplitMahnung().eventYouAreSelected(false);

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD
				|| eI.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (eI.getSource() == getPanelDetailMahnung()) {
				getPanelSplitMahnung().eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailMahnung) {
				Object oKey = panelDetailMahnung.getKeyWhenDetailPanel();
				panelQueryMahnung.eventYouAreSelected(false);
				panelQueryMahnung.setSelectedId(oKey);
				panelSplitMahnung.eventYouAreSelected(false);
			}
		}
	}

	private void printMahnung() throws Throwable {
		Integer mahnungIId = (Integer) panelQueryMahnung.getSelectedId();
		if (mahnungIId != null) {
			MahnungDto mahnungDto = DelegateFactory.getInstance()
					.getMahnwesenDelegate().mahnungFindByPrimaryKey(mahnungIId);
			RechnungDto rechnungDto = DelegateFactory.getInstance()
					.getRechnungDelegate()
					.rechnungFindByPrimaryKey(mahnungDto.getRechnungIId());
			KundeDto kundeDto = DelegateFactory.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(rechnungDto.getKundeIId());
			getInternalFrame().showReportKriterien(
					new ReportEinzelMahnung(getInternalFrame(), getAktuellesPanel(), mahnungIId,
							LPMain.getInstance().getTextRespectUISPr(
									"lp.mahnung")), kundeDto.getPartnerDto(),
					rechnungDto.getAnsprechpartnerIId());
		}
	}

	private void printAlleMahnungen() throws Throwable {
		if (getMahnlaufDto() != null) {
			Integer mahnlaufIId = getMahnlaufDto().getIId();
			if (mahnlaufIId != null) {
				panelQueryMahnung.eventYouAreSelected(false);
				JasperPrintLP[] prints = null;
				try {
					prints = DelegateFactory.getInstance()
							.getFinanzReportDelegate()
							.printSammelMahnungen(mahnlaufIId, false);
					DelegateFactory.getInstance().getMahnwesenDelegate()
							.mahneMahnlauf(mahnlaufIId);

				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN) {
						String sFehler = LPMain.getInstance().getMsg(ex);
						if (ex.getAlInfoForTheClient() != null
								&& !ex.getAlInfoForTheClient().isEmpty()) {
							sFehler += ": " + ex.getAlInfoForTheClient().get(0);
						}
						DialogFactory.showModalDialog(LPMain.getInstance()
								.getTextRespectUISPr("lp.error"), sFehler);
						return;
					} else {
						throw ex;
					}
				}
				for (int i = 0; i < prints.length; i++) {
					Integer kundeIId = (Integer) prints[i]
							.getAdditionalInformation(JasperPrintLP.KEY_KUNDE_I_ID);
					PartnerDto partnerDtoEmpfaenger = null;
					Integer ansprechpartnerIIdErster = null;
					if (kundeIId != null) {
						KundeDto kundeDto = DelegateFactory.getInstance()
								.getKundeDelegate()
								.kundeFindByPrimaryKey(kundeIId);
						partnerDtoEmpfaenger = kundeDto.getPartnerDto();
						AnsprechpartnerDto ansprechpartnerDtoErster = DelegateFactory
								.getInstance()
								.getAnsprechpartnerDelegate()
								.ansprechpartnerFindErstenEinesPartnersOhneExc(
										partnerDtoEmpfaenger.getIId());
						if (ansprechpartnerDtoErster != null) {
							ansprechpartnerIIdErster = ansprechpartnerDtoErster
									.getIId();
						}
					}
					getInternalFrame()
							.showReportKriterien(
									new ReportSammelmahnung(
											getInternalFrame(),
											prints[i],
											LPMain.getTextRespectUISPr("finanz.sammelmahnung")),
									partnerDtoEmpfaenger,
									ansprechpartnerIIdErster);
				}
			}
		}
	}

	/**
	 * getPanelQueryMahnung.
	 * 
	 * @return PanelQuery
	 * @throws Throwable
	 */
	public PanelQuery getPanelQueryMahnung() throws Throwable {
		if (panelQueryMahnung == null) {
			String[] aWhichButtonIUseMahnung = { PanelBasis.ACTION_PRINT };
			FilterKriterium[] filtersMahnung = FinanzFilterFactory
					.getInstance().createFKMahnung(getMahnlaufDto());

			panelQueryMahnung = new PanelQuery(null, filtersMahnung,
					QueryParameters.UC_ID_MAHNUNG, aWhichButtonIUseMahnung,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("finanz.mahnlauf"), true);
			panelQueryMahnung.setMultipleRowSelectionEnabled(true);
		}
		return panelQueryMahnung;
	}

	/**
	 * getPanelQueryMahnung.
	 * 
	 * @return PanelBasis
	 * @throws Throwable
	 */
	private PanelFinanzMahnungKopfdaten getPanelDetailMahnung()
			throws Throwable {
		if (panelDetailMahnung == null) {
			panelDetailMahnung = new PanelFinanzMahnungKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("finanz.mahnlauf"), // Titel
					// soll
					// absichtlich
					// nicht
					// "Mahnung"
					// sein
					null, this);
		}
		return panelDetailMahnung;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();
		if (selectedIndex == IDX_0_MAHNLAUF) {
			getPanelQueryMahnlauf().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_1_MAHNUNGEN) {
			getPanelSplitMahnung().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_2_MAHNSPERREN) {
			getPanelQueryMahnsperren().eventYouAreSelected(false);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_PRINT_MAHNLAUF)) {
			if (getMahnlaufDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportMahnlauf(getInternalFrame(), mahnlaufDto
								.getIId(), LPMain.getInstance()
								.getTextRespectUISPr("bes.mahnlauf")));
			}
		}
	}

	private void newMahnlauf() throws Throwable {
		Boolean b = DelegateFactory.getInstance().getMahnwesenDelegate()
				.bGibtEsEinenOffenenMahnlauf();
		if (b.booleanValue()) {
			// Buttons gleich wieder richtig schalten
			getPanelQueryMahnlauf().updateButtons();
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("bes.mahnlauf.offen"));
		} else {
			MahnlaufDto mahnlaufDtoNew = DelegateFactory.getInstance()
					.getMahnwesenDelegate().createMahnlaufMitMahnvorschlag();
			// nur wenn einer erstellt wurde.
			if (mahnlaufDtoNew != null) {
				this.setSelectedComponent(panelSplitMahnung);
				// vorne auch selektieren
				getPanelQueryMahnlauf().eventYouAreSelected(false);
				getPanelQueryMahnlauf().setSelectedId(mahnlaufDtoNew.getIId());
				this.setMahnlaufDto(mahnlaufDtoNew);
				getPanelQueryMahnung().eventYouAreSelected(false);
			} else {
				// Damit die Buttons wieder den richtigen status haben.
				getPanelQueryMahnlauf().eventYouAreSelected(false);
			}
		}
	}

	public Integer getSelectedIIdMahnlauf() {
		return (Integer) panelQueryMahnlauf.getSelectedId();
	}

	/**
	 * Einen ausgewaehlten Mahnlauf holen und die Panels aktualisieren
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeMahnlaufDto(Object key) throws Throwable {
		if (key != null) {
			MahnlaufDto dto = DelegateFactory.getInstance()
					.getMahnwesenDelegate()
					.mahnlaufFindByPrimaryKey((Integer) key);
			setMahnlaufDto(dto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
		} else {
			setMahnlaufDto(null);
		}
	}

	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryMahnlauf().getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);
		JMenu journal = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_JOURNAL);
		journal.add(new JSeparator(), 0);
		WrapperMenuItem menuItemDateiMahnlaufDrucken = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("bes.mahnlauf"), null);
		menuItemDateiMahnlaufDrucken.addActionListener(this);
		menuItemDateiMahnlaufDrucken
				.setActionCommand(MENU_ACTION_PRINT_MAHNLAUF);
		journal.add(menuItemDateiMahnlaufDrucken, 0);
		return wmb;
	}

	public Object getInseratDto() {
		return mahnlaufDto;
	}

	private void sendMahnlauf(String cKommuniaktionsart) throws ExceptionLP,
			Throwable {
		if (getMahnlaufDto() != null) {
			Integer mahnlaufIId = getMahnlaufDto().getIId();
			String sAbsenderadresse = null;
			if (mahnlaufIId != null) {
				// Pruefung ob fuer Personal Mail definiert ist
				if (PartnerFac.KOMMUNIKATIONSART_EMAIL
						.equals(cKommuniaktionsart)) {
					PersonalDto personalDto = DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(
									LPMain.getInstance().getTheClient()
											.getIDPersonal());
					if (personalDto.getCEmail() != null) {
						sAbsenderadresse = personalDto.getCEmail();
						if (sAbsenderadresse == null) {

							throw new ExceptionLP(
									EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
									new Exception(
											LPMain.getTextRespectUISPr("bestellung.fehler.keinemailadressedefiniert")));
						}
						if (!Helper.validateEmailadresse(sAbsenderadresse)) {
							throw new ExceptionLP(
									EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE,
									new Exception(
											LPMain.getTextRespectUISPr("bestellung.fehler.ungueltigemailadressedefiniert")));
						}
					} else {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
								new Exception(
										LPMain.getTextRespectUISPr("bestellung.fehler.keinemailadressedefiniert")));
					}

				}
				JasperPrintLP[] prints = null;
				try {

					prints = DelegateFactory.getInstance()
							.getFinanzReportDelegate()
							.printSammelMahnungen(mahnlaufIId, true);
					DelegateFactory.getInstance().getMahnwesenDelegate()
							.mahneMahnlauf(mahnlaufIId);

				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN) {
						String sFehler = LPMain.getInstance().getMsg(ex);
						if (ex.getAlInfoForTheClient() != null
								&& !ex.getAlInfoForTheClient().isEmpty()) {
							sFehler += ": " + ex.getAlInfoForTheClient().get(0);
						}
						DialogFactory.showModalDialog(LPMain.getInstance()
								.getTextRespectUISPr("lp.error"), sFehler);
						return;
					} else {
						throw ex;
					}
				}
				String sLieferantenNichtGemahnt = "";
				for (int i = 0; i < prints.length; i++) {
					Integer iKundeIId = (Integer) prints[i]
							.getAdditionalInformation(JasperPrintLP.KEY_KUNDE_I_ID);
					Integer iMahnstufe = (Integer) prints[i]
							.getAdditionalInformation(JasperPrintLP.KEY_MAHNSTUFE);
					String sRechnungen = (String) prints[i]
							.getAdditionalInformation(JasperPrintLP.KEY_RECHNUNG_C_NR);
					// Kunde holen
					KundeDto kundeDto = DelegateFactory.getInstance()
							.getKundeDelegate()
							.kundeFindByPrimaryKey(iKundeIId);
					Integer iPartnerIId = kundeDto.getPartnerIId();
					// Kommunikation holen
					PartnerDto partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey(iPartnerIId);

					String sEmpfaenger = null;
					if (PartnerFac.KOMMUNIKATIONSART_EMAIL
							.equals(cKommuniaktionsart)) {

						String ubersteuerterEmpfaenger = DelegateFactory
								.getInstance()
								.getAnsprechpartnerDelegate()
								.getUebersteuerteEmpfaenger(partnerDto,
										FinanzReportFac.REPORT_SAMMELMAHNUNG,
										true);

						if (ubersteuerterEmpfaenger != null
								&& ubersteuerterEmpfaenger.length() > 0) {
							sEmpfaenger = ubersteuerterEmpfaenger;
						} else {

							if (partnerDto.getCEmail() != null) {
								sEmpfaenger = partnerDto.getCEmail();
								if (!Helper.validateEmailadresse(sEmpfaenger)) {
									sEmpfaenger = null;
								}
							}
						}

					}
					if (PartnerFac.KOMMUNIKATIONSART_FAX
							.equals(cKommuniaktionsart)) {

						String ubersteuerterEmpfaenger = DelegateFactory
								.getInstance()
								.getAnsprechpartnerDelegate()
								.getUebersteuerteEmpfaenger(partnerDto,
										FinanzReportFac.REPORT_SAMMELMAHNUNG,
										false);

						if (ubersteuerterEmpfaenger != null
								&& ubersteuerterEmpfaenger.length() > 0) {
							sEmpfaenger = ubersteuerterEmpfaenger;
						} else {

							if (partnerDto.getCFax() != null) {
								sEmpfaenger = DelegateFactory
										.getInstance()
										.getPartnerDelegate()
										.passeInlandsAuslandsVorwahlAn(
												partnerDto.getIId(),
												partnerDto.getCFax());

								if (!Helper.validateFaxnummer(sEmpfaenger)) {
									sEmpfaenger = null;
								}
							}
						}
					}
					if (sEmpfaenger != null) {
						VersandauftragDto versDto = new VersandauftragDto();
						versDto.setCEmpfaenger(sEmpfaenger);
						versDto.setCAbsenderadresse(sAbsenderadresse);
						String sLocMahnung = LPMain
								.getTextRespectUISPr("lp.mahnung");
						versDto.setCDateiname(sLocMahnung + ".pdf");
						partnerDto.getLocaleCNrKommunikation();
						versDto.setCBetreff(sLocMahnung);
						versDto.setOInhalt(JasperExportManager
								.exportReportToPdf(prints[i].getPrint()));

						if (PartnerFac.KOMMUNIKATIONSART_EMAIL
								.equals(cKommuniaktionsart)) {

							MailtextDto mailtextDto = new MailtextDto();
							mailtextDto.setParamLocale(LPMain.getTheClient()
									.getLocUi());
							mailtextDto.setParamMandantCNr(LPMain
									.getTheClient().getMandant());
							mailtextDto
									.setParamModul(FinanzReportFac.REPORT_MODUL);
							mailtextDto
									.setParamXslFile(FinanzReportFac.REPORT_SAMMELMAHNUNG);

							PartnerDto partnerDtoEmpfaenger = kundeDto
									.getPartnerDto();
							AnsprechpartnerDto ansprechpartnerDtoErster = DelegateFactory
									.getInstance()
									.getAnsprechpartnerDelegate()
									.ansprechpartnerFindErstenEinesPartnersOhneExc(
											partnerDtoEmpfaenger.getIId());
							if (ansprechpartnerDtoErster != null) {
								mailtextDto
										.setMailAnprechpartnerIId(ansprechpartnerDtoErster
												.getIId());
							}
							Locale locKunde = Helper.string2Locale(kundeDto
									.getPartnerDto()
									.getLocaleCNrKommunikation());
							mailtextDto.setMailVertreter(null);
							mailtextDto.setMailBelegdatum(new java.sql.Date(
									System.currentTimeMillis()));
							mailtextDto.setMailBelegnummer(null);
							mailtextDto
									.setMailBezeichnung(LPMain
											.getTextRespectUISPr("rech.mailbezeichnung.sammelmahnung")
											+ " " + sRechnungen);
							mailtextDto.setMailFusstext(null);
							mailtextDto.setMailPartnerIId(kundeDto
									.getPartnerIId());
							mailtextDto.setMailProjekt(null);
							mailtextDto.setMailText(null);
							mailtextDto.setParamLocale(locKunde);
							if (iMahnstufe != null) {
								String sBetreff = iMahnstufe
										+ ". "
										+ LPMain.getTextRespectUISPr("rech.mailbetreff.mahnung")
										+ " " + sRechnungen;
								mailtextDto.setMailBetreff(sBetreff);
							}

							String s = DelegateFactory.getInstance()
									.getVersandDelegate()
									.getDefaultTextForBelegEmail(mailtextDto);

							versDto.setCText(s);
						}

						DelegateFactory.getInstance().getVersandDelegate()
								.updateVersandauftrag(versDto, false);
					} else {
						sLieferantenNichtGemahnt += partnerDto
								.formatFixTitelName1Name2() + "\n";
					}
				}
				if (sLieferantenNichtGemahnt != null
						&& sLieferantenNichtGemahnt.length() > 0) {

					String sMessage = LPMain
							.getTextRespectUISPr("rech.mahnung.nichtgesendet")
							+ ":\n\n";
					sMessage += sLieferantenNichtGemahnt;
					sMessage += "\n"
							+ LPMain.getTextRespectUISPr("rech.mahnung.manuellmahnen");
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"), sMessage);
				}
			}
		}
	}
}
