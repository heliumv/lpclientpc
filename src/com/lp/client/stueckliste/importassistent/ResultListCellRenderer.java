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
 package com.lp.client.stueckliste.importassistent;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import net.miginfocom.swing.MigLayout;

import com.lp.server.artikel.service.ArtikelDto;

public class ResultListCellRenderer extends JPanel implements ListCellRenderer {
	
	private static final long serialVersionUID = -1313860568182602531L;

	private JLabel cnr;
	private JLabel cBez;
	private JLabel cZBez;
	private JLabel cZBez2;
	
	public ResultListCellRenderer() {
		setLayout(new MigLayout("wrap 1, fill, hidemode 2", "[fill|fill,grow]"));
		setOpaque(true);
		cnr = new JLabel();
		cBez = new JLabel();
		cZBez = new JLabel();
		cZBez2 = new JLabel();
		add(cnr);
		add(cBez);
		add(cZBez);
		add(cZBez2);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		boolean showBez = false;
		
		if(value instanceof ArtikelDto) {
			ArtikelDto dto = (ArtikelDto) value;
			cnr.setText(dto.getCNr());
			if(value != StklImportPage3Ctrl.HANDARTIKEL 
					&& value != StklImportPage3Ctrl.FLR_LISTE
					&& value != StklImportPage3Ctrl.ZUVIELE_ARTIKEL_FLR_LISTE) {
				cBez.setText(dto.getArtikelsprDto().getCBez());
				cZBez.setText(dto.getArtikelsprDto().getCZbez());
				cZBez2.setText(dto.getArtikelsprDto().getCZbez2());
				showBez = true;
			} else if(value == StklImportPage3Ctrl.ZUVIELE_ARTIKEL_FLR_LISTE) {
				cBez.setText(dto.getArtikelsprDto().getCBez());
				showBez = true;
			}
		}
		cBez.setVisible(showBez);
		cZBez.setVisible(showBez);
		cZBez2.setVisible(showBez);
		
		invalidate();
		validate();
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		Color fg = isSelected ? list.getSelectionForeground() : list.getForeground();
		
		cnr.setForeground(fg);
		cBez.setForeground(fg);
		cZBez.setForeground(fg);
		cZBez2.setForeground(fg);
		return this;
	}
}
