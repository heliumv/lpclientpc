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
package com.lp.client.finanz;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import javax.swing.JDialog;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FibuExportFac;

public class DialogBuchungsjournalExport extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = -7236071859832945050L;

	private WrapperComboBox format;
	private WrapperDateField von;
	private WrapperDateField bis;
	private WrapperCheckBox mitAutoEB;
	private WrapperCheckBox mitAutoB;
	private WrapperCheckBox mitManEB;
	private WrapperCheckBox mitStornierte;
	private WrapperTextField bezeichnung;
	private WrapperButton exportieren;
	
	public DialogBuchungsjournalExport(Frame owner) {
		super(owner, LPMain.getTextRespectUISPr("fb.buchungsjournal.export"));
		setSize(new Dimension(Defaults.getInstance().bySizeFactor(300), Defaults.getInstance().bySizeFactor(250)));
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		jbInit();
	}
	
	private void jbInit() {
		format = new WrapperComboBox(new Object[]{FibuExportFac.DATEV, FibuExportFac.HV_RAW, FibuExportFac.RZL_CSV});
		von = new WrapperDateField();
		von.setMandatoryField(true);
		bis = new WrapperDateField();
		bis.setMandatoryField(true);
		
		mitAutoEB = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export.automatischeeroeffnungsbuchungen"));
		mitAutoEB.setSelected(true);
		mitAutoB = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export.automatikbuchungen"));
		mitAutoB.setSelected(true);
		mitManEB = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export.manuelleeroeffnungsbuchungen"));
		mitManEB.setSelected(true);
		mitStornierte = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.plusstornierte"));
		mitStornierte.setSelected(true);
//		mitStornierte.setEnabled(false);
		bezeichnung = new WrapperTextField(30);
		exportieren = new WrapperButton(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export.exportieren"));
		exportieren.addActionListener(this);

		format.setLightWeightPopupEnabled(false);
		format.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(FibuExportFac.DATEV.equals(format.getSelectedItem())) {
					mitStornierte.setSelected(true);
//					mitStornierte.setEnabled(false);
				} else {
					mitStornierte.setEnabled(true);
				}
			}
		});
		
		Container c = getContentPane();
		c.setLayout(new MigLayout("wrap 2", "[40%,fill|60%,fill]"));
//		c.setPreferredSize(new Dimension(3000, 2000));
		
		c.add(format, "span");
		
		c.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.von")));
		c.add(von);
		
		c.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.bis")));
		c.add(bis);
		
		c.add(mitAutoEB, "span");
		
		c.add(mitAutoB, "span");
		
		c.add(mitManEB, "span");
		
		c.add(mitStornierte, "span");
		
		c.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.bezeichnung")));
		c.add(bezeichnung);
		
		c.add(exportieren, "span");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() != exportieren) return ;

		try {
			if(von.hasContent() && bis.hasContent()) {
				List<String> zeilen = DelegateFactory.getInstance().getFibuExportDelegate().exportiereBuchungsjournal(format.getSelectedItem().toString(),
						von.getDate(), bis.getDate(),
						mitAutoEB.isSelected(), mitManEB.isSelected(), mitAutoB.isSelected(), mitStornierte.isSelected(), bezeichnung.getText());
				File temp = File.createTempFile("buchungsjournalexport", "csv");
				BufferedWriter fw = new BufferedWriter(new FileWriter(temp));
				for (String zeile : zeilen) {
					fw.write(zeile);
					fw.newLine();
				}
				fw.close();
				HelperClient.showSaveFileDialog(temp, new File(getFilename()), this, ".csv");
				setVisible(false);
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));				
			}
		} catch (Throwable t) {
			handleOwnException(t);
		}
	}

	public void handleOwnException(Throwable t) {
		new DialogError(LPMain.getInstance().getDesktop(), t,
				DialogError.TYPE_INFORMATION);
	}
	
	private String getFilename() {
		String filename = "";
		
		if(FibuExportFac.RZL_CSV.equals(format.getSelectedItem().toString())) {
			filename += FibuExportFac.RZL_CSV_FILENAME_PREFIX;
		}
		filename += bezeichnung.getText() + ".csv";
		
		return filename;
	}
}
