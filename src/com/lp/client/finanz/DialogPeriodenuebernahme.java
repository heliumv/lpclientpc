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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.SaldovortragModelPersonenKonto;
import com.lp.server.finanz.service.SaldovortragModelSachkonto;

public class DialogPeriodenuebernahme extends JDialog implements ActionListener {
	private static final long serialVersionUID = -1278373916025267622L;

	private WrapperRadioButton wrbDiesesKonto ;
	private WrapperRadioButton wrbDebitoren ;
	private WrapperRadioButton wrbKreditoren ;

	private WrapperCheckBox wcbDeleteEB ;
	
	private WrapperLabel wlbKontoInWork ;
	
	private WrapperButton wbDoIt ;
	private WrapperButton wbCancel ;
	private WrapperButton wbuOkay ;
	
	private KontoDto theKontoDto ;
	private Integer geschaeftsJahr ;
	
	public DialogPeriodenuebernahme(Frame owner, String title, KontoDto kontoDto, Integer geschaeftsJahr) {
		super(owner, title, true) ;
		
		theKontoDto = kontoDto ;
		this.geschaeftsJahr = geschaeftsJahr ;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE) ;

		jbInit() ;
		pack() ;
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this) ;	
	}
	
	private void jbInit() {
		
		initializeComponents() ;		
		createLayout() ;
	}

	private void initializeComponents() {
		wrbDiesesKonto = new WrapperRadioButton(LPMain.getTextRespectUISPr("finanz.nurKonto") + " " + getKontoNummer()) ;
		wrbDebitoren = new WrapperRadioButton(LPMain.getTextRespectUISPr("finanz.debitorenkonten")) ;
		wrbKreditoren = new WrapperRadioButton(LPMain.getTextRespectUISPr("finanz.kreditorenkonten")) ;

		ButtonGroup buttonGroup = new ButtonGroup() ;
		buttonGroup.add(wrbDiesesKonto);
		buttonGroup.add(wrbDebitoren);
		buttonGroup.add(wrbKreditoren);

		if(theKontoDto != null) {
			wrbDiesesKonto.setSelected(true);
		}
		
		wcbDeleteEB = new WrapperCheckBox(LPMain.getTextRespectUISPr("finanz.manuelleEBEntfernen"), false) ;
		wlbKontoInWork = new WrapperLabel("") ;
		wlbKontoInWork.setHorizontalAlignment(JLabel.LEFT) ;
		
		wbDoIt = new WrapperButton(LPMain.getTextRespectUISPr("lp.finanz.periodenuebernahme")) ;
		wbDoIt.addActionListener(this) ;
		wbCancel = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen")) ;
		wbCancel.addActionListener(this) ;	

		wbuOkay = new WrapperButton(LPMain.getTextRespectUISPr("button.ok"));
		wbuOkay.addActionListener(this) ;
		wbuOkay.setVisible(false) ;
	}
	
	private void createLayout() {
		setPreferredSize(new Dimension(400, 200));
		getContentPane().setLayout(new GridBagLayout()) ;
		
		JPanel panelWork = new JPanel() ; 
		panelWork.setLayout(new GridBagLayout()) ;
		
		panelWork.add(wrbDiesesKonto, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, 
                		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0)) ;
		panelWork.add(wrbDebitoren, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0)) ;
		panelWork.add(wrbKreditoren, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0)) ;
		
		panelWork.add(wcbDeleteEB, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0)) ;
		
		JPanel panelInfo = new JPanel() ;
		panelInfo.setLayout(new GridBagLayout()) ;
		
		panelInfo.setBorder(BorderFactory.createEtchedBorder()) ;		
		panelInfo.add(wlbKontoInWork, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0)) ;
		

		JPanel panelButton = new JPanel() ;
		panelButton.setLayout(new GridBagLayout());
		
		
		panelButton.add(wbuOkay, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 100, 0));	
		panelButton.add(wbDoIt, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 100, 0));	
		panelButton.add(wbCancel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 100, 0));
		
		
		getContentPane().add(panelWork, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, getInsets2(), 0, 0)) ;
		getContentPane().add(panelInfo, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.BOTH, getInsets2(), 0, 0) ) ;
		getContentPane().add(panelButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.BOTH, getInsets2(), 0, 0) ) ;
	}
	
	private Insets getInsets2() {
		return new Insets(2, 2, 2, 2) ;
	}
	
	private String getKontoNummer() {
		return theKontoDto == null ? "" : (" " + theKontoDto.getCNr()) ; 
	}
	
	private void processPeriodenUebernahmeSpezifischesKonto() throws Throwable {		
		if(FinanzServiceFac.KONTOTYP_SACHKONTO.equals(theKontoDto.getKontotypCNr())) {
			SaldovortragModelSachkonto model = new SaldovortragModelSachkonto(
				geschaeftsJahr, theKontoDto.getIId()) ;
			model.setDeleteManualEB(wcbDeleteEB.isSelected()) ;
			DelegateFactory.getInstance().getBuchenDelegate().createSaldovortragsBuchung(model) ;
		} else {
			SaldovortragModelPersonenKonto model = SaldovortragModelPersonenKonto.createFromKontotyp(
				theKontoDto.getKontotypCNr(), geschaeftsJahr, theKontoDto.getIId(), null) ;
			model.setDeleteManualEB(wcbDeleteEB.isSelected()) ;
			DelegateFactory.getInstance().getBuchenDelegate().createSaldovortragsBuchungErmittleOP(model) ;
		}
	}
	
	private void processPeriodenUebernahme(String kontotypCnr) throws Throwable {
		DelegateFactory.getInstance().getBuchenDelegate()
			.createSaldovortragsBuchungErmittleOP(kontotypCnr, geschaeftsJahr, wcbDeleteEB.isSelected()) ;
	}
	
	private void actionPeriodenuebernahme() {
		try {
			wbDoIt.setVisible(false) ;
			wbCancel.setEnabled(false) ;
			wbuOkay.setVisible(true) ;
			
			if(wrbDiesesKonto.isSelected()) {
				processPeriodenUebernahmeSpezifischesKonto() ;
			}
			if(wrbDebitoren.isSelected()) {
				processPeriodenUebernahme(FinanzServiceFac.KONTOTYP_DEBITOR) ;
			}
			if(wrbKreditoren.isSelected()) {
				processPeriodenUebernahme(FinanzServiceFac.KONTOTYP_KREDITOR) ;				
			}
		} catch(Throwable t) {
			if(t instanceof ExceptionLP) {
				wlbKontoInWork.setText(((ExceptionLP)t).getSMsg()) ;
			} else {
				wlbKontoInWork.setText(t.getMessage()) ;				
			}
			validate() ;
		} finally {
			wbDoIt.setEnabled(true) ;
			wbCancel.setEnabled(true) ;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource() ;
		if(source == wbDoIt) {
			actionPeriodenuebernahme() ;
		}

		if(source == wbCancel) {
			dispose() ;
		}

		if(source == wbuOkay) {
			dispose() ;
		}
	}
}
