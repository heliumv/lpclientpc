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
package com.lp.client.personal;


import java.sql.Timestamp;

import com.lp.server.personal.service.ZeitmodellDto;


/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: heidi $</p>
 *
 * @version not attributable Date $Date: 2008/08/07 13:04:03 $
 */
public class SchichtzeitEinstellungen
{
  private int kwGueltigBis;
  public SchichtzeitEinstellungen() {
  }


  private int jahrGueltigBis;

  private int jahrGueltigAb;

  private Timestamp gueltigBisDatum;

  private Timestamp gueltigAbDatum;

  private int kwGueltigAb;

  private String tagesartSchichtwechsel;

  private ZeitmodellDto schicht4ZeilmodellDto;

  private ZeitmodellDto schicht3ZeilmodellDto;
  private ZeitmodellDto schicht2ZeilmodellDto;

  public void setSchicht1ZeilmodellDto(ZeitmodellDto schicht1ZeilmodellDto) {
    this.schicht1ZeilmodellDto = schicht1ZeilmodellDto;
  }


  public void setSchicht3ZeilmodellDto(ZeitmodellDto schicht3ZeilmodellDto) {
    this.schicht3ZeilmodellDto = schicht3ZeilmodellDto;
  }


  public void setSchicht4ZeilmodellDto(ZeitmodellDto schicht4ZeilmodellDto) {
    this.schicht4ZeilmodellDto = schicht4ZeilmodellDto;
  }


  public void setSchicht2ZeilmodellDto(ZeitmodellDto schicht2ZeilmodellDto) {
    this.schicht2ZeilmodellDto = schicht2ZeilmodellDto;
  }


  public void setTagesartSchichtwechsel(String tagesartSchichtwechsel) {
    this.tagesartSchichtwechsel = tagesartSchichtwechsel;
  }


  public void setKwGueltigAb(int kwGueltigAb) {
    this.kwGueltigAb = kwGueltigAb;
  }


  public void setKwGueltigBis(int kwGueltigBis) {
    this.kwGueltigBis = kwGueltigBis;
  }


  public void setGueltigAbDatum(Timestamp gueltigAbDatum) {
    this.gueltigAbDatum = gueltigAbDatum;
  }


  public void setGueltigBisDatum(Timestamp gueltigBisDatum) {
    this.gueltigBisDatum = gueltigBisDatum;
  }


  public void setJahrGueltigAb(int jahrGueltigAb) {
    this.jahrGueltigAb = jahrGueltigAb;
  }


  public void setJahrGueltigBis(int jahrGueltigBis) {
    this.jahrGueltigBis = jahrGueltigBis;
  }


  public ZeitmodellDto getSchicht1ZeilmodellDto() {
    return schicht1ZeilmodellDto;
  }


  public ZeitmodellDto getSchicht3ZeilmodellDto() {
    return schicht3ZeilmodellDto;
  }


  public ZeitmodellDto getSchicht4ZeilmodellDto() {
    return schicht4ZeilmodellDto;
  }


  public ZeitmodellDto getSchicht2ZeilmodellDto() {
    return schicht2ZeilmodellDto;
  }


  public String getTagesartSchichtwechsel() {
    return tagesartSchichtwechsel;
  }


  public int getKwGueltigAb() {
    return kwGueltigAb;
  }


  public int getKwGueltigBis() {
    return kwGueltigBis;
  }


  public Timestamp getGueltigAbDatum() {
    return gueltigAbDatum;
  }


  public Timestamp getGueltigBisDatum() {
    return gueltigBisDatum;
  }


  public int getJahrGueltigAb() {
    return jahrGueltigAb;
  }


  public int getJahrGueltigBis() {
    return jahrGueltigBis;
  }


  private ZeitmodellDto schicht1ZeilmodellDto;
}
