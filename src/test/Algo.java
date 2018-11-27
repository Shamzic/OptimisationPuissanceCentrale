package test;

import java.util.ArrayList;

public class Algo {
		

	static int STOP = 160;
	private double p00;
	private double p10;
	private double p01;
	private double p11;
	private double p02;
	private double p12;
	private double p03;
	
	private double ElevAm = 172.11;
	private double ElevAv = 139.009;
	private int QmaxTurb;
	
	// Débit total à turbiner
	private double Qtot;
	
	// Débits max pour chaque turbine
	private double Qmax1;
	private double Qmax2;
	private double Qmax3;
	private double Qmax4;
	private double Qmax5;
	
	// Tableau des débit max à turbiner pour chaque turbine
	// Pour le tab5, on calcul directement la puissance max possible pour chaque débit
	private ArrayList<Double> tab5;
	private ArrayList<Double> tab4;
	private ArrayList<Double> tab3;
	private ArrayList<Double> tab2;
	private ArrayList<Double> tab1;
	
	ArrayList<Integer> best_xn5;
	ArrayList<Integer> best_xn4;
	ArrayList<Integer> best_xn3;
	ArrayList<Integer> best_xn2;
	int best_xn1 = 0;
	
	 static double[] coeffP1 = {  0.08651, -0.0025430, -0.1976, 0.008183, 0.002892, 7.371 * 0.000001, -1.191 * 0.00001 };
	 static double[] coeffP2 = {  0.81220, -0.0237400, -0.2442, 0.006492, 0.003838, 2.207 * 0.000010, -1.665 * 0.00001 };
	 static double[] coeffP3 = { -0.02446,  0.0009464, -0.2157, 0.006353, 0.003541, 2.214 * 0.000010, -1.569 * 0.00001 };
	 static double[] coeffP4 = { -0.04632,  0.0017690, -0.1905, 0.004951, 0.003537, 3.487 * 0.000010, -1.689 * 0.00001 };
	
	
	//Constructor1
	public Algo() {
		this.tab5 =new ArrayList<Double>();
		this.tab4= new ArrayList<Double>();
		this.tab3 =new ArrayList<Double>();
		this.tab2= new ArrayList<Double>();
		this.tab1 =new ArrayList<Double>();
		this.best_xn5 = new ArrayList<Integer>();
		this.best_xn4 = new ArrayList<Integer>();
		this.best_xn3 = new ArrayList<Integer>();
		this.best_xn2 = new ArrayList<Integer>();
	}

	// Constructor2
	public Algo(double ElevAm, double Qtot, double Qmax1, double Qmax2, 
			    double Qmax3, double Qmax4, double Qmax5) {
		
		this.ElevAv = ElevAm;
		this.Qmax1 = Qmax1;
		this.Qmax2 = Qmax2;
		this.Qmax3 = Qmax3;
		this.Qmax4 = Qmax4;
		this.Qmax5 = Qmax5;
		this.Qtot = Qtot;
		this.QmaxTurb= (int)Qtot;
		this.tab5 =new ArrayList<Double>();
	}
	
	public void setElevAm(double ElevAm) {
		this.ElevAv = ElevAm;
	}
	
	public void setQtot(double Qtot) {
		this.Qtot = Qtot;
		this.QmaxTurb= (int)Qtot;
	}
	
	public void setDebitsMax(double Qmax1, double Qmax2, 
			    double Qmax3, double Qmax4, double Qmax5) {
		this.Qmax1 = Qmax1;
		this.Qmax2 = Qmax2;
		this.Qmax3 = Qmax3;
		this.Qmax4 = Qmax4;
		this.Qmax5 = Qmax5;
	}
	
	/* Calcul élévation avale en fonction de 
	 * l'élévation amont en entrée (=> constructeur) */
	public void calculElevAv() {
		double p1 = -7.378 * Math.pow(10, -7);
		double p2 = 0.004195;
		double p3 = 137.1;
		ElevAv = p1 * Math.pow(Qtot, 2) + p2 * Qtot + p3;
	}
	
	public double getElevAv() {
		return this.ElevAv;
	}
	
