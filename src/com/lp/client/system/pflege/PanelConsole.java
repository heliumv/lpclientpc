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
package com.lp.client.system.pflege;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class PanelConsole extends JTextPane {
	
	private static final long serialVersionUID = 5398392399411053902L;

	private PrintWriter out;
	private PrintWriter err;
	private StyledDocument doc;
	private SimpleAttributeSet outAttribute;
	private SimpleAttributeSet errAttribute;
	
	public PanelConsole() {
		setFont(Font.decode("Courier"));
		setEditable(false);
		setBackground(Color.white);
		
		doc = getStyledDocument();
		outAttribute = new SimpleAttributeSet();
		errAttribute = new SimpleAttributeSet();
		StyleConstants.setForeground(outAttribute, Color.BLACK);
		StyleConstants.setForeground(errAttribute, Color.RED);
		
		out = new PrintWriter(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				try {
					doc.insertString(doc.getLength(), "" + (char)b, outAttribute);
//					setCaretPosition(doc.getLength()-1);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}, true);
		
		err = new PrintWriter(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				try {
					doc.insertString(doc.getLength(), "" + (char)b, errAttribute);
//					setCaretPosition(doc.getLength()-1);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}, true);
	}

	public PrintWriter getOutputWriter() {
		return out;
	}
	
	public PrintWriter getErrorWriter() {
		return err;
	}
	

}
