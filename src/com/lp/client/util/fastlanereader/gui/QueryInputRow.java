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
package com.lp.client.util.fastlanereader.gui;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;


/**
 * This class is the visible representation of a FilterKriterium. It is build
 * and configured from a {@link QueryType}.
 *
 * @author werner
 */
public class QueryInputRow
    extends JPanel
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
   * the width of the kriteria name label.
   */
  private static final int WIDTH_LABEL_ROW_NAME = 120;

  /**
   * the width of the combobox that holds the operator values.
   */
  private static final int WIDTH_COMBO_BOX_L_OPERATOR = 60;

  /**
   * the width of the text field where the user can type the criteria's value
   * in.
   */
  private static final int WIDTH_TEXT_FIELD_VALUE = 120;

  /**
   * the height of this row.
   */
  private static final int HEIGHT = 25;

  /**
   * the label that is displayed on the left hand side.
   */
  private WrapperLabel labelRowName;

  /**
   * the combobox that holds the logical operators.
   */
  private WrapperComboBox comboBoxLogicalOperator;

  /**
   * the text field where the user can type the criteria's value in.
   */
  private WrapperTextField textFieldValue;

  /**
   * the remove button. Pressing this button removes this row from the view.
   */
  private JButton buttonRemoveFilterzeile;

  /**
   * a copy of the FilterKriterium that was passed in QueryType. This
   * FilterKriterium will be modified by user input and returned for the query
   * building process.
   */
  private FilterKriterium kriterium;

  /**
   * the QueryInputRowListeners.
   */
  private Vector<QueryInputRowListener> listeners;

  /**
   * determines wether the value from the user input should be wrapped in
   * single quotes (example: 'value' instead of value) before setting it in
   * FilterKriterium. If true wrapping is enabled.
   */
  private boolean wrapValueWithSingleQuotes = false;

  /**
   * determines wether the value from the user input should be wrapped in
   * "%" (example: %value% instead of value) before setting it in
   * FilterKriterium. If true wrapping is enabled.
   */
  private boolean wrapValueWithProzent = false;

  /**
   * determines wheter the equal operator should be replaced by the like
   * operator when set in the FilterKriterium. This is useful if "=" should be
   * displayed in the operator combobox, but like is required in the query, as
   * it is for Strings and Dates.
   */
  private boolean replaceEqualByLike = false;
  private PanelBasis lPPanel;

  /**
   * creates a new QueryInputRow instance using {@link QueryType}as template.
   *
   * @param type the template for the creation process.
   * @param lPPanelI PanelBasis
   */
  public QueryInputRow(QueryType type, PanelBasis lPPanelI) {

    lPPanel = lPPanelI;

    this.listeners = new Vector<QueryInputRowListener>();

    FilterKriterium tmpKriterium = type.getKriterium();
    this.kriterium = new FilterKriterium(tmpKriterium.kritName,
                                         tmpKriterium.isKrit,
                                         tmpKriterium.value,
                                         tmpKriterium.operator,
                                         tmpKriterium.isBIgnoreCase());

    this.labelRowName = new WrapperLabel(type.getName());
//    this.labelRowName.setPreferredSize(new Dimension(
//        QueryInputRow.WIDTH_LABEL_ROW_NAME, QueryInputRow.HEIGHT));

    String[] allowedOperators = type.getAllowedOperators();
    this.comboBoxLogicalOperator = new WrapperComboBox(allowedOperators);
//    this.comboBoxLogicalOperator
//        .setPreferredSize(new Dimension(
//        QueryInputRow.WIDTH_COMBO_BOX_L_OPERATOR,
//        QueryInputRow.HEIGHT));

    this.wrapValueWithProzent = type.isWrapWithProzent();
    this.wrapValueWithSingleQuotes = type.isWrapValueWithSingleQuotes();
    this.replaceEqualByLike = type.isReplaceEqualByLike();
    if (this.replaceEqualByLike &&
        FilterKriterium.OPERATOR_LIKE.equals(this.kriterium.operator)) {
      this.comboBoxLogicalOperator.setSelectedItem(FilterKriterium.
          OPERATOR_EQUAL);
    }
    else {
      this.comboBoxLogicalOperator.setSelectedItem(this.kriterium.operator);
    }

    //texteingabe
    this.textFieldValue = new WrapperTextField(this.kriterium.value,
                                               WIDTH_TEXT_FIELD_VALUE);

    //so mache ich schnell einen button.
    buttonRemoveFilterzeile = lPPanel.createButtonActionListenerThis(
        "/com/lp/client/res/minus_sign.png", "Entferne diese Filterzeile.",
        PanelQuery.ACTION_REMOVE_FILTERKRITERIUM);
    buttonRemoveFilterzeile.setPreferredSize(new Dimension(23, 23));
    buttonRemoveFilterzeile.setEnabled(true);

    // Groesse der elemente auf die gui-design-richtlinien anpassen
    Dimension d1 = labelRowName.getPreferredSize();
    Dimension d2 = comboBoxLogicalOperator.getPreferredSize();
    Dimension d3 = textFieldValue.getPreferredSize();
    Dimension d4 = buttonRemoveFilterzeile.getPreferredSize();
    // Minimumbreite fuer das Label, damit mehrere zeilen gleich ausgerichtet sind
    labelRowName.setMinimumSize(new Dimension(150, 23));
    labelRowName.setPreferredSize(new Dimension(150, 23));
    comboBoxLogicalOperator.setMinimumSize(new Dimension(50, 23));
    comboBoxLogicalOperator.setPreferredSize(new Dimension(50, 23));
    //textFieldValue.setMinimumSize(new Dimension((int)d3.getSize().getWidth(), 23));
    //textFieldValue.setPreferredSize(new Dimension((int)d3.getSize().getWidth(), 23));
    textFieldValue.setMinimumSize(new Dimension(WIDTH_TEXT_FIELD_VALUE, 23));
    textFieldValue.setPreferredSize(new Dimension(WIDTH_TEXT_FIELD_VALUE, 23));
    buttonRemoveFilterzeile.setMinimumSize(new Dimension( (int) d4.getSize().getWidth(),
        23));
    buttonRemoveFilterzeile.setPreferredSize(new Dimension( (int) d4.getSize().getWidth(),
        23));
    this.add(this.labelRowName);
    this.add(this.comboBoxLogicalOperator);
    this.add(this.textFieldValue);
    this.add(this.buttonRemoveFilterzeile);
    //alle ctrls. jetzt updaten
    this.validate();
  }


  /**
   * fires removeButtonPressed when buttonRemove has been pressed.
   *
   * @param e ActionEvent
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(this.buttonRemoveFilterzeile)) {
      this.fireRemoveButtonPressed(new QueryInputRowEvent(this));
    }
  }


  /**
   * adds a listener for notification of a QueryInputRowEvent.
   *
   * @param listener
   *            the listener to register.
   */
  public void addQueryInputRowListener(QueryInputRowListener listener) {
    this.listeners.add(listener);
  }


  /**
   * removes the specified QueryInputRowListener from the notification list.
   *
   * @param listener
   *            the listener to remove.
   */
  public void removeQueryInputRowListener(QueryInputRowListener listener) {
    this.listeners.remove(listener);
  }


  public void removeFilterzeile(QueryInputRowEvent e) {
//    QueryInputRow rowToRemove = (QueryInputRow) e.getSource();
//    rowToRemove.removeQueryInputRowListener(this);
//    this.panelFilterCenter.remove(rowToRemove);
//    this.queryInputRows.remove(rowToRemove);
//    SwingUtilities.getRootPane(this).revalidate();
//    SwingUtilities.getRootPane(this).repaint();
  }


  /**
   * calls removeButtonPressed(e) on all registered listeners.
   *
   * @param e
   *            the event to pass to the listeners.
   */
  protected void fireRemoveButtonPressed(QueryInputRowEvent e) {
    for (int i = 0; i < this.listeners.size(); i++) {
      QueryInputRowListener listener = (QueryInputRowListener)this.listeners
          .elementAt(i);
      listener.removeButtonPressed(e);
    }
  }


  /**
   * builds the FilterKriterium for use in a query according to the user's
   * input. If isWrapValueWithSingleQuotes() is true, the entered text will be
   * wrapped in single quotes before setting it in the return value. If the
   * selected operator is the equal operator and replaceEqualByLike is true,
   * then the FilterKriterium will contain the like operator instead.
   *
   * @return the FilterKriterium built according to the user's input.
   */
  public FilterKriterium getKriterium() {
    String textValue = textFieldValue.getText();
    if (textValue != null && textValue.length() > 0) {
      if (this.wrapValueWithProzent) {
        textValue = "%" + textValue + "%";
      }
      if (this.wrapValueWithSingleQuotes) {
        textValue = "'" + textValue + "'";
      }
      this.kriterium.value = textValue;
      this.kriterium.isKrit = true;
      String operator = (String)this.comboBoxLogicalOperator
          .getSelectedItem();
      if (operator.equals(FilterKriterium.OPERATOR_EQUAL)
          && this.replaceEqualByLike) {
        operator = FilterKriterium.OPERATOR_LIKE;
      }
      this.kriterium.operator = operator;
    }
    else {
      this.kriterium.isKrit = false;
      this.kriterium.value = " ";
    }
    return this.kriterium;
  }


  /**
   * @return Returns the wrapValueWithSingleQuotes.
   */
  public boolean isWrapValueWithSingleQuotes() {
    return wrapValueWithSingleQuotes;
  }


  /**
   * returns the panel
   *
   * @return JPanel
   */
  public JPanel getJPanel() {
    return lPPanel;
  }


  /**
   * @param wrapValueWithSingleQuotes
   *            The wrapValueWithSingleQuotes to set.
   */
  public void setWrapValueWithSingleQuotes(boolean wrapValueWithSingleQuotes) {
    this.wrapValueWithSingleQuotes = wrapValueWithSingleQuotes;
  }


  public String getValue() {
    return textFieldValue.getText();
  }


  public void setValue(String sValue) {
    textFieldValue.setText(sValue);
  }
}
