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

import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.Desktop;
import com.lp.server.system.jms.service.LPTopicManageBean;

public class TopicManage extends LPTopic implements ILPTopic {

//	private TopicSubscriber subscrib = null;

	public TopicManage(Desktop desktopI) throws NamingException, ExceptionLP {
		super.desktop = desktopI;
		super.ctx = new InitialContext();
		try {
			super.subscribe(LPTopicManageBean.DESTINATION_TOPIC_NAME_MANAGE, true, this);
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

	@Override
	public void onMessage(Message message) {
		super.onMessage(message);
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
