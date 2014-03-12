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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public class DialogLosgroesseaendern extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperNumberField wnfLosgroesse = null;
	private Integer neueLosgroesse = null;
	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();
	private TabbedPaneLos tpLos = null;

	private WrapperRadioButton wrbJa = new WrapperRadioButton();
	private WrapperRadioButton wrbNein = new WrapperRadioButton();

	private ButtonGroup buttonGroupMaterial = new ButtonGroup();

	public DialogLosgroesseaendern(TabbedPaneLos tpLos, Integer neueLosgroesse) {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.neueLosgroesse = neueLosgroesse;
		this.tpLos = tpLos;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(300, 200);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"lp.menu.losgroesseaendern"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {
				if (wnfLosgroesse.getInteger() != null
						&& wnfLosgroesse.getInteger().intValue() != 0) {

					if (wnfLosgroesse.getInteger().doubleValue() > tpLos
							.getLosDto().getNLosgroesse().doubleValue()) {
						wrbNein.setSelected(true);
					}

					if (wrbJa.isSelected() || wrbNein.isSelected()) {

						DelegateFactory.getInstance().getFertigungDelegate()
								.aendereLosgroesse(tpLos.getLosDto().getIId(),
										wnfLosgroesse.getInteger(),
										wrbJa.isSelected());
						tpLos.getLosDto().setNLosgroesse(
								new BigDecimal(wnfLosgroesse.getInteger()
										.doubleValue()));
						setVisible(false);

					} else {
						DialogFactory
								.showModalDialog(
										LPMain
												.getInstance()
												.getTextRespectUISPr("lp.error"),
										LPMain
												.getInstance()
												.getTextRespectUISPr(
														"fert.losgroesseaendern.optionwaehlen"));
						return;
					}
				}
			} catch (Throwable e1) {
				tpLos.handleException(e1, true);
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			this.setVisible(false);
		}

	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		wnfLosgroesse = new WrapperNumberField();
		wnfLosgroesse.setMinimumValue(1);
		wnfLosgroesse.setFractionDigits(0);
		if (neueLosgroesse != null) {
			wnfLosgroesse.setInteger(neueLosgroesse);
		}
		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.report.save"));
		wbuAbbrechen
				.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		WrapperLabel wlaNeueLosgroesse = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("fert.losgroesseaendern.neuelosgroesse"));
		WrapperLabel wlaFrage = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr(
						"fert.losgroesseaendern.waspassiertmitmaterial"));

		wrbJa.setText(LPMain.getInstance().getTextRespectUISPr("lp.ja"));
		wrbNein.setText(LPMain.getInstance().getTextRespectUISPr("lp.nein"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		buttonGroupMaterial.add(wrbJa);
		buttonGroupMaterial.add(wrbNein);

		add(panel1);
		panel1.add(wlaNeueLosgroesse, new GridBagConstraints(0, 0, 2, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wnfLosgroesse, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wlaFrage, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(wrbJa, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(wrbNein, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		panel1.add(wbuSpeichern, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		setContentPane(panel1);

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change
				 * the JOptionPane's value property.
				 */
				setVisible(false);
			}
		});

	}

	public void clearAndHide() {
		setVisible(false);
	}

}
