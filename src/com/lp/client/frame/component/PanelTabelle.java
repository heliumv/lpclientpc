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

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LPDefaultTableCellRenderer;
import com.lp.client.frame.LockStateValue;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.ReportPanelQuery;
import com.lp.client.util.dtable.DistributedTableDataSourceImpl;
import com.lp.client.util.dtable.DistributedTableModelImpl;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * @author Uli Walch
 */
public abstract class PanelTabelle extends PanelBasis implements ISourceEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel panelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private int iYGridBagNext = 0;

	private JPanel panelFilterKriterien = null;
	private JPanel panelOptionaleZeile = null;
	
	private boolean strechColumns = true;
	private int[] columnHeaderWidths;

	public WrapperTable table;
	private JScrollPane tableScrollPane;

	/** Zum automtischen Fuellen der Table. */
	private DistributedTableDataSourceImpl dataSource;

	/** Das TableModel. */
	private DistributedTableModelImpl tableModel;

	/** Die eindeutige UseCase ID. */
	private int idUsecase;

	public static final String LEAVEALONE_PRINTPANELQUERY = LEAVEALONE
			+ "PRINTPANELQUERY";

	/**
	 * Fuer den Zugriff auf die DB koennen ueber ein Selektionsfenster Filter
	 * gesetzt werden, die ueber die GUI nicht beeinflusst werden koennen.
	 */
	private FilterKriterium[] aDefaultFilter = null;

	/** Diese Action wird vom Knopf zum Anzeigen der Diagramme ausgeloest. */
	static final public String ACTION_SPECIAL_SHOW_DIAGRAMM = "action_special_show_diagramm";

	/**
	 * Die erste Spalte enthaelt die Ueberschrift fuer die Zeile und hat immer
	 * diese Breite.
	 */
	public int SPALTENBREITE_ZEILENHEADER = 120;

	/**
	 * Jede vertikale Spalte, die zur opritschen Trennung verwendet wird, hat
	 * diese Breite.
	 */
	static final public int SPALTENBREITE_LEERESPALTE = 5;

	public PanelTabelle(int iUsecaseIdI, String sTitelTabbedPaneI,
			InternalFrame oInternalFrameI) throws Throwable {

		super(oInternalFrameI, sTitelTabbedPaneI);

		idUsecase = iUsecaseIdI;

		jbInit();
		initComponents();
	}

	/**
	 * Initialisiere alle Komponenten; braucht der JBX-Designer; hier bitte
	 * keine wilden Dinge wie zum Server gehen, etc. machen.
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen von 10
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// auf dem Aussenpanel wird die Toolbar angebracht
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// CK: Listen-Druck-Icon einbauen
//		JButton bPrint = createAndSaveButton(
//				"/com/lp/client/res/table_sql_view.png",
//				LPMain.getTextRespectUISPr("lp.printer"),
//				LEAVEALONE_PRINTPANELQUERY, null, null);
//		bPrint.setEnabled(true);

		getToolBar().addButtonRight(
				"/com/lp/client/res/table_sql_view.png",
				LPMain.getTextRespectUISPr("lp.printer"),
				LEAVEALONE_PRINTPANELQUERY, null, null);
		enableToolsPanelButtons(true, LEAVEALONE_PRINTPANELQUERY);
//		getToolBar().getToolsPanelRight().add(bPrint);

		// auf dem Aussenpanel wird ausserdem das WorkingPanel angebracht, das
		// alles andere enthaelt
		panelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		panelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		add(panelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Zeile - das Panel mit den Filterkriterien
		panelWorkingOn.add(getPanelFilterKriterien(), new GridBagConstraints(0,
				iYGridBagNext, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile - das Panel fuer die optionale zweite Zeile
		iYGridBagNext++;

		panelWorkingOn.add(getPanelOptionaleZweiteZeile(),
				new GridBagConstraints(0, iYGridBagNext, 1, 1, 1.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iYGridBagNext++;

		dataSource = new DistributedTableDataSourceImpl(new Integer(idUsecase));

		tableModel = new DistributedTableModelImpl(this.dataSource);

		// String[] dbColumnNames = this.dataSource.getTableInfo()
		// .getDataBaseColumnNames();

		this.table = new WrapperTable(getInternalFrame(), this.tableModel);
		this.table.setLocale(LPMain.getInstance().getUISprLocale());
		// this.table.addKeyListener(this);
		// this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// this.table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.table.setColumnSelectionAllowed(false);
		this.table.setRowSelectionAllowed(false);
		// this.table.addMouseListener(this);

		// Feature Spalten mit der Maus verschieben deaktivieren
		table.getTableHeader().setReorderingAllowed(false);

		tableScrollPane = new JScrollPane(table);
		tableScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// die Tabelle hat ein hellgraues Gitter
		table.setGridColor(HelperClient.getHintergrundColor());

		// die erste Spalte ist immer der ZeilenHeader
		TableColumn tcZeilenHeader = table.getColumnModel().getColumn(0);
		tcZeilenHeader.setCellRenderer(new ZeilenHeaderRenderer());
//		tcZeilenHeader.setPreferredWidth(SPALTENBREITE_ZEILENHEADER);

		/**
		 * @todo das kommt 2 mal vor -> eine Methode
		 */
		// die restlichen Spalten werden per default formatiert

		// die Breite der Spalten einstellen
		columnHeaderWidths = dataSource.getTableInfo()
				.getColumnHeaderWidths();

		if (columnHeaderWidths != null) {
			for (int i = 1; i < columnHeaderWidths.length; i++) {

				// alle Spalten mit -1 sind variabel
				if (columnHeaderWidths[i] != -1) {
					setColumnWidth(i,
							Helper.getBreiteInPixel(columnHeaderWidths[i]));
				}
			}
		}

		// die letzte Spalte Verstecken, wenn Color
		Class<?> letzteKlasse = dataSource.getColumnClasses()[dataSource
				.getColumnClasses().length - 1];
		if (letzteKlasse.equals(java.awt.Color.class)) {
			setColumnWidthToZero(dataSource.getColumnClasses().length - 1);
		}

		table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		// Zeile
		iYGridBagNext++;

		panelWorkingOn.add(tableScrollPane, new GridBagConstraints(0,
				iYGridBagNext, 1, 1, 1.0, 0.7, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (strechColumns && table.getWidth() > 0) {
			strechColumns = false;
			strechShareWithRestColumns(columnHeaderWidths);
			repaint();
		}
	}

	private void strechShareWithRestColumns(int[] columnWidths) {
		if (columnWidths == null)
			return;
		List<TableColumn> columnsToStrech = new ArrayList<TableColumn>();
		int alreadyDefinedSize = 0;
		for (int i = 1; i < columnWidths.length-1; i++) {
			TableColumn tc = table.getColumnModel().getColumn(i);
			if (columnWidths[i] == -1 && tc.getMaxWidth() > 0)
				columnsToStrech.add(tc);
			else
				alreadyDefinedSize += tc.getPreferredWidth();
		}

		if (columnsToStrech.size() == 0)
			return;

		int sharedTotal = table.getColumnModel().getTotalColumnWidth()
				- alreadyDefinedSize;
		for (TableColumn tc : columnsToStrech) {
			int newSize = sharedTotal / columnsToStrech.size();
			tc.setPreferredWidth(newSize);
			tc.setWidth(newSize);
		}

	}
	
	/**
	 * Die Spalte verstecken.
	 * 
	 * @param iColumnIndex
	 *            der Index der Spalte
	 * @param iColumnWidth
	 *            doe absolute Breite der Spalte
	 */
	private void setColumnWidthToZero(int iColumnIndex) {
		TableColumn tc = table.getColumnModel().getColumn(iColumnIndex);
		tc.setMinWidth(0);
		tc.setPreferredWidth(0);

		boolean showIIds = iColumnIndex == 0
				&& Defaults.getInstance().isShowIIdColumn();
		tc.setMaxWidth(showIIds ? Helper
				.getBreiteInPixel(QueryParameters.FLR_BREITE_M) : 0);
		tc.setResizable(showIIds);
	}

	/**
	 * Die absoulte Breite einer Spalte festlegen.
	 * 
	 * @param iColumnIndex
	 *            der Index der Spalte
	 * @param iColumnWidth
	 *            die absolute Breite der Spalte
	 */
	protected void setColumnWidth(int iColumnIndex, int iColumnWidth) {
		if(iColumnWidth == 0) {
			setColumnWidthToZero(iColumnIndex);
			return;
		}
		
		TableColumn tc = table.getColumnModel().getColumn(iColumnIndex);
		tc.setMinWidth(Helper
				.getBreiteInPixel(QueryParameters.FLR_BREITE_MINIMUM));
//		tc.setMaxWidth(iColumnWidth);
//		tc.setMinWidth(iColumnWidth);
		tc.setPreferredWidth(iColumnWidth);
		tc.setWidth(iColumnWidth);
	}

	public void setDefaultFilter(FilterKriterium[] defaultFilter) {
		this.aDefaultFilter = defaultFilter;
	}

	public FilterKriterium[] getDefaultFilter() {
		return aDefaultFilter;
	}

	public JPanel getPanelFilterKriterien() {
		if (panelFilterKriterien == null) {
			panelFilterKriterien = new JPanel();
			FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
			panelFilterKriterien.setLayout(flowLayout);
		}

		return panelFilterKriterien;
	}

	/**
	 * Die optionale zweite Zeile hat das GridBagLayout, damit man positionieren
	 * kann.
	 * 
	 * @return JPanel
	 */
	public JPanel getPanelOptionaleZweiteZeile() {
		if (panelOptionaleZeile == null) {
			panelOptionaleZeile = new JPanel();
			GridBagLayout gridBagLayout = new GridBagLayout();
			panelOptionaleZeile.setLayout(gridBagLayout);
		}

		return panelOptionaleZeile;
	}

	/**
	 * eventActionRefresh
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoRefreshI
	 *            boolean
	 * @throws Throwable
	 * @todo Diese com.lp.client.ui2.LPPanel-Methode implementieren PJ 5110
	 */
	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		long tStart = System.currentTimeMillis();
		FilterBlock filter = new FilterBlock(aDefaultFilter,
				FilterKriterium.BOOLOPERATOR_AND);

		table.invalidate();
		QueryParameters query = new QueryParameters(
				new Integer(this.idUsecase), null, filter, null, null);

		tableModel.setQuery(query);

		table.revalidate();
		table.repaint();

		// wenn der Zeilen Header angezeigt werden soll, farblich kennzeichnen
		// table.getColumnModel().getColumn(0).
		// table.getColumnModel().getColumn(0).setWidth(0);
		// table.getColumnModel().getColumn(0).setMaxWidth(0);
		// table.getColumnModel().getColumn(0).setMinWidth(0);
		// table.getColumnModel().getColumn(0).setPreferredWidth(0);

		// @uw bei einem PanelSplit muss die Detailanzeige auch refresh kriegen
		// getInternalFrame().fireItemChanged(this,
		// ItemChangedEvent.ITEM_CHANGED);
		long tEnd = System.currentTimeMillis();
		myLogger.info("refresh PanelTabelle UC=" + this.idUsecase + ": "
				+ (tEnd - tStart) + " [ms]");
	}

	/**
	 * eventActionSpecial
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().startsWith(LEAVEALONE)) {
			if (e.getActionCommand().equals(LEAVEALONE_PRINTPANELQUERY)) {
				// LISTE DRUCKEN
				FilterBlock filter = new FilterBlock(aDefaultFilter,
						FilterKriterium.BOOLOPERATOR_AND);

				QueryParameters query = new QueryParameters(new Integer(
						this.idUsecase), null, filter, null, null);

				String title = getInternalFrame().getTitle();

				getInternalFrame().showReportKriterien(
						new ReportPanelQuery("", getInternalFrame(), title,
								query));
			}
		}
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

		FilterBlock filter = new FilterBlock(aDefaultFilter,
				FilterKriterium.BOOLOPERATOR_AND);

		QueryParameters query = new QueryParameters(
				new Integer(this.idUsecase), null, filter, null, null);

		getInternalFrame().showReportKriterien(
				new ReportPanelQuery("", getInternalFrame(), "", query));
	}

	/**
	 * Bei jeder Auswahl wird die Anzeige aktualisiert.
	 * 
	 * @param bNeedNoYouAreSelectedI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		eventActionRefresh(null, false);
		super.eventYouAreSelected(false);
	}

	/**
	 * Da es in dieser Tabelle vorerst nicht geawehlt wird, kann null
	 * zurueckgegeben werden.
	 * 
	 * @return Object ist immer null
	 */
	public Object getIdSelected() {
		return null;
	}

	/**
	 * btnstate: 10 Hier wird festgelegt, ob und wann welche Buttons aktiviert
	 * sind.
	 * 
	 * @throws Exception
	 * @param iAspectI
	 *            int
	 * @param lockstateValueI
	 *            LockStateValue
	 */
	public void updateButtons(int iAspectI, LockStateValue lockstateValueI)
			throws Exception {
		// kein super!
		Collection<?> buttons = getHmOfButtons().values();

		for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
			LPButtonAction item = (LPButtonAction) iter.next();

			item.getButton().setEnabled(true);
		}
	}

	/**
	 * Steuert die Sichtbarkeit der ersten Spalte in der Tabelle. <br>
	 * Die erste Spalte enthaelt die Zeilenheader.
	 * 
	 * @param bVisibleI
	 *            boolean
	 */
	public void setFirstColumnVisible(boolean bVisibleI) {
		if (bVisibleI) {
			table.getColumnModel().getColumn(0)
					.setWidth(SPALTENBREITE_ZEILENHEADER);
			table.getColumnModel().getColumn(0)
					.setMaxWidth(SPALTENBREITE_ZEILENHEADER);
			table.getColumnModel().getColumn(0)
					.setMinWidth(SPALTENBREITE_ZEILENHEADER);
			table.getColumnModel().getColumn(0)
					.setPreferredWidth(SPALTENBREITE_ZEILENHEADER);
		} else {
			setColumnWidthToZero(0);
		}
	}

	public WrapperTable getTable() {
		return table;
	}

	/**
	 * Renderer-Klasse fuer die erste Spalte, die die Zeilen Header enthaelt.
	 */
	public static class ZeilenHeaderRenderer extends LPDefaultTableCellRenderer {
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen und einfaerben.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			Font myFont = new Font("Arial", Font.BOLD, 12);

			setFont(myFont);
			if (value != null) {
				setBackground(Color.lightGray);
			}
			setText((value == null) ? "" : (String) value);
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	/**
	 * Renderer-Klasse fuer eine Spalte, die nur leere Felder enthaelt.
	 */
	public static class LeeresFeldRenderer extends LPDefaultTableCellRenderer {
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen und einfaerben.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			setBackground(HelperClient.getHintergrundColor());
			setText("");
		}
	}

	protected String getLockMeWer() throws Exception {
		return null; // nichts bestimmtes Locken
	}

	public String getAspect() {
		return "";
	}

	public void cleanup() {
		if (dataSource != null)
			dataSource.cleanup();
		dataSource = null;
	}

	protected PropertyVetoException eventActionVetoableChangeLP()
			throws Throwable {
		cleanup();
		PropertyVetoException pve = null;
		return pve;
	}

}
