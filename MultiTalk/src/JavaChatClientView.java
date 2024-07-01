//JavaChatClientView
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import javax.swing.JTextPane;

public class JavaChatClientView extends JFrame{
    private JPanel contentPane;
    private JTextField txtInput;
    private String UserName;
    private JButton btnSend;
//    private JTextArea textArea;
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

    private JLabel lblUserName;
    private Socket socket; // 연결소켓
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private SocketManager socketManager;
    private String Members = "";
    private JTextPane textPane;
    private StyledDocument doc;
    //private String[] Imoticons = {"imoticon1.JPG, imoticon2.JPG, imoticon3.JPG, imoticon4.JPG"};

	/**
	 * Create the frame.
	 */
	public JavaChatClientView(String username, SocketManager socketmanager, String[] members) {
		
		this.socketManager = socketmanager;
		this.socket = socketManager.getSocket();
		this.oos = socketManager.getObjectOutputStream();
		this.ois = socketManager.getObjectInputStream();
		this.UserName = username;

		
		
		for(int i = 0; i < members.length; i++) {
			Members += members[i]+ " ";
		}

		
		setTitle(Members+ " 채팅방");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 392, 631);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 352, 449);
		contentPane.add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setBackground(new Color(155, 187, 212));
		
		scrollPane.setViewportView(textPane);
		
		doc = textPane.getStyledDocument();

		txtInput = new JTextField();
		txtInput.setBounds(81, 469, 283, 65);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("전송");
		btnSend.setBounds(290, 544, 76, 40);
		contentPane.add(btnSend);
		
		lblUserName = new JLabel("Name");
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(12, 469, 67, 40);
		contentPane.add(lblUserName);
		setVisible(true);
	
		//AppendText("User " + UserName + " connected \n");
		//UserName = username;
		lblUserName.setText(UserName + ">");
		
//		textArea = new JTextArea();
//		textArea.setBounds(12, 280, 350, 69);
//		contentPane.add(textArea);
//		textArea.setEditable(false);
		
		ImageIcon sendPictureImg = new ImageIcon("sendImage.JPG");
		JLabel SendPictureIcon = new JLabel(sendPictureImg);
		SendPictureIcon.setBounds(12, 533, 57, 51);
		SendPictureIcon.addMouseListener(new MouseAdapter() {
  			@Override
  			public void mouseClicked(MouseEvent e) {
  				JFileChooser fileChooser = new JFileChooser();
  		        fileChooser.setDialogTitle("이미지 선택");
  		        
  		        String defaultDirectory = "C:/eclipse_java/Images";
  		        fileChooser.setCurrentDirectory(new File(defaultDirectory));
  	        
  		        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

  		        int result = fileChooser.showOpenDialog(getParent());

  		        if (result == JFileChooser.APPROVE_OPTION) {
  		            File selectedFile = fileChooser.getSelectedFile();
  		            byte[] imageBytes = ImageUtils.convertImageToByteArray(selectedFile.getAbsolutePath());
  		            DataMessage dataMessage = new DataMessage(UserName, imageBytes, Members);
  		            try {
						oos.writeObject(dataMessage);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
  		            //System.out.println("Selected File: " + selectedFile.getAbsolutePath());
  		        }
  			}
  		});
		
		contentPane.add(SendPictureIcon);
		
		ImageIcon sendImoticonImg = new ImageIcon("sendImoticon.JPG");
		JLabel SendImoticonIcon = new JLabel(sendImoticonImg);
		SendImoticonIcon.setBounds(69, 533, 57, 51);
		SendImoticonIcon.addMouseListener(new MouseAdapter() {
  			@Override
  			public void mouseClicked(MouseEvent e) {
  				JavaChatImoticon ImoticonView = new JavaChatImoticon(UserName, socketManager, Members);
  			}
  		});
		contentPane.add(SendImoticonIcon);

        Myaction action = new Myaction();
        btnSend.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
        txtInput.addActionListener(action);
        txtInput.requestFocus();

    }
	

	

	
//	public void readMessage(DataMessage dataMessage) {
//		try {
//			DataMessage msg = (DataMessage) ois.readObject();
//			
////			if(msg.getDataType() == DataType.MSG) {
////				String checkMessage = msg.getMemberData();
////				if(checkMessage.equals(Members)) {
//			if(msg.getDataType() == DataType.MSG) {
//				AppendText(msg.getStringData());
//			}
//					
////				}
////			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}




	// keyboard enter key 치면 서버로 전송
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				msg = String.format("%s %s\n", UserName, txtInput.getText());
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

    // 화면에 출력
//    public void AppendText(String msg) {
//        textArea.append(msg);
//        textArea.setCaretPosition(textArea.getText().length());
//    }

