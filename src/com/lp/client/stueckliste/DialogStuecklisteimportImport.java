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
package com.lp.client.stueckliste;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

//VS4E -- DO NOT REMOVE THIS LINE!
public class DialogStuecklisteimportImport extends JDialog implements TableModelListener {

	private static final long serialVersionUID = 1L;
	private JTableX tblImport;
	private JScrollPane jScrollPane0;
	
	private Vector<Vector> tabledata;
	
	private boolean cellSetAktiv = false;
	private JButton btnAbbrechen;
	private JButton bntImportieren;
	private Integer stuecklisteIId;

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());
	private JPanel jPanel0;
	private JButton btnUebernehmen;

	private final static int COL_BAUTEIL = 0;
	private final static int COL_BAUFORM = 1;
	private final static int COL_MENGE = 2;
	private final static int COL_POSITION = 3;
	private final static int COL_GEFUNDEN = 4;
	private final static int COL_ARTIKEL = 5;
	private final static int COL_BEZEICHNUNG = 6;
	
	public DialogStuecklisteimportImport() {
		initComponents();
	}

	public DialogStuecklisteimportImport(Frame parent) {
		super(parent);
		initComponents();
	}

	public DialogStuecklisteimportImport(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	public DialogStuecklisteimportImport(Frame parent, String title) {
		super(parent, title);
		initComponents();
	}

	public DialogStuecklisteimportImport(Frame parent, boolean b, Integer stuecklisteIId) {
		super(parent, b);
		this.stuecklisteIId = stuecklisteIId;
		initComponents();
	}

	public DialogStuecklisteimportImport(Frame parent, String title,
			boolean modal, GraphicsConfiguration arg) {
		super(parent, title, modal, arg);
		initComponents();
	}

	public DialogStuecklisteimportImport(Dialog parent) {
		super(parent);
		initComponents();
	}

	public DialogStuecklisteimportImport(Dialog parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	public DialogStuecklisteimportImport(Dialog parent, String title) {
		super(parent, title);
		initComponents();
	}

	public DialogStuecklisteimportImport(Dialog parent, String title,
			boolean modal) {
		super(parent, title, modal);
		initComponents();
	}

	public DialogStuecklisteimportImport(Dialog parent, String title,
			boolean modal, GraphicsConfiguration arg) {
		super(parent, title, modal, arg);
		initComponents();
	}

	public DialogStuecklisteimportImport(Window parent) {
		super(parent);
		initComponents();
	}

	public DialogStuecklisteimportImport(Window parent,
			ModalityType modalityType) {
		super(parent, modalityType);
		initComponents();
	}

	public DialogStuecklisteimportImport(Window parent, String title) {
		super(parent, title);
		initComponents();
	}

	public DialogStuecklisteimportImport(Window parent, String title,
			ModalityType modalityType) {
		super(parent, title, modalityType);
		initComponents();
	}

	public DialogStuecklisteimportImport(Window parent, String title,
			ModalityType modalityType, GraphicsConfiguration arg) {
		super(parent, title, modalityType, arg);
		initComponents();
	}

	private void initComponents() {
		setTitle("Import");
		setMinimumSize(new Dimension(800, 600));
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setBackground(Color.white);
		setForeground(Color.black);
		setLayout(new GroupLayout());
		add(getJScrollPane0(), new Constraints(new Bilateral(11, 12, 22), new Bilateral(10, 64, 26, 403)));
		add(getJPanel0(), new Constraints(new Bilateral(12, 13, 854), new Trailing(12, 40, 48, 566)));
		setSize(881, 644);
	}

	private JButton getBtnUebernehmen() {
		if (btnUebernehmen == null) {
			btnUebernehmen = new JButton();
			btnUebernehmen.setText("\u00DCbernehmen");
			btnUebernehmen.addActionListener(new ActionListener() {
	
				public void actionPerformed(ActionEvent event) {
					btnUebernehmenActionActionPerformed(event);
				}
			});
		}
		return btnUebernehmen;
	}

	private JPanel getJPanel0() {
		if (jPanel0 == null) {
			jPanel0 = new JPanel();
			jPanel0.setLayout(new GroupLayout());
			jPanel0.add(getBtnAbbrechen(), new Constraints(new Leading(748, 10, 10), new Leading(7, 12, 12)));
			jPanel0.add(getBntImportieren(), new Constraints(new Leading(636, 12, 12), new Leading(7, 12, 12)));
			jPanel0.add(getBtnUebernehmen(), new Constraints(new Leading(517, 12, 12), new Leading(7, 12, 12)));
		}
		return jPanel0;
	}

	private JButton getBntImportieren() {
		if (bntImportieren == null) {
			bntImportieren = new JButton();
			bntImportieren.setText("Importieren");
			bntImportieren.addActionListener(new ActionListener() {
	
				public void actionPerformed(ActionEvent event) {
					bntImportierenActionActionPerformed(event);
				}
			});
		}
		return bntImportieren;
	}

	private JButton getBtnAbbrechen() {
		if (btnAbbrechen == null) {
			btnAbbrechen = new JButton();
			btnAbbrechen.setText("Abbrechen");
			btnAbbrechen.addActionListener(new ActionListener() {
	
				public void actionPerformed(ActionEvent event) {
					btnAbbrechenActionActionPerformed(event);
				}
			});
		}
		return btnAbbrechen;
	}

	private JScrollPane getJScrollPane0() {
		if (jScrollPane0 == null) {
			jScrollPane0 = new JScrollPane();
			jScrollPane0.setViewportView(getTblImport());
		}
		return jScrollPane0;
	}

	private JTable getTblImport() {
		if (tblImport == null) {
			tblImport = new JTableX();
			tblImport.setModel(new DefaultTableModel(new Object[][] { { "", "", "", "", "0", "", "" }, }, new String[] { "Bauteil", "Bauform", "Menge", "Positionsnummer", "gefunden", "Artikel", "Bezeichnung" }) {
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class };
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}
			});
		}
		return tblImport;
	}

	public void setTabledata(Vector<Vector> tabledata) {
		this.tabledata = tabledata;
		DefaultTableModel model = (DefaultTableModel)tblImport.getModel();
		while (model.getRowCount()>0)
			model.removeRow(0);
		RowEditorModel rm = new RowEditorModel();
		tblImport.setRowEditorModel(rm);
		for (int i=0; i < tabledata.size(); i++) {
			Vector v = tabledata.get(i);
			Object[] sa1 = new Object[7];
			sa1[COL_BAUTEIL] = v.get(0);
			sa1[COL_BAUFORM] = v.get(1);
			sa1[COL_MENGE] = v.get(2);
			sa1[COL_POSITION] = v.get(3);
			ArtikelDto[] artikelDtos = artikelSucheSpezial((String)sa1[COL_BAUTEIL], (String)sa1[COL_BAUFORM]);
			if (artikelDtos == null)
				sa1[COL_GEFUNDEN] = "0";
			else
				sa1[COL_GEFUNDEN] = String.format("%d", artikelDtos.length);
			if (artikelDtos != null && artikelDtos.length > 0) {
				sa1[COL_ARTIKEL] = artikelDtos[0].getCNr();
				try {
					sa1[COL_BEZEICHNUNG] = ((ArtikelsprDto) DelegateFactory.getInstance().getArtikelDelegate().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelDtos[0].getIId(), 
							LPMain.getInstance().getTheClient().getLocMandantAsString())).getCBez();
				} catch (Throwable e) {
					sa1[COL_BEZEICHNUNG] = "???";
				}
			} else
				sa1[COL_BEZEICHNUNG] = "???";
			//System.arraycopy(sa,0,sa1,0,sa.length);
			model.addRow(sa1);
			if (artikelDtos != null && artikelDtos.length >1) {
				//TableColumn c = tblImport.getColumnModel().getColumn(5);
				JComboBox cbo = new JComboBox();
				cbo.setEditable(true);
				for (int ix=0; ix<artikelDtos.length; ix++) {
					cbo.addItem(artikelDtos[ix].getCNr());
				}
				DefaultCellEditor ed =  new DefaultCellEditor(cbo);
				rm.addEditorForRow(COL_ARTIKEL * (model.getRowCount()),ed);
				JComboBox cbo1 = new JComboBox();
				cbo1.setEditable(true);
				for (int ix=0; ix<artikelDtos.length; ix++) {
					String s="";
					try {
						s = ((ArtikelsprDto) DelegateFactory.getInstance().getArtikelDelegate().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelDtos[ix].getIId(), 
									LPMain.getInstance().getTheClient().getLocMandantAsString())).getCBez();
					} catch (Throwable e) {
						//
					}
					cbo1.addItem(s);
				}
				ed =  new DefaultCellEditor(cbo1);
				rm.addEditorForRow(COL_BEZEICHNUNG * (model.getRowCount()),ed);
			}
		}
		tblImport.getModel().addTableModelListener(this);
	}

	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int col = e.getColumn();
		if (!cellSetAktiv && (col == COL_ARTIKEL || col == COL_BEZEICHNUNG)) {
			RowEditorModel rm = tblImport.getRowEditorModel();
			DefaultCellEditor tc = (DefaultCellEditor)rm.getEditor(col * (row+1));
			if (tc != null) {
				JComboBox cb = (JComboBox)tc.getComponent();
				int index = cb.getSelectedIndex();
				if (col == COL_ARTIKEL) {
					tc = (DefaultCellEditor)rm.getEditor(COL_BEZEICHNUNG * (row+1));
					cb = (JComboBox)tc.getComponent();
					cb.setSelectedIndex(index);
					cellSetAktiv = true;
					tblImport.setValueAt(cb.getSelectedItem(), row, COL_BEZEICHNUNG);
					cellSetAktiv = false;
				} else {
					tc = (DefaultCellEditor)rm.getEditor(COL_ARTIKEL * (row+1));
					cb = (JComboBox)tc.getComponent();
					cb.setSelectedIndex(index);
					cellSetAktiv = true;
					tblImport.setValueAt(cb.getSelectedItem(), row, COL_ARTIKEL);
					cellSetAktiv = false;
				}
			}
		}
	}
	
	public Vector<Vector> getTabledata() {
		return tabledata;
	}
	
	private ArtikelDto[] artikelSucheSpezial(String bauteil, String bauform) {
		try {
			ArtikelDto[] artikelDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindSpezial(bauteil, bauform);
			return artikelDto;
		} catch (Throwable e) {
			//
			int i=0;
		}
		return null;
	}

	public class RowEditorModel
	{
		private Hashtable data;
		public RowEditorModel()
		{
			data = new Hashtable();
		}
		public void addEditorForRow(int row, TableCellEditor e )
		{
			data.put(new Integer(row), e);
		}
		public void removeEditorForRow(int row)
		{
			data.remove(new Integer(row));
		}
		public TableCellEditor getEditor(int row)
		{
			return (TableCellEditor)data.get(new Integer(row));
		}
	}

	public class JTableX extends JTable
	{
		protected RowEditorModel rm;

		public JTableX()
		{
			super();
			rm = null;
		}

		public JTableX(TableModel tm)
		{
			super(tm);
			rm = null;
		}

		public JTableX(TableModel tm, TableColumnModel cm)
		{
			super(tm,cm);
			rm = null;
		}

		public JTableX(TableModel tm, TableColumnModel cm,
				ListSelectionModel sm)
		{
			super(tm,cm,sm);
			rm = null;
		}

		public JTableX(int rows, int cols)
		{
			super(rows,cols);
			rm = null;
		}

		public JTableX(final Vector rowData, final Vector columnNames)
		{
			super(rowData, columnNames);
			rm = null;
		}

		public JTableX(final Object[][] rowData, final Object[] colNames)
		{
			super(rowData, colNames);
			rm = null;
		}

		// new constructor
		public JTableX(TableModel tm, RowEditorModel rm)
		{
			super(tm,null,null);
			this.rm = rm;
		}

		public void setRowEditorModel(RowEditorModel rm)
		{
			this.rm = rm;
		}

		public RowEditorModel getRowEditorModel()
		{
			return rm;
		}

		public TableCellEditor getCellEditor(int row, int col)
		{
			if (col == COL_ARTIKEL || col == COL_BEZEICHNUNG) {
				TableCellEditor tmpEditor = null;
				if (rm != null)
					tmpEditor = rm.getEditor(col*(row+1));
				if (tmpEditor != null)
					return tmpEditor;
				return super.getCellEditor(row,col);
			} else {
				return super.getCellEditor(row,col);
			}
		}
	}

	private void bntImportierenActionActionPerformed(ActionEvent event) {
		doImport();
		this.dispose();
	}

	private void btnAbbrechenActionActionPerformed(ActionEvent event) {
		this.dispose();
	}
	
	private void doImport() {
		DefaultTableModel model = (DefaultTableModel)tblImport.getModel();
		Vector<Vector> data = model.getDataVector();
		ArrayList<StuecklistepositionDto> list = new ArrayList<StuecklistepositionDto>();

		ParametermandantDto parameter;
		String defaultEinheit = null;
		try {
			parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);
			defaultEinheit = parameter.getCWert();
		} catch (Throwable e1) {
			myLogger.error("Keine Default-Einheit definiert! " + e1.getMessage());
		}
		
		MontageartDto[] dtos;
		Integer defaultMontageartIId = null;
		try {
			dtos = DelegateFactory.getInstance().getStuecklisteDelegate().montageartFindByMandantCNr();
			if (dtos != null && dtos.length > 0)
				defaultMontageartIId= dtos[0].getIId();
		} catch (Throwable e1) {
			myLogger.error("Keine Montageart gefunden! " + e1.getMessage());
		}

		for (int i=0; i < data.size(); i++) {
			Vector<Object> v = data.get(i);
			StuecklistepositionDto dto = new StuecklistepositionDto();
			dto.setBMitdrucken(Helper.boolean2Short(false));
			//dto.setMontageartIId(panelBottomPositionen.defaultMontageartDto.getIId());
			dto.setStuecklisteIId(stuecklisteIId);
			dto.setMontageartIId(defaultMontageartIId);
			ArtikelDto artikelDto = null;
			try {
				artikelDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByCNr((String)v.get(COL_ARTIKEL));
				dto.setArtikelIId(artikelDto.getIId());
				dto.setEinheitCNr(artikelDto.getEinheitCNr());
				dto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
				//dto.setCKommentar(sLine[5]);
				dto.setNMenge(new BigDecimal((String)v.get(COL_MENGE)));
				dto.setCPosition((String)v.get(COL_POSITION));
				list.add(dto);
			} catch (ExceptionLP ex1) {
				if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
					// Handartikel anlegen
					dto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
					if (v.get(COL_ARTIKEL) == null 
							|| ((String)v.get(COL_BEZEICHNUNG)).equals("???")) {
						String s = (String)v.get(COL_BAUTEIL) + " " + (String)v.get(COL_BAUFORM);
						if (s.length() > 40)
							s = s.substring(0,40);
						dto.setSHandeingabe(s);
					} else
						dto.setSHandeingabe((String)v.get(COL_ARTIKEL));
					dto.setNMenge(new BigDecimal((String)v.get(COL_MENGE)));
					dto.setCPosition((String)v.get(COL_POSITION));
					dto.setCBez((String)v.get(COL_BEZEICHNUNG));
					//dto.setCKommentar(sLine[5]);
					dto.setEinheitCNr(defaultEinheit);
					list.add(dto);
				}
			} catch (Throwable e) {
				myLogger.warn("Fehler Zeile " + i + " " + e.getMessage());
			}
		}		
		try {
			if (list.size() > 0) {
				StuecklistepositionDto[] returnArray = new StuecklistepositionDto[list.size()];
				returnArray = (StuecklistepositionDto[]) list.toArray(returnArray);
				DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.createStuecklistepositions(returnArray);
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
						"lp.hint"),
						"" + list.size() + " Positionen erstellt.");
			}
		} catch (Throwable e) {
			myLogger.error("Fehler bei der Verarbeitung der Positionen " + e.getMessage());
		}
	}

	private void btnUebernehmenActionActionPerformed(ActionEvent event) {
		int row = tblImport.getSelectedRow();
		String vergleich = (String) tblImport.getValueAt(row, COL_BAUTEIL) + (String) tblImport.getValueAt(row, COL_BAUFORM);
		String value = (String) tblImport.getValueAt(row, COL_ARTIKEL);
		String bezeichnung = (String) tblImport.getValueAt(row, COL_BEZEICHNUNG);
		
		// keine change events waehrend update
		tblImport.getModel().removeTableModelListener(this);
		for (int i=0; i < tblImport.getRowCount(); i++) {
			if (i != row) {
				try {
					String zeile = (String) tblImport.getValueAt(i, COL_BAUTEIL) + (String) tblImport.getValueAt(i, COL_BAUFORM);
					if (zeile.compareTo(vergleich) == 0) {
						tblImport.setValueAt(value, i, COL_ARTIKEL);
						tblImport.setValueAt(bezeichnung, i, COL_BEZEICHNUNG);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
		// events wieder zulassen
		tblImport.getModel().addTableModelListener(this);
	}
} 
