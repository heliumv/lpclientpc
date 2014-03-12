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
package com.lp.client.frame.delegate;


import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.DatenformatDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediaartDto;
import com.lp.server.system.service.MediaartsprDto;
import com.lp.server.system.service.MediastandardDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
public class MediaDelegate
    extends Delegate
{
  private Context context;
  private MediaFac mediaFac;
  public MediaDelegate()
      throws ExceptionLP {
    try {
      context = new InitialContext();
      mediaFac = (MediaFac) context.lookup("lpserver/MediaFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public DatenformatDto datenformatFindByPrimaryKey(String cNr)
      throws ExceptionLP {
    try {
      return mediaFac.datenformatFindByPrimaryKey(cNr);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


 


  public void removeMediastandard(MediastandardDto mediastandardDto)
      throws ExceptionLP {
    try {
      mediaFac.removeMediastandard(mediastandardDto, LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public MediastandardDto updateMediastandard(MediastandardDto mediastandardDto)
      throws ExceptionLP {
    try {
      if (mediastandardDto.getIId() != null) {
        return mediaFac.updateMediastandard(mediastandardDto,
                                            LPMain.getInstance().getTheClient());
      }
      else {
        return mediaFac.createMediastandard(mediastandardDto,
                                            LPMain.getInstance().getTheClient());
      }
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public MediastandardDto mediastandardFindByPrimaryKey(Integer mediastandardIId)
      throws ExceptionLP {
    try {
      return mediaFac.mediastandardFindByPrimaryKey(mediastandardIId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public Map<?, ?> getAllDatenformat()
      throws ExceptionLP {
    try {
      return mediaFac.getAllDatenformat(Helper.locale2String(LPMain.getInstance().
          getUISprLocale()));
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  // Mediaart ------------------------------------------------------------------

  public String createMediaart(MediaartDto oMediaartDtoI)
      throws ExceptionLP {
    String cNr = null;

    try {
      cNr = mediaFac.createMediaart(oMediaartDtoI, LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return cNr;
  }


  public void removeMediaart(MediaartDto mediaartDto)
      throws ExceptionLP {
    try {
      mediaFac.removeMediaart(mediaartDto, LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public void updateMediaart(MediaartDto mediaartDto)
      throws ExceptionLP {
    try {
      mediaFac.updateMediaart(mediaartDto, LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public MediaartDto mediaartFindByPrimaryKey(String cNr)
      throws ExceptionLP {
    MediaartDto mediaartDto = null;

    try {
      mediaartDto = mediaFac.mediaartFindByPrimaryKey(cNr,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return mediaartDto;
  }


  // Mediaartspr ---------------------------------------------------------------

  public String createMediaartspr(MediaartsprDto oMediaartsprDtoI)
      throws ExceptionLP {
    String cNr = null;

    try {
      cNr = mediaFac.createMediaartspr(oMediaartsprDtoI, LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return cNr;
  }


  public void removeMediaartspr(MediaartsprDto mediaartsprDto)
      throws ExceptionLP {
    try {
      mediaFac.removeMediaartspr(mediaartsprDto, LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public void updateMediaartspr(MediaartsprDto mediaartsprDto)
      throws ExceptionLP {
    try {
      mediaFac.updateMediaartspr(mediaartsprDto, LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public MediaartsprDto mediaartsprFindByPrimaryKey(String cNr)
      throws ExceptionLP {
    MediaartsprDto mediaartsprDto = null;

    try {
      mediaartsprDto = mediaFac.mediaartsprFindByPrimaryKey(
          cNr,
          Helper.locale2String(LPMain.getInstance().getUISprLocale()),
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return mediaartsprDto;
  }

  public MediastandardDto createDefaultMediastandard(String cNrMediaartI, String localeCNrI)
      throws ExceptionLP {
    MediastandardDto mediastandardDto = null;
    try {
      mediastandardDto = mediaFac.createDefaultMediastandard(cNrMediaartI, localeCNrI,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
    return mediastandardDto;
  }


  public MediastandardDto createMediastandard(MediastandardDto mediastandardDto)
      throws ExceptionLP {
     try {
      mediastandardDto = mediaFac.createMediastandard(mediastandardDto,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
    return mediastandardDto;
  }

  public MediastandardDto mediastandardFindByCNrDatenformatCNr(String cNrI,
      String datenformatCNrI)
      throws ExceptionLP {
    MediastandardDto mediastandardDto = null;
    try {
      mediastandardDto = mediaFac.mediastandardFindByCNrDatenformatCNrMandantCNr(
          cNrI, datenformatCNrI, LPMain.getInstance().getTheClient().getMandant(),
          LPMain.getInstance().getTheClient().getLocUiAsString(),
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
    return mediastandardDto;
  }
}
