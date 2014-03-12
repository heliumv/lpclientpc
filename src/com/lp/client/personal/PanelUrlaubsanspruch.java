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

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lp.client.frame.*;
import com.lp.client.frame.component.*;
import com.lp.client.pc.*;
import com.lp.server.personal.service.*;
import com.lp.util.EJBExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;

public class PanelUrlaubsanspruch extends PanelBasis implements ChangeListener {

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
	private UrlaubsanspruchDto urlaubsanspruchDto = null;
	private WrapperSpinner wspJahr = new WrapperSpinner(new Integer(0),
			new Integer(0), new Integer(9999), new Integer(1));

	private WrapperLabel wlaJahr = new WrapperLabel();
	private WrapperLabel wlaTage = new WrapperLabel();
	private WrapperLabel wlaStunden = new WrapperLabel();
	private WrapperLabel wlaTageZusaetzlich = new WrapperLabel();
	private WrapperLabel wlaStundenZusaetzlich = new WrapperLabel();
	private WrapperLabel wlaEintrittsdatum = new WrapperLabel();
	private WrapperLabel wlaKeinGuelitgesEintrittsdatum = new WrapperLabel();
	private JButton wbuAliquot = new JButton();
	static final public String ACTION_SPECIAL_ALIQUOT = "ACTION_SPECIAL_ALIQUOT";
	static final public String ACTION_SPECIAL_ENDEDESJAHRES = "ACTION_SPECIAL_ENDEDESJAHRES";
	private JButton wbuEndeDesJahres = new JButton();
	private WrapperLabel wlaVerfuegbar = new WrapperLabel();
	private WrapperTextField wtfUrlaubverfuegbar = new WrapperTextField();
	private WrapperNumberField wnfTage = new WrapperNumberField();
	private WrapperNumberField wnfUmrechnungTage = new WrapperNumberField();
	private WrapperLabel wlaInTagen = new WrapperLabel();

	private WrapperLabel wlaAnspruchInWochen = new WrapperLabel();
	private WrapperNumberField wnfAnspruchInWochen = new WrapperNumberField();

	private WrapperNumberField wnfStunden = new WrapperNumberField();
	private WrapperNumberField wnfTageZusaetzlich = new WrapperNumberField();
	private WrapperNumberField wnfStundenZusaetzlich = new WrapperNumberField();
	private WrapperDateField wdfEintrittsdatum = new WrapperDateField();
	private WrapperLabel wlaDummy = new WrapperLabel();

	private WrapperLabel wlaResturlaubStunden = new WrapperLabel();
	private WrapperNumberField wnfResturlaubStunden = new WrapperNumberField();

	private WrapperLabel wlaResturlaubTage = new WrapperLabel();
	private WrapperNumberField wnfResturlaubTage = new WrapperNumberField();

	private WrapperCheckBox wcbGesperrt = new WrapperCheckBox();

	public PanelUrlaubsanspruch(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfTage;
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		urlaubsanspruchDto = new UrlaubsanspruchDto();

		leereAlleFelder(this);
		GregorianCalendar cal = new GregorianCalendar();
		wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		// urlaubsanspruch berechnen
		if (e.getActionCommand().equals(ACTION_SPECIAL_ALIQUOT)) {
			java.sql.Date dAbrechnungszeitpunkt = new java.sql.Date(
					System.currentTimeMillis());

			UrlaubsabrechnungDto v = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.berechneUrlaubsAnspruch(
							internalFramePersonal.getPersonalDto().getIId(),
							dAbrechnungszeitpunkt);
			wtfUrlaubverfuegbar
					.setText(v.getNVerfuegbarerUrlaubTage().toString()
							+ " Tage / "
							+ v.getNVerfuegbarerUrlaubStunden().toString()
							+ " Stunden");
			DialogUrlaubsanspruch d = new DialogUrlaubsanspruch(v);

			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ENDEDESJAHRES)) {
			java.sql.Date dAbrechnungszeitpunkt = new java.sql.Date(
					System.currentTimeMillis());

			UrlaubsabrechnungDto v = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.berechneUrlaubsAnspruch(
							internalFramePersonal.getPersonalDto().getIId(),
							dAbrechnungszeitpunkt);
			DialogUrlaubsanspruchEndeDesJahres d = new DialogUrlaubsanspruchEndeDesJahres(
					v);
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);

