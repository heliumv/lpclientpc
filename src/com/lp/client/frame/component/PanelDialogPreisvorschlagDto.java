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
package com.lp.client.frame.component;


import java.math.BigDecimal;
import java.sql.Date;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.partner.service.KundeDto;


/**
 * <p>In diesem Dto werden alle Parameter fuer PanelDialogPreisvorschlag
 * transportiert.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 15.09.2005</p>
 * <p> </p>
 * @author uli walch
 * @version 1.0
 */
public class PanelDialogPreisvorschlagDto
{
  /** Der Artikel, fuer den der Preisvorschlag erstellt wird. */
  private ArtikelDto artikelDto = null;

  /** Die Menge des Artikels, fuer den der Preisvorschlag erstellt wird. */
  private BigDecimal nMenge = null;

  private BigDecimal nMaterialzuschlag = null;
  
  public BigDecimal getNMaterialzuschlag() {
	return nMaterialzuschlag;
}


public void setNMaterialzuschlag(BigDecimal nMaterialzuschlag) {
	this.nMaterialzuschlag = nMaterialzuschlag;
}


/** Das Lager, auf dem der gewuenschte Artikel liegt. */
  private Integer iIdLager = null;

  /** Das Datum, zu dem der Preisvorschlag erstellt werden muss. */
  private Date datGueltigkeitsdatumFuerPreise = null;

  /** Der Kunde, fuer den der Preisvorschlag erstellt wird. */
  private KundeDto kundeDto = null;

  /** Die zuletzt gewaehlte Kundensonderkondition. */
  private Integer iIdKundesokomengenstaffelZuletztGewaehlt = null;

  /** Die zuletzt gewaehlte VK-Mengenstaffel. */
  private Integer iIdVkmengenstaffelZuletztGewaehlt = null;

  /** Die zuletzt gewaehlte Preisliste. */
  private Integer iIdPreislisteZuletztGewaehlt = null;

  /** Die Waehrung des Belegs fuer dessen Position der Preisvorschlag erstellt wird. */
  private String cNrWaehrung = null;

  /**
   * Der hinterlegte Wechselkurs von Mandantenwaehrung zu Belegwaehrung des Belegs
   * fuer dessen Position der Preisvorschlag erstellt wird.
   */
  private Double ddWechselkurs = null;

  /** Der Verkaufspreis, der momentan fuer die Position erfasst wurde. */
  private VerkaufspreisDto aktuellerVerkaufspreisDto = null;
  /** Ergebnis der VKPF */
  private VkpreisfindungDto vkpreisfindungDto = null;
  /** Handelt es sich um eine blosse Ansicht. */
  private boolean bEditable = false;

  /** Der Fixpreis des Artikels, fuer den der Preisvorschlag erstellt wird. */
  private BigDecimal nFixPreis = null;

  private boolean bNettopreisUebersteuert = false;
  
  public PanelDialogPreisvorschlagDto(
      ArtikelDto artikelDtoI,
      BigDecimal nMengeI,
      Integer iIdLagerI,
      Date datGueltigkeitsdatumFuerPreiseI,
      KundeDto kundeDtoI,
      Integer iIdPreislisteZuletztGewaehltI,
      Integer iIdKundesokomengenstaffelZuletztGewaehltI,
      Integer iIdVkmengenstaffelZuletztGewaehltI,
      String cNrWaehrungI,
      Double ddWechselkursI,
      VerkaufspreisDto aktuellerVerkaufspreisDtoI,
      VkpreisfindungDto vkpreisfindungDtoI,
      BigDecimal nFixPreisI,BigDecimal nMaterialzuschlag,
      boolean bEditableI,
      boolean bNettopreisUebersteuertI) {
    this.artikelDto = artikelDtoI;
    this.nMenge = nMengeI;
    this.iIdLager = iIdLagerI;
    this.datGueltigkeitsdatumFuerPreise = datGueltigkeitsdatumFuerPreiseI;
    this.kundeDto = kundeDtoI;
    this.iIdPreislisteZuletztGewaehlt = iIdPreislisteZuletztGewaehltI;
    this.iIdKundesokomengenstaffelZuletztGewaehlt = iIdKundesokomengenstaffelZuletztGewaehltI;
    this.iIdVkmengenstaffelZuletztGewaehlt = iIdVkmengenstaffelZuletztGewaehltI;
    this.cNrWaehrung = cNrWaehrungI;
    this.ddWechselkurs = ddWechselkursI;
    this.aktuellerVerkaufspreisDto = aktuellerVerkaufspreisDtoI;
    this.vkpreisfindungDto = vkpreisfindungDtoI;
    this.bEditable = bEditableI;
    this.nFixPreis = nFixPreisI;
    this.bNettopreisUebersteuert = bNettopreisUebersteuertI;
    this.nMaterialzuschlag=nMaterialzuschlag;
  }


