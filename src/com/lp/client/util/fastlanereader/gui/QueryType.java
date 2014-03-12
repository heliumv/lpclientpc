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
package com.lp.client.util.fastlanereader.gui;

import com.lp.client.frame.component.PanelQuery;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

/**
 * This class defines types of filter criterias the user can choose from in
 * order to narrow the viewed data to the data he's interested in. For each
 * QueryType the user can add one or more {@link QueryInputRow}to the
 * {@link PanelQuery}. Query types are therefore necessary for the
 * instanciation of a {@link PanelQuery}.
 *
 * @author werner
 */
public class QueryType {

  /**
   * the name of the query type that will be displayed in
   * {@link QueryPanel#comboBoxTypes}.
   */
  private String name;

  /**
   * the FilterKriterium used to discribe the filter type.
   */
  private FilterKriterium kriterium;

  /**
   * the allowed logical operators a user can choose from in a QueryInputRow
   * of this type.
   */
  private String[] allowedOperators;

  /**
   * if true, the QueryInputRow will wrap it's value into single quotes when
   * it's FilterKriterium is build whithin the query process.
   */
  private boolean wrapValueWithSingleQuotes = false;

  /**
   * Wenn true, dann wird der Wert in der QueryInputRow in "%" eingeschlossen,
   * sobald das FilterKriterium zusammengebaut wird.
   */
  private boolean wrapValueWithProzent = false;

  /**
   * replaces the equal operator by the like operator in QueryInputRows of
   * this type when set to true.
   */
  private boolean replaceEqualByLike = false;

  /**
   * creates a new QueryType.
   *
   * @param name
   *            the displayed name of the query type.
   * @param kriterium
   *            the criteria used for filtering.
   * @param allowedOperators
   *            operators the user is allowed to choose from.
   * @param wrapValueWithSingleQuotes
   *            determines wheter the values input by a user should get
   *            wrapped in single quotes for the query.
   * @param replaceEqualByLike
   *            if true, the '=' will be replaced by 'like' in the query
   *            process.
   */
  public QueryType(String name,
                   FilterKriterium kriterium,
                   String[] allowedOperators,
                   boolean wrapValueWithSingleQuotes,
                   boolean replaceEqualByLike) {

    this.name = name;
    this.kriterium = kriterium;
    this.allowedOperators = allowedOperators;
    this.wrapValueWithSingleQuotes = wrapValueWithSingleQuotes;
    // @todo @uw  PJ 5388
    this.wrapValueWithProzent = true;
    this.replaceEqualByLike = replaceEqualByLike;
  }

  /**
   * creates a new QueryType.
   *
   * @param name
   *            the displayed name of the query type.
   * @param kriterium
   *            the criteria used for filtering.
   * @param allowedOperators
   *            operators the user is allowed to choose from.
   * @param wrapValueWithSingleQuotes
   *            determines wheter the values input by a user should get
   *            wrapped in single quotes for the query.
   * @param wrapValueWithProzent
   *            determines wheter the values input by a user should get
   *            wrapped in "%" signs for the query.
   *
   * @param replaceEqualByLike
   *            if true, the '=' will be replaced by 'like' in the query
   *            process.
   */
  public QueryType(String name,
                   FilterKriterium kriterium,
                   String[] allowedOperators,
                   boolean wrapValueWithSingleQuotes,
                   boolean wrapValueWithProzent,
                   boolean replaceEqualByLike) {

    this.name = name;
    this.kriterium = kriterium;
    this.allowedOperators = allowedOperators;
    this.wrapValueWithSingleQuotes = wrapValueWithSingleQuotes;
    this.wrapValueWithProzent = wrapValueWithProzent;
    this.replaceEqualByLike = replaceEqualByLike;
  }

  /**
   * @return Returns the kriterium.
   */
  public FilterKriterium getKriterium() {
    return kriterium;
  }

  /**
   * @return Returns the name.
   */
  public String getName() {
    return name;
  }

  /**
   * @return name.
   */
  public String toString() {
    return this.name;
  }

  /**
   * @return Returns the allowedOperators.
   */
  public String[] getAllowedOperators() {
    return allowedOperators;
  }

  /**
   * @return Returns the replaceEqualByLike.
   */
  public boolean isReplaceEqualByLike() {
    return replaceEqualByLike;
  }

  /**
   * @return Returns the wrapValueWithSingleQuotes.
   */
  public boolean isWrapValueWithSingleQuotes() {
    return wrapValueWithSingleQuotes;
  }

  public boolean isWrapWithProzent() {
    return wrapValueWithProzent;
  }
}
