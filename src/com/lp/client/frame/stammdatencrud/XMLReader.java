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


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.LPMain;
import com.lp.util.EJBExceptionLP;


/**
 * <p>Diese Klasse kuemmert sich um das Einlesen der XML-Datei mit
 * den Panel-spezifischen Eigenschaften.<br><br>
 * Allgemeine Einstellungen werden in den korrespondierenden
 * LPControlHandler eingetragen.<br>
 * Es werden neue LPControls erzeugt und mit den gewuenschten Eigenschaften
 * aus der Konfigurationsdatei versorgt.
 * </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>16.03.05</I></p>
 *
 * <p> </p>
 *
 * @author Christian Winhart
 * @version $Revision: 1.2 $
 */

public class XMLReader
{
  private LPControlHandler controlHandler;
  private InternalFrame internalFrame;
  private String add2Title;

  /**
   * Konstruktor: Dateiname der Konfigurationsdatei und Instanz des zugehoerigen
   * LPControlHandlers werden uebergeben
   *
   * @param filenameI String
   * @param handlerI LPControlHandler
   * @param internalFrameI InternalFrame
   * @param add2TitleI String
   * @throws Throwable
   */
  public XMLReader(String filenameI, LPControlHandler handlerI,
                   InternalFrame internalFrameI, String add2TitleI)
      throws Throwable {

    this.internalFrame = internalFrameI;
    this.add2Title = add2TitleI;

    init(filenameI, handlerI);
  }


  private void init(String filenameI, LPControlHandler handlerI)
      throws Throwable {
    // Positionen im XML-File
    final String IDENT = "ident";
    final String KEY = "key";
    final String DELEGATE = "delegate";
    final String BUTTONS = "buttons";

    controlHandler = handlerI;
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    Document doc = null;
    File f = null;
    InputStream in = null;

    docBuilder = docBuilderFactory.newDocumentBuilder();

    f = LPMain.getInstance().getXMLFileByName(filenameI);

    try {
      if (!f.exists()) {
        // bei Kunden: xml aus .jar holen
        in = LPMain.getInstance().getXMLFileFromJar(filenameI);
        doc = docBuilder.parse(in);
      }
      else {
        // in dev
        doc = docBuilder.parse(f);
      }
    }
    catch (Exception ex) {
      throw new ExceptionLP(
          EJBExceptionLP.FEHLER_STAMMDATENCRUD_FEHLER,
          new Exception("Bitte in Klasse" +
                        " XMLReader in Methode init das Parsen ueberpruefen!" +
                        " Bitte auch die xml-Datei selbst und deren Pfadangabe in Klasse" +
                        " HelperClient ueberpruefen!"));
    }

    // allgemeine Einstellungen fuer alle Controls holen
    Node root = doc.getDocumentElement();

    handlerI.setIdent(root.getAttributes().getNamedItem(IDENT).getNodeValue());
    handlerI.setKey(root.getAttributes().getNamedItem(KEY).getNodeValue());
    handlerI.setDelegate(root.getAttributes().getNamedItem(DELEGATE).getNodeValue());
    handlerI.setButtons(root.getAttributes().getNamedItem(BUTTONS).getNodeValue());

    readControls(root.getChildNodes());
  }


  /**
   * liest die einzelnen Controls aus, erzeugt davon neue LPControls und
   * uebergibt diese an den LPControlHandler
   *
   * @param nl NodeList
   * @throws IOException
   * @throws ExceptionLP
   */
  public void readControls(NodeList nl)
      throws IOException, ExceptionLP {
    final String KLASSE = "klasse";
    final String COLNAME = "colname";
    final String DTYP = "dtyp";
    final String LABELBEZ = "labelbez";
    final String ISTSPRACHE = "istsprache";

    NodeList nlAttribute = null;
    NamedNodeMap nm;
    LPControl lpc;
    for (int i = 0; i < nl.getLength(); i++) {
      if (nl.item(i).getNodeType() == 1) {
        nm = nl.item(i).getAttributes();
        String conClass = nm.getNamedItem(KLASSE).getNodeValue();
        if (conClass.equals("com.lp.client.frame.component.WrapperEditorField")) {
          lpc = new LPControl(conClass, internalFrame, add2Title);
        }
        else {
          lpc = new LPControl(nm.getNamedItem(KLASSE).getNodeValue());
        }
        if (!conClass.equals("com.lp.client.frame.component.WrapperLabel")) {
          lpc.setColName(nm.getNamedItem(COLNAME).getNodeValue());
          lpc.setDTyp(nm.getNamedItem(DTYP).getNodeValue());
        }
        lpc.setLabelbez(nm.getNamedItem(LABELBEZ).getNodeValue());
        try {
          nm.getNamedItem(ISTSPRACHE).getNodeValue();
          lpc.setIstSprache(true);
          controlHandler.setHatSprache(true);
        }
        catch (Throwable e) {
          // kein ExceptionHandling noetig, da tag istsprache nicht unbedingt
          // vorhanden sein muss!
        }

        if (nl.item(i).hasChildNodes()) {
          nlAttribute = nl.item(i).getChildNodes();
        }

        for (int j = 0; j < nlAttribute.getLength(); j++) {
          if (nlAttribute.item(j).getNodeType() == 1) {
            lpc.addAttribut(nlAttribute.item(j).getAttributes().
                            item(0).getNodeName(),
                            nlAttribute.item(j).getAttributes().
                            item(0).getNodeValue()
                );
          }
        }
        controlHandler.addControl(lpc);
      }
    }
  }
}