			d.setVisible(true);
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeUrlaubsanspruch(urlaubsanspruchDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		urlaubsanspruchDto.setPersonalIId(internalFramePersonal
				.getPersonalDto().getIId());
		urlaubsanspruchDto.setIJahr((Integer) wspJahr.getValue());
		urlaubsanspruchDto.setFTage(wnfTage.getDouble());
		urlaubsanspruchDto.setFStunden(wnfStunden.getDouble());
		urlaubsanspruchDto.setFTagezusaetzlich(wnfTageZusaetzlich.getDouble());
		urlaubsanspruchDto.setFStundenzusaetzlich(wnfStundenZusaetzlich
				.getDouble());
		urlaubsanspruchDto.setBGesperrt(wcbGesperrt.getShort());
		urlaubsanspruchDto.setFResturlaubjahresendestunden(wnfResturlaubStunden
				.getDouble());
		urlaubsanspruchDto.setFResturlaubjahresendetage(wnfResturlaubTage
				.getDouble());
		urlaubsanspruchDto.setFJahresurlaubinwochen(wnfAnspruchInWochen
				.getDouble());

	}

	protected void dto2Components() throws Throwable {
		wspJahr.setValue(urlaubsanspruchDto.getIJahr());
		wnfTage.setDouble(urlaubsanspruchDto.getFTage());
		wnfStunden.setDouble(urlaubsanspruchDto.getFStunden());
		wnfTageZusaetzlich.setDouble(urlaubsanspruchDto.getFTagezusaetzlich());
		wnfStundenZusaetzlich.setDouble(urlaubsanspruchDto
				.getFStundenzusaetzlich());
		wcbGesperrt.setShort(urlaubsanspruchDto.getBGesperrt());

		wnfResturlaubStunden.setDouble(urlaubsanspruchDto
				.getFResturlaubjahresendestunden());
		wnfResturlaubTage.setDouble(urlaubsanspruchDto
				.getFResturlaubjahresendetage());
		wnfAnspruchInWochen.setDouble(urlaubsanspruchDto
				.getFJahresurlaubinwochen());
		wnfUmrechnungTage.setDouble(urlaubsanspruchDto
				.getFJahresurlaubinwochen() * 5);
		this.setStatusbarPersonalIIdAendern(urlaubsanspruchDto
				.getPersonaIIdAendern());
		this.setStatusbarTAendern(urlaubsanspruchDto.getTAendern());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (urlaubsanspruchDto.getIId() == null) {
				urlaubsanspruchDto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate()
						.createUrlaubsanspruch(urlaubsanspruchDto));
				setKeyWhenDetailPanel(urlaubsanspruchDto.getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updateUrlaubsanspruch(urlaubsanspruchDto);
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

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaJahr.setText(LPMain
				.getTextRespectUISPr("pers.urlaubsanspruch.abjahr"));
		wlaTage.setText(LPMain
				.getTextRespectUISPr("pers.urlaubsanspruch.tageltzm"));
		wlaStunden.setText(LPMain
				.getTextRespectUISPr("pers.urlaubsanspruch.stdltzm"));

		wlaTageZusaetzlich.setText(LPMain
				.getTextRespectUISPr("pers.urlaubsanspruch.tagezusaetzlich"));
		wlaStundenZusaetzlich
				.setText(LPMain
						.getTextRespectUISPr("pers.urlaubsanspruch.stundenzusaetzlich"));

		wlaKeinGuelitgesEintrittsdatum.setForeground(Color.RED);
		
		wcbGesperrt.setText(LPMain
				.getTextRespectUISPr("pers.gleitzeitsaldo.gesperrt"));

		wlaResturlaubTage.setText(LPMain
				.getTextRespectUISPr("pers.urlaubanspruch.tageresturlaub"));
		wlaResturlaubStunden.setText(LPMain
				.getTextRespectUISPr("pers.urlaubanspruch.stundenresturlaub"));

		wlaEintrittsdatum.setText(LPMain
				.getTextRespectUISPr("pers.eintrittaustritt.eintritt"));
		wbuAliquot.setMaximumSize(new Dimension(50, 19));
		wbuAliquot.setMinimumSize(new Dimension(50, 19));
		wbuAliquot.setPreferredSize(new Dimension(50, 19));
		wbuAliquot.setText(LPMain
				.getTextRespectUISPr("pers.urlaubsanspruch.anspruchaliquot"));
		wbuAliquot.setActionCommand(ACTION_SPECIAL_ALIQUOT);
		wbuAliquot.addActionListener(this);

		wlaAnspruchInWochen
				.setText(LPMain
						.getTextRespectUISPr("pers.urlaubsanspruch.jahresanspruchinwochen"));
		wnfAnspruchInWochen.setFractionDigits(2);

		wbuEndeDesJahres.setMaximumSize(new Dimension(50, 19));
		wbuEndeDesJahres.setMinimumSize(new Dimension(50, 19));
		wbuEndeDesJahres.setPreferredSize(new Dimension(50, 19));
		wbuEndeDesJahres.setText(LPMain
				.getTextRespectUISPr("pers.urlaubsanspruch.endedesjahres"));
		wbuEndeDesJahres.setActionCommand(ACTION_SPECIAL_ENDEDESJAHRES);
		wbuEndeDesJahres.addActionListener(this);

		wlaVerfuegbar.setHorizontalAlignment(SwingConstants.CENTER);
		wlaVerfuegbar.setText(LPMain
				.getTextRespectUISPr("pers.urlaubsanspruch.verfuegbarerurlaub")
				+ ":");
		wlaInTagen.setHorizontalAlignment(SwingConstants.CENTER);
		wlaInTagen
				.setText(LPMain
						.getTextRespectUISPr("pers.urlaubsanspruch.jahresanspruchintagen"));

		wtfUrlaubverfuegbar.setActivatable(false);
		wtfUrlaubverfuegbar.setText("");
		wnfTage.setFractionDigits(2);

		wnfTage.setMandatoryField(true);
		wnfAnspruchInWochen.setMandatoryField(true);

		wnfAnspruchInWochen
				.addFocusListener(new PanelUrlaubsanspruch_wnfAnspruchInWochen_focusAdapter(
						this));
		wnfUmrechnungTage
				.addFocusListener(new PanelUrlaubsanspruch_wnfUmrechnungTage_focusAdapter(
						this));

		wnfStunden.setMandatoryField(true);

		wnfStunden.setActivatable(false);

		wnfTageZusaetzlich.setMandatoryField(true);
		wnfTageZusaetzlich.setFractionDigits(2);

		wnfStundenZusaetzlich.setMandatoryField(true);

		wdfEintrittsdatum.setActivatable(false);
		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		wlaDummy.setRequestFocusEnabled(true);
		wlaDummy.setText("");
		wspJahr.setMandatoryField(true);

		wspJahr.addChangeListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaJahr, new GridBagConstraints(0, iZeile, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 150, 0));

		jpaWorkingOn.add(wspJahr, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), -25, 0));
		jpaWorkingOn.add(wlaKeinGuelitgesEintrittsdatum, new GridBagConstraints(2, iZeile, 2, 1,
				0.00, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaAnspruchInWochen, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfAnspruchInWochen, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -25, 0));

		jpaWorkingOn.add(wlaInTagen, new GridBagConstraints(2, iZeile, 1, 1,
				0.00, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wnfUmrechnungTage, new GridBagConstraints(3, iZeile,
				1, 1, 0.00, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), -25, 0));

		jpaWorkingOn.add(wlaVerfuegbar, new GridBagConstraints(4, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaTage, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfTage, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), -25, 0));

		jpaWorkingOn.add(wlaDummy, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 100, 0));
		jpaWorkingOn.add(wtfUrlaubverfuegbar, new GridBagConstraints(3, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaTageZusaetzlich, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfTageZusaetzlich, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -25, 0));
		jpaWorkingOn.add(wbuAliquot, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		jpaWorkingOn.add(wbuEndeDesJahres, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaStunden, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfStunden, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), -25, 0));
		jpaWorkingOn.add(wlaResturlaubTage, new GridBagConstraints(2, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));
		jpaWorkingOn.add(wnfResturlaubTage, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaStundenZusaetzlich, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfStundenZusaetzlich, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -25, 0));
		jpaWorkingOn.add(wlaResturlaubStunden, new GridBagConstraints(2,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));
		jpaWorkingOn.add(wnfResturlaubStunden, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaEintrittsdatum, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfEintrittsdatum, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbGesperrt, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		wlaKeinGuelitgesEintrittsdatum.setText("");
		try {
			
			java.sql.Timestamp tEintritt=null;
			
			EintrittaustrittDto eaDto=	DelegateFactory
			.getInstance()
			.getPersonalDelegate()
			.eintrittaustrittFindLetztenEintrittBisDatum(
					internalFramePersonal.getPersonalDto().getIId());
			if(eaDto!=null){
				tEintritt=eaDto.getTEintritt();
			}
			wdfEintrittsdatum.setTimestamp(tEintritt);
		} catch (ExceptionLP e) {
			
			if(e.getICode()==EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM){
			
				wlaKeinGuelitgesEintrittsdatum.setText(LPMain
						.getTextRespectUISPr("pers.urlaubsanspruch.keineintritt"));
			} else {
				handleException(e, false);
			}
		}

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
			Calendar cal = Calendar.getInstance();
			wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));
			wnfStundenZusaetzlich.setBigDecimal(new java.math.BigDecimal(0));
			wnfTageZusaetzlich.setBigDecimal(new java.math.BigDecimal(0));
			wnfAnspruchInWochen.setBigDecimal(new java.math.BigDecimal(0));
			wnfTage.setBigDecimal(new java.math.BigDecimal(0));
			wnfStunden.setInteger(0);

		} else {

			urlaubsanspruchDto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.urlaubsanspruchFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource().equals(wspJahr)) {

			try {
				LockStateValue lockstateValue = getLockedstateDetailMainKey();
				int iLockState = lockstateValue.getIState();
				if (iLockState == LOCK_IS_LOCKED_BY_ME
						|| iLockState == LOCK_FOR_NEW) {

					if (wspJahr.getInteger() != null
							&& wspJahr.getInteger() < 2009) {
						wnfStunden.setEditable(true);
						wnfStunden.setActivatable(true);

						wnfTage.setEditable(true);
						wnfTage.setActivatable(true);

					} else {
						wnfStunden.setEditable(false);
						wnfStunden.setActivatable(false);

						wnfTage.setEditable(false);
						wnfTage.setActivatable(false);
					}
				}
			} catch (Throwable e1) {
				handleException(e1, true);
			}

		}

	}

	void wnfAnspruchInWochen_focusLost(FocusEvent e) {
		try {
			if (wnfAnspruchInWochen.getDouble() != null) {
				wnfUmrechnungTage
						.setDouble(wnfAnspruchInWochen.getDouble() * 5);
			}
		} catch (Throwable ex) {
			// nothing here
		}
	}

	void wnfUmrechnungTage_focusLost(FocusEvent e) {
		try {
			if (wnfUmrechnungTage.getDouble() != null) {
				wnfAnspruchInWochen.setDouble(wnfUmrechnungTage.getDouble()
						/ ((double) 5));
			}
		} catch (Throwable ex) {
			// nothing here
		}
	}

}

class PanelUrlaubsanspruch_wnfAnspruchInWochen_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelUrlaubsanspruch adaptee;

	PanelUrlaubsanspruch_wnfAnspruchInWochen_focusAdapter(
			PanelUrlaubsanspruch adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfAnspruchInWochen_focusLost(e);
	}
}

class PanelUrlaubsanspruch_wnfUmrechnungTage_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelUrlaubsanspruch adaptee;

	PanelUrlaubsanspruch_wnfUmrechnungTage_focusAdapter(
			PanelUrlaubsanspruch adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfUmrechnungTage_focusLost(e);
	}
}
