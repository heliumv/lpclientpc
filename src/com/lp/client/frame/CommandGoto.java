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
package com.lp.client.frame;


/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 24.10.06</p>
 *
 * <p>@author $Author: heidi $</p>
 *
 * @version not attributable Date $Date: 2008/08/08 11:18:04 $
 */
public class CommandGoto
    extends Command
{
  private Class<?> classTabbedPane = null;
  private String sPanel = null;
  private String sAction = null;

  public CommandGoto(String sInternalFrameI,
                     Class<?> classTabbedPaneI,
                     String sPanelI,
                     String sActionI) {

    super(sInternalFrameI);

    classTabbedPane = classTabbedPaneI;
    sPanel = sPanelI;
    sAction = sActionI;
  }


  public void setTabbedPaneAsClass(Class<?> classTabbedPaneI) {
    this.classTabbedPane = classTabbedPaneI;
  }


  public void setCPanel(String sPanelI) {
    this.sPanel = sPanelI;
  }


  public Class<?> getTabbedPaneAsClass() {
    return classTabbedPane;
  }


  public String getsPanel() {
    return sPanel;
  }


  public void setSAction(String sAction) {
    this.sAction = sAction;
  }


  public String getSAction() {
    return sAction;
  }
}
