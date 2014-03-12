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
package com.lp.client.frame.direktanlegen;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um den Ansprechpartner</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum xx.12.04</p>
 * @author $Author: christian $
 * @version $Revision: 1.11 $
 * Date $Date: 2012/08/29 14:29:30 $
 */
public class PanelAnsprechpartnerFuerPanelQueryFLR extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = null;
	private Border border = null;
	private JPanel panelButtonAction = null;
	private WrapperLabel wlaGueltigAb = null;
	private WrapperDateField wdfGueltigAb = null;
	private WrapperButton wbuAnsprechpartnerfunktion = null;
	private WrapperTextField wtfAnsprechpartnerfunktion = null;
	private WrapperButton wbuAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerfunktion = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerAuswahl = null;
	private GridBagLayout gridBagLayout = null;
	private GridBagLayout gridBagLayoutAll = null;
	private WrapperLabel wlaBemerkung = null;
	private WrapperTextArea wefBemerkung = null;
	private WrapperLabel wlaDurchwahl = null;
	private WrapperTextField wtfDurchwahl = null;
	private WrapperLabel wlaEmail = null;
	private WrapperTextField wtfEmail = null;
	private WrapperLabel wlaFaxdurchwahl = null;
	private WrapperTextField wtfFaxdurchwahl = null;
	private WrapperLabel wlaHandy = null;
	private WrapperTextField wtfHandy = null;
	private WrapperLabel wlaDirektfax = null;
	private WrapperTextField wtfDirektfax = null;
	private WrapperLabel wlaSort = null;
	private JDialog owner = null;
	private Integer partnerIId = null;
	protected WrapperNumberField wtfSort = null;
	private AnsprechpartnerDto ansprechpartnerDto = null;

	protected WrapperComboBox wcoAnrede = null;
	protected WrapperLabel wlaTitel = null;
	protected WrapperTextField wtfTitel = null;
	protected WrapperLabel wlaNtitel = null;
	protected WrapperTextField wtfNtitel = null;
	protected WrapperLabel wlaVorname = null;
	protected WrapperTextField wtfVorname = null;
	protected WrapperLabel wlaGebDatum = null;
	protected WrapperDateField wdfGebDatum = null;
	private WrapperLabel wlaFremdsystem = null;
	private WrapperTextField wtfFremdsystem = null;

	static final public String ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION = "action_special_flr_ansprechpartner_funktion";
	static final public String ACTION_SPECIAL_FLR_ANSPRECHPARTNER = "action_special_flr_ansprechpartner";

	public PanelAnsprechpartnerFuerPanelQueryFLR(InternalFrame internalFrame,
			String add2TitleI, Integer partnerIId, JDialog owner)
			throws Throwable {

		super(internalFrame, add2TitleI, null);

		jbInit();
		initComponents();
		this.owner = owner;
		this.partnerIId = partnerIId;
		ansprechpartnerDto = new AnsprechpartnerDto();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// Buttons.
		String[] aButton = { PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// Das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und einhaengen.
		panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn = new JPanel();
		gridBagLayout = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayout);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Ab hier Ansprechpartnerfelder.
		wlaGueltigAb = new WrapperLabel();
		wlaGueltigAb.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gueltigab"));
		wdfGueltigAb = new WrapperDateField();
		wdfGueltigAb.setMandatoryFieldDB(true);

		wlaTitel = new WrapperLabel();
		wlaVorname = new WrapperLabel();
		wlaGebDatum = new WrapperLabel();
		wlaTitel.setText(LPMain.getInstance().getTextRespectUISPr("lp.titel"));
		wlaNtitel = new WrapperLabel();
		wlaNtitel
				.setText(LPMain.getInstance().getTextRespectUISPr("lp.ntitel"));
		wlaVorname = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.vorname"));
		wlaGebDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalangehoerige.geburtsdatum"));

		wtfVorname = new WrapperTextField();
		wdfGebDatum = new WrapperDateField();
		wcoAnrede = new WrapperComboBox();
		wtfTitel = new WrapperTextField();
		wtfNtitel = new WrapperTextField();

		wtfAnsprechpartnerfunktion = new WrapperTextField();
		wtfAnsprechpartnerfunktion.setActivatable(false);
		wtfAnsprechpartnerfunktion.setMandatoryFieldDB(true);

		wbuAnsprechpartnerfunktion = new WrapperButton();
		wbuAnsprechpartnerfunktion.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.ansprechpartner_funktion"));
		wbuAnsprechpartnerfunktion
				.setActionCommand(ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION);
		wbuAnsprechpartnerfunktion.addActionListener(this);

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ansprechpartner.long"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_FLR_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		// wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartner.setMandatoryFieldDB(true);

		wlaBemerkung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bemerkung"));
		wlaBemerkung.setVerticalAlignment(SwingConstants.NORTH);
		wefBemerkung = new WrapperTextArea();

		wlaDurchwahl = new WrapperLabel();
		wlaDurchwahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.durchwahl"));
		wlaDurchwahl.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wlaDurchwahl.setPreferredSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));

		wtfDurchwahl = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);
		wtfDurchwahl.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wtfDurchwahl.setPreferredSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));

		wlaEmail = new WrapperLabel();
		wlaEmail.setText(LPMain.getInstance().getTextRespectUISPr("lp.email"));
		wlaEmail.setMinimumSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wlaEmail.setPreferredSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wtfEmail = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaFremdsystem = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.ansprechpartner.fremdsystem"));
		wtfFremdsystem = new WrapperTextField(30);

		wlaFaxdurchwahl = new WrapperLabel();
		wlaFaxdurchwahl = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.faxdurchwahl"));
		wtfFaxdurchwahl = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaHandy = new WrapperLabel();
		wlaHandy = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.handy"));
		wtfHandy = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaDirektfax = new WrapperLabel();
		wlaDirektfax = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.direktfax"));
		wtfDirektfax = new WrapperTextField(PartnerFac.MAX_KOMMART_INHALT);

		wlaSort = new WrapperLabel();
		wlaSort = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.sortierung"));
		wtfSort = new WrapperNumberField();
		wtfSort.setMandatoryFieldDB(true);
		wtfSort.setMinimumValue(new Integer(0));
		wtfSort.setMaximumValue(new Integer(9999));

		// Ab hier einhaengen.
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 160, 0));
		jpaWorkingOn.add(wcoAnrede, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(2, iZeile,
				1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 130, 0));
		jpaWorkingOn.add(wlaGebDatum, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(wdfGebDatum, new GridBagConstraints(4, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaVorname, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVorname, new GridBagConstraints(2, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		wlaTitel.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaTitel, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfTitel, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 30, 2, 2), 0, 0));
		wlaNtitel.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaNtitel, new GridBagConstraints(4, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfNtitel, new GridBagConstraints(4, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 60, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuAnsprechpartnerfunktion, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartnerfunktion, new GridBagConstraints(1,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaGueltigAb, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfGueltigAb, new GridBagConstraints(4, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		/*
		 * //Zeile iZeile++; jpaWorkingOn.add(wlaGueltigAb, new
		 * GridBagConstraints(0, iZeile, 1, 1, 0, 0.0 ,
		 * GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2,
		 * 2, 2), 0, 0)); jpaWorkingOn.add(wdfGueltigAb, new
		 * GridBagConstraints(1, iZeile, 1, 1, 0, 0.0 ,
		 * GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2,
		 * 2, 2), 0, 0));
		 */
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(wefBemerkung);

		jpaWorkingOn.add(scrollPane, new GridBagConstraints(1, iZeile, 4, 2,
				0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile += 2;
		jpaWorkingOn.add(wlaDurchwahl, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfDurchwahl, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEmail, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfEmail, new GridBagConstraints(3, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaFaxdurchwahl, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFaxdurchwahl, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaHandy, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfHandy, new GridBagConstraints(3, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaDirektfax, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfDirektfax, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFremdsystem, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFremdsystem, new GridBagConstraints(3, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaSort, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSort, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(new WrapperLabel(""), new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	final public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);

		// Die normale Telefon/Faxnummer vor der Durchwajl anzeigen

		wlaDurchwahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.durchwahl"));

		wlaFaxdurchwahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.faxdurchwahl"));

		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();

			if (key == null
					|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
				leereAlleFelder(this);
				setDefaults();
				clearStatusbar();
			} else {

				setStatusbar();

				dto2Components();
			}

		}
	}

	final protected void setStatusbar() throws Throwable {
	}

	protected void setDefaults() throws Throwable {

		// getPartnerDto().setPartnerartCNr(PartnerFac.PARTNERART_ANSPRECHPARTNER);
		wdfGueltigAb.setDate(new java.sql.Date(System.currentTimeMillis()));
		// eine neue Position bekommt ein eindeutiges iSort
		Integer iSort = DelegateFactory.getInstance()
				.getAnsprechpartnerDelegate().getMaxISort(partnerIId);
		iSort = new Integer(iSort.intValue() + 1);
		wtfSort.setInteger(iSort);
		Map<?, ?> tmAnreden = (SortedMap<?, ?>) DelegateFactory.getInstance()
				.getPartnerDelegate()
				.getAllAnreden(LPMain.getInstance().getTheClient().getLocUi());
		wcoAnrede.setMap(tmAnreden);
		wcoAnrede.setKeyOfSelectedItem(PartnerFac.PARTNER_ANREDE_HERR);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ANSPRECHPARTNER)) {

			String[] aWhichButtonIUse = null;

			QueryType[] querytypes = null;
			panelQueryFLRAnsprechpartnerAuswahl = new PanelQueryFLR(querytypes,
					null, QueryParameters.UC_ID_PARTNER, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("button.ansprechpartner.long"));

			panelQueryFLRAnsprechpartnerAuswahl
					.befuellePanelFilterkriterienDirekt(PartnerFilterFactory
							.getInstance().createFKDPartnerName(),
							PartnerFilterFactory.getInstance()
									.createFKDPartnerLandPLZOrt());

			new DialogQuery(panelQueryFLRAnsprechpartnerAuswahl);
		}

		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION)) {
			String[] aWhichButtonIUse = null;
			QueryType[] querytypes = null;
			FilterKriterium[] filters = null;
			panelQueryFLRAnsprechpartnerfunktion = new PanelQueryFLR(
					querytypes, filters,
					QueryParameters.UC_ID_ANSPRECHPARTNERFUNKTION,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("part.ansprechpartner_funktion"));
			new DialogQuery(panelQueryFLRAnsprechpartnerfunktion);
		}
	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRAnsprechpartnerfunktion) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				ansprechpartnerDto.setAnsprechpartnerfunktionIId(iId);

				AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = null;
				if (iId != null) {
					ansprechpartnerfunktionDto = DelegateFactory.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerfunktionFindByPrimaryKey(iId);
					wtfAnsprechpartnerfunktion
							.setText(ansprechpartnerfunktionDto != null ? ansprechpartnerfunktionDto
									.getCNr() : null);
				}
			} else if (e.getSource() == panelQueryFLRAnsprechpartnerAuswahl) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					ansprechpartnerDto.setPartnerIIdAnsprechpartner(key);
					PartnerDto partnerDto = null;
					partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate().partnerFindByPrimaryKey(key);
					wtfAnsprechpartner.setText(partnerDto
							.getCName1nachnamefirmazeile1());
					wtfTitel.setText(partnerDto.getCTitel());
					wtfNtitel.setText(partnerDto.getCNtitel());
					wtfVorname
							.setText(partnerDto.getCName2vornamefirmazeile2());
					wdfGebDatum.setDate(partnerDto
							.getDGeburtsdatumansprechpartner());
					wcoAnrede.setKeyOfSelectedItem(partnerDto.getAnredeCNr());
				}
			}
		}
	}

	protected void dto2Components() throws Throwable {

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			// Wenn Partner manuell eingegeben wurde, dann vorher anlegen
			if (ansprechpartnerDto.getPartnerIIdAnsprechpartner() == null) {
				PartnerDto partnerDto = new PartnerDto();
				partnerDto.setCName1nachnamefirmazeile1(wtfAnsprechpartner
						.getText());
				partnerDto.setCName2vornamefirmazeile2(wtfVorname.getText());
				partnerDto.setCTitel(wtfTitel.getText());
				partnerDto.setCNtitel(wtfNtitel.getText());
				partnerDto.setAnredeCNr((String) wcoAnrede
						.getKeyOfSelectedItem());
				partnerDto.setDGeburtsdatumansprechpartner(wdfGebDatum
						.getDate());
				partnerDto
						.setPartnerartCNr(PartnerFac.PARTNERART_ANSPRECHPARTNER);
				partnerDto.setBVersteckt(com.lp.util.Helper
						.boolean2Short(false));
				partnerDto.setLocaleCNrKommunikation(LPMain.getInstance()
						.getTheClient().getLocUiAsString());
				String kbez = wtfAnsprechpartner.getText();
				if (kbez.length() > 14) {
					kbez = kbez.substring(0, 13);
				}
				partnerDto.setCKbez(kbez);
				ansprechpartnerDto.setPartnerIIdAnsprechpartner(DelegateFactory
						.getInstance().getPartnerDelegate()
						.createPartner(partnerDto));
			}

			// create.
			Integer iIdAnsprechpartner = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.createAnsprechpartner(ansprechpartnerDto);
			// Dto neu laden
			ansprechpartnerDto = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);
			// diesem panel den key setzen.
			setKeyWhenDetailPanel(iIdAnsprechpartner);
			owner.setVisible(false);
		}
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		setKeyWhenDetailPanel(null);
		owner.setVisible(false);

	}

	protected void components2Dto() throws Throwable {

		ansprechpartnerDto.setPartnerIId(partnerIId);
		ansprechpartnerDto.setDGueltigab(wdfGueltigAb.getDate());
		ansprechpartnerDto.setXBemerkung(wefBemerkung.getText());
		ansprechpartnerDto.setISort(wtfSort.getInteger());
		ansprechpartnerDto.setCFremdsystemnr(wtfFremdsystem.getText());
		// Partnerkommunikation.
		// Kommunikationsdaten

		ansprechpartnerDto.setCDirektfax(wtfDirektfax.getText());

		ansprechpartnerDto.setCTelefon(wtfDurchwahl.getText());

		ansprechpartnerDto.setCEmail(wtfEmail.getText());

		ansprechpartnerDto.setCFax(wtfFaxdurchwahl.getText());

		ansprechpartnerDto.setCHandy(wtfHandy.getText());

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuAnsprechpartner;
	}

}
