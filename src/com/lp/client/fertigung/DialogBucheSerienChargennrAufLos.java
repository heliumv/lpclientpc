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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogBucheSerienChargennrAufLos extends JDialog implements
		ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelSnrChnr = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	WrapperLabel wlaSumme = new WrapperLabel();
	public boolean bEsIstNichtsAufLager = true;
	JButton jButtonUebernehmen = new JButton();

	private Integer losIId = null;
	private ArtikelDto artikelDto = null;
	private BigDecimal gesamtmenge = null;

	private static int LAGER = 0;
	private static int SERIENCHARGENNR = 1;
	private static int LAGERSTAND = 2;
	private static int COMPONENTE = 3;
	private static int BUTTON = 4;

	public boolean bAbbruch = false;
	private ArrayList alBereitsGebucht = new ArrayList();

	public static SeriennrChargennrAufLagerDto[] bereinigeBereitsVerbrauchteSnrChnr(
			ArrayList al, SeriennrChargennrAufLagerDto[] snrchnrauflagerDtos,
			Integer artikelIId, Integer lagerIId) {

		ArrayList<SeriennrChargennrAufLagerDto> alVorhandene = new ArrayList<SeriennrChargennrAufLagerDto>();

		for (int i = 0; i < snrchnrauflagerDtos.length; i++) {
			alVorhandene.add(snrchnrauflagerDtos[i]);
		}

		for (int i = 0; i < al.size(); i++) {
			BucheSerienChnrAufLosDto bucheSerienChnrAufLosDto = (BucheSerienChnrAufLosDto) al
					.get(i);
			for (int j = 0; j < alVorhandene.size(); j++) {
				SeriennrChargennrAufLagerDto dto = (SeriennrChargennrAufLagerDto) alVorhandene
						.get(j);
				if (bucheSerienChnrAufLosDto.getArtikelIId().equals(artikelIId)) {
					if (bucheSerienChnrAufLosDto.getLagerIId().equals(
							lagerIId)) {
						if (dto.getCSeriennrChargennr().equals(
								bucheSerienChnrAufLosDto
										.getCSeriennrChargennr())) {
							// dto entfernen, bzw. verringern
							if (bucheSerienChnrAufLosDto.getNMenge()
									.doubleValue() >= dto.getNMenge()
									.doubleValue()) {
								alVorhandene.remove(j);
							} else {
								dto.setNMenge(dto.getNMenge().subtract(
										bucheSerienChnrAufLosDto.getNMenge()));
								alVorhandene.set(j, dto);
							}

						}
					}
				}
			}
		}
		SeriennrChargennrAufLagerDto[] temp = new SeriennrChargennrAufLagerDto[alVorhandene
				.size()];
		return (SeriennrChargennrAufLagerDto[]) alVorhandene.toArray(temp);
	}

	private ArrayList<Object[]> komponenten = new ArrayList<Object[]>();

	GridBagLayout gridBagLayout2 = new GridBagLayout();

	public DialogBucheSerienChargennrAufLos(Integer losIId,
			ArtikelDto artikelDto, BigDecimal gesamtmenge,
			ArrayList alBereitsGebucht) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr(
						"artikel.handlagerbewegung.seriennrchargennr")
				+ " " + artikelDto.formatArtikelbezeichnung(), true);
		this.losIId = losIId;
		this.alBereitsGebucht = alBereitsGebucht;
		this.artikelDto = artikelDto;
		this.gesamtmenge = gesamtmenge;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {

				bAbbruch = true;
			}
		});

		jbInit();
		pack();

	}

	final public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jButtonUebernehmen)) {

			try {
				if (getGewaehlteMenge().doubleValue() != gesamtmenge
						.doubleValue()) {

					boolean b = DialogFactory.showModalJaNeinDialog(null,
							LPMain.getInstance().getTextRespectUISPr(
									"fert.error.zuwenigsnrchnrausgewaehlt"),
							LPMain.getInstance()
									.getTextRespectUISPr("lp.frage"));
					if (b == true) {
						this.setVisible(false);
					}
				} else {
					this.setVisible(false);

				}

			} catch (Throwable ex) {
				LPMain.getInstance().getDesktop().exitClientNowErrorDlg(ex);
			}

		} else if (e.getSource() instanceof JButton) {

			for (int i = 0; i < komponenten.size(); i++) {
				Object[] oTemp = (Object[]) komponenten.get(i);
				JButton bu = (JButton) oTemp[BUTTON];

				if (e.getSource().equals(bu)) {
					WrapperNumberField wnf = (WrapperNumberField) oTemp[COMPONENTE];
					BigDecimal aufLager = (BigDecimal) oTemp[LAGERSTAND];
					try {
						BigDecimal diff = gesamtmenge
								.subtract(getGewaehlteMenge());
						if (diff.doubleValue() >= aufLager.doubleValue()) {
							wnf.setBigDecimal(aufLager);
						} else {
							wnf.setBigDecimal(diff);

						}
						getGewaehlteMenge();
					} catch (Throwable ex) {
						LPMain.getInstance().getDesktop()
								.exitClientNowErrorDlg(ex);
					}

				}
			}

		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() instanceof WrapperNumberField) {

			try {
				wlaSumme.setText("Gew\u00E4hlt: "
						+ Helper.formatZahl(getGewaehlteMenge(), 4, LPMain
								.getInstance().getTheClient().getLocUi()));

			} catch (Throwable ex) {
				LPMain.getInstance().getDesktop().exitClientNowErrorDlg(ex);
			}
		}

	}

	public ArrayList<BucheSerienChnrAufLosDto> getDaten(
			Integer lossollmaterialIId) throws Throwable {
		ArrayList<BucheSerienChnrAufLosDto> alSnrchnr = new ArrayList<BucheSerienChnrAufLosDto>();

		for (int i = 0; i < komponenten.size(); i++) {
			Object[] oTemp = (Object[]) komponenten.get(i);
			WrapperNumberField wnf = (WrapperNumberField) oTemp[COMPONENTE];

			if (wnf.getBigDecimal() != null
					&& wnf.getBigDecimal().doubleValue() != 0) {
				BucheSerienChnrAufLosDto dtoTemp = new BucheSerienChnrAufLosDto();
				dtoTemp.setArtikelIId(artikelDto.getIId());
				dtoTemp.setLagerIId((Integer) oTemp[LAGER]);
				dtoTemp.setCSeriennrChargennr((String) oTemp[SERIENCHARGENNR]);
				dtoTemp.setNMenge(wnf.getBigDecimal());
				dtoTemp.setLossollmaterialIId(lossollmaterialIId);
				alSnrchnr.add(dtoTemp);
			}
		}
		return alSnrchnr;
	}

	private BigDecimal getGewaehlteMenge() throws Throwable {
		BigDecimal gewaehlt = new BigDecimal(0);

		for (int i = 0; i < komponenten.size(); i++) {
			Object[] oTemp = (Object[]) komponenten.get(i);
			WrapperNumberField wnf = (WrapperNumberField) oTemp[COMPONENTE];

			if (wnf.getBigDecimal() != null) {
				gewaehlt = gewaehlt.add(wnf.getBigDecimal());
			}

		}

		wlaSumme.setText("Gew\u00E4hlt: "
				+ Helper.formatZahl(gewaehlt, 3, LPMain.getInstance()
						.getTheClient().getLocUi()));

		return gewaehlt;
	}

	private void jbInit() throws Throwable {

		this.getContentPane().setLayout(gridBagLayout2);
		panelSnrChnr.setLayout(gridBagLayout1);

		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.uebernehmen"));
		jButtonUebernehmen.addActionListener(this);

		wlaSumme.setText("Gew\u00E4hlt: "
				+ Helper.formatZahl(getGewaehlteMenge(), 3, LPMain
						.getInstance().getTheClient().getLocUi()));

		this.getContentPane().add(
				panelSnrChnr,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 10, 10, 10), 0, 0));
		int iZeile = 0;
		panelSnrChnr.add(new JLabel("Es werden Gesamt "
				+ Helper.formatZahl(gesamtmenge, 3, LPMain.getInstance()
						.getTheClient().getLocUi()) + " "
				+ artikelDto.getEinheitCNr().trim() + " ben\u00F6tigt:"),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(10, 2, 10, 2), 0, 0));

		iZeile++;

		// Anzahl der Chargen
		LoslagerentnahmeDto[] laeger = DelegateFactory.getInstance()
				.getFertigungDelegate().loslagerentnahmeFindByLosIId(losIId);

		for (int i = 0; i < laeger.length; i++) {
			SeriennrChargennrAufLagerDto[] snrchnrauflagerDtos = DelegateFactory
					.getInstance().getLagerDelegate()
					.getAllSerienChargennrAufLagerInfoDtos(artikelDto.getIId(),
							laeger[i].getLagerIId());

			snrchnrauflagerDtos = bereinigeBereitsVerbrauchteSnrChnr(
					alBereitsGebucht, snrchnrauflagerDtos, artikelDto.getIId(),laeger[i].getLagerIId());

			LagerDto lagerDto = DelegateFactory.getInstance()
					.getLagerDelegate().lagerFindByPrimaryKey(
							laeger[i].getLagerIId());

			if (snrchnrauflagerDtos.length > 0) {
				bEsIstNichtsAufLager = false;
			}

			for (int j = 0; j < snrchnrauflagerDtos.length; j++) {
				SeriennrChargennrAufLagerDto dto = snrchnrauflagerDtos[j];

				Object[] o = new Object[5];

				o[LAGER] = laeger[i].getLagerIId();
				o[SERIENCHARGENNR] = dto.getCSeriennrChargennr();
				o[LAGERSTAND] = dto.getNMenge();

				WrapperNumberField wnfMenge = new WrapperNumberField();
				wnfMenge.setFractionDigits(3);
				wnfMenge.addKeyListener(this);

				if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
					wnfMenge.setMaximumValue(new BigDecimal(1));
				} else {
					wnfMenge.setMaximumValue(dto.getNMenge());
				}

				JButton button = new JButton(dto.getCSeriennrChargennr());
				button.addActionListener(this);
				o[BUTTON] = button;

				panelSnrChnr.add(button, new GridBagConstraints(0, iZeile, 1,
						1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
				panelSnrChnr.add(new JLabel("vom "
						+ Helper.formatTimestamp(dto.getTBuchungszeit(), LPMain
								.getInstance().getTheClient().getLocUi())),
						new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));

				panelSnrChnr.add(wnfMenge, new GridBagConstraints(2, iZeile, 1,
						1, 0, 0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
				panelSnrChnr.add(new JLabel(lagerDto.getCNr()),
						new GridBagConstraints(3, iZeile, 1, 1, 0.5, 0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));
				panelSnrChnr.add(new JLabel(Helper.formatZahl(dto.getNMenge(),
						3, LPMain.getInstance().getTheClient().getLocUi())),
						new GridBagConstraints(4, iZeile, 1, 1, 0.5, 0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));

				o[COMPONENTE] = wnfMenge;

				komponenten.add(o);

				iZeile++;

			}

		}

		panelSnrChnr.add(wlaSumme, new GridBagConstraints(2, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		panelSnrChnr.add(jButtonUebernehmen, new GridBagConstraints(0, iZeile,
				5, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

	}
}
