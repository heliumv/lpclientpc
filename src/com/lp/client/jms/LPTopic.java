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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.NachrichtarchivDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.TheClientDto;

public class LPTopic {
	protected Desktop desktop = null;
	protected Context ctx = null;
	protected TopicSession session = null;
	protected TopicConnection connect = null;
	private String topicName = null;
	private java.sql.Timestamp tsAlleIgnorieren = null;

	public LPTopic() {
		super();
	}

	protected TopicSubscriber subscribe(String topicName, boolean durable,
			MessageListener listener) throws NamingException, JMSException,
			InterruptedException {
		this.topicName = topicName;
		TopicSubscriber subscrib = null;
		Topic topic = null;
		TheClientDto theClient = null;
		try {
			theClient = LPMain.getTheClient();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (theClient != null) {
			TopicConnectionFactory fact = (TopicConnectionFactory) ctx
					.lookup("ConnectionFactory");
			connect = fact.createTopicConnection();
			String client = theClient.getBenutzername().trim();
			connect.setClientID(client + topicName);
			session = connect.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);
			topic = (Topic) ctx.lookup(topicName);
			if (durable)
				subscrib = session.createDurableSubscriber(topic, "lpclientpc");
			else
				subscrib = session.createSubscriber(topic);
			subscrib.setMessageListener(listener);
			connect.start();
		}
		return subscrib;
	}

	protected void send(String sMessageI) throws NamingException, JMSException {
		TopicConnectionFactory fact = (TopicConnectionFactory) ctx
				.lookup("ConnectionFactory");
		Topic topic = (Topic) ctx.lookup(this.topicName);
		TopicConnection connect = fact.createTopicConnection();
		TopicSession session = connect.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		TopicPublisher sender = session.createPublisher(topic);
		connect.start();
		TextMessage tm = session.createTextMessage(sMessageI);
		tm.setJMSPriority(ILPTopic.LP_JMS_PRIORITY_LOW);
		sender.publish(tm);
		connect.close();
		session.close();
	}

	protected void onMessage(Message message) {
		try {
			TextMessage msg = (TextMessage) message;
			String msgText = msg.getText();
			String msgtoshow;
			if (LPMain.getInstance().getBenutzernameRaw().startsWith("LPAdmin")) {
				msgtoshow = message.toString();
			} else {
				msgtoshow = msgText;
			}
			Integer nachrichtarchivIId = null;
			try {
				nachrichtarchivIId = message
						.getIntProperty(BenutzerFac.NPROP_NACHRICHTARCHIV_I_ID);
			} catch (NumberFormatException e) {
				// Property nicht vorhanden -> nur Info
			}
			if (nachrichtarchivIId == null) {
				// ist nur eine Info Message
				JOptionPane.showMessageDialog(getDesktop(), msgtoshow,
						LPMain.getTextRespectUISPr("jms.frage.titel"),
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				if (tsAlleIgnorieren != null) {
					try {
						NachrichtarchivDto nDto = DelegateFactory
								.getInstance()
								.getBenutzerDelegate()
								.nachrichtarchivFindByPrimaryKey(
										nachrichtarchivIId);
						if (nDto.getTZeit().getTime() < tsAlleIgnorieren
								.getTime()) {
							return;
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				// Nachricht muss uebernommen / erledigt werden
				Object[] aOptionen = new Object[4];
				final int indexUebernehmen = 0;
				final int indexErledigen = 1;
				final int indexIgnorieren = 2;
				final int indexAlleIgnorieren = 3;
				aOptionen[indexUebernehmen] = LPMain
						.getTextRespectUISPr("jms.frage.uebernehmen");
				aOptionen[indexErledigen] = LPMain
						.getTextRespectUISPr("jms.frage.erledigen");
				aOptionen[indexIgnorieren] = LPMain
						.getTextRespectUISPr("jms.frage.ignorieren");
				aOptionen[indexAlleIgnorieren] = LPMain
						.getTextRespectUISPr("jms.frage.alle.ignorieren");

				int iAuswahl = JOptionPane.showOptionDialog(getDesktop(),
						msgtoshow,
						LPMain.getTextRespectUISPr("jms.frage.titel"),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, // Icon
						aOptionen, aOptionen[0]);
				switch (iAuswahl) {
				case indexAlleIgnorieren:
					tsAlleIgnorieren = new java.sql.Timestamp(
							System.currentTimeMillis());

					break;
				case indexUebernehmen:
					if (nachrichtarchivIId != null) {
						Integer personalIId = null;
						try {
							personalIId = DelegateFactory.getInstance()
									.getBenutzerDelegate()
									.weiseNachrichtPersonZu(nachrichtarchivIId);
						} catch (ExceptionLP e) {
							e.printStackTrace();
						} catch (Throwable e) {
							e.printStackTrace();
						}
						if (personalIId != null) {
							PersonalDto personalDto = null;
							try {
								personalDto = DelegateFactory.getInstance()
										.getPersonalDelegate()
										.personalFindByPrimaryKey(personalIId);
								JOptionPane
										.showMessageDialog(
												getDesktop(),
												"Die Aufgabe wurde bereits von "
														+ personalDto
																.formatFixUFTitelName2Name1()
														+ " \u00FCbernommen!",
												"Hinweis",
												JOptionPane.INFORMATION_MESSAGE);
							} catch (ExceptionLP e) {
								e.printStackTrace();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
					break;
				case indexErledigen:
					try {

						String response = JOptionPane
								.showInputDialog(
										null,
										LPMain.getTextRespectUISPr("system.nachrichtarchiv.erledigungsgrund"),
										LPMain.getTextRespectUISPr("system.nachrichtarchiv.erledigungsgrund"),
										JOptionPane.QUESTION_MESSAGE);

						if (response != null && response.length() > 0) {

							if (response.length() > 80) {
								response = response.substring(0, 79);
							}

							DelegateFactory
									.getInstance()
									.getBenutzerDelegate()
									.erledigeNachricht(nachrichtarchivIId,
											response);
						} else {

							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("system.nachrichtarchiv.erledigungsgrund.pflichtfeld"));

							return;
						}

					} catch (ExceptionLP e) {
						e.printStackTrace();
					} catch (Throwable e) {
						e.printStackTrace();
					}
					break;
				case indexIgnorieren:
					break;
				}
			}
		} catch (JMSException ex) {
			System.out.println(ex.getMessage());
		}
		getDesktop().aktualisiereAnzahlJMSMessages();
	}

	/**
	 * Called by the garbage collector on an object when garbage collection
	 * determines that there are no more references to the object.
	 * 
	 * @throws Throwable
	 *             the <code>Exception</code> raised by this method
	 */
	protected void finalize() throws Throwable {
		try {
			if (null != ctx) {
				ctx.close();
			}
		} catch (Exception ex) {
		}
		try {
			if (null != session) {
				session.close();
			}
		} catch (Exception ex) {
		}
		try {
			if (null != connect) {
				connect.close();
			}
		} catch (Exception ex) {
		}
		
		super.finalize() ;
	}

	public Desktop getDesktop() {
		return desktop;
	}

}
