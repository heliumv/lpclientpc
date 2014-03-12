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
package com.lp.client.lieferschein;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.service.Artikelset;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.Helper;

public class DialogPositionenBarcodeerfassungArtikelset extends
		DialogPositionenBarcodeerfassung {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7984698777658929878L;
	private Artikelset artikelset ;
	private BelegpositionVerkaufDto[] cachedPositionsInSet = new BelegpositionVerkaufDto[]{};
	private BigDecimal totalCountSnr = BigDecimal.ZERO ;
	
	public DialogPositionenBarcodeerfassungArtikelset(
			Integer lagerIId, Artikelset artikelset,
			InternalFrame internalFrame) throws Throwable {
		super(lagerIId, internalFrame) ;
		this.setArtikelset(artikelset) ;
		setSindVerschiedeneArtikelErlaubt(true) ;
		setSeriennummern(artikelset.getIdentities()) ;
//		setGesamtMengeText(BigDecimal.ZERO) ;
	}

	public Artikelset getArtikelset() {
		return artikelset;
	}

	public void setArtikelset(Artikelset newArtikelset) {
		artikelset = newArtikelset;
		cachedPositionsInSet = artikelset.getAllPositions() ;	
	}
	
	public void setTotalCountSeriennummern(BigDecimal value) {
		totalCountSnr = value ;
		try {
			setGesamtMengeText(getGesamtMenge()) ;
		} catch(Throwable t) {	
		}
	}

	public BigDecimal getTotalCountSeriennummern() {
		if(null == totalCountSnr) {
			totalCountSnr = BigDecimal.ZERO ;
		}
		return totalCountSnr ;
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {
		if (tfSnrchnr.getText() != null) {
			actionPerformed(new ActionEvent(this, 0, ACTION_ADD_FROM_HAND));
		}

		boolean allowClose = true ;
		if(getTotalCountSeriennummern().compareTo(getGesamtMenge()) != 0) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("artikel.barcoderfassung.error.unvollstaendig"));
			allowClose = false ;
		}
		
		if(allowClose) {
			alSeriennummernReturn = alSeriennummern;
			setVisible(false);
		}
	}

	protected void setGesamtMengeText(BigDecimal bdGesamt) throws Throwable {
		BigDecimal istValue = bdGesamt ; 
		BigDecimal sollValue = getTotalCountSeriennummern() ;

		String istMenge = Helper.formatZahl(istValue, 4, LPMain.getTheClient().getLocUi()) ;
		String sollMenge = Helper.formatZahl(sollValue, 4, LPMain.getTheClient().getLocUi()) ;

		boolean tooMuch = false ;
		boolean areEqual = false ;
		if(istValue != null && sollValue != null) {
			tooMuch = istValue.compareTo(sollValue) > 0 ;
			areEqual = istValue.compareTo(sollValue) == 0 ;
		}

		String formattedText = "<html>" + 
				(areEqual ? "<font color=green>" : "") +
				(tooMuch  ? "<font color=red>" : "" ) + 
				istMenge + 
				(tooMuch ? "</font>" : "") +
				" / " + 
				"<b>" + sollMenge + "</b>" +
				(areEqual ? "</font>" : "") +
				"</html>" ;
		// jLabelGesamtMenge.setText(istMenge + "/" + sollMenge);	
		jLabelGesamtMenge.setText(formattedText) ;
	}
	
	protected void addNewItem(Integer artikelIId, String serialnumber) throws Throwable {
		boolean foundItem = false ;

		for (BelegpositionVerkaufDto belegpositionDto : cachedPositionsInSet) {
			if(artikelIId.equals(belegpositionDto.getArtikelIId())) {
				foundItem = true ;
				break ;
			}
		}
		
		if(!foundItem){
			DialogFactory
				.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("artikel.barcoderfassung.error.unbekannt")
							+ " " + serialnumber) ;
			return ;
		}
		
		super.addNewItem(artikelIId, serialnumber) ;
	}	
}
