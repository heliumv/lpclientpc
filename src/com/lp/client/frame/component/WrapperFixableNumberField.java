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
package com.lp.client.frame.component;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;


/**
 * <p> Diese Klasse kuemmert sich um ein NumberField, dessen Wert mit Hilfe
 * eines Radiobuttons fixiert werden kann</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004 - 2007</p>
 *
 * <p>Erstellung: Victor Finder; 14.11.07</p>
 *
 * <p>@author $Author: valentin $</p>
 * @version not attributable Date $Date: 2008/08/11 08:39:23 $
 */
public class WrapperFixableNumberField
   extends JPanel implements IControl
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperNumberField wnfNumber = null;
  private WrapperRadioButton wrbFixNumber = null;

  public WrapperFixableNumberField()
      throws ExceptionLP {
    wnfNumber = new WrapperNumberField();
    jbInit();
  }

	@Override
	public boolean hasContent() throws Throwable {
		return wnfNumber.hasContent();
	}

  public WrapperFixableNumberField(BigDecimal minimumValue, BigDecimal maximumValue)
      throws ExceptionLP {
    wnfNumber = new WrapperNumberField(minimumValue, maximumValue);
    jbInit();
  }


  public WrapperFixableNumberField(int iMinimumValue, int iMaximumValue)
      throws ExceptionLP {
    wnfNumber = new WrapperNumberField(new BigDecimal(iMinimumValue),
                                       new BigDecimal(iMaximumValue));
    jbInit();
  }


  private void jbInit() {
    wrbFixNumber = new WrapperRadioButton();
    /**
     * @todo
     */
    wrbFixNumber.setToolTipText("Preis Fixieren");
    wrbFixNumber.setSelected(true);

    this.setLayout(new GridBagLayout());

    this.add(wrbFixNumber,
             new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                    GridBagConstraints.CENTER,
                                    GridBagConstraints.HORIZONTAL,
                                    new Insets(0, 0, 0, 0),
                                    10, 0));
    this.add(wnfNumber,
             new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                                    GridBagConstraints.CENTER,
                                    GridBagConstraints.HORIZONTAL,
                                    new Insets(0, 0, 0, 0),
                                    0, 0));
  }


  public void removeContent()
      throws Throwable {
    wnfNumber.setBigDecimal(null);
  }


  /**
   * setActivatable
   *
   * @param isActivatable boolean
   */
  public void setActivatable(boolean isActivatable) {
    wnfNumber.setActivatable(isActivatable);
  }


  /**
   * isActivateable
   *
   * @return boolean
   */
  public boolean isActivatable() {
    return wnfNumber.isActivatable();
  }


  /**
   * isMandatoryField
   *
   * @return boolean
   */
  public boolean isMandatoryField() {
    return wnfNumber.isMandatoryField();
  }


  /**
   * isMandatoryField
   *
   * @return boolean
   */
  public boolean isMandatoryFieldDB() {
    return wnfNumber.isMandatoryFieldDB();
  }


  public void setMandatoryField(boolean isMandatoryField) {
    wnfNumber.setMandatoryField(isMandatoryField);
  }


  public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
    wnfNumber.setMandatoryFieldDB(isMandatoryFieldDB);
  }


  /**
   * Numberfield in JComponent "verstecken". diese Methode sollte von aussen nur
   * zum Einfuegen ins Layout verwendet werden.
   *
   * @return JComponent
   */
  public JComponent getWrapperWrapperNumberField() {
    return wnfNumber;
  }


  public BigDecimal getBigDecimal()
      throws ExceptionLP {
    return wnfNumber.getBigDecimal();
  }


  public void setDependenceField(boolean dependenceField)
      throws ExceptionLP {
    wnfNumber.setDependenceField(dependenceField);
  }


  public void setBigDecimal(BigDecimal bdI)
      throws ExceptionLP {
 //   if (!wrbFixNumber.isSelected()) {
      wnfNumber.setBigDecimal(bdI);
 //   }
  }


  public void setFractionDigits(int fractionDigits) {
    wnfNumber.setFractionDigits(fractionDigits);
  }

  public void setVisible(boolean aFlag){
    wnfNumber.setVisible(aFlag);
    wrbFixNumber.setVisible(aFlag);
  }


  public void setEditable(boolean bEditable) {
    wnfNumber.setEditable(bEditable);
  }

  public WrapperRadioButton getWrbFixNumber(){
    return wrbFixNumber;
  }

}

