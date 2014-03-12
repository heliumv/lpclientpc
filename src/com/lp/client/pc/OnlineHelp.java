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
package com.lp.client.pc;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.JHelp;
import javax.help.SwingHelpUtilities;
import javax.swing.JPanel;


/**
 * <p> Diese Klasse kuemmert sich um
 * Anzeige der Helium Benutzerdokumentation als Onlinehilfe</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Markus Rabenberger 22.01.07</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 */
public class OnlineHelp extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final String HELPCONTEXTROOT = "/heliumhelp/heliumhelp/";
    static final String HELPPORT = "8080";

    static final String helpsetName = "HeliumOnlineHelp.hs";
    static final String helpsetLabel = "Helium Online Hilfe";
    static final String startTarget = "index_htm_0.7192443030612613";

//    private static JFrame frame;

   // The initial width and height of the frame
   public static int WIDTH = 800;
   public static int HEIGHT =600;


    // Main HelpSet & Broker
    HelpSet hs = null;
    HelpBroker hb = null;
    private static JHelp jh = null;
    SwingHelpUtilities swingHelpUtilities;

  public OnlineHelp() {
		try {
			createNetworkedHelpSet();
			if (hs != null) {
				hb = hs.createHelpBroker();
				hb.setDisplayed(true);

				jh = new JHelp(hs);
				jh.setForeground(Color.black);
				jh.setDoubleBuffered(true);
				jh.setOpaque(true);
				jh.setAutoscrolls(true);

				// jh.setBackground(Color.lightGray);
				jh.setSize(WIDTH, HEIGHT);
				jh.setVisible(true);

			} else {
				System.out.println("HelpSet wasn't found.");
			}

		} catch (Exception ee) {
			System.out.println("Help Set " + helpsetName + " not found."
					+ ee.getMessage());
			return;
		} catch (ExceptionInInitializerError ex) {
			System.err.println("initialization error:");
			ex.getException().printStackTrace();
		}
  }

  private void createNetworkedHelpSet() {
          ClassLoader loader = null;
          URL helpsetURL = null;
          URL helpsetURLFull = null;
          try {

            String sHelpsetURL = createOnlineHelpURL();
            helpsetURL = new URL(sHelpsetURL);
            helpsetURLFull = new URL(helpsetURL+"/"+helpsetName);
            System.out.println("HelpSetUrl: " +helpsetURLFull);
            
            ClassLoader myClassLoader = LPMain.class.getClassLoader() ;
//            loader = new URLClassLoader(new URL[]{helpsetURL,helpsetURLFull});
            loader = new URLClassLoader(new URL[]{helpsetURL,helpsetURLFull}, myClassLoader);

              URL url = null;
              url = HelpSet.findHelpSet(loader, helpsetName);
              System.out.println("findHelpSet url=" + url);
              hs = new HelpSet(loader, url);
          } catch (MalformedURLException ex) {
            System.out.println("MalformedURLException:");
            ex.printStackTrace();
          } catch (Exception ee) {
              System.out.println ("Trouble in createHelpSet;");
              System.out.println( "HelpSet " + ee.getMessage());
              ee.printStackTrace();
              return;
          }
      }



  private void createHelpSet() {
          ClassLoader loader = this.getClass().getClassLoader();
          URL url;
          try {
              url = HelpSet.findHelpSet(loader, helpsetName);
              System.out.println("findHelpSet url=" + url);
              if (url == null) {
                  System.out.println("codeBase url=<" + url+">");
              }
              hs = new HelpSet(loader, url);
          } catch (Exception ee) {
              System.out.println ("Trouble in createHelpSet;");
              System.out.println( "HelpSet " + ee.getMessage());
              ee.printStackTrace();
              return;
          }
      }


 /**
   * Erzeuge URL zu der Online Help.
   * Hat die Form http://servername:portname/onlinehelpcontextname/
   *
   * @return String
   */
private String createOnlineHelpURL(){
  StringBuffer sOnlineHelpURL = new StringBuffer();
  sOnlineHelpURL.append("http://");

  String sServerName = getServerName();
  sOnlineHelpURL.append(sServerName);
  sOnlineHelpURL.append(":"+HELPPORT);

  sOnlineHelpURL.append(HELPCONTEXTROOT);

  return sOnlineHelpURL.toString();
}

/**
 * Servername aus Client naming provider herauslesen
 * @return String
 */
private String getServerName(){
  String server = System.getProperty("java.naming.provider.url");

  try {
     int iB = server.indexOf("//") + 2;
     int iM = server.lastIndexOf(":");
     server = server.substring(iB, iM);
  }catch (Exception ex) {
       server = "?";
  }
  return server;
}



}
