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
package com.lp.client.fertigung;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.KapazitaetsvorschauDetailDto;
import com.lp.server.fertigung.service.KapazitaetsvorschauDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinengruppeDto;

@SuppressWarnings("static-access") 
public class PanelDialogKapazitaetsvorschauDetails
    extends PanelDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperTable wtaDatum = null;
  private KapazitaetsvorschauDto kapDto = null;
  private int index;

  public PanelDialogKapazitaetsvorschauDetails(InternalFrame p0, String title,
                                               KapazitaetsvorschauDto kapDto, int index)
      throws Throwable {
    super(p0, "Details f\u00FCr Kalenderwoche " + title);
    this.kapDto = kapDto;
    this.index = index;
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    fillTable();
    JScrollPane jspScroll = new JScrollPane();
    jspScroll.getViewport().add(wtaDatum, null);

    jpaWorkingOn.setLayout(new GridBagLayout());
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(jspScroll,
                     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));
  }


  private void fillTable()
      throws Throwable {
    // Zeilenanzahl in der Tabelle bestimmen
    int iZeilen = 0;
    BigDecimal bdGesamtVerfuegbar = new BigDecimal(0);
    LinkedList<KapazitaetsvorschauDetailDto>[][] details = kapDto.getDetails();
    for (int i = 0; i < details.length; i++) {
      iZeilen = iZeilen + details[i][index].size();
      bdGesamtVerfuegbar = bdGesamtVerfuegbar.add(kapDto.getBdVerfuegbareStunden()[i][index]);
    }
    String [] sColumnNames = new String[] {
        LPMain.getInstance().getTextRespectUISPr("fert.tab.unten.los.title"), // Los
        LPMain.getInstance().getTextRespectUISPr("label.identnummer"),
        LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"),
        LPMain.getInstance().getTextRespectUISPr("stkl.arbeitsplan.arbeitsgang"),
        LPMain.getInstance().getTextRespectUISPr("lp.maschine"),
        LPMain.getInstance().getTextRespectUISPr("lp.dauer"),
        LPMain.getInstance().getTextRespectUISPr("lp.gruppe")
    };
    int iTabllenzeile = 0;
    Object[][] data = new Object[iZeilen][sColumnNames.length];
    for (int i = 0; i < details.length; i++) {
      for (Iterator<?> iter = details[i][index].iterator(); iter.hasNext(); ) {
        KapazitaetsvorschauDetailDto item = (KapazitaetsvorschauDetailDto) iter.next();
        LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate().
            losFindByPrimaryKey(item.getLosIId());
        ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate().
            artikelFindByPrimaryKey(item.getArtikelIIdTaetigkeit());
        LossollarbeitsplanDto losAZDto = DelegateFactory.getInstance().getFertigungDelegate().
            lossollarbeitsplanFindByPrimaryKey(item.getLossollarbeitsplanIId());
        data[iTabllenzeile][0] = losDto.getCNr();
        data[iTabllenzeile][1] = artikelDto.getCNr();
        data[iTabllenzeile][2] = artikelDto.getArtikelsprDto().getCBez();
        data[iTabllenzeile][3] = losAZDto.getIArbeitsgangnummer();
        if (losAZDto.getMaschineIId() != null) {
          MaschineDto maschineDto = DelegateFactory.getInstance().
              getZeiterfassungDelegate().maschineFindByPrimaryKey(losAZDto.getMaschineIId());
          data[iTabllenzeile][4] = maschineDto.getBezeichnung();
        }
        else {
          data[iTabllenzeile][4] = "";
        }
        data[iTabllenzeile][5] = item.getBdDauer();
        if (item.getArtikelgruppeIId()!= null) {
          ArtgruDto artgruDto = DelegateFactory.getInstance().getArtikelDelegate().
              artgruFindByPrimaryKey(item.getArtikelgruppeIId());
          data[iTabllenzeile][6] = artgruDto.getArtgrusprDto().getCBez();
        }
        else if (item.getMaschinengruppeIId()!= null) {
          MaschinengruppeDto mdDto = DelegateFactory.getInstance().
              getZeiterfassungDelegate().maschinengruppeFindByPrimaryKey(item.
              getMaschinengruppeIId());
          data[iTabllenzeile][6] = mdDto.getCBez();
        }
        else {
          data[iTabllenzeile][6] = "";
        }


        iTabllenzeile++;
      }
    }
    wtaDatum = new WrapperTable(getInternalFrame(), data, sColumnNames);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtaDatum;
  }
}