	public void calculTab5() {
		
		p00 = 31.62;
		p10 = 14.61;
		p01 = 2.614;
		p11 = 1.631;
		p02 = 0.4912;
		p12 = 0.2792;
		p03 = 0.07843;
		double puissance = 0;
		double puissanceMax = 0;
		
		for(int d = 0; d <= QmaxTurb; d += 5) {
			
			double pertes_de_charge = 0.5*Math.pow(10, -5)*Math.pow(d, 2);
			double hcn = ElevAm - ElevAv - pertes_de_charge; 
			
			// f(x,y) = p00 + p10*x + p01*y + p11*x*y + p02*y^2 + p12*x*y^2 + p03*y^3
			// f(x,y) = p00 + p10*x + p01*y + p11*x*y + p02*y^2 + p12*x*y^2 + p03*y^3
			//  - en x on a le débit : d
			//  - en y on a la hauteur de chute nette : hcn
			if(d<=QmaxTurb) {
				//   f(x,y) = p00 + p10*x + p01*y + p11*x*y + p02*y^2 + p12*x*y^2 + p03*y^3
//				puissance = p00 + p10*d + p01*hcn + p11*d*hcn +
//						p02*Math.pow(hcn, 2) + p12*d*Math.pow(hcn, 2) + 
//						p03*Math.pow(hcn, 3);
			 double[] coeffP5 = {  0.29460, -0.0080740, -0.1834, 0.008090, 0.002706, 1.949 * 0.000010, -1.318 * 0.00001 };
			// f(x,y) = p00 + p10*x + p01*y + p20*x^2 + p11*x*y + p02*y^2 + p30*x^3 + p21*x^2*y 
	          //          + p12*x*y^2
				puissance = coeffP5[0] + (coeffP5[1] * hcn) + (coeffP5[2] * d) + (coeffP5[3] * hcn * d)
						+ (coeffP5[4] * d * d) + (coeffP5[5] * d * d * hcn) + (coeffP5[6] * d * d * d);
				if(puissance>puissanceMax)
					puissanceMax = puissance;
			}
			
			if(d <= QmaxTurb) {
			if(puissance<0)
				puissance = 0;
			this.tab5.add(puissance);
			}
			else
			 this.tab5.add(puissanceMax);
		}

	}
	
	public void displyTab5(){
		System.out.println("Tab5 = [ ");
		
		for(int i = 0 ; i < tab5.size(); i++) {
			System.out.println("fn("+i*5+") = "+tab5.get(i));
		}
		System.out.println("] ");
	}
	
   public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

	public void calculTab(ArrayList<Double> tab, ArrayList<Double> tab_np1, ArrayList<Integer> best_xn, double[] coeffP){
		
		double hcn;
		double Gn;
		double Fetoile_np1;
		double Fn_sn_xn;
		double Fetoile_n = 0;
		
		// S(n) :  variables d'état -> volume d'eau restant à turbiner pour la turbine n (avec 0 <= n <= 160).
		// x(n) : variables de décision -> volume d'eau à turbiner alloué à la turbine n (avec 0 <= n <= 160).
		String stringbuffer = "";
		for(int sn = 0; sn <= QmaxTurb; sn += 5) {
			stringbuffer += "sn = "+sn+" [ ";
			for(int xn = 0; xn <= STOP; xn += 5) {
				if((sn - xn) >= 0) {
					hcn = ElevAm - ElevAv - 0.5*Math.pow(10, -5)*Math.pow(xn, 2);

					// Calcul du gain
					//Gn = p00 + p10*xn + p01*hcn + p11*xn*hcn + p02*Math.pow(hcn,2) + p12*xn*Math.pow(hcn,2) + p03*Math.pow(hcn,3);
					
					Gn = coeffP[0] + (coeffP[1] * hcn) + (coeffP[2] * xn) + (coeffP[3] * hcn * xn)
							+ (coeffP[4] * xn * xn) + (coeffP[5] * xn * xn * hcn) + (coeffP[6] * xn * xn * xn);
					
					
					Fetoile_np1 = tab_np1.get((sn/5) - (xn/5));
					
					// Calcul de Fn(sn, xn)
					Fn_sn_xn = Gn + Fetoile_np1;
					
					//calcul de Fn*(sn) : recherche du maximum des xn
					if(xn == 0) {
						Fetoile_n = 0;
						best_xn.add(sn/5, 0);	
					}
					
					if(Fn_sn_xn > Fetoile_n) {
						//System.out.println("search bestxn : "+Fn_sn_xn+" >? "+ Fetoile_n);
						Fetoile_n = Fn_sn_xn;
						best_xn.set(sn/5, xn); // (indice, élément)
						//System.out.print("xn : "+xn+" ");
					}
					
					// affichage : 
					stringbuffer+=""+round(Fn_sn_xn, 2)+" ";
		
				}					
			}
			stringbuffer+="\n";
			if(Fetoile_n<0)
				Fetoile_n = 0;
			tab.add(Fetoile_n);	
		}

		//System.out.println("Tableau des fn(sn,xn) = \n"+stringbuffer);
		System.out.println("Tableau des best_xn = "+best_xn);
	}
	
