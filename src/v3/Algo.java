package v3;
import java.util.ArrayList;

public class Algo {
//coeff turbines
	private static double[] coeffTurb1 = {  0.08651, -0.1976, -0.002543, 0.002892, 0.008183, -1.191e-05, 7.371e-06 };
	private static double[] coeffTurb2 = { 0.8122, -0.2442, -0.02374, 0.003838, 0.006492, -1.665e-05, 2.207e-05 };
	private static double[] coeffTurb3 = { -0.02446,  -0.2157,  0.0009464, 0.003541, 0.006353, -1.569e-05, 2.214e-05 };
	private static double[] coeffTurb4 = { -0.04632, -0.1905, 0.001769, 0.003537, 0.004951,-1.689e-05, 3.487e-05 };
	private static double[] coeffTurb5 = { 0.2946,  -0.1834,-0.008074,  0.002706, 0.008090, -1.318e-05, 1.949e-05 };
	
	double maximum;
	int maxIndex=0;
	
	int Qtot=550;
	double elevAval;
	double elevAm = 172.11;
	int debitMaxTurb= 160;
	
	private double puissance;
	private int debitRestant;
	private double puissanceMax;
	
	double[][] tableau2a4 = new double[Qtot/5+50][debitMaxTurb/5+1];
	
	ArrayList<Double> fEtoile=new ArrayList<Double>();
	ArrayList<Integer> XnEtoile=new ArrayList<Integer>();
	ArrayList<Double> fEtoile2 = new ArrayList<Double>();
	ArrayList<Double> fEtoile3 = new ArrayList<Double>();
	ArrayList<Double> fEtoile4 = new ArrayList<Double>();
	ArrayList<Integer> XnEtoile1=new ArrayList<Integer>();
	ArrayList<Integer> XnEtoile2=new ArrayList<Integer>();
	ArrayList<Integer> XnEtoile3=new ArrayList<Integer>();
	ArrayList<Integer> XnEtoile4=new ArrayList<Integer>();

	ArrayList<Double> fEtoile1=new ArrayList<Double>();
	ArrayList<Integer> XnEtoile0=new ArrayList<Integer>();
	
	public Algo(int Qtot, double elevAmont) {
		this.Qtot = Qtot;
		this.elevAm = elevAmont;
	}
	
	//calcul des gains de toutes les turbines
	public double calculGainTurb(int numTurb, int Xn, double hcn) {
		if (Xn==0) return 0;
		else{
			if(numTurb==5) {
				puissance = coeffTurb5[0] + (coeffTurb5[1] * Xn) + (coeffTurb5[2] * hcn) +(coeffTurb5[3]*Math.pow(Xn, 2))+ (coeffTurb5[4] * hcn * Xn) + (coeffTurb5[5] *Math.pow(Xn, 3))+ (coeffTurb5[6] *Math.pow(Xn, 2)* hcn);
			}
			if(numTurb==4) {
				puissance = coeffTurb4[0] + (coeffTurb4[1] * Xn) + (coeffTurb4[2] * hcn) +(coeffTurb4[3]*Math.pow(Xn, 2))+ (coeffTurb4[4] * hcn * Xn) + (coeffTurb4[5] *Math.pow(Xn, 3))+ (coeffTurb4[6] *Math.pow(Xn, 2)* hcn);
				}
			if(numTurb==3) {
				puissance = coeffTurb3[0] + (coeffTurb3[1] * Xn) + (coeffTurb3[2] * hcn) +(coeffTurb3[3]*Math.pow(Xn, 2))+ (coeffTurb3[4] * hcn * Xn) + (coeffTurb3[5] *Math.pow(Xn, 3))+ (coeffTurb3[6] *Math.pow(Xn, 2)* hcn);
			}
			if(numTurb==2) {
				puissance = coeffTurb2[0] + (coeffTurb2[1] * Xn) + (coeffTurb2[2] * hcn) +(coeffTurb2[3]*Math.pow(Xn, 2))+ (coeffTurb2[4] * hcn * Xn) + (coeffTurb2[5] *Math.pow(Xn, 3))+ (coeffTurb2[6] *Math.pow(Xn, 2)* hcn);
			}
			if(numTurb==1) {
				puissance = coeffTurb1[0] + (coeffTurb1[1] * Xn) + (coeffTurb1[2] * hcn) +(coeffTurb1[3]*Math.pow(Xn, 2))+ (coeffTurb1[4] * hcn * Xn) + (coeffTurb1[5] *Math.pow(Xn, 3))+ (coeffTurb1[6] *Math.pow(Xn, 2)* hcn);
			}
		}
		return puissance;
	}

	//calcul de l'elevation avale en fonction du debit total
	public double calculElevAv() {
		double p1 = -7.378 * Math.pow(10, -7);
		double p2 = 0.004195;
		double p3 = 137.1;
		elevAval = p1 * Math.pow(Qtot, 2) + p2 * Qtot + p3;
		return elevAval;
	}
	

	
//tableau 5:
	
