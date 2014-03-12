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
import java.math.BigDecimal;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogSnrauswahl;
import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelFehlmengenAufloesen extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;

	private BigDecimal bdVerfuegbareMenge = null;
	private String[] cSelectedSnrs = null;
	private String[] cZugebuchteSnrsChnr = null;
	private LagerDto lagerDto = null;

	private final static String ACTION_SPECIAL_MEHRERE_ZUBUCHEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_mehrere_zubuchen";

	static final public String ACTION_SPECIAL_SNRAUSWAHL = "action_snrauswahl";

	private GridBagLayout gridBagLayoutWorkingPanel = null;

	WrapperLabel wlaBeleg = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("lp.beleg"));
	WrapperTextField wtfBeleg = new WrapperTextField();

	WrapperLabel wlaArtikel = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("lp.artikel"));
	WrapperTextField wtfArtikel = new WrapperTextField();

	WrapperLabel wlaLager = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("label.lager"));
	WrapperTextField wtfLager = new WrapperTextField();

	WrapperLabel wlaZubuchungsmenge = new WrapperLabel("Menge");

	WrapperNumberField wnfZubuchungsmenge = new WrapperNumberField();

	WrapperLabel wlaVerfuegbaremenge = new WrapperLabel();

	WrapperTextField wtfSnrChargennr = new WrapperTextField();

	WrapperButton wbuSeriennr = new WrapperButton();

	ArtikelDto artikelDto = null;

	PanelQuery panelQuery = null;

	com.lp.server.fertigung.service.LossollmaterialDto lossollmaterialDto = null;
	com.lp.server.fertigung.service.LosDto losDto = null;
	JDialog dialogParent = null;

	public PanelFehlmengenAufloesen(InternalFrame internalFrame,
			ArtikelDto artikelDto, String add2TitleI,
			BigDecimal bdVerfuegbareMenge, Integer lagerIId,
			String[] cZugebuchteSnrsChnr, PanelQuery panelQuery,
			JDialog dialogParent) throws Throwable {
		super(internalFrame, add2TitleI);
		this.bdVerfuegbareMenge = bdVerfuegbareMenge;
		this.cZugebuchteSnrsChnr = cZugebuchteSnrsChnr;
		this.lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(lagerIId);
		this.artikelDto = artikelDto;
		this.panelQuery = panelQuery;
		this.dialogParent = dialogParent;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfZubuchungsmenge;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKELFEHLMENGE;
	}

	protected void dto2Components() throws ExceptionLP {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() instanceof PanelQuery) {
				Integer key = (Integer) ((PanelQuery) e.getSource())
						.getSelectedId();
				if (key != null) {
					ArtikelfehlmengeDto dto = DelegateFactory.getInstance()
							.getFehlmengeDelegate()
							.artikelfehlmengeFindByPrimaryKey(key);

					lossollmaterialDto = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.lossollmaterialFindByPrimaryKey(
									dto.getIBelegartpositionid());

					losDto = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.losFindByPrimaryKey(lossollmaterialDto.getLosIId());

					wtfBeleg.setText(dto.getCBelegartnr().trim() + ": "
							+ losDto.getCNr());

					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(dto.getArtikelIId());

					wtfArtikel.setText(artikelDto
							.formatArtikelbezeichnungMitZusatzbezeichnung());

					wlaVerfuegbaremenge.setText("Verf\u00FCgbare Menge: "
							+ Helper.formatZahl(bdVerfuegbareMenge, 3, LPMain
									.getInstance().getUISprLocale()) + " "
							+ artikelDto.getEinheitCNr().trim());

					enableAllComponents(this, false);
					setzeButton(PanelBasis.ACTION_SAVE, false, false);
					setzeButton(PanelBasis.ACTION_DISCARD, false, false);
					setzeButton(PanelBasis.ACTION_REFRESH, true, false);
					setzeButton(PanelBasis.ACTION_UPDATE, true, false);
					setzeButton(PanelBasis.ACTION_REFRESH, true, true);
					// setzeButton(PanelBasis.ACTION_PRINT, true, true);

				} else {
					setzeButton(PanelBasis.ACTION_SAVE, false, false);
					setzeButton(PanelBasis.ACTION_DISCARD, false, false);
					setzeButton(PanelBasis.ACTION_REFRESH, false, false);
					setzeButton(PanelBasis.ACTION_UPDATE, false, false);
					// setzeButton(PanelBasis.ACTION_PRINT, true, true);

				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_ESCAPE) {
			getInternalFrame().removeItemChangedListener(this);
			dialogParent.dispose();
		}
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wtfArtikel.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfBeleg.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wnfZubuchungsmenge.setMandatoryField(true);
		wnfZubuchungsmenge.setFractionDigits(3);
		wtfArtikel.setActivatable(false);
		wtfBeleg.setActivatable(false);
		wnfZubuchungsmenge.setMinimumValue(0);
		wbuSeriennr.addActionListener(this);
		wbuSeriennr.setActionCommand(ACTION_SPECIAL_SNRAUSWAHL);
		wbuSeriennr.setText("Seriennummern...");
		wlaVerfuegbaremenge.setHorizontalAlignment(SwingConstants.LEFT);
		wtfSnrChargennr.setActivatable(false);
		wtfSnrChargennr.setActivatable(false);
		wtfLager.setActivatable(false);
		wtfSnrChargennr.setMandatoryField(true);

		wlaZubuchungsmenge.setLabelFor(wnfZubuchungsmenge);
		wlaZubuchungsmenge.setDisplayedMnemonic('M');

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaArtikel, new GridBagConstraints(0, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfArtikel, new GridBagConstraints(1, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLager, new GridBagConstraints(0, 1, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLager, new GridBagConstraints(1, 1, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaBeleg, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBeleg, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaZubuchungsmenge, new GridBagConstraints(0, 3, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfZubuchungsmenge, new GridBagConstraints(1, 3, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend()) == true
				|| Helper.short2Boolean(artikelDto.getBChargennrtragend()) == true) {

			if (Helper.short2Boolean(artikelDto.getBSeriennrtragend()) == true) {
				jPanelWorkingOn.add(wbuSeriennr, new GridBagConstraints(0, 4,
						1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

			} else {
				wtfSnrChargennr.setText(cZugebuchteSnrsChnr[0]);
				WrapperLabel wlaChnr = new WrapperLabel("Chargennummer");
				jPanelWorkingOn.add(wlaChnr, new GridBagConstraints(0, 4, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
			}

			jPanelWorkingOn.add(wtfSnrChargennr,
					new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

		}
		jPanelWorkingOn.add(wlaVerfuegbaremenge, new GridBagConstraints(0, 5,
				2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD,
		// ACTION_PRINT,
		};

		this.createAndSaveAndShowButton("/com/lp/client/res/data_next.png",
				"Markierte Zeilen aufl\u00F6sen STRG+K",
				ACTION_SPECIAL_MEHRERE_ZUBUCHEN, KeyStroke.getKeyStroke('K',
						java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_FERT_LOS_CUD);

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

	}

	private void verminderSnrs(String[] verbuchteSnrs) {

		if (verbuchteSnrs != null && cZugebuchteSnrsChnr != null) {
			String[] neu = new String[cZugebuchteSnrsChnr.length
					- verbuchteSnrs.length];
			int iNeu = 0;
			for (int i = 0; i < cZugebuchteSnrsChnr.length; i++) {
				boolean bIstDrin = false;
				for (int j = 0; j < verbuchteSnrs.length; j++) {
					if (cZugebuchteSnrsChnr[i].equals(verbuchteSnrs[j])) {
						bIstDrin = true;
					}
				}

				if (bIstDrin == false) {
					neu[iNeu] = cZugebuchteSnrsChnr[i];
					iNeu++;
				}

			}
			cZugebuchteSnrsChnr = neu;
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_SNRAUSWAHL)) {
			DialogSnrauswahl d = new DialogSnrauswahl(cZugebuchteSnrsChnr);

			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

			cSelectedSnrs = d.sSeriennrArray;
			d.dispose();

			if (cSelectedSnrs != null) {
				String s = "";

				for (int i = 0; i < cSelectedSnrs.length; i++) {
					if (i != 0) {
						s += "," + cSelectedSnrs[i];
					} else {
						s = cSelectedSnrs[i];
					}
				}
				wtfSnrChargennr.setText(s);
				wnfZubuchungsmenge.setBigDecimal(new BigDecimal(
						cSelectedSnrs.length));
			} else {
				wtfSnrChargennr.setText("");
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MEHRERE_ZUBUCHEN)) {
			mehrereZeilenZubuchen();
		}
	}

	protected void setDefaults() throws Throwable {

		wtfLager.setText(lagerDto.getCNr());
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		super.eventActionDelete(e, false, false);
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		int i = 0;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
	}

	protected void components2Dto() throws ExceptionLP {

	}

	private void setzeButton(String button, boolean bEnabled,
			boolean bPanelQuery) {
		Collection<?> buttons = getHmOfButtons().values();
		if (bPanelQuery == true) {
			buttons = panelQuery.getHmOfButtons().values();
		}
		for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
			LPButtonAction item = (LPButtonAction) iter.next();
			if (item.getButton().getActionCommand().equals(button)) {
				item.getButton().setEnabled(bEnabled);
			}

		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

	}

	protected void eventActionEscape(ActionEvent e) throws Throwable {
		int u = 0;
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		eventYouAreSelected(false);
		enableAllComponents(this, false);

		setzeButton(PanelBasis.ACTION_SAVE, false, false);
		setzeButton(PanelBasis.ACTION_DISCARD, false, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, false);
		setzeButton(PanelBasis.ACTION_UPDATE, true, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, true);
		// setzeButton(PanelBasis.ACTION_PRINT, true, true);

	}

	protected void eventActionUpdate(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// super.eventActionUpdate(e, false);

		if (lossollmaterialDto != null) {
			BigDecimal ausgegeben = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.getAusgegebeneMenge(lossollmaterialDto.getIId());
			BigDecimal benoetigt = lossollmaterialDto.getNMenge().subtract(
					ausgegeben);

			if (benoetigt.doubleValue() < 0) {
				wnfZubuchungsmenge.setInteger(0);
			} else {
				if (bdVerfuegbareMenge.doubleValue() > benoetigt.doubleValue()) {
					wnfZubuchungsmenge.setBigDecimal(benoetigt);
				} else {
					wnfZubuchungsmenge.setBigDecimal(bdVerfuegbareMenge);
				}
			}

		}
		if (panelQuery.getSelectedId() != null) {
			enableAllComponents(this, true);
			setzeButton(PanelBasis.ACTION_SAVE, true, false);
			setzeButton(PanelBasis.ACTION_DISCARD, true, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, false);
			setzeButton(PanelBasis.ACTION_UPDATE, false, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, true);
			// setzeButton(PanelBasis.ACTION_PRINT, false, true);
		} else {
			enableAllComponents(this, false);
			setzeButton(PanelBasis.ACTION_SAVE, false, false);
			setzeButton(PanelBasis.ACTION_DISCARD, false, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, false);
			setzeButton(PanelBasis.ACTION_UPDATE, false, false);
			// setzeButton(PanelBasis.ACTION_PRINT, true, true);

		}
		setFirstFocusableComponent();
	}

	private void mehrereZeilenZubuchen() throws Throwable {

		Object[] ids = panelQuery.getSelectedIds();

		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (bdVerfuegbareMenge.doubleValue() > 0) {

					ArtikelfehlmengeDto dto = DelegateFactory.getInstance()
							.getFehlmengeDelegate()
							.artikelfehlmengeFindByPrimaryKey((Integer) ids[i]);
					LossollmaterialDto lossollmaterialDtoTemp = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.lossollmaterialFindByPrimaryKey(
									dto.getIBelegartpositionid());

					losDto = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.losFindByPrimaryKey(
									lossollmaterialDtoTemp.getLosIId());

					LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
					losistmaterialDto
							.setLossollmaterialIId(lossollmaterialDtoTemp
									.getIId());
					losistmaterialDto.setLagerIId(lagerDto.getIId());

					losistmaterialDto.setBAbgang(Helper.boolean2Short(true));

					BigDecimal menge = dto.getNMenge();
					if (bdVerfuegbareMenge.doubleValue() < dto.getNMenge()
							.doubleValue()) {
						menge = bdVerfuegbareMenge;
					}

					losistmaterialDto.setNMenge(menge);

					if (Helper.short2Boolean(artikelDto.getBChargennrtragend()) == false
							&& Helper.short2Boolean(artikelDto
									.getBSeriennrtragend()) == false) {

						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.gebeMaterialNachtraeglichAus(
										lossollmaterialDtoTemp,
										losistmaterialDto, null, true);
						bdVerfuegbareMenge = bdVerfuegbareMenge.subtract(menge);

						FehlmengenAufloesen.addAufgeloesteFehlmenge(artikelDto,
								lagerDto, null, losDto, menge);

					}
				}
			}
			panelQuery.eventYouAreSelected(false);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if (wnfZubuchungsmenge.getBigDecimal().doubleValue() > bdVerfuegbareMenge
					.doubleValue()) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.artikel"), "Menge zu gro\u00DF.");
			} else {

				components2Dto();
				if (lossollmaterialDto != null) {

					if (losDto.getStuecklisteIId() != null) {
						StuecklisteDto stklDto = DelegateFactory
								.getInstance()
								.getStuecklisteDelegate()
								.stuecklisteFindByPrimaryKey(
										losDto.getStuecklisteIId());

						if (Helper.short2boolean(stklDto
								.getBMaterialbuchungbeiablieferung()) == true) {
							boolean b = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getInstance()
													.getTextRespectUISPr(
															"artikel.fehlmengen.aufloesen.warning"));

							if (b == false) {
								return;
							}
						}

					}

					LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
					losistmaterialDto.setLossollmaterialIId(lossollmaterialDto
							.getIId());
					losistmaterialDto.setLagerIId(lagerDto.getIId());
					losistmaterialDto.setNMenge(wnfZubuchungsmenge
							.getBigDecimal());
					losistmaterialDto.setBAbgang(Helper.boolean2Short(true));

					if (Helper.short2Boolean(artikelDto.getBChargennrtragend()) == false
							&& Helper.short2Boolean(artikelDto
									.getBSeriennrtragend()) == false) {

						DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.gebeMaterialNachtraeglichAus(
										lossollmaterialDto, losistmaterialDto,
										null, true);
						bdVerfuegbareMenge = bdVerfuegbareMenge
								.subtract(wnfZubuchungsmenge.getBigDecimal());

						FehlmengenAufloesen.addAufgeloesteFehlmenge(artikelDto,
								lagerDto, null, losDto,
								wnfZubuchungsmenge.getBigDecimal());

					} else {
						if (Helper.short2Boolean(artikelDto
								.getBChargennrtragend()) == true) {

							DelegateFactory
									.getInstance()
									.getFertigungDelegate()
									.gebeMaterialNachtraeglichAus(
											lossollmaterialDto,
											losistmaterialDto,
											SeriennrChargennrMitMengeDto
													.erstelleDtoAusEinerChargennummer(
															cZugebuchteSnrsChnr[0],
															wnfZubuchungsmenge
																	.getBigDecimal()),
											true);
							bdVerfuegbareMenge = bdVerfuegbareMenge
									.subtract(wnfZubuchungsmenge
											.getBigDecimal());

							FehlmengenAufloesen.addAufgeloesteFehlmenge(
									artikelDto, lagerDto, cZugebuchteSnrsChnr,
									losDto, wnfZubuchungsmenge.getBigDecimal());

						} else {
							try {
								Helper.erzeugeSeriennummernArray(
										wtfSnrChargennr.getText(),
										wnfZubuchungsmenge.getBigDecimal(),
										true);

								losistmaterialDto.setNMenge(new BigDecimal(1));
								for (int i = 0; i < cSelectedSnrs.length; i++) {

									DelegateFactory
											.getInstance()
											.getFertigungDelegate()
											.gebeMaterialNachtraeglichAus(
													lossollmaterialDto,
													losistmaterialDto,
													SeriennrChargennrMitMengeDto
															.erstelleDtoAusEinerSeriennummer(cSelectedSnrs[i]),
													true);

								}
								bdVerfuegbareMenge = bdVerfuegbareMenge
										.subtract(wnfZubuchungsmenge
												.getBigDecimal());
								FehlmengenAufloesen.addAufgeloesteFehlmenge(
										artikelDto, lagerDto, cSelectedSnrs,
										losDto,
										wnfZubuchungsmenge.getBigDecimal());

							} catch (Throwable ex) {
								if (ex instanceof EJBExceptionLP) {
									throw new ExceptionLP(
											((EJBExceptionLP) ex).getCode(),
											ex.getCause());
								} else {
									handleException(ex, true);
								}
								return;
							}
						}
						verminderSnrs(cSelectedSnrs);

					}
				}
				panelQuery.eventYouAreSelected(false);
				super.eventActionSave(e, true);
				eventYouAreSelected(false);
			}
		}

		enableAllComponents(this, false);
		setzeButton(PanelBasis.ACTION_SAVE, false, false);
		setzeButton(PanelBasis.ACTION_DISCARD, false, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, false);
		setzeButton(PanelBasis.ACTION_UPDATE, true, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, true);
		setzeButton(PanelBasis.ACTION_PRINT, true, true);

		wnfZubuchungsmenge.setText("");
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = panelQuery.getSelectedId();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
			wlaVerfuegbaremenge.setText("Verf\u00FCgbare Menge: "
					+ Helper.formatZahl(bdVerfuegbareMenge, 3, LPMain
							.getInstance().getUISprLocale()) + " "
					+ artikelDto.getEinheitCNr().trim());
		} else {
			ArtikelfehlmengeDto dto = DelegateFactory.getInstance()
					.getFehlmengeDelegate()
					.artikelfehlmengeFindByPrimaryKey((Integer) key);
			lossollmaterialDto = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(
							dto.getIBelegartpositionid());

			losDto = DelegateFactory.getInstance().getFertigungDelegate()
					.losFindByPrimaryKey(lossollmaterialDto.getLosIId());

			wtfBeleg.setText(dto.getCBelegartnr().trim() + ": "
					+ losDto.getCNr());

			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(dto.getArtikelIId());

			wtfArtikel.setText(artikelDto
					.formatArtikelbezeichnungMitZusatzbezeichnung());

			wlaVerfuegbaremenge.setText("Verf\u00FCgbare Menge: "
					+ Helper.formatZahl(bdVerfuegbareMenge, 3, LPMain
							.getInstance().getUISprLocale()) + " "
					+ artikelDto.getEinheitCNr().trim());
		}
	}

}
