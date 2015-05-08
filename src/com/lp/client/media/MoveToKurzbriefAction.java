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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

public class MoveToKurzbriefAction extends AbstractAction {
	private static final long serialVersionUID = -6335209342614901195L;

	private TabbedPaneMediaController controller ;
	
	public MoveToKurzbriefAction(TabbedPaneMediaController controller, String text) {
		super(text) ;
		this.controller = controller ;
	}
	
	public MoveToKurzbriefAction(TabbedPaneMediaController controller, String text, Icon icon) {
		super(text, icon) ;
		this.controller = controller ;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Moving something to kurzbrief") ;
		if(controller.getEmailMetaDto() != null) {
			System.out.println("From: " + controller.getEmailMetaDto().getCFrom()) ;
			System.out.println("To: " + controller.getEmailMetaDto().getCTo()) ;
			
			try {
				controller.createKurzbrief();
			} catch(Throwable t) {
				
			}
		}
	}
}
