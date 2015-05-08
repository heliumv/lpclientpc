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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JComponent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogSerienChargenauswahl;
import com.lp.client.frame.component.DialogSnrChnrauswahlAlle;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCHNRField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/*
 * <p><I>Dialog zur Nachtraeglichen Entnahme von Material auf ein Los</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>17. 10. 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.4 $
 */
public class PanelDialogSnrChnrAendern extends PanelDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArtikelDto artikelDto = null;

	private WrapperLabel wlaArtikel = new WrapperLabel();
	private WrapperTextField wtfArtikel = new WrapperTextField();
	private Integer artikelIId = null;

	private WrapperButton wbuSnrChnr = new WrapperButton();

	private WrapperButton wbuAendern = new WrapperButton();
	private WrapperSNRField wtfSeriennrVon = new WrapperSNRField();
	private WrapperCHNRField wtfChargennrVon = new WrapperCHNRField();
	private WrapperSNRField wtfSeriennrNach = new WrapperSNRField();
	private WrapperCHNRField wtfChargennrNach = new WrapperCHNRField();

	private WrapperLabel wlaVersion = new WrapperLabel();
	private WrapperTextField wtfVersionVon = new WrapperTextField();
	private WrapperTextField wtfVersionNach = new WrapperTextField();

	static final public String ACTION_SPECIAL_SNRCHNRAUSWAEHLEN = "ACTION_SPECIAL_SNRCHNRAUSWAEHLEN";

	public PanelDialogSnrChnrAendern(InternalFrameArtikel internalFrame,
			String title) throws Throwable {
		super(internalFrame, title);
		this.artikelDto = internalFrame.getArtikelDto();
		init();
		setDefaults();
		initComponents();

		if (internalFrame.getArtikelDto() != null) {
			wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
			wtfArtikel.setText(internalFrame.getArtikelDto()
					.formatArtikelbezeichnung());
			artikelIId = internalFrame.getArtikelDto().getIId();
		}

	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */
	private void init() throws Throwable {

		wlaArtikel
				.setText(LPMain
						.getTextRespectUISPr("artikel.report.artikelbestellt.selektierterartikel")
						+ ": ");

		wlaVersion.setText(LPMain.getTextRespectUISPr("artikel.lager.version")
				+ ": ");

		wtfArtikel.setActivatable(false);

		wbuSnrChnr.addActionListener(this);
		wbuSnrChnr.setActionCommand(ACTION_SPECIAL_SNRCHNRAUSWAEHLEN);

		if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
			wbuSnrChnr.setText(LPMain
					.getTextRespectUISPr("lp.chargennummer_lang"));
		} else if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
			wbuSnrChnr.setText(LPMain.getTextRespectUISPr("auft.seriennummer"));
		} else {
			DialogFactory.showModalDialog("Fehler",
					"Artikel ist nicht Seriennumern-/Chargennumernbehaftet");
			return;
		}

		wbuAendern.setText(LPMain.getTextRespectUISPr("lp.durchfuehren"));
		wbuAendern.addActionListener(this);

		wtfVersionVon.setActivatable(false);

		wtfSeriennrVon
				.addFocusListener(new PanelDialogSnrChnrAendern_wtfSeriennrVon_focusAdapter(
						this));
		wtfChargennrVon.getWtfCHNR()
				.addFocusListener(new PanelDialogSnrChnrAendern_wtfChargennrVon_focusAdapter(
						this));

		getInternalFrame().addItemChangedListener(this);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaArtikel, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, iZeile, 3, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuSnrChnr, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {

			jpaWorkingOn.add(wtfChargennrVon,
					new GridBagConstraints(1, iZeile, 3, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		} else {
			jpaWorkingOn.add(wtfSeriennrVon, new GridBagConstraints(1, iZeile,
					1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200,
					0));
			jpaWorkingOn.add(wlaVersion, new GridBagConstraints(2, iZeile, 1,
					1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100,
					0));
			jpaWorkingOn.add(wtfVersionVon, new GridBagConstraints(3, iZeile,
					1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100,
					0));
		}

		iZeile++;

		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("artikel.pflege.snrchnraendernin")),
						new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));
		if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
			jpaWorkingOn.add(wtfChargennrNach,
					new GridBagConstraints(1, iZeile, 3, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		} else {
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_SERIENNUMMER_NUMERISCH,
							ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());
			if ((Boolean) parameter.getCWertAsObject()) {
				wtfSeriennrNach.setMaskNumerisch();
			}

			jpaWorkingOn.add(wtfSeriennrNach,
					new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wtfVersionNach, new GridBagConstraints(3, iZeile,
					1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100,
					0));
		}

		iZeile++;
		jpaWorkingOn.add(wbuAendern, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getSource().equals(wbuAendern)) {
			if (allMandatoryFieldsSetDlg()) {

				if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {

					int iAnzahlVon = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.lagerbewegungFindByArtikelIIdCSeriennrChargennr(
									artikelDto.getIId(),
									wtfChargennrVon.getChargennummer()).length;

					if (iAnzahlVon < 1) {

						DialogFactory
								.showModalDialog(
										"Fehler",
										"Es konnten keine vorhandenen Chargen mit der Nummer '"
												+ wtfChargennrVon
														.getChargennummer()
												+ "' gefunden werden. Die \u00C4nderung kann nicht durchgef\u00FChrt werden");
						return;

					}

					int iAnzahlBis = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.lagerbewegungFindByArtikelIIdCSeriennrChargennr(
									artikelDto.getIId(),
									wtfChargennrNach.getChargennummer()).length;

					if (iAnzahlBis > 0) {

						boolean b = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										"Es gibt bereits Chargen mit der Nummer '"
												+ wtfChargennrNach
														.getChargennummer()
												+ "' im System. Wollen Sie diese wirklich dahingehend \u00E4ndern?");

						if (b == false) {
							return;
						}
					}

				} else if (Helper.short2boolean(artikelDto
						.getBSeriennrtragend())) {
					int iAnzahlVon = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.lagerbewegungFindByArtikelIIdCSeriennrChargennr(
									artikelDto.getIId(),
									wtfSeriennrVon.getText()).length;

					if (iAnzahlVon < 1) {

						DialogFactory
								.showModalDialog(
										"Fehler",
										"Es konnte keine vorhandene Seriennummer '"
												+ wtfSeriennrVon.getText()
												+ "' gefunden werden. Die \u00C4nderung kann nicht durchgef\u00FChrt werden");
						return;

					}

					if (wtfSeriennrVon.getText().equals(
							wtfSeriennrNach.getText())) {
						// Es wird nur die Version geaendert
					} else {

						int iAnzahlBis = DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.lagerbewegungFindByArtikelIIdCSeriennrChargennr(
										artikelDto.getIId(),
										wtfSeriennrNach.getText()).length;

						if (iAnzahlBis > 0) {

							DialogFactory
									.showModalDialog(
											"Fehler",
											"Es gibt bereits eine Seriennummer '"
													+ wtfSeriennrNach.getText()
													+ "' im System. Die \u00C4nderung kann nicht durchgef\u00FChrt werden.");

							return;
						}
					}
				}

				int iGeaendert = 0;
				if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {

					iGeaendert = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.aendereEinzelneSerienChargennummerEinesArtikel(
									artikelIId,
									wtfChargennrVon.getChargennummer(),
									wtfChargennrNach.getChargennummer(), null,
									null);
				} else if (Helper.short2boolean(artikelDto
						.getBSeriennrtragend())) {
					iGeaendert = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.aendereEinzelneSerienChargennummerEinesArtikel(
									artikelIId, wtfSeriennrVon.getText(),
									wtfSeriennrNach.getText(),
									wtfVersionVon.getText(),
									wtfVersionNach.getText());
				}

				DialogFactory.showModalDialog("Meldung", "Es wurden "
						+ iGeaendert + " Buchungen erfolgreich ge\u00E4ndert.");

				getInternalFrame().closePanelDialog();
			}
		} else if (e.getSource().equals(wbuSnrChnr)) {
			DialogSnrChnrauswahlAlle d = new DialogSnrChnrauswahlAlle(
					artikelIId, null, false, null);
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

			if (d.alSeriennummern != null && d.alSeriennummern.size() > 0) {
				List<SeriennrChargennrMitMengeDto> selektierteSnrs = d.alSeriennummern;
				SeriennrChargennrMitMengeDto dto = selektierteSnrs.get(0);

				if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
					wtfChargennrVon
							.setChagennummer(dto.getCSeriennrChargennr());
				} else {
					wtfSeriennrVon.setText(dto.getCSeriennrChargennr());
					wtfVersionVon.setText(dto.getCVersion());
					
					wtfSeriennrNach.setText(wtfSeriennrVon.getText());
					wtfVersionNach.setText(wtfVersionVon.getText());
					
					
				}

			}

		}
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

		}
	}

	/**
	 * Dialogfenster zur Artikelauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */

	private void setDefaults() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	void wtfSeriennrVon_focusLost(FocusEvent e) {
		wtfVersionVon.setText(null);
		wtfVersionNach.setText(null);
		wtfSeriennrNach.setText(wtfSeriennrVon.getText());

		if (wtfSeriennrVon.getText() != null) {

			try {
				SeriennrChargennrAufLagerDto[] snrDtos = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getAllSerienChargennrAufLagerInfoDtos(artikelIId,
								null, wtfSeriennrVon.getText(), true, null);

				if (snrDtos != null && snrDtos.length > 0) {
					wtfVersionVon.setText(snrDtos[0].getCVersion());
					wtfVersionNach.setText(snrDtos[0].getCVersion());
				}

			} catch (Throwable e1) {
				getInternalFrame().handleException(e1, true);
			}
		}

	}

	void wtfChargennrVon_focusLost(FocusEvent e) {

		wtfChargennrNach.setChagennummer(wtfChargennrVon.getChargennummer());

	}

}

class PanelDialogSnrChnrAendern_wtfSeriennrVon_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelDialogSnrChnrAendern adaptee;

	PanelDialogSnrChnrAendern_wtfSeriennrVon_focusAdapter(
			PanelDialogSnrChnrAendern adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfSeriennrVon_focusLost(e);
	}
}

class PanelDialogSnrChnrAendern_wtfChargennrVon_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelDialogSnrChnrAendern adaptee;

	PanelDialogSnrChnrAendern_wtfChargennrVon_focusAdapter(
			PanelDialogSnrChnrAendern adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfChargennrVon_focusLost(e);
	}
}
