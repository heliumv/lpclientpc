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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;

public class DialogRechnungsLieferadresse extends JDialog implements  KeyListener,
MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	public KundeDto kundeDto = null;

	JScrollPane jScrollPane1 = new JScrollPane();
	JTable jTableSnrChnrs = new JTable();
	JButton jButtonUebernehmen = new JButton();
	ArrayList<KundeDto> kunden = null;

	public DialogRechnungsLieferadresse(ArrayList<KundeDto> kunden, String title) {
		super(LPMain.getInstance().getDesktop(), title, true);
		this.kunden = kunden;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(800, 400);
		String[][] data = new String[kunden.size()][1];
		for (int i = 0; i < kunden.size(); i++) {

			KundeDto kdDto = kunden.get(i);

			
			String s=kdDto.getPartnerDto().getCName1nachnamefirmazeile1();
			
			if(kdDto.getPartnerDto().getCName2vornamefirmazeile2()!=null){
				s+=" "+kdDto.getPartnerDto().getCName2vornamefirmazeile2();
			}
			
			if(kdDto.getPartnerDto().getCName2vornamefirmazeile2()!=null){
				s+=" "+kdDto.getPartnerDto().getCName2vornamefirmazeile2();
			}
			
			
			
			if (kdDto.getPartnerDto().getLandplzortDto() != null) {
				
				
				s+= "/ "+kdDto.getPartnerDto().getLandplzortDto()
				.getLandDto().getCLkz()+"-"+kdDto.getPartnerDto().getLandplzortDto().getCPlz()+" "+kdDto.getPartnerDto().getLandplzortDto()
				.getOrtDto().getCName(); 
				
			
			}
			if(kdDto.getPartnerDto().getCKbez()!=null){
				s+="/ "+kdDto.getPartnerDto().getCKbez();
			}
			
			data[i][0] =s;
			
			
			/*data[i][0] = kdDto.getPartnerDto().getCName1nachnamefirmazeile1();
			data[i][1] = kdDto.getPartnerDto().getCName2vornamefirmazeile2();
			data[i][2] = kdDto.getPartnerDto().getCName3vorname2abteilung();
			data[i][3] = kdDto.getPartnerDto().getCKbez();
			if (kdDto.getPartnerDto().getLandplzortDto() != null) {
				data[i][4] = kdDto.getPartnerDto().getLandplzortDto()
						.getLandDto().getCLkz();
				data[i][5] = kdDto.getPartnerDto().getLandplzortDto().getCPlz();
				data[i][6] = kdDto.getPartnerDto().getLandplzortDto()
						.getOrtDto().getCName();
			}*/

		}
		
		String[] colNames = new String[] {
				LPMain.getTextRespectUISPr("lp.kunde")+"/"+LPMain.getTextRespectUISPr("lp.adresse.kbez")/*,"",
				LPMain.getTextRespectUISPr("lp.abteilung"),
				LPMain.getTextRespectUISPr("lp.kurzbez"),
				LPMain.getTextRespectUISPr("lp.lkz1"),
				LPMain.getTextRespectUISPr("lp.plz"),
				LPMain.getTextRespectUISPr("lp.ort")*/};
		
		jTableSnrChnrs = new JTable(data,colNames);
		
		jTableSnrChnrs = new JTable(new MyTableModel2(colNames, data));
		
		jTableSnrChnrs.setRowSelectionAllowed(true);
		
		jTableSnrChnrs.addKeyListener(this);
		jTableSnrChnrs.addMouseListener(this);
		jTableSnrChnrs.setCellSelectionEnabled(false);
		jTableSnrChnrs.setRowSelectionAllowed(true);
		jTableSnrChnrs
		.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		jTableSnrChnrs.repaint();

		jScrollPane.getViewport().add(jTableSnrChnrs);
		
		this.repaint();
		

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen
				.setText(LPMain.getTextRespectUISPr("lp.uebernehmen"));
		jButtonUebernehmen
				.addActionListener(new DialogSnr_jButtonUebernehmen_actionAdapter(
						this));
		add(panel1);
		panel1.add(jScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {
		int[] rows = jTableSnrChnrs.getSelectedRows();

		if (rows.length > 0) {

			kundeDto = kunden.get(rows[0]);
			setVisible(false);
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getClickCount() == 2) {
			if (e.getSource().equals(jTableSnrChnrs)) {
				jButtonUebernehmen_actionPerformed(null);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource().equals(jTableSnrChnrs)) {
				jButtonUebernehmen_actionPerformed(null);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	
	
}

class MyTableModel2 extends AbstractTableModel {
	private String[] columnNames = null;
	private Object[][] data = null;

	public MyTableModel2(String[] columnNames, Object[][] data) {
		this.columnNames = columnNames;
		this.data = data;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {

		return false;
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}
class DialogSnr_jButtonUebernehmen_actionAdapter implements ActionListener {
	private DialogRechnungsLieferadresse adaptee;

	DialogSnr_jButtonUebernehmen_actionAdapter(
			DialogRechnungsLieferadresse adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}

