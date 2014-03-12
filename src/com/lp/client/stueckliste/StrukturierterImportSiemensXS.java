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
package com.lp.client.stueckliste;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.stueckliste.service.StrukturierterImportSiemensNXDto;
import com.lp.server.system.service.SystemFac;
import com.lp.util.Helper;

public class StrukturierterImportSiemensXS {

	public ArrayList<StrukturierterImportSiemensNXDto> importSheet(
			ArrayList<StrukturierterImportSiemensNXDto> list)
			throws IOException {
		ArrayList<StrukturierterImportSiemensNXDto> gesamtliste = new ArrayList<StrukturierterImportSiemensNXDto>();

		for (int i = 0; i < list.size(); i++) {
			StrukturierterImportSiemensNXDto row = list.get(i);
			addToParent(gesamtliste, row);

		}

		return gesamtliste;
	}

	protected void addToParent(
			ArrayList<StrukturierterImportSiemensNXDto> list,
			StrukturierterImportSiemensNXDto row) {
		StrukturierterImportSiemensNXDto lastRow = null;

		int rowEbenen = row.getIEbene();

		if (list.size() > 0 && rowEbenen > 1) {
			lastRow = list.get(list.size() - 1);
			while (lastRow.getPositionen().size() > 0 && (--rowEbenen > 1)) {
				lastRow = lastRow.getPositionen().get(
						lastRow.getPositionen().size() - 1);
			}

			lastRow.getPositionen().add(row);
		} else {
			list.add(row);
		}
	}

	protected int getEbenen(String posnr) {
		String[] s = posnr.split("\\.");
		return s.length;
	}

	public StrukturierterImportSiemensNXDto setupRow(String[] cells) {
		StrukturierterImportSiemensNXDto row = new StrukturierterImportSiemensNXDto();
		String sEbene = cells[0];

		if (cells.length > 3) {
			row.setArtikelnummer(cells[1].trim().toUpperCase());

			try {
				row.setMenge(new BigDecimal(cells[3]));
			} catch (NumberFormatException e1) {
				// lt. WH wird dann 1 angenommen
				row.setMenge(new BigDecimal(1));
			}
			int iEbene = 1;

			while (sEbene.startsWith("    ")) {
				sEbene = sEbene.substring(4);
				iEbene++;
			}

			row.setiEbene(iEbene);

			try {

				ArtikelDto aDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByCNr(cells[1].trim().toUpperCase());

				if (aDto.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)) {
					// Dann Stuecklistenposition mm??
					row.setStklPosEinheit(SystemFac.EINHEIT_QUADRATMILLIMETER);

					try {
						row.setDBreite(new Double(cells[7]));
					} catch (NumberFormatException e1) {
						//
					}

					try {
						row.setDLaenge(new Double(cells[8]));
					} catch (NumberFormatException e1) {
						//
					}

				} else if (aDto.getEinheitCNr().equals(SystemFac.EINHEIT_METER)) {
					// Dann Stuecklistenposition mm
					row.setStklPosEinheit(SystemFac.EINHEIT_MILLIMETER);

					try {
						row.setDLaenge(new Double(cells[8]));
					} catch (NumberFormatException e1) {
						//
					}

					// laenge aus CSV bei uns in Breite eintragen

				} else if (aDto.getEinheitCNr()
						.equals(SystemFac.EINHEIT_STUECK)) {
					// Dann Stuecklistenposition Stk
					row.setStklPosEinheit(SystemFac.EINHEIT_STUECK);

				} else {
					// Fehlermeldung: Artikel XXX muss in Einheit m??, m oder Stk
					// definiert sein

					DialogFactory
							.showModalDialog(
									"Fehler",
									"Artikel '"
											+ row.getArtikelnummer()
											+ "' muss in Einheit m??, m oder Stk definiert sein.");

				}

				Integer artikelIId = aDto.getIId();
				row.setArtikelIId(artikelIId);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return row;
	}

	public StrukturierterImportSiemensNXDto setupRowRohmaterial(String[] cells) {
		StrukturierterImportSiemensNXDto row = new StrukturierterImportSiemensNXDto();
		String sEbene = cells[0];

		if (cells.length > 4) {
			if (cells[5].trim().length() > 2) {
				String artikelnummer = cells[5].trim();

				if (artikelnummer.indexOf("_") > -1) {
					artikelnummer = artikelnummer.substring(0,
							artikelnummer.indexOf("_"));
				}

				row.setArtikelnummer(artikelnummer);
				try {
					row.setDBreite(new Double(cells[7]));
				} catch (NumberFormatException e1) {
					//
				}

				try {
					row.setDLaenge(new Double(cells[8]));
				} catch (NumberFormatException e1) {
					//
				}

				// Einheit in HV muss inner m oder m?? sein
				// SP970: Rohmaterial muss ummer Menge 1 haben
				row.setMenge(new BigDecimal(1));
				int iEbene = 1;

				while (sEbene.startsWith("    ")) {
					sEbene = sEbene.substring(4);
					iEbene++;
				}

				row.setiEbene(iEbene + 1);

				try {
					Integer artikelIId = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByCNr(artikelnummer).getIId();
					row.setArtikelIId(artikelIId);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return row;
	}

	protected StrukturierterImportDto setupAnhaenge(
			StrukturierterImportDto row, File[] fileArray) throws IOException {
		HashMap<String, byte[]> hm = new HashMap<String, byte[]>();

		for (File f : fileArray) {
			if (isAttachmentForRow(row, f)) {
				hm.put(f.getName(), Helper.getBytesFromFile(f));
				break;
			}
		}

		if (hm.size() > 0) {
			row.setAnhaenge(hm);
		}

		return row;
	}

	/**
	 * Ermittelt, ob die angegebene Datei tats&auml;chlich ein Attachment f&uuml;r die
	 * aktuelle Zeile ist</br> Beginnt der Name des Anhangs mit der
	 * Artikelnummer der aktuellen Zeile so handelt es sich um einen zu
	 * verwendenden Anhang
	 * 
	 * @param row
	 * @param f
	 * @return true wenn es sich um ein Attachment f&uuml;r die aktuelle Zeile
	 *         handelt
	 */
	protected boolean isAttachmentForRow(StrukturierterImportDto row, File f) {
		return f.isFile() && f.getName().startsWith(row.getArtikelnr());
	}
}
