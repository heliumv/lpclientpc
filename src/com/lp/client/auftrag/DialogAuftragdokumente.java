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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragauftragdokumentDto;
import com.lp.server.auftrag.service.AuftragdokumentDto;

@SuppressWarnings("static-access")
public class DialogAuftragdokumente extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private WrapperLabel wlaTage = new WrapperLabel();

	private WrapperDateField wnfDatum = new WrapperDateField();

	private ArrayList<AuftragdokumentDto> auftragdokumentDto = null;

	private AuftragdokumentDto[] alleAuftragdokumentDto = null;

	private WrapperCheckBox[] wcbDokumente = null;

	private WrapperCheckBox wcbKeineDokumente = null;

	private Integer auftragIId = null;

	public ArrayList<AuftragdokumentDto> getAuftragdokumentDto() {
		return auftragdokumentDto;
	}

	public DialogAuftragdokumente(Integer auftragIId) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.auftragIId = auftragIId;

		this.setSize(300, 80);
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setTitle(LPMain.getInstance().getTextRespectUISPr("lp.datum"));

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

				auftragdokumentDto = new ArrayList<AuftragdokumentDto>();
				for (int i = 0; i < wcbDokumente.length; i++) {

					if (wcbDokumente[i].isSelected()) {
						auftragdokumentDto.add(alleAuftragdokumentDto[i]);
					}

				}

				
				if(auftragdokumentDto.size()==0 && wcbKeineDokumente.isSelected()==false){
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.info"), LPMain.getInstance()
							.getTextRespectUISPr("auft.keinedokumentebenoetigt.bestaetigen"));
					
					return;
				}
				
				setVisible(false);

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			auftragdokumentDto = null;
			this.setVisible(false);
		} else if (e.getSource().equals(wcbKeineDokumente)) {

			for (int i = 0; i < wcbDokumente.length; i++) {
				wcbDokumente[i].setEnabled(!wcbKeineDokumente.isSelected());

				if (wcbKeineDokumente.isSelected()) {
					wcbDokumente[i].setSelected(false);
				}
			}

		}

	}

	private void jbInit() throws Exception {

		alleAuftragdokumentDto = DelegateFactory.getInstance()
				.getAuftragServiceDelegate().auftragdokumentFindByBVersteckt();
		int iZeile = 0;
		if (alleAuftragdokumentDto != null) {
			this.setSize(400, 120 + ((alleAuftragdokumentDto.length / 3) * 20));
			wcbDokumente = new WrapperCheckBox[alleAuftragdokumentDto.length];

			panel1.setLayout(gridBagLayout1);

			wbuSpeichern.setText(LPMain.getTextRespectUISPr("button.ok"));
			wbuAbbrechen.setText(LPMain.getTextRespectUISPr("Cancel"));

			wnfDatum.setMandatoryField(true);

			wlaTage.setText(LPMain.getInstance()
					.getTextRespectUISPr("lp.datum"));

			wbuSpeichern.addActionListener(this);
			wbuAbbrechen.addActionListener(this);
			

			wcbKeineDokumente = new WrapperCheckBox(LPMain.getInstance()
					.getTextRespectUISPr("auft.keinedokumentebenoetigt"));
			wcbKeineDokumente.addActionListener(this);
			
			add(panel1);

			int iSpalte = 0;

			AuftragauftragdokumentDto[] auftragauftragdokumentDtos = null;

			if (auftragIId != null) {

				auftragauftragdokumentDtos = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.auftragauftragdokumentFindByAuftragIId(auftragIId);
			
			}

			for (int i = 0; i < alleAuftragdokumentDto.length; i++) {

				wcbDokumente[i] = new WrapperCheckBox(alleAuftragdokumentDto[i]
						.getBezeichnung());

				if (auftragauftragdokumentDtos != null
						&& auftragauftragdokumentDtos.length > 0) {

					for (int j = 0; j < auftragauftragdokumentDtos.length; j++) {

						if (alleAuftragdokumentDto[i].getIId().equals(
								auftragauftragdokumentDtos[j]
										.getAuftragdokumentIId())) {
							wcbDokumente[i].setSelected(true);
						}
					}

				}

				panel1.add(wcbDokumente[i],
						new GridBagConstraints(iSpalte, iZeile, 1, 1, 1.0, 1.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 0), 150, 0));

				iSpalte++;
				if (iSpalte == 3) {
					iSpalte = 0;
					iZeile++;
				}
			}
			
			if(auftragIId != null && (auftragauftragdokumentDtos==null || auftragauftragdokumentDtos.length==0)){
				wcbKeineDokumente.setSelected(true);
				actionPerformed(new ActionEvent(wcbKeineDokumente,0,""));
			}
			
			

		}
		iZeile++;

	
		
	

		
		
		panel1.add(wcbKeineDokumente, new GridBagConstraints(0, iZeile, 3, 1,
				1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 0, 0, 0), 0, 0));
		iZeile++;
		panel1.add(wbuSpeichern, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(1, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

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
