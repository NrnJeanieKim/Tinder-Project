import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;

public class TChat extends JFrame implements Runnable{
	Container cp;
	JPanel pNorth, pCenter, pSouth;
	//JTextArea ta; �̰� ���� �Ⱦ�
	JPanel chatPanel;/////////////
	JScrollPane scroll;//////////////
	JScrollPane sp;
	JLabel Id;
    JTextField tf;
	JButton bBack, cSend, bReport; //�ڷ� ����, �޽��� ������, �Ű� ��ư
	ImageIcon ii1, ii2, ii3;
	String ip = "127.0.0.1";
	int port = 5000;
	Socket s;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
    DataInputStream dis;
	String line;
	String report = "[ ��Ӿ�, ���� ���� ������ �Ű� ó���Ǿ����ϴ�.\n 5ȸ ������ ������ �����˴ϴ�.\n 3�� �� ��ȭ�� ����˴ϴ�.]";
	String myId = Login.loginid;
	String yourId = "���Ǿ��̵�";
	//////////////////
	InetAddress inetAddress;
	DatagramSocket datagramSocket = null;
	DatagramPacket datagramPacket = null;
	DatagramPacket datagramPacket2 = null;
	byte[] buffer = new byte[512];
	MulticastSocket multicastSocket = null;
	int first = 1; //static���� �����..........�������״¾ȶ߳�..????
	static final long serialVersionUID = 1L;//////////////////



