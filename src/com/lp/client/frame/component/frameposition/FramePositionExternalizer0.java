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

public class FramePositionExternalizer0 extends FramePositionExternalizer {

	public FramePositionExternalizer0(FramePositionData fpData) {
		super(fpData);
	}
	
	public FramePositionExternalizer0() {
		super();
	}
	
	@Override
	public int getVersionsNr() {
		return 0;
	}

	@Override
	public void readExternalImpl(ObjectInput in) throws IOException,
			ClassNotFoundException {
		int posX = in.readInt();
		int posY = in.readInt();
		int sizeX = in.readInt();
		int sizeY = in.readInt();
		boolean iconified = in.readBoolean();
		boolean maximized = in.readBoolean();
		
		getFpData().setLocation(new Point(posX, posY));
		getFpData().setSize(new Dimension(sizeX, sizeY));
		getFpData().setIconified(iconified);
		getFpData().setMaximized(maximized);
	}

	@Override
	public void writeExternalImpl(ObjectOutput out) throws IOException {
		int posX = getFpData().getLocation().x;
		int posY = getFpData().getLocation().y;
		int sizeX = getFpData().getSize().width;
		int sizeY = getFpData().getSize().height;
		boolean iconified = getFpData().isIconified();
		boolean maximized = getFpData().isMaximized();
		
		out.writeInt(posX);
		out.writeInt(posY);
		out.writeInt(sizeX);
		out.writeInt(sizeY);
		out.writeBoolean(iconified);
		out.writeBoolean(maximized);
	}


}
