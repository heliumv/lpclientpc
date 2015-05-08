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
package com.lp.client.frame.component;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.lp.client.frame.NumberColumnFormat;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogSnrChnrauswahl extends JDialog implements KeyListener,
		MouseListener {

	private static final long serialVersionUID = 1L;
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	public String[] sSeriennrChargennrArray = new String[0];
	public List<SeriennrChargennrMitMengeDto> alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>();
	public boolean bSnrBehaftet = false;
	JScrollPane jScrollPane1 = new JScrollPane();
	JTable jTableSnrChnrs = new JTable();
	JButton jButtonUebernehmen = new JButton();

	public DialogSnrChnrauswahl(Integer artikelIId, Integer lagerIId,
			boolean bMultiselection,
			List<SeriennrChargennrMitMengeDto> alSeriennummernBereitsSelektiert)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		SeriennrChargennrAufLagerDto[] dtos = DelegateFactory.getInstance()
				.getLagerDelegate()
				.getAllSerienChargennrAufLager(artikelIId, lagerIId, true);
		ArtikelDto artikelDto = DelegateFactory.getInstance()
				.getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);
		setTitle("Auswahl Seriennummern f\u00FCr Artikel "
				+ artikelDto.formatArtikelbezeichnung());
		bSnrBehaftet = Helper.short2boolean(artikelDto.getBSeriennrtragend());

		ArrayList<SeriennrChargennrAufLagerDto> alListeMitSelektiertenBereinigt = new ArrayList<SeriennrChargennrAufLagerDto>();

		for (int i = 0; i < dtos.length; i++) {

			SeriennrChargennrAufLagerDto dtoZeile = dtos[i];

			boolean bGefunden = false;
			// Bereits selektierte rausfiltern
			if (alSeriennummernBereitsSelektiert != null) {
				for (int j = 0; j < alSeriennummernBereitsSelektiert.size(); j++) {

					if (dtoZeile.getCSeriennrChargennr().equals(
							alSeriennummernBereitsSelektiert.get(j)
									.getCSeriennrChargennr())) {
						bGefunden = true;
						BigDecimal mengeNeu = dtoZeile.getNMenge().subtract(
								alSeriennummernBereitsSelektiert.get(j)
										.getNMenge());
						dtoZeile.setNMenge(mengeNeu);
						if (mengeNeu.doubleValue() > 0) {
							alListeMitSelektiertenBereinigt.add(dtoZeile);
						}
					}

				}
			}
			if (bGefunden == false) {
				alListeMitSelektiertenBereinigt.add(dtoZeile);
			}
		}

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(400, 400);
		Object[][] data = null;
		String[] colNames = null;
		if (bSnrBehaftet) {
			colNames = new String[] {
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.seriennummer"),
					LPMain.getInstance().getTextRespectUISPr("artikel.lager.version") };
			data = new String[alListeMitSelektiertenBereinigt.size()][2];
			for (int i = 0; i < alListeMitSelektiertenBereinigt.size(); i++) {
				if (alListeMitSelektiertenBereinigt.get(i) != null) {
					data[i][0] = alListeMitSelektiertenBereinigt.get(i)
							.getCSeriennrChargennr();
					data[i][1] = alListeMitSelektiertenBereinigt.get(i)
							.getCVersion();
				}
			}
		} else {
			colNames = new String[] {
					LPMain.getInstance().getTextRespectUISPr(
							"lp.chargennummer_lang"),
					LPMain.getInstance().getTextRespectUISPr("lp.menge") };
			data = new Object[alListeMitSelektiertenBereinigt.size()][2];
			for (int i = 0; i < alListeMitSelektiertenBereinigt.size(); i++) {
				if (alListeMitSelektiertenBereinigt.get(i) != null) {
					data[i][0] = alListeMitSelektiertenBereinigt.get(i)
							.getCSeriennrChargennr();
					data[i][1] = alListeMitSelektiertenBereinigt.get(i)
							.getNMenge();
				}
			}

		}

		jTableSnrChnrs = new JTable(new MyTableModel(colNames, data));
		jTableSnrChnrs.addKeyListener(this);
		jTableSnrChnrs.addMouseListener(this);
		jTableSnrChnrs.setRowSelectionAllowed(true);

		if (!bSnrBehaftet) {

			NumberColumnFormat numberCF2 = new NumberColumnFormat(
					"###,###,###.####");
			TableColumn tc = jTableSnrChnrs.getColumnModel().getColumn(1);
			tc.setCellRenderer(numberCF2.getRenderer());
			tc.setCellEditor(numberCF2.getEditor());
		}

		if (bMultiselection) {
			jTableSnrChnrs
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			jTableSnrChnrs
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		jScrollPane.getViewport().add(jTableSnrChnrs);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.uebernehmen"));
		jButtonUebernehmen
				.addActionListener(new DialogSnrChnr_jButtonUebernehmen_actionAdapter(
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
		sSeriennrChargennrArray = new String[rows.length];
		for (int i = 0; i < rows.length; i++) {
			sSeriennrChargennrArray[i] = (String) jTableSnrChnrs.getModel()
					.getValueAt(rows[i], 0);

			if (bSnrBehaftet) {

				SeriennrChargennrMitMengeDto snrDto = new SeriennrChargennrMitMengeDto(
						(String) jTableSnrChnrs.getModel().getValueAt(rows[i],
								0), new BigDecimal(1));
				snrDto.setCVersion((String) jTableSnrChnrs.getModel()
						.getValueAt(rows[i], 1));

				alSeriennummern.add(snrDto);
			} else {
				alSeriennummern.add(new SeriennrChargennrMitMengeDto(
						(String) jTableSnrChnrs.getModel().getValueAt(rows[i],
								0), (BigDecimal) jTableSnrChnrs.getModel()
								.getValueAt(rows[i], 1)));
			}

		}
		setVisible(false);
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

class DialogSnrChnr_jButtonUebernehmen_actionAdapter implements ActionListener {
	private DialogSnrChnrauswahl adaptee;

	DialogSnrChnr_jButtonUebernehmen_actionAdapter(DialogSnrChnrauswahl adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}

class MyTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1458184566910151330L;
	private String[] columnNames = null;
	private Object[][] data = null;

	public MyTableModel(String[] columnNames, Object[][] data) {
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
