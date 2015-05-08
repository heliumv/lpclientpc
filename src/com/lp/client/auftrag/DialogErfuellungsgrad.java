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
 ******************************************************************************/
package com.lp.client.auftrag;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;

@SuppressWarnings("static-access") 
public class DialogErfuellungsgrad
    extends JDialog implements PropertyChangeListener
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private JPanel panel1 = new JPanel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JOptionPane optionPane;
  private WrapperNumberField wnfErfuellungsgrad = null;
  private Double fErfuellungsgrad = null;
  private AuftragDto auftragDto = null;
  private String btnString1 = null;
  private String btnString2 = null;


  public DialogErfuellungsgrad(AuftragDto auftragDto,Double fErfuellungsgrad) {
    super(LPMain.getInstance().getDesktop(),"",true);
    this.fErfuellungsgrad = fErfuellungsgrad;
    this.auftragDto = auftragDto;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    this.setSize(300,200);

    setTitle(LPMain.getInstance().getTextRespectUISPr("auft.menu.bearbeiten.erfuellungsgrad"));


    this.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        setVisible(false);
        dispose();
      }
    });

  }

  private void jbInit()
      throws Exception {
    panel1.setLayout(gridBagLayout1);
    wnfErfuellungsgrad = new WrapperNumberField();
    wnfErfuellungsgrad.setMinimumValue(0);
    wnfErfuellungsgrad.setMaximumValue(100);
    if(fErfuellungsgrad != null){
      wnfErfuellungsgrad.setDouble(fErfuellungsgrad);
    }
    btnString1 = LPMain.getInstance().getTextRespectUISPr("lp.report.save");
    btnString2 = LPMain.getInstance().getTextRespectUISPr("Cancel");
    add(panel1);
    panel1.add(wnfErfuellungsgrad, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
        0));


       String msgString1 = LPMain.getInstance().getTextRespectUISPr("auft.hint.erfuellungsgraderfassen")+"?";
       Object[] array = {msgString1, wnfErfuellungsgrad};

       //Create an array specifying the number of dialog buttons
       //and their text.
       Object[] options = {btnString1, btnString2};

       //Create the JOptionPane.
       optionPane = new JOptionPane(array,
                                   JOptionPane.QUESTION_MESSAGE,
                                   JOptionPane.YES_NO_OPTION,
                                   null,
                                   options,
                                   options[0]);

       setContentPane(optionPane);

       //Handle window closing correctly.
       setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
       addWindowListener(new WindowAdapter() {
               public void windowClosing(WindowEvent we) {
               /*
                * Instead of directly closing the window,
                * we're going to change the JOptionPane's
                * value property.
                */
                   optionPane.setValue(new Integer(
                                       JOptionPane.CLOSED_OPTION));
           }
       });
       addComponentListener(new ComponentAdapter() {
           public void componentShown(ComponentEvent ce) {
               wnfErfuellungsgrad.requestFocusInWindow();
           }
       });

    optionPane.addPropertyChangeListener(this);
    /*
    panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    */
  }



  public void propertyChange(PropertyChangeEvent e) {
      String prop = e.getPropertyName();

      if (isVisible()
       && (e.getSource() == optionPane)
       && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
           JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
          Object value = optionPane.getValue();

          if (value == JOptionPane.UNINITIALIZED_VALUE) {
              //ignore reset
              return;
          }

          //Reset the JOptionPane's value.
          //If you don't do this, then if the user
          //presses the same button next time, no
          //property change event will be fired.
          optionPane.setValue(
                  JOptionPane.UNINITIALIZED_VALUE);
          if (btnString1.equals(value)) {
                   try {
                      fErfuellungsgrad = wnfErfuellungsgrad.getDouble();
                      auftragDto.setFErfuellungsgrad(fErfuellungsgrad);
                      DelegateFactory.getInstance().getAuftragDelegate().
                          updateAuftragOhneWeitereAktion(auftragDto);
                    }
                    catch (ExceptionLP ex1) {
                    }
                    catch (Throwable ex1) {
                        fErfuellungsgrad = null;
                    }

              if (fErfuellungsgrad != null) {
                  fErfuellungsgrad = null;
                clearAndHide();
              } else {
                /*
                  JOptionPane.showMessageDialog(
                                  CustomDialog.this,
                                  "Sorry, \"" + typedText + "\" "
                                  + "isn't a valid response.\n"
                                  + "Please enter "
                                  + magicWord + ".",
                                  "Try again",
                                  JOptionPane.ERROR_MESSAGE);
                  typedText = null;
                  textField.requestFocusInWindow();
                */
              }
          } else { //user closed dialog or clicked cancel
              fErfuellungsgrad = null;
              clearAndHide();
          }
      }
    }


      public void clearAndHide() {
        setVisible(false);
      }


  public Double getFErfuellungsgrad() {
    return fErfuellungsgrad;
  }


  public void setFErfuellungsgrad(Double fErfuellungsgrad) {
    this.fErfuellungsgrad = fErfuellungsgrad;
  }
}


