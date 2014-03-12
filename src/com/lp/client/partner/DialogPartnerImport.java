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
package com.lp.client.partner;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.PartnerImportDto;

@SuppressWarnings("static-access") 
public class DialogPartnerImport
    extends JDialog implements  ActionListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JPanel panelUrlaubsanspruch = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  JButton wbuAbbrechen = new JButton();

  JButton wbuImportieren = new JButton();
  
  
  
  private PartnerImportDto[] daten=null;
  
  private JScrollPane jspScrollPane = new JScrollPane();
  private WrapperTextArea wtaFehler = new WrapperTextArea();
  private TabbedPanePartner tpPartner=null;
  private WrapperCheckBox wcbErzeugeKunde = new WrapperCheckBox();


  public DialogPartnerImport(PartnerImportDto[] daten, TabbedPanePartner tpPartner)
      throws Throwable {
    super(LPMain.getInstance().getDesktop(),
          "Partner importieren", true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        setVisible(false);
        dispose();
      }
    });

    this.daten=daten;
    this.tpPartner=tpPartner;
    
       jbInit();
    pack();
 
  }


  private void jbInit()
      throws Exception {
    panelUrlaubsanspruch.setLayout(gridBagLayout1);



    wbuImportieren.setText("Importieren");
    wcbErzeugeKunde.setText("erzeuge Kunde");
    
    wtaFehler.setText(DelegateFactory.getInstance().getPartnerDelegate().pruefeCSVImport(daten));
    
    
    wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("lp.abbrechen"));


    wbuImportieren.addActionListener(this);
    
    if(wtaFehler.getText() != null && wtaFehler.getText().length()>0){
    	wbuImportieren.setEnabled(false);
    } else {
    	wtaFehler.setText("Keine Fehler gefunden");
    }
    
    
    setSize(500, 500);
    
    wbuAbbrechen.addActionListener(this);
    this.getContentPane().setLayout(gridBagLayout2);

    this.getContentPane().add(panelUrlaubsanspruch,
                              new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 250,
        50));

 

    panelUrlaubsanspruch.add(jspScrollPane, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0
            , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 2), 0, 0));
        jspScrollPane.getViewport().add(wtaFehler, null);
        
        
        
        
        panelUrlaubsanspruch.add(wcbErzeugeKunde, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2),
                100, 0));       
        panelUrlaubsanspruch.add(wbuImportieren, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2),
                100, 0));
        panelUrlaubsanspruch.add(wbuAbbrechen, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2),
                100, 0));
        
  }




  public void actionPerformed(ActionEvent e) {

    if (e.getSource().equals(wbuImportieren)) {
    	try {
			DelegateFactory.getInstance().getPartnerDelegate()
					.importierePartner(daten,wcbErzeugeKunde.isSelected());
			
			
			this.setVisible(false);
			
		} catch (Exception e2) {
			tpPartner.getPanelPartnerQP1().handleException(e2, false);
		}
      
    } else if (e.getSource().equals(wbuAbbrechen)) {
    	this.setVisible(false);
    	
    }
  }

}
