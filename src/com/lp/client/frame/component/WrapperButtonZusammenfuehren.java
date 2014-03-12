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

import com.lp.client.frame.Defaults;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:39:22 $
 */
public class WrapperButtonZusammenfuehren
    extends WrapperButton
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public static final int SELECT_LINKEN_DATENINHALT = 0;
  public static final int SELECT_RECHTEN_DATENINHALT = 1;
  public static final int SELECT_KOMBINIERTE_DATENINHALTE = 2;

  private boolean bButtonKombinierbar = false;
  private boolean bIsButtonAlle = false;

  private int iSelection = SELECT_LINKEN_DATENINHALT;

  public WrapperButtonZusammenfuehren() {
    this.setISelection(this.iSelection);
    /* <- -> <-> */
    this.setMaximumSize(new Dimension(70, Defaults.getInstance().getControlHeight()));
    this.setPreferredSize(new Dimension(55, Defaults.getInstance().getControlHeight()));
    this.setMinimumSize(new Dimension(45, Defaults.getInstance().getControlHeight()));
   /* links/rechts/beide */
   /*
   this.setMaximumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
   this.setPreferredSize(new Dimension(65, Defaults.getInstance().getControlHeight()));
   this.setMinimumSize(new Dimension(60, Defaults.getInstance().getControlHeight()));
   */
  }

  public WrapperButtonZusammenfuehren(int iSelection) {
    this.setISelection(iSelection);
  }

  public void setNextSelection() {
    if (this.isButtonKombinierbar()) {
      if (this.getISelection() == SELECT_LINKEN_DATENINHALT) {
        this.setISelection(SELECT_RECHTEN_DATENINHALT);
      }
      else if (this.getISelection() == SELECT_RECHTEN_DATENINHALT) {
        this.setISelection(SELECT_KOMBINIERTE_DATENINHALTE);
      }
      else if (this.getISelection() == SELECT_KOMBINIERTE_DATENINHALTE) {
        this.setISelection(SELECT_LINKEN_DATENINHALT);
      }
    }
    else {
      if (this.getISelection() == SELECT_LINKEN_DATENINHALT) {
        this.setISelection(SELECT_RECHTEN_DATENINHALT);
      }
      else if (this.getISelection() == SELECT_RECHTEN_DATENINHALT) {
        this.setISelection(SELECT_LINKEN_DATENINHALT);
      }
      else {
        this.setISelection(SELECT_LINKEN_DATENINHALT);
      }
    }
  }

  public int getNextSelection() {
    int returnval = SELECT_LINKEN_DATENINHALT;
    if (this.isButtonKombinierbar()) {
      if (this.getISelection() == SELECT_LINKEN_DATENINHALT) {
        returnval = SELECT_RECHTEN_DATENINHALT;
      }
      else if (this.getISelection() == SELECT_RECHTEN_DATENINHALT) {
        returnval = SELECT_KOMBINIERTE_DATENINHALTE;
      }
      else if (this.getISelection() == SELECT_KOMBINIERTE_DATENINHALTE) {
        returnval = SELECT_LINKEN_DATENINHALT;
      }
    }
    else {
      if (this.getISelection() == SELECT_LINKEN_DATENINHALT) {
        returnval = SELECT_RECHTEN_DATENINHALT;
      }
      else if (this.getISelection() == SELECT_RECHTEN_DATENINHALT) {
        returnval = SELECT_LINKEN_DATENINHALT;
      }
    }
    return returnval;
  }

  public void setISelection(int iSelection) {
    this.iSelection = iSelection;
    switch (iSelection) {
      case SELECT_LINKEN_DATENINHALT:
        this.setText(LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.label.links"));
        if (bIsButtonAlle) {
          this.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.alle") + ": " + LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.links"));
        }
        else {
          this.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.links"));
        }
        break;
      case SELECT_RECHTEN_DATENINHALT:
        this.setText(LPMain.getInstance().getTextRespectUISPr("part.wrapperbuttonzusammenfuehren.label.rechts"));
        if (bIsButtonAlle) {
          this.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.alle") + ": " + LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.rechts"));
        }
        else {
          this.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.rechts"));
        }
        break;
      case SELECT_KOMBINIERTE_DATENINHALTE:
        this.setText(LPMain.getInstance().getTextRespectUISPr("part.wrapperbuttonzusammenfuehren.label.kombiniert"));
        if (bIsButtonAlle) {
          this.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.alle") + ": " + LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.kombiniert"));
        }
        else {
          this.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
              "part.wrapperbuttonzusammenfuehren.tooltip.kombiniert"));
        }
        break;
    }

  }


  public int getISelection() {
    return iSelection;
  }

  public void setButtonKombinierbar(boolean bButtonKombinierbar) {
    this.bButtonKombinierbar = bButtonKombinierbar;
  }

  public boolean isButtonKombinierbar() {
    return bButtonKombinierbar;
  }

  public void setButtonIsAlle(boolean bIsButtonAlle) {
    this.bIsButtonAlle = bIsButtonAlle;
    this.setISelection(this.getISelection());
  }

  public boolean isButtonAlle() {
    return bIsButtonAlle;
  }

}


