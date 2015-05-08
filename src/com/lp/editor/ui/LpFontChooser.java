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
package com.lp.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.lp.editor.LpEditor;
import com.lp.editor.util.LpEditorMessages;

@SuppressWarnings("static-access")
/**
 * <p>Ueberschrift: LogP Editor</p>
 * <p>Beschreibung: Ein Dialog zum Einstellen von Schriftattributen.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Sascha Zelzer
 * @author Kajetan Fuchsberger
 * @version 0.0
 */
public class LpFontChooser extends JDialog implements ItemListener,
		ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextPane preview;

	JButton fgColorButton;
	JButton bgColorButton;
	// JLabel fgColorLabel;
	// JLabel bgColorLabel;
	JColorChooser colorChooser;

	JComboBox fontNameCombo;
	JComboBox fontSizeCombo;

	JCheckBox subscriptCheck;
	JCheckBox superscriptCheck;
	JCheckBox underlineCheck;
	JCheckBox boldCheck;
	JCheckBox strikethroughCheck;
	JCheckBox italicCheck;

	MutableAttributeSet styleAttributes;
	int mode = 0;

	int option = JOptionPane.CLOSED_OPTION;
	// false, wenn kein background-color attribute gesetzt war
	boolean bOpaqueBackground = false;

	public LpFontChooser(JFrame parent, int mode, String[] fontNames,
			String[] fontSizes) {
		this(parent, mode, fontNames, fontSizes, null);
	}

	/**
	 * 
	 * @param parent
	 *            Der Parent-Frame.
	 * @param mode
	 *            int
	 * @param fontNames
	 *            Verfuegbare Schriftarten.
	 * @param fontSizes
	 *            Vordefinierte Schriftgroessen.
	 * @param locale
	 *            Locale
	 */
	public LpFontChooser(JFrame parent, int mode, String[] fontNames,
			String[] fontSizes, Locale locale) {
		super(parent, LpEditorMessages.getInstance().getString(
				"FontChooser.ChangeTextProp"), true);

		this.mode = mode;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JPanel p = new JPanel(new GridLayout(2, 2, 10, 0));
		p.setBorder(new TitledBorder(new EtchedBorder(), LpEditorMessages
				.getInstance().getString("FontChooser.Font")));

		p.add(new JLabel(LpEditorMessages.getInstance().getString(
				"FontChooser.Name")));
		p.add(new JLabel(LpEditorMessages.getInstance().getString(
				"FontChooser.Size")));

		if (fontNames != null) {
			fontNameCombo = new JComboBox(fontNames);
		} else {
			fontNameCombo = new JComboBox();
		}

		p.add(fontNameCombo);

		fontSizeCombo = new JComboBox(fontSizes);
		fontSizeCombo.setEditable(true);
		p.add(fontSizeCombo);

		getContentPane().add(p);

		p = new JPanel(new GridLayout(2, 3, 10, 5));
		p.setBorder(new TitledBorder(new EtchedBorder(), LpEditorMessages
				.getInstance().getString("FontChooser.Style")));

		boldCheck = new JCheckBox(LpEditorMessages.getInstance().getString(
				"FontChooser.Bold"));
		p.add(boldCheck);

		italicCheck = new JCheckBox(LpEditorMessages.getInstance().getString(
				"FontChooser.Italic"));
		p.add(italicCheck);

		underlineCheck = new JCheckBox(LpEditorMessages.getInstance()
				.getString("FontChooser.Underline"));
		underlineCheck.setActionCommand("underline");
		p.add(underlineCheck);

		strikethroughCheck = new JCheckBox(LpEditorMessages.getInstance()
				.getString("FontChooser.Strikethrough"));
		strikethroughCheck.setActionCommand("strikethrough");
		p.add(strikethroughCheck);

		subscriptCheck = new JCheckBox(LpEditorMessages.getInstance()
				.getString("FontChooser.Subscript"));
		subscriptCheck.setActionCommand("subscript");

		superscriptCheck = new JCheckBox(LpEditorMessages.getInstance()
				.getString("FontChooser.Superscript"));
		superscriptCheck.setActionCommand("superscript");

		if (mode != LpEditor.MODE_JASPER) {
			p.add(subscriptCheck);
			p.add(superscriptCheck);
		}

		getContentPane().add(p);

		p = new JPanel();
		p.setBorder(new TitledBorder(new EtchedBorder(), LpEditorMessages
				.getInstance().getString("FontChooser.Color")));
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(new JLabel(LpEditorMessages.getInstance().getString(
				"FontChooser.ForegroundColor")
				+ " "));
		fgColorButton = new JButton("     ");
		fgColorButton.setMargin(new Insets(1, 1, 1, 1));
		// fgColorLabel = new JLabel("     ");
		// fgColorLabel.setOpaque(true);
		// ****** Die folgende Zeile fuehrt zu eine NPE *************
		// ****** siehe
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4694008
		// fgColorButton.add(fgColorLabel);
		p.add(fgColorButton);
		p.add(Box.createHorizontalStrut(10));
		p.add(Box.createHorizontalGlue());
		p.add(new JLabel(LpEditorMessages.getInstance().getString(
				"FontChooser.BackgroundColor")
				+ " "));
		bgColorButton = new JButton("     ");
		bgColorButton.setMargin(new Insets(1, 1, 1, 1));
		// bgColorLabel = new JLabel("     ");
		// bgColorLabel.setOpaque(true);
		// bgColorButton.add(bgColorLabel);
		p.add(bgColorButton);
		getContentPane().add(p);

		colorChooser = new JColorChooser();
		if (locale != null) {
			colorChooser.setLocale(locale);

		}
		p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(new EtchedBorder(), LpEditorMessages
				.getInstance().getString("FontChooser.Preview")));
		preview = new JTextPane();
		preview.setPreferredSize(new Dimension(120, 40));
		preview.setMargin(new Insets(3, 6, 2, 6));
		preview.setEditable(false);
		p.add(preview, BorderLayout.CENTER);
		getContentPane().add(p);

		JButton okButton = new JButton(LpEditorMessages.getInstance()
				.getString("Ok"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				option = JOptionPane.OK_OPTION;
				setVisible(false);
			}
		});

		JButton cancelButton = new JButton(LpEditorMessages.getInstance()
				.getString("Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				option = JOptionPane.CANCEL_OPTION;
				setVisible(false);
			}
		});

		p = new JPanel(new FlowLayout());
		JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
		p1.add(okButton);
		p1.add(cancelButton);
		p.add(p1);
		getContentPane().add(p);

		pack();

		setResizable(false);

		fontNameCombo.addItemListener(this);
		fontSizeCombo.addItemListener(this);

		fgColorButton.setActionCommand("fgcolor");
		fgColorButton.addActionListener(this);
		bgColorButton.setActionCommand("bgcolor");
		bgColorButton.addActionListener(this);

		boldCheck.addActionListener(this);
		italicCheck.addActionListener(this);
		underlineCheck.addActionListener(this);
		strikethroughCheck.addActionListener(this);
		superscriptCheck.addActionListener(this);
		subscriptCheck.addActionListener(this);
	}

	public void itemStateChanged(ItemEvent ie) {
		updatePreview();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("fgcolor")) {
			Color oldColor = StyleConstants.getForeground(preview
					.getCharacterAttributes());
			Color newColor = JColorChooser.showDialog(this, LpEditorMessages
					.getInstance().getString("Action.ColorForeground"),
					oldColor);
			if (newColor != null) {
				// fgColorLabel.setBackground(newColor);
				fgColorButton.setBackground(newColor);
			}
		} else if (ae.getActionCommand().equals("bgcolor")) {
			Color oldColor;
			if (preview.getCharacterAttributes().isDefined(
					StyleConstants.Background)) {
				oldColor = StyleConstants.getBackground(preview
						.getCharacterAttributes());
			} else {
				oldColor = null;
			}
			Color newColor = colorChooser.showDialog(this, LpEditorMessages
					.getInstance().getString("Action.ColorBackground"),
					oldColor);
			if (newColor != null) {
				// bgColorLabel.setBackground(newColor);
				bgColorButton.setBackground(newColor);
				bOpaqueBackground = true;
			}
		} else if (ae.getActionCommand().equals("underline")) {
			if (underlineCheck.isSelected()) {
				strikethroughCheck.setSelected(false);
			}
		} else if (ae.getActionCommand().equals("strikethrough")) {
			if (strikethroughCheck.isSelected()) {
				underlineCheck.setSelected(false);
			}
		} else if (ae.getActionCommand().equals("subscript")) {
			if (subscriptCheck.isSelected()) {
				superscriptCheck.setSelected(false);
			}
		} else if (ae.getActionCommand().equals("superscript")) {
			if (superscriptCheck.isSelected()) {
				subscriptCheck.setSelected(false);
			}
		}

		updatePreview();
	}

	/**
	 * Setzt die Dialog-Elemente entsprechend dem AttributeSet.
	 * 
	 * @param attr
	 *            Die zu bearbeitenden Attribute.
	 * @param text
	 *            Der selektierte Text. Dieser wird im preview-Fenster
	 *            angezeigt. Falls null, wird "some text..." angezeigt.
	 */
	public void setAttributes(AttributeSet attr, String text) {
		styleAttributes = new SimpleAttributeSet(attr);

		String fontName = StyleConstants.getFontFamily(attr);
		fontNameCombo.setSelectedItem(fontName);

		int fontSize = StyleConstants.getFontSize(attr);
		fontSizeCombo.setSelectedItem(Integer.toString(fontSize));

		boldCheck.setSelected(StyleConstants.isBold(attr));
		italicCheck.setSelected(StyleConstants.isItalic(attr));
		underlineCheck.setSelected(StyleConstants.isUnderline(attr));
		strikethroughCheck.setSelected(StyleConstants.isStrikeThrough(attr));
		subscriptCheck.setSelected(StyleConstants.isSubscript(attr));
		superscriptCheck.setSelected(StyleConstants.isSuperscript(attr));

		// fgColorLabel.setBackground(StyleConstants.getForeground(attr));
		fgColorButton.setBackground(StyleConstants.getForeground(attr));
		if (attr.isDefined(StyleConstants.Background)) {
			// bgColorLabel.setBackground(StyleConstants.getBackground(attr));
			bgColorButton.setBackground(StyleConstants.getBackground(attr));
			bOpaqueBackground = true;
		} else {
			bOpaqueBackground = false;
			// bgColorLabel.setBackground(bgColorButton.getBackground());
			bgColorButton.setBackground(bgColorButton.getBackground());
		}

		if (text == null) {
			preview.setText(LpEditorMessages.getInstance().getString(
					"FontChooser.PreviewText"));
		} else {
			preview.setText(text);

		}
		updatePreview();
	}

	/**
	 * 
	 * @return Liefert die neuen Attribute.
	 */
	public AttributeSet getAttributes() {
		if (styleAttributes == null) {
			return null;
		}

		return styleAttributes;
	}

	public int getOption() {
		return option;
	}

	public void setFonts(String[] aFonts) {
		ItemListener[] listeners = fontNameCombo.getItemListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				fontNameCombo.removeItemListener(listeners[i]);
			}
		}

		fontNameCombo.removeAllItems();
		for (int i = 0; i < aFonts.length; i++) {
			fontNameCombo.addItem(aFonts[i]);
		}
		fontNameCombo.setSelectedIndex(0);

		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				fontNameCombo.addItemListener(listeners[i]);
			}
		}

	}

	private void updatePreview() {
		String fontName = (String) fontNameCombo.getSelectedItem();
		int fontSize = Integer.parseInt((String) fontSizeCombo
				.getSelectedItem());
		if (fontSize <= 0) {
			return;
		}

		styleAttributes = new SimpleAttributeSet();

		if (fontName != null) {

			StyleConstants.setFontFamily(styleAttributes,
					(String) fontNameCombo.getSelectedItem());
		}
		StyleConstants.setFontSize(styleAttributes,
				Integer.parseInt((String) fontSizeCombo.getSelectedItem()));
		StyleConstants.setBold(styleAttributes, boldCheck.isSelected());
		StyleConstants.setItalic(styleAttributes, italicCheck.isSelected());
		StyleConstants.setUnderline(styleAttributes,
				underlineCheck.isSelected());
		StyleConstants.setStrikeThrough(styleAttributes,
				strikethroughCheck.isSelected());
		if (mode != LpEditor.MODE_JASPER) {
			StyleConstants.setSubscript(styleAttributes,
					subscriptCheck.isSelected());
			StyleConstants.setSuperscript(styleAttributes,
					superscriptCheck.isSelected());
		}
		// StyleConstants.setForeground(styleAttributes,
		// fgColorLabel.getBackground());
		StyleConstants.setForeground(styleAttributes,
				fgColorButton.getBackground());
		if (bOpaqueBackground) {
			// StyleConstants.setBackground(styleAttributes,
			// bgColorLabel.getBackground());
			StyleConstants.setBackground(styleAttributes,
					bgColorButton.getBackground());

			// preview.setParagraphAttributes funktioniert nicht fuer die
			// Hintergrundfarbe, deshalb so.
		}
		preview.setSelectionStart(0);
		preview.setSelectionEnd(preview.getText().length());
		preview.setCharacterAttributes(styleAttributes, true);

		preview.updateUI();
	}

}
