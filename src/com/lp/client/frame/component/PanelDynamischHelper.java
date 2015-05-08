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
 *******************************************************************************/
package com.lp.client.frame.component;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogDynamischChargeneigenschaften.ExecButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

public class PanelDynamischHelper {

	private PanelbeschreibungDto[] dtos = null;

	private Integer artgruIId = null;
	private String panelCNr = null;
	private InternalFrame internalFrame = null;
	private Component[] komponenten = null;

	public PanelDynamischHelper(String panelCNr, Integer artgruIId,
			InternalFrame internalFrame) throws Throwable {
		this.panelCNr = panelCNr;
		this.artgruIId = artgruIId;
		this.internalFrame = internalFrame;

	}

	public void enablePrintButton() {
		for (int i = 0; i < dtos.length; i++) {
			for (int j = 0; j < komponenten.length; j++) {
				if (dtos[i].getCName().equals(komponenten[j].getName())) {
					if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPERPRINTBUTTON)) {
						komponenten[j].setEnabled(!komponenten[j].isEnabled());
					}
				}
			}
		}
	}

	public void registerPrintButtonActionListener(ActionListener listener) {
		for (int i = 0; i < dtos.length; i++) {
			for (int j = 0; j < komponenten.length; j++) {
				if (dtos[i].getCName().equals(komponenten[j].getName())) {
					if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPERPRINTBUTTON)) {
						if (komponenten[j] instanceof WrapperButton) {
							((WrapperButton) komponenten[j])
									.addActionListener(listener);
						}
					}
				}
			}
		}
	}

	public JPanel panelErzeugen() throws Throwable {

		JPanel panelToAdd = new JPanel();

		panelToAdd.setLayout(new GridBagLayout());
		boolean bWrapToScrollpane = false;

		if (internalFrame instanceof InternalFrameArtikel
				&& panelCNr.equals(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN)) {
			dtos = DelegateFactory
					.getInstance()
					.getPanelDelegate()
					.panelbeschreibungFindByPanelCNrMandantCNr(
							panelCNr,
							((InternalFrameArtikel) internalFrame)
									.getArtikelDto().getArtgruIId());
		} else if (internalFrame instanceof InternalFrameKunde) {
			dtos = DelegateFactory
					.getInstance()
					.getPanelDelegate()
					.panelbeschreibungFindByPanelCNrMandantCNrPartnerklasseIId(
							panelCNr,
							((InternalFrameKunde) internalFrame).getKundeDto()
									.getPartnerDto().getPartnerklasseIId());
		} else {
			dtos = DelegateFactory
					.getInstance()
					.getPanelDelegate()
					.panelbeschreibungFindByPanelCNrMandantCNr(panelCNr,
							artgruIId);
		}

		Component comp = null;

		komponenten = new Component[dtos.length];

		for (int i = 0; i < dtos.length; i++) {
			PanelbeschreibungDto dto = dtos[i];

			if (dto.getCTyp().equals(PanelFac.TYP_WRAPPERLABEL)) {
				WrapperLabel label = new WrapperLabel();
				String text = "";

				try {
					text = ResourceBundle.getBundle(
							LPMain.RESOURCE_BUNDEL_ALLG,
							LPMain.getInstance().getUISprLocale()).getString(
							dto.getCTokeninresourcebundle());

				} catch (Exception ex) {
					text = dto.getCTokeninresourcebundle();
				}

				label.setText(text);
				comp = label;

			} else if (dto.getCTyp().equals(PanelFac.TYP_WRAPPERCOMBOBOX)) {
				WrapperComboBox label = new WrapperComboBox();
				Map<String, String> m = new LinkedHashMap<String, String>();
				StringTokenizer token = new StringTokenizer(
						dto.getCTokeninresourcebundle(), "|");
				while (token.hasMoreTokens()) {
					String wert = token.nextToken();
					m.put(wert, wert);
				}

				try {

				} catch (Exception ex) {
				}
				label.setMandatoryField(Helper.short2boolean(dto
						.getBMandatory()));
				label.setMap(m);

				if (dto.getCDefault() != null) {
					label.setKeyOfSelectedItem(dto.getCDefault());
				}

				comp = label;

			} else if (dto.getCTyp().equals(PanelFac.TYP_WRAPPERPRINTBUTTON)) {
				WrapperButton printButton = new WrapperButton();
				String text = "";

				try {
					text = ResourceBundle.getBundle(
							LPMain.RESOURCE_BUNDEL_ALLG,
							LPMain.getInstance().getUISprLocale()).getString(
							dto.getCTokeninresourcebundle());

				} catch (Exception ex) {
					text = dto.getCTokeninresourcebundle();
				}

				printButton.setToolTipText(dto.getCName());
				printButton.setActionCommand(dto.getCName());
				printButton.setText(text);
				comp = printButton;

			} else if (dto.getCTyp().equals(PanelFac.TYP_WRAPPEREXECBUTTON)) {

				ExecButton execButton = new ExecButton(dto.getCName());

				String text = "";

				try {
					text = ResourceBundle.getBundle(
							LPMain.RESOURCE_BUNDEL_ALLG,
							LPMain.getInstance().getUISprLocale()).getString(
							dto.getCTokeninresourcebundle());

				} catch (Exception ex) {
					text = dto.getCTokeninresourcebundle();
				}
				execButton.getButton().setToolTipText(dto.getCName());
				execButton.getButton().setText(text);
				comp = execButton;

			} else if (dto.getCTyp().equals(PanelFac.TYP_WRAPPERTEXTFIELD)) {
				WrapperTextField textfield = new WrapperTextField();
				textfield.setMandatoryField(Helper.short2boolean(dto
						.getBMandatory()));

				textfield.setText(dto.getCDefault());

				comp = textfield;
			} else if (dto.getCTyp().equals(PanelFac.TYP_WRAPPERTEXTAREA)) {
				WrapperTextArea textarea = new WrapperTextArea();
				textarea.setMandatoryField(Helper.short2boolean(dto
						.getBMandatory()));

				textarea.setText(dto.getCDefault());

				bWrapToScrollpane = true;

				comp = textarea;
			} else if (dto.getCTyp().equals(PanelFac.TYP_WRAPPEREDITOR)) {
				WrapperEditorField textfield = new WrapperEditorField(
						internalFrame, "");
				textfield.setMandatoryField(Helper.short2boolean(dto
						.getBMandatory()));
				textfield.setText(dto.getCDefault());
				textfield.setNameFuerEigenschaftsdefinition(dto.getCName());
				comp = textfield;
			} else if (dto.getCTyp().equals(PanelFac.TYP_WRAPPERCHECKBOX)) {
				WrapperCheckBox textfield = new WrapperCheckBox();

				String text = "";

				try {
					text = ResourceBundle.getBundle(
							LPMain.RESOURCE_BUNDEL_ALLG,
							LPMain.getInstance().getUISprLocale()).getString(
							dto.getCTokeninresourcebundle());

				} catch (Exception ex) {
					text = dto.getCTokeninresourcebundle();
				}
				textfield.setText(text);
				if (dto.getCDefault() != null && dto.getCDefault().equals("1")) {
					textfield.setSelected(true);
				}

				comp = textfield;
			}
			comp.setName(dto.getCName());

			int anchor = -1;
			if (dto.getCAnchor().equals(PanelFac.ANCHOR_CENTER)) {
				anchor = GridBagConstraints.CENTER;
			} else if (dto.getCAnchor().equals(PanelFac.ANCHOR_EAST)) {
				anchor = GridBagConstraints.EAST;
			} else if (dto.getCAnchor().equals(PanelFac.ANCHOR_NORTH)) {
				anchor = GridBagConstraints.NORTH;
			} else if (dto.getCAnchor().equals(PanelFac.ANCHOR_NORTHEAST)) {
				anchor = GridBagConstraints.NORTHEAST;
			} else if (dto.getCAnchor().equals(PanelFac.ANCHOR_NORTHWEST)) {
				anchor = GridBagConstraints.NORTHWEST;
			} else if (dto.getCAnchor().equals(PanelFac.ANCHOR_SOUTH)) {
				anchor = GridBagConstraints.SOUTH;
			} else if (dto.getCAnchor().equals(PanelFac.ANCHOR_SOUTHEAST)) {
				anchor = GridBagConstraints.SOUTHEAST;
			} else if (dto.getCAnchor().equals(PanelFac.ANCHOR_SOUTHWEST)) {
				anchor = GridBagConstraints.SOUTHWEST;
			} else if (dto.getCAnchor().equals(PanelFac.ANCHOR_WEST)) {
				anchor = GridBagConstraints.WEST;
			} else {
				// UNGUELITGER WERT
			}

			int fill = -1;
			if (dto.getCFill().equals(PanelFac.FILL_BOTH)) {
				fill = GridBagConstraints.BOTH;
			} else if (dto.getCFill().equals(PanelFac.FILL_HORIZONTAL)) {
				fill = GridBagConstraints.HORIZONTAL;
			} else if (dto.getCFill().equals(PanelFac.FILL_VERTICAL)) {
				fill = GridBagConstraints.VERTICAL;
			} else if (dto.getCFill().equals(PanelFac.FILL_NONE)) {
				fill = GridBagConstraints.NONE;
			}

			komponenten[i] = comp;

			if (bWrapToScrollpane) {
				comp = new JScrollPane(comp);
			}

			panelToAdd.add(comp, new GridBagConstraints(dto.getIGridx()
					.intValue(), dto.getIGridy().intValue(), dto
					.getIGridwidth().intValue(), dto.getIGridheigth()
					.intValue(), dto.getFWeightx().doubleValue(), dto
					.getFWeighty().doubleValue(), anchor, fill, new Insets(dto
					.getIInsetstop().intValue(), dto.getIInsetsleft()
					.intValue(), dto.getIInsetsbottom().intValue(), dto
					.getIInsetsright().intValue()), dto.getIIpadx().intValue(),
					dto.getIIpady().intValue()));

		}

		return panelToAdd;

	}

	public PaneldatenDto[] components2Dto(String oKey) throws Throwable {
		PaneldatenDto[] paneldatenDtos = new PaneldatenDto[dtos.length];

		if (internalFrame instanceof InternalFrameArtikel
				&& panelCNr.equals(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN)) {
			oKey = ((InternalFrameArtikel) internalFrame).getArtikelDto()
					.getIId() + "";
		}

		for (int i = 0; i < dtos.length; i++) {
			paneldatenDtos[i] = new PaneldatenDto();
			paneldatenDtos[i].setPanelCNr(panelCNr);
			paneldatenDtos[i].setCKey(oKey);
			for (int j = 0; j < komponenten.length; j++) {

				String name = komponenten[j].getName();
				if (komponenten[j] instanceof WrapperEditorField) {
					name = ((WrapperEditorField) komponenten[j])
							.getNameFuerEigenschaftsdefinition();
				}

				if (dtos[i].getCName().equals(name)) {
					if (dtos[i].getCTyp().equals(PanelFac.TYP_WRAPPERTEXTFIELD)) {
						if (((WrapperTextField) komponenten[j]).getText() != null) {
							paneldatenDtos[i]
									.setXInhalt(((WrapperTextField) komponenten[j])
											.getText());
						} else {
							paneldatenDtos[i].setXInhalt("");
						}
						paneldatenDtos[i].setPanelbeschreibungIId(dtos[i]
								.getIId());
						// SK: Wieso hier Integer?
						paneldatenDtos[i].setCDatentypkey("java.lang.Integer");

					} else if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPEREDITOR)) {

						if (((WrapperEditorField) komponenten[j]).getText() != null) {
							paneldatenDtos[i]
									.setXInhalt(((WrapperEditorField) komponenten[j])
											.getText());
						} else {
							paneldatenDtos[i].setXInhalt("");
						}

						paneldatenDtos[i].setPanelbeschreibungIId(dtos[i]
								.getIId());
						paneldatenDtos[i].setCDatentypkey("java.lang.String");
					}else if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPERTEXTAREA)) {

						if (((WrapperTextArea) komponenten[j]).getText() != null) {
							paneldatenDtos[i]
									.setXInhalt(((WrapperTextArea) komponenten[j])
											.getText());
						} else {
							paneldatenDtos[i].setXInhalt("");
						}

						paneldatenDtos[i].setPanelbeschreibungIId(dtos[i]
								.getIId());
						paneldatenDtos[i].setCDatentypkey("java.lang.String");
					} else if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPEREXECBUTTON)) {

						if (((ExecButton) komponenten[j]).getTextfield()
								.getText() != null) {
							paneldatenDtos[i]
									.setXInhalt(((ExecButton) komponenten[j])
											.getTextfield().getText());
						} else {
							paneldatenDtos[i].setXInhalt("");
						}

						paneldatenDtos[i].setPanelbeschreibungIId(dtos[i]
								.getIId());
						paneldatenDtos[i].setCDatentypkey("java.lang.String");
					} else if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPERCHECKBOX)) {
						paneldatenDtos[i]
								.setXInhalt((((WrapperCheckBox) komponenten[j])
										.getShort() + ""));
						paneldatenDtos[i].setPanelbeschreibungIId(dtos[i]
								.getIId());
						paneldatenDtos[i].setCDatentypkey("java.lang.Short");
					} else if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPERCOMBOBOX)) {
						paneldatenDtos[i]
								.setXInhalt((((WrapperComboBox) komponenten[j])
										.getKeyOfSelectedItem() + ""));
						paneldatenDtos[i].setPanelbeschreibungIId(dtos[i]
								.getIId());
						paneldatenDtos[i].setCDatentypkey("java.lang.String");
					}
				}
			}
		}
		return paneldatenDtos;
	}

	public void dto2Components(String oKey) throws ExceptionLP, Throwable {
		PaneldatenDto[] paneldatenDtos = DelegateFactory.getInstance()
				.getPanelDelegate()
				.paneldatenFindByPanelCNrCKey(panelCNr, oKey);
		dto2Components(paneldatenDtos);
	}

	protected void dto2Components(PaneldatenDto[] paneldatenDtos)
			throws ExceptionLP, Throwable {

		if (paneldatenDtos != null) {
			for (int i = 0; i < dtos.length; i++) {
				for (int j = 0; j < komponenten.length; j++) {

					String name = komponenten[j].getName();
					if (komponenten[j] instanceof WrapperEditorField) {
						name = ((WrapperEditorField) komponenten[j])
								.getNameFuerEigenschaftsdefinition();
					}

					if (dtos[i].getCName().equals(name)) {
						if (dtos[i].getCTyp().equals(
								PanelFac.TYP_WRAPPERTEXTFIELD)) {
							for (int k = 0; k < paneldatenDtos.length; k++) {
								if (paneldatenDtos[k].getPanelbeschreibungIId()
										.equals(dtos[i].getIId())) {
									((WrapperTextField) komponenten[j])
											.setText(paneldatenDtos[k]
													.getXInhalt());
								}
							}
						} else if (dtos[i].getCTyp().equals(
								PanelFac.TYP_WRAPPERTEXTAREA)) {
							for (int k = 0; k < paneldatenDtos.length; k++) {
								if (paneldatenDtos[k].getPanelbeschreibungIId()
										.equals(dtos[i].getIId())) {
									((WrapperTextArea) komponenten[j])
											.setText(paneldatenDtos[k]
													.getXInhalt());
								}
							}
						} else if (dtos[i].getCTyp().equals(
								PanelFac.TYP_WRAPPEREDITOR)) {
							for (int k = 0; k < paneldatenDtos.length; k++) {
								if (paneldatenDtos[k].getPanelbeschreibungIId()
										.equals(dtos[i].getIId())) {
									((WrapperEditorField) komponenten[j])
											.setText(paneldatenDtos[k]
													.getXInhalt());
								}
							}
						} else if (dtos[i].getCTyp().equals(
								PanelFac.TYP_WRAPPERCHECKBOX)) {
							for (int k = 0; k < paneldatenDtos.length; k++) {
								if (paneldatenDtos[k].getPanelbeschreibungIId()
										.equals(dtos[i].getIId())) {
									((WrapperCheckBox) komponenten[j])
											.setShort(new Short(
													paneldatenDtos[k]
															.getXInhalt()));
								}
							}
						} else if (dtos[i].getCTyp().equals(
								PanelFac.TYP_WRAPPERCOMBOBOX)) {
							for (int k = 0; k < paneldatenDtos.length; k++) {
								if (paneldatenDtos[k].getPanelbeschreibungIId()
										.equals(dtos[i].getIId())) {
									((WrapperComboBox) komponenten[j])
											.setKeyOfSelectedItem(paneldatenDtos[k]
													.getXInhalt());
								}
							}
						} else if (dtos[i].getCTyp().equals(
								PanelFac.TYP_WRAPPEREXECBUTTON)) {
							for (int k = 0; k < paneldatenDtos.length; k++) {
								if (paneldatenDtos[k].getPanelbeschreibungIId()
										.equals(dtos[i].getIId())) {
									((ExecButton) komponenten[j])
											.getTextfield().setText(
													paneldatenDtos[k]
															.getXInhalt());
								}
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	public class ExecButton extends JPanel implements IControl

	{
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		private boolean bMandantory = false;
		private String command = "";
		private WrapperButton button = new WrapperButton();
		private WrapperTextField textfield = new WrapperTextField();

		public ExecButton(String command) throws Throwable {
			this.command = command;
			jbInit();
		}

		private void jbInit() throws Exception {
			this.setLayout(new GridBagLayout());
			button.setText("jB1");
			textfield.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
			button.addActionListener(new Panel1_jButton1_actionAdapter(this));
			this.add(button, new GridBagConstraints(0, 0, 1, 1, 1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 0), 0, 0));
			this.add(textfield, new GridBagConstraints(1, 0, 1, 1, 1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 0), 0, 0));
		}

		public WrapperTextField getTextfield() {
			return textfield;
		}

		public WrapperButton getButton() {
			return button;
		}

		public void jButton1_actionPerformed(ActionEvent e) {
			try {
				String line;
				String param = "";
				if (getTextfield().getText() != null) {
					param = getTextfield().getText();
				}

				Process p = Runtime.getRuntime().exec(command + " " + param);
				BufferedReader input = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					System.out.println(line);
				}
				input.close();
			} catch (Exception err) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), err.getMessage());
			}

		}

		public boolean isMandatoryField() {
			return bMandantory;
		}

		public void setMandatoryField(boolean isMandatoryField) {
			bMandantory = isMandatoryField;
		}

		public void setEditable(boolean bEnabled) {
			textfield.setEditable(bEnabled);
			button.setEnabled(true);
		}

		public void setEnabled(boolean bEnabled) {
			textfield.setEditable(bEnabled);
			button.setEnabled(true);
		}

		public boolean isActivatable() {
			return true;
		}

		@Override
		public boolean hasContent() throws Throwable {
			return textfield.getText() != null
					&& !textfield.getText().trim().isEmpty();
		}

		public void setActivatable(boolean isActivatable) {

		}

		public void removeContent() {
			textfield.setText("");
		}

		class Panel1_jButton1_actionAdapter implements ActionListener {
			private ExecButton adaptee;

			Panel1_jButton1_actionAdapter(ExecButton adaptee) {
				this.adaptee = adaptee;
			}

			public void actionPerformed(ActionEvent e) {
				adaptee.jButton1_actionPerformed(e);
			}
		}

	}
}
