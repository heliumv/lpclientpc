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
package com.lp.client.frame.delegate;


import java.util.LinkedHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.ejb.BsmahnstufePK;
import com.lp.server.bestellung.service.BSMahnlaufDto;
import com.lp.server.bestellung.service.BSMahnstufeDto;
import com.lp.server.bestellung.service.BSMahntextDto;
import com.lp.server.bestellung.service.BSMahnungDto;
import com.lp.server.bestellung.service.BSMahnwesenFac;


public class BSMahnwesenDelegate
    extends Delegate
{
  private Context context;
  private BSMahnwesenFac bSMahnwesenFac;
  public BSMahnwesenDelegate()
      throws ExceptionLP {
    try {
      context = new InitialContext();
      bSMahnwesenFac = (BSMahnwesenFac) context.lookup("lpserver/BSMahnwesenFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

  }

  /*BSMahnung*******************************************************************/
  public BSMahnungDto createBSMahnung(BSMahnungDto bSMahnungDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.createBSMahnung(bSMahnungDto, LPMain.getTheClient());
      return bSMahnungDto;

    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public void removeBSMahnung(BSMahnungDto bSMahnungDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.removeBSMahnung(bSMahnungDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }

  public void removeBSMahnung(Integer iId)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.removeBSMahnung(iId, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public BSMahnungDto updateBSMahnung(BSMahnungDto bSMahnungDto)
      throws ExceptionLP {
    try {
      if (bSMahnungDto.getIId() != null) {
        return bSMahnwesenFac.updateBSMahnung(bSMahnungDto,
                                              LPMain.getTheClient());
      }
      else {
        return bSMahnwesenFac.createBSMahnung(bSMahnungDto,
                                              LPMain.getTheClient());
      }
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public void updateBSMahnungs(BSMahnungDto[] bSMahnungDtos)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.updateBSMahnungs(bSMahnungDtos, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public BSMahnungDto bsmahnungFindByPrimaryKey(Integer iId)
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.bsmahnungFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }

  }


  public BSMahnungDto[] bsmahnungFindByBestellpositionIIdOhneExc(Integer iId)
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.bsmahnungFindByBestellpositionIIdOhneExc(iId, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }

  }


  public void mahneBSMahnungRueckgaengig(Integer mahnungIId)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.mahneBSMahnungRueckgaengig(mahnungIId,
                                                LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void mahneBSMahnung(Integer mahnungIId)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.mahneBSMahnung(mahnungIId, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  /*BSMahnlauf******************************************************************/
  public Integer createBSMahnlauf(BSMahnlaufDto bSMahnlaufDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.createBSMahnlauf(bSMahnlaufDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return bSMahnlaufDto.getIId();
  }


  public void removeBSMahnlauf(BSMahnlaufDto bSMahnlaufDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.removeBSMahnlauf(bSMahnlaufDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public void updateBSMahnlauf(BSMahnlaufDto bSMahnlaufDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.updateBSMahnlauf(bSMahnlaufDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public void updateBSMahnlaufs(BSMahnlaufDto[] bSMahnlaufDtos)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.updateBSMahnlaufs(bSMahnlaufDtos, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public BSMahnlaufDto bsmahnlaufFindByPrimaryKey(Integer iId)
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.bsmahnlaufFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }

  }


  public void mahneBSMahnlaufRueckgaengig(Integer bsmahnlaufIId)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.mahneBSMahnlaufRueckgaengig(bsmahnlaufIId,
                                                 LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void mahneBSMahnlauf(Integer bsmahnlaufIId)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.mahneBSMahnlauf(bsmahnlaufIId, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public Boolean bGibtEsEinenOffenenBSMahnlauf()
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.bGibtEsEinenOffenenBSMahnlauf(LPMain.getTheClient().
          getMandant(), LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return Boolean.FALSE;
    }
  }


  public BSMahnlaufDto createBSMahnlaufEchteLiefermahnungen()
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.createBSMahnlaufEchteLiefermahnungen(LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public BSMahnlaufDto createBSMahnlaufABMahnungen()
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.createBSMahnlaufABMahnungen(LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

  public BSMahnlaufDto createBSMahnlaufLiefererinnerung()
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.createBSMahnlaufLiefererinnerung(LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public BSMahnlaufDto createABMahnungenUndLieferMahnungenUndLiefererinnerungen()
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.createABMahnungenUndLieferMahnungenUndLiefererinnerungen(LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  /*BSMahntext******************************************************************/
  public Integer createBSMahntext(BSMahntextDto bSMahntextDto)
      throws ExceptionLP {
    Integer iId = null;
    try {
      iId = bSMahnwesenFac.createBSMahntext(bSMahntextDto,
                                            LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return iId;
  }


  public void removeBSMahntext(BSMahntextDto bSMahntextDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.removeBSMahntext(bSMahntextDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public void updateBSMahntext(BSMahntextDto bSMahntextDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.updateBSMahntext(bSMahntextDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public void updateBSMahntexts(BSMahntextDto[] bSMahntextDtos)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.updateBSMahntexts(bSMahntextDtos, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public BSMahntextDto bsmahntextFindByPrimaryKey(Integer iId)
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.bsmahntextFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }

  }


  public BSMahntextDto bsmahntextFindByMandantLocaleCNr(String localeCNr,
      Integer bsmahnstufeIId)
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.bsmahntextFindByMandantLocaleCNr(
    		  LPMain.getTheClient().getMandant(), localeCNr, bsmahnstufeIId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public BSMahntextDto createDefaultBSMahntext(Integer bsmahnstufeIId,
                                               String sTextinhaltI)
      throws ExceptionLP {
    BSMahntextDto oBSMahntextDto = null;

    try {
      oBSMahntextDto = bSMahnwesenFac.createDefaultBSMahntext(
          bsmahnstufeIId, sTextinhaltI, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

    return oBSMahntextDto;
  }


  /*BSMahnstufe*****************************************************************/
  public Integer createBSMahnstufe(BSMahnstufeDto bSMahnstufeDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.createBSMahnstufe(bSMahnstufeDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return bSMahnstufeDto.getIId();
  }


  public void removeBSMahnstufe(BSMahnstufeDto bSMahnstufeDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.removeBSMahnstufe(bSMahnstufeDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public void updateBSMahnstufe(BSMahnstufeDto bSMahnstufeDto)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.updateBSMahnstufe(bSMahnstufeDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }

  }


  public void updateBSMahnstufes(BSMahnstufeDto[] bSMahnstufeDtos)
      throws ExceptionLP {
    try {
      bSMahnwesenFac.updateBSMahnstufes(bSMahnstufeDtos, LPMain.getTheClient());
    }
    catch (Throwable ex) {
    	handleThrowable(ex);
    }

  }
  
  public BSMahnungDto[] bsmahnungFindByBSMahnlaufIIdLieferantIId(Integer mahnlaufIId,Integer lieferantIId)
	throws ExceptionLP {
	  try {
		return bSMahnwesenFac.bsmahnungFindByBSMahnlaufIIdLieferantIId(mahnlaufIId, lieferantIId, LPMain.
		          getTheClient().getMandant());
	} catch (Throwable e) {
		handleThrowable(e);
	}
	return null;
  }


  public BSMahnstufeDto bsmahnstufeFindByPrimaryKey(Integer iId)
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.bsmahnstufeFindByPrimaryKeyOhneExc(new BsmahnstufePK(iId,
          LPMain.getTheClient().getMandant()));
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }

  }


  public LinkedHashMap<?, ?> getAllBSMahnstufen()
      throws ExceptionLP {
    try {
      return bSMahnwesenFac.getAllBSMahnstufen(
          LPMain.getTheClient().getMandant(),
          LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

}
