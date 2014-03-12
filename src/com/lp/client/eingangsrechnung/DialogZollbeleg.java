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
package com.lp.client.eingangsrechnung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;

@SuppressWarnings("static-access")
public class DialogZollbeleg extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	private WrapperLabel wlaZollbelegnummer = new WrapperLabel();
	private WrapperTextField wtfZollbelegnummer = new WrapperTextField();

	private WrapperSelectField wsfEingangsrechnung = null;
	private Integer eingangsrechnungIId = null;

	private TabbedPaneEingangsrechnung tabbedpaneEingangsrechnung = null;

	public DialogZollbeleg(Integer eingangsrechnungIId,
			TabbedPaneEingangsrechnung internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain
				.getTextRespectUISPr("er.eingangsrechnung.frage.zollimportpapiere"), true);
		this.tabbedpaneEingangsrechnung = internalFrame;
		this.eingangsrechnungIId = eingangsrechnungIId;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(400, 200);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAbbrechen)) {

			this.setVisible(false);
		} else if (e.getSource().equals(btnOK)) {

			
			if(wtfZollbelegnummer.getText()==null){
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
				return;
			}
			
			
			if(wsfEingangsrechnung.getIKey()!=null && wsfEingangsrechnung.getIKey().equals(tabbedpaneEingangsrechnung.getEingangsrechnungDto()
										.getIId())){
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("er.zollimport.gleicheer"));
				return;
			}
			
			try {
				
				
				
				DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.toggleZollimportpapiereErhalten(
								tabbedpaneEingangsrechnung.getEingangsrechnungDto()
										.getIId(), wtfZollbelegnummer.getText(),wsfEingangsrechnung.getIKey());
				
				

				this.setVisible(false);
			} catch (Throwable e1) {
				tabbedpaneEingangsrechnung.handleException(e1, true);
			}

		} else {
			this.setVisible(false);
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));
		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		wlaZollbelegnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.zollbelegnummer"));

		wsfEingangsrechnung = new WrapperSelectField(WrapperSelectField.EINGANGSRECHNUNGEN,
				tabbedpaneEingangsrechnung.getInternalFrame(), true);

		EingangsrechnungDto reDto = DelegateFactory.getInstance()
				.getEingangsrechnungDelegate()
				.eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);

		wsfEingangsrechnung.setKey(reDto.getMahnstufeIId());
		wtfZollbelegnummer.setMandatoryField(true);

		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;

		iZeile++;
		this.getContentPane().add(
				wlaZollbelegnummer,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wtfZollbelegnummer,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;
		this.getContentPane().add(
				wsfEingangsrechnung.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wsfEingangsrechnung.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;

		this.getContentPane().add(
				btnOK,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				btnAbbrechen,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
