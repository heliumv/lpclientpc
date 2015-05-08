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
package com.lp.editor.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.lp.client.util.logger.LpLogger;

/**
 *
 * <p><I>Eine Singelton - Klasse fuer bequemen Zugriffe auf Locale
 * abhaengige Resourcen</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.3 $
 */
public class LpEditorMessages {
  private static final String RESOURCE_BUNDLE_MESSAGES =
      "com.lp.editor.res.messages";
  private static final String RESOURCE_BUNDLE_MNEMONICS =
      "com.lp.editor.res.mnemonics";

  static private LpEditorMessages instance = null;

  private LpLogger myLogger = (LpLogger) LpLogger.getInstance(LpEditorMessages.class);

  private ResourceBundle bundleMsg = null;
  private ResourceBundle bundleMnemonics = null;
  private Locale locale = null;

  private HashMap<Object, Object> maps = new HashMap<Object, Object>();

  private LpEditorMessages() {
    bundleMsg = ResourceBundle.getBundle(RESOURCE_BUNDLE_MESSAGES);
    bundleMnemonics = ResourceBundle.getBundle(RESOURCE_BUNDLE_MNEMONICS);

  }

  static public LpEditorMessages getInstance() {
    if (instance == null) {
      instance = new LpEditorMessages();
      return instance;
    }
    else {
      return instance;
    }
  }

  public void setLocale(Locale locale) {
    if (locale == null) {
      bundleMnemonics = ResourceBundle.getBundle(RESOURCE_BUNDLE_MNEMONICS);
      bundleMsg = ResourceBundle.getBundle(RESOURCE_BUNDLE_MESSAGES);
    }
    else {
      bundleMsg = ResourceBundle.getBundle(RESOURCE_BUNDLE_MESSAGES, locale);
      bundleMnemonics = ResourceBundle.getBundle(RESOURCE_BUNDLE_MNEMONICS,
                                                 locale);
    }
  }

  public String format(String key, Object[] args) {
    return MessageFormat.format(getString(key), args);
  }

  public String getString(String key) {
    try {
      return bundleMsg.getString(key);
    }
    catch (MissingResourceException e) {
      myLogger.warn("missing text resource key: " + key);
      return key;
    }
  }

  public String getString(String key, String defaultString) {
    try {
      return bundleMsg.getString(key);
    }
    catch (MissingResourceException e) {
      return defaultString;
    }
  }

  public Integer getMnemonic(String key) {
    try {
      String strChar = bundleMnemonics.getString(key);
      if (strChar.length() > 1) {
        return new Integer(0);
      }
      else {
        return new Integer( (int) strChar.charAt(0));
      }
    }
    catch (MissingResourceException e) {
      myLogger.warn("missing mnemonic resource key: " + key);
      return new Integer(0);
    }
  }

}
