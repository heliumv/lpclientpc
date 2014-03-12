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
package com.lp.client.util;

import java.util.Hashtable;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

@SuppressWarnings("static-access")
/**
 * <p>Logistik Pur ServiceLocator</p>
 * <p>Singleton Service Locator implementiert J2EE ServiceLocator Pattern.</p>
 * <br>Wird am Server zum Holen des local Interface verwendet.
 * <br>Wird am Client zum Holen des remote Interface verwendet.
 * <p>Copyright (c) 2004 All Rights Reserved.</p>
 * <p>Logistik Pur Software GmbH </p>
 *
 * @author uli walch
 * @version $Revision: 1.3 $
 */
public class ServiceLocator {
  private static ServiceLocator instance;
  private static InitialContext context = null;
  private ILPLogger myLogger = null;

  /**
   * Constructor.
   */
  private ServiceLocator() {
    super();

    init();
  }

  /**
   * Init Methode.
   */
  private void init() {
    myLogger = LPLogService.getInstance().getLogger(getClass());

    try {
      this.context = this.getInitialContext();
    }
    catch (Exception ex) {
      myLogger.error("init", ex);
    }
  }

  /**
   * Method for initializing the JNDI context for JBoss.
   *
   * @return InitialContext
   * @throws NamingException
   */
  private InitialContext getInitialContext() throws NamingException {
    Hashtable<?, ?> environment = null;

    //04.11.04: JO machen wir ueber startup ...
//    environment = new Hashtable();
//
//    environment.put(Context.INITIAL_CONTEXT_FACTORY,
//                    "org.jnp.interfaces.NamingContextFactory");
//    environment.put(Context.URL_PKG_PREFIXES,
//                    "org.jboss.naming:org.jnp.interfaces");
//    environment.put(Context.PROVIDER_URL, "jnp://localhost:1099");

    return new InitialContext(environment);
  }

  /**
   * Get the single instance of this class.
   *
   * @return ServiceLocator
   */
  public static ServiceLocator getInstance() {
    if (instance == null) {
      instance = new ServiceLocator();
    }

    return instance;
  }
  /**
   * Lookup fuer ein remote Interface.
   * @param ejbName String
   * @param ejbClass Class
   * @throws ServiceLocatorException
   * @return EJBHome
   */
  public static EJBHome getEjbHome(String ejbName, Class<?> ejbClass) throws
      ServiceLocatorException {
    try {
      Object object = context.lookup(ejbName);
      EJBHome ejbHome = null;
      ejbHome = (EJBHome) PortableRemoteObject.narrow(object, ejbClass);
      if (ejbHome == null) {
        throw new ServiceLocatorException("Could not get home for " + ejbName);
      }
      return ejbHome;
    }
    catch (NamingException ne) {
      throw new ServiceLocatorException(ne.getMessage());
    }
  }

  /**
   * Lookup fuer ein local interface. Praefix "java:comp/env" verwenden.
   * @param ejbName String
   * @throws ServiceLocatorException
   * @return EJBLocalHome
   */
  public static EJBLocalHome getEjbLocalHome(String ejbName) throws
      ServiceLocatorException {
    try {
      Object object = context.lookup(ejbName);
      EJBLocalHome ejbLocalHome = null;
      ejbLocalHome = (EJBLocalHome) object;
      if (ejbLocalHome == null) {
        throw new ServiceLocatorException("Could not get local home for " +
                                          ejbName);
      }
      return ejbLocalHome;
    }
    catch (NamingException ne) {
      throw new ServiceLocatorException(ne.getMessage());
    }
  }
}
