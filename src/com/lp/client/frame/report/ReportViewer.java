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
package com.lp.client.frame.report;

import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access") 
/**
 * <p><I>Diese Klasse kuemmert sich um das Anzeigen von Reports.</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungs: Martin Bluehweis dd.mm.05</p>
 *
 * @todo MB->MB nach dem Klick auf Speichern soll pdf ausgew&auml;hlt sein.  PJ 5159
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class ReportViewer
    extends JRViewer {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public final static int DEFAULT_ZOOM = 75;

  ReportViewer(String fileName, boolean isXML) throws JRException {
    super(fileName, isXML);
    setDefaults();
  }
  
  public void loadReport(JasperPrint jrPrint){
	  super.loadReport(jrPrint);
  }
  
  public void 	refreshPage(){
	  super.refreshPage();
  }
  



  private void setDefaults() {
    this.setLocale(LPMain.getInstance().getUISprLocale());
    // Tooltiptexte der buttons
    getBtnPrint().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.print"));
    getBtnFirst().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.firstpage"));
//    getBtnFirst().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
//        getKeyStroke(KeyEvent.VK_HOME, 0), "lp.report.firstpage");
    getBtnPrevious().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.previouspage"));
//    getBtnPrevious().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
//        getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "lp.report.previouspage");
    getBtnNext().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.nextpage"));
//    getBtnNext().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
//        getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "lp.report.nextpage");
    getBtnLast().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.lastpage"));
//    getBtnLast().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
//        getKeyStroke(KeyEvent.VK_END, 0), "lp.report.lastpage");
    getTxtGoto().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.gotopage"));
    getBtnActualSize().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.actualsize"));
    getBtnFitPage().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.fitpage"));
    getBtnFitWidth().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.fitwidth"));
    getBtnZoomIn().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.zoomin"));
    getBtnZoomOut().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.zoomout"));
    getCmbZoom().setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.report.zoomratio"));
  }


  ReportViewer(InputStream is, boolean isXML) throws JRException {
    super(is, isXML);
    setDefaults();
  }

  public ReportViewer(JasperPrint jrPrint) throws JRException {
    super(jrPrint);
    setDefaults();
  }

  void setZoom(int zoom) {
    cmbZoom.setSelectedItem(zoom+"%");
  }

  JToggleButton getBtnActualSize() {
    return btnActualSize;
  }

  JButton getBtnFirst() {
    return btnFirst;
  }

  JToggleButton getBtnFitPage() {
    return btnFitPage;
  }

  JToggleButton getBtnFitWidth() {
    return btnFitWidth;
  }

  JButton getBtnLast() {
    return btnLast;
  }

  JButton getBtnNext() {
    return btnNext;
  }

  JButton getBtnPrevious() {
    return btnPrevious;
  }

  JButton getBtnPrint() {
    return btnPrint;
  }

  JButton getBtnReload() {
    return btnReload;
  }

  JButton getBtnSave() {
    return btnSave;
  }

  JButton getBtnZoomIn() {
    return btnZoomIn;
  }

  JButton getBtnZoomOut() {
    return btnZoomOut;
  }

  JTextField getTxtGoto() {
    return txtGoTo;
  }

  JComboBox getCmbZoom() {
    return cmbZoom;
  }
}
