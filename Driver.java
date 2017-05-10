package slides10.test09;

public class Driver {
	public static void main(String[] args) {
		ClassABC obj = new ClassDEF();
		obj.m2();

		// since ClassDEF inherits m2:
		ClassDEF obj1 = new ClassDEF();
		obj1.m2();

		// forget about the subclass:
		ClassABC obj2 = new ClassABC();
		obj2.m2();	

		System.out.println("Calling ClassDEF.m1(7)");
		ClassDEF obj3 = new ClassDEF();
		obj3.m1(7);

		// obj has dynamic type ClassDEF
		// but the call
		//obj.m1(7);
		// is illegal
		// We need a cast to call m1 on obj:
		if(obj instanceof ClassDEF) {
			((ClassDEF)obj).m1(7);
		}

		// The following cast would crash the program:
		if(obj2 instanceof ClassDEF) {
			((ClassDEF)obj2).m1(7);
		}

		ClassDEF obj4 = ((ClassDEF)obj2);

	}
}
