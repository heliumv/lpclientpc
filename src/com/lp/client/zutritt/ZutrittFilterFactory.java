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
package com.lp.client.zutritt;


import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;

@SuppressWarnings("static-access")
public class ZutrittFilterFactory
{
  private static ZutrittFilterFactory filterFactory = null;

  private ZutrittFilterFactory() {
    // Singleton.
  }


  static public ZutrittFilterFactory getInstance() {
    if (filterFactory == null) {
      filterFactory = new ZutrittFilterFactory();
    }

    return filterFactory;
  }


  /**
   * Filterkriterium fuer Filter "mandant_c_nr".
   *
   * @return FilterKriterium[] die Filter Kriterien
   * @throws Throwable
   */
  public FilterKriterium[] createFKZutrittsklasse(boolean bHauptmandat)
      throws Throwable {
    FilterKriterium[] kriterien = null;

    if(bHauptmandat){
      return null;} else{

     kriterien = new FilterKriterium[1];

      FilterKriterium krit1 = new FilterKriterium(
          "mandant_c_nr",
          true,
          "'" + LPMain.getInstance().getTheClient().getMandant() + "'",
          FilterKriterium.OPERATOR_EQUAL, false);

      kriterien[0] = krit1;

    }
    return kriterien;
  }


  /**
   * Filterkriterium fuer Filter "mandant_c_nr_objekt".
   *
   * @return FilterKriterium[] die Filter Kriterien
   * @throws Throwable
   */
  public FilterKriterium[] createFKZutrittsprotokoll(boolean bHauptmandat)
      throws Throwable {
    FilterKriterium[] kriterien = null;

    if(bHauptmandat){
      return null;
    } else{

     kriterien = new FilterKriterium[1];

      FilterKriterium krit1 = new FilterKriterium(
          ZutrittscontrollerFac.FLR_ZUTRITTSLOG_MANDANT_C_NR_OBJEKT,
          true,
          "'" + LPMain.getInstance().getTheClient().getMandant() + "'",
          FilterKriterium.OPERATOR_EQUAL, false);

      kriterien[0] = krit1;

    }
    return kriterien;
  }


  public FilterKriterium[] createFKDaueroffen()
      throws Throwable {
    FilterKriterium[] kriterien = null;


     kriterien = new FilterKriterium[1];

      FilterKriterium krit1 = new FilterKriterium(
          ZutrittscontrollerFac.FLR_ZUTRITTDAUEROFFEN_FLRZUTRITTSOBJEKT+ ".mandant_c_nr",
          true,
          "'" + LPMain.getInstance().getTheClient().getMandant() + "'",
          FilterKriterium.OPERATOR_EQUAL, false);

      kriterien[0] = krit1;


    return kriterien;
  }


  public FilterKriterium[] createFKZutrittmodelltag(Integer zutrittmodell_i_id) {
    FilterKriterium[] kriterien = new FilterKriterium[1];
    FilterKriterium krit1 = new FilterKriterium(ZutrittscontrollerFac.
                                                FLR_ZUTRITTSMODELLTAG_FLRZUTRITTSMODELL + ".i_id",
                                                true,
                                                zutrittmodell_i_id + "",
                                                FilterKriterium.OPERATOR_EQUAL, false);
    kriterien[0] = krit1;
    return kriterien;
  }


  public FilterKriterium[] createFKZutrittsobjektverwendung(Integer zutrittsobjektIId) {
    FilterKriterium[] kriterien = new FilterKriterium[1];
    kriterien[0] = new FilterKriterium(ZutrittscontrollerFac.
                                       FLR_ZUTRITTSOBJEKTVERWENDUNG_FLRZUTRITTSOBJEKT+".i_id",
                                       true,
                                       zutrittsobjektIId + "",
                                       FilterKriterium.OPERATOR_EQUAL, false);

    return kriterien;
  }

