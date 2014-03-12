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
package com.lp.client.jms;


import java.io.IOException;

import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.system.jms.service.LPInfoTopicBean;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um InfoTopics.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 18.10.06</p>
 *
 * <p>@author $Author: Gerold $</p>
 *
 * @version not attributable Date $Date: 2011/12/14 16:42:17 $
 */
public class InfoTopic extends LPTopic
implements ILPTopic
{
	
	private final String TOPIC_FILTER_RECEIVER = "topic_receiver";
	private final String TOPIC_FILTER_ALL_USER = "topic_filter_all_user";

//	private TopicSubscriber subscrib = null;
	
	public InfoTopic(Desktop desktopI) throws NamingException, ExceptionLP {
		super.desktop = desktopI;
		super.ctx = new InitialContext();
		try {
			super.subscribe(LPInfoTopicBean.DESTINATION_INFOTOPIC_NAME, false, this);
		} catch (JMSException e) {
			if (e instanceof InvalidClientIDException) {
				// erster Client bereits registriert
			} else {
				throw new ExceptionLP(0,e.getMessage(),e);
			}
		} catch (InterruptedException e) {
			throw new ExceptionLP(0,e.getMessage(),e);
		}
	}

	public void send2AllUser(String sMessageI)
	throws NamingException, JMSException {
		super.send(sMessageI);
/*		Topic topic = null;
		TopicConnectionFactory fact = (TopicConnectionFactory) ctx.lookup(
				"ConnectionFactory");
		topic = (Topic) ctx.lookup(LPInfoTopicBean.DESTINATION_INFOTOPIC_NAME);

		connect = fact.createTopicConnection();
		session = connect.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicPublisher sender = session.createPublisher(topic);

		connect.start();

		TextMessage tm = session.createTextMessage(sMessageI);
		tm.setJMSPriority(ILPTopic.LP_JMS_PRIORITY_LOW);
		tm.setStringProperty(TOPIC_FILTER_RECEIVER, TOPIC_FILTER_ALL_USER);
		sender.publish(tm); */
	}

	public void onMessage(Message message) {
		//super.onMessage(message);
		try {
			TextMessage msg = (TextMessage) message;
			LPMain.getInstance().getInfoTopicBuffer().add2InfoTopicBuffer(msg.getText());
			
			String msgtoshow;
			if (LPMain.getInstance().getBenutzernameRaw().startsWith("LPAdmin")) {
				msgtoshow = message.toString();
			} else {
				msgtoshow = msg.getText();
			}
			JOptionPane.showMessageDialog(getDesktop(), msgtoshow, 
					LPMain.getTextRespectUISPr("jms.info.titel"),
					JOptionPane.INFORMATION_MESSAGE);
		}
		catch (JMSException ex) {
			System.out.println(ex.getMessage());
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void finalize() throws Throwable {
//		try {
//			if (null != subscrib) {
//				subscrib.close();
//			}
//		}
//		catch (Exception ex) {}
		super.finalize();
	}
	
}
