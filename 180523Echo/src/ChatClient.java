import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// swing 클라이언트로 사용할 스레드 클래스
// 윈도우화면을 사용하기위해 JFrame 상속
// Thread로 동작하기 위해 Runnable 인터페이스 구현
// 버튼 클릭이벤트 처리를 위해 ActionListener 구현
public class ChatClient extends JFrame implements Runnable, ActionListener {
	// 서버와 읽고쓰기를 하기위한 스트림
	BufferedReader br;
	PrintWriter pw;

	// 서버와 통신할 스레드
	Thread handler;

	// 컴포넌트를 배치하기 위한 Container 변수
	Container c;
	// 대화내용을 표시할 컨트롤
	JTextArea display;
	// 대화명을 입력할 텍스트필드
	JTextField id;
	// 메시지를 입력할 텍스트필드
	JTextField inData;
	// 채팅창에 대화명을 표시할 레이블
	JLabel displayId;
	// 메시지를 전송하기 위한 버튼
	JButton send;
	// 하나의 화면을 카드처럼 만들기 위한 레이아웃
	CardLayout window;

	// 생성자 - 화면 생성
	public ChatClient() {
		super("채팅 클라이언트");

		c = getContentPane();
		window = new CardLayout();
		c.setLayout(window);

		// 첫번쨰 화면 - 대화명 설정 창
		JPanel login = new JPanel(new BorderLayout());
		JPanel bottom = new JPanel();
		JLabel idLabel = new JLabel("로그인");

		// 아이디 입력필드의 생성
		id = new JTextField(15);
		// 텍스트필드에서 엔터를 누르면 사용할 메소드의 위치
		id.addActionListener(this);

		// 로그인창의 컴포넌트 배치
		bottom.add(idLabel);
		bottom.add(id);
		login.add("South", bottom);
		c.add("login", login);

		// 두번째 화면 - 채팅창
		JPanel chat = new JPanel(new BorderLayout());
		// 채팅내용을 출력할 컴포넌트
		display = new JTextArea(10, 30);
		// 화면 영역을 넘어갈 가능성이 있는 컴포넌트는 스크롤을 추가해야함
		JScrollPane s = new JScrollPane(display);
		chat.add("Center", s);
		// 대화표시 화면에 임의로 입력 금지
		display.setEditable(false);
		// 채팅창의 하단화면
		JPanel mess = new JPanel();
		// 메시지 입력창
		mess.add(new JLabel("메시지"));
		inData = new JTextField(20);
		mess.add(inData);
		inData.addActionListener(this);
		// 버튼
		send = new JButton("보내기");
		mess.add(send);
		send.addActionListener(this);

		// 채팅창의 컴포넌트 배치
		chat.add("South", mess);

		// 아이디 출력화면
		displayId = new JLabel();
		chat.add("North", displayId);

		c.add("chat", chat);

		// 카드 레이아웃의 첫 화면 설정
		window.show(c, "login");

		// 크기와 좌표 설정
		setSize(400, 400);
		setLocation(100, 100);
		setVisible(true);

	}

	// 버튼이나 텍스트필드에서 엔터를 눌렀을 때 호출되는 메소드
	@Override
	public void actionPerformed(ActionEvent e) {
		// id란에서 엔터를 눌렀을 때
		if(e.getSource() == id) {
			// 입력된 문자열 가져오기
			String name = id.getText().trim();
			id.setText("");
			displayId.setText(name);
			// 입력한 내용이 없으면 아무일도 하지 않음
			if(name == null || name.length() == 0) {
				return;
			}
			// 입력한 내용이 있으면 서버로 전송
			pw.println(name);
			pw.flush();
			// 채팅창 출력
			window.show(c,  "chat");
			// 채팅 메시지 입력란에 포커스
			inData.requestFocus();
			
		// send 버튼을 누르거나 메시지입력란에서 엔터를 눌렀을 때
		}else if(e.getSource() == inData || e.getSource() == send) {
			// 입력한 내용을 서버에 전송
			pw.println(inData.getText().trim());
			pw.flush();
			inData.setText("");
			inData.requestFocus();
		}

	}

	// 스레드로 수행할 내용
	@Override
	public void run() {
		try {
			// 소켓 생성
			Socket s = new Socket("192.168.0.218", 9999);
			// 서버와 문자열을 주고받을 스트림
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());

			// 메시지를 받을 메소드를 호출
			execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// 메시지를 받아서 화면에 출력하는 메소드
	public void execute() {
		try {
			while (true) {
				String line = br.readLine();
				display.append(line + "\n");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// 예외가 발생하면 stop() 메소드 호출
			stop();
		}
	}

	// 스트림을 닫는 메소드
	public void stop() {
		try {
			if (br != null)
				br.close();
			if (pw != null)
				pw.close();
		} catch (Exception e) {}
	}
}
