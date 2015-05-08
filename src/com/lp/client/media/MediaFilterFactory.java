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

import com.lp.client.pc.LPMain;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;

public class MediaFilterFactory {
	private static MediaFilterFactory filterFactory = null ;
	
	private MediaFilterFactory() {		
	}
	
	public static MediaFilterFactory getInstance() {
		if(filterFactory == null) {
			 filterFactory = new MediaFilterFactory() ;
		}
		return filterFactory ;
	}
	
	public FilterKriterium[] createFKMyInbox() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2] ;
		kriterien[0] = new FilterKriterium(EmailMediaFac.FLR_MEDIAINBOX_PERSONALID, true,
				"'" + LPMain.getTheClient().getIDPersonal() + "'",
				FilterKriterium.OPERATOR_EQUAL, false) ;
		kriterien[1] = new FilterKriterium(EmailMediaFac.FLR_MEDIAINBOX_STATUS, true,
				"('" + LocaleFac.STATUS_ANGELEGT + "')", 
				FilterKriterium.OPERATOR_IN, false) ;
		return kriterien ;
	}
	
	public FilterKriterium createFKVInbox() throws Throwable {
		FilterKriterium fkVersteckt = new FilterKriterium(""
				+ EmailMediaFac.FLR_MEDIAINBOX_VERSTECKT, true, "(1)",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;		
	}
	
	public FilterKriteriumDirekt createFKDMetaFrom() throws Throwable {
		return new FilterKriteriumDirekt(EmailMediaFac.FLR_MEDIAINBOX_EMAILMETA_FROM,
				"", FilterKriterium.OPERATOR_LIKE, "Von",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);		
	}
	
	public FilterKriteriumDirekt createFKDMetaSubject() throws Throwable {
		return new FilterKriteriumDirekt(EmailMediaFac.FLR_MEDIAINBOX_EMAILMETA_SUBJECT,
				"", FilterKriterium.OPERATOR_LIKE, "Betreff",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);				
	}
	
	
	public FilterKriterium[] createFKMyStoreBeleg() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2] ;
		kriterien[0] = new FilterKriterium(EmailMediaFac.FLR_MEDIASTOREBELEG_PERSONALIDANLEGEN, true,
				"'" + LPMain.getTheClient().getIDPersonal() + "'",
				FilterKriterium.OPERATOR_EQUAL, false) ;
		kriterien[1] = new FilterKriterium(EmailMediaFac.FLR_MEDIASTOREBELEG_MANDANTCNR, true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);		
		return kriterien ;
	}
	
	public FilterKriteriumDirekt createFKDStoreBelegFrom() throws Throwable {
		return new FilterKriteriumDirekt(EmailMediaFac.FLR_MEDIASTOREBELEG_EMAILMETA_FROM,
				"", FilterKriterium.OPERATOR_LIKE, "Von",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);		
	}
	
	public FilterKriteriumDirekt createFKDStoreBelegSubject() throws Throwable {
		return new FilterKriteriumDirekt(EmailMediaFac.FLR_MEDIASTOREBELEG_EMAILMETA_SUBJECT,
				"", FilterKriterium.OPERATOR_LIKE, "Betreff",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);				
	}
}
