import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class JavaChatSelectView extends JFrame{
	
    private String[] FriendsArray;
    private JPanel panel;
    private List<JCheckBox> checkBoxList = new ArrayList<>();
    private Vector<String> usersExMe = new Vector<String>();
    private String SelectedFriends = "";
    private ObjectOutputStream oos;
    private ObjectInputStream ios;
    private Socket socket;
    private SocketManager socketManager;
    
    
	public JavaChatSelectView(String username, Vector<String> UsersExMe, SocketManager socketManager) {
		
		this.socketManager = socketManager;
		this.socket = socketManager.getSocket();
		this.oos = socketManager.getObjectOutputStream();
		this.ios = socketManager.getObjectInputStream();
		
		setBounds(300, 300, 261, 329);
		this.usersExMe = UsersExMe;
		getContentPane().setBackground(new Color(255, 255, 255));
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(18, 39, 207, 213);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("친구 선택");
		lblNewLabel.setBackground(new Color(255, 255, 255));
		lblNewLabel.setBounds(18, 10, 93, 27);
		getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("채팅방 생성");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < checkBoxList.size(); i++) {
				    JCheckBox checkBox = checkBoxList.get(i);
				    if (checkBox.isSelected()) {
				        SelectedFriends += checkBox.getText() + " ";
				       
				    }
				}
				SelectedFriends += username;
				DataMessage createRoom = new DataMessage("/CreateRoom", SelectedFriends);
				try {
					oos.writeObject(createRoom);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		btnNewButton.setBounds(57, 262, 122, 23);
		getContentPane().add(btnNewButton);
		
		setBox();
		
		setVisible(true);
	}
	
	public void setBox() {
		
		int yIndex = 0;

		for (int j = 0; j < usersExMe.size(); j++) {
			
		    String name = usersExMe.get(j);
		    
		    JCheckBox checkBox = new JCheckBox(name);
		    checkBox.setBounds(0, yIndex, 100, 20);
		    checkBox.setBackground(Color.WHITE);		    
		    checkBoxList.add(checkBox);
		    yIndex+= 25;

		    panel.add(checkBox);

      }
		
	}
	


}