  public FilterKriterium[] createFKZutrittsklasseobjekt(Integer zutrittsklasse_i_id) {
    FilterKriterium[] kriterien = new FilterKriterium[1];
    FilterKriterium krit1 = new FilterKriterium(ZutrittscontrollerFac.
                                                FLR_ZUTRITTSKLASSEOBJEKT_ZUTRITTSKLASSE_I_ID,
                                                true,
                                                zutrittsklasse_i_id + "",
                                                FilterKriterium.OPERATOR_EQUAL, false);
    kriterien[0] = krit1;
    return kriterien;
  }


  public FilterKriterium[] createFKZutrittsmodelltagdetail(Integer zutrittmodelltag_i_id) {
    FilterKriterium[] kriterien = new FilterKriterium[1];
    FilterKriterium krit1 = new FilterKriterium(ZutrittscontrollerFac.
                                                FLR_ZUTRITTSMODELLTAGDETAIL_FLRZUTRITTSMODELLTAG +
                                                ".i_id",
                                                true,
                                                zutrittmodelltag_i_id + "",
                                                FilterKriterium.OPERATOR_EQUAL, false);
    kriterien[0] = krit1;
    return kriterien;
  }

  /**
   * Filterkriterium fuer Filter "mandant_c_nr".
   *
   * @return FilterKriterium[] die Filter Kriterien
   * @throws Throwable
   */
  public FilterKriterium[] createFKMandantCNr()
      throws Throwable {
    FilterKriterium[] kriterien = new FilterKriterium[1];

    FilterKriterium krit1 = new FilterKriterium(
        "mandant_c_nr",
        true,
        "'" + LPMain.getInstance().getTheClient().getMandant() + "'",
        FilterKriterium.OPERATOR_EQUAL, false);

    kriterien[0] = krit1;

    return kriterien;
  }

  public FilterKriteriumDirekt createFKDZutrittslogObjekt()
      throws Throwable {
    return new FilterKriteriumDirekt(
        ZutrittscontrollerFac.FLR_ZUTRITTSLOG_C_ZUTRITTSOBJEKT,
        "",
        FilterKriterium.OPERATOR_LIKE,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsobjekt"),
        FilterKriteriumDirekt.PROZENT_TRAILING,
        true, true, Facade.MAX_UNBESCHRAENKT);

  }

  public FilterKriteriumDirekt createFKDZutrittslogPersonal()
      throws Throwable {
    return new FilterKriteriumDirekt(
        ZutrittscontrollerFac.FLR_ZUTRITTSLOG_C_PERSON,
        "",
        FilterKriterium.OPERATOR_LIKE,
        LPMain.getInstance().getTextRespectUISPr("label.personal"),
        FilterKriteriumDirekt.PROZENT_BOTH,
        true, true, Facade.MAX_UNBESCHRAENKT);

  }

  public FilterKriteriumDirekt createFKDZutrittsklasseobjektObjekt()
      throws Throwable {
    return new FilterKriteriumDirekt(
        ZutrittscontrollerFac.FLR_ZUTRITTSKLASSEOBJEKT_FLRZUTRITTSOBJEKT+".c_nr",
        "",
        FilterKriterium.OPERATOR_LIKE,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsobjekt"),
        FilterKriteriumDirekt.PROZENT_LEADING,
        true, true, Facade.MAX_UNBESCHRAENKT);

  }


  public FilterKriteriumDirekt createFKDZutrittsklasseobjektModell()
      throws Throwable {
    return new FilterKriteriumDirekt(
        ZutrittscontrollerFac.FLR_ZUTRITTSKLASSEOBJEKT_FLRZUTRITTSMODELL+".c_nr",
        "",
        FilterKriterium.OPERATOR_LIKE,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsmodell"),
        FilterKriteriumDirekt.PROZENT_LEADING,
        true, true, Facade.MAX_UNBESCHRAENKT);

  }


  public QueryType[] createQTZutrittsmodell() {
    QueryType[] types = new QueryType[1];

    FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
                                             FilterKriterium.OPERATOR_LIKE, true);

    types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr("lp.zeitmodell"),
                             f1,
                             new String[] {
                             FilterKriterium.OPERATOR_EQUAL}
                             ,
                             true, true);

    return types;
  }

}
