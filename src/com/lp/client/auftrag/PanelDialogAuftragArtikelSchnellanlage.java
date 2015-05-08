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
 *******************************************************************************/
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelDynamischHelper;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.SystemFac;
import com.lp.util.Helper;

public class PanelDialogAuftragArtikelSchnellanlage extends
		PanelDialogKriterien implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArtikelDto artikelDto = null;
	JPanel panel0 = new JPanel();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	InternalFrame internalFrame = null;
	Integer artikelIId = null;
	public boolean bAbbruch = false;

	private WrapperLabel wlaKurzbezeichnung = new WrapperLabel();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperLabel wlaZusatzbez = new WrapperLabel();
	private WrapperLabel wlaZusatzbez2 = new WrapperLabel();
	private WrapperTextField wtfKurzbezeichnung = new WrapperTextField();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperTextField wtfZusatzbez = new WrapperTextField();
	private WrapperTextField wtfZusatzbez2 = new WrapperTextField();
	private WrapperLabel wlaRevision = new WrapperLabel();
	private WrapperTextField wtfRevision = new WrapperTextField();
	private WrapperCheckBox wcbPacklisteDrucken = new WrapperCheckBox();

	PanelDynamischHelper phd = null;

	public PanelDialogAuftragArtikelSchnellanlage(InternalFrame internalFrame,
			Integer artikelIId) throws Throwable {

		super(internalFrame, LPMain.getTextRespectUISPr("auft.schnellanlage"));

		this.internalFrame = internalFrame;
		this.artikelIId = artikelIId;

		artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(artikelIId);

		this.setSize(750, 650);

		jbInit();

	}

	public ArtikelDto getArtikelDtoVorhandenMitNeuenBezeichnungen(){
		
		artikelDto.setCRevision(wtfRevision.getText());
		if(artikelDto.getArtikelsprDto()!=null){
			artikelDto.getArtikelsprDto().setCKbez(wtfKurzbezeichnung.getText());
			artikelDto.getArtikelsprDto().setCBez(wtfBezeichnung.getText());
			artikelDto.getArtikelsprDto().setCZbez(wtfZusatzbez.getText());
			artikelDto.getArtikelsprDto().setCZbez2(wtfZusatzbez2.getText());
		}
		
		return artikelDto;
	}
	
	public ArtikelDto getArtikelDto() {
		ArtikelDto artikelDto = new ArtikelDto();

		artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
		artikelDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
		artikelDto.setArtgruIId(this.artikelDto.getArtgruIId());
		artikelDto.setVorzugIId(this.artikelDto.getVorzugIId());
		artikelDto.setBVersteckt(Helper.boolean2Short(false));
		artikelDto.setCRevision(wtfRevision.getText());
		ArtikelsprDto artikelsprDto = new ArtikelsprDto();
		artikelsprDto.setCKbez(wtfKurzbezeichnung.getText());
		artikelsprDto.setCBez(wtfBezeichnung.getText());
		artikelsprDto.setCZbez(wtfZusatzbez.getText());
		artikelsprDto.setCZbez2(wtfZusatzbez2.getText());

		artikelDto.setArtikelsprDto(artikelsprDto);

		return artikelDto;

	}

	public boolean getPacklisteDrucken(){
		return wcbPacklisteDrucken.isSelected();
	}
	
	public PaneldatenDto[] getPaneldatenDtos() throws Throwable {
		return phd.components2Dto(null);
	}

	private void jbInit() throws Throwable {

		JPanel panelartikel = new JPanel(new GridBagLayout());

		panel1.setLayout(gridBagLayout1);

		jpaWorkingOn.add(panelartikel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 20));

		wlaKurzbezeichnung.setText(LPMain
				.getTextRespectUISPr("artikel.kurzbez"));
		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wlaZusatzbez
				.setText(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wlaZusatzbez2.setText(LPMain
				.getTextRespectUISPr("lp.zusatzbezeichnung2"));
		wlaRevision.setText(LPMain.getTextRespectUISPr("artikel.revision"));
		
		wcbPacklisteDrucken.setText(LPMain.getTextRespectUISPr("auft.schnellanlage.packlistedrucken"));
		wcbPacklisteDrucken.setSelected(true);

		wtfKurzbezeichnung
				.setColumnsMax(ArtikelFac.MAX_ARTIKEL_KURZBEZEICHNUNG);
		wtfBezeichnung.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ARTIKELBEZEICHNUNG);
		wtfZusatzbez.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ZUSATZBEZEICHNUNG);
		wtfZusatzbez2.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ZUSATZBEZEICHNUNG2);

		wtfRevision.setText(artikelDto.getCRevision());

		if (artikelDto.getArtikelsprDto() != null) {
			wtfKurzbezeichnung
					.setText(artikelDto.getArtikelsprDto().getCKbez());
			wtfBezeichnung.setText(artikelDto.getArtikelsprDto().getCBez());
			wtfZusatzbez.setText(artikelDto.getArtikelsprDto().getCZbez());
			wtfZusatzbez2.setText(artikelDto.getArtikelsprDto().getCZbez2());
		}

		int iZeileArtikel = 0;
		panelartikel.add(wlaKurzbezeichnung, new GridBagConstraints(0,
				iZeileArtikel, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelartikel.add(wtfKurzbezeichnung, new GridBagConstraints(1,
				iZeileArtikel, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelartikel.add(wlaRevision, new GridBagConstraints(2, iZeileArtikel,
				1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelartikel.add(wtfRevision, new GridBagConstraints(3, iZeileArtikel,
				1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeileArtikel++;

		panelartikel.add(wlaBezeichnung, new GridBagConstraints(0,
				iZeileArtikel, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelartikel.add(wtfBezeichnung, new GridBagConstraints(1,
				iZeileArtikel, 3, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeileArtikel++;

		panelartikel.add(wlaZusatzbez, new GridBagConstraints(0, iZeileArtikel,
				1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelartikel.add(wtfZusatzbez, new GridBagConstraints(1, iZeileArtikel,
				3, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeileArtikel++;

		panelartikel.add(wlaZusatzbez2, new GridBagConstraints(0,
				iZeileArtikel, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelartikel.add(wtfZusatzbez2, new GridBagConstraints(1,
				iZeileArtikel, 2, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		
		panelartikel.add(wcbPacklisteDrucken, new GridBagConstraints(3,
				iZeileArtikel,1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		
		
		iZeileArtikel++;

		phd = new PanelDynamischHelper(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN,
				artikelDto.getArtgruIId(), internalFrame);

		jpaWorkingOn.add(phd.panelErzeugen(), new GridBagConstraints(0, 1, 1,
				1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(panel2, new GridBagConstraints(0, 2, 0, 0, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		phd.dto2Components(artikelDto.getIId()+"");

	}

}
