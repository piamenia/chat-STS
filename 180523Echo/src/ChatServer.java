import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// 채팅 서버 클래스
public class ChatServer {
	// 클라이언트들을 저장할 List
	ArrayList<ChatHandler> handlers;
	
	// 생성자
	public ChatServer(int port) {
		try {
			ServerSocket ss = new ServerSocket(port);
			handlers = new ArrayList<ChatHandler>();
			System.out.println("서버 준비 완료");
			
			while(true) {
				// 클라이언트 접속 대기
				Socket client = ss.accept();
				// 클라이언트 IP 출력
				System.out.println("접속한 클라이언트: " + client.getInetAddress().toString());
				// 클라이언트 스레드 생성
				ChatHandler chatHandler = new ChatHandler(this, client);
				// 리스트에 추가
				handlers.add(chatHandler);
				// 스레드 시작
				chatHandler.start();
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// 프로그램 시작
	public static void main(String[] args) {
		new ChatServer(9999);
	}
}