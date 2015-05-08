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


import java.awt.Component;
import java.lang.reflect.Method;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.pc.LPMain;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access") 
/**
 * <p>Diese Klasse kuemmert sich um ein einzelnes Control auf dem
 * PanelStammdatenCRUD, das dynamisch aus einer Konfigurationsdatei
 * geladen wird und mit gewuenschten Attributen versehen wird.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>21.03.05</I></p>
 *
 * <p> </p>
 *
 * @author Christian Winhart
 * @version $Revision: 1.3 $
 */
public class LPControl
    extends Component
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// Klassenname des Controls
  private String klasse = null;

  // Zugehoeriger Spaltennamen aus der Datenbank
  private String colName = null;

  // Name der Zugriffsmethode auf den Inhalt
  private String zugriff = null;

  // Datentyp des Inhalts des Controls
  private String dTyp = null;

  // Bezeichnung des zugehoerigen Labels
  private String labelbez = null;

  // Instanz des Controls
  private Component comp = null;

  // Klasse des Controls
  private Class<?> cClass = null;

  // ob das Control die Uebersetzung enthaelt
  private boolean istSprache = false;

  // ein PK-Feld wird nur bei eventActionNew aktiviert, sonst ist es deaktiviert
  private boolean istPKFeld = false;

  /**
   * Konstruktor: Legt ein neues WrapperEditorField an
   *
   * @param klasseI String
   * @param internalFrameI InternalFrame
   * @param add2TitleI String
   * @throws ExceptionLP
   */
  public LPControl(String klasseI, InternalFrame internalFrameI,
                   String add2TitleI)
      throws ExceptionLP {

    setKlasse(klasseI);
    try {
      cClass = Class.forName(klasse);
      comp = new WrapperEditorField(internalFrameI, add2TitleI);
    }
    catch (Throwable e) {
      throw new ExceptionLP(EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                            "Bitte in Klasse LPControl den Konstruktor fuer WrapperEditorField ueberpruefen!",
                            null);
    }
  }


  /**
   * Konstruktor: Legt ein neues Control der uebergebenen Klasse an
   *
   * @param klasseI String
   * @throws ExceptionLP
   */
  public LPControl(String klasseI)
      throws ExceptionLP {

    setKlasse(klasseI);
    try {
      cClass = Class.forName(klasse);
      comp = (Component) cClass.newInstance();
    }
    catch (Throwable e) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          "Bitte in Klasse LPControl den allgemeinen Konstruktor ueberpruefen!",
          null);
    }
  }


  public String getKlasse() {
    return klasse;
  }


  public void setKlasse(String klasseI) {
    this.klasse = klasseI;
  }


  public String getColName() {
    return colName;
  }


  public void setColName(String nameI) {
    colName = nameI;
  }


  public Class<?> getCClass() {
    return cClass;
  }


  public String getZugriff() {
    return zugriff;
  }


  public void setZugriff(String zugriffI) {
    zugriff = zugriffI;
  }


  public String getDTyp() {
    return dTyp;
  }


  public void setDTyp(String typI) {
    dTyp = typI;
    if (typI.equals("java.lang.String")) {
      setZugriff("Text");
    }
    else if (typI.equals("java.lang.Integer")) {
      setZugriff("Integer");
    }
    else if (typI.equals("java.lang.Short")) {
      setZugriff("Short");
    }
    else if (typI.equals("java.lang.Double")) {
      setZugriff("Double");
    }
    else if (typI.equals("java.lang.Float")) {
      setZugriff("Float");
    }
    else if (typI.equals("java.math.BigDecimal")) {
      setZugriff("BigDecimal");
    }
    else if (typI.equals("java.util.Date")) {
      setZugriff("Date");
    }

  }


  public String getLabelbez() {
    return labelbez;
  }


  public void setLabelbez(String labelbezI) {
    this.labelbez = labelbezI;
  }


  public Component getComponent() {
    return comp;
  }


  public boolean getIstSprache() {
    return this.istSprache;
  }


  public void setIstSprache(boolean stateI) {
    this.istSprache = stateI;
  }


  public boolean getIstPKFeld() {
    return this.istPKFeld;
  }


  /**
   * ruft auf das aktuelle Control die Methode 'methode' auf und uebergibt als
   * Parameter 'wert'
   *
   * @param methode String
   * @param wert String
   * @throws ExceptionLP
   */
  public void addAttribut(String methode, String wert)
      throws ExceptionLP {
    Class<?>[] paramTyp = null;
    Object[] param = null;
    Method mtd;
    try {
      if (methode.equals("setMandatoryField") ||
          methode.equals("setMandatoryFieldDB") ||
          methode.equals("setActivatable")) {
        paramTyp = new Class[] {
            boolean.class};
        if (wert.equals("true")) {
          param = new Boolean[] {
              Boolean.TRUE};
        }
        else {
          param = new Boolean[] {
              Boolean.FALSE};
        }
      }

      else if (methode.equals("setColumnsMax") ||
               methode.equals("setFractionDigits") ||
               methode.equals("setMaximumIntegerDigits") ||
               methode.equals("setMaximumValue") ||
               methode.equals("setMinimumValue")) {
        paramTyp = new Class[] {
            int.class};
        param = new Integer[] {
            Integer.valueOf(wert)};
      }

      else if (methode.equals("setToolTipText")) {
        paramTyp = new Class[] {
            String.class};
        param = new String[] {
            LPMain.getInstance().getTextRespectUISPr(wert).toString()};
      }

      if (methode.equals("setActivatable")) {
        this.istPKFeld = true;
      }

      mtd = cClass.getMethod(methode, paramTyp);
      mtd.invoke(comp, param);

    }
    catch (Throwable e) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          "Bitte in Klasse LPControl Methode addAttribut und aufgerufenen Methode: " +
          methode + " ueberpruefen, ob diese unterstuetzt wird!",
          null);
    }
  }

}
