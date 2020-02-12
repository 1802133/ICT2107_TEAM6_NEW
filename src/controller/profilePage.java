package controller;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import listener.Listener;

public class profilePage{

	JPanel contentPane;
	JFrame temp;
	JPanel panelTemp;
	JButton btnSave;
	JLabel lblUser, lblBio;
	JTextField txtUser;
	JLabel lblImage;
	JTextArea textAreaBio;
	private JScrollPane scrollPane;
	private JButton btnUpload;
	String userImage;
//	Image image;
	private JPanel panel;
	ImageIcon myimage;
	MulticastSocket multicastSocket;
	InetAddress multicastGroup;
	String path,bio;
	
	public profilePage() {}

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 * @param multicastGroup 
	 * @param multicastSocket 
	 * @wbp.parser.entryPoint
	 */
	public void changeProf(String user, String userId) {
		JFileChooser chooser = new JFileChooser();
		System.out.println("Profile page says User: " + user + " User ID: " + userId + " in profile");
		
		temp = new JFrame("Change Group Name");
		panelTemp = new JPanel();
		btnSave = new JButton("Save");
		lblUser = new JLabel("User Name: ");
		lblUser.setBounds(49, 202, 146, 26);
		lblBio = new JLabel("Bio: ");
		lblBio.setBounds(79, 249, 116, 26);
		txtUser = new JTextField(user);
		txtUser.setBounds(216, 199, 188, 32);
		txtUser.setColumns(10);
		
		btnSave.setBounds(191,362,103,35);
		temp.getContentPane().setLayout(null);
		temp.getContentPane().add(lblUser);
		temp.getContentPane().add(lblBio);
		temp.getContentPane().add(btnSave);
		temp.getContentPane().add(txtUser);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Profile Picture", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(167, 13, 144, 175);
		temp.getContentPane().add(panel);
		panel.setLayout(null);
		
		List<String> profile = new ArrayList<String>();
		profile = Listener.userProf.get(user);
		if(profile.size()==0) {
			path = null;
			bio = "";
		}
		else {
			path = profile.get(0);
			System.out.println("Path of profile " + user + ": "+ path);
			bio = profile.get(1);
		}
		
		
		if(path == null) {
			lblImage = new JLabel("No Image");
			lblImage.setBounds(6, 28, 132, 140);
		}
		else {
			myimage = new ImageIcon(path);
			Image img = myimage.getImage();
			Image newImg = img.getScaledInstance(132, 140, Image.SCALE_SMOOTH);
			ImageIcon image = new ImageIcon(newImg);
			lblImage.setIcon(image);
		}
		
		panel.add(lblImage);
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(215, 254, 189, 99);
		temp.getContentPane().add(scrollPane);
		
		// Set a bio
		if(bio == null || bio == "") {
			textAreaBio = new JTextArea("Enter your bio here");
			textAreaBio.setWrapStyleWord(true);
			textAreaBio.setLineWrap(true);
		}
		else {
			textAreaBio = new JTextArea(bio);
			textAreaBio.setWrapStyleWord(true);
			textAreaBio.setLineWrap(true);
		}
		scrollPane.setViewportView(textAreaBio);
		
		btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
					path = chooser.getSelectedFile().getAbsolutePath();
					myimage = new ImageIcon(path);
					System.out.println(path.getClass().getSimpleName());
					Image img = myimage.getImage();
					Image newImg = img.getScaledInstance(132, 140, Image.SCALE_SMOOTH);
					ImageIcon image = new ImageIcon(newImg);
					lblImage.setIcon(image);
					lblImage.setText("");
					//TODO: Save this image somewhere
				}
			}
		});
		
		btnUpload.setBounds(279, 143, 141, 35);
		temp.getContentPane().add(btnUpload);
		temp.setVisible(true);
		
		//Size of JFrame
		temp.setSize(496,530);
		temp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		if(user.equals(userId)) {
			textAreaBio.setEditable(true);
			txtUser.setEditable(true);
			btnSave.setVisible(true);
			btnUpload.setVisible(true);
		}
		else {
			textAreaBio.setEditable(false);
			txtUser.setEditable(false);
			btnSave.setVisible(false);
			btnUpload.setVisible(false);
		}
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO save updates to storage thingy.
				try {
//			      BufferedImage bImage = ImageIO.read(new File(chooser.getSelectedFile().getAbsolutePath()));
//			      ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			      ImageIO.write(bImage, "jpg", bos);
//			      byte [] data = bos.toByteArray();
//			      System.out.println(data);
//			      lblUser.setIcon(new ImageIcon(data));
					multicastGroup = InetAddress.getByName(Listener.IP);
					multicastSocket = new MulticastSocket(6789);
					multicastSocket.joinGroup(multicastGroup);
					String message = "UPP:"+userId+"~"+path+"~"+textAreaBio.getText();
					System.out.println(message);
					byte[] buf = message.getBytes();
					DatagramPacket dgpConnected = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
					multicastSocket.send(dgpConnected);
				}catch(Exception e) {
					}
				}
			});
	}
}
