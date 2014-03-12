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
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelQueryFLRArtikelShopgruppe;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.3 $
 */
public class TabbedPaneShopgruppe extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryShopgruppe = null;

	public PanelQuery getPanelQueryShopgruppe() {
		return panelQueryShopgruppe;
	}

	private PanelQuery panelQueryWebshop = null;
	private PanelBasis panelDetailShopgruppe = null;
	private PanelBasis panelSplit = null;
	private PanelBasis panelDetailWebshop = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_WEBSHOP = 2;

	private PanelQueryFLR panelRolle = null;

	private ShopgruppeDto shopgruppeDto = null;

	public ShopgruppeDto getShopgruppeDto() {
		return shopgruppeDto;
	}

	public void setShopgruppeDto(ShopgruppeDto shopgruppeDto) {
		this.shopgruppeDto = shopgruppeDto;
	}

	public TabbedPaneShopgruppe(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.shopgruppe"));

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// 1 Materialauswahlliste
		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1, 
				PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN } ;
		
		panelQueryShopgruppe = new PanelQuery(null,ArtikelFilterFactory.getInstance().createFKShopgruppeMandantCNr(null),
				QueryParameters.UC_ID_SHOPGRUPPE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.shopgruppe"), true);

		
		panelQueryShopgruppe.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("shopgruppe.c_nr", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.shopgruppe"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT), new FilterKriteriumDirekt("shopgruppesprset.c_bez", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.bezeichnung"),
						FilterKriteriumDirekt.PROZENT_BOTH, true, true,
						Facade.MAX_UNBESCHRAENKT));
		
		addTab(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.auswahl"), panelQueryShopgruppe);
		panelQueryShopgruppe.eventYouAreSelected(false);

		if ((Integer) panelQueryShopgruppe.getSelectedId() != null) {
			setShopgruppeDto(DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.shopgruppeFindByPrimaryKey(
							(Integer) panelQueryShopgruppe.getSelectedId()));
		}

		// 2 SystemrolleBezeichnung/Kennung
		panelDetailShopgruppe = new PanelShopgruppe(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				panelQueryShopgruppe.getSelectedId());
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				panelDetailShopgruppe);
		String[] aWhichButtonIUse2 = { PanelBasis.ACTION_NEW };
		// 3 Rechte
		panelQueryWebshop = new PanelQuery(null, null,
				QueryParameters.UC_ID_SHOPGRUPPEWEBSHOP, aWhichButtonIUse2,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.rechte"), true);

		panelQueryWebshop.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("flrwebshop.c_bez", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.webshop"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT), null);

		panelDetailWebshop = new PanelShopgruppewebshop(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.webshop"), null);

		panelSplit = new PanelSplit(getInternalFrame(), panelDetailWebshop,
				panelQueryWebshop, 350);
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.webshop"),
				panelSplit);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryShopgruppe) {
				setSelectedComponent(panelDetailShopgruppe);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailWebshop) {
				panelDetailWebshop.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailShopgruppe) {
				panelDetailShopgruppe.eventYouAreSelected(false);

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailShopgruppe) {
				panelQueryShopgruppe.clearDirektFilter();
				Object oKey = panelDetailShopgruppe.getKeyWhenDetailPanel();
				panelQueryShopgruppe.setSelectedId(oKey);
			} else if (e.getSource() == panelDetailWebshop) {
				Object oKey = panelDetailWebshop.getKeyWhenDetailPanel();
				panelQueryWebshop.eventYouAreSelected(false);
				panelQueryWebshop.setSelectedId(oKey);
				panelSplit.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailWebshop) {
				panelQueryWebshop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryWebshop) {
				Integer iId = (Integer) panelQueryWebshop.getSelectedId();
				panelDetailWebshop.setKeyWhenDetailPanel(iId);
				panelDetailWebshop.eventYouAreSelected(false);
				panelQueryWebshop.updateButtons();
			} else if (e.getSource() == panelQueryShopgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					getInternalFrameBenutzer().setKeyWasForLockMe(
							panelQueryShopgruppe.getSelectedId() + "");

					setShopgruppeDto(DelegateFactory.getInstance()
							.getArtikelDelegate()
							.shopgruppeFindByPrimaryKey((Integer) key));

					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							LPMain.getInstance().getTextRespectUISPr(
									"lp.shopgruppe")
									+ ": "
									+ getShopgruppeDto().getBezeichnung());
				}
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
			refreshTitle();
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailShopgruppe) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryShopgruppe);
				setKeyWasForLockMe();
				panelQueryShopgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailWebshop) {
				setKeyWasForLockMe();
				if (panelDetailWebshop.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWebshop
							.getId2SelectAfterDelete();
					panelQueryWebshop.setSelectedId(oNaechster);
				}

				panelSplit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryShopgruppe) {
				setShopgruppeDto(null);
				panelDetailShopgruppe.eventActionNew(e, true, false);
				this.setSelectedComponent(panelDetailShopgruppe);
			} else if (e.getSource() == panelQueryWebshop) {
				panelDetailWebshop.eventActionNew(e, true, false);
				panelDetailWebshop.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplit);
			}
		} else if(e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if(e.getSource() == panelQueryShopgruppe) {
				setShopgruppeDto(null);
				panelDetailShopgruppe.eventActionNew(e, true, false);
				this.setSelectedComponent(panelDetailShopgruppe);				
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if(e.getSource() == panelQueryShopgruppe) {
				int iPos = panelQueryShopgruppe.getTable().getSelectedRow() ;
				if(iPos < panelQueryShopgruppe.getTable().getRowCount() - 1) {
					Integer idPos1 = (Integer) panelQueryShopgruppe.getSelectedId() ;
					Integer idPos2 = (Integer) panelQueryShopgruppe.getTable().getValueAt(iPos + 1, 0) ;
					
					DelegateFactory.getInstance().getArtikelDelegate().vertauscheShopgruppen(idPos1, idPos2) ;
					panelQueryShopgruppe.setSelectedId(idPos1) ;
				}
			}
		} else if(e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if(e.getSource() == panelQueryShopgruppe) {
				int iPos = panelQueryShopgruppe.getTable().getSelectedRow() ;
				if(iPos > 0) {
					Integer idPos1 = (Integer) panelQueryShopgruppe.getSelectedId() ;
					Integer idPos2 = (Integer) panelQueryShopgruppe.getTable().getValueAt(iPos - 1, 0) ;
					
					DelegateFactory.getInstance().getArtikelDelegate().vertauscheShopgruppen(idPos1, idPos2) ;
					panelQueryShopgruppe.setSelectedId(idPos1) ;
				}
			}			
		}
	}

	public InternalFrameArtikel getInternalFrameBenutzer() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryShopgruppe.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.shopgruppe"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getShopgruppeDto() != null) {

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getShopgruppeDto().getBezeichnung());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQueryShopgruppe.eventYouAreSelected(false);
			panelQueryShopgruppe.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			panelDetailShopgruppe.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_WEBSHOP) {
			panelQueryWebshop.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKShopgruppewebshop(
							getShopgruppeDto().getIId()));

			panelSplit.eventYouAreSelected(false);
			panelQueryWebshop.updateButtons();
		}
		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;
	}

}
