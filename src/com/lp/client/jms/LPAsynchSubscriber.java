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
package com.lp.client.jms;


import java.util.ArrayList;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JOptionPane;

import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.jms.service.LPMessage;
import com.lp.server.system.jms.service.LPQueueBean;


/**
 *
 * <p> Diese Klasse kuemmert sich um alle asynchronen Jobs, die an den
 * Server gehen.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 28.06.06</p>
 *
 * <p>@author $Author: adi $</p>
 *
 * @version not attributable Date $Date: 2010/06/21 13:31:18 $
 */
public class LPAsynchSubscriber
    implements MessageListener
{
  private ConnectionFactory connectionFactory = null;
  private Connection connection = null;
  private InitialContext initialContext = null;
  private Session session = null;
  private MessageProducer messageProducer = null;
  private javax.jms.Queue queueSender = null;
  private Desktop desktop = null;
  private ArrayList<Object> listOfMessages = null;
  protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());


  private ObjectMessage messageLast = null;

  public LPAsynchSubscriber()
      throws NamingException, JMSException {

    initialContext = new InitialContext();
    connectionFactory = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");

    queueSender = (javax.jms.Queue) initialContext.lookup(LPQueueBean.DESTINATION_LPQUEUE_NAME);

    connection = connectionFactory.createConnection();
    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    messageProducer = session.createProducer(queueSender);

    desktop = LPMain.getInstance().getDesktop();
    desktop.getDesktopStatusBar().setLPQueue("0");
  }


  /**
   * Eine LPQueuemessage wurde am Server abgearbeitet; jetzt wurde der Client
   * verstaendigt.
   *
   * @param messageI Message
   */
  public void onMessage(Message messageI) {

    if (messageI instanceof ObjectMessage) {
      try {
        messageLast = (ObjectMessage) messageI;
        // hole alle Messages die am Server aktuell laufen.
        browse();
      }
      catch (Exception ex) {
        // Wir brechen hier nicht ab!
        myLogger.error("Exception in onMessage(): " + ex.toString());
      }

    }
    else {
      myLogger.info("Falscher Messagetyp: " + messageI.toString());
    }

    getDesktop().getDesktopStatusBar().setLPQueue(listOfMessages.size() + " M");
  }


  /**
   *
   * Hole alle Messages die am Server aktuell laufen.
   * @throws Exception
   * @return ArrayList alle Messages
   */
  public ArrayList<Object> browse()
      throws Exception {
	  
    QueueBrowser browser = session.createBrowser(queueSender);
    listOfMessages = new ArrayList<Object>();
    for (Enumeration<?> e = browser.getEnumeration(); e.hasMoreElements(); ) {
      listOfMessages.add(e.nextElement());
    }

    return listOfMessages;
  }


  /**
   *
   * @param lPMessageI LPMessage
   * @throws JMSException
   * @throws Exception
   */
  public void send(LPMessage lPMessageI)
      throws JMSException, Exception {

    try {
      // Consumer
      javax.jms.Queue temporaryQueue = session.createTemporaryQueue();
      MessageConsumer consumer = session.createConsumer(temporaryQueue);

      consumer.setMessageListener(this);

      ObjectMessage oMessage = session.createObjectMessage();
      oMessage.setObject(lPMessageI);
      oMessage.setJMSReplyTo(temporaryQueue);

      messageProducer.send(oMessage);
      myLogger.info("LPQueue: Asynchronen Job abgeschickt: " + lPMessageI.getIiWhatIWant());

      connection.start();

      browse();

      getDesktop().getDesktopStatusBar().setLPQueue(listOfMessages.size() + "");
    }
    finally {
      if (initialContext != null) {
        try {
          initialContext.close();
        }
        catch (Throwable t) {
          myLogger.error("Could not close connection!", t);
        }
      }
    }
  }


  public Desktop getDesktop() {
    return desktop;
  }


  public LPMessage getLpmLast()
      throws JMSException {
    return (LPMessage)messageLast.getObject();
  }


  public ObjectMessage getMsg() {
    return messageLast;
  }
}
