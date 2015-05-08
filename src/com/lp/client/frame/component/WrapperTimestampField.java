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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.util.Helper;


/**
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>13.03.05</I></p>
 *
 * <p> </p>
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class WrapperTimestampField
    extends JPanel implements IControl
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperDateField wdfDatum = null;
  public WrapperDateField getWdfDatum() {
	return wdfDatum;
}


private WrapperTimeField wtfZeit = null;
  public WrapperTimeField getWtfZeit() {
	return wtfZeit;
}


private GridBagLayout gridbagLayoutAll = null;
  private GregorianCalendar gcAlteZeit = new GregorianCalendar();

  public WrapperTimestampField() {
    HelperClient.setDefaultsToComponent(this);
    jbInit();
  }


  private boolean showSeconds;

  /**
   * jbInit
   *
   * @throws Throwable
   */
  private void jbInit() {
    wdfDatum = new WrapperDateField();
    wtfZeit = new WrapperTimeField();
    HelperClient.setDefaultsToComponent(wdfDatum);
    HelperClient.setDefaultsToComponent(wtfZeit);
    gridbagLayoutAll = new GridBagLayout();
    this.setLayout(gridbagLayoutAll);
    this.add(wdfDatum,
             new GridBagConstraints(0, 0, 1, 1, 0.6, 0.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(wtfZeit,
             new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
  }


  public boolean isMandatoryField() {
    return wdfDatum.isMandatoryField() || wtfZeit.isMandatoryField();
  }


  public void setMandatoryField(boolean isMandatoryField) {
    wdfDatum.setMandatoryField(isMandatoryField);
    wtfZeit.setMandatoryField(isMandatoryField);
  }


  public boolean isActivatable() {
    return wdfDatum.isActivatable() || wtfZeit.isActivatable();
  }


  public void setActivatable(boolean isActivatable) {
    wdfDatum.setActivatable(isActivatable);
    wtfZeit.setActivatable(isActivatable);
  }


  public void removeContent() {
    wdfDatum.removeContent();
    wtfZeit.removeContent();
  }


  public void setTimestamp(Timestamp ts) {
    wdfDatum.setTimestamp(ts);
    if (ts != null) {
      wtfZeit.setTime(new Time(ts.getTime()));
    }
    else {
      wtfZeit.setTime(null);
    }
  }


  public Timestamp getTimestamp()
      throws ExceptionLP {
    long spinnerHelper = wtfZeit.getTime().getTime();
    GregorianCalendar gcDatum = new GregorianCalendar();
    GregorianCalendar gcZeit = new GregorianCalendar();
    if (wdfDatum.getDate() == null) {
      return null;
    }
    gcZeit.setTimeInMillis(spinnerHelper);
    gcDatum.setTime(Helper.cutDate(wdfDatum.getDate()));
    // die uhrzeit zum datum uebertragen
    gcDatum.set(GregorianCalendar.HOUR, gcZeit.get(GregorianCalendar.HOUR_OF_DAY));
    gcDatum.set(GregorianCalendar.MINUTE, gcZeit.get(GregorianCalendar.MINUTE));
    gcDatum.set(GregorianCalendar.SECOND, gcZeit.get(GregorianCalendar.SECOND));
    gcAlteZeit.set(GregorianCalendar.HOUR, gcZeit.get(GregorianCalendar.HOUR_OF_DAY));
    gcAlteZeit.set(GregorianCalendar.MINUTE, gcZeit.get(GregorianCalendar.MINUTE));
    gcAlteZeit.set(GregorianCalendar.SECOND, gcZeit.get(GregorianCalendar.SECOND));

    return new Timestamp(gcDatum.getTimeInMillis());
}


  public void setEditable(boolean bEnabled) {
    wdfDatum.setEnabled(bEnabled);
    wtfZeit.setEnabled(bEnabled);
  }


  public void setShowSeconds(boolean showSeconds) {
    this.showSeconds = showSeconds;
    wtfZeit.setShowSeconds(showSeconds);
  }


  public boolean isShowSeconds() {
    return showSeconds;
  }

  public void setShowDate(boolean showDate){
    wdfDatum.setVisible(showDate);
  }

  public boolean isShowDate(){
    return wdfDatum.isVisible();
  }
	@Override
	public boolean hasContent() throws Throwable {
	    return wdfDatum.hasContent() && wtfZeit.hasContent();
	}
}
