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
package com.lp.client.frame.component.frameposition;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class FramePositionExternalizer1 extends FramePositionExternalizer {

	public FramePositionExternalizer1(FramePositionData fpData) {
		super(fpData);
	}

	public FramePositionExternalizer1() {
		super();
	}

	@Override
	public int getVersionsNr() {
		return 1;
	}

	@Override
	public void readExternalImpl(ObjectInput in) throws IOException,
			ClassNotFoundException {
		Point pos = new Point(in.readInt(), in.readInt());
		Dimension size = new Dimension(in.readInt(), in.readInt());
		Point iconPos = new Point(in.readInt(), in.readInt());
		Dimension iconSize = new Dimension(in.readInt(), in.readInt());
		boolean iconified = in.readBoolean();
		boolean maximized = in.readBoolean();

		if(iconPos.x == Integer.MIN_VALUE) {
			iconPos = null;
		}
		if(iconSize.width == Integer.MIN_VALUE) {
			iconSize = null;
		}

		getFpData().setLocation(pos);
		getFpData().setSize(size);
		getFpData().setIconLocation(iconPos);
		getFpData().setIconSize(iconSize);
		getFpData().setIconified(iconified);
		getFpData().setMaximized(maximized);
	}

	@Override
	public void writeExternalImpl(ObjectOutput out) throws IOException {
		Point pos = getFpData().getLocation();
		Dimension size = getFpData().getSize();
		Point iconPos = getFpData().getIconLocation();
		Dimension iconSize = getFpData().getIconSize();
		boolean iconified = getFpData().isIconified();
		boolean maximized = getFpData().isMaximized();

		if (iconPos == null) {
			iconPos = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		}
		if (iconSize == null) {
			iconSize = new Dimension(Integer.MIN_VALUE, Integer.MIN_VALUE);
		}

		out.writeInt(pos.x);
		out.writeInt(pos.y);
		out.writeInt(size.width);
		out.writeInt(size.height);
		out.writeInt(iconPos.x);
		out.writeInt(iconPos.y);
		out.writeInt(iconSize.width);
		out.writeInt(iconSize.height);

		out.writeBoolean(iconified);
		out.writeBoolean(maximized);
	}
}
