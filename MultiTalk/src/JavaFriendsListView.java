//JavaFriendsListView
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class JavaFriendsListView extends JFrame {
	
	private JPanel contentPane;
	private String username;
	private String port_no;
	private JLabel bottomIcon1 = new JLabel();
	private JButton createChatRoom = new JButton();
	private JLabel title = new JLabel("      친구");
	
	private ImageIcon icon1 = new ImageIcon("friendicon.png");
	private ImageIcon icon2 = new ImageIcon("chaticon1.png");
	//private ImageIcon icon3 = new ImageIcon("chatRoom.png");
	private ImageIcon icon1Pressed = new ImageIcon("friendiconPressed.png");
	private ImageIcon icon1_1Pressed = new ImageIcon("chaticonPressed.png");
	private ImageIcon iconCreateChatRoom = new ImageIcon("채팅방생성.png");
	
    private JTextField txtInput;
    private String UserName;
    private JButton btnSend;
    private JTextArea textArea;
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private Socket socket; // 연결소켓
    private InputStream is;
    private OutputStream os;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
    private JLabel lblUserName;
    private String[] FriendsArray;
    private SocketManager socketManager;
    
    
    private JavaChatClientView chatView;
    private JPanel FriendsPanel;
    //private JScrollPane FriendsPanel;
    private JPanel RoomsPanel;
    
    private JavaChatSelectView selectView;
    private final JLabel bottomIcon1_1 = new JLabel();
    private Vector<String> Rooms = new Vector<String>();
    private int cur = 0;
    private String MembersString = "";

    private Map<String, String> ProfileMessageMap = new HashMap<>();
    private Map<String, byte[]> ProfileImageMap = new HashMap<>();
    private byte[] defaultProfileImage;

    

    public JavaFriendsListView(String username, String ip_addr, String port_no) {
    	this.UserName = username;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 374, 600);
		setTitle(username);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(236,236,236));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		createChatRoom = new JButton(iconCreateChatRoom);
		createChatRoom.setBorderPainted(false);
		createChatRoom.setBackground(new Color(255, 255, 255));
		createChatRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Vector<String> UsersExMe = new Vector<String>();
            	for(int i = 0; i < FriendsArray.length; i++) {
            		if(!FriendsArray[i].equals(username)) {
            			UsersExMe.add(FriendsArray[i]);
            		}
            	}
            	selectView = new JavaChatSelectView(username, UsersExMe, socketManager);  	
            	
            }
        });
		

		
		
		
		createChatRoom.setBounds(291, 10, 57, 33); 
		//createChatRoom.addMouseListener(new MyMouseListener());
		
		bottomIcon1.setIcon(icon1Pressed);
		bottomIcon1.setBounds(82, 488, 70, 72);
		contentPane.add(bottomIcon1);
		bottomIcon1.addMouseListener(new MouseAdapter() {
  			@Override
  			public void mouseClicked(MouseEvent e) {
  				bottomIcon1.setIcon(icon1Pressed);
  				bottomIcon1_1.setIcon(icon2);
  				
  				cur = 0;
  				if(RoomsPanel != null) {
  	  				RoomsPanel.removeAll();
  				}
  				RefeshFriends();
  			}
  		}); 
		
		
		title.setFont(new Font("돋움체", Font.BOLD, 14));
		title.setBounds(0, 0, 374, 50);
		title.setOpaque(true); 
		title.setBackground(new Color(255,255,255));
		
		contentPane.add(createChatRoom);
		contentPane.add(title);
		
  		bottomIcon1_1.setIcon(icon2);
  		bottomIcon1_1.setBounds(213, 488, 82, 72);
  		contentPane.add(bottomIcon1_1);
  		
  		bottomIcon1_1.addMouseListener(new MouseAdapter() {
  			@Override
  			public void mouseClicked(MouseEvent e) {
  				bottomIcon1.setIcon(icon1); 
				bottomIcon1_1.setIcon(icon1_1Pressed);
				
  				cur=1;
  				if(FriendsPanel != null) {
  	  				FriendsPanel.removeAll();
  				}
  				makeRoom(MembersString);
  			}
  		});

      setVisible(true);
      

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
      	os = socket.getOutputStream();
          is = socket.getInputStream();
          oos = new ObjectOutputStream(os);
          ois = new ObjectInputStream(is);
          socketManager = new SocketManager(socket, oos, ois);


          SendMyName("/login " + username);
          ListenNetwork net = new ListenNetwork();
          net.start();

      } catch (NumberFormatException | IOException e) {
          e.printStackTrace();
          //AppendText("connect error");
      }
		
    }
    
    public void SendMyName(String msg) {
        try {
        	DataMessage message = new DataMessage(msg);
            oos.writeObject(message);
        } catch (IOException e) {
            //AppendText("dos.write() error");
            try {
                oos.close();
                ois.close();
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
        }
    }
    
    class ListenNetwork extends Thread {
    	
        public void run() {
            while (true) {
                try {

                	Object object = ois.readObject();
                	if(object instanceof DataMessage) {
                		DataMessage datamessage = (DataMessage)object;
                		DataType dataType = datamessage.getDataType();
                    	if(dataType == DataType.MSG) {
                    		String msg = datamessage.getStringData();
                    		if(chatView != null) {
                    			String[] CheckSendUser = msg.split(" ");
                    			String sendUser = CheckSendUser[0];
                    			if(sendUser.equals(UserName)) {
                    				chatView.AppendMyText(msg);
                    			}else {
                    				chatView.AppendFriendsText(msg);
                    			}
                    		}

                    	}else if(dataType == DataType.FRIENDS) {
                    		String Friends = datamessage.getFriendsData();
                    		FriendsArray = Friends.split(" ");
                    		if(cur == 0) {
                    			RefeshFriends();
                    		}
//                    		if(ProfileImageMap.containsKey(UserName)) {
//                    			String p_msg = ProfileMessageMap.get(UserName);
//                    			byte[] p_img = ProfileImageMap.get(UserName);
//                    			DataMessage dataMessage = new DataMessage(UserName, p_msg, p_img);
//                    			oos.writeObject(dataMessage);
//                    		}
                    		
                    	}else if(dataType == DataType.ROOM) {
                    		Rooms.add(datamessage.getStringData()); 
                    		MembersString = datamessage.getMemberData();
                    		if(cur == 1) {
                    			makeRoom(MembersString);
                    		}
                    		
                    	}else if(dataType == DataType.IMAGE) {
                    		byte[] ImageBytes = datamessage.getImageData();
                    		if(chatView != null) {
                    			String friendsName = datamessage.getUserData();
                        		if(friendsName.equals(UserName)) {
                        			chatView.addMyImage(ImageBytes);
                        		}else {
                        			chatView.addFriendsImage(friendsName, ImageBytes);
                        		}
                    		}
                    	}else if(dataType == DataType.PROFILE){
                    		String p_name = datamessage.getUserData();
                    		String p_msg = datamessage.getStringData();
                    		byte[] p_img = datamessage.getImageData();
                    		for(int i = 0; i < FriendsArray.length; i++) {
                    			if(p_name.equals(FriendsArray[i])) {
                    				ProfileMessageMap.put(p_name, p_msg);
                    				ProfileImageMap.put(p_name, p_img);
                    			}
                    		}
                    		if(cur == 0) {
                    			RefeshFriends();
                    		}
                    		
                    	}
                	

                	}
                    
                } catch (IOException | ClassNotFoundException e) {
                    //AppendText("dis.read() error");
                    try {
                    	oos.close();
                        ois.close();
                        socket.close();
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }
    }
    

    
    public void makeRoom(String members) {
    	
    	if(RoomsPanel != null) {
    		RoomsPanel.removeAll();
    	}
    	
    	RoomsPanel = new JPanel();
    	RoomsPanel.setBounds(0, 51, 360, 400);
  		contentPane.add(RoomsPanel);
  		RoomsPanel.setLayout(null);
    	
		int yIndex = 0;
		
		
			for (int j = 0; j < Rooms.size(); j++) {
				
			    String name = Rooms.get(j);
		  		JLabel profile = new JLabel();
		        JLabel bottomLine = new JLabel();
		        JLabel chatRoom = new JLabel(name);
		        
		        chatRoom.addMouseListener(new MouseAdapter() {
		  			@Override
		  			public void mouseClicked(MouseEvent e) {
		  				String[] Members = members.split(" ");
		  				chatView = new JavaChatClientView(UserName, socketManager, Members);
		  			}
		  		});
		        
		        
		        
		  		ImageIcon profileIcon = new ImageIcon("chatRoom.png");
		
		  		profile.setIcon(profileIcon);
		  		profile.setBounds(0, yIndex, 100, 100);
		  		profile.setHorizontalAlignment(JLabel.CENTER);
		      	
		  		chatRoom.setBounds(100, yIndex, 370, 80);
		      	
		      	bottomLine.setOpaque(true);
		      	bottomLine.setBackground(new Color(210,210,210));
		      	bottomLine.setBounds(8, yIndex+80, 360, 1);
		      		      	
		      	RoomsPanel.add(profile);
		      	RoomsPanel.add(chatRoom);
		      	RoomsPanel.add(bottomLine);
      
		      	yIndex += 70;
		      	
		      	
		      	
	      }
			
			RoomsPanel.repaint();
    	
    }



public void RefeshFriends() {
	
		if(FriendsPanel != null) {
			FriendsPanel.removeAll();
		}
		
		
		FriendsPanel = new JPanel();
		FriendsPanel.setBounds(0, 51, 360, 400);
		contentPane.add(FriendsPanel);
		FriendsPanel.setLayout(null);


		int yIndex = 0;

		for (int j = 0; j < FriendsArray.length; j++) {
			
			String name = FriendsArray[j];
			
			JLabel profile = new JLabel();
			JLabel bottomLine = new JLabel();
			JLabel newUser = new JLabel(name);
			JLabel ProfileMessageLable = new JLabel(""); 
			ImageIcon profileIcon;
			
			
			 if(ProfileMessageMap.containsKey(name)) {
					byte[] p_img = ProfileImageMap.get(name);
					 String p_msg = ProfileMessageMap.get(name);
					 profileIcon = new ImageIcon(p_img);
					 ProfileMessageLable = new JLabel(p_msg);
			 }else {
				 profileIcon = new ImageIcon("프로필.png");
			 }
	  		
	  		newUser.addMouseListener(new MouseAdapter() {
	  			@Override
	  			public void mouseClicked(MouseEvent e) {
	  				String profileName = newUser.getText();
	  				if(profileName.equals(UserName)) {
	  					if(ProfileImageMap.containsKey(UserName)) {
	  						JavaChatProfileView profileView = new JavaChatProfileView(socketManager, UserName, ProfileImageMap.get(UserName), ProfileMessageMap.get(UserName));
	  					}else {
	  						
	  						try {
	  							defaultProfileImage = ImageUtils.imageToByteArray(new FileInputStream(new File("프로필.png")));
								JavaChatProfileView profileView = new JavaChatProfileView(socketManager, UserName, defaultProfileImage, "");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	  						
	  					}
	  					
	  				}
	  			}
	  		}); 
	  		
	
	  		profile.setIcon(profileIcon);
	  		profile.setBounds(0, yIndex, 100, 100);
	  		profile.setHorizontalAlignment(JLabel.CENTER);
	      	
	      	newUser.setBounds(85, yIndex+15, 100, 20);
	      	
	      	bottomLine.setOpaque(true);
	      	bottomLine.setBackground(new Color(210,210,210));
	      	bottomLine.setBounds(8, yIndex+80, 360, 1);
	      		
	      	ProfileMessageLable.setBounds(95, yIndex+40, 250, 30);
	      	
	      	FriendsPanel.add(profile);
	      	FriendsPanel.add(newUser);
	      	FriendsPanel.add(bottomLine); 
	      	FriendsPanel.add(ProfileMessageLable);

	        
	      	yIndex += 70;
   	
      }

		FriendsPanel.repaint();

      	
  }

    
}



