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

import java.util.Hashtable;

import com.lp.client.util.logger.LpLogger;

/**
 *
 * <p><I>Verwaltet die Icons fuer den LpEditor</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>September 2003</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.2 $
 */

public class LpEditorIconManager {

  private LpLogger myLogger = (LpLogger) LpLogger.getInstance(
      LpEditorIconManager.class);

  private final static String RESOURCE_PREFIX_LOGP = "res/images/";

  public final static String ICON_FILE_NEW = "FileNew";
  public final static String ICON_FILE_OPEN = "FileOpen";
  public final static String ICON_FILE_SAVE = "FileSave";
  public final static String ICON_FILE_SAVEAS = "FileSaveAs";

  public final static String ICON_EDIT_CUT = "EditCut";
  public final static String ICON_EDIT_COPY = "EditCopy";
  public final static String ICON_EDIT_PASTE = "EditPaste";
  public final static String ICON_EDIT_INSERT_DATE = "InsertDate";
  public final static String ICON_EDIT_INSERT_SIGNATUR = "InsertSignatur";

  public final static String ICON_EDIT_UNDO = "EditUndo";
  public final static String ICON_EDIT_REDO = "EditRedo";


  public final static String ICON_STYLE_BOLD = "StyleBold";
  public final static String ICON_STYLE_ITALIC = "StyleItalic";
  public final static String ICON_STYLE_UNDERLINE = "StyleUnderline";
  public final static String ICON_STYLE_STRIKETHROUGH = "StyleStrikethrough";

  public final static String ICON_STYLE_ALIGN_LEFT = "StyleAlignLeft";
  public final static String ICON_STYLE_ALIGN_RIGHT = "StyleAlignRight";
  public final static String ICON_STYLE_ALIGN_CENTER = "StyleAlignCenter";
  public final static String ICON_STYLE_ALIGN_JUSTIFIED = "StyleAlignJustified";

  public final static String ICON_STYLE_COLOR_FOREGROUND =
      "StyleColorForeground";
  public final static String ICON_STYLE_COLOR_BACKGROUNG =
      "StyleColorBackground";

  public final static String ICON_STYLE_FONT = "StyleFont";

  public final static String ICON_INSERT_TABLE = "InsertTable";
  public final static String ICON_DELETE_TABLE = "DeleteTable";
  public final static String ICON_INSERT_ROW_BEFORE = "InsertRowBefore";
  public final static String ICON_INSERT_ROW_AFTER = "InsertRowAfter";
  public final static String ICON_DELETE_ROW = "DeleteRow";

  private Hashtable<String, String> iconResourceNames;

  public LpEditorIconManager() {
    iconResourceNames = new Hashtable<String, String>();
    initIconHashtable();
  }

  private void initIconHashtable() {
    iconResourceNames.clear();

    iconResourceNames.put(ICON_FILE_NEW, RESOURCE_PREFIX_LOGP + "stock_new.png");
    iconResourceNames.put(ICON_FILE_OPEN,
                          RESOURCE_PREFIX_LOGP + "stock_open.png");
    iconResourceNames.put(ICON_FILE_SAVE,
                          RESOURCE_PREFIX_LOGP + "stock_save.png");
    iconResourceNames.put(ICON_FILE_SAVEAS,
                          RESOURCE_PREFIX_LOGP + "stock_save_as.png");

    iconResourceNames.put(ICON_EDIT_CUT, RESOURCE_PREFIX_LOGP + "stock_cut.png");
    iconResourceNames.put(ICON_EDIT_COPY,
                          RESOURCE_PREFIX_LOGP + "stock_copy.png");
    iconResourceNames.put(ICON_EDIT_PASTE,
                          RESOURCE_PREFIX_LOGP + "stock_paste.png");
    iconResourceNames.put(ICON_EDIT_INSERT_DATE,
                          RESOURCE_PREFIX_LOGP + "date_time.png");


    iconResourceNames.put(ICON_EDIT_UNDO,
                          RESOURCE_PREFIX_LOGP + "stock_undo.png");
    iconResourceNames.put(ICON_EDIT_REDO,
                          RESOURCE_PREFIX_LOGP + "stock_redo.png");

    iconResourceNames.put(ICON_STYLE_BOLD,
                          RESOURCE_PREFIX_LOGP + "stock_text_bold.png");
    iconResourceNames.put(ICON_STYLE_ITALIC,
                          RESOURCE_PREFIX_LOGP + "stock_text_italic.png");
    iconResourceNames.put(ICON_STYLE_UNDERLINE,
                          RESOURCE_PREFIX_LOGP + "stock_text_underlined.png");
    iconResourceNames.put(ICON_STYLE_STRIKETHROUGH,
                          RESOURCE_PREFIX_LOGP + "stock_text-strikethrough.png");

    iconResourceNames.put(ICON_STYLE_ALIGN_LEFT,
                          RESOURCE_PREFIX_LOGP + "stock_text_left.png");
    iconResourceNames.put(ICON_STYLE_ALIGN_RIGHT,
                          RESOURCE_PREFIX_LOGP + "stock_text_right.png");
    iconResourceNames.put(ICON_STYLE_ALIGN_CENTER,
                          RESOURCE_PREFIX_LOGP + "stock_text_center.png");
    iconResourceNames.put(ICON_STYLE_ALIGN_JUSTIFIED,
                          RESOURCE_PREFIX_LOGP + "stock_text_justify.png");

    iconResourceNames.put(ICON_STYLE_COLOR_FOREGROUND,
                          RESOURCE_PREFIX_LOGP +
                          "stock_text_color_foreground.png");
    iconResourceNames.put(ICON_STYLE_COLOR_BACKGROUNG,
                          RESOURCE_PREFIX_LOGP +
                          "stock_text_color_background.png");

    iconResourceNames.put(ICON_STYLE_FONT,
                          RESOURCE_PREFIX_LOGP + "stock_font-size.png");

    iconResourceNames.put(ICON_INSERT_TABLE,
                          RESOURCE_PREFIX_LOGP + "stock_data-new-table.png");
    iconResourceNames.put(ICON_DELETE_TABLE,
                          RESOURCE_PREFIX_LOGP + "stock_data-delete-table.png");
    iconResourceNames.put(ICON_INSERT_ROW_BEFORE,
                          RESOURCE_PREFIX_LOGP + "stock_insert-rows.png");
    iconResourceNames.put(ICON_INSERT_ROW_AFTER,
                          RESOURCE_PREFIX_LOGP + "stock_insert-rows.png");
    iconResourceNames.put(ICON_DELETE_ROW,
                          RESOURCE_PREFIX_LOGP + "stock_delete-row.png");
    iconResourceNames.put(ICON_EDIT_INSERT_SIGNATUR,
            RESOURCE_PREFIX_LOGP + "contract.png");

  }

  public int getIconCount() {
    return iconResourceNames.size();
  }

  /**
   * Liefert das Icon passend zum Key
   * @param sIconKey String
   * @return ImageIcon
   */
  public javax.swing.ImageIcon getIcon(String sIconKey) {
    javax.swing.ImageIcon icon = null;

    try {
      String sIconResource = (String) iconResourceNames.get(sIconKey);
      icon = new javax.swing.ImageIcon(getClass().getResource(sIconResource));
    }
    catch (NullPointerException ex) {
      myLogger.warn("Icon nicht vorhanden: " + iconResourceNames.get(sIconKey));
    }

    return icon;
  }

}
