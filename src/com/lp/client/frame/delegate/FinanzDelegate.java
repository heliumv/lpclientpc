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


import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.KontoImporterResult;
import com.lp.server.finanz.service.KontolaenderartDto;
import com.lp.server.finanz.service.KontolandDto;
import com.lp.server.finanz.service.RechenregelDto;
import com.lp.server.system.service.TheClientDto;

@SuppressWarnings("static-access") 
/**
 * <p><I>Business-Delegate fuer das Finanzmodul</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>02. 09. 2004</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.14 $
 */
public class FinanzDelegate
    extends Delegate
{
  private Context context;
  private FinanzFac finanzFac;

  public FinanzDelegate()
      throws ExceptionLP {
    
    try {
    	context = new InitialContext();
      finanzFac = (FinanzFac) context.lookup("lpserver/FinanzFacBean/remote");
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }




  public void removeKonto(KontoDto kontoDto)
      throws ExceptionLP {
    try {
      finanzFac.removeKonto(kontoDto, LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public KontoDto updateKonto(KontoDto kontoDto)
      throws ExceptionLP {
    myLogger.entry();
    try {
      if (kontoDto.getIId() != null) {
        myLogger.debug("update Konto");
        return finanzFac.updateKonto(kontoDto,
                                     LPMain.getInstance().getTheClient());
      }
      else {
        myLogger.debug("create Konto");
        return finanzFac.createKonto(kontoDto,
                                     LPMain.getInstance().getTheClient());
      }
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public KontoDto kontoFindByPrimaryKey(Integer iId)
      throws
      ExceptionLP {
    try {
      return finanzFac.kontoFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public KontoDtoSmall kontoFindByPrimaryKeySmall(Integer iId)
      throws
      ExceptionLP {
    try {
      return finanzFac.kontoFindByPrimaryKeySmall(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

  public KontoDto kontoFindByCnrKontotypMandantOhneExc(String cNr, String kontotyp, String mandant) 
	      throws ExceptionLP{
	  try {
		  return finanzFac.kontoFindByCnrKontotypMandantOhneExc(cNr, kontotyp, mandant, LPMain.getTheClient());
	  } catch(Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }
  
  public boolean isMitlaufendesKonto(Integer kontoIId) 
	      throws ExceptionLP{
	  try {
		  return finanzFac.isMitlaufendesKonto(kontoIId);
	  } catch(Throwable ex) {
		  handleThrowable(ex);
		  return false;
	  }
  }

  /**
   * 
   * @param sCnr
   * @param sMandant
   * @return
   * @throws ExceptionLP
   * @deprecated use #kontoFindByCnrKontotypCnrMandant instead
   */
  @Deprecated
  public KontoDto kontoFindByCnrMandant(String sCnr, String sMandant)
      throws
      ExceptionLP {
    try {
      return finanzFac.kontoFindByCnrMandant(sCnr, sMandant,
                                             LPMain.getTheClient()
                                             );
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public void removeFinanzamt(FinanzamtDto finanzamtDto)
      throws
      ExceptionLP {
    try {
      finanzFac.removeFinanzamt(finanzamtDto,
                                LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public FinanzamtDto updateFinanzamt(FinanzamtDto finanzamtDto)
      throws
      ExceptionLP {
    myLogger.entry();
    try {
      if (finanzamtDto.getPartnerIId() != null) {
        return finanzFac.updateFinanzamt(finanzamtDto,
                                         LPMain.getInstance().getTheClient());
      }
      else {
        return finanzFac.createFinanzamt(finanzamtDto,
                                         LPMain.getInstance().getTheClient());
      }
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public FinanzamtDto finanzamtFindByPrimaryKey(Integer partnerIId,
                                                String mandantCNr)
      throws
      ExceptionLP {
    try {
      return finanzFac.finanzamtFindByPrimaryKey(partnerIId, mandantCNr,
                                                 LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public FinanzamtDto finanzamtFindByPartnerIIdMandantCNr(Integer partnerIId, String mandantCNr) throws ExceptionLP {
    try {
      return finanzFac.finanzamtFindByPartnerIIdMandantCNr(partnerIId, mandantCNr, LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

  public FinanzamtDto[] finanzamtFindAll(TheClientDto theClientDto) throws ExceptionLP {
	  try {
		  return finanzFac.finanzamtFindAll(theClientDto);
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }
  
  public void removeBankverbindung(BankverbindungDto bankverbindungDto)
      throws
      ExceptionLP {
    try {
      finanzFac.removeBankverbindung(bankverbindungDto);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public BankverbindungDto updateBankverbindung(BankverbindungDto bankverbindungDto)
      throws
      ExceptionLP {
    try {
      if (bankverbindungDto.getIId() != null) {
        myLogger.debug("update Bankverbindung");
        return finanzFac.updateBankverbindung(bankverbindungDto,
                                              LPMain.getInstance().getTheClient()
                                              );
      }
      else {
        myLogger.debug("create Bankverbindung");
        return finanzFac.createBankverbindung(bankverbindungDto,
                                              LPMain.getInstance().getTheClient()
                                              );
      }
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public BankverbindungDto bankverbindungFindByPrimaryKey(Integer iId)
      throws
      ExceptionLP {
    try {
      return finanzFac.bankverbindungFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public BankverbindungDto bankverbindungFindByKontoIIdOhneExc(Integer kontoIId)
      throws
      ExceptionLP {
    try {
      return finanzFac.bankverbindungFindByKontoIIdOhneExc(kontoIId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public void removeKassenbuch(KassenbuchDto kassenbuchDto)
      throws
      ExceptionLP {
    try {
      finanzFac.removeKassenbuch(kassenbuchDto,
                                 LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public KassenbuchDto updateKassenbuch(KassenbuchDto kassenbuchDto)
      throws
      ExceptionLP {
    try {
      if (kassenbuchDto.getIId() != null) {
        myLogger.debug("update Kassenbuch");
        return finanzFac.updateKassenbuch(kassenbuchDto,
                                          LPMain.getInstance().getTheClient()
                                          );
      }
      else {
        myLogger.debug("create Kassenbuch");
        return finanzFac.createKassenbuch(kassenbuchDto,
                                          LPMain.getInstance().getTheClient()
                                          );
      }
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public KassenbuchDto kassenbuchFindByPrimaryKey(Integer iId)
      throws
      ExceptionLP {
    try {
      return finanzFac.kassenbuchFindByPrimaryKey(iId,
                                                  LPMain.getInstance().
                                                  getTheClient()
                                                  );
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public RechenregelDto rechenregelFindByPrimaryKey(String cNr)
      throws
      ExceptionLP {
    try {
      return finanzFac.rechenregelFindByPrimaryKey(cNr);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  /**
   * Alle Rechenregeln in der UI-Sprache holen.
   *
   * @throws Exception
   * @return Map
   */
  public Map<?, ?> getAllRechenregel() {
    /**
     * @todo vom server holen PJ 4678
     */
    return new TreeMap();
  }


  public void removeErgebnisgruppe(ErgebnisgruppeDto ergebnisgruppeDto)
      throws
      ExceptionLP {
    try {
      finanzFac.removeErgebnisgruppe(ergebnisgruppeDto,
                                     LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public ErgebnisgruppeDto updateErgebnisgruppe(ErgebnisgruppeDto ergebnisgruppeDto)
      throws
      ExceptionLP {
    try {
      if (ergebnisgruppeDto.getIId() == null) {
        return finanzFac.createErgebnisgruppe(ergebnisgruppeDto,
                                              LPMain.getInstance().getTheClient());
      }
      else {
        return finanzFac.updateErgebnisgruppe(ergebnisgruppeDto,
                                              LPMain.getInstance().getTheClient());
      }
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public ErgebnisgruppeDto ergebnisgruppeFindByPrimaryKey(Integer iId)
      throws
      ExceptionLP {
    try {
      return finanzFac.ergebnisgruppeFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public Integer getAnzahlStellenVonKontoNummer(String kontotypCNr)
      throws
      ExceptionLP {
    try {
      return finanzFac.getAnzahlStellenVonKontoNummer(kontotypCNr,
          LPMain.getInstance().getTheClient().getMandant());
    }
    catch (Throwable t) {
      handleThrowable(t);
      return null;
    }
  }

  public Integer getAnzahlDerFinanzaemter()
      throws
      ExceptionLP {
    try {
      return finanzFac.getAnzahlDerFinanzaemter(LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
      return null;
    }
  }


  public KontolaenderartDto createKontolaenderart(KontolaenderartDto kontolaenderartDto)
      throws Exception {
    try {
      return finanzFac.createKontolaenderart(kontolaenderartDto,
                                             LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
      return null;
    }
  }


  public void removeKontolaenderart(KontolaenderartDto kontolaenderartDto)
      throws Exception {
    try {
      finanzFac.removeKontolaenderart(kontolaenderartDto, LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }

  public void removeKontoland(KontolandDto kontolandDto)
    throws Exception {
  try {
    finanzFac.removeKontoland(kontolandDto);
  }
  catch (Throwable t) {
    handleThrowable(t);
  }
}



  public KontolaenderartDto updateKontolaenderart(KontolaenderartDto kontolaenderartDto)
      throws Exception {
    try {
      return finanzFac.updateKontolaenderart(kontolaenderartDto,
                                             LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
      return null;
    }
  }

  public void updateKontoland(KontolandDto kontolandDto)
    throws Exception {
  try {
      finanzFac.updateKontoland(kontolandDto,LPMain.getInstance().getTheClient());
  }
  catch (Throwable t) {
    handleThrowable(t);
  }
}



  public KontolaenderartDto kontolaenderartFindByPrimaryKey(Integer kontoIId,
      String laenderartCNr, Integer finanzamtIId, String mandantCNr)
      throws Exception {
    try {
      return finanzFac.kontolaenderartFindByPrimaryKey(kontoIId, laenderartCNr, finanzamtIId, mandantCNr);
    }
    catch (Throwable t) {
      handleThrowable(t);
      return null;
    }
  }

  public KontolandDto kontolandFindByPrimaryKey(Integer kontoIId, Integer LandIId)
    throws Exception {
  try {
    return finanzFac.kontolandFindByPrimaryKey(kontoIId,LandIId);
  }
  catch (Throwable t) {
    handleThrowable(t);
    return null;
  }
}



  public void updateKontoDtoUIDaten(Integer kontoIIdI, String cLetztesortierungI,
                                    Integer iLetzteselektiertebuchungI)
      throws ExceptionLP {
    try {
      finanzFac.updateKontoDtoUIDaten(kontoIIdI, cLetztesortierungI,
                                      iLetzteselektiertebuchungI);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public void vertauscheErgebnisgruppen(Integer iIdEG1I, Integer iIdEG2I)
      throws ExceptionLP {
    try {
      finanzFac.vertauscheErgebnisgruppen(iIdEG1I, iIdEG2I, LPMain.getInstance().getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }


  public FinanzamtDto[] finanzamtFindAllByMandantCNr(TheClientDto theClientDto) throws ExceptionLP {
	  try {
		  return finanzFac.finanzamtFindAllByMandantCNr(theClientDto);
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }
  
	/**
	 * Handelt es sich beim angegebenen Konto um eines welches einen Vorperiodensaldo unterstuetzt
	 * 
	 * @param kontoIId
	 * @param theClientDto
	 * @return true wenn das Konto einen Vorperiodensaldo kennt. Also beispielsweise Bankkonto oder
	 * Kassenbuch
	 */
	public boolean isKontoMitSaldo(Integer kontoIId) throws ExceptionLP {
		try {
			return finanzFac.isKontoMitSaldo(kontoIId, LPMain.getTheClient()) ;
		} catch(Throwable ex) {
			handleThrowable(ex) ;
			return false ;
		}
	}

	public KassenbuchDto kassenbuchFindByKontoIIdOhneExc(Integer kontoIId) throws ExceptionLP {
		try {
			return finanzFac.kassenbuchFindByKontoIIdOhneExc(kontoIId);
		}
		catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public KontoImporterResult importCsv(List<String[]> allLines, boolean checkOnly) throws ExceptionLP {
		try {
			return finanzFac.importCsv(allLines, checkOnly, LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return null ;
		}
	}
}
