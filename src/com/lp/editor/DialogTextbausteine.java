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
package com.lp.editor;

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
import java.math.BigDecimal;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;

@SuppressWarnings("static-access")
public class DialogTextbausteine extends JDialog implements ActionListener,
		KeyListener,MouseListener {

	private static final long serialVersionUID = 1L;

	ArtikelDto artikelDto = null;
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	JScrollPane jScrollPane1 = new JScrollPane();
	JTable jTableSnrChnrs = null;
	JButton jButtonUebernehmen = new JButton();

	public Integer mediastandardIId = null;

	String[] colNames = null;
	public MediastandardDto[] mDtos = null;

	public DialogTextbausteine() throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		setTitle("Auswahl Textbausteine");

		colNames = new String[] { "Bezeichnung", "Datenformat" };

		mDtos = DelegateFactory
				.getInstance()
				.getMediaDelegate()
				.mediastandardFindByDatenformatCNrMandantCNr(
						MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(500, 500);
		
		Object[][] data = null;
		
		if (mDtos != null) {
			data = new Object[mDtos.length][2];
			for (int i = 0; i < mDtos.length; i++) {

				data[i][0] = mDtos[i].getCNr();
				data[i][1] = mDtos[i].getDatenformatCNr();

			}
		} else {
			data = new Object[0][2];
		}
		jTableSnrChnrs = new JTable(new MyTableModel1(colNames, data));

		jTableSnrChnrs.addKeyListener(this);
		jTableSnrChnrs.addMouseListener(this);
		

		jTableSnrChnrs.setRowSelectionAllowed(true);

		jTableSnrChnrs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jScrollPane.getViewport().add(jTableSnrChnrs);

		if (jTableSnrChnrs.getModel().getRowCount() > 0) {
			jTableSnrChnrs.changeSelection(0, 0, false, false);
		}
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		jButtonUebernehmen.grabFocus();
	}



	public void actionPerformed(ActionEvent e) {

	}


	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.uebernehmen"));
		jButtonUebernehmen
				.addActionListener(new DialogTextbausteine_jButtonUebernehmen_actionAdapter(
						this));
		jButtonUebernehmen.setMnemonic('B');

		add(panel1);

		panel1.add(jScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 1, 0, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		SwingUtilities.updateComponentTreeUI(this);
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
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource().equals(jTableSnrChnrs)) {
				jButtonUebernehmen_actionPerformed(null);
			}
		}

	
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {
		if (jTableSnrChnrs.getSelectedRow() > -1) {
			mediastandardIId = mDtos[jTableSnrChnrs.getSelectedRow()].getIId();
		}
		setVisible(false);
	}

	public class ButtonAbstractAction extends AbstractAction {
		private static final long serialVersionUID = -8681526632257782909L;
		private DialogTextbausteine adaptee;
		private String sActionCommand = null;

		public ButtonAbstractAction(DialogTextbausteine adaptee,
				String sActionCommandI) {
			this.adaptee = adaptee;
			sActionCommand = sActionCommandI;
		}

		public void actionPerformed(java.awt.event.ActionEvent e) {
			ActionEvent ae = new ActionEvent(this, 1, sActionCommand);
			adaptee.actionPerformed(ae);
		}

		public void setSActionCommand(String sActionCommand) {

			this.sActionCommand = sActionCommand;
		}

		public String getSActionCommand() {
			return sActionCommand;
		}
	}

}

class DialogTextbausteine_jButtonUebernehmen_actionAdapter implements
		ActionListener {
	private DialogTextbausteine adaptee;

	DialogTextbausteine_jButtonUebernehmen_actionAdapter(
			DialogTextbausteine adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}

class MyTableModel1 extends AbstractTableModel {
	private static final long serialVersionUID = 1458184566910151330L;
	private String[] columnNames = null;
	private Object[][] data = null;

	public MyTableModel1(String[] columnNames, Object[][] data) {
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

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col < 2) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
