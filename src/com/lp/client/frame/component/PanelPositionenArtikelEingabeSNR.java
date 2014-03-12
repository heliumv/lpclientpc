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
package com.lp.client.frame.component;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
public class PanelPositionenArtikelEingabeSNR
    extends PanelPositionenArtikelVerkauf
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public WrapperSNRField wtfSeriennr = null;
  public WrapperButton wbuSeriennr = null;
  static final public String ACTION_SPECIAL_SNRAUSWAHL =
      "action_snrauswahl";

  public PanelPositionenArtikelEingabeSNR(InternalFrame internalFrame,
                                          String add2TitleI,
                                          Object key,
                                          String sLockMeWer,
                                          int iSpaltenbreite1I)
      throws Throwable {
    super(internalFrame, add2TitleI, key, sLockMeWer, iSpaltenbreite1I); // VKPF
    jbInitPanel();
    initComponents();
  }


  private void jbInitPanel()
      throws Throwable {
    wtfSeriennr = new WrapperSNRField();
    wtfSeriennr.setEditable(false);
    ParametermandantDto parameter =
        DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
            LPMain.getInstance().getTheClient().getMandant(),
            ParameterFac.KATEGORIE_AUFTRAG,
            ParameterFac.PARAMETER_AUFTRAGSERIENNUMMERN_MIT_ETIKETTEN);

    Short sValue = new Short(parameter.getCWert());
    if (Helper.short2boolean(sValue)) {
      wtfSeriennr.setMandatoryField(false);
     }else{
      wtfSeriennr.setMandatoryField(true);
     }
    wbuSeriennr = new WrapperButton();
    wbuSeriennr.addActionListener(this);
    wbuSeriennr.setActionCommand(ACTION_SPECIAL_SNRAUSWAHL);
    wbuSeriennr.setText(LPMain.getInstance().getTextRespectUISPr("artikel.report.seriennummern")+"...");
    wbuSeriennr.setEnabled(false);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    super.eventActionSpecial(e);
    if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
        MandantFac.
        ZUSATZFUNKTION_AUFTRAG_SERIENNUMMERN)) {
      aktualisiereFelderSnrChnr(true);
    }
    if (e.getActionCommand().equals(ACTION_SPECIAL_SNRAUSWAHL)) {
      InternalFrameAuftrag intFrame = (InternalFrameAuftrag)getInternalFrame();
      DialogAuftragSnrauswahl d = new DialogAuftragSnrauswahl(intFrame,
          intFrame.getTabbedPaneAuftrag(),
          (Integer)intFrame.getTabbedPaneAuftrag().getAuftragPositionenTop().getSelectedId());
      java.awt.Dimension panelSize = this.getSize();
      d.setMaximumSize(panelSize);
      d.setMinimumSize(panelSize);
      d.setPreferredSize(panelSize);
      Point p = this.getLocation();
      d.setLocation(p);
      d.setVisible(true);
     }

  }


  public void aktualisiereFelderSnrChnr(boolean bEnableField) {
    if(getArtikelDto() != null && getArtikelDto().getIId() != null ) {
      if (Helper.short2boolean(getArtikelDto().getBSeriennrtragend())) {
        add(wbuSeriennr,
            new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
                                   , GridBagConstraints.WEST,
                                   GridBagConstraints.BOTH,
                                   new Insets(2, 2, 2, 2),
                                   0, 0));
        add(wtfSeriennr,
            new GridBagConstraints(1, 8, 3, 1, 0.0, 0.0
                                   , GridBagConstraints.WEST,
                                   GridBagConstraints.BOTH,
                                   new Insets(2, 2, 2, 2),
                                   0, 0));
      }
      else {
        remove(wtfSeriennr);
      }
      if (bEnableField) {
        wtfSeriennr.setEditable(bEnableField);
        wbuSeriennr.setEnabled(bEnableField);
      }
    } else {
      remove(wtfSeriennr);
    }
  }



  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    super.eventItemchanged(eI);
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

    }
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
      super.eventYouAreSelected(false); // buttons schalten

}


    public void artikelDto2components()
        throws Throwable {
      super.artikelDto2components();
      if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
          MandantFac.
          ZUSATZFUNKTION_AUFTRAG_SERIENNUMMERN)) {
        aktualisiereFelderSnrChnr(true);
      }

    }

}
