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
package com.lp.client.frame.stammdatencrud;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access") 
/**
 * <p><I>Diese Klasse kuemmert sich um die dynamische Generierung der
 * Eingabefelder und der gewuenschten Buttons fuer ein Detail-Panel
 * aus einem XML-File und verwaltet dynamisch die entpsrechenden Events</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>10.03.05</I></p>
 *
 * <p> </p>
 *
 * @author Christian Winhart
 * @version $Revision: 1.4 $
 */
public class PanelStammdatenCRUD
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private GridBagLayout gridBagLayoutAll = null;
  private JPanel jPanelWorkingOn = new JPanel();
  private JPanel panelButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private LPControlHandler handler;
  private int anzahlControls = 0;
  private Vector<?> lpControls = new Vector<Object>();

  private LPControl curControl; // aktuell zu bearbeitendes LPControl
  private Method mtd; // aufzurufende Methode
  private Method mtd2; // 2. aufzurufende Methode
  private Class<?> typ; // Klasse des Datentyps fuer die Methoden
  private Class<?> cClass; // Klasse des Controls
  private Object dto; // enthaelt die aktuelle Dto fuer die Controls
  private Object sprDto; // enthaelt die aktuelle sprDto
  private Object delegate; // enthaelt den delegater
  private Object value; // enthaelt den Wert fuer ein Control
  private Object dtoZugriffsKlasse; // verweist auf die Klasse mit getter und setter fuer dto
  private String lockme; // enthaelt den passenden LockMe-String
  private final int MAX_ANZAHL_CONTROLS = 10;

  /**
   * Konstruktor: Beim Aufruf werden die Parameter fuer das PanelBasis
   * uebergeben (1-3).
   * <br> Zusaetzlich spezifisch der Dateiname der Konfigurationsdatei und die
   * Instanz der Klasse, die die Zugriffsmethoden fuer die spezifischen dtos
   * enthaelt.
   *
   * @param internalFrameI InternalFrame
   * @param add2TitleI String
   * @param pkI Object
   * @param xmlFilenameI String
   * @param dtoZugriffsKlasseI Object
   * @param lockMeI String
   * @throws Throwable
   */
  public PanelStammdatenCRUD(InternalFrame internalFrameI,
                             String add2TitleI,
                             Object pkI,
                             String xmlFilenameI,
                             Object dtoZugriffsKlasseI,
                             String lockMeI)
      throws Throwable {

    super(internalFrameI, add2TitleI, pkI);

    this.dtoZugriffsKlasse = dtoZugriffsKlasseI;
    handler = new LPControlHandler(xmlFilenameI, internalFrameI, add2TitleI);
    lpControls = handler.getControls();
    anzahlControls = lpControls.size();
    this.lockme = lockMeI;

    if (anzahlControls > MAX_ANZAHL_CONTROLS) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_ZUVIELE_CONTROLS,
          new Throwable("Es wurden zu viele " +
                        "Controls in der XML angegeben! Maximal werden 10 verarbeitet!"));
    }

    jbInit(lpControls);
    initComponents();
    enableAllComponents(this, false);
  }


  public int getAnzahlControls() {
    return anzahlControls;
  }


  private Object getDtoZugriffsklasse() {
    return this.dtoZugriffsKlasse;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {

    try {
      super.eventActionNew(eventObject, true, false);
    }
    catch (Throwable ex) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventActionNew(): " +
                        "super.eventActionNew()"));
    }

    // holt sich die eigene Dto
    dto = getMyDto();
    // holt sich den Methodenaufruf zum Setzen der Dto
    try {
      mtd = getDtoZugriffsklasse().getClass().getMethod("set" + handler.getDto(),
          new Class[] {dto.getClass()});
    }
    catch (SecurityException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventActionNew(): " +
                        "SecurityException - set" + handler.getDto()));
    }
    catch (NoSuchMethodException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventActionNew(): " +
                        "Methode set" + handler.getDto() + " in " +
                        "DtoZugriffsklasse: " +
                        getDtoZugriffsklasse().getClass().toString() +
                        " ueberpruefen."));
    }
    // ruft die Methode auf und setzt die Dto auf eine neue, leere Instanz
    try {
      mtd.invoke(getDtoZugriffsklasse(),
                 new Object[] { (dto.getClass()).newInstance()});
    }
    catch (InstantiationException ex2) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventActionNew(): " +
                        "InstantiationException: " + dto.getClass().toString()));
    }
    catch (InvocationTargetException ex2) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventActionNew(): " +
                        "InstantiationException: " + dto.getClass().toString()));
    }
    catch (IllegalArgumentException ex2) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventActionNew(): " +
                        "IllegalArgumentException: " + dto.getClass().toString()));
    }
    catch (IllegalAccessException ex2) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventActionNew(): " +
                        "IllegalAccessException: " + dto.getClass().toString()));
    }
    // sprDto auf null setzen, wird in eventActionSave behandelt
    sprDto = null;
    leereAlleFelder(this);
    aktivierePKFelder(true);
  }


  protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
      throws Throwable {
    super.eventActionUpdate(aE, bNeedNoUpdateI);
    aktivierePKFelder(false);
  }


  protected void eventActionDiscard(ActionEvent e)
      throws Throwable {
    super.eventActionDiscard(e);
    aktivierePKFelder(false);
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    if (!bNeedNoDeleteI) {
      if (!isLockedDlg()) {
        delegate = getMyDelegate();

        // holt sich die Methode zum Loeschen vom Delegater
        mtd2 = delegate.getClass().getMethod("remove" + handler.getIdent(),
                                             new Class[] {getMyDto().getClass()});
        // fuehrt die Methode zum Loeschen aus
        try {
          mtd2.invoke(delegate, new Object[] {getMyDto()});
        }
        catch (InvocationTargetException ex1) {
          throw ex1.getTargetException();
        }
        catch (IllegalArgumentException ex1) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionDelete(): " +
                            "IllegalArgumentException"));
        }
        catch (IllegalAccessException ex1) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionDelete(): " +
                            "IllegalAccessException"));
        }
      }
    }
    try {
      this.setKeyWhenDetailPanel(null);
      super.eventActionDelete(e, false, false);
    }
    catch (Throwable t2) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventActionDelete(): " +
                        "super.eventActionDelete()"));
    }
  }


  protected void components2Dto()
      throws SecurityException, NoSuchMethodException, ExceptionLP {

    dto = getMyDto();
    // es werden die Werte aus allen Controls in die Dto geschrieben,
    // wobei das Control mit dem Attribut 'istSprache', sofern vorhanden,
    // fuer die sprDto bestimmt ist!

    for (int i = 0; i < anzahlControls; i++) {
      curControl = handler.getControl(i);

      // holt sich die Klasse des Datentyps fuer den Inhalt des Controls
      try {
        if (!curControl.getCClass().equals(WrapperLabel.class)) {
          typ = Class.forName(curControl.getDTyp());
        }
      }
      catch (ClassNotFoundException ex) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                          "ClassNotFoundException: " + curControl.getDTyp()));
      }
      // holt sich die Klasse des Controls
      try {
        cClass = Class.forName(curControl.getKlasse());
      }
      catch (ClassNotFoundException ex1) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                          "ClassNotFoundException: " + curControl.getKlasse()));
      }
      // holt sich die Methode zum Lesen eines Wertes aus dem Control
      try {
        if (!curControl.getCClass().equals(WrapperLabel.class)) {
          mtd = cClass.getMethod("get" + curControl.getZugriff());
        }
      }
      catch (SecurityException ex2) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                          "SecurityException"));
      }
      catch (NoSuchMethodException ex2) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                          "NoSuchMethodException: get" +
                          curControl.getZugriff()));
      }
      // ruft die Methode auf und holt den Wert aus dem Control
      try {
        if (!curControl.getCClass().equals(WrapperLabel.class)) {
          value = mtd.invoke(curControl.getComponent());
        }
      }
      catch (InvocationTargetException ex3) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                          "InvocationTargetException"));
      }
      catch (IllegalArgumentException ex3) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                          "IllegalArgumentException"));
      }
      catch (IllegalAccessException ex3) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                          "IllegalAccessException"));
      }

      if (curControl.getIstSprache()) {
        sprDto = getMySprDto();

        if (sprDto == null && value == null) {
          // ist sprDto null, so wurde noch keine angelegt
          // kein Wert als Bezeichnung eingegeben => sprDto auf null setzen
          sprDto = null;
        }
        else {
          // Werte in sprDto schreiben:
          delegate = getMyDelegate();

          if (sprDto == null) {
            // legt eine neue Instanz der sprDto an
            // die sprDto befindet sich im gleichen Package, wie die Dto
            try {
              sprDto = Class.forName(dto.getClass().getPackage().getName() + "." +
                                     handler.getSprDto()).newInstance();
            }
            catch (ClassNotFoundException ex4) {
              throw new ExceptionLP(
                  EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                  new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                                "ClassNotFoundException: " + handler.getSprDto()));
            }
            catch (IllegalAccessException ex4) {
              throw new ExceptionLP(
                  EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                  new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                                "IllegalAccessException"));
            }
            catch (InstantiationException ex4) {
              throw new ExceptionLP(
                  EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                  new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                                "InstantiationException"));
            }
            // setzt die Sprache auf die aktuelle Anmeldesprache
            // holt sich die Methode zum Setzen der Sprache
            mtd = sprDto.getClass().getMethod("setLocaleCNr",
                                              new Class[] {String.class});

            // ruft die Methode auf und schreibt den Wert in die sprDto
            try {
              mtd.invoke(sprDto, new Object[] {LPMain.getInstance().
                         getUISprLocale().toString().replaceAll("_", "")});
            }
            catch (InvocationTargetException ex6) {
              throw new ExceptionLP(
                  EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                  new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                                "InvocationTargetException"));
            }
            catch (IllegalArgumentException ex6) {
              throw new ExceptionLP(
                  EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                  new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                                "IllegalArgumentException"));
            }
            catch (IllegalAccessException ex6) {
              throw new ExceptionLP(
                  EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                  new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                                "IllegalAccessException"));
            }

          }
          // holt sich den Methodenaufruf zum Setzen des Wertes in die sprDto
          try {
            mtd = sprDto.getClass().getMethod("set" +
                                              curControl.getColName(),
                                              new Class[] {typ});
          }
          catch (SecurityException ex7) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                              "SecurityException"));
          }
          catch (NoSuchMethodException ex7) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                              "NoSuchMethodException: set" +
                              curControl.getColName()));
          }
          // ruft die Methode auf und schreibt den Wert in die sprDto
          try {
            mtd.invoke(sprDto, new Object[] {value});
          }
          catch (InvocationTargetException ex8) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                              "InvocationTargetException"));
          }
          catch (IllegalArgumentException ex8) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                              "IllegalArgumentException"));
          }
          catch (IllegalAccessException ex8) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                              "IllegalAccessException"));
          }
        }

        // der dto die neue sprDto setzen; wurde kein UI-Wert eingetragen,
        // so ist sprDto null
        try {
          mtd = dto.getClass().getMethod("set" + handler.getSprDto(),
                                         new Class[] {Class.forName(
                                             dto.getClass().getPackage().
                                             getName() + "." +
                                             handler.getSprDto())});
        }
        catch (ClassNotFoundException ex10) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "ClassNotFoundException: " + handler.getSprDto()));
        }
        catch (SecurityException ex10) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "SecurityException"));
        }
        catch (NoSuchMethodException ex10) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "NoSuchMethodException: set" + handler.getSprDto()));
        }

        try {
          mtd.invoke(dto, new Object[] {sprDto});
        }
        catch (InvocationTargetException ex11) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "InvocationTargetException"));
        }
        catch (IllegalArgumentException ex11) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "IllegalArgumentException"));
        }
        catch (IllegalAccessException ex11) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "IllegalAccessException"));
        }

      }
      else { // Werte in Dto schreiben
        // holt sich die Methode zum Setzen des Wertes in die Dto
        try {
          if (!curControl.getCClass().equals(WrapperLabel.class)) {
            mtd = dto.getClass().getMethod("set" +
                                           curControl.getColName(),
                                           new Class[] {typ});
          }
        }
        catch (SecurityException ex12) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "SecurityException"));
        }
        catch (NoSuchMethodException ex12) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "NoSuchMethodException: set" +
                            curControl.getColName()));
        }

        // ruft die Methode auf und schreibt den Wert in die Dto
        try {
          if (!curControl.getCClass().equals(WrapperLabel.class)) {
            mtd.invoke(dto, new Object[] {value});
          }
        }
        catch (InvocationTargetException ex13) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "InvocationTargetException"));
        }
        catch (IllegalArgumentException ex13) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "IllegalArgumentException"));
        }
        catch (IllegalAccessException ex13) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> components2Dto(): " +
                            "IllegalAccessException"));
        }
      }
    }
  }


  protected void dto2Components()
      throws ExceptionLP, Throwable {

    dto = getMyDto();

    for (int i = 0; i < anzahlControls; i++) {
      curControl = handler.getControl(i);

      if (curControl.getIstSprache()) {
        // Wert aus sprDto holen:
        delegate = getMyDelegate();

        sprDto = getMySprDto();

        if (sprDto != null) {
          // hole spr-wert
          try {
            mtd2 = sprDto.getClass().getMethod("get" + curControl.getColName());
          }
          catch (SecurityException ex) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                              "SecurityException"));
          }
          catch (NoSuchMethodException ex) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                              "NoSuchMethodException: get" +
                              curControl.getColName()));
          }

          //setzt value auf sprDto-Wert
          try {
            value = mtd2.invoke(sprDto);
          }
          catch (InvocationTargetException ex1) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                              "InvocationTargetException"));
          }
          catch (IllegalArgumentException ex1) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                              "IllegalArgumentException"));
          }
          catch (IllegalAccessException ex1) {
            throw new ExceptionLP(
                EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
                new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                              "IllegalAccessException"));
          }
        }
        else {
          // keine Bezeichnung vorhanden
          value = "";
        }
      }
      else { // Wert aus Dto holen
        // holt sich die Methode zum Holen des Wertes aus der Dto
        try {
          if (!curControl.getCClass().equals(WrapperLabel.class)) {

            mtd = dto.getClass().getMethod("get" +
                                           curControl.getColName());
          }
        }
        catch (SecurityException ex2) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable(dto.getClass() + "." + curControl.getColName() + " security!"));
        }
        catch (NoSuchMethodException ex2) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable(dto.getClass() + "." + curControl.getColName() +
                            " not found!"));
        }
        // ruft die Methode auf und holt sich den den Wert der Dto
        try {
          if (!curControl.getCClass().equals(WrapperLabel.class)) {
            value = mtd.invoke(dto);
          }
        }
        catch (InvocationTargetException ex3) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                            "InvocationTargetException"));
        }
        catch (IllegalArgumentException ex3) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                            "IllegalArgumentException"));
        }
        catch (IllegalAccessException ex3) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                            "IllegalAccessException"));
        }
      }

      // holt sich die Klasse des Datentyps fuer den Inhalt des Controls
      try {
        if (!curControl.getCClass().equals(WrapperLabel.class)) {
          typ = Class.forName(curControl.getDTyp());
        }
      }
      catch (ClassNotFoundException ex4) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                          "ClassNotFoundException: " + curControl.getDTyp() +
                          "; Bitte XML-Datei: dtyp ueberpruefen!"));
      }

      // holt sich die Klasse des Controls
      try {
        cClass = Class.forName(curControl.getKlasse());
      }
      catch (ClassNotFoundException ex5) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                          "ClassNotFoundException: " + curControl.getKlasse()));
      }

      // holt sich die Methode zum Setzen eines Wertes auf das Control
      try {
        if (!curControl.getCClass().equals(WrapperLabel.class)) {
          mtd = cClass.getMethod("set" +
                                 curControl.getZugriff(),
                                 new Class[] {typ});
        }
      }
      catch (SecurityException ex6) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                          "SecurityException"));
      }
      catch (NoSuchMethodException ex6) {
        throw new Throwable("NoSuchMethodException > " + cClass + " > " +
                            curControl.getZugriff() + " > " + typ);
      }

      // ruft die Methode auf und setzt den Wert aus der Dto auf das Control
      try {
        if (!curControl.getCClass().equals(WrapperLabel.class)) {
          mtd.invoke(curControl.getComponent(), new Object[] {value});
        }
      }
      catch (InvocationTargetException ex7) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                          "InvocationTargetException"));
      }
      catch (IllegalArgumentException t) {
        throw new Throwable("IllegalArgumentException > " +
                            curControl.getColName() + " > " + value.toString());
      }
      catch (IllegalAccessException ex7) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> dto2Components(): " +
                          "IllegalAccessException"));
      }
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    boolean bExit = false;

    //    try {
    if (allMandatoryFieldsSetDlg()) {

      // Ist der Schluesselwert in der Dto null,
      // so wurde ein neuer Datensatz angelegt!
      dto = getMyDto();
      delegate = getMyDelegate();

      // holt sich den Schluessel aus der dto
      try {
        mtd = dto.getClass().getMethod("get" +
                                       handler.getKey());
      }
      catch (SecurityException ex) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                          "SecurityException"));
      }
      catch (NoSuchMethodException ex) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                          "NoSuchMethodException: get" +
                          handler.getKey() + " in der Dto"));
      }

      // ruft die Methode auf und holt sich den Wert der Dto
      try {
        value = mtd.invoke(dto);
      }
      catch (InvocationTargetException ex1) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                          "InvocationTargetException"));
      }
      catch (IllegalArgumentException ex1) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                          "IllegalArgumentException"));
      }
      catch (IllegalAccessException ex1) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                          "IllegalAccessException"));
      }

      // ist der Schluessel null, erfolgt ein create
      if (value == null) {
        components2Dto();
        // neuen Datensatz anlegen
        try {
          mtd = delegate.getClass().getMethod("create" +
                                              handler.getIdent(),
                                              new Class[] {dto.getClass()});
        }
        catch (SecurityException ex2) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                            "SecurityException"));
        }
        catch (NoSuchMethodException ex2) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                            "NoSuchMethodException: create" +
                            handler.getIdent() + " in " +
                            handler.getDelegate().toString()));
        }

        Object myKey = null;
        try {
          myKey = mtd.invoke(delegate, new Object[] {dto});
        }
        catch (InvocationTargetException ex3) {
          // OK hier
          handleException(ex3.getCause(), true);
          bExit = true;
        }
        catch (IllegalArgumentException ex3) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                            "IllegalArgumentException"));
        }
        catch (IllegalAccessException ex3) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                            "IllegalAccessException"));
        }
        if (bExit) {
          return;
        }

        if (myKey == null) {
          throw new Throwable("myKey==null; create gibt nix zurueck :-(");
        }
        setKeyWhenDetailPanel(myKey);
      }
      else {
        components2Dto();
        // Datensatz updaten
        try {
          mtd = delegate.getClass().getMethod("update" +
                                              handler.getIdent(),
                                              new Class[] {dto.getClass()});
        }
        catch (SecurityException ex4) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                            "SecurityException"));
        }

        try {
          mtd.invoke(delegate, new Object[] {dto});
        }
        catch (InvocationTargetException ex5) {
          throw ex5.getTargetException();
        }
        catch (IllegalArgumentException ex5) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                            "IllegalArgumentException"));
        }
        catch (IllegalAccessException ex5) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                            "IllegalAccessException"));
        }
      }

      try {
        super.eventActionSave(e, true);
      }
      catch (Throwable ex6) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                          "super.eventActionSave()"));
      }
      aktivierePKFelder(false);

      try {
        eventYouAreSelected(false);
      }
      catch (Throwable ex7) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventActionSave(): " +
                          "eventYouAreSelected()"));
      }
    }
