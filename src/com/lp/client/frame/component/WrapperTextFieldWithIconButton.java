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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;

/**
 * <p>
 * Diese Klasse enth&auml;lt die Basisimplementierung Komponente aus TextField und Button mit Icon.
 * Findet z.B. Anwendung bei WrapperEmailField, WrapperURLField und WrapperTelefonField.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004 - 2012
 * </p>
 * 
 * <p>
 * Erstellung: Robert Kreiseder; 20.09.12
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 * </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:13 $
 */

public abstract class WrapperTextFieldWithIconButton extends JPanel implements
		IControl, ActionListener {	//FocusListener
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -8424742569077186347L;
	protected boolean isMandatoryField = false;
	protected boolean isMandatoryFieldDB = false;
	protected boolean isActivatable = true;
	protected WrapperTextField wtfText = null;
	protected JButton jButton = null;
	
	
	public WrapperTextFieldWithIconButton(ImageIcon imageIcon, String toolTipToken) {
		super(new GridBagLayout());
	    HelperClient.setDefaultsToComponent(this);
	    
		initComponents(imageIcon);
		setProperties(toolTipToken);
		installComponents();
	}
	
	protected void initComponents(ImageIcon imageIcon) {
		wtfText = new WrapperTextField();
		jButton = new JButton();
		jButton.addActionListener(this);
		jButton.setIcon(imageIcon);
	}
	
	protected void setProperties(String toolTipToken) {
		jButton.setToolTipText(LPMain.getTextRespectUISPr(toolTipToken));
		
		Dimension d = new Dimension(Defaults.getInstance().getControlHeight(),
									Defaults.getInstance().getControlHeight());
		jButton.setMinimumSize(d);
		jButton.setPreferredSize(d);
	}
	protected void installComponents() {
		if(installComponent(wtfText)) {
			add(wtfText, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        					GridBagConstraints.CENTER,
                        					GridBagConstraints.BOTH,
                        					new Insets(0, 0, 0, 0),
                        					0, 0));
		}
		if(installComponent(jButton)) {
			add(jButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        					GridBagConstraints.CENTER,
                        					GridBagConstraints.BOTH,
                        					new Insets(0, 0, 0, 0),
                        					0, 0));
		}
		
	}
	/**
	 * Override und return false um TextField nicht darzustellen.
	 */
	protected boolean installComponent(WrapperTextField wTextField) {
		return true;
	}
	/**
	 * Override und return false um Button nicht darzustellen.
	 */
	protected boolean installComponent(JButton jButton) {
		return true;
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jButton))
			actionEventImpl(e);
	}
	/**
	 * Wird beim Buttonklick aufgerufen.
	 * @see {@link #actionPerformed(ActionEvent)}
	 */
	protected abstract void actionEventImpl(ActionEvent e);
	
	public boolean isMandatoryField() {
	    return isMandatoryField || isMandatoryFieldDB;
	}

	public void setMandatoryField(boolean isMandatoryField) {
		if (!isMandatoryFieldDB || isMandatoryField) {
			this.isMandatoryField = isMandatoryField;
			if (this.isMandatoryField) {
				setBorder(Defaults.getInstance().getMandatoryFieldBorder());
			}
			else {
	        	setBorder(new WrapperTextField().getBorder()); //ueberarbeiten
	        }
		}
	}
	
	public boolean isMandatoryFieldDB() {
	    return isMandatoryFieldDB;
	}
	
	public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
	    this.isMandatoryFieldDB = isMandatoryFieldDB;
	    if (isMandatoryFieldDB) {
	      setMandatoryField(true);
	    }
	}
	
	public boolean isActivatable() {
	    return isActivatable;
	}

	public void setActivatable(boolean isActivatable) {
	    this.isActivatable = isActivatable;
	}
	
	public void setEditable(boolean bEditable) {
	    wtfText.setEditable(bEditable);
	    jButton.setEnabled(!bEditable);

	    wtfText.setBackground(
	    		bEditable?HelperClient.getEditableColor():HelperClient.getNotEditableColor());
	}
	
	public void removeContent() throws Throwable {
		wtfText.removeContent();
	}

	public void setText(String text) {
		wtfText.setText(text);
	}
	public String getText() {
		return wtfText.getText();
	}
	
	/**
	 * @deprecated don't use this method
	 * @param bEnabled boolean
	 */
	public void setEnabled(boolean bEnabled) {
		wtfText.setEnabled(bEnabled);
		if (bEnabled == true) {
			jButton.setEnabled(false);
		}
		else {
			jButton.setEnabled(true);
		}
	}
	
	@Override
	public boolean hasContent() throws Throwable {
		return getText() != null && !getText().trim().isEmpty();
	}
}
