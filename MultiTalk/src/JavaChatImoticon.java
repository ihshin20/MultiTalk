import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JLabel;

public class JavaChatImoticon extends JFrame{
	
    private Socket socket; // 연결소켓
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String Members ="";
    private String UserName = "";
	
	private String[] Imoticons = {"imoticon1.JPG" , "imoticon2.JPG", "imoticon3.JPG", "imoticon4.JPG"};
	
	public JavaChatImoticon(String username, SocketManager socketManager, String members) {
		
		
		this.socket = socketManager.getSocket();
		this.oos = socketManager.getObjectOutputStream();
		this.ois = socketManager.getObjectInputStream();
		this.Members = members;
		this.UserName = username;
		
		setBounds(200, 200, 220, 220);
		getContentPane().setLayout(new GridLayout(2, 2, 0, 0));
		
		
		//ImageIcon sendImoticonImg = new ImageIcon("sendImoticon.JPG");
		
		for(int i = 0; i < Imoticons.length; i++) {
			ImageIcon imoticonIcon = new ImageIcon(Imoticons[i]);
			JLabel imoticon = new JLabel(imoticonIcon);
			
			int makeFinal = i;
			imoticon.addMouseListener(new MouseAdapter() {
	  			@Override
	  			public void mouseClicked(MouseEvent e) {
	  				try {
						byte[] imageBytes = ImageUtils.imageToByteArray(new FileInputStream(new File(Imoticons[makeFinal])));
						DataMessage dataMessage = new DataMessage(UserName, imageBytes, Members);
						oos.writeObject(dataMessage);
						dispose();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	  			}
	  		});
			getContentPane().add(imoticon);
		}
		
		setVisible(true);
	}
	

}


