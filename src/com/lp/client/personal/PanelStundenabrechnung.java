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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.GleitzeitsaldoDto;
import com.lp.server.personal.service.StundenabrechnungDto;

@SuppressWarnings("static-access")
public class PanelStundenabrechnung extends PanelBasis implements
		PropertyChangeListener {

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
	private StundenabrechnungDto stundenabrechnungDto = null;

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();
	private WrapperLabel wlaMehrstunden = new WrapperLabel();
	private WrapperLabel wlaUestfrei50 = new WrapperLabel();
	private WrapperLabel wlaUestpflichtig50 = new WrapperLabel();
	private WrapperLabel wlaUestfrei100 = new WrapperLabel();
	private WrapperLabel wlaUest200 = new WrapperLabel();
	private WrapperLabel wlaUestpflichtig100 = new WrapperLabel();
	private WrapperLabel wlaUestdpausch = new WrapperLabel();
	private WrapperLabel wlaQualifikationspraemie = new WrapperLabel();
	private WrapperNumberField wnfMehrstunden = new WrapperNumberField();
	private WrapperNumberField wnfUestfrei50 = new WrapperNumberField();
	private WrapperNumberField wnfUestpflichtig50 = new WrapperNumberField();
	private WrapperNumberField wnfUestfrei100 = new WrapperNumberField();
	private WrapperNumberField wnfUest200 = new WrapperNumberField();
	private WrapperNumberField wnfUestpflichtig100 = new WrapperNumberField();
	private WrapperNumberField wnfGutstunden = new WrapperNumberField();
	private WrapperNumberField wnfQualifikationspraemie = new WrapperNumberField();
	private WrapperLabel wlaEinheitMehrstd = new WrapperLabel();
	private WrapperLabel wlaEinheitUestfrei50 = new WrapperLabel();
	private WrapperLabel wrapperLabel3 = new WrapperLabel();
	private WrapperLabel wrapperLabel4 = new WrapperLabel();
	private WrapperLabel wrapperLabel5 = new WrapperLabel();
	private WrapperLabel wlaeinheitUestpauschale = new WrapperLabel();
	private WrapperLabel wlaEinheitQualifikationspraemie = new WrapperLabel();
	private WrapperLabel wlaVerfuegbar = new WrapperLabel();
	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperNumberField wnfMehrstundenVorhanden = new WrapperNumberField();
	private WrapperNumberField wnfNormalstundenVorhanden = new WrapperNumberField();
	private WrapperNumberField wnfUestfrei50Vorhanden = new WrapperNumberField();
	private WrapperNumberField wnfUestpflichtig50Vorhanden = new WrapperNumberField();
	private WrapperNumberField wnfUestfrei100Vorhanden = new WrapperNumberField();
	private WrapperNumberField wnfUestpflichtig100Vorhanden = new WrapperNumberField();
	private WrapperNumberField wnfUest200Vorhanden = new WrapperNumberField();
	private WrapperTextField wtfKommentar = new WrapperTextField();

	private WrapperLabel wlaNormalstunden = new WrapperLabel();
	private WrapperNumberField wnfNormalstunden = new WrapperNumberField();

	public PanelStundenabrechnung(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfMehrstunden;
	}

	protected void setDefaults() throws Throwable {
		wlaEinheitQualifikationspraemie.setText(DelegateFactory.getInstance()
				.getMandantDelegate().mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr());

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		stundenabrechnungDto = new StundenabrechnungDto();

		leereAlleFelder(this);
		wdfDatum
				.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeStundenabrechnung(stundenabrechnungDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		stundenabrechnungDto.setPersonalIId(internalFramePersonal
				.getPersonalDto().getIId());

		stundenabrechnungDto.setTDatum(wdfDatum.getTimestamp());
		stundenabrechnungDto.setNMehrstunden(wnfMehrstunden.getBigDecimal());
		stundenabrechnungDto.setNQualifikationspraemie(wnfQualifikationspraemie
				.getBigDecimal());
		stundenabrechnungDto.setNUestfrei100(wnfUestfrei100.getBigDecimal());
		stundenabrechnungDto.setNUestfrei50(wnfUestfrei50.getBigDecimal());
		stundenabrechnungDto.setNGutstunden(wnfGutstunden.getBigDecimal());
		stundenabrechnungDto.setNUestpflichtig100(wnfUestpflichtig100
				.getBigDecimal());
		stundenabrechnungDto.setNUest200(wnfUest200.getBigDecimal());
		stundenabrechnungDto.setNUestpflichtig50(wnfUestpflichtig50
				.getBigDecimal());
		stundenabrechnungDto
				.setNNormalstunden(wnfNormalstunden.getBigDecimal());
		stundenabrechnungDto.setCKommentar(wtfKommentar.getText());
	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wdfDatum.setTimestamp(stundenabrechnungDto.getTDatum());
		wnfMehrstunden.setBigDecimal(stundenabrechnungDto.getNMehrstunden());
		wnfUestfrei100.setBigDecimal(stundenabrechnungDto.getNUestfrei100());
		wnfUestfrei50.setBigDecimal(stundenabrechnungDto.getNUestfrei50());
		wnfGutstunden.setBigDecimal(stundenabrechnungDto.getNGutstunden());
		wnfNormalstunden
				.setBigDecimal(stundenabrechnungDto.getNNormalstunden());
		wnfUestpflichtig100.setBigDecimal(stundenabrechnungDto
				.getNUestpflichtig100());
		wnfUest200.setBigDecimal(stundenabrechnungDto.getNUest200());
		wnfUestpflichtig50.setBigDecimal(stundenabrechnungDto
				.getNUestpflichtig50());
		wnfQualifikationspraemie.setBigDecimal(stundenabrechnungDto
				.getNQualifikationspraemie());
		wtfKommentar.setText(stundenabrechnungDto.getCKommentar());

		this.setStatusbarPersonalIIdAendern(stundenabrechnungDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(stundenabrechnungDto.getTAendern());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (stundenabrechnungDto.getIId() == null) {
				stundenabrechnungDto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate().createStundenabrechnung(
								stundenabrechnungDto));
				setKeyWhenDetailPanel(stundenabrechnungDto.getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updateStundenabrechnung(stundenabrechnungDto);
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

		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.stundenabrechnung.auszahlungsdatum"));
		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		wlaMehrstunden.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.gleitzeitsaldo.mehrstunden"));
		wlaUestfrei50.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.stundenabrechnung.uestfrei50"));
		wlaUestpflichtig50.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.stundenabrechnung.uestpflichtig50"));
		wlaUestfrei100.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.stundenabrechnung.uestfrei100"));
		wlaUestpflichtig100.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.stundenabrechnung.uestpflichtig100"));
		wlaUest200.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.stundenabrechnung.200"));
		wlaUestdpausch.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.stundenabrechnung.gutstunden"));
		wlaQualifikationspraemie.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"pers.stundenabrechnung.qualifikationspraemie"));
		wlaNormalstunden.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.kollektiv.normalstunden"));

		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));

		wtfKommentar.setColumnsMax(80);

		wdfDatum.setMandatoryField(true);

		wdfDatum.getDisplay().addPropertyChangeListener(this);

		wnfMehrstunden.setMinimumValue(0);
		wnfUestfrei50.setMinimumValue(0);
		wnfUestpflichtig50.setMinimumValue(0);
		wnfUestfrei100.setMinimumValue(0);
		wnfUestpflichtig100.setMinimumValue(0);
		wnfGutstunden.setMinimumValue(0);
		wnfNormalstunden.setMinimumValue(0);
		wnfQualifikationspraemie.setMinimumValue(0);
		wlaEinheitMehrstd.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitMehrstd.setText("h");
		wlaEinheitUestfrei50.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitUestfrei50.setText("h");
		wrapperLabel3.setHorizontalAlignment(SwingConstants.LEFT);
		wrapperLabel3.setText("h");
		wrapperLabel4.setHorizontalAlignment(SwingConstants.LEFT);
		wrapperLabel4.setText("h");
		wrapperLabel5.setHorizontalAlignment(SwingConstants.LEFT);
		wrapperLabel5.setText("h");
		wlaeinheitUestpauschale.setHorizontalAlignment(SwingConstants.LEFT);
		wlaeinheitUestpauschale.setText("h");
		wlaEinheitQualifikationspraemie
				.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitQualifikationspraemie.setText("WHG");
		wlaVerfuegbar.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.stundenabrechnung.verfuegbar"));
		wnfMehrstundenVorhanden.setActivatable(false);
		wnfUestfrei50Vorhanden.setActivatable(false);
		wnfUestpflichtig50Vorhanden.setActivatable(false);
		wnfUest200Vorhanden.setActivatable(false);
		wnfUestfrei100Vorhanden.setActivatable(false);
		wnfUestpflichtig100Vorhanden.setActivatable(false);
		wnfNormalstundenVorhanden.setActivatable(false);
		// wnfGutstundenVorhanden.setActivatable(false);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, 0, 7, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
						2, 2, 0), 100, 0));
		jpaWorkingOn.add(wlaNormalstunden, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMehrstunden, new GridBagConstraints(0, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaUestfrei50, new GridBagConstraints(0, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaUestpflichtig50, new GridBagConstraints(0, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaUestfrei100, new GridBagConstraints(0, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaUestpflichtig100, new GridBagConstraints(0, 6, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaUest200, new GridBagConstraints(0, 7, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaUestdpausch, new GridBagConstraints(0, 8, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaQualifikationspraemie, new GridBagConstraints(0, 9,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUestfrei50, new GridBagConstraints(1, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfUestpflichtig50, new GridBagConstraints(1, 4, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfUestfrei100, new GridBagConstraints(1, 5, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfUestpflichtig100, new GridBagConstraints(1, 6, 4,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfUest200, new GridBagConstraints(1, 7, 4,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfGutstunden, new GridBagConstraints(1, 8, 5, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfQualifikationspraemie, new GridBagConstraints(1, 9,
				6, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wlaEinheitUestfrei50, new GridBagConstraints(2, 2, 7,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrapperLabel3, new GridBagConstraints(3, 3, 6, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrapperLabel4, new GridBagConstraints(4, 4, 5, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrapperLabel5, new GridBagConstraints(5, 5, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaeinheitUestpauschale, new GridBagConstraints(6, 6,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		
		WrapperLabel w200=new WrapperLabel("h");
		w200.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(w200,
				new GridBagConstraints(7, 7, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wlaEinheitQualifikationspraemie,
				new GridBagConstraints(7, 9, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wnfNormalstunden, new GridBagConstraints(1, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfMehrstunden, new GridBagConstraints(1, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wlaEinheitMehrstd, new GridBagConstraints(7, 1, 2, 1,
				0.05, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfNormalstundenVorhanden, new GridBagConstraints(9,
				1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wnfMehrstundenVorhanden, new GridBagConstraints(9, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wnfUestfrei50Vorhanden, new GridBagConstraints(9, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wnfUestpflichtig50Vorhanden, new GridBagConstraints(9,
				4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wnfUestfrei100Vorhanden, new GridBagConstraints(9, 5,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wnfUestpflichtig100Vorhanden, new GridBagConstraints(
				9, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wnfUest200Vorhanden, new GridBagConstraints(
				9, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		// jPanelWorkingOn.add(wnfGutstundenVorhanden,
		// new GridBagConstraints(9, 6, 1, 1, 0.0, 0.0
		// , GridBagConstraints.CENTER,
		// GridBagConstraints.NONE,
		// new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaVerfuegbar, new GridBagConstraints(8, 0, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, 10, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, 10, 9, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void propertyChange(PropertyChangeEvent e) {

		if (e.getSource() == wdfDatum.getDisplay()
				&& e.getNewValue() instanceof Date
				&& e.getPropertyName().equals("date")) {
			wdfDatum.setDate((Date) e.getNewValue());
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTime(wdfDatum.getDate());
				GleitzeitsaldoDto dto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.gleitzeitsaldoFindLetztenGleitzeitsaldo(
								internalFramePersonal.getPersonalDto().getIId(),
								new Integer(cal.get(Calendar.YEAR)),
								new Integer(cal.get(Calendar.MONTH)));

				if (dto != null) {
					wnfUestfrei100Vorhanden.setBigDecimal(dto
							.getNSaldouestfrei100());
					wnfUestfrei50Vorhanden.setBigDecimal(dto
							.getNSaldouestfrei50());
					wnfUestpflichtig100Vorhanden.setBigDecimal(dto
							.getNSaldouestpflichtig100());
					wnfUestpflichtig50Vorhanden.setBigDecimal(dto
							.getNSaldouestpflichtig50());
					wnfMehrstundenVorhanden.setBigDecimal(dto
							.getNSaldomehrstunden());
					wnfUest200Vorhanden.setBigDecimal(dto
							.getNSaldouest200());

					wnfNormalstundenVorhanden.setBigDecimal(dto.getNSaldo()
							.subtract(dto.getNSaldomehrstunden()).subtract(
									dto.getNSaldouest200()).subtract(
											dto.getNSaldouestfrei100()).subtract(
									dto.getNSaldouestpflichtig100()).subtract(
									dto.getNSaldouestfrei50()).subtract(
									dto.getNSaldouestpflichtig50()));
				} else {
					wnfUestfrei100Vorhanden.setBigDecimal(null);
					wnfUestfrei50Vorhanden.setBigDecimal(null);
					wnfUestpflichtig100Vorhanden.setBigDecimal(null);
					wnfUestpflichtig50Vorhanden.setBigDecimal(null);
					wnfMehrstundenVorhanden.setBigDecimal(null);
					wnfNormalstundenVorhanden.setBigDecimal(null);
					wnfUest200Vorhanden.setBigDecimal(null);
				}
			} catch (Throwable ex) {
				// brauche ich
				handleException(ex, false);
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

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			clearStatusbar();
		} else {
			stundenabrechnungDto = DelegateFactory.getInstance()
					.getPersonalDelegate().stundenabrechnungFindByPrimaryKey(
							(Integer) key);
			dto2Components();
		}
	}
}
