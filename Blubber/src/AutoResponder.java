
public class AutoResponder {
	
	public static void main(String[] args) {
	ServerParameters.my_queue_name = "ping";
	System.out.println("Autoresponding on Queue Name " + ServerParameters.my_queue_name);
	Thread t = new Thread(new Pong(), "ponger");
	t.start();
		
}

}
