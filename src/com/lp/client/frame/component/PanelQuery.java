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
package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.ReportPanelQuery;
import com.lp.client.util.dtable.DistributedTableDataSourceImpl;
import com.lp.client.util.dtable.DistributedTableModelImpl;
import com.lp.client.util.fastlanereader.gui.QueryInputRow;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumSchnellansicht;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.Helper;
import com.lp.util.Pair;

/**
 * This class represents the view of the FastLaneReader. It contains a table
 * that displays the data returned by the FastLaneReader. The user can create
 * filter to reduce the displayed data to the amount he is interested in. The
 * table provides sorting functionality. Clicking on a column's header, data
 * will be sorted by this column's values. Clicking on the same header again
 * reverses sorting order. Clicking on a column while holding the CTRL key
 * pressed this column is added to the sorting and previous sorting is kept.
 * <p>
 * 
 * This view is completely configured through instances of type
 * {@link QueryType}. The first row of the view is composed of a label
 * ("Kriterium"), a combobox that lists the query types and a button that adds a
 * {@link QueryInputRow}of the selected query type to the view. <br>
 * If the user clicks on the refresh button, the entered filter will be
 * evaluated and the desired data shows in the table below.
 * 
 * @author werner
 */
public class PanelQuery extends PanelBasis implements ISourceEvent {
	/**
	 * 
	 */

	private static final ImageIcon DOKUMENTE = HelperClient
			.createImageIcon("document_attachment_green16x16.png");
	private static final ImageIcon KEINE_DOKUMENTE = HelperClient
			.createImageIcon("document_attachment16x16.png");
	private static final ImageIcon icon_up = HelperClient
			.createImageIcon("navigate_open.png");
	private static final ImageIcon icon_down = HelperClient
			.createImageIcon("navigate_close.png");
	private static final ImageIcon iNoSort = HelperClient
			.createImageIcon("nosort.png");
	private static final long serialVersionUID = 1L;
	// private JPanel jpButtons = null;
	// mehrfachselekt: default false
	private boolean bMultipleRowSelectionEnabled = false;
	private boolean bFilterHasChanged = false;

	public final static String MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK";

	/**
	 * Auf diesem Panel sitzt die Reihe der fuer dieses Panel vorgesehenen
	 * Buttons
	 */
	private JPanel panelButtonAction = null;

	/**
	 * Auf diesem Panel sitzen alle weiteren Panels und die Tabelle der
	 * Datensaetze
	 */
	private JPanel panelWorkingOn = null;

	private JPanel panelKriterium = null;

	private JPanel panelFilter = null;
	private LinkedHashMap<Integer, PanelFilterKriteriumDirekt> hmDirektFilter = null;

	public LinkedHashMap<Integer, PanelFilterKriteriumDirekt> getHmDirektFilter() {
		return hmDirektFilter;
	}

	private BildAufAbListener bildAufAbListener = new BildAufAbListener();

	private JCheckBox cbSchnellansicht = new JCheckBox();
	private FilterKriterium[] fkSchnellansicht = null;
	private FilterKriterium[] fkKey = null;

	public FilterKriterium fkComboBox = null;

	private QueryParameters queryParameters = null;
	// sitzt am panelKriterium links
	private WrapperLabel labelCriteria = null;
	// sitzt am panelKriterium in der Mitte; Auswahl eines QueryType, der dann
	// ueber
	// den Add Button in Gestalt einer QueryInputRow hinzugefuegt wird
	private WrapperComboBox comboBoxTypes = null;
	// sitzt am panelKriterium rechts; ueber diesen Button wird eine
	// QueryInputRow hinzugefuegt
	private JButton buttonAddFilterkriterium = null;

	// diese QueryTypes werden in der comboBoxTypes angezeigt
	private QueryType[] types = null;

	// flrextradata 2: Extra Daten, die man im FLR auswerten kann!
	private ArrayList<?> listOfExtraData = null;

	private int iAnzahlZeilen = -1;

	/** Auf diesem Panel sitzen die zuschaltbaren QueryInputRows */
	private JPanel panelFilterzeilen = null;
	// die momentan in der Ansicht vorhandenen QueryInputRows
	private Vector<QueryInputRow> queryInputRows = new Vector<QueryInputRow>();

	private JButton jbDokumente;
	private PanelDokumentenablage panelDokumentenverwaltung = null;
	private Boolean bShowDocButton = null;

	private boolean strechColumns = false;
	private int[] columnWidthsFromHandler;
	private Object uebersteuerteId = null;
	private boolean bBenutzeUebersteuerteId = false;

	// private boolean wasVisibleSinceLastSave = false;

	private String[] dbColumnNames = new String[0];

	public boolean isBBenutzeUebersteuerteId() {
		return bBenutzeUebersteuerteId;
	}

	public void setBBenutzeUebersteuerteId(boolean benutzeUebersteuerteId) {
		bBenutzeUebersteuerteId = benutzeUebersteuerteId;
	}

	public Object getUebersteuerteId() {
		return uebersteuerteId;
	}

	public void setUebersteuerteId(Object uebersteuerteId) {
		this.uebersteuerteId = uebersteuerteId;
	}

	/**
	 * ------------------------------------------------------------------------
	 * -- Auf diesem Panel sitzen die direkt eingebbaren Filterkriterien. <br>
	 * Es kann 0..2 dieser Filterkriterien geben. <br>
	 * Die Filterkriterien sind jeweils vom Typ String. <br>
	 * Das Ausloesen eines Roundtrip zur DB erfolgt dabei ueber ENTER.
	 * 
	 * Ausserdem gibt es eine optionale CheckBox, die entscheidet, ob versteckte
	 * Entitaeten in der Liste angezeigt werden oder nicht. <br>
	 * Das Ausloesen eines Roundtrip zur DB erfolgt dabei ueber die Aenderung
	 * des Flag. <br>
	 * Per default werden die versteckten Entitaeten mitangezeigt.
	 */
	// private JPanel panelFilterkriterienDirekt = null;
	private WrapperCheckBox wcbVersteckteFelderAnzeigen = null;
	private FilterKriterium fkVersteckteFelderNichtAnzeigen = null;
	// --------------------------------------------------------------------------
	// -

	private WrapperCheckBox wcbErsteSortierungUebersteuern = null;
	private SortierKriterium sortierkriteriumErsteSortierungUebersteuern = null;

	private WrapperComboBox wcoFilter = new WrapperComboBox();
	private JPanel multiselektInfo;

	public void setFilterComboBox(Map<?, ?> m, FilterKriterium fk,
			boolean bMandatory) {
		setFilterComboBox(m, fk, bMandatory, null);

	}

	public void setFilterComboBox(Map<?, ?> m, FilterKriterium fk,
			boolean bMandatory, String beiLeer) {

		if (beiLeer != null) {
			wcoFilter.setEmptyEntry(beiLeer);
		}
		wcoFilter.setMandatoryField(bMandatory);
		wcoFilter.setMap(m, true);
		wcoFilter.addActionListener(this);

		fkComboBox = fk;
		wcoFilter.setPreferredSize(new Dimension(150, -1));
		getToolBar().getToolsPanelCenter().add(wcoFilter);

		if (idUsecase == QueryParameters.UC_ID_ARTIKELLISTE
				&& getInternalFrame() != null) {
			if (getInternalFrame().getILetzteGewaehlteArtikelgruppenIId() != null) {
				wcoFilter.setKeyOfSelectedItem(getInternalFrame()
						.getILetzteGewaehlteArtikelgruppenIId());
			}
		}

	}

	/** Tabelle zur Anzeige der Datensaetze */
	protected WrapperTable table;
	private JScrollPane tableScrollPane;

	/** die DataSource zum TabelModel */
	private DistributedTableDataSourceImpl dataSource;

	public DistributedTableDataSourceImpl getDataSource() {
		return dataSource;
	}

	/** das TableModel */
	private DistributedTableModelImpl tableModel;

	/**
	 * the SortierKriterium[] as initialized using
	 * {@link com.lp.util.fastlanereader.query.TableInfo#getDataBaseColumnNames()
	 * TableInfo#getDataBaseColumnNames()}. There is one SortierKriterium for
	 * each data base column name.
	 */
	private HashMap<String, SortierKriterium> sortierKriterien;

	// the list of currently active SortierKriterium.
	private ArrayList<SortierKriterium> currentSortierKriterien;

	/**
	 * the use case id to identify the use case to the server. The server uses
	 * this to delegate to the correct instance of
	 * {@link com.lp.server.util.fastlanereader.UseCaseHandler UseCaseHandler}.
	 */
	private int idUsecase;

	/**
	 * This FilterBlock contains the user defined criteria for the use case.
	 */
	// private FilterKriterium[] filters = null;
	/**
	 * This FilterBlock contains the default criteria for the use case. Used for
	 * selecting mandant, ...
	 */
	private FilterKriterium[] defaultFilter = null;
	private FilterKriterium[] defaultFilter2Restore = null;

	/**
	 * True ... Die Tabelle wird erst dann befuellt, wenn auf die obere lasche
	 * geklickt wurde. <br>
	 * False ... Die Tabelle wird sofort beim ersten Anlegen befuellt.
	 */
	private boolean refreshWhenYouAreSelected = false;
	protected boolean fireItemChangedEventAfterChange = true;

	private static final int MAX_FILTER = 3;

	public static final String ACTION_ADD_FILTERKRITERIUM = "action_add_filterkriterium";
	public static final String ACTION_REMOVE_FILTERKRITERIUM = "action_remove_filterkriterium";

	static final public String LEAVEALONE_QUERYVIEW_SAVE = LEAVEALONE
			+ "QUERYVIEWSAVE";

	public static final String LEAVEALONE_PRINTPANELQUERY = LEAVEALONE
			+ "PRINTPANELQUERY";

	public static final String LEAVEALONE_DOKUMENTE = LEAVEALONE + "DOKUMENTE";

	public void addButtonAuswahlBestaetigen(String recht) throws Throwable {
		createAndSaveAndShowButton("/com/lp/client/res/check2.png",
				LPMain.getTextRespectUISPr("lp.tooltip.kriterienuebernehmen"),
				MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK, recht);
	}

	public PanelQuery(QueryType[] typesI, /** @todo saubaer PJ 5059 */
	FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI) throws Throwable {
		this(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI,
				add2TitleI, false);

		// super(internalFrameI, add2TitleI);
		//
		// if (typesI == null && isFilterIn(aWhichButtonIUseI)) {
		// throw new Throwable("typesI == null && isIn(aWhichButtonIUseI)");
		// }
		//
		// // filters = filtersI;
		// defaultFilter = filtersI;
		// types = typesI;
		// idUsecase = idUsecaseI;
		// // aWhichButtonIUse = aWhichButtonIUseI;
		//
		// jbInit();
		// initComponents();
		//
		// setDefaults();
		// if (aWhichButtonIUseI != null) {
		// enableToolsPanelButtons(aWhichButtonIUseI);
		// }
	}

	public PanelQuery(QueryType[] typesI, FilterKriterium[] filtersI,
			int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,
			boolean refreshWhenYouAreSelectedI,
			FilterKriterium kritVersteckteFelderNichtAnzeigenI)
			throws Throwable {

		super(internalFrameI, add2TitleI);

		/*
		 * Laut CK darf typesI durchaus null sein. Daher die Pr&uuml;fung entfernt
		 * (ghp) if (typesI == null && isFilterIn(aWhichButtonIUseI)) { throw
		 * new Throwable("typesI == null && isIn(aWhichButtonIUseI)"); }
		 */

		// filters = filtersI;

		this.defaultFilter = filtersI;
		types = typesI;
		idUsecase = idUsecaseI;
		refreshWhenYouAreSelected = refreshWhenYouAreSelectedI;

		jbInit();

		befuellePanelFilterkriterienDirektUndVersteckte(null, null,
				kritVersteckteFelderNichtAnzeigenI);

		initComponents();

		setDefaults();

		if (aWhichButtonIUseI != null) {
			enableToolsPanelButtons(aWhichButtonIUseI);
		}

	}

	public PanelQuery(QueryType[] typesI, /** @todo saubaer PJ 5059 */
	FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,
			boolean refreshWhenYouAreSelectedI) throws Throwable {

		super(internalFrameI, add2TitleI);

		/*
		 * Laut CK darf typesI durchaus null sein. Daher die Pr&uuml;fung entfernt
		 * (ghp) if (typesI == null && isFilterIn(aWhichButtonIUseI)) { throw
		 * new Throwable("typesI == null && isIn(aWhichButtonIUseI)"); }
		 */

		// filters = filtersI;
		this.defaultFilter = filtersI;
		this.defaultFilter2Restore = filtersI;
		types = typesI;
		idUsecase = idUsecaseI;
		refreshWhenYouAreSelected = refreshWhenYouAreSelectedI;

		jbInit();
		initComponents();

		setDefaults();

		if (aWhichButtonIUseI != null) {
			enableToolsPanelButtons(aWhichButtonIUseI);
		}

	}

