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
package com.lp.client.pc;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.lp.client.frame.component.frameposition.LocalSettingsPathGenerator;
import com.lp.client.frame.dialog.DialogFactory;


/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/11/06 12:55:18 $
 */
public class PasteBuffer
{

  private static final String DIR_PASTEBUFFER = new LocalSettingsPathGenerator().getDefaultPath() + "pastebuffer";

  public static final String FILE_PASTEBUFFER_POSITIONS = "positions.xml";
  public static final String FILE_PASTEBUFFER_POSITIONSCLASS = "positions.class";

  public PasteBuffer() {
	  init() ;
  }


  public void init() {
    //PasteBuffer anlegen.
    File dirPasteBuffer = new File(DIR_PASTEBUFFER);
    dirPasteBuffer.mkdir();
  }

  /**
   * @deprecated use writeObjectToPasteBuffer
   * @param sDocI String
   * @param filenamePastebufferI String
   * @throws IOException
   */
  public void updatePasteBuffer(String sDocI, String filenamePastebufferI)
      throws IOException {

    if (existsPasteBuffer(filenamePastebufferI) != null) {
      File filePasteBuffer = new File(DIR_PASTEBUFFER, filenamePastebufferI);
      filePasteBuffer.delete();
    }

    File filePasteBuffer = new File(DIR_PASTEBUFFER, filenamePastebufferI);
    filePasteBuffer.createNewFile();
    FileWriter f = new FileWriter(filePasteBuffer);
    f.write(sDocI);
    f.close();
  }

  /**
   * @param filenamePastebufferI String
   * @return File
   * @throws IOException
   */
  public File existsPasteBuffer(String filenamePastebufferI)
      throws IOException {

    File filePasteBuffer = null;

    try {
      filePasteBuffer = new File(DIR_PASTEBUFFER, filenamePastebufferI);
      if (!filePasteBuffer.exists() || filePasteBuffer.length() == 0) {
        filePasteBuffer = null;
      }
    }
    catch (Exception ex) {
      //nothing here
    }

    return filePasteBuffer;
  }


  public Object readObjectFromPasteBuffer() throws Throwable {
    Object o = null;
    try {
      FileInputStream fs = new FileInputStream(DIR_PASTEBUFFER  + File.separator + LPMain.getTheClient().getIDPersonal()+FILE_PASTEBUFFER_POSITIONSCLASS);
      ObjectInputStream is = new ObjectInputStream(fs);
      o = (Object) is.readObject();
      is.close();
      fs.close();
    }
    catch (FileNotFoundException e) {
    	DialogFactory.showModalDialog(LPMain
				.getTextRespectUISPr("lp.error"), e.getMessage());
    } 
    catch (ClassNotFoundException e) {
      System.err.println(e.toString());
    }
    catch (IOException e) {
      System.err.println(e.toString());
    }
    return o;
  }


  public void writeObjectToPasteBuffer(Object o) throws Throwable {

    try {
      FileOutputStream fs = new FileOutputStream(DIR_PASTEBUFFER + File.separator + LPMain.getTheClient().getIDPersonal()+FILE_PASTEBUFFER_POSITIONSCLASS);
      ObjectOutputStream os = new ObjectOutputStream(fs);
      os.writeObject(o);
      os.flush();
      os.close();
    }
    catch (FileNotFoundException e) {
    	DialogFactory.showModalDialog(LPMain
				.getTextRespectUISPr("lp.error"), e.getMessage());
    } 
    catch (IOException e) {
    	   System.err.println(e.toString());
	}
  }

  /**
   * @deprecated use readObjectFromPasteBuffer
   * @param filenamePastebufferI String
   * @return String
   * @throws IOException
   */
  public String getPasteBuffer(String filenamePastebufferI)
      throws IOException {

    StringBuffer sDoc = null;

    try {
      sDoc = new StringBuffer();
      File filePasteBuffer = new File(DIR_PASTEBUFFER, filenamePastebufferI);
      BufferedReader in = new BufferedReader(new FileReader(filePasteBuffer));
      String line = null;
      while ( (line = in.readLine()) != null) {
        sDoc.append(line);
      }
      in.close();
    }
    catch (FileNotFoundException ex) {
      //nothing here
    }
    catch (IOException ex) {
      //nothing here
    }

    return sDoc.toString();
  }

}
