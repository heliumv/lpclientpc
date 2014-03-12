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
package com.lp.client.partner;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.ejb.LflfliefergruppePK;
import com.lp.server.partner.fastlanereader.generated.service.FLRLFLiefergruppePK;
import com.lp.server.partner.service.LflfliefergruppeDto;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 *
 * <p>@author $Author: sebastian $</p>
 *
 * @version not attributable Date $Date: 2009/03/06 14:17:54 $
 */
public class PanelLieferantLiefergruppe
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private JPanel jpaWorking = null;
  private GridBagLayout gblWorkingPanel = null;
  private Border border = null;
  private GridBagLayout gblAussen = null;

  private WrapperButton wbuLiefergruppe = null;
  static final public String ACTION_SPECIAL_FLR_LIEFERGRUPPE =
      "action_special_flr_liefergruppe";
  private PanelQueryFLR panelQueryFLRLiefergruppeAuswahl = null;
  private WrapperTextField wtfLiefergruppe = null;

  public PanelLieferantLiefergruppe(InternalFrame internalFrame,
                                    String add2TitleI,
                                    Object keyI)
      throws Throwable {

    super(internalFrame, add2TitleI, keyI);

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
    getInternalFrame().addItemChangedListener(this);

    // buttons.
    resetToolsPanel();

    String[] aButton = {
        PanelBasis.ACTION_SAVE,
        PanelBasis.ACTION_DELETE,
        PanelBasis.ACTION_DISCARD
    };
    enableToolsPanelButtons(aButton);

    // von hier ...
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);

    wbuLiefergruppe = new WrapperButton();
    wbuLiefergruppe.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.liefergruppe.flr"));
    wbuLiefergruppe.setActionCommand(ACTION_SPECIAL_FLR_LIEFERGRUPPE);
    wbuLiefergruppe.addActionListener(this);

    wtfLiefergruppe = new WrapperTextField();
    wtfLiefergruppe.setActivatable(false);
    wtfLiefergruppe.setMandatoryFieldDB(true);

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
    add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                           , GridBagConstraints.NORTH,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 0, 0));

    // Ab hier einhaengen.
    //Zeile
    jpaWorking.add(wbuLiefergruppe,
                   new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0
                                          , GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(2, 2, 2, 2), 0, 0));
    jpaWorking.add(wtfLiefergruppe,
                   new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0
                                          , GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(2, 2, 2, 2), 0, 0));
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key != null && key.equals(LPMain.getLockMeForNew()))) {
      // Neu.
      leereAlleFelder(this);
      clearStatusbar();

      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          getInternalFrameLieferant().getLieferantDto().getPartnerDto().
          formatFixTitelName1Name2());
    }
    else {
      FLRLFLiefergruppePK oKey = (FLRLFLiefergruppePK) key;
      // Update.
      getInternalFrameLieferant().setLflfliefergruppeDto(
          DelegateFactory.getInstance().getLieferantDelegate().
          lflfliefergruppeFindByPrimaryKey(oKey.getLieferant_i_id(),
                                           oKey.getLfliefergruppe_i_id()));
      dto2Components();

      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          getInternalFrameLieferant().getLieferantDto().getPartnerDto().
          formatFixTitelName1Name2());
    }
  }


  /**
   * dto2Components
   *
   * @throws Throwable
   */
  private void dto2Components()
      throws Throwable {
    if (getInternalFrameLieferant().getLflfliefergruppeDto() != null) {
      LfliefergruppeDto lf = DelegateFactory.getInstance().getLieferantServicesDelegate().
          lfliefergruppeFindByPrimaryKey(getInternalFrameLieferant().
                                         getLflfliefergruppeDto().getLfliefergruppeIId());
      String sBez = null;
      if (lf.getLfliefergruppesprDto() != null && lf.getLfliefergruppesprDto().getCBez() != null) {
        sBez = lf.getLfliefergruppesprDto().getCBez();
      }
      else {
        sBez = lf.getCNr();
      }
      wtfLiefergruppe.setText(sBez);
    }
  }


  /**
   * Behandle Ereignis Save.
   *
   * @param e Ereignis
   * @param bNeedNoSaveI boolean
   * @throws Throwable
   */
  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {

    if (allMandatoryFieldsSetDlg()) {

      checkLockedDlg();

      Integer iIdLiefergruppe = null;

      components2Dto();

      if (getInternalFrameLieferant().getLflfliefergruppeDto().getLieferantIId() == null) {
        // Create.
        getInternalFrameLieferant().getLflfliefergruppeDto().setLieferantIId(
            getInternalFrameLieferant().getLieferantDto().getIId());

        LflfliefergruppePK lfpk = DelegateFactory.getInstance().getLieferantDelegate().
            createLflfliefergruppe(getInternalFrameLieferant().getLflfliefergruppeDto());
        FLRLFLiefergruppePK oKey = new FLRLFLiefergruppePK(lfpk.getLieferantIId(),
            lfpk.getLfliefergruppeIId());
        setKeyWhenDetailPanel(oKey);
      }
      else {
        // Update.
        throw new Exception("no update possible!");
      }
      super.eventActionSave(e, false);
      eventYouAreSelected(false);
    }
  }


  protected void eventActionDelete(ActionEvent e,
                                   boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {

    if (!isLockedDlg()) {
      DelegateFactory.getInstance().getLieferantDelegate().removeLflfliefergruppe(
          getInternalFrameLieferant().getLflfliefergruppeDto().getLieferantIId(),
          getInternalFrameLieferant().getLflfliefergruppeDto().getLfliefergruppeIId());
      getInternalFrameLieferant().setLflfliefergruppeDto(new LflfliefergruppeDto());
      this.setKeyWhenDetailPanel(null);

      super.eventActionDelete(e, false, false);
    }
  }


  /**
   * components2Dto
   */
  private void components2Dto() {
    // nothing here
  }


  /**
   * Fuelle die Helpercontrols, jene aus denen ausgewaehlt werden kann.
   *
   * @throws ExceptionForLPClients
   * @throws Throwable
   */
  private void initPanel()
      throws Throwable {
    // nothing here
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_LIEFERANT;
  }


  private InternalFrameLieferant getInternalFrameLieferant() {
    return ( (InternalFrameLieferant) getInternalFrame());
  }


  public void eventActionNew(EventObject eventObject,
                             boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {

    super.eventActionNew(eventObject, true, false);

    if (!bNeedNoNewI) {
      getInternalFrameLieferant().setLflfliefergruppeDto(new LflfliefergruppeDto());
      setDefaults();
    }
  }


  /**
   * setDefaults
   *
   * @throws Throwable
   */
  private void setDefaults()
      throws Throwable {
    // nothing here
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {

    if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_LIEFERGRUPPE)) {

      String[] aWhichButtonIUse = null;

      FilterKriterium[] f = SystemFilterFactory.getInstance().createFKMandantCNr();
      QueryType[] querytypes = null;
      panelQueryFLRLiefergruppeAuswahl = new PanelQueryFLR(
          querytypes,
          f,
          QueryParameters.UC_ID_LIEFERGRUPPEN,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("part.liefergruppe"));

      DialogQuery dialogQueryAnsprechpartner = new DialogQuery(
          panelQueryFLRLiefergruppeAuswahl);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws ExceptionLP, Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == panelQueryFLRLiefergruppeAuswahl) {
        Integer iId = (Integer) ( (ISourceEvent) e.getSource()).getIdSelected();
        getInternalFrameLieferant().getLflfliefergruppeDto().
            setLfliefergruppeIId(iId);

        LfliefergruppeDto lflfliefergruppeDto = null;
        if (iId != null) {
          lflfliefergruppeDto = DelegateFactory.getInstance().
              getLieferantServicesDelegate().lfliefergruppeFindByPrimaryKey(iId);
          wtfLiefergruppe.setText(lflfliefergruppeDto != null ?
                                  lflfliefergruppeDto.getCNr() : null);
        }
      }
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuLiefergruppe;
  }
}