	public ArrayList<Integer> tableauN(int numTurb) {
		for(int Sn=0; Sn<=Qtot; Sn+=5) {
			//calcul de pertes
			double pertes = 0.5*Math.pow(10, -5)*Math.pow(Sn, 2);
			
			//calcul de chute nette pour chaque turbine en fonction du debit alloué
			double hcn = elevAm - calculElevAv() - pertes;
			
			if(Sn>debitMaxTurb) {
				fEtoile.add(calculGainTurb(numTurb, debitMaxTurb, hcn));
				XnEtoile.add(debitMaxTurb);
			}
			else {
				fEtoile.add(calculGainTurb(numTurb, Sn, hcn));
				XnEtoile.add(Sn);
			}
		}
		return XnEtoile;
	}

	
	public void maxTabFEtoile(ArrayList<Double> tab) {
		maximum=0;
		maxIndex=0;
		for(int i=0; i<tab.size();i++){
			if(tab.get(i)>maximum){
				maximum=tab.get(i);
				maxIndex=i*5;
			}
		}
	}
	
	
//tableau de 4 a 2
	public void tableau2a4(int numTurb, ArrayList<Double> f, ArrayList<Integer> X) {
		int supLigne=0;
		
		for (int sn = 0; sn <= Qtot; sn+=5) {
			for (int xn = 0; xn <= debitMaxTurb; xn+=5) {
				//calcul de pertes
				double pertes = 0.5*Math.pow(10, -5)*Math.pow(xn, 2);
				
				//calcul de chute nette pour chaque turbine en fonction du debit alloué
				double hcn = elevAm - calculElevAv() - pertes;
				
				if (sn - xn < 0 || sn==0) {
				// zone vide du tableau
					tableau2a4[sn/5][xn/5] = 0;
				}
				else {
						if(numTurb==4) {
							tableau2a4[sn/5][xn/5]= calculGainTurb(numTurb, xn, hcn) + fEtoile.get(sn/5-xn/5);
						}
						if(numTurb==3) {
							tableau2a4[sn/5][xn/5]= calculGainTurb(numTurb, xn, hcn)+ fEtoile4.get(sn/5-xn/5);
						}
						if(numTurb==2) {
							tableau2a4[sn/5][xn/5]= calculGainTurb(numTurb, xn, hcn)+ fEtoile3.get(sn/5-xn/5);
						}
				}
			}
		}
		
		//tableau fn*
		for (int sn = 0; sn <= Qtot; sn+=5) {
			double best=0;
			for (int xn = 0; xn <= debitMaxTurb; xn+=5) {
								
				if(tableau2a4[sn/5][xn/5]>best) {
					
					best=tableau2a4[sn/5][xn/5];
					supLigne=xn;		
				}
			}
				f.add(best);
				X.add(supLigne);				
		}
	}
	
	//tableau turb 1
	public void tableau1() {
		double firstTurb;
		maximum=0;
		
		for (int xn=0; xn<=debitMaxTurb; xn+=5) {
			
			
			//calcul de pertes
			
			double pertes = 0.5*Math.pow(10, -5)*Math.pow(xn, 2);
			
			//calcul de chute nette pour chaque turbine en fonction du debit alloué
			double hcn = elevAm - calculElevAv() - pertes;
			
			//recherche du f* et du xn* pour le tableau 1
			firstTurb=(int)(calculGainTurb(1, xn, hcn) + fEtoile2.get(Qtot/5-xn/5));
			
			if(firstTurb>=maximum) {
				
				maximum=firstTurb;
				maxIndex=xn;
			}
			
		}
		
	
		
	}
	
	
	//forward pass
	
	public void forward() {
		
		debitRestant=(int) (Qtot-maxIndex);
		puissanceMax=maximum;

		System.out.println(" --- Turbine 1 --- " );
		int index = debitRestant  > 0 ? debitRestant  : 0;
		System.out.println("Debit turbiné par la turbine 1 : "+maxIndex);
		System.out.println("Puissance générée par la turbine 1 :"+((puissanceMax-fEtoile2.get(index/5)) >0 ? (puissanceMax-fEtoile2.get(index/5)) : 0));

		System.out.println("Puissance générée de turb 1 à turb 5 :"+puissanceMax);
		System.out.println("débit restant : "+debitRestant);
		

		System.out.println(" --- Turbine 2 --- " );
		System.out.println("Débit turbiné par la turbine 2 :  "+XnEtoile2.get(index/5));
		System.out.println("Puissance générée par la turbine 2 :"+fEtoile2.get(XnEtoile2.get(index/5)/5));
		debitRestant=(int) (debitRestant-XnEtoile2.get(index/5));
		System.out.println("Puissance générée de turb 2 à turb 5 :"+fEtoile2.get(index/5));
		System.out.println("debit restant: "+debitRestant);

		index = debitRestant  > 0 ? debitRestant  : 0;
		System.out.println(" --- Turbine 3 --- " );
		System.out.println("Débit turbiné par la turbine 3 : "+XnEtoile3.get(index/5));
		System.out.println("Puissance générée par la turbine 3 :"+fEtoile3.get(XnEtoile3.get(index/5)/5));
		debitRestant=(int) (debitRestant-XnEtoile3.get(index/5));
		System.out.println("Puissance générée de turb 3 à turb 5 :"+fEtoile3.get(index/5));
		System.out.println("debit restant: "+debitRestant);

		index = debitRestant  > 0 ? debitRestant  : 0;
		System.out.println(" --- Turbine 4 --- " );
		System.out.println("Débit turbiné par la turbine 4 :"+XnEtoile4.get(index/5));
		System.out.println("Puissance générée par la turbine 4 :"+fEtoile4.get(XnEtoile4.get(index/5)/5));
		debitRestant=(int) (debitRestant-XnEtoile4.get(index/5));
		
		
		System.out.println("Puissance générée de turb 4 à turb 5 :"+fEtoile4.get(index/5));
		System.out.println("debit restant: "+debitRestant);

		index = debitRestant  > 0 ? debitRestant  : 0;
		System.out.println(" --- Turbine 5 --- " );
		System.out.println("Débit turbiné par la turbine 5 : "+XnEtoile.get(index/5));
		System.out.println("Puissance générée par turbine 5 :"+fEtoile.get(index/5));
		debitRestant=(int) (debitRestant-XnEtoile.get(index/5));

		
		

		System.out.println("debit restant: "+debitRestant);
		index = debitRestant  > 5 ? debitRestant  : 0;
		
	}
}
