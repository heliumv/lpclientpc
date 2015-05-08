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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

public class FavoriteList {

	private List<FavElement> favorites = new ArrayList<FavoriteList.FavElement>() ;
	private int limit = 5 ;
	
	public FavoriteList() {		
	}
	
	public int getLimit() {
		return limit ;
	}
	
	public void setLimit(int limit) {
		this.limit = limit ;
	}
	
	public boolean hasId(Integer id) {
		for (FavElement entry : favorites) {
			if(id.equals(entry.getId())) return true ;
		}
		return false ;
	}
	
	public boolean add(Integer id, String description) {
		for (FavElement entry : favorites) {
			if(entry.getId().equals(id)) return false ;
		}
		
		if(favorites.size() > getLimit()) {
			favorites.remove(0) ;
		}

		favorites.add(new FavElement(id, description)) ;
		return true ;
	}
	
	
	public Integer getId(String description) {
		for (FavElement entry : favorites) {
			if(entry.getDescription().equals(description)) return entry.getId() ;
		}
		
		return null ;
	}

	public List<JMenuItem> asMenuItems() {
		List<JMenuItem> items = new ArrayList<JMenuItem>() ;
		for (FavElement entry : favorites) {
			JMenuItem item = createMenuItem(entry.getId(), entry.getDescription()) ;
			attachActions(item, entry.getId(), entry.getDescription()) ;
			items.add(item) ;
		}
		
		return items ;
	}

	protected JMenuItem createMenuItem(Integer id, String description) {
		return new JMenuItem(description) ;
	}
	
	protected void attachActions(JMenuItem menuItem, Integer id, String description) {
		menuItem.setActionCommand("FAVORITE_" + id) ;		
	}
	
	public class FavElement {
		private Integer id ;
		private String description ;
		
		public FavElement(Integer id, String description) {
			this.id = id ;
			this.description = description ;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
	}
}
