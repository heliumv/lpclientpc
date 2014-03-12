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
package com.lp.client.frame.component;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lp.client.anfrage.InternalFrameAnfrage;
import com.lp.client.anfrage.TabbedPaneAnfrage;
import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.angebot.TabbedPaneAngebot;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.artikel.TabbedPaneArtikel;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.auftrag.TabbedPaneAuftrag;
import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.bestellung.TabbedPaneBestellung;
import com.lp.client.eingangsrechnung.InternalFrameEingangsrechnung;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.fertigung.TabbedPaneLos;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.finanz.TabbedPaneKonten;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.inserat.InseratFilterFactory;
import com.lp.client.inserat.InternalFrameInserat;
import com.lp.client.inserat.TabbedPaneInserat;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.lieferschein.TabbedPaneLieferschein;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.InternalFramePartner;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPaneKunde;
import com.lp.client.partner.TabbedPaneLieferant;
import com.lp.client.partner.TabbedPanePartner;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.InternalFrameProjekt;
import com.lp.client.projekt.TabbedPaneProjekt;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.client.rechnung.TabbedPaneGutschrift;
import com.lp.client.rechnung.TabbedPaneRechnung;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.stueckliste.TabbedPaneStueckliste;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;

/**
 * <p>
 * Gewrappter JButton<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.22 $
 */
