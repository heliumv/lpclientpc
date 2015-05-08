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
package com.lp.client.system.pflege;

import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AGPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AGSTKLPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AGSTUECKLISTE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_ANFPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_ANFRAGE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_ANGEBOT;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AUFPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_AUFTRAG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_BENUTZER;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_BESTELLUNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_BESTPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_EINGANGSRECHNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_GUTSCHRIFT;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_GUTSPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_KASSENBUCH;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_KUNDE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LIEFERANT;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LIEFERSCHEIN;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LIEFERSPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LOS;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_LOSABLIEFERUNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PARTNER;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PERSONAL;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PROFORMAPOS;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PROFORMARECHNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PROJEKT;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_PROJHISTORY;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_RECHNUNG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_RECHPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_REKLAMATION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_SALDENLISTE;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_STKLPOSITION;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_UVA;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_WARENEINGANG;
import static com.lp.server.system.jcr.service.docnode.DocNodeBase.BELEGART_WEPOSITION;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDokumentenablage;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.service.DocumentResult;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class DokumentenpfadeErneuern extends PflegefunktionSupportsProgressBar
		implements ActionListener, ListSelectionListener, FocusListener,
		DocumentListener {

	private static final long DELAY_WAITING_FOR_KBHIT = 500;
	private static final String CB_ITEM_ALL = "Alle Belegarten";
	private static final String CB_DELETE_EMPTY = "Leere Pfade entfernen";

	private static final String SEARCH_HINT = "[Bitte geben Sie einen Suchbegriff ein...]";

	private JPanel panel;
	private JSplitPane mainPane;
	private JSplitPane selectionPane;
	private JPanel modifierPane;
	private JTextField searchField;
	private JList pathsList;
	private JList documentsList;
	private JList dtoSelectionList;
	private JRadioButton showFound;
	private JRadioButton showNotFound;
	private JButton applyToSelected;
	private JButton applyToAll;
	private JButton saveWithoutDtoSelected;
	private JButton saveWithoutDtoAll;
	private JLabel lbSaveWithDto;
	private JLabel lbSaveWithoutDto;
	private JLabel taSettings;
	private JLabel taStatistics;
	private JLabel lbLookingFor;
	private JLabel lbTimeCounter;
	private WrapperGotoButton gotoButton;
	private Timer instantSearchTimer;
	private JComboBox cbBelegart;
	private Timer countingTimer;

	private SwingWorker<Void, Void> swingWorker;

	private boolean isRunning = false;
	private List<DocumentResult> documentResults;
	private List<String> belegarten;
	private List<String> pflegeArgs;

	private int foundFileCount = 0;
	private int notFoundFileCount = 0;

	private int foundPathCount = 0;
	private int notFoundPathCount = 0;

	private boolean cancelPressed = false;

	public DokumentenpfadeErneuern() {
	}

	@Override
	public void run() {
		// isRunning = true ;
		// try {
		// doTheDirtyWork() ;
		// } catch(Exception e) {
		// System.out.println("ex" + e.getStackTrace()) ;
		// }
		isRunning = true;
		documentResults = new ArrayList<DocumentResult>();
		swingWorker = new PflegeWorker();
		swingWorker.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					setProgressValue((Integer) evt.getNewValue());
					fireProgressEvent(null);
				}
			}
		});
		fireStartedEvent(new EventObject(this));
		refreshSelectionLists();
		swingWorker.execute();
	}

	private void showDokumentenablage(JCRDocDto jcr) {
		InternalFrame frame = ((PanelPflegeNew) SwingUtilities
				.getAncestorOfClass(PanelPflegeNew.class, panel))
				.getInternalFrame();
		PanelDokumentenablage panelDokumentenverwaltung;

		DocPath path = jcr.getDocPath();
		path.asDocNodeList().remove(path.getLastDocNode());
		try {
			panelDokumentenverwaltung = new PanelDokumentenablage(frame,
					jcr.getsFilename(), path, "", jcr.getsRow(), true, null);
			frame.showPanelDialog(panelDokumentenverwaltung);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void refreshSelectionLists() {
		if (documentResults == null)
			documentResults = new ArrayList<DocumentResult>();
		Collection<String> foundPathList = new ArrayList<String>();
		List<String> notFoundPathList = new ArrayList<String>();

		foundFileCount = 0;
		notFoundFileCount = 0;

		for (DocumentResult result : documentResults) {
			if (result == null)
				continue;
			foundPathList.addAll(result.getAllNewPaths());
			notFoundPathList.addAll(result.getAllOldPaths());
			foundFileCount += result.getNewFileCount();
			notFoundFileCount += result.getOldFileCount();
		}

		foundPathCount = foundPathList.size();
		notFoundPathCount = notFoundPathList.size();
		Object selection = pathsList.getSelectedValue();
		if (showFound.isSelected()) {
			pathsList.setListData(foundPathList.toArray());
		} else {
			pathsList.setListData(notFoundPathList.toArray());
		}
		if (selection != null)
			pathsList.setSelectedValue(selection, false);
		refreshFileList();
		refreshStatisticsText();

	}

	private void refreshFileList() {

		String path = (String) pathsList.getSelectedValue();
		List<String> files = null;

		for (DocumentResult result : documentResults) {
			if (showFound.isSelected()) {
				files = result.getNewFiles(path);
			} else {
				files = result.getOldFiles(path);
			}
			if (files != null) {
				documentsList.setListData(files.toArray());
				break;
			}
		}
		ListModel listModel = documentsList.getModel();
		if (files == null || listModel.getSize() == 0) {
			documentsList.setListData(new Object[] {});
			refreshMatches();
		}
	}

	private void refreshStatisticsText() {
		taStatistics.setText("<html>Neue Dateien: " + foundFileCount
				+ "<br>Neue Pfade: " + foundPathCount
				+ "<br><br>Nicht zuordenbare Dateien: " + notFoundFileCount
				+ "<br>Nicht zuordenbare Pfade: " + notFoundPathCount
				+ "</html>");
	}

	private String getFoundLabelText() {
		if (pflegeArgs.contains(JCRDocFac.ARG_READONLY))
			return "Gefunden aber nicht gespeicherte Dokumente";
		if (pflegeArgs.contains(JCRDocFac.ARG_DELETEOLDFILES))
			return "Verschobene Dokumente";
		return "Kopierte Dokumente";
	}

	private String getSettingsText() {
		String text = "<html>Gefundene Dokumente werden ";
		if (pflegeArgs.contains(JCRDocFac.ARG_READONLY))
			text += "gefunden aber nicht gespeichert.<br> Alte Dokumente werden nicht gel\u00F6scht. ";
		else if (pflegeArgs.contains(JCRDocFac.ARG_DELETEOLDFILES))
			text += "verschoben.";
		else
			text += "kopiert. <br>Alte Dokumente werden nicht gel\u00F6scht.";
		if (pflegeArgs.contains(JCRDocFac.ARG_CLEARNEWFOLDERS))
			text += "<br>Neue Pfade wurden vorher ges\u00E4ubert. ";
		return text + "</html>";
	}

	private void createPanel() {
		belegarten = Arrays.asList(new String[] {
				// BELEGART_LAGERSTANDSLISTE,
				BELEGART_ANGEBOT,
				BELEGART_AGPOSITION,
				BELEGART_AGSTUECKLISTE,
				BELEGART_AGSTKLPOSITION,
				BELEGART_ANFRAGE,
				BELEGART_ANFPOSITION,
				// BELEGART_ARTIKEL,
				BELEGART_STKLPOSITION, BELEGART_AUFTRAG, BELEGART_AUFPOSITION,

				BELEGART_PARTNER, BELEGART_PERSONAL, BELEGART_BENUTZER,
				BELEGART_KUNDE, BELEGART_LIEFERANT,

				BELEGART_BESTELLUNG, BELEGART_BESTPOSITION,
				BELEGART_WARENEINGANG, BELEGART_WEPOSITION,
				BELEGART_EINGANGSRECHNG,

				BELEGART_UVA, BELEGART_SALDENLISTE, BELEGART_KASSENBUCH,

				BELEGART_LOS, BELEGART_LOSABLIEFERUNG, BELEGART_LIEFERSCHEIN,
				BELEGART_LIEFERSPOSITION, BELEGART_PROJEKT,
				BELEGART_PROJHISTORY,

				BELEGART_RECHNUNG, BELEGART_GUTSCHRIFT,
				BELEGART_PROFORMARECHNG, BELEGART_RECHPOSITION,
				BELEGART_GUTSPOSITION, BELEGART_PROFORMAPOS,

				BELEGART_REKLAMATION, });

		pflegeArgs = new ArrayList<String>();

		pflegeArgs.addAll(Arrays
				.asList(new String[] { JCRDocFac.ARG_DELETEOLDFILES,
				// DokumentenpflegeFacBean.ARG_READONLY,
				// DokumentenpflegeFacBean.ARG_CLEARNEWFOLDERS
				}));

		panel = new JPanel();
		searchField = new JTextField(SEARCH_HINT);
		mainPane = new JSplitPane();
		selectionPane = new JSplitPane();
		modifierPane = new JPanel();

		pathsList = new JList();
		documentsList = new JList();
		dtoSelectionList = new JList();

		gotoButton = new WrapperGotoButton(
				WrapperGotoButton.GOTO_PARTNER_AUSWAHL);
		gotoButton.setText(LPMain.getTextRespectUISPr("button.partner"));
		gotoButton.setEnabled(false);

		dtoSelectionList.addListSelectionListener(this);
		documentsList.addListSelectionListener(this);
		showFound = new JRadioButton();
		showNotFound = new JRadioButton();
		cbBelegart = new JComboBox();

		Collections.sort(belegarten);

		cbBelegart.addItem(CB_ITEM_ALL);
		cbBelegart.addItem(CB_DELETE_EMPTY);
		for (String belegart : belegarten) {
			cbBelegart.addItem(belegart);
		}
		showFound.setText(getFoundLabelText());
		showNotFound.setText("Nicht gefundene Dokumente");

		showFound.setActionCommand("showingList");
		showNotFound.setActionCommand("showingList");

		showFound.addActionListener(this);
		showNotFound.addActionListener(this);

		ButtonGroup bg = new ButtonGroup();
		bg.add(showNotFound);
		bg.add(showFound);
		showNotFound.setSelected(true);

		searchField.addFocusListener(this);
		searchField.getDocument().addDocumentListener(this);
		lbSaveWithDto = new JLabel("unter ausgew\u00E4hlter Zuordnung speichern");
		applyToSelected = new JButton("Auswahl");
		applyToAll = new JButton("Alle");

		lbSaveWithoutDto = new JLabel("ohne Zuordnung speichern");
		lbSaveWithoutDto.setForeground(Color.red);
		saveWithoutDtoSelected = new JButton("Auswahl");
		saveWithoutDtoAll = new JButton("Alle");

		applyToSelected.addActionListener(this);
		applyToAll.addActionListener(this);
		saveWithoutDtoSelected.addActionListener(this);
		saveWithoutDtoAll.addActionListener(this);

		applyToSelected.setActionCommand("saveSelectedWithDto");
		applyToAll.setActionCommand("saveAllWithDto");
		saveWithoutDtoSelected.setActionCommand("saveSelectedNoDto");
		saveWithoutDtoAll.setActionCommand("saveAllNoDto");

		taSettings = new JLabel();
		taStatistics = new JLabel();
		lbLookingFor = new JLabel();
		lbTimeCounter = new JLabel();
		// taSettings.set

		// taSettings.setContentType("text/html");
		// taStatistics.setContentType("text/html");
		//
		// taSettings.setEditable(false);
		// taStatistics.setEditable(false);
		taSettings.setText(getSettingsText());

		pathsList.setCellRenderer(new ListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				String renderingValue = (String) value;

				if (showFound.isSelected()) {
					for (int i = 0; i < documentResults.size(); i++) {
						DocumentResult result = documentResults.get(i);
						renderingValue = result.getVisualPath((String) value);
						if (renderingValue != null)
							break;
					}
				}
				return new DefaultListCellRenderer()
						.getListCellRendererComponent(list, renderingValue,
								index, isSelected, cellHasFocus);
			}
		});
		// documentsList.setCellRenderer(new ListCellRenderer() {
		// @Override
		// public Component getListCellRendererComponent(JList list,
		// Object value, int index, boolean isSelected,
		// boolean cellHasFocus) {
		// String filename = (String)value;
		// String renderingValue = cd.getMandantCNr() + " " + cd.getFilename();
		// return new
		// DefaultListCellRenderer().getListCellRendererComponent(list,
		// renderingValue , index, isSelected, cellHasFocus);
		// }
		// });
		//
		// dtoSelectionList.addListSelectionListener(new ListSelectionListener()
		// {
		//
		// @Override
		// public void valueChanged(ListSelectionEvent e) {
		// dtoSelectionList.setFixedCellHeight(100);
		// dtoSelectionList.setFixedCellHeight(-1);
		// dtoSelectionList.validate();
		// }
		// });
		dtoSelectionList.setCellRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				String filename = getFirstDocumentOfSelectedPath();
				String renderingValue = null;
				if (filename == null) {
					renderingValue = value.toString();
				} else {
					if (value instanceof PartnerDto) {
						PartnerDto partner = (PartnerDto) value;
						if (isSelected)
							gotoButton.setOKey(partner.getIId());
						renderingValue = "<html>" + partner.formatName();
						String adresse = partner.formatAdresse();
						if (!adresse.trim().isEmpty()) {
							renderingValue += "<br>" + adresse;
						}
						renderingValue += "</html>";
					}
				}
				if (renderingValue == null && value != null) {
					for (Method m : value.getClass().getMethods()) {
						if (m.getName().equals("formatName")
								|| m.getName().equals("getCNr")) {
							try {
								renderingValue = m.invoke(value).toString();
							} catch (Exception e) {
							}
							break;
						}
					}
					if (renderingValue == null)
						renderingValue = value.toString();
				}

				JPanel panel = new JPanel();
				JLabel renderer = new JLabel(renderingValue);
				panel.setLayout(new BorderLayout());
				panel.setBackground(isSelected ? new Color(180, 230, 255)
						: Color.white);

				panel.add(renderer, BorderLayout.WEST);
				if (isSelected) {
					panel.setPreferredSize(new Dimension((int) dtoSelectionList
							.getVisibleRect().getWidth(), 50));
					panel.setSize(new Dimension((int) dtoSelectionList
							.getVisibleRect().getWidth(), 50));
					// renderer.setPreferredSize(new
					// Dimension((int)dtoSelectionList.getVisibleRect().getWidth(),
					// 50));
				} else {
					panel.setPreferredSize(new Dimension((int) dtoSelectionList
							.getVisibleRect().getWidth(), 35));
					panel.setSize(new Dimension((int) dtoSelectionList
							.getVisibleRect().getWidth(), 35));
				}
				dtoSelectionList.setFixedCellHeight(1);
				dtoSelectionList.setFixedCellHeight(-1);
				// renderer.setPreferredSize(null);
				return panel;

				// return new
				// DefaultListCellRenderer().getListCellRendererComponent(list,
				// renderingValue, index, isSelected, cellHasFocus);
			}
		});

		mainPane.setBorder(null);
		mainPane.setLeftComponent(selectionPane);
		mainPane.setRightComponent(modifierPane);
		selectionPane.setLeftComponent(new JScrollPane(pathsList));
		selectionPane.setRightComponent(new JScrollPane(documentsList));

		modifierPane.setLayout(new GridBagLayout());
		modifierPane.add(searchField, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
				new Insets(1, 0, 1, 1), 0, 0));
		modifierPane.add(new JScrollPane(dtoSelectionList),
				new GridBagConstraints(0, 1, 1, 9, 1, 1,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 0, 1, 1), 0, 0));
		modifierPane.add(gotoButton, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 0, 1, 1), 0, 0));
		modifierPane.add(lbSaveWithDto, new GridBagConstraints(1, 1, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));
		modifierPane.add(applyToSelected, new GridBagConstraints(1, 2, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));
		modifierPane.add(applyToAll, new GridBagConstraints(1, 3, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));
		modifierPane.add(lbSaveWithoutDto, new GridBagConstraints(1, 4, 1, 1,
				0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(20, 1, 1, 1), 0, 0));
		modifierPane.add(saveWithoutDtoSelected, new GridBagConstraints(1, 5,
				1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		modifierPane.add(saveWithoutDtoAll, new GridBagConstraints(1, 6, 1, 1,
				0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));
		modifierPane.add(taSettings, new GridBagConstraints(1, 7, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));
		modifierPane.add(taStatistics, new GridBagConstraints(1, 8, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(20, 1, 1, 1), 0, 0));

		JPanel lookingForPanel = new JPanel();
		lookingForPanel.setLayout(new BorderLayout());
		lookingForPanel.add(lbLookingFor, BorderLayout.WEST);
		lookingForPanel.add(lbTimeCounter, BorderLayout.CENTER);
		modifierPane.add(lookingForPanel, new GridBagConstraints(1, 9, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));

		panel.setLayout(new GridBagLayout());

		pathsList.getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		pathsList.getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pathsList.addListSelectionListener(this);

		documentsList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (((JList) e.getSource()).getSelectedValue() instanceof String) {
						String filename = documentsList.getSelectedValue()
								.toString();
						String path = pathsList.getSelectedValue().toString();
						DocPath docPath = new DocPath(path);

						docPath.add(new DocNodeFile(filename));
						JCRDocDto jcr;
						try {
							jcr = DelegateFactory.getInstance()
									.getJCRDocDelegate()
									.getJCRDocDtoFromNode(docPath);
							showDokumentenablage(jcr);
						} catch (Throwable e1) {
						}
					}
				}
			}
		});

		panel.add(showFound, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
				new Insets(1, 1, 1, 1), 0, 0));
		panel.add(showNotFound, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
				new Insets(1, 1, 1, 1), 0, 0));
		panel.add(cbBelegart, new GridBagConstraints(2, 0, 1, 1, 0, 0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
				new Insets(1, 1, 1, 1), 0, 0));
		panel.add(mainPane, new GridBagConstraints(0, 1, 3, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		selectionPane.setContinuousLayout(true);
		selectionPane.setResizeWeight(0.5);
		mainPane.setContinuousLayout(true);
		mainPane.setResizeWeight(0.5);
		mainPane.setDividerLocation(0.5);
		selectionPane.setDividerLocation(0.5);

	}

	@Override
	public String getKategorie() {
		return KATEGORIE_DOKUMENTENABLAGE;
	}

	@Override
	public String getName() {
		return "Pfade erneuern";
	}

	@Override
	public String getBeschreibung() {
		return "<html>Mit dieser Funktion werden alle Dokumente der Dokumentenablage "
				+ "auf die neue Pfadstruktur umgestellt. Mit dieser k\u00F6nnen CNRs und "
				+ "Partnernamen ver\u00E4ndert werden, ohne dass Dokumente verloren gehen.<br/>"
				+ "<span style=\"color:red\">Legen Sie <b>unbedingt</b> ein Backup von <b>LP_DOKUMENTE</b> an "
				+ "<b>bevor</b> Sie diese Funktion nutzen!</span></html>";
	}

	@Override
	public JPanel getPanel() {
		if (panel == null)
			createPanel();
		return panel;
	}

	@Override
	public boolean isStartable() {
		return !isRunning;
	}

	@Override
	public void init() {
	}

	@Override
	public void eventYouAreSelected() {
		fireEnabledStartableEvent(new EventObject(this));
	}

	@Override
	public boolean isRunning() {
		return !cancelPressed && isRunning;
	}

	@Override
	public void cancel() {
		cancelPressed = true;
		fireCanceledEvent(new EventObject(this));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			for (Object pathObj : pathsList.getSelectedValues()) {
				String path = pathObj.toString();
				List<String> documents = new ArrayList<String>();
				String belegart = getBelegartFromPath(path);
				DocumentResult succeed = null;
				if (e.getActionCommand().equals("saveSelectedWithDto")
						|| e.getActionCommand().equals("saveSelectedNoDto")) {
					for (Object value : documentsList.getSelectedValues()) {
						documents.add(value.toString());
					}
				} else {
					for (DocumentResult result : documentResults) {
						if (result.contains(path)) {
							for (String filename : result.getOldFiles(path))
								documents.add(filename);
						}
					}
				}
				if (e.getActionCommand().equals("saveSelectedWithDto")
						|| e.getActionCommand().equals("saveAllWithDto")) {
					try {
						succeed = DelegateFactory
								.getInstance()
								.getJCRDocDelegate()
								.applyDtoTo(path, documents, belegart,
										dtoSelectionList.getSelectedValue());
					} catch (Throwable e1) {
					}
				} else if (e.getActionCommand().equals("saveSelectedNoDto")
						|| e.getActionCommand().equals("saveAllNoDto")) {
					try {
						succeed = DelegateFactory
								.getInstance()
								.getJCRDocDelegate()
								.applyDtoTo(path, documents, belegart,
										JCRDocFac.SAVE_AS_ORPHAN);
					} catch (Throwable e1) {
					}
				}
				if (succeed != null) {
					DocumentResult containingTheOldPath = null;
					for (DocumentResult result : documentResults) {
						if (result.contains(path)) {
							containingTheOldPath = result;
							break;
						}
					}
					for (String filename : documents) {
						containingTheOldPath.removeOldFile(path, filename);
					}
					documentResults.add(succeed);
				}
			}
		} catch (NullPointerException ex) {
		}
		searchField.setText("");
		refreshSelectionLists();
		refreshMatches();
		refreshModifierPane();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(pathsList)) {
			refreshFileList();
			refreshMatches();
		}
		refreshModifierPane();
	}

	private void refreshModifierPane() {
		if (showFound.isSelected() || pathsList.getSelectedIndices().length > 1) {
			searchField.setEnabled(false);
			dtoSelectionList.setEnabled(false);
			applyToAll.setEnabled(false);
			applyToSelected.setEnabled(false);
			saveWithoutDtoAll.setEnabled(!showFound.isSelected()
					&& pathsList.getSelectedIndices().length > 1);
			saveWithoutDtoSelected.setEnabled(false);
		} else {
			dtoSelectionList
					.setEnabled(documentsList.getModel().getSize() != 0);
			applyToAll.setEnabled(dtoSelectionList.getSelectedIndex() != -1
					&& documentsList.getModel().getSize() != 0);
			applyToSelected
					.setEnabled(dtoSelectionList.getSelectedIndex() != -1
							&& documentsList.getSelectedIndex() != -1);
			saveWithoutDtoAll
					.setEnabled(documentsList.getModel().getSize() != 0);
			saveWithoutDtoSelected
					.setEnabled(documentsList.getSelectedIndex() != -1);
			searchField.setEnabled(documentsList.getModel().getSize() != 0);
			// gotoButton.setEnabled(dtoSelectionList.getSelectedValue()
			// instanceof PartnerDto);
		}
		if (dtoSelectionList.getSelectedValue() == null)
			gotoButton.setOKey(null);
	}

	private String getFirstDocumentOfSelectedPath() {
		if (documentsList.getModel().getSize() == 0)
			return null;
		return (String) documentsList.getModel().getElementAt(0);
	}

	private String getBelegartFromPath(String path) {
		for (DocumentResult result : documentResults) {
			if (result.contains(path))
				return result.getBelegart();
		}
		return null;
	}

	private void refreshMatches() {

		if (pathsList.getSelectedIndices().length == 1
				&& documentsList.getModel().getSize() != 0
				&& showNotFound.isSelected()) {

			String filename = getFirstDocumentOfSelectedPath();
			String path = (String) pathsList.getSelectedValue();

			if (filename == null || path == null) {
				dtoSelectionList.setListData(new Object[] {});
				return;
			}
			//
			// String belegart = getBelegartFromPath(path);
			// QueryType[] queryTypes = null;
			// FilterKriterium[] filterKriterium = null;
			// int ucID = 0;
			// if(belegart.equals(BELEGART_AUFTRAG)) {
			// queryTypes =
			// AuftragFilterFactory.getInstance().createQTPanelAuftragAuswahl();
			// try {
			// filterKriterium =
			// AuftragFilterFactory.getInstance().createFKSchnellansicht();
			// } catch (Throwable e) {
			// e.printStackTrace();
			// }
			// ucID = QueryParameters.UC_ID_ANFRAGE;
			// } else if(belegart.equals(BELEGART_ANFRAGE)) {
			// queryTypes =
			// AnfrageFilterFactory.getInstance().createQTPanelAnfrageauswahl();
			// filterKriterium =
			// AnfrageFilterFactory.getInstance().createFKAnfrageSchnellansicht();
			// ucID = QueryParameters.UC_ID_ANFRAGE;
			// }
			// else if(belegart.equals(BELEGART_ANGEBOT)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_ANGEBOT;
			// } else if(belegart.equals(BELEGART_ARTIKEL)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_ARTIKEL;
			// } else if(belegart.equals(BELEGART_BESTELLUNG)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_BESTELLUNG;
			// } else if(belegart.equals(BELEGART_EINGANGSRECHNG)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_EINGANGSRECHNUNG;
			// } else if(belegart.equals(BELEGART_GUTSCHRIFT)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_GUTSCHRIFT;
			// } else if(belegart.equals(BELEGART_RECHNUNG)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_RECHNUNG;
			// } else if(belegart.equals(BELEGART_PROFORMARECHNG)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_PROFORMARECHNUNG
			// } else if(belegart.equals(BELEGART_LIEFERSCHEIN)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_LIEFERSCHEIN;
			// } else if(belegart.equals(BELEGART_LOS)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_LOS;
			// } else if(belegart.equals(BELEGART_UVA)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_FINANZAMT;
			// }
			// else if(belegart.equals(BELEGART_)) {
			// queryTypes =
			// filterKriterium =
			// ucID = QueryParameters.UC_ID_
			// }
			// try {
			// panelQuery = new PanelQuery(queryTypes, filterKriterium, ucID,
			// null, null, null);
			// } catch (Throwable e1) {
			// }

			try {
				List<?> list = DelegateFactory
						.getInstance()
						.getJCRDocDelegate()
						.getDtoMatches(
								path + filename,
								getBelegartFromPath(path),
								searchField.getText().equals(SEARCH_HINT) ? ""
										: searchField.getText().trim());
				if (list == null)
					dtoSelectionList.setListData(new Object[] {});
				else
					dtoSelectionList.setListData(list.toArray());
			} catch (Throwable e) {
				dtoSelectionList.setListData(new Object[] {});
			}
		} else {
			dtoSelectionList.setListData(new Object[] {});
		}
		panel.repaint();
	}

	private void initInstantSearch() {
		if (instantSearchTimer != null) {
			instantSearchTimer.cancel();
			instantSearchTimer.purge();
		}
		instantSearchTimer = new Timer();
		instantSearchTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				refreshMatches();
			}
		}, DELAY_WAITING_FOR_KBHIT);
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		initInstantSearch();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		initInstantSearch();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		if (searchField.getText().equals(SEARCH_HINT)) {
			searchField.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		if (searchField.getText().equals("")) {
			searchField.setText(SEARCH_HINT);
		}
	}

	private void setPflegeProgress(DefaultBoundedRangeModel dbrm) {
		setProgress(dbrm);
	}

	private class PflegeWorker extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() throws Exception {
			documentResults = new ArrayList<DocumentResult>();
			int progress = 0;
			setProgress(0);
			cbBelegart.setEnabled(false);
			List<String> selectedBelegarten = new ArrayList<String>();
			setPflegeProgress(new DefaultBoundedRangeModel(0, 0, 0,
					selectedBelegarten.size() + 1));
			if (!cbBelegart.getSelectedItem().equals(CB_DELETE_EMPTY)) {
				selectedBelegarten = cbBelegart.getSelectedItem()
						.equals(CB_ITEM_ALL) ? belegarten
						: new ArrayList<String>();
				if (selectedBelegarten.size() == 0)
					selectedBelegarten.add((String) cbBelegart
							.getSelectedItem());
				setPflegeProgress(new DefaultBoundedRangeModel(0, 0, 0,
						selectedBelegarten.size() + 1));

				for (String belegart : selectedBelegarten) {
					if (cancelPressed) {
						break;
					}

					try {
						pflegeArgs.add(belegart);
						lbLookingFor.setText("Suche nach " + belegart.trim()
								+ "...");
						DocumentResult result;
						int startIndex = 0;
						do {

							if (countingTimer != null)
								countingTimer.cancel();
							countingTimer = new Timer();
							countingTimer.scheduleAtFixedRate(
									new CountingTask(), 0, 1000);

							pflegeArgs.add(JCRDocFac.ARG_STARTINDEX
									+ startIndex);
							result = runPflegeOnServer(pflegeArgs
									.toArray(new String[0]));
							pflegeArgs.remove(JCRDocFac.ARG_STARTINDEX
									+ startIndex);

							if (result != null) {
								startIndex += result.getCheckedFilesCount()
										- result.getDeletedFilesCount();

								if (result.getFileCount() > 0)
									documentResults.add(result);
								refreshSelectionLists();
							} else {
								System.out.println("result = null");
							}
						} while (result != null
								&& result.getCheckedFilesCount() > 0
								&& !cancelPressed);

						refreshSelectionLists();
						progress++;
						setProgress(progress);
					} catch (Throwable e) {
						System.out.println("throwing " + e.getMessage());
					}
					pflegeArgs.remove(belegart);
				}
				if (countingTimer != null)
					countingTimer.cancel();

				lbTimeCounter.setText("");
			}
			
			int countDelete = 0;
			if (!cancelPressed) {
				lbLookingFor.setText("L\u00F6sche leere Pfade...");
				try {
					countDelete = DelegateFactory.getInstance().getJCRDocDelegate()
							.removeEmptyNodes();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

			if (!cancelPressed && selectedBelegarten.size() > 1) {
				lbLookingFor
						.setText("Suche nach nicht gefundenen Dokumenten...");
				try {
					DocumentResult result = DelegateFactory.getInstance()
							.getJCRDocDelegate().getAllDocuments();
					DocumentResult notFoundFiles = new DocumentResult("");
					for (String path : result.getAllOldPaths()) {
						boolean containing = false;
						for (DocumentResult alreadyFound : documentResults) {
							if (alreadyFound.contains(path)) {
								containing = true;
								break;
							}
						}
						if (!containing) {
							for (String filename : result.getOldFiles(path))
								notFoundFiles.addNotFoundFile(path, filename);
						}
					}
					documentResults.add(notFoundFiles);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

			if (cancelPressed) {
				cancelPressed = false;
				lbLookingFor.setText("Pflege abgebrochen.");

			} else {
				if (progress != selectedBelegarten.size())
					lbLookingFor
							.setText("<html><span style=\"color:red\">Es ist ein Fehler aufgetreten!</span></html>");
				else
					lbLookingFor.setText("Pflege erfolgreich! " + countDelete + " leere Pfade gel\u00F6scht!");

			}
			setProgress(selectedBelegarten.size() + 1);
			cbBelegart.setEnabled(true);

			refreshSelectionLists();
			return null;
		}

		@Override
		protected void done() {
			Toolkit.getDefaultToolkit().beep();
			isRunning = false;
			fireDoneEvent(new EventObject(this));
			super.done();
		}

		private DocumentResult runPflegeOnServer(String... args)
				throws Throwable {
			try {
				return DelegateFactory.getInstance().getJCRDocDelegate()
						.runPflege(args);
			} catch (Throwable t) {
				System.out.println(t.getMessage());
				throw t;
			}
		}
	}

	private class CountingTask extends TimerTask {

		int seconds;

		public CountingTask() {
			seconds = 60;
		}

		@Override
		public void run() {
			seconds--;
			StringBuffer sb = new StringBuffer();

			sb.append(seconds);
			lbTimeCounter.setText(sb.toString());
		}

	}

}
