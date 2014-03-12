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
package com.lp.client.finanz;


import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.util.csv.LPCSVReader;
import com.lp.util.csv.LPCSVWriter;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.IntrastatDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;


/**
 * <p> Diese Klasse kuemmert sich um die Tabs der Intrastat-Meldung</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004 - 2007</p>
 *
 * <p>Erstellung: Martin Bluehweis; 07.08.07</p>
 *
 * <p>@author $Author: adi $</p>
 *
 * @version not attributable Date $Date: 2010/07/01 13:39:09 $
 */
public class TabbedPaneIntrastat
    extends TabbedPane
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryIDEP = null;
  private PanelDialogKriterienIntrastat pdKriterienIntrastat = null;
  private PanelDialogKriterienIntrastat pdKriterienVorschau = null;

  private WarenverkehrsnummerDto wvkDto = null;

  private static final int IDX_IDEP_LISTE = 0;

  private final static String MENU_ACTION_INTRASTAT = "menu_action_intrastat";
  private final static String MENU_ACTION_VORSCHAU = "menu_action_vorschau";
  private final static String ACTION_SPECIAL_IMPORT_IDEP = "action_special_import_idep_" + PanelBasis.ALWAYSENABLED;
  private final static int WVKNR_BEZ_MAXIMALE_LAENGE = 1000;

  public TabbedPaneIntrastat(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getTextRespectUISPr("finanz.tab.unten.intrastat.title"));
    jbInit();
    initComponents();
  }


  public WarenverkehrsnummerDto getWarenverkehrsnummerDto() {
    return wvkDto;
  }


  public void setWarenverkehrsnummerDto(WarenverkehrsnummerDto wvkDto)
      throws Throwable {
    this.wvkDto = wvkDto;
    String sTitle = null;
    if (getWarenverkehrsnummerDto() != null) {
      sTitle = getWarenverkehrsnummerDto().getCNr();
    }
    else {
      sTitle = "";
    }
    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
  }


  private void jbInit()
      throws Throwable {
    // Tab 1: Exportlaeufe
    insertTab(LPMain.getTextRespectUISPr("finanz.tab.oben.wvk.title"),
              null,
              null,
              LPMain.getTextRespectUISPr("finanz.tab.oben.wvk.tooltip"),
              IDX_IDEP_LISTE);
    setSelectedComponent(getPanelQueryIDEP());
    // refresh
    getPanelQueryIDEP().eventYouAreSelected(false);
    // damit gleich einer selektiert ist
    ItemChangedEvent it = new ItemChangedEvent(getPanelQueryIDEP(),
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
   * @param e ItemChangedEvent
   * @throws Throwable
   */
  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {
    if (e.getID() == ItemChangedEvent.ITEM_CHANGED||
             e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryIDEP) {
        String key = (String) panelQueryIDEP.getSelectedId();
        if (key != null) {
          WarenverkehrsnummerDto warenverkehrsnummerDto = DelegateFactory.getInstance().
              getFinanzServiceDelegate().warenverkehrsnummerFindByPrimaryKey(key);
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_IDEP_LISTE, true);
          setWarenverkehrsnummerDto(warenverkehrsnummerDto);
        }
        else {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_IDEP_LISTE, false);
          setWarenverkehrsnummerDto(null);
        }
        panelQueryIDEP.updateButtons();
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
      if (e.getSource() == getPdKriterienIntrastat()) {
        generiereIntrastatmeldung();
      }
      if (e.getSource() == getPdKriterienVorschau()) {
        getInternalFrame().showReportKriterien(
            new ReportIntrastat(getInternalFrame(),
                                getPdKriterienVorschau().getVerfahren(),
                                getPdKriterienVorschau().getDVon(),
                                getPdKriterienVorschau().getDBis(),
                                getPdKriterienVorschau().getTTransportkosten(),
                                LPMain.getTextRespectUISPr(
            "finanz.tab.unten.intrastat.title")));
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
      if (e.getSource() == getPanelQueryIDEP()) {
        if (((PanelQuery)e.getSource()).getAspect().equals(ACTION_SPECIAL_IMPORT_IDEP)) {
          boolean bAnswer = DialogFactory.showModalJaNeinDialog(
              getInternalFrame(),
              LPMain.getTextRespectUISPr("finanz.frage.warenverkehrsnummern.importieren"),
              LPMain.getTextRespectUISPr("lp.frage"));
          if (bAnswer) {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileFilter()
            {
              public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith("txt") || f.isDirectory();
              }
              public String getDescription() {
                return LPMain.getTextRespectUISPr("lp.textdateien");
              }
            });
            int returnVal = fc.showOpenDialog(LPMain.getInstance().getDesktop());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
              File file = fc.getSelectedFile();
              // Tab-getrenntes File einlesen.
              LPCSVReader reader = new LPCSVReader(new FileReader(file), (char)KeyEvent.VK_TAB,null);
              String [] sLine;
              ArrayList<WarenverkehrsnummerDto> list = new ArrayList<WarenverkehrsnummerDto>();
              do {
                // zeilenweise einlesen.
                sLine = reader.readNext();
                if (sLine != null) {
                  String sWVK = sLine[0].trim();
                  // jede Zeile, die mit einer gueltigen WVK Nr. beginnt.
                  boolean bWVKFound = Helper.checkWarenverkehrsnummer(sWVK);
                  if (bWVKFound) {
                    WarenverkehrsnummerDto dto = new WarenverkehrsnummerDto();
                    dto.setCNr(sWVK);

                    //200800408 WH: Wenn CBez laenger als WVKNR_BEZ_MAXIMALE_LAENGE Zeichen ist, dann abschneiden.
                    if(sLine[1] != null && (sLine[1].length() > WVKNR_BEZ_MAXIMALE_LAENGE) ){
                      sLine[1] = sLine[1].substring(0,WVKNR_BEZ_MAXIMALE_LAENGE);
                    }
                    dto.setCBez(sLine[1]);
                    list.add(dto);
                  }
                }
              }
              while (sLine != null);
              // gesammelte Eintraege in Array umwandeln und speichern speichern.
              WarenverkehrsnummerDto[] dtos = new WarenverkehrsnummerDto[list.size()];
              dtos = list.toArray(dtos);
              DelegateFactory.getInstance().getFinanzServiceDelegate().
                  createWarenverkehrsnummernUndLoescheAlte(dtos);
              // Panel aktualisieren
              panelQueryIDEP.eventYouAreSelected(false);
            }
            else {
              // keine auswahl
            }
          }
        }
      }
    }
  }


  private void generiereIntrastatmeldung()
      throws Throwable {
    ParametermandantDto parameter = null;
    if (getPdKriterienIntrastat().getVerfahren().equals(FinanzReportFac.
        INTRASTAT_VERFAHREN_VERSAND)) {
      parameter = DelegateFactory.getInstance().getParameterDelegate().
          getMandantparameter(LPMain.getTheClient().getMandant(),
                              ParameterFac.KATEGORIE_FINANZ,
                              ParameterFac.PARAMETER_INTRASTAT_EXPORTZIEL_VERSAND);
    }
    else if (getPdKriterienIntrastat().getVerfahren().equals(FinanzReportFac.
        INTRASTAT_VERFAHREN_WARENEINGANG)) {
      parameter = DelegateFactory.getInstance().getParameterDelegate().
          getMandantparameter(LPMain.getTheClient().getMandant(),
                              ParameterFac.KATEGORIE_FINANZ,
                              ParameterFac.PARAMETER_INTRASTAT_EXPORTZIEL_EINGANG);
    }
    try {
      ArrayList<IntrastatDto> data = DelegateFactory.getInstance().
          getFibuExportDelegate().exportiereIntrastatmeldung(
          getPdKriterienIntrastat().getVerfahren(),
          getPdKriterienIntrastat().getDVon(),
          getPdKriterienIntrastat().getDBis(),
          getPdKriterienIntrastat().getTTransportkosten());
      if (data != null &&
          data.size() == 0) {
        DialogFactory.showModalDialog(
            LPMain.getTextRespectUISPr("lp.error"),
            LPMain.getTextRespectUISPr("fb.export.keinebelegezuexportieren"));
      }
      else if (data != null &&
               data.size() > 0) {
        File file = new File(parameter.getCWert());
        LPCSVWriter writer = new LPCSVWriter(new FileWriter(file), ';',
                                             LPCSVWriter.NO_QUOTE_CHARACTER);

        writer.writeNext(new String[] {
                         "WVK_NR",
                         "UID",
                         "URSPRUNG",
                         "BEISTELL",
                         "MENGE",
                         "ART",
                         "WERT",
                         "STATISTWRT",
                         "STAT_WERT",
                         "GEWICHT",
                         "TEXT",
                         "VERFAHREN",
                         "VERKEHR"});
        for (IntrastatDto iDto : data) {
          String[] zeile = new String[13];
          if(iDto.getUid() != ""){
        	  zeile[0] = iDto.getWarenverkehrsnummerDto().getCNr();
        	  zeile[1] = Helper.getAllStartCharacters(iDto.getUid());
        	  zeile[2] = iDto.getUrsprung();
        	  zeile[3] = iDto.getBeistell();
        	  zeile[4] = Helper.formatZahl(iDto.getMenge(),
        			  FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_MENGE,
        			  LPMain.getTheClient().getLocUi());
        	  zeile[5] = ""; // ART ... keine Zuordnung
        	  zeile[6] = Helper.formatZahl(iDto.getWert(),
        			  FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
        			  LPMain.getTheClient().getLocUi());
        	  zeile[7] = Helper.formatZahl(iDto.getStatistischerWert(),
        			  FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
        			  LPMain.getTheClient().getLocUi());
        	  zeile[8] = ""; // STAT_WERT ... keine Zuordnung
        	  zeile[9] = Helper.formatZahl(iDto.getGewichtInKg(),
        			  FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_GEWICHT,
        			  LPMain.getTheClient().getLocUi());
        	  zeile[10] = Helper.cutString(iDto.getWarenverkehrsnummerDto().getCBez(), 78);
        	  zeile[11] = getPdKriterienIntrastat().getVerfahren();
        	  zeile[12] = iDto.getVerkehrszweig() + "";
        	  writer.writeNext(zeile);
          }
        }
        writer.close();
        DialogFactory.showModalDialog(
            LPMain.getTextRespectUISPr("lp.hinweis"),
            LPMain.getTextRespectUISPr("lp.hint.dateiwurdegespeichert") + " (" +
            file.getAbsolutePath() + ") ");

      }
    }
    catch (IOException ex) {
      DialogFactory.showModalDialog(
          LPMain.getTextRespectUISPr("lp.hinweis"),
          LPMain.getTextRespectUISPr("lp.error.dateikannnichterzeugtwerden") + ": " +
          parameter.getCWert());
    }
    catch (Throwable t) {
      handleExportExceptions(t);
    }
  }


  private void handleExportExceptions(Throwable t)
      throws Throwable {
    if (t instanceof ExceptionLP) {
      ExceptionLP e = (ExceptionLP) t;
      ArrayList<?> a = e.getAlInfoForTheClient();
      StringBuffer sb = new StringBuffer();
      if (a != null && !a.isEmpty()) {
        for (Iterator<?> iter = a.iterator(); iter.hasNext(); ) {
          String item = (String) iter.next();
          sb.append(item + "\n");
        }
      }
      String sToken = null;
      switch (e.getICode()) {
        case EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_KEINE_WVK_NR: {
          sToken = "finanz.error.wvknichtdefiniert";
        }
        break;
        case EJBExceptionLP.
            FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT: {
          sToken = "finanz.error.belegistnochnichtaktiviert";
        }
        break;
        default: {
          throw e;
        }
      }
      String sText = LPMain.getTextRespectUISPr(sToken);
      DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
                                           sText + "\n" + sb.toString());
    }
    else {
      throw t;
    }
  }



  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_IDEP_LISTE) {
      panelQueryIDEP.eventYouAreSelected(false);
    }
  }


  protected void lPActionEvent(java.awt.event.ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(MENU_ACTION_INTRASTAT)) {
      getInternalFrame().showPanelDialog(getPdKriterienIntrastat());
    }
    else if (e.getActionCommand().equals(MENU_ACTION_VORSCHAU)) {
      getInternalFrame().showPanelDialog(getPdKriterienVorschau());
    }
  }


  private PanelQuery getPanelQueryIDEP()
      throws Throwable {
    if (panelQueryIDEP == null) {
      String[] aWhichButtonIUse = {};
      panelQueryIDEP = new PanelQuery(
          null,
          null,
          QueryParameters.UC_ID_WARENVERKEHRSNUMMER,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getTextRespectUISPr("finanz.tab.oben.wvk.title"), true);
      panelQueryIDEP.befuellePanelFilterkriterienDirekt(
          FinanzFilterFactory.getInstance().createFKDWarenverkehrsnummerCNr(),
          FinanzFilterFactory.getInstance().createFKDWarenverkehrsnummerCBez());
      // Import-Button.
      panelQueryIDEP.createAndSaveAndShowButton(
          "/com/lp/client/res/import1.png",
          LPMain.getTextRespectUISPr("finanz.warenverkehrsnummern.importieren"),
          ACTION_SPECIAL_IMPORT_IDEP,
          RechteFac.RECHT_FB_FINANZ_CUD);
      setComponentAt(IDX_IDEP_LISTE, panelQueryIDEP);
    }
    return panelQueryIDEP;
  }


  protected JMenuBar getJMenuBar()
      throws Throwable {
    WrapperMenuBar wmb = new WrapperMenuBar(this);
    // ------------------------------------------------------------------------
    // Modul - Menu
    // ------------------------------------------------------------------------
    JMenu jmModul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);
    jmModul.add(new JSeparator(), 0);
    // Export
    WrapperMenuItem menueItemVorschau = new WrapperMenuItem(LPMain.getTextRespectUISPr(
        "lp.vorschau"), null);
    menueItemVorschau.addActionListener(this);
    menueItemVorschau.setActionCommand(MENU_ACTION_VORSCHAU);
    jmModul.add(menueItemVorschau, 0);
    // Vorschau
    WrapperMenuItem menueItemExport = new WrapperMenuItem(LPMain.getTextRespectUISPr(
        "fb.menu.intrastatmeldung"), RechteFac.RECHT_FB_FINANZ_CUD);
    menueItemExport.addActionListener(this);
    menueItemExport.setActionCommand(MENU_ACTION_INTRASTAT);
    jmModul.add(menueItemExport, 0);

    return wmb;
  }


  private PanelDialogKriterienIntrastat getPdKriterienIntrastat()
      throws Throwable {
    if (pdKriterienIntrastat == null) {
      pdKriterienIntrastat = new PanelDialogKriterienIntrastat(
          getInternalFrame(),
          LPMain.getTextRespectUISPr("fb.kriterien.intrastat"));
    }
    return pdKriterienIntrastat;
  }

  private PanelDialogKriterienIntrastat getPdKriterienVorschau()
      throws Throwable {
    if (pdKriterienVorschau == null) {
      pdKriterienVorschau = new PanelDialogKriterienIntrastat(
          getInternalFrame(),
          LPMain.getTextRespectUISPr("fb.kriterien.intrastat"));
    }
    return pdKriterienVorschau;
  }

}