//    }
//    catch (Throwable ex8) {
//      // von allMandatoryFieldsSetDlg(); keine Behandlung
//    }
  }


  protected void eventItemchanged(EventObject eI)
      throws ExceptionLP, Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {

      setKeyWhenDetailPanel( ( (ISourceEvent) e.getSource()).getIdSelected());

      if (getKeyWhenDetailPanel() != null) {

        delegate = getMyDelegate();
        // holt sich die Methode zum Finden des PK
        try {
          mtd = delegate.getClass().getMethod(
              handler.getIdent().toLowerCase() + "FindByPrimaryKey",
              new Class[] {getKeyWhenDetailPanel().getClass()});
        }
        catch (SecurityException ex) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "SecurityException"));
        }
        catch (NoSuchMethodException ex) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "NoSuchMethodException: " + handler.getIdent().toLowerCase() +
                            "FindByPrimaryKey "));
        }

        // fuehrt die Methode aus und holt sich die gewuenschte Dto
        // anhand des PK
        try {
          dto = mtd.invoke(delegate, new Object[] {getKeyWhenDetailPanel()});
        }
        catch (InvocationTargetException ex1) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "InvocationTargetException"));
        }
        catch (IllegalArgumentException ex1) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "IllegalArgumentException"));
        }
        catch (IllegalAccessException ex1) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "IllegalAccessException"));
        }

        // holt sich die Methode zum Setzen der Dto
        try {
          mtd = getDtoZugriffsklasse().getClass().getMethod("set" +
              handler.getDto(), new Class[] {dto.getClass()});
        }
        catch (SecurityException ex2) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "SecurityException"));
        }
        catch (NoSuchMethodException ex2) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "NoSuchMethodException: set" +
                            handler.getDto()));
        }

        // ruft die Methode auf und setzt die Dto auf die vorher
        // per PK Geholte
        try {
          mtd.invoke(getDtoZugriffsklasse(), new Object[] {dto});
        }
        catch (InvocationTargetException ex3) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "InvocationTargetException"));
        }
        catch (IllegalArgumentException ex3) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "IllegalArgumentException"));
        }
        catch (IllegalAccessException ex3) {
          throw new ExceptionLP(
              EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
              new Throwable("Fehler in PanelStammdatenCRUD -> eventItemchanged(): " +
                            "IllegalAccessException"));
        }

        dto2Components();
      }
    }
  }


  private void jbInit(Vector<?> controls)
      throws Throwable {

    // holt sich die gewuenschten Buttons

    enableToolsPanelButtons(handler.getButtons());

    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    //Actionpanel von Oberklasse holen und anhaengen.
    panelButtonAction = getToolsPanel();
    this.setActionMap(null);

    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine dynamischen felder
    jPanelWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    for (int i = 0; i < controls.size(); i++) {
      if(handler.getControl(i).getComponent() instanceof WrapperLabel) {
        WrapperLabel wlaText = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
            handler.getControl(i).getLabelbez()));
        wlaText.setHorizontalAlignment(SwingConstants.LEFT);
        jPanelWorkingOn.add(wlaText,
                            new GridBagConstraints(1, i, 1, 1, 0.1, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(2, 2, 2, 2), 0, 0));
      }
      else {
        jPanelWorkingOn.add(new WrapperLabel(LPMain.getInstance()
                                             .getTextRespectUISPr(
                                                 handler.getControl(i)
                                                 .getLabelbez())),
                            new GridBagConstraints(0, i, 1, 1, 0.1, 0.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL,
            new Insets(2, 2, 2, 2), 0, 0));
        double dWeighty = 0.0;
        int iFill = GridBagConstraints.HORIZONTAL;
        // MB 26.07.06 Editorfelder nehmen sich die verfuegbare Hoehe
        if(handler.getControl(i).getComponent() instanceof WrapperEditorField) {
          dWeighty = 1.0;
          iFill = GridBagConstraints.BOTH;
        }
        jPanelWorkingOn.add(handler.getControl(i).getComponent(),
                            new GridBagConstraints(1, i, 1, 1, 0.1, dWeighty,
            GridBagConstraints.CENTER,
            iFill,
            new Insets(2, 2, 2, 2), 0, 0));
      }
    }

  }


  protected String getLockMeWer()
      throws Exception {
    return this.lockme;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    try {
      super.eventYouAreSelected(false);
    }
    catch (Throwable ex) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> eventYouAreSelected(): " +
                        "super.eventYouAreSelected()"));
    }

    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key != null && key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);

      clearStatusbar();
    }
    else {
      delegate = getMyDelegate();

      // holt sich die Methode zum Finden des PK
      try {
        mtd = delegate.getClass().getMethod(
            handler.getIdent().toLowerCase() + "FindByPrimaryKey",
            new Class[] {key.getClass()});
      }
      catch (SecurityException ex1) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventYouAreSelected(): " +
                          "SecurityException"));
      }

      // fuehrt die Methode aus und holt sich die gewuenschte Dto anhand des PK
      try {
        dto = mtd.invoke(delegate, new Object[] {key});
      }
      catch (InvocationTargetException ex2) {
        throw ex2.getTargetException();
      }
      catch (IllegalArgumentException ex2) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventYouAreSelected(): " +
                          "IllegalArgumentException"));
      }
      catch (IllegalAccessException ex2) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventYouAreSelected(): " +
                          "IllegalAccessException"));
      }

      // holt sich die Methode zum Setzen der Dto
      mtd = getDtoZugriffsklasse().getClass().getMethod("set" + handler.getDto(),
          new Class[] {dto.getClass()});

      // ruft die Methode auf und setzt die Dto auf die vorher per PK Geholte
      try {
        mtd.invoke(getDtoZugriffsklasse(), new Object[] {dto});
      }
      catch (InvocationTargetException ex4) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventYouAreSelected(): " +
                          "InvocationTargetException"));
      }
      catch (IllegalArgumentException ex4) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventYouAreSelected(): " +
                          "IllegalArgumentException"));
      }
      catch (IllegalAccessException ex4) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> eventYouAreSelected(): " +
                          "IllegalAccessException"));
      }

      dto2Components();
    }
  }


  /**
   * holt sich die entsprechenden Dto und gibt diese als Objekt zurueck
   *
   * @return Object
   * @throws ExceptionLP
   * @throws NoSuchMethodException
   */
  private Object getMyDto()
      throws ExceptionLP, NoSuchMethodException {
    // holt sich den Methodenaufruf fuer die Dto
    try {
      mtd = getDtoZugriffsklasse().getClass().getMethod("get" + handler.getDto());
    }
    catch (SecurityException ex) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDto(): " +
                        "SecurityException"));
    }