  public ArtikelDto getArtikelDto() {
    return this.artikelDto;
  }


  public void setArtikelDto(ArtikelDto artikelDtoI) {
    this.artikelDto = artikelDtoI;
  }


  public BigDecimal getNMenge() {
    return nMenge;
  }


  public void setNMenge(BigDecimal nMengeI) {
    nMenge = nMengeI;
  }

  public BigDecimal getNFixPreis() {
    return nFixPreis;
  }


  public void setNFixPreis(BigDecimal nFixPreis) {
    this.nFixPreis = nFixPreis;
  }


  public Integer getLagerIId() {
    return this.iIdLager;
  }


  public void setLagerIId(Integer iIdLagerI) {
    this.iIdLager = iIdLagerI;
  }


  public Date getDatGueltigkeitsdatumFuerPreise() {
    return this.datGueltigkeitsdatumFuerPreise;
  }


  public void setDatGueltigkeitsdatumFuerPreise(Date datGueltigkeitsdatumFuerPreiseI) {
    this.datGueltigkeitsdatumFuerPreise = datGueltigkeitsdatumFuerPreiseI;
  }


  public KundeDto getKundeDto() {
    return this.kundeDto;
  }


  public void setKundeDto(KundeDto kundeDtoI) {
    this.kundeDto = kundeDtoI;
  }


  public Integer getIIdPreislisteZuletztGewaehlt() {
    return this.iIdPreislisteZuletztGewaehlt;
  }


  public void setIIdPreislisteZuletztGewaehlt(Integer iIdPreislisteZuletztGewaehltI) {
    this.iIdPreislisteZuletztGewaehlt = iIdPreislisteZuletztGewaehltI;
  }


  public Integer getIIdKundesokomengenstaffelZuletztGewaehlt() {
    return this.iIdKundesokomengenstaffelZuletztGewaehlt;
  }


  public void setIIdKundesokomengenstaffelZuletztGewaehlt(Integer iIdKundesokomengenstaffelZuletztGewaehltI) {
    this.iIdKundesokomengenstaffelZuletztGewaehlt = iIdKundesokomengenstaffelZuletztGewaehltI;
  }


  public Integer getIIdVkmengenstaffelZuletztGewaehlt() {
    return this.iIdVkmengenstaffelZuletztGewaehlt;
  }


  public void setIIdVkmengenstaffelZuletztGewaehlt(Integer iIdVkmengenstaffelZuletztGewaehltI) {
    this.iIdVkmengenstaffelZuletztGewaehlt = iIdVkmengenstaffelZuletztGewaehltI;
  }


  public String getCNrWaehrung() {
    return this.cNrWaehrung;
  }


  public void setCNrWaehrung(String cNrWaehrungI) {
    this.cNrWaehrung = cNrWaehrungI;
  }


  public Double getDdWechselkurs() {
    return this.ddWechselkurs;
  }


  public void setDdWechselkurs(Double ddWechselkursI) {
    this.ddWechselkurs = ddWechselkursI;
  }


  public VerkaufspreisDto getAktuellerVerkaufspreisDto() {
    return this.aktuellerVerkaufspreisDto;
  }


  public void setAktuellerVerkaufspreisDto(VerkaufspreisDto aktuellerVerkaufspreisDtoI) {
    aktuellerVerkaufspreisDto = aktuellerVerkaufspreisDtoI;
  }

  public VkpreisfindungDto getVkpreisfindungDto() {
    return vkpreisfindungDto;
  }

  public boolean getBEditable() {
    return bEditable;
  }

  public void setBEditable(boolean bEditableI) {
    bEditable = bEditableI;
  }


  public void setBNettopreisUebersteuert(boolean bNettopreisUebersteuert) {
	  this.bNettopreisUebersteuert = bNettopreisUebersteuert;
  }


  public boolean isBNettopreisUebersteuert() {
	  return bNettopreisUebersteuert;
  }
}
