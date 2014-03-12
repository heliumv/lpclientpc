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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import com.lp.editor.util.IClipboardParser;

public class ClipboardParserHTML implements IClipboardParser {

	@Override
	public String parseToString(Object data) {
		if (!(data instanceof String))
			return null;

		String rawHTML = (String) data;
		// System.out.println("The HTML Input Text has (" + rawHTML.length() +
		// ") Bytes and is:") ;
		// System.out.println(rawHTML) ;
		rawHTML = Pattern
				.compile("<head(.*?)</head(.*?)>",
						Pattern.DOTALL + Pattern.CASE_INSENSITIVE)
				.matcher(rawHTML).replaceAll("");//HEAD Tag entfernen
		rawHTML = rawHTML.replaceAll("> <", ">&nbsp;<"); // sonst werden spaces
															// zwischen tags
															// ignoriert
		StringReader reader = new StringReader(rawHTML);
		List<HTMLStylePart> parts = new ArrayList<HTMLStylePart>();
		ParserCallback callback = new HTMLCallback(parts);

		ParserDelegator delegator = new ParserDelegator();
		try {
			delegator.parse(reader, callback, true);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

		StringBuffer buffer = new StringBuffer();
		for (HTMLStylePart part : parts) {
			buffer.append(part.toHTML());
		}

		if (buffer.lastIndexOf("\n") == buffer.length() - 1)
			buffer.deleteCharAt(buffer.length() - 1);

		// da schummelt sich irgnedwo ein char 160 rein statt einem Space
		// darum hier raus
		return buffer.toString().replace((char) 160, ' ');
	}

	private class HTMLCallback extends ParserCallback {

		private List<HTMLStylePart> parts;

		private Map<String, String> tagStartReplacements = new HashMap<String, String>() {
			private static final long serialVersionUID = 4211610201850559057L;

			{
				put("br", "\n");
			}
		};
		private Map<String, String> tagEndReplacements = new HashMap<String, String>() {
			private static final long serialVersionUID = 4211610201850559057L;

			{
				put("p", "\n");
			}
		};

		private HTMLStylePart getLastPart() {
			return parts.get(parts.size() - 1);
		}

		public HTMLCallback(List<HTMLStylePart> parts) {
			this.parts = parts;
			parts.add(new HTMLStylePart());
		}

		@Override
		public void handleText(char[] data, int pos) {
			String s = new String(data);
			getLastPart().appendText(s);
		}

		@Override
		public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
			String tag = t.toString();
			if (tagStartReplacements.containsKey(tag)) {
				getLastPart().appendText(tagStartReplacements.get(tag));
			} else
				handleStyleTag(tag, true);
		}

		private void handleStyleTag(String tag, boolean startTag) {
			if (tag.equals("b") || tag.equals("i") || tag.equals("u")
					|| tag.equals("strong")) {
				if (getLastPart().hasText())
					parts.add(new HTMLStylePart(getLastPart()));

				if (tag.equals("b") || tag.equals("strong"))
					getLastPart().setBold(startTag);
				else if (tag.equals("i"))
					getLastPart().setItalic(startTag);
				else if (tag.equals("u"))
					getLastPart().setUnderline(startTag);
			}
		}

		@Override
		public void handleEndTag(Tag t, int pos) {
			String tag = t.toString();
			if (tagEndReplacements.containsKey(tag)) {
				getLastPart().appendText(tagEndReplacements.get(tag));
			} else
				handleStyleTag(tag, false);
		}

		@Override
		public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
			String tag = t.toString();

			if (tagStartReplacements.containsKey(tag)) {
				getLastPart().appendText(tagStartReplacements.get(tag));
			}

		}

	}

}
