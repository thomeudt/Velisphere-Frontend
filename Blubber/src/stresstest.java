import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class stresstest {

	public static void main(String[] args) throws Exception {

		Thread bombThread;
		
		bombThread = new Thread(new Bombarder(), "bomb");
		
		ExecutorService B52 = Executors.newFixedThreadPool(100);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.execute(bombThread);
	 	B52.shutdown();
		
	}

}
