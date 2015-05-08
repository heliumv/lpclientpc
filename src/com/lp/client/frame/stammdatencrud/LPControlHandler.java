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
package com.lp.client.frame.stammdatencrud;


import java.util.Vector;

import com.lp.client.frame.component.InternalFrame;


/**
 * <p>Diese Klasse kuemmert sich die Verwaltung der LPControls und beinhaltet
 * allgemeine Einstellungen fuer alle Controls und fuer die Methoden des
 * PanelStammdatenCRUD</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>20.03.05</I></p>
 *
 * <p> </p>
 *
 * @author Christian Winhart
 * @version 1.0
 */
public class LPControlHandler
{
  private String ident;
  private String key;
  private String delegate;
  private String buttons;
  private Vector<LPControl> controls = new Vector<LPControl>();
  private boolean hatSprache = false;

  /**
   * Konstruktor: Es wird der Dateiname der Konfigurationsdatei uebergeben.
   * Diese wird dann vom XMLReader verarbeitet.
   *
   * @param xmlFileName String
   * @param internalFrameI InternalFrame
   * @param add2TitleI String
   * @throws Throwable
   */
  public LPControlHandler(String xmlFileName, InternalFrame internalFrameI,
                          String add2TitleI)
      throws Throwable {
    new XMLReader(xmlFileName, this, internalFrameI, add2TitleI);
  }


  public void addControl(LPControl control) {
    controls.add(control);
  }


  public LPControl getControl(int index) {
    if (index >= controls.size()) {
      return null;
    }
    return (LPControl) controls.get(index);
  }


  public Vector<LPControl> getControls() {
    return controls;
  }


  public String getDto() {
    return ident + "Dto";
  }


  public String getIdent() {
    return ident;
  }


  public void setIdent(String identI) {
    this.ident = identI;
  }


  public String getSprDto() {
    return ident + "sprDto";
  }


  public String getDelegate() {
    return delegate;
  }


  public void setDelegate(String delegateI) {
    this.delegate = delegateI;
  }


  public String[] getButtons() {
    if (buttons.equals("")) {
      return new String[] {};
    }
    return buttons.split(",");
  }


  public void setButtons(String buttonsI) {
    this.buttons = buttonsI;
  }


  public void setKey(String keyI) {
    this.key = keyI;
  }


  public String getKey() {
    return this.key;
  }


  public void setHatSprache(boolean hatSpracheI) {
    this.hatSprache = hatSpracheI;
  }


  public boolean getHatSprache() {
    return this.hatSprache;
  }


  public String getSprKey() {
    return this.ident + this.key;
  }

}
