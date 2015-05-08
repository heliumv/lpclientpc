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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogSnrChnrauswahl;
import com.lp.client.frame.component.DialogSnrauswahl;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class DialogGeraeteseriennummern extends JDialog implements
		ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	public JButton jButtonOK = new JButton();
	Integer losIId = null;
	ArrayList<WrapperSNRField> wtfSnr = new ArrayList<WrapperSNRField>();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	public boolean bAbgebrochen = true;

	public boolean bSnrBehafteteArtikelVorhanden = false;

	public ArrayList<GeraetesnrDto> alGeraetesnr = new ArrayList<GeraetesnrDto>();
	public ArrayList<JButton> buttons = new ArrayList<JButton>();

	public DialogGeraeteseriennummern(String title, Integer losIId)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), title, true);
		this.losIId = losIId;

		jbInit();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();

				setVisible(false);
				dispose();
			}
		});

		setVisible(false);

	}

	private void jbInit() throws Throwable {
		int height = 100;

		int iZeile = 0;
		LossollmaterialDto[] sollMatDtos = DelegateFactory.getInstance()
				.getFertigungDelegate().lossollmaterialFindByLosIId(losIId);

		this.getContentPane().setLayout(gridBagLayout2);
		for (int i = 0; i < sollMatDtos.length; i++) {
			if (sollMatDtos[i].getArtikelIId() != null) {

				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate().artikelFindByPrimaryKey(
								sollMatDtos[i].getArtikelIId());
				if (Helper.short2boolean(artikelDto.getBSeriennrtragend()) == true) {
					bSnrBehafteteArtikelVorhanden = true;
					WrapperLabel wla = new WrapperLabel();
					wla.setText(artikelDto.formatArtikelbezeichnung());

					GeraetesnrDto gDto = new GeraetesnrDto();
					gDto.setArtikelIId(artikelDto.getIId());

					alGeraetesnr.add(gDto);

					this.getContentPane().add(
							wla,
							new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 0), 150, 0));

					WrapperSNRField snrField = new WrapperSNRField();
					snrField.setMandatoryField(true);
					snrField.addKeyListener(this);
					wtfSnr.add(snrField);

					// SP466, Wenn Menge==1, dann SNR bereits vorbesetzen
					if (sollMatDtos[i].getNMenge().doubleValue() == 1) {

						LosistmaterialDto[] istmatDtos = DelegateFactory
								.getInstance().getFertigungDelegate()
								.losistmaterialFindByLossollmaterialIId(
										sollMatDtos[i].getIId());
						for (int j = 0; j < istmatDtos.length; j++) {
							if (istmatDtos[j].getNMenge().doubleValue() == 1) {
								List<SeriennrChargennrMitMengeDto> l = DelegateFactory
										.getInstance()
										.getLagerDelegate()
										.getAllSeriennrchargennrEinerBelegartposition(
												LocaleFac.BELEGART_LOS,
												istmatDtos[j].getIId());
								if (l.size() == 1) {
									snrField.setText(l.get(0)
											.getCSeriennrChargennr());
									break;
								}

							}
						}

					}

					this.getContentPane().add(
							snrField,
							new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 0), 150, 0));

					JButton b = new JButton("...");
					b.setActionCommand(sollMatDtos[i].getIId() + "");
					b.addActionListener(this);

					buttons.add(b);

					this.getContentPane().add(
							b,
							new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 0), 0, 0));

					iZeile++;
					height = height + 24;
				}

			}
		}
		if (bSnrBehafteteArtikelVorhanden == false) {
			setVisible(false);
			dispose();
		}
		this.setSize(400, height);
		jButtonOK.setText("Ok");
		jButtonOK.addActionListener(this);
		this.getContentPane().add(
				jButtonOK,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(15, 15, 15, 0), 0, 0));

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jButtonOK)) {
			for (int i = 0; i < wtfSnr.size(); i++) {
				if (wtfSnr.get(i).getText() == null) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain
											.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				// Pruefen, ob auch wirklich aufs Los gebucht
				try {
					boolean b = DelegateFactory.getInstance()
							.getLagerDelegate()
							.pruefeObSnrChnrAufBelegGebuchtWurde(
									LocaleFac.BELEGART_LOS, losIId,
									alGeraetesnr.get(i).getArtikelIId(),
									wtfSnr.get(i).getText());

					if (b == false) {
						ArtikelDto artikelDto = DelegateFactory.getInstance()
								.getArtikelDelegate().artikelFindByPrimaryKey(
										alGeraetesnr.get(i).getArtikelIId());

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain
												.getTextRespectUISPr("fert.geraeteseriennummern.error")
												+ " "
												+ artikelDto
														.formatArtikelbezeichnung()
												+ " - "
												+ wtfSnr.get(i).getText());
						return;
					}
				} catch (Throwable e1) {
					LPMain.getInstance().exitFrame(null, e1);
				}

				alGeraetesnr.get(i).setCSnr(wtfSnr.get(i).getText());

			}
			bAbgebrochen = false;
			setVisible(false);

		} else {
			for (int i = 0; i < buttons.size(); i++) {
				if (e.getSource().equals(buttons.get(i))) {

					try {
						Integer sollmaterialIId = new Integer(e
								.getActionCommand());
						ArrayList<String> alOffeneSnrs = DelegateFactory
								.getInstance().getFertigungDelegate()
								.getOffeneGeraeteSnrEinerSollPosition(
										sollmaterialIId);

						// LossollmaterialDto
						// sollmatDto=DelegateFactory.getInstance
						// ().getFertigungDelegate
						// ().lossollmaterialFindByPrimaryKey(sollmaterialIId);

						String[] snrs = new String[0];
						snrs = alOffeneSnrs.toArray(snrs);
						DialogSnrauswahl d = new DialogSnrauswahl(snrs, false);
						LPMain.getInstance().getDesktop()
								.platziereDialogInDerMitteDesFensters(d);

						d.setVisible(true);
						String[] sSelektiert = d.sSeriennrArray;
						if (sSelektiert != null && sSelektiert.length > 0) {
							wtfSnr.get(i).setText(sSelektiert[0]);
						}

					} catch (Throwable e1) {
						LPMain.getInstance().exitFrame(null, e1);
					}

					break;
				}
			}
		}

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getSource() instanceof WrapperSNRField
				&& e.getKeyCode() == KeyEvent.VK_ENTER) {

			for (int i = 0; i < wtfSnr.size(); i++) {
				if (e.getSource().equals(wtfSnr.get(i))) {
					if (i == wtfSnr.size() - 1) {
						jButtonOK.requestFocus();
					} else {
						wtfSnr.get(i + 1).requestFocus();
					}
				}
			}

		}

	}

	public void keyReleased(KeyEvent e) {
	}
}
