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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.lp.client.frame.ExceptionLP;

/**
 * Erm&ouml;glicht einen HttpRequest zu SNOM Telefonen mit Authentifizierung
 * 
 * Die auszuwertende URL ist im format "|user|password|http://snom...."
 * Wird user/password erkannt, wird eine BasicAuth gemacht, ansonsten
 * der Call zum normalen HttpPhoneDialer weitergeleitet.
 * 
 * Ja, das password ist im Klartext in den Arbeitsplatzparameter 
 * ersichtlich. Diese Klasse ist allerdings nur als n&auml;chster Schritt in
 * die Starface API gedacht und auch nur im eigenen Haus anzuwenden.
 * (Ber&uuml;hmt ber&uuml;chtigt letzte Worte des Software-Entwicklers)
 * 
 * @author Gerold
 */
public class HttpPhoneDialerAuth extends HttpPhoneDialer {

	private String authUser ;
	private String authPassword ;
	

	public HttpPhoneDialerAuth() {
		super() ;
	}
	
	public HttpPhoneDialerAuth(String numberToDial) {
		super(numberToDial) ;
	}
	
	@Override
	protected void dialImpl(String number) throws ExceptionLP {
		authUser = null ;
		authPassword = null ;
		
		String newNumber = convertNumber(number) ;
		super.dialImpl(newNumber);
	}
	
	@Override
	protected void prepareClient(HttpClient client, GetMethod method) {
		super.prepareClient(client, method);
		
		if(authUser != null && authPassword != null) {
			client.getState().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
//      			new UsernamePasswordCredentials("admin", "0000")) ;
					new UsernamePasswordCredentials(authUser, authPassword)) ;
			method.setDoAuthentication(true) ;			
		}
	}
	
	/**
	 * Extrahieren der beiden Werte fuer die Credentials
	 * Syntax numberUri: "|user|password|http://....."
	 * @param numberUri
	 * @return
	 */
	private String convertNumber(String numberUri) {
		if(!numberUri.startsWith("|")) return numberUri ; 

		String[] tokens = numberUri.split("\\|") ;
		if(tokens.length != 4) return numberUri ;
		
		authUser = tokens[1] ;
		authPassword = tokens[2] ;
		return tokens[3] ; 
	}	
}
