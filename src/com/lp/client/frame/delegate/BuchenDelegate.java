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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.KassenbuchungsteuerartDto;
import com.lp.server.finanz.service.SaldovortragModelPersonenKonto;
import com.lp.server.finanz.service.SaldovortragModelSachkonto;

@SuppressWarnings("static-access") 
/**
 * <p><I>BusinessDelegate zum Buchen</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.26 $
 */
public class BuchenDelegate
    extends Delegate
{
  private Context context;
  private BuchenFac buchenFac;

  /**
   * BuchenDelegate
   *
   * @throws ExceptionLP
   */
  public BuchenDelegate()
      throws ExceptionLP {
    try {
      context = new InitialContext();
      buchenFac = (BuchenFac) context.lookup("lpserver/BuchenFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }
  }




  /**
   * buchungFindByPrimaryKey.
   *
   * @param iId Integer
   * @throws ExceptionLP
   * @return BuchungDto
   */
  public BuchungDto buchungFindByPrimaryKey(Integer iId)
      throws
      ExceptionLP {
    try {
      return buchenFac.buchungFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  /**
   * buchungdetailFindByKontoIId.
   *
   * @param iId Integer
   * @throws ExceptionLP
   * @return BuchungdetailDto[]
   */
  public BuchungdetailDto[] buchungdetailFindByKontoIId(Integer iId)
      throws
      ExceptionLP {
    try {
      return buchenFac.buchungdetailFindByKontoIId(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }
  public Map<?, ?> getListeDerGegenkonten(Integer buchungdetailIId)
	  throws
      ExceptionLP {
    try {
      return buchenFac.getListeDerGegenkonten(buchungdetailIId, LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

  /**
   * buchungdetailFindByPrimaryKey.
   *
   * @param iId iId
   * @throws ExceptionLP
   * @return BuchungdetailDto
   */
  public BuchungdetailDto buchungdetailFindByPrimaryKey(Integer iId)
      throws ExceptionLP {
    try {
      return buchenFac.buchungdetailFindByPrimaryKey(iId);
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

  public void storniereBuchung(Integer buchungIId)
      throws
      ExceptionLP {
    try {
      buchenFac.storniereBuchung(buchungIId,  LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
  }


  public boolean hatPartnerBuchungenAufKonto(
      Integer iIdPartnerI,
      Integer iIdKontoI)
      throws ExceptionLP {

    Boolean bRet = new Boolean(false);
    try {
      bRet = buchenFac.hatPartnerBuchungenAufKonto(
          iIdPartnerI,
          iIdKontoI,
          LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return bRet.booleanValue();
  }
  
  public BigDecimal getSaldoVonKontoMitEB(Integer kontoIId, int geschaeftsjahr, int periode) throws ExceptionLP {
	  try {
		return buchenFac.getSaldoVonKontoMitEB(kontoIId, geschaeftsjahr, periode, LPMain.getTheClient());
	} catch (Throwable e) {
		handleThrowable(e);
		return null;
	}
  }


  /**
   * getSaldoVonKonto.
   *
   * @param kontoIId Integer
   * @throws ExceptionLP
   * @return BigDecimal
   */
  public BigDecimal getSaldoVonKonto(Integer kontoIId) throws ExceptionLP {
    try {
	    int geschaeftsjahr = DelegateFactory.getInstance().getParameterDelegate().getGeschaeftsjahr();
    	Calendar cal = Calendar.getInstance();
	    int periode =  cal.get(Calendar.MONTH)+1;
	    return buchenFac.getSaldoOhneEBVonKonto(kontoIId, geschaeftsjahr, periode, true, LPMain.getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }


  public BigDecimal getSaldoVonKonto(Integer kontoIId, int geschaeftsjahr,
			int periode, boolean kummuliert) throws ExceptionLP {
	  try {
		  return buchenFac.getSaldoOhneEBVonKonto(kontoIId, geschaeftsjahr, periode, true, LPMain.getTheClient()) ;
	  } catch(Throwable ex) {
		  handleThrowable(ex) ;
		  return null ;
	  }
  }

  
  public BigDecimal getSaldoVonKontoByAuszug(Integer kontoIId, Integer auszug, Integer geschaeftsjahr) throws ExceptionLP {
	  try {
		  return buchenFac.getSaldoVonKontoByAuszug(kontoIId, geschaeftsjahr, auszug, true, true, LPMain.getTheClient());
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }
  
  public BigDecimal getSummeEroeffnungKontoIId(Integer kontoIId,
			int geschaeftsjahr, int periode, boolean kummuliert) throws ExceptionLP {
	  try {
		  return buchenFac.getSummeEroeffnungKontoIId(kontoIId, geschaeftsjahr, periode, kummuliert, LPMain.getTheClient()) ;
	  } catch(Throwable ex) {
		  handleThrowable(ex) ;
		  return null ;
	  }
  }

  
  /**
   * Verbuchen einer Umbuchung.
   *
   * @param buchungDto die Kopfdaten der Buchung
   * @param buchungdetailDtos die buchungszeilen
   * @throws ExceptionLP
   */
  public BuchungDto verbucheUmbuchung(BuchungDto buchungDto,
  BuchungdetailDto[] buchungdetailDtos) throws
      ExceptionLP {
    try {
      return buchenFac.verbucheUmbuchung(buchungDto, buchungdetailDtos,
                                  LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
    }
    return null;
  }

  public String getBelegnummerUmbuchung(Integer geschaeftsjahr) throws ExceptionLP {
    try {
      return buchenFac.getBelegnummerUmbuchung(geschaeftsjahr, LPMain.getInstance().getTheClient());
    }
    catch (Throwable ex) {
      handleThrowable(ex);
      return null;
    }
  }

  public Timestamp[] getDatumVonBisGeschaeftsjahr(Integer geschaeftsjahr) throws ExceptionLP {
	  try {
		  return buchenFac.getDatumVonBisGeschaeftsjahr(geschaeftsjahr, LPMain.getTheClient());
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }


  public Integer getPeriodeImGJFuerDatum(Date date) throws ExceptionLP  {
	  try {
		  return buchenFac.getPeriodeImGJFuerDatum(date, LPMain.getTheClient().getMandant()) ;
	  } catch(Throwable ex) {
		  handleThrowable(ex) ;
		  return null ;
	  }
  }
  
  
  public BuchungDto verbucheKassenbuchung(BuchungDto buchungDto, BuchungdetailDto buchungdetailDto,
		  Integer kassenKontoIId, KassenbuchungsteuerartDto kstDto) throws ExceptionLP {
	  try {
		  return buchenFac.verbucheKassenbuchung(buchungDto, buchungdetailDto, kassenKontoIId, kstDto,
				  LPMain.getInstance().getTheClient());
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }




  public BuchungdetailDto[] buchungdetailsFindByBuchungIId(Integer buchungIId) throws ExceptionLP {
	  try {
		  return buchenFac.buchungdetailsFindByBuchungIId(buchungIId);
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }

  public KassenbuchungsteuerartDto getKassenbuchungSteuerart(Integer buchungIId) throws ExceptionLP {
	  try {
		  return buchenFac.getKassenbuchungSteuerart(buchungIId, LPMain.getTheClient());
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }




  public BigDecimal getSaldoVonKontoByAusziffern(Integer kontoIId,
		Integer auszifferkennzeichen, Integer geschaeftsjahr) throws ExceptionLP {
	  try {
		  return buchenFac.getSaldoVonKontoByAusziffern(kontoIId, geschaeftsjahr, auszifferkennzeichen, LPMain.getTheClient());
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }


  public boolean hatKontoBuchungen(Integer kontoIId) throws ExceptionLP {
	  try {
		  return buchenFac.hatKontoBuchungen(kontoIId);
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return true;
	  }
  }




  public boolean isUvaVerprobt(Integer buchungIId) throws ExceptionLP {
	  try {
		  return buchenFac.isUvaVerprobt(buchungIId);
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return true;
	  }
}

  public BuchungdetailDto[] buchungdetailsFindByBuchungIIdOhneMitlaufende(Integer buchungIId) throws ExceptionLP {
	  try {
		  return buchenFac.buchungdetailsFindByBuchungIIdOhneMitlaufende(buchungIId, LPMain.getTheClient());
	  }
	  catch (Throwable ex) {
		  handleThrowable(ex);
		  return null;
	  }
  }
  
  public void createSaldovortragsBuchung(SaldovortragModelSachkonto saldovortragModel) throws ExceptionLP {
	  try {
		  buchenFac.createSaldovortragsBuchung(saldovortragModel, LPMain.getTheClient()) ;
	  } catch(Throwable t) {
		  handleThrowable(t) ;
	  }
  }
  
  public void createSaldovortragsBuchungErmittleOP(SaldovortragModelPersonenKonto saldovortragModel) throws ExceptionLP {
	  try {
		  buchenFac.createSaldovortragsBuchungErmittleOP(saldovortragModel, LPMain.getTheClient()) ;
	  } catch(Throwable t) {
		  handleThrowable(t) ;
	  }
  }
  
  public void createSaldovortragsBuchungErmittleOP(String kontotyp, int geschaeftsjahr, boolean deleteManualEB) throws ExceptionLP {
	  try {
		  buchenFac.createSaldovortragsBuchungErmittleOP(
				  kontotyp, geschaeftsjahr, deleteManualEB, LPMain.getTheClient()) ;
	  } catch(Throwable t) {
		  handleThrowable(t) ;
	  }
  }
}
