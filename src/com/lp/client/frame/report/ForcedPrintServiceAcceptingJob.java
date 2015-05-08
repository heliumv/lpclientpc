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
package com.lp.client.frame.report;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.ServiceUIFactory;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.event.PrintServiceAttributeListener;

public class ForcedPrintServiceAcceptingJob implements PrintService {

	private PrintService printServiceDelegate ;
	private boolean overrideAttributes ;
	
	public ForcedPrintServiceAcceptingJob(PrintService delegate) {
		this(delegate, false) ;
	}
	
	public ForcedPrintServiceAcceptingJob(PrintService delegate, boolean override) {
		printServiceDelegate = delegate ;
		this.overrideAttributes = override ;
	}
	
	@Override
	public String getName() {
		return printServiceDelegate.getName();
	}

	@Override
	public DocPrintJob createPrintJob() {
		return printServiceDelegate.createPrintJob() ;
	}

	@Override
	public void addPrintServiceAttributeListener(
			PrintServiceAttributeListener listener) {
		printServiceDelegate.addPrintServiceAttributeListener(listener) ;
	}

	@Override
	public void removePrintServiceAttributeListener(
			PrintServiceAttributeListener listener) {
		printServiceDelegate.removePrintServiceAttributeListener(listener) ;
	}

	@Override
	public PrintServiceAttributeSet getAttributes() {
		return printServiceDelegate.getAttributes() ;
	}

	@Override
	public <T extends PrintServiceAttribute> T getAttribute(Class<T> category) {
		if(PrinterIsAcceptingJobs.class.equals(category)) {
			T attribute =  printServiceDelegate.getAttribute(category) ;
			System.out.println ("actual state for printer " + printServiceDelegate.getName() + " is " + attribute.toString()) ;

			if(overrideAttributes) {
				PrinterIsAcceptingJobs accepting = PrinterIsAcceptingJobs.ACCEPTING_JOBS ;
				attribute = (T) accepting ;
			}
			
			return attribute ;
		}

		return printServiceDelegate.getAttribute(category) ;
	}

	@Override
	public DocFlavor[] getSupportedDocFlavors() {
		return printServiceDelegate.getSupportedDocFlavors() ;
	}

	@Override
	public boolean isDocFlavorSupported(DocFlavor flavor) {
		return printServiceDelegate.isDocFlavorSupported(flavor);
	}

	@Override
	public Class<?>[] getSupportedAttributeCategories() {
		return printServiceDelegate.getSupportedAttributeCategories() ;
	}

	@Override
	public boolean isAttributeCategorySupported(
			Class<? extends Attribute> category) {
		return printServiceDelegate.isAttributeCategorySupported(category) ;
	}

	@Override
	public Object getDefaultAttributeValue(Class<? extends Attribute> category) {
		return printServiceDelegate.getDefaultAttributeValue(category);
	}

	@Override
	public Object getSupportedAttributeValues(
			Class<? extends Attribute> category, DocFlavor flavor,
			AttributeSet attributes) {
		return printServiceDelegate.getSupportedAttributeValues(category, flavor, attributes);
	}

	@Override
	public boolean isAttributeValueSupported(Attribute attrval,
			DocFlavor flavor, AttributeSet attributes) {
		return printServiceDelegate.isAttributeValueSupported(attrval, flavor, attributes);
	}

	@Override
	public AttributeSet getUnsupportedAttributes(DocFlavor flavor,
			AttributeSet attributes) {
		return printServiceDelegate.getUnsupportedAttributes(flavor, attributes);
	}

	@Override
	public ServiceUIFactory getServiceUIFactory() {
		return printServiceDelegate.getServiceUIFactory();
	}
}
