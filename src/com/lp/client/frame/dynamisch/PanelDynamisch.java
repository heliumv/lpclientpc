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
package com.lp.client.frame.dynamisch;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.IControl;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

public class PanelDynamisch extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PaneldatenDto[] paneldatenDtos = null;
	private PanelQuery panelQueryAuswahl = null;

	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private String paneltyp = "";
	ButtonGroup buttonGroup1 = new ButtonGroup();
	Border border1 = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	private String lockme = null;

	private Component[] komponenten = null;

	private PanelbeschreibungDto[] dtos = null;

	private String[] aWhichButtonIUse = null;

	public PanelDynamisch(InternalFrame internalFrame, String add2TitleI,
			PanelQuery panelQueryAuswahl, String paneltyp, String lockme,
			String[] buttons) throws Throwable {
		super(internalFrame, add2TitleI, panelQueryAuswahl.getSelectedId());
		
		
		if(getInternalFrame() instanceof InternalFrameArtikel){
			 setKeyWhenDetailPanel(((InternalFrameArtikel)getInternalFrame()).getArtikelDto().getIId()+"");
		}
		
		
		this.panelQueryAuswahl = panelQueryAuswahl;
		this.paneltyp = paneltyp;
		this.lockme = lockme;
		this.aWhichButtonIUse = buttons;
		if (aWhichButtonIUse == null) {
			aWhichButtonIUse = new String[0];
		}

		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		try {
			
			String oKey=panelQueryAuswahl.getSelectedId() + "";
			
			if(getInternalFrame() instanceof InternalFrameArtikel){
				 oKey=((InternalFrameArtikel)getInternalFrame()).getArtikelDto().getIId()+"";
			}
			
			
			paneldatenDtos = DelegateFactory
					.getInstance()
					.getPanelDelegate()
					.paneldatenFindByPanelCNrCKey(paneltyp,
							oKey);

			dto2Components();
		} catch (Throwable ex) {
			paneldatenDtos = new PaneldatenDto[dtos.length];
			leereAlleFelder(this);
		}
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

	protected void dto2Components() throws ExceptionLP, Throwable {
		leereAlleFelder(this);
		for (int i = 0; i < dtos.length; i++) {
			for (int j = 0; j < komponenten.length; j++) {
				
				
				String name = komponenten[j].getName();
				if (komponenten[j] instanceof WrapperEditorField) {
					name = ((WrapperEditorField) komponenten[j])
							.getNameFuerEigenschaftsdefinition();
				}
				
				if (dtos[i].getCName().equals(name)) {
					if (dtos[i].getCTyp().equals(PanelFac.TYP_WRAPPERTEXTFIELD)) {
						for (int k = 0; k < paneldatenDtos.length; k++) {
							if (paneldatenDtos[k].getPanelbeschreibungIId()
									.equals(dtos[i].getIId())) {
								((WrapperTextField) komponenten[j])
										.setText(paneldatenDtos[k].getXInhalt());
							}
						}
					} else if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPEREDITOR)) {
						for (int k = 0; k < paneldatenDtos.length; k++) {
							if (paneldatenDtos[k].getPanelbeschreibungIId()
									.equals(dtos[i].getIId())) {
								((WrapperEditorField) komponenten[j])
										.setText(paneldatenDtos[k].getXInhalt());
							}
						}
					} else if (dtos[i].getCTyp().equals(
							PanelFac.TYP_WRAPPERCHECKBOX)) {
						for (int k = 0; k < paneldatenDtos.length; k++) {
							if (paneldatenDtos[k].getPanelbeschreibungIId()
									.equals(dtos[i].getIId())) {
								((WrapperCheckBox) komponenten[j])
										.setShort(new Short(paneldatenDtos[k]
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
										.getTextfield()
										.setText(paneldatenDtos[k].getXInhalt());
							}
						}
					}
				}
			}
		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		if (getInternalFrame() instanceof InternalFrameArtikel) {
			dtos = DelegateFactory
					.getInstance()
					.getPanelDelegate()
					.panelbeschreibungFindByPanelCNrMandantCNr(
							paneltyp,
							((InternalFrameArtikel) getInternalFrame())
									.getArtikelDto().getArtgruIId());
		} else if (getInternalFrame() instanceof InternalFrameKunde) {
			dtos = DelegateFactory
					.getInstance()
					.getPanelDelegate()
					.panelbeschreibungFindByPanelCNrMandantCNrPartnerklasseIId(
							paneltyp,
							((InternalFrameKunde) getInternalFrame())
									.getKundeDto().getPartnerDto().getPartnerklasseIId());
		} else {
			dtos = DelegateFactory.getInstance().getPanelDelegate()
					.panelbeschreibungFindByPanelCNrMandantCNr(paneltyp, null);
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
				printButton.addActionListener(this);
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
				comp = textfield;
			} else if (dto.getCTyp().equals(PanelFac.TYP_WRAPPEREDITOR)) {
				WrapperEditorField textfield = new WrapperEditorField(
						getInternalFrame(), "");
				textfield.setMandatoryField(Helper.short2boolean(dto
						.getBMandatory()));
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

			jPanelWorkingOn.add(comp, new GridBagConstraints(dto.getIGridx()
					.intValue(), dto.getIGridy().intValue(), dto
					.getIGridwidth().intValue(), dto.getIGridheigth()
					.intValue(), dto.getFWeightx().doubleValue(), dto
					.getFWeighty().doubleValue(), anchor, fill, new Insets(dto
					.getIInsetstop().intValue(), dto.getIInsetsleft()
					.intValue(), dto.getIInsetsbottom().intValue(), dto
					.getIInsetsright().intValue()), dto.getIIpadx().intValue(),
					dto.getIIpady().intValue()));

		}

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		
		String oKey=panelQueryAuswahl.getSelectedId() + "";
		
		if(getInternalFrame() instanceof InternalFrameArtikel){
			 oKey=((InternalFrameArtikel)getInternalFrame()).getArtikelDto().getIId()+"";
		}
		
		
		getInternalFrame().showReportKriterien(
				new ReportPanelDynamisch(getInternalFrame(), "", paneltyp, e
						.getActionCommand(), oKey));
	}

	protected String getLockMeWer() throws Exception {
		return lockme;
	}

	protected void setDefaults() throws Throwable {
	}

	protected void components2Dto() throws Throwable {
		paneldatenDtos = new PaneldatenDto[dtos.length];
		String oKey=panelQueryAuswahl.getSelectedId() + "";
		
		
		if(getInternalFrame() instanceof InternalFrameArtikel){
			 oKey=((InternalFrameArtikel)getInternalFrame()).getArtikelDto().getIId()+"";
		}
		
		for (int i = 0; i < dtos.length; i++) {
			paneldatenDtos[i] = new PaneldatenDto();
			paneldatenDtos[i].setPanelCNr(paneltyp);
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
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();
			DelegateFactory.getInstance().getPanelDelegate()
					.createPaneldaten(paneldatenDtos);
			super.eventActionSave(e, true);
			eventYouAreSelected(false);

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
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
			return textfield.getText() != null && !textfield.getText().trim().isEmpty();
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