	public void calculTab4() {
		p00 = 34.5;
		p10 = 15.42;
		p01 = 2.376;
		p11 = 1.246;
		p02 = 0.3717;
		p12 = 0.1407;
		p03 = 0.06054;
		this.best_xn4.clear();
		this.calculTab(tab4, tab5, best_xn4, coeffP4);
	}
	
	public void calculTab3() {
		p00 = 33.35;
		p10 = 13.57;
		p01 = 2.314;
		p11 = 1.262;
		p02 = 0.4717;
		p12 = 0.1962;
		p03 = 0.08876;
		this.best_xn3.clear();
		this.calculTab(tab3, tab4, best_xn3, coeffP3);
	}
	
	public void calculTab2() {
		p00 = 30.88;
		p10 = 15.35;
		p01 = 2.077;
		p11 = 1.135;
		p02 = 0.3508;
		p12 = 0.1033;
		p03 = 0.06088;
		this.best_xn2.clear();
		this.calculTab(tab2, tab3, best_xn2, coeffP2);
	}
	
	public void displayTab4(){
		System.out.println("Tab4 (fn*) : "+this.tab4);
	}
	
	
	public void displayTab3(){
		System.out.println("Tab3 (fn*) : "+this.tab3);
	}
	
	public void displayTab2(){
		System.out.println("Tab2 (fn*) : "+this.tab2);
	}
	
	
public void calculTab1() {
		
    p00 =       31.21 ;
    p10 =       14.43  ;
    p01 =        2.33  ;
    p11 =       1.211;
    p02 =      0.5485  ;
    p12 =      0.1455 ;
    p03 =      0.0964;

	double hcn;
	double Gn;
	double Fetoile_np1;
	double Fn_sn_xn;
	double Fetoile_n = 0;
	System.out.println("Tab 1 des fn(sn = "+QmaxTurb+", xn) = [");
	String stringbuffer = "";
	// S(n) :  variables d'état -> volume d'eau restant à turbiner pour la turbine n (avec 0 <= n <= 160).
	// x(n) : variables de décision -> volume d'eau à turbiner alloué à la turbine n (avec 0 <= n <= 160).
	int sn = QmaxTurb;
		for(int xn = 0; xn <= STOP; xn += 5) {
			if((sn - xn) >= 0) {
				hcn = ElevAm - ElevAv - 0.5*Math.pow(10, -5)*Math.pow(xn, 2);

				// Calcul du gain
				// Gn = p00 + p10*xn + p01*hcn + p11*xn*hcn + p02*Math.pow(hcn,2) + p12*xn*Math.pow(hcn,2) + p03*Math.pow(hcn,3);
				Gn = coeffP1[0] + (coeffP1[1] * hcn) + (coeffP1[2] * xn) + (coeffP1[3] * hcn * xn)
						+ (coeffP1[4] * xn * xn) + (coeffP1[5] * xn * xn * hcn) + (coeffP1[6] * xn * xn * xn);
				//System.out.println(" sn : "+sn);
				//System.out.println(" xn : "+xn);
				
				// Calcul de f* de n+1 (sn - xn)
				Fetoile_np1 = tab2.get((sn/5) - (xn/5));
				
				// Calcul de Fn(sn, xn)
				Fn_sn_xn = Gn + Fetoile_np1; 
				stringbuffer+=" "+Fn_sn_xn;
				//System.out.println("Gain : "+ Gn+", Fn(sn="+sn+",xn="+xn+") = "+Fn_sn_xn);
				
				//calcul de Fn*(sn) : recherche du maximum des xn
				if(xn == 0) {
					Fetoile_n = Fn_sn_xn;
				} else {
					if(Fn_sn_xn>Fetoile_n) {
						Fetoile_n = Fn_sn_xn;
						best_xn1= xn;
					}
				}
			}					
		}
		tab1.add(Fetoile_n);
		System.out.println(""+stringbuffer+"\n]");
		System.out.println("\nle f*n du tab1 est : "+Fetoile_n);
}
	
