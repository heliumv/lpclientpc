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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;


public class WrapperDateRangeController
    extends JPanel implements ActionListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperDateField wdfVon = null;
  private WrapperDateField wdfBis = null;
  private GridBagLayout gridBagLayout = null;
  private JButton wbuUp = null;
  private JButton wbuDown = null;
  private final static String ACTION_UP = "up";
  private final static String ACTION_DOWN = "down";

  private ImageIcon imageIconUp = null;
  private ImageIcon imageIconDown = null;

  public WrapperDateRangeController(WrapperDateField wdfVon, WrapperDateField wdfBis) {
    this.wdfVon = wdfVon;
    this.wdfBis = wdfBis;
    jbInit();
  }


  /**
   * jbInit
   */
  private void jbInit() {
    gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);
    wbuUp = new JButton();
    wbuDown = new JButton();
    int width = Defaults.getInstance().getControlHeight();

    wbuUp.setMinimumSize(new Dimension(width, 2));
    wbuUp.setPreferredSize(new Dimension(width, 2));
    wbuUp.setMaximumSize(new Dimension(width, 2));
    wbuDown.setMinimumSize(new Dimension(width, 2));
    wbuDown.setPreferredSize(new Dimension(width, 2));
    wbuDown.setMaximumSize(new Dimension(width, 2));
    wbuUp.setIcon(getImageIconUp());
    wbuDown.setIcon(getImageIconDown());

    this.add(wbuDown,
             new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(wbuUp,
             new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    wbuUp.addActionListener(this);
    wbuDown.addActionListener(this);
    wbuUp.setActionCommand(ACTION_UP);
    wbuDown.setActionCommand(ACTION_DOWN);
  }


  public void actionPerformed(ActionEvent e) {
    java.util.Date dAktuell = wdfVon.getDate();


    if (dAktuell == null) {
      GregorianCalendar gcHelp = new GregorianCalendar();
      // default ist der vormonat
      gcHelp.set(Calendar.MONTH, gcHelp.get(Calendar.MONTH) - 1);
      dAktuell = gcHelp.getTime();
    }

    GregorianCalendar gcNeu = new GregorianCalendar();
    gcNeu.setTime(dAktuell);


    //Wenn Shift gedrueckt wird, dann wir da ganzes Jahr veraendert
    if (e.getModifiers() == 17) {

      gcNeu.set(Calendar.DAY_OF_MONTH, 1);
      gcNeu.set(Calendar.MONTH, 0);
      if (e.getActionCommand().equalsIgnoreCase(ACTION_DOWN)) {
        gcNeu.set(Calendar.YEAR, gcNeu.get(Calendar.YEAR) + 1);
      }
      else {
        gcNeu.set(Calendar.YEAR, gcNeu.get(Calendar.YEAR) - 1);
      }

      wdfVon.setDate(gcNeu.getTime());
      gcNeu.set(Calendar.MONTH, 11);
      gcNeu.set(Calendar.DAY_OF_MONTH, 31);
      wdfBis.setDate(gcNeu.getTime());
    }
    else {
      if (gcNeu.get(Calendar.DATE) >= 1) {
        gcNeu.set(Calendar.DAY_OF_MONTH, 1);
        if (e.getActionCommand().equalsIgnoreCase(ACTION_UP)) {
          gcNeu.set(Calendar.MONTH, gcNeu.get(Calendar.MONTH) + 1);
        }
//        else if (e.getActionCommand().equalsIgnoreCase(ACTION_DOWN)) {
//
//        }
      }
      if (e.getActionCommand().equalsIgnoreCase(ACTION_UP)) {
        gcNeu.set(Calendar.MONTH, gcNeu.get(Calendar.MONTH) - 2);
      }
      else if (e.getActionCommand().equalsIgnoreCase(ACTION_DOWN)) {
        gcNeu.set(Calendar.MONTH, gcNeu.get(Calendar.MONTH) + 1);
      }
      wdfVon.setDate(gcNeu.getTime());
      gcNeu.set(Calendar.MONTH, gcNeu.get(Calendar.MONTH) + 1);
      gcNeu.set(Calendar.DATE, gcNeu.get(Calendar.DATE) - 1);
      wdfBis.setDate(gcNeu.getTime());
    }
  }


  public void doClickUp() {
    wbuUp.doClick();
  }


  public void doClickDown() {
    wbuDown.doClick();
  }


  private ImageIcon getImageIconUp() {
    if (imageIconUp == null) {
      imageIconUp = new ImageIcon(getClass().getResource(
          "/com/lp/client/res/navigate_close.png"));
    }
    return imageIconUp;
  }


  private ImageIcon getImageIconDown() {
    if (imageIconDown == null) {
      imageIconDown = new ImageIcon(getClass().getResource(
          "/com/lp/client/res/navigate_open.png"));
    }
    return imageIconDown;
  }

}
