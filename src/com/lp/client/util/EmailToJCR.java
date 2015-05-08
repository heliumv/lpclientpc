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
package com.lp.client.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeMail;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.util.Helper;

public class EmailToJCR extends FileToJCR {

	public static final String MIME = ".eml";

	@Override
	protected List<JCRDocDto> createJCRImpl(File file, JCRDocDto jcr)
			throws IOException {

		Properties props = System.getProperties();
		props.put("mail.host", "smtp.dummydomain.com");
		props.put("mail.transport.protocol", "smtp");

		Session mailSession = Session.getDefaultInstance(props, null);
		InputStream source = new FileInputStream(file);
		MimeMessage message;
		List<JCRDocDto> jcrs = new ArrayList<JCRDocDto>();
		try {

			message = new MimeMessage(mailSession, source);
			jcr.setsName(message.getSubject() == null
					|| message.getSubject().isEmpty() ? "Email" : message
					.getSubject());
			StringBuffer schlagworte = new StringBuffer();

			schlagworte.append(addressArraytoString(message.getFrom()));
			schlagworte.append("TO ");
			schlagworte.append(addressArraytoString(message
					.getRecipients(RecipientType.TO)));

			jcr.setsSchlagworte(schlagworte.toString());
			jcr.setDocPath(new DocPath().add(new DocNodeMail(jcr.getsName())));

			jcrs.addAll(createJCRs(getAttachments(message), jcr));
			jcr.getDocPath().add(new DocNodeFile("original"));
			jcr.setbData(Helper.getBytesFromFile(file));
			jcrs.add(jcr);

		} catch (MessagingException e) {
			return null;
		}
		return jcrs;
	}

	private List<BodyPart> getAttachments(MimeMessage message)
			throws IOException, MessagingException {
		List<BodyPart> parts = new ArrayList<BodyPart>();
		if (message.getContent() instanceof Multipart) {
			Multipart multipart = (Multipart) message.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
					parts.add(bodyPart);
				}
			}
		}
		return parts;
	}

	private List<JCRDocDto> createJCRs(List<BodyPart> parts, JCRDocDto vorlage)
			throws IOException, MessagingException {

		List<JCRDocDto> jcrs = new ArrayList<JCRDocDto>();

		for (BodyPart part : parts) {
			JCRDocDto attachment = new JCRDocDto();
			attachment.setbData(readData(part));

			attachment.setsFilename(part.getFileName());
			attachment.setsMIME(Helper.getMime(part.getFileName()));
			attachment.setsName(Helper.getName(part.getFileName()));

			attachment.setsSchlagworte(vorlage.getsSchlagworte());
			attachment.setDocPath(vorlage.getDocPath().getDeepCopy()
					.add(new DocNodeFile(part.getFileName())));
			jcrs.add(attachment);
		}
		return jcrs;
	}

	private String addressArraytoString(Address[] array) {
		if (array == null)
			return " ";
		StringBuffer s = new StringBuffer();
		for (Address address : array) {
			if (address instanceof InternetAddress)
				s.append(((InternetAddress) address).getAddress() + "; ");
		}
		return s.toString();
	}

	private byte[] readData(BodyPart part) throws IOException,
			MessagingException {
		InputStream is = part.getInputStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

}
