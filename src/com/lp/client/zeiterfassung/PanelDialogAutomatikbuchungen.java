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
package com.lp.client.zeiterfassung;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Calendar;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.Helper;


/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Lieferschein Umsatz.</I></p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>13. 03. 2005</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelDialogAutomatikbuchungen
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Cache for convenience. */
  private InternalFrameZeiterfassung intFrame = null;
  /** Cache for convenience. */
  private TabbedPaneZeiterfassung tpZeiterfassung = null;
  private WrapperLabel wlaVon = new WrapperLabel();
  private WrapperDateField wdfVon = new WrapperDateField();
  private WrapperLabel wlaBis = new WrapperLabel();
  private WrapperDateField wdfBis = new WrapperDateField();
  private WrapperLabel wlaAuswahl = new WrapperLabel();
  private WrapperTextField wtfPersonal = new WrapperTextField();
  private WrapperCheckBox wcbPersonalAlle = new WrapperCheckBox();
  private WrapperCheckBox wcbAlteLoeschen = new WrapperCheckBox();
  private Integer personalIId = null;
  private WrapperDateRangeController wdrBereich = null;

  public PanelDialogAutomatikbuchungen(InternalFrame oInternalFrameI,
                                       String title)
      throws
      Throwable {
    super(oInternalFrameI, title);

    intFrame = (InternalFrameZeiterfassung) getInternalFrame();
    tpZeiterfassung = intFrame.getTabbedPaneZeiterfassung();

    jbInit();

    if (intFrame.getPersonalDto() != null) {
      wtfPersonal.setText(intFrame.getPersonalDto().formatAnrede());
      personalIId = intFrame.getPersonalDto().getIId();
    }
    wdrBereich.doClickDown();
    wdrBereich.doClickUp();

    setDefaults();
    initComponents();
  }


  /**
   * Dialog initialisieren
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    // die Gruppe mit nach Zeiteinheit
    wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.zeitraum")+" "+LPMain.getInstance().getTextRespectUISPr("lp.von"));
    wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
    wlaAuswahl.setText(LPMain.getInstance().getTextRespectUISPr(
        "zeiterfassung.report.monatsabrechnung.selektierteperson") + ":");
    wcbPersonalAlle.setText(LPMain.getInstance().getTextRespectUISPr("lp.alle"));
    wtfPersonal.setActivatable(false);
    wtfPersonal.setEditable(false);
    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
    wdfVon.setMandatoryField(true);
    wdfBis.setMandatoryField(true);
    wcbAlteLoeschen.setText(LPMain.getInstance().getTextRespectUISPr("zeiterfassung.alteautomatikbuchungenloeschen"));
    wcbAlteLoeschen.setSelected(true);
    wcbAlteLoeschen.setEnabled(false);



    jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, 5, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdfVon, new GridBagConstraints(2, 5, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaBis, new GridBagConstraints(3, 5, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdfBis, new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdrBereich,
                     new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 190),
                                            0, 0));

    jpaWorkingOn.add(wcbPersonalAlle, new GridBagConstraints(2, 4, 5, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(2, 2, 4, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaAuswahl, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        100, 0));

    jpaWorkingOn.add(wcbAlteLoeschen, new GridBagConstraints(2, 6, 4, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

  }



	public boolean pruefeObBuchungMoeglich() throws ExceptionLP, Throwable {
		boolean bRechtChefbuchhalter = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.ZEITBUCHUNGEN_NACHTRAEGLICH_BUCHEN_BIS,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		int iTag = (Integer) parameter.getCWertAsObject();

		Calendar cAktuelleZeit = Calendar.getInstance();
		cAktuelleZeit.setTimeInMillis(DelegateFactory.getInstance()
				.getSystemDelegate().getServerTimestamp().getTime());

		Calendar cBisDahinDarfGeaendertWerden = Calendar.getInstance();
		cBisDahinDarfGeaendertWerden.setTimeInMillis(Helper.cutTimestamp(
				DelegateFactory.getInstance().getSystemDelegate()
						.getServerTimestamp()).getTime());

		// Im aktuelle Monat darf geaendert werden
		cBisDahinDarfGeaendertWerden.set(Calendar.DAY_OF_MONTH, 1);

		if (cAktuelleZeit.get(Calendar.DAY_OF_MONTH) <= iTag) {
			// Im Vormonat darf geaendert werden
			cBisDahinDarfGeaendertWerden.set(Calendar.MONTH,
					cBisDahinDarfGeaendertWerden.get(Calendar.MONTH) - 1);
		}

		if (cBisDahinDarfGeaendertWerden.getTimeInMillis() > getTVon()
				.getTime()) {

			if (bRechtChefbuchhalter) {
				// Warnung anzeigen
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("pers.error.zeitbuchungenduerfenichtmehrgeaendertwerden.trotzdem"));
				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { Helper.formatDatum(
						cBisDahinDarfGeaendertWerden.getTime(), LPMain
								.getTheClient().getLocUi()) };
				String sMsg = mf.format(pattern);

				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(), sMsg,
						LPMain.getTextRespectUISPr("lp.warning"));
				if (b == false) {
					return false;
				}

			} else {
				// Fehler anzeigen
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("pers.error.zeitbuchungenduerfenichtmehrgeaendertwerden"));

				try {
					mf.setLocale(LPMain.getTheClient().getLocUi());
				} catch (Throwable ex) {
				}

				Object pattern[] = { Helper.formatDatum(
						cBisDahinDarfGeaendertWerden.getTime(), LPMain
								.getTheClient().getLocUi()) };

				String sMsg = mf.format(pattern);

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"), sMsg);

				return false;
			}

		}

		return true;
	}

  
  private void setDefaults()
      throws Throwable {
  }


  public Integer getPersonlIId() {
    return personalIId;
  }


  public Date getTVon() {
    return wdfVon.getDate();
  }


  public Date getTBis() {
    return wdfBis.getDate();
  }


  public boolean isAlleSelected() {
    return wcbPersonalAlle.isSelected();
  }


  public boolean isLoeschen() {
    return wcbAlteLoeschen.isSelected();
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      allMandatoryFieldsSetDlg();
     
    }

    // den Dialog verlassen
    super.eventActionSpecial(e);

    if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      tpZeiterfassung.gotoAuswahl();
    }
  }


  public FilterKriterium[] getAlleFilterKriterien()
      throws Throwable {
    return buildFilterKriterien();
  }
}
