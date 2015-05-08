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


import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;


/**
 * <p> Diese Klasse wrappt ein JMenuItem</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004 - 2007</p>
 *
 * <p>Erstellung: Martin Bluehweis; 18.09.07</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:39:21 $
 */
public class WrapperMenuItem
    extends JMenuItem
{
//  /**
//   * MB: den Konstruktor brauchen wir wahrscheinlich nicht (ist ausserdem leicht verwechselbar) . falls doch, bitte einfach "einkommentieren"
//   * @param rechtCNr String
//   * @throws Throwable
//   */
//  public WrapperMenuItem(String rechtCNr)
//      throws Throwable {
//    super();
//    workoutRechtCNr(rechtCNr);
//  }


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


public WrapperMenuItem(Icon icon, String rechtCNr)
      throws Throwable {
    super(icon);
    workoutRechtCNr(rechtCNr);
  }


  public WrapperMenuItem(String text, String rechtCNr)
      throws Throwable {
    super(text);
    workoutRechtCNr(rechtCNr);
  }


  public WrapperMenuItem(Action a, String rechtCNr)
      throws Throwable {
    super(a);
    workoutRechtCNr(rechtCNr);
  }


  public WrapperMenuItem(String text, Icon icon, String rechtCNr)
      throws Throwable {
    super(text, icon);
    workoutRechtCNr(rechtCNr);
  }


  public WrapperMenuItem(String text, int mnemonic, String rechtCNr)
      throws Throwable {
    super(text, mnemonic);
    workoutRechtCNr(rechtCNr);
  }


  private void workoutRechtCNr(String rechtCNr)
      throws Throwable {
    HelperClient.setToolTipTextMitRechtToComponent(this, rechtCNr);
    if (rechtCNr != null) {
      // Hat der Benutzer kein Recht, so ist der Menuepunkt nicht sichtbar.
      if (!DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(rechtCNr)) {
        this.setVisible(false);
      }
    }
  }
}
