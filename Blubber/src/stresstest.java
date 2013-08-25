
public class stresstest {

	public static void main(String[] args) throws Exception {
		int i = 0;
		
		while (i<2000) {
			Send.main("Specifies .", "adam");
			i = i + 1;
		}
		
		System.out.println("Done!");
		
	}

}
