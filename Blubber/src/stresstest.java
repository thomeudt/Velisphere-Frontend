
public class stresstest {

	public static void main(String[] args) throws Exception {
		int i = 0;
		
		while (i<1000) {
			Send.main("the quick brown fox jumps over the lazy dog", "controller");
			i = i + 1;
		}
		
		System.out.println("Done!");
		
	}

}
