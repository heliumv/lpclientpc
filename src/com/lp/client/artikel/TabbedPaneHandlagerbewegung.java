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
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.system.service.MandantFac;
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
 * @version $Revision: 1.9 $
 */
public class TabbedPaneHandlagerbewegung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryTopHandlagerbewegung = null;
	private PanelBasis panelDetailBottomHandlagerbewegung = null;
	private PanelBasis panelSplit = null;

	private WrapperMenuBar wrapperManuBar = null;

	public TabbedPaneHandlagerbewegung(InternalFrame internalFrameI)
			throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"artikel.title.panel.handlagerbewegungen"));

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		// 1 Handlagerbewegungen
		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
				PanelBasis.ACTION_FILTER };
		FilterKriterium[] filtersHandlager = null;
		String mandant = "'" + LPMain.getInstance().getTheClient().getMandant()
				+ "'";
		if (!LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
				|| (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM) && LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER))) {

			filtersHandlager = new FilterKriterium[1];
			FilterKriterium krit1 = new FilterKriterium(
					"handlagerbewegung."
							+ LagerFac.FLR_HANDLAGERBEWEGUNG_FLRLAGER
							+ ".mandant_c_nr", true, mandant,
					FilterKriterium.OPERATOR_EQUAL, false);
			filtersHandlager[0] = krit1;
		}

		panelQueryTopHandlagerbewegung = new PanelQuery(ArtikelFilterFactory
				.getInstance().createQTHandlagerbewegung(), filtersHandlager,
				QueryParameters.UC_ID_HANDLAGERBEWEGUNG, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.handlagerbewegungen"), true);
		panelDetailBottomHandlagerbewegung = new PanelHandlagerbewegung(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.handlagerbewegungen"), null);

		panelQueryTopHandlagerbewegung.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("handlagerbewegung.flrartikel.c_nr",
						"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("artikel.artikelnummer"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT)

				, new FilterKriteriumDirekt("aspr.c_bez", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.bezeichnung"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT));

		panelSplit = new PanelSplit(getInternalFrame(),
				panelDetailBottomHandlagerbewegung,
				panelQueryTopHandlagerbewegung, 245);
		addTab(LPMain.getInstance().getTextRespectUISPr(
				"artikel.title.panel.handlagerbewegungen"), panelSplit);

		refreshTitle();
		addChangeListener(this);
		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryTopHandlagerbewegung) {
				panelDetailBottomHandlagerbewegung
						.setKeyWhenDetailPanel(panelQueryTopHandlagerbewegung
								.getSelectedId());
				panelDetailBottomHandlagerbewegung.eventYouAreSelected(false);
				panelQueryTopHandlagerbewegung.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplit.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryTopHandlagerbewegung) {
				panelDetailBottomHandlagerbewegung.eventActionNew(e, true,
						false);
				panelDetailBottomHandlagerbewegung.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailBottomHandlagerbewegung) {
				Object oKey = panelDetailBottomHandlagerbewegung
						.getKeyWhenDetailPanel();
				panelQueryTopHandlagerbewegung.eventYouAreSelected(false);
				panelQueryTopHandlagerbewegung.setSelectedId(oKey);
				panelSplit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailBottomHandlagerbewegung) {
				panelQueryTopHandlagerbewegung
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD
				|| e.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (e.getSource() == panelDetailBottomHandlagerbewegung) {

				panelSplit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelDetailBottomHandlagerbewegung) {
				panelSplit.eventYouAreSelected(false);
			}
		}
	}

	private void refreshTitle() {
		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.handlagerbewegungen"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		panelSplit.eventYouAreSelected(false);
		panelQueryTopHandlagerbewegung.updateButtons();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {
			wrapperManuBar = new WrapperMenuBar(this);
		}
		return wrapperManuBar;
	}
}
