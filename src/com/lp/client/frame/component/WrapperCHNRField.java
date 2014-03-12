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
package com.lp.client.frame.component;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access") 
/**
 *
 * <p><I>Textfeld zur Eingabe von Serien/Chargennummern</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>03.10.2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.9 $
 * @deprecated
 */
public class WrapperCHNRField
    extends JPanel implements IControl
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected boolean isMandatoryField = false;
  protected boolean isMandatoryFieldDB = false;
  protected boolean isActivatable = true;
  private boolean dependenceField = false;
  private boolean bMindesthaltbarkeitsdatum = false;

  private Pattern regPattern;

  private WrapperDateField wdfMHD = new WrapperDateField();
  private WrapperTextField wtfCHNR = new WrapperTextField();
  public WrapperCHNRField()
      throws Throwable {

    ParametermandantDto parameter = (ParametermandantDto)
        DelegateFactory.getInstance().getParameterDelegate().
        getParametermandant(ParameterFac.
                            PARAMETER_CHARGENNUMMER_BEINHALTET_MINDESTHALTBARKEITSDATUM,
                            ParameterFac.KATEGORIE_ARTIKEL,
                            LPMain.getInstance().getTheClient().getMandant());

    if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
      bMindesthaltbarkeitsdatum = true;
    }

    HelperClient.setDefaultsToComponent(this);
    jbInit();
  }


  public void setMandatoryField(boolean isMandatoryField) {
     this.isMandatoryField=isMandatoryField;
       wtfCHNR.setMandatoryField(isMandatoryField);
  }
  
	@Override
	public boolean hasContent() throws Throwable {
	    return wtfCHNR.hasContent();
	}

  protected void setMask() {
    String sLeer="[ ]{0,}";
    String regEinzeln="[A-Za-z0-9.-/%-&_-_]{1,}"+sLeer;
    String regExp = sLeer + "|" + regEinzeln;
    this.regPattern = Pattern.compile(regExp);
    
    //!!! SIEHE AUCH Helper.istSerienChargennummerGueltig() !!!!!!
  }


  private void jbInit() {
    wdfMHD.setMandatoryField(true);
    this.setMask();
    wtfCHNR.setDocument(new NumberDocument());

    this.setLayout(new GridBagLayout());

    if (bMindesthaltbarkeitsdatum == true) {

      this.add(wdfMHD,
               new GridBagConstraints(0, 0, 1, 1, 0, 0.0,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(0, 0, 0, 0),
                                      0, 0));
    }
    this.add(wtfCHNR,
             new GridBagConstraints(1, 0, 1, 1, 1, 0.0,
                                    GridBagConstraints.CENTER,
                                    GridBagConstraints.HORIZONTAL,
                                    new Insets(0, 0, 0, 0),
                                    0, 0));
  }


 
  
  public String getChargennummer() {
    if (bMindesthaltbarkeitsdatum == true) {
      if (wdfMHD.getTimestamp() != null) {
    	  
    	  if(wtfCHNR.getText()==null && isMandatoryField == true){
    		  return null;
    	  }
    	  
        StringBuffer s = new StringBuffer();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(wdfMHD.getTimestamp().getTime());

        int iTag = c.get(Calendar.DAY_OF_MONTH);
        int iMonat = c.get(Calendar.MONTH) + 1;
        int iJahr = c.get(Calendar.YEAR);

        s.append(iJahr);

        if (iMonat < 10) {
          s.append("0").append(iMonat);
        }
        else {
          s.append(iMonat);
        }
        if (iTag < 10) {
          s.append("0").append(iTag);
        }
        else {
          s.append(iTag);
        }

        if (wtfCHNR.getText() != null) {
          s.append(wtfCHNR.getText());
        }

        return new String(s);
      } else {
        return null;
      }
    }
    else {
      return wtfCHNR.getText();
    }
  }


  public void setChagennummer(String chargennummer) {
    if (bMindesthaltbarkeitsdatum == true) {
      if (chargennummer != null && chargennummer.length() > 7) {
        String datum = chargennummer.substring(0, 8);
        String zusatz = chargennummer.substring(8);

        int iJahr = 0;
        int iMonat = 0;
        int iTag = 0;
        try {
          iJahr = new Integer(datum.substring(0, 4));
          iMonat = new Integer(datum.substring(4, 6));
          iTag = new Integer(datum.substring(6, 8));
          Calendar c = Calendar.getInstance();
          c.set(iJahr, iMonat -1, iTag, 0, 0, 0);

          wdfMHD.setDate(c.getTime());

          wtfCHNR.setText(zusatz);

        }
        catch (NumberFormatException ex) {
          wtfCHNR.setText(chargennummer);
        }

      }

    }
    else {
      wtfCHNR.setText(chargennummer);
    }
  }


  /**
   * Leeren des Feldes.
   */
  public void removeContent() {
    wtfCHNR.setText("");
    wdfMHD.setTimestamp(null);
  }


  /**
   * isActivateable
   *
   * @return boolean
   */
  public boolean isActivatable() {
    return isActivatable;
  }


  /**
   * isMandatoryField
   *
   * @return boolean
   */
  public boolean isMandatoryField() {
    return isMandatoryField || isMandatoryFieldDB;
  }


  public boolean isMandatoryFieldDB() {
    return isMandatoryFieldDB;
  }


  public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
    this.isMandatoryFieldDB = isMandatoryFieldDB;
    if (isMandatoryFieldDB == true) {
      setMandatoryField(true);
    }
  }


  public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;		
		if(!isActivatable) {
			setEditable(false) ;
		}
  }


  public boolean isDependenceField() {
    return dependenceField;
  }


  public void setDependenceField(boolean dependenceField) {
    this.dependenceField = dependenceField;
    if (dependenceField) {
      this.setBackground(HelperClient.getDependenceFieldBackgroundColor());
    }
    else {
      this.setBackground(new WrapperSNRField().getBackground());
    }
  }


  public void setMinimumSize(Dimension d) {
    super.setMinimumSize(new Dimension(d.width, Defaults.getInstance().getControlHeight()));
  }


  public void setMaximumSize(Dimension d) {
    super.setMaximumSize(new Dimension(d.width, Defaults.getInstance().getControlHeight()));
  }


  public void setPreferredSize(Dimension d) {
    super.setPreferredSize(new Dimension(d.width, Defaults.getInstance().getControlHeight()));
  }

  public void setEnabled(boolean bEditable) {
    wdfMHD.setEnabled(bEditable);
    wtfCHNR.setEditable(bEditable);
  }
  public void setEditable(boolean bEditable) {
    wdfMHD.setEnabled(bEditable);
    wtfCHNR.setEditable(bEditable);
  }

  protected class NumberDocument
      extends PlainDocument {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void remove(int offs, int len) throws BadLocationException {
      if (regPattern != null) {
        String text = wtfCHNR.getText();
        StringBuffer stringBuffer = new StringBuffer(text == null ? "" : text);

        stringBuffer.delete(offs, offs + len);
        if (!regPattern.matcher(stringBuffer).matches()) {
          Toolkit.getDefaultToolkit().beep();
          return;
        }
      }
      super.remove(offs, len);
    }

    public void insertString(int offs, String str, AttributeSet a) throws
        BadLocationException {
      StringBuffer strInsert = new StringBuffer(str);
      if (strInsert.length() > 0) {
        if (regPattern != null) {
          StringBuffer stringBuffer = new StringBuffer("");
          if (wtfCHNR.getText() != null) {
            stringBuffer = new StringBuffer(wtfCHNR.getText());
          }
          stringBuffer.insert(offs, strInsert.toString());
          if (!regPattern.matcher(stringBuffer).matches()) {
            Toolkit.getDefaultToolkit().beep();
            return;
          }
        }
        super.insertString(offs, strInsert.toString(), a);
      }
    }
  }

}
