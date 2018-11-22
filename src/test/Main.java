package test;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Algorithme lancé...");
		
		Algo a= new Algo();
		a.setElevAm(172.110000610351);
		a.setQtot(548.958435058593);
		a.setDebitsMax(160, 160, 160, 160, 160);
		a.calculElevAv();
		System.out.println("Elevation aval : "+a.getElevAv());
		a.calculTab5();
		a.displyTab5();
		a.calculTab4();
		a.displyTab4();
		
	}
}
