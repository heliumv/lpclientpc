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

import java.awt.Component;
import java.awt.MenuComponent;


/**
 * <p>
 * Diese Klasse kuemmert sich um Events.
 * </p>
 *
 * <p>Beschreibung: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Organisation: LP </p>
 * @author $Author: valentin $
 * @version $Revision: 1.2 $
 */
public class ItemChangedEvent
    extends java.awt.AWTEvent
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// mache nichts!
  public final static int ACTION_JOB_DONE = 0;

  //zb. wurde auf einen anderen artikel in der tabelle geklickt.
  public final static int ITEM_CHANGED = 1;

  //gehe zum detailpanel.
  public final static int GOTO_DETAIL_PANEL = 2;

  //new was pushed.
  public final static int ACTION_NEW = 3;

  //print was pushed.
  public final static int ACTION_PRINT = 4;

  //print was pushed.
  public final static int ACTION_YOU_ARE_SELECTED = 5;

  //open was pushed.
  public final static int ACTION_OPEN = 6;

  //delete was pushed.
  public final static int ACTION_DELETE = 7;

  //i want to go to my defaultpanel; for instance delete in D2.
  public final static int ACTION_GOTO_MY_DEFAULT_QP = 8;

  //ein spezialbutton ruft
  public final static int ACTION_SPECIAL_BUTTON = 9;

  //update was pushed.
  public final static int ACTION_UPDATE = 10;

  //leeren was pushed.
  public final static int ACTION_LEEREN = 11;

  //ein zusaetzlicher neu button was pushed
  public final static int ACTION_MY_OWN_NEW = 12;

  //in einem PanelDialog wurde die Kriterienauswahl getroffen
  public final static int ACTION_KRITERIEN_HAVE_BEEN_SELECTED = 13;

  //Button discard was pushed
  public final static int ACTION_DISCARD = 14;

  //Button save was pushed
  public final static int ACTION_SAVE = 15;

  public final static int ACTION_POSITION_VONNNACHNMINUS1 = 16;

  public final static int ACTION_POSITION_VONNNACHNPLUS1 = 17;

  public final static int ACTION_POSITION_VORPOSITIONEINFUEGEN = 18;

  //ein "leave me alone" button ruft
  public final static int ACTION_LEAVE_ME_ALONE_BUTTON = 19;

  public final static int ACTION_TABLE_SELECTION_CHANGED = 20;

  public final static int ACTION_KOPIEREN = 21;

  public final static int ACTION_EINFUEGEN = 22;

  public final static int ACTION_ESCAPE = 23;

  public ItemChangedEvent(Object source, int idI) {
    super(source, idI);
  }

  public void setId(int iDI) {
    super.id = iDI;
  }


  /**
   * Returns a String representation of this object.
   *
   * @return String
   */
  public String toString() {
      String srcName = null;
      if (source instanceof Component) {
          srcName = ((Component)source).getName();
      } else if (source instanceof MenuComponent) {
          srcName = ((MenuComponent)source).getName();
      }
      String sAction = null;
      switch (id) {
        case ACTION_JOB_DONE: {
          sAction="ACTION_JOB_DONE";
          break;
        }
        case ITEM_CHANGED: {
          sAction="ITEM_CHANGED";
          break;
        }
        case GOTO_DETAIL_PANEL: {
          sAction="GOTO_DETAIL_PANEL";
          break;
        }
        case ACTION_NEW: {
          sAction="ACTION_NEW";
          break;
        }
        case ACTION_PRINT: {
          sAction="ACTION_PRINT";
          break;
        }
        case ACTION_YOU_ARE_SELECTED: {
          sAction="ACTION_YOU_ARE_SELECTED";
          break;
        }
        case ACTION_OPEN: {
          sAction="ACTION_OPEN";
          break;
        }
        case ACTION_DELETE: {
          sAction="ACTION_DELETE";
          break;
        }
        case ACTION_GOTO_MY_DEFAULT_QP: {
          sAction="ACTION_GOTO_MY_DEFAULT_QP";
          break;
        }
        case ACTION_SPECIAL_BUTTON: {
          sAction="ACTION_SPECIAL_BUTTON";
          break;
        }
        case ACTION_UPDATE: {
          sAction="ACTION_UPDATE";
          break;
        }
        case ACTION_LEEREN: {
          sAction="ACTION_LEEREN";
          break;
        }
        case ACTION_MY_OWN_NEW: {
          sAction="ACTION_MY_OWN_NEW";
          break;
        }
        case ACTION_KRITERIEN_HAVE_BEEN_SELECTED: {
          sAction="ACTION_KRITERIEN_HAVE_BEEN_SELECTED";
          break;
        }
        case ACTION_DISCARD: {
          sAction="ACTION_DISCARD";
          break;
        }
        case ACTION_SAVE: {
          sAction="ACTION_SAVE";
          break;
        }
        case ACTION_POSITION_VONNNACHNMINUS1: {
          sAction="ACTION_POSITION_VONNNACHNMINUS1";
          break;
        }
        case ACTION_POSITION_VONNNACHNPLUS1: {
          sAction="ACTION_POSITION_VONNNACHNPLUS1";
          break;
        }
        case ACTION_POSITION_VORPOSITIONEINFUEGEN: {
          sAction="ACTION_POSITION_VORPOSITIONEINFUEGEN";
          break;
        }
        case ACTION_LEAVE_ME_ALONE_BUTTON: {
          sAction="ACTION_LEAVE_ME_ALONE_BUTTON";
          break;
        }
      }
      String sUseCase=null;
      if(this.getSource() instanceof PanelQuery) {
        sUseCase = " UC=" + ( (PanelQuery)this.getSource()).getIdUsecase();
      }
      return (sAction != null ? sAction : "") + " on " +
          (srcName != null ? srcName : source.getClass().getName())+
          (sUseCase != null ? sUseCase : "");
  }
}
