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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.lp.editor.util.clipboardparserhtml.ClipboardParserHTML;

public class ClipboardParser {
	
	private static Map<String, IClipboardParser> parsers = new HashMap<String, IClipboardParser> () {
		private static final long serialVersionUID = -7101611543934447635L;
		
		{
			put("text/html; class=java.lang.String; charset=Unicode", new ClipboardParserHTML());
		}
		
	};

	public static String parseToLpEditorFormat(Clipboard c) {
		DataFlavor dfs[] = c.getAvailableDataFlavors();
		String parsed = null;
//		DataFlavor df = new DataFlavor("text/html; class=<java.lang.String>;", "text/html");

//		System.out.println("Dataflavours Beginn...") ;
		for(DataFlavor df : dfs) {
//			System.out.println("Dataflavour <" + df.getMimeType() + ">") ;
			
			IClipboardParser parser = parsers.get(df.getMimeType());
			if(parser != null) {
				try {
					parsed = parser.parseToString(c.getData(df));
					if(parsed != null)
						return parsed;
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		System.out.println("Dataflavours Ende (kein Format gefunden).") ;
		return null;
	}
}
