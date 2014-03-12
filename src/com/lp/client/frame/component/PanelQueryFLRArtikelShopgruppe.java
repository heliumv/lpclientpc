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
package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public class PanelQueryFLRArtikelShopgruppe extends PanelQueryFLR {
	private static final long serialVersionUID = 7387065079924947082L;
	
	private WrapperCheckBox wcbAlleArtikel = new WrapperCheckBox() ;
	private FilterKriterium[] originalFilter = null ;
	
	public PanelQueryFLRArtikelShopgruppe(QueryType[] typesI,
			FilterKriterium[] filtersI, int idUsecaseI,
			String[] aWhichButtonIUseI, InternalFrame internalFrameI,
			String add2TitleI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI) ;
		originalFilter = filtersI ;
		jbInit() ;
	}
	
	public PanelQueryFLRArtikelShopgruppe(QueryType[] typesI, 
			FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,
			boolean refreshWhenYouAreSelectedI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, true) ;
		originalFilter = filtersI ;
		jbInit() ;
		setRefreshWenYouAreSelected(refreshWhenYouAreSelectedI) ;
	}
	
	
	public PanelQueryFLRArtikelShopgruppe(QueryType[] typesI,
			FilterKriterium[] filtersI,
			int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,
			FilterKriterium kritVersteckteFelderNichtAnzeigenI)
			throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, kritVersteckteFelderNichtAnzeigenI) ;
		originalFilter = filtersI ;
		jbInit() ;
	}
	
	
	private void jbInit() throws Exception {
		wcbAlleArtikel = new WrapperCheckBox() ;
		wcbAlleArtikel.setText(LPMain.getTextRespectUISPr("artikel.shopgruppe.alleartikelanzeigen")) ;
		wcbAlleArtikel.setSelected(false) ;
		wcbAlleArtikel.addActionListener(this) ;
		
		getPanelFilter().add(wcbAlleArtikel, 
				new GridBagConstraints(
						8, 5, 1, 1, 0.1, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,new Insets(2, 2, 2, 2), 0, 0)) ;
	}

	private void adaptFilters() {
		if(originalFilter != null) {
			if(wcbAlleArtikel != null && wcbAlleArtikel.isSelected()) {
				List<FilterKriterium> newFilters = new ArrayList<FilterKriterium>() ;
				for (FilterKriterium filterKriterium : originalFilter) {
					if(!"artikelliste.flrshopgruppe.i_id".equals(filterKriterium.kritName)) {
						newFilters.add(filterKriterium) ;
					}
				}
				setDefaultFilter(newFilters.toArray(new FilterKriterium[0])) ;
			} else {
				setDefaultFilter(originalFilter) ;
			}
		}		
	}
	
	@Override
	public void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		adaptFilters() ;		
		super.eventActionRefresh(e, bNeedNoRefreshI);
	}

	
	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if(wcbAlleArtikel.equals(e.getSource())) {
			eventActionRefresh(e, false) ;
		}
		
		super.eventActionSpecial(e);
	}
}
