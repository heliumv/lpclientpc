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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.lp.client.anfrage.InternalFrameAnfrage;
import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.InternalFramePartner;
import com.lp.client.pc.LPMain;
import com.lp.client.reklamation.InternalFrameReklamation;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.system.DialogExtraliste;
import com.lp.client.system.ReportExtraliste;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.ExtralisteDto;
import com.lp.util.Helper;

public class WrapperMenuBar extends JMenuBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MENU_MODUL = 0;
	public static final int MENU_BEARBEITEN = 1;
	public static final int MENU_JOURNAL = 2;
	private boolean bExtrasAdded = false;
	private TabbedPane tp = null;
	public static String DOKUMENTENLINK_ACTION = "DOKUMENTENLINK_ACTION";
	private String EXTRALISTE_ACTION = "EXTRALISTE_ACTION";

	public static final String ACTION_BEENDEN = "action_beenden";

	public WrapperMenuBar(TabbedPane tabbedPane) throws Throwable {
		// 03.09.07 MB: das Menue erhaelt immer den Namen des Tabs
		JMenu menuModul = new JMenu(tabbedPane.getSAddTitle());
		// Namen des Menus wird auf "menu_"+name des tabs gesetz.
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menuModul.setName(HelperClient.COMP_PRAEFIX_MENU
					+ tabbedPane.getName().replaceFirst("tabbedPane", ""));
		}

		tp = tabbedPane;

		WrapperMenuItem menueItemBeenden = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.beenden"), null);
		menueItemBeenden.setActionCommand(ACTION_BEENDEN);
		menueItemBeenden.setToolTipText(LPMain
				.getTextRespectUISPr("lp.beenden"));
		menueItemBeenden.addActionListener(tabbedPane.getInternalFrame());
		menuModul.add(menueItemBeenden);

		JMenu menuBearbeiten = new WrapperMenu("lp.bearbeiten", tabbedPane);
		JMenu menuJournal = new WrapperMenu("lp.journal", tabbedPane);

		add(menuModul);

		if (tabbedPane.getInternalFrame().getTabbedPaneRoot()
				.getSelectedIndex() == 0) {
			// Dokumentenlinks
			DokumentenlinkDto[] dokumentenlinkDtos = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(
							tabbedPane.getInternalFrame().getBelegartCNr(),
							false);

			if (dokumentenlinkDtos.length > 0) {
				JMenu menueDokumentenlink = new JMenu(
						LPMain.getTextRespectUISPr("system.dokumentenlink"));
				menueDokumentenlink.setToolTipText(LPMain
						.getTextRespectUISPr("system.dokumentenlink"));
				menuModul.add(menueDokumentenlink, 0);

				for (int i = 0; i < dokumentenlinkDtos.length; i++) {
					JMenuItem menueItemLink = new JMenuItem(
							dokumentenlinkDtos[i].getCMenuetext());
					menueItemLink.setActionCommand(DOKUMENTENLINK_ACTION
							+ dokumentenlinkDtos[i].getIId());
					menueItemLink.setToolTipText(dokumentenlinkDtos[i]
							.getCMenuetext());
					menueItemLink.addActionListener(this);
					menueDokumentenlink.add(menueItemLink, i);
				}

			}
		}

		add(menuBearbeiten);
		add(menuJournal);

		// Wenn in LP_EXTRALISTE Eintraege vorhanden sind

		if (tabbedPane.getInternalFrame().getTabbedPaneRoot()
				.getSelectedIndex() == 0) {

			ExtralisteDto[] dtos = null;
			try {
				dtos = DelegateFactory
						.getInstance()
						.getSystemDelegate()
						.extralisteFindByBelegartCNr(
								tabbedPane.getInternalFrame().getBelegartCNr());
			} catch (Throwable ex) {
				/**
				 * @todo handle exception
				 */
				ex.printStackTrace();
			}

			if (dtos != null && dtos.length > 0) {

				JMenu menuExtras = new WrapperMenu("lp.extras", tabbedPane);

				for (int i = 0; i < dtos.length; i++) {
					JMenuItem menueItemExtras = new JMenuItem(dtos[i].getCBez());
					menueItemExtras.setActionCommand(EXTRALISTE_ACTION
							+ dtos[i].getIId());
					menueItemExtras.setToolTipText(dtos[i].getCBez());
					menueItemExtras.addActionListener(this);
					menuExtras.add(menueItemExtras);
				}
				add(menuExtras);
				bExtrasAdded = true;
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().startsWith(DOKUMENTENLINK_ACTION)) {
				String modulPlatzhalter = null;
				if (tp.getInternalFrame() instanceof InternalFrameArtikel) {
					modulPlatzhalter = ((InternalFrameArtikel) tp
							.getInternalFrame()).getArtikelDto().getCNr();
				}

				if (tp.getInternalFrame() instanceof InternalFrameStueckliste) {
					modulPlatzhalter = ((InternalFrameStueckliste) tp
							.getInternalFrame()).getStuecklisteDto()
							.getArtikelDto().getCNr();
				}

				if (tp.getInternalFrame() instanceof InternalFrameAuftrag) {
					modulPlatzhalter = ((InternalFrameAuftrag) tp
							.getInternalFrame()).getTabbedPaneAuftrag()
							.getAuftragDto().getCNr();
				}

				if (tp.getInternalFrame() instanceof InternalFrameAngebot) {
					modulPlatzhalter = ((InternalFrameAngebot) tp
							.getInternalFrame()).getTabbedPaneAngebot()
							.getAngebotDto().getCNr();
				}

				if (tp.getInternalFrame() instanceof InternalFrameAnfrage) {
					modulPlatzhalter = ((InternalFrameAnfrage) tp
							.getInternalFrame()).getTabbedPaneAnfrage()
							.getAnfrageDto().getCNr();
				}
				if (tp.getInternalFrame() instanceof InternalFrameFertigung) {
					modulPlatzhalter = ((InternalFrameFertigung) tp
							.getInternalFrame()).getTabbedPaneLos().getLosDto()
							.getCNr();
				}
				if (tp.getInternalFrame() instanceof InternalFrameReklamation) {
					modulPlatzhalter = ((InternalFrameReklamation) tp
							.getInternalFrame()).getReklamationDto().getCNr();
				}
				if (tp.getInternalFrame() instanceof InternalFrameKunde) {
					modulPlatzhalter = ((InternalFrameKunde) tp
							.getInternalFrame()).getKundeDto().getPartnerDto()
							.getCKbez();
				}
				if (tp.getInternalFrame() instanceof InternalFramePartner) {
					modulPlatzhalter = ((InternalFramePartner) tp
							.getInternalFrame()).getTpPartner().getPartnerDto()
							.getCKbez();
				}
				if (tp.getInternalFrame() instanceof InternalFrameLieferant) {
					modulPlatzhalter = ((InternalFrameLieferant) tp
							.getInternalFrame()).getLieferantDto()
							.getPartnerDto().getCKbez();
				}

				if (modulPlatzhalter != null) {
					String[] charFrom = new String[] { "\u00E4", "\u00C4", "\u00F6", "\u00D6", "\u00FC",
							"\u00DC", "\u00DF" };
					String[] charTo = new String[] { "ae", "Ae", "oe", "Oe",
							"ue", "Ue", "ss" };

					for (int i = 0; i < charFrom.length; i++)
						modulPlatzhalter = modulPlatzhalter.replaceAll(
								charFrom[i], charTo[i]);

					modulPlatzhalter = modulPlatzhalter.replaceAll(
							"[^a-zA-Z0-9-.]", "_");

					Integer dokumentenlinkIId = new Integer(e
							.getActionCommand().replaceAll(
									DOKUMENTENLINK_ACTION, ""));
					DokumentenlinkDto dokumentenlinkDto = DelegateFactory
							.getInstance().getMandantDelegate()
							.dokumentenlinkFindByPrimaryKey(dokumentenlinkIId);
					String pfad = dokumentenlinkDto.getCBasispfad();
					pfad = pfad + modulPlatzhalter;
					if (dokumentenlinkDto.getCOrdner() != null) {
						pfad += dokumentenlinkDto.getCOrdner();
					}

					if (Helper.short2boolean(dokumentenlinkDto.getBUrl())) {
						try {
							int i = pfad.indexOf("://");
							URI uri = new URI((i < 0 ? "http://" : "")
									+ pfad.trim());
							java.awt.Desktop.getDesktop().browse(uri);
						} catch (URISyntaxException ex1) {
							DialogFactory.showModalDialog(LPMain
									.getTextRespectUISPr("lp.error"), LPMain
									.getTextRespectUISPr("lp.fehlerhafteurl"));
						} catch (IOException ex1) {
							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									ex1.getMessage());
						}
					} else {
						try {
							java.io.File f = new File(pfad);
							if (!f.exists()) {
								new File(pfad).mkdirs();
								f = new File(pfad);
							}

							try {
								java.awt.Desktop.getDesktop().open(f);
							} catch (Exception e1) {
								String message = "Dokumentenpfad: "
										+ f.toString();
								LpLogger.getInstance(this.getClass()).warn(
										message, e1);
							}
						} catch (java.lang.IllegalArgumentException e1) {
							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									e1.getMessage());
						}
					}

				}
			} else if (e.getActionCommand().startsWith(EXTRALISTE_ACTION)) {
				Integer extralisteIId=new Integer(e
						.getActionCommand().replaceAll(EXTRALISTE_ACTION, ""));
				DialogExtraliste d = new DialogExtraliste(extralisteIId);

				if (d.bPrint == true) {
					tp.getInternalFrame().showReportKriterien(
							new ReportExtraliste(tp.getInternalFrame(), "",
									d.extralisteRueckgabeTabelleDto,extralisteIId));
				}
			}

		} catch (Throwable ex) {
			// Was tun?
			ex.printStackTrace();
			String message = "DOKUMENTENLINK_ACTION";
			LpLogger.getInstance(this.getClass()).warn(message, ex);
		}
	}

	/**
	 * Fuegt einen Menuepunkt zwischen dem Hilfemenue und dem vorletzten
	 * Menuepunkt ein.
	 * 
	 * @param jmenu
	 *            JMenu
	 */
	public void addJMenuItem(JMenu jmenu) {
		if (bExtrasAdded) {
			add(jmenu, getComponentCount() - 1);
		} else {
			add(jmenu, getComponentCount());
		}
	}
}
