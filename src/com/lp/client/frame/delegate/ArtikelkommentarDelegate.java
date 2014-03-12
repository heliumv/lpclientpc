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
package com.lp.client.frame.delegate;

import java.util.ArrayList;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.SperrenDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ArtikelkommentarDelegate extends Delegate {
	private Context context;
	private ArtikelkommentarFac artikelkommentarFac;

	public ArtikelkommentarDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			artikelkommentarFac = (ArtikelkommentarFac) context
					.lookup("lpserver/ArtikelkommentarFacBean/remote");
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}

	}

	public Integer createArtikelkommentar(
			ArtikelkommentarDto artikelkommentarDto) throws ExceptionLP {
		try {
			return artikelkommentarFac.createArtikelkommentar(
					artikelkommentarDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean gibtEsKommentareInAnderenSprachen(Integer artikelkommentarIId)
			throws ExceptionLP {
		try {
			return artikelkommentarFac.gibtEsKommentareInAnderenSprachen(
					artikelkommentarIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}
	}

	public Integer createArtikelkommentardruck(
			ArtikelkommentardruckDto artikelkommentardruckDto)
			throws ExceptionLP {
		try {
			return artikelkommentarFac
					.createArtikelkommentardruck(artikelkommentardruckDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikelkommentarart(
			ArtikelkommentarartDto artikelkommentarartDto) throws ExceptionLP {
		try {
			return artikelkommentarFac
					.createArtikelkommentarart(artikelkommentarartDto, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeArtikelkommentar(ArtikelkommentarDto dto)
			throws ExceptionLP {
		try {
			artikelkommentarFac.removeArtikelkommentar(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikelkommentardruck(ArtikelkommentardruckDto dto)
			throws ExceptionLP {
		try {
			artikelkommentarFac.removeArtikelkommentardruck(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikelkommentarart(ArtikelkommentarartDto dto)
			throws ExceptionLP {
		try {
			artikelkommentarFac.removeArtikelkommentarart(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikelkommentar(ArtikelkommentarDto artikelkommentarDto)
			throws ExceptionLP {
		try {
			artikelkommentarFac.updateArtikelkommentar(artikelkommentarDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikelkommentardruck(
			ArtikelkommentardruckDto artikelkommentardruckDto)
			throws ExceptionLP {
		try {
			artikelkommentarFac
					.updateArtikelkommentardruck(artikelkommentardruckDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikelkommentarart(
			ArtikelkommentarartDto artikelkommentarartDto) throws ExceptionLP {
		try {
			artikelkommentarFac
					.updateArtikelkommentarart(artikelkommentarartDto, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheArtikelkommentar(Integer iiD1, Integer iId2)
			throws ExceptionLP {
		try {
			artikelkommentarFac.vertauscheArtikelkommentar(iiD1, iId2);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public ArtikelkommentarDto artikelkommentarFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return artikelkommentarFac.artikelkommentarFindByPrimaryKey(iId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelkommentardruckDto artikelkommentardruckFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return artikelkommentarFac
					.artikelkommentardruckFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelkommentarDto[] artikelkommentardruckFindByArtikelIIdBelegartCNr(
			Integer artikelIId, String localeCNr, String belegartCNr)
			throws ExceptionLP {
		try {
			return artikelkommentarFac
					.artikelkommentardruckFindByArtikelIIdBelegartCNr(
							artikelIId, belegartCNr, localeCNr, LPMain
									.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelkommentarDto artikelkommentarFindByPrimaryKeyUndLocale(
			Integer iId, String localeCNr, TheClientDto theClientDto)
			throws ExceptionLP {

		try {
			return artikelkommentarFac
					.artikelkommentarFindByPrimaryKeyUndLocale(iId, localeCNr,
							theClientDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelDto pruefeArtikel(ArtikelDto artikelDto, String belegartCNr,
			InternalFrame internalFrame) throws ExceptionLP {

		try {

			// Artikelhinweise anzeigen
			String[] hinweise = artikelkommentarFac.getArtikelhinweise(
					artikelDto.getIId(), belegartCNr, LPMain.getInstance()
							.getTheClient());
			for (int i = 0; i < hinweise.length; i++) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hinweis"), Helper
						.strippHTML(hinweise[i]));
			}

			ArtikelsperrenDto[] artikelsperrenDtos = DelegateFactory
					.getInstance().getArtikelDelegate()
					.artikelsperrenFindByArtikelIId(artikelDto.getIId());

			boolean bGesperrtAllgemein = false;
			boolean bGesperrtFuerBelgart = false;
			String grund = "";

			// PJ 16944 Hinweis, wenn offene Reklamationen
			ReklamationDto[] rDtos = DelegateFactory
					.getInstance()
					.getReklamationDelegate()
					.reklamationfindOffeneReklamationenEinesArtikels(
							artikelDto.getIId());
			if (rDtos != null && rDtos.length > 0) {
				String reklas = LPMain.getInstance().getTextRespectUISPr(
						"artikel.offenereklamationen.hinweis")
						+ "\n";
				for (int i = 0; i < rDtos.length; i++) {
					reklas += rDtos[i].getCNr() + "\n";
				}
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hinweis"), reklas);
			}

			// PJ17927 Hinweis, wenn Artikel gewartet werden muss
			if (artikelDto.getIWartungsintervall() != null) {

				if (artikelDto.getTLetztewartung() == null) {

					DialogFactory.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr(
									"lp.hinweis"),
							LPMain.getInstance().getTextRespectUISPr(
									"artikel.hinweis.letztewartung"));

				} else {
					Calendar cWartungFaellig = Calendar.getInstance();
					cWartungFaellig.setTimeInMillis(artikelDto
							.getTLetztewartung().getTime());
					cWartungFaellig.add(Calendar.MONTH,
							artikelDto.getIWartungsintervall());

					if (cWartungFaellig.getTimeInMillis() < System
							.currentTimeMillis()) {

						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr(
														"lp.hinweis"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"artikel.hinweis.letztewartung.faellig")
												+ " "
												+ Helper.formatDatum(
														cWartungFaellig
																.getTime(),
														LPMain.getTheClient()
																.getLocUi()));

					}
				}
			}

			if (belegartCNr != null) {

				for (int i = 0; i < artikelsperrenDtos.length; i++) {

					SperrenDto sperrenDto = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.sperrenFindByPrimaryKey(
									artikelsperrenDtos[i].getSperrenIId());

					if (Helper.short2boolean(sperrenDto.getBGesperrt())) {
						bGesperrtAllgemein = true;
						grund += artikelsperrenDtos[i].getCGrund() + ", ";
					}

					if (Helper.short2boolean(sperrenDto.getBGesperrteinkauf())) {
						if (LocaleFac.BELEGART_ANFRAGE.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}

						} else if (LocaleFac.BELEGART_BESTELLUNG
								.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}
						} else if (LocaleFac.BELEGART_EINGANGSRECHNUNG
								.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}
						}
					}

					if (Helper.short2boolean(sperrenDto.getBGesperrtverkauf())) {
						if (LocaleFac.BELEGART_AGSTUECKLISTE
								.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}
						} else if (LocaleFac.BELEGART_ANGEBOT
								.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}
						} else if (LocaleFac.BELEGART_AUFTRAG
								.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}
						} else if (LocaleFac.BELEGART_LIEFERSCHEIN
								.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}
						} else if (LocaleFac.BELEGART_RECHNUNG
								.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}
						}
					}

					if (Helper.short2boolean(sperrenDto.getBGesperrtlos())) {
						if (LocaleFac.BELEGART_LOS.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}
						}
					}

					if (Helper.short2boolean(sperrenDto
							.getBGesperrtstueckliste())) {
						if (LocaleFac.BELEGART_STUECKLISTE.equals(belegartCNr)) {
							bGesperrtFuerBelgart = true;
							if (bGesperrtAllgemein == false) {
								grund += artikelsperrenDtos[i].getCGrund()
										+ ", ";
							}

						}
					}

				}
			}
			if (bGesperrtAllgemein == true) {

				if (artikelDto.getArtikelIIdErsatz() != null) {
					ArtikelDto ersatz_artikelDto = DelegateFactory
							.getInstance().getArtikelDelegate()
							.getErsatzartikel(artikelDto.getIId());
					// Dialog Ersatzartikel verwenden

					final String sErsatz = LPMain
							.getTextRespectUISPr("artikel.option.ersatz");
					final String sAlten = LPMain
							.getTextRespectUISPr("artikel.option.alten");
					Object[] options = { sErsatz, sAlten, };
					JOptionPane pane = InternalFrame
							.getNarrowOptionPane(Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
					pane.setMessage(LPMain.getInstance().getTextRespectUISPr(
							"artikel.error.gesperrt.ersatz")
							+ " ("
							+ ersatz_artikelDto.formatArtikelbezeichnung()
							+ ")");
					pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
					pane.setOptions(options);
					pane.setInitialValue(sErsatz);
					JDialog dialog = pane.createDialog(internalFrame, LPMain
							.getInstance().getTextRespectUISPr("lp.frage"));
					pane.selectInitialValue();
					dialog.setVisible(true);

					if (pane.getValue() != null) {

						// Wenn Ja, dann artikelDto mit ersatz_artikelDto
						// tauschen
						if (pane.getValue().equals(sErsatz) == true) {
							artikelDto = ersatz_artikelDto;
						} else {
							return artikelDto;
						}
					} else {
						return null;
					}
				} else {
					// Dialog Hinweis gesperrt / Trotzdem verwenden JA/NEIN
					boolean bAnswer = DialogFactory.showModalJaNeinDialog(
							internalFrame,
							LPMain.getInstance().getTextRespectUISPr(
									"artikel.error.gesperrt")
									+ "\n"
									+ LPMain.getInstance().getTextRespectUISPr(
											"lp.grund") + ": " + grund,
							LPMain.getInstance().getTextRespectUISPr(
									"lp.hinweis"));
					if (bAnswer) {
						return artikelDto;
					} else {
						return null;
					}
				}
			} else {
				if (bGesperrtFuerBelgart == true) {
					// Dialog Hinweis gesperrt fuer belegart / Trotzdem verwenden
					// JA/NEIN
					boolean bAnswer = DialogFactory.showModalJaNeinDialog(
							internalFrame,
							LPMain.getInstance().getTextRespectUISPr(
									"artikel.error.gesperrt")
									+ "\n"
									+ LPMain.getInstance().getTextRespectUISPr(
											"lp.grund") + ": " + grund,
							LPMain.getInstance().getTextRespectUISPr(
									"lp.hinweis"));

					if (bAnswer) {
						return artikelDto;
					} else {
						return null;
					}

				}
			}

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return artikelDto;
	}

	public String[] getArtikelhinweise(Integer artikelIId, String belegartCNr)
			throws ExceptionLP {

		try {
			return artikelkommentarFac.getArtikelhinweise(artikelIId,
					belegartCNr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public ArrayList<byte[]> getArtikelhinweiseBild(Integer artikelIId,
			String belegartCNr) throws ExceptionLP {

		try {
			return artikelkommentarFac.getArtikelhinweiseBild(artikelIId,
					belegartCNr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public byte[] getArtikeldefaultBild(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelkommentarFac.getArtikeldefaultBild(artikelIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<byte[]> getArtikelBilder(Integer artikelIId)
			throws ExceptionLP {
		try {
			return artikelkommentarFac.getArtikelBilder(artikelIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelkommentarartDto artikelkommentarartFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return artikelkommentarFac.artikelkommentarartFindByPrimaryKey(iId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
