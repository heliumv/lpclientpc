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
 *******************************************************************************/
package com.lp.client.frame.component;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;

public class HvValueHolder<T> implements FocusListener {
	private T value ;
	private Component reference ;
	private Collection<IHvValueHolderListener> listeners ;
	private FocusListener focusListener ;
	
	public HvValueHolder(Component theReference, T value) {
		this.reference = theReference ;
		this.value = value ; 
	}
	
	public T getValue() {
		return value ;
	}
	
	@SuppressWarnings("unchecked")
	public void updateValueWithoutEvent() {
		value = (T)((IHvValueHolder) reference).getValueHolderValue();
	}
	
	public void setValue(T theValue) {
		if(value == null && theValue == null) return ;
		if(value != null) {
			if(value.equals(theValue)) return ;
		}
		
		T oldValue = value ;
		value = theValue ;
		fireChanged(oldValue) ;
	}
	
	protected Collection<IHvValueHolderListener> getListeners() {
		if(listeners == null) {
			listeners = new ArrayList<IHvValueHolderListener>() ;
		}
		return listeners ;
	}
	
	@SuppressWarnings("unchecked")
	protected synchronized void installFocusListener() {
		if(focusListener == null && getListeners().size() > 0) {
			if(reference instanceof IHvValueHolder) {
				reference.addFocusListener(this);
				focusListener = this ;
				setValue((T) ((IHvValueHolder) reference).getValueHolderValue()) ;
			}
		}
	}
	
	protected synchronized void removeFocusListener() {
		if(focusListener != null) {
			reference.removeFocusListener(focusListener) ;
			focusListener = null ;
		}
	}
	
	protected void fireChanged(T oldValue) {
		for (IHvValueHolderListener listener : getListeners()) {
			listener.valueChanged(reference, oldValue, value) ;
		}
	}
	
	public synchronized void addListener(IHvValueHolderListener l) {
		if(l == null) return ;
		
		getListeners().add(l) ;
		installFocusListener() ;
	}
	
	public synchronized void removeListener(IHvValueHolderListener l) {
		if(l == null) return ;
		
		getListeners().remove(l) ;
		removeFocusListener() ;
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		setValue((T) ((IHvValueHolder) reference).getValueHolderValue()) ;		
	}	
}