	/**
	 * Konstruktor fuer eine FLR Liste, in der ein bestimmter Datensatz
	 * selektiert ist. <br>
	 * Der Key dieses Datensatzes wird als Parameter uebergeben.
	 * 
	 * @param typesI
	 *            die UI Filterkriterien fuer den Benutzer
	 * @param filtersI
	 *            die default Filterkriterien, die fuer den Benutzer nicht
	 *            sichtbar sind
	 * @param idUsecaseI
	 *            die ID des gewuenschten UseCase
	 * @param aWhichButtonIUseI
	 *            welche Buttons sind auf dem Panel sichtbar
	 * @param internalFrameI
	 *            den InternalFrame als Kontext setzen
	 * @param add2TitleI
	 *            der Titel dieses Panels
	 * @param oSelectedIdI
	 *            der Datensatz mit diesem Key soll selektiert werden
	 * @throws Throwable
	 */
	public PanelQuery(QueryType[] typesI, /** @todo saubaer PJ 5059 */
	FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI, Object oSelectedIdI)
			throws Throwable {
		this(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI,
				add2TitleI, false);

		// super(internalFrameI, add2TitleI);
		//
		// if (typesI == null && isFilterIn(aWhichButtonIUseI)) {
		// throw new Throwable("typesI == null && isIn(aWhichButtonIUseI)");
		// }
		//
		// types = typesI;
		// defaultFilter = filtersI;
		// idUsecase = idUsecaseI;
		// refreshWhenYouAreSelected = false;
		//
		// jbInit();
		//
		// setDefaults();
		//
		// if (aWhichButtonIUseI != null) {
		// enableToolsPanelButtons(aWhichButtonIUseI);
		// }

		// jetzt mussen wir dafuer sorgen, dass der richtige Datensatz
		// selektiert ist
		tableModel.sort(null, oSelectedIdI);

		scrollAndSelect();
	}

	/**
	 * isIn
	 * 
	 * @param aWhichButtonIUseI
	 *            String[]
	 * @return boolean
	 */
	// private boolean isFilterIn(String[] aWhichButtonIUseI) {
	// boolean found = false;
	// if (aWhichButtonIUseI != null) {
	// for (int i = 0; i < aWhichButtonIUseI.length; i++) {
	// if (aWhichButtonIUseI[i].equals(ACTION_FILTER)) {
	// found = true;
	// break;
	// }
	// }
	// }
	// return found;
	// }

	/**
	 * Setze alle Defaultwerte; hier koennen jetzt "schwerer" Methoden
	 * aufgerufen werden. jbinit: 4
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {

		ArbeitsplatzparameterDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.holeArbeitsplatzparameter(
						ParameterFac.ARBEITSPLATZPARAMETER_MAX_ANZAHL_IN_AUSWAHLLISTEN);

		if (parameter != null && parameter.getCWert() != null) {
			iAnzahlZeilen = new Integer(parameter.getCWert());
		}

		if (!refreshWhenYouAreSelected) {
			eventActionRefresh(null, false);
		}

		// jetzt fix: R
		String[] aWhichButtonIUse = {
		// PanelBasis.ACTION_NEW,
		PanelBasis.ACTION_REFRESH
		// PanelBasis.ACTION_FILTER,
		};

		// welche buttons brauchen wir?
		enableButtonAction(aWhichButtonIUse);

		// @uw : initiale Markierung der ersten Zeile nach Listenaufbau
		if (table.getModel().getRowCount() > 0) {
			table.setRowSelectionInterval(0, 0);
		}
	}

	/**
	 * Initialisiere alle Komponenten; braucht der JBX-Designer; hier bitte
	 * keine wilden Dinge wie zum Server gehen, etc. machen. jbinit: 3
	 * 
	 * @throws Exception
	 */

	public JPanel getPanelButtons() {
		return getToolBar().getToolsPanelRight();
	}

	public JPanel getJpButtons() {
		return getToolBar().getToolsPanelRight();
	}

	public JCheckBox getCbSchnellansicht() {
		return cbSchnellansicht;
	}

	public Object getKeyOfFilterComboBox() {
		return wcoFilter.getKeyOfSelectedItem();
	}

	public void setKeyOfFilterComboBox(Object key) {
		if (wcoFilter != null) {
			wcoFilter.setKeyOfSelectedItem(key);
		}
	}

