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
package com.lp.client.artikel;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Auswahl von Seriennummern.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>16. 03. 2005</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelDialogSeriennummer
    extends PanelDialogKriterien
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperTable jtTabelle = null;
  private JScrollPane tableScrollPane = null;

  protected String[] aSeriennummern = null;

  private WrapperLabel wlaArtikelCNr = null;
  private WrapperTextField wtfArtikelCNr = null;
  private WrapperLabel wlaArtikelCBez = null;
  private WrapperTextField wtfArtikelCBez = null;
  private WrapperLabel wlaArtikelZusatzbez = null;
  private WrapperTextField wtfArtikelZusatzbez = null;

  protected WrapperLabel wlaEingabe = null;
  protected WrapperTextField wsfEingabe = null;

  protected WrapperTextField wtfSnreingabeeinzeln = null;

  /** Es geht um die Serien- oder Chargennummern dieses Artikels. */
  protected Integer iIdArtikel = null;
  /** Von diesem Lager soll abgebucht werden. */
  protected Integer iIdLager = null;
  /** Diese Menge wurde vom Benutzer eingegeben. */
  protected Double ddGewuenschteMenge = null;

  private WrapperLabel wlaGewuenschteMenge = null;
  protected WrapperLabel wnfGewuenschteMenge = null;

  private WrapperLabel wlaGewaehlteMenge = null;
  protected WrapperLabel wnfGewaehlteMenge = null;

  protected boolean bPruefeObMengeAufLager = false;

  private ArtikelDto artikelDto = null;

  public PanelDialogSeriennummer(
      InternalFrame oInternalFrameI,
      String add2Title,
      WrapperTable jtTabelleI,
      String[] aSeriennummernI,
      Integer iIdArtikelI,
      Integer iIdLagerI,
      Double ddGewuenschteMengeI,
      boolean bPruefeObMengeAufLagerI)
      throws Throwable {
    super(oInternalFrameI, add2Title);

    jtTabelle = jtTabelleI;
    aSeriennummern = aSeriennummernI;
    iIdArtikel = iIdArtikelI;
    iIdLager = iIdLagerI;
    ddGewuenschteMenge = ddGewuenschteMengeI;
    bPruefeObMengeAufLager = bPruefeObMengeAufLagerI;

    jbInit();
    setDefaults();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    jtTabelle.setColumnSelectionAllowed(false);
    jtTabelle.setRowSelectionAllowed(true);
    jtTabelle.addMouseListener(this);
    jtTabelle.addKeyListener(this);
    jtTabelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    tableScrollPane = new JScrollPane(jtTabelle);
    tableScrollPane.
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    tableScrollPane.
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    wlaArtikelCNr = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "artikel.artikelnummer"));
    HelperClient.setDefaultsToComponent(wlaArtikelCNr, 150);

    wtfArtikelCNr = new WrapperTextField();
    wtfArtikelCNr.setEditable(false);

    wlaArtikelCBez = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "label.bezeichnung"));

    wtfArtikelCBez = new WrapperTextField();
    wtfArtikelCBez.setEditable(false);

    wlaArtikelZusatzbez = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "lp.zusatzbezeichnung"));

    wtfArtikelZusatzbez = new WrapperTextField();
    wtfArtikelZusatzbez.setEditable(false);

    wlaEingabe = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "artikel.seriennummer"));

    wsfEingabe = new WrapperTextField();
    wsfEingabe.setText(Helper.erzeugeStringAusStringArray(aSeriennummern)); // wird im Konstruktor gesetzt

    wlaGewuenschteMenge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "lp.gewuenschtemenge"));
    wnfGewuenschteMenge = new WrapperLabel();
    wnfGewuenschteMenge.setHorizontalAlignment(SwingConstants.LEADING);

    wlaGewaehlteMenge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "lp.gewaehltemenge"));
    wnfGewaehlteMenge = new WrapperLabel();
    wnfGewaehlteMenge.setHorizontalAlignment(SwingConstants.LEADING);

    // Zeile - die Tabelle der moeglichen Serien- oder Chargennummern
    jpaWorkingOn.add(tableScrollPane,
                     new GridBagConstraints(0, iZeile, 2, 1, 1.0, 1.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaArtikelCNr,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfArtikelCNr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaArtikelCBez,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfArtikelCBez,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaArtikelZusatzbez,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfArtikelZusatzbez,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaEingabe,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(10, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wsfEingabe,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(10, 2, 2, 2),
                                            0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaGewuenschteMenge,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wnfGewuenschteMenge,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    // Zeile
    iZeile++;
    jpaWorkingOn.add(wlaGewaehlteMenge,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wnfGewaehlteMenge,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    showGewaehlteMenge(false);
  }


  public void setDefaults()
      throws Throwable {
    artikelDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(
        iIdArtikel);

    wtfArtikelCNr.setText(artikelDto.getCNr());

    if (artikelDto.getArtikelsprDto() != null) {
      if (artikelDto.getArtikelsprDto().getCBez() != null) {
        wtfArtikelCBez.setText(artikelDto.getArtikelsprDto().getCBez());
      }

      if (artikelDto.getArtikelsprDto().getCZbez() != null) {
        wtfArtikelZusatzbez.setText(artikelDto.getArtikelsprDto().getCZbez());
      }
    }

    String cGewuenschteMenge = Helper.formatZahl(ddGewuenschteMenge, 3,
                                                 LPMain.getInstance().getTheClient().
                                                 getLocUi());
    wnfGewuenschteMenge.setText(cGewuenschteMenge);
  }


  protected void showGewaehlteMenge(boolean bShowI) {
    wlaGewaehlteMenge.setVisible(bShowI);
    wnfGewaehlteMenge.setVisible(bShowI);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {

    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {

      // Menge auf Lager darf nur fuer neue Seriennrchargennr ausgefuehrt werden
      String[] aSeriennrchargennrBisher = aSeriennummern;

      aSeriennummern = erzeugeSeriennummernChargenArray();

      // wenn keine Seriennummern eingegeben wurden
      if (aSeriennummern == null || aSeriennummern.length == 0) {
        DialogFactory.showModalDialog(
            LPMain.getInstance().getTextRespectUISPr("lp.warning"),
            LPMain.getInstance().getTextRespectUISPr("lp.korrektemengewaehlen"));

        aSeriennrchargennrBisher = null;
      }
      else {

        // pruefen, ob die gewuenschte Menge erreicht ist
        if (!pruefeGewaehlteMenge()) {
          showGewaehlteMenge(true);
          String cGewaehlteMenge = Helper.formatZahl(new Double(aSeriennummern.length), 3,
              LPMain.getInstance().getTheClient().getLocUi());
          wnfGewaehlteMenge.setText(cGewaehlteMenge);

          DialogFactory.showModalDialog(
              LPMain.getInstance().getTextRespectUISPr("lp.warning"),
              LPMain.getInstance().getTextRespectUISPr("lp.korrektemengewaehlen"));

          aSeriennrchargennrBisher = null;
        }
        else {
          showGewaehlteMenge(false);
          wnfGewaehlteMenge.setText(null);

          boolean bMengeAufLagerOderNichtRelevant = true;

          if (bPruefeObMengeAufLager) {
            bMengeAufLagerOderNichtRelevant = pruefeObAlleAufLager(
                aSeriennrchargennrBisher);
          }
          if (bMengeAufLagerOderNichtRelevant) {
            super.eventActionSpecial(e);
          }
        }
      }
    }
    else if (e.getActionCommand().equals(PanelBasis.ESC) ||
             e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      super.eventActionSpecial(e);
    }
  }


  /**
   * Hier wird ueberprueft, ob die gewuenschte Menge durch die Eingabe erreicht
   * werden kann.
   *
   * @return boolean true, wenn die gewuenschte Menge durch die Eingabe
   *   erreicht werden kann.
   * @throws Throwable
   */
  protected boolean pruefeGewaehlteMenge()
      throws Throwable {
    boolean bMengeErreicht = false;

    if (aSeriennummern.length == ddGewuenschteMenge.intValue()) {
      bMengeErreicht = true;
    }

    return bMengeErreicht;
  }


  /**
   * Der Benutzer kann frei ingeben, hier wird seine Eingabe auf ihre
   * Gueltigkeit ueberprueft.
   *
   * @return boolean true, wenn die Eingabe gueltig ist.
   * @throws Throwable Ausnahme
   * @param aSeriennummernBisherI String[]
   */
  protected boolean pruefeObAlleAufLager(String[] aSeriennummernBisherI)
      throws Throwable {
    boolean bAlleAufLager = true;

    for (int i = 0; i < aSeriennummern.length; i++) {

      // die Menge wird nur fuer neue Seriennummern geprueft
      if (!Helper.enthaeltStringArrayString(aSeriennummernBisherI, aSeriennummern[i])) {
        BigDecimal ddMenge = DelegateFactory.getInstance().getLagerDelegate().getMengeAufLager(
            iIdArtikel,
            iIdLager, aSeriennummern[i]);

        if (ddMenge.doubleValue() < 1) {
          bAlleAufLager = false;

          MessageFormat mf = new MessageFormat(
              LPMain.getInstance().getTextRespectUISPr(
                  "lp.seriennummernichtauflager"));

          mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

          Object pattern[] = {
              DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(iIdArtikel).
              getCNr(),
              aSeriennummern[i],
              DelegateFactory.getInstance().getLagerDelegate().lagerFindByPrimaryKey(iIdLager).
              getCNr()
          };

          DialogFactory.showModalDialog(
              LPMain.getInstance().getTextRespectUISPr("lp.warning"),
              mf.format(pattern));
        }
      }
    }

    return bAlleAufLager;
  }


  /**
   * Hier koennen die eingegebenen Seriennummern abgeholt werden.
   * @return String[] null, wenn es keine gueltige Eingabe gibt oder die Eingabe ist leer
   */
  public String[] getSeriennummern() {
    return aSeriennummern;
  }


  /**
   * Aus der Benutzereingabe ein Seriennummern Array erzeugen.
   * @return String[] das Array
   * @throws Throwable Ausnahme
   */
  protected String[] erzeugeSeriennummernChargenArray()
      throws Throwable {
    return Helper.erzeugeSeriennummernArray(
        wsfEingabe.getText(),
        new BigDecimal(ddGewuenschteMenge.doubleValue()),
        false);
  }


  protected void eventMouseClicked(MouseEvent e)
      throws Throwable {
    if (e.getSource() == jtTabelle && e.getClickCount()==2) {
      addSelectedSNR();
    }
  }

  protected void eventKeyPressed(KeyEvent e)
      throws Throwable {
    if (e.getSource() == jtTabelle && e.getKeyCode() == KeyEvent.VK_ENTER) {
      addSelectedSNR();
    }
  }

  protected void eventKeyReleased(KeyEvent e)
      throws Throwable {
    // nothing here
  }

  protected void eventKeyTyped(KeyEvent e)
      throws Throwable {
    // nothing here
  }

  private void addSelectedSNR() {
    String snr = (String) jtTabelle.getValueAt(jtTabelle.getSelectedRow(), 1);
    String sEingegeben = wsfEingabe.getText();
    if (sEingegeben == null || sEingegeben.trim().equals("")) {
      sEingegeben = "";
    }
    else {
      sEingegeben = sEingegeben.trim()  + ",";
    }
    wsfEingabe.setText(sEingegeben + snr);
  }

  public JComponent getFirstFocusableComponent() {
    return jtTabelle;
  }
}
