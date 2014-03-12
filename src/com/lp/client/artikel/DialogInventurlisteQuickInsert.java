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
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.InventurlisteDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogInventurlisteQuickInsert extends JDialog implements
		KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	WrapperLabel wlaArtikelnummer = new WrapperLabel();
	JTextField wtfArtikelnummer = new JTextField();
	WrapperLabel wlaBezeichnung = new WrapperLabel();
	JTextField wtfBezeichnung = new JTextField();
	WrapperLabel wlaSerienChargennummer = new WrapperLabel();
	JTextField wtfSerienChargennummer = new JTextField();
	WrapperLabel wlaMenge = new WrapperLabel();
	WrapperNumberField wnfMenge = new WrapperNumberField();
	JButton wbuFertig = new JButton();
	WrapperLabel wlaLager = new WrapperLabel();
	WrapperComboBox wcoLager = new WrapperComboBox();

	ArtikelDto letzter_gebuchter_artikelDto = null;
	ArtikelDto artikelDto = null;
	Integer inventurIId = null;
	PanelInventurliste panelInventurliste = null;

	public DialogInventurlisteQuickInsert(
			PanelInventurliste panelInventurliste, Integer inventurIId)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("artikel.inventurliste.schnelleingabe"),
				true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.panelInventurliste = panelInventurliste;
		this.inventurIId = inventurIId;
		jbInit();
		pack();
		wtfArtikelnummer.requestFocus();
	}

	public void dispose() {
		try {

			if (wtfArtikelnummer.getText() != null
					&& wtfArtikelnummer.getText().length() > 0
					&& artikelDto != null && wnfMenge.getBigDecimal() != null) {

				KeyEvent k = new KeyEvent(wnfMenge, 0, 0, 0, KeyEvent.VK_ENTER,
						' ');

				keyPressed(k);
			}

		} catch (Throwable ex) {
			panelInventurliste.handleException(ex, true);
		}

		super.dispose();
	}

	private void jbInit() throws Exception {
		panelUrlaubsanspruch.setLayout(gridBagLayout1);

		wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"));
		wlaSerienChargennummer.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"artikel.handlagerbewegung.seriennrchargennr"));
		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));
		wlaLager.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.lager"));

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setEnabled(false);
		wtfSerienChargennummer.setEnabled(false);

		wtfArtikelnummer.addKeyListener(this);

		wtfSerienChargennummer.addKeyListener(this);
		wnfMenge.addKeyListener(this);
		wcoLager.setMandatoryField(true);

		try {
			wcoLager.setMap(DelegateFactory.getInstance().getLagerDelegate()
					.getAllLager());
		} catch (Throwable ex) {
			panelInventurliste.handleException(ex, true);
		}

		wbuFertig.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.beenden"));
		wbuFertig
				.addActionListener(new DialogInventurlisteQuickInsert_wbuFertig_actionAdapter(
						this));
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panelUrlaubsanspruch,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 250, 50));

		panelUrlaubsanspruch.add(wcoLager, new GridBagConstraints(2, 0, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaLager, new GridBagConstraints(0, 0, 1, 1,
				0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wtfArtikelnummer, new GridBagConstraints(2, 1,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaArtikelnummer, new GridBagConstraints(0, 1,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wtfBezeichnung, new GridBagConstraints(2, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaBezeichnung, new GridBagConstraints(0, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wlaSerienChargennummer,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		panelUrlaubsanspruch.add(wtfSerienChargennummer,
				new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		panelUrlaubsanspruch.add(wlaMenge, new GridBagConstraints(0, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfMenge, new GridBagConstraints(2, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wbuFertig, new GridBagConstraints(2, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void wbuFertig_actionPerformed(ActionEvent e) {

		this.dispose();
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == wtfArtikelnummer) {

				try {
					artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByCNr(wtfArtikelnummer.getText());

				} catch (Throwable ex) {
					if (ex instanceof ExceptionLP) {
						ExceptionLP exLP = (ExceptionLP) ex;
						if (exLP.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
							DialogFactory.showModalDialog(LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
									"Artikel konnte nicht gefunden werden.");
							artikelDto = null;
							wtfArtikelnummer.setText(null);
							wtfBezeichnung.setText(null);
							wtfArtikelnummer.requestFocus();
						} else {
							panelInventurliste.handleException(ex, true);
						}
					}

					else {
						panelInventurliste.handleException(ex, true);
					}

				}

				if (artikelDto != null) {
					wtfBezeichnung.setText(artikelDto
							.formatArtikelbezeichnung());

					if (Helper.short2boolean(artikelDto.getBChargennrtragend())
							|| Helper.short2boolean(artikelDto
									.getBSeriennrtragend())) {
						wtfSerienChargennummer.setEnabled(true);
						wtfSerienChargennummer.setText(null);
						wtfSerienChargennummer.requestFocus();
						if (Helper.short2boolean(artikelDto
								.getBSeriennrtragend())) {
							try {
								wnfMenge.setDouble(new Double(1));
							} catch (ExceptionLP ex2) {
								// wird hoffentlich gehen
							}
						}

					} else {
						wtfSerienChargennummer.setText(null);
						wtfSerienChargennummer.setEnabled(false);
						try {
							wnfMenge.setDouble(null);
						} catch (ExceptionLP ex2) {
							// wird hoffentlich gehen
						}

						wnfMenge.requestFocus();
					}
				}
			} else if (e.getSource() == wtfSerienChargennummer) {
				if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {

					try {

						InventurlisteDto inventurlisteDto = new InventurlisteDto();

						inventurlisteDto.setArtikelIId(artikelDto.getIId());
						inventurlisteDto.setInventurIId(inventurIId);
						inventurlisteDto.setLagerIId((Integer) wcoLager
								.getKeyOfSelectedItem());
						inventurlisteDto
								.setNInventurmenge(new java.math.BigDecimal(1));
						Boolean bKorrekturbuchung = true;

						if (wtfSerienChargennummer.getText() != null
								&& !wtfSerienChargennummer.getText().equals("")) {

							String[] snrs = Helper.erzeugeSeriennummernArray(
									wtfSerienChargennummer.getText(), null,
									false);

							if (snrs.length == 1) {

								inventurlisteDto
										.setCSeriennrchargennr(wtfSerienChargennummer
												.getText());

								InventurlisteDto dto = null;
								try {
									dto = DelegateFactory
											.getInstance()
											.getInventurDelegate()
											.inventurlisteFindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr(
													inventurIId,
													artikelDto.getIId(),
													(Integer) wcoLager
															.getKeyOfSelectedItem(),
													wtfSerienChargennummer
															.getText());

									if (dto != null) {
										DialogInventurlisteArtikelDoppelt d = new DialogInventurlisteArtikelDoppelt();
										LPMain.getInstance()
												.getDesktop()
												.platziereDialogInDerMitteDesFensters(
														d);
										d.setVisible(true);
										bKorrekturbuchung = d.bKorrekturbuchung;
									}
								} catch (ExceptionLP ex3) {
									if (ex3.getICode() != EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
										panelInventurliste.handleException(ex3,
												false);
									}
								}

								if (bKorrekturbuchung != null) {

									DelegateFactory
											.getInstance()
											.getInventurDelegate()
											.inventurlisteErfassenMitScanner(
													inventurlisteDto,
													bKorrekturbuchung);

									wtfArtikelnummer.setText(null);
									wtfBezeichnung.setText(null);
									wtfSerienChargennummer.setText(null);
									wnfMenge.setDouble(null);
									wtfArtikelnummer.requestFocus();

									letzter_gebuchter_artikelDto = artikelDto;

								}
								wtfArtikelnummer.setText(null);
								wtfBezeichnung.setText(null);
								wtfSerienChargennummer.setText(null);

								wnfMenge.setDouble(null);
							} else {

								// PJ18242
								ArrayList<String> al = DelegateFactory
										.getInstance()
										.getInventurDelegate()
										.sindSeriennumernBereitsInventiert(
												inventurlisteDto, snrs);

								if (al.size() > 0) {
									// Zuerst Dialog

									int iOptionDurchfuehren = 0;
									int iOptionAbbrechen = 1;

									Object[] options = new Object[2];
									options[iOptionDurchfuehren] = LPMain
											.getInstance()
											.getTextRespectUISPr(
													"artikel.snrinventur.option1");
									options[iOptionAbbrechen] = LPMain
											.getInstance()
											.getTextRespectUISPr(
													"artikel.snrinventur.option2");

									String meldung = LPMain
											.getInstance()
											.getTextRespectUISPr(
													"artikel.snrinventur.warnung")
											+ " ";

									for (int i = 0; i < al.size(); i++) {
										meldung += al.get(i);

										if (i != al.size() - 1) {
											meldung += ",";
										}

									}

									int i = DialogFactory.showModalDialog(
											panelInventurliste
													.getInternalFrame(),
											meldung,
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.info"),
											options, iOptionAbbrechen);

									if (i != iOptionDurchfuehren) {

										return;
									}

								}

								DelegateFactory
										.getInstance()
										.getInventurDelegate()
										.mehrereSeriennumernInventieren(
												inventurlisteDto, snrs);

								wtfArtikelnummer.setText(null);
								wtfBezeichnung.setText(null);
								wtfSerienChargennummer.setText(null);
								wnfMenge.setDouble(null);
								wtfArtikelnummer.requestFocus();

								letzter_gebuchter_artikelDto = artikelDto;

							}

						}

					} catch (Throwable ex) {

						if (ex instanceof EJBExceptionLP) {

							panelInventurliste.handleException(
									new ExceptionLP(((EJBExceptionLP) ex)
											.getCode(), "",
											((EJBExceptionLP) ex)
													.getAlInfoForTheClient(),
											ex.getCause()), true);

						} else {
							panelInventurliste.handleException(ex, true);
						}
						return;

					}

					wtfArtikelnummer.requestFocus();

				} else if (Helper.short2boolean(artikelDto
						.getBChargennrtragend())) {
					wnfMenge.requestFocus();
				}
			} else if (e.getSource() == wnfMenge) {
				try {
					InventurlisteDto inventurlisteDto = new InventurlisteDto();

					inventurlisteDto.setArtikelIId(artikelDto.getIId());
					inventurlisteDto.setInventurIId(inventurIId);

					inventurlisteDto
							.setNInventurmenge(wnfMenge.getBigDecimal());
					inventurlisteDto.setLagerIId((Integer) wcoLager
							.getKeyOfSelectedItem());

					Boolean bKorrekturbuchung = true;
					if (wtfSerienChargennummer.getText() != null
							&& !wtfSerienChargennummer.getText().equals("")) {

						String[] snrs = Helper.erzeugeSeriennummernArray(
								wtfSerienChargennummer.getText(), null, false);

						if (snrs.length == 1) {

							inventurlisteDto
									.setCSeriennrchargennr(wtfSerienChargennummer
											.getText());

							InventurlisteDto dto = null;
							try {
								dto = DelegateFactory
										.getInstance()
										.getInventurDelegate()
										.inventurlisteFindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr(
												inventurIId,
												artikelDto.getIId(),
												(Integer) wcoLager
														.getKeyOfSelectedItem(),
												wtfSerienChargennummer
														.getText());

								if (dto != null) {
									DialogInventurlisteArtikelDoppelt d = new DialogInventurlisteArtikelDoppelt();
									LPMain.getInstance()
											.getDesktop()
											.platziereDialogInDerMitteDesFensters(
													d);
									d.setVisible(true);
									bKorrekturbuchung = d.bKorrekturbuchung;
								}
							} catch (ExceptionLP ex3) {
								if (ex3.getICode() != EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
									panelInventurliste.handleException(ex3,
											false);
								}
							}
						} else {
							// PJ18242
							ArrayList<String> al = DelegateFactory
									.getInstance()
									.getInventurDelegate()
									.sindSeriennumernBereitsInventiert(
											inventurlisteDto, snrs);

							if (al.size() > 0) {
								// Zuerst Dialog

								int iOptionDurchfuehren = 0;
								int iOptionAbbrechen = 1;

								Object[] options = new Object[2];
								options[iOptionDurchfuehren] = LPMain
										.getInstance().getTextRespectUISPr(
												"artikel.snrinventur.option1");
								options[iOptionAbbrechen] = LPMain
										.getInstance().getTextRespectUISPr(
												"artikel.snrinventur.option2");

								String meldung = LPMain.getInstance()
										.getTextRespectUISPr(
												"artikel.snrinventur.warnung")
										+ " ";

								for (int i = 0; i < al.size(); i++) {
									meldung += al.get(i);

									if (i != al.size() - 1) {
										meldung += ",";
									}

								}

								int i = DialogFactory
										.showModalDialog(
												panelInventurliste
														.getInternalFrame(),
												meldung,
												LPMain.getInstance()
														.getTextRespectUISPr(
																"lp.info"),
												options, iOptionAbbrechen);

								if (i != iOptionDurchfuehren) {
									wtfArtikelnummer.setText(null);
									wtfBezeichnung.setText(null);
									wtfSerienChargennummer.setText(null);
									wnfMenge.setDouble(null);
									wtfArtikelnummer.requestFocus();

									letzter_gebuchter_artikelDto = artikelDto;
									return;
								}

							}

							DelegateFactory
									.getInstance()
									.getInventurDelegate()
									.mehrereSeriennumernInventieren(
											inventurlisteDto, snrs);
							wtfArtikelnummer.setText(null);
							wtfBezeichnung.setText(null);
							wtfSerienChargennummer.setText(null);
							wnfMenge.setDouble(null);
							wtfArtikelnummer.requestFocus();

							letzter_gebuchter_artikelDto = artikelDto;
							return;
						}

					} else {
						InventurlisteDto[] dtos = DelegateFactory
								.getInstance()
								.getInventurDelegate()
								.inventurlisteFindByInventurIIdLagerIIdArtikelIId(
										inventurIId,
										artikelDto.getIId(),
										(Integer) wcoLager
												.getKeyOfSelectedItem());

						if (dtos != null && dtos.length > 0) {
							DialogInventurlisteArtikelDoppelt d = new DialogInventurlisteArtikelDoppelt();
							LPMain.getInstance().getDesktop()
									.platziereDialogInDerMitteDesFensters(d);
							d.setVisible(true);
							bKorrekturbuchung = d.bKorrekturbuchung;
						}

					}

					if (bKorrekturbuchung != null) {

						DelegateFactory
								.getInstance()
								.getInventurDelegate()
								.inventurlisteErfassenMitScanner(
										inventurlisteDto, bKorrekturbuchung);

						wtfArtikelnummer.setText(null);
						wtfBezeichnung.setText(null);
						wtfSerienChargennummer.setText(null);
						wnfMenge.setDouble(null);
						wtfArtikelnummer.requestFocus();

						letzter_gebuchter_artikelDto = artikelDto;
					}
				} catch (Throwable ex) {
					panelInventurliste.handleException(ex, false);
				}
			}

		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}

class DialogInventurlisteQuickInsert_wbuFertig_actionAdapter implements
		ActionListener {
	private DialogInventurlisteQuickInsert adaptee;

	DialogInventurlisteQuickInsert_wbuFertig_actionAdapter(
			DialogInventurlisteQuickInsert adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuFertig_actionPerformed(e);
	}
}
