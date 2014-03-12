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
package com.lp.editor;

import java.io.File;

/**
 *
 * <p><I>Ein FileFilter fuer die Speichern und Oeffnen
 * Dialoge</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>September 2003</I></p>
 * <p> </p>
 * @author Kajetan Fuchsberger
 * @author Sascha Zelzer
 * @version $Revision: 1.2 $
 */

public class LpFileFilter
    extends javax.swing.filechooser.FileFilter {

  /** Datei - Endung fuer Jasper XML - Dokumente */
  public final static String FILE_EXTENSION_JRXML = ".jrxml";

  /** Datei - Endung fuer Jasper Report - Dokumente */
  public final static String FILE_EXTENSION_JASPER = ".jasper";

  /** Extension der zu Akzeptierenden Dateien */
  private String sExtension;

  /** Bezeichnung der zu akzeptierenden Dateien */
  private String sDescription;

  /**
   * Konstruktor, der einen neuen FileFilter mit der entsprechenden Endung
   * und Bezeichnung anlegt.
   *
   * @param sExtension Die Dateiendung der zu akzeptierenden Dateien.
   * @param sDescription Die entsprechende Beschreibung zu der angegebenen
   *        Dateinendung.
   */
  public LpFileFilter(String sExtension, String sDescription) {
    this.sExtension = sExtension.toLowerCase();
    this.sDescription = sDescription;
  }

  /**
   * Konstruktor, nur mit einer Dateiendung aufzurufen.
   * -> Bezeichnung wird automatisch auf "*.xxx" gesetzt.
   *
   * @param sExtension Die Dateiendung der zu akzeptierenden Dateien.
   */
  public LpFileFilter(String sExtension) {
    this(sExtension, "*" + sExtension);
  }

  /**
   * Bestimmt, ob das angegebene File akzeptiert wird, oder nicht.
   *
   * @param file Das File, das ueberprueft werden soll.
   * @return true: File akzeptiert, false: File abgelehnt.
   */
  public boolean accept(File file) {
    if (file.isDirectory()) {
      return true;
    }
    else if (file.getName().toLowerCase().endsWith(sExtension)) {
      return true;
    }
    else {
      return false;
    }
  }

  public String getDescription() {
    return sDescription;
  }

  public String getExtension() {
    return sExtension;
  }

}
