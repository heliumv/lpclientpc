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
package com.lp.editor.util.clipboardparserhtml;

public class HTMLStylePart {

	private boolean bold = false;
	private boolean italic = false;
	private boolean underline = false;

	private StringBuffer text = new StringBuffer();

	public HTMLStylePart() {
	}
	public HTMLStylePart(HTMLStylePart original) {
		this.bold = original.bold;
		this.italic = original.italic;
		this.underline = original.underline;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public boolean equalsStyle(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!obj.getClass().getName().equals(this.getClass().getName()))
			return false;
		HTMLStylePart hsp = (HTMLStylePart) obj;
		return hsp.bold == bold && hsp.italic == italic
				&& hsp.underline == underline;
	}

	public String toHTML() {
		if(bold || italic || underline)
		return "<style " + (bold ? "isBold=\"true\" " : "")
				+ (italic ? "isItalic=\"true\" " : "")
				+ (underline ? "isUnderline=\"true\" " : "") + ">" + text.toString() + "</style>";
		return text.toString();
	}

	public void appendText(String text) {
		this.text.append(text);
	}
	
	public String getText() {
		return text.toString();
	}
	public boolean hasText() {
		return text.length()>0;
	}

}
