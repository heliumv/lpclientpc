/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
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
import java.util.LinkedHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.ejb.MahnstufePK;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.finanz.service.MahnwesenFac;
import com.lp.server.rechnung.service.RechnungDto;

/**
 * <p><I>BusinesDelegate fuer das Mahnwesen</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.7 $
 */
public class MahnwesenDelegate extends Delegate {
	private MahnwesenFac mahnwesenFac = null;
	private Context context;

	public MahnwesenDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			mahnwesenFac = (MahnwesenFac) context
					.lookup("lpserver/MahnwesenFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public MahnlaufDto createMahnlaufMitMahnvorschlag() throws ExceptionLP {
		try {
			return mahnwesenFac.createMahnlaufMitMahnvorschlag(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeMahnlauf(MahnlaufDto mahnlaufDto) throws ExceptionLP {
		try {
			mahnwesenFac.removeMahnlauf(mahnlaufDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMahnlauf(MahnlaufDto mahnlaufDto) throws ExceptionLP {
		try {
			mahnwesenFac.updateMahnlauf(mahnlaufDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public MahnlaufDto mahnlaufFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return mahnwesenFac.mahnlaufFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMahnstufe(MahnstufeDto mahnstufeDto)
			throws ExceptionLP {
		try {
			mahnwesenFac.createMahnstufe(mahnstufeDto, LPMain.getTheClient());
			return mahnstufeDto.getIId();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeMahnstufe(MahnstufeDto mahnstufeDto) throws ExceptionLP {
		try {
			mahnwesenFac.removeMahnstufe(mahnstufeDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMahnstufe(MahnstufeDto mahnstufeDto) throws ExceptionLP {
		try {
			mahnwesenFac.updateMahnstufe(mahnstufeDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public MahnstufeDto mahnstufeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return mahnwesenFac.mahnstufeFindByPrimaryKey(new MahnstufePK(iId,
					LPMain.getTheClient().getMandant()));
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MahnstufeDto[] mahnstufeFindAll() throws ExceptionLP {
		try {
			return mahnwesenFac.mahnstufeFindByMandantCNr(LPMain.getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LinkedHashMap<?, ?> getAllMahnstufe() throws ExceptionLP {
		try {
			return mahnwesenFac.getAllMahnstufe(LPMain.getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeMahnung(MahnungDto mahnungDto) throws ExceptionLP {
		try {
			mahnwesenFac.removeMahnung(mahnungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public MahnungDto updateMahnung(MahnungDto mahnungDto) throws ExceptionLP {
		try {
			if (mahnungDto.getIId() != null) {
				return mahnwesenFac.updateMahnung(mahnungDto, LPMain
						.getTheClient());
			} else {
				return mahnwesenFac.createMahnung(mahnungDto, LPMain
						.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MahnungDto mahnungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return mahnwesenFac.mahnungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void mahneMahnung(Integer mahnungIId) throws ExceptionLP {
		try {
			mahnwesenFac.mahneMahnung(mahnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void mahneMahnungRueckgaengig(Integer mahnungIId) throws ExceptionLP {
		try {
			mahnwesenFac.mahneMahnungRueckgaengig(mahnungIId, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void mahneMahnlaufRueckgaengig(Integer mahnlaufIId)
			throws ExceptionLP {
		try {
			mahnwesenFac.mahneMahnlaufRueckgaengig(mahnlaufIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void mahneMahnlauf(Integer mahnlaufIId) throws ExceptionLP {
		try {
			mahnwesenFac.mahneMahnlauf(mahnlaufIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer berechneMahnstufeFuerRechnung(RechnungDto rechnungDto)
			throws ExceptionLP {
		try {
			return mahnwesenFac.berechneMahnstufeFuerRechnung(rechnungDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.sql.Date berechneFaelligkeitsdatumFuerMahnstufe(
			java.util.Date dDatum, Integer zahlungszielIId, Integer mahnstufeIId)
			throws ExceptionLP {
		try {
			return mahnwesenFac.berechneFaelligkeitsdatumFuerMahnstufe(dDatum,
					zahlungszielIId, mahnstufeIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Boolean bGibtEsEinenOffenenMahnlauf() throws ExceptionLP {
		try {
			return mahnwesenFac.bGibtEsEinenOffenenMahnlauf(LPMain.getTheClient().getMandant(), 
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return Boolean.FALSE ;
		}
	}

	public BigDecimal getSummeEinesKundenImMahnlauf(Integer mahnlaufIId,
			Integer kundeIId) throws ExceptionLP {
		try {
			return mahnwesenFac.getSummeEinesKundenImMahnlauf(mahnlaufIId,
					kundeIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getAktuelleMahnstufeEinerRechnung(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return mahnwesenFac.getAktuelleMahnstufeEinerRechnung(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.sql.Date getAktuellesMahndatumEinerRechnung(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return mahnwesenFac.getAktuellesMahndatumEinerRechnung(rechnungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	public MahnungDto[] mahnungFindByRechnungIId(Integer rechnungIId)
			throws ExceptionLP {
		try {
			return mahnwesenFac.mahnungFindByRechnungIId(rechnungIId);
		} catch (Throwable ex) {
			return null;
		}
	}
}
