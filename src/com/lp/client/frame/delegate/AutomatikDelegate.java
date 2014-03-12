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
package com.lp.client.frame.delegate;



import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.system.service.AutoPaternosterFac;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobFac;
import com.lp.server.system.service.AutomatikjobtypeDto;
import com.lp.server.system.service.AutomatikjobtypeFac;
import com.lp.server.system.service.AutomatiktimerDto;
import com.lp.server.system.service.AutomatiktimerFac;


public class AutomatikDelegate
    extends Delegate
{
  private Context context;
  private AutomatikjobFac automatikjobFac;
  private AutomatiktimerFac automatiktimerFac;
  private AutomatikjobtypeFac automatikjobtypeFac;
  private AutoPaternosterFac autopaternosterFac;

  public AutomatikDelegate()
      throws ExceptionLP {
    try {
    	context = new InitialContext();
      automatikjobFac = (AutomatikjobFac) context.lookup("lpserver/AutomatikjobFacBean/remote");
      automatiktimerFac = (AutomatiktimerFac) context.lookup("lpserver/AutomatiktimerFacBean/remote");
      automatikjobtypeFac = (AutomatikjobtypeFac) context.lookup("lpserver/AutomatikjobtypeFacBean/remote");
      autopaternosterFac = (AutoPaternosterFac) context.lookup("lpserver/AutoPaternosterFacBean/remote");
    }
    catch (Throwable t) {
      handleThrowable(t);
    }


  }


  public void updateAutomatikjobs(AutomatikjobDto[] automatikjobDtos)
      throws RemoteException {
    automatikjobFac.updateAutomatikjobs(automatikjobDtos);
  }

  public void updateAutomatikjob(AutomatikjobDto automatikjobDto)
      throws RemoteException {
    automatikjobFac.updateAutomatikjob(automatikjobDto);
  }

  public AutomatikjobDto automatikjobFindByPrimaryKey(Integer iId)
      throws FinderException, RemoteException {
    return automatikjobFac.automatikjobFindByPrimaryKey(iId);
  }

  public AutomatikjobDto automatikjobFindByISort(Integer iSort)
      throws FinderException, RemoteException {
    return automatikjobFac.automatikjobFindByISort(iSort);
  }

  public AutomatikjobDto[] automatikjobFindByBActive(Integer bActive)
      throws RemoteException, FinderException {
    return automatikjobFac.automatikjobFindByBActive(bActive);
  }

  public AutomatikjobDto[] automatikjobFindByBMonthjob(Integer bMonthjob)
      throws FinderException, RemoteException {
    return automatikjobFac.automatikjobFindByBMonthjob(bMonthjob);
  }

  public AutomatikjobDto[] automatikjobFindBydNextperform(Timestamp dNextPerform)
      throws FinderException, RemoteException {
    return automatikjobFac.automatikjobFindBydNextperform(dNextPerform);
  }

  public AutomatikjobDto automatikjobFindByCName(String cName)
      throws FinderException, RemoteException {
    return automatikjobFac.automatikjobFindByCName(cName);
  }

  public void updateAutomatiktimer(AutomatiktimerDto automatiktimerDto)
      throws RemoteException {
    automatiktimerFac.updateAutomatiktimer(automatiktimerDto);
  }

  public AutomatiktimerDto automatiktimerFindByPrimaryKey(Integer iId)
      throws FinderException, RemoteException {
    return automatiktimerFac.automatiktimerFindByPrimaryKey(iId);
  }

  public AutomatikjobtypeDto automatikjobtypeFindByPrimaryKey(Integer iId)
      throws FinderException, RemoteException {
    return automatikjobtypeFac.automatikjobtypeFindByPrimaryKey(iId);
  }

  public void setTimer(long millisTillPerform)
      throws RemoteException, EJBException {
    automatiktimerFac.setTimer(millisTillPerform);
    autopaternosterFac.setTimer(millisTillPerform);
  }
}
