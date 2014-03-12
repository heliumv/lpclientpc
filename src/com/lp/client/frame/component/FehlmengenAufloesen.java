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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.lp.client.artikel.DialogFehlmengen;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.AufgeloesteFehlmengenDto;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/09/18 12:09:04 $
 */
public class FehlmengenAufloesen {
	private static TreeMap<String, ArrayList<AufgeloesteFehlmengenDto>> tmAufgeloesteFehlmengen = new TreeMap<String, ArrayList<AufgeloesteFehlmengenDto>>();

	public static void fehlmengenAufloesen(InternalFrame internalframe,
			Integer artikelIId, Integer lagerIId, String[] sSeriennummernArray,
			BigDecimal bdZugebuchteMenge) throws Throwable {

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						RechteFac.RECHT_FERT_DARF_FEHLMENGEN_PER_DIALOG_AUFLOESEN)) {

			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);
			BigDecimal bdVerfuegbaremenge = bdZugebuchteMenge;
			if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
				bdVerfuegbaremenge = new BigDecimal(sSeriennummernArray.length);
			}

			BigDecimal bdfehlmenge = DelegateFactory.getInstance()
					.getFehlmengeDelegate()
					.getAnzahlderPositivenFehlmengenEinesArtikels(artikelIId);

			if (bdfehlmenge.doubleValue() > 0) {
				DialogFehlmengen d = new DialogFehlmengen(artikelDto, lagerIId,
						sSeriennummernArray, bdVerfuegbaremenge, internalframe);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
			}
		}
	}

	public static void fehlmengenAufloesen(InternalFrame internalframe,
			Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge,
			BigDecimal bdZugebuchteMenge) throws Throwable {

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						RechteFac.RECHT_FERT_DARF_FEHLMENGEN_PER_DIALOG_AUFLOESEN)) {

			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);
			BigDecimal bdVerfuegbaremenge = bdZugebuchteMenge;
			if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
				bdVerfuegbaremenge = new BigDecimal(
						alSeriennrChargennrMitMenge.size());

				BigDecimal bdfehlmenge = DelegateFactory
						.getInstance()
						.getFehlmengeDelegate()
						.getAnzahlderPositivenFehlmengenEinesArtikels(
								artikelIId);

				if (bdfehlmenge.doubleValue() > 0) {
					DialogFehlmengen d = new DialogFehlmengen(
							artikelDto,
							lagerIId,
							SeriennrChargennrMitMengeDto
									.erstelleStringArrayAusMehrerenSeriennummern(alSeriennrChargennrMitMenge),
							bdVerfuegbaremenge, internalframe);
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				}

			} else if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
				bdVerfuegbaremenge = bdZugebuchteMenge;

				BigDecimal bdfehlmenge = DelegateFactory
						.getInstance()
						.getFehlmengeDelegate()
						.getAnzahlderPositivenFehlmengenEinesArtikels(
								artikelIId);

				if (bdfehlmenge.doubleValue() > 0) {

					for (int i = 0; i < alSeriennrChargennrMitMenge.size(); i++) {

						String[] s = new String[] { alSeriennrChargennrMitMenge
								.get(i).getCSeriennrChargennr() };

						DialogFehlmengen d = new DialogFehlmengen(artikelDto,
								lagerIId, s, bdVerfuegbaremenge, internalframe);
						LPMain.getInstance().getDesktop()
								.platziereDialogInDerMitteDesFensters(d);
						d.setVisible(true);
					}

				}

			} else {
				BigDecimal bdfehlmenge = DelegateFactory
						.getInstance()
						.getFehlmengeDelegate()
						.getAnzahlderPositivenFehlmengenEinesArtikels(
								artikelIId);

				if (bdfehlmenge.doubleValue() > 0) {
					DialogFehlmengen d = new DialogFehlmengen(artikelDto,
							lagerIId, null, bdVerfuegbaremenge, internalframe);
					LPMain.getInstance().getDesktop()
							.platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				}
			}

		}
	}

	public static void fehlmengenAufloesenMitLosBezug(
			InternalFrame internalframe, Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge,
			BigDecimal bdZugebuchteMenge, Integer lossollmaterialIId)
			throws Throwable {

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						RechteFac.RECHT_FERT_DARF_FEHLMENGEN_PER_DIALOG_AUFLOESEN)) {

			ArtikelfehlmengeDto fmDto = DelegateFactory
					.getInstance()
					.getFehlmengeDelegate()
					.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
							LocaleFac.BELEGART_LOS, lossollmaterialIId);

			if (fmDto != null && fmDto.getArtikelIId().equals(artikelIId)) {

				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIId);

				LossollmaterialDto lossollDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(lossollmaterialIId);

				LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
				losistmaterialDto.setLossollmaterialIId(lossollmaterialIId);
				losistmaterialDto.setLagerIId(lagerIId);

				losistmaterialDto.setBAbgang(Helper.boolean2Short(true));

				BigDecimal menge = bdZugebuchteMenge;
				if (fmDto.getNMenge().doubleValue() < menge.doubleValue()) {
					menge = fmDto.getNMenge();
				}
				losistmaterialDto.setNMenge(menge);

				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.gebeMaterialNachtraeglichAus(lossollDto,
								losistmaterialDto, alSeriennrChargennrMitMenge,
								true);

				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate().lagerFindByPrimaryKey(lagerIId);
				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losFindByPrimaryKey(lossollDto.getLosIId());

				FehlmengenAufloesen
						.addAufgeloesteFehlmenge(
								artikelDto,
								lagerDto,
								SeriennrChargennrMitMengeDto
										.erstelleStringArrayAusMehrerenSeriennummern(alSeriennrChargennrMitMenge),
								losDto, menge);

			}

		}
	}

	public static void loescheAufgeloesteFehlmengen() {
		tmAufgeloesteFehlmengen = new TreeMap<String, ArrayList<AufgeloesteFehlmengenDto>>();
	}

	public static void addAufgeloesteFehlmenge(ArtikelDto artikelDto,
			LagerDto lagerDto, String[] sSeriennrChnr, LosDto losDto,
			BigDecimal aufgeloesteMenge) throws Throwable {

		AufgeloesteFehlmengenDto aufgeloesteFehlmengenDto = new AufgeloesteFehlmengenDto();
		aufgeloesteFehlmengenDto.setArtikelDto(artikelDto);
		aufgeloesteFehlmengenDto.setArtikelCNr(artikelDto.getCNr());
		aufgeloesteFehlmengenDto.setLagerDto(lagerDto);
		aufgeloesteFehlmengenDto.setLagerCNr(lagerDto.getCNr());
		aufgeloesteFehlmengenDto.setLosDto(losDto);
		aufgeloesteFehlmengenDto.setLosCNr(losDto.getCNr());
		aufgeloesteFehlmengenDto.setAufgeloesteMenge(aufgeloesteMenge);
		aufgeloesteFehlmengenDto.setSSeriennrChnr(sSeriennrChnr);
		aufgeloesteFehlmengenDto.setLosDto(losDto);

		if (tmAufgeloesteFehlmengen.containsKey(losDto.getCNr())) {
			ArrayList<AufgeloesteFehlmengenDto> al = (ArrayList<AufgeloesteFehlmengenDto>) tmAufgeloesteFehlmengen
					.get(losDto.getCNr());
			al.add(aufgeloesteFehlmengenDto);
			tmAufgeloesteFehlmengen.put(losDto.getCNr(), al);
		} else {
			ArrayList<AufgeloesteFehlmengenDto> al = new ArrayList<AufgeloesteFehlmengenDto>();
			al.add(aufgeloesteFehlmengenDto);
			tmAufgeloesteFehlmengen.put(losDto.getCNr(), al);
		}

	}

	public static TreeMap<String, ArrayList<AufgeloesteFehlmengenDto>> getAufgeloesteFehlmengen() {
		return tmAufgeloesteFehlmengen;
	}

}
