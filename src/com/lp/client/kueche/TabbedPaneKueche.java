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
package com.lp.client.kueche;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.20 $
 */
public class TabbedPaneKueche extends TabbedPane implements ICopyPaste {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQuerySpeiseplan = null;
	private PanelBasis panelSplitSpeiseplan = null;
	private PanelSpeiseplan panelBottomSpeiseplan = null;

	public String selektierteFertigungsgruppeIId = null;

	private JMenu menuAnsicht = null;

	private ButtonGroup bgAnsicht = new ButtonGroup();

	JRadioButtonMenuItem[] menuItemsAnsicht = null;

	private final static String MENU_ANSICHT_FILTERS = "MENU_ANSICHT_FILTERS";

	private static int IDX_PANEL_SPEISEPLAN = 0;

	private final String MENU_BEARBEITEN_STIFTEEINLESEN = "MENU_BEARBEITEN_STIFTEEINLESEN";
	private final String MENU_BEARBEITEN_OSCAR_EINLESEN = "MENU_BEARBEITEN_OSCAR_EINLESEN";
	private final String MENU_BEARBEITEN_ADS3000_EINLESEN = "MENU_BEARBEITEN_ADS3000_EINLESEN";

	private final String MENU_BEARBEITEN_ALLES_EINLESEN = "MENU_BEARBEITEN_ALLES_EINLESEN";

	private final String MENU_BEARBEITEN_SOFORTVERBRAUCH = "MENU_BEARBEITEN_SOFORTVERBRAUCH";

