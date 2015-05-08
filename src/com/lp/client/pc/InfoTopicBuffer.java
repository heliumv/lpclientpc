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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.lp.util.Helper;


/** @todo JO mit pastebuffer zusammenfassen!  PJ 5378 */
/**
 *
 * <p> Diese Klasse kuemmert sich um die infotopics.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 18.10.06</p>
 *
 * <p>@author $Author: Gerold $</p>
 *
 * @version not attributable Date $Date: 2011/12/13 14:51:52 $
 */
public class InfoTopicBuffer
{

  private static final String DIR_INFOTOPICBUFFER = "infotopicbuffer";

  public static final String FILE_INFOTOPICBUFFER = "allinfo.txt";

  public InfoTopicBuffer() {
	  init() ;
  }


  public void init() {
    File dirInfoTopic = new File(DIR_INFOTOPICBUFFER);
    dirInfoTopic.mkdir();
  }


  public void add2InfoTopicBuffer(String sMsgI)
      throws IOException {

    String sMsgOld = getInfoTopic(FILE_INFOTOPICBUFFER);

    File fileInfoTopic = null;
    if ( (fileInfoTopic = existsInfoTopic(FILE_INFOTOPICBUFFER)) == null) {
      fileInfoTopic = new File(DIR_INFOTOPICBUFFER, FILE_INFOTOPICBUFFER);
      fileInfoTopic.createNewFile();
    }

    FileWriter fw = new FileWriter(fileInfoTopic);

    fw.append(sMsgOld);
    fw.append(Helper.CR_LF);

    String sLine = new Date(System.currentTimeMillis()).toString();
    fw.append(sLine);
    fw.append(Helper.CR_LF);

    fw.append(sMsgI);
    fw.append(Helper.CR_LF);
    fw.close();
  }


  public File existsInfoTopic(String filenameInfoTopicI)
      throws IOException {

    File fileInfoTopic = null;

    try {
      fileInfoTopic = new File(DIR_INFOTOPICBUFFER, filenameInfoTopicI);
      if (!fileInfoTopic.exists() || fileInfoTopic.length() == 0) {
        fileInfoTopic = null;
      }
    }
    catch (Exception ex) {
      //nothing here
    }

    return fileInfoTopic;
  }


  public String getInfoTopic(String filenameInfoTopicI)
      throws IOException {

    StringBuffer sDoc = null;

    try {
      sDoc = new StringBuffer();
      File fileInfoTopic = new File(DIR_INFOTOPICBUFFER, filenameInfoTopicI);
      BufferedReader in = new BufferedReader(new FileReader(fileInfoTopic));
      String line = null;
      while ( (line = in.readLine()) != null) {
        sDoc.append(line + Helper.CR_LF);
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
