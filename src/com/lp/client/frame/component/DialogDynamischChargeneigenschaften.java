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
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.util.Facade;

public class DialogDynamischChargeneigenschaften extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArtikelDto artikelDto = null;
	JPanel panel0 = new JPanel();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JButton jButtonUebernehmen = new JButton();
	InternalFrame internalFrame = null;
	public boolean bAbbruch = false;
	public PaneldatenDto[] paneldatenDtos = null;
	Integer i_id_buchung = null;
	public SeriennrChargennrMitMengeDto dto = null;
	PanelDynamischHelper phd = null;

	public DialogDynamischChargeneigenschaften(ArtikelDto artikelDto,
			InternalFrame internalFrame, SeriennrChargennrMitMengeDto dto)
			throws Throwable {

		super(LPMain.getInstance().getDesktop(), LPMain
				.getTextRespectUISPr("artikel.chargeninformatinen.label")
				+ dto.getCSeriennrChargennr(), true);
		this.artikelDto = artikelDto;
		this.internalFrame = internalFrame;
		this.setSize(750, 600);
		this.dto = dto;

		jbInit();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				bAbbruch = true;
				setVisible(false);
				dispose();
			}
		});

		paneldatenDtos = dto.getPaneldatenDtos();
		dto2Components();

	}

	public void actionPerformed(ActionEvent e) {

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		phd.dto2Components(paneldatenDtos);

	}

	protected void components2Dto() throws Throwable {
		paneldatenDtos = phd.components2Dto(i_id_buchung + "");
	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen
				.setText(LPMain.getTextRespectUISPr("lp.uebernehmen"));
		jButtonUebernehmen
				.addActionListener(new DialogDynamisch_jButtonUebernehmen_actionAdapter(
						this));
		jButtonUebernehmen.setMnemonic('B');

		setLayout(new GridBagLayout());

		phd = new PanelDynamischHelper(PanelFac.PANEL_CHARGENEIGENSCHAFTEN, artikelDto.getArtgruIId(),
				internalFrame);
		
		add(phd.panelErzeugen(), new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		add(panel2, new GridBagConstraints(0, 2, 0, 0, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel2.add(jButtonUebernehmen);

		

		

	}

	public boolean allMandatoryFieldsSetDlg() throws Throwable {
		boolean bOKSave = true;
		if (!allMandatorySet(this)) {
			showDialogPflichtfelderAusfuellen();
			bOKSave = false;
		}
		return bOKSave;
	}

	public void showDialogPflichtfelderAusfuellen() {
		DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
				LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
	}

	private boolean allMandatorySet(Component component) throws Throwable {

		if (component instanceof IControl) {
			IControl c = (IControl) component;
			if (c.isMandatoryField() && !c.hasContent())
				return false;

		} else if (component instanceof java.awt.Container
				&& component.isVisible()) {
			Container c = (Container) component;
			for (Component child : c.getComponents()) {
				if (!allMandatorySet(child))
					return false;
			}
		} else if (component instanceof javax.swing.JScrollPane) {
			// Komponenten fuer TextAreas und Tabellen auf Scrollpanes
			// ermitteln
			JScrollPane jScrollPane = (JScrollPane) component;
			if (!allMandatorySet(jScrollPane.getViewport().getView())) {
				return false;
			}
		}
		// alle Pflichtfelder sind gefuellt
		return true;
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {

		try {
			if (allMandatoryFieldsSetDlg()) {
				// Eigenschaften
				components2Dto();
				dto.setPaneldatenDtos(paneldatenDtos);
			} else {
				return;
			}
		} catch (Throwable e1) {
			internalFrame.handleException(e1, false);
		}

		setVisible(false);
	}

	class DialogDynamisch_jButtonUebernehmen_actionAdapter implements
			ActionListener {
		private DialogDynamischChargeneigenschaften adaptee;

		DialogDynamisch_jButtonUebernehmen_actionAdapter(
				DialogDynamischChargeneigenschaften adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.jButtonUebernehmen_actionPerformed(e);
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
