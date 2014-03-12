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
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class SheetImportController implements ISheetImportController {

	@Override
	public ArrayList<StrukturierterImportDto> importSheet(Sheet sheet,
			File[] attachments) throws Throwable {
		ArrayList<StrukturierterImportDto> gesamtliste = new ArrayList<StrukturierterImportDto>();

		for (int i = 1; i < sheet.getRows(); i++) {

			Cell[] cells = sheet.getRow(i);

			if (cells[0] != null && cells[0].getType() != CellType.EMPTY
					&& cells[0].getContents().trim().length() > 0) {
				StrukturierterImportDto row = setupRow(cells);
				if (row.getPosnr().trim().length() > 0) {
					setupAnhaenge(row, attachments);
					addToParent(gesamtliste, row);
				}
			}
		}

		return gesamtliste;
	}

	protected void addToParent(ArrayList<StrukturierterImportDto> list,
			StrukturierterImportDto row) {
		StrukturierterImportDto lastRow = null;

		int rowEbenen = getEbenen(row.getPosnr());

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

	@Override
	public StrukturierterImportDto setupRow(Cell[] cells) throws Throwable {
		StrukturierterImportDto row = new StrukturierterImportDto();

		row.setPosnr(cells[0].getContents());

		if (cells[1].getType() == CellType.NUMBER) {
			row.setMenge(((NumberCell) cells[1]).getValue());
		} else {
			// Fehler
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_IMPORT_DATENTYP_SPALTE_MENGE_FALSCH,
					new Exception("FEHLER_IMPORT_DATENTYP_SPALTE_MENGE_FALSCH"));
		}

		row.setArtikelnr(cells[2].getContents().trim().toUpperCase());

		if (cells[3].getType() == CellType.LABEL) {
			row.setArtikelbez(((LabelCell) cells[3]).getString());
		}
		if (cells.length > 4) {
			if (cells[4].getType() == CellType.LABEL) {
				row.setAbmessungen(((LabelCell) cells[4]).getString());
			}
		}
		if (cells.length > 5) {
			if (cells[5].getType() == CellType.LABEL) {

				String material = ((LabelCell) cells[5]).getString();

				if (material != null && material.length() > 0) {
					Integer materialIId = DelegateFactory.getInstance()
							.getMaterialDelegate()
							.materialFindByCNrOhneExc(material);

					row.setMaterial(materialIId);

					if (materialIId == null) {
						ArrayList al = new ArrayList();
						al.add(material);
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_IMPORT_MATERIAL_NICHT_VORHANDEN,
								"",
								al,
								new Exception(
										"FEHLER_IMPORT_MATERIAL_NICHT_VORHANDEN"));
					}

				}

			}
		}
		if (cells.length > 6) {
			if (cells[6] != null && cells[6].getType() != CellType.EMPTY) {
				if (cells[6].getType() == CellType.NUMBER) {
					row.setGewicht(((NumberCell) cells[6]).getValue());
				} else {
					// Fehler
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_IMPORT_DATENTYP_SPALTE_GEWICHT_FALSCH,
							new Exception(
									"FEHLER_IMPORT_DATENTYP_SPALTE_GEWICHT_FALSCH"));
				}
			}
		}
		if (cells.length > 7) {
			row.setLiefergruppe(cells[7].getContents());
		}

		if (cells.length > 8) {
			if (cells[8] != null && cells[8].getType() != CellType.EMPTY
					&& cells[8].getContents().trim().length() > 0) {
				row.setEinheitCNrZielmenge(cells[8].getContents());

				// Mengen in HV suchen und nachsehen
				EinheitDto ehtDto;
				try {
					ehtDto = DelegateFactory.getInstance().getSystemDelegate()
							.einheitFindByPrimaryKey(cells[8].getContents());
					row.setEinheitCNrZielmenge(ehtDto.getCNr());
				} catch (Exception e) {

					// Keine Einheit gefunden
				}

				Double dDimension1 = null;
				Double dDimension2 = null;
				Double dDimension3 = null;
				if (cells.length > 9) {
					if (cells[9] != null
							&& cells[9].getType() != CellType.EMPTY
							&& cells[9].getType() == CellType.NUMBER) {
						dDimension1 = ((NumberCell) cells[9]).getValue();
					}
				}
				if (cells.length > 10) {
					if (cells[10] != null
							&& cells[10].getType() != CellType.EMPTY
							&& cells[10].getType() == CellType.NUMBER) {
						dDimension2 = ((NumberCell) cells[10]).getValue();
					}
				}
				if (cells.length > 11) {
					if (cells[11] != null
							&& cells[11].getType() != CellType.EMPTY
							&& cells[11].getType() == CellType.NUMBER) {
						dDimension3 = ((NumberCell) cells[11]).getValue();
					}
				}

				row.setDimension1(dDimension1);
				row.setDimension2(dDimension2);
				row.setDimension3(dDimension3);

			}

		}

		return row;
	}

	protected StrukturierterImportDto setupAnhaenge(
			StrukturierterImportDto row, File[] fileArray) throws IOException {
		HashMap<String, Object[]> hm = new HashMap<String, Object[]>();

		for (File f : fileArray) {
			if (isAttachmentForRow(row, f)) {

				Object[] oFile = new Object[2];
				oFile[0] = Helper.getBytesFromFile(f);
				oFile[1] = new java.sql.Timestamp(f.lastModified());

				hm.put(f.getName(), oFile);
				break;
			}
		}

		if (hm.size() > 0) {
			row.setAnhaengeMitFileDatum(hm);
		}

		return row;
	}

	/**
	 * Ermittelt, ob die angegebene Datei tats&auml;chlich ein Attachment
	 * f&uuml;r die aktuelle Zeile ist</br> Beginnt der Name des Anhangs mit der
	 * Artikelnummer der aktuellen Zeile so handelt es sich um einen zu
	 * verwendenden Anhang
	 * 
	 * @param row
	 * @param f
	 * @return true wenn es sich um ein Attachment f&uuml;r die aktuelle Zeile
	 *         handelt
	 */
	protected boolean isAttachmentForRow(StrukturierterImportDto row, File f) {
		return f.isFile()
				&& f.getName().toUpperCase().startsWith(row.getArtikelnr());
	}
}
