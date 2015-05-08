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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;

@SuppressWarnings("static-access") 
/**
 * <p>Basisfenster fuer LP5 Positionen.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2005-02-11</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelPositionenTextbaustein
    extends PanelBasis {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private JPanel jPanelWorkingOn = null;

  private WrapperButton wbuTextmodul = null;
  public WrapperTextField wtfTextmodul = null;

  private WrapperLabel wlaDatenformat = null;
  public WrapperTextField wtfDatenformat = null;

  public WrapperEditorField wefText = null;

  private JScrollPane jScrollPane1 = null;
  private PanelImage jpaBild = null;

  private PanelQueryFLR panelQueryFLRTextmodul = null;

  private final static String ACTION_SPECIAL_TEXTMODUL_FROM_LISTE =
      "action_special_textmodul_from_liste";

  private int iSpaltenbreite1;

  /**
   * @todo private machen
   */
  public MediastandardDto oMediastandardDto = null;

  /**
   * Konstruktor.
   * @param internalFrame der InternalFrame auf dem das Panel sitzt
   * @param add2TitleI der default Titel des Panels
   * @param key PK der Position
   * @param iSpaltenbreite1I die Breite der ersten Spalte
   * @throws java.lang.Throwable Ausnahme
   */
  public PanelPositionenTextbaustein(InternalFrame internalFrame,
                                            String add2TitleI,
                                            Object key,
             int iSpaltenbreite1I) throws Throwable {
    super(internalFrame, add2TitleI, key);

    iSpaltenbreite1 = iSpaltenbreite1I;

    jbInit();
    initComponents();
  }

  private void jbInit() throws Throwable {
    // wegen Dialogauswahl auf FLR events hoeren
    getInternalFrame().addItemChangedListener(this);

    setLayout(new GridBagLayout());

    // Workingpanel
    jPanelWorkingOn = new JPanel();
    jPanelWorkingOn.setLayout(new GridBagLayout());
    add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    wbuTextmodul = new WrapperButton(LPMain.getInstance().getTextRespectUISPr("lp.datei"));
    wbuTextmodul.setMaximumSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));
    wbuTextmodul.setMinimumSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));
    wbuTextmodul.setPreferredSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));
    wbuTextmodul.setActionCommand(ACTION_SPECIAL_TEXTMODUL_FROM_LISTE);
    wbuTextmodul.addActionListener(this);

    wtfTextmodul = new WrapperTextField();
    wtfTextmodul.setActivatable(false);
    wtfTextmodul.setMandatoryField(true);

    wlaDatenformat = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.datenformat"));
    wtfDatenformat = new WrapperTextField();
    wtfDatenformat.setActivatable(false);

    wefText = new WrapperEditorFieldTexteingabe(getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("anf.anfrage"));
    wefText.setEnabled(false);

    jpaBild = new PanelImage(null);
    jpaBild.setLayout(new GridBagLayout());

    jScrollPane1 = new JScrollPane();
    jScrollPane1.getViewport().add(jpaBild);

    // Zeile
    jPanelWorkingOn.add(wbuTextmodul,  new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfTextmodul,  new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

    // Zeile
    iZeile++;
    jPanelWorkingOn.add(wlaDatenformat,  new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
          ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfDatenformat,  new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
          ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

    // Zeile
    iZeile++;
    jPanelWorkingOn.add(wefText,  new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.1
          ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

    // Zeile
    iZeile++;
    jPanelWorkingOn.add(jScrollPane1, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.1
         , GridBagConstraints.NORTH, GridBagConstraints.BOTH,
         new Insets(2, 2, 2, 2), 0, 0));
  }

  private void setTextVisible(boolean bVisibleI) {
    wefText.setVisible(bVisibleI);
  }

  private void setBildVisible(boolean bVisibleI) {
    jpaBild.setVisible(bVisibleI);
    jScrollPane1.setVisible(bVisibleI);
  }

  protected void setDefaults() throws Throwable {
    oMediastandardDto = new MediastandardDto();

    setTextVisible(false);
    setBildVisible(false);
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
      if (e.getActionCommand().equals(ACTION_SPECIAL_TEXTMODUL_FROM_LISTE)) {
        dialogQueryTextmodul(e);
      }
  }

  protected void eventItemchanged(EventObject eI) throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == panelQueryFLRTextmodul) {
        Object oKey = ( (ISourceEvent) e.getSource()).getIdSelected();

        if (oKey != null) {
          oMediastandardDto = DelegateFactory.getInstance().getMediaDelegate().
              mediastandardFindByPrimaryKey((Integer) oKey);

          dto2Components();
        }
      }
    }
  }

  public void dto2Components() {
    wtfTextmodul.setText(oMediastandardDto.getCNr());
    wtfDatenformat.setText(oMediastandardDto.getDatenformatCNr());

    if (oMediastandardDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
      setTextVisible(true);
      setBildVisible(false);

      wefText.setText(oMediastandardDto.getOMediaText());
    }
    else
    if (oMediastandardDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)||
              oMediastandardDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)||
              oMediastandardDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {

      setBildVisible(true);
      setTextVisible(false);

      Image myImage = Toolkit.getDefaultToolkit().createImage(oMediastandardDto.getOMediaImage());
      jpaBild.setImage(myImage);
      jpaBild.repaint();
      jScrollPane1.repaint();
    }
  }

  /**
   * Dialogfenster zur Auswahl des Textmoduls.
   *
   * @param e ActionEvent
   * @throws java.lang.Throwable Ausnahme
   */
  public void dialogQueryTextmodul(ActionEvent e)
      throws Throwable {
    panelQueryFLRTextmodul = SystemFilterFactory.getInstance().createPanelFLRMediastandard(
        getInternalFrame());
    new DialogQuery(panelQueryFLRTextmodul);
  }
}
