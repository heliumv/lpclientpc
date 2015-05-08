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
package com.lp.client.finanz;

import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ArtikelFilterFactory;
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
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 22.06.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/08/06 07:42:39 $
 */
public class TabbedPaneWaehrung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryWaehrung = null;
	private PanelSplit panelSplitWaehrung = null;
	private PanelFinanzWaehrung panelWaehrung = null;

	private PanelQuery panelQueryWechselkurs = null;
	private PanelSplit panelSplitWechselkurs = null;
	private PanelFinanzWechselkurs panelWechselkurs = null;

	private PanelQuery panelQueryMahnspesen = null;
	private PanelSplit panelSplitMahnspesen = null;
	private PanelMahnspesen panelMahnspesen = null;

	private WaehrungDto waehrungDto = null;

	private final static int IDX_WAEHRUNG = 0;
	private final static int IDX_WECHSELKURS = 1;
	private final static int IDX_MAHNSPESEN = 2;

	public TabbedPaneWaehrung(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.waehrung"));
		jbInit();
		initComponents();
	}

	public WaehrungDto getWaehrungDto() {
		return waehrungDto;
	}

	public void setWaehrungDto(WaehrungDto waehrungDto) throws Throwable {
		this.waehrungDto = waehrungDto;
		if (waehrungDto != null) {
			StringBuffer sTitle = new StringBuffer();
			sTitle.append(waehrungDto.getCNr());
			if (waehrungDto.getCKommentar() != null) {
				sTitle.append(" " + waehrungDto.getCKommentar());
			}
			// getInternalFrame().setKeyWasForLockMe(waehrungDto.getCNr());
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					sTitle.toString());
			getPanelQueryWechselkurs(true).setDefaultFilter(
					FinanzFilterFactory.getInstance().createFKWechselkurs(
							waehrungDto.getCNr()));
		} else {
			getInternalFrame()
					.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		}
		// Wechselkurspanel nur fuer Mandantenwaehrung oder uebersteuerte
		// Mandantenwaehrung
		ParametermandantDto paramterWaehrungUebersteuert = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FINANZ,
						ParameterFac.PARAMETER_FINANZ_EXPORT_UEBERSTEUERTE_MANDANTENWAEHRUNG);
		String sWaehrung = paramterWaehrungUebersteuert.getCWert();
		if (waehrungDto != null
				&& (LPMain.getTheClient().getSMandantenwaehrung()
						.equals(this.waehrungDto.getCNr()) || sWaehrung
						.equals(this.waehrungDto.getCNr()))) {

			setEnabledAt(IDX_WECHSELKURS, true);
			setEnabledAt(IDX_MAHNSPESEN, false);

		} else {
			setEnabledAt(IDX_WECHSELKURS, false);
			setEnabledAt(IDX_MAHNSPESEN, true);
		}
	}

	private void jbInit() throws Throwable {
		// Tab 1: Kassenbuecher
		insertTab(LPMain.getTextRespectUISPr("lp.waehrung"), null, null,
				LPMain.getTextRespectUISPr("lp.waehrung"), IDX_WAEHRUNG);
		// Tab 2: Kopfdaten
		insertTab(LPMain.getTextRespectUISPr("lp.wechselkurs"), null, null,
				LPMain.getTextRespectUISPr("lp.wechselkurs"), IDX_WECHSELKURS);

		insertTab(LPMain.getTextRespectUISPr("fb.mahnspesen"), null, null,
				LPMain.getTextRespectUISPr("fb.mahnspesen"), IDX_MAHNSPESEN);

		setSelectedComponent(getPanelSplitWaehrung(true));
		// damit gleich eine selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(getPanelSplitWaehrung(true),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryWaehrung(true).getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			getPanelSplitWaehrung(true).eventYouAreSelected(false);

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryWaehrung(true)) {
				Object key = getPanelQueryWaehrung(true).getSelectedId();
				getPanelWaehrung(true).setKeyWhenDetailPanel(key);
				getInternalFrame().setKeyWasForLockMe("" + key);
				getPanelWaehrung(true).eventYouAreSelected(false);
				getPanelQueryWaehrung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryWechselkurs(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelWechselkurs(true).setKeyWhenDetailPanel(key);
				getPanelWechselkurs(true).eventYouAreSelected(false);
				getPanelQueryWechselkurs(true).updateButtons();
			} else if (e.getSource() == getPanelQueryMahnspesen(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelMahnspesen(true).setKeyWhenDetailPanel(key);
				getPanelMahnspesen(true).eventYouAreSelected(false);
				getPanelQueryMahnspesen(true).updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == getPanelWaehrung(true)) {
				getPanelQueryWaehrung(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelWechselkurs(false)) {
				getPanelQueryWechselkurs(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelMahnspesen(false)) {
				getPanelQueryMahnspesen(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryWaehrung(false)) {
				getPanelWaehrung(true).eventActionNew(e, true, false);
				getPanelWaehrung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryWechselkurs(false)) {
				getPanelWechselkurs(true).eventActionNew(e, true, false);
				getPanelWechselkurs(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryMahnspesen(false)) {
				getPanelMahnspesen(true).eventActionNew(e, true, false);
				getPanelMahnspesen(true).eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelWaehrung(true)) {
				getPanelSplitWaehrung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelWechselkurs(false)) {
				getPanelSplitWechselkurs(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelMahnspesen(false)) {
				getPanelSplitMahnspesen(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelWaehrung(true)) {
				Object oKey = getPanelWaehrung(true).getKeyWhenDetailPanel();
				getPanelQueryWaehrung(true).eventYouAreSelected(false);
				getPanelQueryWaehrung(true).setSelectedId(oKey);
				getPanelSplitWaehrung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelWechselkurs(false)) {
				Object oKey = getPanelWechselkurs(true).getKeyWhenDetailPanel();
				getPanelQueryWechselkurs(true).eventYouAreSelected(false);
				getPanelQueryWechselkurs(true).setSelectedId(oKey);
				getPanelSplitWechselkurs(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelMahnspesen(false)) {
				Object oKey = getPanelMahnspesen(true).getKeyWhenDetailPanel();
				getPanelQueryMahnspesen(true).eventYouAreSelected(false);
				getPanelQueryMahnspesen(true).setSelectedId(oKey);
				getPanelSplitMahnspesen(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelWaehrung(true)) {
				setKeyWasForLockMe();
				getPanelSplitWaehrung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelWechselkurs(false)) {
				setKeyWasForLockMe();
				getPanelSplitWechselkurs(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelMahnspesen(false)) {
				setKeyWasForLockMe();
				getPanelSplitMahnspesen(true).eventYouAreSelected(false);
			}
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		// lazy loading und titel setzen
		int index = this.getSelectedIndex();
		switch (index) {
		case IDX_WAEHRUNG: {
			getPanelSplitWaehrung(true).eventYouAreSelected(false);
			Object oKeyI = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant())
					.getWaehrungCNr();
			getPanelQueryWaehrung(true).setSelectedId(oKeyI);
			getPanelSplitWaehrung(true).eventYouAreSelected(true);
		}
			break;
		case IDX_WECHSELKURS: {
			getPanelSplitWechselkurs(true).eventYouAreSelected(false);
		}
			break;
		case IDX_MAHNSPESEN: {

			getPanelQueryMahnspesen(true);
			panelQueryMahnspesen.setDefaultFilter(FinanzFilterFactory
					.getInstance().createFKMahnspesen(
							getWaehrungDto().getCNr(),
							LPMain.getTheClient().getMandant()));

			getPanelSplitMahnspesen(true).eventYouAreSelected(false);
		}
			break;
		}
	}

	private PanelSplit getPanelSplitWaehrung(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitWaehrung == null && bNeedInstantiationIfNull) {
			panelSplitWaehrung = new PanelSplit(getInternalFrame(),
					getPanelWaehrung(true), getPanelQueryWaehrung(true), 200);
			this.setComponentAt(IDX_WAEHRUNG, panelSplitWaehrung);
		}
		return panelSplitWaehrung;
	}

	private PanelSplit getPanelSplitWechselkurs(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitWechselkurs == null && bNeedInstantiationIfNull) {
			panelSplitWechselkurs = new PanelSplit(getInternalFrame(),
					getPanelWechselkurs(true), getPanelQueryWechselkurs(true),
					200);
			this.setComponentAt(IDX_WECHSELKURS, panelSplitWechselkurs);
		}
		return panelSplitWechselkurs;
	}

	private PanelSplit getPanelSplitMahnspesen(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitMahnspesen == null && bNeedInstantiationIfNull) {
			panelSplitMahnspesen = new PanelSplit(getInternalFrame(),
					getPanelMahnspesen(true), getPanelQueryMahnspesen(true),
					300);
			this.setComponentAt(IDX_MAHNSPESEN, panelSplitMahnspesen);
		}
		return panelSplitMahnspesen;
	}

	private PanelFinanzWaehrung getPanelWaehrung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelWaehrung == null && bNeedInstantiationIfNull) {
			panelWaehrung = new PanelFinanzWaehrung(getInternalFrame(), "",
					null, this);
		}
		return panelWaehrung;
	}

	private PanelFinanzWechselkurs getPanelWechselkurs(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelWechselkurs == null && bNeedInstantiationIfNull) {
			panelWechselkurs = new PanelFinanzWechselkurs(getInternalFrame(),
					"", null, this);
		}
		return panelWechselkurs;
	}

	private PanelMahnspesen getPanelMahnspesen(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelMahnspesen == null && bNeedInstantiationIfNull) {
			panelMahnspesen = new PanelMahnspesen(getInternalFrame(), "", null,
					this);
		}
		return panelMahnspesen;
	}

	private PanelQuery getPanelQueryWaehrung(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryWaehrung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseWaehrung = { PanelBasis.ACTION_NEW };
			panelQueryWaehrung = new PanelQuery(null, null,
					QueryParameters.UC_ID_WAEHRUNG, aWhichButtonIUseWaehrung,
					getInternalFrame(), "", true);
		}
		return panelQueryWaehrung;
	}

	private PanelQuery getPanelQueryWechselkurs(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryWechselkurs == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseWechselkurs = { PanelBasis.ACTION_NEW };
			panelQueryWechselkurs = new PanelQuery(null, null,
					QueryParameters.UC_ID_WECHSELKURS,
					aWhichButtonIUseWechselkurs, getInternalFrame(), "", true);
			panelQueryWechselkurs.befuellePanelFilterkriterienDirekt(
					FinanzFilterFactory.getInstance()
							.createFKDWechselkursWaehrung(), null);
		}
		return panelQueryWechselkurs;
	}

	private PanelQuery getPanelQueryMahnspesen(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryMahnspesen == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseWechselkurs = { PanelBasis.ACTION_NEW };
			panelQueryMahnspesen = new PanelQuery(null, null,
					QueryParameters.UC_ID_MAHNSPESEN,
					aWhichButtonIUseWechselkurs, getInternalFrame(), "", true);

		}
		return panelQueryMahnspesen;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	public Object getDto() {
		return waehrungDto;
	}

}
