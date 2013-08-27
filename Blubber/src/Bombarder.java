
public class Bombarder implements Runnable {

	
	public void run() {
		int i = 0;
		
		while (i<100000) {
			try {
				Send.main("S.", "adam");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		
		System.out.println("Done!");
	}

}
