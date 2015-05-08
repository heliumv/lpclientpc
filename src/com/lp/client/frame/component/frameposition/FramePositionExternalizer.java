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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class FramePositionExternalizer implements Externalizable {

	private static final long serialVersionUID = 7452644853163364400L;
	
	private FramePositionData fpData = null;
	protected FramePositionExternalizer(FramePositionData fpData) {
		this.fpData = fpData;
	}
	
	public FramePositionExternalizer() {
		this.fpData = new FramePositionData();
	}
	
	private static int getLatestVersionNumber() {
		return 1;
	}

	public static FramePositionExternalizer getLatestExternalizer() throws ClassNotFoundException {
		return getExternalizerOfVersion(getLatestVersionNumber());
	}
	public static FramePositionExternalizer getLatestExternalizer(FramePositionData fpData) throws ClassNotFoundException {
		return  getExternalizerOfVersion(getLatestVersionNumber(), fpData);
	}
	public abstract int getVersionsNr();
	
	public FramePositionData getFpData() {
		return fpData;
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {

		int version = in.readInt();
		if(version != getVersionsNr()) {
			getExternalizerOfVersion(version).readExternalImpl(in);
		} else readExternalImpl(in);
	}

	private static FramePositionExternalizer getExternalizerOfVersion(int version) throws ClassNotFoundException {
		return getExternalizerOfVersion(version, new FramePositionData());
	}
	
	private static FramePositionExternalizer getExternalizerOfVersion(int version, FramePositionData fpData)
			throws ClassNotFoundException {
		switch (version) {
		case 0:
			return new FramePositionExternalizer0(fpData);
		case 1:
			return new FramePositionExternalizer1(fpData);
		default:
			throw new ClassNotFoundException(
					"FramePositionExternalizer mit Version "
							+ version
							+ " existiert nicht oder wurde noch nicht implementiert.");
		}
	}
	

	public abstract void readExternalImpl(ObjectInput in) throws IOException, ClassNotFoundException;

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(getVersionsNr());

		if(fpData == null) throw new IOException("Speichern Fehlgeschlagen. FramePositionData ist null.");
		writeExternalImpl(out);
	}
	
	public abstract void writeExternalImpl(ObjectOutput out) throws IOException;

}
