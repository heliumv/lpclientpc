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
package com.lp.client.kueche;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.kueche.service.Kdc100logDto;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.util.Helper;


//CK/AD: Immer 100ms Pause zwischen dem senden einzelner Zeichen einhalten

@SuppressWarnings("static-access")
public class DialogKdc100 extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();
	JEditorPane textPanelLog = new JEditorPane();
	JLabel labelPort = new JLabel();
	JTextField textfieldPort = new JTextField();
	public JButton jButtonStart = new JButton();
	JButton jButtonStop = new JButton();
	JButton jButtonClear = new JButton();
	static CommPortIdentifier portID;
	InputStream ins;
	OutputStream out;
	static SerialPort serss;
	TextField ausgabe;
	Button bt;
	boolean bGeloescht = false;

	ArrayList<String[]> alStifte = new ArrayList<String[]>();
	ArrayList<Kdc100logDto> kdc100 = new ArrayList<Kdc100logDto>();
	byte[] readBuffer = new byte[200000];

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	int pauseNachSenden = 500;

	JScrollPane jScrollPane1 = new JScrollPane();

	public DialogKdc100() throws Throwable {
		myLogger.info("KDC100 Importprogramm gestartet");

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ImageIcon ii = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/scanner16x16.png"));
		setIconImage(ii.getImage());

		setTitle("KDC100");
		String delay = "300";
		try {
			pauseNachSenden = new Integer(delay).intValue();
		} catch (NumberFormatException ex1) {
			// default
			pauseNachSenden = 500;
		}

		Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList
					.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// initPort(portId.getName());
				String port = portId.getName();

				try {
					portID = CommPortIdentifier.getPortIdentifier(port);

					serss = (SerialPort) portID.open("mein_programm", 2000);

					ins = serss.getInputStream();
					out = serss.getOutputStream();
					serss.addEventListener(new commListener());
					serss.notifyOnDataAvailable(true);

					serss.setSerialPortParams(9600, SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					logToTextPane(htmlBlau("Warten auf Antwort der \u00DCbertragungsstation auf "
							+ port));

					// Hole Seriennummer des KDC10
					String s = holeSeriennummer();
					boolean bIstKDC100 = false;
					if (s != null && s.indexOf('@') > 0) {
						s = s.substring(0, s.indexOf('@'));
						bIstKDC100 = true;
					}

					if (bIstKDC100) {
						// Hole anzahl der gespeicherten Barcodes
						String anz = holeAnzahlDerDatensaetze();

						if (anz != null && anz.indexOf('@') > 0) {
							anz = anz.substring(0, anz.indexOf('@'));
						}

						String[] sA = new String[3];
						sA[0] = s;
						sA[1] = port;
						sA[2] = anz;

						alStifte.add(sA);
						// holeUndSpeichereBarcodes();
					}

					serss.close();
					
				}catch (PortInUseException ex) {
					ex.printStackTrace();
				} 
				

			}
		}

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myLogger.info("ZE-Stift Importprogramm gestoppt");
				dispose();
				serss.close();

				setVisible(false);
				dispose();
			}
		});

		setVisible(false);

	}

	public DialogKdc100(String port) throws Throwable {
		myLogger.info("KDC100 Importprogramm gestartet");

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ImageIcon ii = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/scanner16x16.png"));
		setIconImage(ii.getImage());

		setTitle("KDC100");
		String delay = "300";
		try {
			pauseNachSenden = new Integer(delay).intValue();
		} catch (NumberFormatException ex1) {
			// default
			pauseNachSenden = 500;
		}

		// initPort(portId.getName());

		try {
			portID = CommPortIdentifier.getPortIdentifier(port);

			serss = (SerialPort) portID.open("mein_programm", 2000);

			ins = serss.getInputStream();
			out = serss.getOutputStream();
			serss.addEventListener(new commListener());
			serss.notifyOnDataAvailable(true);

			serss.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			logToTextPane(htmlBlau("Warten auf Antwort der \u00DCbertragungsstation auf "
					+ port));

			// Hole Seriennummer des KDC10
			String s = holeSeriennummer();
			boolean bIstKDC100 = false;
			if (s != null && s.indexOf('@') > 0) {
				s = s.substring(0, s.indexOf('@'));
				bIstKDC100 = true;
			}

			if (bIstKDC100) {
				// Hole anzahl der gespeicherten Barcodes

				// Puerfen ob Stift gueltig

				ArbeitsplatzDto apDto = DelegateFactory.getInstance()
						.getParameterDelegate()
						.arbeitsplatzFindByCTypCGeraetecode(
								ParameterFac.ARBEITSPLATZ_TYP_KDC100, s);

				if (apDto != null) {

					holeUndSpeichereBarcodes(s);
				} else {
					DialogFactory
							.showModalDialog(
									"Fehler",
									"Es befindet sich ein unbekannter Stift an '"
											+ port
											+ "'. Bitte setzen Sie sich mit Ihrem Helium V H\u00E4ndler in Verbindung.");
				}

			}
			serss.close();
		}catch (PortInUseException ex) {
			ex.printStackTrace();
		} 

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myLogger.info("ZE-Stift Importprogramm gestoppt");
				dispose();
				serss.close();

				setVisible(false);
				dispose();
			}
		});

		setVisible(false);

	}

	public DialogKdc100(boolean bLeeren, String port) throws Throwable {

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ImageIcon ii = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/scanner16x16.png"));
		setIconImage(ii.getImage());

		setTitle("KDC100");
		String delay = "300";
		try {
			pauseNachSenden = new Integer(delay).intValue();
		} catch (NumberFormatException ex1) {
			// default
			pauseNachSenden = 500;
		}

		// initPort(portId.getName());

		try {
			portID = CommPortIdentifier.getPortIdentifier(port);

			serss = (SerialPort) portID.open("mein_programm", 2000);

			ins = serss.getInputStream();
			out = serss.getOutputStream();
			serss.addEventListener(new commListener());
			serss.notifyOnDataAvailable(true);

			serss.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			logToTextPane(htmlBlau("Warten auf Antwort der \u00DCbertragungsstation auf "
					+ port));

			// Hole Seriennummer des KDC10
			String s = holeSeriennummer();
			boolean bIstKDC100 = false;
			if (s != null && s.indexOf('@') > 0) {
				s = s.substring(0, s.indexOf('@'));
				bIstKDC100 = true;
			}

			if (bIstKDC100) {
				bGeloescht = loescheGespeicherteBarcodes();
				
				
				//Datum Uhrzeit setzten
				setzeDatumUhrzeit();
			}
			serss.close();
		}catch (PortInUseException ex) {
			ex.printStackTrace();
		} 
		

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myLogger.info("ZE-Stift Importprogramm gestoppt");
				dispose();
				serss.close();

				setVisible(false);
				dispose();
			}
		});

		setVisible(false);

	}

	private void warten(int ms) {
		try {
			Thread.currentThread().sleep(ms);
		} catch (InterruptedException ex) {
			logToTextPane(htmlRot(ex.toString()));
		}

	}

	public class sendeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			sendeZeichenkette("P".toCharArray());
		}
	}

	private boolean loescheGespeicherteBarcodes() {
		sendeZeichenkette("E".toCharArray());

		if (readBuffer[0] == 64) {
			return true;
		} else {
			return false;
		}
	}

	private short[] unsignShort(short[] s) {

		for (int i = 0; i < s.length; i++) {
			if (s[i] < 0) {
				s[i] = (short) ((int) s[i] + (int) 256);
			}
		}

		return s;

	}

	

	
	private void holeUndSpeichereBarcodes(String seriennummer) throws Throwable {
		sendeZeichenkette("P".toCharArray());
		short[] laengeByte = new short[3];
		Byte.toString(readBuffer[0]);
		laengeByte[0] = readBuffer[0];
		laengeByte[1] = readBuffer[1];
		laengeByte[2] = readBuffer[2];

		laengeByte=unsignShort(laengeByte);
		
		int laenge = toIntLaenge(laengeByte);
		int i = 3;

		while (laenge > i) {
			int iTeilLaenge = readBuffer[i];
			byte[] cCode = new byte[iTeilLaenge - 5];
			byte bBarcodetyp=readBuffer[i + 1];

			short[] timestamp = new short[4];

			timestamp[0] = readBuffer[i + iTeilLaenge - 3];
			timestamp[1] = readBuffer[i + iTeilLaenge - 2];
			timestamp[2] = readBuffer[i + iTeilLaenge - 1];
			timestamp[3] = readBuffer[i + iTeilLaenge];

			timestamp=unsignShort(timestamp);
			int itemp = 0;
			for (int h = i + 2; h < i + iTeilLaenge - 3; h++) {
				cCode[itemp] = readBuffer[h];
				itemp++;
			}
			i += iTeilLaenge + 1;

			Kdc100logDto kdc10logDto = new Kdc100logDto();
			kdc10logDto.setCBarcode(new String(cCode));
			kdc10logDto.setCSeriennummer(seriennummer);
			// Stiftzeit auslesen

			kdc10logDto.setTStiftzeit(Helper.getKDCTStoTimestamp(timestamp));

			if(bBarcodetyp==-1){
				//PJ 15017 ist als geloescht markiert, auslassen
				continue;
			}
			
			kdc100.add(kdc10logDto);
		}

	}


	private static int toIntLaenge(short b[]) {
		return (b[0] & 0xFF << 16) | ((b[1] & 0xFF) << 8) | ((b[2] & 0xFF));
	}

	private String holeSeriennummer() throws Throwable {
		sendeZeichenkette("M".toCharArray());

		return new String(readBuffer);
	}

	private String holeAnzahlDerDatensaetze() throws Throwable {
		sendeZeichenkette("N".toCharArray());

		int i = readBuffer[3];

		return i + "";
	}

	private void setzeDatumUhrzeit() {
		
		Calendar c=Calendar.getInstance();
		
		char[] chars=new char[7];


		chars[0]='C';
		chars[1]=(char)(c.get(Calendar.YEAR)-2000);
		chars[2]=(char)(c.get(Calendar.MONTH)+1);
		chars[3]=(char)(c.get(Calendar.DATE));
		chars[4]=(char)(c.get(Calendar.HOUR_OF_DAY));
		chars[5]=(char)(c.get(Calendar.MINUTE));
		chars[6]=(char)(c.get(Calendar.SECOND));
		
		
		//sendeZeichenkette(ca);
		
		sendeZeichenMitPause(chars);
		
		
		readBuffer=null;
		}

	
	private void sendeZeichenMitPause(char[] chars) {
		
		for(int i=0;i<chars.length;i++){
			char[] c=new char[1];
			c[0]=chars[i];
			sendeZeichenkette(c);
		}
		
		
	}

	
	private void sendeZeichenkette(char[] chars) {
		readBuffer = new byte[200000];
		/*
		 * try { // AUF EMPFANGEN UMSCHALTEN
		 * serss.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN); } catch
		 * (UnsupportedCommOperationException ex1) { ex1.printStackTrace(); }
		 */
		PrintWriter aus = new PrintWriter(out);
		ausgabe.setText("");
		aus.print(chars);
		aus.flush();
		warten(100);

		logToTextPane(htmlBlau("OUT:" + new String(chars)));
		aus.close();
		warten(pauseNachSenden);
		try {
			// AUF SENDEN UMSCHALTEN
			serss.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_OUT);
			serss.setRTS(true);
		} catch (UnsupportedCommOperationException ex) {
			logToTextPane(htmlRot("" + ex));
		}
	}

		private String htmlBlau(String s) {
		if (s != null) {
			s = "<font color=\"#0000FF\" face=\"Verdana\" size=\"2\">" + s
					+ "</font>";
		}
		return s;
	}

	private String htmlRot(String s) {
		if (s != null) {
			s = "<font color=\"#FF0000\" face=\"Verdana\" size=\"2\">" + s
					+ "</font>";
		}
		return s;
	}

	private void logToTextPane(String s) {
		textPanelLog.setText(s + "\n" + textPanelLog.getText());
		if (textPanelLog.getText() != null
				&& textPanelLog.getText().length() > 30000) {
			textPanelLog.removeAll();
			textPanelLog.setText(s + "\n");
		}
	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		labelPort.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPort.setText("Port:");
		textPanelLog.setEditable(false);
		textfieldPort.setEditable(false);
		jButtonStart.setText("START");
		jButtonStop.setText("STOP");
		jButtonStop
				.addActionListener(new DialogKdc100_jButtonStop_actionAdapter(
						this));
		jButtonClear.setText("CLEAR");
		jButtonClear
				.addActionListener(new DialogKdc100_jButtonClear_actionAdapter(
						this));
		add(panel1);
		jScrollPane.getViewport().add(textPanelLog);
		textPanelLog.setContentType("text/html");

		jButtonStart.addActionListener(new sendeListener());
		// add(ausgabe = new TextField("", 100));
		ausgabe = new TextField("", 100);
		panel1.add(labelPort, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(ausgabe, new GridBagConstraints(0, 6, 5, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(jScrollPane, new GridBagConstraints(0, 7, 5, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(textfieldPort, new GridBagConstraints(2, 5, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(jButtonStop, new GridBagConstraints(2, 0, 3, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
						15, 15, 15, 0), 50, 0));
		panel1.add(jButtonStart, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(15, 15, 15, 0), 50, 0));
		panel1.add(jButtonClear, new GridBagConstraints(3, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						15, 0, 15, 15), 50, 0));

	}

	public void jButtonClear_actionPerformed(ActionEvent e) {
		textPanelLog.removeAll();
		ausgabe.setText("");
	}

	public void jButtonStop_actionPerformed(ActionEvent e) {
		jButtonStop.setText("warten...");
	}

	public class commListener implements SerialPortEventListener {
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

				try {
					while (ins.available() > 0) {
						int numBytes = ins.read(readBuffer);
					}
					String nachricht = new String(readBuffer);
					ausgabe.setText(ausgabe.getText() + nachricht);

				} catch (IOException e) {
					System.out.println("Fehler: " + e);
				}
			}
		}
	}

}

class DialogKdc100_jButtonStop_actionAdapter implements ActionListener {
	private DialogKdc100 adaptee;

	DialogKdc100_jButtonStop_actionAdapter(DialogKdc100 adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonStop_actionPerformed(e);
	}
}

class DialogKdc100_jButtonClear_actionAdapter implements ActionListener {
	private DialogKdc100 adaptee;

	DialogKdc100_jButtonClear_actionAdapter(DialogKdc100 adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonClear_actionPerformed(e);
	}
}
