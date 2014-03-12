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
package com.lp.client.frame;

import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;

/**
 * <code>NumberColumnFormat</code> makes available a simple NumberRenderer and
 * NumberEditor for JTable. Usage example:
 * 
 * <pre>
 * <code>
 * &NumberColumnFormat numberColumnFormat = new NumberColumnFormat();
 * &table.setDefaultRenderer(Number.class, numberColumnFormat.getRenderer());
 * &table.setDefaultEditor(Number.class, numberColumnFormat.getEditor());
 * </code>
 * </pre>
 * 
 * @author Andr? Uhres
 * @version 1.0 16.09.2007
 */

public class NumberColumnFormat {
	private DecimalFormat formatterR, formatterE;
	private DefaultTableCellRenderer renderer;
	private DecimalFormatSymbols numberSymbols;
	{
		numberSymbols = new DecimalFormatSymbols();
		numberSymbols.setGroupingSeparator('.');
		numberSymbols.setDecimalSeparator(',');
	}

	private DefaultCellEditor editor;

	/**
	 * Constructs a <code>NumberColumnFormat</code> using the pattern
	 * "###,###,###.##" and the default number format symbols for the default
	 * locale.
	 */
	public NumberColumnFormat() {
		this(null);
	}

	/**
	 * Constructs a <code>NumberColumnFormat</code> using the given pattern and
	 * the default number format symbols for the default locale.
	 * 
	 * 
	 * 
	 * @param pattern
	 *            the pattern describing the number format, "###,###,###.##" if
	 *            null
	 */
	public NumberColumnFormat(String pattern) {
		this(pattern, pattern);
	}

	/**
	 * Constructs a <code>NumberColumnFormat</code> using the given patterns and
	 * the default number format symbols for the default locale.
	 * 
	 * 
	 * 
	 * @param patternR
	 *            the pattern describing the number format of the renderer,
	 *            "###,###,###.##" if null
	 * @param patternE
	 *            the pattern describing the number format of the editor,
	 *            "#########.##" if null
	 */
	public NumberColumnFormat(String patternR, String patternE) {
		if (patternR == null)
			patternR = "###,###,###.##";
		formatterR = new DecimalFormat(patternR, numberSymbols);
		formatterR.setDecimalFormatSymbols(numberSymbols);
		int d = patternR.split("\\.")[1].length();
		formatterR.setMinimumFractionDigits(d);
		formatterR.setMaximumFractionDigits(d);
		if (patternE == null)
			patternE = "#########.##";
		formatterE = new DecimalFormat(patternE, numberSymbols);
		renderer = new NumberRenderer();
		editor = new NumberEditor();
		editor.setClickCountToStart(1);
		
	}

	/**
	 * Returns the number cell renderer.
	 * 
	 * @return the table number cell renderer
	 */
	public DefaultTableCellRenderer getRenderer() {
		return renderer;
	}

	/**
	 * Returns the number cell editor.
	 * 
	 * @return the table number cell editor
	 */
	public DefaultCellEditor getEditor() {
		return editor;
	}

	/**
	 * Returns the DecimalFormatSymbols.
	 * 
	 * @return the DecimalFormatSymbols used for the number rendering.
	 */
	public DecimalFormatSymbols getNumberSymbols() {
		return numberSymbols;
	}

	/**
	 * Sets the DecimalFormatSymbols used for the number rendering.
	 */
	public void setNumberSymbols(DecimalFormatSymbols numberSymbols) {
		this.numberSymbols = numberSymbols;
		formatterR.setDecimalFormatSymbols(numberSymbols);
		formatterE.setDecimalFormatSymbols(numberSymbols);
	}

	/**
	 * Sets the GroupingSeparator used for the number rendering.
	 */
	public void setGroupingSeparator(char sep) {
		numberSymbols.setGroupingSeparator(sep);
		setNumberSymbols(numberSymbols);
	}

	/**
	 * Sets the DecimalSeparator used for the number rendering.
	 */
	public void setDecimalSeparator(char sep) {
		numberSymbols.setDecimalSeparator(sep);
		setNumberSymbols(numberSymbols);
	}

	private class NumberRenderer extends DefaultTableCellRenderer {
		public NumberRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public void setValue(Object value) {
			setText((value == null) ? "" : formatterR.format(value));
		}
	}

	private class NumberEditor extends DefaultCellEditor {
		public NumberEditor() {
			super(new JTextField());
			((JTextField) getComponent())
					.setHorizontalAlignment(JTextField.RIGHT);
		}

		@Override
		public boolean stopCellEditing() {
			String value = ((JTextField) getComponent()).getText();
			if (!value.equals("")) {
				try {
					formatterE.parse(value);
				} catch (ParseException e) {
					((JComponent) getComponent()).setBorder(new LineBorder(
							Color.red));
					return false;
				}
			}
			return super.stopCellEditing();
		}

			@Override
		public Component getTableCellEditorComponent(final JTable table,
				final Object value, final boolean isSelected, final int row,
				final int column) {
			JTextField tf = ((JTextField) getComponent());
			tf.setBorder(new LineBorder(Color.black));
			
			try {
				tf.setText(formatterE.format(value));
			} catch (Exception e) {
				tf.setText("");
			}
			
			return tf;
		}

		@Override
		public Object getCellEditorValue() {
			try {
				return formatterE
						.parse(((JTextField) getComponent()).getText());
			} catch (ParseException ex) {
				return null;
			}
		}
	}
}
