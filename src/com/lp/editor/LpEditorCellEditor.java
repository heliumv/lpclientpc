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
package com.lp.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.lp.editor.ui.LpEditorTable;
import com.lp.editor.util.TextBlockAttributes;

/**
 *
 * <p><I>Eine Implementation von AbstractCellEditor fuer die
 * Darstellung und Bearbeitung von Tabellen mit formatierten
 * Text</I>. Die Zeilenhoehe wird waehrend des editierens einer
 * Zelle automatisch angepasst und mit TAB und den Cursor-Tasten
 * wechselt man zwischen den Zellen</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 *
 * @author Sascha Zelzer
 * @version $Revision: 1.4 $
 */

public class LpEditorCellEditor
    extends AbstractCellEditor
    implements TableCellEditor, CellEditorListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
   * Identifiziert den CellEditor, der an den eigentlichen
   * editor (JTextPane) via putClientProperty(Object) angehaengt
   * wird.
   */
  public static String EDITOR_PARENT_PROP = "EditorParent";

  public static String IS_EDIT_EVENT = "isCellEditing";

  private CellTextPane editor;
  private CellDocumentListener docListener = new CellDocumentListener();
  private Vector<CaretListener> caretListener = new Vector<CaretListener>();
  private StyledDocument doc = null;
  //private Vector vecCellHeight = null;
  private int iRow = 0;
  private int iColumn = 0;
  private int iRowCount = 0;
  private int iColumnCount = 0;
  private LpEditorTable table = null;

  private boolean bEditing = false;
  private boolean bShift = false;
  private boolean bCaretListenerRemoved = false;

  public LpEditorCellEditor() {
    editor = new CellTextPane();
    editor.setContentType("text/jasper");
    editor.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    editor.putClientProperty(EDITOR_PARENT_PROP, this);
    doc = editor.getStyledDocument();

    addCellEditorListener(this);
    // Fuer den TAB - Key
    editor.addKeyListener(new CellKeyListener());
    // Zum Navigieren zwischen den Zellen
    editor.setNavigationFilter(new CellNavigationFilter());
  }

  public LpEditorCellEditor(AttributeSet attributes) {
    this();
    setDefaultAttributes(attributes);
  }

  /**
   * Liefert den Text in der Zelle, abhaengig vom ContentType.
   * @return Object
   */
  public Object getCellEditorValue() {
    return editor.getText();
  }

  /**
   * Liefert das verwendete Control zum editieren von Text
   * @return JTextPane
   */
  public JTextPane getEditor() {
    return editor;
  }

  public int getEditingColumn() {
    return iColumn;
  }

  /**
   * Setzt die default Attribute wie Schriftart, Schriftgroesze, ...
   * fuer den CellEditor.
   *
   * @param attributes AttributeSet
   */
  public void setDefaultAttributes(AttributeSet attributes) {
    Style style = doc.getStyle(StyleContext.DEFAULT_STYLE);
    style.removeAttributes(style);
    style.addAttribute(StyleConstants.NameAttribute, StyleContext.DEFAULT_STYLE);
    style.addAttributes(attributes);
    editor.setLogicalStyle(style);
  }

  public Component getTableCellEditorComponent(JTable table,
                                               Object value,
                                               boolean bSelected,
                                               int row,
                                               int column) {

    ( (LpEditorTable) table).setPreparingEditor(true);

    // Wenn der Benutzer in eine Zelle einer anderen Tabelle
    // klickt, wird der Text in der alten Zelle akzeptiert.
    if (this.table != null && !table.equals(this.table)) {
      this.stopCellEditing();
    }

    value = table.getModel().getValueAt(row, column);
    table.setRowSelectionInterval(row, row);
    table.setColumnSelectionInterval(column, column);

    this.table = (LpEditorTable) table;
    iRow = row;
    iColumn = column;
    iRowCount = table.getRowCount();
    iColumnCount = table.getColumnCount();

    bEditing = true;

    doc.removeDocumentListener(docListener);

    DocumentFilter filter = ( (AbstractDocument) editor.getDocument()).
        getDocumentFilter();
    ( (AbstractDocument) editor.getDocument()).setDocumentFilter(null);
    editor.setText("");

    if (value != null) {
      editor.setText(value.toString());

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
    else {
      setOverflow(false);

    }
    ( (AbstractDocument) editor.getDocument()).setDocumentFilter(filter);
    doc.addDocumentListener(docListener);

    if (caretListener != null && bCaretListenerRemoved) {
      for (int i = 0; i < caretListener.size(); i++) {
        editor.addCaretListener( (CaretListener) caretListener.get(i));
      }
      bCaretListenerRemoved = false;
    }

    this.table.setPreparingEditor(false);
    editor.firePropertyChange(IS_EDIT_EVENT, false, true);

    return editor;
  }

  public void addCaretListener(CaretListener listener) {
    caretListener.add(listener);
    editor.addCaretListener(listener);
  }

  /**
   * Feuert ein "isEditing" PropertyChange Event ab.
   * @param event ChangeEvent
   */
  public void editingStopped(ChangeEvent event) {
    table.updateRowHeight(iRow);
    bEditing = false;

    /*
     * Listener entfernen, damit das caretUpdate Event der Zelle
     * nicht die InputAttributes ueberschreibt (und damit den
     * Status der ToolBar - Buttons im Editor). Normalerweise
     * nicht noetig, aber beim Springen mit den Cursor-Tasten
     * in eine andere Zelle wird leider zum Schlusz noch ein
     * caretUpdate der alten Zelle aufgerufen, welches dann
     * den Status ueberschreiben wuerde.
     */
    if (caretListener != null) {
      for (int i = 0; i < caretListener.size(); i++) {
        editor.removeCaretListener( (CaretListener) caretListener.get(i));
      }
      bCaretListenerRemoved = true;
    }

    editor.firePropertyChange(IS_EDIT_EVENT, true, false);
  }

  /**
   * Feuert ein "isEditing" PropertyChange Event ab.
   * @param event ChangeEvent
   */
  public void editingCanceled(ChangeEvent event) {
    bEditing = false;
    editor.firePropertyChange(IS_EDIT_EVENT, true, false);
  }

  /**
   * @return true wenn gerade editiert wird, false sonst.
   */
  public boolean isEditing() {
    return bEditing;
  }

  /**
   * @return die Tabelle, zu der der CellEditor gerade gehoert.
   */
  public LpEditorTable getTable() {
    return table;
  }

  public void setOverflow(boolean flag) {
    if (flag) {
      editor.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
    }
    else {
      editor.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }
  }

   /**
    * Startet das Editieren in der naechsten Zelle
    */
   private void editNextCell() {
     int iNewRow, iNewColumn;

     if (iColumn == iColumnCount - 1) {
       if (iRow == iRowCount - 1) {
         return;
       }
       else {
         iNewRow = iRow + 1;
         iNewColumn = 0;
       }
     }
     else {
       iNewRow = iRow;
       iNewColumn = iColumn + 1;
     }
     fireEditingStopped();
     table.editCellAt(iNewRow, iNewColumn);
     table.getEditorComponent().requestFocusInWindow();
     ( (JTextPane) table.getEditorComponent()).setCaretPosition(0);
   }

  /**
   *
   * <p><I>Passt die Tabellenzeile an die optimale Hoehe an,
   * in der der gesamte Text noch dargestellt wird</I>. </p>
   * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
   * <p>Erstellungsdatum <I>Oktober 2004</I></p>
   * <p> </p>
   * @author Sascha Zelzer
   * @version $Revision: 1.4 $
   */
  protected class CellDocumentListener
      implements DocumentListener {
    public void insertUpdate(DocumentEvent e) {
      table.updateRowHeight(iRow);
    }

    public void removeUpdate(DocumentEvent e) {
      table.updateRowHeight(iRow);
    }

    public void changedUpdate(DocumentEvent e) {
      table.updateRowHeight(iRow);
    }

  }

  /**
   *
   * <p><I>Implementiert die Cursor-Navigation zwischen Zellen</I>.</p>
   * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
   * <p>Erstellungsdatum <I>5. November 2004</I></p>
   * <p> </p>
   * @author Sascha Zelzer
   * @version $Revision: 1.4 $
   */
  protected class CellKeyListener
      extends KeyAdapter {

    public void keyPressed(KeyEvent e) {
      // TAB springt immer in die naechste Zelle
      if (e.getKeyCode() == KeyEvent.VK_TAB) {
        e.consume();
        editNextCell();
      }
      else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
        bShift = true;
      }
    }

    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
        bShift = false;
      }
    }
  }

  protected class CellNavigationFilter
      extends NavigationFilter {
    public int getNextVisualPositionFrom(JTextComponent text,
                                         int pos,
                                         Position.Bias bias,
                                         int direction,
                                         Position.Bias[] biasRet) throws
        BadLocationException {

      int nextPos = super.getNextVisualPositionFrom(text, pos, bias, direction,
          biasRet);

      // Nicht in eine andere Zelle huepfen, wenn sich der
      // Cursor noch innerhalb des Textes befindet.
      if (nextPos != pos) {
        return nextPos;
      }

      // Kein Zellenwechsel bei gedrueckter SHIFT-Taste
      if (bShift) {
        return nextPos;
      }

      int iNewRow, iNewColumn;

      // Naechste Zelle editieren
      if (direction == SwingConstants.EAST) {
        editNextCell();
        nextPos = 0;
      }
      // Vorherige Zelle editieren
      else if (direction == SwingConstants.WEST) {
        if (iColumn == 0) {
          if (iRow == 0) {
            return nextPos;
          }
          else {
            iNewRow = iRow - 1;
            iNewColumn = iColumnCount - 1;
          }
        }
        else {
          iNewRow = iRow;
          iNewColumn = iColumn - 1;
        }
        fireEditingStopped();
        table.editCellAt(iNewRow, iNewColumn);
        table.getEditorComponent().requestFocusInWindow();
        ( (JTextPane) table.getEditorComponent()).setCaretPosition( -1);
        nextPos = doc.getLength();
      }
      // Die Zelle oberhalb editieren
      else if (direction == SwingConstants.NORTH) {
        if (iRow > 0) {
          fireEditingStopped();
          table.editCellAt(iRow - 1, iColumn);
          table.getEditorComponent().requestFocusInWindow();
        }
      }
      // Die Zelle unterhalb editieren
      else if (direction == SwingConstants.SOUTH) {
        if (iRow < iRowCount - 1) {
          fireEditingStopped();
          table.editCellAt(iRow + 1, iColumn);
          table.getEditorComponent().requestFocusInWindow();
        }
      }

      return nextPos;
    }
  }

  /**
   * <p><I>Ueberschreibt setCaretPosition in JTextPane, um registrierte
   * Listener davon in Kenntnis setzten zu koennen (notwendig zum
   * wechseln der Zellen mit den Cursor-Tasten</I> </p>
   * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
   * <p>Erstellungsdatum <I>November 2004</I></p>
   * <p> </p>
   * @author Sascha Zelzer
   * @version $Revision: 1.4 $
   */
  private class CellTextPane
      extends JTextPane {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setCaretPosition(int position) {
      if (position == -1) {
        position = getDocument().getLength();

      }
      super.setCaretPosition(position);

      CaretListener[] listeners = super.getCaretListeners();
      for (int i = listeners.length - 1; i >= 0; i--) {
        listeners[i].caretUpdate(new CellCaretEvent(editor, position, position));

      }
    }
  }

  private class CellCaretEvent
      extends CaretEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dot, mark;

    public CellCaretEvent(Object source, int dot, int mark) {
      super(source);
      this.dot = dot;
      this.mark = mark;
    }

    public int getDot() {
      return dot;
    }

    public int getMark() {
      return mark;
    }
  }

}