//    public void AppendText(String msg) {
//    	addTextWithBackgroundColor(doc, msg, Color.YELLOW);
//        textArea.append(msg);
//        textArea.setCaretPosition(textArea.getText().length());
//    }
    
    public void AppendMyText(String msg) {
    	String[] removeMyName = msg.split(" ");
    	StringBuilder removedNameText = new StringBuilder();
        for (int i = 1; i < removeMyName.length; i++) {
        	removedNameText.append(removeMyName[i]);
            if (i < removeMyName.length - 1) {
            	removedNameText.append(" ");
            }
        }
        String MyMessage = removedNameText.toString();
        addMyTextWithBackgroundColor(doc, MyMessage, new Color(254, 240, 27));
//        textArea.append(msg);
//        textArea.setCaretPosition(textArea.getText().length());
    }
    
    public void AppendFriendsText(String msg) {
    	String[] SplitMsg = msg.split(" ");
    	StringBuilder removedNameText = new StringBuilder();
        for (int i = 1; i < SplitMsg.length; i++) {
        	removedNameText.append(SplitMsg[i]);
            if (i < SplitMsg.length - 1) {
            	removedNameText.append(" ");
            }
        }
        String FriendName = SplitMsg[0]+ "\n";
        String FriendMessage = removedNameText.toString();
    	
        addFrinedsTextWithBackgroundColor(doc, FriendName, FriendMessage, Color.WHITE);
//        textArea.append(msg);
//        textArea.setCaretPosition(textArea.getText().length());
    }
    
	private void addMyTextWithBackgroundColor(StyledDocument doc, String msg, Color bgColor) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBackground(attrs, bgColor);
        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
        try {
        	int length = doc.getLength();
            doc.setParagraphAttributes(length, 1, attrs, false);
        	//doc.setParagraphAttributes(length, length, attrs, false);
            doc.insertString(doc.getLength(), msg, attrs);
            
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
	
	private void addFrinedsTextWithBackgroundColor(StyledDocument doc, String name, String msg, Color bgColor) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBackground(attrs, bgColor);
        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
        try {
        	int length = doc.getLength();
            doc.setParagraphAttributes(length, 1, attrs, false);
        	doc.insertString(doc.getLength(), name, null);
            doc.insertString(doc.getLength(), msg, attrs);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
	
//	public void addMyImage(byte[] imageBytes) {
//		byte[] ImageBytes = imageBytes;
//
//        // 바이트 배열을 이미지로 변환
//        try {
//			BufferedImage receivedImage = ImageUtils.byteArrayToImage(ImageBytes);
//			Icon icon = new ImageIcon(receivedImage);
//			JLabel imageLabel = new JLabel(icon);
//			//imageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
//			SimpleAttributeSet attrs = new SimpleAttributeSet();
//            StyleConstants.setComponent(attrs, imageLabel);
//            StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
//            //Document doc = textPane.getStyledDocument();
//            int length = doc.getLength();
//            doc.setParagraphAttributes(length, 1, attrs, false);
//            //doc.setParagraphAttributes(length, 1, attrs, false);
//            
//            doc.insertString(doc.getLength(), " ", attrs);
//            doc.insertString(doc.getLength(), "\n", null);
//		} catch (IOException | BadLocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void addMyImage(byte[] imageBytes) {
		byte[] ImageBytes = imageBytes;

        // 바이트 배열을 이미지로 변환
        try {
			BufferedImage receivedImage = ImageUtils.byteArrayToImage(ImageBytes);
			//Icon icon = new ImageIcon(receivedImage);
			Image originalImage = receivedImage;
			Image resizedImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
			Icon icon = new ImageIcon(resizedImage);
			
	        SimpleAttributeSet attr = new SimpleAttributeSet();
	        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
	        doc.setParagraphAttributes(doc.getLength(), 1, attr, false);
	        StyleConstants.setIcon(attr, icon);
	        
	        textPane.getStyledDocument().insertString(doc.getLength(), " ", attr);
	        
            doc.insertString(doc.getLength(), "\n", null);
		} catch (IOException | BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addFriendsImage(String friendName, byte[] imageBytes) {
		byte[] ImageBytes = imageBytes;

        // 바이트 배열을 이미지로 변환
        try {
        	BufferedImage receivedImage = ImageUtils.byteArrayToImage(ImageBytes);
        	Image originalImage = receivedImage;
			Image resizedImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
			Icon icon = new ImageIcon(resizedImage);
			
	        SimpleAttributeSet attr = new SimpleAttributeSet();
	        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
	        doc.setParagraphAttributes(doc.getLength(), 1, attr, false);
	        doc.insertString(doc.getLength(), friendName+"\n", null);
	        StyleConstants.setIcon(attr, icon);
	        
	        textPane.getStyledDocument().insertString(doc.getLength(), " ", attr);
	        
	        doc.insertString(doc.getLength(), "\n", null);
		} catch (IOException | BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
//	public void addFriendsImage(byte[] imageBytes) {
//		byte[] ImageBytes = imageBytes;
//
//        // 바이트 배열을 이미지로 변환
//        try {
//			BufferedImage receivedImage = ImageUtils.byteArrayToImage(ImageBytes);
//			Icon icon = new ImageIcon(receivedImage);
//			JLabel imageLabel = new JLabel(icon);
//			SimpleAttributeSet attrs = new SimpleAttributeSet();
//            StyleConstants.setComponent(attrs, imageLabel);
//            //StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
//            //Document doc = textPane.getStyledDocument();
//            int length = doc.getLength();
//            doc.setParagraphAttributes(length, length, attrs, false);
//            doc.insertString(doc.getLength(), " ", attrs);
//            doc.insertString(doc.getLength(), "\n", null);
//		} catch (IOException | BadLocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

   

    // Server에게 network으로 전송
    public void SendMessage(String msg) {
        try {
            // Use writeUTF to send messages
        	DataMessage Msg = new DataMessage(msg, Members);
            oos.writeObject(Msg);
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
}
