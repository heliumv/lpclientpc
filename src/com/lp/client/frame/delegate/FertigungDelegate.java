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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.fertigung.DialogStklGeaendert;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.SperrenDto;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.InternebestellungDto;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.fertigung.service.KapazitaetsvorschauDto;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LoslosklasseDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LostechnikerDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.WiederholendeloseDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * BusinessDelegate fuer die Fertigung
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 06.10.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/30 10:23:46 $
 */
public class FertigungDelegate extends Delegate {
	private Context context;
	private FertigungFac fertigungFac;
	private FertigungReportFac fertigungReportFac;
	private InternebestellungFac internebestellungFac;

	public FertigungDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			fertigungFac = (FertigungFac) context
					.lookup("lpserver/FertigungFacBean/remote");
			fertigungReportFac = (FertigungReportFac) context
					.lookup("lpserver/FertigungReportFacBean/remote");
			internebestellungFac = (InternebestellungFac) context
					.lookup("lpserver/InternebestellungFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeLos(LosDto losDto) throws ExceptionLP {
		try {
			fertigungFac.removeLos(losDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeWiederholendelose(
			WiederholendeloseDto wiederholendeloseDto) throws ExceptionLP {
		try {
			fertigungFac.removeWiederholendelose(wiederholendeloseDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void terminVerschieben(Integer losIId, Timestamp tsBeginnNeu,
			Timestamp tsEndeNeu) throws ExceptionLP {
		try {
			fertigungFac.terminVerschieben(losIId, tsBeginnNeu, tsEndeNeu,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public int loseAusAuftragAnlegen(
			ArrayList<LosAusAuftragDto> losAusAuftragDto, Integer auftragIId)
			throws ExceptionLP {
		try {
			return fertigungFac.loseAusAuftragAnlegen(losAusAuftragDto,
					auftragIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return 0;
		}
	}

	public void removeZusatzstatus(ZusatzstatusDto zusatzstatusDto)
			throws ExceptionLP {
		try {
			fertigungFac.removeZusatzstatus(zusatzstatusDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void alleLoseEinerStuecklisteNachkalkulieren(String artikelnummer,
			String sAbErledigtdatum) throws ExceptionLP {
		try {
			fertigungFac.alleLoseEinerStuecklisteNachkalkulieren(artikelnummer,
					sAbErledigtdatum, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LossollmaterialDto[] lossollmaterialFindyByLosIIdArtikelIId(
			Integer losIId, Integer artikelIId) throws ExceptionLP {
		try {
			return fertigungFac.lossollmaterialFindyByLosIIdArtikelIId(losIId,
					artikelIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto)
			throws ExceptionLP {
		try {
			fertigungFac.removeLoszusatzstatus(loszusatzstatusDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public ArrayList<LosAusAuftragDto> vorschlagMitUnterlosenAusAuftrag(
			Integer auftragIId) throws ExceptionLP {
		try {
			return fertigungFac.vorschlagMitUnterlosenAusAuftrag(auftragIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void manuellErledigen(Integer losIId,
			boolean bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden)
			throws ExceptionLP {
		try {
			fertigungFac.manuellErledigen(losIId,
					bDatumDerLetztenAblieferungAlsErledigtDatumVerwenden,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateBewertungKommentarImLos(LosDto losDto) throws ExceptionLP {
		try {
			fertigungFac.updateBewertungKommentarImLos(losDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void aendereLosgroesse(Integer losIId, Integer neueLosgroesse,
			boolean bUeberzaehligesMaterialZurueckgeben) throws ExceptionLP {
		try {
			fertigungFac.aendereLosgroesse(losIId, neueLosgroesse,
					bUeberzaehligesMaterialZurueckgeben, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LosDto updateLos(LosDto losDto) throws ExceptionLP {
		try {
			if (losDto.getIId() != null) {
				return fertigungFac.updateLos(losDto, LPMain.getTheClient());
			} else {
				return fertigungFac.createLos(losDto, LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void updateZusatzstatus(ZusatzstatusDto zusatzstatusDto)
			throws ExceptionLP {
		try {
			fertigungFac.updateZusatzstatus(zusatzstatusDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto)
			throws ExceptionLP {
		try {
			fertigungFac.updateLoszusatzstatus(loszusatzstatusDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLosgutschlecht(LosgutschlechtDto losgutschlechtDto)
			throws ExceptionLP {
		try {
			fertigungFac.updateLosgutschlecht(losgutschlechtDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Integer createWiederholendelose(
			WiederholendeloseDto wiederholendeloseDto) throws ExceptionLP {
		try {
			return fertigungFac.createWiederholendelose(wiederholendeloseDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer createZusatzstatus(ZusatzstatusDto zusatzstatusDto)
			throws ExceptionLP {
		try {
			return fertigungFac.createZusatzstatus(zusatzstatusDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer createLoszusatzstatus(LoszusatzstatusDto loszusatzstatusDto)
			throws ExceptionLP {
		try {
			return fertigungFac.createLoszusatzstatus(loszusatzstatusDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void updateWiederholendelose(
			WiederholendeloseDto wiederholendeloseDto) throws ExceptionLP {
		try {
			fertigungFac.updateWiederholendelose(wiederholendeloseDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLosKommentar(Integer losIId, String xText)
			throws ExceptionLP {
		try {
			fertigungFac.updateLosKommentar(losIId, xText);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLosProduktionsinformation(Integer losIId,
			String xProduktionsinformation) throws ExceptionLP {
		try {
			fertigungFac.updateLosProduktionsinformation(losIId,
					xProduktionsinformation);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LosDto losFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return fertigungFac.losFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public BigDecimal getMengeDerLetztenLosablieferungEinerAuftragsposition(
			Integer auftragIId, Integer artikelIId) throws ExceptionLP {
		try {
			return fertigungFac
					.getMengeDerLetztenLosablieferungEinerAuftragsposition(
							auftragIId, artikelIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public ZusatzstatusDto zusatzstatusFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.zusatzstatusFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LoszusatzstatusDto loszusatzstatusFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.loszusatzstatusFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public WiederholendeloseDto wiederholendeloseFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.wiederholendeloseFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LosDto losFindByCNrMandantCNr(String losCNr, String mandantCNr)
			throws ExceptionLP {
		try {
			return fertigungFac.losFindByCNrMandantCNr(losCNr, mandantCNr);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	/**
	 * storniere Los.
	 * 
	 * @param losIId
	 *            Integer
	 * @throws ExceptionLP
	 */
	public void storniereLos(Integer losIId,
			boolean bAuftragspositionsbezugEntfernen) throws ExceptionLP {
		try {
			fertigungFac.storniereLos(losIId, bAuftragspositionsbezugEntfernen,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * storniere Los rueckgaengig.
	 * 
	 * @param losIId
	 *            Integer
	 * @throws ExceptionLP
	 */
	public void storniereLosRueckgaengig(Integer losIId) throws ExceptionLP {
		try {
			fertigungFac
					.storniereLosRueckgaengig(losIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void manuellErledigenRueckgaengig(Integer losIId) throws ExceptionLP {
		try {
			fertigungFac.manuellErledigenRueckgaengig(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public JasperPrintLP printFehlerstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer iSortierung) throws ExceptionLP {
		try {
			return fertigungReportFac.printFehlerstatistik(tVon, tBis,
					iSortierung, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	/**
	 * gebe Los aus.
	 * 
	 * @param losIId
	 *            Integer
	 * @param bHandausgabe
	 *            boolean
	 * @throws ExceptionLP
	 */
	public void gebeLosAus(Integer losIId, boolean bHandausgabe,
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos)
			throws ExceptionLP {
		try {

			LosDto losDto = fertigungFac.losFindByPrimaryKey(losIId);

			if (losDto.getStuecklisteIId() != null) {

				StuecklisteDto stklDto = DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());

				String[] hinweise = DelegateFactory
						.getInstance()
						.getArtikelkommentarDelegate()
						.getArtikelhinweise(stklDto.getArtikelIId(),
								LocaleFac.BELEGART_LOS);
				if (hinweise != null) {
					for (int i = 0; i < hinweise.length; i++) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								Helper.strippHTML(hinweise[i]));
					}
				}

				ArtikelsperrenDto[] artikelsperrenDtos = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelsperrenFindByArtikelIId(stklDto.getArtikelIId());

				for (int i = 0; i < artikelsperrenDtos.length; i++) {
					SperrenDto sperrenDto = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.sperrenFindByPrimaryKey(
									artikelsperrenDtos[i].getSperrenIId());

					if (Helper.short2boolean(sperrenDto.getBGesperrt())
							|| Helper.short2boolean(sperrenDto
									.getBGesperrtlos())) {

						boolean b = DialogFactory
								.showModalJaNeinDialog(
										null,
										LPMain.getTextRespectUISPr("fertl.los.ausgabe.stklgesperrt")
												+ "\n"
												+ LPMain.getTextRespectUISPr("lp.grund")
												+ ": "
												+ artikelsperrenDtos[i]
														.getCGrund());
						if (b == false) {
							return;
						}

					}
				}

			}

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_FEHLMENGE_ENTSTEHEN_WARNUNG,
							ParameterFac.KATEGORIE_FERTIGUNG,
							LPMain.getTheClient().getMandant());
			if ((Boolean) parameter.getCWertAsObject()) {
				try {
					fertigungFac.gebeLosAus(losIId, bHandausgabe, true,
							LPMain.getTheClient(), bucheSerienChnrAufLosDtos);
				} catch (EJBExceptionLP e) {
					if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN) {

						boolean bAntwort = DialogFactory
								.showModalJaNeinDialog(
										null,
										LPMain.getTextRespectUISPr("fert.ausgabe.fehlmengenentstehen"));
						if (bAntwort == true) {
							fertigungFac.gebeLosAus(losIId, bHandausgabe,
									false, LPMain.getTheClient(),
									bucheSerienChnrAufLosDtos);
						} else {
							return;
						}

					} else {
						throw e;
					}
				}
			} else {

				fertigungFac.gebeLosAus(losIId, bHandausgabe, false,
						LPMain.getTheClient(), bucheSerienChnrAufLosDtos);

			}

			// PJ13767
			DelegateFactory.getInstance().getFertigungDelegate()
					.zeigeArtikelhinweiseAllerLossollpositionen(losIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void gebeMehrereLoseAus(Integer fertigungsgruppeIId)
			throws ExceptionLP {
		try {
			String lose = fertigungFac.gebeMehrereLoseAus(fertigungsgruppeIId,
					true, true, LPMain.getTheClient());

			if (lose != null && lose.length() > 0) {

				DialogStklGeaendert d = new DialogStklGeaendert(lose);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

				if (d.bOK == false) {
					return;
				}

			}
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_FEHLMENGE_ENTSTEHEN_WARNUNG,
							ParameterFac.KATEGORIE_FERTIGUNG,
							LPMain.getTheClient().getMandant());
			if ((Boolean) parameter.getCWertAsObject()) {

				try {
					fertigungFac.gebeMehrereLoseAus(fertigungsgruppeIId, true,
							false, LPMain.getTheClient());
				} catch (EJBExceptionLP e) {
					if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN) {

						boolean bAntwort = DialogFactory
								.showModalJaNeinDialog(
										null,
										LPMain.getTextRespectUISPr("fert.ausgabe.fehlmengenentstehen"));
						if (bAntwort == true) {
							fertigungFac.gebeMehrereLoseAus(
									fertigungsgruppeIId, false, false,
									LPMain.getTheClient());
						} else {
							return;
						}
					} else {
						throw e;
					}
				}

			} else {
				fertigungFac.gebeMehrereLoseAus(fertigungsgruppeIId, false,
						false, LPMain.getTheClient());
			}

		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void perAuftragsnummerLoseAbliefern(Integer auftragIId)
			throws ExceptionLP {
		try {
			fertigungFac.perAuftragsnummerLoseAbliefern(auftragIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * gebe Los aus rueckgaengig.
	 * 
	 * @param losIId
	 *            Integer
	 * @throws ExceptionLP
	 */
	public void gebeLosAusRueckgaengig(Integer losIId) throws ExceptionLP {
		try {
			fertigungFac.gebeLosAusRueckgaengig(losIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * gebe Los aus rueckgaengig.
	 * 
	 * @param losIId
	 *            Integer
	 * @throws ExceptionLP
	 */
	public int wiederholendeLoseAnlegen() throws ExceptionLP {
		try {
			return fertigungFac.wiederholendeLoseAnlegen(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return 0;
		}
	}

	public void removeLossollmaterial(LossollmaterialDto lossollmaterialDto)
			throws ExceptionLP {
		try {
			fertigungFac.removeLossollmaterial(lossollmaterialDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LossollmaterialDto updateLossollmaterial(
			LossollmaterialDto lossollmaterialDto) throws ExceptionLP {
		try {
			if (lossollmaterialDto.getIId() == null) {
				return fertigungFac.createLossollmaterial(lossollmaterialDto,
						LPMain.getTheClient());
			} else {
				return fertigungFac.updateLossollmaterial(lossollmaterialDto,
						LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public String verknuepfungZuBestellpositionUndArbeitsplanDarstellen(
			Integer lossollmaterialIId) throws ExceptionLP {
		try {

			return fertigungFac
					.verknuepfungZuBestellpositionUndArbeitsplanDarstellen(
							lossollmaterialIId, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LossollmaterialDto lossollmaterialFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.lossollmaterialFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LosistmaterialDto losistmaterialFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.losistmaterialFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public ArrayList<String> getOffeneGeraeteSnrEinerSollPosition(
			Integer lossollmaterialIId) throws ExceptionLP {
		try {
			return fertigungFac.getOffeneGeraeteSnrEinerSollPosition(
					lossollmaterialIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LosistmaterialDto[] losistmaterialFindByLossollmaterialIId(
			Integer sollmaterialIId) throws ExceptionLP {
		try {
			return fertigungFac
					.losistmaterialFindByLossollmaterialIId(sollmaterialIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void updateLosistmaterialMenge(Integer losistmaterialIId,
			BigDecimal bdMengeNeu) throws ExceptionLP {
		try {
			fertigungFac.updateLosistmaterialMenge(losistmaterialIId,
					bdMengeNeu, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LossollmaterialDto[] lossollmaterialFindByLosIId(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungFac.lossollmaterialFindByLosIId(losIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto) throws ExceptionLP {
		try {
			fertigungFac.removeLossollarbeitsplan(lossollarbeitsplanDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LossollarbeitsplanDto updateLossollarbeitsplan(
			LossollarbeitsplanDto lossollarbeitsplanDto) throws ExceptionLP {
		try {
			if (lossollarbeitsplanDto.getIId() == null) {
				return fertigungFac.createLossollarbeitsplan(
						lossollarbeitsplanDto, LPMain.getTheClient());
			} else {
				return fertigungFac.updateLossollarbeitsplan(
						lossollarbeitsplanDto, LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LossollarbeitsplanDto lossollarbeitsplanFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.lossollarbeitsplanFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
			Integer losIId, Integer artikelIIdTaetigkeit) throws ExceptionLP {
		try {
			return fertigungFac
					.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(losIId,
							artikelIIdTaetigkeit);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LossollarbeitsplanDto[] lossollarbeitsplanFindByLosIId(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungFac.lossollarbeitsplanFindByLosIId(losIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto)
			throws ExceptionLP {
		try {
			fertigungFac.removeLoslagerentnahme(loslagerentnahmeDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LoslagerentnahmeDto updateLoslagerentnahme(
			LoslagerentnahmeDto loslagerentnahmeDto) throws ExceptionLP {
		try {
			if (loslagerentnahmeDto.getIId() == null) {
				return fertigungFac.createLoslagerentnahme(loslagerentnahmeDto,
						LPMain.getTheClient());
			} else {
				return fertigungFac.updateLoslagerentnahme(loslagerentnahmeDto,
						LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LoslagerentnahmeDto loslagerentnahmeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.loslagerentnahmeFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LosgutschlechtDto[] losgutschlechtFindAllFehler(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungFac.losgutschlechtFindAllFehler(losIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LoslagerentnahmeDto[] loslagerentnahmeFindByLosIId(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungFac.loslagerentnahmeFindByLosIId(losIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void aktualisiereSollArbeitsplanAusStueckliste(Integer losIId)
			throws ExceptionLP {
		try {
			fertigungFac.aktualisiereSollArbeitsplanAusStueckliste(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void aktualisiereSollMaterialAusStueckliste(Integer losIId)
			throws ExceptionLP {
		try {
			fertigungFac.aktualisiereSollMaterialAusStueckliste(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public JasperPrintLP printTheoretischeFehlmengen(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printTheoretischeFehlmengen(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printAblieferEtikett(Integer losablieferungIId,
			Integer iExemplare, BigDecimal bdHandmenge) throws ExceptionLP {
		try {
			return fertigungReportFac.printAblieferEtikett(losablieferungIId,
					iExemplare, bdHandmenge, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printAusgabeListe(Integer[] losIId,
			Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId,
			String alternativerReport) throws ExceptionLP {
		try {
			return fertigungReportFac
					.printAusgabeListe(losIId, iSortierung,
							bVerdichtetNachIdent,
							bVorrangigNachFarbcodeSortiert, artikelklasseIId,
							alternativerReport, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printAblieferungsstatistik(java.sql.Date dVon,
			java.sql.Date dBis, Integer artikelIId,
			boolean bSortiertNachArtikel, boolean bVerdichtetNachArtikel)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printAblieferungsstatistik(dVon, dBis,
					artikelIId, bSortiertNachArtikel, bVerdichtetNachArtikel,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printFertigungsbegleitschein(Integer losIId,
			Boolean bStammtVonSchnellanlage) throws ExceptionLP {
		try {
			return fertigungReportFac.printFertigungsbegleitschein(losIId,
					bStammtVonSchnellanlage, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printNachkalkulation(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printNachkalkulation(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printProduktionsinformation(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printProduktionsinformation(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printLosstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer losIId, Integer stuecklisteIId,
			Integer auftragIId, boolean bArbeitsplanSortiertNachAG,
			boolean bVerdichtet, java.sql.Timestamp tStichtag)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printLosstatistik(tVon, tBis, losIId,
					stuecklisteIId, auftragIId, bArbeitsplanSortiertNachAG,
					bVerdichtet, tStichtag, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printMaterialliste(Integer losIId) throws ExceptionLP {
		try {
			return fertigungReportFac.printMaterialliste(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printRankingliste() throws ExceptionLP {
		try {
			return fertigungReportFac.printRankingliste(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printLoszeiten(Integer iIdAuftragI, int iSortierung,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printLoszeiten(iIdAuftragI, iSortierung,
					tVon, tBis, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printMonatsauswertung(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bVerdichtet) throws ExceptionLP {
		try {
			return fertigungReportFac.printMonatsauswertung(tVon, tBis,
					bVerdichtet, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printZeitentwicklung(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bSortiertNachArtikelgruppe)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printZeitentwicklung(tVon, tBis,
					bSortiertNachArtikelgruppe, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printAuslastungsvorschau(java.sql.Timestamp tStichtag,
			boolean bSortiertNachArtikelgruppe) throws ExceptionLP {
		try {
			return fertigungReportFac.printAuslastungsvorschau(tStichtag,
					bSortiertNachArtikelgruppe, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printAuslieferliste(java.sql.Timestamp tStichtag)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printAuslieferliste(tStichtag,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public BigDecimal getAusgegebeneMenge(Integer lossollmaterialIId)
			throws ExceptionLP {
		try {
			return fertigungFac.getAusgegebeneMenge(lossollmaterialIId, null,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public BigDecimal getAusgegebeneMengePreis(Integer lossollmaterialIId)
			throws ExceptionLP {
		try {
			return fertigungFac.getAusgegebeneMengePreis(lossollmaterialIId,
					null, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeLoslosklasse(LoslosklasseDto loslosklasseDto)
			throws ExceptionLP {
		try {
			fertigungFac.removeLoslosklasse(loslosklasseDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void bucheMaterialAufLos(LosDto losDto, BigDecimal nMenge,
			boolean bHandausgabe,
			boolean bNurFehlmengenAnlegenUndReservierungenLoeschen,
			boolean bUnterstuecklistenAbbuchen,
			ArrayList<BucheSerienChnrAufLosDto> bucheSerienChnrAufLosDtos)
			throws ExceptionLP {
		try {
			fertigungFac.bucheMaterialAufLos(losDto, nMenge, bHandausgabe,
					bNurFehlmengenAnlegenUndReservierungenLoeschen,
					bUnterstuecklistenAbbuchen, LPMain.getTheClient(),
					bucheSerienChnrAufLosDtos, false);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LoslosklasseDto updateLoslosklasse(LoslosklasseDto loslosklasseDto)
			throws Exception {
		try {
			if (loslosklasseDto.getIId() == null) {
				return fertigungFac.createLoslosklasse(loslosklasseDto,
						LPMain.getTheClient());
			} else {
				return fertigungFac.updateLoslosklasse(loslosklasseDto,
						LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer createLostechniker(LostechnikerDto lostechnikerDto)
			throws Exception {
		try {
			return fertigungFac.createLostechniker(lostechnikerDto,
					LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
			return null;

		}
	}

	public void updateLostechniker(LostechnikerDto lostechnikerDto)
			throws Exception {
		try {
			fertigungFac.updateLostechniker(lostechnikerDto,
					LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);

		}
	}

	public LoslosklasseDto loslosklasseFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.loslosklasseFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LostechnikerDto lostechnikerFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.lostechnikerFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeLosablieferung(Object[] loablieferungIIds)
			throws ExceptionLP {
		try {
			fertigungFac.removeLosablieferung(loablieferungIIds,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeLostechniker(LostechnikerDto lostechnikerDto)
			throws ExceptionLP {
		try {
			fertigungFac.removeLostechniker(lostechnikerDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeLosgutschlecht(LosgutschlechtDto losgutschlechtDto)
			throws ExceptionLP {
		try {
			fertigungFac.removeLosgutschlecht(losgutschlechtDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LosablieferungDto createLosablieferung(
			LosablieferungDto losablieferungDto, boolean bErledigt)
			throws ExceptionLP {
		try {
			return fertigungFac.createLosablieferung(losablieferungDto,
					LPMain.getTheClient(), bErledigt);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer createLosgutschlecht(LosgutschlechtDto losgutschlechtDto)
			throws ExceptionLP {
		try {
			return fertigungFac.createLosgutschlecht(losgutschlechtDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void vonQuelllagerUmbuchenUndAnsLosAusgeben(Integer lagerIId_Quelle,
			Integer lossollmaterialIId, BigDecimal nMenge,
			List<SeriennrChargennrMitMengeDto> l) throws ExceptionLP {
		try {
			fertigungFac.vonQuelllagerUmbuchenUndAnsLosAusgeben(
					lagerIId_Quelle, lossollmaterialIId, nMenge, l,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void createLosgutschlechtMitMaschine(
			LosgutschlechtDto losgutschlechtDto) throws ExceptionLP {
		try {
			fertigungFac.createLosgutschlechtMitMaschine(losgutschlechtDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LosDto createLoseAusAuftrag(LosDto losDto, Integer auftragIId)
			throws ExceptionLP {
		try {
			return fertigungFac.createLoseAusAuftrag(losDto, auftragIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LosablieferungDto updateLosablieferung(
			LosablieferungDto losablieferungDto, boolean bMaterialZurueckbuchen)
			throws ExceptionLP {
		try {

			return fertigungFac.updateLosablieferung(losablieferungDto,
					bMaterialZurueckbuchen, LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LosablieferungDto losablieferungFindByPrimaryKey(Integer iId,
			boolean bNeuberechnungDesGestehungspreisesFallsNotwendig)
			throws ExceptionLP {
		try {
			return fertigungFac.losablieferungFindByPrimaryKey(iId,
					bNeuberechnungDesGestehungspreisesFallsNotwendig,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LosgutschlechtDto losgutschlechtFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungFac.losgutschlechtFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public BigDecimal getMengeDerJuengstenLosablieferung(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungFac.getMengeDerJuengstenLosablieferung(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public BigDecimal getErledigteMenge(Integer losIId) throws ExceptionLP {
		try {
			return fertigungFac
					.getErledigteMenge(losIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public BigDecimal getAnzahlInFertigung(Integer artikelIId)
			throws ExceptionLP {
		try {
			return fertigungFac.getAnzahlInFertigung(artikelIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public List<SeriennrChargennrMitMengeDto> getOffenenSeriennummernBeiGeraeteseriennummer(
			Integer losIId) throws ExceptionLP {
		try {
			return fertigungFac.getOffenenSeriennummernBeiGeraeteseriennummer(
					losIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public LossollmaterialDto getArtikelIIdOffenenSeriennummernBeiGeraeteseriennummer(
			Integer losIId) throws ExceptionLP {
		try {
			return fertigungFac
					.getArtikelIIdOffenenSeriennummernBeiGeraeteseriennummer(
							losIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void stoppeProduktion(Integer losIId) throws ExceptionLP {
		try {
			fertigungFac.stoppeProduktion(losIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void stoppeProduktionRueckgaengig(Integer losIId) throws ExceptionLP {
		try {
			fertigungFac.stoppeProduktionRueckgaengig(losIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public JasperPrintLP printHalbfertigfabrikatsinventur(
			java.sql.Timestamp tStichtag, int iSortierung, boolean bVerdichtet,
			Integer partnerIIdFertigungsort) throws ExceptionLP {
		try {
			return fertigungReportFac.printHalbfertigfabrikatsinventur(
					tStichtag, iSortierung, bVerdichtet,
					partnerIIdFertigungsort, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printAlle(ReportJournalKriterienDto krit,
			boolean bNurAngelegteLose, Integer fertigungsgruppeIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = fertigungReportFac.printAlle(krit, bNurAngelegteLose,
					fertigungsgruppeIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return print;
	}

	public JasperPrintLP printOffene(java.sql.Date dStichtag,
			Integer iOptionStichtag, String belegNrVon, String belegNrBis,
			Integer kundeIId, Integer kostenstelleIId,
			Integer fertigungsgruppeIId, int iSortierung) throws ExceptionLP {
		try {
			return fertigungReportFac.printOffene(dStichtag, iOptionStichtag,
					belegNrVon, belegNrBis, kundeIId, kostenstelleIId,
					fertigungsgruppeIId, iSortierung, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public TreeMap<String, Object[]> aktualisiereLoseAusStueckliste(
			Integer stuecklisteIId) throws ExceptionLP {
		try {
			return fertigungFac.aktualisiereLoseAusStueckliste(stuecklisteIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printOffeneArbeitsgaenge(java.sql.Date dStichtag,
			Integer iOptionStichtag, String belegNrVon, String belegNrBis,
			Integer kundeIId, Integer kostenstelleIId,
			Integer fertigungsgruppeIId, Integer artikelgruppeIId,
			Integer maschineIId, boolean bSollstundenbetrachtung)
			throws ExceptionLP {
		try {
			return fertigungReportFac
					.printOffeneArbeitsgaenge(dStichtag, iOptionStichtag,
							belegNrVon, belegNrBis, kundeIId, kostenstelleIId,
							fertigungsgruppeIId, artikelgruppeIId, maschineIId,
							bSollstundenbetrachtung, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void erzeugeInterneBestellung(boolean bVorhandeneLoeschen,
			Integer iVorlaufzeit, Integer iToleranz,
			java.sql.Date dLieferterminFuerArtikelOhneReservierung,
			Boolean bVerdichten, Integer iVerdichtungstage) throws ExceptionLP {
		try {
			internebestellungFac.erzeugeInterneBestellung(bVorhandeneLoeschen,
					iVorlaufzeit, iToleranz,
					dLieferterminFuerArtikelOhneReservierung, bVerdichten,
					iVerdichtungstage, true, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public InternebestellungDto internebestellungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return internebestellungFac.internebestellungFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void gebeMaterialNachtraeglichAus(
			LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto,
			List<SeriennrChargennrMitMengeDto> listSnrChnr,
			boolean bReduziereFehlmenge) throws ExceptionLP {
		try {
			fertigungFac.gebeMaterialNachtraeglichAus(lossollmaterialDto,
					losistmaterialDto, listSnrChnr, bReduziereFehlmenge,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public InternebestellungDto updateInternebestellung(
			InternebestellungDto internebestellungDto) throws ExceptionLP {
		try {
			if (internebestellungDto.getIId() != null) {
				return internebestellungFac.updateInternebestellung(
						internebestellungDto, LPMain.getTheClient());
			} else {
				return internebestellungFac.createInternebestellung(
						internebestellungDto, LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeInternebestellung(
			InternebestellungDto internebestellungDto) throws ExceptionLP {
		try {
			internebestellungFac.removeInternebestellung(internebestellungDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeInternebestellungEinesMandanten() throws ExceptionLP {
		try {
			internebestellungFac.removeInternebestellungEinesMandanten(LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void bucheTOPSArtikelAufHauptLager(Integer losIId,
			BigDecimal zuzubuchendeSatzgroessen) throws ExceptionLP {
		try {
			fertigungFac.bucheTOPSArtikelAufHauptLager(losIId,
					LPMain.getTheClient(), zuzubuchendeSatzgroessen);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void pruefePositionenMitSollsatzgroesseUnterschreitung(
			Integer losIId, BigDecimal bdZuErledigendeMenge) throws ExceptionLP {
		try {
			fertigungFac.pruefePositionenMitSollsatzgroesseUnterschreitung(
					losIId, bdZuErledigendeMenge, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public JasperPrintLP printAufloesbareFehlmengen(Integer iSortierung,
			Boolean bNurArtikelMitLagerstand,
			Boolean bOhneEigengefertigteArtikel) throws ExceptionLP {
		try {
			return fertigungReportFac.printAufloesbareFehlmengen(iSortierung,
					bNurArtikelMitLagerstand, bOhneEigengefertigteArtikel,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printFehlmengenAllerLose(
			boolean bNurEigengefertigteArtikel,
			boolean bAlleOhneEigengefertigteArtikel) throws ExceptionLP {
		try {
			return fertigungReportFac.printFehlmengenAllerLose(
					bNurEigengefertigteArtikel,
					bAlleOhneEigengefertigteArtikel, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer getNextZusatzstatus() throws ExceptionLP {
		try {
			return fertigungFac.getNextZusatzstatus(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getNextArbeitsgang(Integer losIId) throws ExceptionLP {
		try {
			return fertigungFac.getNextArbeitsgang(losIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal wievileTOPSArtikelWurdenBereitsZugebucht(Integer losIId)
			throws ExceptionLP {
		try {
			return fertigungFac.wievileTOPSArtikelWurdenBereitsZugebucht(
					losIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void vertauscheLoslagerentnahme(Integer iiDLagerentnahme1,
			Integer iIdLagerentnahme2) throws ExceptionLP {
		try {
			fertigungFac.vertauscheLoslagerentnahme(iiDLagerentnahme1,
					iIdLagerentnahme2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public JasperPrintLP printStueckrueckmeldung(Integer losIId, int iSortierung)
			throws ExceptionLP {
		try {
			return fertigungReportFac.printStueckrueckmeldung(losIId,
					iSortierung, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printFehlteile(Integer losIId,
			boolean bNurPositionenMitFehlmengen) throws ExceptionLP {
		try {
			return fertigungReportFac.printFehlteile(losIId,
					bNurPositionenMitFehlmengen, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void interneBestellungUeberleiten(Integer interneBestellungIId)
			throws ExceptionLP {
		try {
			internebestellungFac.interneBestellungUeberleiten(
					interneBestellungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public KapazitaetsvorschauDto getKapazitaetsvorschau() throws ExceptionLP {
		try {
			return fertigungFac.getKapazitaetsvorschau(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void aktualisiereNachtraeglichPreiseAllerLosablieferungen(
			Integer losIId) throws ExceptionLP {
		try {
			fertigungFac.aktualisiereNachtraeglichPreiseAllerLosablieferungen(
					losIId, LPMain.getTheClient(), false);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	public void erledigteLoseImZeitraumNachkalkulieren(
			java.sql.Date tVon, java.sql.Date tBis) throws ExceptionLP {
		try {
			fertigungFac.erledigteLoseImZeitraumNachkalkulieren(
					tVon,tBis, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateLosZuordnung(LosDto losDto) throws ExceptionLP {
		try {
			fertigungFac.updateLosZuordnung(losDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Set<?> getInternebestellungIIdsEinesMandanten() throws ExceptionLP {
		try {
			return internebestellungFac
					.getInternebestellungIIdsEinesMandanten(LPMain
							.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	/**
	 * Die IB-Eintraege dieses Mandanten verdichten.
	 * 
	 * @throws ExceptionLP
	 * @param iVerdichtungstage
	 *            Integer
	 */
	public void verdichteInterneBestellung(Integer iVerdichtungstage)
			throws ExceptionLP {
		try {
			internebestellungFac.verdichteInterneBestellung(iVerdichtungstage,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheWiederholendelose(Integer iId1, Integer iId2)
			throws ExceptionLP {
		try {
			fertigungFac.vertauscheWiederholendelose(iId1, iId2);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Alle Los I_ID's des Mandanten holen.
	 * 
	 * @throws ExceptionLP
	 * @return ArrayList
	 */
	public ArrayList<Integer> getAllLosIIdDesMandanten() throws ExceptionLP {
		try {
			return fertigungFac.getAllLosIIdDesMandanten(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void zeigeArtikelhinweiseAllerLossollpositionen(Integer losIId)
			throws ExceptionLP {
		try {
			String s = fertigungFac.getArtikelhinweiseAllerLossollpositionen(
					losIId, LPMain.getTheClient());
			if (s != null && s.length() > 0) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hinweis"), s);
			}
		} catch (Throwable t) {
			handleThrowable(t);

		}
	}

	/**
	 * Alle Los I_ID's des Mandanten holen.
	 * 
	 * @throws ExceptionLP
	 * @return ArrayList
	 */
	public ArrayList<?> pruefeOffeneRahmenmengen() throws ExceptionLP {
		try {
			return internebestellungFac.pruefeOffeneRahmenmengen(LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printLosEtikett(Integer losIId, BigDecimal bdMenge,
			String sKommentar, boolean bMitInhalten, Integer iExemplare)
			throws ExceptionLP {
		try {
			return fertigungReportFac
					.printLosEtikett(losIId, bdMenge, sKommentar, bMitInhalten,
							iExemplare, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printLosEtikettA4(Integer losIId, BigDecimal bdMenge,
			String sKommentar, boolean bMitInhalten, Integer iExemplare)
			throws ExceptionLP {
		try {
			return fertigungReportFac
					.printLosEtikettA4(losIId, bdMenge, sKommentar,
							bMitInhalten, iExemplare, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public ArrayList<Integer> getAllPersonalIIdEinesSollarbeitsplansUeberLogGutSchlecht(
			Integer loslollarbeitsplanIId) throws ExceptionLP {
		try {
			return fertigungFac
					.getAllPersonalIIdEinesSollarbeitsplansUeberLogGutSchlecht(loslollarbeitsplanIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void vonSollpositionMengeZuruecknehmen(Integer lossollmaterialIId,
			BigDecimal nMenge, Integer sollpositionIIdZiel, Integer lagerIIdZiel)
			throws ExceptionLP {
		try {
			fertigungFac.vonSollpositionMengeZuruecknehmen(lossollmaterialIId,
					nMenge, sollpositionIIdZiel, lagerIIdZiel,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}
	}

}
