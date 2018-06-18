import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 서버가 사용할 스레드 
// 접속할 클라이언트 각각 동작할 스레드 
public class ChatHandler extends Thread {

	Socket socket;
	BufferedReader br;
	PrintWriter pw;

	// 서버에 대한 포인터
	ChatServer server;

	// 생성자
	// server와 socket을 받아서 대입하고 br과 pw를 생성
	// throws 예외를 호출하는 곳에서 처리하도록 설정
	public ChatHandler(ChatServer server, Socket socket) throws Exception {
		this.server = server;
		this.socket = socket;

		// 소켓으로부터 전송되어 온 문자열을 읽을 수 있는 스트림 생성
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// 소켓에게 문자열을 전송할 스트림 생성
		pw = new PrintWriter(socket.getOutputStream());

	}

	// 스레드로 수행할 내용 작성
	public void run() {
		String name = "";
		try {
			// 처음 전송돼 온 데이터는 이름으로 설정
			name = br.readLine();
			// 이름을 모든 클라이언트에게 전송
			broadcast(name + "님이 입장하셨습니다.");
			// 클라이언들에게서 메시지가 오면 모든 클라이언트에게 전송
			while(true) {
				String msg = br.readLine();
				broadcast(name + " : " + msg);
			}
		} catch (Exception e) {
			System.out.println(name + "님이 퇴장하셨습니다.");
		} finally {
			// 현재 클라이언트를 리스트에서 제거
			server.handlers.remove(this);
			try {
				if(br != null) br.close();
				if(pw != null) pw.close();
				if(socket != null) socket.close();
			}catch(Exception e) {
				
			}
		}
	}
	
	// 모든 클라이언트에게 메시지를 전송하는 메소드
	public void broadcast(String message) {
		// 블럭 내에서는 handlers를 동시에 수정할 수 없도록 설정
		synchronized(server.handlers) {
			// 리스트의 데이터 개수
			// 반복문에서 메소드의 리턴값을 계속 사용하는 경우에는 스택에 저장해두고 사용하는게 좋음
			int n = server.handlers.size();
			for(int i = 0; i<n; i=i+1) {
				// 모든 클라이언트를 1개씩 가져오기
				ChatHandler handler = server.handlers.get(i);
				// 클라이언트에게 메시지 전송을 동기적으로 처리
				synchronized(handler) {
					// 메시지 전송
					handler.pw.println(message);
				}
				// 버퍼의 내용을 비워서 전부 전송
				handler.pw.flush();
			}
		}
	}
}