//    catch (NoSuchMethodException ex) {
//      throw new ExceptionLP(
//          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
//          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDto(): " +
//                        "NoSuchMethodException: get" + handler.getDto()));
//    }

    // ruft die Methode auf und gibt die Dto zurueck
    try {
      Object myDto = mtd.invoke(getDtoZugriffsklasse());
      if (myDto == null) {
        throw new ExceptionLP(
            EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
            new Throwable("Fehler in PanelStammdatenCRUD -> getMyDto(): " +
                          handler.getDto() + " ist NULL!"));
      }

      return myDto;
    }
    catch (InvocationTargetException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDto(): " +
                        "InvocationTargetException"));
    }
    catch (IllegalArgumentException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDto(): " +
                        "IllegalArgumentException"));
    }
    catch (IllegalAccessException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDto(): " +
                        "IllegalAccessException"));
    }
  }


  /**
   * holt sich die zugehoerige sprDto und gibt dieses als Object zurueck
   *
   * @return Object
   * @throws ExceptionLP
   * @throws NoSuchMethodException
   */
  private Object getMySprDto()
      throws ExceptionLP, NoSuchMethodException {
    Object dto = getMyDto();
    // holt sich den Methodenaufruf fuer die sprDto
    try {
      mtd = dto.getClass().getMethod("get" + handler.getSprDto());
    }
    catch (SecurityException ex) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMySprDto(): " +
                        "SecurityException"));
    }
    catch (NoSuchMethodException ex) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMySprDto(): " +
                        "NoSuchMethodException: get" + handler.getSprDto()));
    }
    // ruft die Methode auf und gibt die sprDto zurueck
    try {
      return mtd.invoke(dto);
    }
    catch (InvocationTargetException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMySprDto(): " +
                        "InvocationTargetException"));

    }
    catch (IllegalArgumentException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMySprDto(): " +
                        "IllegalArgumentException"));
    }
    catch (IllegalAccessException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMySprDto(): " +
                        "IllegalAccessException"));
    }
  }


  /**
   * holt sich den entsprechenden Delegater und gibt diesen als Objekt zurueck
   *
   * @return Object
   * @throws ExceptionLP
   */
  private Object getMyDelegate()
      throws ExceptionLP {
    // holt sich den Methodenaufruf fuer den Delegater
    try {
      mtd = DelegateFactory.getInstance().getClass().getMethod("get" +
          handler.getDelegate());
    }
    catch (SecurityException ex) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDelegate(): " +
                        "SecurityException"));
    }
    catch (NoSuchMethodException ex) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDelegate(): " +
                        "NoSuchMethodException: get" +
                        handler.getDelegate()));
    }
    // ruft die Methode auf und gibt den Delegater zurueck
    try {
      return mtd.invoke(DelegateFactory.getInstance());
    }
    catch (InvocationTargetException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDelegate(): " +
                        "InvocationTargetException"));
    }
    catch (IllegalArgumentException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDelegate(): " +
                        "IllegalArgumentException"));

    }
    catch (IllegalAccessException ex1) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Throwable("Fehler in PanelStammdatenCRUD -> getMyDelegate(): " +
                        "IllegalAccessException"));
    }

  }


  /**
   * Diese Methode behandelt als PK-Feld markierte Controls im Panel true: als
   * PK-Feld markierte Controls werden aktiviert -> wenn ein neuer Datensatz
   * angelegt wird false: als PK-Feld markierte Controls werden deaktiviert ->
   * beim Bearbeiten eines Datensatzes
   *
   * @param isActivatable boolean
   * @throws ExceptionLP
   */
  private void aktivierePKFelder(boolean isActivatable)
      throws ExceptionLP {
    int size = this.lpControls.size();

    for (int i = 0; i < size; i++) {
      if ( ( (LPControl) lpControls.get(i)).getIstPKFeld()) {
        ( (LPControl) lpControls.get(i)).addAttribut("setActivatable",
            String.valueOf(isActivatable));
      }
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return (JComponent)((LPControl) lpControls.get(0)).getComponent();
  }


}
