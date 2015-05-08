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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.KontoImportStats;
import com.lp.server.finanz.service.KontoImporterResult;
import com.lp.util.EJBLineNumberExceptionLP;

public class DialogCsvResult extends JDialog implements TableModel, TableModelListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel panelImport = new JPanel();
	
	private WrapperLabel  wlbVerify = new WrapperLabel() ;
	private WrapperLabel  wlbTotalLines = new WrapperLabel() ;
	private WrapperLabel  wlbIgnoredLines = new WrapperLabel() ; 
	private WrapperLabel  wlbErrorLines   = new WrapperLabel() ;
	private WrapperLabel  wlbGoodLines   = new WrapperLabel() ;
	
	private WrapperButton wbuImport = new WrapperButton() ;
	private WrapperButton wbuCancel = new WrapperButton() ;
	private WrapperButton wbuOkay   = new WrapperButton() ;
	
	private JTable jTableResults ;
	private JScrollPane jPanelTable ;
	
	private List<EJBLineNumberExceptionLP> importErrors = new ArrayList<EJBLineNumberExceptionLP>() ;
	private KontoImportStats importStats = new KontoImportStats() ;
	private ICsvKontoImportController importController ;
	
	private String[] columnNames = new String[] {
			LPMain.getTextRespectUISPr("fb.konto.import.head.zeile"),
			LPMain.getTextRespectUISPr("fb.konto.import.head.klasse"),
			LPMain.getTextRespectUISPr("fb.konto.import.head.fehlernr"),
			LPMain.getTextRespectUISPr("fb.konto.import.head.fehlerbez"),
			LPMain.getTextRespectUISPr("fb.konto.import.head.daten"),
			LPMain.getTextRespectUISPr("fb.konto.import.head.detail")} ;
	private Class[] columnClasses = new Class[] {Integer.class, String.class, Integer.class, String.class, String.class, String.class} ;
	private String[] severityMap = new String[] {"-", "D", "I", "W", "F"} ;
	
	public DialogCsvResult(Frame owner, String title, boolean modal, ICsvKontoImportController controller) throws Throwable {
		super(owner, title, modal) ;
	
		if(null == controller) throw new IllegalArgumentException("controller == null") ;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE) ;
		importController = controller ;
		importErrors = new ArrayList<EJBLineNumberExceptionLP>() ;
		
		jbInit() ;
		pack() ;
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				actionVerify() ;
			}
		});
	}

	private void jbInit() throws Throwable {
		panelImport.setLayout(new GridBagLayout()) ;

		wbuImport.setText("Importieren") ;
		wbuImport.addActionListener(this) ;
		wbuImport.setVisible(true) ;
		
		wlbVerify.setText("") ;
		wlbVerify.setHorizontalAlignment(SwingConstants.LEFT) ;
		wlbErrorLines.setHorizontalAlignment(SwingConstants.LEFT) ;
		wlbIgnoredLines.setHorizontalAlignment(SwingConstants.LEFT) ;
		wlbTotalLines.setHorizontalAlignment(SwingConstants.LEFT) ;
		wlbGoodLines.setHorizontalAlignment(SwingConstants.LEFT) ;

		wbuCancel.setText(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wbuCancel.addActionListener(this) ;
		wbuOkay.setText(LPMain.getTextRespectUISPr("button.ok"));
		wbuOkay.addActionListener(this) ;
		wbuOkay.setVisible(false) ;
		
		initTableResults() ;

		getContentPane().setLayout(new GridBagLayout()) ;
		getContentPane().add(panelImport,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.BOTH, getInsets2(), 0, 0));

		panelImport.add(wlbVerify, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0));
		panelImport.add(jPanelTable, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.BOTH, getInsets2(), 0, 0));
		
		JPanel panelInfo = new JPanel() ;
		panelInfo.setLayout(new GridBagLayout()) ;
		panelInfo.setBorder(BorderFactory.createEtchedBorder()) ;
		
		panelInfo.add(wlbTotalLines, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0));
		panelInfo.add(wlbGoodLines, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0));
		panelInfo.add(wlbErrorLines, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0));
		panelInfo.add(wlbIgnoredLines, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, getInsets2(), 0, 0));
		
		JPanel panelButton = new JPanel() ;
		panelButton.setLayout(new GridBagLayout()) ;
		
		panelButton.add(wbuImport, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.NONE, getInsets2(), 100, 0));
		panelButton.add(wbuOkay, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.NONE, getInsets2(), 100, 0));
		panelButton.add(wbuCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.NONE, getInsets2(), 100, 0));

		getContentPane().add(panelInfo, 
				new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.BOTH, getInsets2(), 0, 0)) ;
		getContentPane().add(panelButton,
                new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.BOTH, getInsets2(), 0, 0));

		resetFieldsForStats() ;
	}

	private Insets getInsets2() {
		return new Insets(2, 2, 2, 2) ;
	}
	
	private String getFileName() {
		File f = importController.getImportFile() ;
		return f == null ? "" : f.getName() ;
	}
	
	private void setFieldsForStats(boolean imported) {
		wlbTotalLines.setText(
				LPMain.getMessageTextRespectUISPr("fb.konto.import.lines", 
						new Object[]{getFileName(), importStats.getTotalRows()})) ;
		
		Integer count = importStats.getErrorRowCount() ;
		if(count > 0) {
			wlbErrorLines.setText(
					LPMain.getMessageTextRespectUISPr("fb.konto.import.errors", new Object[]{count})) ;
			wlbErrorLines.setForeground(Color.RED) ;
		} else {
			wlbErrorLines.setText(LPMain.getTextRespectUISPr("fb.konto.import.noerrors")) ;
			wlbErrorLines.setForeground(Color.GREEN) ;
		}
		
		count = importStats.getIgnoredRowCount() ;
		if(count > 0) {
			wlbIgnoredLines.setText(LPMain.getMessageTextRespectUISPr("fb.konto.import.ignored", new Object[]{count})) ;		
			wlbIgnoredLines.setForeground(Color.ORANGE) ;
		} else {
			wlbIgnoredLines.setText("") ;
			wlbIgnoredLines.setForeground(Color.BLACK) ;
		}
	
		count = imported ?
				importStats.getGoodRowCount() : 
				importStats.getPossibleGoodRowCount()  ;
		if(count > 0) {
			wlbGoodLines.setText(
				imported ? 	
					LPMain.getMessageTextRespectUISPr("fb.konto.import.saved", new Object[]{count}) :
					LPMain.getMessageTextRespectUISPr("fb.konto.import.checked", new Object[]{count})) ;
			wlbGoodLines.setForeground(Color.GREEN) ;			
		} else {
			wlbGoodLines.setText(LPMain.getTextRespectUISPr("fb.konto.import.nolines")) ;
			wlbGoodLines.setForeground(Color.RED) ;			
		}
	}
	
	private void resetFieldsForStats() {
		wlbTotalLines.setText("") ;
		wlbErrorLines.setText("") ;
		wlbIgnoredLines.setText("") ;
		wlbGoodLines.setText("") ;
	}
	
	private void initTableResults() throws Throwable {
		jTableResults = new JTable(this) ;
		jTableResults.setAutoCreateRowSorter(true) ;
		jTableResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
		
		jPanelTable = new JScrollPane(jTableResults) ;
		jPanelTable.setPreferredSize(new Dimension(710, 205));
		
		setPreferredColumnWith(jTableResults, new Integer[]{50, 50, 60, 350}) ;
		jTableResults.getModel().addTableModelListener( this );
	}

	private void setPreferredColumnWith(JTable table, Integer[] preferredSizes) {
		for(int i = 0 ; i < preferredSizes.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i) ;
			col.setPreferredWidth(preferredSizes[i]) ;
		}
	}

	
	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex] ;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length ;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex] ;
	}

	@Override
	public int getRowCount() {
		return importErrors.size() ;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= importErrors.size()) return null ;
		
		EJBLineNumberExceptionLP ex = importErrors.get(rowIndex) ;

		if(0 == columnIndex) return ex.getLinenumber() ;
		if(1 == columnIndex) {
			if((ex.getSeverity() >= EJBLineNumberExceptionLP.SEVERITY_DEBUG) && 
				ex.getSeverity() <= EJBLineNumberExceptionLP.SEVERITY_ERROR) {
				return severityMap[ex.getSeverity()] ;
			} else {
				return "?" ;
			}
		}
		if(2 == columnIndex) return new Integer(ex.getCode()) ;
		if(3 == columnIndex) return LPMain.getTextRespectUISPr("fb.konto.import." + ex.getCode()) ;
		if(4 == columnIndex) {			
			ArrayList<Object> msgs = ex.getAlInfoForTheClient() ;
			if(msgs != null) {
				return msgs.get(0) ;
			}
		}

		if(5 == columnIndex) {
			Throwable t = ex.getCause() ;
			return t != null ? t.getMessage() : null ;
		}

		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource() ;
		if(source == wbuImport) {
			actionImport() ;			
		}
		if(source == wbuCancel || source == wbuOkay) {
			setVisible(false) ;
		}
	}
	
	private void setImportResult(KontoImporterResult result) {
		importErrors = result.getMessages() ;
		importStats = result.getStats() ;
	}
	
	protected void actionVerify() {
		wlbVerify.setText(LPMain.getTextRespectUISPr("fb.konto.import.pruefen")) ;
		resetFieldsForStats() ;
		invalidate() ;
		
		try {
			wbuImport.setEnabled(false) ;
			setImportResult(importController.checkImport()) ;
			wlbVerify.setText(LPMain.getTextRespectUISPr("fb.konto.import.geprueft")) ;
			setFieldsForStats(false) ;
		} catch(IOException e){			
		} finally {
			wbuImport.setEnabled(importStats.getPossibleGoodRowCount() > 0) ;
		}
	}

	protected void actionImport() {
		wlbVerify.setText(LPMain.getTextRespectUISPr("fb.konto.import.importieren")) ;
		resetFieldsForStats() ;
		invalidate() ;
		
		try {
			wbuImport.setEnabled(false) ;
			setImportResult(importController.doImport()) ;
			wlbVerify.setText(LPMain.getTextRespectUISPr("fb.konto.import.importiert")) ;
			setFieldsForStats(true) ;
		} catch(IOException e){			
		} finally {
			wbuImport.setVisible(false) ;
			wbuCancel.setVisible(false) ;
			wbuOkay.setVisible(true) ;
		}
	}

	/*
	 *  Update the TableColumn with the newly calculated width
	 */
	private void updateTableColumn(int column, int width)
	{
		final TableColumn tableColumn = jTableResults.getColumnModel().getColumn(column);

		if (! tableColumn.getResizable()) return;

		jTableResults.getTableHeader().setResizingColumn(tableColumn);
		tableColumn.setWidth(width);
	}
	
	/*
	 *  Get the preferred width for the specified cell
	 */
	private int getCellDataWidth(int row, int column)
	{
		//  Inovke the renderer for the cell to calculate the preferred width

		TableCellRenderer cellRenderer = jTableResults.getCellRenderer(row, column);
		Component c = jTableResults.prepareRenderer(cellRenderer, row, column);
		int width = c.getPreferredSize().width + jTableResults.getIntercellSpacing().width;

		return width;
	}
	
	/*
	 *  Calculate the width based on the widest cell renderer for the
	 *  given column.
	 */
	private int getColumnDataWidth(int column)
	{
		int preferredWidth = 0;
		int maxWidth = jTableResults.getColumnModel().getColumn(column).getMaxWidth();

		for (int row = 0; row < jTableResults.getRowCount(); row++)
		{
    		preferredWidth = Math.max(preferredWidth, getCellDataWidth(row, column));

			//  We've exceeded the maximum width, no need to check other rows

			if (preferredWidth >= maxWidth)
			    break;
		}

		return preferredWidth;
	}

	/*
	 *  Adjust the width of the specified column in the table
	 */
	public void adjustColumn(final int column)
	{
		TableColumn tableColumn = jTableResults.getColumnModel().getColumn(column);

		if (! tableColumn.getResizable()) return;

		int preferredWidth = getColumnDataWidth(column) ;
		updateTableColumn(column, preferredWidth);
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE)
		{
			adjustColumn(4);
		}
	}
}
