import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public class ImageUtils {
	    public static byte[] imageToByteArray(InputStream inputStream) throws IOException {
	        BufferedImage bufferedImage = ImageIO.read(inputStream);
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
	        return byteArrayOutputStream.toByteArray();
	    }
	    
	    public static BufferedImage byteArrayToImage(byte[] imageData) throws IOException {
	        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
	        return ImageIO.read(byteArrayInputStream);
	    }
		    
	    public static byte[] convertImageToByteArray(String imagePath) {
	        try {
	            // 파일 경로로부터 Path 객체 생성
	            Path path = new File(imagePath).toPath();

	            // Files 클래스를 사용하여 파일의 내용을 byte 배열로 읽어옴
	            return Files.readAllBytes(path);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    public static byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ImageIO.write(image, "png", byteArrayOutputStream);
	        return byteArrayOutputStream.toByteArray();
	    }
	

	}
