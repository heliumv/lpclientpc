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
package com.lp.client.kueche;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.kueche.service.Kdc100logDto;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/*
 * <p><I>Dialog zur Nachtraeglichen Entnahme von Material auf ein Los</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>17. 10. 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.9 $
 */
public class PanelDialogStifteeinlesen extends PanelDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList gefundeneStifte = null;

	private WrapperLabel[] wlaName = null;
	private WrapperLabel[] wlaAnzahlDerDatensaetze = null;
	private WrapperLabel[] wlaPort = null;
	private WrapperLabel[] wlaSeriennummer = null;
	private WrapperSelectField[] wsfLager = null;
	private WrapperSelectField[] wsfZielLager = null;
	private WrapperButton[] wbuEinlesen = null;
	private WrapperButton[] wbuLeeren = null;

	public PanelDialogStifteeinlesen(InternalFrame internalFrame,
			ArrayList gefundeneStifte, String title) throws Throwable {
		super(internalFrame, title);
		this.gefundeneStifte = gefundeneStifte;
		init();
		setDefaults();
		initComponents();
	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */
	private void init() throws Throwable {

		if (gefundeneStifte != null) {

			wlaName = new WrapperLabel[gefundeneStifte.size()];
			wlaAnzahlDerDatensaetze = new WrapperLabel[gefundeneStifte.size()];
			wlaPort = new WrapperLabel[gefundeneStifte.size()];
			wlaSeriennummer = new WrapperLabel[gefundeneStifte.size()];
			wsfLager = new WrapperSelectField[gefundeneStifte.size()];
			wsfZielLager = new WrapperSelectField[gefundeneStifte.size()];
			wbuEinlesen = new WrapperButton[gefundeneStifte.size()];
			wbuLeeren = new WrapperButton[gefundeneStifte.size()];

			for (int i = 0; i < gefundeneStifte.size(); i++) {

				String[] sa = (String[]) gefundeneStifte.get(i);

				wlaAnzahlDerDatensaetze[i] = new WrapperLabel(sa[2]);

				wlaPort[i] = new WrapperLabel(sa[1]);

				wlaSeriennummer[i] = new WrapperLabel(sa[0]);

				wsfLager[i] = new WrapperSelectField(WrapperSelectField.LAGER,
						getInternalFrame(), false);
				wsfZielLager[i] = new WrapperSelectField(
						WrapperSelectField.LAGER, getInternalFrame(), true);

				wsfLager[i].setMandatoryField(true);

				wlaName[i] = new WrapperLabel();

				ArbeitsplatzDto apDto = DelegateFactory.getInstance()
						.getParameterDelegate()
						.arbeitsplatzFindByCTypCGeraetecode(
								ParameterFac.ARBEITSPLATZ_TYP_KDC100, sa[0]);

				if (apDto == null) {

					wlaName[i].setText(LPMain
							.getTextRespectUISPr("lp.unbekannt"));
				} else {
					wlaName[i].setText(apDto.getCPcname());

					ArbeitsplatzparameterDto parameter = DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.holeArbeitsplatzparameter(
									apDto.getCPcname(),
									ParameterFac.ARBEITSPLATZPARAMETER_KDC100_DEFAULTLAGER);

					if (parameter != null && parameter.getCWert() != null) {
						Integer lagerIId_Default = new Integer(parameter
								.getCWert());
						wsfLager[i].setKey(lagerIId_Default);
					}

					parameter = DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.holeArbeitsplatzparameter(
									apDto.getCPcname(),
									ParameterFac.ARBEITSPLATZPARAMETER_KDC100_DEFAULTZIELLAGER);

					if (parameter != null && parameter.getCWert() != null) {
						Integer lagerIId_Default = new Integer(parameter
								.getCWert());
						wsfZielLager[i].setKey(lagerIId_Default);
					}

					wbuEinlesen[i] = new WrapperButton();
					wbuEinlesen[i].setText(LPMain
							.getTextRespectUISPr("kue.stifteinlesen"));

					wbuEinlesen[i].setActionCommand(sa[0]);
					wbuEinlesen[i].addActionListener(this);

					wbuLeeren[i] = new WrapperButton();
					wbuLeeren[i].setText("Leeren");

					wbuLeeren[i].setActionCommand(sa[0]);
					wbuLeeren[i].addActionListener(this);

				}

			}

		}

		getInternalFrame().addItemChangedListener(this);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(new WrapperLabel("COM-Port"), new GridBagConstraints(
				0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel("Seriennummer"),
				new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(new WrapperLabel("Name"), new GridBagConstraints(2,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel("Datens\u00E4tze"),
				new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(new WrapperLabel("Lager"), new GridBagConstraints(4,
				iZeile, 2, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel("Ziellager"), new GridBagConstraints(6,
				iZeile, 2, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel("Einlesen"), new GridBagConstraints(
				8, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(new WrapperLabel("Leeren"), new GridBagConstraints(9,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		iZeile++;
		for (int i = 0; i < gefundeneStifte.size(); i++) {
			jpaWorkingOn.add(wlaPort[i],
					new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wlaSeriennummer[i],
					new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wlaName[i],
					new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wlaAnzahlDerDatensaetze[i],
					new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));

			if (wbuEinlesen[i] != null) {
				jpaWorkingOn.add(wsfLager[i], new GridBagConstraints(4, iZeile,
						1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
				jpaWorkingOn.add(wsfLager[i].getWrapperTextField(),
						new GridBagConstraints(5, iZeile, 1, 1, 1.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));
				
				jpaWorkingOn.add(wsfZielLager[i], new GridBagConstraints(6, iZeile,
						1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
				jpaWorkingOn.add(wsfZielLager[i].getWrapperTextField(),
						new GridBagConstraints(7, iZeile, 1, 1, 1.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));
				
				jpaWorkingOn.add(wbuEinlesen[i], new GridBagConstraints(8,
						iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
				jpaWorkingOn.add(wbuLeeren[i], new GridBagConstraints(9,
						iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
			}

			iZeile++;

		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand() != null) {
			for (int i = 0; i < wbuEinlesen.length; i++) {
				if (e.getSource().equals(wbuEinlesen[i])) {
					if (wsfLager[i].getIKey() != null) {

						String[] sa = (String[]) gefundeneStifte.get(i);

						DialogKdc100 d = new DialogKdc100(sa[1]);

						if (d.kdc100 != null && d.kdc100.size() > 0) {

							ArrayList<Kdc100logDto> alDaten = d.kdc100;
							Kdc100logDto[] dtos = new Kdc100logDto[alDaten
									.size()];

							for (int j = 0; j < alDaten.size(); j++) {
								dtos[j] = (Kdc100logDto) alDaten.get(j);
								dtos[j].setLagerIId(wsfLager[i].getIKey());
							}

							int iFehler = DelegateFactory.getInstance()
									.getKuecheDelegate().stiftdatenImportieren(
											dtos,wsfZielLager[i].getIKey());

							// dann loeschen
							DialogKdc100 d2 = new DialogKdc100(true, sa[1]);
							if (d2.bGeloescht == false) {
								DialogFactory
										.showModalDialog(
												"Fehler",
												"Stift konnte nicht geleert werden, bitte l\u00F6schen Sie die Daten des Stift manuell.");
							} else {
								wlaAnzahlDerDatensaetze[i].setText("0");

								if (iFehler == 0) {

									LPMain
											.getInstance()
											.getDesktop()
											.showModalDialog(
													"Info",
													"Stift fehlerfrei eingelesen und geleert.",
													JOptionPane.INFORMATION_MESSAGE);
								} else {
									LPMain
											.getInstance()
											.getDesktop()
											.showModalDialog(
													"Fehler",
													"Beim Einlesen des Stiftes wurde(n) "
															+ iFehler
															+ " Fehler festgestellt. Bitte \u00FCberpr\u00FCfen Sie diese im Protokoll.",
													JOptionPane.ERROR_MESSAGE);

								}

							}
						}
						// Zuerst Stiftdaten importieren

					} else {
						DialogFactory.showModalDialog("Fehler",
								"Bitte geben Sie ein Lager an.");
					}

				}

				if (e.getSource().equals(wbuLeeren[i])) {
					String[] sa = (String[]) gefundeneStifte.get(i);
					boolean b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									"Wollen Sie wirklich alle Daten des Barcode- Lesestiftes l\u00F6schen?",
									"Frage");

					if (b == true) {
						DialogKdc100 d2 = new DialogKdc100(true, sa[1]);
						if (d2.bGeloescht == false) {
							DialogFactory
									.showModalDialog(
											"Fehler",
											"Stift konnte nicht geleert werden, bitte l\u00F6schen Sie die Daten des Stift manuell.");
						} else {
							wlaAnzahlDerDatensaetze[i].setText("0");
						}
					}

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
}
