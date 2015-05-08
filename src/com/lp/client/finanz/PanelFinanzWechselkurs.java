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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.fastlanereader.generated.service.FLRWechselkursPK;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.WechselkursDto;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um die Wechselkurse</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 22.06.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2011/11/22 14:51:46 $
 */
public class PanelFinanzWechselkurs extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaWaehrungVon = new WrapperLabel();
	private WrapperLabel wtfWaehrungVon = new WrapperLabel();
	private WrapperLabel wlaKursVon = new WrapperLabel();
	private WrapperNumberField wnfKurs = new WrapperNumberField();
	private TabbedPaneWaehrung tpWaehrung = null;
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();
	private WrapperLabel wlaWaehrungZu = new WrapperLabel();
	private WrapperComboBox wcoWaehrungZu = new WrapperComboBox();
	private WrapperLabel wlaKursZu = new WrapperLabel();
	private WrapperNumberField wnfKursInvertiert = new WrapperNumberField();
	private WrapperLabel wlaKursZu2 = new WrapperLabel();
	private WrapperLabel wlaKursVon2 = new WrapperLabel();

	private WechselkursDto kursDto = null;

	public PanelFinanzWechselkurs(InternalFrame internalFrame,
			String add2TitleI, Object pk, TabbedPaneWaehrung tpWaehrung)
			throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.tpWaehrung = tpWaehrung;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() throws Throwable {
		if (tpWaehrung.getWaehrungDto() != null) {
			wtfWaehrungVon.setText(tpWaehrung.getWaehrungDto().getCNr());
		}
		wlaKursVon.setText("1 " + wtfWaehrungVon.getText() + " =");
		wlaKursZu2.setText(wtfWaehrungVon.getText());
		wcoWaehrungZu.setMap(DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen());
		wdfDatum.setDate(new java.sql.Date(System.currentTimeMillis()));
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		kursDto = null;
		leereAlleFelder(this);
		setDefaults();
		clearStatusbar();
		wtfWaehrungVon.setText(tpWaehrung.getWaehrungDto().getCNr());
	}

	protected void dto2Components() throws Throwable {
		if (kursDto != null) {
			wtfWaehrungVon.setText(kursDto.getWaehrungCNrVon());
			wcoWaehrungZu.setKeyOfSelectedItem(kursDto.getWaehrungCNrZu());
			wnfKurs.setBigDecimal(kursDto.getNKurs());
			wnfKurs_focusLost();
			wdfDatum.setDate(kursDto.getTDatum());
			wlaKursVon.setText("1 " + wtfWaehrungVon.getText() + " =");
			wlaKursZu2.setText(wtfWaehrungVon.getText());
			// StatusBar
			this.setStatusbarPersonalIIdAnlegen(kursDto.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(kursDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(kursDto.getPersonalIIdAendern());
			this.setStatusbarTAendern(kursDto.getTAendern());
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
			wdfDatum.setDate(new java.sql.Date(System.currentTimeMillis()));
		} else {
			FLRWechselkursPK kursPK = (FLRWechselkursPK) key;
			kursDto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.wechselkursFindByPrimaryKey(kursPK.getWaehrung_c_nr_von(),
							kursPK.getWaehrung_c_nr_zu(), kursPK.getT_datum());
			dto2Components();
		}

	}

	protected void components2Dto() throws Throwable {
		if (kursDto == null) {
			kursDto = new WechselkursDto();
		}
		kursDto.setWaehrungCNrVon(wtfWaehrungVon.getText());
		kursDto.setWaehrungCNrZu((String) wcoWaehrungZu.getKeyOfSelectedItem());
		kursDto.setNKurs(wnfKurs.getBigDecimal());
		kursDto.setTDatum(wdfDatum.getDate());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if (kursDto != null && kursDto.getTDatum() != null) {

				if (kursDto.getTDatum().equals(wdfDatum.getDate())) {
					boolean b = DialogFactory.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getInstance().getTextRespectUISPr(
									"system.kurs.zumaktuellendatumaendern"));
					if (b == false) {
						return;
					}
				}
			}

			components2Dto();
			if (kursDto.getWaehrungCNrVon().equals(kursDto.getWaehrungCNrZu())) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
						.getTextRespectUISPr("finanz.anderewaehrungangeben"));
				return;
			}
			// Kurs 0 nicht zulaessig
			if (kursDto.getNKurs().compareTo(new BigDecimal(0)) == 0) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
						.getTextRespectUISPr("finanz.kursnullnichtzulaessig"));
				return;
			}
			DelegateFactory.getInstance().getLocaleDelegate()
					.updateWechselkurs(kursDto);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						tpWaehrung.getWaehrungDto().getCNr());
			}
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
			Object key = new FLRWechselkursPK(kursDto.getWaehrungCNrVon(),
					kursDto.getWaehrungCNrZu(), kursDto.getTDatum());
			this.setKeyWhenDetailPanel(key);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getLocaleDelegate()
				.removeWechselkurs(kursDto);
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaWaehrungVon.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.von"));
		wlaWaehrungZu
				.setText(LPMain.getInstance().getTextRespectUISPr("lp.zu"));
		wcoWaehrungZu.setMandatoryField(true);
		getInternalFrame().addItemChangedListener(this);

		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));
		wnfKurs.setMinimumValue(new BigDecimal(0));
		wnfKurs.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wnfKursInvertiert
				.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);
		wnfKursInvertiert
				.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wnfKurs.addFocusListener(new PanelFinanzWechselkurs_wnfKurs_focusAdapter(
				this));
		wnfKursInvertiert
				.addFocusListener(new PanelFinanzWechselkurs_wnfKursInvertiert_focusAdapter(
						this));
		wdfDatum.setMandatoryFieldDB(true);
		wnfKurs.setMandatoryFieldDB(true);

		wtfWaehrungVon.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wcoWaehrungZu.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wcoWaehrungZu.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaWaehrungVon.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrungVon.setPreferredSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wlaWaehrungZu.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrungZu.setPreferredSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));

		wlaKursVon2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaKursZu2.setHorizontalAlignment(SwingConstants.LEFT);

		wcoWaehrungZu
				.addActionListener(new PanelFinanzWechselkurs_wcoWaehrungZu_actionAdapter(
						this));

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfDatum, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaWaehrungVon, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfWaehrungVon, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrungZu, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcoWaehrungZu, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaKursVon, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfKurs, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaKursVon2, new GridBagConstraints(2, iZeile, 3,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaKursZu, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfKursInvertiert, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaKursZu2, new GridBagConstraints(2, iZeile, 3, 1,
				1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_WAEHRUNG;
	}

	protected void wnfKurs_focusLost() throws Throwable {
		BigDecimal bdKurs = wnfKurs.getBigDecimal();
		if (bdKurs != null) {
			// division durch 0 abfangen
			if (bdKurs.compareTo(new BigDecimal(0)) != 0) {
				wnfKursInvertiert.setBigDecimal(new BigDecimal(1.0).divide(
						bdKurs, LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
						BigDecimal.ROUND_HALF_EVEN).setScale(
						LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
						BigDecimal.ROUND_HALF_EVEN));
			}
		} else {
			wnfKursInvertiert.setBigDecimal(null);
		}
	}

	protected void wnfKursInvertiert_focusLost() throws Throwable {
		BigDecimal bdKurs = wnfKursInvertiert.getBigDecimal();
		if (bdKurs != null) {
			// division durch 0 abfangen
			if (bdKurs.compareTo(new BigDecimal(0)) != 0) {
				wnfKurs.setBigDecimal(new BigDecimal(1.0).divide(bdKurs,
						LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
						BigDecimal.ROUND_HALF_EVEN).setScale(
						LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
						BigDecimal.ROUND_HALF_EVEN));
			}
		} else {
			wnfKursInvertiert.setBigDecimal(null);
		}
	}

	protected void wcoWaehrungZu_actionPerformed(ActionEvent e) {
		wlaKursZu.setText("1 " + wcoWaehrungZu.getKeyOfSelectedItem() + " =");
		wlaKursVon2.setText((String) wcoWaehrungZu.getKeyOfSelectedItem());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfDatum;
	}
}

class PanelFinanzWechselkurs_wnfKurs_focusAdapter implements
		java.awt.event.FocusListener {
	PanelFinanzWechselkurs adaptee;

	PanelFinanzWechselkurs_wnfKurs_focusAdapter(PanelFinanzWechselkurs adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfKurs_focusLost();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}

class PanelFinanzWechselkurs_wnfKursInvertiert_focusAdapter implements
		java.awt.event.FocusListener {
	PanelFinanzWechselkurs adaptee;

	PanelFinanzWechselkurs_wnfKursInvertiert_focusAdapter(
			PanelFinanzWechselkurs adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfKursInvertiert_focusLost();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}

class PanelFinanzWechselkurs_wcoWaehrungZu_actionAdapter implements
		ActionListener {
	private PanelFinanzWechselkurs adaptee;

	PanelFinanzWechselkurs_wcoWaehrungZu_actionAdapter(
			PanelFinanzWechselkurs adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoWaehrungZu_actionPerformed(e);
	}
}
