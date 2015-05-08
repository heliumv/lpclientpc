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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2fprod.gui.plaf.skin.impl.gtk.parser.Token;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.service.LpHvDirekthilfeDto;

public class PflegeHvDirekthilfe implements IPflegefunktion, ActionListener, ListSelectionListener, FocusListener {
	
	private static Logger logger = LpLogger.getLogger(PflegeHvDirekthilfe.class);
	
	private static final String FILE_EXT = ".hvdh";
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private static final int CHOICE_SERVER = 1;
	private static final int CHOICE_DATEI = 2;
	private static final int CHOICE_MERGE = 3;

	private static final Border NOT_SELECTED = BorderFactory.createEmptyBorder(1, 1, 1, 1);
	private static final Border SELECTED = BorderFactory.createLineBorder(Color.green.darker());
	
	private Map<Component, Action> actionMap;

	private Map<String, Map<String, LpHvDirekthilfeDto>> serverTexte;
	private Map<String, Map<String, LpHvDirekthilfeDto>> dateiTexte;
	private Map<String, Map<String, LpHvDirekthilfeDto>> mergeTexte;
	
	private JCheckBox changedOnly;
	private JList<ListEntry> aenderungenList;
	private JTextArea textServer;
	private JTextArea textDatei;
	private JTextArea textMerged;
	private JScrollPane scrollServer;
	private JScrollPane scrollDatei;
	private JScrollPane scrollMerged;

	private JLabel labelServer;
	private JLabel labelDatei;

	private JRadioButton rbServer;
	private JRadioButton rbDatei;
	private JRadioButton rbMerged;
	
	private JButton datei;
	
	private JTextField dateiPfad;
	
	private JButton pushToServer;
	private JButton exportieren;
	
	private JLabel statisticsLabel;
	
	private JPanel panelMain;
	
	private File file;
	
	private ListEntry selectedEntry;
	
	private Statistics statistics = new Statistics();

	private TreeSet<ListEntry> cachedList;

	@Override
	public String getKategorie() {
		return KATEGORIE_ALLGEMEIN;
	}

