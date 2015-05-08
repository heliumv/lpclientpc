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
package com.lp.client.frame.component;

import java.awt.BorderLayout;

import com.lp.client.pc.LPMain;
import com.lp.editor.util.TextBlockOverflowException;

public class PanelPositionenHtmlTexteingabe extends PanelBasis implements IControlContent  {
	private static final long serialVersionUID = 1832347283901465177L;
	private WrapperHtmlField whtmlText = null ;

	public PanelPositionenHtmlTexteingabe(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		setLayout(new BorderLayout());

		whtmlText = new WrapperHtmlField(getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"), true);
		
		add(whtmlText, BorderLayout.CENTER) ;
//		add(whtmlText, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
//				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
//						2, 2, 2, 2), 0, 0));
	}
	
	public void requestFocus() {
		whtmlText.requestFocusInWindow();
	}	
	
	public void setText(String newText) {
		whtmlText.setText(newText) ;
	}
	
	public String getText() throws TextBlockOverflowException {
		return whtmlText.getText() ;
	}

	@Override
	public void removeContent() throws Throwable {
		setText("") ;
	}

	@Override
	public boolean hasContent() throws Throwable {
		return whtmlText.hasContent() ;
	}
	
	public void setEditable(boolean value) {
	}
	
	public void startEditing() {
		whtmlText.startEditing();
	}
}
