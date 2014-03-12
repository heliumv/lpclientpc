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
package com.lp.client.finanz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzamtDto;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um die Mandantdaten.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>08.03.05</I></p>
 * 
 * @author $Author: adi $
 * 
 * @version $Revision: 1.5 $
 */
public class PanelBuchungsparameter extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = null;
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperSelectField wsfEBSachkonten = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfEBDebitoren = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfEBKreditoren = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfErhaltAnzahlungVerr = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfErhaltAnzahlung = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfGegebenAnzahlungVerr = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfGegebenAnzahlungBezahlt = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	
	private WrapperSelectField wsfReverseChargeErhaltAnzahlungVerr = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfReverseChargeErhaltAnzahlungBezahlt = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfReverseChargeGegebenAnzahlungVerr = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);
	private WrapperSelectField wsfReverseChargeGegebenAnzahlungBezahlt = new WrapperSelectField(
			WrapperSelectField.SACHKONTO, getInternalFrame(), true);

	private TabbedPaneFinanzamt tpFinanzamt = null;

	public PanelBuchungsparameter(InternalFrame internalFrame,
			String add2TitleI, Object keyI, TabbedPaneFinanzamt tpFinanzamt)
			throws Throwable {
		super(internalFrame, add2TitleI, keyI);
		this.tpFinanzamt = tpFinanzamt;
		jbInit();
		initComponents();
	}

	private void initPanel() throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();

		add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		
		wsfEBKreditoren.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.kreditoren"));
		wsfEBDebitoren.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.debitoren"));
		wsfErhaltAnzahlung.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.erhaltene.anzahlungen"));
		wsfErhaltAnzahlungVerr.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.erhaltene.anzahlungen.verrechnung"));
		wsfGegebenAnzahlungBezahlt.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.geleistete.anzahlungen"));
		wsfGegebenAnzahlungVerr.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.geleistete.anzahlungen.verrechnung"));

		wsfReverseChargeErhaltAnzahlungBezahlt.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.erhaltene.anzahlungen"));
		wsfReverseChargeErhaltAnzahlungVerr.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.erhaltene.anzahlungen.verrechnung"));
		wsfReverseChargeGegebenAnzahlungBezahlt.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.geleistete.anzahlungen"));
		wsfReverseChargeGegebenAnzahlungVerr.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungsparameter.geleistete.anzahlungen.verrechnung"));

		// jetzt meine Felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		WrapperLabel wlaEBKonten = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
			"fb.buchungsparameter.ebkonten"));
		wlaEBKonten.setHorizontalAlignment(SwingConstants.LEFT);

		WrapperLabel wlaAnzahlungen = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
			"fb.buchungsparameter.anzahlungen"));
		WrapperLabel wlaReverseChargeAnzahlungen = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"fb.buchungsparameter.reversechargeanzahlungen"));
		wlaAnzahlungen.setHorizontalAlignment(SwingConstants.LEFT);
		wlaReverseChargeAnzahlungen.setHorizontalAlignment(SwingConstants.LEFT);
		
		iZeile++;
		jpaWorkingOn.add(wlaEBKonten,
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfEBSachkonten.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 120, 0));
		jpaWorkingOn.add(wsfEBSachkonten.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wsfEBDebitoren.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfEBDebitoren.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfEBKreditoren.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfEBKreditoren.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAnzahlungen,
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfErhaltAnzahlungVerr.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfErhaltAnzahlungVerr.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfErhaltAnzahlung.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfErhaltAnzahlung.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfGegebenAnzahlungVerr.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfGegebenAnzahlungVerr.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfGegebenAnzahlungBezahlt.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfGegebenAnzahlungBezahlt.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaReverseChargeAnzahlungen,
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfReverseChargeErhaltAnzahlungVerr.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfReverseChargeErhaltAnzahlungVerr.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfReverseChargeErhaltAnzahlungBezahlt.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfReverseChargeErhaltAnzahlungBezahlt.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfReverseChargeGegebenAnzahlungVerr.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfReverseChargeGegebenAnzahlungVerr.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfReverseChargeGegebenAnzahlungBezahlt.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wsfReverseChargeGegebenAnzahlungBezahlt.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));
	}

	protected void dto2Components() throws Throwable {
		wsfEBDebitoren.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdEbdebitoren());
		wsfEBKreditoren.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdEbkreditoren());
		wsfEBSachkonten.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdEbsachkonten());
		wsfErhaltAnzahlung.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdAnzahlungErhaltBezahlt());
		wsfErhaltAnzahlungVerr.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdAnzahlungErhaltVerr());
		wsfGegebenAnzahlungBezahlt.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdAnzahlungGegebenBezahlt());
		wsfGegebenAnzahlungVerr.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdAnzahlungGegebenVerr());
		
		wsfReverseChargeErhaltAnzahlungBezahlt.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdRCAnzahlungErhaltBezahlt());
		wsfReverseChargeErhaltAnzahlungVerr.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdRCAnzahlungErhaltVerr());
		wsfReverseChargeGegebenAnzahlungBezahlt.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdRCAnzahlungGegebenBezahlt());
		wsfReverseChargeGegebenAnzahlungVerr.setKey(tpFinanzamt.getFinanzamtDto()
				.getKontoIIdRCAnzahlungGegebenVerr());
	}

	protected void components2Dto() throws Throwable {
		FinanzamtDto famt = tpFinanzamt.getFinanzamtDto();
		
		famt.setKontoIIdEbdebitoren(
				wsfEBDebitoren.getIKey());
		famt.setKontoIIdEbkreditoren(
				wsfEBKreditoren.getIKey());
		famt.setKontoIIdEbsachkonten(
				wsfEBSachkonten.getIKey());

		famt.setKontoIIdAnzahlungErhaltBezahlt(wsfErhaltAnzahlung.getIKey());
		famt.setKontoIIdAnzahlungErhaltVerr(wsfErhaltAnzahlungVerr.getIKey());
		famt.setKontoIIdAnzahlungGegebenBezahlt(wsfGegebenAnzahlungBezahlt.getIKey());
		famt.setKontoIIdAnzahlungGegebenVerr(wsfGegebenAnzahlungVerr.getIKey());

		famt.setKontoIIdRCAnzahlungErhaltBezahlt(wsfReverseChargeErhaltAnzahlungBezahlt.getIKey());
		famt.setKontoIIdRCAnzahlungErhaltVerr(wsfReverseChargeErhaltAnzahlungVerr.getIKey());
		famt.setKontoIIdRCAnzahlungGegebenBezahlt(wsfReverseChargeGegebenAnzahlungBezahlt.getIKey());
		famt.setKontoIIdRCAnzahlungGegebenVerr(wsfReverseChargeGegebenAnzahlungVerr.getIKey());
		
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);

		if (!bNeedNoYouAreSelectedI) {

			initPanel();

			dto2Components();

			setStatusbar();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getFinanzDelegate()
					.updateFinanzamt(tpFinanzamt.getFinanzamtDto());

			super.eventActionSave(e, true);

			eventYouAreSelected(false);

			dto2Components();
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MANDANT;
	}

	protected void setStatusbar() throws Throwable {

	}

	protected void eventActionRefresh(ActionEvent aE, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(aE, bNeedNoRefreshI);

	}

}
