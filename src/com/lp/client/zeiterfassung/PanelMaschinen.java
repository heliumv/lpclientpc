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
package com.lp.client.zeiterfassung;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinengruppeDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


public class PanelMaschinen
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//von hier ...
  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jpaWorkingOn = new JPanel();
  private JPanel jpaButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private WrapperLabel wlaInventarnummer = new WrapperLabel();
  private WrapperTextField wtfInventarnummer = new WrapperTextField();

  private WrapperLabel wlaBezeichnung = new WrapperLabel();
  private WrapperTextField wtfBezeichnung = new WrapperTextField();
  private WrapperCheckBox wcbAutogehtbeiende = new WrapperCheckBox();
  private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
  private InternalFrameZeiterfassung internalFrameZeiterfassung = null;


  private WrapperLabel wlaKaufdatum = new WrapperLabel();

  private WrapperDateField wdfKaufdatum = new WrapperDateField();

  private WrapperLabel wlaIdentifikationsnr = new WrapperLabel();
  private WrapperTextField wtfIdentifikationsnr = new WrapperTextField();

  private WrapperButton wbuMaschinengruppe = new WrapperButton();
  private WrapperTextField wtfMaschinengruppe = new WrapperTextField();
  private PanelQueryFLR panelQueryFLRMaschinengruppe = null;

  static final public String ACTION_SPECIAL_MASCHINENGRUPPE_FROM_LISTE =
      "ACTION_SPECIAL_MASCHINENGRUPPE_FROM_LISTE";
  private WrapperLabel wlaEinheitProzent = new WrapperLabel();
  public PanelMaschinen(InternalFrame internalFrame, String add2TitleI,
                        Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfInventarnummer;
  }


  protected void setDefaults()
      throws Throwable {

  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    internalFrameZeiterfassung.setMaschineDto(new MaschineDto());
    leereAlleFelder(this);
    wcbAutogehtbeiende.setSelected(true);

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_MASCHINENGRUPPE_FROM_LISTE)) {
      dialogQueryMaschinengruppeFromListe(e);
    }

  }


  void dialogQueryMaschinengruppeFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN};

    panelQueryFLRMaschinengruppe = new PanelQueryFLR(
        null,
        null,
        QueryParameters.UC_ID_MASCHINENGRUPPE,
        aWhichButtonIUse,
        internalFrameZeiterfassung,
        LPMain.getTextRespectUISPr(
            "pers.maschinengruppe"));
    panelQueryFLRMaschinengruppe.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
        getInstance().createFKDBezeichnung(), null);
    panelQueryFLRMaschinengruppe.setSelectedId(internalFrameZeiterfassung.getMaschineDto().
                                               getMaschinengruppeIId());

    new DialogQuery(panelQueryFLRMaschinengruppe);
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZeiterfassungDelegate().removeMaschine(
        internalFrameZeiterfassung.getMaschineDto());
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws Throwable {
    internalFrameZeiterfassung.getMaschineDto().setCInventarnummer(wtfInventarnummer.
        getText());
    internalFrameZeiterfassung.getMaschineDto().setCIdentifikationsnr(
        wtfIdentifikationsnr.
        getText());
    internalFrameZeiterfassung.getMaschineDto().setCBez(wtfBezeichnung.
        getText());
    internalFrameZeiterfassung.getMaschineDto().setBVersteckt(wcbVersteckt.getShort());
    internalFrameZeiterfassung.getMaschineDto().setBAutoendebeigeht(wcbAutogehtbeiende.
        getShort());
    internalFrameZeiterfassung.getMaschineDto().setTKaufdatum(wdfKaufdatum.getTimestamp());
  }


  protected void dto2Components()
      throws Throwable {
    wtfInventarnummer.setText(internalFrameZeiterfassung.getMaschineDto().
                              getCInventarnummer());
    wtfBezeichnung.setText(internalFrameZeiterfassung.getMaschineDto().getCBez());
    wtfIdentifikationsnr.setText(internalFrameZeiterfassung.getMaschineDto().
                                 getCIdentifikationsnr());
    wcbAutogehtbeiende.setShort(internalFrameZeiterfassung.getMaschineDto().
                                getBAutoendebeigeht());
    wdfKaufdatum.setTimestamp(internalFrameZeiterfassung.getMaschineDto().getTKaufdatum());
    wcbVersteckt.setShort(internalFrameZeiterfassung.getMaschineDto().getBVersteckt());
    MaschinengruppeDto maschiengruppeDto = DelegateFactory.getInstance().
         getZeiterfassungDelegate().
         maschinengruppeFindByPrimaryKey(internalFrameZeiterfassung.getMaschineDto().getMaschinengruppeIId());
     wtfMaschinengruppe.setText(maschiengruppeDto.getCBez());



  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (internalFrameZeiterfassung.getMaschineDto().getIId() == null) {
        internalFrameZeiterfassung.getMaschineDto().setIId(DelegateFactory.getInstance().
            getZeiterfassungDelegate().
            createMaschine(internalFrameZeiterfassung.getMaschineDto()));
        setKeyWhenDetailPanel(internalFrameZeiterfassung.getMaschineDto().getIId());
      }
      else {
        DelegateFactory.getInstance().getZeiterfassungDelegate().updateMaschine(
            internalFrameZeiterfassung.getMaschineDto());
      }
      super.eventActionSave(e, true);
      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameZeiterfassung.getMaschineDto().
                                              getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRMaschinengruppe) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        MaschinengruppeDto maschiengruppeDto = DelegateFactory.getInstance().
            getZeiterfassungDelegate().
            maschinengruppeFindByPrimaryKey( (Integer)
                                            key);
        wtfMaschinengruppe.setText(maschiengruppeDto.getCBez());
        internalFrameZeiterfassung.getMaschineDto().setMaschinengruppeIId(
            maschiengruppeDto.getIId());
      }
    }

  }


  private void jbInit()
      throws Throwable {
    //von hier ...
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    //das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    //Actionpanel von Oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();
    this.setActionMap(null);

    wlaInventarnummer.setText(LPMain.getTextRespectUISPr(
        "zeiterfassung.inventarnummer"));
    wlaIdentifikationsnr.setText(LPMain.getTextRespectUISPr(
        "zeiterfassung.maschinen.identifikationsnr"));

    wtfInventarnummer.setColumnsMax(ZeiterfassungFac.MAX_TAETIGKEIT_KENNUNG);
    wtfInventarnummer.setText("");
    wtfInventarnummer.setMandatoryField(true);
    wtfMaschinengruppe.setActivatable(false);
     wtfMaschinengruppe.setMandatoryField(true);
    wtfIdentifikationsnr.setMandatoryField(true);

	wcbVersteckt.setText(LPMain.getTextRespectUISPr(
	"lp.versteckt"));
    
    wbuMaschinengruppe.setText(LPMain.getTextRespectUISPr(
        "pers.maschinengruppe") + "...");
    wbuMaschinengruppe.setActionCommand(ACTION_SPECIAL_MASCHINENGRUPPE_FROM_LISTE);
    wbuMaschinengruppe.addActionListener(this);
    wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
    wlaKaufdatum.setText(LPMain.getTextRespectUISPr(
        "zeiterfassung.kaufdatum"));

    wtfIdentifikationsnr.setColumnsMax(2);
    wtfIdentifikationsnr.setUppercaseField(true);

    wtfBezeichnung.setColumns(ZeiterfassungFac.MAX_TAETIGKEIT_BEZEICHNUNG);
    getInternalFrame().addItemChangedListener(this);

    wcbAutogehtbeiende.setText(LPMain.getTextRespectUISPr(
        "zeiterfassung.autoendebeigeht"));
    wlaEinheitProzent.setRequestFocusEnabled(true);
    wlaEinheitProzent.setHorizontalAlignment(SwingConstants.LEFT);
    wlaEinheitProzent.setText("%");

    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.SOUTHEAST,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaInventarnummer,
                     new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfInventarnummer,
                     new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaKaufdatum, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdfKaufdatum, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wcbAutogehtbeiende, new GridBagConstraints(1, 7, 2, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wbuMaschinengruppe, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfMaschinengruppe, new GridBagConstraints(1, 2, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfIdentifikationsnr, new GridBagConstraints(1, 3, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaIdentifikationsnr, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
 
    jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
            70, 0));

    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD,
    };

    enableToolsPanelButtons(aWhichButtonIUse);

  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_MASCHINE;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {
      leereAlleFelder(this);
      clearStatusbar();
   
    }
    else {
      internalFrameZeiterfassung.setMaschineDto(DelegateFactory.getInstance().
                                                getZeiterfassungDelegate().
                                                maschineFindByPrimaryKey( (Integer) key));
      dto2Components();
    }
  }
}
