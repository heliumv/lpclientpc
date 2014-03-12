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
package com.lp.client.angebot;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;


/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Angebot Uebersicht.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>21. 01. 2005</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelDialogKriterienAngebotUebersicht
    extends PanelDialogKriterien {
	
		private static final long serialVersionUID = 1L;
		private InternalFrameAngebot intFrame = null;
		private TabbedPaneAngebot tpAngebot = null;
		
		private ButtonGroup jbgVertreter = null;
		private WrapperRadioButton wrbAlleVertreter = null;
		private WrapperRadioButton wrbEinVertreter = null;
	
		private WrapperButton wbuVertreter = null;
		private WrapperTextField wtfVertreter = null;
		private PersonalDto vertreterDto = null;
	
		private WrapperLabel wlaGeschaeftsjahr = null;
		private WrapperLabel wlaEmptyLabel3 = null;
		private WrapperComboBox wcoGeschaeftsjahr = null;
	
		private WrapperLabel wlaGeschaeftsjahresbeginn = null;
		private WrapperLabel wlaDatum = null;
	  
		private PanelQueryFLR panelQueryFLRVertreter = null;
	  
		private ButtonGroup jbgJahr = null;
		private WrapperRadioButton wrbKalenderjahr = null;
		private WrapperRadioButton wrbGeschaeftsjahr = null;
		private WrapperLabel wlaPeriode = null;
	
		private static final String ACTION_SPECIAL_FLR_VERTRETER =
			"action_special_flr_vertreter";
		static final private String ACTION_RADIOBUTTON_ALLE_VERTRETER =
      "action_radiobutton_alle_vertreter";
		static final private String ACTION_RADIOBUTTON_EIN_VERTRETER =
      "action_radiobutton_ein_vertreter";
	
	public PanelDialogKriterienAngebotUebersicht(InternalFrame oInternalFrameI,
	                                               String title)
	      throws
	      Throwable {
	    super(oInternalFrameI, title);
	
	    intFrame = (InternalFrameAngebot) getInternalFrame();
	    tpAngebot = intFrame.getTabbedPaneAngebot();
	
	    jbInit();
	    setDefaults();
	    initComponents();
	  }


  /**
   * Dialog initialisieren
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    getInternalFrame().addItemChangedListener(this);

    wrbAlleVertreter = new WrapperRadioButton(LPMain.getTextRespectUISPr("lp.allevertreter"));
    wrbAlleVertreter.setActionCommand(ACTION_RADIOBUTTON_ALLE_VERTRETER);
    wrbAlleVertreter.addActionListener(this);

    wrbEinVertreter = new WrapperRadioButton(LPMain.getTextRespectUISPr("lp.einvertreter"));
    wrbEinVertreter.setActionCommand(ACTION_RADIOBUTTON_EIN_VERTRETER);
    wrbEinVertreter.addActionListener(this);

    jbgVertreter = new ButtonGroup();
    jbgVertreter.add(wrbAlleVertreter);
    jbgVertreter.add(wrbEinVertreter);

    wbuVertreter = new WrapperButton();
    HelperClient.setDefaultsToComponent(wbuVertreter, 120);
    wbuVertreter.setText(LPMain.getTextRespectUISPr(
        "button.vertreter"));
    wbuVertreter.setActionCommand(ACTION_SPECIAL_FLR_VERTRETER);
    wbuVertreter.addActionListener(this);

    wtfVertreter = new WrapperTextField();
    HelperClient.setDefaultsToComponent(wtfVertreter, 300);
    wtfVertreter.setActivatable(false);
    wtfVertreter.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

    wlaGeschaeftsjahr = new WrapperLabel(
        LPMain.getTextRespectUISPr("label.geschaeftsjahr"));
    wlaGeschaeftsjahr.setHorizontalAlignment(SwingConstants.LEADING);
    wlaEmptyLabel3 = new WrapperLabel();
    wcoGeschaeftsjahr = new WrapperComboBox();
    wcoGeschaeftsjahr.setMandatoryFieldDB(true);

    wlaGeschaeftsjahresbeginn = new WrapperLabel(LPMain.getTextRespectUISPr(
        "lp.geschaeftsjahresbeginn"));
    wlaGeschaeftsjahresbeginn.setHorizontalAlignment(SwingConstants.LEADING);
    wlaDatum = new WrapperLabel();
    wlaDatum.setHorizontalAlignment(SwingConstants.LEADING);
    
    
    wlaPeriode = new WrapperLabel(
            LPMain.getTextRespectUISPr("label.periode"));
    wlaPeriode.setHorizontalAlignment(SwingConstants.LEADING);

    wrbKalenderjahr = new WrapperRadioButton();
    wrbKalenderjahr.setText(
        LPMain.getTextRespectUISPr("label.kalenderjahr"));

    wrbGeschaeftsjahr = new WrapperRadioButton();
    wrbGeschaeftsjahr.setText(
        LPMain.getTextRespectUISPr("label.geschaeftsjahr"));
      jbgJahr=new ButtonGroup();
    jbgJahr.add(wrbKalenderjahr);
    jbgJahr.add(wrbGeschaeftsjahr);


    iZeile++;
    jpaWorkingOn.add(wrbAlleVertreter,
                     new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wrbEinVertreter,
                     new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaEmptyLabel3,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wbuVertreter,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfVertreter,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaPeriode,
                     new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbKalenderjahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbGeschaeftsjahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));


    iZeile++;
    jpaWorkingOn.add(wcoGeschaeftsjahr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaGeschaeftsjahresbeginn,
                     new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaDatum,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  private void setDefaults()
      throws Throwable {
    vertreterDto = new PersonalDto();

    // @todo es gibt gemeinsame Elemente mit PanelDialogKriterienAuftragUebersicht  PJ 4909
    GregorianCalendar gcReferenz = new GregorianCalendar();

    wrbAlleVertreter.setSelected(true);
    wbuVertreter.setEnabled(false);

    wcoGeschaeftsjahr.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllGeschaeftsjahr());
    // Default ist das aktuelle GJ
    wcoGeschaeftsjahr.setKeyOfSelectedItem(DelegateFactory.getInstance().getParameterDelegate().
                                           getGeschaeftsjahr());

    ParametermandantDto pmBeginnMonat = DelegateFactory.getInstance().getParameterDelegate().
        getMandantparameter(
          LPMain.getTheClient().getMandant(),
          ParameterFac.KATEGORIE_ALLGEMEIN,
          ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);

    // die Monate im GregorianCalendar beginnen mit 0
    int iIndexBeginnMonat = ((Integer) pmBeginnMonat.getCWertAsObject()).intValue() - 1;

    String[] defaultMonths;
    DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getTheClient().getLocUi());
    defaultMonths = symbols.getMonths();

    String sNameMonat = defaultMonths[iIndexBeginnMonat];

    wlaDatum.setText("1. " + sNameMonat + " " + gcReferenz.get(Calendar.YEAR));
    wrbGeschaeftsjahr.setSelected(true);
  }

  /**
   * Die gewaehlten Kriterien zusammenbauen.
   * <br>Es gilt fuer Angebot Uebersicht:
   * <br>Krit1 : Geschaeftsjahr = Auswahl Geschaeftsjahr
   * <br>Krit2 : Vertreter = null oder i_id; wenn null erfolgt die Auswertung ueber alle Vertreter
   * @return FilterKriterium[] die Kriterien des Benutzers
   * @throws java.lang.Throwable Ausnahme
   */
  public FilterKriterium[] buildFilterKriterien() throws Throwable {
    aAlleKriterien = new FilterKriterium[AngebotFac.ANZAHL_KRITERIEN];
    FilterKriterium fkjahr = null;
    
    if(wrbKalenderjahr.isSelected()) {
        // Kalenderjahr
         fkjahr = new FilterKriterium(
              AngebotFac.KRIT_UEBERSICHT_KALENDERJAHR,
              true,
              wcoGeschaeftsjahr.getKeyOfSelectedItem().toString(),
              FilterKriterium.OPERATOR_EQUAL,
              false);
      }
      if(wrbGeschaeftsjahr.isSelected()) {
    	    // Geschaeftsjahr
    	 fkjahr = new FilterKriterium(
    	          AngebotFac.KRIT_UEBERSICHT_GESCHAEFTSJAHR,
    	          true,
    	          wcoGeschaeftsjahr.getKeyOfSelectedItem().toString(),
    	          FilterKriterium.OPERATOR_EQUAL,
    	          false);

      }
    aAlleKriterien[AngebotFac.IDX_KRIT_GESCHAEFTSJAHR] = fkjahr;

    // alle oder ein Vertreter
    String cVertreterIId = null;

    if (wrbEinVertreter.isSelected()) {
      cVertreterIId = vertreterDto.getIId().toString();
    }
    else {
      vertreterDto = new PersonalDto();
    }

    FilterKriterium fkVertreter = new FilterKriterium(
          AngebotFac.KRIT_UEBERSICHT_VERTRETER_I_ID,
          true,
          cVertreterIId,
          FilterKriterium.OPERATOR_EQUAL,
          false);

    aAlleKriterien[AngebotFac.IDX_KRIT_VERTRETER_I_ID] = fkVertreter;

    return aAlleKriterien;
  }

  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_VERTRETER)) {
      dialogQueryVertreter(e);
    }
    else if (e.getActionCommand().equals(ACTION_RADIOBUTTON_ALLE_VERTRETER)) {
      wbuVertreter.setEnabled(false);
      wtfVertreter.setText("");
      wtfVertreter.setMandatoryField(false);
      vertreterDto = new PersonalDto();
    }
    else if (e.getActionCommand().equals(ACTION_RADIOBUTTON_EIN_VERTRETER)) {
      wbuVertreter.setEnabled(true);
      wtfVertreter.setMandatoryField(true);
    }
    else {
      if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
        buildFilterKriterien();
      }

      // den Dialog verlassen
      super.eventActionSpecial(e);

      if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
        tpAngebot.setBKriterienAngebotUebersichtUeberMenueAufgerufen(false);
        tpAngebot.gotoAuswahl();
      }
    }
  }

  private void dialogQueryVertreter(ActionEvent e)
      throws Throwable {
    panelQueryFLRVertreter =
        PersonalFilterFactory.getInstance().createPanelFLRPersonal(intFrame, true, false, vertreterDto.getIId());

    DialogQuery dialogQueryVertreter = new DialogQuery(panelQueryFLRVertreter);
  }

  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRVertreter) {
        Integer iIdPersonal = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();

        vertreterDto = DelegateFactory.getInstance().getPersonalDelegate().
            personalFindByPrimaryKey(iIdPersonal);

        if (vertreterDto != null && vertreterDto.getIId() != null) {
          wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
        }
      }
    }
  }

  public FilterKriterium[] getAlleFilterKriterien() throws Throwable {
    return buildFilterKriterien();
  }
}
