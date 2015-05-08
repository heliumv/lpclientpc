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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

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

	public DialogLosgroesseaendern(TabbedPaneLos tpLos, Integer neueLosgroesse) {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.neueLosgroesse = neueLosgroesse;
		this.tpLos = tpLos;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(300, 140);

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
						// PJ18876 Wenn Losgroesse erhoeht wird, dann Material
						// anhand SSG nachbuchen

						BigDecimal diff = new BigDecimal(wnfLosgroesse
								.getInteger().doubleValue()
								- tpLos.getLosDto().getNLosgroesse()
										.doubleValue());

						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.aendereLosgroesse(tpLos.getLosDto().getIId(),
										wnfLosgroesse.getInteger(), false);

						tpLos.getLosDto().setNLosgroesse(
								new BigDecimal(wnfLosgroesse.getInteger()
										.doubleValue()));

						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG,
										ParameterFac.KATEGORIE_FERTIGUNG,
										LPMain.getInstance().getTheClient()
												.getMandant());

						boolean bKeineAutomatischeMaterialbuchung = ((Boolean) parameter
								.getCWertAsObject());

						StuecklisteDto stklDto = null;
						if (tpLos.getLosDto().getStuecklisteIId() != null) {

							stklDto = DelegateFactory
									.getInstance()
									.getStuecklisteDelegate()
									.stuecklisteFindByPrimaryKey(
											tpLos.getLosDto()
													.getStuecklisteIId());
						}

						// PJ18630

						boolean bMaterialbuchungBeiAblieferung = true;
						if (stklDto != null) {
							bKeineAutomatischeMaterialbuchung = Helper
									.short2boolean(stklDto
											.getBKeineAutomatischeMaterialbuchung());

							bMaterialbuchungBeiAblieferung = Helper
									.short2boolean(stklDto
											.getBMaterialbuchungbeiablieferung());

						}

						if (bKeineAutomatischeMaterialbuchung == false) {

							if (bMaterialbuchungBeiAblieferung == false) {

								DelegateFactory
										.getInstance()
										.getFertigungDelegate()
										.bucheMaterialAufLos(
												tpLos.getLosDto(),
												tpLos.getLosDto()
														.getNLosgroesse()
														.subtract(
																DelegateFactory
																		.getInstance()
																		.getFertigungDelegate()
																		.getErledigteMenge(
																				tpLos.getLosDto()
																						.getIId())),
												false,
												false,
												true,
												tpLos.getAbzubuchendeSeriennrChargen(
														tpLos.getLosDto()
																.getIId(),
														tpLos.getLosDto()
																.getNLosgroesse()
																.subtract(
																		DelegateFactory
																				.getInstance()
																				.getFertigungDelegate()
																				.getErledigteMenge(
																						tpLos.getLosDto()
																								.getIId())),
														true));
							}
						}

						setVisible(false);

					} else {
						int iAntwort = DialogFactory
								.showModalJaNeinAbbrechenDialog(
										tpLos.getInternalFrame(),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"fert.losgroesseaendern.waspassiertmitmaterial"),
										LPMain.getInstance()
												.getTextRespectUISPr("lp.frage"));

						if (iAntwort == JOptionPane.YES_OPTION
								|| iAntwort == JOptionPane.NO_OPTION) {
							boolean bUberzaehligesMaterialZureuckbuchen;
							if (iAntwort == JOptionPane.YES_OPTION) {
								bUberzaehligesMaterialZureuckbuchen = true;
							} else {
								bUberzaehligesMaterialZureuckbuchen = false;
							}
							DelegateFactory
									.getInstance()
									.getFertigungDelegate()
									.aendereLosgroesse(
											tpLos.getLosDto().getIId(),
											wnfLosgroesse.getInteger(),
											bUberzaehligesMaterialZureuckbuchen);
							tpLos.getLosDto().setNLosgroesse(
									new BigDecimal(wnfLosgroesse.getInteger()
											.doubleValue()));

						} else {
							// CANCEL

						}

						setVisible(false);
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

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		add(panel1);
		panel1.add(wlaNeueLosgroesse, new GridBagConstraints(0, 0, 2, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wnfLosgroesse, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

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
