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
package com.lp.client.system;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.inserat.InseratFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Versandauftraege.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>06.06.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.8 $
 */

public class TabbedPaneVersandauftrag extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryPostausgang = null;
	private PanelQuery panelQueryGesendet = null;
	private PanelQuery panelQueryPapierkorb = null;
	private PanelQuery panelQueryFehlgeschlagen = null;
	private PanelVersandauftrag panelDetailPostausgang = null;
	private PanelVersandauftrag panelDetailGesendet = null;
	private PanelVersandauftrag panelDetailPapierkorb = null;
	private PanelVersandauftrag panelDetailFehlgeschlagen = null;
	private PanelSplit panelSplitPostausgang = null;
	private PanelSplit panelSplitGesendet = null;
	private PanelSplit panelSplitPapierkorb = null;
	private PanelSplit panelSplitFehlgeschlagen = null;

	private final static int IDX_POSTAUSGANG = 0;
	private final static int IDX_GESENDET = 1;
	private final static int IDX_PAPIERKORB = 2;
	private final static int IDX_FEHLGESCHLAGEN = 3;
	public static final String ACTION_SPECIAL_WEITERLEITEN = "action_special_weiterleiten";

	public TabbedPaneVersandauftrag(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.versandauftrag"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// Tab 1: Postausgang
		insertTab(
				LPMain.getTextRespectUISPr("lp.system.versand.tab.oben.postausgang.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("lp.system.versand.tab.oben.postausgang.tooltip"),
				IDX_POSTAUSGANG);
		// Tab 2: Gesendet
		insertTab(
				LPMain.getTextRespectUISPr("lp.system.versand.tab.oben.gesendet.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("lp.system.versand.tab.oben.gesendet.tooltip"),
				IDX_GESENDET);
		// Tab 3: Papierkorb
		insertTab(
				LPMain.getTextRespectUISPr("lp.system.versand.tab.oben.papierkorb.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("lp.system.versand.tab.oben.papierkorb.tooltip"),
				IDX_PAPIERKORB);
		// Tab 1: Fehlgeschlagen
		insertTab(
				LPMain.getTextRespectUISPr("lp.system.versand.tab.oben.fehlgeschlagen.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("lp.system.versand.tab.oben.fehlgeschlagen.tooltip"),
				IDX_FEHLGESCHLAGEN);
		setSelectedComponent(getPanelSplitPostausgang(true));
		// damit gleich eine selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(
				getPanelSplitPostausgang(true), ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private PanelQuery getPanelQueryPostausgang(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryPostausgang == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseQPTheJudge = { PanelBasis.ACTION_REFRESH };

			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKVersandauftrag(null);
			panelQueryPostausgang = new PanelQuery(null, filters,
					QueryParameters.UC_ID_VERSANDAUFTRAG,
					aWhichButtonIUseQPTheJudge, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.versandauftrag"), true);
			panelQueryPostausgang.createAndSaveAndShowButton(
					"/com/lp/client/res/mail_forward.png",
					LPMain.getTextRespectUISPr("lp.weiterleiten"),
					PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_WEITERLEITEN,
					RechteFac.RECHT_LP_SYSTEM_CUD);
			panelQueryPostausgang.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBetreff(),
					SystemFilterFactory.getInstance().createFKDEmpfaenger());
		}
		return panelQueryPostausgang;
	}

	private PanelQuery getPanelQueryGesendet(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryGesendet == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseQPTheJudge = { PanelBasis.ACTION_REFRESH };
			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKVersandauftrag(VersandFac.STATUS_ERLEDIGT);
			panelQueryGesendet = new PanelQuery(null, filters,
					QueryParameters.UC_ID_VERSANDAUFTRAG,
					aWhichButtonIUseQPTheJudge, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.versandauftrag"), true);
			panelQueryGesendet.createAndSaveAndShowButton(
					"/com/lp/client/res/mail_forward.png",
					LPMain.getTextRespectUISPr("lp.weiterleiten"),
					PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_WEITERLEITEN,
					RechteFac.RECHT_LP_SYSTEM_CUD);

			panelQueryGesendet.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBetreff(),
					SystemFilterFactory.getInstance().createFKDEmpfaenger());

		}
		return panelQueryGesendet;
	}

	public PanelQuery getPanelQueryPapierkorb(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryPapierkorb == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseQPTheJudge = { PanelBasis.ACTION_REFRESH };

			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKVersandauftrag(VersandFac.STATUS_STORNIERT);
			panelQueryPapierkorb = new PanelQuery(null, filters,
					QueryParameters.UC_ID_VERSANDAUFTRAG,
					aWhichButtonIUseQPTheJudge, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.versandauftrag"), true);
			panelQueryPapierkorb.createAndSaveAndShowButton(
					"/com/lp/client/res/mail_forward.png",
					LPMain.getTextRespectUISPr("lp.weiterleiten"),
					PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_WEITERLEITEN,
					RechteFac.RECHT_LP_SYSTEM_CUD);
			panelQueryPapierkorb.setMultipleRowSelectionEnabled(true);
			panelQueryPapierkorb.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBetreff(),
					SystemFilterFactory.getInstance().createFKDEmpfaenger());
		}
		return panelQueryPapierkorb;
	}

	public PanelQuery getPanelQueryFehlgeschlagen(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryFehlgeschlagen == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseQPTheJudge = { PanelBasis.ACTION_REFRESH };

			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKVersandauftrag(VersandFac.STATUS_FEHLGESCHLAGEN);
			panelQueryFehlgeschlagen = new PanelQuery(null, filters,
					QueryParameters.UC_ID_VERSANDAUFTRAG,
					aWhichButtonIUseQPTheJudge, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.versandauftrag"), true);
			panelQueryFehlgeschlagen.createAndSaveAndShowButton(
					"/com/lp/client/res/mail_forward.png",
					LPMain.getTextRespectUISPr("lp.weiterleiten"),
					PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_WEITERLEITEN,
					RechteFac.RECHT_LP_SYSTEM_CUD);
			panelQueryFehlgeschlagen.setMultipleRowSelectionEnabled(true);
			panelQueryFehlgeschlagen.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBetreff(),
					SystemFilterFactory.getInstance().createFKDEmpfaenger());
		}
		return panelQueryFehlgeschlagen;
	}

	private PanelVersandauftrag getPanelDetailPostausgang(
			boolean bNeedInstatiationIfNull) throws Throwable {
		if (panelDetailPostausgang == null && bNeedInstatiationIfNull) {
			panelDetailPostausgang = new PanelVersandauftrag(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.versandauftrag"), null,
					this, PanelVersandauftrag.I_ASPECT_POSTAUSGANG);
		}
		return panelDetailPostausgang;
	}

	private PanelVersandauftrag getPanelDetailGesendet(
			boolean bNeedInstatiationIfNull) throws Throwable {
		if (panelDetailGesendet == null && bNeedInstatiationIfNull) {
			panelDetailGesendet = new PanelVersandauftrag(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.versandauftrag"), null,
					this, PanelVersandauftrag.I_ASPECT_GESENDET);
		}
		return panelDetailGesendet;
	}

	private PanelVersandauftrag getPanelDetailPapierkorb(
			boolean bNeedInstatiationIfNull) throws Throwable {
		if (panelDetailPapierkorb == null && bNeedInstatiationIfNull) {
			panelDetailPapierkorb = new PanelVersandauftrag(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.versandauftrag"), null,
					this, PanelVersandauftrag.I_ASPECT_PAPIERKORB);
		}
		return panelDetailPapierkorb;
	}

	private PanelVersandauftrag getPanelDetailFehlgeschlagen(
			boolean bNeedInstatiationIfNull) throws Throwable {
		if (panelDetailFehlgeschlagen == null && bNeedInstatiationIfNull) {
			panelDetailFehlgeschlagen = new PanelVersandauftrag(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.versandauftrag"), null,
					this, PanelVersandauftrag.I_ASPECT_FEHLGESCHLAGEN);
		}
		return panelDetailFehlgeschlagen;
	}

	private PanelSplit getPanelSplitPostausgang(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitPostausgang == null && bNeedInstantiationIfNull) {
			panelSplitPostausgang = new PanelSplit(getInternalFrame(),
					getPanelDetailPostausgang(true),
					getPanelQueryPostausgang(true), 190);
			this.setComponentAt(IDX_POSTAUSGANG, panelSplitPostausgang);
		}
		return panelSplitPostausgang;
	}

	private PanelSplit getPanelSplitGesendet(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitGesendet == null && bNeedInstantiationIfNull) {
			panelSplitGesendet = new PanelSplit(getInternalFrame(),
					getPanelDetailGesendet(true), getPanelQueryGesendet(true),
					190);
			this.setComponentAt(IDX_GESENDET, panelSplitGesendet);
		}
		return panelSplitGesendet;
	}

	private PanelSplit getPanelSplitPapierkorb(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitPapierkorb == null && bNeedInstantiationIfNull) {
			panelSplitPapierkorb = new PanelSplit(getInternalFrame(),
					getPanelDetailPapierkorb(true),
					getPanelQueryPapierkorb(true), 190);
			this.setComponentAt(IDX_PAPIERKORB, panelSplitPapierkorb);
		}
		return panelSplitPapierkorb;
	}

	private PanelSplit getPanelSplitFehlgeschlagen(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitFehlgeschlagen == null && bNeedInstantiationIfNull) {
			panelSplitFehlgeschlagen = new PanelSplit(getInternalFrame(),
					getPanelDetailFehlgeschlagen(true),
					getPanelQueryFehlgeschlagen(true), 190);
			this.setComponentAt(IDX_FEHLGESCHLAGEN, panelSplitFehlgeschlagen);
		}
		return panelSplitFehlgeschlagen;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_POSTAUSGANG: {
			getPanelSplitPostausgang(true).eventYouAreSelected(false);
		}
			break;
		case IDX_GESENDET: {
			getPanelSplitGesendet(true).eventYouAreSelected(false);
		}
			break;
		case IDX_PAPIERKORB: {
			getPanelSplitPapierkorb(true).eventYouAreSelected(false);
		}
			break;
		case IDX_FEHLGESCHLAGEN: {
			getPanelSplitFehlgeschlagen(true).eventYouAreSelected(false);
		}
			break;
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == getPanelQueryPostausgang(false)) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				getPanelDetailPostausgang(true).setKeyWhenDetailPanel(key);
				getPanelDetailPostausgang(true).eventYouAreSelected(false);
				getPanelQueryPostausgang(true).updateButtons();
			} else if (eI.getSource() == getPanelQueryGesendet(false)) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				getPanelDetailGesendet(true).setKeyWhenDetailPanel(key);
				getPanelDetailGesendet(true).eventYouAreSelected(false);
				getPanelQueryGesendet(true).updateButtons();
			} else if (eI.getSource() == getPanelQueryPapierkorb(false)) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				getPanelDetailPapierkorb(true).setKeyWhenDetailPanel(key);
				getPanelDetailPapierkorb(true).eventYouAreSelected(false);
				getPanelQueryPapierkorb(true).updateButtons();
			} else if (eI.getSource() == getPanelQueryFehlgeschlagen(false)) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				getPanelDetailFehlgeschlagen(true).setKeyWhenDetailPanel(key);
				getPanelDetailFehlgeschlagen(true).eventYouAreSelected(false);
				getPanelQueryFehlgeschlagen(true).updateButtons();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == getPanelDetailPostausgang(false)) {
				Object oKey = getPanelQueryPostausgang(true).getSelectedId();
				getInternalFrame().setKeyWasForLockMe(
						(oKey == null) ? null : oKey + "");
				getPanelSplitPostausgang(true).eventYouAreSelected(false);

			} else if (eI.getSource() == getPanelDetailGesendet(false)) {
				Object oKey = getPanelQueryGesendet(true).getSelectedId();
				getInternalFrame().setKeyWasForLockMe(
						(oKey == null) ? null : oKey + "");
				getPanelSplitGesendet(true).eventYouAreSelected(false);

			} else if (eI.getSource() == getPanelDetailPapierkorb(false)) {
				Object oKey = getPanelQueryPapierkorb(true).getSelectedId();
				getInternalFrame().setKeyWasForLockMe(
						(oKey == null) ? null : oKey + "");
				getPanelSplitPapierkorb(true).eventYouAreSelected(false);

			} else if (eI.getSource() == getPanelDetailFehlgeschlagen(false)) {
				Object oKey = getPanelQueryFehlgeschlagen(true).getSelectedId();
				getInternalFrame().setKeyWasForLockMe(
						(oKey == null) ? null : oKey + "");
				getPanelSplitFehlgeschlagen(true).eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {

			boolean bDarfAndereVeraendern = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_ALLE_VERSANDAUFTRAEGE_UD);

			if (eI.getSource() == getPanelQueryPostausgang(false)) {
				VersandauftragDto vsDto = getPanelDetailPostausgang(true)
						.getVersandauftragDto();

				if (vsDto != null
						&& !LPMain.getTheClient().getIDPersonal()
								.equals(vsDto.getPersonalIId())
						&& bDarfAndereVeraendern == false) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("system.error.versandauftrag.bearbeiten"));
					return;
				}

				if (getPanelQueryPostausgang(true).getSelectedId() == null) {
					return;
				}
				getPanelDetailPostausgang(true).eventActionNew(eI, true, false);
				LockStateValue lockstateValue = new LockStateValue(null, null,
						PanelBasis.LOCK_FOR_NEW);
				getPanelDetailPostausgang(true).updateButtons(lockstateValue);
				getPanelQueryPostausgang(true).updateButtons(lockstateValue);

			} else if (eI.getSource() == getPanelQueryFehlgeschlagen(false)) {
				VersandauftragDto vsDto = getPanelDetailFehlgeschlagen(true)
						.getVersandauftragDto();

				if (vsDto != null
						&& !LPMain.getTheClient().getIDPersonal()
								.equals(vsDto.getPersonalIId())
						&& bDarfAndereVeraendern == false) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("system.error.versandauftrag.bearbeiten"));
					return;
				}

				if (getPanelQueryFehlgeschlagen(true).getSelectedId() == null) {
					return;
				}
				getPanelDetailFehlgeschlagen(true).eventActionNew(eI, true,
						false);
				LockStateValue lockstateValue = new LockStateValue(null, null,
						PanelBasis.LOCK_FOR_NEW);
				getPanelDetailFehlgeschlagen(true)
						.updateButtons(lockstateValue);
				getPanelQueryFehlgeschlagen(true).updateButtons(lockstateValue);

			} else if (eI.getSource() == getPanelQueryGesendet(false)) {
				VersandauftragDto vsDto = getPanelDetailGesendet(true)
						.getVersandauftragDto();

				if (vsDto != null
						&& !LPMain.getTheClient().getIDPersonal()
								.equals(vsDto.getPersonalIId())
						&& bDarfAndereVeraendern == false) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("system.error.versandauftrag.bearbeiten"));
					return;
				}

				if (getPanelQueryGesendet(true).getSelectedId() == null) {
					return;
				}
				getPanelDetailGesendet(true).eventActionNew(eI, true, false);
				LockStateValue lockstateValue = new LockStateValue(null, null,
						PanelBasis.LOCK_FOR_NEW);
				getPanelDetailGesendet(true).updateButtons(lockstateValue);
				getPanelQueryGesendet(true).updateButtons(lockstateValue);

			} else if (eI.getSource() == getPanelQueryPapierkorb(false)) {
				VersandauftragDto vsDto = getPanelDetailPapierkorb(true)
						.getVersandauftragDto();

				if (vsDto != null
						&& !LPMain.getTheClient().getIDPersonal()
								.equals(vsDto.getPersonalIId())
						&& bDarfAndereVeraendern == false) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("system.error.versandauftrag.bearbeiten"));
					return;
				}

				if (getPanelQueryPapierkorb(true).getSelectedId() == null) {
					return;
				}
				getPanelDetailPapierkorb(true).eventActionNew(eI, true, false);
				LockStateValue lockstateValue = new LockStateValue(null, null,
						PanelBasis.LOCK_FOR_NEW);
				getPanelDetailPapierkorb(true).updateButtons(lockstateValue);
				getPanelQueryPapierkorb(true).updateButtons(lockstateValue);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == getPanelDetailPostausgang(false)) {
				getPanelDetailPostausgang(true).eventYouAreSelected(false);
			} else if (eI.getSource() == getPanelDetailFehlgeschlagen(false)) {
				getPanelDetailFehlgeschlagen(true).eventYouAreSelected(false);
			} else if (eI.getSource() == getPanelDetailGesendet(false)) {
				getPanelDetailGesendet(true).eventYouAreSelected(false);
			} else if (eI.getSource() == getPanelDetailPapierkorb(false)) {
				getPanelDetailPapierkorb(true).eventYouAreSelected(false);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == getPanelDetailPostausgang(false)) {
				Object key = getPanelDetailPostausgang(true)
						.getKeyWhenDetailPanel();
				getPanelSplitPostausgang(true).eventYouAreSelected(false);
				getPanelQueryPostausgang(true).setSelectedId(key);
				getPanelSplitPostausgang(true).eventYouAreSelected(false);

			} else if (eI.getSource() == getPanelDetailFehlgeschlagen(false)) {
				Object key = getPanelDetailFehlgeschlagen(true)
						.getKeyWhenDetailPanel();
				getPanelSplitFehlgeschlagen(true).eventYouAreSelected(false);
				getPanelQueryFehlgeschlagen(true).setSelectedId(key);
				getPanelSplitFehlgeschlagen(true).eventYouAreSelected(false);

			} else if (eI.getSource() == getPanelDetailGesendet(false)) {
				Object key = getPanelDetailGesendet(true)
						.getKeyWhenDetailPanel();
				getPanelSplitGesendet(true).eventYouAreSelected(false);
				getPanelQueryGesendet(true).setSelectedId(key);
				getPanelSplitGesendet(true).eventYouAreSelected(false);

			} else if (eI.getSource() == getPanelQueryPapierkorb(false)) {
				Object key = getPanelDetailPapierkorb(true)
						.getKeyWhenDetailPanel();
				getPanelSplitPapierkorb(true).eventYouAreSelected(false);
				getPanelQueryPapierkorb(true).setSelectedId(key);
				getPanelSplitPapierkorb(true).eventYouAreSelected(false);
			}
		}
	}
}
