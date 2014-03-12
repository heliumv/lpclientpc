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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JTextField;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.util.Helper;

/**
 * 
 * <p>
 * <I>Textfeld zur Eingabe von Serien/Chargennummern</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>03.10.2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.16 $
 */
public class WrapperSnrChnrField extends JTextField implements IControl,
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean isMandatoryField = false;
	protected boolean isMandatoryFieldDB = false;
	protected boolean isActivatable = true;
	private boolean dependenceField = false;
	List<SeriennrChargennrMitMengeDto> alSeriennummern = null;
	private Integer artikelIId = null;
	private Integer lagerIId = null;
	private WrapperButton buttonSnrAuswahl = null;
	private static final String ACTION_AUSWAHL = "ACTION_AUSWAHL";
	InternalFrame internalFrame = null;
	

	private WrapperNumberField wnfBelegMenge = null;

	public WrapperNumberField getWnfBelegMenge() {
		return wnfBelegMenge;
	}

	public void setWnfBelegMenge(WrapperNumberField wnfBelegMenge) {
		this.wnfBelegMenge = wnfBelegMenge;
	}

	public WrapperSnrChnrField(InternalFrame internalFrame) {
		HelperClient.setDefaultsToComponent(this);
		this.internalFrame = internalFrame;
		buttonSnrAuswahl = new WrapperButton();
		buttonSnrAuswahl.addActionListener(this);
		buttonSnrAuswahl.setActionCommand(ACTION_AUSWAHL);
		buttonSnrAuswahl.setText(LPMain.getTextRespectUISPr("lp.seriennummer"));
		this.setActivatable(false);

	}

	public WrapperButton getButtonSnrAuswahl() {
		return buttonSnrAuswahl;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_AUSWAHL)) {
			try {
				DialogSerienChargenauswahl d = new DialogSerienChargenauswahl(
						artikelIId, lagerIId, alSeriennummern, true,false,
						internalFrame,wnfBelegMenge);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);
				alSeriennummern = d.alSeriennummern;

				setSeriennummern(alSeriennummern, artikelIId, lagerIId);

				if (wnfBelegMenge != null) {
					wnfBelegMenge.requestFocus();
					if(d.wcbRueckgabe.isSelected()){
						wnfBelegMenge.setBigDecimal(getMenge().negate());
					} else {
						wnfBelegMenge.setBigDecimal(getMenge());
					}
					
				}

			} catch (Throwable e1) {
				internalFrame.handleException(e1, false);
			}
		}
	}

	/**
	 * @param text
	 *            String
	 */
	public void setText(String text) {
		// Ignorieren des vom Designer generierten Codes
		if (text != null) {
			if (!text.startsWith("wrapperSNRField")) {
				super.setText(text);
			}
		} else {
			super.setText("");
		}
	}

	public String getText() {
		// Originalmethode aufrufen und String gleich trimmen
		String s = super.getText();
		if (s != null) {
			// s = s.trim();
			if (s.length() > 0) {
				// gib String zurueck und verlasse Methode
				return s;
			}
			if (s.equals("")) {
				s = null;
			}
		}
		return s;
	}

	/**
	 * Leeren des Feldes.
	 */
	public void removeContent() {
		super.setText("");
		alSeriennummern = null;
	}

	/**
	 * isActivateable
	 * 
	 * @return boolean
	 */
	public boolean isActivatable() {
		return isActivatable;
	}

	/**
	 * isMandatoryField
	 * 
	 * @return boolean
	 */
	public boolean isMandatoryField() {
		return isMandatoryField || isMandatoryFieldDB;
	}

	/**
	 * isMandatoryField
	 * 
	 * @return boolean
	 */
	public boolean isMandatoryFieldDB() {
		return isMandatoryFieldDB;
	}

	/**
	 * setMandatoryField
	 * 
	 * @param isMandatoryField
	 *            boolean
	 */
	public void setMandatoryField(boolean isMandatoryField) {
		if (isMandatoryFieldDB == false || isMandatoryField == true) {
			this.isMandatoryField = isMandatoryField;
			if (this.isMandatoryField == true) {
				setBorder(Defaults.getInstance().getMandatoryFieldBorder());
			} else {
				setBorder(new WrapperTextField().getBorder());
			}
		}
	}

	public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
		this.isMandatoryFieldDB = isMandatoryFieldDB;
		if (isMandatoryFieldDB == true) {
			setMandatoryField(true);
		}
	}

	/**
	 * setActivatable
	 * 
	 * @param isActivatable
	 *            boolean
	 */
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		if (!isActivatable) {
			setEditable(false);
		}
	}

	public boolean isDependenceField() {
		return dependenceField;
	}

	public void setDependenceField(boolean dependenceField) {
		this.dependenceField = dependenceField;
		if (dependenceField) {
			this.setBackground(HelperClient.getDependenceFieldBackgroundColor());
		} else {
			this.setBackground(new WrapperSnrChnrField(internalFrame)
					.getBackground());
		}
	}

	public void setEditable(boolean bEditable) {
		super.setEditable(bEditable);
		this.setBackground(HelperClient.getNotEditableColor());
		if (isActivatable() && getButtonSnrAuswahl() != null) {
			getButtonSnrAuswahl().setEnabled(bEditable);
		}

	}

	public void setInteger(Integer iWert) {
		if (iWert != null) {
			this.setText(iWert.toString());
		}
	}

	public Integer getInteger() {
		String sText = this.getText();
		Integer iWert = null;
		if (sText != null) {
			iWert = new Integer(Integer.parseInt(sText));
		}
		return iWert;
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

	/**
	 * @deprecated use setActivatable
	 * @param bEnabled
	 *            boolean
	 */
	public void setEnabled(boolean bEnabled) {
		super.setEnabled(bEnabled);
	}

	public boolean pruefeLaengeStringArray(int iAnzahlI) {
		return Helper.pruefeLaengeStringArray(this.getText(), iAnzahlI);
	}

	public String[] erzeugeSeriennummernArray(java.math.BigDecimal menge,
			boolean bPruefeMenge) {
		return Helper.erzeugeSeriennummernArray(this.getText(), menge,
				bPruefeMenge);
	}

	public void setArtikelIdLagerId(ArtikelDto artikelDto, Integer lagerIId) {
		this.artikelIId = artikelDto.getIId();
		this.lagerIId = lagerIId;
		if (Helper.short2Boolean(artikelDto.getBChargennrtragend())) {
			buttonSnrAuswahl.setText(LPMain
					.getTextRespectUISPr("lp.chargennummer_lang"));
			buttonSnrAuswahl.setMnemonic('C');
		} else {
			buttonSnrAuswahl.setText(LPMain
					.getTextRespectUISPr("lp.seriennummer"));
			buttonSnrAuswahl.setMnemonic('S');
		}
	}

	public void setSeriennummern(List<SeriennrChargennrMitMengeDto> snrs,
			ArtikelDto artikelDto, Integer lagerIId) throws Throwable {
		alSeriennummern = snrs;
		this.artikelIId = artikelDto.getIId();
		this.lagerIId = lagerIId;
		String s = "";

		if (Helper.short2Boolean(artikelDto.getBChargennrtragend())) {
			buttonSnrAuswahl.setText(LPMain
					.getTextRespectUISPr("lp.chargennummer_lang"));
			buttonSnrAuswahl.setMnemonic('C');
		} else {
			buttonSnrAuswahl.setText(LPMain
					.getTextRespectUISPr("lp.seriennummer"));
			buttonSnrAuswahl.setMnemonic('S');
		}
		if (snrs != null && snrs.size() > 0) {

			if (snrs.size() == 1 && snrs.get(0).getCSeriennrChargennr() == null) {
				alSeriennummern = null;
				setText(null);
				return;
			}

			for (int i = 0; i < snrs.size(); i++) {
				s += snrs.get(i).getCSeriennrChargennr();

				if (Helper.short2Boolean(artikelDto.getBChargennrtragend())) {
					s += " "
							+ Helper.formatZahl(snrs.get(i).getNMenge(), 4,
									LPMain.getTheClient().getLocUi());
				}

				if (!(i == snrs.size() - 1)) {
					s += ", ";

				}
			}
		}

		super.setText(s);

	}

	public void setSeriennummern(List<SeriennrChargennrMitMengeDto> snrs,
			Integer artikelIId, Integer lagerIId) throws Throwable {
		alSeriennummern = snrs;
		this.artikelIId = artikelIId;
		this.lagerIId = lagerIId;

		if (artikelIId != null) {

			ArtikelDto aDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);
			setSeriennummern(snrs, aDto, lagerIId);
		} else {
			setText(null);
		}

	}

	public BigDecimal getMenge() {
		BigDecimal menge = new BigDecimal(0);

		if (alSeriennummern != null) {
			for (int i = 0; i < alSeriennummern.size(); i++) {
				menge = menge.add(alSeriennummern.get(i).getNMenge());
			}
		}
		return menge;

	}

	public List<SeriennrChargennrMitMengeDto> getSeriennummern() {
		return alSeriennummern;

	}

	@Override
	public boolean hasContent() throws Throwable {
		return getText() != null && !getText().trim().isEmpty();
	}
}
