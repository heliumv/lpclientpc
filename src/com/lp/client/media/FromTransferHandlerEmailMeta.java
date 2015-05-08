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

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.lp.client.util.logger.LpLogger;

public class FromTransferHandlerEmailMeta extends TransferHandler {
	private static final long serialVersionUID = -6919782993226467636L;	
	private final LpLogger log = (LpLogger) LpLogger.getInstance(this.getClass());

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE ;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		if(c instanceof ISupportTransferableEmailMeta) {
			ISupportTransferableEmailMeta transferableEmailMeta = (ISupportTransferableEmailMeta) c ;
			return new TransferableEmailMeta(transferableEmailMeta.getTransferableEntity()) ;
		}
		
		return super.createTransferable(c);
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		log.info("Drop done with action " + action);
		
		try {
			if(source instanceof ISupportTransferableEmailMeta) {
				ISupportTransferableEmailMeta transferableEmailMeta = (ISupportTransferableEmailMeta) source ;
				transferableEmailMeta.dropDone(data, action) ;
			}
		} catch(Throwable t) {
			log.error("Throwable on exportDone:", t) ;
		}
	} 
}