	TChat(){

		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//System.out.println("ID�� �Է��ϼ��� : ");
		//try{
		//	myId = br.readLine();
		//}catch(IOException ie){}
		// init();
		inetAddress = null;
		  try{
			  inetAddress = InetAddress.getByName("224.1.1.0"); // 224.0.0.0 to 239.255.255.255 ���� ����ؾ� ��Ƽ���� ��...
		  }catch(UnknownHostException ue){
			  System.out.println("�ùٸ��� ���� ������");}
		 try {
			 //Socket ����
				datagramSocket = new DatagramSocket();
		   System.out.println("*********** " + myId + "�� ���� ***********");

	   } catch (IOException e) {
		e.printStackTrace();
	  }
	  Thread th = new Thread(this);
	  th.start();
	}
	void init(){
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 74, 3));
		pNorth = new JPanel(); pCenter = new JPanel(); pSouth = new JPanel();
		pNorth.setLayout(new FlowLayout());
		pNorth.setBackground(Color.white);
		pCenter.setLayout(new FlowLayout());
		pCenter.setBackground(Color.white);
		pSouth.setLayout(new BorderLayout());
		pSouth.setBackground(Color.white);
		chatPanel = new JPanel();
		chatPanel.setBackground(Color.white);
		loadImageIcon();

		bBack = new JButton(ii1);
		pNorth.add(bBack);
		bBack.setBorderPainted(false);
		bBack.setFocusPainted(false);
		bBack.setContentAreaFilled(false);

		//��ȭ�ϴ� ���� ���̵� �߰� �ؾ� ��! (���� �� ��)
		Id = new JLabel("Chat ID");
		pNorth.add(Id);

		bReport = new JButton(ii2);
		pNorth.add(bReport);
		getContentPane().add(pNorth, BorderLayout.NORTH);
		bReport.setBorderPainted(false);
		bReport.setFocusPainted(false);
		bReport.setContentAreaFilled(false);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.PAGE_AXIS));
		chatPanel.add(Box.createVerticalGlue());

		sp = new JScrollPane(chatPanel);
		//sp.setViewportView(chatPanel);
		sp.setPreferredSize(new Dimension(390, 350));
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {  
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}
		});
		pCenter.add(sp);
		getContentPane().add(pCenter, BorderLayout.CENTER);

		tf = new JTextField();
		tf.setColumns(34);
		pSouth.add(tf, BorderLayout.NORTH);
		tf.setEnabled(true);
		tf.requestFocus();

		cSend = new JButton(ii3);
		pSouth.add(cSend, BorderLayout.SOUTH);
		getContentPane().add(pSouth, BorderLayout.SOUTH);
		getContentPane().setVisible(true);
		cSend.setBorderPainted(false);
		cSend.setFocusPainted(false);
		cSend.setContentAreaFilled(false);

		setUI();
	}
	void setUI(){
		ActionListener listener = new ChatHandler();
		bBack.addActionListener(listener);
		bReport.addActionListener(listener);
		cSend.addActionListener(listener);
		tf.addActionListener(listener);
		always();
	}
	void always(){
		setTitle("Tinder? Tinder!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(400, 540);
		setLocation(500, 100);
		setVisible(true);
		getContentPane().setBackground(Color.white);
	}
	void loadImageIcon(){
		try{
			BufferedImage bi = ImageIO.read(new File("imgs/back3.png"));
			ii1 = new ImageIcon(bi);
			BufferedImage bi2 = ImageIO.read(new File("imgs/report4.png"));
			ii2 = new ImageIcon(bi2);
			BufferedImage bi3 = ImageIO.read(new File("imgs/send4.png"));
			ii3 = new ImageIcon(bi3);
		}catch(IOException ie){
		}
	}

	void append(String str) {
		LeftArrowBubble leftArrowBubble = new LeftArrowBubble();

		final int size = 500;
		leftArrowBubble.setMaximumSize(new Dimension(size, size));

		JLabel tac = new JLabel();

		tac.setMaximumSize(new Dimension(size - 50, size - 50));

		final int maximumSize = 56;
		String textWithSeparators = "";
		final StringTokenizer textTokenizer = new StringTokenizer(str, " \t\n\r", true);

		while(textTokenizer.hasMoreTokens()) {
			final String part = textTokenizer.nextToken();
			for (int beginIndex = 0; beginIndex < part.length();
				 beginIndex += maximumSize)
				textWithSeparators += (beginIndex == 0 ? "" : " ")
					+ part.substring(beginIndex,
									 Math.min(part.length(),
											  beginIndex + maximumSize));
		}
		System.out.println(textWithSeparators);

		tac.setText("<html><body style='width:" + (size - 265) + "px;padding:15px;display:block;'>"
						+ textWithSeparators + "</body></html>");

		tac.setOpaque(false);
		leftArrowBubble.add(tac, BorderLayout.NORTH);

		chatPanel.add(leftArrowBubble);

		chatPanel.add(Box.createRigidArea(new Dimension(0,5)));
		Rectangle rect = chatPanel.getBounds();
		Rectangle r2 = sp.getViewport().getVisibleRect();
		chatPanel.scrollRectToVisible(new Rectangle((int) rect.getWidth(),
				(int) rect.getHeight(), (int) r2.getWidth(), (int) r2.getHeight()));
		revalidate();
		repaint();
	}

	void appendR(String str) {
		RightArrowBubble rightArrowBubble = new RightArrowBubble();

		final int size = 400;
		rightArrowBubble.setMaximumSize(new Dimension(size, size));

		JLabel tac = new JLabel();

		tac.setMaximumSize(new Dimension(size - 50, size - 50));

		final int maximumSize = 56;
		String textWithSeparators = "";
		final StringTokenizer textTokenizer = new StringTokenizer(str, " \t\n\r", true);

		while(textTokenizer.hasMoreTokens()) {
			final String part = textTokenizer.nextToken();
			for (int beginIndex = 0; beginIndex < part.length();
				 beginIndex += maximumSize)
				textWithSeparators += (beginIndex == 0 ? "" : " ")
					+ part.substring(beginIndex,
									 Math.min(part.length(),
											  beginIndex + maximumSize));
		}
		System.out.println(textWithSeparators);

		tac.setText("<html><body style='width:" + (size - 200) + "px;padding:15px;display:block;'>"
						+ textWithSeparators + "</body></html>");

		tac.setOpaque(false);
		rightArrowBubble.add(tac, BorderLayout.NORTH);

		chatPanel.add(rightArrowBubble);

		chatPanel.add(Box.createRigidArea(new Dimension(0,5)));
		Rectangle rect = chatPanel.getBounds();
		Rectangle r2 = sp.getViewport().getVisibleRect();
		chatPanel.scrollRectToVisible(new Rectangle((int) rect.getWidth(),
				(int) rect.getHeight(), (int) r2.getWidth(), (int) r2.getHeight()));
		revalidate();
		repaint();
	}

	public void run(){
		byte[] buffer2 = new byte[512];
		try{
			multicastSocket = new MulticastSocket(port);// 2. DatagramPacket�� �ޱ� ���� Socket ����
			multicastSocket.joinGroup(inetAddress);// 3. �׷� ��� - ��� �����ϰ� ��
			int popCount=0;
			while(true) {	// �޽��� ��� ����
	// 4. Data�� ���� Packet ����
				datagramPacket = new DatagramPacket(buffer2, buffer2.length);
	// 5. ��Ƽĳ��Ʈ�� �����ϴ� �޽��� ����
				//datagramSocket.receive(datagramPacket2);
				multicastSocket.receive(datagramPacket);
	// 6. ���ŵ� �޽��� ���

				String msg = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
				if(msg.startsWith(myId)){
					popCount++;
					System.out.println(myId);
				}if (msg.contains("pop")&&popCount==0){
					System.out.println("whose");
					new AskYes(this);
					popCount++;
					continue;
				}if (msg.contains("chat")&&popCount==0){
					System.out.println("Chat");
					init();
					continue;
				}
				if (msg.startsWith(myId)){
					first++;
				}else if (first ==1){
					// new AskYes();
					append(msg);
					first++;
				}else if(msg.equals(report)){
					try{
						//ta.append("[ ��Ӿ�, ���� ���� ������ �Ű� ó���Ǿ����ϴ�.\n 5ȸ ������ ������ �����˴ϴ�.\n 3�� �� ��ȭ�� ����˴ϴ�.]");
						append("[ ��Ӿ�, ���� ���� ������ �Ű� ó���Ǿ����ϴ�.\n 5ȸ ������ ������ �����˴ϴ�.\n 3�� �� ��ȭ�� ����˴ϴ�.]");
						Thread.sleep(3000);
						System.exit(0);
					}catch(InterruptedException ie2){}
				}else{
					append(msg);
					//first++;
				}
			}
		}catch (IOException e) {
			System.err.println(e);
			System.exit(0);
			} finally {
				multicastSocket.close();
			}
	}

	 public static void main(String[] args) {
		 new TChat();
	 }

	 class ChatHandler implements ActionListener { ///����Ŭ������ ������ ����
		 public void actionPerformed(ActionEvent e){
			Object obj = e.getSource();
			if(obj == bBack){
				System.exit(0);
			}else if(obj == cSend){
				line = tf.getText().trim();
				tf.setText("");
				if(line.length() != 0 && !(line.equals(""))){
					try{
						appendR(myId+" : "+line+"\n"); //chat ID �տ� ä�� ���� �ʰ�
						line = myId+" : "+line;
						buffer = line.getBytes();
						datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
						datagramSocket.send(datagramPacket);
					}catch(IOException ie){System.out.println("send ����");}
				}
			}else if (obj == tf){
				line = tf.getText().trim();
				tf.setText("");
				if(line.length() != 0 && !(line.equals(""))){
					try{
						appendR(myId+" : "+line+"\n"); //chat ID �տ� ä�� ���� �ʰ�
						line = myId+" : "+line;
						buffer = line.getBytes();
						datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
						datagramSocket.send(datagramPacket);
					}catch(IOException ie){System.out.println("send ����");}
				}
			}
			else if(obj == bReport){
				int answer = JOptionPane.showConfirmDialog(null, "�Ű��Ͻðڽ��ϱ�?\n(�Ű��� ��ȭ ������ ����Ʈ�Ǹ�, ��ȭ�� ����˴ϴ�.)",
					"�Ű��ϱ�", JOptionPane.OK_CANCEL_OPTION);
				if(answer == JOptionPane.YES_OPTION){
					JOptionPane.showMessageDialog(null, "�Ű��� �Ϸ�Ǿ����ϴ�. ��ȭ�� �����մϴ�.", "�Ű��Ϸ�",
						JOptionPane.INFORMATION_MESSAGE);
					//���� ��ȭâ�� ����
					try{
						buffer = report.getBytes();
						datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
						datagramSocket.send(datagramPacket);
						}catch(IOException ie){
					}finally{
						 System.exit(0);
					}
				}
			}
		 }
	 }
	 void pop(){
		 try{
			String pop= myId+": pop";
		 	buffer = pop.getBytes();
		 	datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
		 	datagramSocket.send(datagramPacket);
			//AskYes ay = new AskYes(this);
			// ay.start();
		 }catch(IOException ie){
		 }
	 }
 }
 class AskYes extends JFrame implements Runnable{ //������ ��ȭ�� �������� ��, ��ȭ�� �ųİ� ����� Ŭ����
	int yesNo;
	TChat tc ;
	AskYes(){
		new Thread(this).start();
		}
	AskYes(TChat tc){
		this.tc = tc;
		new Thread(this).start();
	}
	 public void run(){
		yesNo = JOptionPane.showConfirmDialog(this, "���ƿ��� ������ ��ȭ ��û�� �ֽ��ϴ�. Ȯ���� �����ø� ��ȭâ���� �̵��մϴ�.", "����", JOptionPane.OK_CANCEL_OPTION);
		if(yesNo==0){
			tc.init();
		}
	}
}
////��𼱰� datagramSocket.close();�������.�ٺ����������ݾƾ���...