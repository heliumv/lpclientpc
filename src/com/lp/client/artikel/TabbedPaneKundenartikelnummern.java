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
package com.lp.client.artikel;

import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>Tabbed pane fuer Komponente Preislisten.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-10-28</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class TabbedPaneKundenartikelnummern extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PanelQuery preislistennameTop = null;
	public PanelKundenartikelnummern preislistennameBottom = null;
	public PanelSplit preislistenname = null; // FLR 1:n Liste

	FilterKriteriumDirekt fkdArtikelnummer=null;
	
	public final static int IDX_PANEL_KUNDENARTIKELNUMMERN = 0;

	private KundesokoDto kundesokoDto = null;

	public TabbedPaneKundenartikelnummern(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"artikel.kundenartikelnummern"));

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.kundenartikelnummern"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.kundenartikelnummern"),
				IDX_PANEL_KUNDENARTIKELNUMMERN);

		refreshPanelPreislisten();
		setSelectedComponent(preislistenname);
		preislistennameTop.eventYouAreSelected(false);
		setKeyWasForLockMe();
		setTitlePreisliste(LPMain.getInstance()
				.getTextRespectUISPr("lp.detail"));
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private PanelSplit refreshPanelPreislisten() throws Throwable {
		if (preislistenname == null) {
			preislistennameBottom = new PanelKundenartikelnummern(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.kundenartikelnummern"), null);

			QueryType[] qtPreislistenname = null;
			FilterKriterium[] kriterien = new FilterKriterium[1];

			kriterien[0] = new FilterKriterium("kundesoko.flrkunde.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

			// posreihung: 3 die zusaetzlichen Buttons am PanelQuery anbringen
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };

			preislistennameTop = new PanelQuery(qtPreislistenname, kriterien,
					QueryParameters.UC_ID_KUNDENIDENTNUMMER, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.kundenartikelnummern"), true);

			preislistennameTop.befuellePanelFilterkriterienDirekt(
					new FilterKriteriumDirekt("kundesoko.c_kundeartikelnummer", "",
							FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
									.getTextRespectUISPr(
											"kunde.soko.kundeartikelnummer"),
							FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																	// als '%XX'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT),
					new FilterKriteriumDirekt("kundesoko.flrkunde."
							+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							"", FilterKriterium.OPERATOR_LIKE, LPMain
									.getInstance().getTextRespectUISPr(
											"lp.firma"),
							FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
							Facade.MAX_UNBESCHRAENKT));
			
		
			preislistennameTop.addDirektFilter(new FilterKriteriumDirekt(
					ArtikelFac.FLR_ARTIKELLISTE_C_VOLLTEXT, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.textsuche"),
					FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
					Facade.MAX_UNBESCHRAENKT));
			
			
			
			fkdArtikelnummer=new FilterKriteriumDirekt("kundesoko.flrartikel.c_nr", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
					.getTextRespectUISPr(
							"artikel.artikelnummer"),
			FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
													// als '%XX'
			true, // wrapWithSingleQuotes
			true, Facade.MAX_UNBESCHRAENKT);
			
			preislistennameTop.addDirektFilter(fkdArtikelnummer);
			

			preislistenname = new PanelSplit(getInternalFrame(),
					preislistennameBottom, preislistennameTop,380);

			setComponentAt(IDX_PANEL_KUNDENARTIKELNUMMERN, preislistenname);
		}

		return preislistenname;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_KUNDENARTIKELNUMMERN:

			// das Panel existiert auf jeden Fall
			preislistenname.eventYouAreSelected(false);
			preislistennameTop.updateButtons(preislistennameBottom
					.getLockedstateDetailMainKey());
			break;
		}
	}

	public void setTitlePreisliste(String sTitleOhrwaschloben) throws Throwable {
		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.kundenartikelnummern"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				sTitleOhrwaschloben);

		// TITLE_IDX_AS_I_LIKE setzen
		String sPreisliste = "";

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				sPreisliste);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) { // Zeile in Tabelle
															// gewaehlt
			if (e.getSource() == preislistennameTop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				preislistennameBottom.setKeyWhenDetailPanel(key);
				preislistennameBottom.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				preislistennameTop.updateButtons(preislistennameBottom
						.getLockedstateDetailMainKey());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			setTitlePreisliste(LPMain.getInstance().getTextRespectUISPr(
					"lp.detail"));
			preislistenname.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == preislistennameBottom) {
				setKeyWasForLockMe();
				preislistenname.eventYouAreSelected(false);
			}
		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
		e.getActionCommand();
	}

	public PanelQuery getPreislistennameTop() {
		return this.preislistennameTop;
	}

	/**
	 * Diese Methode setzt ide aktuelle Preisliste als den zu lockenden Auftrag.
	 */
	public void setKeyWasForLockMe() {
		Object oKey = preislistennameTop.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_VKPF_MAXIMALZEHNPREISLISTEN:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
					.getTextRespectUISPr("vkpf.hint.maximalzehnpreislisten"));

			try {
				preislistenname.eventYouAreSelected(false); // @JO hier sitzt
															// ein Lock drauf???
			} catch (Throwable t) {
				super.handleException(t, true);
			}

			break;

		default:
			bErrorErkannt = false;
		}

		return bErrorErkannt;
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	public Object getDto() {
		return kundesokoDto;
	}
}
