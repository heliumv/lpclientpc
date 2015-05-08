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
package com.lp.client.bestellung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
public class DialogTransportkostenWaehrung extends JDialog implements
		ActionListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();

	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private WrapperComboBox wcoWaehrung = new WrapperComboBox();

	private WrapperLabel wlaTransportkosten = new WrapperLabel();
	private WrapperNumberField wnfTransportkosten = new WrapperNumberField();

	private WrapperLabel wlaZollkosten = new WrapperLabel();
	private WrapperNumberField wnfZollkosten = new WrapperNumberField();

	private WrapperLabel wlaBankspesen = new WrapperLabel();
	private WrapperNumberField wnfBankspesen = new WrapperNumberField();

	private WrapperLabel wlaSonstigespesen = new WrapperLabel();
	private WrapperNumberField wnfSonstigespesen = new WrapperNumberField();

	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	private PanelBestellungWareneingang panelWE = null;
	private String vorherigeWaehrung = null;

	public DialogTransportkostenWaehrung(PanelBestellungWareneingang panelWE)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("bes.we.transportkosten.umrechnen"), true);

		this.panelWE = panelWE;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(400, 300);

	}

	private void neuBerechnen() throws Throwable {
		if (!vorherigeWaehrung.equals((String) wcoWaehrung
				.getKeyOfSelectedItem())) {
			if (wdfDatum.getTimestamp() != null) {
				if (wnfBankspesen.getBigDecimal() != null) {
					wnfBankspesen
							.setBigDecimal(DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrungGerundetZuDatum(
											wnfBankspesen.getBigDecimal(),
											vorherigeWaehrung,
											(String) wcoWaehrung
													.getKeyOfSelectedItem(),
											wdfDatum.getDate()));
				}
				if (wnfSonstigespesen.getBigDecimal() != null) {
					wnfSonstigespesen
							.setBigDecimal(DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrungGerundetZuDatum(
											wnfSonstigespesen.getBigDecimal(),
											vorherigeWaehrung,
											(String) wcoWaehrung
													.getKeyOfSelectedItem(),
											wdfDatum.getDate()));
				}
				if (wnfTransportkosten.getBigDecimal() != null) {
					wnfTransportkosten
							.setBigDecimal(DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrungGerundetZuDatum(
											wnfTransportkosten.getBigDecimal(),
											vorherigeWaehrung,
											(String) wcoWaehrung
													.getKeyOfSelectedItem(),
											wdfDatum.getDate()));
				}
				if (wnfZollkosten.getBigDecimal() != null) {
					wnfZollkosten
							.setBigDecimal(DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrungGerundetZuDatum(
											wnfZollkosten.getBigDecimal(),
											vorherigeWaehrung,
											(String) wcoWaehrung
													.getKeyOfSelectedItem(),
											wdfDatum.getDate()));
				}
				vorherigeWaehrung = (String) wcoWaehrung.getKeyOfSelectedItem();
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource().equals(wcoWaehrung)) {

				neuBerechnen();

			} else if (e.getSource().equals(wdfDatum)) {

			} else if (e.getSource().equals(btnAbbrechen)) {

				this.setVisible(false);
			} else {

				if (wnfBankspesen.getBigDecimal() != null) {
					panelWE.wnfBankspesen
							.setBigDecimal(DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrungGerundetZuDatum(
											wnfBankspesen.getBigDecimal(),
											(String) wcoWaehrung
													.getKeyOfSelectedItem(),
											panelWE.getTabbedPaneBestellung()
													.getBesDto()
													.getWaehrungCNr(),
											wdfDatum.getDate()));
				}
				if (wnfSonstigespesen.getBigDecimal() != null) {
					panelWE.wnfSonstigespesen
							.setBigDecimal(DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrungGerundetZuDatum(
											wnfSonstigespesen.getBigDecimal(),
											(String) wcoWaehrung
													.getKeyOfSelectedItem(),
											panelWE.getTabbedPaneBestellung()
													.getBesDto()
													.getWaehrungCNr(),
											wdfDatum.getDate()));
				}
				if (wnfTransportkosten.getBigDecimal() != null) {
					panelWE.wnfTransportkosten
							.setBigDecimal(DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrungGerundetZuDatum(
											wnfTransportkosten.getBigDecimal(),
											(String) wcoWaehrung
													.getKeyOfSelectedItem(),
											panelWE.getTabbedPaneBestellung()
													.getBesDto()
													.getWaehrungCNr(),
											wdfDatum.getDate()));
				}
				if (wnfZollkosten.getBigDecimal() != null) {
					panelWE.wnfZollkosten
							.setBigDecimal(DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrungGerundetZuDatum(
											wnfZollkosten.getBigDecimal(),
											(String) wcoWaehrung
													.getKeyOfSelectedItem(),
											panelWE.getTabbedPaneBestellung()
													.getBesDto()
													.getWaehrungCNr(),
											wdfDatum.getDate()));
				}

				this.setVisible(false);
			}
		} catch (ExceptionLP e1) {
			if (e1.getICode() == EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT) {
				MessageFormat mf = new MessageFormat(LPMain.getInstance()
						.getTextRespectUISPr(
								"lp.error.keinwechselkurshinterlegt"));

				String cWaehrungMandant = null;

				try {
					cWaehrungMandant = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mandantFindByPrimaryKey(
									LPMain.getInstance().getTheClient()
											.getMandant()).getWaehrungCNr();
					mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());
				} catch (Throwable t) {
					LPMain.getInstance().exitFrame(panelWE.getInternalFrame(),
							t);
				}

				Object pattern[] = { cWaehrungMandant,
						wcoWaehrung.getKeyOfSelectedItem() };

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), mf.format(pattern));
			} else {
				panelWE.handleException(e1, true);
			}
		} catch (Throwable e1) {
			panelWE.handleException(e1, true);
		}
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));
		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);
		wcoWaehrung.setMandatoryField(true);
		wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen());
		wcoWaehrung.setSelectedItem(panelWE.getTabbedPaneBestellung()
				.getBesDto().getWaehrungCNr());
		vorherigeWaehrung = panelWE.getTabbedPaneBestellung().getBesDto()
				.getWaehrungCNr();
		wcoWaehrung.addActionListener(this);
		wdfDatum.setMandatoryField(true);
		wdfDatum.setTimestamp(panelWE.wdfWareneingangsDatum.getTimestamp());
		wdfDatum.addPropertyChangeListener(this);

		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wlaWaehrung.setText(LPMain.getTextRespectUISPr("lp.waehrung"));

		wlaZollkosten.setText(LPMain.getTextRespectUISPr("bes.zollkosten"));
		wlaBankspesen.setText(LPMain.getTextRespectUISPr("bes.bankspesen"));
		wlaTransportkosten.setText(LPMain
				.getTextRespectUISPr("bes.transportkosten"));

		wlaSonstigespesen.setText(LPMain
				.getTextRespectUISPr("bes.sonstigespesen"));

		wnfTransportkosten.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfSonstigespesen.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfBankspesen.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfZollkosten.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());

		wnfTransportkosten.setBigDecimal(panelWE.wnfTransportkosten
				.getBigDecimal());
		wnfSonstigespesen.setBigDecimal(panelWE.wnfSonstigespesen
				.getBigDecimal());
		wnfBankspesen.setBigDecimal(panelWE.wnfBankspesen.getBigDecimal());
		wnfZollkosten.setBigDecimal(panelWE.wnfZollkosten.getBigDecimal());

		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;

		this.getContentPane().add(
				wlaDatum,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						100, 0));
		this.getContentPane().add(
				wdfDatum,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;
		this.getContentPane().add(
				wlaWaehrung,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wcoWaehrung,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				wlaTransportkosten,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wnfTransportkosten,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				wlaZollkosten,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wnfZollkosten,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				wlaBankspesen,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wnfBankspesen,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				wlaSonstigespesen,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wnfSonstigespesen,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;
		this.getContentPane().add(
				btnOK,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				btnAbbrechen,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getSource().equals(wdfDatum)) {
			System.out.println(evt.getPropertyName());
		}

	}

}
