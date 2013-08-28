import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class stresstest {

	public static void main(String[] args) throws Exception {

		
		int numworkers = 100;
		
		ExecutorService B52 = Executors.newFixedThreadPool(1000);
		Bombarder[] bombThread = new Bombarder[numworkers];
		for (int i = 0; i < numworkers; i++) {
        
			bombThread[i] = new Bombarder(i);
			B52.execute(bombThread[i]);
            
        }
		
		
	 	B52.shutdown();
		
	}

}
