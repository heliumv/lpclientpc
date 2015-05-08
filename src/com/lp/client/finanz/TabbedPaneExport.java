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

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.finanz.service.ExportlaufDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Panels fuer den Fibu-Export
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 30.01.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:17 $
 */
public class TabbedPaneExport extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryExportlauf = null;
	private PanelQuery panelQueryExportdaten = null;
	private PanelDialogKriterienExportlauf pdKriterienExportlauf = null;

	private ExportlaufDto exportlaufDto = null;

	private static final int IDX_EXPORTLAUF = 0;
	private static final int IDX_EXPORTDATEN = 1;

	private final static String MENU_ACTION_EXPORT = "menu_action_export";

	private final static String MENU_ACTION_IMPORT_OP = "menu_action_import_op";

	private final static String MENU_ACTION_EXPORT_SACHKONTEN = "menu_action_export_sachkonten";
	private final static String MENU_ACTION_EXPORT_DEBITORENKONTEN = "menu_action_export_debitorenkonten";
	private final static String MENU_ACTION_EXPORT_KREDITORENKONTEN = "menu_action_export_kreditorenkonten";

	private final static String ACTION_SPECIAL_REMOVE_EXPORTLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_removeexportlauf";
	private final static String ACTION_SPECIAL_REMOVE_EXPORTBELEG_EINZELN = PanelBasis.ALWAYSENABLED
			+ "_action_special_removeexportbeleg_einzeln";

	public TabbedPaneExport(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("finanz.tab.unten.export.title"));
		jbInit();
		initComponents();
	}

	public ExportlaufDto getExportlaufDto() {
		return exportlaufDto;
	}

	public void setExportlaufDto(ExportlaufDto exportlaufDto) throws Throwable {
		this.exportlaufDto = exportlaufDto;
		String sTitle = null;
		if (getExportlaufDto() != null) {
			getPanelQueryExportdaten().setDefaultFilter(
					FinanzFilterFactory.getInstance().createFKExportdaten(
							getExportlaufDto().getIId()));
			sTitle = Helper.formatTimestamp(getExportlaufDto().getTAendern(),
					LPMain.getTheClient().getLocUi());
			LPButtonAction item1 = (LPButtonAction) getPanelQueryExportlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_REMOVE_EXPORTLAUF);
			item1.getButton().setEnabled(true);
		} else {
			sTitle = "";
			LPButtonAction item1 = (LPButtonAction) getPanelQueryExportlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_REMOVE_EXPORTLAUF);
			item1.getButton().setEnabled(false);
		}
		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	private void jbInit() throws Throwable {
		// Tab 1: Exportlaeufe
		insertTab(
				LPMain.getTextRespectUISPr("finanz.tab.oben.exportlauf.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.exportlauf.tooltip"),
				IDX_EXPORTLAUF);
		insertTab(LPMain.getTextRespectUISPr("finanz.tab.oben.belege.title"),
				null, null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.belege.title"),
				IDX_EXPORTDATEN);
		setSelectedComponent(getPanelQueryExportlauf());
		// refresh
		getPanelQueryExportlauf().eventYouAreSelected(false);
		// damit gleich einer selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(getPanelQueryExportlauf(),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * 4 Verarbeite unsere eigenen Itemevents die von anderen Panels, Dialogen,
	 * ... kommen.
	 * 
	 * @param e
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryExportlauf) {
				Object key = panelQueryExportlauf.getSelectedId();
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					ExportlaufDto exportlaufDto = DelegateFactory.getInstance()
							.getFibuExportDelegate()
							.exportlaufFindByPrimaryKey((Integer) key);
					setExportlaufDto(exportlaufDto);
					setSelectedComponent(getPanelQueryExportdaten());
					updatePanelQueryExportdaten();
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryExportlauf) {
				Object key = panelQueryExportlauf.getSelectedId();
				if (key != null) {
					ExportlaufDto exportlaufDto = DelegateFactory.getInstance()
							.getFibuExportDelegate()
							.exportlaufFindByPrimaryKey((Integer) key);
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_EXPORTLAUF, true);
					setExportlaufDto(exportlaufDto);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_EXPORTLAUF, false);
					setExportlaufDto(null);
				}
				panelQueryExportlauf.updateButtons();
			} else if (e.getSource() == panelQueryExportdaten) {
				panelQueryExportdaten.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryExportlauf) {
				getInternalFrame().showPanelDialog(getPdKriterienExportlauf());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (e.getSource() == getPanelQueryExportlauf()) {
				if (this.getExportlaufDto() != null) {

				}
			} else if (e.getSource() == getPanelQueryExportdaten()) {

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			if (e.getSource() == getPanelQueryExportlauf()) {
				if (getExportlaufDto() != null) {
					try {
						// Das Loeschen muss bestaetigt werden
						Object pattern[] = { Helper.formatDatum(
								getExportlaufDto().getTStichtag(), LPMain
										.getTheClient().getLocUi()) };
						MessageFormat mf = new MessageFormat(
								LPMain.getTextRespectUISPr("fb.frage.exportlaufloeschen"));
						boolean bLoeschen = DialogFactory
								.showModalJaNeinDialog(getInternalFrame(),
										mf.format(pattern),
										LPMain.getTextRespectUISPr("lp.frage"));
						// Wenn der Exportlauf aelter als 2 Wochen ist, dann
						// noch eine Meldung
						if (bLoeschen
								&& Helper.getDifferenzInTagen(
										new java.util.Date(System
												.currentTimeMillis()),
										getExportlaufDto().getTStichtag()) > 14) {
							bLoeschen = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr("fb.frage.exportlaufloeschen2"),
											LPMain.getTextRespectUISPr("lp.frage"));
						}
						if (bLoeschen) {
							DelegateFactory
									.getInstance()
									.getFibuExportDelegate()
									.nehmeExportlaufZurueckUndLoescheIhn(
											getExportlaufDto().getIId());
							// nach dem Loeschen ein Refresh
							setExportlaufDto(null);
							getPanelQueryExportlauf()
									.eventYouAreSelected(false);
						}
					} catch (Throwable ex) {
						handleExportExceptions(ex);
					}
				}
			} else if (e.getSource() == getPanelQueryExportdaten()) {
				if (getPanelQueryExportdaten().getSelectedId() != null) {
					try {
						boolean bLoeschen = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("fb.frage.diesenbelegausexportentfernen"),
										LPMain.getTextRespectUISPr("lp.frage"));
						if (bLoeschen) {
							DelegateFactory
									.getInstance()
									.getFibuExportDelegate()
									.removeExportdaten(
											(Integer) getPanelQueryExportdaten()
													.getSelectedId());
							getPanelQueryExportdaten().eventYouAreSelected(
									false);
						}
					} catch (Throwable ex) {
						handleExportExceptions(ex);
					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getPdKriterienExportlauf()) {
				createExportlauf();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPdKriterienExportlauf()) {
				getPanelQueryExportlauf().eventYouAreSelected(false);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_EXPORTLAUF) {
			panelQueryExportlauf.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_EXPORTDATEN) {
			getPanelQueryExportdaten().eventYouAreSelected(false);
			updatePanelQueryExportdaten();
		}
	}

	private FilterKriterium[] buildFiltersExportlauf() throws Throwable {
		FilterKriterium[] filter = null;
		ExportlaufDto exportlaufDto = getExportlaufDto();
		if (exportlaufDto != null) {
			filter = FinanzFilterFactory.getInstance().createFKExportdaten(
					exportlaufDto.getIId());
		}
		return filter;
	}

	private void updatePanelQueryExportdaten() throws Throwable {
		getPanelQueryExportdaten().setDefaultFilter(buildFiltersExportlauf());
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_EXPORT)) {
			getInternalFrame().showPanelDialog(getPdKriterienExportlauf());
		}
		if (e.getActionCommand().equals(MENU_ACTION_IMPORT_OP)) {
			importOffenePosten();
		}
		if (e.getActionCommand().equals(MENU_ACTION_EXPORT_DEBITORENKONTEN)) {
			exportiereKonten(FinanzServiceFac.KONTOTYP_DEBITOR);
		}
		if (e.getActionCommand().equals(MENU_ACTION_EXPORT_KREDITORENKONTEN)) {
			exportiereKonten(FinanzServiceFac.KONTOTYP_KREDITOR);
		}
		if (e.getActionCommand().equals(MENU_ACTION_EXPORT_SACHKONTEN)) {
			exportiereKonten(FinanzServiceFac.KONTOTYP_SACHKONTO);
		}
	}

	private void importOffenePosten() throws Throwable {
		File[] files = HelperClient.chooseFile(this,
				HelperClient.FILE_FILTER_CSV, false);
		File f = null;
		if (files != null && files.length > 0) {
			f = files[0];
		}
		if (f != null) {
			ArrayList<String[]> al;
			LPCSVReader reader = new LPCSVReader(new FileReader(f), ';');
			al = (ArrayList<String[]>) reader.readAll();
			reader.close();
			if (al.size() > 0) {
				String err = DelegateFactory.getInstance()
						.getFibuExportDelegate().importiereOffenePosten(al);
				if (err.length() > 0) {
//					JTextArea textArea = new JTextArea(err) ;
//					JScrollPane scrollPane = new JScrollPane(textArea) ;
//					textArea.setLineWrap(true) ;
//					textArea.setWrapStyleWord(true) ;

					JList listArea = new JList(err.split("\n")) ;
					JScrollPane scrollPane = new JScrollPane(listArea) ;
					scrollPane.setPreferredSize(new Dimension(600, 300));
					JOptionPane.showMessageDialog(this, scrollPane, "Fehler beim Import", JOptionPane.ERROR_MESSAGE);
//					JOptionPane.showMessageDialog(this, err,
//							"Fehler beim Import", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this,
							"Daten erfolgreich importiert",
							"Offene Posten Import",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	private void createExportlauf() throws Throwable {
		try {
			// Kriterien aus dem Dialogpanel holen
			FibuExportKriterienDto exportKriterienDto = new FibuExportKriterienDto();
			// Stichtag
			exportKriterienDto.setDStichtag(getPdKriterienExportlauf()
					.getTStichtag());
			// Belege ausserhalb Gueltigheitszeitraum
			exportKriterienDto
					.setBAuchBelegeAusserhalbGueltigkeitszeitraum(getPdKriterienExportlauf()
							.getBExportiereBelegeAusserhalbGueltigkeitszeitraum());
			exportKriterienDto
					.setBBelegeAusserhalbGueltigkeitszeitraumAlsExportiertMarkieren(getPdKriterienExportlauf()
							.getBBelegeAusserhalbGueltigkeitszeitraumNurMarkieren());
			// Belegart
			exportKriterienDto.setSBelegartCNr(getPdKriterienExportlauf()
					.getSBelegartCNr());
			String data = DelegateFactory.getInstance().getFibuExportDelegate()
					.exportiereBelege(exportKriterienDto);
			saveExportFile(data, getPdKriterienExportlauf().getSBelegartCNr());
		} catch (Throwable t) {
			handleExportExceptions(t);
		} finally {
			// immer refresh (damit auch nach einem aufgetretenen fehler)
			getPanelQueryExportlauf().eventYouAreSelected(false);
			// den letzten (=neuesten) selektieren
			Integer iIdLetzter = DelegateFactory.getInstance()
					.getFibuExportDelegate().exportlaufFindLetztenExportlauf();
			getPanelQueryExportlauf().setSelectedId(iIdLetzter);
			if (iIdLetzter != null) {
				ExportlaufDto exportlaufDto = DelegateFactory.getInstance()
						.getFibuExportDelegate()
						.exportlaufFindByPrimaryKey(iIdLetzter);
				setExportlaufDto(exportlaufDto);
			}
		}
	}

	private PanelQuery getPanelQueryExportlauf() throws Throwable {
		if (panelQueryExportlauf == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKMandantCNr();

			panelQueryExportlauf = new PanelQuery(null, filters,
					QueryParameters.UC_ID_EXPORTLAUF, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true);
			panelQueryExportlauf.createAndSaveAndShowButton(
					"/com/lp/client/res/delete2.png",
					LPMain.getTextRespectUISPr("fb.exportlaufloeschen"),
					ACTION_SPECIAL_REMOVE_EXPORTLAUF,
					RechteFac.RECHT_FB_DARF_EXPORT_ZURUECKNEHMEN);
			setComponentAt(IDX_EXPORTLAUF, panelQueryExportlauf);
		}
		return panelQueryExportlauf;
	}

	private PanelQuery getPanelQueryExportdaten() throws Throwable {
		if (panelQueryExportdaten == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
			panelQueryExportdaten = new PanelQuery(null, null,
					QueryParameters.UC_ID_EXPORTDATEN, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.tab.oben.belege.title"),
					true);
			panelQueryExportdaten
					.createAndSaveAndShowButton(
							"/com/lp/client/res/delete2.png",
							LPMain.getTextRespectUISPr("fb.belegausexportlaufloeschen"),
							ACTION_SPECIAL_REMOVE_EXPORTBELEG_EINZELN,
							RechteFac.RECHT_FB_CHEFBUCHHALTER);
			setComponentAt(IDX_EXPORTDATEN, panelQueryExportdaten);
		}
		return panelQueryExportdaten;
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);
		// ------------------------------------------------------------------------
		// Modul - Menu
		// ------------------------------------------------------------------------
		JMenu jmModul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);
		// Export
		WrapperMenuItem menueItemExport = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.export"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemExport.addActionListener(this);
		menueItemExport.setActionCommand(MENU_ACTION_EXPORT);
		jmModul.add(menueItemExport, 0);

		// Import
		WrapperMenuItem menueItemImportOp = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.import.op"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemImportOp.addActionListener(this);
		menueItemImportOp.setActionCommand(MENU_ACTION_IMPORT_OP);
		jmModul.add(menueItemImportOp, 1);

		jmModul.add(new JSeparator(), 2);

		// ------------------------------------------------------------------------
		// Journal - Menu
		// ------------------------------------------------------------------------
		JMenu jmJournal = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_JOURNAL);
		// Sachkonten - Export
		WrapperMenuItem menueItemExportSachkonten = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.export.sachkonten"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemExportSachkonten.addActionListener(this);
		menueItemExportSachkonten
				.setActionCommand(MENU_ACTION_EXPORT_SACHKONTEN);
		jmJournal.add(menueItemExportSachkonten, 0);
		// Debitorenkonten - Export
		WrapperMenuItem menueItemExportDebitorenkonten = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.export.debitorenkonten"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemExportDebitorenkonten.addActionListener(this);
		menueItemExportDebitorenkonten
				.setActionCommand(MENU_ACTION_EXPORT_DEBITORENKONTEN);
		jmJournal.add(menueItemExportDebitorenkonten, 1);
		// Kreditorenkonten - Export
		WrapperMenuItem menueItemExportKreditorenkonten = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.export.kreditorenkonten"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemExportKreditorenkonten.addActionListener(this);
		menueItemExportKreditorenkonten
				.setActionCommand(MENU_ACTION_EXPORT_KREDITORENKONTEN);
		jmJournal.add(menueItemExportKreditorenkonten, 2);

		return wmb;
	}

	private void handleExportExceptions(Throwable t) throws Throwable {
		if (t instanceof ExceptionLP) {
			ExceptionLP e = (ExceptionLP) t;
			ArrayList<?> a = e.getAlInfoForTheClient();
			StringBuffer sb = new StringBuffer();
			if (a != null && !a.isEmpty()) {
				for (Iterator<?> iter = a.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					sb.append(item + "\n");
				}
			}
			String sToken = null;
			switch (e.getICode()) {
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT: {
				sToken = "finanz.error.debitorenkontonichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KREDITORENKONTO_NICHT_DEFINIERT: {
				sToken = "finanz.error.kreditorenkontonichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_UST_KONTO_NICHT_DEFINIERT: {
				sToken = "finanz.error.ustkontonichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_EINGANGSRECHNUNG_IST_GELOCKT: {
				sToken = "finanz.error.ergesperrt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_RECHNUNG_IST_GELOCKT: {
				sToken = "finanz.error.regesperrt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_GUTSCHRIFT_IST_GELOCKT: {
				sToken = "finanz.error.gsgesperrt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KOSTENSTELLE_HAT_KEIN_SACHKONTO: {
				sToken = "finanz.error.kostenstellehatkeinsachkonto";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_EINGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT: {
				sToken = "finanz.error.eristnichtvollstaendigkontiert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_ES_DARF_NUR_DER_LETZTE_GELOESCHT_WERDEN: {
				sToken = "finanz.error.esdarfnurderletzteexportlaufgeloeschtwerden";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KONTOLAENDERART_NICHT_DEFINIERT: {
				sToken = "finanz.error.kontolaenderartnichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_LAENDERART_NICHT_FESTSTELLBAR_FUER_PARTNER: {
				sToken = "finanz.error.laenderartkannnichtfestgestelltwerden";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT: {
				sToken = "finanz.error.belegistnochnichtaktiviert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_AUSGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT: {
				sToken = "finanz.error.belegistnichtvollstaendigkontiert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_LIEFERANTENRECHNUNGSNUMMER_FEHLT: {
				sToken = "finanz.error.lieferantenrechnungsnummerfehlt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_STICHTAG_NICHT_DEFINIERT: {
				sToken = "finanz.error.stichtagnichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_LIEGT_AUSSERHALB_GUELIGEM_EXPORTZEITRAUM: {
				sToken = "finanz.error.belegausserhalbgueltigemzeitraum";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_FINANZAMT_NICHT_VOLLSTAENDIG_DEFINIERT: {
				sToken = "finanz.error.finanzamtnichtvollstaendigdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KONTO_HAT_KEIN_FINANZAMT: {
				sToken = "finanz.error.kontohatkeinfinanzamt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_SALDO_UNGLEICH_NULL: {
				sToken = "finanz.error.saldoungleichnull";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE: {
				sToken = "finanz.export.error.keineartikelgruppe";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_WAEHRUNG_NICHT_GEFUNDEN: {
				sToken = "finanz.error.waehrungnichtgefunden";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KORREKTURBUCHUNG_ZUHOCH: {
				sToken = "finanz.error.korrekturzuhoch";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEINKURS_ZUDATUM: {
				sToken = "finanz.error.keinkurs";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE: {
				sToken = "finanz.error.keinmwstcode";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE: {
				sToken = "finanz.error.keinkontofuerartikelgruppe";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_UST_KONTO_DEFINIERT: {
				sToken = "finanz.error.keinustkonto";
			}
				break;
			default: {
				throw e;
			}
			}
			String sText = LPMain.getTextRespectUISPr(sToken);
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					sText + "\n" + sb.toString());
		} else {
			throw t;
		}
	}

	private void saveExportFile(String data, String sBelegartCNr)
			throws Throwable {
		if (data != null) {
			ParametermandantDto parameter = null;
			if (sBelegartCNr.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
				parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_EINGANGSRECHNUNG);
			} else if (sBelegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {
				parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_RECHNUNG);
			} else if (sBelegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
				parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_GUTSCHRIFT);
			}
			// Id des erzeugten Exportlaufs holen
			Integer iIdLetzterExportlauf = DelegateFactory.getInstance()
					.getFibuExportDelegate().exportlaufFindLetztenExportlauf();
			ParametermandantDto pASCII = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_ASCII);
			Boolean bASCII = false;
			if (pASCII != null) {
				bASCII = (Boolean) pASCII.getCWertAsObject();
			}
			// Daten speichern

			boolean bSaved = LPMain.getInstance()
					.saveFile(getInternalFrame(), parameter.getCWert(),
							data.getBytes("windows-1252"), bASCII);
			if (bSaved) {
				ParametermandantDto pMitBelegen = null;
				pMitBelegen = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_EXPORT_MIT_BELEGEN);
				if (pMitBelegen.getCWert().equals("1"))
					saveExportDokumente(iIdLetzterExportlauf, sBelegartCNr,
							new File(parameter.getCWert()).getParent());
			}
			if (!bSaved) {
				// Falls nicht gespeichert werden konnte, wird der Exportlauf
				// zurueckgenommen
				DelegateFactory
						.getInstance()
						.getFibuExportDelegate()
						.nehmeExportlaufZurueckUndLoescheIhn(
								iIdLetzterExportlauf);
			} else {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("fb.export.datensichern2"));
			}
		} else {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("fb.export.keinebelegezuexportieren"));
		}
	}

	private void saveExportDokumente(Integer exportlaufIId, String belegartCNr,
			String exportDir) {
		ExportdatenDto[] exportdatenDtos;
		try {
			exportdatenDtos = DelegateFactory
					.getInstance()
					.getFibuExportDelegate()
					.exportdatenFindByExportlaufIIdBelegartCNr(exportlaufIId,
							belegartCNr);
			if (belegartCNr.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
				saveExportDokumenteEr(exportdatenDtos, exportDir);
			} else if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {
				saveExportDokumenteAr(exportdatenDtos, exportDir);
			} else if (belegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
				saveExportDokumenteGs(exportdatenDtos, exportDir);
			}
		} catch (ExceptionLP e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void saveExportDokumenteEr(ExportdatenDto[] exportdatenDtos,
			String exportDir) {
		for (int i = 0; i < exportdatenDtos.length; i++) {
			try {
				PrintInfoDto values = DelegateFactory
						.getInstance()
						.getJCRDocDelegate()
						.getPathAndPartnerAndTable(
								exportdatenDtos[i].getIBelegiid(),
								QueryParameters.UC_ID_EINGANGSRECHNUNG);
				JCRDocDto jcr = new JCRDocDto();
				jcr.setDocPath(values.getDocPath()); // + "/ER");
				jcr.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
				jcr.setlPartner(values.getiId());
				jcr.setsTable(values.getTable());
				// HELIUMV/Eingangsrechng/Eingangsrechng/11.0000204/ER
				ArrayList<?> jcrdocs = getJcrDocs(jcr);
				for (int j = 0; j < jcrdocs.size(); j++) {
					if (jcrdocs.get(j) instanceof JCRDocDto) {
						jcr = getLastJcrVersion((JCRDocDto) jcrdocs.get(j));
						if (jcr == null)
							jcr = (JCRDocDto) jcrdocs.get(j);
						if (jcr != null) {
							// eine gefunden, als PDF speichern
							JCRDocDto jcrdocDto = DelegateFactory.getInstance()
									.getJCRDocDelegate().getData(jcr);
							if (jcrdocDto.getbData() != null) {
								saveAs(exportDir, jcrdocDto);
								// saveAsPdf(exportDir, jcrdocDto, "RE");
							}
						}
					}
				}
			} catch (ExceptionLP e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

	}

	private void saveExportDokumenteAr(ExportdatenDto[] exportdatenDtos,
			String exportDir) {
		for (int i = 0; i < exportdatenDtos.length; i++) {
			try {
				PrintInfoDto values = DelegateFactory
						.getInstance()
						.getJCRDocDelegate()
						.getPathAndPartnerAndTable(
								exportdatenDtos[i].getIBelegiid(),
								QueryParameters.UC_ID_RECHNUNG);
				JCRDocDto jcr = new JCRDocDto();
				jcr.setDocPath(values.getDocPath().add(
						new DocNodeFile("rech_rechnung.jasper")));
				jcr.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
				jcr.setlPartner(values.getiId());
				jcr.setsTable(values.getTable());

				jcr = getLastJcrVersion(jcr);
				if (jcr != null) {
					// eine gefunden, als PDF speichern
					JCRDocDto jcrdocDto = DelegateFactory.getInstance()
							.getJCRDocDelegate().getData(jcr);
					if (jcrdocDto.getbData() != null) {
						saveAsPdf(exportDir, jcrdocDto, "RE");
					}
				}
			} catch (ExceptionLP e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private void saveExportDokumenteGs(ExportdatenDto[] exportdatenDtos,
			String exportDir) {
		for (int i = 0; i < exportdatenDtos.length; i++) {
			try {
				PrintInfoDto values = DelegateFactory
						.getInstance()
						.getJCRDocDelegate()
						.getPathAndPartnerAndTable(
								exportdatenDtos[i].getIBelegiid(),
								QueryParameters.UC_ID_GUTSCHRIFT);
				JCRDocDto jcr = new JCRDocDto();
				jcr.setDocPath(values.getDocPath().add(
						new DocNodeFile("rech_gutschrift.jasper")));
				jcr.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
				jcr.setlPartner(values.getiId());
				jcr.setsTable(values.getTable());

				jcr = getLastJcrVersion(jcr);
				// if (jcr == null) {
				// // aufgrund eines Bug sind die Gutschriften teilweise bei den
				// Rechnungen abgelegt
				// jcr = new JCRDocDto();
				// jcr.setsFullNodePath(((String)
				// values[0]).replace("Gutschrift","Rechnung") +
				// "/rech_gutschrift.jasper");
				// jcr.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
				// jcr.setlPartner((Integer) values[1]);
				// jcr.setsTable((String)values[2]);
				// jcr = getLastJcrVersion(jcr);
				// }
				if (jcr != null) {
					// eine gefunden, als PDF speichern
					JCRDocDto jcrdocDto = DelegateFactory.getInstance()
							.getJCRDocDelegate().getData(jcr);
					if (jcrdocDto.getbData() != null) {
						saveAsPdf(exportDir, jcrdocDto, "GS");
					}
				}
			} catch (ExceptionLP e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private ArrayList<JCRDocDto> getJcrDocs(JCRDocDto jcr) {
		ArrayList<JCRDocDto> al = new ArrayList<JCRDocDto>();
		List<DocNodeBase> children;
		try {
			children = DelegateFactory.getInstance().getJCRDocDelegate()
					.getDocNodeChildrenFromNode(jcr.getDocPath());
			for (DocNodeBase docNode : children) {
				al.add(DelegateFactory
						.getInstance()
						.getJCRDocDelegate()
						.getJCRDocDtoFromNode(
								jcr.getDocPath().getDeepCopy().add(docNode)));
			}
		} catch (ExceptionLP e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return al;
	}

	private JCRDocDto getLastJcrVersion(JCRDocDto jcr) {
		ArrayList<?> al = new ArrayList<Object>();
		try {
			al = DelegateFactory.getInstance().getJCRDocDelegate()
					.getAllDocumentVersions(jcr);
		} catch (ExceptionLP e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		long version = -1;
		int last = -1;
		// letzte Version nehmen
		for (int j = 0; j < al.size(); j++) {
			if (al.get(j) instanceof JCRDocDto) {
				JCRDocDto jcrdocDto = (JCRDocDto) al.get(j);
				if (jcrdocDto.getlVersion() > version) {
					version = jcrdocDto.getlVersion();
					last = j;
				}
			}
		}
		if (last >= 0)
			return (JCRDocDto) al.get(last);
		else
			return null;
	}

	private void saveAsPdf(String exportDir, JCRDocDto jcrdocDto, String kennung) {
		try {
			ByteArrayInputStream bStream = new ByteArrayInputStream(
					jcrdocDto.getbData());
			ObjectInputStream oStream = new ObjectInputStream(bStream);
			JasperPrint jPrint = (JasperPrint) oStream.readObject();
			byte[] ba = JasperExportManager.exportReportToPdf(jPrint);
			File file = new File(exportDir + "\\" + kennung
					+ jcrdocDto.getsBelegnummer().replace(".", "_") + ".pdf");
			FileOutputStream fo = new FileOutputStream(file);
			fo.write(ba);
			fo.flush();
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					"Fehler beim Speichern des Exportdokuments " + kennung
							+ jcrdocDto.getsBelegnummer() + "\n"
							+ e.getMessage());
		}
	}

	private void saveAs(String exportDir, JCRDocDto jcrdocDto) {
		try {
			File file = new File(exportDir + "\\" + jcrdocDto.getsName()
					+ jcrdocDto.getsBelegnummer().replace(".", "_") + ".pdf");
			FileOutputStream fo = new FileOutputStream(file);
			fo.write(jcrdocDto.getbData());
			fo.flush();
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					"Fehler beim Speichern des Exportdokuments ER"
							+ jcrdocDto.getsBelegnummer() + "\n"
							+ e.getMessage());
		}
	}

	private void exportiereKonten(String kontotypCNr) throws Throwable {
		String sParameter = null;
		// Die Zieldatei ist fuer jeden kontotyp in einem mandantenparameter
		// definiert
		if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			sParameter = ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_DEBITORENKONTEN;
		} else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
			sParameter = ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_KREDITORENKONTEN;
		} else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			sParameter = ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_SACHKONTEN;
		}
		// Parameter holen
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FINANZ, sParameter);
		// Daten holen
		String sDaten = DelegateFactory.getInstance().getFibuExportDelegate()
				.exportierePersonenkonten(kontotypCNr);
		if (sDaten != null) {
			// und speichern
			LPMain.getInstance().saveFile(getInternalFrame(),
					parameter.getCWert(), sDaten.getBytes(), false);
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("fb.export.datensichern"));
		} else {
			String sToken = null;
			if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
				sToken = "fb.export.keinedebitorenkonteneingetragen";
			} else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
				sToken = "fb.export.keinekreditorenkonteneingetragen";
			} else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
				sToken = "fb.export.keinesachkonteneingetragen";
			}
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr(sToken));
		}
	}

	private PanelDialogKriterienExportlauf getPdKriterienExportlauf()
			throws Throwable {
		if (pdKriterienExportlauf == null) {
			pdKriterienExportlauf = new PanelDialogKriterienExportlauf(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fb.kriterien.exportlauf"));
		}
		return pdKriterienExportlauf;
	}
}
