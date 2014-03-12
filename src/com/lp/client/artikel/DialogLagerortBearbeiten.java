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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access") 
public class DialogLagerortBearbeiten
    extends JDialog implements KeyListener, ActionListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JPanel panelUrlaubsanspruch = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  WrapperLabel wlaArtikelnummer = new WrapperLabel();
  WrapperTextField wtfArtikelnummer = new WrapperTextField();
  WrapperLabel wlaBezeichnung = new WrapperLabel();
  JTextField wtfBezeichnung = new JTextField();
  WrapperLabel wlaLagerplatz = new WrapperLabel();
  WrapperTextField wnfLagerplatz = new WrapperTextField();
  JButton wbuSpeichern = new JButton();
  WrapperLabel wlaLager = new WrapperLabel();
  WrapperTextField wtfLager = new WrapperTextField();

  ArtikellagerplaetzeDto artikellagerplaetzeDto = null;

  private ArtikelDto artikelDto = null;
  private Integer lagerIId = null;
  PanelQuery panelQueryArtikel = null;

  public DialogLagerortBearbeiten(Integer lagerIId, PanelQuery panelQueryArtikel)
      throws Throwable {
    super(LPMain.getInstance().getDesktop(),
          LPMain.getInstance().getTextRespectUISPr("artikel.lagerortbearbeiten"), true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        setVisible(false);
        dispose();
      }
    });

    this.lagerIId = lagerIId;
    this.panelQueryArtikel = panelQueryArtikel;
    jbInit();
    pack();
    wtfArtikelnummer.requestFocus();

  }


  private void jbInit()
      throws Exception {
    panelUrlaubsanspruch.setLayout(gridBagLayout1);

    wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.artikelnummer"));
    wlaLagerplatz.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.title.panel.lagerplatz"));
    wlaLager.setText(LPMain.getInstance().getTextRespectUISPr("label.lager"));

    wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
    wtfBezeichnung.setEnabled(false);

    wtfArtikelnummer.addKeyListener(this);
    wtfArtikelnummer.setMandatoryField(true);

    wnfLagerplatz.addKeyListener(this);
    wtfLager.setMandatoryField(true);
    wtfLager.setEditable(false);
    try {

      ArbeitsplatzparameterDto parameter = DelegateFactory.getInstance().
          getParameterDelegate().
          holeArbeitsplatzparameter(ParameterFac.
                                    ARBEITSPLATZPARAMETER_LAGERPLATZ_DIREKT_ERFASSEN);

      Integer lagerIId = new Integer(parameter.getCWert());

      wtfLager.setText(DelegateFactory.getInstance().getLagerDelegate().
                       lagerFindByPrimaryKey(lagerIId).getCNr());

    }
    catch (Throwable ex) {
      panelQueryArtikel.handleException(ex, true);
    }

    wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr("lp.speichern"));


    wbuSpeichern.addActionListener(this);
    this.getContentPane().setLayout(gridBagLayout2);

    this.getContentPane().add(panelUrlaubsanspruch,
                              new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 250,
        50));

    panelUrlaubsanspruch.add(wtfLager,
                             new GridBagConstraints(2, 0, 1, 1, 0.2, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    panelUrlaubsanspruch.add(wlaLager,
                             new GridBagConstraints(0, 0, 1, 1, 0.07, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    panelUrlaubsanspruch.add(wtfArtikelnummer,
                             new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    panelUrlaubsanspruch.add(wlaArtikelnummer,
                             new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    panelUrlaubsanspruch.add(wtfBezeichnung, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    panelUrlaubsanspruch.add(wlaBezeichnung, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    panelUrlaubsanspruch.add(wlaLagerplatz, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    panelUrlaubsanspruch.add(wnfLagerplatz, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    panelUrlaubsanspruch.add(wbuSpeichern, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2),
        100, 0));

  }


  public void keyPressed(KeyEvent e) {

    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      if (e.getSource() == wtfArtikelnummer) {

        try {

          artikelDto = DelegateFactory.getInstance().getArtikelDelegate().
              artikelFindByCNr(wtfArtikelnummer.getText());

          try {
            artikellagerplaetzeDto = DelegateFactory.getInstance().getLagerDelegate().
                artikellagerplaetzeFindByArtikelIIdLagerIId(artikelDto.getIId(), lagerIId);

            wnfLagerplatz.setText(artikellagerplaetzeDto.getLagerplatzDto().
                                  getCLagerplatz());

          }
          catch (ExceptionLP ex2) {
            if (ex2 instanceof ExceptionLP) {
              ExceptionLP exLP = (ExceptionLP) ex2;
              if (exLP.getICode() ==
                  EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
                //es wurde kein vorhandener Lagerplatz gefunden
              }
              else {
                panelQueryArtikel.handleException(ex2, true);
              }

            }
          }
          wnfLagerplatz.requestFocus();
        }
        catch (Throwable ex) {
          if (ex instanceof ExceptionLP) {
            ExceptionLP exLP = (ExceptionLP) ex;
            if (exLP.getICode() ==
                EJBExceptionLP.FEHLER_BEI_FIND) {
              DialogFactory.showModalDialog(LPMain.getInstance().
                                            getTextRespectUISPr("lp.error"),
                                            "Artikel konnte nicht gefunden werden.");
              wtfArtikelnummer.setText(null);
              wtfBezeichnung.setText(null);
              wtfArtikelnummer.requestFocus();
            }
            else {
              panelQueryArtikel.handleException(ex, true);
            }
          }

          else {
            panelQueryArtikel.handleException(ex, true);
          }

        }

        if (artikelDto != null) {
          wtfBezeichnung.setText(artikelDto.formatArtikelbezeichnung());

        }
      }

      else if (e.getSource() == wnfLagerplatz) {
        ActionEvent ev=new ActionEvent(wbuSpeichern,0,"");
        actionPerformed(ev);
      }

    }

  }


  public void keyReleased(KeyEvent e) {
  }


  public void keyTyped(KeyEvent e) {
  }


  public void actionPerformed(ActionEvent e) {

    if (e.getSource().equals(wbuSpeichern)) {
      if (wtfArtikelnummer.getText() == null) {
        DialogFactory.showModalDialog(LPMain.getInstance().
                                      getTextRespectUISPr("lp.error"),
                                      LPMain.getInstance().
                                      getTextRespectUISPr(
                                          "artikel.error.handlagerbewegung.artikellagerauswaehlen"));

        return;
      }
      try {

        if (artikellagerplaetzeDto != null) {
          //Dann update oder Delete
          if (wnfLagerplatz.getText() != null) {
            artikellagerplaetzeDto.getLagerplatzDto().setCLagerplatz(wnfLagerplatz.
                getText());
            DelegateFactory.getInstance().getLagerDelegate().updateArtikellagerplaetze(
                artikellagerplaetzeDto);
          }
          else {
            DelegateFactory.getInstance().getLagerDelegate().removeArtikellagerplaetze(
                artikellagerplaetzeDto);
          }
        }
        else {
          if (artikelDto != null) {
            //Neu anlegen
            ArtikellagerplaetzeDto artikellagerplaetzeDto = new ArtikellagerplaetzeDto();
            artikellagerplaetzeDto.setLagerplatzDto(new LagerplatzDto());
            artikellagerplaetzeDto.getLagerplatzDto().setCLagerplatz(wnfLagerplatz.
                getText());
            artikellagerplaetzeDto.getLagerplatzDto().setLagerIId(lagerIId);
            artikellagerplaetzeDto.setArtikelIId(artikelDto.getIId());
            DelegateFactory.getInstance().getLagerDelegate().createArtikellagerplaetze(
                artikellagerplaetzeDto);
          }
        }
      }
      catch (Throwable ex) {
        panelQueryArtikel.handleException(ex, false);
      }
      this.setVisible(false);

    }
  }

}
