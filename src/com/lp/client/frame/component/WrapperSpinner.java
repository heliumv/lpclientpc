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
package com.lp.client.frame.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;

/**
 * <p>
 * Spinner fuer Integer - Werte<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.6 $
 */
public class WrapperSpinner extends JSpinner implements IControl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isActivatable = true;
	private boolean isMandatoryField = false;
	private Number iStartWert = null;
//	private CornerInfoButton cib = null;

	/**
	 * setDefaults
	 */
	private void setDefaults() {
		HelperClient.setDefaultsToComponent(this);
		// Damit die Zahlen ohne Tausenderpunkt formatiert werden
		NumberEditor editor = new NumberEditor(this, "0");

//		cib = new CornerInfoButton(this);
		this.setEditor(editor);
		editor.addFocusListener(new WrapperSpinner_focusAdapter(this));
		this.addFocusListener(new WrapperSpinner_focusAdapter(this));
		editor.getTextField().addFocusListener(
				new WrapperSpinner_focusAdapter(this));
	}
	

	/*
	 * siehe: http://stackoverflow.com/questions/15328185/make-jspinner-select-text-when-focused
	 */
    public void installFocusListener(FocusListener listener) {

        JComponent spinnerEditor = getEditor();

        if (spinnerEditor != null) {
        	
            // This is me spending a few days trying to make this work and 
            // eventually throwing a hissy fit and just grabbing all the 
            // JTextComponent components contained within the editor....
            List<JTextComponent> lstChildren = findAllChildren(this, JTextComponent.class);
            if (lstChildren != null && lstChildren.size() > 0) {
                JTextComponent editor = lstChildren.get(0);
                editor.addFocusListener(listener);
            }

        }

    }
    public <T extends Component> List<T> findAllChildren(JComponent component, Class<T> clazz) {

        List<T> lstChildren = new ArrayList<T>(5);
        for (Component comp : component.getComponents()) {
            if (clazz.isInstance(comp)) {
                lstChildren.add(clazz.cast(comp));
            } else if (comp instanceof JComponent) {
                lstChildren.addAll(findAllChildren((JComponent) comp, clazz));
            }
        }
        return Collections.unmodifiableList(lstChildren);

    }

	/**
	 * Konstruktor
	 * 
	 * @param spinnerModel
	 *            SpinnerModel
	 */
	public WrapperSpinner(SpinnerModel spinnerModel) {
		super(spinnerModel);
		setDefaults();
	}

	/**
	 * Konstruktor
	 * 
	 * @param value
	 *            Integer
	 * @param minimum
	 *            Integer
	 * @param maximum
	 *            Integer
	 */
	public WrapperSpinner(Integer value, Integer minimum, Integer maximum) {
		this(value, minimum, maximum, new Integer(1));
	}

	/**
	 * Konstruktor
	 * 
	 * @param value
	 *            Integer
	 * @param minimum
	 *            Integer
	 * @param maximum
	 *            Integer
	 * @param stepSize
	 *            Integer
	 */
	public WrapperSpinner(Integer value, Integer minimum, Integer maximum,
			Integer stepSize) {
		this.iStartWert = value;
		setDefaults();
		SpinnerModel model = new SpinnerNumberModel(value, minimum, maximum,
				stepSize);
		this.setModel(model);
	}

	/**
	 * 
	 * @return den aktuellen Wert, null wenn das Model kein SpinnerNumberModel
	 *         ist
	 */
	public Integer getInteger() {
		try {
			this.commitEdit();
		} catch (ParseException ex) {
			// nothing here
		}
		if (getModel() instanceof SpinnerNumberModel) {
			SpinnerNumberModel model = (SpinnerNumberModel) getModel();
			return new Integer(model.getNumber().intValue());
		}
		return null;
	}

	/**
	 * Wenn das Model kein SpinnerNumberModel ist, passiert nichts
	 * 
	 * @param iValue
	 */
	public void setInteger(Integer iValue) {
		if (getModel() instanceof SpinnerNumberModel) {
			SpinnerNumberModel model = (SpinnerNumberModel) getModel();
			model.setValue(iValue == null ? model.getMinimum() : iValue);
		}
	}

	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
	}

	public boolean isActivatable() {
		return isActivatable;
	}

	public void setMandatoryField(boolean isMandatoryField) {
		this.isMandatoryField = isMandatoryField;
	}

	public boolean isMandatoryField() {
		return isMandatoryField;
	}

	public void removeContent() {
		if (iStartWert != null) {
			this.setValue(iStartWert);
		}
	}
	
	@Override
	public boolean hasContent() throws Throwable {
		return true;
	}

	public void setMinimumSize(Dimension d) {
		super.setMinimumSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	public void setMaximumSize(Dimension d) {
		super.setMaximumSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	void focusGained() {
		if (getInteger() != null) {
			JSpinner.NumberEditor editor = (JSpinner.NumberEditor) this
					.getEditor();
			String sText = editor.getTextField().getText();
			editor.getTextField().select(0, sText.length());
			SwingUtilities.invokeLater(new Selectlater(editor.getTextField()));
		}
	}

	/**
	 * @todo das is nur ein workaround (java bug) PJ 5348
	 */

	private static class Selectlater implements Runnable {
		JTextComponent comp;

		Selectlater(JTextComponent src) {
			comp = src;
		}

		public void run() {
			comp.selectAll();
		}
	}
//
//	@Override
//	public void setToken(String token) {
//		cib.setToolTipToken(token);
//	}
//
//	@Override
//	public Point getLocationOffset() {
//		return InfoButtonRelocator.getInstance().getRelocation(this);
//	}
//
//	@Override
//	public void removeCib() {
//		cib = null;
//	}
}

class WrapperSpinner_focusAdapter implements java.awt.event.FocusListener {
	WrapperSpinner adaptee;

	WrapperSpinner_focusAdapter(WrapperSpinner adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {

	}

	public void focusGained(FocusEvent e) {
		adaptee.focusGained();
	}
}
