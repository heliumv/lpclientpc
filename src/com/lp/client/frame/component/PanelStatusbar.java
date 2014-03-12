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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.util.Helper;


/**
 * <p><I>Panel statusbar</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>28.01.2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelStatusbar
    extends JPanel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private JPanel jpaWorkingOn = new JPanel();
  private WrapperLabel wlaSpalte1 = null;
  private WrapperLabel wlaSpalte4 = null;
  private WrapperLabel wlaSpalte3 = null;
  private WrapperLabel wlaSpalte0 = null;
  private WrapperLabel wlaSpalte5 = null;
  private WrapperLabel wlaSpalte6 = null;
  private WrapperLabel wlaSpalte2 = null;
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private final static int iHeight = 18;
  private ImageIcon imageIconLocked = null;

  public PanelStatusbar()
      throws Throwable {
    jbInit();
  }


  private void jbInit()
      throws Throwable {
    Border borderStatusbarFeld = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
        Color.white,
        Color.white,
        new Color(115, 114, 105), /** @todo JO 18.03.06 ->lp.properties  PJ 5108 */
        new Color(165, 163, 151));
    jpaWorkingOn = new JPanel();
    wlaSpalte0 = new WrapperLabel();
    wlaSpalte1 = new WrapperLabel();
    wlaSpalte2 = new WrapperLabel();
    wlaSpalte3 = new WrapperLabel();
    wlaSpalte4 = new WrapperLabel();
    wlaSpalte5 = new WrapperLabel();
    wlaSpalte6 = new WrapperLabel();
    gridBagLayout = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayout);
    //jpaWorkingOn.setBackground(SystemColor.activeCaptionBorder);
    jpaWorkingOn.setBorder(BorderFactory.createEmptyBorder());
    this.setLayout(gridBagLayout1);
    wlaSpalte0.setMaximumSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
    wlaSpalte0.setMinimumSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
    wlaSpalte0.setPreferredSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
    wlaSpalte1.setMaximumSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
    wlaSpalte1.setMinimumSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
    wlaSpalte1.setPreferredSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
    wlaSpalte2.setMaximumSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
    wlaSpalte2.setMinimumSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
    wlaSpalte2.setPreferredSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
    wlaSpalte3.setMaximumSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
    wlaSpalte3.setMinimumSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
    wlaSpalte3.setPreferredSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
    wlaSpalte4.setMaximumSize(new Dimension(10000, iHeight));
    wlaSpalte4.setMinimumSize(new Dimension(10, iHeight));
    wlaSpalte4.setPreferredSize(new Dimension(10, iHeight));
    wlaSpalte5.setMaximumSize(new Dimension(10000, iHeight));
    wlaSpalte5.setMinimumSize(new Dimension(10, iHeight));
    wlaSpalte5.setPreferredSize(new Dimension(10, iHeight));
    wlaSpalte6.setMaximumSize(new Dimension(10000, iHeight));
    wlaSpalte6.setMinimumSize(new Dimension(10, iHeight));
    wlaSpalte6.setPreferredSize(new Dimension(10, iHeight));
    wlaSpalte0.setBorder(borderStatusbarFeld);
    wlaSpalte1.setBorder(borderStatusbarFeld);
    wlaSpalte2.setBorder(borderStatusbarFeld);
    wlaSpalte3.setBorder(borderStatusbarFeld);
    wlaSpalte4.setBorder(borderStatusbarFeld);
    wlaSpalte5.setBorder(borderStatusbarFeld);
    wlaSpalte6.setBorder(borderStatusbarFeld);
    Font defaultFont = HelperClient.getDefaultFont();
    // ca. 90% der standardgroesse
    Font statusbarFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(),
                                  (defaultFont.getSize() * 9) / 10);
    wlaSpalte0.setFont(statusbarFont);
    wlaSpalte1.setFont(statusbarFont);
    wlaSpalte2.setFont(statusbarFont);
    wlaSpalte3.setFont(statusbarFont);
    wlaSpalte4.setFont(statusbarFont);
    wlaSpalte5.setFont(statusbarFont);
    wlaSpalte6.setFont(statusbarFont);
    wlaSpalte0.setHorizontalAlignment(SwingConstants.CENTER);
    wlaSpalte1.setHorizontalAlignment(SwingConstants.CENTER);
    wlaSpalte2.setHorizontalAlignment(SwingConstants.CENTER);
    wlaSpalte3.setHorizontalAlignment(SwingConstants.CENTER);
    wlaSpalte4.setHorizontalAlignment(SwingConstants.LEFT);
    wlaSpalte5.setHorizontalAlignment(SwingConstants.LEFT);
    wlaSpalte6.setHorizontalAlignment(SwingConstants.LEFT);

    jpaWorkingOn.add(wlaSpalte0, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 2), 0, 0));
    jpaWorkingOn.add(wlaSpalte1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 2), 0, 0));
    jpaWorkingOn.add(wlaSpalte2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 2), 0, 0));
    jpaWorkingOn.add(wlaSpalte3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 2), 0, 0));
    jpaWorkingOn.add(wlaSpalte4, new GridBagConstraints(4, 0, 1, 1, 0.3, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 2), 0, 0));
    jpaWorkingOn.add(wlaSpalte5, new GridBagConstraints(5, 0, 1, 1, 0.3, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 2), 0, 0));
    jpaWorkingOn.add(wlaSpalte6, new GridBagConstraints(6, 0, 1, 1, 0.4, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 2), 0, 0));
    this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
  }


  /**
   * Einen Wert in ein Label setzen.
   *
   * @param iWhichI int
   * @param valueLblI Object
   */
  private void setValueAt(int iWhichI, Object valueLblI) {
    String sText = null;
    if (valueLblI == null) {
      sText = "";
    }
    else if (valueLblI instanceof String) {
      sText = (String) valueLblI;
    }
    else if (valueLblI instanceof java.util.Date) {
      java.util.Date dDatum = (java.util.Date) valueLblI;
      sText = Helper.formatDatum(dDatum, Defaults.getInstance().getLocUI());
    }
    else if (valueLblI instanceof java.sql.Timestamp) {
      java.util.Date dDatum = new java.sql.Timestamp( ( (java.util.Date)
          valueLblI).getTime());
      sText = Helper.formatDatum(dDatum, Defaults.getInstance().getLocUI());
    }
    else {
      sText = valueLblI.toString();
    }

    switch (iWhichI) {
      case 0:
        wlaSpalte0.setText(sText);
        break;
      case 1:
        wlaSpalte1.setText(sText);
        break;
      case 2:
        wlaSpalte2.setText(sText);
        break;
      case 3:
        wlaSpalte3.setText(sText);
        break;
      case 4:
        wlaSpalte4.setText(sText);
        break;
      case 5:
        wlaSpalte5.setText(sText);
        break;
      default:
        wlaSpalte0.setText("0 >" + iWhichI + "> 5 sorry");
    }
  }


  /**
   * Leeren der StatusBar.
   */
  public void clearStatusbar() {
    for (int i = 0; i <= 5; i++) {
      this.setValueAt(i, null);
    }
  }


  public void setLockField(String sText) {
    if (sText == null || sText.equals("")) {
      wlaSpalte6.setIcon(null);
      wlaSpalte6.setText("");
    }
    else {
      wlaSpalte6.setIcon(getImageIconLocked());
      wlaSpalte6.setText(sText);
    }
  }

  public void setLockFieldWithoutIcon(String sText) {
    if (sText == null || sText.equals("")) {
      wlaSpalte6.setIcon(null);
      wlaSpalte6.setText("");
    }
    else {
      wlaSpalte6.setIcon(null);
      wlaSpalte6.setText(sText);
    }
  }



  private ImageIcon getImageIconLocked() {
    if (imageIconLocked == null) {
      imageIconLocked = new ImageIcon(getClass().
                                      getResource(
                                          "/com/lp/client/res/lock.png"));
    }
    return imageIconLocked;
  }


  void setPersonalIIdAnlegen(Integer personalIIdAnlegen)
      throws Throwable {
    this.setValueAt(0, Defaults.getInstance().getPersonalKurzzeichen(personalIIdAnlegen));
  }


  void setTAnlegen(Timestamp tAnlegen) {
    this.setValueAt(1, tAnlegen);
  }


  void setPersonalIIdAendern(Integer personalIIdAendern)
      throws Throwable {
    this.setValueAt(2, Defaults.getInstance().getPersonalKurzzeichen(personalIIdAendern));
  }


  void setTAendern(Timestamp tAendern) {
    this.setValueAt(3, tAendern);
  }


  public void addToSpalteStatus(String sToAdd){
	  
	  String s=wlaSpalte4.getText();
	  if(s!=null){
		  this.setValueAt(4,s.trim()+sToAdd);
	  } else {
		  this.setValueAt(4,sToAdd); 
	  }
		  
  }
  
  public void setStatusCNr(String statusCNr)
      throws Throwable {
    String sUebersetzung = null;
    if (statusCNr != null) {
      sUebersetzung = Defaults.getInstance().getStatusUebersetzt(statusCNr);
    }
    this.setValueAt(4, sUebersetzung);
  }


  void setSpalte5(Object o) {
    this.setValueAt(5, o);
  }

  void setSpalte4(Object o) {
    this.setValueAt(4, o);
  }

  void setTextColorSpalte5(Color color){
    wlaSpalte5.setForeground(color);
  }

}
