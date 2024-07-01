
import java.io.Serializable;

// 데이터 타입을 정의하는 열거형
enum DataType {
    MSG, FRIENDS, ROOM, IMAGE, PROFILE
}

// 데이터를 포함하는 메시지 클래스
public class DataMessage implements Serializable {
	private static final long serialVersionUID = 1L;
    private DataType dataType;
    private String stringData;
    private String Members;
    private String User;
    private byte[] ImageBytes;
    

    // 친구 목록을 전송할 때 사용하는 생성자
    public DataMessage(String friends) {
        this.dataType = DataType.FRIENDS;
        this.stringData = friends;
    }

    // 메세지 전송할 때 사용하는 생성자
    public DataMessage(String msg, String members) {
        this.dataType = DataType.MSG;
        this.stringData = msg;
        this.Members = members;
    }
    
    public DataMessage(String room, String members, int i) {
    	this.dataType = DataType.ROOM;
    	this.stringData = room;
    	this.Members = members;
    	
    }
    
    public DataMessage(String user, byte[] imageBytes, String members) {
    	this.dataType = DataType.IMAGE;
    	this.User = user;
    	this.ImageBytes = imageBytes;
    	this.Members = members;
    }
    
    public DataMessage(String user, String p_msg, byte[] p_imageByte) {
    	this.dataType = DataType.PROFILE;
    	this.User = user;
    	this.stringData = p_msg;
    	this.ImageBytes = p_imageByte;
    	
    }

    // Getter 메서드
    public DataType getDataType() {
        return dataType;
    }

    public String getFriendsData() {
        return stringData;
    }

    public String getStringData() {
        return stringData;
    }
    
    public String getMemberData() {
    	return Members;
    }
    
    public byte[] getImageData() {
    	return ImageBytes;
    }
    
    public String getUserData() {
    	return User;
    }
    

}