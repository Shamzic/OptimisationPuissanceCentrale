package test;

import java.util.ArrayList;

public class Algo {
	
	private int it;
	//private int xn=0;
	private int QmaxTurb=160;
	
	private double p00;
	private double p10;
	private double p01;
	private double p11;
	private double p02;
	private double p12;
	private double p03;
	
	private double ElevAm = 172.11;
	private double ElevAv = 139.009;
	
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
	double best_xn1 = 0;
	
	
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
		this.tab5 =new ArrayList<Double>();
	}
	
	public void setElevAm(double ElevAm) {
		this.ElevAv = ElevAm;
	}
	
	public void setQtot(double Qtot) {
		this.Qtot = Qtot;
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
	
	public void calculTab5() { // OK
		
		p00 = -665.4;
		p10 = 1.773;
		p01 = 62.27;
		p11 = -0.113;
		p02 = -1.924;
		p12 = 0.002065;
		p03 = 0.01963;

		double Puissance = 0;
		
		for(int d = 0; d <= QmaxTurb; d += 5) {
			
			double pertes_de_charge = 0.5*Math.pow(10, -5)*Math.pow(d, 2);
			double hcn = ElevAm - ElevAv - pertes_de_charge; 

			
			// f(x,y) = p00 + p10*x + p01*y + p11*x*y + p02*y^2 + p12*x*y^2 + p03*y^3
			//  - en x on a le débit : d
			//  - en y on a la hauteur de chute nette : hcn
			
			Puissance = p00 + p10*d + p01*hcn + p11*d*hcn +
					p02*Math.pow(hcn, 2) + p12*d*Math.pow(hcn, 2) + 
					p03*Math.pow(hcn, 3);
			
			this.tab5.add(Puissance);
		}
	}
	
	public void displyTab5(){
		System.out.println("Tab5 : "+this.tab5);
	}

	public void calculTab(ArrayList<Double> tab, ArrayList<Double> tab_np1, ArrayList<Integer> best_xn){
		
		double hcn;
		double Gn;
		double Fetoile_np1;
		double Fn_sn_xn;
		double Fetoile_n = 0;
		
		// S(n) :  variables d'état -> volume d'eau restant à turbiner pour la turbine n (avec 0 <= n <= 160).
		// x(n) : variables de décision -> volume d'eau à turbiner alloué à la turbine n (avec 0 <= n <= 160).
		for(int sn = 0; sn <= QmaxTurb; sn += 5) {
			for(int xn = 0; xn <= QmaxTurb; xn += 5) {
				if((sn - xn) >= 0) {
					hcn = ElevAm - ElevAv - 0.5*Math.pow(10, -5)*Math.pow(xn, 2);

					// vieux code : Puissancecalcule= p00 + p10*it + p01*HauteurChuteNette4 + p11*it*HauteurChuteNette4 + p02*Math.pow(HauteurChuteNette4,2) + p12*it*Math.pow(HauteurChuteNette4,2) + p03*Math.pow(HauteurChuteNette4,3)+this.tab5.get(j-it);

					// Calcul du gain
					Gn = p00 + p10*xn + p01*hcn + p11*xn*hcn + p02*Math.pow(hcn,2) + p12*xn*Math.pow(hcn,2) + p03*Math.pow(hcn,3);
					
					//System.out.println(" sn : "+sn);
					//System.out.println(" xn : "+xn);
					
					// Calcul de f* de n+1 (sn - xn)
					Fetoile_np1 = tab_np1.get((sn/5) - (xn/5));
					
					// Calcul de Fn(sn, xn)
					Fn_sn_xn = Gn + Fetoile_np1; 
					System.out.println("Gain : "+ Gn+", Fn(sn="+sn+",xn="+xn+") = "+Fn_sn_xn);
					
					//calcul de Fn*(sn) : recherche du maximum des xn
					if(xn == 0) {
						Fetoile_n = Fn_sn_xn;
						best_xn.add(xn);
					} else {
						if(Fn_sn_xn > Fetoile_n) {
							Fetoile_n = Fn_sn_xn;
							best_xn.set(sn/5, xn); // (indice, élément)
						}
					}
				}					
			}
			tab.add(Fetoile_n);	
		}
		System.out.println("Tableau des best_xn = "+best_xn);
	}
	
	public void calculTab4() {
		this.p00 =-436.5;
		this.p10 =0.6018;
		this.p01 =43.03;
		this.p11 =-0.03336;
		this.p02 =-1.406;
		this.p12 =0.0007168;
		this.p03 =0.01525;
		this.best_xn4.clear();
		this.calculTab(tab4, tab5, best_xn4);
	}
	
	public void calculTab3() {
		this.p00 =-713.9;
		this.p10 =1.331;
		this.p01 =67.91;
		this.p11 =-0.08218;
		this.p02 =-2.137;
		this.p12 =0.001528;
		this.p03 =0.02226;
		this.best_xn3.clear();
		this.calculTab(tab3, tab4, best_xn3);
	}
	
	public void calculTab2() {
		p00 =-463.8;
		p10 =0.8633;
		p01 =44.78;
		p11 =-0.05074;
		p02 =-1.432;
		p12 =0.001016;
		p03 =0.01517;
		this.best_xn2.clear();
		this.calculTab(tab2, tab3, best_xn3);
	}
	
	public void displayTab4(){
		System.out.println("Tab4 : "+this.tab4);
	}
	
	
	public void displayTab3(){
		System.out.println("Tab3 : "+this.tab3);
	}
	
	public void displayTab2(){
		System.out.println("Tab2 : "+this.tab2);
	}
	
	
public void calculTab1() {
		
	this.p00 =-688.9;
	this.p10 =0.8937;
	this.p01 =67.96;
	this.p11 =-0.05336;
	this.p02 =-2.223;
	this.p12 =0.001051;
	this.p03 =0.02414;

	double hcn;
	double Gn;
	double Fetoile_np1;
	double Fn_sn_xn;
	double Fetoile_n = 0;
	
	// S(n) :  variables d'état -> volume d'eau restant à turbiner pour la turbine n (avec 0 <= n <= 160).
	// x(n) : variables de décision -> volume d'eau à turbiner alloué à la turbine n (avec 0 <= n <= 160).
	int sn = QmaxTurb;
		for(int xn = 0; xn <= QmaxTurb; xn += 5) {
			if((sn - xn) >= 0) {
				hcn = ElevAm - ElevAv - 0.5*Math.pow(10, -5)*Math.pow(xn, 2);

				// vieux code : Puissancecalcule= p00 + p10*it + p01*HauteurChuteNette4 + p11*it*HauteurChuteNette4 + p02*Math.pow(HauteurChuteNette4,2) + p12*it*Math.pow(HauteurChuteNette4,2) + p03*Math.pow(HauteurChuteNette4,3)+this.tab5.get(j-it);

				// Calcul du gain
				Gn = p00 + p10*xn + p01*hcn + p11*xn*hcn + p02*Math.pow(hcn,2) + p12*xn*Math.pow(hcn,2) + p03*Math.pow(hcn,3);
				
				//System.out.println(" sn : "+sn);
				//System.out.println(" xn : "+xn);
				
				// Calcul de f* de n+1 (sn - xn)
				Fetoile_np1 = tab2.get((sn/5) - (xn/5));
				
				// Calcul de Fn(sn, xn)
				Fn_sn_xn = Gn + Fetoile_np1; 
				//System.out.println(" tab1 : fn => "+Fn_sn_xn);
				
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
		System.out.println("le f*n du tab1 est : "+Fetoile_n);
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
	
	public int searchMax(ArrayList<Double> tab, double debitrestant) {
		double max = tab.get(0);
		int indice_retour = 0;
		for(int i = 0; i <= debitrestant; i++) {
			//System.out.println(" i : "+i+" ; "+ tab.get(i));
			if(tab.get(i)>max) {
				max = tab.get(i);
				indice_retour = i;
			}
		}
		return indice_retour;
	}
	
	public void backward () {
		System.out.println("le meilleur xn du tab 1 est : "+best_xn1*5);
		double debit_restant = (this.QmaxTurb/5) - best_xn1;
		System.out.println("Il reste : " + debit_restant * 5+" m^3 à turbiner");
		System.out.println("...");
		int indice_debit_restant = searchMax(tab2, debit_restant);
		//System.out.println("Indice debit restant: " + indice_debit_restant);
		// debit_restant = (this.QmaxTurb/5) - best_xn2.get(indice_debit_restant);
		//System.out.println("Il reste : " + debit_restant * 5+" m^3 à turbiner");
	}

}