	public double maxTab(ArrayList<Double> tab) {
		double maximum=0;
		for(int i=0; i<=tab.size();i++){
			if(tab.get(i)>maximum){
				maximum=tab.get(i);
			}
		}
		return maximum;
		
	}
	
	public int searchMax(ArrayList<Double> tab, int d, int limitQtot) {
		double max = tab.get(0);
		int indice_retour = 0;
		for(int i = 0; i <= d && i <= limitQtot; i++) {
			//System.out.println(" i : "+i+" ; "+ tab.get(i));
			if(tab.get(i)>max) {
				max = tab.get(i);
				indice_retour = i;
			}
		}
		return indice_retour;
	}
	
	public void forward () {
		
		/* ****************** Turbine 1 ******************/
		int debitOptimalT1 = best_xn1;
		System.out.println("La turbine 1 va turbiner optimalement : " + debitOptimalT1*5 + " m^3");
		
		int debit_restantT1 = ((int)this.Qtot/5) - debitOptimalT1; // cast en int arrondi le qtot à un entier pour comparer avec les xn
		System.out.println("Il reste : " + debit_restantT1 * 5 + " m^3 à turbiner (:");
		
		/* ****************** Turbine 2 ******************/
		System.out.println("Qmax2 : "+Qmax2);
		int indice_debit_restant2 = searchMax(tab2, debit_restantT1, (int)Qmax2/5); // limite 160 m^3 par turbine (32*5)
		
		System.out.println("La turbine 2 va turbiner optimalement : " + indice_debit_restant2*5 + " m^3");
	
		int debit_restantT2 = debit_restantT1 - indice_debit_restant2 ;
		System.out.println("Il reste : " + debit_restantT2 * 5  + " m^3 à turbiner");
		
		/* ****************** Turbine 3 ******************/
		int indice_debit_restant3 = searchMax(tab3, debit_restantT2, (int)Qmax3/5); // limite 160 m^3 par turbine (32*5)
		
		System.out.println("La turbine 3 va turbiner optimalement : " + indice_debit_restant3*5 + " m^3");
	
		int debit_restantT3 = debit_restantT2 - indice_debit_restant3 ;
		System.out.println("Il reste : " + debit_restantT3 * 5  + " m^3 à turbiner");
		
		
		/* ****************** Turbine 4 ******************/
		int indice_debit_restant4 = searchMax(tab4, debit_restantT3, (int)Qmax4/5); // limite 160 m^3 par turbine (32*5)
		
		System.out.println("La turbine 4 va turbiner optimalement : " + indice_debit_restant4*5 + " m^3");
	
		int debit_restantT4 = debit_restantT3 - indice_debit_restant4 ;
		System.out.println("Il reste : " + debit_restantT4 * 5  + " m^3 à turbiner");
		
		/* ****************** Turbine 5 ******************/
		int indice_debit_restant5 = searchMax(tab5, debit_restantT4, (int)Qmax5/5); // limite 160 m^3 par turbine (32*5)
		
		System.out.println("La turbine 5 va turbiner optimalement : " + indice_debit_restant5*5 + " m^3");
	
		int debit_restantT5 = debit_restantT4 - indice_debit_restant5 ;
		System.out.println("Il reste : " + debit_restantT5 * 5  + " m^3 à turbiner");
		
	}

}
