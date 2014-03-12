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
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelArtikellieferantstaffelpreise extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArtikellieferantDto artikellieferantDto = null;
	private InternalFrameArtikel internalFrameArtikel = null;
	private ArtikellieferantstaffelDto artikellieferantstaffelDto = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpalWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaLieferant = new WrapperLabel();
	private WrapperTextField wtfLieferant = new WrapperTextField();
	private WrapperLabel wlaEinzelpreis = new WrapperLabel();
	private WrapperNumberField wnfEinzelpreis = new WrapperNumberField();
	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();
	private WrapperLabel wlaRabatt = new WrapperLabel();
	private WrapperLabel wlaNettopreis = new WrapperLabel();
	private WrapperNumberField wnfRabatt = new WrapperNumberField();
	private WrapperNumberField wnfNettopreis = new WrapperNumberField();
	private WrapperLabel wlaGueltigab = new WrapperLabel();
	private WrapperDateField wdfGueltigab = new WrapperDateField();
	private WrapperLabel wlaEinheitMenge = new WrapperLabel();
	private WrapperLabel wlaEinheitEinzelpreis = new WrapperLabel();
	private WrapperLabel wlaProzent = new WrapperLabel();
	private WrapperLabel wlaEinheitNettopreis = new WrapperLabel();

	private WrapperLabel wlaWiederbeschaffungszeit = new WrapperLabel();
	private WrapperNumberField wnfWiederbeschaffungszeit = new WrapperNumberField();
	private WrapperLabel wlaWiederbeschaffungszeitEinheit = new WrapperLabel();

	private WrapperLabel wlaEinheitBestellmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitArtikelmenge = new WrapperLabel();
	private WrapperNumberField wnfNettopreisBestellmenge = new WrapperNumberField();
	private WrapperNumberField wnfEinzelpreisBestellmenge = new WrapperNumberField();

	private WrapperRadioButton wrbRabatt = new WrapperRadioButton();
	private WrapperRadioButton wrbNettopreis = new WrapperRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperLabel wlaArtikellieferantGueltigab = new WrapperLabel();

	public PanelArtikellieferantstaffelpreise(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfMenge;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (artikellieferantDto != null) {

			super.eventActionNew(eventObject, true, false);
			leereAlleFelder(this);
			artikellieferantstaffelDto = new ArtikellieferantstaffelDto();

		} else {
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.error.keinartikellieferantdefiniert"));
		}

	}

	protected void components2Dto() throws Throwable {
		artikellieferantstaffelDto.setArtikellieferantIId(artikellieferantDto
				.getIId());
		artikellieferantstaffelDto.setTPreisgueltigab(wdfGueltigab
				.getTimestamp());
		artikellieferantstaffelDto.setTPreisgueltigbis(null);
		artikellieferantstaffelDto.setNMenge(wnfMenge.getBigDecimal());
		artikellieferantstaffelDto.setFRabatt(wnfRabatt.getDouble());
		artikellieferantstaffelDto
				.setNNettopreis(wnfNettopreis.getBigDecimal());
		artikellieferantstaffelDto
				.setIWiederbeschaffungszeit(wnfWiederbeschaffungszeit
						.getInteger());
		if (wrbRabatt.isSelected() == true) {
			artikellieferantstaffelDto.setBRabattbehalten(Helper
					.boolean2Short(true));
		} else {
			artikellieferantstaffelDto.setBRabattbehalten(Helper
					.boolean2Short(false));
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {

			components2Dto();
			if (artikellieferantstaffelDto.getIId() == null) {
				artikellieferantstaffelDto.setIId(DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.createArtikellieferantstaffel(
								artikellieferantstaffelDto));

				setKeyWhenDetailPanel(artikellieferantstaffelDto.getIId());

			} else {
				DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.updateArtikellieferantstaffel(
								artikellieferantstaffelDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getArtikelDto().getIId()
								.toString());
			}

			eventYouAreSelected(false);
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate()
				.removeArtikellieferantstaffel(artikellieferantstaffelDto);
		artikellieferantstaffelDto = null;
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	private void setTooltipMandantenwaehrung(WrapperNumberField wnf,
			String lieferantenwaehrung) throws Throwable {
		if (wnf.getBigDecimal() != null) {
			BigDecimal umgerechnet = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.rechneUmInAndereWaehrung(
							wnf.getBigDecimal(),
							lieferantenwaehrung,
							LPMain.getInstance().getTheClient()
									.getSMandantenwaehrung());
			wnf.setToolTipText(Helper.formatZahl(umgerechnet, LPMain
					.getInstance().getTheClient().getLocUi())
					+ " "
					+ LPMain.getInstance().getTheClient()
							.getSMandantenwaehrung());
		} else {
			wnf.setToolTipText(null);
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wrbNettopreis)
				|| e.getSource().equals(wrbRabatt)) {
			if (wrbNettopreis.isSelected()) {
				wnfRabatt.setEditable(false);
				wnfNettopreis.setEditable(true);
			} else {
				wnfNettopreis.setEditable(false);
				wnfRabatt.setEditable(true);

			}
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		eventActionSpecial(new ActionEvent(wrbNettopreis, 0, ""));

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (artikellieferantDto != null) {
			leereAlleFelder(this);
			wtfLieferant.setText(artikellieferantDto.getLieferantDto()
					.getPartnerDto().formatAnrede());
			wnfEinzelpreis.setBigDecimal(artikellieferantDto.getNEinzelpreis());

			
			BigDecimal faktor = internalFrameArtikel.getArtikelDto()
					.getNUmrechnungsfaktor();
			
			wnfEinzelpreisBestellmenge.setBigDecimal(null);
			if (faktor != null && wnfEinzelpreis.getBigDecimal()!=null) {
				if (faktor.doubleValue() != 0) {
					if (Helper.short2boolean(internalFrameArtikel
							.getArtikelDto()
							.getbBestellmengeneinheitInvers())) {
						wnfEinzelpreisBestellmenge
						.setBigDecimal(wnfEinzelpreis
								.getBigDecimal().multiply(faktor));
					} else {
						wnfEinzelpreisBestellmenge
						.setBigDecimal(wnfEinzelpreis
								.getBigDecimal().divide(faktor, 2,
										BigDecimal.ROUND_HALF_EVEN));
					}
					
				}
			}
			wlaArtikellieferantGueltigab
					.setText(LPMain.getInstance().getTextRespectUISPr(
							"artikel.staffel.einzelpreisgueltigab")
							+ " "
							+ Helper.formatDatum(
									artikellieferantDto.getTPreisgueltigab(),
									LPMain.getTheClient().getLocUi()));
			setTooltipMandantenwaehrung(wnfEinzelpreis, artikellieferantDto
					.getLieferantDto().getWaehrungCNr());
			if (artikellieferantDto.getNEinzelpreis() == null) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"lp.error.keineinzelpreisimartikellieferant"));
			}
			wlaEinheitMenge.setText(internalFrameArtikel.getArtikelDto()
					.getEinheitCNr());
			wlaEinheitEinzelpreis.setText(artikellieferantDto.getLieferantDto()
					.getWaehrungCNr());
			wlaEinheitNettopreis.setText(artikellieferantDto.getLieferantDto()
					.getWaehrungCNr());

			Object key = getKeyWhenDetailPanel();

			wlaEinheitArtikelmenge.setText(internalFrameArtikel.getArtikelDto()
					.getEinheitCNr().trim());

			if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

				wlaEinheitBestellmenge.setText("");
				if (internalFrameArtikel.getArtikelDto()
						.getEinheitCNrBestellung() != null) {

					wlaEinheitBestellmenge.setText(internalFrameArtikel
							.getArtikelDto().getEinheitCNrBestellung().trim());
					wlaEinheitBestellmenge.setVisible(true);
					wlaEinheitArtikelmenge.setVisible(true);
					wnfEinzelpreisBestellmenge.setVisible(true);
					wnfNettopreisBestellmenge.setVisible(true);

				} else {
					wlaEinheitBestellmenge.setVisible(false);
					wlaEinheitArtikelmenge.setVisible(false);
					wnfEinzelpreisBestellmenge.setVisible(false);
					wnfNettopreisBestellmenge.setVisible(false);
				}

				wdfGueltigab.setTimestamp(new java.sql.Timestamp(System
						.currentTimeMillis()));
				eventActionSpecial(new ActionEvent(wrbNettopreis, 0, ""));
			}

			else {
				artikellieferantstaffelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikellieferantstaffelFindByPrimaryKey((Integer) key);

				if (internalFrameArtikel.getArtikelDto()
						.getEinheitCNrBestellung() != null) {

					wlaEinheitBestellmenge.setText(internalFrameArtikel
							.getArtikelDto().getEinheitCNrBestellung().trim());
					wlaEinheitBestellmenge.setVisible(true);
					wlaEinheitArtikelmenge.setVisible(true);
					wnfEinzelpreisBestellmenge.setVisible(true);
					wnfNettopreisBestellmenge.setVisible(true);

				} else {
					wlaEinheitBestellmenge.setVisible(false);
					wlaEinheitArtikelmenge.setVisible(false);
					wnfEinzelpreisBestellmenge.setVisible(false);
					wnfNettopreisBestellmenge.setVisible(false);
				}

				dto2Components();

				
				if (faktor != null) {
					if (faktor.doubleValue() != 0) {
						if (Helper.short2boolean(internalFrameArtikel
								.getArtikelDto()
								.getbBestellmengeneinheitInvers())) {
							wnfEinzelpreisBestellmenge
									.setBigDecimal(wnfEinzelpreis
											.getBigDecimal().multiply(faktor));
							setTooltipMandantenwaehrung(
									wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());

							wnfNettopreisBestellmenge
									.setBigDecimal(wnfNettopreis
											.getBigDecimal().multiply(faktor));

							setTooltipMandantenwaehrung(
									wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());
						} else {
							wnfEinzelpreisBestellmenge
									.setBigDecimal(wnfEinzelpreis
											.getBigDecimal().divide(faktor, 2,
													BigDecimal.ROUND_HALF_EVEN));
							setTooltipMandantenwaehrung(
									wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());

							wnfNettopreisBestellmenge
									.setBigDecimal(wnfNettopreis
											.getBigDecimal().divide(faktor, 2,
													BigDecimal.ROUND_HALF_EVEN));

							setTooltipMandantenwaehrung(
									wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());
						}
					} else {
						wnfNettopreisBestellmenge.setBigDecimal(new BigDecimal(
								0));
						wnfNettopreisBestellmenge.setToolTipText(null);
						wnfEinzelpreisBestellmenge
								.setBigDecimal(new BigDecimal(0));
						wnfEinzelpreisBestellmenge.setToolTipText(null);
					}
				}

			}
		} else {
			leereAlleFelder(this);

		}
	}

	protected void dto2Components() throws Throwable {
		wdfGueltigab.setTimestamp(artikellieferantstaffelDto
				.getTPreisgueltigab());
		wnfMenge.setBigDecimal(artikellieferantstaffelDto.getNMenge());
		wnfNettopreis
				.setBigDecimal(artikellieferantstaffelDto.getNNettopreis());
		wnfWiederbeschaffungszeit.setInteger(artikellieferantstaffelDto
				.getIWiederbeschaffungszeit());
		setTooltipMandantenwaehrung(wnfNettopreis, artikellieferantDto
				.getLieferantDto().getWaehrungCNr());

		wnfRabatt.setBigDecimal(new BigDecimal(artikellieferantstaffelDto
				.getFRabatt()));

		if (Helper.short2boolean(artikellieferantstaffelDto
				.getBRabattbehalten()) == true) {
			wrbRabatt.setSelected(true);
		} else {
			wrbNettopreis.setSelected(true);
		}

		this.setStatusbarPersonalIIdAendern(artikellieferantstaffelDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(artikellieferantstaffelDto.getTAendern());

	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpalWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpalWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaEinheitArtikelmenge.setHorizontalAlignment(SwingConstants.CENTER);
		wlaEinheitBestellmenge.setHorizontalAlignment(SwingConstants.CENTER);

		wlaLieferant.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.lieferant"));
		wtfLieferant.setActivatable(false);
		wtfLieferant.setMandatoryField(true);
		wtfLieferant.setText("");
		wtfLieferant.setColumnsMax(500);
		wlaEinzelpreis.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.einzelpreis"));
		wnfEinzelpreis.setActivatable(false);
		wnfEinzelpreis.setMandatoryField(true);

		wnfEinzelpreisBestellmenge.setActivatable(false);

		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));
		wlaRabatt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.rabattsumme"));
		wlaNettopreis.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.nettopreis"));
		wlaGueltigab.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gueltigab"));
		wlaWiederbeschaffungszeit.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"artikel.artikellieferant.wiederbeschaffungszeit"));
		wlaWiederbeschaffungszeitEinheit
				.setHorizontalAlignment(SwingConstants.LEFT);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null) {
			if (parameter.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				wlaWiederbeschaffungszeitEinheit.setText(LPMain
						.getTextRespectUISPr("lp.kw"));
			} else if (parameter.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				wlaWiederbeschaffungszeitEinheit.setText(LPMain
						.getTextRespectUISPr("lp.tage"));
			} else {
				wlaWiederbeschaffungszeitEinheit.setText("?");
			}
		}
		wnfWiederbeschaffungszeit.setFractionDigits(0);
		wnfWiederbeschaffungszeit.setMinimumValue(0);
		wnfMenge.setMandatoryField(true);
		wnfRabatt.setMandatoryField(true);
		wnfRabatt.setDependenceField(true);
		wnfRabatt
				.addFocusListener(new PanelArtikellieferantstaffelpreise_wnfRabatt_focusAdapter(
						this));
		wnfNettopreis.setMandatoryField(true);
		wnfNettopreis.setDependenceField(true);
		wnfNettopreis
				.addFocusListener(new PanelArtikellieferantstaffelpreise_wnfNettopreis_focusAdapter(
						this));

		wnfNettopreisBestellmenge
				.addFocusListener(new PanelArtikellieferantstaffelpreise_wnfNettopreisBestelleinheit_focusAdapter(
						this));

		wdfGueltigab.setMandatoryField(true);
		wlaEinheitMenge.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitMenge.setText("");
		wlaEinheitEinzelpreis.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitEinzelpreis.setText("");
		wlaProzent.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent.setText("%");
		wlaEinheitNettopreis.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitNettopreis.setText("");

		buttonGroup.add(wrbRabatt);
		buttonGroup.add(wrbNettopreis);

		wrbRabatt.setSelected(true);

		wrbRabatt.addActionListener(this);
		wrbNettopreis.addActionListener(this);

		// Projekt 2619 Nachkommastellen
		int iNachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseEK();
		wnfEinzelpreis.setFractionDigits(iNachkommastellen);
		wnfNettopreis.setFractionDigits(iNachkommastellen);
		wnfNettopreisBestellmenge.setFractionDigits(iNachkommastellen);
		wnfEinzelpreisBestellmenge.setFractionDigits(iNachkommastellen);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpalWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		int iZeile = 0;
		
		jpalWorkingOn.add(wlaLieferant, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 30, 2), 0, 0));
		jpalWorkingOn.add(wtfLieferant, new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 30, 2), 0, 0));
		iZeile++;
		jpalWorkingOn.add(wlaEinheitArtikelmenge, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wlaEinheitBestellmenge, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpalWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.6, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wlaEinheitMenge, new GridBagConstraints(2, iZeile, 1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpalWorkingOn.add(wlaEinzelpreis, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wnfEinzelpreis, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wlaEinheitEinzelpreis, new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wnfEinzelpreisBestellmenge, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wlaArtikellieferantGueltigab, new GridBagConstraints(4, iZeile, 3, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpalWorkingOn.add(wlaRabatt, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wnfRabatt, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		JPanel prozentPanel = new JPanel(new GridBagLayout());
		prozentPanel.add(wlaProzent, new GridBagConstraints(0, 0, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		prozentPanel.add(wrbRabatt, new GridBagConstraints(1, 0, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(prozentPanel, new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpalWorkingOn.add(wlaNettopreis, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wnfNettopreis, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		JPanel rabattPanel = new JPanel(new GridBagLayout());
		rabattPanel.add(wlaEinheitNettopreis, new GridBagConstraints(0, 0, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		rabattPanel.add(wrbNettopreis, new GridBagConstraints(1, 0, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(rabattPanel, new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wnfNettopreisBestellmenge, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpalWorkingOn.add(wlaGueltigab, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wdfGueltigab, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wlaWiederbeschaffungszeit, new GridBagConstraints(2, iZeile, 3, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wnfWiederbeschaffungszeit, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpalWorkingOn.add(wlaWiederbeschaffungszeitEinheit, new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	void wnfRabatt_focusLost(FocusEvent e) {
		if (wrbRabatt.isSelected()) {
			// z.b: Einzelpreis = 88, Rabatt = 22
			// Nettopreis: 68,64 = 88 - ( ( 88 / 100 ) * 22)
			try {
				if (wnfEinzelpreis.getBigDecimal() != null
						&& wnfRabatt.getBigDecimal() != null) {

					BigDecimal einzelpreis = wnfEinzelpreis.getBigDecimal();
					BigDecimal rabattpreis = einzelpreis.multiply(
							wnfRabatt.getBigDecimal()).divide(
							new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);

					wnfNettopreis.setBigDecimal(einzelpreis
							.subtract(rabattpreis));
					setTooltipMandantenwaehrung(wnfNettopreis,
							artikellieferantDto.getLieferantDto()
									.getWaehrungCNr());

					if (wnfEinzelpreisBestellmenge.getBigDecimal() != null) {
						BigDecimal einzelpreisBestellmenge = wnfEinzelpreisBestellmenge
								.getBigDecimal();
						BigDecimal rabattpreistellmenge = einzelpreisBestellmenge
								.multiply(wnfRabatt.getBigDecimal()).divide(
										new BigDecimal(100), 4,
										BigDecimal.ROUND_HALF_EVEN);
						wnfNettopreisBestellmenge
								.setBigDecimal(einzelpreisBestellmenge
										.subtract(rabattpreistellmenge));

					}

				}
			} catch (Throwable ex) {
				// nothing here
			}
		}
	}

	void wnfNettopreis_focusLost(FocusEvent e) {
		if (wrbNettopreis.isSelected()) {
			try {
				// z.b: Rabatt = 1 - ( Nettopreis / Einzelpreis )
				if (wnfEinzelpreis.getBigDecimal() != null
						&& wnfNettopreis.getBigDecimal() != null
						&& wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {
					BigDecimal rabattsatz = new BigDecimal(1)
							.subtract(wnfNettopreis.getBigDecimal().divide(
									wnfEinzelpreis.getBigDecimal(), 4,
									BigDecimal.ROUND_HALF_EVEN));
					wnfRabatt.setBigDecimal(rabattsatz.multiply(new BigDecimal(
							100)));
				}
			} catch (ExceptionLP ex) {
				// nothing here
			}
		}

	}

	public void wnfNettopreisBestelleinheit_focusLost(FocusEvent e) {
		try {
			BigDecimal faktor = internalFrameArtikel.getArtikelDto()
					.getNUmrechnungsfaktor();
			if (faktor != null
					&& wnfEinzelpreisBestellmenge.getBigDecimal() != null
					&& wnfNettopreisBestellmenge.getBigDecimal() != null
					&& wnfEinzelpreisBestellmenge.getBigDecimal().doubleValue() != 0) {

				BigDecimal rabattsatz = new BigDecimal(1)
						.subtract(wnfNettopreisBestellmenge.getBigDecimal()
								.divide(wnfEinzelpreisBestellmenge
										.getBigDecimal(), 4,
										BigDecimal.ROUND_HALF_EVEN));
				wnfRabatt.setBigDecimal(rabattsatz
						.multiply(new BigDecimal(100)));

				if (wnfEinzelpreis.getBigDecimal() != null
						&& wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {

					wnfNettopreis.setBigDecimal(wnfEinzelpreis.getBigDecimal()
							.subtract(
									wnfEinzelpreis.getBigDecimal().multiply(
											rabattsatz)));

					setTooltipMandantenwaehrung(wnfNettopreis,
							artikellieferantDto.getLieferantDto()
									.getWaehrungCNr());

				}

			}
		} catch (Throwable ex) {
			// nix
		}
	}

}

class PanelArtikellieferantstaffelpreise_wnfRabatt_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelArtikellieferantstaffelpreise adaptee;

	PanelArtikellieferantstaffelpreise_wnfRabatt_focusAdapter(
			PanelArtikellieferantstaffelpreise adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfRabatt_focusLost(e);
	}
}

class PanelArtikellieferantstaffelpreise_wnfNettopreis_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelArtikellieferantstaffelpreise adaptee;

	PanelArtikellieferantstaffelpreise_wnfNettopreis_focusAdapter(
			PanelArtikellieferantstaffelpreise adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfNettopreis_focusLost(e);
	}
}

class PanelArtikellieferantstaffelpreise_wnfNettopreisBestelleinheit_focusAdapter
		extends java.awt.event.FocusAdapter {
	PanelArtikellieferantstaffelpreise adaptee;

	PanelArtikellieferantstaffelpreise_wnfNettopreisBestelleinheit_focusAdapter(
			PanelArtikellieferantstaffelpreise adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfNettopreisBestelleinheit_focusLost(e);
	}
}
