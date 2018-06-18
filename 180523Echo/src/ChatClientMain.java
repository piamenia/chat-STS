
public class ChatClientMain {

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		Thread th = new Thread(client);
		th.start();
	}
}
