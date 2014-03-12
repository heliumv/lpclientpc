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
package com.lp.client.artikel;

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
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class TabbedPaneMaterial extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryMaterial = null;
	private PanelQuery panelQueryMaterialzuschlag = null;
	private PanelBasis panelDetailMaterial = null;
	private PanelBasis panelSplit = null;
	private PanelBasis panelDetailMaterialzuschlag = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_ZUSCHLAG = 2;

	public TabbedPaneMaterial(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"label.material"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryMaterial() {
		return panelQueryMaterial;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryMaterial == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryMaterial = new PanelQuery(null, null,
					QueryParameters.UC_ID_MATERIAL, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryMaterial.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance()
							.createFKDSprTabelleBezeichnung(
									MaterialFac.FLR_MATERIAL_MATERIALSPRSET));

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryMaterial);

		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailMaterial == null) {
			panelDetailMaterial = new PanelMaterial(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailMaterial);
		}
	}

	private void createMaterialzuschlag(Integer key) throws Throwable {

		if (panelQueryMaterialzuschlag == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKMaterialzuschlag(key);

			panelQueryMaterialzuschlag = new PanelQuery(null, filters,
					QueryParameters.UC_ID_MATERIALZUSCHLAG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.zuschlag"), true);

			panelDetailMaterialzuschlag = new PanelMaterialzuschlag(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.zuschlag"), null);

			panelSplit = new PanelSplit(getInternalFrame(),
					panelDetailMaterialzuschlag, panelQueryMaterialzuschlag,
					350);

			setComponentAt(IDX_PANEL_ZUSCHLAG, panelSplit);
		} else {
			// filter refreshen.
			panelQueryMaterialzuschlag.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKMaterialzuschlag(key));
		}
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_DETAIL);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_MATERIALZUSCHLAG)) {

			insertTab(LPMain.getInstance()
					.getTextRespectUISPr("label.zuschlag"), null, null, LPMain
					.getInstance().getTextRespectUISPr("label.zuschlag"),
					IDX_PANEL_ZUSCHLAG);
		}

		createAuswahl();

		panelQueryMaterial.eventYouAreSelected(false);
		if ((Integer) panelQueryMaterial.getSelectedId() != null) {
			getInternalFrameArtikel().materialDto = DelegateFactory
					.getInstance()
					.getMaterialDelegate()
					.materialFindByPrimaryKey(
							(Integer) panelQueryMaterial.getSelectedId());
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryMaterial,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryMaterial) {
				Integer iId = (Integer) panelQueryMaterial.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailMaterial);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailMaterialzuschlag) {
				panelDetailMaterialzuschlag.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailMaterial) {
				panelDetailMaterial.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailMaterialzuschlag) {
				panelQueryMaterialzuschlag.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailMaterial) {
				panelQueryMaterial.clearDirektFilter();
				Object oKey = panelDetailMaterial.getKeyWhenDetailPanel();
				panelQueryMaterial.setSelectedId(oKey);
			}

			if (e.getSource() == panelDetailMaterialzuschlag) {
				Object oKey = panelDetailMaterialzuschlag
						.getKeyWhenDetailPanel();
				panelQueryMaterialzuschlag.eventYouAreSelected(false);
				panelQueryMaterialzuschlag.setSelectedId(oKey);
				panelSplit.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryMaterialzuschlag) {
				Integer iId = (Integer) panelQueryMaterialzuschlag
						.getSelectedId();
				panelDetailMaterialzuschlag.setKeyWhenDetailPanel(iId);
				panelDetailMaterialzuschlag.eventYouAreSelected(false);
				panelQueryMaterialzuschlag.updateButtons();
			} else if (e.getSource() == panelQueryMaterial) {
				if (panelQueryMaterial.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					getInternalFrameArtikel().setKeyWasForLockMe(key + "");

					getInternalFrameArtikel().materialDto = DelegateFactory
							.getInstance().getMaterialDelegate()
							.materialFindByPrimaryKey((Integer) key);
					String sBezeichnung = "";
					if (getInternalFrameArtikel().materialDto
							.getMaterialsprDto() != null) {
						sBezeichnung = getInternalFrameArtikel().materialDto
								.getMaterialsprDto().getCBez() + "";
					}
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							LPMain.getInstance().getTextRespectUISPr(
									"label.material")
									+ ": "
									+ getInternalFrameArtikel().materialDto
											.getCNr()
									+ ", "
									+ LPMain.getInstance().getTextRespectUISPr(
											"label.bezeichnung")
									+ ": "
									+ sBezeichnung);

					if (panelQueryMaterial.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, true);
					}
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailMaterial) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryMaterial);
				setKeyWasForLockMe();
				panelQueryMaterial.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailMaterialzuschlag) {
				setKeyWasForLockMe();
				if (panelDetailMaterialzuschlag.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMaterialzuschlag
							.getId2SelectAfterDelete();
					panelQueryMaterialzuschlag.setSelectedId(oNaechster);
				}
				panelSplit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryMaterial) {

				createDetail((Integer) panelQueryMaterial.getSelectedId());
				if (panelQueryMaterial.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailMaterial.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailMaterial);
			} else if (e.getSource() == panelQueryMaterialzuschlag) {
				panelDetailMaterialzuschlag.eventActionNew(e, true, false);
				panelDetailMaterialzuschlag.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplit);
			}
		}

	}

	public InternalFrameArtikel getInternalFrameArtikel() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryMaterial.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("label.material"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		String sBezeichnung = "";
		if (getInternalFrameArtikel().materialDto != null) {
			if (getInternalFrameArtikel().materialDto.getMaterialsprDto() != null) {
				sBezeichnung = getInternalFrameArtikel().materialDto
						.getMaterialsprDto().getCBez();
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance().getTextRespectUISPr("label.material")
							+ ": "
							+ getInternalFrameArtikel().materialDto.getCNr()
							+ ", "
							+ LPMain.getInstance().getTextRespectUISPr(
									"label.bezeichnung") + ": " + sBezeichnung);
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryMaterial.eventYouAreSelected(false);
			if (panelQueryMaterial.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryMaterial.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFrameArtikel().materialDto != null) {
				key = getInternalFrameArtikel().materialDto.getIId();
			}
			createDetail(key);
			panelDetailMaterial.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_ZUSCHLAG) {
			createMaterialzuschlag(getInternalFrameArtikel().materialDto
					.getIId());
			panelSplit.eventYouAreSelected(false);
			panelQueryMaterialzuschlag.updateButtons();
		}
		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

}
