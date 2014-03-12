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
package com.lp.client.zutritt;


import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

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
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;

@SuppressWarnings("static-access")
public class DialogReadFingerprint
    extends JDialog implements ActionListener
{

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
  JButton jButtonStart = new JButton();

  JButton jButtonZurueck = new JButton();

  static CommPortIdentifier portID;
  InputStream ins;
  OutputStream out;
  static SerialPort serss;
  TextField ausgabe;

  TextField textfieldMeldung = new TextField();

  boolean bFertig = false;
  byte[] rueckgabe = null;

  byte[] template1 = null;
  byte[] template2 = null;

  int pauseNachSenden = 1000;
  JScrollPane jScrollPane1 = new JScrollPane();

  public byte[] getTemplate1() {
    return template1;
  }


  public byte[] getTemplate2() {
    return template2;
  }


  public DialogReadFingerprint(InternalFrame internalFrame) throws Throwable {
    super(LPMain.getInstance().getDesktop(), true);
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    ArbeitsplatzparameterDto parameter = DelegateFactory.getInstance().
        getParameterDelegate().
        holeArbeitsplatzparameter(ParameterFac.
                                  ARBEITSPLATZPARAMETER_PORT_FINGERPRINTLESER);

    if (parameter !=null && parameter.getCWert() != null) {
      textfieldPort.setText(parameter.getCWert());
    } else {
      textfieldPort.setText("Bitte Arbeitsplatzparameter 'PORT_FINGERPRINTLESER' setzen.");
    }




    setTitle(LPMain.getInstance().getTextRespectUISPr("pers.zutritt.fingereinlesen"));

    this.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        dispose();
        if (serss != null) {
          serss.close();
        }
        setVisible(false);
      }
    });

  }


  final public void actionPerformed(ActionEvent e) {
    if (serss != null) {
      serss.close();
    }
    this.setVisible(false);
  }


  private void warten(int ms) {
    try {
      Thread.currentThread().sleep(ms);
    }
    catch (InterruptedException ex) {
      logToTextPane(htmlRot(ex.toString()));
    }

  }


  private void initPort(String port) {
    try {
      portID = CommPortIdentifier.getPortIdentifier(port);

      serss = (SerialPort) portID.open("mein_programm", 2000);

      ins = serss.getInputStream();
      out = serss.getOutputStream();
      serss.addEventListener(new commListener());
      serss.notifyOnDataAvailable(true);

      serss.setSerialPortParams(115200,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
    }
    catch (UnsupportedCommOperationException ex) {
      logToTextPane(htmlRot(ex + ""));
    }
    catch (TooManyListenersException ex) {
      logToTextPane(htmlRot(ex + ""));
    }
    catch (IOException ex) {
      logToTextPane(htmlRot(ex + ""));
    }
    catch (PortInUseException ex) {
      logToTextPane(htmlRot("Port '" + port + "' derzeit in Verwendung"));
    }
    catch (NoSuchPortException ex) {
      logToTextPane(htmlRot("Port '" + port + "' nicht vorhanden."));
    }

  }


  private void sendeZeichenkette(char[] chars) {

    PrintWriter aus = new PrintWriter(out);
    ausgabe.setText("");
    aus.print(chars);
    aus.flush();
    warten(50);
    System.out.print("OUT:");
    for (int i = 0; i < 13; i++) {
      System.out.print(rechneNachHex(chars[i]));
    }
    aus.close();
    warten(pauseNachSenden);
  }


  private String htmlBlau(String s) {
    if (s != null) {
      s = "<font color=\"#0000FF\" face=\"Verdana\" size=\"2\">" + s + "</font>";
    }
    return s;
  }


  private String htmlRot(String s) {
    if (s != null) {
      s = "<font color=\"#FF0000\" face=\"Verdana\" size=\"2\">" + s + "</font>";
    }
    return s;
  }


  private void logToTextPane(String s) {
    textPanelLog.setText(s + "\n" + textPanelLog.getText());
    if (textPanelLog.getText() != null &&
        textPanelLog.getText().length() > 30000) {
      textPanelLog.removeAll();
      textPanelLog.setText(s + "\n");
    }
    textPanelLog.repaint();
  }


  private void jbInit()
      throws Exception {
    panel1.setLayout(gridBagLayout1);
    labelPort.setHorizontalAlignment(SwingConstants.RIGHT);
    labelPort.setText("Port:");
    textPanelLog.setEditable(false);
    jButtonStart.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.fingereinlesen"));
    jButtonZurueck.setText(LPMain.getInstance().getTextRespectUISPr("lp.uebernehmen"));

    jButtonZurueck.addActionListener(this);

    jButtonStart.addActionListener(new
                                   DialogReadFingerprint_jButtonEinlesen_actionAdapter(this));
    add(panel1);
    jScrollPane.getViewport().add(textPanelLog);
    textPanelLog.setContentType("text/html");

    ausgabe = new TextField("", 100);

    panel1.add(labelPort, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.HORIZONTAL,
                                                 new Insets(2, 2, 2, 2), 0, 0));
    panel1.add(ausgabe, new GridBagConstraints(0, 6, 5, 1, 1.0, 0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0,
                                               0));
    panel1.add(jScrollPane, new GridBagConstraints(0, 7, 5, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
        0));
    panel1.add(textfieldPort, new GridBagConstraints(2, 4, 3, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    panel1.add(textfieldMeldung, new GridBagConstraints(2, 5, 3, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    panel1.add(jButtonStart, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 15, 15, 0), 50,
        0));
    panel1.add(jButtonZurueck, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 15, 15, 0), 50,
        0));
  }


  public void jButtonEinlesen_actionPerformed(ActionEvent e) {
    try {
      //PORT initialisieren
      jButtonStart.setEnabled(false);

      initPort(textfieldPort.getText());

      char[] zeichen = new char[13];

      //ZUERST ALLE TEMPLATES AUS LESER LOeSCHEN
      zeichen[0] = 0x40;
      zeichen[1] = 0x17;
      zeichen[2] = 0x00;
      zeichen[3] = 0x00;
      zeichen[4] = 0x00;
      zeichen[5] = 0x00;
      zeichen[6] = 0x00;
      zeichen[7] = 0x00;
      zeichen[8] = 0x00;
      zeichen[9] = 0x00;
      zeichen[10] = 0x00;
      zeichen[11] = 0x00;
      zeichen[12] = 0x0A;


      logToTextPane(htmlBlau("Warte auf Antwort"));

      sendeZeichenkette(berechneChecksumme(zeichen));
      while (bFertig == false) {
        //warten
      }
      bFertig = false;



      DialogFactory.showModalDialog("Achtung", "Bitte Finger 2x einlesen");
      this.repaint();

      //ENROLL BY SCAN starten, d.h. der Finger muss 2 mal ueber den Leser gefueht werden, danach wir verglichen

      zeichen[0] = 0x40;
      zeichen[1] = 0x05;
      zeichen[2] = 0x01;
      zeichen[3] = 0x00;
      zeichen[4] = 0x00;
      zeichen[5] = 0x00;
      zeichen[6] = 0x00;
      zeichen[7] = 0x00;
      zeichen[8] = 0x00;
      zeichen[9] = 0x00;
      zeichen[10] = 0x00;
      zeichen[11] = 0x46;
      zeichen[12] = 0x0A;

      sendeZeichenkette(berechneChecksumme(zeichen));

      //1ter Finger

      while (bFertig == false) {
        //warten
      }
      bFertig = false;
      byte[] finger1scan_success = rueckgabe;



      if (finger1scan_success[10] != 0x62 && finger1scan_success[10] != 0x61) {
        DialogFactory.showModalDialog("Achtung",
                                      "Finger nicht erfolgreich eingelesen, bitte starten sie den Vorgang erneut.");
        throw new Exception("Finger nicht erfolgreich eingelesen");
      }
      while (bFertig == false) {
        //warten
      }
      byte[] finger1_success = rueckgabe;
      bFertig = false;
        if (finger1_success[10] != 0x61 && finger1_success[10] != 0x62) {
          DialogFactory.showModalDialog("Achtung",
                                        "Finger nicht erfolgreich verabreitet, bitte starten sie den Vorgang erneut.");
          throw new Exception("Finger nicht erfolgreich verarbeitet");
        }

      //2ter finger


      while (bFertig == false) {
        //warten
      }
      bFertig = false;
      byte[] finger2scan_success = rueckgabe;
      int iQualitaet2 = finger2scan_success[6];
      logToTextPane(htmlBlau("Score: "+iQualitaet2));
      if (finger2scan_success[10] != 0x62 && finger2scan_success[10] != 0x61) {
        //Erster Finger NOT_SUCCESS
        System.out.println("Finger 2 FEHLER");
      }
      else {
        System.out.println("Finger 2 SCAN_SUCCESS");
      }

      while (bFertig == false) {
        //warten
      }
      byte[] finger2_success = rueckgabe;
      bFertig = false;
      try {
        if (finger2_success[10] != 0x61 && finger2_success[10] != 0x62) {
          DialogFactory.showModalDialog("Achtung",
              "Finger nicht erfolgreich eingelesen, bitte starten sie den Vorgang erneut. Score:"+iQualitaet2);
          throw new Exception("Finger nicht erfolgreich eingelesen");
        }
      }
      catch (ArrayIndexOutOfBoundsException ex1) {
        int i = 0;
      }
      //NUN Template auslesen
      zeichen[0] = 0x40;
      zeichen[1] = 0x14;
      zeichen[2] = 0x01;
      zeichen[3] = 0x00;
      zeichen[4] = 0x00;
      zeichen[5] = 0x00;
      zeichen[6] = 0x00;
      zeichen[7] = 0x00;
      zeichen[8] = 0x00;
      zeichen[9] = 0x00;
      zeichen[10] = 0x00;
      zeichen[11] = 0x46;
      zeichen[12] = 0x0A;
      zeichen = berechneChecksumme(zeichen);
      sendeZeichenkette(zeichen);

      while (bFertig == false) {
        //warten
      }
      byte[] template = rueckgabe;

      //INPUT zerlegen
      template1 = new byte[384];
      template1[383] = 0x00;
      for (int i = 13; i < 396; i++) {
        template1[i - 13] = template[i];
      }

      template2 = new byte[384];
      template2[383] = 0x00;
      for (int i = 411; i < 794; i++) {
        template2[i - 411] = template[i];
      }



      DialogFactory.showModalDialog("Achtung", "Templates erzeugt. Score: " + iQualitaet2);

      /*     //Check User
            zeichen[0] = 0x40;
            zeichen[1] = 0x19;
            zeichen[2] = 0x01;
            zeichen[3] = 0x00;
            zeichen[4] = 0x00;
            zeichen[5] = 0x00;
            zeichen[6] = 0x00;
            zeichen[7] = 0x10;
            zeichen[8] = 0x00;
            zeichen[9] = 0x00;
            zeichen[10] = 0x00;
            zeichen[11] = 0x00;
            zeichen[12] = 0x0A;
            zeichen = berechneChecksumme(zeichen);
            sendeZeichenkette(zeichen);

            while (bFertig == false) {
              //warten
         }

         //Check User

          zeichen[0] = 0x40;
          zeichen[1] = 0x66;
          zeichen[2] = 0x01;
          zeichen[3] = 0x00;
          zeichen[4] = 0x00;
          zeichen[5] = 0x00;
          zeichen[6] = 0x00;
          zeichen[7] = 0x10;
          zeichen[8] = 0x00;
          zeichen[9] = 0x00;
          zeichen[10] = 0x00;
          zeichen[11] = 0x00;
          zeichen[12] = 0x0A;
          zeichen = berechneChecksumme(zeichen);
          sendeZeichenkette(zeichen);

          while (bFertig == false) {
            //warten
         }

           //Nun noch Bild einlesen

         zeichen[0] = 0x40;
         zeichen[1] = 0x84;
         zeichen[2] = 0x00;
         zeichen[3] = 0x00;
         zeichen[4] = 0x00;
         zeichen[5] = 0x00;
         zeichen[6] = 0x00;
         zeichen[7] = 0x10;
         zeichen[8] = 0x00;
         zeichen[9] = 0x00;
         zeichen[10] = 0x00;
         zeichen[11] = 0x00;
         zeichen[12] = 0x0A;
         zeichen = berechneChecksumme(zeichen);
         sendeZeichenkette(zeichen);

         while (bFertig == false) {
           //warten
         }
              byte[] bild = new byte[384];
              bild[383] = 0x00;
              for (int i = 13; i < 396; i++) {
                bild[i - 13] = rueckgabe[i];
           }*/

    }
    catch (Exception ex) {
      ex.printStackTrace();
      if (serss != null) {
        serss.close();
      }

      DialogFactory.showModalDialog("Achtung", "Bitte Vorgang neu starten.");

      textfieldMeldung.setText(htmlRot(ex.getMessage()));
      jButtonStart.setEnabled(true);

    }
    serss.close();
    jButtonStart.setEnabled(true);

  }


  private static String rechneNachHex(int dezimal) {
    String ergebnis = "";
    String hexarray[] = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F"};
    int rest = Math.abs(dezimal);
    int aktbasiszahl = 0;
    if (rest > 0) {
      while (rest > 0) {
        aktbasiszahl = rest % 16;
        rest = (rest / 16);
        ergebnis = hexarray[aktbasiszahl] + ergebnis;
      }
    }
    else {
      ergebnis = "00";
    }
    if (ergebnis.length() == 1) {
      ergebnis = "0" + ergebnis;
    }

    return ergebnis;
  }


  private char[] berechneChecksumme(char[] string) {
    int chksum = -1;
    chksum = 0;
    for (int i = 0; i < 11; i++) {
      char c = string[i];
      chksum += c;
//        chksum = chksum & 255;
    }
    chksum = chksum % 256;
    string[11] = (char) chksum;
    string[12] = 0x0A;

    return string;
  }


  public class commListener
      implements SerialPortEventListener
  {
    public void serialEvent(SerialPortEvent event) {
      if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
        System.out.println("IN:");
        byte[] readBuffer = new byte[900];
        try {
          int numBytes = 0;
          while (ins.available() > 0) {
            numBytes = ins.read(readBuffer);
          }
          String nachricht = new String(readBuffer);
          ausgabe.setText(ausgabe.getText() + nachricht);
          rueckgabe = new byte[numBytes];
          for (int i = 0; i < numBytes; i++) {
            rueckgabe[i] = readBuffer[i];
            System.out.println("Zeichen " + i + " " + rechneNachHex(readBuffer[i]));
          }
          bFertig = true;
        }
        catch (IOException e) {
          System.out.println("Fehler: " + e);
        }
      }
    }
  }

}


class DialogReadFingerprint_jButtonEinlesen_actionAdapter
    implements ActionListener
{
  private DialogReadFingerprint adaptee;
  DialogReadFingerprint_jButtonEinlesen_actionAdapter(DialogReadFingerprint adaptee) {
    this.adaptee = adaptee;
  }


  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonEinlesen_actionPerformed(e);
  }
}