	private void jbInit() throws Exception {
		// MB 19.09.06 damit ich das table-selection-event mitkriege
		getInternalFrame().addItemChangedListener(this);
		setLayout(new GridBagLayout());

		panelButtonAction = getToolsPanel();

		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// jpButtons = new JPanel();
		// GridBagLayout gridBagLayoutAll = new GridBagLayout();
		// jpButtons.setLayout(gridBagLayoutAll);

		// PJ 14497
		cbSchnellansicht.setText(LPMain
				.getTextRespectUISPr("lp.schnellansicht"));
		cbSchnellansicht.setVisible(false);
		cbSchnellansicht.setSelected(true);
		getToolBar().getToolsPanelRight().add(cbSchnellansicht);

		// SK Dokumentenablage
		getToolBar().addButtonRight(
				"/com/lp/client/res/document_attachment16x16.png",
				LPMain.getTextRespectUISPr("lp.dokumentablage"),
				LEAVEALONE_DOKUMENTE, null, null);

		// CK: Listen-Druck-Icon einbauen
		getToolBar().addButtonRight("/com/lp/client/res/table_sql_view.png",
				LPMain.getTextRespectUISPr("lp.flrdruck"),
				LEAVEALONE_PRINTPANELQUERY, null, null);

		// RK: Tabelleneinstellungen-speichern Button
		getToolBar()
				.addButtonRight(
						"/com/lp/client/res/table_sql_add.png",
						LPMain.getTextRespectUISPr("lp.tabelle.einstellungen.speichern"),
						LEAVEALONE_QUERYVIEW_SAVE, null, null);

		jbDokumente = getHmOfButtons().get(LEAVEALONE_DOKUMENTE).getButton();
		enableToolsPanelButtons(true, LEAVEALONE_QUERYVIEW_SAVE,
				LEAVEALONE_PRINTPANELQUERY);

		panelWorkingOn = new JPanel();
		panelWorkingOn.setLayout(new GridBagLayout());

		add(panelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// alle weiteren Panels und die Tabelle werden auf das panelWorkingOn
		// gesetzt
		// Auf diesem Panel sitzt die ComboBox mit den moeglichen
		// Filterkriterien
		panelKriterium = new JPanel();
		panelKriterium.setLayout(new GridBagLayout());
		panelWorkingOn.add(panelKriterium, new GridBagConstraints(0, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Auf diesem Panel sitzen die zuschaltbaren QueryInputRows
		iZeile++;

		panelFilterzeilen = new JPanel();
		panelFilterzeilen.setLayout(new GridBagLayout());
		panelFilterzeilen.setAlignmentY((float) 0.5);
		panelFilterzeilen.setPreferredSize(new Dimension(300, 1));
		panelWorkingOn.add(panelFilterzeilen, new GridBagConstraints(0, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Auf diesem Panel sitzen die direkt eingebbaren Filterkriterien
		iZeile++;

		panelFilter = new JPanel();
		panelFilter.setLayout(new GridBagLayout());
		// panelFilterkriterienDirekt.setBorder(new EtchedBorder());
		panelWorkingOn.add(panelFilter, new GridBagConstraints(0, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// n+1 zeile: tabelle
		iZeile++;
		if (sortierKriterien != null) {
			sortierKriterien.clear();
		}
		if (currentSortierKriterien != null) {
			currentSortierKriterien.clear();
		}
		sortierKriterien = new HashMap<String, SortierKriterium>();
		List<SortierKriterium> tmpList = ClientPerspectiveManager.getInstance()
				.loadQueryColumnSorting(idUsecase);

		// setQueryViewSaveBtnLoaded(tmpList != null && tmpList.size() > 0);
		if (tmpList == null)
			currentSortierKriterien = new ArrayList<SortierKriterium>();
		else
			currentSortierKriterien = new ArrayList<SortierKriterium>(tmpList);

		// den Datenbestand fuellen
		dataSource = new DistributedTableDataSourceImpl(new Integer(idUsecase));

		// die UI Namen aller moeglichen Sortierkriterien mit den Spaltennamen
		// auf der Tabelle verknuepfen
		TableInfo helper = this.dataSource.getTableInfo();
		dbColumnNames = helper.getDataBaseColumnNames();
		Object[] columnNames = this.dataSource.getTableInfo()
				.getColumnHeaderValues();

		for (int i = 0; i < columnNames.length; i++) {
			SortierKriterium kriterium = null;
			for (SortierKriterium krit : currentSortierKriterien) {
				if (dbColumnNames[i].toString().equals(krit.kritName)) {
					kriterium = krit;
					break;
				}
			}
			if (kriterium == null) {
				kriterium = new SortierKriterium(dbColumnNames[i], true,
						SortierKriterium.SORT_ASC);
			}
			sortierKriterien.put(columnNames[i].toString(), kriterium);
		}

		boolean clearKriterien = false;

		// ueberpruefen ob die geladenen Sortierkriterien den Spalten zugewiesen
		// werden koennen
		List<String> columnNamesList = Arrays.asList(dbColumnNames);
		for (SortierKriterium krit : currentSortierKriterien) {
			if (!columnNamesList.contains(krit.kritName)) {
				clearKriterien = true;
				break;
			}
		}
		// Sortierkriterien loeschen wenn Spalte fuer mind. ein Kriterium nicht
		// vorhanden ist
		if (clearKriterien)
			currentSortierKriterien.clear();

		// den Datenbestand dem TableModel zuordnen
		tableModel = new DistributedTableModelImpl(this.dataSource);

		// die Tabelle aufbauen
		table = new WrapperTable(getInternalFrame(), tableModel);
		table.setLocale(LPMain.getInstance().getUISprLocale());
		// per Default keine Mehrfachselektierung erlaubt
		this.setMultipleRowSelectionEnabled(bMultipleRowSelectionEnabled);
		table.setColumnSelectionAllowed(false);

		table.addKeyListener(bildAufAbListener); // ich werde von allen
													// KeyEvents auf der
		// Tabelle informiert
		table.addMouseListener(this); // ich werde von allen MouseEvents auf der
		// Tabelle informiert

		// jetzt den TableHeader einstellen
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().addMouseListener(this); // ich werde von allen
		// MouseEvents auf den
		// TableHeader
		// informiert
		// table.getTableHeader().setDefaultRenderer(HelperClient.
		// getImageIconHeaderRenderer());
		// die Breite der Spalten einstellen

		initColumnsSize(dbColumnNames);

		// die Tabelle in ein ScrollPane einbetten und anzeigen
		tableScrollPane = new JScrollPane(table);
		tableScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		panelWorkingOn.add(tableScrollPane, new GridBagConstraints(0, iZeile,
				1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		multiselektInfo = new JPanel();
		multiselektInfo.setBackground(Color.yellow);
		multiselektInfo.setBorder(BorderFactory.createEmptyBorder());
		multiselektInfo.setLayout(new GridBagLayout());
		panelWorkingOn.add(multiselektInfo, new GridBagConstraints(0, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		hmDirektFilter = new LinkedHashMap<Integer, PanelFilterKriteriumDirekt>();
	}

	private void sortTableModel() throws ExceptionLP {
		SortierKriterium[] sortKriterien = new SortierKriterium[currentSortierKriterien
				.size()];
		sortKriterien = currentSortierKriterien.toArray(sortKriterien);

		if (queryParameters != null)
			queryParameters.setSortKrit(sortKriterien.length == 0 ? null
					: sortKriterien);

		myLogger.debug(SortierKriterium.arrayToString(sortKriterien));

		setVertauscheButtonsEnabled(sortKriterien.length == 0);

		// die Liste neu aufbauen, nach dem Sortieren ist wieder der aktuell
		// selektierte Datensatz markiert

		tableModel.sort(sortKriterien.length == 0 ? null : sortKriterien,
				getSelectedId());
	}

	private void initColumnsSize(String[] dbColumnNames) throws ExceptionLP {
		// int[] columnHeaderWidths = null;
		int[] columnHeaderWidths = ClientPerspectiveManager.getInstance()
				.loadQueryColumnWidthsAsArray(idUsecase);
		columnWidthsFromHandler = dataSource.getTableInfo()
				.getColumnHeaderWidths();
		if (columnHeaderWidths == null
				|| (columnHeaderWidths != null && dbColumnNames.length > columnHeaderWidths.length)) {
			// wenn keine einstellungen gefunden/geladen
			// oder spaltenanzahl geaendert dann die breiten aus dem UCHandler
			if (columnWidthsFromHandler != null) {
				strechColumns = true;
				columnHeaderWidths = new int[columnWidthsFromHandler.length];
				for (int i = 0; i < columnWidthsFromHandler.length; i++) {
					if (columnWidthsFromHandler[i] != -1)
						columnHeaderWidths[i] = Helper.getBreiteInPixel(Math
								.abs(columnWidthsFromHandler[i]));
					else
						columnHeaderWidths[i] = -1;
				}
			}
		} else if (columnHeaderWidths != null) {
			setQueryViewSaveBtnLoaded(true);
		}

		// die erste Spalte verstecken
		setColumnWidthToZero(0);

		// die letzte Spalte Verstecken, wenn Color
		Class<?> letzteKlasse = dataSource.getColumnClasses()[dataSource
				.getColumnClasses().length - 1];
		if (letzteKlasse.equals(java.awt.Color.class)) {
			setColumnWidthToZero(dataSource.getColumnClasses().length - 1);
		}

		if (columnHeaderWidths != null) {

			// table.setRowSorter(new TableRowSorter<TableModel>(tableModel));
			// TableRowSorter<?> sorter =
			// (TableRowSorter<?>)table.getRowSorter();
			// table.setRowSorter(sorter);

			// die Breite aller weiteren Spalten setzen
			for (int i = 1; i < columnHeaderWidths.length
					&& i < table.getColumnModel().getColumnCount(); i++) {
				TableColumn tc = table.getColumnModel().getColumn(i);

				if (tc.getHeaderValue() instanceof String
						&& i < dbColumnNames.length) {
					JLabel jlaLabel;
					if (dbColumnNames[i]
							.equals(com.lp.server.util.Facade.NICHT_SORTIERBAR)) {
						jlaLabel = new JLabel((String) tc.getHeaderValue(),
								iNoSort, SwingConstants.LEADING);

						// sorter.setSortable(i, false);
					} else {
						jlaLabel = new JLabel((String) tc.getHeaderValue());
						// sorter.setSortable(i, true);

					}
					tc.setHeaderRenderer(new HeaderCellRenderer());
					jlaLabel.setToolTipText((String) tc.getHeaderValue());
					jlaLabel.setBorder(BorderFactory.createRaisedBevelBorder());
					tc.setHeaderValue(jlaLabel);
				}

				setHeaderColumnWidth(i, columnHeaderWidths[i]);
			}

		}

		table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (strechColumns && table.getWidth() > 0) {
			strechColumns = false;
			strechShareWithRestColumns(columnWidthsFromHandler);
			repaint();
		}
	}

	private void strechShareWithRestColumns(int[] columnWidths) {
		if (columnWidths == null)
			return;
		List<TableColumn> columnsToStrech = new ArrayList<TableColumn>();
		int alreadyDefinedSize = 0;
		for (int i = 1; i < columnWidths.length; i++) {
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

	public int getColumnWidth() {
		int iW = 0;
		try {
			iW = dataSource.getTableInfo().getColumnHeaderWidth();
		} catch (Throwable t) {
			myLogger.error("Handler hat keine Spaltenbreiten gesetzt!");
		}
		return iW;
	}

	public final static int BREITE_CHAR = 8;
	private String sAspect = null;

	/**
	 * Das ist der Inhalt des zuschaltbaren Panels mit der Auswahl der
	 * moeglichen Kriterien.
	 */
	private void createAndShowKriterium() {
		labelCriteria = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.kriterium"));

		comboBoxTypes = new WrapperComboBox(types);

		buttonAddFilterkriterium = createWrapperButtonActionListenerThis(
				"/com/lp/client/res/plus_sign.png",
				LPMain.getTextRespectUISPr("lp.tooltip.kriterium"),
				ACTION_ADD_FILTERKRITERIUM);
		buttonAddFilterkriterium.setEnabled(true);
		buttonAddFilterkriterium.setPreferredSize(new Dimension(Defaults
				.getInstance().getControlHeight(), Defaults.getInstance()
				.getControlHeight()));

		panelKriterium.add(labelCriteria, new GridBagConstraints(0, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		panelKriterium.add(comboBoxTypes, new GridBagConstraints(1, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		panelKriterium.add(buttonAddFilterkriterium, new GridBagConstraints(2,
				0, 1, 1, 0.1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.WEST, new Insets(2, 2, 2, 2), 0, 0));
	}

	public void setDefaultFilter(FilterKriterium[] defaultFilter) {
		this.defaultFilter = defaultFilter;
	}

	public FilterKriterium[] getDefaultFilter() {
		return defaultFilter;
	}

	public void setListOfExtraData(ArrayList<?> listOfExtraData) {
		this.listOfExtraData = listOfExtraData;
	}

	/**
	 * called whenever buttonAdd or buttonRefresh is clicked. Depending on the
	 * clicked button either a new QueryInputRow is added to the view, or the
	 * current query is executed and the result gets displayed in the table.
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void lPPanelActionPerformed(ActionEvent e) {

		// else if (e.getSource().equals(this.buttonRefresh)) {
		// // @uw, 2004-08-30 add default filter kriterien
		// int size = 0;
		//
		// if (this.defaultFilter != null && this.defaultFilter.length > 0) {
		// size = +this.defaultFilter.length;
		// }
		//
		// if (this.queryInputRows != null && this.queryInputRows.size() > 0) {
		// size = +this.queryInputRows.size();
		// }
		//
		// // kriterien instanziieren
		// FilterKriterium[] kriterien = new FilterKriterium[size];
		//
		// // default kriterien
		// if (this.defaultFilter != null) {
		// for (int i = 0; i < this.defaultFilter.length; i++) {
		// kriterien[i] = this.defaultFilter[i];
		// }
		// }
		//
		// // GUI kriterien
		// if (this.queryInputRows != null) {
		// for (int i = 0; i < this.queryInputRows.size(); i++) {
		// kriterien[i] = ( (QueryInputRow)this.queryInputRows
		// .elementAt(i)).getKriterium();
		// }
		// }
		//
		// FilterBlock filter = new FilterBlock(kriterien,
		// FilterKriterium.BOOLOPERATOR_AND);
		//
		// SortierKriterium[] sortKriterien = new SortierKriterium[this.
		// currentSortierKriterien
		// .size()];
		// sortKriterien = (SortierKriterium[])this.currentSortierKriterien
		// .toArray(sortKriterien);
		// QueryParameters query = new QueryParameters(new
		// Integer(this.idUsecase),
		// sortKriterien,
		// filter,
		// this.getSelectedId());
		// this.tableModel.setQuery(query);
		// this.scrollAndSelect();
		// this.hideFirstColumn();
		// }
	}

	/**
	 * called whenever the user clicks on a column's header. The sorting is
	 * implemented here.
	 * 
	 * @param e
	 *            MouseEvent
	 * @throws ExceptionLP
	 */
	protected void eventMouseClicked(MouseEvent e) throws ExceptionLP {

		if (e.getSource().getClass() == WrapperTable.class) {
			// in die tabelle geklickt
			if (e.getClickCount() == 1) {
				/** @todo VF->JO funktioniert das? PJ 5060 */
				// getInternalFrame().fireItemChanged(this,
				// ItemChangedEvent.ITEM_CHANGED);
			}
			if (e.getClickCount() == 2) {
				fireItemChangedEvent_GOTO_DETAIL_PANEL();
			}
		} else if (e.getSource().getClass() == JTableHeader.class) {
			// auf tabellenheaderspalte geklickt -> sortieren
			int index = table.getColumnModel().getColumnIndexAtX(e.getX());

			if (dbColumnNames[index]
					.equals(com.lp.server.util.Facade.NICHT_SORTIERBAR)
					|| index == 0)
				return;

			String columnName = table.getColumnName(index);

			SortierKriterium tmpSortierKriterium = sortierKriterien
					.get(columnName);

			// @uw 2005-02-23 : Wir haben seit spaetestens Hibernate3 einen Bug,
			// wenn
			// man nach einem Kriterium in einer referenzierten FLR Klasse
			// sortiert:
			// Alle Zeilen, in denen das Kriterium NULL ist, fallen aus der
			// Ergebnis-
			// liste, Exception am Client, weil Zeilen fuer die bereits
			// bestehende
			// Tabelle fehlen -> Workaround: Nach diesen Spalten nicht sortieren
			// AUSNAHMEN: Wenn in diese Spalten auf der DB als NOT NULL
			// derfiniert sind
			// - partner.cname1nachnamefirmazeile1
			/** @todo JO ??? PJ 5060 */
			// if (tmpSortierKriterium.kritName.indexOf(PartnerFac.
			// FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1) !=
			// -1 ||
			// tmpSortierKriterium.kritName.indexOf(".") == -1) {
			boolean directionChanged = false;

			if (currentSortierKriterien.contains(tmpSortierKriterium)) {
				// change sort direction
				if (SortierKriterium.SORT_ASC.equals(tmpSortierKriterium.value)) {
					tmpSortierKriterium.value = SortierKriterium.SORT_DESC;
				} else {
					tmpSortierKriterium.value = SortierKriterium.SORT_ASC;
				}

				directionChanged = true;
			}

			if ((e.getModifiers() & InputEvent.CTRL_MASK) == 0) {
				// delete current sort -> switch back to single column sort
				this.currentSortierKriterien.clear();
			}

			if (!currentSortierKriterien.contains(tmpSortierKriterium)) {
				// add the column to the sorting
				if (!directionChanged) {
					// default search is ascending
					tmpSortierKriterium.value = SortierKriterium.SORT_ASC;
				}

				currentSortierKriterien.add(tmpSortierKriterium);
			}

			if (e.getButton() == 3) {
				if ((e.getModifiers() & InputEvent.CTRL_MASK) == 0)
					currentSortierKriterien.clear();
				else
					currentSortierKriterien.remove(tmpSortierKriterium);
			}

			// die Liste neu aufbauen, nach dem Sortieren ist wieder der aktuell
			// selektierte Datensatz markiert

			sortTableModel();
			scrollAndSelect();
			// }

			// else {
			// Object oHeaderValue =
			// table.getColumnModel().getColumn(index).getHeaderValue();
			// }
		} else if (e.getSource() == wcbVersteckteFelderAnzeigen) {
			try {
				eventActionRefresh(null, false);
			} catch (Throwable ex) {
				// @todo UW->JO Was tun mit dieser Exception? PJ 5060
				myLogger.error("UW->JO", ex);
			}
		} else if (e.getSource() == wcbErsteSortierungUebersteuern) {
			try {
				eventActionRefresh(null, false);
			} catch (Throwable ex) {
				// @todo UW->JO Was tun mit dieser Exception? PJ 5060
				myLogger.error("UW->JO", ex);
			}
		} else if (e.getSource() == cbSchnellansicht) {
			try {
				eventActionRefresh(null, false);
			} catch (Throwable ex) {
				// @todo UW->JO Was tun mit dieser Exception? PJ 5060
				myLogger.error("UW->JO", ex);
			}
		}
	}

	public Object holeKeyNaechsteZeile() throws Throwable {
		int iZeileCursor = table.getSelectionModel().getAnchorSelectionIndex();
		iZeileCursor++;
		return table.getValueAt(iZeileCursor, 0);

	}

	public Object holeKeyVorherigeZeile() throws Throwable {
		int iZeileCursor = table.getSelectionModel().getAnchorSelectionIndex();
		iZeileCursor--;
		return table.getValueAt(iZeileCursor, 0);

	}

	/**
	 * gets the id of the row currently selected in the table (this is not the
	 * row number!).
	 * 
	 * MB 19.09.06 aufgrund der mehrfachselektierung ist es auch moeglich, dass
	 * gar kein datensatz ausgewaehlt ist. in diesem fall zieht die zeile, in
	 * der der cursor steht.
	 * 
	 * sind mehrere zeilen selektiert, so soll die zuletzt markierte (cursor)
	 * ziehen.
	 * 
	 * @return the row's id.
	 */
	public Object getSelectedId() {
		Object selectedId = null; // key object des Datensatzes
		// ist mindestens eine zeile selektiert?
		if (table.getSelectedRow() >= 0
				&& table.getSelectedRow() < tableModel.getRowCount()) {
			int iZeileCursor = table.getSelectionModel()
					.getAnchorSelectionIndex();
			int[] iZeilenSelektiert = table.getSelectedRows();
			boolean bZeileMitCursorIstSelektiert = false;
			for (int i = 0; i < iZeilenSelektiert.length; i++) {
				if (iZeilenSelektiert[i] == iZeileCursor) {
					bZeileMitCursorIstSelektiert = true;
				}
			}
			if (bZeileMitCursorIstSelektiert) {
				selectedId = table.getValueAt(iZeileCursor, 0);
			}
			// Zeile mit Cursor ist nicht selektiert. daher zieht die erste
			// selektierte.
			else {
				selectedId = table.getValueAt(table.getSelectedRow(), 0);
			}
		}
		// keine zeile selektiert
		else {
			// wenn die Tabelle aber nicht leer ist, dann gilt die zeile, in der
			// der cursor steht als selektiert.
			if (table.getRowCount() >= 1) {
				int iZeileCursor = table.getSelectionModel()
						.getAnchorSelectionIndex();
				if (iZeileCursor == -1) { // nichts selektiert
					selectedId = table.getValueAt(0, 0); // erste Zeile
					// selektieren
				} else {
					selectedId = table.getValueAt(iZeileCursor, 0);
				}
			}
		}
		// AD: System.out.println("UC: " + this.idUsecase + " Selected ID: " +
		// selectedId);

		return selectedId;
	}

	/**
	 * gets the id of the row currently selected in the table (this is not the
	 * row number!).
	 * 
	 * @return the row's id.
	 */
	public Object[] getSelectedIds() {
		Object[] selectedIds = null; // key object des Datensatzes
		if (table.getSelectedRow() >= 0
				&& table.getSelectedRow() < tableModel.getRowCount()) {
			selectedIds = new Object[table.getSelectedRowCount()];
			for (int i = 0; i < table.getSelectedRowCount(); i++) {
				selectedIds[i] = table
						.getValueAt(table.getSelectedRows()[i], 0);
			}
		}
		return selectedIds;
	}

	public void uebersteuereSpaltenUeberschrift(int spalte,
			String textUebersteuert) {

		JTableHeader header = getTable().getTableHeader();

		javax.swing.table.TableColumn c = header.getColumnModel().getColumn(
				spalte);

		if (c != null) {
			JLabel label = new JLabel(textUebersteuert);
			label.setBorder(BorderFactory.createRaisedBevelBorder());
			c.setHeaderValue(label);
		}

	}

	/**
	 * gets the id of the row currently selected in the table (this is not the
	 * row number!).
	 * 
	 * @return the row's id.
	 */
	public Object getId2SelectAfterDelete() {
		Object selectedId = null; // key object des Datensatzes
		if (table.getSelectedRow() >= 0
				&& table.getSelectedRow() < tableModel.getRowCount()) {
			// wenn es darunter noch eine Zeile gibt, dann die
			if (table.getSelectedRow() <= (tableModel.getRowCount() - 2)) {
				selectedId = table.getValueAt(table.getSelectedRow() + 1, 0);
			}
			// sonst die darueber liegende
			else if (table.getSelectedRow() > 0) {
				selectedId = table.getValueAt(table.getSelectedRow() - 1, 0);
			}
		}
		return selectedId;
	}

	public Object getIdLastRow() {
		Object selectedId = null; // key object des Datensatzes
		if (tableModel.getRowCount() > 0) {
			selectedId = table.getValueAt(table.getRowCount() - 1, 0);
		}
		return selectedId;
	}

	// /**
	// * Der Tabelle wird die selektierte Zeile gesetzt.
	// * <br>Der Index der Zeile muss dafuer bekannt sein.
	// *
	// * @param iSelectedRowI iSelectedRowI
	// * @throws Throwable
	// */
	// public void setSelectedId(int iSelectedRowI)
	// throws Throwable {
	//
	// if (iSelectedRowI > -1 && iSelectedRowI <= table.getRowCount()) {
	// table.setRowSelectionInterval(iSelectedRowI - 1, iSelectedRowI - 1);
	// }
	// else {
	// throw new Throwable("$Ungueltige Zeilennummer$");
	// }
	// }
	//
	//
	public void setSelectedId(Object oKeyI) throws Throwable {

		if (oKeyI != null) {
			// Jetzt muessen wir dafuer sorgen, dass der richtige Datensatz
			// selektiert ist.
			tableModel.sort(null, oKeyI);

			// AD: System.out.println("UC: " + this.idUsecase +
			// " Set Selected ID: " + oKeyI.toString());

			scrollAndSelect();
			getSelectedId();
		}
	}

	/**
	 * whenever a sort occured, this method asures that the row that was
	 * selected before the sort operation is selected and visisble again.
	 */
	private void scrollAndSelect() {
		int indexOfSelectedRow = this.tableModel.getIndexOfSelectedRow();

		if (indexOfSelectedRow >= 0
				&& indexOfSelectedRow < this.table.getRowCount()) {
			int totalRows = this.table.getRowCount();
			int scrollMax = this.table.getRowHeight() * totalRows;
			// myLogger.info("scrollMax = " + scrollMax);
			int scrollValue = (scrollMax / totalRows) * indexOfSelectedRow;
			// myLogger.info("scroll to = " + scrollValue);

			// @uw Hier wird die Scrollbarhoehe bestimmt und dem Scroll Pane
			// gesetzt.
			// Durch ein Flag wird bestimmt, dasz bei der Abfrage lediglich ein
			// count
			// abgesetzt wird und keine Daten geholt werden.
			this.dataSource.setReturnNullOnGetValueAt(true);
			this.tableModel.fireTableDataChanged();
			this.tableScrollPane.getVerticalScrollBar().setValue(scrollValue);
			this.dataSource.setReturnNullOnGetValueAt(false);

			// @uw Hier werden die FLR Daten der Tabelle aktualisiert.
			this.tableModel.fireTableDataChanged();

			// @uw die augenblickliche Selektion ist mit der Tabelle verdrahtet
			// und
			// aendert sich bei einer Umsortierung nicht automatisch.
			// Neuerliches Lesen nicht erforderlich, zerstoert die
			// farbliche Markierung der Zeile, die zuvor gesetzt wurde!
			this.table.clearSelection();
			this.table.setRowSelectionInterval(indexOfSelectedRow,
					indexOfSelectedRow);
			Rectangle rect = table.getCellRect(indexOfSelectedRow, 0, true);
			table.scrollRectToVisible(rect);
			// myLogger.debug("1 Index of selected row = " +
			// indexOfSelectedRow);
		} else {
			// @uw wenn keine positionen vorhanden sind, muss eine eventuelle
			// markierung geloescht werden
			this.table.clearSelection();
			this.tableModel.fireTableDataChanged();
		}
	}

	public void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		long tStart = System.currentTimeMillis();
		// die fuer die Abfrage zu verwendenden FilterKriterien sammeln
		ArrayList<FilterKriterium> alFilterKriterien = new ArrayList<FilterKriterium>();

		// die default FilterKriterien einhaengen
		if (defaultFilter != null && defaultFilter.length > 0) {
			for (int i = 0; i < defaultFilter.length; i++) {
				alFilterKriterien.add(defaultFilter[i]);
			}
		}

		// die UI Zeilen mit FilterKriterien einhaengen
		if (queryInputRows != null && queryInputRows.size() > 0) {
			for (int i = 0; i < queryInputRows.size(); i++) {
				alFilterKriterien.add(queryInputRows.elementAt(i)
						.getKriterium());
			}
		}
		// Alle DirektFilter einhaengen
		for (Iterator<Integer> iter = hmDirektFilter.keySet().iterator(); iter
				.hasNext();) {
			PanelFilterKriteriumDirekt panelFkd = hmDirektFilter.get(iter
					.next());
			if (panelFkd.fkd != null
					&& panelFkd.fkd.iTyp == FilterKriteriumDirekt.TYP_STRING
					&& panelFkd.wtfFkdirektValue1.getText() != null
					&& panelFkd.wtfFkdirektValue1.getText().length() > 0) {
				// den eingegeben Wert bestimmen, bereinigen und richtig
				// verpacken
				String sValue = panelFkd.wtfFkdirektValue1.getText()
						.replaceAll("'", "");
				panelFkd.wtfFkdirektValue1.setText(sValue); // korrigierten wert
				// zurueckschreiben.

				panelFkd.fkd.value = sValue;
				panelFkd.fkd.wrapWithProzent();
				panelFkd.fkd.wrapWithSingleQuotes();

				alFilterKriterien.add(new FilterKriterium(
						panelFkd.fkd.kritName, true, panelFkd.fkd.value,
						panelFkd.fkd.operator, panelFkd.fkd.isBIgnoreCase()));
			}
			if (panelFkd.fkd != null
					&& panelFkd.fkd.iTyp == FilterKriteriumDirekt.TYP_DECIMAL
					&& panelFkd.wnfFkdirektValue1.getBigDecimal() != null) {
				// den eingegeben Wert bestimmen, bereinigen und richtig
				// verpacken
				alFilterKriterien.add(new FilterKriterium(
						panelFkd.fkd.kritName, true, panelFkd.wnfFkdirektValue1
								.getBigDecimal(), panelFkd.fkd.operator,
						panelFkd.fkd.isBIgnoreCase()));
			}

		}

		// Filterkriterium fuer Schnellansicht einhaengen
		if (cbSchnellansicht.isEnabled() && cbSchnellansicht.isSelected()
				&& fkSchnellansicht != null) {

			for (int i = 0; i < fkSchnellansicht.length; i++) {
				alFilterKriterien.add(fkSchnellansicht[i]);
			}
		}

		if (fkComboBox != null && wcoFilter.getKeyOfSelectedItem() != null) {

			if (fkComboBox.wrapWithSingleQuotes == true) {
				fkComboBox.value = "'" + wcoFilter.getKeyOfSelectedItem() + "'";
			} else {
				fkComboBox.value = wcoFilter.getKeyOfSelectedItem() + "";
			}

			alFilterKriterien.add(fkComboBox);
		}

		// Filterkriterium fuer Schnellansicht einhaengen
		if (fkKey != null) {

			for (int i = 0; i < fkKey.length; i++) {
				alFilterKriterien.add(fkKey[i]);
			}
		}

		// das FilterKriterium fuer die Anzeige der versteckten Felder
		// einhaengen
		if (fkVersteckteFelderNichtAnzeigen != null) {
			// workaround: Wenn die CheckBox mit der SpaceBar gesetzt wurde, ist
			// der Wert verdreht
			if (e != null
					&& e.getActionCommand().equals(
							"WORKAROUND_ZWEITER_PARAMETER_IST_KEYCODE")) {
				if (wcbVersteckteFelderAnzeigen.isSelected()) { // genau
					// verkehrt!
					alFilterKriterien.add(fkVersteckteFelderNichtAnzeigen);
				}
			} else {
				// den eingegebenen Wert bestimmen und richtig verpacken
				if (!wcbVersteckteFelderAnzeigen.isSelected()) {
					alFilterKriterien.add(fkVersteckteFelderNichtAnzeigen);
				}
				// else: alle Felder anzeigen, d.h. dass das Kriterium nicht
				// uebergeben wird
			}
		}

		// das Array der FilterKriterien instanziieren und befuellen
		FilterKriterium[] kriterien = new FilterKriterium[alFilterKriterien
				.size()];

		for (int i = 0; i < alFilterKriterien.size(); i++) {
			kriterien[i] = alFilterKriterien.get(i);
		}

		// alle FilterKriterien werden in einem Block verpackt und sind mit AND
		// verknuepft!
		FilterBlock filter = new FilterBlock(kriterien,
				FilterKriterium.BOOLOPERATOR_AND);

		// PJ18155

		ArrayList<SortierKriterium> kritsWithUebersteuertem = new ArrayList<SortierKriterium>();

		if (sortierkriteriumErsteSortierungUebersteuern != null) {
			if (wcbErsteSortierungUebersteuern != null
					&& wcbErsteSortierungUebersteuern.isSelected()) {
				kritsWithUebersteuertem
						.add(sortierkriteriumErsteSortierungUebersteuern);
			}
		}

		for (int i = 0; i < currentSortierKriterien.size(); i++) {
			kritsWithUebersteuertem.add(currentSortierKriterien.get(i));
		}

		SortierKriterium[] sortKriterien = kritsWithUebersteuertem
				.toArray(new SortierKriterium[0]);

		queryParameters = new QueryParameters(new Integer(idUsecase),
				sortKriterien, filter, this.getSelectedId(), listOfExtraData);

		queryParameters.iMaxAnzahlZeilen = iAnzahlZeilen;

		tableModel.setQuery(queryParameters);

		// sortTableModel();

		scrollAndSelect();

		// die erste Spalte verstecken
		// ist doch schon alles gesetzt, warum nochmal?
		// setColumnWidthToZero(0);
		// // die letzte Spalte Verstecken, wenn Color
		// Class<?> letzteKlasse = dataSource.getColumnClasses()[dataSource
		// .getColumnClasses().length - 1];
		// if (letzteKlasse.equals(java.awt.Color.class)) {
		// setColumnWidthToZero(dataSource.getColumnClasses().length - 1);
		// }

		long tEnd = System.currentTimeMillis();
		myLogger.info("refresh FLR Liste UC=" + idUsecase + ": "
				+ (tEnd - tStart) + " ms");
		// @uw bei einem PanelSplit muss die Detailanzeige auch refresh kriegen
		fireItemChangedEvent_ITEM_CHANGED();
	}

	/**
	 * Die Breite einer Spalte festlegen.
	 * 
	 * @param iColumnIndex
	 *            der Index der Spalte
	 * @param iColumnWidth
	 *            die Breite der Spalte in Zeichen
	 */
	private void setHeaderColumnWidth(int iColumnIndex, int iColumnWidth) {

		// 28.02.13 RK: alle Spalten sind ab sofort variabel!

		TableColumn tc = table.getTableHeader().getColumnModel()
				.getColumn(iColumnIndex);
		// if (iColumnWidth != QueryParameters.FLR_BREITE_SHARE_WITH_REST) {
		tc.setPreferredWidth(iColumnWidth);
		tc.setWidth(iColumnWidth);
		// tc.setResizable(false);
		// }
		tc.setMinWidth(Helper
				.getBreiteInPixel(QueryParameters.FLR_BREITE_MINIMUM));

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
	 * eventActionFilter
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionFilter(ActionEvent e) throws Throwable {
		super.eventActionFilter(e);

		if (isFilterPushed()) {
			createAndShowKriterium();
		} else {
			panelFilterzeilen.removeAll();
			panelFilterzeilen.setMinimumSize(new Dimension(300, 1));
			panelFilterzeilen.setPreferredSize(new Dimension(300, 1));
			panelKriterium.removeAll();
			queryInputRows.removeAllElements();
		}
		SwingUtilities.getRootPane(this).revalidate();
		SwingUtilities.getRootPane(this).repaint();
	}

	private void setQueryViewSaveBtnLoaded(boolean b) {
		ImageIcon icon = HelperClient.createImageIcon("table_sql_"
				+ (b ? "check" : "add") + ".png");
		getHmOfButtons().get(LEAVEALONE_QUERYVIEW_SAVE).getButton()
				.setIcon(icon);
	}

	/**
	 * Event Spezial wurde ausgeloest; das sind alle die du hier selbst
	 * definierst; dh. du wirst aufgerufen.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		// spezialbutton: wird wieder null gesetzt
		sAspect = null;

		if (e.getSource().equals(wcoFilter)) {

			// SP1600
			if (idUsecase == QueryParameters.UC_ID_ARTIKELLISTE
					&& getInternalFrame() != null) {
				if (wcoFilter.getKeyOfSelectedItem() != null
						&& wcoFilter.getKeyOfSelectedItem() instanceof Integer) {
					getInternalFrame().setILetzteGewaehlteArtikelgruppenIId(
							(Integer) wcoFilter.getKeyOfSelectedItem());
				} else {
					getInternalFrame().setILetzteGewaehlteArtikelgruppenIId(
							null);
				}
			}

			eventActionRefresh(null, false);
		}

		if (e.getActionCommand().equals(LEAVEALONE_QUERYVIEW_SAVE)) {
			saveQueryViewSettings();
			setQueryViewSaveBtnLoaded(true);
		}

		else if (e.getActionCommand().equals(ACTION_ADD_FILTERKRITERIUM)) {

			QueryType tmpType = (QueryType) this.comboBoxTypes
					.getSelectedItem();

			QueryInputRow newRow = new QueryInputRow(tmpType, this);
			Dimension d = new Dimension(10, 30);
			newRow.setMinimumSize(d);
			newRow.setPreferredSize(d);
			queryInputRows.add(newRow);

			panelFilterzeilen.add(newRow, new GridBagConstraints(0,
					panelFilterzeilen.getComponentCount(), 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(2, 2, 2, 2), 0, 0));

			Dimension oldSize = panelFilterzeilen.getSize();
			Dimension newSize = new Dimension((int) oldSize.getWidth(),
					(int) oldSize.getHeight() + 33);
			panelFilterzeilen.setMinimumSize(newSize);
			panelFilterzeilen.setPreferredSize(newSize);

			if (panelFilterzeilen.getComponentCount() >= MAX_FILTER) {
				buttonAddFilterkriterium.setEnabled(false);
			}
			SwingUtilities.getRootPane(this).revalidate();
			SwingUtilities.getRootPane(this).repaint();
		}

		else if (e.getActionCommand().equals(ACTION_REMOVE_FILTERKRITERIUM)) {
			// loesche das panel wo der button drauf ist.
			Component c = (((JButton) e.getSource()).getParent());
			panelFilterzeilen.remove(c);
			queryInputRows.remove(c);
			if (panelFilterzeilen.getComponentCount() < MAX_FILTER) {
				buttonAddFilterkriterium.setEnabled(true);
			}
			Dimension oldSize = panelFilterzeilen.getSize();
			Dimension newSize = new Dimension((int) oldSize.getWidth(),
					(int) oldSize.getHeight() - 33);
			panelFilterzeilen.setMinimumSize(newSize);
			panelFilterzeilen.setPreferredSize(newSize);

			SwingUtilities.getRootPane(this).revalidate();
			SwingUtilities.getRootPane(this).repaint();
		}

		else if (e.getActionCommand().equals(
				ACTION_POSITION_VORPOSITIONEINFUEGEN)) {
			// posreihung: 1 eindeutiger spezieller Neu Button

			// btnstate: 7 Wie bei neu schalten.
			LockStateValue l = new LockStateValue(null, null, LOCK_FOR_NEW);
			updateButtons(l);

			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN);
		}

		else if (e.getActionCommand().startsWith(ACTION_MY_OWN_NEW)) {
			// allgemeiner spezieller Neu Button: sAspect String wird mit button
			// info befuellt
			sAspect = e.getActionCommand();

			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_MY_OWN_NEW);
		}

		else if (e.getActionCommand().indexOf("special") != -1) {
			// spezialbutton: sAspect String wird mit button info befuellt
			sAspect = e.getActionCommand();

			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_SPECIAL_BUTTON);
		}

		else if (e.getActionCommand().equals(ACTION_POSITION_VONNNACHNMINUS1)) {
			// posreihung: 2 die Actions der Buttons fuer die Reihung der
			// Positionen weitergeben
			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1);
		}

		else if (e.getActionCommand().equals(ACTION_POSITION_VONNNACHNPLUS1)) {
			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1);
		} else if (e.getActionCommand().equals(ACTION_KOPIEREN)) {
			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_KOPIEREN);
		} else if (e.getActionCommand().equals(ACTION_EINFUEGEN_LIKE_NEW)) {
			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_EINFUEGEN);
		}

		else if (e.getActionCommand().startsWith(LEAVEALONE)) {
			if (e.getActionCommand().equals(LEAVEALONE_PRINTPANELQUERY)) {
				// LISTE DRUCKEN

				if (this instanceof PanelQueryFLR) {
					PanelQueryFLR thisPanel = (PanelQueryFLR) this;
					thisPanel.getDialog().setVisible(false);
				}
				getInternalFrame().showReportKriterien(
						new ReportPanelQuery(getDataSource().getUuid(),
								getInternalFrame(), "", queryParameters));
			}
			if (e.getActionCommand().equals(LEAVEALONE_DOKUMENTE)) {
				// Dokumentenablage oeffnen
				Object sKey = null;
				if (bBenutzeUebersteuerteId == true) {
					sKey = getUebersteuerteId();
				} else {
					sKey = getSelectedId();
				}

				PrintInfoDto values = DelegateFactory.getInstance()
						.getJCRDocDelegate()
						.getPathAndPartnerAndTable(sKey, idUsecase);

				DocPath docPath = values.getDocPath();
				Integer iPartnerIId = values.getiId();
				String sTable = values.getTable();
				if (docPath != null) {
					panelDokumentenverwaltung = new PanelDokumentenablage(
							getInternalFrame(), "", docPath, sTable,
							sKey.toString(), true, iPartnerIId);
					getInternalFrame().showPanelDialog(
							panelDokumentenverwaltung);
					getInternalFrame().addItemChangedListener(
							panelDokumentenverwaltung);
				} else {
					// Show Dialog
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("jcr.hinweis.keinpfad"));
				}
			} else {
				getInternalFrame().fireItemChanged(this,
						ItemChangedEvent.ACTION_LEAVE_ME_ALONE_BUTTON);
			}
		}
	}

	@Override
	protected void eventKeyPressed(KeyEvent e) throws Throwable {
	}

	/**
	 * 
	 * @param e
	 *            KeyEvent
	 * @throws Throwable
	 */
	private class BildAufAbListener implements KeyListener, MouseListener {
		public void keyPressed(KeyEvent e) {
			try {
				// Direktfilter
				if (e.getSource().getClass() == WrapperTextField.class
						|| e.getSource().getClass() == WrapperNumberField.class) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER
							|| e.getKeyCode() == 10) {
						// die Abfrage mit den aktuellen Kriterien ausloesen
						if (!getBFilterHasChanged()) {
							// in der tabelle
							fireItemChangedEvent_ITEM_CHANGED();

							if (getSelectedId() != null) {
								fireItemChangedEvent_GOTO_DETAIL_PANEL();
							}
							eventKeyPressed(e);
							e.consume();
						} else {
							// in den direktfilter
							eventActionRefresh(null, false);
							setBFilterHasChanged(false);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_UP
							|| e.getKeyCode() == KeyEvent.VK_DOWN) {
						int iRowCount = table.getRowCount();
						int iSelectedRow = table.getSelectedRow();
						if (e.getKeyCode() == KeyEvent.VK_UP
								&& iSelectedRow > 0) {
							table.setRowSelectionInterval(iSelectedRow - 1,
									iSelectedRow - 1);
							// JO 24.4.06 opt. kein evt wenn key up.
							fireItemChangedEvent_ITEM_CHANGED();
						} else if (e.getKeyCode() == KeyEvent.VK_DOWN
								&& iSelectedRow < (iRowCount - 1)) {
							table.setRowSelectionInterval(iSelectedRow + 1,
									iSelectedRow + 1);
							// JO 24.4.06 opt. kein evt wenn key down.
							fireItemChangedEvent_ITEM_CHANGED();
						}
						int indexOfSelectedRow = table.getSelectedRow();
						table.scrollRectToVisible(table.getCellRect(
								indexOfSelectedRow, 0, true));
					} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP
							|| e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
						if (table.getRowCount() > 0) {
							int iSichtbareHoehe = (int) tableScrollPane
									.getViewport().getSize().getHeight();
							int iZellenHoehe = (int) table.getCellRect(0, 0,
									true).getHeight();
							int iSichtbareZeilen = iSichtbareHoehe
									/ iZellenHoehe;

							int iSelectedRow = table.getSelectedRow();
							if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
								iSelectedRow -= iSichtbareZeilen;
								if (iSelectedRow < 0) {
									iSelectedRow = 0;
								}
							} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
								iSelectedRow += iSichtbareZeilen;
								if (iSelectedRow >= (table.getRowCount() - 1)) {
									iSelectedRow = table.getRowCount() - 1;
								}
							}
							table.setRowSelectionInterval(iSelectedRow,
									iSelectedRow);
							table.scrollRectToVisible(table.getCellRect(
									iSelectedRow, 0, true));
							fireItemChangedEvent_ITEM_CHANGED();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_END) {
						if (table.getRowCount() > 0) {
							boolean wasVisible = table.isVisible();
							table.setVisible(false);
							int selectedRow = table.getRowCount() - 1;
							table.setRowSelectionInterval(selectedRow,
									selectedRow);
							table.scrollRectToVisible(table.getCellRect(
									selectedRow, 0, true));
							table.setVisible(wasVisible);
							fireItemChangedEvent_ITEM_CHANGED();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_HOME) {
						if (table.getRowCount() > 0) {
							/**
							 * Table auf non-visible setzen. Hintergrund: Der
							 * JTable repaint (scrollRectToVisible) Mechanismus
							 * nutzt anscheinend manchmal die zuletzt
							 * selektierte Zeile f&uuml;r den repaint. Das ist hier
							 * aber fatal, da dann noch mal die Daten aus der
							 * Datenbank gezogen werden. Deshalb als Workaround
							 * auf non-visible setzen. Kann auch ein "Bug" von
							 * sun/oracle sein. Keine Ahnung.
							 * 
							 * Gerold, 6.12.2011
							 */
							boolean wasVisible = table.isVisible();
							table.setVisible(false);
							table.setRowSelectionInterval(0, 0);
							table.scrollRectToVisible(table.getCellRect(0, 0,
									true));
							table.setVisible(wasVisible);
							fireItemChangedEvent_ITEM_CHANGED();
						}
					} else {
						setBFilterHasChanged(true);
					}
				}

				// JO 15.11.05 key up down in QP jetzt richtig; comment to del
				// 15.01.06
				// else if (e.getSource() == table) {
				// if (e.getKeyCode() == KeyEvent.VK_DOWN) { // Pfeil nach unten
				// getInternalFrame().fireItemChanged(this,
				// ItemChangedEvent.ITEM_CHANGED);
				// }
				// else if (e.getKeyCode() == KeyEvent.VK_UP) { // Pfeil nach
				// oben
				// getInternalFrame().fireItemChanged(this,
				// ItemChangedEvent.ITEM_CHANGED);
				// }
				// else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// getInternalFrame().fireItemChanged(this,
				// ItemChangedEvent.GOTO_DETAIL_PANEL);
				// e.consume();
				// }
				// }

				// @todo UW->JO egal ob keyPressed, keyTyped, keyRealeased, ich
				// bekomme
				// immer den Vorzustand der CheckBox, Swing Fehler? PJ 5068
				else if (e.getSource() == wcbVersteckteFelderAnzeigen) {
					if (e.getKeyChar() == KeyEvent.VK_SPACE
							|| e.getKeyCode() == 32) {
						// workaround in Abstimmung mit JO
						ActionEvent ae = new ActionEvent(this, e.getKeyCode(),
								"WORKAROUND_ZWEITER_PARAMETER_IST_KEYCODE");
						eventActionRefresh(ae, false); // UW->JO
					} else {
						eventKeyPressed(e);
						e.consume();
					}
				} else if (e.getSource() == table) {
					if (e.getKeyCode() == KeyEvent.VK_UP
							|| e.getKeyCode() == KeyEvent.VK_DOWN) {
						int iRowCount = table.getRowCount();
						int iSelectedRow = table.getSelectedRow();
						if (e.getKeyCode() == KeyEvent.VK_UP
								&& iSelectedRow > 0) {
							// ListSelectionEvent muss ausgeloest werden. die
							// beiden
							// nachfolgenden zeilen machen das :-)
							table.setRowSelectionInterval(iSelectedRow - 1,
									iSelectedRow - 1);
							e.consume();
							// sichtbaren bereich noetigenfalls anpassen
							table.scrollRectToVisible(table.getCellRect(
									iSelectedRow - 1, 0, true));
							// zuhoerer informieren
							fireItemChangedEvent_ITEM_CHANGED();
						} else if (e.getKeyCode() == KeyEvent.VK_DOWN
								&& iSelectedRow < (iRowCount - 1)) {
							// ListSelectionEvent muss ausgeloest werden. die
							// beiden
							// nachfolgenden zeilen machen das :-)
							table.setRowSelectionInterval(iSelectedRow + 1,
									iSelectedRow + 1);
							e.consume();
							// sichtbaren bereich noetigenfalls anpassen
							table.scrollRectToVisible(table.getCellRect(
									iSelectedRow + 1, 0, true));
							// zuhoerer informieren
							fireItemChangedEvent_ITEM_CHANGED();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						fireItemChangedEvent_GOTO_DETAIL_PANEL();
						eventKeyPressed(e);
					} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP
							|| e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
						if (table.getRowCount() > 0) {
							int iSichtbareHoehe = (int) tableScrollPane
									.getViewport().getSize().getHeight();
							int iZellenHoehe = (int) table.getCellRect(0, 0,
									true).getHeight();
							int iSichtbareZeilen = iSichtbareHoehe
									/ iZellenHoehe;

							int iSelectedRow = table.getSelectedRow();
							if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
								iSelectedRow -= iSichtbareZeilen;
								if (iSelectedRow < 0) {
									iSelectedRow = 0;
								}
							} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
								iSelectedRow += iSichtbareZeilen;
								if (iSelectedRow >= (table.getRowCount() - 1)) {
									iSelectedRow = table.getRowCount() - 1;
								}
							}
							table.setRowSelectionInterval(iSelectedRow,
									iSelectedRow);
							e.consume();
							table.scrollRectToVisible(table.getCellRect(
									iSelectedRow, 0, true));
							fireItemChangedEvent_ITEM_CHANGED();
						}
					}
				}
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				clearAllFilters();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getSource() == wcbVersteckteFelderAnzeigen) {

			}
		}

		/**
		 * getIdSelected
		 * 
		 * @deprecated MB: use getSelectedId()
		 * 
		 * @return Object
		 */
	}

	public Object getIdSelected() {
		return getSelectedId();
	}

	/**
	 * eventActionNew
	 * 
	 * @param eventObject
	 *            ActionEvent
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		LockStateValue l = new LockStateValue(null, null, LOCK_FOR_NEW);
		updateButtons(l);

		getInternalFrame().fireItemChanged(this, ItemChangedEvent.ACTION_NEW);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		LockStateValue l = new LockStateValue(null, null, LOCK_DISABLE_ALL);
		updateButtons(l);
		getInternalFrame()
				.fireItemChanged(this, ItemChangedEvent.ACTION_UPDATE);
		// super.eventActionUpdate(aE,bNeedNoUpdateI);
	}

	/**
	 * eventActionPrint
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getInternalFrame().fireItemChanged(this, ItemChangedEvent.ACTION_PRINT);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		getInternalFrame()
				.fireItemChanged(this, ItemChangedEvent.ACTION_DELETE);
	}

	/**
	 * getInternalFrameTitle
	 * 
	 * @return String
	 */
	/*
	 * protected String getInternalFrameTitle() { return titel; }
	 */

	public int getIdUsecase() {
		return idUsecase;
	}

	/**
	 * 
	 * @param bNeedNoYouAreSelectedI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				this.getAdd2Title());
		if (refreshWhenYouAreSelected) {
			eventActionRefresh(null, false);
		}
		setFirstFocusableComponent();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		if (!hmDirektFilter.isEmpty()) {
			// Das Eingabefeld des ersten Direktfilters
			return hmDirektFilter.get(new Integer(0)).wtfFkdirektValue1;
		} else {
			return NO_VALUE_THATS_OK_JCOMPONENT;
		}
	}

	public boolean isLockedDlg() {
		return false;
	}

	/**
	 * 
	 * @return LockStateValue
	 * @throws Throwable
	 */
	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		return new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED);
	}

	/**
	 * Hole den "Lock" fuer diese Panel.
	 * 
	 * @return LockMeDto
	 * @throws Exception
	 */
	public LockMeDto getLockMe() throws Exception {
		/**
		 * @todo MB das schoener machen ;-) PJ 5069
		 */
		return null;
	}

	/**
	 * btnstate: 8 Hier werden die Buttons speziell fuer das PanelQuery gesetzt.
	 * 
	 * @throws Throwable
	 */
	public void updateButtons() throws Throwable {

		LockStateValue l = getLockedstateDetailMainKey();
		updateButtons(l);
	}

	/**
	 * btnstate: 9 Hier werden die Buttons speziell fuer das PanelQuery gesetzt.
	 * 
	 * @throws Exception
	 * @param lockstateValueI
	 *            LockStateValue
	 */
	public void updateButtons(LockStateValue lockstateValueI) throws Exception {

		boolean bMehrfachSelektiert = table.getSelectedRowCount() > 1;
		// kein super!

		// precondition
		if (getInternalFrame().getRechtModulweit() == null) {
			myLogger.error("getInternalFrame().getRechtModulweit() == null");
		}

		int iLockstate = lockstateValueI.getIState();

		// Rechte
		if (getInternalFrame().getRechtModulweit().equals(
				RechteFac.RECHT_MODULWEIT_READ)) {
			iLockstate = LOCK_ENABLE_REFRESHANDPRINT_ONLY;
		}

		Collection<?> buttons = getHmOfButtons().values();

		if (!bMehrfachSelektiert) {
			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();

				if (iLockstate == LOCK_DISABLE_ALL) {
					setEnabledDirektFilterFelder(false);
					if (item.getButton().getActionCommand()
							.equals(ACTION_REFRESH)) {
						item.getButton().setEnabled(true);
					} else {
						item.getButton().setEnabled(false);
					}
				}

				else if (iLockstate == LOCK_FOR_NEW) {
					// Neu...
					setEnabledDirektFilterFelder(false);
					if (item.getButton().getActionCommand().equals(ACTION_NEW)
							|| item.getButton().getActionCommand()
									.startsWith(ACTION_MY_OWN_NEW)
							|| item.getButton().getActionCommand()
									.equals(ACTION_REFRESH)
							|| item.getButton().getActionCommand()
									.equals(ACTION_FILTER)
							|| item.getButton().getActionCommand()
									.equals(ACTION_POSITION_VONNNACHNMINUS1)
							|| item.getButton().getActionCommand()
									.equals(ACTION_POSITION_VONNNACHNPLUS1)
							|| item.getButton().getActionCommand()
									.equals(ACTION_PRINT)
							|| item.getButton().getActionCommand()
									.equals(ACTION_KOPIEREN) // ||
					// item.getButton().getActionCommand().equals(
					// ACTION_EINFUEGEN) //||
					// item.getButton().getActionCommand().equals(
					// ACTION_POSITION_VORPOSITIONEINFUEGEN)
					) {
						item.getButton().setEnabled(false);
					}
				}

				else if (iLockstate == LOCK_FOR_EMPTY) {
					// wenn es in der Tabelle keinen Eintrag gibt ...
					setEnabledDirektFilterFelder(true);
					if (item.getButton().getActionCommand().equals(ACTION_NEW)
							|| item.getButton().getActionCommand()
									.startsWith(ACTION_MY_OWN_NEW)
							|| item.getButton().getActionCommand()
									.equals(ACTION_REFRESH)
							|| item.getButton().getActionCommand()
									.equals(ACTION_EINFUEGEN_LIKE_NEW)) {
						item.getButton().setEnabled(true);
					} else {
						if (item.getButton().getActionCommand()
								.equals(ACTION_PRINT)
								|| item.getButton().getActionCommand()
										.equals(ACTION_KOPIEREN)
								|| item.getButton().getActionCommand()
										.indexOf(LEAVEALONE) == -1) {
							// es ist kein leave me alone button
							item.getButton().setEnabled(false);
						}
					}
				}

				else if (iLockstate == LOCK_IS_NOT_LOCKED) {
					// wenn es mindestens einen Eintrag in der Tabelle gibt ...
					setEnabledDirektFilterFelder(true);
					if (item.getButton().getActionCommand().equals(ACTION_NEW)
							|| item.getButton().getActionCommand()
									.startsWith(ACTION_MY_OWN_NEW)
							|| item.getButton().getActionCommand()
									.equals(ACTION_REFRESH)
							|| item.getButton().getActionCommand()
									.equals(ACTION_UPDATE)
							|| item.getButton().getActionCommand()
									.equals(ACTION_FILTER)
							|| item.getButton().getActionCommand()
									.equals(ACTION_POSITION_VONNNACHNMINUS1)
							|| item.getButton().getActionCommand()
									.equals(ACTION_POSITION_VONNNACHNPLUS1)
							|| item.getButton().getActionCommand()
									.equals(ACTION_PRINT)
							|| item.getButton().getActionCommand()
									.equals(ACTION_KOPIEREN)
							|| item.getButton().getActionCommand()
									.equals(ACTION_NEXT)
							|| item.getButton().getActionCommand()
									.equals(ACTION_PREVIOUS)// ||
					// item.getButton().getActionCommand().equals(
					// ACTION_EINFUEGEN) //||
					// item.getButton().getActionCommand().equals(
					// ACTION_POSITION_VORPOSITIONEINFUEGEN)
					) {
						item.getButton().setEnabled(true);
					} else {
						if (item.getButton().getActionCommand()
								.indexOf(LEAVEALONE) == -1) {
							// es ist kein leave me alone button
							item.getButton().setEnabled(false);
						}
					}
				}

				else if (iLockstate == LOCK_IS_LOCKED_BY_OTHER_USER
						|| iLockstate == LOCK_IS_LOCKED_BY_ME) {
					setEnabledDirektFilterFelder(true);
					// der refresh-button ist immer enabled
					if (item.getButton().getActionCommand()
							.equals(ACTION_REFRESH)) {
						item.getButton().setEnabled(true);
					} else if (item.getButton().getActionCommand()
							.indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button
						item.getButton().setEnabled(false);
					}
				}

				else if (iLockstate == LOCK_ENABLE_REFRESHANDPRINT_ONLY) {
					setEnabledDirektFilterFelder(true);
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button

						if (item.getButton().getActionCommand()
								.indexOf(ACTION_REFRESH) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_KOPIEREN) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_FILTER) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_PRINT) > -1) {
							// es ist ein Refresh button
							item.getButton().setEnabled(true);
						} else {
							// es ist kein Refresh button
							item.getButton().setEnabled(false);
						}
					}
				} else if (iLockstate == LOCK_ENABLE_REFRESHANDUPDATE_ONLY) {
					setEnabledDirektFilterFelder(true);
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button

						if (item.getButton().getActionCommand()
								.indexOf(ACTION_REFRESH) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_KOPIEREN) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_UPDATE) > -1) {
							// es ist ein Refresh button
							item.getButton().setEnabled(true);
						} else {
							// es ist kein Refresh button
							item.getButton().setEnabled(false);
						}
					}
				} else if (iLockstate == LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY) {
					setEnabledDirektFilterFelder(true);
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button

						if (item.getButton().getActionCommand()
								.indexOf(ACTION_REFRESH) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_UPDATE) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_KOPIEREN) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_PRINT) > -1) {
							// es ist ein Refresh button
							item.getButton().setEnabled(true);
						} else {
							// es ist kein Refresh button
							item.getButton().setEnabled(false);
						}
					}
				} else {
					throw new Exception("iAspectI not supported: " + iLockstate);
				}
			}
		} else {
			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();

				if (iLockstate == LOCK_DISABLE_ALL) {
					setEnabledDirektFilterFelder(false);
					if (item.getButton().getActionCommand()
							.equals(ACTION_REFRESH)) {
						item.getButton().setEnabled(true);
					} else {
						item.getButton().setEnabled(false);
					}
				}
				/** @todo JO->MB bitte review wegen IMS 2485 PJ 5070 */
				// JO kommt vor ;-) dieser fall sollte nicht vorkommen
				else if (iLockstate == LOCK_FOR_NEW) {
					// Neu...
					setEnabledDirektFilterFelder(false);
					if (item.getButton().getActionCommand().equals(ACTION_NEW)
							|| item.getButton().getActionCommand()
									.startsWith(ACTION_MY_OWN_NEW)
							|| item.getButton().getActionCommand()
									.equals(ACTION_REFRESH)
							|| item.getButton().getActionCommand()
									.equals(ACTION_FILTER)
							|| item.getButton().getActionCommand()
									.equals(ACTION_POSITION_VONNNACHNMINUS1)
							|| item.getButton().getActionCommand()
									.equals(ACTION_POSITION_VONNNACHNPLUS1)
							|| item.getButton().getActionCommand()
									.equals(ACTION_PRINT)
							|| item.getButton().getActionCommand()
									.equals(ACTION_KOPIEREN)
							|| item.getButton().getActionCommand()
									.equals(ACTION_EINFUEGEN_LIKE_NEW)
							|| item.getButton().getActionCommand()
									.equals(ACTION_NEXT)
							|| item.getButton().getActionCommand()
									.equals(ACTION_PREVIOUS) // ||
					// item.getButton().getActionCommand().equals(
					// ACTION_POSITION_VORPOSITIONEINFUEGEN)
					) {
						item.getButton().setEnabled(false);
					}
				}

				// dieser auch nicht
				// else if (iLockstate == LOCK_FOR_EMPTY) {
				// // wenn es in der Tabelle keinen Eintrag gibt ...
				// setEnabledDirektFilterFelder(true);
				// if (item.getButton().getActionCommand().equals(ACTION_NEW) ||
				// item.getButton().getActionCommand().startsWith(
				// ACTION_MY_OWN_NEW) ||
				// item.getButton().getActionCommand().equals(ACTION_REFRESH) ||
				// item.getButton().getActionCommand().equals(ACTION_EINFUEGEN))
				// {
				// item.getButton().setEnabled(true);
				// }
				// else {
				// if (item.getButton().getActionCommand().equals(ACTION_PRINT)
				// ||
				// item.getButton().getActionCommand().equals(ACTION_KOPIEREN)
				// ||
				// item.getButton().getActionCommand().indexOf(LEAVEALONE) ==
				// -1) {
				// // es ist kein leave me alone button
				// item.getButton().setEnabled(false);
				// }
				// }
				// }

				else if (iLockstate == LOCK_IS_NOT_LOCKED) {
					// wenn es mindestens einen Eintrag in der Tabelle gibt ...
					setEnabledDirektFilterFelder(true);
					if ( // item.getButton().getActionCommand().equals(ACTION_NEW
					// ) ||
					// item.getButton().getActionCommand().startsWith(
					// ACTION_MY_OWN_NEW) ||
					item.getButton().getActionCommand().equals(ACTION_REFRESH)
							|| item.getButton().getActionCommand()
									.equals(ACTION_FILTER)
							||
							// item.getButton().getActionCommand().equals(
							// ACTION_POSITION_VONNNACHNMINUS1) ||
							// item.getButton().getActionCommand().equals(
							// ACTION_POSITION_VONNNACHNPLUS1) ||
							item.getButton().getActionCommand()
									.equals(ACTION_PRINT)
							|| item.getButton().getActionCommand()
									.equals(ACTION_KOPIEREN)
							|| item.getButton().getActionCommand()
									.equals(MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK)
							|| item.getButton().getActionCommand()
									.equals(ACTION_EINFUEGEN_LIKE_NEW) // ||
					// item.getButton().getActionCommand().equals(
					// ACTION_POSITION_VORPOSITIONEINFUEGEN)
					) {
						item.getButton().setEnabled(true);
					} else {
						if (item.getButton().getActionCommand()
								.indexOf(LEAVEALONE) == -1) {
							// es ist kein leave me alone button
							item.getButton().setEnabled(false);
						}
					}
				}

				else if (iLockstate == LOCK_IS_LOCKED_BY_OTHER_USER
						|| iLockstate == LOCK_IS_LOCKED_BY_ME) {
					setEnabledDirektFilterFelder(true);
					// der refresh-button ist immer enabled
					if (item.getButton().getActionCommand()
							.equals(ACTION_REFRESH)) {
						item.getButton().setEnabled(true);
					} else if (item.getButton().getActionCommand()
							.indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button
						item.getButton().setEnabled(false);
					}
				}

				else if (iLockstate == LOCK_ENABLE_REFRESHANDPRINT_ONLY) {
					setEnabledDirektFilterFelder(true);
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button

						if (item.getButton().getActionCommand()
								.indexOf(ACTION_REFRESH) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_KOPIEREN) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_PRINT) > -1) {
							// es ist ein Refresh button
							item.getButton().setEnabled(true);
						} else {
							// es ist kein Refresh button
							item.getButton().setEnabled(false);
						}
					}
				} else if (iLockstate == LOCK_ENABLE_REFRESHANDUPDATE_ONLY) {
					setEnabledDirektFilterFelder(true);
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button

						if (item.getButton().getActionCommand()
								.indexOf(ACTION_REFRESH) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_KOPIEREN) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_UPDATE) > -1) {
							// es ist ein Refresh button
							item.getButton().setEnabled(true);
						} else {
							// es ist kein Refresh button
							item.getButton().setEnabled(false);
						}
					}
				} else if (iLockstate == LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY) {
					setEnabledDirektFilterFelder(true);
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button

						if (item.getButton().getActionCommand()
								.indexOf(ACTION_REFRESH) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_UPDATE) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_KOPIEREN) > -1
								|| item.getButton().getActionCommand()
										.indexOf(ACTION_PRINT) > -1) {
							// es ist ein Refresh button
							item.getButton().setEnabled(true);
						} else {
							// es ist kein Refresh button
							item.getButton().setEnabled(false);
						}
					}
				} else {
					throw new Exception("iAspectI not supported: " + iLockstate);
				}
			}
		}

		super.updateButtonsAlwaysEnabled(buttons);
	}

	public WrapperTable getTable() {
		return table;
	}

	public JScrollPane getTableScrollPane() {
		return tableScrollPane;
	}

	public ArrayList<?> getListOfExtraData() {
		return listOfExtraData;
	}

	public void removeToolsPanel() throws Exception {
		this.remove(getToolsPanel());
	}

	/**
	 * Setzen von 0..2 direkten Filterkriterien und der CheckBox fuer die
	 * versteckten Entitaeten.
	 * 
	 * @param kritDirekt1I
	 *            der erste Direktfilter, null erlaubt
	 * @param kritDirekt2I
	 *            der zweite Firektfilter, null erlaubt
	 * @param kritVersteckteFelderNichtAnzeigenI
	 *            FilterKriterium fuer die versteckten Entitaeten, null erlaubt
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void befuellePanelFilterkriterienDirektUndVersteckte(
			FilterKriteriumDirekt kritDirekt1I,
			FilterKriteriumDirekt kritDirekt2I,
			FilterKriterium kritVersteckteFelderNichtAnzeigenI)
			throws Throwable {
		befuellePanelFilterkriterienDirektUndVersteckte(kritDirekt1I,
				kritDirekt2I, kritVersteckteFelderNichtAnzeigenI,
				LPMain.getTextRespectUISPr("lp.versteckte"));
	}

	/**
	 * Setzen von 0..2 direkten Filterkriterien und der CheckBox fuer die
	 * versteckten Entitaeten.
	 * 
	 * @param kritDirekt1I
	 *            der erste Direktfilter, null erlaubt
	 * @param kritDirekt2I
	 *            der zweite Firektfilter, null erlaubt
	 * @param kritVersteckteFelderNichtAnzeigenI
	 *            FilterKriterium fuer die versteckten Entitaeten, null erlaubt
	 * @param sTextLabelVersteckt
	 *            String
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void befuellePanelFilterkriterienDirektUndVersteckte(
			FilterKriteriumDirekt kritDirekt1I,
			FilterKriteriumDirekt kritDirekt2I,
			FilterKriterium kritVersteckteFelderNichtAnzeigenI,
			String sTextLabelVersteckt) throws Throwable {

		// das erste Kriterium
		if (kritDirekt1I != null) {
			addDirektFilter(kritDirekt1I);
		}

		if (kritDirekt2I != null) {
			addDirektFilter(kritDirekt2I);
		}

		if (kritVersteckteFelderNichtAnzeigenI != null) {
			fkVersteckteFelderNichtAnzeigen = kritVersteckteFelderNichtAnzeigenI;

			wcbVersteckteFelderAnzeigen = new WrapperCheckBox(
					sTextLabelVersteckt);
			wcbVersteckteFelderAnzeigen.addKeyListener(bildAufAbListener);
			wcbVersteckteFelderAnzeigen.addMouseListener(this);
			HelperClient
					.setDefaultsToComponent(wcbVersteckteFelderAnzeigen, 40);

			// filterkritversteckt: 2 hier wird ueber die Anzeige entschieden
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
				wcbVersteckteFelderAnzeigen
						.setSelected(DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getMandantparameter(
										LPMain.getTheClient().getMandant(),
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_AUSWAHLLISTE_VERSTECKTE_ANZEIGEN_DEFAULT)
								.asBoolean());
				panelFilter.add(wcbVersteckteFelderAnzeigen,
						new GridBagConstraints(8, iZeile, 1, 1, 0.1, 0.0,
								GridBagConstraints.NORTH,
								GridBagConstraints.BOTH,
								new Insets(2, 2, 2, 2), 0, 0));
			} else {
				wcbVersteckteFelderAnzeigen.setSelected(false);
			}

			if (this instanceof PanelQueryFLR) {// CK: 2009-07-30 wg.
				// performance
				eventActionRefresh(null, false);
			}
		}
	}

	public void setzeErstesUebersteuerbaresSortierkriterium(String text,
			SortierKriterium sortierkriterium) throws Throwable {
		this.sortierkriteriumErsteSortierungUebersteuern = sortierkriterium;

		wcbErsteSortierungUebersteuern = new WrapperCheckBox(text);
		wcbErsteSortierungUebersteuern.addKeyListener(bildAufAbListener);
		wcbErsteSortierungUebersteuern.addMouseListener(this);
		HelperClient.setDefaultsToComponent(wcbErsteSortierungUebersteuern, 40);

		panelFilter.add(wcbErsteSortierungUebersteuern, new GridBagConstraints(
				8, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wcbErsteSortierungUebersteuern.setSelected(false);
		if (this instanceof PanelQueryFLR) {// CK: 2009-07-30 wg.
			// performance
			eventActionRefresh(null, false);
		}

	}

	private Boolean getMandantParameterSchnellansicht(
			FilterKriteriumSchnellansicht fks) {
		return getMandantParameterSchnellansicht(fks.getKategorie(),
				fks.getParameter());
	}

	private Boolean getMandantParameterSchnellansicht(String kategorie,
			String parameter) {
		try {
			return DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameterReturnsNull(kategorie, parameter)
					.asBoolean();
		} catch (Throwable e) {
		}
		return null;
	}

	public void befuelleFilterkriteriumSchnellansicht(
			FilterKriterium[] kritSchnellansicht) {
		fkSchnellansicht = kritSchnellansicht;
		if (kritSchnellansicht == null)
			return;
		Boolean defaultValue = null;
		if (fkSchnellansicht.length > 0
				&& fkSchnellansicht[0] instanceof FilterKriteriumSchnellansicht) {
			defaultValue = getMandantParameterSchnellansicht((FilterKriteriumSchnellansicht) fkSchnellansicht[0]);
		}
		if (defaultValue == null) {
			defaultValue = getMandantParameterSchnellansicht(
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_AUSWAHLLISTE_NUR_OFFENE_DEFAULT);
		}
		cbSchnellansicht
				.setSelected(defaultValue == null ? true : defaultValue);
		cbSchnellansicht.setVisible(true);
		cbSchnellansicht.removeMouseListener(this);
		cbSchnellansicht.addMouseListener(this);
	}

	public void befuelleFilterkriteriumKey(FilterKriterium[] kritKey) {
		fkKey = kritKey;
	}

	/**
	 * Setzen von 0..2 direkten Filterkriterien.
	 * 
	 * @param oKrit1
	 *            das erste Kriterium
	 * @param oKrit2
	 *            das zweite Kriterium
	 * @throws ExceptionLP
	 */
	public void befuellePanelFilterkriterienDirekt(
			FilterKriteriumDirekt oKrit1, FilterKriteriumDirekt oKrit2)
			throws ExceptionLP {
		if (oKrit1 != null) {
			addDirektFilter(oKrit1);
		}
		if (oKrit2 != null) {
			addDirektFilter(oKrit2);
		}
	}

	public String getAspect() {
		return sAspect;
	}

	private void setBFilterHasChanged(boolean bFilterHasChangedI) {
		this.bFilterHasChanged = bFilterHasChangedI;
	}

	public boolean getBFilterHasChanged() {
		return bFilterHasChanged;
	}

	/**
	 * mouseClicked.
	 * 
	 * @param e
	 *            MouseEvent
	 */
	final public void mouseReleased(MouseEvent e) {
		try {
			if (e.getSource() == table) {
				fireItemChangedEvent_ITEM_CHANGED();
			}
			// else if(e.getSource() == table.getTableHeader()) {
			// saveSpaltenbreite();
			// }
			super.mouseReleased(e);
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	private void saveQueryViewSettings() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				List<Integer> widths = new ArrayList<Integer>();
				int size = table.getColumnCount();
				for (int i = 0; i < size; i++) {
					widths.add(table.getColumnModel().getColumn(i).getWidth());
				}
				ClientPerspectiveManager.getInstance().saveQueryColumnWidths(
						idUsecase, widths);
				ClientPerspectiveManager.getInstance().saveQueryColumnSorting(
						idUsecase, currentSortierKriterien);
				ClientPerspectiveManager.getInstance().saveInternalFrame(getInternalFrame().getBelegartCNr());
				return null;
			}
		};
		worker.execute();
	}

	public void clearDirektFilter() {
		for (Iterator<Integer> iter = hmDirektFilter.keySet().iterator(); iter
				.hasNext();) {
			PanelFilterKriteriumDirekt panelFkd = hmDirektFilter.get(iter
					.next());
			panelFkd.wtfFkdirektValue1.setText(null);
		}
	}

	public void clearZusatzFilter() {
		for (Iterator<QueryInputRow> iter = queryInputRows.iterator(); iter
				.hasNext();) {
			QueryInputRow item = iter.next();
			item.setValue(null);
		}
	}

	public void clearAllFilters() {
		clearDirektFilter();
		clearZusatzFilter();
	}

	public void clearDefaultFilters() {
		this.defaultFilter = null;
	}

	public void restoreDefaultFilters() {
		this.defaultFilter = defaultFilter2Restore;
	}

	/**
	 * Pruefen, ob in einen Filter etwas eingegeben wurde.
	 * 
	 * @return boolean
	 */
	public boolean isFilterActive() {
		boolean bActive = false;
		for (Iterator<Integer> iter = hmDirektFilter.keySet().iterator(); iter
				.hasNext();) {
			PanelFilterKriteriumDirekt panelFkd = hmDirektFilter.get(iter
					.next());
			if (panelFkd.wtfFkdirektValue1.getText() != null
					|| getBFilterHasChanged()) {
				bActive = true;
			}
		}
		// und auch die Zusatzfilter anschaun
		for (Iterator<QueryInputRow> iter = queryInputRows.iterator(); iter
				.hasNext();) {
			QueryInputRow item = iter.next();
			if (item.getValue() != null) {
				bActive = true;
			}
		}

		return bActive;
	}

	protected void fireItemChangedEvent_ITEM_CHANGED() throws Throwable {
		// Wenn gewuenscht wird ein ItemChangedEvent gefeuert
		if (fireItemChangedEventAfterChange) {
			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ITEM_CHANGED);
			/**
			 * @todo MB->JO workaround fuers panelSplit, damit der focus im
			 *       direktfilter bleibt PJ 5078 bitte beseitigen
			 */
			this.setFirstFocusableComponent();
		}
	}

	public void addDirektFilter(FilterKriteriumDirekt fkd) throws ExceptionLP {
		PanelFilterKriteriumDirekt panelFkd = new PanelFilterKriteriumDirekt(
				fkd);
		int index = hmDirektFilter.size();

		// fuer jeden zweiten direktfilter eine neue zeile beginnen
		if (index > 0 && index % 2 == 0) {
			iZeile++;
		}
		// die ungeraden rechts positionieren
		int iSpalte = 4 * (index % 2);

		panelFilter.add(panelFkd.wlaFkdirektName1, new GridBagConstraints(
				iSpalte, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iSpalte++;
		panelFilter.add(panelFkd.wlaFkdirektOperator1, new GridBagConstraints(
				iSpalte, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iSpalte++;
		if (fkd.iTyp == FilterKriteriumDirekt.TYP_STRING) {
			panelFilter.add(panelFkd.wtfFkdirektValue1, new GridBagConstraints(
					iSpalte, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		} else if (fkd.iTyp == FilterKriteriumDirekt.TYP_DECIMAL) {
			panelFilter.add(panelFkd.wnfFkdirektValue1, new GridBagConstraints(
					iSpalte, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iSpalte++;
		panelFilter.add(panelFkd.wlaEmpty1, new GridBagConstraints(iSpalte,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iSpalte++;
		// KeyListener

		if (fkd.iTyp == FilterKriteriumDirekt.TYP_STRING) {
			panelFkd.wtfFkdirektValue1.addKeyListener(bildAufAbListener);
			panelFkd.wtfFkdirektValue1.addMouseListener(bildAufAbListener);
		} else if (fkd.iTyp == FilterKriteriumDirekt.TYP_DECIMAL) {
			panelFkd.wnfFkdirektValue1.addKeyListener(bildAufAbListener);
			panelFkd.wnfFkdirektValue1.addMouseListener(bildAufAbListener);
		}

		// die Direktfilter werden in einer Map gesammelt
		hmDirektFilter.put(new Integer(index), panelFkd);
	}

	public void addStatusBar() throws Throwable {
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public void setStatusbarSpalte5(Object o, boolean bEnabled)
			throws Throwable {
		super.setStatusbarSpalte5(o);
	}

	public void setStatusbarSpalte6(String o, boolean bEnabled)
			throws Throwable {
		super.getPanelStatusbar().setLockFieldWithoutIcon(o);
	}

	public void setStatusbarSpalte4(String o) throws Throwable {
		super.getPanelStatusbar().setSpalte4(o);
	}

	protected void fireItemChangedEvent_GOTO_DETAIL_PANEL() {
		getInternalFrame().fireItemChanged(this,
				ItemChangedEvent.GOTO_DETAIL_PANEL);
	}

	private void setEnabledDirektFilterFelder(boolean bEnabled) {
		for (Iterator<Integer> iter = hmDirektFilter.keySet().iterator(); iter
				.hasNext();) {
			PanelFilterKriteriumDirekt item = hmDirektFilter.get(iter.next());
			if (item.fkd.iTyp == FilterKriteriumDirekt.TYP_STRING) {
				item.wtfFkdirektValue1.setEditable(bEnabled);
			} else if (item.fkd.iTyp == FilterKriteriumDirekt.TYP_DECIMAL) {
				item.wnfFkdirektValue1.setEditable(bEnabled);
			}
		}
	}

	public void setMultipleRowSelectionEnabled(
			boolean bMultipleRowSelectionEnabled) {
		this.bMultipleRowSelectionEnabled = bMultipleRowSelectionEnabled;
		// mehrfachselekt: diese methode setzt das
		if (bMultipleRowSelectionEnabled) {
			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
	}

	public boolean isMultipleRowSelectionEnabled() {
		return bMultipleRowSelectionEnabled;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		// wurde die Selektion in meiner tabelle veraendert?
		if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED
				&& e.getSource() == table) {
			// Buttons updaten
			updateButtons();
		}

		if (e.getID() == ItemChangedEvent.ACTION_SAVE
				|| e.getID() == ItemChangedEvent.ACTION_DELETE
				|| e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
		}

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			bShowDocButton = false;

			PrintInfoDto values = null;

			boolean online = false;
			boolean hasFiles = false;

			if (bBenutzeUebersteuerteId == true) {

				if (getUebersteuerteId() != null) {
					values = DelegateFactory
							.getInstance()
							.getJCRDocDelegate()
							.getPathAndPartnerAndTable(getUebersteuerteId(),
									idUsecase);
				}
			} else {
				if (getSelectedId() != null) {
					values = DelegateFactory
							.getInstance()
							.getJCRDocDelegate()
							.getPathAndPartnerAndTable(getSelectedId(),
									idUsecase);
				}
			}

			if (values != null) {
				if (values.getDocPath() != null) {
					bShowDocButton = true;
					online = DelegateFactory.getInstance()
							.getJCRDocDelegate().isOnline();
					if(online) {
						hasFiles = DelegateFactory.getInstance()
								.getJCRDocDelegate()
								.checkIfNodeExists(values.getDocPath());
					}
				}
			}

			if (bShowDocButton) {
				jbDokumente.setIcon(hasFiles ? DOKUMENTE : KEINE_DOKUMENTE);
				jbDokumente.setVisible(true);
				enableToolsPanelButtons(online, LEAVEALONE_DOKUMENTE);
				if(!online) {
					getHmOfButtons().get(LEAVEALONE_DOKUMENTE).getButton().setToolTipText(LPMain.getTextRespectUISPr("lp.dokumente.offline"));
				}
			} else {
				jbDokumente.setVisible(false);
			}
			if (getSelectedId() != null) {
				jbDokumente.setEnabled(online);
			} else {
				jbDokumente.setEnabled(false);
			}
			List<Object> iids = new ArrayList<Object>();
			Object[] array = getSelectedIds();
			if (array != null) {
				for (Object iid : array) {
					iids.add(iid);
				}
			}
			if (dataSource != null) {
				setInfoPairList(dataSource.getInfoForSelectedIIds(iids));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {

			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == this) {
				if (sAspectInfo.equals(MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK)) {
					if (getSelectedIds() != null) {
						fireItemChangedEvent_GOTO_DETAIL_PANEL();
					}
				}
			}
		}

	}

	private void setInfoPairList(List<Pair<?, ?>> pairs) {
		if (pairs == null) {
			multiselektInfo.setVisible(false);
			return;
		}
		multiselektInfo.removeAll();
		int i = 0;
		for (Pair<?, ?> pair : pairs) {
			WrapperLabel label = new WrapperLabel(pair.getKey() + " = "
					+ pair.getValue());
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setPreferredSize(new Dimension(0, 0));
			multiselektInfo.add(label,
					new GridBagConstraints(i, 0, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
									0), 0, 0));
			i++;
		}
		multiselektInfo.setVisible(true);

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

	protected void setVertauscheButtonsEnabled(boolean enabled) {
		Collection<?> buttons = getHmOfButtons().values();

		for (Iterator<?> iterator = buttons.iterator(); iterator.hasNext();) {
			LPButtonAction item = (LPButtonAction) iterator.next();

			if (ACTION_POSITION_VONNNACHNMINUS1.equals(item.getButton()
					.getActionCommand())
					|| ACTION_POSITION_VONNNACHNPLUS1.equals(item.getButton()
							.getActionCommand())) {

				item.getButton().setEnabled(enabled);
				item.getButton().setVisible(enabled);
			}
		}
	}

	protected JPanel getPanelFilter() {
		return panelFilter;
	}

	protected void setRefreshWenYouAreSelected(boolean refreshWenYouAreSelected) {
		this.refreshWhenYouAreSelected = refreshWenYouAreSelected;
	}

	// @Override
	// public void hierarchyChanged(HierarchyEvent arg0) {
	// if(HierarchyEvent.SHOWING_CHANGED == arg0.getChangeFlags()) {
	// if(!table.isShowing() && wasVisibleSinceLastSave) {
	// wasVisibleSinceLastSave = false;
	// saveQueryViewSettings();
	// } else if(table.isShowing() && wasVisibleSinceLastSave == false) {
	// wasVisibleSinceLastSave = true;
	// }
	// }
	// }

	private class HeaderCellRenderer implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (value == null)
				return null;

			SortierKriterium krit = sortierKriterien.get(table
					.getColumnName(column));
			JLabel label;
			if (value instanceof JLabel)
				label = (JLabel) value;
			else
				label = new JLabel(value.toString());

			if (currentSortierKriterien.contains(krit)) {
				if (krit.value.equals(SortierKriterium.SORT_ASC)) {
					label.setIcon(icon_up);
				} else if (krit.value.equals(SortierKriterium.SORT_DESC)) {
					label.setIcon(icon_down);
				}
				label.setHorizontalTextPosition(SwingConstants.RIGHT);
			} else if (!iNoSort.equals(label.getIcon())) {
				label.setIcon(null);
			}
			return label;
		}
	}
}

class JComponentCellRenderer implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		return (JComponent) value;
	}
}
