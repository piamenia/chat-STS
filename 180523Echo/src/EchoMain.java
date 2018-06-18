import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoMain {

	public static void main(String[] args) {
		try {
			// TCP 서버 소켓 만들기
			ServerSocket ss = new ServerSocket(10001);
			while(true) {
				System.out.println("대기중...");
				// 클라이언트와의 연결 대기
				Socket socket = ss.accept();
				// 클라이언트 정보 출력
				InetAddress addr = socket.getInetAddress();
				System.out.println(addr.toString());
				
				// 클라이언트와 문자열을 주고받을 스트림
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line = br.readLine();
				System.out.println("메시지: "+line);
				
				PrintWriter pw = new PrintWriter(socket.getOutputStream());
				pw.print(line);
				pw.flush();
				
				// 사용한 스트림과 소켓 닫기
				pw.close();
				br.close();
				socket.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
