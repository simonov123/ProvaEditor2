
public class Main {

	public static void main(String[] args) {
		
		runtime run=new runtime();
		if(args.length!=0) {
		run.run(args[0]);}
		else {
			run.run(null);
		}

	}

}