public class WrapperGotoButton extends JPanel implements ActionListener,
		IControl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperButton wrapperButton = null;
	private Object oKey = null;
	private WrapperButton buttonGoto = null;
	private int iWhereToGo = -1;
	public static final String ACTION_GOTO = "ACTION_GOTO";

	// gotobutton: 1 Hier neue GOTO- Konstante definieren
	public static final int GOTO_ARTIKEL_AUSWAHL = 1;
	public static final int GOTO_PARTNER_AUSWAHL = 2;
	public static final int GOTO_KUNDE_AUSWAHL = 3;
	public static final int GOTO_LIEFERANT_AUSWAHL = 4;
	public static final int GOTO_FERTIGUNG_AUSWAHL = 5;
	public static final int GOTO_STUECKLISTE_AUSWAHL = 6;
	public static final int GOTO_STUECKLISTE_DETAIL = 7;
	public static final int GOTO_EINGANGSRECHNUNG_AUSWAHL = 8;
	public static final int GOTO_PROJEKT_AUSWAHL = 9;
	public static final int GOTO_AUFTRAG_AUSWAHL = 10;
	public static final int GOTO_ANGEBOT_AUSWAHL = 11;
	public static final int GOTO_BUCHUNGDETAIL = 12;
	public static final int GOTO_RECHNUNG_AUSWAHL = 13;
	public static final int GOTO_LIEFERSCHEIN_AUSWAHL = 14;
	public static final int GOTO_KREDITORENKONTO_AUSWAHL = 15;
	public static final int GOTO_DEBITORENKONTO_AUSWAHL = 16;
	public static final int GOTO_BESTELLUNG_AUSWAHL = 17;
	public static final int GOTO_BESTELLUNG_POSITION = 18;
	public static final int GOTO_KUNDE_KONDITIONEN = 19;
	public static final int GOTO_INSERAT_AUSWAHL = 20;
	public static final int GOTO_ANFRAGE_AUSWAHL = 21;
	public static final int GOTO_GUTSCHRIFT_AUSWAHL = 22;


	private static Map<String, Integer> kvpKontoTypPanel;

	public WrapperGotoButton(int iWhereToGo) {
		this.iWhereToGo = iWhereToGo;
		jbInit();
	}

	Dialog dialogToClose = null;

	public void closeDialogOnGoto(Dialog dialogToClose) {
		this.dialogToClose = dialogToClose;
	}

	public WrapperGotoButton(String sText, int iWhereToGo) {
		this.iWhereToGo = iWhereToGo;
		jbInit();
		wrapperButton.setText(sText);
	}

	public WrapperButton getWrapperButton() {
		return wrapperButton;
	}

	public WrapperButton getWrapperButtonGoTo() {
		return buttonGoto;
	}

	public void setActionCommand(String command) {
		wrapperButton.setActionCommand(command);
	}

	public void addActionListener(ActionListener l) {
		wrapperButton.addActionListener(l);
	}

	public void setEnabled(boolean bEnabled) {
		wrapperButton.setEnabled(bEnabled);

		if (oKey == null) {
			buttonGoto.setEnabled(false);
		} else {
			buttonGoto.setEnabled(!bEnabled);
		}
	}

	public void setText(String sText) {
		wrapperButton.setText(sText);
	}

	public void setToolTipText(String sText) {
		wrapperButton.setToolTipText(sText);
	}

	public void setActivatable(boolean isActivatable) {
		wrapperButton.setActivatable(isActivatable);
	}

	public boolean isActivatable() {
		return wrapperButton.isActivatable();
	}

	public void setMandatoryField(boolean isMandatoryField) {
		wrapperButton.setMandatoryField(isMandatoryField);
	}

	public boolean isMandatoryField() {
		return wrapperButton.isMandatoryField();
	}

	public void setWhereToGo(int iWhereToGo) {
		this.iWhereToGo = iWhereToGo;
	}

	public void removeContent() {
		oKey = null;
	}

	private void jbInit() {
		wrapperButton = new WrapperButton();
		buttonGoto = new WrapperButton();
		buttonGoto.setIcon(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/data_into.png")));
		buttonGoto.setActionCommand(ACTION_GOTO);
		buttonGoto.addActionListener(this);

		this.setLayout(new GridBagLayout());

		this.add(wrapperButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		/**
		 * @todo MB: buttonGoto nur sichtbar, wenn Modul zur Verfuegung steht.
		 */
		this.add(buttonGoto, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 10, 0));
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_GOTO)) {
			if (oKey != null) {
				// UND hier einsprungspunkt einbauen
				try {
					// gotobutton: 2 Fuer die Konstante ein neues Ziel
					// definieren
					if (iWhereToGo == GOTO_PARTNER_AUSWAHL) {

						KundeDto kundeDto = DelegateFactory
								.getInstance()
								.getKundeDelegate()
								.kundeFindByiIdPartnercNrMandantOhneExc(
										(Integer) oKey,
										LPMain.getTheClient().getMandant());

						LieferantDto lieferantDto = DelegateFactory
								.getInstance()
								.getLieferantDelegate()
								.lieferantFindByiIdPartnercNrMandantOhneExc(
										(Integer) oKey,
										LPMain.getTheClient().getMandant());

						Integer okeyVohrer = new Integer((Integer) oKey);
						if (kundeDto != null && lieferantDto != null) {

							// Custom button text
							Object[] options = {
									LPMain.getTextRespectUISPr("label.kunde"),
									LPMain.getTextRespectUISPr("label.lieferant"),
									LPMain.getTextRespectUISPr("part.partner") };
							int n = JOptionPane
									.showOptionDialog(
											this,
											LPMain.getTextRespectUISPr("lp.partner.goto.wechsel"),
											LPMain.getTextRespectUISPr("lp.frage"),
											JOptionPane.YES_NO_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE, null,
											options, options[2]);

							if (n == 0) {
								iWhereToGo = GOTO_KUNDE_AUSWAHL;
								oKey = kundeDto.getIId();
								actionPerformed(new ActionEvent(this, 0,
										ACTION_GOTO));
								iWhereToGo = GOTO_PARTNER_AUSWAHL;
								oKey = okeyVohrer;
								return;
							} else if (n == 1) {
								iWhereToGo = GOTO_LIEFERANT_AUSWAHL;
								oKey = lieferantDto.getIId();
								actionPerformed(new ActionEvent(this, 0,
										ACTION_GOTO));
								iWhereToGo = GOTO_PARTNER_AUSWAHL;
								oKey = okeyVohrer;
								return;
							}

						} else {
							if (kundeDto != null) {
								iWhereToGo = GOTO_KUNDE_AUSWAHL;
								oKey = kundeDto.getIId();
								actionPerformed(new ActionEvent(this, 0,
										ACTION_GOTO));
								iWhereToGo = GOTO_PARTNER_AUSWAHL;
								oKey = okeyVohrer;
								return;
							} else if (lieferantDto != null) {
								iWhereToGo = GOTO_LIEFERANT_AUSWAHL;
								oKey = lieferantDto.getIId();
								actionPerformed(new ActionEvent(this, 0,
										ACTION_GOTO));
								iWhereToGo = GOTO_PARTNER_AUSWAHL;
								oKey = okeyVohrer;
								return;
							}
						}

						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_PARTNER)) {
							InternalFramePartner ifPartner = (InternalFramePartner) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_PARTNER);
							ifPartner
									.geheZu(InternalFramePartner.IDX_PANE_PARTNER,
											TabbedPanePartner.IDX_PANEL_QP,
											oKey,
											null,
											PartnerFilterFactory.getInstance()
													.createFKPartnerKey(
															(Integer) oKey));
						}

					} else if (iWhereToGo == GOTO_KUNDE_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_KUNDE)) {
							InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_KUNDE);
							ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE,
									TabbedPaneKunde.IDX_PANE_KUNDE, oKey, null,
									PartnerFilterFactory.getInstance()
											.createFKPartnerKey((Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_INSERAT_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_INSERAT)) {
							InternalFrameInserat ifInserat = (InternalFrameInserat) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_INSERAT);
							ifInserat.geheZu(InternalFrameInserat.IDX_TABBED_PANE_INSERAT,
									TabbedPaneInserat.IDX_PANEL_INSERATAUSWAHL, oKey, null,
									InseratFilterFactory.getInstance()
											.createFKInseratKey((Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_KUNDE_KONDITIONEN) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_KUNDE)) {
							InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_KUNDE);
							ifKunde.getTpKunde().getOnlyPanelKundeQP1().clearDefaultFilters();
							ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE,
									TabbedPaneKunde.IDX_PANE_KONDITIONEN, oKey, null,
									PartnerFilterFactory.getInstance()
											.createFKPartnerKey((Integer) oKey));
							ifKunde.getTpKunde().getOnlyPanelKundeQP1().restoreDefaultFilters();
						}
					}

					else if (iWhereToGo == GOTO_LIEFERANT_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_LIEFERANT)) {
							InternalFrameLieferant ifLieferant = (InternalFrameLieferant) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_LIEFERANT);
							ifLieferant
									.geheZu(InternalFrameLieferant.IDX_PANE_LIEFERANT,
											TabbedPaneLieferant.IDX_PANE_LIEFERANT,
											oKey,
											null,
											PartnerFilterFactory.getInstance()
													.createFKPartnerKey(
															(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_ARTIKEL_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_ARTIKEL)) {
							InternalFrameArtikel ifArtikel = (InternalFrameArtikel) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_ARTIKEL);
							ifArtikel
									.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL,
											TabbedPaneArtikel.IDX_PANEL_AUSWAHL,
											oKey,
											null,
											ArtikelFilterFactory.getInstance()
													.createFKArtikellisteKey(
															(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_RECHNUNG_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_RECHNUNG)) {
							InternalFrameRechnung ifRechnung = (InternalFrameRechnung) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_RECHNUNG);
							ifRechnung
									.geheZu(InternalFrameRechnung.IDX_TABBED_PANE_RECHNUNG,
											TabbedPaneRechnung.IDX_RECHNUNGEN,
											oKey,
											null,
											RechnungFilterFactory.getInstance()
													.createFKRechnungKey(
															(Integer) oKey));
						}
					}else if (iWhereToGo == GOTO_GUTSCHRIFT_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_GUTSCHRIFT)) {
							InternalFrameRechnung ifRechnung = (InternalFrameRechnung) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_RECHNUNG);
							ifRechnung
									.geheZu(InternalFrameRechnung.IDX_TABBED_PANE_GUTSCHRIFT,
											TabbedPaneGutschrift.IDX_RECHNUNGEN,
											oKey,
											null,
											RechnungFilterFactory.getInstance()
													.createFKRechnungKey(
															(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_LIEFERSCHEIN_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_LIEFERSCHEIN)) {
							InternalFrameLieferschein ifLieferschein = (InternalFrameLieferschein) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_LIEFERSCHEIN);
							ifLieferschein
									.geheZu(InternalFrameLieferschein.IDX_TABBED_PANE_LIEFERSCHEIN,
											TabbedPaneLieferschein.IDX_PANEL_LIEFERSCHEINAUSWAHL,
											oKey,
											null,
											LieferscheinFilterFactory
													.getInstance()
													.createFKLieferscheinKey(
															(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_BUCHUNGDETAIL) {
						gotoBuchungdetail();
					} else if (iWhereToGo == GOTO_STUECKLISTE_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_STUECKLISTE)) {
							InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_STUECKLISTE);
							ifStueckliste
									.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
											TabbedPaneStueckliste.IDX_PANEL_AUSWAHL,
											oKey,
											null,
											StuecklisteFilterFactory
													.getInstance()
													.createFKStuecklisteKey(
															(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_STUECKLISTE_DETAIL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_STUECKLISTE)) {
							InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_STUECKLISTE);
							ifStueckliste
									.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
											TabbedPaneStueckliste.IDX_PANEL_DETAIL,
											oKey,
											null,
											StuecklisteFilterFactory
													.getInstance()
													.createFKStuecklisteKey(
															(Integer) oKey));
						}
					}

					else if (iWhereToGo == GOTO_FERTIGUNG_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_LOS)) {
							InternalFrameFertigung ifFertigung = (InternalFrameFertigung) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_LOS);
							ifFertigung.geheZu(
									InternalFrameFertigung.IDX_TABBED_PANE_LOS,
									TabbedPaneLos.IDX_AUSWAHL, oKey, null,
									FertigungFilterFactory.getInstance()
											.createFKLosKey((Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_EINGANGSRECHNUNG_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
							InternalFrameEingangsrechnung ifER = (InternalFrameEingangsrechnung) LPMain
									.getInstance()
									.getDesktop()
									.holeModul(
											LocaleFac.BELEGART_EINGANGSRECHNUNG);
							ifER.geheZu(
									InternalFrameEingangsrechnung.IDX_TABBED_PANE_EINGANGSRECHNUNG,
									ifER.getTabbedPaneEingangsrechnung().IDX_EINGANGSRECHNUNGEN,
									oKey,
									null,
									SystemFilterFactory.getInstance()
											.createFKKeyAuswahlliste(
													(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_PROJEKT_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_PROJEKT)) {
							InternalFrameProjekt ifPJ = (InternalFrameProjekt) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_PROJEKT);
							ifPJ.geheZu(
									InternalFrameProjekt.IDX_TABBED_PANE_PROJEKT,
									TabbedPaneProjekt.IDX_PANEL_PROJEKTAUSWAHL,
									oKey,
									null,
									SystemFilterFactory.getInstance()
											.createFKKeyAuswahlliste(
													(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_ANGEBOT_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_ANGEBOT)) {
							InternalFrameAngebot ifAN = (InternalFrameAngebot) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_ANGEBOT);
							ifAN.geheZu(
									InternalFrameAngebot.IDX_TABBED_PANE_ANGEBOT,
									TabbedPaneAngebot.IDX_PANEL_AUSWAHL,
									oKey,
									null,
									SystemFilterFactory.getInstance()
											.createFKKeyAuswahlliste(
													(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_ANFRAGE_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_ANFRAGE)) {
							InternalFrameAnfrage ifAN = (InternalFrameAnfrage) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_ANFRAGE);
							ifAN.geheZu(
									InternalFrameAnfrage.IDX_TABBED_PANE_ANFRAGE,
									TabbedPaneAnfrage.IDX_PANEL_AUSWAHL,
									oKey,
									null,
									SystemFilterFactory.getInstance()
											.createFKKeyAuswahlliste(
													(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_AUFTRAG_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_AUFTRAG)) {
							InternalFrameAuftrag ifAB = (InternalFrameAuftrag) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_AUFTRAG);
							ifAB.geheZu(
									InternalFrameAuftrag.IDX_TABBED_PANE_AUFTRAG,
									TabbedPaneAuftrag.IDX_PANEL_AUFTRAGAUSWAHL,
									oKey, null,
									AuftragFilterFactory.getInstance()
											.createFKAuftragKey((Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_BESTELLUNG_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_BESTELLUNG)) {
							InternalFrameBestellung ifAB = (InternalFrameBestellung) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_BESTELLUNG);
							ifAB.geheZu(
									InternalFrameBestellung.IDX_PANE_BESTELLUNG,
									TabbedPaneBestellung.IDX_PANEL_AUSWAHL,
									oKey,
									null,
									BestellungFilterFactory.getInstance()
											.createFKBestellungKey(
													(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_BESTELLUNG_POSITION) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_BESTELLUNG)) {
							InternalFrameBestellung ifAB = (InternalFrameBestellung) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_BESTELLUNG);

							BestellpositionDto bDto = DelegateFactory
									.getInstance()
									.getBestellungDelegate()
									.bestellpositionFindByPrimaryKey(
											(Integer) oKey);

							ifAB.geheZu(
									InternalFrameBestellung.IDX_PANE_BESTELLUNG,
									TabbedPaneBestellung.IDX_PANEL_BESTELLPOSITION,
									bDto.getBestellungIId(),
									oKey,
									BestellungFilterFactory.getInstance()
											.createFKBestellungKey(
													(Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_DEBITORENKONTO_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
							InternalFrameFinanz ifAB = (InternalFrameFinanz) LPMain
									.getInstance()
									.getDesktop()
									.holeModul(
											LocaleFac.BELEGART_FINANZBUCHHALTUNG);
							ifAB.geheZu(
									InternalFrameFinanz.IDX_TABBED_PANE_DEBITORENKONTEN,
									TabbedPaneKonten.iDX_KONTEN, oKey, null,
									FinanzFilterFactory.getInstance()
											.createFKKontoKey((Integer) oKey));
						}
					} else if (iWhereToGo == GOTO_KREDITORENKONTO_AUSWAHL) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
							InternalFrameFinanz ifAB = (InternalFrameFinanz) LPMain
									.getInstance()
									.getDesktop()
									.holeModul(
											LocaleFac.BELEGART_FINANZBUCHHALTUNG);
							ifAB.geheZu(
									InternalFrameFinanz.IDX_TABBED_PANE_KREDITORENKONTEN,
									TabbedPaneKonten.iDX_KONTEN, oKey, null,
									FinanzFilterFactory.getInstance()
											.createFKKontoKey((Integer) oKey));
						}
					}
				} catch (Throwable t) {
					LPMain.getInstance().exitClientNowErrorDlg(t);
				}
			}
		}

		if (dialogToClose != null) {
			dialogToClose.setVisible(false);
			dialogToClose.dispose();
		}

	}

	protected void gotoBuchungdetail() throws Throwable, ExceptionLP {
		if (!LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG))
			return;

		InternalFrameFinanz ifFinanz = (InternalFrameFinanz) LPMain
				.getInstance().getDesktop()
				.holeModul(LocaleFac.BELEGART_FINANZBUCHHALTUNG);

		BuchungdetailDto bdDto = DelegateFactory.getInstance()
				.getBuchenDelegate()
				.buchungdetailFindByPrimaryKey((Integer) oKey);

		KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(bdDto.getKontoIId());

		if(null == kvpKontoTypPanel) {
			kvpKontoTypPanel = new HashMap<String, Integer>();
			kvpKontoTypPanel.put(FinanzServiceFac.KONTOTYP_DEBITOR,
					InternalFrameFinanz.IDX_TABBED_PANE_DEBITORENKONTEN);
			kvpKontoTypPanel.put(FinanzServiceFac.KONTOTYP_KREDITOR,
					InternalFrameFinanz.IDX_TABBED_PANE_KREDITORENKONTEN);
			kvpKontoTypPanel.put(FinanzServiceFac.KONTOTYP_SACHKONTO,
					InternalFrameFinanz.IDX_TABBED_PANE_SACHKONTEN);
		}
		Integer panelIndex = kvpKontoTypPanel.get(kontoDto.getKontotypCNr());
		if (null != panelIndex) {
			ifFinanz.geheZu(panelIndex, TabbedPaneKonten.iDX_BUCHUNGEN,
					kontoDto.getIId(), bdDto.getIId(), FinanzFilterFactory
							.getInstance().createFKKontoKey(kontoDto.getIId()));
		}

		// if
		// (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR))
		// {
		// ifFinanz.geheZu(
		// InternalFrameFinanz.IDX_TABBED_PANE_DEBITORENKONTEN,
		// TabbedPaneKontenDebitorenkonten.iDX_BUCHUNGEN,
		// kontoDto.getIId(),
		// bdDto.getIId(),
		// FinanzFilterFactory.getInstance()
		// .createFKKontoKey(
		// (Integer) kontoDto
		// .getIId()));
		// } else if
		// (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_KREDITOR))
		// {
		// ifFinanz.geheZu(
		// InternalFrameFinanz.IDX_TABBED_PANE_KREDITORENKONTEN,
		// TabbedPaneKontenKreditorenkonten.iDX_BUCHUNGEN,
		// kontoDto.getIId(),
		// bdDto.getIId(),
		// FinanzFilterFactory.getInstance()
		// .createFKKontoKey(
		// (Integer) kontoDto
		// .getIId()));
		// } else if
		// (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO))
		// {
		// ifFinanz.geheZu(
		// InternalFrameFinanz.IDX_TABBED_PANE_SACHKONTEN,
		// TabbedPaneKontenSachkonten.iDX_BUCHUNGEN,
		// kontoDto.getIId(),
		// bdDto.getIId(),
		// FinanzFilterFactory.getInstance()
		// .createFKKontoKey(
		// (Integer) kontoDto
		// .getIId()));
		// }
		//
	}

	public void setOKey(Object oKey) {
		this.oKey = oKey;
		// der Goto-Button darf immer dann enabled sein, wenn ein Key vorhanden
		// ist.
		buttonGoto.setEnabled(oKey != null);
	}

	public Object getOKey() {
		return oKey;
	}

	public boolean requestFocusInWindow() {
		return getWrapperButton().requestFocusInWindow();
	}

	public void setRechtCNr(String rechtCNr) {
		getWrapperButton().setRechtCNr(rechtCNr);

	}

	public void setMnemonic(int toSet) {
		wrapperButton.setMnemonic(toSet);
	}

	public void setMnemonic(char toSet) {
		wrapperButton.setMnemonic(toSet);
	}
	

	@Override
	public boolean hasContent() throws Throwable {
		return true;
	}
}
