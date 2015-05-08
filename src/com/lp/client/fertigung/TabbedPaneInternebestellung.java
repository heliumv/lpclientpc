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
package com.lp.client.fertigung;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.InternebestellungDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Interne Bestellung </I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>03.12.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.8 $
 */
public class TabbedPaneInternebestellung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryInternebestellung = null;
	private PanelSplit panelSplitInternebestellung = null;
	private PanelInternebestellung panelDetailInternebestellung = null;

	private PanelTabelleBewegungsvorschau panelTabelleBewegungsvorschau = null;

	private InternebestellungDto internebestellungDto = null;
	private StuecklisteDto stuecklisteDto = null;

	private PanelDialogKriterienInternebestellung panelDialogKriterienInternebestellung = null;

	private static final int IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG = 0;
	private static final int IDX_PANEL_BEWEGUNGSVORSCHAU = 1;

	private static final String ACTION_SPECIAL_NEUE_INTERNEBESTELLUNG = PanelBasis.ALWAYSENABLED
			+ "action_special_neue_internebestellung";
	private static final String ACTION_SPECIAL_LOSE_ANLEGEN = PanelBasis.ALWAYSENABLED
			+ "action_special_lose_anlegen";
	private static final String ACTION_SPECIAL_VERDICHTEN = PanelBasis.ALWAYSENABLED
			+ "action_special_verdichten";

	private boolean bInterneBestellungVerdichtenMitRahmenpruefung = false;

	public TabbedPaneInternebestellung(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("fert.tab.oben.internebestellung.title"));

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_INT_BEST_VERDICHTEN_RAHMENPRUEFUNG);
		bInterneBestellungVerdichtenMitRahmenpruefung = (java.lang.Boolean) parameter
				.getCWertAsObject();

		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		insertTab(
				LPMain.getTextRespectUISPr("fert.tab.oben.internebestellung.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("fert.tab.oben.internebestellung.tooltip"),
				IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG);

		insertTab(
				LPMain.getTextRespectUISPr("fert.tab.oben.bewegungsvorschau.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("fert.tab.oben.bewegungsvorschau.tooltip"),
				IDX_PANEL_BEWEGUNGSVORSCHAU);

		getPanelSplitInternebestellung(true);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	protected InternebestellungDto getInternebestellungDto() {
		return internebestellungDto;
	}

	protected void setInternebestellungDto(
			InternebestellungDto internebestellungDto) throws Throwable {
		this.internebestellungDto = internebestellungDto;
		if (internebestellungDto != null) {
			refreshFilterBewegungsvorschau();
			// Stueckliste nur dann neu laden, wenn sie nicht eh schon da ist.
			if (this.getStuecklisteDto() == null
					|| !this.getStuecklisteDto().getIId()
							.equals(internebestellungDto.getStuecklisteIId())) {
				this.setStuecklisteDto(DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(
								internebestellungDto.getStuecklisteIId()));
			}
		}
	}

	private void refreshFilterBewegungsvorschau() throws Throwable {
		if (getInternebestellungDto() != null) {
			if (getPanelTabelleBewegungsvorschau(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKBewegungsvorschau(
								getStuecklisteDto().getArtikelIId(), true);
				getPanelTabelleBewegungsvorschau(true).setDefaultFilter(krit);
			}
		}
	}

	protected void setStuecklisteDto(StuecklisteDto stuecklisteDto) {
		this.stuecklisteDto = stuecklisteDto;
	}

	protected StuecklisteDto getStuecklisteDto() {
		return stuecklisteDto;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (panelDialogKriterienInternebestellung != null) {
			panelDialogKriterienInternebestellung.eventItemchanged(e);
		}

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == getPanelQueryInternebestellung(false)) {
				// bei Doppelklick auf die Bewegungsvorschau wechseln
				setSelectedComponent(getPanelTabelleBewegungsvorschau(true));
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryInternebestellung(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG, true);
				}
				//
				holeInternebestellungDto(key);
				getPanelDetailInternebestellung(true)
						.eventYouAreSelected(false);
				// Buttons richtig schalten
				getPanelQueryInternebestellung(true).updateButtons();

				// Ueberleitung nur aktiviert wenn nicht leer
				LPButtonAction item = (LPButtonAction) getPanelQueryInternebestellung(
						true).getHmOfButtons().get(ACTION_SPECIAL_LOSE_ANLEGEN);
				if (getPanelQueryInternebestellung(true).getTable()
						.getRowCount() > 0) {
					item.getButton().setEnabled(true);
				} else {
					item.getButton().setEnabled(false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == getPanelDetailInternebestellung(false)) {
				// im QP die Buttons in den Zustand neu setzen.
				getPanelQueryInternebestellung(false).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelDetailInternebestellung(false)) {
				getPanelSplitInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelDetailInternebestellung(false)) {
				Object oKey = getPanelDetailInternebestellung(true)
						.getKeyWhenDetailPanel();
				getPanelQueryInternebestellung(true).eventYouAreSelected(false);
				getPanelQueryInternebestellung(true).setSelectedId(oKey);
				getPanelSplitInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelDetailInternebestellung(false)) {
				setKeyWasForLockMe();

				if (getPanelDetailInternebestellung(true)
						.getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryInternebestellung(true)
							.getId2SelectAfterDelete();
					getPanelQueryInternebestellung(true).setSelectedId(
							oNaechster);
				}

				getPanelSplitInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryInternebestellung(true)) {
				getPanelDetailInternebestellung(true).eventActionNew(e, true,
						false);
				getPanelDetailInternebestellung(true)
						.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getPanelDialogKriterienIB()) {
				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.erzeugeInterneBestellung(
								getPanelDialogKriterienIB()
										.getVorhandeneLoeschen(),
								getPanelDialogKriterienIB()
										.getAuftragsvorlaufzeit(),
								getPanelDialogKriterienIB().getToleranz(),
								getPanelDialogKriterienIB()
										.getLieferterminFuerArtikelOhneReservierung(),
								getPanelDialogKriterienIB().getBVerdichten(),
								getPanelDialogKriterienIB()
										.getVerdichtungstage(),
								getPanelDialogKriterienIB().getLosIId());
				getPanelQueryInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.equals(ACTION_SPECIAL_NEUE_INTERNEBESTELLUNG)) {
				getInternalFrame().showPanelDialog(getPanelDialogKriterienIB());
			} else if (sAspectInfo.equals(ACTION_SPECIAL_LOSE_ANLEGEN)) {

				// PJ18367

				Set<Integer> setIIds = null;

				int indexAlle = 0;
				int indexMarkierte = 1;
				int iAnzahlOptionen = 2;

				Object[] aOptionenVerdichten = new Object[iAnzahlOptionen];

				aOptionenVerdichten[indexAlle] = LPMain
						.getTextRespectUISPr("fert.internebestellung.loseanlegen.alle");
				aOptionenVerdichten[indexMarkierte] = LPMain
						.getTextRespectUISPr("fert.internebestellung.loseanlegen.markierte");

				int iAuswahl = DialogFactory
						.showModalDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("fert.internebestellung.loseanlegen.frage"),
								LPMain.getTextRespectUISPr("lp.frage"),
								aOptionenVerdichten, aOptionenVerdichten[0]);

				if (iAuswahl == indexAlle) {
					setIIds = DelegateFactory.getInstance()
							.getFertigungDelegate()
							.getInternebestellungIIdsEinesMandanten();
				} else if (iAuswahl == indexMarkierte) {
					setIIds = new HashSet<Integer>();

					Object[] ids = getPanelQueryInternebestellung(true)
							.getSelectedIds();
					for (int i = 0; i < ids.length; i++) {
						setIIds.add((Integer) ids[i]);
					}

				}

				if (setIIds != null) {
					for (Iterator<?> iter = setIIds.iterator(); iter.hasNext();) {
						Integer ibIId = (Integer) iter.next();
						DelegateFactory.getInstance().getFertigungDelegate()
								.interneBestellungUeberleiten(ibIId);
					}
				}
				setInternebestellungDto(null);
				getPanelQueryInternebestellung(true).eventYouAreSelected(false);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_VERDICHTEN)) {

				// PJ18367
				int indexAlle = 0;
				int indexMarkierte = 1;
				int iAnzahlOptionen = 2;

				Object[] aOptionenVerdichten = new Object[iAnzahlOptionen];

				aOptionenVerdichten[indexAlle] = LPMain
						.getTextRespectUISPr("fert.internebestellung.verdichten.alle");
				aOptionenVerdichten[indexMarkierte] = LPMain
						.getTextRespectUISPr("fert.internebestellung.verdichten.markierte");

				int iAuswahl = DialogFactory
						.showModalDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("fert.internebestellung.verdichten.frage"),
								LPMain.getTextRespectUISPr("lp.frage"),
								aOptionenVerdichten, aOptionenVerdichten[0]);

				if (iAuswahl == indexAlle) {
					DelegateFactory.getInstance().getFertigungDelegate()
							.verdichteInterneBestellung((Integer) null);

					if (bInterneBestellungVerdichtenMitRahmenpruefung == true) {
						ArrayList<?> al = DelegateFactory.getInstance()
								.getFertigungDelegate()
								.pruefeOffeneRahmenmengen();

						Object[] aOptionen = new Object[3];
						aOptionen[0] = "Trotzdem \u00FCbernehmen";
						aOptionen[1] = "Restrahmen \u00FCbernehmen";
						aOptionen[2] = "Nicht \u00FCbernehmen";
						String nachricht = "";
						for (int i = 0; i < al.size(); i++) {
							Object[] oTemp = (Object[]) al.get(i);

							nachricht += "Bei "
									+ (String) oTemp[0]
									+ " ist "
									+ oTemp[4]
									+ " von "
									+ Helper.rundeKaufmaennisch(
											(BigDecimal) oTemp[1], 3)
									+ "eingetragen. Es besteht aber "
									+ /* kein / */"ein zu kleiner Bedarf"/*
																		 * von "
																		 * +
																		 * Helper
																		 * .
																		 * rundeKaufmaennisch
																		 * ( (
																		 * BigDecimal
																		 * )
																		 * oTemp
																		 * [2],
																		 * 3)
																		 */
									+ " daf\u00FCr.\n";

						}
						if (!nachricht.equals("")) {
							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hinweis"),
									nachricht);
						}
					}

				} else if (iAuswahl == indexMarkierte) {
					HashSet<Integer> set = new HashSet<Integer>();
					Object[] ids = getPanelQueryInternebestellung(true)
							.getSelectedIds();
					for (int i = 0; i < ids.length; i++) {

						InternebestellungDto ibDto = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.internebestellungFindByPrimaryKey(
										(Integer) ids[i]);

						set.add(ibDto.getStuecklisteIId());

					}

					DelegateFactory.getInstance().getFertigungDelegate()
							.verdichteInterneBestellung(set);
				}

				getPanelQueryInternebestellung(true).eventYouAreSelected(false);
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
		case IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG: {
			getPanelSplitInternebestellung(true).eventYouAreSelected(false);
		}
			break;
		case IDX_PANEL_BEWEGUNGSVORSCHAU: {
			getPanelTabelleBewegungsvorschau(true).eventYouAreSelected(false);
			getPanelTabelleBewegungsvorschau(true).updateButtons(
					new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));
		}
			break;
		}

	}

	/**
	 * Diese Methode setzt die aktuelle Interne Bestellung aus der Auswahlliste
	 * als die zu lockende Bestellung.
	 * 
	 * @throws Throwable
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryInternebestellung(true).getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	private PanelInternebestellung getPanelDetailInternebestellung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailInternebestellung == null && bNeedInstantiationIfNull) {
			panelDetailInternebestellung = new PanelInternebestellung(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.unten.internebestellung.title"),
					null, // eventuell gibt es noch keine Position
					this);
		}
		return panelDetailInternebestellung;
	}

	public PanelQuery getPanelQueryInternebestellung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryInternebestellung == null && bNeedInstantiationIfNull) {
			FilterKriterium[] fkInternebestellung = SystemFilterFactory
					.getInstance().createFKMandantCNr();
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryInternebestellung = new PanelQuery(
					null,
					fkInternebestellung,
					QueryParameters.UC_ID_INTERNEBESTELLUNG,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.title.panel.internebestellung"),
					true); // flag, damit flr erst beim aufruf des panels
							// loslaeuft

			panelQueryInternebestellung
					.createAndSaveAndShowButton(
							"/com/lp/client/res/clipboard.png",
							LPMain.getTextRespectUISPr("fert.internebestellungdurchfuehren"),
							ACTION_SPECIAL_NEUE_INTERNEBESTELLUNG,
							RechteFac.RECHT_FERT_LOS_CUD);

			String textVerdichten = LPMain
					.getTextRespectUISPr("fert.internebestellungverdichten");
			if (bInterneBestellungVerdichtenMitRahmenpruefung == true) {
				textVerdichten += " + "
						+ LPMain.getTextRespectUISPr("fert.pruefeoffenerahmenmengen");
			}

			panelQueryInternebestellung.createAndSaveAndShowButton(
					"/com/lp/client/res/branch.png", textVerdichten,
					ACTION_SPECIAL_VERDICHTEN, RechteFac.RECHT_FERT_LOS_CUD);

			panelQueryInternebestellung
					.createAndSaveAndShowButton(
							"/com/lp/client/res/clipboard_next.png",
							LPMain.getTextRespectUISPr("fert.internebestellungloseanglegen"),
							ACTION_SPECIAL_LOSE_ANLEGEN,
							RechteFac.RECHT_FERT_LOS_CUD);

			FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory
					.getInstance().createFKDArtikelnummerInterneBestellung();

			panelQueryInternebestellung.befuellePanelFilterkriterienDirekt(
					fkDirekt1, null);
			panelQueryInternebestellung.setMultipleRowSelectionEnabled(true);

		}
		return panelQueryInternebestellung;
	}

	private PanelSplit getPanelSplitInternebestellung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitInternebestellung == null && bNeedInstantiationIfNull) {
			panelSplitInternebestellung = new PanelSplit(getInternalFrame(),
					getPanelDetailInternebestellung(true),
					getPanelQueryInternebestellung(true), 240);
			setComponentAt(IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG,
					panelSplitInternebestellung);
		}
		return panelSplitInternebestellung;
	}

	private PanelTabelle getPanelTabelleBewegungsvorschau(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelTabelleBewegungsvorschau == null && bNeedInstantiationIfNull) {
			panelTabelleBewegungsvorschau = new PanelTabelleBewegungsvorschau(
					QueryParameters.UC_ID_BEWEGUNGSVORSCHAU2,
					LPMain.getTextRespectUISPr("fert.tab.oben.bewegungsvorschau.title"),
					getInternalFrame());

			setComponentAt(IDX_PANEL_BEWEGUNGSVORSCHAU,
					panelTabelleBewegungsvorschau);
			if (getStuecklisteDto() != null) {
				panelTabelleBewegungsvorschau
						.setDefaultFilter(FertigungFilterFactory.getInstance()
								.createFKBewegungsvorschau(
										getStuecklisteDto().getArtikelIId(),
										true));
			}
		}
		return panelTabelleBewegungsvorschau;
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	public Integer getSelectedIIdInternebestellung() throws Throwable {
		return (Integer) getPanelQueryInternebestellung(true).getSelectedId();
	}

	/**
	 * hole EingangsrechnungDto.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeInternebestellungDto(Object key) throws Throwable {
		if (key != null) {
			InternebestellungDto ibDto = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.internebestellungFindByPrimaryKey((Integer) key);
			setInternebestellungDto(ibDto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
			if (getPanelDetailInternebestellung(false) != null) {
				getPanelDetailInternebestellung(true)
						.setKeyWhenDetailPanel(key);
			}
		} else {
			setInternebestellungDto(null);
		}
	}

	private PanelDialogKriterienInternebestellung getPanelDialogKriterienIB()
			throws Throwable {
		if (panelDialogKriterienInternebestellung == null) {
			panelDialogKriterienInternebestellung = new PanelDialogKriterienInternebestellung(
					getInternalFrame(), "");
		}
		return panelDialogKriterienInternebestellung;
	}

	public Object getDto() {
		return internebestellungDto;
	}
}
