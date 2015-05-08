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
package com.lp.client.partner;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.PartnerDto;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich die Anreden.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; dd.11.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 09:44:46 $
 */
public class PanelPartnerAnreden
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TabbedPanePartner tpPartner = null;
  private JPanel jpaWorking = null;
  private GridBagLayout gblWorkingPanel = null;
  private GridBagLayout gblAussen = null;
  private WrapperTable tAnreden = null;

  private String[] columnNamesDummy = {
      "1",
      "2",
  };
  private Object[][] data = {
      {
      LPMain.getInstance().getTextRespectUISPr("part.adresse"),
      ""
  }, {
      LPMain.getInstance().getTextRespectUISPr("part.anrede"),
      ""
  }
      , {
      LPMain.getInstance().getTextRespectUISPr("part.briefanrede"),
      "",
  }
      , {
      LPMain.getInstance().getTextRespectUISPr("part.fix_titel_name1_name2"),
      "",
  }
      , {
      LPMain.getInstance().getTextRespectUISPr("part.titelanrede"),
      "",
  }
      , {
      LPMain.getInstance().getTextRespectUISPr("part.formatfixanredetitelname2name1"),
      "",
  }
      , {
      LPMain.getInstance().getTextRespectUISPr("part.formatfixname2name1"),
      "",
  }
  };

  public PanelPartnerAnreden(InternalFrame internalFrame,
                             String add2TitleI,
                             Object keyI,
      TabbedPanePartner tpPartner)
      throws Throwable {

    super(internalFrame, add2TitleI, keyI);
    this.tpPartner = tpPartner;

    jbInit();
    initComponents();
    initPanel();
  }


  /**
   * jbInit
   *
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    // buttons.
    resetToolsPanel();

    String[] aButton = {
    };
    enableToolsPanelButtons(aButton);

    // von hier ...

    //das aussenpanel hat immer das gridbaglayout.
    gblAussen = new GridBagLayout();
    setLayout(gblAussen);

    // actionpanel von Oberklasse holen und anhaengen.
    add(getToolsPanel(),
        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                               , GridBagConstraints.NORTHWEST,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(0, 0, 0, 0), 0, 0));

    // Statusbar an den unteren Rand des Panels haengen.
    add(getPanelStatusbar(),
        new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                               , GridBagConstraints.CENTER,
                               GridBagConstraints.BOTH,
                               new Insets(0, 0, 0, 0), 0, 0));

    //Workingpanel
    jpaWorking = new JPanel();
    gblWorkingPanel = new GridBagLayout();
    jpaWorking.setLayout(gblWorkingPanel);

    tAnreden = new WrapperTable(getInternalFrame(), data, columnNamesDummy);
    tAnreden.getColumnModel().getColumn(0).setMinWidth(180);
    tAnreden.getColumnModel().getColumn(0).setMaxWidth(180);
    tAnreden.setEnabled(false);
    add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                           , GridBagConstraints.NORTH,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 0, 0));

    // Ab hier einhaengen.
    // Zeile
    iZeile++;
    jpaWorking.add(tAnreden,
                   new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0
                                          , GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(2, 2, 2, 2), 0, 0));

  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    Integer iId = getTabbedPanePartner().getPartnerDto().getIId();

    getTabbedPanePartner().setPartnerDto(
        DelegateFactory.getInstance().getPartnerDelegate().
        partnerFindByPrimaryKey(iId));
    dto2Components();

    getInternalFrame().setLpTitle(
        InternalFrame.TITLE_IDX_AS_I_LIKE,
        getTabbedPanePartner().getPartnerDto().formatFixTitelName1Name2());
  }


  /**
   * dto2Components
   *
   * @throws Throwable
   */
  private void dto2Components()
      throws Throwable {

    PartnerDto partnerDto = getPartnerDto();

    if (partnerDto.getIId() != null) {
      data[0][1] = getPartnerDto().formatAdresse();
      data[1][1] = getPartnerDto().formatAnrede();

      String briefanrede=DelegateFactory.getInstance().getPartnerDelegate().formatBriefAnrede(partnerDto,LPMain.getInstance().
                                            getTheClient().getLocMandant());

      data[2][1] = LPMain.getInstance().getTheClient().getLocMandant() + ": " +
          briefanrede;
      data[3][1] = getPartnerDto().formatFixTitelName1Name2();
      data[4][1] = getPartnerDto().formatTitelAnrede();
      data[5][1] = getPartnerDto().formatFixAnredeTitelName2Name1();
      data[6][1] = getPartnerDto().formatFixName2Name1();
    }
  }


  /**
   * Fuelle die Helpercontrols, jene aus denen ausgewaehlt werden kann.
   *
   * @throws ExceptionForLPClients
   * @throws Throwable
   */
  private void initPanel()
      throws Throwable {
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_PARTNER;
  }


  protected PartnerDto getPartnerDto() {
    return getTabbedPanePartner().getPartnerDto();
  }


  private TabbedPanePartner getTabbedPanePartner() {
    return tpPartner;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return NO_VALUE_THATS_OK_JCOMPONENT;
  }

}
