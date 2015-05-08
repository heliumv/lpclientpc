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
 *******************************************************************************/
package com.lp.client.media;

import java.awt.datatransfer.Transferable;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.util.dtable.DistributedTableModelImpl;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

public class DragPanelQuery extends PanelQuery implements ISupportTransferableEmailMeta {
	private static final long serialVersionUID = -304803875889889334L;

	private TabbedPaneMediaController controller ;
	
	public DragPanelQuery(QueryType[] typesI, 
	FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI) ;
	}

	public DragPanelQuery(QueryType[] typesI, FilterKriterium[] filtersI,
			int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,
			boolean refreshWhenYouAreSelectedI,
			FilterKriterium kritVersteckteFelderNichtAnzeigenI,
			String labelTextVersteckte) throws Throwable{
		super(typesI, filtersI,idUsecaseI,aWhichButtonIUseI,internalFrameI,add2TitleI,refreshWhenYouAreSelectedI,kritVersteckteFelderNichtAnzeigenI,labelTextVersteckte,null,null);
	}
	
	public DragPanelQuery(QueryType[] typesI, FilterKriterium[] filtersI,
			int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,
			boolean refreshWhenYouAreSelectedI,
			FilterKriterium kritVersteckteFelderNichtAnzeigenI,
			String labelTextVersteckte, SortierKriterium sortierkriterium,
			String textSortierkriterium) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, refreshWhenYouAreSelectedI, 
				kritVersteckteFelderNichtAnzeigenI, labelTextVersteckte, sortierkriterium, textSortierkriterium) ;
	}
	
	public DragPanelQuery(QueryType[] typesI, 
			FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,
			boolean refreshWhenYouAreSelectedI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, refreshWhenYouAreSelectedI) ;
	}

	public DragPanelQuery(QueryType[] typesI, 
			FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI, Object oSelectedIdI)
			throws Throwable {	
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, oSelectedIdI) ;
	}
	

	public void installDragSupport(TabbedPaneMediaController controller) {
		this.controller = controller ;
//		setTransferHandler(new FromTransferHandlerEmailMeta());
//		table.setTransferHandler(new FromTransferHandlerEmailMeta());
//		table.setDragEnabled(true) ;
		((DragWrapperTable)table).setController(controller) ;
	}
	
	@Override
	public MediaEmailMetaDto getTransferableEntity() {
		if(controller == null) return null ;
		return controller.getEmailMetaDto() ;
	}
	
	@Override
	public void dropDone(Transferable data, int action) throws Throwable {	
		myLogger.info("dropDone with action " + action + ".") ;
	}
	
	@Override
	protected WrapperTable createTable(InternalFrame internalFrame,
			DistributedTableModelImpl tableModel) {
		WrapperTable t = new DragWrapperTable(internalFrame, tableModel) ;
		return t ;
	}
}
