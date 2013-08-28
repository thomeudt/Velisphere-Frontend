
public class Bombarder implements Runnable {

	
	 private int workerNumber;

	    Bombarder(int number) {
	        workerNumber = number;
	    }
	public void run() {
		int i = 0;
		
		while (i<1000) {
			try {
				Send.main("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH.", "adam");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		
		System.out.println("Done!");
	}

}
