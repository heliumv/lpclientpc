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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import com.lp.client.frame.HelperClient;

public class CornerInfoIcon {

	private static final String RADIOBUTTON_PATH = "/com/lp/client/res/toogleButtonIcons/windowsRadioButton_";
	private static final String CHECKBOX_PATH = "/com/lp/client/res/toogleButtonIcons/windowsCheckBox_";

	private static final String ICONDEFAULT = "defaultIcon.png";
	private static final String ICONPRESSED = "pressedIcon.png";
	private static final String ICONROLLOVER = "rollOverIcon.png";
	private static final String ICONSELECTED = "selectedIcon.png";
	private static final String ICONROLLOVERSELECTED = "rollOverSelectedIcon.png";
	private static final String ICONDISABLED = "disabledIcon.png";
	private static final String ICONDISABLEDSELECTED = "disabledSelectedIcon.png";

	public static final int INVALID_STATE = -1;
	public static final int DEFAULT_STATE = 0;
	public static final int ACTIVE_STATE = 1;
	public static final int SET_STATE = 2;
	public static final int EMPTY_STATE = 3;

	private static List<ImageIcon> iconStates = Arrays.asList(
			HelperClient.createImageIcon("cornerInfo.png"),
			HelperClient.createImageIcon("cornerInfoActive.png"),
			HelperClient.createImageIcon("cornerInfoDefiniert.png"),
			HelperClient.createImageIcon("cornerInfoNichtDefiniert.png"));

	private static List<HashMap<String, ImageIcon>> radioButtonHashMap = getRadioButtonHashMaps();
	private static List<HashMap<String, ImageIcon>> checkBoxHashMap = getCheckBoxHashMaps();

	public static ImageIcon getRadioButtonIcon(int cornerState, String iconState) {
		if (cornerState == INVALID_STATE)
			return radioButtonHashMap.get(4).get(iconState);
		return radioButtonHashMap.get(cornerState).get(iconState);
	}

	public static ImageIcon getCheckBoxIcon(int cornerState, String iconState) {
		if (cornerState == INVALID_STATE)
			return checkBoxHashMap.get(4).get(iconState);
		return checkBoxHashMap.get(cornerState).get(iconState);
	}

	public static ImageIcon getCornerIcon(int state) {
		if(state==INVALID_STATE)
			return new ImageIcon();
		return iconStates.get(state);
	}

	private static List<HashMap<String, ImageIcon>> getRadioButtonHashMaps() {
		List<HashMap<String, ImageIcon>> list = new ArrayList<HashMap<String, ImageIcon>>();

		for (int i = 0; i < 4; i++) {
			list.add(getRadioButtonCornerIconHashMapForState(i));
		}
		list.add(getRadioButtonStandardIconHashMapForState());

		return list;
	}

	private static List<HashMap<String, ImageIcon>> getCheckBoxHashMaps() {
		List<HashMap<String, ImageIcon>> list = new ArrayList<HashMap<String, ImageIcon>>();

		for (int i = 0; i < 4; i++) {
			list.add(getCheckBoxCornerIconHashMapForState(i));
		}
		list.add(getCheckBoxStandardIconHashMapForState());

		return list;
	}

	private static HashMap<String, ImageIcon> getRadioButtonStandardIconHashMapForState() {
		HashMap<String, ImageIcon> hm = new HashMap<String, ImageIcon>();
		hm.put(ICONDEFAULT, loadRadioButtonStandardIcon(ICONDEFAULT));
		hm.put(ICONPRESSED, loadRadioButtonStandardIcon(ICONPRESSED));
		hm.put(ICONROLLOVER, loadRadioButtonStandardIcon(ICONROLLOVER));
		hm.put(ICONSELECTED, loadRadioButtonStandardIcon(ICONSELECTED));
		hm.put(ICONROLLOVERSELECTED,
				loadRadioButtonStandardIcon(ICONROLLOVERSELECTED));
		hm.put(ICONDISABLED, loadRadioButtonStandardIcon(ICONDISABLED));
		hm.put(ICONDISABLEDSELECTED,
				loadRadioButtonStandardIcon(ICONDISABLEDSELECTED));
		return hm;
	}

