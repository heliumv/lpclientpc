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


import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageReportFac;
import com.lp.server.anfrage.service.ReportAnfragelieferdatenuebersichtKriterienDto;
import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.util.report.JasperPrintLP;


/**
 * <p><I>Diese Klasse kuemmert sich um die Anfrage Reports.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>15.06.05</I></p>
 * @author $Author: heidi $
 * @version $Revision: 1.6 $
 */
public class AnfrageReportDelegate
    extends Delegate
{
  private Context context;
  private AnfrageReportFac anfrageReportFac;

  public AnfrageReportDelegate()
      throws Exception {
    context = new InitialContext();
    anfrageReportFac = (AnfrageReportFac) context.lookup("lpserver/AnfrageReportFacBean/remote");
  }


  /**
   * Eine Anfrage drucken.
   *
   * @param iIdAnfrageI PK der Anfrage
   * @param iAnzahlKopienI wieviele Kopien zusaetzlich zum Original?
   * @param bMitLogo Boolean
   * @return JasperPrint[] der Druck und seine Kopien
   * @throws ExceptionLP Ausnahme
   */
  public JasperPrintLP[] printAnfrage(Integer iIdAnfrageI, Integer iAnzahlKopienI, Boolean bMitLogo)
      throws ExceptionLP {
    JasperPrintLP[] aJasperPrint = null;

    try {
      aJasperPrint = anfrageReportFac.printAnfrage(
          iIdAnfrageI,
          iAnzahlKopienI,
          bMitLogo,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return aJasperPrint;
  }

  /**
   * Die Anfragestatistik fuer einen bestimmten Artikel ausdrucken.
   * @param reportAnfragestatistikKriterienDtoI die Kriterien
   * @return JasperPrint der Druck
   * @throws ExceptionLP Ausnahme
   */
  public JasperPrintLP printAnfragestatistik(ReportAnfragestatistikKriterienDto
                                           reportAnfragestatistikKriterienDtoI)
      throws ExceptionLP {
    JasperPrintLP oPrint = null;

    try {
      oPrint = anfrageReportFac.printAnfragestatistik(
          reportAnfragestatistikKriterienDtoI,
          LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return oPrint;
  }


  /**
   * Die Lieferdatenuebersicht fuer alle Anfragen nach bestimmten Kriterien drucken.
   * @param kritDtoI die Kriterien des Benutzers
   * @return JasperPrint der Druck
   * @throws ExceptionLP Ausnahme
   */
  public JasperPrintLP printLieferdatenuebersicht(
      ReportAnfragelieferdatenuebersichtKriterienDto kritDtoI)
      throws ExceptionLP {
    JasperPrintLP oPrint = null;

    try {
      oPrint = anfrageReportFac.printLieferdatenuebersicht(
          kritDtoI, LPMain.getTheClient());
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    return oPrint;
  }
}