	private final String MENU_JOURNAL_KUECHENAUSWERTUNG1 = "MENU_JOURNAL_KUECHENAUSWERTUNG1";
	private final String MENU_JOURNAL_KUECHENAUSWERTUNG2 = "MENU_JOURNAL_KUECHENAUSWERTUNG2";
	private final String MENU_JOURNAL_DECKUNGSBEITRAG = "MENU_JOURNAL_DECKUNGSBEITRAG";

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneKueche(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"kue.modulname"));

		jbInit();
		initComponents();
	}

	public InternalFrameStueckliste getInternalFrameStueckliste() {
		return (InternalFrameStueckliste) getInternalFrame();
	}

	public PanelQuery getPanelQuerySpeiseplan() {
		return panelQuerySpeiseplan;
	}

	public void refreshQPSpeisplan(Date dateI, boolean isEditModeI)
			throws Throwable {

		if (isEditModeI) {
			panelQuerySpeiseplan.updateButtons(new LockStateValue(
					PanelBasis.LOCK_FOR_NEW));
		} else {
			createSpeispelan(dateI);
			panelQuerySpeiseplan.eventYouAreSelected(false);
			panelQuerySpeiseplan.updateButtons(new LockStateValue(
					PanelBasis.LOCK_IS_NOT_LOCKED));
		}

	}

	private void createSpeispelan(Date dateI) throws Throwable {
		if (panelSplitSpeiseplan == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };
			panelQuerySpeiseplan = new PanelQuery(null, KuecheFilterFactory
					.getInstance().createFKSpeiseplanZuDatum(dateI,
							selektierteFertigungsgruppeIId),
					QueryParameters.UC_ID_SPEISEPLAN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.title.panel.auswahl"),
					true);

			panelBottomSpeiseplan = new PanelSpeiseplan(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("kue.speiseplan"),
					panelQuerySpeiseplan.getSelectedId());
			panelSplitSpeiseplan = new PanelSplit(getInternalFrame(),
					panelBottomSpeiseplan, panelQuerySpeiseplan, 250);

			panelQuerySpeiseplan.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);
			panelQuerySpeiseplan.setMultipleRowSelectionEnabled(true);
			setComponentAt(IDX_PANEL_SPEISEPLAN, panelSplitSpeiseplan);
		}
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("kue.speiseplan"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("kue.speiseplan"),
				IDX_PANEL_SPEISEPLAN);
		createSpeispelan(new java.sql.Date(System.currentTimeMillis()));

		// Itemevents an MEIN Detailpanel senden kann.
		refreshTitle();
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQuerySpeiseplan) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					getInternalFrame().setKeyWasForLockMe(key.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}

				panelBottomSpeiseplan.setKeyWhenDetailPanel(key);
				panelBottomSpeiseplan.eventYouAreSelected(false);
				panelQuerySpeiseplan.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitSpeiseplan.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomSpeiseplan) {
				panelQuerySpeiseplan.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQuerySpeiseplan) {
				panelBottomSpeiseplan.eventActionNew(e, true, false);
				panelBottomSpeiseplan.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomSpeiseplan) {
				panelSplitSpeiseplan.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomSpeiseplan) {
				Object oKey = panelBottomSpeiseplan.getKeyWhenDetailPanel();
				panelQuerySpeiseplan.eventYouAreSelected(false);
				panelQuerySpeiseplan.setSelectedId(oKey);
				panelSplitSpeiseplan.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomSpeiseplan) {
				Object oKey = panelQuerySpeiseplan.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomSpeiseplan.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySpeiseplan
							.getId2SelectAfterDelete();
					panelQuerySpeiseplan.setSelectedId(oNaechster);
				}
				panelSplitSpeiseplan.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (e.getSource() == panelQuerySpeiseplan) {
				copyHV();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				if (e.getSource() == panelQuerySpeiseplan) {
					einfuegenHV();
				}
			}
		}

	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.grunddaten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	public PanelBasis getPanelDetailSpeiseplan() {
		return panelBottomSpeiseplan;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_SPEISEPLAN) {
			createSpeispelan(new java.sql.Date(System.currentTimeMillis()));
			panelSplitSpeiseplan.eventYouAreSelected(false);
			panelQuerySpeiseplan.updateButtons();
		}

	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_BEARBEITEN_STIFTEEINLESEN)) {
			DialogKdc100 d = new DialogKdc100();

			getInternalFrame()
					.showPanelDialog(
							new PanelDialogStifteeinlesen(getInternalFrame(),
									d.alStifte, LPMain.getInstance()
											.getTextRespectUISPr(
													"kue.stifteeinlesen")));

		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_SOFORTVERBRAUCH)) {
			DelegateFactory.getInstance().getLagerDelegate()
					.sofortverbrauchAllerArtikelAbbuchen();

		} else if (e.getActionCommand()
				.equals(MENU_BEARBEITEN_ADS3000_EINLESEN)) {
			// Dateiauswahldialog
			JFileChooser chooser = new JFileChooser();

			chooser.setMultiSelectionEnabled(false);
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();

				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				ArrayList al = new ArrayList();

				String zeile = br.readLine();
				while (zeile != null) {

					al.add(zeile);
					zeile = br.readLine();
				}
				String[] returnArray = new String[al.size()];
				DelegateFactory.getInstance().getKuecheDelegate()
						.importiereKassenfileGaestehaus(al);

			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_OSCAR_EINLESEN)) {
			// Dateiauswahldialog
			JFileChooser chooser = new JFileChooser();

			chooser.setMultiSelectionEnabled(false);
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();

				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				ArrayList al = new ArrayList();

				String zeile = br.readLine();
				while (zeile != null) {

					al.add(zeile);
					zeile = br.readLine();
				}
				String[] returnArray = new String[al.size()];
				DelegateFactory.getInstance().getKuecheDelegate()
						.importiereKassenfileOscar(al);

			}

		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_ALLES_EINLESEN)) {
			DelegateFactory.getInstance().getKuecheDelegate()
					.importiereAlleKassenfiles();
		} else if (e.getActionCommand().startsWith(MENU_ANSICHT_FILTERS)) {

			selektierteFertigungsgruppeIId = null;

			if (e.getActionCommand().equals(MENU_ANSICHT_FILTERS)) {
				FilterKriterium[] fk = KuecheFilterFactory.getInstance()
						.createFKSpeiseplanZuDatum(
								panelBottomSpeiseplan.wdfDatum.getDate(), null);
				panelQuerySpeiseplan.setDefaultFilter(fk);
				panelQuerySpeiseplan.eventYouAreSelected(false);

			} else {
				selektierteFertigungsgruppeIId = e.getActionCommand()
						.replaceAll(MENU_ANSICHT_FILTERS, "");
				FilterKriterium[] fk = KuecheFilterFactory.getInstance()
						.createFKSpeiseplanZuDatum(
								panelBottomSpeiseplan.wdfDatum.getDate(),
								selektierteFertigungsgruppeIId);
				panelQuerySpeiseplan.setDefaultFilter(fk);
				panelQuerySpeiseplan.eventYouAreSelected(false);
			}

		} else if (e.getActionCommand().equals(MENU_JOURNAL_KUECHENAUSWERTUNG1)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"kue.kuechenauswertung1");
			getInternalFrame()
					.showReportKriterien(
							new ReportKuechenauswertung1(getInternalFrame(),
									add2Title));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_KUECHENAUSWERTUNG2)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"kue.kuechenauswertung2");
			getInternalFrame()
					.showReportKriterien(
							new ReportKuechenauswertung2(getInternalFrame(),
									add2Title));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_DECKUNGSBEITRAG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"kue.deckungsbeitragsanalyse");
			getInternalFrame().showReportKriterien(
					new ReportDeckungsbeitrag(getInternalFrame(), add2Title));
		}
	}

	public String getSelektierteFertigungsgruppeIId() {
		return selektierteFertigungsgruppeIId;
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu jmModul = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);

			// Menue Bearbeiten
			JMenu jmBearbeiten = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

			JMenuItem menuItemBearbeitenStifteEinlesen = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("kue.stifteeinlesen"));
			menuItemBearbeitenStifteEinlesen.addActionListener(this);
			menuItemBearbeitenStifteEinlesen
					.setActionCommand(MENU_BEARBEITEN_STIFTEEINLESEN);
			jmBearbeiten.add(menuItemBearbeitenStifteEinlesen, 0);

			jmBearbeiten.add(new JSeparator(), 1);

			JMenuItem menuItemBearbeitenADS3000Einlesen = new JMenuItem(
					"ADS3000 einlesen");
			menuItemBearbeitenADS3000Einlesen.addActionListener(this);
			menuItemBearbeitenADS3000Einlesen
					.setActionCommand(MENU_BEARBEITEN_ADS3000_EINLESEN);
			jmBearbeiten.add(menuItemBearbeitenADS3000Einlesen, 2);

			JMenuItem menuItemBearbeitenOscarEinlesen = new JMenuItem(
					"OSCAR einlesen");
			menuItemBearbeitenOscarEinlesen.addActionListener(this);
			menuItemBearbeitenOscarEinlesen
					.setActionCommand(MENU_BEARBEITEN_OSCAR_EINLESEN);
			jmBearbeiten.add(menuItemBearbeitenOscarEinlesen, 3);

			JMenuItem menuItemBearbeitenAllesEinlesen = new JMenuItem(
					"Alle Dateien einlesen");
			menuItemBearbeitenAllesEinlesen.addActionListener(this);
			menuItemBearbeitenAllesEinlesen
					.setActionCommand(MENU_BEARBEITEN_ALLES_EINLESEN);
			jmBearbeiten.add(menuItemBearbeitenAllesEinlesen, 4);

			jmBearbeiten.add(new JSeparator(), 5);
			JMenuItem menuItemSofortverbrauch = new JMenuItem(
					"Sofortverbrauch durchf\u00FChren");
			menuItemSofortverbrauch.addActionListener(this);
			menuItemSofortverbrauch
					.setActionCommand(MENU_BEARBEITEN_SOFORTVERBRAUCH);
			jmBearbeiten.add(menuItemSofortverbrauch, 6);

			JMenu journal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			WrapperMenuItem menuItemKuechenauswertung1 = new WrapperMenuItem(
					LPMain.getInstance().getTextRespectUISPr(
							"kue.kuechenauswertung1"),
					RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemKuechenauswertung1.addActionListener(this);
			menuItemKuechenauswertung1
					.setActionCommand(MENU_JOURNAL_KUECHENAUSWERTUNG1);
			journal.add(menuItemKuechenauswertung1);

			WrapperMenuItem menuItemKuechenauswertung2 = new WrapperMenuItem(
					LPMain.getInstance().getTextRespectUISPr(
							"kue.kuechenauswertung2"),
					RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemKuechenauswertung2.addActionListener(this);
			menuItemKuechenauswertung2
					.setActionCommand(MENU_JOURNAL_KUECHENAUSWERTUNG2);
			journal.add(menuItemKuechenauswertung2);

			WrapperMenuItem menuItemDeckungsbeitrag = new WrapperMenuItem(
					LPMain.getInstance().getTextRespectUISPr(
							"kue.deckungsbeitragsanalyse"),
					RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemDeckungsbeitrag.addActionListener(this);
			menuItemDeckungsbeitrag
					.setActionCommand(MENU_JOURNAL_DECKUNGSBEITRAG);
			journal.add(menuItemDeckungsbeitrag);

			FertigungsgruppeDto[] fertigungsgruppeDtos = DelegateFactory
					.getInstance().getStuecklisteDelegate()
					.fertigungsgruppeFindByMandantCNr();

			if (menuAnsicht == null && fertigungsgruppeDtos != null
					&& fertigungsgruppeDtos.length > 0) {
				menuAnsicht = new WrapperMenu("lp.ansicht", this);
				JRadioButtonMenuItem menuItemAlle = new JRadioButtonMenuItem(
						LPMain.getInstance().getTextRespectUISPr("lp.alle"),
						true);
				menuItemAlle.addActionListener(this);
				menuItemAlle.setActionCommand(MENU_ANSICHT_FILTERS);

				menuAnsicht.add(menuItemAlle);
				bgAnsicht.add(menuItemAlle);

				menuItemsAnsicht = new JRadioButtonMenuItem[fertigungsgruppeDtos.length];

				for (int i = 0; i < fertigungsgruppeDtos.length; i++) {
					JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(
							fertigungsgruppeDtos[i].getCBez());
					menuItem.addActionListener(this);
					menuItem.setActionCommand(MENU_ANSICHT_FILTERS
							+ fertigungsgruppeDtos[i].getIId());

					menuItemsAnsicht[i] = menuItem;
					bgAnsicht.add(menuItem);
					menuAnsicht.add(menuItem);
				}

				wrapperMenuBar.addJMenuItem(menuAnsicht);

			}
		}

		return wrapperMenuBar;
	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object aoIIdPosition[] = panelQuerySpeiseplan.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			SpeiseplanDto[] dtos = new SpeiseplanDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory
						.getInstance()
						.getKuecheDelegate()
						.speiseplanFindByPrimaryKey(
								(Integer) aoIIdPosition[i]);
			}

			LPMain.getInstance().getPasteBuffer()
					.writeObjectToPasteBuffer(dtos);

		}
	}


	public void einfuegenHV() throws ExceptionLP, ParserConfigurationException,
			SAXException, IOException, Throwable {
		Object o = LPMain.getInstance().getPasteBuffer()
				.readObjectFromPasteBuffer();
		
		//Es koennen nur SpeiseplanDtos verarbeitet werden
		if (o instanceof SpeiseplanDto[]) {
			SpeiseplanDto[] positionen=(SpeiseplanDto[])o;
			for(int i=0;i<positionen.length;i++){
				
				SpeiseplanDto spDto=positionen[i];
				spDto.setIId(null);
				spDto.setTDatum(Helper.cutTimestamp(panelBottomSpeiseplan.wdfDatum.getTimestamp()));
				
				DelegateFactory
				.getInstance()
				.getKuecheDelegate().createSpeiseplan(spDto);
			}
			
			panelQuerySpeiseplan.eventYouAreSelected(false);
			
		}
		

	}

	@Override
	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI)
			throws Throwable {
		// TODO Auto-generated method stub

	}

}
