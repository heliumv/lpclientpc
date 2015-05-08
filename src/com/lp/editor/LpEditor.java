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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.editor.text.LpFormatParser;
import com.lp.editor.text.LpJasperGenerator;
import com.lp.editor.text.LpJasperParser;
import com.lp.editor.ui.LpDecoratedTextPane;
import com.lp.editor.ui.LpEditorSettings;
import com.lp.editor.ui.LpEditorSheetPanel;
import com.lp.editor.ui.LpEditorTable;
import com.lp.editor.ui.LpRuler;
import com.lp.editor.ui.LpToolBarButton;
import com.lp.editor.ui.LpToolBarToggleButton;
import com.lp.editor.ui.LpToolsPanel;
import com.lp.editor.util.FontNotFoundException;
import com.lp.editor.util.LpEditorMessages;
import com.lp.editor.util.LpEditorReportData;
import com.lp.editor.util.LpEditorRow;
import com.lp.editor.util.TextBlockAttributes;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;

/**
 * 
 * <p>
 * <I>Der Editor fuer LogistikPur</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>August 2003</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Kajetan Fuchsberger
 * @author Sascha Zelzer
 * @version $Revision: 1.7 $
 */
public class LpEditor extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private LpLogger log = null;
	private LpLogger myLogger = (LpLogger) LpLogger.getInstance(LpEditor.class);

	/**
	 * Identifiziert einen Vector, der an das Dokument via putProperty(Object)
	 * angehaengt wird und Spalteneigenschaften wie Breite, Default-Attribute,
	 * Text-Kapazitaet, ... enthaelt
	 * 
	 * @see TextBlockAttributes
	 * @see #getTextBlockAttributes(int)
	 */
	public static final String COLUMN_TEXT_ATTRIBUTES = "ColTextAttribs";
	/**
	 * Identifiziert einen Vector, der an das Dokument via putProperty(Object)
	 * angehaengt wird und den Inhalt der Tabellen enthaelt.
	 */
	public static final String TABLE_MODELS_PROP = "TableModels";

	/** Feld fuer den Jasper Modus des Editors */
	public static int MODE_JASPER = 0;

	private static String EXTRA_TABLE_NEWLINE = "ExtraTableNewline";

	private int iEditorMode = 0;

	public int getEditorMode() {
		return iEditorMode;
	}

	private boolean bTabRulerVisibility = true;
	private boolean bFileIsEdited = false;
	private boolean bReadingInProgress = false;
	private boolean bFirstDocChange = true;
	private boolean bUpdateBuffer = true;
	private boolean bForceInsert = false;

	private File actualFile;
	private JFrame ownerFrame;

	public JFrame getOwnerFrame() {
		return ownerFrame;
	}

	public void setOwnerFrame(JFrame ownerFrame) {
		this.ownerFrame = ownerFrame;
	}

	private static String[] asSystemFonts = null;
	private static String[] asAvailableFontNames = null;
	private static String[] asAvailableFontSizes = null;

	public static String[] getAsAvailableFontSizes() {
		return asAvailableFontSizes;
	}

	public static void setAsAvailableFontSizes(String[] asAvailableFontSizes) {
		LpEditor.asAvailableFontSizes = asAvailableFontSizes;
	}

	private static String[] asZoomFactors = null;

	private TextBlockAttributes textPaneAttributes = null;
	private TextBlockAttributes currentTextBlockAttributes = null;
	private long currentTextBlockLength = 0;
	private Vector<TextBlockAttributes> vecColTextAttributes = new Vector<TextBlockAttributes>();

	private Position currentTextBlockStart = null;
	private Position currentTextBlockEnd = null;

	private Vector<Position> vecMarkersBefore = new Vector<Position>();
	private Vector<Position> vecMarkersAfter = new Vector<Position>();
	private Vector<LpEditorTableModel> vecTableModels = new Vector<LpEditorTableModel>();
	private Vector<LpEditorTable> vecTables = new Vector<LpEditorTable>();
	LpEditorTable currentTable = null;

	private Keymap customKeymap = null;

	private Vector<TableCellEditor> vecCellEditors = new Vector<TableCellEditor>();
	private Vector<LpEditorCellRenderer> vecCellRenderers = new Vector<LpEditorCellRenderer>();
	private LpEditorCellEditor currentCellEditor = null;

	LpCompoundUndoManager undoManager = null;
	UndoableEditSupport undoSupport = new UndoableEditSupport();

	LpEditorIconManager iconManager = new LpEditorIconManager();

	private LpEditorMessages messages = LpEditorMessages.getInstance();

	// Zum debuggen
	LpDocumentDumpFrame lpDocumentDumpFrame;

	// Actions
	Action actionFileNew;
	Action actionFileOpen, actionFileSave, actionFileSaveAs, actionFileExit;
	ActionEditCut actionEditCut;
	ActionEditCopy actionEditCopy;
	ActionEditPaste actionEditPaste;
	Action actionEditSelectAll;
	Action actionFormatFont, actionFormatColorForeground,
			actionFormatColorBackground;
	Action actionFormatAlignLeft, actionFormatAlignRight,
			actionFormatAlignCenter, actionFormatAlignJustified;
	Action actionFormatStyleBold, actionFormatStyleItalic,
			actionFormatStyleUnderline, actionFormatStyleStrikethrough;
	Action actionInsertTable, actionDeleteTable, actionInsertRowBefore,
			actionInsertRowAfter, actionDeleteRow;
	Action actionInsertDateTimeUserShortcut;
	Action actionInsertSignatur;
	Action actionInsertTextbaustein;

	UndoAction actionEditUndo;
	RedoAction actionEditRedo;

	// Listener und Dialoge
	private LpCaretListener caretListener = new LpCaretListener();
	LpDocumentListener lpDocumentListener = new LpDocumentListener();
	LpDocumentFilter lpDocumentFilter = new LpDocumentFilter();
	LpMouseAdapter lpMouseAdapter = new LpMouseAdapter();

	// ======== UI Variablen ================

	// Menu
	JMenuBar jMenuBar = new JMenuBar();

	JMenu jMenuFile = new JMenu();
	JMenuItem jMenuFileNew = new JMenuItem();
	JMenuItem jMenuFileOpen = new JMenuItem();
	JMenuItem jMenuFileSave = new JMenuItem();
	JMenuItem jMenuFileSaveAs = new JMenuItem();
	JMenuItem jMenuFileExit = new JMenuItem();

	JMenu jMenuEdit = new JMenu();
	JMenuItem jMenuEditUndo = new JMenuItem();
	JMenuItem jMenuEditRedo = new JMenuItem();
	JMenuItem jMenuEditCut = new JMenuItem();
	JMenuItem jMenuEditCopy = new JMenuItem();
	JMenuItem jMenuEditPaste = new JMenuItem();
	JMenuItem jMenuEditinsertDateTimeUserShortcut = new JMenuItem();
	JMenuItem jMenuEditinsertSignatur = new JMenuItem();
	JMenuItem jMenuEditinsertTextbaustein = new JMenuItem();
	// JMenuItem jMenuEditSelectAll = new JMenuItem();
	JCheckBoxMenuItem jMenuEditPageBreak = new JCheckBoxMenuItem();
	JMenuItem jMenuEditSettings = new JMenuItem();

	JMenu jMenuFormat = new JMenu();
	JMenuItem jMenuFormatFont = new JMenuItem();
	JMenu jMenuFormatColor = new JMenu();
	JMenuItem jMenuFormatColorForeground = new JMenuItem();
	JMenuItem jMenuFormatColorBackground = new JMenuItem();
	JMenu jMenuFormatAlign = new JMenu();
	JMenuItem jMenuFormatAlignLeft = new JMenuItem();
	JMenuItem jMenuFormatAlignRight = new JMenuItem();
	JMenuItem jMenuFormatAlignCenter = new JMenuItem();
	JMenuItem jMenuFormatAlignJustified = new JMenuItem();
	JMenu jMenuFormatStyle = new JMenu();
	JMenuItem jMenuFormatStyleBold = new JMenuItem();
	JMenuItem jMenuFormatStyleItalic = new JMenuItem();
	JMenuItem jMenuFormatStyleUnderline = new JMenuItem();
	JMenuItem jMenuFormatStyleStrikethrough = new JMenuItem();

	JMenu jMenuTable = new JMenu();
	JMenuItem jMenuTableInsert = new JMenuItem();
	JMenuItem jMenuTableDelete = new JMenuItem();
	JMenuItem jMenuTableInsertRowBefore = new JMenuItem();
	JMenuItem jMenuTableInsertRowAfter = new JMenuItem();
	JMenuItem jMenuTableDeleteRow = new JMenuItem();

	// Popup Menu
	JPopupMenu jPopupMenu = new JPopupMenu();

	JMenuItem jMenuEditUndoPopup = new JMenuItem();
	JMenuItem jMenuEditRedoPopup = new JMenuItem();
	JMenuItem jMenuEditCutPopup = new JMenuItem();
	JMenuItem jMenuEditCopyPopup = new JMenuItem();
	JMenuItem jMenuEditPastePopup = new JMenuItem();
	JMenuItem jMenuEditinsertDateTimeUserShortcutPopup = new JMenuItem();
	// JMenuItem jMenuEditSelectAllPopup = new JMenuItem();

	JMenu jMenuFormatPopup = new JMenu();
	JMenuItem jMenuFormatFontPopup = new JMenuItem();
	JMenu jMenuFormatColorPopup = new JMenu();
	JMenuItem jMenuFormatColorForegroundPopup = new JMenuItem();
	JMenuItem jMenuFormatColorBackgroundPopup = new JMenuItem();
	JMenu jMenuFormatAlignPopup = new JMenu();
	JMenuItem jMenuFormatAlignLeftPopup = new JMenuItem();
	JMenuItem jMenuFormatAlignRightPopup = new JMenuItem();
	JMenuItem jMenuFormatAlignCenterPopup = new JMenuItem();
	JMenuItem jMenuFormatAlignJustifiedPopup = new JMenuItem();
	JMenu jMenuFormatStylePopup = new JMenu();
	JMenuItem jMenuFormatStyleBoldPopup = new JMenuItem();
	JMenuItem jMenuFormatStyleItalicPopup = new JMenuItem();
	JMenuItem jMenuFormatStyleUnderlinePopup = new JMenuItem();
	JMenuItem jMenuFormatStyleStrikethroughPopup = new JMenuItem();

	JMenu jMenuTablePopup = new JMenu();
	JMenuItem jMenuTableInsertPopup = new JMenuItem();
	JMenuItem jMenuTableDeletePopup = new JMenuItem();
	JMenuItem jMenuTableInsertRowBeforePopup = new JMenuItem();
	JMenuItem jMenuTableInsertRowAfterPopup = new JMenuItem();
	JMenuItem jMenuTableDeleteRowPopup = new JMenuItem();

	// Toolbar
	LpToolsPanel jPanelToolBars = new LpToolsPanel();
	JToolBar jToolBarFile = new JToolBar();
	JToolBar jToolBarOther = new JToolBar();
	JToolBar jToolBarAlignment = new JToolBar();
	JToolBar jToolBarFontStyle = new JToolBar();
	JToolBar jToolBarTable = new JToolBar();

	JComboBox jComboBoxFontNames;
	JComboBox jComboBoxFontSizes;

	ButtonGroup buttonGroupAlign = new ButtonGroup();

	LpToolBarButton lpButtonFileNew = new LpToolBarButton();
	LpToolBarButton lpButtonFileOpen = new LpToolBarButton();
	LpToolBarButton lpButtonFileSave = new LpToolBarButton();
	LpToolBarButton lpButtonEditUndo = new LpToolBarButton();
	LpToolBarButton lpButtonEditRedo = new LpToolBarButton();
	LpToolBarButton lpButtonEditCut = new LpToolBarButton();
	LpToolBarButton lpButtonEditCopy = new LpToolBarButton();
	LpToolBarButton lpButtonEditPaste = new LpToolBarButton();
	LpToolBarButton lpButtonInsertDateTimeUserShortcut = new LpToolBarButton();
	LpToolBarButton lpButtonInsertSignatur = new LpToolBarButton();
	LpToolBarButton lpButtonInsertTextbaustein = new LpToolBarButton();
	LpToolBarToggleButton lpButtonFormatAlignLeft = new LpToolBarToggleButton();
	LpToolBarToggleButton lpButtonFormatAlignRight = new LpToolBarToggleButton();
	LpToolBarToggleButton lpButtonFormatAlignCenter = new LpToolBarToggleButton();
	LpToolBarToggleButton lpButtonFormatAlignJustified = new LpToolBarToggleButton();
	LpToolBarToggleButton lpButtonFormatStyleBold = new LpToolBarToggleButton();
	LpToolBarToggleButton lpButtonFormatStyleItalic = new LpToolBarToggleButton();
	LpToolBarToggleButton lpButtonFormatStyleUnderline = new LpToolBarToggleButton();
	LpToolBarToggleButton lpButtonFormatStyleStrikethrough = new LpToolBarToggleButton();
	// LpToolBarButton lpButtonFormatFont = new LpToolBarButton();
	LpToolBarButton lpButtonFormatColorForeground = new LpToolBarButton();
	LpToolBarButton lpButtonFormatColorBackground = new LpToolBarButton();
	LpToolBarButton lpButtonInsertTable = new LpToolBarButton();
	LpToolBarButton lpButtonDeleteTable = new LpToolBarButton();
	LpToolBarButton lpButtonInsertRowBefore = new LpToolBarButton();
	LpToolBarButton lpButtonInsertRowAfter = new LpToolBarButton();
	LpToolBarButton lpButtonDeleteRow = new LpToolBarButton();

	// Status Panel
	JPanel jPanelStatus = new JPanel();
	BorderLayout borderLayout3 = new BorderLayout();
	JLabel jLabelFileName = new JLabel();
	JPanel jPanelStatusBuffer = new JPanel();
	JLabel jLabelBufferText = new JLabel();
	BorderLayout borderLayout5 = new BorderLayout();
	JProgressBar jProgressBarBuffer = new JProgressBar();

	Box jTextPaneBox;
	JScrollPane jScrollPane = new JScrollPane();
	LpDecoratedTextPane jTextPane;
	Box rulerBox;
	LpRuler ruler;
	JComboBox jComboBoxZoom;

	public String localeFuerSignatur = null;

	public void setLocaleAsStringFuerSignatur(String localeFuerSignatur) {
		this.localeFuerSignatur = localeFuerSignatur;
	}

	public void cleanup() {
		jTextPane=null;
		jComboBoxFontNames = null;
		jComboBoxFontSizes = null;
		jComboBoxZoom = null;
		jTextPane = null;
		ruler=null;
		jScrollPane=null;
		jToolBarOther=null;
		
		actionFileNew = null;
		actionFileOpen = null;
		actionFileSave = null;
		actionFileSaveAs = null;
		actionFormatStyleBold = null;
		actionFormatStyleItalic = null;
		actionFormatStyleUnderline = null;
		actionFormatStyleStrikethrough = null;
		actionEditCut = null;
		actionEditCopy = null;
		actionEditPaste = null;
		actionInsertDateTimeUserShortcut = null;
		actionInsertSignatur = null;
		actionInsertTextbaustein = null;

		actionInsertTable = null;
		actionDeleteTable = null;
		actionInsertRowBefore = null;
		actionInsertRowAfter = null;
		actionDeleteRow = null;

		actionFormatAlignLeft = null;
		actionFormatAlignRight = null;
		actionFormatAlignCenter = null;

		actionFormatFont = null;
		actionFormatColorForeground = null;
		actionFormatColorBackground = null;
		actionEditUndo = null;
		actionEditRedo = null;
	}

	/**
	 * Konstruktor fuer den LpEditor. Ruft LpEditor(ownerFrame, null) auf.
	 * 
	 * @see #LpEditor(JFrame, Locale)
	 * @param ownerFrame
	 *            Der JFrame in dem der Editor eingebettet ist
	 */
	public LpEditor(JFrame ownerFrame) {
		this(ownerFrame, null);
	}

	/**
	 * Konstruktor fuer den LpEditor
	 * 
	 * @param ownerFrame
	 *            Der Frame, in dem der Editor eingebettet ist. Wird fuer das
	 *            oeffnen von weiteren Dialogen benoetigt.
	 * 
	 * @param locale
	 *            Die zu verwendende Sprache. null bedeutet default-locale
	 */
	public LpEditor(JFrame ownerFrame, Locale locale) {
		this.ownerFrame = ownerFrame;

		messages.setLocale(locale);

		try {
			String zoom = null;
			String font = null;
			String size = null;
			try {
				ArbeitsplatzparameterDto zoomDto = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_EDITOR_ZOOM);
				ArbeitsplatzparameterDto fontDto = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_EDITOR_SCHRIFTART);
				ArbeitsplatzparameterDto sizeDto = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_EDITOR_SCHRIFT_GROESSE);
				zoom = zoomDto == null ? null : zoomDto.getCWert();
				font = fontDto == null ? null : fontDto.getCWert();
				size = sizeDto == null ? null : sizeDto.getCWert();
			} catch (Throwable e) {
			}
			zoom =  zoom == null ? "100" : zoom;
			font = font == null ? "Arial" : font;
			size = size == null ? "10" : size;
			
			
			if (asSystemFonts == null) {
				try {
					setFontFilter(DelegateFactory.getInstance()
							.getSystemDelegate().getErlaubteFonts(), false);
				} catch (Throwable e) {
					setFontFilter(null, false);
				}
			}
			boolean fontAvailable = false;
			for(String sysFont : asSystemFonts) {
				if(sysFont.equals(font)) {
					fontAvailable = true;
					break;
				}
			}
			font = fontAvailable ? font : "Arial";
			Font awtFont = new Font(font, Font.PLAIN, new Integer(size));
			UIManager.put("TextPane.font", awtFont);
			jTextPane.registerEditorKitForContentType("text/jasper",
					"com.lp.editor.text.LpJasperReportEditorKit");
			jbInit();

			// AD: test
			/** @todo FontFilter PJ 4760 */
			// setFontFilter(new String[] {"Arial","Courier"},false);

			disableTableItems();

			Keymap defaultKeymap = jTextPane.getKeymap();
			customKeymap = jTextPane.addKeymap("custom", defaultKeymap);
			customKeymap.addActionForKeyStroke(
					actionEditCopy.getAcceleratorKey(), actionEditCopy);
			customKeymap.addActionForKeyStroke(
					actionEditPaste.getAcceleratorKey(), actionEditPaste);
			customKeymap.addActionForKeyStroke(
					actionEditCut.getAcceleratorKey(), actionEditCut);
			jTextPane.setKeymap(customKeymap);

			jTextPane.addCaretListener(caretListener);
			jTextPane.addMouseListener(lpMouseAdapter);
			jTextPane.setNavigationFilter(new LpNavigationFilter());
			jTextPane.requestFocusInWindow();

			jTextPane.setPageFormat(LpDecoratedTextPane.FORMAT_A4,
					LpDecoratedTextPane.UNIT_MM);

			// zur Zeit gibt es nur diesen Editor - Modus, der den
			// Editor fuer Jasper Reports vorbereitet
			if (iEditorMode == MODE_JASPER) {
				showStatusBar(true);
				showTabRuler(false);
				showMenu(false);
				showFileItems(false);
				/** @todo AD Test table PJ 4768 */
				// showTableItems(true);
				newFile("text/jasper");
			} else {
				newFile("text/plain");
			}

			jComboBoxZoom.setSelectedItem(zoom + "%");
			

		} catch (Exception ex) {
			myLogger.error(ex.getLocalizedMessage(), ex);
		}

	}

	/**
	 * Schaltet den Debug - Modus des Editors ein/aus. (Erzeugt eine weiteres
	 * Fenster zum Betracheten des Dokumenteninhalts und Ein - /Aus- Schalten
	 * der Bedienelemente.
	 * 
	 * @param bDebug
	 *            true: Debug-Modus einschalten, false: Debug - Modus
	 *            ausschalten.
	 */
	public void setDebug(boolean bDebug) {
		if (bDebug) {
			lpDocumentDumpFrame = new LpDocumentDumpFrame(this);
			lpDocumentDumpFrame.validate();
			lpDocumentDumpFrame.setSize(500, 500);
			lpDocumentDumpFrame.setLocation(200, 200);
			lpDocumentDumpFrame.setVisible(true);
		} else {
			lpDocumentDumpFrame.setVisible(false);
			lpDocumentDumpFrame = null;
		}
	}

	/**
	 * Initialisierung der Oberflaechenelemente des Editors.
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {

		// asAvailableFontNames =
		// GraphicsEnvironment.getLocalGraphicsEnvironment().
		// getAvailableFontFamilyNames();

		asAvailableFontSizes = new String[] { "8", "9", "10", "11", "12", "14",
				"16", "18", "20", "22", "24", "26", "28", "36", "48", "72" };

		asZoomFactors = new String[] { "50%", "75%", "100%", "125%", "150%",
				"175%", "200%" };

		actionFileNew = new ActionFileNew(this);
		actionFileOpen = new ActionFileOpen(this);
		actionFileSave = new ActionFileSave(this);
		actionFileSaveAs = new ActionFileSaveAs(this);
		actionFormatStyleBold = new ActionFormatStyleBold(this);
		actionFormatStyleItalic = new ActionFormatStyleItalic(this);
		actionFormatStyleUnderline = new ActionFormatStyleUnderline(this);
		actionFormatStyleStrikethrough = new ActionFormatStyleStrikethrough(
				this);
		actionEditCut = new ActionEditCut(this);
		actionEditCopy = new ActionEditCopy(this);
		actionEditPaste = new ActionEditPaste(this);
		actionInsertDateTimeUserShortcut = new ActionInsertDateTimeUserShortcut(
				this);
		actionInsertSignatur = new ActionInsertSignatur(this);
		actionInsertTextbaustein = new ActionInsertTextbaustein(this);

		actionInsertTable = new ActionInsertTable(this);
		actionDeleteTable = new ActionDeleteTable(this);
		actionInsertRowBefore = new ActionInsertRowAbove(this);
		actionInsertRowAfter = new ActionInsertRowBelow(this);
		actionDeleteRow = new ActionDeleteRow(this);

		jMenuEditPageBreak.setText(messages.getString("Menu.ShowPageBreak"));
		jMenuEditPageBreak.setMnemonic(messages.getMnemonic(
				"Menu.ShowPageBreak").intValue());
		jMenuEditPageBreak.setState(false);
		jMenuEditPageBreak.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				jTextPane.showPageBreak(jMenuEditPageBreak.isSelected());
			}
		});

		jMenuEditSettings.setText(messages.getString("Menu.Settings_"));
		jMenuEditSettings.setMnemonic(messages.getMnemonic("Menu.Settings_")
				.intValue());
		jMenuEditSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				LpEditorSettings lpSettings = new LpEditorSettings(ownerFrame,
						messages.getString("Dialog.Settings"));

				lpSettings.setFormat(jTextPane.getPageFormat());
				lpSettings.setPageMargin(jTextPane.getPageMargin());
				lpSettings.setVisible(true);
				if (lpSettings.getOption() == JOptionPane.OK_OPTION) {
					Dimension dim;
					if ((dim = lpSettings.getFormat()) != null) {
						jTextPane.setPageFormat(dim);
					}
					jTextPane.setPageMargin(lpSettings.getPageMargin());
				}
				lpSettings.dispose();
			}
		});

		actionFormatAlignLeft = new ActionFormatAlignment(this,
				"Action.LeftAlign", StyleConstants.ALIGN_LEFT);
		actionFormatAlignRight = new ActionFormatAlignment(this,
				"Action.RightAlign", StyleConstants.ALIGN_RIGHT);
		actionFormatAlignCenter = new ActionFormatAlignment(this,
				"Action.Center", StyleConstants.ALIGN_CENTER);

		// Funktioniert nicht (gleicher Effekt wie Center)
		// Bug ID: 4263904 auf bugs.sun.com
		actionFormatAlignJustified = new ActionFormatAlignment(this,
				"Action.Justify", StyleConstants.ALIGN_JUSTIFIED);
		actionFormatFont = new ActionFormatFont(this);
		actionFormatColorForeground = new ActionFormatColorForeground(this);
		actionFormatColorBackground = new ActionFormatColorBackground(this);
		actionEditUndo = new UndoAction(this);
		actionEditRedo = new RedoAction(this);

		// prepare Combo - Boxes:
		jComboBoxFontNames = new JComboBox(asSystemFonts);

		// AD: leere Eintraege wieder entfernen
		for (int i = 0; i < jComboBoxFontNames.getItemCount() - 1; i++)
			if (jComboBoxFontNames.getItemAt(i).toString().compareTo("") == 0)
				jComboBoxFontNames.removeItemAt(i);

		jComboBoxFontNames
				.setMaximumSize(jComboBoxFontNames.getPreferredSize());
		jComboBoxFontNames.setEditable(true);
		jComboBoxFontNames.setSelectedItem("Arial");

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (jComboBoxFontNames.getSelectedIndex() < 0) {
					return;
				}
				String sFontName = jComboBoxFontNames.getSelectedItem()
						.toString();
				MutableAttributeSet attributeSet = new SimpleAttributeSet();
				StyleConstants.setFontFamily(attributeSet, sFontName);
				setCharacterAttributes(attributeSet, false);
				requestFocusInWindow();
			}
		};
		jComboBoxFontNames.addActionListener(actionListener);

		jComboBoxFontSizes = new JComboBox(asAvailableFontSizes);
		Dimension d = new Dimension(Defaults.getInstance().bySizeFactor(50),
				jComboBoxFontSizes.getPreferredSize().height);
		jComboBoxFontSizes.setMaximumSize(d);
		jComboBoxFontSizes.setPreferredSize(d);
		jComboBoxFontSizes.setEditable(true);
		
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int fontSize = 0;
				try {
					fontSize = Integer.parseInt(jComboBoxFontSizes
							.getSelectedItem().toString());
				} catch (NumberFormatException ex) {
					return;
				}

				MutableAttributeSet attributeSet = new SimpleAttributeSet();
				StyleConstants.setFontSize(attributeSet, fontSize);
				setCharacterAttributes(attributeSet, false);
				requestFocusInWindow();
			}
		};
		jComboBoxFontSizes.addActionListener(actionListener);

		jComboBoxZoom = new JComboBox(asZoomFactors);
		jComboBoxZoom.setEditable(true); 
		jComboBoxZoom.setPreferredSize(new Dimension(Defaults.getInstance().bySizeFactor(60),
				jComboBoxZoom.getPreferredSize().height));
		// jComboBoxZoom.setSelectedIndex(2);
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (jComboBoxZoom.getSelectedItem() == null) {
					return;
				}
				String sZoom = jComboBoxZoom.getSelectedItem().toString()
						.replace("%", "");
				if(sZoom.isEmpty()) {
					jComboBoxZoom.setSelectedItem("100%");
					return;
				}
				double scale = new Double(sZoom) / 100;
				jTextPane.setZoomFactor(scale);
				jScrollPane.revalidate();
				jScrollPane.repaint();
			}
		};
		jComboBoxZoom.addActionListener(actionListener);

		// Status
		jPanelStatus.setMinimumSize(new Dimension(0, 24));
		jPanelStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		jPanelStatus.setLayout(borderLayout3);
		jLabelFileName.setText(messages.getString("Status.FileName"));
		jPanelStatusBuffer.setLayout(borderLayout5);

		jProgressBarBuffer.setMaximum(100);
		jProgressBarBuffer.setStringPainted(true);
		jComboBoxZoom.setRequestFocusEnabled(false);

		jPanelStatus.add(jLabelFileName, BorderLayout.CENTER);
		jPanelStatus.add(jPanelStatusBuffer, BorderLayout.EAST);
		jPanelStatusBuffer.add(jLabelBufferText, BorderLayout.WEST);
		jPanelStatusBuffer.add(jProgressBarBuffer, BorderLayout.CENTER);

		// Init MenuBar
		jMenuFile.setText(messages.getString("Menu.File"));
		jMenuFile.setMnemonic(messages.getMnemonic("Menu.File").intValue());
		jMenuFileNew.setAction(actionFileNew);
		jMenuFileOpen.setAction(actionFileOpen);
		jMenuFileSave.setAction(actionFileSave);
		jMenuFileSaveAs.setAction(actionFileSaveAs);
		jMenuFileExit.setText(messages.getString("Menu.Exit"));
		jMenuFileExit.setMnemonic(messages.getMnemonic("Menu.Exit").intValue());
		jMenuBar.add(jMenuFile);
		jMenuFile.add(jMenuFileNew);
		jMenuFile.add(jMenuFileOpen);
		jMenuFile.add(jMenuFileSave);
		jMenuFile.add(jMenuFileSaveAs);
		jMenuFile.addSeparator();
		jMenuFile.add(jMenuFileExit);

		jMenuEdit.setText(messages.getString("Menu.Edit"));
		jMenuEdit.setMnemonic(messages.getMnemonic("Menu.Edit").intValue());
		jMenuEditUndo.setAction(actionEditUndo);
		jMenuEditRedo.setAction(actionEditRedo);
		jMenuEditCut.setAction(actionEditCut);
		jMenuEditCopy.setAction(actionEditCopy);
		jMenuEditPaste.setAction(actionEditPaste);
		jMenuEditinsertDateTimeUserShortcut
				.setAction(actionInsertDateTimeUserShortcut);
		// jMenuEditSelectAll.setAction(actionEditSelectAll);
		jMenuEdit.add(jMenuEditUndo);
		jMenuEdit.add(jMenuEditRedo);
		jMenuEdit.addSeparator();
		jMenuEdit.add(jMenuEditCut);
		jMenuEdit.add(jMenuEditCopy);
		jMenuEdit.add(jMenuEditPaste);
		jMenuEdit.addSeparator();
		jMenuEdit.add(jMenuEditPageBreak);
		jMenuEdit.add(jMenuEditSettings);
		jMenuEdit.add(jMenuEditinsertDateTimeUserShortcut);
		jMenuBar.add(jMenuEdit);

		jMenuTable.setText(messages.getString("Menu.Table"));
		jMenuTable.setMnemonic(messages.getMnemonic("Menu.Table").intValue());
		jMenuTableInsert.setAction(actionInsertTable);
		jMenuTableDelete.setAction(actionDeleteTable);
		jMenuTableInsertRowBefore.setAction(actionInsertRowBefore);
		jMenuTableInsertRowAfter.setAction(actionInsertRowAfter);
		jMenuTableDeleteRow.setAction(actionDeleteRow);
		jMenuTable.add(jMenuTableInsert);
		jMenuTable.add(jMenuTableDelete);
		jMenuTable.add(jMenuTableInsertRowBefore);
		jMenuTable.add(jMenuTableInsertRowAfter);
		jMenuTable.add(jMenuTableDeleteRow);
		jMenuBar.add(jMenuTable);

		jMenuFormat.setText(messages.getString("Menu.Format"));
		jMenuFormat.setMnemonic(messages.getMnemonic("Menu.Format").intValue());
		jMenuFormatAlign.setText(messages.getString("Menu.Alignment"));
		jMenuFormatAlign.setMnemonic(messages.getMnemonic("Menu.Alignment")
				.intValue());
		jMenuFormatAlignLeft.setAction(actionFormatAlignLeft);
		jMenuFormatAlignRight.setAction(actionFormatAlignRight);
		jMenuFormatAlignCenter.setAction(actionFormatAlignCenter);
		jMenuFormatAlignJustified.setAction(actionFormatAlignJustified);
		jMenuFormatStyle.setText(messages.getString("Menu.Style"));
		jMenuFormatStyle.setMnemonic(messages.getMnemonic("Menu.Style")
				.intValue());
		jMenuFormatStyleBold.setAction(actionFormatStyleBold);
		jMenuFormatStyleItalic.setAction(actionFormatStyleItalic);
		jMenuFormatStyleUnderline.setAction(actionFormatStyleUnderline);
		jMenuFormatStyleStrikethrough.setAction(actionFormatStyleStrikethrough);
		jMenuFormatFont.setAction(actionFormatFont);
		jMenuFormatColor.setText(messages.getString("Menu.Color"));
		jMenuFormatColor.setMnemonic(messages.getMnemonic("Menu.Color")
				.intValue());
		jMenuFormatColorForeground.setAction(actionFormatColorForeground);
		jMenuFormatColorBackground.setAction(actionFormatColorBackground);
		jMenuBar.add(jMenuFormat);
		jMenuFormat.add(jMenuFormatAlign);
		jMenuFormatAlign.add(jMenuFormatAlignLeft);
		jMenuFormatAlign.add(jMenuFormatAlignRight);
		jMenuFormatAlign.add(jMenuFormatAlignCenter);
		// jMenuFormatAlign.add(jMenuFormatAlignJustified);
		jMenuFormat.add(jMenuFormatStyle);
		jMenuFormatStyle.add(jMenuFormatStyleBold);
		jMenuFormatStyle.add(jMenuFormatStyleItalic);
		jMenuFormatStyle.add(jMenuFormatStyleUnderline);
		jMenuFormatStyle.add(jMenuFormatStyleStrikethrough);
		jMenuFormat.addSeparator();
		jMenuFormat.add(jMenuFormatFont);
		jMenuFormat.add(jMenuFormatColor);
		jMenuFormatColor.add(jMenuFormatColorForeground);
		jMenuFormatColor.add(jMenuFormatColorBackground);

		// popup - Menu:

		jMenuEditUndoPopup.setAction(actionEditUndo);
		jMenuEditRedoPopup.setAction(actionEditRedo);
		jMenuEditCutPopup.setAction(actionEditCut);
		jMenuEditCopyPopup.setAction(actionEditCopy);
		jMenuEditPastePopup.setAction(actionEditPaste);
		jMenuEditinsertDateTimeUserShortcutPopup
				.setAction(actionInsertDateTimeUserShortcut);
		jMenuEditinsertSignatur.setAction(actionInsertSignatur);
		jMenuEditinsertTextbaustein.setAction(actionInsertTextbaustein);
		// jMenuEditSelectAllPopup.setAction(actionEditSelectAll);

		jMenuTablePopup.setText(jMenuTable.getText());
		jMenuTablePopup.setMnemonic(jMenuTable.getMnemonic());
		jMenuTableInsertPopup.setAction(actionInsertTable);
		jMenuTableDeletePopup.setAction(actionDeleteTable);
		jMenuTableInsertRowBeforePopup.setAction(actionInsertRowBefore);
		jMenuTableInsertRowAfterPopup.setAction(actionInsertRowAfter);
		jMenuTableDeleteRowPopup.setAction(actionDeleteRow);
		jMenuTablePopup.add(jMenuTableInsertPopup);
		jMenuTablePopup.add(jMenuTableDeletePopup);
		jMenuTablePopup.add(jMenuTableInsertRowBeforePopup);
		jMenuTablePopup.add(jMenuTableInsertRowAfterPopup);
		jMenuTablePopup.add(jMenuTableDeleteRowPopup);

		jMenuFormatPopup.setText(jMenuFormat.getText());
		jMenuFormatPopup.setMnemonic(jMenuFormat.getMnemonic());
		jMenuFormatAlignPopup.setText(jMenuFormatAlign.getText());
		jMenuFormatAlignPopup.setMnemonic(jMenuFormatAlign.getMnemonic());
		jMenuFormatAlignLeftPopup.setAction(actionFormatAlignLeft);
		jMenuFormatAlignRightPopup.setAction(actionFormatAlignRight);
		jMenuFormatAlignCenterPopup.setAction(actionFormatAlignCenter);
		jMenuFormatAlignJustifiedPopup.setAction(actionFormatAlignJustified);

		jMenuFormatStylePopup.setText(jMenuFormatStyle.getText());
		jMenuFormatStylePopup.setMnemonic(jMenuFormatStyle.getMnemonic());
		jMenuFormatStyleBoldPopup.setAction(actionFormatStyleBold);
		jMenuFormatStyleItalicPopup.setAction(actionFormatStyleItalic);
		jMenuFormatStyleUnderlinePopup.setAction(actionFormatStyleUnderline);
		jMenuFormatStyleStrikethroughPopup
				.setAction(actionFormatStyleStrikethrough);
		jMenuFormatFontPopup.setAction(actionFormatFont);

		jMenuFormatColorPopup.setText(jMenuFormatColor.getText());
		jMenuFormatColorPopup.setMnemonic(jMenuFormatColor.getMnemonic());
		jMenuFormatColorForegroundPopup.setAction(actionFormatColorForeground);
		jMenuFormatColorBackgroundPopup.setAction(actionFormatColorBackground);

		jMenuFormatPopup.add(jMenuFormatAlignPopup);
		jMenuFormatAlignPopup.add(jMenuFormatAlignLeftPopup);
		jMenuFormatAlignPopup.add(jMenuFormatAlignRightPopup);
		jMenuFormatAlignPopup.add(jMenuFormatAlignCenterPopup);
		// jMenuFormatAlignPopup.add(jMenuFormatAlignJustifiedPopup);
		jMenuFormatPopup.add(jMenuFormatStylePopup);
		jMenuFormatStylePopup.add(jMenuFormatStyleBoldPopup);
		jMenuFormatStylePopup.add(jMenuFormatStyleItalicPopup);
		jMenuFormatStylePopup.add(jMenuFormatStyleUnderlinePopup);
		jMenuFormatStylePopup.add(jMenuFormatStyleStrikethroughPopup);
		jMenuFormatPopup.addSeparator();
		jMenuFormatPopup.add(jMenuFormatFontPopup);
		jMenuFormatPopup.add(jMenuFormatColorPopup);
		jMenuFormatColorPopup.add(jMenuFormatColorForegroundPopup);
		jMenuFormatColorPopup.add(jMenuFormatColorBackgroundPopup);

		jPopupMenu.add(jMenuEditUndoPopup);
		jPopupMenu.add(jMenuEditRedoPopup);
		jPopupMenu.addSeparator();
		jPopupMenu.add(jMenuEditCutPopup);
		jPopupMenu.add(jMenuEditCopyPopup);
		jPopupMenu.add(jMenuEditPastePopup);
		jPopupMenu.addSeparator();
		jPopupMenu.add(jMenuFormatPopup);
		// jPopupMenu.add(jMenuEditSelectAllPopup);
		// jPopupMenu.addSeparator();
		jPopupMenu.add(jMenuEditinsertDateTimeUserShortcutPopup);
		jPopupMenu.add(jMenuTablePopup);

		// Toolbar
		// File Items
		jToolBarFile.setFloatable(false);
		jToolBarFile.setRollover(true);

		lpButtonFileNew.setAction(actionFileNew);
		lpButtonFileNew.setMnemonic(0);
		jToolBarFile.add(lpButtonFileNew);
		lpButtonFileOpen.setAction(actionFileOpen);
		lpButtonFileOpen.setMnemonic(0);
		jToolBarFile.add(lpButtonFileOpen);
		lpButtonFileSave.setAction(actionFileSave);
		lpButtonFileSave.setMnemonic(0);
		jToolBarFile.add(lpButtonFileSave);

		// Other Items
		jToolBarOther.setFloatable(false);
		jToolBarOther.setRollover(true);

		lpButtonEditUndo.setAction(actionEditUndo);
		lpButtonEditUndo.setMnemonic(0);
		jToolBarOther.add(lpButtonEditUndo);

		lpButtonEditRedo.setAction(actionEditRedo);
		lpButtonEditRedo.setMnemonic(0);
		jToolBarOther.add(lpButtonEditRedo);

		jToolBarOther.addSeparator();

		lpButtonEditCut.setAction(actionEditCut);
		lpButtonEditCut.setMnemonic(0);
		jToolBarOther.add(lpButtonEditCut);

		lpButtonEditCopy.setAction(actionEditCopy);
		lpButtonEditCopy.setMnemonic(0);
		jToolBarOther.add(lpButtonEditCopy);

		lpButtonEditPaste.setAction(actionEditPaste);
		lpButtonEditPaste.setMnemonic(0);
		jToolBarOther.add(lpButtonEditPaste);

		lpButtonInsertDateTimeUserShortcut
				.setAction(actionInsertDateTimeUserShortcut);
		lpButtonInsertDateTimeUserShortcut.setMnemonic(0);
		jToolBarOther.add(lpButtonInsertDateTimeUserShortcut);

		lpButtonInsertSignatur.setAction(actionInsertSignatur);
		lpButtonInsertSignatur.setMnemonic(0);
		jToolBarOther.add(lpButtonInsertSignatur);
		
		lpButtonInsertTextbaustein.setAction(actionInsertTextbaustein);
		lpButtonInsertTextbaustein.setMnemonic(0);
		jToolBarOther.add(lpButtonInsertTextbaustein);

		jToolBarOther.addSeparator();

		jToolBarOther.add(jComboBoxFontNames);
		jToolBarOther.add(jComboBoxFontSizes);

		// @todo: HK spaeter einbauen PJ 4769
		// lpButtonFormatFont.setAction(actionFormatFont);
		// lpButtonFormatFont.setMnemonic(0);
		// jToolBarOther.add(lpButtonFormatFont);

		// Alignment Toolbar
		jToolBarAlignment.setFloatable(false);
		jToolBarAlignment.setRollover(true);

		lpButtonFormatAlignLeft.setAction(actionFormatAlignLeft);
		lpButtonFormatAlignLeft.setMnemonic(0);
		jToolBarAlignment.add(lpButtonFormatAlignLeft);
		buttonGroupAlign.add(lpButtonFormatAlignLeft);

		lpButtonFormatAlignRight.setAction(actionFormatAlignRight);
		lpButtonFormatAlignRight.setMnemonic(0);
		jToolBarAlignment.add(lpButtonFormatAlignRight);
		buttonGroupAlign.add(lpButtonFormatAlignRight);

		lpButtonFormatAlignCenter.setAction(actionFormatAlignCenter);
		lpButtonFormatAlignCenter.setMnemonic(0);
		jToolBarAlignment.add(lpButtonFormatAlignCenter);
		buttonGroupAlign.add(lpButtonFormatAlignCenter);

		lpButtonFormatAlignJustified.setAction(actionFormatAlignJustified);
		lpButtonFormatAlignJustified.setMnemonic(0);
		// jToolBarAlignment.add(lpButtonFormatAlignJustified);
		// buttonGroupAlign.add(lpButtonFormatAlignJustified);

		// FontStyle Toolbar
		jToolBarFontStyle.setFloatable(false);
		jToolBarFontStyle.setRollover(true);

		lpButtonFormatStyleBold.setAction(actionFormatStyleBold);
		lpButtonFormatStyleBold.setMnemonic(0);
		jToolBarFontStyle.add(lpButtonFormatStyleBold);

		lpButtonFormatStyleItalic.setAction(actionFormatStyleItalic);
		lpButtonFormatStyleItalic.setMnemonic(0);
		jToolBarFontStyle.add(lpButtonFormatStyleItalic);

		lpButtonFormatStyleUnderline.setAction(actionFormatStyleUnderline);
		lpButtonFormatStyleUnderline.setMnemonic(0);
		jToolBarFontStyle.add(lpButtonFormatStyleUnderline);

		lpButtonFormatStyleStrikethrough
				.setAction(actionFormatStyleStrikethrough);
		lpButtonFormatStyleStrikethrough.setMnemonic(0);
		jToolBarFontStyle.add(lpButtonFormatStyleStrikethrough);

		jToolBarFontStyle.addSeparator();

		lpButtonFormatColorForeground.setAction(actionFormatColorForeground);
		lpButtonFormatColorForeground.setMnemonic(0);
		jToolBarFontStyle.add(lpButtonFormatColorForeground);

		lpButtonFormatColorBackground.setAction(actionFormatColorBackground);
		lpButtonFormatColorBackground.setMnemonic(0);
		jToolBarFontStyle.add(lpButtonFormatColorBackground);

		// Table Items
		jToolBarTable.setFloatable(false);
		jToolBarTable.setRollover(true);
		lpButtonInsertTable.setAction(actionInsertTable);
		lpButtonInsertTable.setMnemonic(0);
		lpButtonDeleteTable.setAction(actionDeleteTable);
		lpButtonDeleteTable.setMnemonic(0);
		lpButtonInsertRowBefore.setAction(actionInsertRowBefore);
		lpButtonInsertRowBefore.setMnemonic(0);
		lpButtonInsertRowAfter.setAction(actionInsertRowAfter);
		lpButtonInsertRowAfter.setMnemonic(0);
		lpButtonDeleteRow.setAction(actionDeleteRow);
		lpButtonDeleteRow.setMnemonic(0);
		jToolBarTable.add(lpButtonInsertTable);
		jToolBarTable.add(lpButtonDeleteTable);
		jToolBarTable.add(lpButtonInsertRowBefore);
		jToolBarTable.add(lpButtonInsertRowAfter);
		jToolBarTable.add(lpButtonDeleteRow);

		jPanelToolBars.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 0));
		jPanelToolBars.add(jToolBarFile);
		jPanelToolBars.add(jToolBarOther);
		jPanelToolBars.add(jToolBarAlignment);
		jPanelToolBars.add(jToolBarFontStyle);
		jPanelToolBars.add(jToolBarTable);
		jPanelToolBars.add(jComboBoxZoom);

		// Settings:

		// Init Edit - area
		jToolBarOther.setOrientation(JToolBar.HORIZONTAL);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Dimension dim = new Dimension(Integer.MAX_VALUE,
				jMenuBar.getPreferredSize().height);
		jMenuBar.setMaximumSize(dim);
		jMenuBar.setMinimumSize(dim);

		this.add(jMenuBar);
		this.add(jPanelToolBars);

		jTextPaneBox = Box.createHorizontalBox();
		jTextPane = new LpDecoratedTextPane();
		jTextPaneBox.add(Box.createHorizontalGlue());
		LpEditorSheetPanel sheet = new LpEditorSheetPanel(jTextPane);
		jTextPaneBox.add(sheet);
		jTextPaneBox.add(Box.createHorizontalGlue());
		// jTextPaneBox.setMinimumSize(sheet.getPreferredSize());
		jScrollPane.getViewport().add(jTextPaneBox, null);
		rulerBox = Box.createHorizontalBox();
		ruler = new LpRuler(jTextPane);
		jTextPane.setRuler(ruler);
		rulerBox.add(Box.createHorizontalGlue());
		rulerBox.add(ruler);
		rulerBox.add(Box.createHorizontalGlue());
		jScrollPane.setColumnHeaderView(rulerBox);

		this.add(jScrollPane);
		this.add(jPanelStatus);
	}

	/**
	 * Oeffentlicher Zugriff auf das MenuBar - Objekt des Lp-Editors. Damit ist
	 * es moeglich, die MenuBar in das Menue eines uebergeordneten Objekts
	 * einzubinden. Das interne Menue kann dann ueber {@link #showMenu} ein bzw.
	 * ausgeschaltet werden.
	 * 
	 * @see #showMenu
	 * @return Das intern konfigurierte Menue des Editors.
	 * 
	 */
	public JMenuBar getMenuBar() {
		return jMenuBar;
	}

	/**
	 * Liefert das MenuItem "Exit". Damit kann der uebergeordnete Frame einen
	 * ActionListener verknuepfen.
	 * 
	 * @see #getMenuBar
	 * @return Das Exit MenuItem.
	 */
	public JMenuItem getExitMenuItem() {
		return jMenuFileExit;
	}

	/**
	 * Oeffentlicher Zugriff auf das JTextPaneElement. Sollte nur selten
	 * gebraucht werden.
	 * 
	 * @see com.lp.editor.ui.LpTextPane
	 * @return das interne LpTextPane
	 */
	public LpDecoratedTextPane getTextPane() {
		return jTextPane;
	}

	/**
	 * Liefert den JasperReport, passend zu dem formatierten Inhalt zurueck. Der
	 * JasperReport wird vollstaendig neu erstellt, sodass er den aktuellen
	 * Formatierung entspricht. Wurde mit setJasperReport(JasperReport) eine
	 * Vorlage zur Verfuegung gestellt, so werden zur Zeit zusaetzliche
	 * Informationen darin nicht in den neuen Report uebernommen.
	 * 
	 * @return Der neue JasperReport, falls ContentType gleich "text/jasper"
	 *         ist, null sonst.
	 * 
	 * @see #setJasperReport(JasperReport)
	 * @see #newFile(String)
	 */
	/*
	 * public JasperReport getJasperReport() { if
	 * (!jTextPane.getContentType().equals("text/jasper")) { return null; }
	 * 
	 * if (jTextPane.getContentType().equals("text/jasper")) { if
	 * (jasperReportTemplate == null) { return ((LpJasperReportEditorKit)
	 * jTextPane.getEditorKit())
	 * .getJasperReport(jTextPane.getStyledDocument()); } else { return
	 * jasperReportTemplate; } } else { return null; } }
	 */

	/**
	 * Liefert den formatierten Inhalt des Editors in strukturierter Form.
	 * 
	 * @see com.lp.editor.util.LpEditorReportData
	 * @see com.lp.editor.util.LpEditorRow
	 * 
	 * @throws TextBlockOverflowException
	 * 
	 * @return Den Editor Inhalt, falls ContentType gleich "text/jasper" ist,
	 *         null sonst.
	 */
	public LpEditorReportData getReportData() throws TextBlockOverflowException {
		if (!jTextPane.getContentType().equals("text/jasper")) {
			return null;
		}

		if (currentCellEditor != null) {
			currentCellEditor.stopCellEditing();

		}
		LpEditorReportData data = LpJasperGenerator.getReportData(jTextPane
				.getStyledDocument());
		if (data == null) {
			return data;
		}

		TextBlockAttributes textBlock = null;
		ArrayList<?> rows = data.getRows();
		for (int i = 0; i < rows.size(); i++) {
			LpEditorRow row = (LpEditorRow) rows.get(i);
			if (row.getText() != null && textPaneAttributes.capacity > 0
					&& row.getText().length() > textPaneAttributes.capacity) {
				throw new TextBlockOverflowException(
						messages.getString("TextBlockOverflowException.Overflow"));
			}
			for (int j = 0; j < data.getColumnNum(); j++) {
				textBlock = vecColTextAttributes.get(j);
				if (row.getTableText(j) != null && textBlock.capacity > 0
						&& row.getTableText(j).length() > textBlock.capacity) {
					throw new TextBlockOverflowException(
							messages.getString("TextBlockOverflowException.Overflow"));
				}
			}
		}

		return data;
	}

	/**
	 * Setzt den Inhalt des Editors auf data
	 * 
	 * @param data
	 *            LpEditorReportData
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void setReportData(LpEditorReportData data)
			throws ArrayIndexOutOfBoundsException {
		if (data.getColumnNum() != vecColTextAttributes.size()) {
			throw new ArrayIndexOutOfBoundsException(
					messages.getString("ArrayIndexOutOfBoundsException.ColumnNumberMismatch"));
		}

		if (!jTextPane.getContentType().equals("text/jasper")) {
			return;
		}

		boolean bSavedEditableState = isEditable();
		jTextPane.setEditable(true);

		jTextPane.setText("");

		ArrayList<?> list = data.getRows();
		Iterator<?> iter = list.iterator();
		LpEditorRow row = null;
		LpEditorTableModel model = null;
		boolean bTableFound = false;
		while (iter.hasNext()) {
			row = (LpEditorRow) iter.next();

			if (row.getText() != null && row.getText().length() > 0) {
				if (bTableFound) {
					insertTable(new LpEditorTable(model));
					bTableFound = false;
				}
				LpJasperParser.parseString(row.getText(),
						jTextPane.getStyledDocument(), jTextPane
								.getStyledDocument().getEndPosition()
								.getOffset() - 1);
			} else {
				if (!bTableFound) {
					model = new LpEditorTableModel(0, data.getColumnNum());
					bTableFound = true;
				}
				Object[] rowData = new Object[data.getColumnNum()];
				for (int i = 0; i < data.getColumnNum(); i++) {
					rowData[i] = row.getTableText(i);
				}
				model.addRow(rowData);
			}
		}

		if (bTableFound) {
			insertTable(new LpEditorTable(model));
		}

		jTextPane.setEditable(bSavedEditableState);
	}

	/**
	 * Ueberschreibt JComponent.requestFocusInWindow() um dem CellEditor den
	 * Focus geben zu koennen, falls dieser aktiv ist.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean requestFocusInWindow() {
		if (currentCellEditor != null) {
			if (currentCellEditor.isEditing()) {
				return currentCellEditor.getEditor().requestFocusInWindow();
			}
		}

		disableTableItems();
		// enableFontStyleItems(textPaneAttributes.isStyledText);
		textStyleChanged();

		actionEditRedo.updateRedoState();
		actionEditUndo.updateUndoState();

		return jTextPane.requestFocusInWindow();
	}

	StyledDocument getStyledDocument() {
		if (currentCellEditor != null && currentCellEditor.isEditing()) {
			return currentCellEditor.getEditor().getStyledDocument();
		} else {
			return jTextPane.getStyledDocument();
		}
	}

	int getCaretPosition() {
		if (currentCellEditor != null && currentCellEditor.isEditing()) {
			return currentCellEditor.getEditor().getCaretPosition();
		} else {
			return jTextPane.getCaretPosition();
		}
	}

	Caret getCaret() {
		if (currentCellEditor != null && currentCellEditor.isEditing()) {
			return currentCellEditor.getEditor().getCaret();
		} else {
			return jTextPane.getCaret();
		}
	}

	void setCharacterAttributes(AttributeSet attributes, boolean flag) {
		if (currentCellEditor != null && currentCellEditor.isEditing()) {
			currentCellEditor.getEditor().setCharacterAttributes(attributes,
					flag);
		} else {
			jTextPane.setCharacterAttributes(attributes, flag);
		}
	}

	AttributeSet getCharacterAttributes() {
		if (currentCellEditor != null && currentCellEditor.isEditing()) {
			return currentCellEditor.getEditor().getCharacterAttributes();
		} else {
			return jTextPane.getCharacterAttributes();
		}
	}

	public void printMarkerInfo() {
		// System.out.println("marker before count: " +
		// vecMarkersBefore.size());
		// System.out.println("marker after count : " + vecMarkersAfter.size());

		for (int i = 0; i < vecMarkersBefore.size(); i++) {
			// System.out.println("" + i + " markers range " +
			// ((Position)vecMarkersBefore.get(i)).getOffset()
			// + " - " + ((Position)vecMarkersAfter.get(i)).getOffset());
		}
	}

	void insertTable(LpEditorTable table) {
		insertTable(table, jTextPane.getCaretPosition());
	}

	protected void insertTable(LpEditorTable table, int offset) {

		LpEditorTableModel tableModel = null;
		Vector<?> colAttr = (Vector<?>) jTextPane.getStyledDocument()
				.getProperty(LpEditor.COLUMN_TEXT_ATTRIBUTES);

		boolean bCalcRowHeight = false;
		if (table == null) {
			tableModel = new LpEditorTableModel(5, colAttr.size());
			table = new LpEditorTable(tableModel);
		} else {
			tableModel = (LpEditorTableModel) table.getModel();
			bCalcRowHeight = true;
		}

		table.putClientProperty(LpEditor.COLUMN_TEXT_ATTRIBUTES, colAttr);

		table.addMouseListener(lpMouseAdapter);

		if (vecCellEditors.size() == 0) {
			setupNewCellEditor(null);
		}
		if (vecCellRenderers.size() == 0) {
			vecCellRenderers.add(new LpEditorCellRenderer());
		}
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(((TextBlockAttributes) colAttr.get(i)).width);
			if (i >= vecCellEditors.size()) {
				column.setCellEditor(vecCellEditors.get(0));
			} else {
				column.setCellEditor(vecCellEditors.get(i));

			}
			if (i >= vecCellRenderers.size()) {
				column.setCellRenderer(vecCellRenderers.get(0));
			} else {
				column.setCellRenderer(vecCellRenderers.get(i));
			}
		}

		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.updateRowHeight(bCalcRowHeight);

		try {
			bUpdateBuffer = false;

			jTextPane.removeOverflowRange(currentTextBlockStart,
					currentTextBlockEnd);

			int index = vecMarkersBefore.size();
			if (vecMarkersBefore.size() > 0) {
				int start = 0;
				int end = 0;
				for (int i = 0; i < vecMarkersBefore.size(); i++) {
					if (i > 0)
						start = vecMarkersAfter.get(i - 1).getOffset();
					end = vecMarkersBefore.get(i).getOffset();

					if (start <= offset && end >= offset) {
						index = i;
						break;
					}
				}
			} else
				index = 0;

			// Hier wird die Tabelle inklusive eines "\n" direkt davor und
			// danach
			// eingefuegt. Position - Objekte werden fuer den DocumentFilter und
			// NavigationFilter benoetigt, sowie zum entfernent der Tabelle
			Position savedPos = jTextPane.getDocument().createPosition(
					jTextPane.getCaretPosition());

			jTextPane.setCaretPosition(offset);
			int additionalInsert = 0;
			if (offset == 0
					|| !jTextPane.getDocument().getText(offset - 1, 1)
							.equals("\n")) {
				additionalInsert = 1;
			}

			undoManager.startCompoundTableEdit(true, table, index, offset
					+ additionalInsert);

			bForceInsert = true;
			if (additionalInsert > 0) {
				jTextPane.getStyledDocument().insertString(offset, "\n", null);
				table.putClientProperty(EXTRA_TABLE_NEWLINE, Boolean.TRUE);
			} else {
				table.putClientProperty(EXTRA_TABLE_NEWLINE, Boolean.FALSE);
			}

			jTextPane.getStyledDocument().insertString(
					offset + additionalInsert, "\n", null);
			jTextPane.setCaretPosition(offset + additionalInsert);
			bUpdateBuffer = true;
			jTextPane.insertComponent(table);

			bForceInsert = false;

			undoManager.endCompoundTableEdit();

			jTextPane.setCaretPosition(savedPos.getOffset());
		} catch (BadLocationException e) {
			myLogger.error(e.getLocalizedMessage(), e);
		} finally {
			bUpdateBuffer = true;
		}
	}

	protected void setupTableData(LpEditorTable table, int offset) {
		LpEditorTableModel model = (LpEditorTableModel) table.getModel();

		vecTableModels.add(model);
		vecTables.add(table);

		try {
			Position posBefore = jTextPane.getStyledDocument().createPosition(
					offset);
			Position posAfter = jTextPane.getStyledDocument().createPosition(
					offset + 1);
			boolean bInserted = false;
			for (int i = 0; i < vecMarkersBefore.size(); i++) {
				if (vecMarkersBefore.get(i).getOffset() > posBefore.getOffset()) {
					vecMarkersBefore.add(i, posBefore);
					vecMarkersAfter.add(i, posAfter);
					bInserted = true;
					break;
				}
			}
			if (!bInserted) {
				vecMarkersBefore.add(posBefore);
				vecMarkersAfter.add(posAfter);
			}
			model.setPosBefore(posBefore);
			model.setPosAfter(posAfter);

			updateBufferStatus(textPaneAttributes, posBefore.getOffset() - 1,
					true);
			updateBufferStatus(textPaneAttributes, posAfter.getOffset() + 1,
					true);
		} catch (BadLocationException exc) {
			myLogger.error(exc.getLocalizedMessage(), exc);
		}
	}

	void deleteTable() {
		deleteTable(currentTable);
	}

	void deleteTable(LpEditorTable table) {

		LpEditorTableModel model = (LpEditorTableModel) table.getModel();
		StyledDocument doc = jTextPane.getStyledDocument();

		Position posBefore = model.getPosBefore();
		Position posAfter = model.getPosAfter();
		// int index = vecTables.indexOf(table);

		try {
			Boolean bExtraNewline = (Boolean) table
					.getClientProperty(EXTRA_TABLE_NEWLINE);
			int additionalRemove = 0;
			if (bExtraNewline != null && bExtraNewline.booleanValue()) {
				additionalRemove = 1;
			}

			undoManager.startCompoundTableEdit(false, table,
					vecTables.indexOf(table), posBefore.getOffset()
							- additionalRemove);
			doc.remove(posBefore.getOffset() - additionalRemove,
					posAfter.getOffset() + 1 - posBefore.getOffset()
							+ additionalRemove);
			undoManager.endCompoundTableEdit();
		} catch (BadLocationException ex) {
			myLogger.error(ex.getLocalizedMessage(), ex);
		}
	}

	protected void cleanTableData(int index) {
		Position posBefore = vecMarkersBefore.get(index);
		Position posAfter = vecMarkersAfter.get(index);

		vecMarkersBefore.remove(index);
		vecMarkersAfter.remove(index);

		vecTables.remove(index);
		vecTableModels.remove(index);

		if (index > 0) {
			jTextPane.removeOverflowRange(vecMarkersAfter.get(index - 1),
					posBefore);
		} else {
			jTextPane.removeOverflowRange(jTextPane.getDocument()
					.getStartPosition(), posBefore);
		}
		if (index < vecMarkersBefore.size()) {
			jTextPane
					.removeOverflowRange(posAfter, vecMarkersBefore.get(index));
		} else {
			jTextPane.removeOverflowRange(posAfter, jTextPane.getDocument()
					.getEndPosition());

		}
		currentTable = null;
		currentCellEditor = null;
		currentTextBlockAttributes = textPaneAttributes;
		updateBufferStatus(null, -1, true);
		disableTableItems();
	}

	void deleteTableRow() {
		if (currentTable != null) {
			if (currentCellEditor != null) {
				currentCellEditor.stopCellEditing();

			}
			LpEditorTableModel tableModel = (LpEditorTableModel) currentTable
					.getModel();

			if (tableModel.getRowCount() == 1) {
				deleteTable();
			} else {
				int[] selRow = currentTable.getSelectedRows();

				for (int i = 0; i < selRow.length; i++) {
					tableModel.removeRow(selRow[0]);
				}
				int iRowCount = tableModel.getRowCount();
				if (selRow[0] >= iRowCount) {
					currentTable.setRowSelectionInterval(iRowCount - 1,
							iRowCount - 1);
				} else {
					currentTable.setRowSelectionInterval(selRow[0], selRow[0]);
				}
			}
		}
	}

	void insertTableRow(boolean above) {
		if (currentTable != null) {
			LpEditorTableModel model = (LpEditorTableModel) currentTable
					.getModel();
			int[] selRows = currentTable.getSelectedRows();
			int insIndex = 0;
			if (above) {
				insIndex = selRows[0];
			} else {
				insIndex = selRows[selRows.length - 1] + 1;
			}

			Object[] row = new Object[vecColTextAttributes.size()];
			for (int i = 0; i < row.length; i++) {
				row[i] = null;
			}

			if (currentCellEditor != null) {
				currentCellEditor.stopCellEditing();
			}

			model.insertRow(insIndex, row);
		}
	}

	/**
	 * Liefert die Attribute des Text-Blocks. Diese koennen dann geaendert
	 * werden, um z.B. die Kapazitaet festzulegen. Bei Verwendung einer Vorlage
	 * (z.B. JasperReport) sollten Attribute erst nach dem Setzen dieser
	 * geaendert werden.
	 * 
	 * @param index
	 *            Wenn kleiner als 0, dann werden die Attribute des Fliesztextes
	 *            uebergeben. Wenn kleiner als die Anzahl der Tabellenspalten
	 *            werden die Attribute der Spalte mit dem passenden Index
	 *            uebergeben, sonst null.
	 * 
	 * @return TextBlockAttributes
	 * 
	 * @see TextBlockAttributes
	 */
	public TextBlockAttributes getTextBlockAttributes(int index) {
		if (index < 0) {
			return textPaneAttributes;
		} else if (index < vecColTextAttributes.size()) {
			return vecColTextAttributes.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Formatierten Text erlauben - damit werden auch die Controls
	 * freigeschaltet
	 */
	public void enableStyledText() {
		textPaneAttributes.isStyledText = true;
		textStyleChanged();
	}

	/**
	 * Formatierten Text sperren - damit werden auch die Controls gesperrt
	 */
	public void disableStyledText() {
		textPaneAttributes.isStyledText = false;
		textStyleChanged();
	}

	/**
	 * Formatierten Text erlauben/sperren
	 * 
	 * @param styledText
	 *            erlauben
	 */
	public void beStyledText(boolean styledText) {
		textPaneAttributes.isStyledText = styledText;
		textStyleChanged();
	}

	public boolean getStyledText() {
		return textPaneAttributes.isStyledText;
	}

	private void textStyleChanged() {
		boolean newValue = textPaneAttributes.isStyledText;
		enableFontStyleItems(newValue);
		enableFontItems(newValue);

		showFontStyleItems(newValue);
		showFontItems(newValue);
	}

	public void enableFontItems(boolean enable) {
		jComboBoxFontNames.setEnabled(enable);
		jComboBoxFontSizes.setEnabled(enable);
	}

	public void showFontItems(boolean visible) {
		jComboBoxFontNames.setVisible(visible);
		jComboBoxFontSizes.setVisible(visible);
	}

	/**
	 * Setzt die Anzahl der Spalten. Ihre Attribute werden mit Default-Werten
	 * initialisiert. Sollte nicht nach dem Laden einer Vorlage aufgerufen
	 * werden.
	 * 
	 * @param count
	 *            Anzahl der Spalten
	 */
	public void setColumnCount(int count) {
		vecColTextAttributes.clear();
		for (int i = 0; i < count; i++) {
			vecColTextAttributes.add(new TextBlockAttributes());
		}
	}

	/**
	 * Ruft getText() des verwendeten JTextPane auf. Abhaengig vom Editor-Modus
	 * wird der Text inklusive Format-Tags zurueckgegeben. Im Jasper Modus wird
	 * der Text inklusive Tabellen-Daten (in HTML-Tags) zurueckgegeben.
	 * 
	 * @see #setText
	 * @see #getReportData
	 * @throws TextBlockOverflowException
	 * 
	 * @return Den gesamten Inhalt des Editors (inklusive Format - tags).
	 */
	public String getText() throws TextBlockOverflowException {
		boolean tableStart = false;
		String s = null;
		String strText = "";

		LpEditorReportData data = this.getReportData();
		Iterator<?> it = data.getRows().iterator();
		while (it.hasNext()) {
			LpEditorRow row = (LpEditorRow) it.next();
			s = row.getText();
			if (s != null) {
				if (tableStart == true) {
					strText += "</table>";
					tableStart = false;
				}
				strText += s;
				// strText += "\n";
			} else {
				if (tableStart == false) {
					strText += "<table>";
					tableStart = true;
				}
				strText += row.getTableRowText();
				strText += "\n";
			}
		}
		if (tableStart == true)
			strText += "</table>";

		// String strText = jTextPane.getText();

		if (textPaneAttributes.capacity <= 0) {
			return strText;
		}

		if (strText.length() > textPaneAttributes.capacity) {
			throw new TextBlockOverflowException(
					messages.getString("TextBlockOverflowException.Overflow"));
		} else {
			return strText;
		}
	}

	public String getPlainText() throws TextBlockOverflowException {
		String strText = getText();
		return getTextBasedOnStyle(strText);
	}

	/**
	 * Liefert den Text passend zum Text-Stil zur&uuml;ck.
	 * 
	 * Hintergrund: F&uuml;r die Jasper-Reports werden einige Zeichen in HTML
	 * encoding dargestellt. In einem "PlainText" Dokument will ich aber kein
	 * &amp; sondern ein & haben.
	 * 
	 * Das das hier an dieser Stelle ist, ist mehr als &uuml;berarbeitungs-
	 * w&uuml;rdig. Eigentlich m&uuml;sste man das dem LpJasperGenerator
	 * beibringen, der den Text mittels getReportData() liefert. Das ist aber
	 * eine &ouml;ffentliche Methode und zus&auml;tzlich werden die so
	 * gelieferten Daten auch noch manipuliert.
	 * 
	 * Vielleicht f&auml;llt mir da sp&auml;ter noch was sinnvolles ein.
	 * 
	 * Achtung: Diese Methode geht davon aus, dass beim Bearbeiten des Textes
	 * bereits der textPaneAttributes.isStyledText == false steht! Denn damit
	 * hat der Anwender gar nicht mehr die M&ouml;glichkeit irgendwelche
	 * Formatierungen im Text einzubringen.
	 */
	private String getTextBasedOnStyle(String strText) {
		if (!textPaneAttributes.isStyledText) {
			strText = LpJasperGenerator.replaceHTMLtoText(strText);
		}

		return strText;
	}

	/**
	 * Initialisiert den Editor mit einem neuen Text (inklusive Formatierung).
	 * Es wird erwartet, dass der Text dem aktuellen ContentType entspricht.
	 * 
	 * @see #getText
	 * @param sText
	 *            Der Text, der als neuer Inhalt des Editors gesetzt werden
	 *            soll.
	 */
	public void setText(String sText) {
		if ((sText != null) && (sText.indexOf("<table>") > 0)) {
			bReadingInProgress = true;
			newFile("text/jasper");
			LpEditorReportData data = new LpEditorReportData(sText);
			setReportData(data);
			bReadingInProgress = false;
		} else {
			newFile(null);

			bReadingInProgress = true;

			Document doc = jTextPane.getDocument();
			DocumentFilter filter = null;
			if (doc instanceof AbstractDocument) {
				filter = ((AbstractDocument) doc).getDocumentFilter();
				((AbstractDocument) doc).setDocumentFilter(null);
			}

			jTextPane.setText(sText);

			if (doc instanceof AbstractDocument) {
				((AbstractDocument) doc).setDocumentFilter(filter);
			}

			bReadingInProgress = false;
		}

		bFileIsEdited = false;
		undoManager.discardAllEdits();
		actionEditUndo.updateUndoState();
		actionEditRedo.updateRedoState();
	}

	/**
	 * Setzt eine nicht-editierbare JasperReport Vorlage
	 * 
	 * @see #setJasperReport(JasperReport, boolean)
	 * @param report
	 *            JasperReport
	 * @throws FontNotFoundException
	 */
	/*
	 * public void setJasperReport(JasperReport report) throws
	 * FontNotFoundException { setJasperReport(report, false); }
	 */

	/**
	 * Setzt als Vorlage einen JasperReport und erzeugt ein leeres Dokument mit
	 * "text/jasper" als ContentType. Die Attribute fuer Fliesztext und Spalten
	 * werden dabei neu geschrieben. Folgende Informationen werden von der
	 * Vorlage verwertet: <br>
	 * <br>
	 * pageWidth, pageHeight, leftMargin, rightMargin, topMargin, bottomMargin<br>
	 * <br>
	 * Weiters wird nach Report Elementen gesucht, deren key Attribute
	 * "column{i}" lautet, wobei {i} fuer eine Zahl ab 1 steht. Die Breite
	 * dieser Elemente wird als Spaltenbreite interpretiert, wobei die
	 * Numerierung durchgehend sein muss und die Spaltenordnung repraesentiert.<br>
	 * <br>
	 * Folgende Formatierungen werden unterstuetzt:<br>
	 * <br>
	 * forecolor, backcolor, fontName, size, isUnderline, isItalic, isBold,
	 * isStrikeThrough textAlignment, isStyledText
	 * 
	 * @param report
	 *            JasperReport
	 * @param bEditable
	 *            true bedeutet, man darf die Report-Vorlage aendern (soweit
	 *            moeglich). Z.B. Alignment, DefaultFont, ...
	 * @throws FontNotFoundException
	 * 
	 * @see #getTextBlockAttributes(int)
	 */
	/*
	 * public void setJasperReport(JasperReport report, boolean bEditable)
	 * throws FontNotFoundException { int top = report.getTopMargin(); int
	 * bottom = report.getBottomMargin(); int left = report.getLeftMargin(); int
	 * right = report.getRightMargin();
	 * 
	 * int width = report.getPageWidth(); int height = report.getPageHeight();
	 * 
	 * JRSection detail = report.getDetailSection();
	 * 
	 * Vector<AttributeSet> vecAttributes = new Vector<AttributeSet>();
	 * 
	 * boolean bThrowFontNotFoundException = false; Vector<String>
	 * vecStrFontNamesNotFound = new Vector<String>();
	 * 
	 * newFile("text/jasper"); jTextPane.setPageFormat(new Dimension(width,
	 * height)); jTextPane.setPageMargin(new Insets(top, left, bottom, right));
	 * 
	 * // ======================== Default Style //
	 * ================================ // Benutzt die neueren "style" Jasper
	 * Elemente um den Default-Stil // fuer den Report zu finden Style
	 * defaultStyle = jTextPane.getStyle(StyleContext.DEFAULT_STYLE);
	 * defaultStyle.removeAttributes(defaultStyle);
	 * 
	 * boolean bDefaultFontFound = false; JRStyle[] styles = report.getStyles();
	 * if (styles != null) { for (int i = 0; i < styles.length; i++) { if
	 * (styles[i].isDefault()) { bDefaultFontFound = true;
	 * 
	 * if (!isFontNameAvailable(styles[i].getFontName())) {
	 * bThrowFontNotFoundException = true; myLogger.warn(messages.format(
	 * "FontNotFoundException.DefaultFontName", new Object[] {
	 * styles[i].getFontName(), report.getName() }));
	 * vecStrFontNamesNotFound.add(styles[i].getFontName());
	 * 
	 * defaultStyle.addAttribute(StyleConstants.FontFamily, ((Font)
	 * UIManager.get("TextPane.font")) .getFontName()); } else {
	 * defaultStyle.addAttribute(StyleConstants.FontFamily,
	 * styles[i].getFontName()); }
	 * 
	 * defaultStyle.addAttribute(StyleConstants.FontSize,
	 * styles[i].getFontSize());
	 * defaultStyle.addAttribute(StyleConstants.Italic, styles[i].isItalic());
	 * defaultStyle.addAttribute(StyleConstants.Bold, styles[i].isBold());
	 * defaultStyle.addAttribute(StyleConstants.Underline,
	 * styles[i].isUnderline());
	 * defaultStyle.addAttribute(StyleConstants.StrikeThrough,
	 * styles[i].isStrikeThrough());
	 * 
	 * break; } } }
	 * 
	 * // Fallback zu den veralteten "reportFont" Jasper-Elementen um nach //
	 * den Default-Font zu suchen if (!bDefaultFontFound) { JRReportFont[] fonts
	 * = report.getFonts(); if (fonts != null) { for (int i = 0; i <
	 * fonts.length; i++) { if (fonts[i].isDefault()) {
	 * 
	 * if (!isFontNameAvailable(fonts[i].getFontName())) {
	 * bThrowFontNotFoundException = true; myLogger.warn(messages.format(
	 * "FontNotFoundException.DefaultFontName", new Object[] {
	 * fonts[i].getFontName(), report.getName() }));
	 * vecStrFontNamesNotFound.add(fonts[i].getFontName());
	 * 
	 * defaultStyle.addAttribute( StyleConstants.FontFamily, ((Font)
	 * UIManager.get("TextPane.font")) .getFontName()); } else {
	 * defaultStyle.addAttribute( StyleConstants.FontFamily,
	 * fonts[i].getFontName()); }
	 * 
	 * defaultStyle.addAttribute(StyleConstants.FontSize, new
	 * Integer(fonts[i].getFontSize()));
	 * defaultStyle.addAttribute(StyleConstants.Italic, new
	 * Boolean(fonts[i].isItalic()));
	 * defaultStyle.addAttribute(StyleConstants.Bold, new
	 * Boolean(fonts[i].isBold()));
	 * defaultStyle.addAttribute(StyleConstants.Underline, new
	 * Boolean(fonts[i].isUnderline()));
	 * defaultStyle.addAttribute(StyleConstants.StrikeThrough, new
	 * Boolean(fonts[i].isStrikeThrough())); break; } } } }
	 * 
	 * // ================== Tabellenspalten - Attribute //
	 * ======================= vecCellEditors.clear(); vecCellRenderers.clear();
	 * vecColTextAttributes.clear(); TextBlockAttributes colAttributes = null;
	 * for (int i = 1;; i++) { JRBaseTextField element = (JRBaseTextField)
	 * detail .getElementByKey("column" + i); if (element == null) { break; }
	 * else { SimpleAttributeSet attributes = new SimpleAttributeSet();
	 * attributes.addAttributes(defaultStyle.copyAttributes());
	 * 
	 * // Alignment if (element.getHorizontalAlignmentValue() ==
	 * HorizontalAlignEnum.LEFT ) { StyleConstants.setAlignment(attributes,
	 * StyleConstants.ALIGN_LEFT); } else if
	 * (element.getHorizontalAlignmentValue() == HorizontalAlignEnum.JUSTIFIED)
	 * { StyleConstants.setAlignment(attributes,
	 * StyleConstants.ALIGN_JUSTIFIED); } else if
	 * (element.getHorizontalAlignmentValue() == HorizontalAlignEnum.CENTER) {
	 * StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER); }
	 * else if (element.getHorizontalAlignmentValue() ==
	 * HorizontalAlignEnum.RIGHT) { StyleConstants.setAlignment(attributes,
	 * StyleConstants.ALIGN_RIGHT);
	 * 
	 * // Color } StyleConstants .setForeground(attributes,
	 * element.getForecolor()); StyleConstants .setBackground(attributes,
	 * element.getBackcolor());
	 * 
	 * // Font String strFontName = element.getFontName(); if
	 * (!isFontNameAvailable(strFontName)) { bThrowFontNotFoundException = true;
	 * myLogger.warn(messages.format( "FontNotFoundException.FontName", new
	 * Object[] { strFontName, element.getKey(), report.getName() }));
	 * vecStrFontNamesNotFound.add(strFontName);
	 * 
	 * attributes.addAttribute(StyleConstants.FontFamily, ((Font)
	 * UIManager.get("TextPane.font")) .getFontName()); } else {
	 * attributes.addAttribute(StyleConstants.FontFamily, strFontName); }
	 * 
	 * attributes.addAttribute(StyleConstants.FontSize, new Integer(
	 * element.getFontSize())); attributes.addAttribute(StyleConstants.Bold, new
	 * Boolean( element.isBold()));
	 * attributes.addAttribute(StyleConstants.Italic, new Boolean(
	 * element.isItalic())); attributes.addAttribute(StyleConstants.Underline,
	 * new Boolean( element.isUnderline()));
	 * attributes.addAttribute(StyleConstants.StrikeThrough, new
	 * Boolean(element.isStrikeThrough()));
	 * 
	 * boolean bEqual = false; for (int j = 0; j < vecAttributes.size(); j++) {
	 * if (vecAttributes.get(j) .isEqual(attributes)) {
	 * vecCellEditors.add(vecCellEditors.get(j));
	 * vecCellRenderers.add(vecCellRenderers.get(j)); bEqual = true; break; } }
	 * if (!bEqual) { vecAttributes.add(attributes);
	 * setupNewCellEditor(attributes); vecCellRenderers.add(new
	 * LpEditorCellRenderer(attributes)); }
	 * 
	 * colAttributes = new TextBlockAttributes(attributes); colAttributes.width
	 * = element.getWidth(); colAttributes.isStyledText =
	 * element.isStyledText(); vecColTextAttributes.add(colAttributes); }
	 * 
	 * showAlignmentItems(bEditable); }
	 * 
	 * // Eigenschaften des TextFeld mit key = "textblock" zum // Default -
	 * Style fuer Fliesztexte hinzufuegen JRBaseTextField textblock =
	 * (JRBaseTextField) detail .getElementByKey("textblock"); if (textblock !=
	 * null) {
	 * 
	 * // Alignment if (textblock.getHorizontalAlignmentValue() ==
	 * HorizontalAlignEnum.LEFT) { StyleConstants.setAlignment(defaultStyle,
	 * StyleConstants.ALIGN_LEFT); } else if
	 * (textblock.getHorizontalAlignmentValue() ==
	 * HorizontalAlignEnum.JUSTIFIED) {
	 * StyleConstants.setAlignment(defaultStyle,
	 * StyleConstants.ALIGN_JUSTIFIED); } else if
	 * (textblock.getHorizontalAlignmentValue() == HorizontalAlignEnum.CENTER) {
	 * StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER); }
	 * else if (textblock.getHorizontalAlignmentValue() ==
	 * HorizontalAlignEnum.RIGHT) { StyleConstants.setAlignment(defaultStyle,
	 * StyleConstants.ALIGN_RIGHT);
	 * 
	 * // Color } StyleConstants .setForeground(defaultStyle,
	 * textblock.getForecolor()); StyleConstants .setBackground(defaultStyle,
	 * textblock.getBackcolor());
	 * 
	 * // Font String strFontName = textblock.getFontName(); if
	 * (!isFontNameAvailable(strFontName)) {
	 * 
	 * bThrowFontNotFoundException = true; myLogger.warn(messages.format(
	 * "FontNotFoundException.FontName", new Object[] { strFontName,
	 * textblock.getKey(), report.getName() }));
	 * vecStrFontNamesNotFound.add(strFontName);
	 * 
	 * defaultStyle.addAttribute(StyleConstants.FontFamily, ((Font)
	 * UIManager.get("TextPane.font")).getFontName()); } else {
	 * defaultStyle.addAttribute(StyleConstants.FontFamily, strFontName); }
	 * 
	 * defaultStyle.addAttribute(StyleConstants.FontSize, new Integer(
	 * textblock.getFontSize())); defaultStyle.addAttribute(StyleConstants.Bold,
	 * new Boolean( textblock.isBold()));
	 * defaultStyle.addAttribute(StyleConstants.Italic, new Boolean(
	 * textblock.isItalic()));
	 * defaultStyle.addAttribute(StyleConstants.Underline, new Boolean(
	 * textblock.isUnderline()));
	 * defaultStyle.addAttribute(StyleConstants.StrikeThrough, new
	 * Boolean(textblock.isStrikeThrough()));
	 * 
	 * textPaneAttributes = new TextBlockAttributes(defaultStyle);
	 * textPaneAttributes.isStyledText = textblock.isStyledText(); }
	 * 
	 * jTextPane.setLogicalStyle(defaultStyle); updateUIElements(defaultStyle,
	 * false); undoManager.discardAllEdits(); jasperReportTemplate = report;
	 * 
	 * if (bThrowFontNotFoundException) { throw new FontNotFoundException(
	 * messages.getString("FontNotFoundException.FontNotFound"),
	 * vecStrFontNamesNotFound.toArray(new String[] {})); } }
	 */

	/**
	 * Schaltet die Sichtbarkeit des eingebetteten Menues um.
	 * 
	 * @see #getMenuBar
	 * @param status
	 *            true: Menue sichtbar, false: Menue unsichtbar
	 */
	public void showMenu(boolean status) {
		jMenuBar.setVisible(status);
	}

	/**
	 * Schaltet die Sichtbarkeit der eingebetteten ToolBar um.
	 * 
	 * @param status
	 *            true: ToolBar sichtbar, false: ToolBar unsichtbar.
	 */
	public void showToolBar(boolean status) {
		// jToolBarOther.setVisible(status) ;
		jPanelToolBars.setVisible(status);
	}

	/**
	 * Schaltet die Sichtbarkeit des eingebetteten TabRulers um.
	 * 
	 * @param status
	 *            true: TabRuler sichtbar, false: TabRuler unsichtbar.
	 */
	public void showTabRuler(boolean status) {
		if (status) {
			if (!bTabRulerVisibility) {
				jScrollPane.setColumnHeaderView(rulerBox);
				bTabRulerVisibility = true;
			}
		} else {
			if (bTabRulerVisibility) {
				jScrollPane.setColumnHeaderView(null);
				bTabRulerVisibility = false;
			}
		}
	}

	/**
	 * Schaltet die Sichtbarkeit der eingebetteten StatusBar um.
	 * 
	 * @param status
	 *            true: StatusBar sichtbar, false: StatusBar unsichtbar.
	 */
	public void showStatusBar(boolean status) {
		jPanelStatus.setVisible(status);
	}

	public boolean isMenuVisible() {
		return jMenuBar.isVisible();
	}

	public boolean isToolBarVisible() {
		return jPanelToolBars.isVisible();
	}

	public boolean isTabRulerVisible() {
		return bTabRulerVisibility;
	}

	public boolean isStatusBarVisible() {
		return jPanelStatus.isVisible();
	}

	public boolean isFileItemVisible() {
		return jMenuFile.isVisible();
	}

	public boolean isTableItemVisible() {
		return jToolBarTable.isVisible();
	}

	public boolean isAlignmentItemVisible() {
		return jToolBarAlignment.isVisible();
	}

	public boolean isFontStyleItemVisible() {
		return jToolBarFontStyle.isVisible();
	}

	public boolean isEditable() {
		return jTextPane.isEditable();
	}

	/**
	 * setzt das TextPane auf editierbar/nicht editierbar und schaltet das Popup
	 * - Menue entsprechend ein/aus.
	 * 
	 * <font color="#ff0000">ACHTUNG:</font> Menue, Toolbar und Ruler muessen
	 * extra ausgeschaltet werden!
	 * 
	 * @param enabled
	 *            boolean
	 * 
	 * @see #showMenu
	 * @see #showToolBar
	 * @see #showTabRuler
	 */
	public void setEditable(boolean enabled) {
		jTextPane.setEditable(enabled);

		if (!enabled) {
			jTextPane.removeMouseListener(lpMouseAdapter);
		} else {
			jTextPane.addMouseListener(lpMouseAdapter);
		}
	}

	/**
	 * Filtert aus den im System verfuegbaren Fonts diese heraus, die auf ein
	 * Element von fontNames passen. Verglichen wird mit Font.getFamily(), wird
	 * kein passender Font gefunden so werden die auswaehlbaren Fonts nicht
	 * geaendert und eine Exception geworfen.
	 * 
	 * @param fontNames
	 *            Die Family Namen, nach denen gefiltert werden soll. null
	 *            bedeutet alle verfuegbaren Fonts bereitzustellen.
	 * @param bExactMatch
	 *            Wenn auf true, dann muessen die Filter-Eintraege exakt mit den
	 *            vorhandenen FontFamilies uebereinstimmen. Bei false bleibt
	 *            Grosz/Kleinschreibung unberuecksichtigt und es wird nur mit
	 *            String.startsWith() verglichen.
	 * 
	 * @throws FontNotFoundException
	 *             wenn ueberhaupt keine Uebereinstimmungen gefunden wurden.
	 * 
	 * @return Die FontFamilies, auf die die Kriterien gepasst haben.
	 */
	public String[] setFontFilter(String[] fontNames, boolean bExactMatch)
			throws FontNotFoundException {

		// Alle Fonts bereitstellen
		if (fontNames == null) {
			asSystemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getAvailableFontFamilyNames();
		} else {
			List<String> systemFonts = Arrays.asList(GraphicsEnvironment
					.getLocalGraphicsEnvironment()
					.getAvailableFontFamilyNames());
			// Filtern
			List<String> matchedFonts = new ArrayList<String>();
			for (String font : fontNames) {
				if (bExactMatch && systemFonts.contains(font))
					matchedFonts.add(font);
				else {
					for (String systemFont : systemFonts) {
						if (systemFont.toLowerCase().startsWith(
								font.toLowerCase())) {
							matchedFonts.add(systemFont);
							break;
						}
					}
				}
			}

			if (matchedFonts.size() == 0) {
				throw new FontNotFoundException(
						messages.getString("FontNotFoundException.NoFonts"),
						fontNames);
			} else {
				asSystemFonts = matchedFonts.toArray(new String[0]);
			}
		}

		// ActionListener[] fontNameListeners = jComboBoxFontNames
		// .getActionListeners();
		// ActionListener[] fontSizeListeners = jComboBoxFontSizes
		// .getActionListeners();
		// if (fontNameListeners != null) {
		// for (int i = 0; i < fontNameListeners.length; i++)
		// jComboBoxFontNames.removeActionListener(fontNameListeners[i]);
		// }
		// if (fontSizeListeners != null) {
		// for (int i = 0; i < fontSizeListeners.length; i++)
		// jComboBoxFontSizes.removeActionListener(fontSizeListeners[i]);
		// }
		//
		// jComboBoxFontNames.removeAllItems();
		// for (int i = 0; i < asAvailableFontNames.length; i++) {
		// if (asAvailableFontNames[i].compareTo("") != 0)
		// jComboBoxFontNames.addItem(asAvailableFontNames[i]);
		// }

		// MutableAttributeSet attributes = jTextPane.getInputAttributes();
		// updateUIElements(attributes, false);

		// if (fontNameListeners != null) {
		// for (int i = 0; i < fontNameListeners.length; i++)
		// jComboBoxFontNames.addActionListener(fontNameListeners[i]);
		// }
		// if (fontSizeListeners != null) {
		// for (int i = 0; i < fontSizeListeners.length; i++)
		// jComboBoxFontSizes.addActionListener(fontSizeListeners[i]);
		// }

		// jComboBoxFontNames = new JComboBox(asSystemFonts);

		return asAvailableFontNames;
	}

	/**
	 * Setzt die Seitenbreite des editierbaren Bereichs.
	 * 
	 * @see #getTextPane
	 * @param width
	 *            Die Breite in Pixel.
	 */
	public void setPageWidth(int width) {
		jTextPane.setPageWidth(width);
	}

	/**
	 * Liefert die Seitenbreite des editierbaren Bereichs.
	 * 
	 * @see #getTextPane
	 * @return die Seitenbreite des editierbaren Bereichs
	 */
	public int getPageWidth() {
		return jTextPane.getPageFormat().width;
	}

	/**
	 * Schaltet die Sichtbarkeit der Datei - Menuepunkte und dazugehoeriger
	 * Toolbar - Buttons um.
	 * 
	 * @param status
	 *            true: Items sichtbar, false: Items unsichtbar
	 */
	public void showFileItems(boolean status) {
		jToolBarFile.setVisible(status);
		jMenuFile.setVisible(status);
		jLabelFileName.setVisible(status);
	}

	/**
	 * Schaltet die Sichtbarkeit der Menue- und Toolbar-Eintraege fuer Tabellen
	 * um.
	 * 
	 * @param status
	 *            boolean
	 */
	public void showTableItems(boolean status) {
		jToolBarTable.setVisible(status);
		jMenuTable.setVisible(status);
		jMenuTablePopup.setVisible(status);
	}

	/**
	 * Schaltet die Sichtbarkeit der Menue- und Toolbar-Eintraege fuer das
	 * Alignment um.
	 * 
	 * @param status
	 *            boolean
	 */
	public void showAlignmentItems(boolean status) {
		jToolBarAlignment.setVisible(status);
		jMenuFormatAlign.setVisible(status);
		// jMenuFormatAlignPopup.setVisible(status);
	}

	/**
	 * Schaltet die Sichtbarkeit der Menue- und Toolbar-Eintraege fuer Bold,
	 * Italic, Underline, Strikethrough, Forecolor und Backcolor um.
	 * 
	 * @param status
	 *            boolean
	 */
	public void showFontStyleItems(boolean status) {
		jToolBarFontStyle.setVisible(status);
		jMenuFormatColor.setVisible(status);
		// jMenuFormatColorPopup.setVisible(status);
		jMenuFormatStyle.setVisible(status);
		// jMenuFormatStylePopup.setVisible(status);
		jMenuFormatFont.setVisible(status);
		// jMenuFormatFontPopup.setVisible(status);
	}

	/**
	 * Initialisiert den Editor mit einem leeren Dokument. Erstellt ein neues
	 * Dokument und verwirft das alte. Der Filename gilt daraufhin als unbekannt
	 * und das Dokument als nicht editiert.
	 * 
	 * @param type
	 *            Der content-type fuer die neue Datei. null bedeutet keine
	 *            Aenderung des Typs.
	 * 
	 * @see #openFile
	 * @see #openFile(String)
	 * @see #saveFile(File)
	 * @see #saveFile(String)
	 */
	public void newFile(String type) {
		if (type != null) {
			jTextPane.setContentType(type);
		}

		vecTables.clear();
		vecTableModels.clear();
		vecMarkersBefore.clear();
		vecMarkersAfter.clear();

		Document document = jTextPane.getEditorKit().createDefaultDocument();
		document.addDocumentListener(lpDocumentListener);

		if (document instanceof AbstractDocument) {
			((AbstractDocument) document).setDocumentFilter(lpDocumentFilter);
		}

		if (document instanceof StyledDocument) {
			vecTableModels.clear();
			document.putProperty("i18n", Boolean.TRUE);
			document.putProperty(LpEditor.TABLE_MODELS_PROP, vecTableModels);
			document.putProperty(LpEditor.COLUMN_TEXT_ATTRIBUTES,
					vecColTextAttributes);
		}

		if (textPaneAttributes == null) {
			textPaneAttributes = new TextBlockAttributes();
		}
		currentTextBlockAttributes = textPaneAttributes;

		jTextPane.setDocument(document);
		undoManager = new LpCompoundUndoManager(jTextPane);

		jTextPane.updateUI();
		actualFile = null;
		bFileIsEdited = false;
		updateBufferStatus(textPaneAttributes, 0, true);
		updateFileName();
	}

	/**
	 * Einlesen eines Textes aus einer Datei. Initialisiert den Editor mit einem
	 * neuen Dokument und liest anschliessend den Fileinhalt ein. Als Filename
	 * gilt anschliessend der Name des Files und das Dokument als nicht
	 * editiert.<br>
	 * Fuer JasperReports noch nicht vollstaendig implementiert.
	 * 
	 * @see #newFile
	 * @see #openFile(String)
	 * @see #saveFile(File)
	 * @see #saveFile(String)
	 * 
	 * @param file
	 *            File, das eingelesen werden soll.
	 * @throws FileNotFoundException
	 *             das angegebene File konnte nicht gefunden werden.
	 * 
	 */
	public void openFile(File file) throws FileNotFoundException {
		LpFormatParser parser = null;
		FileInputStream stream = new FileInputStream(file);

		bReadingInProgress = true;
		if (file.getName().toLowerCase().endsWith(".jrxml")
				|| file.getName().toLowerCase().endsWith(".jasper")) {
			newFile("text/jasper");
			parser = new LpJasperParser();
		} else {
			newFile("text/plain");
		}

		try {
			if (parser != null) {
				parser.parseFromStream(stream, jTextPane.getStyledDocument(),
						jTextPane.getCaretPosition());
				jTextPane.setPageFormat(parser.getPageSize());
				jTextPane.setPageMargin(parser.getPageMargin());
				jTextPane.updateUI();
			} else {
				jTextPane.setPageFormat(LpDecoratedTextPane.FORMAT_A4,
						LpDecoratedTextPane.UNIT_MM);
				jTextPane.setPageMargin(new Insets(5, 5, 5, 5));
				jTextPane.read(stream, null);
			}
			actualFile = file;
			undoManager.discardAllEdits();
			actionEditUndo.updateUndoState();
			actionEditRedo.updateRedoState();

			updateFileName();
			// updateBufferStatus();
			bFileIsEdited = false;
			bFirstDocChange = true;

		} catch (IOException e) {
			myLogger.error(e.getLocalizedMessage(), e);
		}

		bReadingInProgress = false;
	}

	/**
	 * Oeffnen einer Datei, die ueber ihren Filnamen charakterisiert ist.
	 * Initialisiert den Editor mit einem neuen Dokument und liest anschliessend
	 * den Fileinhalt ein. Als Filename gilt anschliessend der Name des Files
	 * und das Dokument als nicht editiert.<br>
	 * Fuer JasperReports noch nicht vollstaendig implementiert.
	 * 
	 * @see #newFile
	 * @see #openFile(File)
	 * @see #saveFile(File)
	 * @see #saveFile(String)
	 * 
	 * @param sFileName
	 *            Der Name des Files, das eingelesen werden soll.
	 * @throws FileNotFoundException
	 *             das angegebene File konnte nicht gefunden werden.
	 * 
	 */
	public void openFile(String sFileName) throws FileNotFoundException {
		openFile(new File(sFileName));
	}

	/**
	 * Speichern des aktuellen Dokuments in eine Datei. Als Filename gilt
	 * anschliessend der Name des Files und das Dokument als nicht editiert.<br>
	 * Neuimplementation notwendig.
	 * 
	 * @see #newFile
	 * @see #openFile(String)
	 * @see #openFile(File)
	 * @see #saveFile(String)
	 * 
	 * @param file
	 *            File, in das der Text gespeichert werden soll.
	 * @throws FileNotFoundException
	 *             das angegebene File konnte nicht gefunden werden.
	 * 
	 */
	public void saveFile(File file) throws FileNotFoundException {
		// FileOutputStream stream =
		// new FileOutputStream(file);
		/*
		 * try { generator.setPageMargin(jTextPane.getPageMargin());
		 * generator.setPageSize(jTextPane.getPageFormat());
		 * generator.writeFormat(getStyledDocument(), stream); actualFile =
		 * file; updateFileName(); bFileIsEdited = false;
		 * log.info("Saved File: " + file.getAbsoluteFile()); } catch
		 * (IOException ex) { log.error(ex.getMessage()); }
		 */
	}

	/**
	 * Speichern des aktuellen Dokuments in eine Datei, die ueber ihren
	 * Dateinamen charakterisiert ist. Als Filename gilt anschliessend der Name
	 * des Files und das Dokument als nicht editiert.
	 * 
	 * @see #newFile
	 * @see #openFile(String)
	 * @see #openFile(File)
	 * @see #saveFile(File)
	 * 
	 * @param sFileName
	 *            Name des Files, in das der Text gespeichert werden soll.
	 * @throws FileNotFoundException
	 *             das angegebene File konnte nicht gefunden werden.
	 * 
	 */
	public void saveFile(String sFileName) throws FileNotFoundException {
		saveFile(new File(sFileName));
	}

	/**
	 * Von dem Frame aufzurufen, der das LpEditor Panel beinhaelt, wenn dieser
	 * beendet wird. Fuehrt dann eine Benutzerabfrage durch, ob das Dokument
	 * gespeichert werden soll.
	 * 
	 * @return false wenn der Benutzer kein Schliessen wuenscht oder das
	 *         Speichern des Dokuments fehlgeschlagen ist. true sonst.
	 */
	public boolean prepareExit() {
		return checkContentDiscardAllowed(messages.getString("Dialog.Quit"));
	}

	private long getCurrentTextBlockLength(int pos) {
		if (currentCellEditor != null && currentCellEditor.isEditing()) {
			return currentCellEditor.getEditor().getText().length();
		}

		if (vecTables.size() == 0) {
			currentTextBlockStart = jTextPane.getDocument().getStartPosition();
			currentTextBlockEnd = jTextPane.getDocument().getEndPosition();
			return jTextPane.getText().length();
		}

		int caretPos = jTextPane.getCaretPosition();
		if (pos > -1) {
			caretPos = pos;

		}
		int start = 0;
		int end = vecMarkersBefore.get(0).getOffset() - 1;
		for (int i = 0; i < vecMarkersBefore.size(); i++) {
			try {
				if (caretPos >= start && caretPos <= end) {
					if (start == 0) {
						currentTextBlockStart = jTextPane.getDocument()
								.getStartPosition();
					} else {
						currentTextBlockStart = jTextPane.getDocument()
								.createPosition(start);
					}
					currentTextBlockEnd = jTextPane.getDocument()
							.createPosition(end + 1);
					return jTextPane.getText(start, end - start).length();
				} else {
					start = vecMarkersAfter.get(i).getOffset() + 1;
					if (i + 1 >= vecMarkersBefore.size()) {
						end = jTextPane.getDocument().getLength();
						currentTextBlockStart = jTextPane.getDocument()
								.createPosition(start);
						currentTextBlockEnd = jTextPane.getDocument()
								.getEndPosition();
						return jTextPane.getText(start, end - start).length();
					} else {
						end = vecMarkersBefore.get(i + 1).getOffset() - 1;
					}
				}
			} catch (BadLocationException ex) {
				myLogger.error(ex.getLocalizedMessage(), ex);
			}
		}
		return 0;
	}

	void updateBufferStatus(TextBlockAttributes textAttr, int pos) {
		updateBufferStatus(textAttr, pos, false);
	}

	void updateUIElements(AttributeSet attr, boolean selected) {
		// falls selektiert wird, combobox nicht aendern, da dies die attribute
		// des selektierten textes aendern wuerde
		if (!selected) {
			jComboBoxFontNames.setSelectedItem(StyleConstants
					.getFontFamily(attr));
			jComboBoxFontSizes.setSelectedItem(Integer.toString(StyleConstants
					.getFontSize(attr)));
		}

		lpButtonFormatStyleBold.setSelected(StyleConstants.isBold(attr));
		lpButtonFormatStyleItalic.setSelected(StyleConstants.isItalic(attr));
		lpButtonFormatStyleUnderline.setSelected(StyleConstants
				.isUnderline(attr));
		lpButtonFormatStyleStrikethrough.setSelected(StyleConstants
				.isStrikeThrough(attr));

		switch (StyleConstants.getAlignment(attr)) {
		case StyleConstants.ALIGN_LEFT:
			lpButtonFormatAlignLeft.setSelected(true);
			break;
		case StyleConstants.ALIGN_RIGHT:
			lpButtonFormatAlignRight.setSelected(true);
			break;
		case StyleConstants.ALIGN_CENTER:
			lpButtonFormatAlignCenter.setSelected(true);
			break;
		case StyleConstants.ALIGN_JUSTIFIED:
			lpButtonFormatAlignJustified.setSelected(true);
			break;
		}
	}

	protected boolean isFontNameAvailable(String strFontName) {
		for (int j = 0; j < jComboBoxFontNames.getItemCount(); j++) {
			if (jComboBoxFontNames.getItemAt(j).equals(strFontName)) {
				return true;
			}
		}

		return false;
	}

	void updateBufferStatus(TextBlockAttributes attr, int pos, boolean bAll) {
		if (!bUpdateBuffer) {
			return;
		}

		TextBlockAttributes textAttr = attr;
		if (textAttr == null) {
			textAttr = currentTextBlockAttributes;
		}
		if (textAttr == null) {
			return;
		}

		if (!bReadingInProgress) {
			if (currentTextBlockLength == -1 || bAll) {
				currentTextBlockLength = getCurrentTextBlockLength(pos);
				if (currentCellEditor == null || !currentCellEditor.isEditing()) {
					jTextPane.removeOverflowRange(currentTextBlockStart,
							currentTextBlockEnd);
				}
			} else if (currentTextBlockLength == -2) {
				currentTextBlockLength = -1;
				return;
			}

			String strBufferText = "";
			String strBufferStat = " (" + currentTextBlockLength + "/";
			int val = 0;
			if (textAttr.capacity > 0) {
				val = (int) (100.0 * currentTextBlockLength / textAttr.capacity);
				strBufferStat += textAttr.capacity + ") ";
			} else {
				strBufferStat += messages.getString("Status.Unlimited") + ") ";

			}
			jProgressBarBuffer.setValue(val);

			if (currentCellEditor != null && currentCellEditor.isEditing()) {
				currentCellEditor.setOverflow(false);
			}

			if (val == 100) {
				strBufferText = messages.getString("Status.BufferFull");
				jLabelBufferText.setForeground(jLabelBufferText.getParent()
						.getForeground());
			} else if (val > 100) {
				strBufferText = messages.getString("Status.BufferOverflow");
				jLabelBufferText.setForeground(Color.RED);
				if (currentCellEditor != null && currentCellEditor.isEditing()) {
					currentCellEditor.setOverflow(true);
				} else if (currentTable == null
						|| (currentTable != null && !currentTable
								.isPreparingEditor())) {
					jTextPane.addOverflowRange(currentTextBlockStart,
							currentTextBlockEnd);
				}
			} else {
				strBufferText = messages.getString("Status.BufferInfo");
				jLabelBufferText.setForeground(jLabelBufferText.getParent()
						.getForeground());
			}

			// stellt sicher, dass die Markierungen fuer Kapazitaets
			// ueberschreitungen neu gezeichnet werden
			jTextPane.repaint();
			jLabelBufferText.setText(strBufferText + strBufferStat);
		}
	}

	/**
	 * Wird z.B. aufgerufen, wenn begonnen wird eine Zelle zu editieren. Dann
	 * werden die entsprechenden Actions aktiviert und der aktuelle CellEditor
	 * gesetzt.
	 * 
	 * @param event
	 *            PropertyChangeEvent
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(LpEditorCellEditor.IS_EDIT_EVENT)) {
			if (((Boolean) event.getNewValue()).booleanValue()) {
				currentCellEditor = (LpEditorCellEditor) ((JComponent) event
						.getSource())
						.getClientProperty(LpEditorCellEditor.EDITOR_PARENT_PROP);

				enableTableItems(currentCellEditor.getTable());
				actionEditUndo.setEnabled(false);
				actionEditRedo.setEnabled(false);
				int iCol = currentCellEditor.getEditingColumn();
				if (iCol >= 0 && iCol < vecColTextAttributes.size()) {
					currentTextBlockAttributes = vecColTextAttributes.get(iCol);
					enableFontStyleItems(currentTextBlockAttributes.isStyledText);
					currentTextBlockLength = -1;
					updateBufferStatus(currentTextBlockAttributes, -1);
				} else {
					enableFontStyleItems(false);

				}
			} else {
				currentCellEditor = null;
			}
		}
	}

	/**
	 * Setzt den Filenamen in der StatusBar, entsprechend dem aktuellen Zustand.
	 */
	private void updateFileName() {
		if (actualFile == null) {
			jLabelFileName.setText(" -- ");
		} else {
			jLabelFileName.setText(actualFile.getAbsolutePath());
		}
	}

	private void setupNewCellEditor(AttributeSet attributes) {
		LpEditorCellEditor cellEditor = new LpEditorCellEditor(attributes);
		LpCaretListener cellCaretListener = new LpCaretListener();
		cellEditor.addCaretListener(cellCaretListener);

		// fuer actions enable/disable und popup menu
		cellEditor.getEditor().addMouseListener(lpMouseAdapter);

		cellEditor.getEditor().addPropertyChangeListener(
				LpEditorCellEditor.IS_EDIT_EVENT, this);
		cellEditor.getEditor().setKeymap(customKeymap);

		Document document = cellEditor.getEditor().getDocument();
		if (document instanceof AbstractDocument) {
			((AbstractDocument) document).setDocumentFilter(lpDocumentFilter);
		}
		document.addDocumentListener(lpDocumentListener);

		vecCellEditors.add(cellEditor);
	}

	/**
	 * Leert den Arbeitsberich und initialisiert ein neues Dokument.
	 */
	protected void openBlank() {
		if (checkContentDiscardAllowed(messages.getString("Dialog.NewDocument"))) {
			newFile(null);
		}
	}

	/**
	 * Oeffnet ein File, nach Auswahl des Filenamens durch den Benutzer
	 * 
	 * @return true, wenn das oeffnen erfolgreich war, false, wenn nicht.
	 * 
	 */
	protected boolean open() {
		boolean bSuccess = false;
		File file;

		if (checkContentDiscardAllowed(messages
				.getString("Dialog.OpenDocument"))) {

			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.addChoosableFileFilter(new LpFileFilter(
					LpFileFilter.FILE_EXTENSION_JRXML, "Jasper XML"));
			jFileChooser.addChoosableFileFilter(new LpFileFilter(
					LpFileFilter.FILE_EXTENSION_JASPER, "Jasper Report"));

			if (jFileChooser.showOpenDialog(LpEditor.this) == JFileChooser.APPROVE_OPTION) {
				file = jFileChooser.getSelectedFile();
				try {
					openFile(file);
					bSuccess = true;
				} catch (FileNotFoundException ex) {
					myLogger.info(ex.getMessage());
					JOptionPane.showMessageDialog(ownerFrame,
							messages.getString("Dialog.ErrorOpenFile"),
							messages.getString("Dialog.Error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		return bSuccess;
	}

	/**
	 * Sichert das Aktuelle file, ggf. nach Frage nach einem Filenamen.
	 * 
	 * @return true, wenn das sichern erfolgreich war, false, wenn nicht.
	 */
	protected boolean save() {
		boolean bSuccess = false;

		if (actualFile == null) {
			bSuccess = saveAs();
		} else {
			try {
				saveFile(actualFile);
				bSuccess = true;
			} catch (FileNotFoundException ex) {
				myLogger.info(ex.getMessage());
				JOptionPane.showMessageDialog(ownerFrame,
						messages.getString("Dialog.ErrorSaveFile"),
						messages.getString("Dialog.Error"),
						JOptionPane.ERROR_MESSAGE);
			}
		}

		return bSuccess;
	}

	/**
	 * Sichert das aktuelle file nach Frage nach einem Filnamen.
	 * 
	 * @return true, wenn das sichern erfolgreich war, false, wenn nicht.
	 */
	protected boolean saveAs() {
		File file;
		boolean bSuccess = false;

		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.addChoosableFileFilter(new LpFileFilter(
				LpFileFilter.FILE_EXTENSION_JRXML, "Jasper XML"));
		jFileChooser.addChoosableFileFilter(new LpFileFilter(
				LpFileFilter.FILE_EXTENSION_JASPER, "Jasper Report"));

		if (jFileChooser.showSaveDialog(LpEditor.this) == JFileChooser.APPROVE_OPTION) {
			file = jFileChooser.getSelectedFile();
			try {
				saveFile(file);
				bSuccess = true;
			} catch (FileNotFoundException ex) {
				myLogger.info(ex.getMessage());
				JOptionPane.showMessageDialog(ownerFrame,
						messages.getString("Dialog.ErrorSaveFile"),
						messages.getString("Dialog.Error"),
						JOptionPane.ERROR_MESSAGE);
			}
		}

		return bSuccess;
	}

	protected void enableFontStyleItems(boolean flag) {
		actionFormatColorBackground.setEnabled(flag);
		actionFormatColorForeground.setEnabled(flag);
		actionFormatFont.setEnabled(flag);
		actionFormatStyleBold.setEnabled(flag);
		actionFormatStyleItalic.setEnabled(flag);
		actionFormatStyleUnderline.setEnabled(flag);
		actionFormatStyleStrikethrough.setEnabled(flag);

		enableTextAlignmentItems(flag);
	}

	protected void enableTableItems(LpEditorTable table) {
		if (currentTable == null) {
			actionInsertTable.setEnabled(false);
			actionDeleteTable.setEnabled(true);
			actionDeleteRow.setEnabled(true);
			actionInsertRowAfter.setEnabled(true);
			actionInsertRowBefore.setEnabled(true);

			enableTextAlignmentItems(false);
		} else if (currentTable != table) {
			currentTable.removeRowSelectionInterval(0,
					currentTable.getRowCount() - 1);
		}

		currentTable = table;
	}

	protected void enableTextAlignmentItems(boolean newValue) {
		actionFormatAlignCenter.setEnabled(newValue);
		actionFormatAlignJustified.setEnabled(newValue);
		actionFormatAlignLeft.setEnabled(newValue);
		actionFormatAlignRight.setEnabled(newValue);
	}

	protected void disableTableItems() {
		actionInsertTable.setEnabled(true);
		actionDeleteTable.setEnabled(false);
		actionDeleteRow.setEnabled(false);
		actionInsertRowAfter.setEnabled(false);
		actionInsertRowBefore.setEnabled(false);

		enableTextAlignmentItems(true);

		if (currentTable != null) {
			currentTable.clearSelection();
		}

		currentTable = null;
	}

	/**
	 * Prueft, ob das gerade geoeffnete File nach der letzten Aenderung
	 * gesichert wurde, oder nicht und fuehrt ggf. eine Entsprechende
	 * BenutzerAbfrage und Aktionen durch.
	 * 
	 * @param sDialogTitle
	 *            Der Titel des Dialogs (sollte im wesentlichen beschreiben,
	 *            welche Aktion gerade durchgefuehrt werden sollte, bevor der
	 *            Dialog aufgerufen wurde.)
	 * @return true, wenn das File gesichert wurde, bzw vom Benutzer bestaetigt
	 *         wurde, dass es geloescht werden darf. false, andernfalls.
	 * 
	 */
	private boolean checkContentDiscardAllowed(String sDialogTitle) {
		boolean bAllowed = false;

		if (!bFileIsEdited) { // File nicht veraendert -> kein Problem
			bAllowed = true;
		} else { // Beim Benutzer nachfragen.
			switch (JOptionPane.showConfirmDialog(ownerFrame,
					messages.getString("Dialog.DocumentNotSaved"),
					sDialogTitle, JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE)) {
			case JOptionPane.CLOSED_OPTION:
			case JOptionPane.CANCEL_OPTION:
				bAllowed = false;
				break;
			case JOptionPane.NO_OPTION:
				bAllowed = true;
				break;
			case JOptionPane.YES_OPTION:
				bAllowed = save();
				break;
			}
		}

		return bAllowed;
	}

	/**
	 * Implementierung von DocumenListener, angepasst fuer den LpEditor.
	 * Bestimmt, ob das aktuelle Dokument editiert wurde und ruft die
	 * aktualisierung des Buffer - Zustandes auf.
	 * 
	 * @see LpEditor#updateBufferStatus(TextBlockAttributes, int)
	 * 
	 */
	protected class LpDocumentListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			// System.out.println("in insertUpdate");
			if (!bReadingInProgress) {
				bFileIsEdited = true;

				updateBufferStatus(currentTextBlockAttributes, -1);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// System.out.println("in removeUpdate");
			if (!bReadingInProgress) {
				bFileIsEdited = true;
				updateBufferStatus(currentTextBlockAttributes, -1);
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// System.out.println("in changedUpdate");
			if (!bReadingInProgress) {
				/*
				 * Wenn openFile() fertig ist, wird leider diese Funktion
				 * aufgerufen, die bFileIsEdited = true setzt und damit das neu
				 * geoeffnete Dokument faelschlicherweise auf geaendert setzt.
				 */
				if (!bFirstDocChange) {
					bFileIsEdited = true;
				} else {
					bFirstDocChange = false;
				}
				currentTextBlockLength = -1;
				updateBufferStatus(currentTextBlockAttributes, -1);
			}
		}

	}

	/**
	 * Setzt den Status der Buttons fuer Italic, Bold, ... abhaengig von der
	 * Cursor Position.
	 */
	protected class LpCaretListener implements CaretListener {

		@Override
		public void caretUpdate(CaretEvent ce) {

			AttributeSet attr = ((JTextPane) ce.getSource())
					.getInputAttributes();

			boolean selected = ce.getDot() != ce.getMark();
			updateUIElements(attr, selected);
		}
	}

	/**
	 * <p>
	 * <I>Setzt den Cursor richtig vor und nach den Tabellen</I>
	 * </p>
	 * <p>
	 * Copyright Logistik Pur Software GmbH (c) 2004-2008
	 * </p>
	 * <p>
	 * Erstellungsdatum <I>November 2004</I>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @author Sascha Zelzer
	 * @version $Revision: 1.7 $
	 */
	protected class LpNavigationFilter extends NavigationFilter {
		@Override
		public int getNextVisualPositionFrom(JTextComponent text, int pos,
				Position.Bias bias, int direction, Position.Bias[] biasRet)
				throws BadLocationException {

			int nextPos = super.getNextVisualPositionFrom(text, pos, bias,
					direction, biasRet);

			for (int i = 0; i < vecMarkersBefore.size(); i++) {
				if (nextPos == vecMarkersBefore.get(i).getOffset()) {
					nextPos = vecMarkersAfter.get(i).getOffset() + 1;
					currentTextBlockLength = -1;
					updateBufferStatus(textPaneAttributes, nextPos);
					break;
				} else if (nextPos == vecMarkersAfter.get(i).getOffset()) {
					nextPos = vecMarkersBefore.get(i).getOffset() - 1;
					currentTextBlockLength = -1;
					updateBufferStatus(textPaneAttributes, nextPos);
					break;
				}
			}

			return nextPos;
		}
	}

	/**
	 * <p>
	 * <I>Passt das Einfuegen/Entfernen von Fliesztext am Rand der Tabellen an
	 * und ueberprueft die Kapazitaet des Fliesztextblocks</I>
	 * </p>
	 * <p>
	 * Copyright Logistik Pur Software GmbH (c) 2004-2008
	 * </p>
	 * <p>
	 * Erstellungsdatum <I>November 2004</I>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @author Sascha Zelzer
	 * @version $Revision: 1.7 $
	 */
	protected class LpDocumentFilter extends DocumentFilter {
		@Override
		public void insertString(DocumentFilter.FilterBypass fb, int offset,
				String text, AttributeSet attr) throws BadLocationException {

			boolean bInserted = false;
			// System.out.println("in filter insert");

			if (currentTextBlockAttributes.capacity > 0
					&& currentTextBlockLength >= currentTextBlockAttributes.capacity
					&& !bForceInsert) {
				return;
			}

			if (attr != null
					&& StyleConstants.getComponent(attr) instanceof LpEditorTable) {
				super.insertString(fb, offset, text, attr);
				setupTableData(
						(LpEditorTable) StyleConstants.getComponent(attr),
						offset);
				return;
			}

			AttributeSet defaultAttrib = null;

			if (currentCellEditor == null || !currentCellEditor.isEditing()) {
				// Direkt vor oder nach Tabellen nur neue Zeilen erlauben
				// und diese richtig setzten, damit sich die Marker nicht
				// verschieben.
				for (int i = 0; i < vecMarkersBefore.size(); i++) {
					if (offset == vecMarkersBefore.get(i).getOffset()) {
						if (text.equals("\n")) {
							super.insertString(fb, offset, text, attr);
							bInserted = true;
							break;
						} else {
							return;
						}
					} else if (offset == vecMarkersAfter.get(i).getOffset()) {
						if (text.equals("\n")) {
							super.insertString(fb, offset + 1, text, attr);
							jTextPane.setCaretPosition(offset + 1);
							bInserted = true;
							break;
						} else {
							return;
						}
					}
				}

				defaultAttrib = textPaneAttributes.getStyleAttributes();
			} else {
				defaultAttrib = vecColTextAttributes.get(
						currentTable.getEditingColumn()).getStyleAttributes();
			}

			if (attr != null && defaultAttrib.containsAttributes(attr)) {
				currentTextBlockLength += text.length();
			} else {
				currentTextBlockLength = -1;

			}
			if (!bInserted) {
				super.insertString(fb, offset, text, attr);
			}
		}

		@Override
		public void replace(DocumentFilter.FilterBypass fb, int offset,
				int length, String text, AttributeSet attr)
				throws BadLocationException {
			// System.out.println("in filter replace");

			if (length == 0
					&& currentTextBlockAttributes.capacity > 0
					&& currentTextBlockLength >= currentTextBlockAttributes.capacity
					&& !bForceInsert) {
				return;
			}

			// SP 922 Tabs unterbinden
			//text = text.replaceAll("\t", " ");

			boolean bReplaced = false;

			if (attr != null
					&& StyleConstants.getComponent(attr) instanceof LpEditorTable) {
				super.insertString(fb, offset, text, attr);
				setupTableData(
						(LpEditorTable) StyleConstants.getComponent(attr),
						offset);
				return;
			}

			if (currentCellEditor == null || !currentCellEditor.isEditing()) {
				// Direkt vor oder nach Tabellen nur neue Zeilen erlauben
				// und diese richtig setzen, damit sich die Marker nicht
				// verschieben.
				for (int i = 0; i < vecMarkersBefore.size(); i++) {
					if (offset == vecMarkersBefore.get(i).getOffset()) {
						if (text.equals("\n")) {
							super.replace(fb, offset, length, text, attr);
							bReplaced = true;
							break;
						} else {
							return;
						}
					} else if (offset == vecMarkersAfter.get(i).getOffset()) {
						if (text.equals("\n")) {
							super.replace(fb, offset + 1, length, text, attr);
							jTextPane.setCaretPosition(offset + 1);
							bReplaced = true;
							break;
						}
						return;
					}
				}
			}

			String pasteText = null;
			if (true) { // (length == 0) {
				/**
				 * @todo currentTextBlockLength = 0 nach &ouml;ffnen des Editors
				 *       -> l&auml;ngenberechnung falsch -> hier neu laden PJ
				 *       4778
				 */
				// pos bei getCurrentTextBlockLength??
				if (currentTextBlockLength == 0)
					currentTextBlockLength = getCurrentTextBlockLength(0);
				currentTextBlockLength -= length;
				if ((currentTextBlockAttributes.capacity > 0)
						&& (currentTextBlockAttributes.capacity
								- currentTextBlockLength - text.length() < 0)) {
					if (text.length() > currentTextBlockAttributes.capacity) {
						pasteText = getStyledText(text.substring(0,
								(int) currentTextBlockAttributes.capacity));
					} else {
						pasteText = text;
					}
					pasteText = pasteText
							.substring(
									0,
									(int) (currentTextBlockAttributes.capacity - currentTextBlockLength));
					pasteText = parseEscapeCharacter(pasteText);
					currentTextBlockLength += pasteText.length();
				} else {
					pasteText = text;
					currentTextBlockLength += text.length();
				}
				// updateBuffer() wird 2x aufgerufen, von removeUpdate() und
				// insertUpdate(). Mit length = -2 wird daher beim ersten
				// Aufruf von updateBuffer() die Laenge nicht neu berechnet
			}
			// else {
			// currentTextBlockLength = -2;
			// // AD Replace bei selektiertem Block
			// pasteText = text;
			// }
			if (!bReplaced) {
				super.replace(fb, offset, length, pasteText, attr);
			}

		}

		@Override
		public void remove(DocumentFilter.FilterBypass fb, int offset,
				int length) throws BadLocationException {
			// System.out.println("in filter remove " + offset + " - " +
			// (offset+length));

			currentTextBlockLength = -1;

			// Komplettes entfernen der Tabelle (inklusive \n )
			for (int i = 0; i < vecMarkersBefore.size(); i++) {
				// Entfernen mittels del for \n
				if (offset == vecMarkersBefore.get(i).getOffset() - 1
						&& length < 2) {
					// System.out.println("filter removes " + offset + " - " +
					// (offset+2));
					super.remove(fb, offset, 2);
					cleanTableData(i);
					return;
				}
				// Entfernen mittels backspace genau nach dem \n
				else if (offset == vecMarkersAfter.get(i).getOffset()
						&& length == 1) {
					currentTextBlockLength = -1;
					// System.out.println("filter removes " + (offset-1) + " - "
					// + (offset+1));
					super.remove(fb, offset - 1, 2);
					cleanTableData(i);
					return;
				}
				// Zu entfernender Bereich beinhaelt ein Tabelle
				else if (offset <= vecMarkersBefore.get(i).getOffset()
						&& offset + length >= vecMarkersAfter.get(i)
								.getOffset()) {
					// System.out.println("filter removes " + offset + " - " +
					// (offset+length));
					super.remove(fb, offset, length);
					cleanTableData(i);
					return;
				}
			}

			super.remove(fb, offset, length);
		}
	}

	/**
	 * Editor-Attribute / Stile &uuml;bernehmen
	 * 
	 * Fontnamen, Fontgr&ouml;&szlig;en, Fontattribute ...
	 * 
	 * @param otherEditor
	 */
	public void setupAttributesFrom(LpEditor otherEditor) {
		if (null == otherEditor)
			throw new IllegalArgumentException("otherEditor");

		showToolBar(otherEditor.isToolBarVisible());
		showMenu(otherEditor.isMenuVisible());
		showTabRuler(otherEditor.isTableItemVisible());
		showFileItems(otherEditor.jToolBarFile.isVisible());

		beStyledText(otherEditor.getStyledText());
	}

	/**
	 * 
	 * <p>
	 * <I>Implementierung der UndoableEditListeners und Erweiterung von
	 * UndoManger, angepasst fuer den LpEditor. Speichert die Editier - Events
	 * fuer ein eventuelles undo/redo und setzt den Status der dazugehoerigen
	 * actions.</I>
	 * </p>
	 * <p>
	 * Copyright Logistik Pur Software GmbH (c) 2004-2008
	 * </p>
	 * <p>
	 * Erstellungsdatum <I>November 2004</I>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @author Sascha Zelzer
	 * @version $Revision: 1.7 $
	 */
	protected class LpCompoundUndoManager extends UndoManager implements
			UndoableEditListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private CompoundEdit compoundEdit = null;

		public LpCompoundUndoManager(JTextComponent textComp) {
			textComp.getDocument().addUndoableEditListener(this);
		}

		@Override
		public void undoableEditHappened(UndoableEditEvent event) {
			if (compoundEdit != null) {
				compoundEdit.addEdit(event.getEdit());
			} else {
				addEdit(event.getEdit());
			}
		}

		@Override
		public boolean addEdit(UndoableEdit edit) {
			boolean ret = super.addEdit(edit);
			actionEditUndo.updateUndoState();
			actionEditRedo.updateRedoState();
			return ret;
		}

		public void startCompoundTableEdit(boolean insert, LpEditorTable table,
				int index, int offset) {
			if (compoundEdit == null) {
				if (insert)
					compoundEdit = new CompoundTableInsertEdit(table, index,
							offset);
				else
					compoundEdit = new CompoundTableDeleteEdit(table, index,
							offset);
			}
		}

		public void endCompoundTableEdit() {
			compoundEdit.end();
			addEdit(compoundEdit);
			compoundEdit = null;
		}
	}

	private class CompoundTableInsertEdit extends CompoundEdit {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int index;
		private int offset;
		private LpEditorTable table;

		public CompoundTableInsertEdit(LpEditorTable table, int index,
				int offset) {
			this.index = index;
			this.offset = offset;
			this.table = table;
		}

		@Override
		public void undo() {
			super.undo();
			cleanTableData(index);
		}

		@Override
		public void redo() {
			super.redo();
			setupTableData(table, offset);
		}
	}

	private class CompoundTableDeleteEdit extends CompoundEdit {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int index;
		private int offset;
		private LpEditorTable table;

		public CompoundTableDeleteEdit(LpEditorTable table, int index,
				int offset) {
			this.index = index;
			this.offset = offset;
			this.table = table;
		}

		@Override
		public void undo() {
			super.undo();
			setupTableData(table, offset);
		}

		@Override
		public void redo() {
			super.redo();
			cleanTableData(index);
		}

	}

	protected class LpMouseAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent() instanceof JTable) {
				if (currentCellEditor != null) {
					currentCellEditor.stopCellEditing();
				}
			} else if (e.getComponent() instanceof LpDecoratedTextPane) {
				if (currentCellEditor != null && currentCellEditor.isEditing()) {
					currentCellEditor.stopCellEditing();
				}

				requestFocusInWindow();

				// if (e.getButton() == e.BUTTON1) {
				// currentTextBlockLength = -1;
				// currentTextBlockAttributes = textPaneAttributes;
				// updateBufferStatus(textPaneAttributes, -1);
				// }
			}

			if (e.getButton() == e.BUTTON3) {
				jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	static private String getStyledText(String text) {
		String parsed = text.replaceAll("&", "&amp;");
		parsed = parsed.replaceAll("\"", "&quot;");
		parsed = parsed.replaceAll("<", "&lt;");
		parsed = parsed.replaceAll(">", "&gt;");

		return parsed;
	}

	static private String parseEscapeCharacter(String text) {
		String parsed = text.replaceAll("&amp;", "&");
		parsed = parsed.replaceAll("&quot;", "\"");
		parsed = parsed.replaceAll("&lt;", "<");
		parsed = parsed.replaceAll("&gt;", ">");

		return parsed;
	}

}
