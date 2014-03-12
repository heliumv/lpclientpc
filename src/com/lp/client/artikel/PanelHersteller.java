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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
public class PanelHersteller extends PanelBasis {

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
	private WrapperButton wbuPartner = new WrapperButton();
	private WrapperLabel wlaHersteller = new WrapperLabel();
	private WrapperTextField wtfHersteller = new WrapperTextField();
	private WrapperTextField wtfPartner = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRPartner = null;
	static final public String ACTION_SPECIAL_PARTNER_FROM_LISTE = "action_partner_from_liste";
	private HerstellerDto herstellerDto = null;

	boolean bHerstellerkopplung = false;
	boolean bAutomatik = true;

	public PanelHersteller(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfHersteller;
	}

	protected void components2Dto() throws Throwable {
		herstellerDto.setCNr(wtfHersteller.getText());

	}

	protected void dto2Components() {
		wtfHersteller.setText(herstellerDto.getCNr());
		wtfPartner.setText(herstellerDto.getPartnerDto().formatAnrede());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (bAutomatik == false) {
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_ARTIKELNUMMER_ZEICHENSATZ,
								ParameterFac.KATEGORIE_ARTIKEL,
								LPMain.getTheClient().getMandant());

				String gueltigeZeichen = parameter.getCWert();

				for (int i = 0; i < wtfHersteller.getText().length(); i++) {
					boolean bErlaubt = false;
					char c = wtfHersteller.getText().charAt(i);

					for (int j = 0; j < gueltigeZeichen.length(); j++) {
						if (c == gueltigeZeichen.charAt(j)) {
							bErlaubt = true;
							break;
						}
					}

					if (bErlaubt == false) {
						ArrayList<Object> l = new ArrayList<Object>();
						l.add(new Character(c));
						l.add(wtfHersteller.getText());
						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								LPMain.getInstance().getTextRespectUISPr(
										"artikel.ungueltigezeichenhersteller")
										+ "'" + c + "'");
						return;
					}

				}
			}

			if (herstellerDto.getIId() == null) {
				herstellerDto.setIId(DelegateFactory.getInstance()
						.getArtikelDelegate().createHersteller(herstellerDto));
				setKeyWhenDetailPanel(herstellerDto.getIId());
			} else {
				DelegateFactory.getInstance().getArtikelDelegate()
						.updateHersteller(herstellerDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						herstellerDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			herstellerDto = DelegateFactory.getInstance().getArtikelDelegate()
					.herstellerFindBdPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate()
				.removeHersteller(herstellerDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		herstellerDto = new HerstellerDto();
		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER_FROM_LISTE)) {
			dialogQueryPartnerFromListe(e);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey((Integer) key);
				wtfPartner.setText(partnerDto.formatAnrede());
				herstellerDto.setPartnerIId(partnerDto.getIId());

				if (bHerstellerkopplung) {
					if (herstellerDto.getPartnerIId() != null) {
						wtfHersteller.setText(DelegateFactory.getInstance()
								.getArtikelDelegate()
								.getHerstellercode(partnerDto.getIId()));
					}

				}

			}
		}

	}

	void dialogQueryPartnerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRPartner = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame(),
						herstellerDto.getPartnerIId(), false);
		new DialogQuery(panelQueryFLRPartner);
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaHersteller.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.hersteller"));
		wtfHersteller.setColumnsMax(ArtikelFac.MAX_HERSTELLER_NAME);
		wtfHersteller.setText("");
		wtfHersteller.setMandatoryField(true);

		bHerstellerkopplung = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_HERSTELLERKURZZEICHENAUTOMATIK,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		bAutomatik = (Boolean) parameter.getCWertAsObject();

		if (bHerstellerkopplung) {

			if (bAutomatik == true) {
				wtfHersteller.setActivatable(false);
			} else {
				int iLaenge = 3;
				parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_ARTIKEL_LAENGE_HERSTELLERBEZEICHNUNG,
								ParameterFac.KATEGORIE_ARTIKEL,
								LPMain.getTheClient().getMandant());
				if (parameter.getCWertAsObject() != null) {
					iLaenge = ((Integer) parameter.getCWertAsObject())
							.intValue();
				}

				wtfHersteller.setColumnsMax(iLaenge);
			}

		}

		wbuPartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.partner")
				+ "...");
		wbuPartner
				.setActionCommand(PanelHersteller.ACTION_SPECIAL_PARTNER_FROM_LISTE);
		wbuPartner.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);

		wtfPartner.setText("");
		wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfPartner.setMandatoryField(true);
		wtfPartner.setActivatable(false);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaHersteller, new GridBagConstraints(0, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfHersteller, new GridBagConstraints(1, 0, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuPartner, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPartner, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_HERSTELLER;
	}

}
