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

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.DialogPositionenBarcodeerfassung;
import com.lp.client.lieferschein.DialogPositionenBarcodeerfassungArtikelset;
import com.lp.client.lieferschein.FindZuzubuchendenArtikel;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.service.Artikelset;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;

public class ArtikelsetViewController {

	private InternalFrame internalFrame ;
	private Integer artikelSetIIdFuerNeuePosition = null ;
	
	public void setInternalFrame(InternalFrame frame) {
		internalFrame = frame ;
	}
	
	public InternalFrame getInternalFrame() {
		return internalFrame ;
	}
	
	public void setArtikelSetIIdFuerNeuePosition(Integer artikelSetIId) {
		artikelSetIIdFuerNeuePosition = artikelSetIId ;
	}
	
	public Integer getArtikelSetIIdFuerNeuePosition() {
		return artikelSetIIdFuerNeuePosition ;
	}
	
	public void resetArtikelSetIIdFuerNeuePosition() {
		artikelSetIIdFuerNeuePosition = null ;
	}
	
	public boolean isArtikelsetKopfartikel(Integer artikelIId) throws Throwable {
		StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId) ;
		if(null == stklDto) return false ;		
		if(!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto.getStuecklisteartCNr())) return false ;

		return true ;
	}

	
	protected boolean hasArtikelsetSeriennrChargennr(Integer kopfartikelIId, BigDecimal satzGroesse) throws Throwable {
		StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(kopfartikelIId) ;
		if(null == stklDto) return false ;		
		if(!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto.getStuecklisteartCNr())) return false ;

		List<Integer> artikelIIds = DelegateFactory.getInstance().getStuecklisteDelegate()
		 	.getSeriennrChargennrArtikelIIdsFromArtikelset(stklDto.getIId(), satzGroesse) ;
		return artikelIIds.size() > 0 ;		
	}

	protected boolean hasArtikelsetSeriennrChargennr(BelegpositionVerkaufDto belegPositionDto) throws Throwable {
		return hasArtikelsetSeriennrChargennr(belegPositionDto.getArtikelIId(), belegPositionDto.getNMenge()) ;
	}
	
	public List<SeriennrChargennrMitMengeDto> handleArtikelsetSeriennummern(boolean isZugang, Integer lagerIId, Artikelset artikelset, BelegpositionVerkaufDto belegPositionDto) throws Throwable {
		List<SeriennrChargennrMitMengeDto> snrs = new ArrayList<SeriennrChargennrMitMengeDto>() ;
		if(!hasArtikelsetSeriennrChargennr(belegPositionDto)) return snrs;

		DialogPositionenBarcodeerfassung d = null ;
		if(artikelset == null) {
			d = new DialogPositionenBarcodeerfassung(lagerIId, getInternalFrame()) ;
		} else {
			DialogPositionenBarcodeerfassungArtikelset dset = 
				new DialogPositionenBarcodeerfassungArtikelset(lagerIId, artikelset, getInternalFrame());
			artikelset.setSlipAmount(artikelset.getHead().getNMenge()) ;
			dset.setTotalCountSeriennummern(getSnrTotalCountForArtikelset(artikelset)) ;			
			d = dset ;
		}
		d.setSindVerschiedeneArtikelErlaubt(true) ;
		if(isZugang) {
			d.setArtikelFinder(new FindZuzubuchendenArtikel()) ;
		}
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);

		d.setVisible(true);
		return d.alSeriennummern;
	}

	public List<SeriennrChargennrMitMengeDto> handleArtikelsetSeriennummern(
			boolean isZugang, Integer lagerIId, BelegpositionVerkaufDto belegPositionDto, 
			List<SeriennrChargennrMitMengeDto> bekannteSnrs) throws Throwable {
		List<SeriennrChargennrMitMengeDto> snrs = new ArrayList<SeriennrChargennrMitMengeDto>() ;
		if(!hasArtikelsetSeriennrChargennr(belegPositionDto)) return snrs;
		
		DialogPositionenBarcodeerfassung d = new DialogPositionenBarcodeerfassung(
				lagerIId, getInternalFrame());
		d.setSindVerschiedeneArtikelErlaubt(true) ;
		if(isZugang) {
			d.setArtikelFinder(new FindZuzubuchendenArtikel()) ;
		}
		if(bekannteSnrs != null) {
			d.setSeriennummern(bekannteSnrs) ;
		}
		
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);

		d.setVisible(true);
		return d.alSeriennummern;
	}
	
	public List<SeriennrChargennrMitMengeDto> handleArtikelsetSeriennummern(boolean isZugang, Integer lagerIId, BelegpositionVerkaufDto belegPositionDto) throws Throwable {
		return handleArtikelsetSeriennummern(isZugang, lagerIId, belegPositionDto, null) ;
//		List<SeriennrChargennrMitMengeDto> snrs = new ArrayList<SeriennrChargennrMitMengeDto>() ;
//		if(!hasArtikelsetSeriennrChargennr(belegPositionDto)) return snrs;
//		
//		DialogPositionenBarcodeerfassung d = new DialogPositionenBarcodeerfassung(
//				lagerIId, getInternalFrame());
//		d.setSindVerschiedeneArtikelErlaubt(true) ;
//		if(isZugang) {
//			d.setArtikelFinder(new FindZuzubuchendenArtikel()) ;
//		}
//		LPMain.getInstance().getDesktop()
//				.platziereDialogInDerMitteDesFensters(d);
//
//		d.setVisible(true);
//		return d.alSeriennummern;
	}
	
	public List<SeriennrChargennrMitMengeDto> handleArtikelsetSeriennummern(Integer lagerIId, BelegpositionVerkaufDto belegPositionDto) throws Throwable {
		return handleArtikelsetSeriennummern(false, lagerIId, belegPositionDto, null) ;
	}
	
	public List<SeriennrChargennrMitMengeDto> handleArtikelsetSeriennummernZugang(Integer lagerIId, BelegpositionVerkaufDto belegPositionDto) throws Throwable {
		return handleArtikelsetSeriennummern(true, lagerIId, belegPositionDto, null) ;
	}
	
	public List<SeriennrChargennrMitMengeDto> handleArtikelsetSeriennummern(Integer lagerIId, BelegpositionVerkaufDto belegPositionDto, 
			List<SeriennrChargennrMitMengeDto> bekannteSnrs) throws Throwable {
		return handleArtikelsetSeriennummern(false, lagerIId, belegPositionDto, bekannteSnrs) ;		
	}

	public List<SeriennrChargennrMitMengeDto> handleArtikelsetSeriennummernZugang(Integer lagerIId, BelegpositionVerkaufDto belegPositionDto,
			List<SeriennrChargennrMitMengeDto> bekannteSnrs) throws Throwable {
		return handleArtikelsetSeriennummern(true, lagerIId, belegPositionDto, bekannteSnrs) ;		
	}
	
	public List<SeriennrChargennrMitMengeDto> handleArtikelsetSeriennummern(
			Integer lagerIId, AuftragpositionDto[] auftragPositionen, 
			List<SeriennrChargennrMitMengeDto> bekannteSeriennummern) throws Throwable {
		List<SeriennrChargennrMitMengeDto> snrs = new ArrayList<SeriennrChargennrMitMengeDto>() ;
		if(auftragPositionen.length < 2) return snrs ;
		if(!isArtikelsetKopfartikel(auftragPositionen[0].getArtikelIId())) return snrs;

		boolean hasIdentity = false ;
		for (int i = 1; i < auftragPositionen.length; i++) {
			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(auftragPositionen[i].getArtikelIId()) ;
			hasIdentity |= artikelDto.isChargennrtragend() || artikelDto.isSeriennrtragend() ;
		}

		if(hasIdentity) {
			DialogPositionenBarcodeerfassung d = new DialogPositionenBarcodeerfassung(
					lagerIId,
					getInternalFrame());
			d.setSindVerschiedeneArtikelErlaubt(true) ;
			d.setSeriennummern(bekannteSeriennummern) ;
			
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);

			d.setVisible(true);
			snrs = d.alSeriennummern;
		}

		return snrs ;
	}
	
	public boolean needArtikelsetSeriennummern(Integer lagerIId, BelegpositionVerkaufDto[] auftragPositionen) throws Throwable {
		if(auftragPositionen.length < 2) return false ;
		if(!isArtikelsetKopfartikel(auftragPositionen[0].getArtikelIId())) return false ;

		boolean hasIdentity = false ;
		for (int i = 1; i < auftragPositionen.length; i++) {
			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(auftragPositionen[i].getArtikelIId()) ;
			hasIdentity |= artikelDto.isChargennrtragend() || artikelDto.isSeriennrtragend() ;
		}

		return hasIdentity ;
	}
	
	
	public BigDecimal getSnrTotalCountForArtikelset(Artikelset artikelset) throws Throwable {
		BigDecimal sollsatzGroesse = artikelset.getSlipAmount() ;
		BigDecimal oldSollsatzGroesse = artikelset.getPreviousSlipAmount() ;
		if(null == oldSollsatzGroesse) {
			oldSollsatzGroesse = sollsatzGroesse ;
		}

		BigDecimal total = BigDecimal.ZERO ;
		List<BelegpositionVerkaufDto> positions = artikelset.getPositions() ;
		for (BelegpositionVerkaufDto auftragpositionDto : positions) {
			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId()) ;
			if(artikelDto.isSeriennrtragend()) {				
//				BigDecimal sollMenge = auftragpositionDto.getNMenge().divide(sollsatzGroesse).multiply(artikelset.getAvailableAmount()) ;

				BigDecimal offeneMenge = auftragpositionDto.getNMenge() ;
				if(auftragpositionDto instanceof AuftragpositionDto) {
					offeneMenge = ((AuftragpositionDto) auftragpositionDto).getNOffeneMenge() ;
				}
				
				BigDecimal sollMenge = offeneMenge.divide(oldSollsatzGroesse).multiply(artikelset.getAvailableAmount()) ;
				total = total.add(sollMenge)  ;
			}
		}
		
		return total ;
	}

	public List<SeriennrChargennrMitMengeDto> inputArtikelsetSeriennummern(
			String title, Integer lagerIId, Artikelset artikelset) throws Throwable {
		DialogPositionenBarcodeerfassungArtikelset d = new DialogPositionenBarcodeerfassungArtikelset(
				lagerIId,
				artikelset, getInternalFrame());
		d.setTitle(title) ;
		d.setTotalCountSeriennummern(getSnrTotalCountForArtikelset(artikelset)) ;
		
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);

		d.setVisible(true);
		return d.alSeriennummern;
	}

	
	public boolean isArtikelsetWithSnrsStoreable(
			BelegpositionVerkaufDto belegPositionDto, 
			List<SeriennrChargennrMitMengeDto> snrs) throws Throwable {
		if(isArtikelsetKopfartikel(belegPositionDto.getArtikelIId()) &&
			hasArtikelsetSeriennrChargennr(belegPositionDto) && 
			(0 == snrs.size())) {
			return false ;
		}
		
		return true ;
	}

	public boolean validateArtikelsetConstraints(BelegpositionDto belegPositionDto) throws Throwable {
		if(null == getArtikelSetIIdFuerNeuePosition()) return true ;
		
		if (!belegPositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
			DialogFactory
			.showModalDialog(
					LPMain
							.getTextRespectUISPr("lp.warning"),
					LPMain
							.getTextRespectUISPr("lp.hint.artikelset_nur_mit_ident"));
			return false ;
		}

		if(null == belegPositionDto.getArtikelIId()) {
			DialogFactory
			.showModalDialog(
					LPMain
							.getTextRespectUISPr("lp.warning"),
					LPMain
							.getTextRespectUISPr("lp.hint.artikelwaehlen"));
			return false ;								
		}
		
		if(isArtikelsetKopfartikel(belegPositionDto.getArtikelIId())) {
			DialogFactory
			.showModalDialog(
					LPMain
							.getTextRespectUISPr("lp.warning"),
					LPMain
							.getTextRespectUISPr("lp.hint.artikelset_nicht_im_artikelset"));
			return false ;				
		}

		return true ;
	}
	
	/**
	 * Bedingungen f&uuml;r den Copy-Teil des Copy&Paste &uuml;berpr&uuml;fen
	 * 
	 * Es wird sichergestellt, dass bei mehreren selektierten Eintr&auml;gen entweder nur 
	 * "normale" Artikel enthalten sind, oder maximal ein Setartikel-Kopf, dann aber keine
	 * Setartikel-Teilartikel.
	 * 
	 * @param dtos
	 * @return true wenn ein Copy machbar und sinnvoll ist. Ansonsten wird nach einer
	 *   Dialognachricht ei
	 * @throws Throwable
	 */
	public boolean validateCopyConstraints(BelegpositionDto[] dtos) throws Throwable {
		if(dtos.length <= 1) return true ;
		
		boolean hasAlreadyArtikelset = false ;
		for (BelegpositionDto belegpositionDto : dtos) {
			if(hasAlreadyArtikelset) {
				return false ;									
			}

			if(isArtikelsetKopfartikel(belegpositionDto.getArtikelIId())) {
				hasAlreadyArtikelset = true ;
			}
		}
		
		return true ;
	}
	
	public boolean validateCopyConstraintsUI(BelegpositionDto[] dtos) throws Throwable {
		boolean valid = validateCopyConstraints(dtos) ;
		if(!valid) {
			DialogFactory
			.showModalDialog(
					LPMain
							.getTextRespectUISPr("lp.warning"),
					LPMain
							.getTextRespectUISPr("lp.hint.belegposition_copy")) ;			
		}
		return valid ;
	}
	
	public boolean validatePasteConstraints(Object o) throws Throwable {
		if(!(o instanceof BelegpositionDto[])) return false ;
		return validateCopyConstraints((BelegpositionDto[]) o) ;
	}
	
	public boolean validatePasteConstraintsUI(Object o) throws Throwable {
		boolean valid = validatePasteConstraints(o) ;
		if(!valid) {
			DialogFactory
			.showModalDialog(
					LPMain
							.getTextRespectUISPr("lp.warning"),
					LPMain
							.getTextRespectUISPr("lp.hint.belegposition_paste")) ;			
		}
		return valid ;
	}
}
