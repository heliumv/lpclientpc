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
package com.lp.client.personal;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperFixableNumberField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.PersonalzeitmodellDto;
import com.lp.server.personal.service.UrlaubsanspruchDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelPersonalgehalt extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFramePersonal internalFramePersonal = null;
	private PersonalgehaltDto personalgehaltDto = null;

	private WrapperLabel wla = new WrapperLabel();

	private ButtonGroup bg = new ButtonGroup();

	private WrapperLabel wlaJahr = new WrapperLabel();
	private WrapperSpinner wspJahr = new WrapperSpinner(new Integer(0),
			new Integer(0), new Integer(9999), new Integer(1));
	private WrapperLabel wlaGehaltBrutto = new WrapperLabel();
	private WrapperLabel wlaUestpauschale = new WrapperLabel();
	private WrapperLabel wlaStundensatz = new WrapperLabel();
	private WrapperLabel wlaVerfuegbarkeit = new WrapperLabel();
	private WrapperLabel wlaKmgeld1 = new WrapperLabel();
	private WrapperLabel wlaBisKilometer = new WrapperLabel();
	private WrapperLabel wlaKmgeld2 = new WrapperLabel();
	private WrapperNumberField wnfGehaltBrutto = new WrapperNumberField();
	private WrapperNumberField wnfUestpauschale = new WrapperNumberField();
	private WrapperFixableNumberField wnfStundensatz = new WrapperFixableNumberField();
	private WrapperNumberField wnfVerfuegbarkeit = new WrapperNumberField();
	private WrapperNumberField wnfKmgeld1 = new WrapperNumberField();
	private WrapperNumberField wnfBisKilometer = new WrapperNumberField();
	private WrapperNumberField wnfKmgeld2 = new WrapperNumberField();
	private WrapperCheckBox wcbAlleinverdiener = new WrapperCheckBox();
	private WrapperCheckBox wcbAlleinerzieher = new WrapperCheckBox();
	private WrapperCheckBox wcbKKSGebBefreit = new WrapperCheckBox();
	private WrapperLabel wlaWaehrungGehaltBrutto = new WrapperLabel();
	private WrapperLabel wlaEinheitUeberstundenpauschale = new WrapperLabel();
	private WrapperLabel wlaEinheitStundensatz = new WrapperLabel();
	private WrapperLabel wlaEinheitVerfuegbarkeit = new WrapperLabel();
	private WrapperLabel wlaWaehrungKMGeld1 = new WrapperLabel();
	private WrapperLabel wlaWaehrungKMGeld2 = new WrapperLabel();
	private WrapperTextField wtfGrundkksgebbefreit = new WrapperTextField();
	private WrapperLabel wlaMonat = new WrapperLabel();
	private WrapperComboBox wcoMonat = new WrapperComboBox();

	double dLohnstundensatzKalenderwochen = 0;
	double dLohnstundensatzKrankwochen = 0;
	double dLohnstundensatzFeiertagswochen = 0;
	double dLohnstundensatzSonderzahlungen = 0;

	private WrapperLabel wlaLohnmittelstundensatz = new WrapperLabel();
	private WrapperNumberField wnfLohnmittelstundensatz = new WrapperNumberField();

	private WrapperLabel wlaKalkIstStunden = new WrapperLabel();
	private WrapperNumberField wnfKalkIstStunden = new WrapperNumberField();

	private JLabel wlaFaktor = new JLabel();
	private WrapperNumberField wnfFaktor = new WrapperNumberField();

	private WrapperFixableNumberField wnfAufschlagLohnmittelstundensatz = new WrapperFixableNumberField();

	private WrapperLabel wlaGehaltBruttoBrutto = new WrapperLabel();
	private WrapperNumberField wnfGehaltBruttoBrutto = new WrapperNumberField();
	private WrapperLabel wlaGehaltNetto = new WrapperLabel();
	private WrapperNumberField wnfGehaltNetto = new WrapperNumberField();
	private WrapperLabel wlaPraemieBruttoBrutto = new WrapperLabel();
	private WrapperNumberField wnfPraemieBruttoBrutto = new WrapperNumberField();

	private WrapperLabel wlaLeistungswert = new WrapperLabel();
	private WrapperNumberField wnfLeistungswert = null;

	private WrapperCheckBox wcbUestdauszahlen = new WrapperCheckBox();
	private WrapperLabel wlaUestdPuffer = new WrapperLabel();
	private WrapperNumberField wnfUestdPuffer = new WrapperNumberField();

	public PanelPersonalgehalt(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		wlaWaehrungGehaltBrutto.setText(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr());
		wlaWaehrungKMGeld1.setText(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr());
		wlaWaehrungKMGeld2.setText(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr());
		wlaEinheitStundensatz.setText(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr());

		DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getInstance()
				.getUISprLocale());
		String[] defaultMonths = symbols.getMonths();
		Map<Integer, String> m = new TreeMap<Integer, String>();
		for (int i = 0; i < defaultMonths.length - 1; i++) {
			m.put(new Integer(i), defaultMonths[i]);

		}
		wcoMonat.setMap(m);

	}

	public void focusGained(FocusEvent e) {

	}

	public void focusLost(FocusEvent e) {
		try {
			wnfFaktor.setForeground(Color.BLACK);
			wnfAufschlagLohnmittelstundensatz.setForeground(Color.BLACK);
			if (e.getSource().equals(wnfPraemieBruttoBrutto)
					|| e.getSource().equals(wnfGehaltBruttoBrutto)) {
				berechneKalkulatorischenStundensatz(false);
			}

			if (e.getSource().equals(wnfLohnmittelstundensatz)) {
				berechneKalkulatorischenStundensatz(true);
			}

			if (e.getSource().equals(wnfFaktor)) {
				if (wnfFaktor.getDouble() == null) {
					wnfFaktor.setDouble(0D);
					wnfAufschlagLohnmittelstundensatz
							.setBigDecimal(BigDecimal.ZERO);
				} else {
					if (wnfFaktor.getDouble().doubleValue() != 0) {

						wnfAufschlagLohnmittelstundensatz.setBigDecimal(Helper
								.getProzentWert(wnfLohnmittelstundensatz
										.getBigDecimal(), wnfFaktor
										.getBigDecimal(), 2));

					} else {
						wnfAufschlagLohnmittelstundensatz
								.setBigDecimal(BigDecimal.ZERO);
					}
				}
			}

			if (e.getSource().equals(
					wnfAufschlagLohnmittelstundensatz
							.getWrapperWrapperNumberField())) {
				if (wnfAufschlagLohnmittelstundensatz.getBigDecimal() == null) {
					wnfFaktor.setDouble(0D);
					wnfAufschlagLohnmittelstundensatz
							.setBigDecimal(BigDecimal.ZERO);
				} else {
					if (wnfAufschlagLohnmittelstundensatz.getBigDecimal()
							.doubleValue() != 0) {

						wnfFaktor.setBigDecimal(Helper.getProzentsatzBD(
								wnfLohnmittelstundensatz.getBigDecimal(),
								wnfAufschlagLohnmittelstundensatz
										.getBigDecimal(), 2));
					} else {
						wnfAufschlagLohnmittelstundensatz
								.setBigDecimal(BigDecimal.ZERO);
					}
				}
			}

			if (wnfAufschlagLohnmittelstundensatz.getWrbFixNumber()
					.isSelected()) {

				wnfStundensatz.setBigDecimal(wnfLohnmittelstundensatz
						.getBigDecimal().add(
								wnfAufschlagLohnmittelstundensatz
										.getBigDecimal()));
			} else {
				// Rabatt berechnen
				// Aufschlag neu berechnnen

				if (wnfStundensatz.getBigDecimal() == null) {
					wnfStundensatz.setBigDecimal(BigDecimal.ZERO);
				}

				wnfAufschlagLohnmittelstundensatz.setBigDecimal(wnfStundensatz
						.getBigDecimal().subtract(
								wnfLohnmittelstundensatz.getBigDecimal()));

				// Prozent berechnen
				wnfFaktor.setBigDecimal(Helper.getProzentsatzBD(
						wnfLohnmittelstundensatz.getBigDecimal(),
						wnfAufschlagLohnmittelstundensatz.getBigDecimal(), 2));

			}

			if (wnfFaktor.getDouble() != null
					&& wnfFaktor.getDouble().doubleValue() < 0) {
				wnfFaktor.setForeground(Color.RED);
			}
			if (wnfAufschlagLohnmittelstundensatz.getBigDecimal() != null
					&& wnfAufschlagLohnmittelstundensatz.getBigDecimal()
							.doubleValue() < 0) {
				wnfAufschlagLohnmittelstundensatz.getWrapperWrapperNumberField().setForeground(Color.RED);
			}

			return;

		} catch (Throwable e1) {
			handleException(e1, false);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfGehaltBrutto;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		personalgehaltDto = new PersonalgehaltDto();

		leereAlleFelder(this);

		GregorianCalendar cal = new GregorianCalendar();
		wcoMonat.setSelectedIndex(cal.get(Calendar.MONTH));

		wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));
		wtfGrundkksgebbefreit.setMandatoryField(false);
		wnfAufschlagLohnmittelstundensatz.getWrbFixNumber().setSelected(true);
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		wnfKalkIstStunden.setDouble(holeJahresIstStundenausZeitmodell());
		berechneKalkulatorischenStundensatz(false);
		focusLost(new FocusEvent(wnfPraemieBruttoBrutto, -1));
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removePersonalgehalt(personalgehaltDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		personalgehaltDto.setPersonalIId(internalFramePersonal.getPersonalDto()
				.getIId());

		personalgehaltDto.setIJahr((Integer) wspJahr.getValue());
		personalgehaltDto.setIMonat((Integer) wcoMonat.getKeyOfSelectedItem());
		personalgehaltDto.setBAlleinerzieher(wcbAlleinerzieher.getShort());
		personalgehaltDto.setBAlleinverdiener(wcbAlleinverdiener.getShort());
		personalgehaltDto.setBKksgebbefreit(wcbKKSGebBefreit.getShort());
		personalgehaltDto.setBUestdauszahlen(wcbUestdauszahlen.getShort());
		personalgehaltDto.setNGehalt(wnfGehaltBrutto.getBigDecimal());
		personalgehaltDto.setNKmgeld1(wnfKmgeld1.getBigDecimal());
		personalgehaltDto.setNKmgeld2(wnfKmgeld2.getBigDecimal());
		personalgehaltDto.setFBiskilometer(wnfBisKilometer.getDouble());
		personalgehaltDto.setFUestpauschale(wnfUestpauschale.getDouble());
		personalgehaltDto.setFVerfuegbarkeit(wnfVerfuegbarkeit.getDouble());
		personalgehaltDto.setNStundensatz(wnfStundensatz.getBigDecimal());
		personalgehaltDto.setNUestdpuffer(wnfUestdPuffer.getBigDecimal());
		personalgehaltDto.setCGrundkksgebbefreit(wtfGrundkksgebbefreit
				.getText());
		personalgehaltDto.setFLeistungswert(wnfLeistungswert.getDouble());

		personalgehaltDto.setNGehaltNetto(wnfGehaltNetto.getBigDecimal());
		personalgehaltDto.setNGehaltBruttobrutto(wnfGehaltBruttoBrutto
				.getBigDecimal());
		personalgehaltDto.setNPraemieBruttobrutto(wnfPraemieBruttoBrutto
				.getBigDecimal());
		personalgehaltDto.setFKalkIstJahresstunden(wnfKalkIstStunden
				.getDouble());

		personalgehaltDto
				.setNAufschlagLohnmittelstundensatz(wnfAufschlagLohnmittelstundensatz
						.getBigDecimal());
		personalgehaltDto
				.setFFaktorLohnmittelstundensatz(wnfFaktor.getDouble());
		personalgehaltDto.setNLohnmittelstundensatz(wnfLohnmittelstundensatz
				.getBigDecimal());

		personalgehaltDto.setBStundensatzFixiert(Helper
				.boolean2Short(wnfStundensatz.getWrbFixNumber().isSelected()));

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wspJahr.setValue(personalgehaltDto.getIJahr());
		wcoMonat.setKeyOfSelectedItem(personalgehaltDto.getIMonat());
		wnfGehaltBrutto.setBigDecimal(personalgehaltDto.getNGehalt());
		wnfVerfuegbarkeit.setDouble(personalgehaltDto.getFVerfuegbarkeit());
		wnfUestpauschale.setDouble(personalgehaltDto.getFUestpauschale());
		wnfBisKilometer.setDouble(personalgehaltDto.getFBiskilometer());
		wnfKmgeld1.setBigDecimal(personalgehaltDto.getNKmgeld1());
		wnfStundensatz.setBigDecimal(personalgehaltDto.getNStundensatz());
		wnfKmgeld2.setBigDecimal(personalgehaltDto.getNKmgeld2());
		wnfUestdPuffer.setBigDecimal(personalgehaltDto.getNUestdpuffer());
		wnfLeistungswert.setDouble(personalgehaltDto.getFLeistungswert());
		wtfGrundkksgebbefreit.setText(personalgehaltDto
				.getCGrundkksgebbefreit());
		wnfLohnmittelstundensatz.setBigDecimal(personalgehaltDto
				.getNLohnmittelstundensatz());

		wcbAlleinerzieher.setShort(personalgehaltDto.getBAlleinerzieher());
		wcbUestdauszahlen.setShort(personalgehaltDto.getBUestdauszahlen());
		wcbAlleinverdiener.setShort(personalgehaltDto.getBAlleinverdiener());
		wcbKKSGebBefreit.setShort(personalgehaltDto.getBKksgebbefreit());

		if (wcbKKSGebBefreit.isSelected()) {
			wtfGrundkksgebbefreit.setMandatoryField(true);
		} else {
			wtfGrundkksgebbefreit.setMandatoryField(false);
		}

		wnfGehaltNetto.setBigDecimal(personalgehaltDto.getNGehaltNetto());
		wnfGehaltBruttoBrutto.setBigDecimal(personalgehaltDto
				.getNGehaltBruttobrutto());
		wnfPraemieBruttoBrutto.setBigDecimal(personalgehaltDto
				.getNPraemieBruttobrutto());
		wnfKalkIstStunden.setDouble(personalgehaltDto
				.getFKalkIstJahresstunden());

		wnfFaktor
				.setDouble(personalgehaltDto.getFFaktorLohnmittelstundensatz());
		wnfAufschlagLohnmittelstundensatz.setBigDecimal(personalgehaltDto
				.getNAufschlagLohnmittelstundensatz());

		if (Helper.short2boolean(personalgehaltDto.getBStundensatzFixiert()) == true) {
			wnfStundensatz.getWrbFixNumber().setSelected(true);
		} else {
			wnfAufschlagLohnmittelstundensatz.getWrbFixNumber().setSelected(
					true);
		}

		this.setStatusbarPersonalIIdAendern(personalgehaltDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(personalgehaltDto.getTAendern());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (personalgehaltDto.getIId() == null) {
				personalgehaltDto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate()
						.createPersonalgehalt(personalgehaltDto));
				setKeyWhenDetailPanel(personalgehaltDto.getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updatePersonalgehalt(personalgehaltDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFramePersonal.getPersonalDto().getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);
		wnfUestdPuffer.setMandatoryField(true);
		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaLeistungswert.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.leistungswert"));
		wnfLeistungswert = new WrapperNumberField();
		wnfLeistungswert.setColumns(14);
		wnfLeistungswert.setFractionDigits(2);

		wla.setHorizontalAlignment(SwingConstants.LEFT);
		wla.setText("%");

		wlaJahr.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.gueltigab")
				+ ":       "
				+ LPMain.getInstance().getTextRespectUISPr("lp.jahr"));
		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		wlaGehaltBrutto.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.gehalt.brutto"));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_KALENDERWOCHEN,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		dLohnstundensatzKalenderwochen = (Double) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_KRANKWOCHEN,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		dLohnstundensatzKrankwochen = (Double) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_FEIERTAGSWOCHEN,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		dLohnstundensatzFeiertagswochen = (Double) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LOHNSTUNDENSATZKALKULATION_SONDERZAHLUNGEN,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		dLohnstundensatzSonderzahlungen = (Double) parameter.getCWertAsObject();

		wlaGehaltBruttoBrutto = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("pers.gehalt.bruttobrutto"));

		wnfKalkIstStunden.setActivatable(false);

		if (dLohnstundensatzKalenderwochen != 0) {
			wnfLohnmittelstundensatz.setActivatable(false);
		}

		wnfGehaltBruttoBrutto.addFocusListener(this);
		wnfPraemieBruttoBrutto.addFocusListener(this);
		wnfLohnmittelstundensatz.addFocusListener(this);

		wnfFaktor.addFocusListener(this);
		wnfAufschlagLohnmittelstundensatz.getWrapperWrapperNumberField()
				.addFocusListener(this);
		wnfStundensatz.getWrapperWrapperNumberField().addFocusListener(this);

		wlaGehaltNetto = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("pers.gehalt.netto"));
		wlaPraemieBruttoBrutto = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("pers.gehalt.praemiebruttobrutto"));

		wlaUestpauschale.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.ueberstundenpauschale"));
		wlaStundensatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.stundensatz"));
		wlaVerfuegbarkeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.verfuegbarkeit"));
		wlaKmgeld1.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.kmgeld")
				+ " 1");
		wlaBisKilometer.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.biskilometer"));
		wlaKmgeld2.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.kmgeld")
				+ " 2");
		wspJahr.setMandatoryField(true);
		wnfGehaltBrutto.setMinimumValue(0);
		wnfUestpauschale.setMinimumValue(0);

		wnfVerfuegbarkeit.setMinimumValue(0);
		wnfKmgeld1.setMinimumValue(0);
		wnfKmgeld1.setFractionDigits(4);
		wnfBisKilometer.setMinimumValue(0);
		wnfKmgeld2.setMinimumValue(0);
		wnfKmgeld2.setFractionDigits(4);

		wnfBisKilometer.setMandatoryField(true);
		wnfUestpauschale.setMandatoryField(true);
		wnfStundensatz.setMandatoryField(true);
		wnfVerfuegbarkeit.setMandatoryField(true);
		wnfKmgeld1.setMandatoryField(true);
		wnfKmgeld2.setMandatoryField(true);
		wnfGehaltBrutto.setMandatoryField(true);

		wcbAlleinverdiener.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.alleinverdiener"));
		wcbAlleinerzieher.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.alleinerzieher"));
		wcbKKSGebBefreit.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.kksgebuehrbefreit"));
		wcbUestdauszahlen.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgehalt.uestdausahlen"));

		wlaLohnmittelstundensatz.setText(LPMain.getInstance()
				.getTextRespectUISPr("pers.gehalt.lohnmittelstundensatz"));

		wlaKalkIstStunden.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.gehalt.kalkiststunden"));

		wlaFaktor.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.gehalt.faktor"));

		wcbKKSGebBefreit
				.addActionListener(new PanelPersonalgehalt_wcbKKSGebBefreit_actionAdapter(
						this));
		wlaWaehrungGehaltBrutto.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungGehaltBrutto.setText("WHG");
		wlaEinheitUeberstundenpauschale
				.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitUeberstundenpauschale.setText("h");
		wlaEinheitStundensatz.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitStundensatz.setText("WHG");
		wlaEinheitVerfuegbarkeit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitVerfuegbarkeit.setText("%");
		wlaWaehrungKMGeld1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungKMGeld1.setText("WHG");
		wlaWaehrungKMGeld2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungKMGeld2.setText("WHG");
		wtfGrundkksgebbefreit.setText("");
		wtfGrundkksgebbefreit
				.setColumnsMax(PersonalFac.MAX_PERSONALGEHALT_GRUNDKKSBEFREIT);
		wlaMonat.setText(LPMain.getInstance().getTextRespectUISPr("lp.monat1"));
		wcoMonat.setMandatoryField(true);

		bg.add(wnfAufschlagLohnmittelstundensatz.getWrbFixNumber());
		bg.add(wnfStundensatz.getWrbFixNumber());

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaJahr, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wspJahr, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 60, 0));
		jpaWorkingOn.add(new WrapperLabel(""), new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 30, 0));

		// Spalte2
		jpaWorkingOn.add(wlaGehaltBrutto, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfGehaltBrutto, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));
		jpaWorkingOn.add(wlaWaehrungGehaltBrutto, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30, 0));

		// Spalte 3
		jpaWorkingOn.add(new WrapperLabel(""), new GridBagConstraints(6,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(new WrapperLabel(""), new GridBagConstraints(7,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 30, 0));

		iZeile++;

		jpaWorkingOn.add(wlaMonat, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoMonat, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 80, 0));

		jpaWorkingOn.add(wlaGehaltNetto, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGehaltNetto, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		WrapperLabel whgNetto = new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung());
		whgNetto.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(whgNetto, new GridBagConstraints(5, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaVerfuegbarkeit, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfVerfuegbarkeit, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));
		jpaWorkingOn.add(wlaEinheitVerfuegbarkeit, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaGehaltBruttoBrutto, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGehaltBruttoBrutto, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		WrapperLabel whgBruttoBrutto = new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung());
		whgBruttoBrutto.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(whgBruttoBrutto, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaLeistungswert, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfLeistungswert, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		WrapperLabel einheitLeistungswert = new WrapperLabel("%");
		einheitLeistungswert.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(einheitLeistungswert, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaPraemieBruttoBrutto, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPraemieBruttoBrutto, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		WrapperLabel whgPraemieBruttoBrutto = new WrapperLabel(LPMain
				.getTheClient().getSMandantenwaehrung());
		whgPraemieBruttoBrutto.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(whgPraemieBruttoBrutto, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaUestpauschale, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUestpauschale, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		jpaWorkingOn.add(wlaEinheitUeberstundenpauschale,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wlaLohnmittelstundensatz, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfLohnmittelstundensatz, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		WrapperLabel whgLohnmittelstundensatz = new WrapperLabel(LPMain
				.getTheClient().getSMandantenwaehrung());
		whgLohnmittelstundensatz.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(whgLohnmittelstundensatz, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (dLohnstundensatzKalenderwochen != 0) {
			jpaWorkingOn.add(wlaKalkIstStunden,
					new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jpaWorkingOn.add(wnfKalkIstStunden, new GridBagConstraints(7,
					iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50,
					0));
		}
		iZeile++;
		jpaWorkingOn.add(wcbUestdauszahlen, new GridBagConstraints(0, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUestdPuffer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), -70, 0));

		wlaFaktor.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaFaktor, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfFaktor, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 40, 2, 20), 30, 0));

		jpaWorkingOn.add(new JLabel("%"), new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 8, 0));

		jpaWorkingOn.add(wnfAufschlagLohnmittelstundensatz,
				new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 50, 0));

		iZeile++;

		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("pers.gehalt.verbleibenderpuffer")),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 20, 2), 0, 0));
		jpaWorkingOn
				.add(wnfUestdPuffer, new GridBagConstraints(1, iZeile, 1, 1,
						0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 20, 2),
						-50, 0));

		jpaWorkingOn.add(wlaStundensatz, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 20, 2), 0, 0));
		jpaWorkingOn
				.add(wnfStundensatz, new GridBagConstraints(4, iZeile, 1, 1,
						0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 20, 2),
						-50, 0));

		jpaWorkingOn.add(wlaEinheitStundensatz, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 20, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKmgeld1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKmgeld1, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		WrapperLabel einheitkmgeld1 = new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung());
		einheitkmgeld1.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(einheitkmgeld1, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbAlleinverdiener, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbAlleinerzieher, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBisKilometer, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBisKilometer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		jpaWorkingOn.add(wcbKKSGebBefreit, new GridBagConstraints(3, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaKmgeld2, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKmgeld2, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		WrapperLabel einheitkmgeld2 = new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung());
		einheitkmgeld2.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(einheitkmgeld2, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfGrundkksgebbefreit, new GridBagConstraints(3,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	private Double holeJahresIstStundenausZeitmodell() throws Throwable {

		if (dLohnstundensatzKalenderwochen > 0) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, wspJahr.getInteger());
			c.set(Calendar.MONTH, (Integer) wcoMonat.getKeyOfSelectedItem());

			PersonalzeitmodellDto dto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalzeitmodellFindZeitmodellZuDatum(
							internalFramePersonal.getPersonalDto().getIId(),
							Helper.cutTimestamp(new java.sql.Timestamp(c
									.getTimeInMillis())));
			if (dto != null) {

				double dJahresurlaubInWochen = 0;
				UrlaubsanspruchDto[] uDtos = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.urlaubsanspruchFindByPersonalIIdIJahrKleiner(
								internalFramePersonal.getPersonalDto().getIId(),
								wspJahr.getInteger());
				if (uDtos.length > 0) {
					dJahresurlaubInWochen = uDtos[0].getFJahresurlaubinwochen();

				}

				return DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.getSummeSollzeitMontagBisSonntag(
								dto.getZeitmodellIId())
						* (dLohnstundensatzKalenderwochen
								- dJahresurlaubInWochen
								- dLohnstundensatzKrankwochen - dLohnstundensatzFeiertagswochen);
			} else {
				return 0D;
			}
		}
		return 0D;
	}

	private void berechneKalkulatorischenStundensatz(
			boolean bKommeVonLohnmittelstundensatz) throws Throwable {

		if (dLohnstundensatzKalenderwochen > 0) {

			if (wnfGehaltBruttoBrutto.getBigDecimal() == null) {
				wnfGehaltBruttoBrutto.setBigDecimal(new BigDecimal(0));
			}
			if (wnfPraemieBruttoBrutto.getBigDecimal() == null) {
				wnfPraemieBruttoBrutto.setBigDecimal(new BigDecimal(0));
			}

			BigDecimal bdBruttoBrutto = wnfGehaltBruttoBrutto.getBigDecimal();
			BigDecimal bdPraemieBruttoBrutto = wnfPraemieBruttoBrutto
					.getBigDecimal();

			if (wnfKalkIstStunden.getBigDecimal() != null
					&& wnfKalkIstStunden.getBigDecimal().doubleValue() != 0) {
				BigDecimal kalkStundensatz = bdBruttoBrutto
						.add(bdPraemieBruttoBrutto)
						.multiply(
								new BigDecimal(
										12 + dLohnstundensatzSonderzahlungen))
						.divide(wnfKalkIstStunden.getBigDecimal(), 4,
								BigDecimal.ROUND_HALF_UP);

				wnfLohnmittelstundensatz.setBigDecimal(kalkStundensatz);
			} else {
				wnfKalkIstStunden.setDouble(0D);
			}

		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		wnfKalkIstStunden.setForeground(Color.BLACK);
		wnfFaktor.setForeground(Color.BLACK);
		wnfAufschlagLohnmittelstundensatz.setForeground(Color.BLACK);
		wnfKalkIstStunden.setToolTipText("");
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			Calendar cal = Calendar.getInstance();
			wcoMonat.setSelectedIndex(cal.get(Calendar.MONTH));
			wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));
			clearStatusbar();

			// PJ18287

			PersonalgehaltDto pdDto_Vorheriges = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalgehaltFindLetztePersonalgehalt(
							internalFramePersonal.getPersonalDto().getIId(),
							cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));

			if (pdDto_Vorheriges != null) {
				wnfBisKilometer.setDouble(pdDto_Vorheriges.getFBiskilometer());
				wnfGehaltBrutto.setBigDecimal(pdDto_Vorheriges.getNGehalt());
				wnfKmgeld1.setBigDecimal(pdDto_Vorheriges.getNKmgeld1());
				wnfKmgeld2.setBigDecimal(pdDto_Vorheriges.getNKmgeld2());
				wnfStundensatz
						.setBigDecimal(pdDto_Vorheriges.getNStundensatz());
				wnfUestdPuffer
						.setBigDecimal(pdDto_Vorheriges.getNUestdpuffer());
				wnfUestpauschale
						.setDouble(pdDto_Vorheriges.getFUestpauschale());
				wnfVerfuegbarkeit.setDouble(pdDto_Vorheriges
						.getFVerfuegbarkeit());
				wnfGehaltNetto
						.setBigDecimal(pdDto_Vorheriges.getNGehaltNetto());
				wnfGehaltBruttoBrutto.setBigDecimal(pdDto_Vorheriges
						.getNGehaltBruttobrutto());
				wnfPraemieBruttoBrutto.setBigDecimal(pdDto_Vorheriges
						.getNPraemieBruttobrutto());
				wnfFaktor.setDouble(pdDto_Vorheriges
						.getFFaktorLohnmittelstundensatz());
				wnfLohnmittelstundensatz.setBigDecimal(pdDto_Vorheriges
						.getNLohnmittelstundensatz());
				wnfAufschlagLohnmittelstundensatz
						.setBigDecimal(pdDto_Vorheriges
								.getNAufschlagLohnmittelstundensatz());

			} else {
				wnfBisKilometer.setInteger(0);
				wnfGehaltBrutto.setInteger(0);
				wnfKmgeld1.setInteger(0);
				wnfKmgeld2.setInteger(0);
				wnfStundensatz.setBigDecimal(new java.math.BigDecimal(0));
				wnfUestdPuffer.setInteger(0);
				wnfUestpauschale.setInteger(0);
				wnfVerfuegbarkeit.setInteger(0);
				wnfGehaltNetto.setInteger(0);
				wnfGehaltBruttoBrutto.setInteger(0);
				wnfPraemieBruttoBrutto.setInteger(0);
				wnfFaktor.setDouble(0D);
				wnfLohnmittelstundensatz.setDouble(0D);
				wnfAufschlagLohnmittelstundensatz.setBigDecimal(new BigDecimal(
						0));

			}
			wnfKalkIstStunden.setDouble(holeJahresIstStundenausZeitmodell());
			berechneKalkulatorischenStundensatz(false);
			focusLost(new FocusEvent(wnfPraemieBruttoBrutto, -1));
		} else {
			personalgehaltDto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.personalgehaltFindByPrimaryKey((Integer) key);

			dto2Components();

			Double d = holeJahresIstStundenausZeitmodell();

			if (d != null) {
				if (wnfKalkIstStunden.getDouble() == null) {
					wnfKalkIstStunden.setForeground(Color.RED);
				} else {
					if (wnfKalkIstStunden.getDouble().doubleValue() != d
							.doubleValue()) {
						wnfKalkIstStunden.setForeground(Color.RED);
						wnfKalkIstStunden
								.setToolTipText(LPMain
										.getInstance()
										.getTextRespectUISPr(
												"pers.personalgehalt.aktuellerrechneteiststunden")
										+ " "
										+ Helper.formatZahl(d, LPMain
												.getInstance().getTheClient()
												.getLocUi()));
					}
				}
			} else {
				if (wnfKalkIstStunden.getDouble() != null) {
					wnfKalkIstStunden.setForeground(Color.RED);
				}
			}

			if (wnfFaktor.getDouble() != null
					&& wnfFaktor.getDouble().doubleValue() < 0) {
				wnfFaktor.setForeground(Color.RED);
			}
			if (wnfAufschlagLohnmittelstundensatz.getBigDecimal() != null
					&& wnfAufschlagLohnmittelstundensatz.getBigDecimal()
							.doubleValue() < 0) {
				wnfAufschlagLohnmittelstundensatz.getWrapperWrapperNumberField().setForeground(Color.RED);
			}

		}
	}

	public void wcbKKSGebBefreit_actionPerformed(ActionEvent e) {
		if (wcbKKSGebBefreit.isSelected()) {
			wtfGrundkksgebbefreit.setMandatoryField(true);
		} else {
			wtfGrundkksgebbefreit.setMandatoryField(false);
		}
	}

}

class PanelPersonalgehalt_wcbKKSGebBefreit_actionAdapter implements
		ActionListener {
	private PanelPersonalgehalt adaptee;

	PanelPersonalgehalt_wcbKKSGebBefreit_actionAdapter(
			PanelPersonalgehalt adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbKKSGebBefreit_actionPerformed(e);
	}
}
