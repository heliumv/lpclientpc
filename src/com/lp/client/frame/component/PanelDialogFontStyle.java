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
package com.lp.client.frame.component;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;

public class PanelDialogFontStyle extends JDialog implements ItemListener,
		KeyListener, ActionListener {

	private static final long serialVersionUID = -6794952846191950557L;

	private JTextArea hint;
	private WrapperLabel fontnameLabel;
	private WrapperComboBox fontname;
	private WrapperLabel fontsizeLabel;
	private WrapperComboBox fontsize;
	private WrapperCheckBox bold;
	private WrapperLabel beispielTitel;
	private WrapperTextField beispielEingabe;
	private WrapperLabel beispielLabel;
	private WrapperCheckBox redGreenBlindness;
	private WrapperButton okay;
	private WrapperButton cancel;
	private WrapperButton reset;

	public PanelDialogFontStyle(Frame owner) {
		super(owner, LPMain
				.getTextRespectUISPr("lp.layout.schrift.einstellungen"));
		initComponents();
		
//		updateUI();
		setSize(Defaults.getInstance().bySizeFactor(400, 400));
		setModal(true);
		setLocationRelativeTo(owner);
		setVisible(true);
	}
	
	private void fontToComponents() {
		Font currentFont = UIManager.getDefaults().getFont("Label.font");
		fontname.setSelectedItem(currentFont.getFamily());
		fontsize.setSelectedItem(currentFont.getSize() + "");
		bold.setSelected(currentFont.getStyle() != Font.PLAIN
				&& currentFont.getStyle() != Font.ITALIC);
		redGreenBlindness.setSelected(HelperClient.COLOR_RED_GREEN_BLINDNESS.equals(Defaults.getInstance().getColorVision()));
	}

	private void initComponents() {
		hint = new JTextArea(
				LPMain.getTextRespectUISPr("lp.layout.einstellungen.hinweis"));
		hint.setEditable(false);
		hint.setWrapStyleWord(true);
		hint.setLineWrap(true);
		hint.setOpaque(false);
		fontnameLabel = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.layout.schrift.art"));
		fontnameLabel.setHorizontalAlignment(JLabel.LEFT);
		fontname = new WrapperComboBox(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		
		fontname.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 6898474317395714596L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				c.setFont(Font.decode(value.toString()));
				return c;
			}
		});

		fontsizeLabel = new WrapperLabel(LPMain.getTextRespectUISPr("lp.layout.schrift.groesse"));
		fontsizeLabel.setHorizontalAlignment(JLabel.LEFT);
		fontsize = new WrapperComboBox(new Object[] { "8", "9", "10", "11",
				"12", "14", "16" });
		fontsize.setEditable(true);

		bold = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.layout.schrift.fett"));
		beispielTitel = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.layout.beispiel"));
		beispielTitel.setHorizontalAlignment(JLabel.LEFT);
		beispielEingabe = new WrapperTextField();
		beispielEingabe.setText("aAbBcCdDeEfFgGhHiIjJkKlLmM...");
		beispielEingabe.setMandatoryField(true);
		beispielLabel = new WrapperLabel("");
		beispielLabel.setHorizontalAlignment(JLabel.LEFT);
		redGreenBlindness = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.layout.redgreenblindness"));
		updateBeispielText();
		fontToComponents();

		fontname.addItemListener(this);
		fontsize.addItemListener(this);
		bold.addItemListener(this);
		beispielEingabe.addKeyListener(this);
		redGreenBlindness.addActionListener(this);
		
		okay = new WrapperButton(LPMain.getTextRespectUISPr("button.ok"));
		cancel = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		reset = new WrapperButton(LPMain.getTextRespectUISPr("button.reset"));
		okay.addActionListener(this);
		cancel.addActionListener(this);
		reset.addActionListener(this);

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		panel.setLayout(new GridBagLayout());
		panel.add(hint, new GridBagConstraints(0, 0, 3, 1, 0, 1,
				GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panel.add(fontnameLabel, new GridBagConstraints(0, 1, 2, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panel.add(fontsizeLabel, new GridBagConstraints(2, 1, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		panel.add(fontname, new GridBagConstraints(0, 2, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		panel.add(fontsize, new GridBagConstraints(2, 2, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		panel.add(bold, new GridBagConstraints(0, 3, 3, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		panel.add(beispielTitel, new GridBagConstraints(0, 4, 3, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		panel.add(beispielEingabe, new GridBagConstraints(0, 5, 3, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		panel.add(beispielLabel, new GridBagConstraints(0, 6, 3, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));

		panel.add(redGreenBlindness, new GridBagConstraints(0, 7, 3, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		
		panel.add(okay, new GridBagConstraints(0, 9, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		panel.add(cancel, new GridBagConstraints(1, 9, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		panel.add(reset, new GridBagConstraints(2, 9, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 0), 0, 0));
		this.getContentPane().add(panel);
	}

	private void updateBeispielText() {
		beispielLabel.setText(beispielEingabe.getText());
	}

	private void updateStyle() {
		Font font = Font.decode(fontname.getSelectedItem().toString());

		Integer size = null;
		try {
			size = new Integer(fontsize.getSelectedItem().toString());
		} catch (NumberFormatException ex) {
			size = font.getSize();
		}

		font = font
				.deriveFont(bold.isSelected() ? Font.BOLD : Font.PLAIN, size);

		beispielLabel.setFont(font);
	}
	
	private void okayPressed() {
		Desktop.setFontChanged(!beispielLabel.getFont().equals(beispielTitel.getFont()));
		ClientPerspectiveManager.getInstance().saveFont(beispielLabel.getFont());
		
		String colorVision = redGreenBlindness.isSelected() ?
				HelperClient.COLOR_RED_GREEN_BLINDNESS : HelperClient.COLOR_NORMAL;
		ClientPerspectiveManager.getInstance().saveColorVision(colorVision);
		Defaults.getInstance().setColorVision(colorVision);
		updateUI();
		this.dispose();
	}
	
	private void cancelPressed() {
		this.dispose();
	}
	
	private void resetPressed() {
		ClientPerspectiveManager.getInstance().resetFont();
		Desktop.setFontChanged(false);
		updateUI();
		fontToComponents();
	}

	private void updateUI() {
		LPMain.getInstance().getDesktop().reloadFont(true);
		SwingUtilities.updateComponentTreeUI(LPMain.getInstance().getDesktop());
		SwingUtilities.updateComponentTreeUI(this);
	}

	@Override
	public void itemStateChanged(ItemEvent ev) {
		updateStyle();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		updateBeispielText();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	protected void colorVisionChanged() {
		String colorVision = redGreenBlindness.isSelected() ? HelperClient.COLOR_RED_GREEN_BLINDNESS : HelperClient.COLOR_NORMAL;
		Border b = BorderFactory.createLineBorder(HelperClient.getMandatoryFieldBorderColor(colorVision));
		beispielEingabe.setBorder(b);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == okay) {
			okayPressed();
		} else if(ev.getSource() == cancel) {
			cancelPressed();
		} else if(ev.getSource() == reset) {
			resetPressed();
		} else if(ev.getSource() == redGreenBlindness) {
			colorVisionChanged();
		}
	}

}
