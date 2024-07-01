import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

public class JavaChatProfileView extends JFrame{
	private JTextField ProfileMessage;
	private byte[] ProfileImage;
	private BufferedImage B_image;
	private ObjectOutputStream oos;
	private byte[] imageBytes;
	
	public JavaChatProfileView(SocketManager socketManager, String name, byte[] profileImage, String profileMessage) {
		getContentPane().setLayout(null);
		setBounds(100, 100, 300, 282);
		
		this.oos = socketManager.getObjectOutputStream();
		this.ProfileImage = profileImage;
		ImageIcon Profile_Image;
		//BufferedImage B_image
		try {
			B_image = ImageUtils.byteArrayToImage(ProfileImage);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		
		
		Profile_Image = new ImageIcon(B_image);
		JLabel P_Img = new JLabel(Profile_Image);
		
		P_Img.addMouseListener(new MouseAdapter() {
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

		            try {
		                BufferedImage originalImage = ImageIO.read(selectedFile);

		                int width = originalImage.getWidth();
		                int height = originalImage.getHeight();

		                if (width > 0 && height > 0) {
		                    Image resizedImage = originalImage.getScaledInstance(55, 55, Image.SCALE_SMOOTH);

		                    BufferedImage resizedBufferedImage = new BufferedImage(
		                            55,
		                            55,
		                            BufferedImage.TYPE_INT_RGB
		                    );

		                    Graphics g = resizedBufferedImage.createGraphics();
		                    g.drawImage(resizedImage, 0, 0, null);
		                    g.dispose();

		                    // 여기서 resizedBufferedImage를 사용하거나 바이트 배열로 변환할 수 있음

		                    // BufferedImage를 바이트 배열로 변환
		                    imageBytes = ImageUtils.bufferedImageToByteArray(resizedBufferedImage);

		                    // 이제 imageBytes를 사용하여 원하는 작업을 수행할 수 있음

		                    // JLabel에 이미지 설정
		                    P_Img.setIcon(new ImageIcon(resizedBufferedImage));
		                } else {
		                    System.err.println("이미지 크기를 가져올 수 없습니다.");
		                }
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
		        }
		    }
		});


		
		P_Img.setBounds(88, 10, 100, 100);
		getContentPane().add(P_Img);
		
		JLabel P_name = new JLabel(name);
		P_name.setHorizontalAlignment(SwingConstants.CENTER);
		P_name.setVerticalAlignment(SwingConstants.CENTER);
		P_name.setBounds(98, 115, 93, 34);
		getContentPane().add(P_name);
		
		if(profileMessage == null) {
			profileMessage = " ";
		}
		
		ProfileMessage = new JTextField();
		ProfileMessage.setBounds(12, 159, 262, 34);
		getContentPane().add(ProfileMessage);
		ProfileMessage.setText(profileMessage);
		//ProfileMessage.setColumns(10);
		
		JButton registbtn = new JButton("수정하기");
		registbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String P_msg = ProfileMessage.getText();
				DataMessage dataMessage = new DataMessage(name, P_msg, imageBytes);
				System.out.println(imageBytes);
				try {
					oos.writeObject(dataMessage);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
			
		});
		registbtn.setBounds(98, 203, 91, 23);
		getContentPane().add(registbtn);
		
		setVisible(true);
	}
}