	@Override
	public String getName() {
		return "Direkthilfe";
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getBeschreibung() {
		return "Exportieren, Importieren und Mergen von HV-Direkthilfe Dateien vom Typ *.hvdh.";
	}

	@Override
	public JPanel getPanel() {
		if(panelMain != null) return panelMain;

		JPanel leftPanel = new JPanel(new MigLayout("wrap 1, ins 0"));
		JPanel rightPanel = new JPanel(new MigLayout("wrap 2, ins 0"));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setDividerLocation(0.3);
		panelMain = new JPanel(new MigLayout("fill", "[fill]", "[fill]"));
		panelMain.add(splitPane);
		
		
		labelServer = new JLabel("Server:");
		aenderungenList = new JList<ListEntry>();
		labelDatei = new JLabel("Datei:");
		
		textServer = new JTextArea();
		textDatei = new JTextArea();
		textMerged = new JTextArea();

		scrollServer = new JScrollPane(textServer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollDatei = new JScrollPane(textDatei, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollMerged = new JScrollPane(textMerged, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		textServer.setEditable(false);
		textDatei.setEditable(false);

		textServer.setLineWrap(true);
		textDatei.setLineWrap(true);
		textMerged.setLineWrap(true);

		textServer.setWrapStyleWord(true);
		textDatei.setWrapStyleWord(true);
		textMerged.setWrapStyleWord(true);
		
		changedOnly = new JCheckBox("Nur Ge\u00E4nderte");
		
		rbServer = new JRadioButton();
		rbDatei = new JRadioButton();
		rbMerged = new JRadioButton();
		
		ButtonGroup group = new ButtonGroup();
		group.add(rbServer);
		group.add(rbDatei);
		group.add(rbMerged);
		
		aenderungenList.addListSelectionListener(this);
		aenderungenList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		aenderungenList.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1L;
			
			
		});

		textMerged.addFocusListener(this);
		
		changedOnly.addActionListener(this);
		
		rbServer.addActionListener(this);
		rbDatei.addActionListener(this);
		rbMerged.addActionListener(this);
		
		leftPanel.add(changedOnly);
		leftPanel.add(new JScrollPane(aenderungenList), "grow, push");
		
		rightPanel.add(labelServer, "wrap");
		
		rightPanel.add(scrollServer, "grow, push, h 0:0:max");
		rightPanel.add(rbServer);
		
		rightPanel.add(labelDatei, "wrap");
		
		rightPanel.add(scrollDatei, "grow, push, h 0:0:max");
		rightPanel.add(rbDatei);
		
		rightPanel.add(new JLabel("Merge:"), "wrap");
		
		rightPanel.add(scrollMerged, "grow, push, h 0:0:max");
		rightPanel.add(rbMerged);
		
		JPanel south = new JPanel(new MigLayout("wrap 3", "[20%, fill|50%, fill]push[20%,fill]"));
		
		datei = new JButton("Datei");
		dateiPfad = new JTextField();
		pushToServer = new JButton("push to server");
		exportieren = new JButton("Exportiere Servertexte");
		statisticsLabel = new JLabel();

		dateiPfad.setEditable(false);
		datei.addActionListener(this);
		pushToServer.addActionListener(this);
		exportieren.addActionListener(this);
		
		south.add(datei);
		south.add(dateiPfad);
		south.add(pushToServer);
		
		south.add(statisticsLabel, "span 2");
		south.add(exportieren);
		
		panelMain.add(south, "south");
		
		actionMap = new HashMap<Component, PflegeHvDirekthilfe.Action>();
		
		actionMap.put(changedOnly, new ActionChangedOnly());

		actionMap.put(rbServer, new ActionChoise());
		actionMap.put(rbDatei, new ActionChoise());
		actionMap.put(rbMerged, new ActionChoise());
		
		actionMap.put(datei, new ActionDateiAuswahl());
		actionMap.put(pushToServer, new ActionPushToServer());
		actionMap.put(exportieren, new ActionExport());
		
		actionMap.put(textMerged, new ActionApplyMergeText());
		
		HelperClient.setComponentNames(this);
		return panelMain;
	}
	
	private Map<String, Map<String, LpHvDirekthilfeDto>> getDateiTexte() {
		if(dateiTexte == null)
			dateiTexte = new HashMap<String, Map<String,LpHvDirekthilfeDto>>();
		return dateiTexte;
	}
	
	private Map<String, Map<String, LpHvDirekthilfeDto>> getServerTexte() {
		if(serverTexte == null)
			serverTexte = new HashMap<String, Map<String,LpHvDirekthilfeDto>>();
		return serverTexte;
	}
	
	private Map<String, Map<String, LpHvDirekthilfeDto>> getMergeTexte() {
		if(mergeTexte == null)
			mergeTexte = new HashMap<String, Map<String,LpHvDirekthilfeDto>>();
		return mergeTexte;
	}
	
	private ListEntry getSelectedListEntry () {
		if(aenderungenList.getSelectedValue() == null) return null;
		return aenderungenList.getSelectedValue();
	}
	
	private List<ListEntry> findDifferences() {
		List<ListEntry> list = new ArrayList<ListEntry>();

		list.addAll(getNotExistingInSecondMap(getDateiTexte(), getServerTexte(), CHOICE_DATEI));
		list.addAll(getNotExistingInSecondMap(getServerTexte(), getDateiTexte(), CHOICE_SERVER));
		
		list.addAll(getChanges(getServerTexte(), getDateiTexte()));
		return list;
	}
	
	private LpHvDirekthilfeDto getTextFromMap(String token, String locale, Map<String, Map<String, LpHvDirekthilfeDto>> map) {
		Map<String, LpHvDirekthilfeDto> localeText = map.get(token);
		if(localeText == null) return null;
		return localeText.get(locale);
	}
	
	private void putMergeText(String token, String locale, String text) {
		Map<String, LpHvDirekthilfeDto> localeText = getMergeTexte().get(token);
		if(localeText == null) {
			localeText = new HashMap<String, LpHvDirekthilfeDto>();
			getMergeTexte().put(token, localeText);
		}
		LpHvDirekthilfeDto hvdh = new LpHvDirekthilfeDto(null, token, text,
				new Timestamp(System.currentTimeMillis()), locale);
		localeText.put(locale, hvdh);
	}
	
	private List<ListEntry> getChanges(Map<String, Map<String, LpHvDirekthilfeDto>> serverMap,
			Map<String, Map<String, LpHvDirekthilfeDto>> dateiMap) {
		List<ListEntry> list = new ArrayList<PflegeHvDirekthilfe.ListEntry>();
		
		for(String token : serverMap.keySet()) {
			Map<String, LpHvDirekthilfeDto> localeText2 = dateiMap.get(token);
			if(localeText2 == null) continue;
			Map<String, LpHvDirekthilfeDto> localeText1 = serverMap.get(token);
			for(String locale : localeText1.keySet()) {
				LpHvDirekthilfeDto text2 = localeText2.get(locale);
				if(text2 == null) continue;
				if(text2.getcText() == null) continue;
				LpHvDirekthilfeDto text1 = localeText1.get(locale);
				if(text2.getcText().equals(text1.getcText())) continue;
				int choice = text1.gettAendern().after(text2.gettAendern())
						? CHOICE_SERVER : CHOICE_DATEI;
				list.add(new ListEntry(token, locale, choice, true));
			}
			
		}
		return list;
	}
	
	private List<ListEntry> getNotExistingInSecondMap(Map<String, Map<String, LpHvDirekthilfeDto>> m1,
			Map<String, Map<String, LpHvDirekthilfeDto>> m2, int presetChoice) {
		List<ListEntry> list = new ArrayList<PflegeHvDirekthilfe.ListEntry>();
		
		for(Map.Entry<String, Map<String, LpHvDirekthilfeDto>> tokenLocale1 : m1.entrySet()) {
			
			for(Map.Entry<String, LpHvDirekthilfeDto> locale : tokenLocale1.getValue().entrySet()) {
				Map<String, LpHvDirekthilfeDto> tokenLocale2 = m2.get(tokenLocale1.getKey());
				
				if(tokenLocale2 == null) {
					// in der Zweiten Map gibt es den Token nicht
					list.add(new ListEntry(tokenLocale1.getKey(), locale.getKey(), presetChoice, false));
				} else if(tokenLocale2.get(locale.getKey()) == null) {
					// in der Zweiten Map gibt es den Token, aber die Locale nicht
					list.add(new ListEntry(tokenLocale1.getKey(), locale.getKey(), presetChoice, false));
				}
			}
		}
		return list;
	}
	
	private Map<String, Map<String, LpHvDirekthilfeDto>> loadDateiTexte() {
		if(file == null) {
			getDateiTexte().clear();
			return getDateiTexte();
		}
		try {
			List<LpHvDirekthilfeDto> list = objectMapper.readValue(file, new TypeReference<List<LpHvDirekthilfeDto>>(){});
			loadTexteIntoMap(getDateiTexte(), list);
		} catch (Exception e) {
			logger.error("Fehler beim lesen der *.hvdh Datei", e);
			JOptionPane.showMessageDialog(panelMain, e.getMessage());
			getDateiTexte().clear();
		}
		return getDateiTexte();
		
		
	}
	private Map<String, Map<String, LpHvDirekthilfeDto>> loadServerTexte() {
		try {
			List<LpHvDirekthilfeDto> list = DelegateFactory.getInstance().getSystemDelegate().getAllHvDirekthilfeDtos();
			
			loadTexteIntoMap(getServerTexte(), list);
			return getServerTexte();
		} catch (Throwable e) {
			getServerTexte().clear();
			throw new RuntimeException(e);
		}
	}
	
	private void loadTexteIntoMap(Map<String, Map<String, LpHvDirekthilfeDto>> map,
			List<LpHvDirekthilfeDto> list) {
		map.clear();
		for(LpHvDirekthilfeDto hvdh : list) {
			Map<String,LpHvDirekthilfeDto> textByLocale = map.get(hvdh.getcToken());
			if(textByLocale == null) {
				textByLocale = new HashMap<String, LpHvDirekthilfeDto>();
				map.put(hvdh.getcToken(), textByLocale);
			}

			LpHvDirekthilfeDto dto = textByLocale.get(hvdh.getcLocale());
			if(dto != null) {
				JOptionPane.showMessageDialog(panelMain, "Die Datens\u00E4tze "
						+ dto.getiId() + "," + dto.getcToken() + "," + dto.getcLocale() + " und "
						+ hvdh.getiId() + "," + hvdh.getcToken() + "," + hvdh.getcLocale() + " verletzen das unique-key constraint!");
			}
			textByLocale.put(hvdh.getcLocale(), hvdh);
		}
		
	}
	
	private void delegateAction(EventObject event) {
		Action a = actionMap.get(event.getSource());
		if(a != null) a.doAction(event);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		refreshComponentState();
		ListEntry entry = getSelectedListEntry();
		if(entry == null) {
			textDatei.setText(null);
			textServer.setText(null);
			textMerged.setText(null);
			labelDatei.setText("Datei:");
			labelServer.setText("Server:");
			return;
		}
		if(selectedEntry != null)
			putMergeText(selectedEntry.getcToken(), selectedEntry.getcLocale(), textMerged.getText());
		selectedEntry = entry;
		LpHvDirekthilfeDto serverHvdh = getTextFromMap(entry.getcToken(), entry.getcLocale(), getServerTexte());
		LpHvDirekthilfeDto dateiHvdh = getTextFromMap(entry.getcToken(), entry.getcLocale(), getDateiTexte());
		LpHvDirekthilfeDto mergedHvdh = getTextFromMap(entry.getcToken(), entry.getcLocale(), getMergeTexte());
		
		DateFormat df = new SimpleDateFormat();
		labelDatei.setText(dateiHvdh == null ? "In Datei nicht vorhanden" : ("Datei: ge\u00E4ndert am " + df.format(dateiHvdh.gettAendern())));
		labelServer.setText(serverHvdh == null ? "Auf Server nicht vorhanden" : ("Server: ge\u00E4ndert am " + df.format(serverHvdh.gettAendern())));
		
		textServer.setText(serverHvdh == null ? null : serverHvdh.getcText());
		textDatei.setText(dateiHvdh == null ? null : dateiHvdh.getcText());
		textMerged.setText(mergedHvdh == null ? null : mergedHvdh.getcText());
		
		if(entry.getChoice() == CHOICE_DATEI)
			rbDatei.setSelected(true);
		else if(entry.getChoice() == CHOICE_MERGE)
			rbMerged.setSelected(true);
		else if(entry.getChoice() == CHOICE_SERVER)
			rbServer.setSelected(true);
		refreshTextFieldBorders();
	}
	
	private void refreshTextFieldBorders() {
		int choice = selectedEntry == null ? -1 : selectedEntry.getChoice();
		scrollDatei.setBorder(choice == CHOICE_DATEI ? SELECTED : NOT_SELECTED);
		scrollServer.setBorder(choice == CHOICE_SERVER ? SELECTED : NOT_SELECTED);
		scrollMerged.setBorder(choice == CHOICE_MERGE ? SELECTED : NOT_SELECTED);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		delegateAction(event);
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		if(arg0.getSource() == textMerged) {
			rbMerged.setSelected(true);
			getSelectedListEntry().setChoice(CHOICE_MERGE);
			refreshTextFieldBorders();
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		delegateAction(arg0);
	}
	
	private class ActionApplyMergeText implements Action {
		@Override
		public void doAction(EventObject event) {
			if(selectedEntry != null)
				putMergeText(selectedEntry.getcToken(), selectedEntry.getcLocale(), textMerged.getText());
		}
	}

	private class ActionDateiAuswahl implements Action {
		@Override
		public void doAction(EventObject event) {
			List<File> files = HelperClient.showOpenFileDialog(file, panelMain, false, FILE_EXT);
			if(files != null && files.size() == 1)
				file = files.get(0);
			else file = null;
			dateiPfad.setText(file == null ? "" : file.getAbsolutePath());
			statisticsLabel.setText("");
			loadTexte();
			
			if(file != null)
				lookForAenderungen();
			else 
				aenderungenList.setModel(new DefaultListModel<ListEntry>());
		}
	}
	
	private void loadTexte() {
		getMergeTexte().clear();
		loadServerTexte();
		loadDateiTexte();
	}
	
	private void lookForAenderungen() {
		
		cachedList = new TreeSet<ListEntry>(findDifferences());
		updateAenderungenList();
	}
	
	private void updateAenderungenList() {
		DefaultListModel<ListEntry> model = new DefaultListModel<ListEntry>();
		for(ListEntry entry : cachedList){
			if(changedOnly.isSelected() && !entry.isChanged())
				continue;
			model.addElement(entry);
		}
		aenderungenList.setModel(model);
	}

	private class ActionPushToServer implements Action {
		@Override
		public void doAction(EventObject event) {

			if(selectedEntry != null)
				putMergeText(selectedEntry.getcToken(), selectedEntry.getcLocale(), textMerged.getText());
			
			statistics = new Statistics();
			
			for(ListEntry entry : cachedList) {
				Map<String, Map<String, LpHvDirekthilfeDto>> map = null;
				statistics.aenderungen++;
				if(entry.getChoice() == CHOICE_DATEI) {
					map = getDateiTexte();
					statistics.datei++;
				} else if(entry.getChoice() == CHOICE_SERVER) {
					map = getServerTexte();
					statistics.server++;
					continue;
				} else if(entry.getChoice() == CHOICE_MERGE) {
					map = getMergeTexte();
					statistics.merge++;
				} else {
					logger.warn("entry " + entry.toString() + " has invalid choice: " + entry.getChoice());
					continue;
				}
				try {
					LpHvDirekthilfeDto hvdh = getTextFromMap(entry.getcToken(), entry.getcLocale(), map);
					if(hvdh == null)
						hvdh = new LpHvDirekthilfeDto(null, entry.getcToken(), null, null, entry.getcLocale());
					DelegateFactory.getInstance().getSystemDelegate().putHvDirekthilfeText(hvdh);
					statistics.pushed++;
				} catch (Throwable e) {
					JOptionPane.showMessageDialog(panelMain, e.getMessage());
					logger.error("Fehler beim Pushen der Texte zum Server");
					statistics.failed++;
				}
			}
			loadServerTexte();
			lookForAenderungen();
			
			statistics.publish();
		}
	}
	
	private class Statistics {
		int aenderungen; 
		int datei;
		int server;
		int merge;
		
		int pushed;
		int failed;
		
		void publish() {
			StringBuffer sb = new StringBuffer();
			sb.append("Server: ")
			.append(server)
			.append(", Datei: ")
			.append(datei)
			.append(", Merged: ")
			.append(merge)
			.append(", Gesamt: ")
			.append(aenderungen)
			.append(";  ")
			.append(pushed)
			.append(" \u00C4nderungen gepushed, ")
			.append(failed)
			.append(" fehlgeschlagen");
			statisticsLabel.setText(sb.toString());
		}
	}

	private class ActionExport implements Action {
		@Override
		public void doAction(EventObject event) {
			List<LpHvDirekthilfeDto> list;
			try {
				list = DelegateFactory.getInstance().getSystemDelegate().getAllHvDirekthilfeDtos();
				
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(panelMain, e.getMessage());
				logger.error("Fehler beim Laden der Texte vom Server");
				return;
			}
			
			File temp;
			try {
				temp = File.createTempFile("direkthilfe", FILE_EXT);
				objectMapper.writeValue(temp, list);
				if(HelperClient.showSaveFileDialog(temp, null, panelMain, FILE_EXT) != null) {
					JOptionPane.showMessageDialog(panelMain, list.size() + " Datens\u00E4tze erfolgreich gespeichert!");
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(panelMain, e.getMessage());
				logger.error("Fehler beim Laden der Texte vom Server");
			}
		}
	}
	
	private class ActionChangedOnly implements Action {
		@Override
		public void doAction(EventObject event) {
			updateAenderungenList();
		}
	}
	
	private class ActionChoise implements Action {
		@Override
		public void doAction(EventObject event) {
			if(event.getSource() == rbServer) {
				getSelectedListEntry().setChoice(CHOICE_SERVER);
			} else if(event.getSource() == rbDatei) {
				getSelectedListEntry().setChoice(CHOICE_DATEI);
			} else if(event.getSource() == rbMerged) {
				getSelectedListEntry().setChoice(CHOICE_MERGE);
			} 
			refreshTextFieldBorders();
		}
	}
	
	private void refreshComponentState() {
		boolean hasItem = getSelectedListEntry() != null;
		rbServer.setEnabled(hasItem);
		rbDatei.setEnabled(hasItem);
		rbMerged.setEnabled(hasItem);
		pushToServer.setEnabled(hasItem);
	}
	private interface Action {
		void doAction(EventObject event);
	}
	
	private class ListEntry extends LpHvDirekthilfeDto implements Comparable<ListEntry> {
		
		private static final long serialVersionUID = -2324584145082848579L;
		private int choice;
		private boolean changed = true;
		
		public ListEntry(String token, String locale, int choice, boolean changed) {
			setcToken(token);
			setcLocale(locale);
			setChoice(choice);
			setChanged(changed);
		}
		
		public void setChoice(int choice) {
			this.choice = choice;
		}
		
		public int getChoice() {
			return choice;
		}
		
		@Override
		public String toString() {
			return getcToken() + ", " + getcLocale();
		}

		@Override
		public int compareTo(ListEntry entry) {
			return toString().compareTo(entry.toString());
		}

		public boolean isChanged() {
			return changed;
		}

		public void setChanged(boolean changed) {
			this.changed = changed;
		}
	}
	
	@Override
	public boolean isStartable() {return false;}

	@Override
	public void run() {}

	@Override
	public void cancel() {}

	@Override
	public boolean isRunning() {return false;}

	@Override
	public void init() {
	}

	@Override
	public void eventYouAreSelected() {
		refreshComponentState();}

	@Override
	public boolean supportsProgressBar() {return false;}

	@Override
	public void addPflegeEventListener(PflegeEventListener listener) {}

	@Override
	public void removeAllPflegeEventListeners() {}
}
