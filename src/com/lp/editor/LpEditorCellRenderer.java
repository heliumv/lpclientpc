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
package com.lp.editor;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.lp.editor.util.TextBlockAttributes;


/**
 *
 * <p><I>Der CellRenderer fuer Tabellen im LpEditor</I>.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.4 $
 */
public class LpEditorCellRenderer
    extends JTextPane
    implements TableCellRenderer {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Color unselectedForeground;
  private Color unselectedBackground;

  private AttributeSet attrDefault = null;

  public LpEditorCellRenderer() {
    setEditable(false);
    setContentType("text/jasper");
  }

  public LpEditorCellRenderer(AttributeSet attributes) {
    this();
    setDefaultAttributes(attributes);
  }

  public void setForeground(Color c) {
    super.setForeground(c);
    unselectedForeground = c;
  }

  public void setBackground(Color c) {
    super.setBackground(c);
    unselectedBackground = c;
  }

  public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean bSelected,
                                                 boolean bFocus,
                                                 int row,
                                                 int column) {
    if (bSelected) {
      super.setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    }
    else {
      super.setForeground( (unselectedForeground != null) ?
                          unselectedForeground
                          : table.getForeground());
      super.setBackground( (unselectedBackground != null) ?
                          unselectedBackground
                          : table.getBackground());
    }

    if (value == null) {
      setText("");
      setOverflow(false);
    }
    else {
      setText(value.toString());
      if (table != null) {
        Vector<?> vecColAttr = (Vector<?>) table.getClientProperty(LpEditor.
            COLUMN_TEXT_ATTRIBUTES);
        if (vecColAttr != null) {
          long capacity = ( (TextBlockAttributes) vecColAttr.get(column)).
              capacity;
          if (capacity > 0 && value.toString().length() > capacity) {
            setOverflow(true);
          }
          else {
            setOverflow(false);
          }
        }
      }
    }
    setDefaultAttributes(attrDefault);
    return this;
  }

  /**
   * Setzt die default Attribute fuer den CellRenderer. Sollten
   * die gleichen sein wie fuer den entsprechenden CellEditor.
   *
   * @see LpEditorCellEditor#setDefaultAttributes(AttributeSet)
   * @param attributes AttributeSet
   */
  public void setDefaultAttributes(AttributeSet attributes) {
    attrDefault = attributes;
    Style style = getStyledDocument().getStyle(StyleContext.DEFAULT_STYLE);
    style.removeAttributes(style);
    style.addAttribute(StyleConstants.NameAttribute, StyleContext.DEFAULT_STYLE);
    style.addAttributes(attributes);
    setLogicalStyle(style);
  }

  public void setOverflow(boolean flag) {
    if (flag) {
      setBorder(BorderFactory.createLineBorder(Color.RED, 1));
    }
    else {
      setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }
  }
}
