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


import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.ejb.FunktionsprPK;
import com.lp.server.system.ejb.PositionsartsprPK;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.BelegartsprDto;
import com.lp.server.system.service.FunktionDto;
import com.lp.server.system.service.FunktionsprDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LieferartsprDto;
import com.lp.server.system.service.LocaleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.PositionsartDto;
import com.lp.server.system.service.PositionsartsprDto;
import com.lp.server.system.service.StatusDto;
import com.lp.server.system.service.StatussprDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.util.Helper;


public class LocaleDelegate
    extends Delegate
{
  private LocaleFac localeFac = null;
  private Context context;

  public LocaleDelegate()
      throws ExceptionLP {

    try {
      context = new InitialContext();
      localeFac = (LocaleFac) context.lookup("lpserver/LocaleFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

  }




  public String createLpLocale(LocaleDto localeDto)
      throws ExceptionLP {

    String cNr = null;
    try {
      cNr = localeFac.createLocale(localeDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return cNr;
  }


  public void removeLpLocale(String cNr)
      throws ExceptionLP {

    try {
      localeFac.removeLocale(cNr);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeLpLocale(LocaleDto localeDto)
      throws ExceptionLP {

    try {
      localeFac.removeLocale(localeDto);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public void updateLpLocale(LocaleDto localeDto)
      throws ExceptionLP {

    try {
      localeFac.updateLocale(localeDto);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public Map<?, ?> getAllLocales(Locale inLocale)
      throws ExceptionLP {

    Map<?, ?> m = null;
    try {
      m = localeFac.getAllLocales(inLocale);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
    return m;
  }



  public HashMap<?, ?> getAllStatiIcon()
      throws ExceptionLP {

    HashMap<?, ?> m = null;
    try {
      m = localeFac.getAllStatiIcon();
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
    return m;
  }



  public LocaleDto localeFindByPrimaryKey(String cNr)
      throws ExceptionLP {

    LocaleDto l = null;
    try {
      l = localeFac.localeFindByPrimaryKey(cNr);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
    return l;
  }


//*** Waehrung *****************************************************************
   public void removeWaehrung(WaehrungDto waehrungDto)
       throws ExceptionLP {
     try {
       localeFac.removeWaehrung(waehrungDto, LPMain.getTheClient());
     }
     catch (Throwable t) {
       handleThrowable(t);
     }
   }


  public void updateWaehrung(WaehrungDto waehrungDto)
      throws ExceptionLP {
    try {
      localeFac.createUpdateWaehrung(waehrungDto, LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  /**
   * Hole alle Waehrungen.
   *
   * @throws ExceptionLP
   * @return Map Alle Waehrungen.
   */
  public Map<?, ?> getAllWaehrungen()
      throws ExceptionLP {
    Map<?, ?> m = null;
    try {
      m = localeFac.getAllWaehrungen();
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
    return m;
  }


//*** Wechselkurs **************************************************************
   public void removeWechselkurs(WechselkursDto wechselkursDto)
       throws ExceptionLP {
     try {
       localeFac.removeWechselkurs(wechselkursDto, LPMain.getTheClient());
     }
     catch (Throwable t) {
       handleThrowable(t);
     }
   }


  public void updateWechselkurs(WechselkursDto wechselkursDto)
      throws ExceptionLP {
    try {
      localeFac.updateWechselkurs(wechselkursDto, LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public WechselkursDto wechselkursFindByPrimaryKey(
      String waehrungCNrVon,
      String waehrungCNrZu,
      java.util.Date tDatum)
      throws ExceptionLP {

    WechselkursDto w = null;
    try {
      w = localeFac.wechselkursFindByPrimaryKey(waehrungCNrVon, waehrungCNrZu, tDatum);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
    return w;
  }



  public BigDecimal getWechselkurs2(String waehrungLocaleCNrVon,
                              String waehrungLocaleCNrZu)
      throws ExceptionLP {

    BigDecimal fWechselkurs = new BigDecimal(1); //Von==Zu
    try {
      if (!waehrungLocaleCNrVon.equals(waehrungLocaleCNrZu)) {
        fWechselkurs = localeFac.getWechselkurs2(waehrungLocaleCNrVon,
                                                waehrungLocaleCNrZu,
                                                LPMain.getTheClient());
      }
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return fWechselkurs;
  }


  /**
   * Einen Betrag von einer Waehrung in die andere umrechnen.
   *
   * @param bdBetragI der Betrag in der Ausgangswaehrung
   * @param cCurrency1I Ausgangswaehrung
   * @param cCurrency2I Zielwaehrung
   * @return BigDecimal der Betrag in der Zielwaehrung
   * @throws ExceptionLP
   */
  public BigDecimal rechneUmInAndereWaehrung(BigDecimal bdBetragI,
                                             String cCurrency1I,
                                             String cCurrency2I)
      throws ExceptionLP {

    try {
      return localeFac.rechneUmInAndereWaehrungZuDatum(bdBetragI, cCurrency1I, cCurrency2I, 
    		  new Date(System.currentTimeMillis()), LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  /**
   * Einen Betrag von einer Waehrung in die andere umrechnen.
   *
   * @param bdBetragI der Betrag in der Ausgangswaehrung
   * @param bdKursMandantenwaehrungZuBelegwaehrung bdKursMandantenwaehrungZuBelegwaehrung
   * @return BigDecimal der Betrag in der Zielwaehrung
   * @throws ExceptionLP
   */
  public BigDecimal rechneUmInMandantenWaehrung(BigDecimal bdBetragI,
                                                BigDecimal
                                                bdKursMandantenwaehrungZuBelegwaehrung)
      throws ExceptionLP {

    try {
      return localeFac.rechneUmInMandantenWaehrung(bdBetragI, bdKursMandantenwaehrungZuBelegwaehrung);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }




  //*** Belegart *************************************************************************

   // stdcrud: 6 delegate -> create
   public String createBelegart(BelegartDto belegartDtoI)
       throws ExceptionLP {
     String s = null;
     try {
       return localeFac.createBelegart(belegartDtoI, LPMain.getTheClient());
     }
     catch (Throwable t) {
       handleThrowable(t);
     }

     return s;
   }


  // stdcrud: 9 delegate -> FindByPrimaryKey
  public BelegartDto belegartFindByPrimaryKey(String cNrI)
      throws ExceptionLP {

    BelegartDto belegartDto = null;
    try {
      belegartDto = localeFac.belegartFindByPrimaryKey(
          cNrI, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return belegartDto;
  }


  public void removeBelegart(String cNr)
      throws ExceptionLP {

    try {
      localeFac.removeBelegart(cNr);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  // stdcrud: 8 delegate -> remove
  public void removeBelegart(BelegartDto belegartDto)
      throws ExceptionLP {
    try {
      localeFac.removeBelegart(belegartDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  // stdcrud: 7 delegate -> update
  public void updateBelegart(BelegartDto belegartDto)
      throws ExceptionLP {
    try {
      localeFac.updateBelegart(belegartDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public BelegartDto belegartFindByPrimaryKey(String cNrI, TheClientDto theClientDto)
      throws ExceptionLP {
    BelegartDto b = null;
    try {
      b = localeFac.belegartFindByPrimaryKey(cNrI, theClientDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return b;
  }


  public BelegartDto[] belegartFindAll()
      throws ExceptionLP {
    BelegartDto[] ab = null;
    try {
      ab = localeFac.belegartFindAll();
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return ab;
  }


  public void createBelegartspr(BelegartsprDto belegartsprDto)
      throws
      ExceptionLP {
    try {
      localeFac.createBelegartspr(belegartsprDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeBelegartspr(String spracheCNr, String belegartCNr)
      throws ExceptionLP {
    try {
      localeFac.removeBelegartspr(spracheCNr, belegartCNr);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeBelegartspr(BelegartsprDto belegartsprDto)
      throws ExceptionLP {
    try {
      localeFac.removeBelegartspr(belegartsprDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updateBelegartspr(BelegartsprDto belegartsprDto)
      throws ExceptionLP {

    try {
      localeFac.updateBelegartspr(belegartsprDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public BelegartsprDto belegartsprFindByPrimaryKey(String belegartCNr)
      throws ExceptionLP {
    BelegartsprDto b = null;
    try {
      b = localeFac.belegartsprFindByPrimaryKey(
          belegartCNr,
          Helper.locale2String(LPMain.getInstance().getUISprLocale()));
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return b;
  }


  /**
   * Alle Belegarten in bestmoeglicher Uebersetzung holen.
   * @param pLocale1 Uebersteuerung der UI Sprache des Benutzers
   * @param pLocale2 UI Sprache des Benutzers
   * @throws ExceptionLP
   * @return Map
   */
  public Map<?, ?> getAllBelegartenUebersetzt(Locale pLocale1,
                                        Locale pLocale2)
      throws ExceptionLP {

    Map<?, ?> m = null;
    try {
      m = localeFac.getAllBelegartenUebersetzt(pLocale1, pLocale2);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return m;
  }


  /**
   * Zu einer Belegart das gesamte Dto holen. Enthaelt die Kurzbezeichnung.
   * @param pCNr String
   * @throws ExceptionLP
   * @return BelegartDto
   */
  public BelegartDto belegartFindByCNr(String pCNr)
      throws ExceptionLP {
    BelegartDto b = null;
    try {
      b = localeFac.belegartFindByCNr(pCNr);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return b;
  }


  //*** Lieferart ************************************************************************
   public Integer createLieferart(LieferartDto lieferartDto)
       throws ExceptionLP {

     Integer i = null;
     try {
       i = localeFac.createLieferart(lieferartDto, LPMain.getTheClient());
     }
     catch (Throwable ex) {
       handleThrowable(ex);
     }
     return i;
   }


  public void createLieferartspr(LieferartsprDto lieferartsprDto)
      throws ExceptionLP {
    try {
      localeFac.createLieferartspr(lieferartsprDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeLieferart(Integer iId)
      throws ExceptionLP {
    try {
      localeFac.removeLieferart(iId, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeLieferart(LieferartDto lieferartDto)
      throws ExceptionLP {
    try {
      localeFac.removeLieferart(lieferartDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updateLieferart(LieferartDto lieferartDto)
      throws ExceptionLP {
    try {
      localeFac.updateLieferart(lieferartDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updateLieferartspr(LieferartsprDto lieferartsprDto)
      throws ExceptionLP {
    try {
      localeFac.updateLieferartspr(lieferartsprDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public LieferartDto lieferartFindByPrimaryKey(Integer iId)
      throws ExceptionLP {
    LieferartDto lieferartDto = null;
    try {
      lieferartDto = localeFac.lieferartFindByPrimaryKey(iId,
          LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return lieferartDto;
  }


  public LieferartsprDto lieferartsprFindByPrimaryKey(Integer lieferartIId)
      throws ExceptionLP {
    LieferartsprDto l = null;
    try {
      l = localeFac.lieferartsprFindByPrimaryKey(
          lieferartIId,
          Helper.locale2String(LPMain.getInstance().getUISprLocale()),
          LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return l;
  }


  //*** Status *****************************************************************
   public String createStatus(StatusDto statusDto)
       throws ExceptionLP {
     String cNr = null;
     try {
       cNr = localeFac.createStatus(statusDto, LPMain.getTheClient());
     }
     catch (Throwable ex) {
       handleThrowable(ex);
     }
     return cNr;
   }


  public void removeStatus(StatusDto statusDto)
      throws ExceptionLP {
    try {
      localeFac.removeStatus(statusDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updateStatus(StatusDto statusDto)
      throws ExceptionLP {
    try {
      localeFac.updateStatus(statusDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public StatusDto statusFindByPrimaryKey(String cNrI)
      throws ExceptionLP {

    StatusDto sStatusDto = null;
    try {
      sStatusDto = localeFac.statusFindByPrimaryKey(cNrI,
          LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return sStatusDto;
  }


  public WaehrungDto waehrungFindByPrimaryKey(String cNrI)
      throws ExceptionLP {

    WaehrungDto waehrungDto = null;
    try {
      waehrungDto = localeFac.waehrungFindByPrimaryKey(cNrI);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return waehrungDto;
  }


  //*** Statussrp **************************************************************
   public StatussprDto statussprFindByPrimaryKey(String cNrI)
       throws ExceptionLP {

     StatussprDto status2sprDto = null;
     try {
       status2sprDto = localeFac.statussprFindByPrimaryKey(
           cNrI,
           Helper.locale2String(LPMain.getInstance().getUISprLocale()),
           LPMain.getTheClient()
           );
     }
     catch (Throwable ex) {
       handleThrowable(ex);
     }
     return status2sprDto;
   }


  public void updateStatusspr(StatussprDto statussprDtoI)
      throws ExceptionLP {
    try {
      localeFac.updateStatusspr(statussprDtoI, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeStatusspr(StatussprDto statussprDtoI)
      throws ExceptionLP {
    try {
      localeFac.removeStatusspr(statussprDtoI, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void createStatusspr(StatussprDto statussprDtoI)
      throws ExceptionLP {
    try {
      localeFac.createStatusspr(statussprDtoI, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


//*** Funktionspr **************************************************************
   public FunktionsprPK createFunktionspr(FunktionsprDto funktionsprDto)
       throws ExceptionLP {
     FunktionsprPK f = null;
     try {
       f = localeFac.createFunktionspr(funktionsprDto, LPMain.getTheClient());
     }
     catch (Throwable ex) {
       handleThrowable(ex);
     }
     return f;
   }


  public void removeFunktionspr(Integer funktionIId, String localeCNr)
      throws ExceptionLP {
    try {
      localeFac.removeFunktionspr(funktionIId, localeCNr, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeFunktionspr(FunktionsprDto funktionsprDto)
      throws ExceptionLP {
    try {
      localeFac.removeFunktionspr(funktionsprDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updateFunktionspr(FunktionsprDto funktionsprDto)
      throws ExceptionLP {
    try {
      localeFac.updateFunktionspr(funktionsprDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public FunktionsprDto funktionsprFindByPrimaryKey(Integer funktionIId, String localeCNr)
      throws ExceptionLP {

    FunktionsprDto f = null;
    try {
      f = localeFac.funktionsprFindByPrimaryKey(funktionIId, localeCNr,
                                                LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return f;
  }


  public FunktionsprDto[] funktionsprFindByFunktionIId(Integer funktionIId)
      throws ExceptionLP {
    FunktionsprDto[] af = null;
    try {
      af = localeFac.funktionsprFindByFunktionIId(funktionIId,
                                                  LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return af;
  }


//*** Funktion *****************************************************************
   public Integer createFunktion(FunktionDto funktionDto)
       throws ExceptionLP {

     Integer iId = null;
     try {
       iId = localeFac.createFunktion(funktionDto, LPMain.getTheClient());
     }
     catch (Throwable ex) {
       handleThrowable(ex);
     }
     return iId;
   }


  public void removeFunktion(Integer iId)
      throws ExceptionLP {
    try {
      localeFac.removeFunktion(iId, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removeFunktion(FunktionDto funktionDto)
      throws ExceptionLP {
    try {
      localeFac.removeFunktion(funktionDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updateFunktion(FunktionDto funktionDto)
      throws ExceptionLP {
    try {
      localeFac.updateFunktion(funktionDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public FunktionDto funktionFindByPrimaryKey(Integer iId)
      throws ExceptionLP {
    FunktionDto f = null;
    try {
      f = localeFac.funktionFindByPrimaryKey(iId, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return f;
  }


  //*** Positionsartspr ********************************************************
   public PositionsartsprPK createPositionsartspr(PositionsartsprDto positionsartsprDto)
       throws ExceptionLP {
     PositionsartsprPK p = null;
     try {
       p = localeFac.createPositionsartspr(positionsartsprDto,
                                           LPMain.getTheClient());
     }
     catch (Throwable ex) {
       handleThrowable(ex);
     }
     return p;
   }


  public void removePositionsartspr(String positionsartCNr, String localeCNr)
      throws ExceptionLP {
    try {
      localeFac.removePositionsartspr(positionsartCNr, localeCNr,
                                      LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removePositionsartspr(PositionsartsprDto positionsartsprDto)
      throws ExceptionLP {
    try {
      localeFac.removePositionsartspr(positionsartsprDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updatePositionsartspr(PositionsartsprDto positionsartsprDto)
      throws ExceptionLP {
    try {
      localeFac.updatePositionsartspr(positionsartsprDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public PositionsartsprDto positionsartsprFindByPrimaryKey(
      String positionsartCNr,
      String localeCNr)
      throws ExceptionLP {
    PositionsartsprDto p = null;
    try {
      p = localeFac.positionsartsprFindByPrimaryKey(
          positionsartCNr, localeCNr, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return p;
  }


  //*** Positionsart ***********************************************************
   public String createPositionsart(PositionsartDto positionsartsprDto)
       throws ExceptionLP {
     String p = null;
     try {
       p = localeFac.createPositionsart(positionsartsprDto,
                                        LPMain.getTheClient());
     }
     catch (Throwable ex) {
       handleThrowable(ex);
     }
     return p;
   }


  public void removePositionsart(String positionsartCNr, String localeCNr)
      throws ExceptionLP {
    try {
      localeFac.removePositionsart(positionsartCNr,
                                   LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void removePositionsart(PositionsartDto positionsartsprDto)
      throws ExceptionLP {
    try {
      localeFac.removePositionsart(
          positionsartsprDto,
          LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public void updatePositionsart(PositionsartDto positionsartsprDto)
      throws ExceptionLP {
    try {
      localeFac.updatePositionsart(positionsartsprDto, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public PositionsartDto positionsartFindByPrimaryKey(String positionsartCNr)
      throws ExceptionLP {

    PositionsartDto p = null;
    try {
      p = localeFac.positionsartFindByPrimaryKey(
          positionsartCNr, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return p;
  }
  
  
  public WechselkursDto getKursZuDatum(String waehrungCNrVonI,
			String waehrungCNrNachI, java.sql.Date dDatum) throws ExceptionLP {
	  WechselkursDto wechselkursDto = null;
	  try{
		  wechselkursDto = localeFac.getKursZuDatum(waehrungCNrVonI, waehrungCNrNachI, dDatum, LPMain.getTheClient());
	  } catch (Throwable t){
		  handleThrowable(t);
	  }
	  return wechselkursDto;
  }


  public BigDecimal rechneUmInAndereWaehrungGerundetZuDatum(BigDecimal bdBetragI,
		  String cCurrency1I, String cCurrency2I, java.sql.Date dDatum) throws ExceptionLP {
	  try {
		  return localeFac.rechneUmInAndereWaehrungGerundetZuDatum(bdBetragI, cCurrency1I, cCurrency2I, dDatum, LPMain.getTheClient());
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }
  
  public String getStatusCBez(String statusCnr) throws ExceptionLP {
	  try {
		  return localeFac.getStatusCBez(statusCnr, LPMain.getTheClient());
	  } catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }
}
