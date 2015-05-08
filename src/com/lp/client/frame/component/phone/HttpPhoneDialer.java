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
package com.lp.client.frame.component.phone;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.util.logger.LpLogger;
import com.lp.util.EJBExceptionLP;

public class HttpPhoneDialer extends PhoneDialer {

	private String dialUri ;
	private HttpClient client ;

	protected final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(HttpPhoneDialer.class);
	
	public HttpPhoneDialer() {
		client = new HttpClient() ;
	}
	
	public HttpPhoneDialer(String dialUri) {
		this.dialUri = dialUri ;
		client = new HttpClient() ;
	}
	
/*
 Mit dem Arbeitsplatzparameter TELEFONWAHL_HTTP_REQUEST kein ein http-Aufruf hinterlegt werden, der direkt das SNOM Telefon ansteuert.
Der Aufruf lautet 
http://192.168.8.5/command.htm?number=###NUMMER###&outgoing_uri=1420601e1@sipgate.de

oder
http://192.168.8.5/index.htm?NUMBER=###NUMMER'''&DIAL=Waehlen&active_line=11 

wobei das ###NUMMER### beim Aufruf durch die gleiche Nummer ersetzt wird, die bei der TAPI Wahl \u00FCbergeben werden w\u00FCrde.
Der HTTP Aufruf fast funktioniert gleich, wie der Aufruf der WebSeite aus den Kunden Detaildaten. Es muss jedoch ein WGET sein. D.h. ich will da ja nicht, dass der Browser aufgeht und dann die SNOM Seite angezeigt wird, sonder nur dass das an das Telefon \u00FCbergeben wird.
Wenns nicht geht, kann man dann immer noch den Browser f\u00FCr die Fehlersuche verwenden.
Es muss auf die R\u00FCckantwort des Telefons gewartet werden und wenn es nicht geht, eine entsprechende Fehlermeldung geliefert werden.
Bei unseren eigenen telefonen Testbar

Bei den letzten Versuchen ging gar nichts, jetzt geht der untere Aufruf, aber ohne  &active_line=11, also:
http://192.168.8.5/index.htm?NUMBER=0711217200580&DIAL=Waehlen

Aber nur auf meinem SNOM von meinem Rechner aus.
Eventuell ist ja auch die Idee, dass man irgendwo die eigene IP eintragen muss, von der aus der Aufruf erlaubt ist.
 *  * 
 * 	(non-Javadoc)
 * @see com.lp.client.frame.component.PhoneDialer#dial()
 */
	@Override
	public void dial() throws ExceptionLP {
		dialImpl(dialUri) ;
	}

	@Override
	public void dial(String number) throws ExceptionLP {
		if(null == number || number.trim().length() == 0)
			throw new ExceptionLP(EJBExceptionLP.FEHLER_TELEFON_WAHLVORGANG, 
					new IllegalArgumentException("Nummer null oder leer")) ;
		dialImpl(number) ;
	}
	
	protected HttpClient getHttpClient() {
		return client ;
	}
	
	protected GetMethod getHttpMethod(String numberToDial) {
		return new GetMethod(numberToDial) ;
	}
	
	protected void prepareClient(HttpClient client, GetMethod method) {
		client.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true) ;		
	}
	
	protected void dialImpl(String number) throws ExceptionLP {		
		myLogger.info("Dialing <" + number + "> ..." ) ;
		
		GetMethod method = getHttpMethod(number) ;
		
		try {
			prepareClient(client, method) ;
			int status = client.executeMethod(method) ;

			myLogger.info("Dialing done with HTTP Status: <" + status + ">") ;
			if(status != HttpStatus.SC_OK) {
				byte[] responseBody = method.getResponseBody() ;
				String s = new String(responseBody) ;

				myLogger.debug("Done dialing with response <" + s + ">") ;
			}
		} catch(HttpException ex) {
			myLogger.debug("Got HttpException <" + ex.getMessage() + ">") ;
			throw new ExceptionLP(EJBExceptionLP.FEHLER_TELEFON_WAHLVORGANG, ex.getMessage(), ex) ;
		} catch(IOException ex) {
			myLogger.debug("Got IOException <" + ex.getMessage() + ">") ;
			throw new ExceptionLP(EJBExceptionLP.FEHLER_TELEFON_WAHLVORGANG, ex.getMessage(), ex) ;
		} finally {
			method.releaseConnection() ;
		}
	}
}