	private static HashMap<String, ImageIcon> getCheckBoxStandardIconHashMapForState() {
		HashMap<String, ImageIcon> hm = new HashMap<String, ImageIcon>();
		hm.put(ICONDEFAULT, loadCheckBoxStandardIcon(ICONDEFAULT));
		hm.put(ICONPRESSED, loadCheckBoxStandardIcon(ICONPRESSED));
		hm.put(ICONROLLOVER, loadCheckBoxStandardIcon(ICONROLLOVER));
		hm.put(ICONSELECTED, loadCheckBoxStandardIcon(ICONSELECTED));
		hm.put(ICONROLLOVERSELECTED,
				loadCheckBoxStandardIcon(ICONROLLOVERSELECTED));
		hm.put(ICONDISABLED, loadCheckBoxStandardIcon(ICONDISABLED));
		hm.put(ICONDISABLEDSELECTED,
				loadCheckBoxStandardIcon(ICONDISABLEDSELECTED));
		return hm;
	}

	private static HashMap<String, ImageIcon> getRadioButtonCornerIconHashMapForState(
			int iconState) {
		HashMap<String, ImageIcon> hm = new HashMap<String, ImageIcon>();
		hm.put(ICONDEFAULT, loadRadioButtonCornerIcon(ICONDEFAULT, iconState));
		hm.put(ICONPRESSED, loadRadioButtonCornerIcon(ICONPRESSED, iconState));
		hm.put(ICONROLLOVER, loadRadioButtonCornerIcon(ICONROLLOVER, iconState));
		hm.put(ICONSELECTED, loadRadioButtonCornerIcon(ICONSELECTED, iconState));
		hm.put(ICONROLLOVERSELECTED,
				loadRadioButtonCornerIcon(ICONROLLOVERSELECTED, iconState));
		hm.put(ICONDISABLED, loadRadioButtonCornerIcon(ICONDISABLED, iconState));
		hm.put(ICONDISABLEDSELECTED,
				loadRadioButtonCornerIcon(ICONDISABLEDSELECTED, iconState));
		return hm;
	}

	private static HashMap<String, ImageIcon> getCheckBoxCornerIconHashMapForState(
			int iconState) {
		HashMap<String, ImageIcon> hm = new HashMap<String, ImageIcon>();
		hm.put(ICONDEFAULT, loadCheckBoxCornerIcon(ICONDEFAULT, iconState));
		hm.put(ICONPRESSED, loadCheckBoxCornerIcon(ICONPRESSED, iconState));
		hm.put(ICONROLLOVER, loadCheckBoxCornerIcon(ICONROLLOVER, iconState));
		hm.put(ICONSELECTED, loadCheckBoxCornerIcon(ICONSELECTED, iconState));
		hm.put(ICONROLLOVERSELECTED,
				loadCheckBoxCornerIcon(ICONROLLOVERSELECTED, iconState));
		hm.put(ICONDISABLED, loadCheckBoxCornerIcon(ICONDISABLED, iconState));
		hm.put(ICONDISABLEDSELECTED,
				loadCheckBoxCornerIcon(ICONDISABLEDSELECTED, iconState));
		return hm;
	}

	private static ImageIcon loadRadioButtonStandardIcon(String iconName) {
		return loadIcon(RADIOBUTTON_PATH, iconName);
	}

	private static ImageIcon loadCheckBoxStandardIcon(String iconName) {
		return loadIcon(CHECKBOX_PATH, iconName);
	}

	private static ImageIcon loadRadioButtonCornerIcon(String iconName,
			int iconState) {
		return HelperClient.mergeIcons(loadIcon(RADIOBUTTON_PATH, iconName),
				iconStates.get(iconState), 1);
	}

	private static ImageIcon loadCheckBoxCornerIcon(String iconName,
			int iconState) {
		return HelperClient.mergeIcons(loadIcon(CHECKBOX_PATH, iconName),
				iconStates.get(iconState), 1);
	}

	private static ImageIcon loadIcon(String path, String iconName) {
		ImageIcon icon = new ImageIcon(CornerInfoIcon.class.getResource(path
				+ iconName));
		return icon;

	}
}
