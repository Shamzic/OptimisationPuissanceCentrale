package test;

import java.util.ArrayList;

public class Algo {
	
	private int it;
	private int xn=0;
	private int QmaxTurb=160;
	
	private double HauteurChuteNette1;
	private double HauteurChuteNette2;
	private double HauteurChuteNette3;
	private double HauteurChuteNette4;
	private double HauteurChuteNette5;
	
	private double p00;
	private double p10;
	private double p01;
	private double p11;
	private double p02;
	private double p12;
	private double p03;
	
	private double Puissancecalcule;
	private double Puissance = 0;
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
	
	
	//Constructor1
	public Algo() {
		this.tab5 =new ArrayList<Double>();
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
	
	public void displyTab5(){
		System.out.println("Tab5 : "+this.tab5);
	}
	
	public void calculTab5() {
		
		p00 = -665.4;
		p10 = 1.773;
		p01 = 62.27;
		p11 = -0.113;
		p02 = -1.924;
		p12 = 0.002065;
		p03 = 0.01963;

		double Puissance = 0;
		
		for(int d=0;d<=QmaxTurb;d+=5) {
			
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
	
	
	public ArrayList<Double> tableau4(){
		
		xn=0;
		
		p00 =-436.5;
		p10 =0.6018;
		p01 =43.03;
		p11 =-0.03336;
		p02 =-1.406;
		p12 =0.0007168;
		p03 =0.01525;
		
		ArrayList<Double> tableau4= new ArrayList<Double>();
		for(int j=0; j<=32;j++) {
			for(it=0;it<=QmaxTurb;it+=5) {
				if(j-it>=0) {
					HauteurChuteNette4 = ElevAm-139.009-0.5*Math.pow(10, -5)*Math.pow(it, 2);
					//calcul de Fn
					Puissancecalcule= p00 + p10*it + p01*HauteurChuteNette4 + p11*it*HauteurChuteNette4 + p02*Math.pow(HauteurChuteNette4,2) + p12*it*Math.pow(HauteurChuteNette4,2) + p03*Math.pow(HauteurChuteNette4,3)+this.tab5.get(j-it);
					//calcul de Fn*
					if(Puissancecalcule>=Puissance) {
						Puissance=Puissancecalcule;
						xn=it;
					}
					
				}
				
			}
			tableau4.add(Puissance);
			
		}
		return tableau4;
	}
	
	public ArrayList<Double> tableau3(){	
		xn=0;
		p00 =-713.9;
		p10 =1.331;
		p01 =67.91;
		p11 =-0.08218;
		p02 =-2.137;
		p12 =0.001528;
		p03 =0.02226;
		
		ArrayList<Double> tableau3= new ArrayList<Double>();
		for(int j=0; j<=32;j++) {
			for(it=0;it<=QmaxTurb;it+=5) {
				if(j-it>=0) {
					HauteurChuteNette3=172.11-139.009-0.5*Math.pow(10, -5)*Math.pow(it, 2);
					//calcul de Fn
					Puissancecalcule= p00 + p10*it + p01*HauteurChuteNette3 + p11*it*HauteurChuteNette3 + p02*Math.pow(HauteurChuteNette3,2) + p12*it*Math.pow(HauteurChuteNette3,2) + p03*Math.pow(HauteurChuteNette3,3)+tableau4().get(j-it);
					//calcul de Fn*
					if(Puissancecalcule>=Puissance) {
						Puissance=Puissancecalcule;
						xn=it;
					}
					
				}
				
			}
			tableau3.add(Puissance);
		}
		return tableau3;
	}
	
	public ArrayList<Double> tableau2(){
		xn=0;		
		p00 =-463.8;
		p10 =0.8633;
		p01 =44.78;
		p11 =-0.05074;
		p02 =-1.432;
		p12 =0.001016;
		p03 =0.01517;
		
		ArrayList<Double> tableau2= new ArrayList<Double>();
		for(int j=0; j<=32;j++) {
			for(it=0;it<=QmaxTurb;it+=5) {
				if(j-it>=0) {
					HauteurChuteNette2=172.11-139.009-0.5*Math.pow(10, -5)*Math.pow(it, 2);
					//calcul de Fn
					Puissancecalcule= p00 + p10*it + p01*HauteurChuteNette2 + p11*it*HauteurChuteNette2 + p02*Math.pow(HauteurChuteNette2,2) + p12*it*Math.pow(HauteurChuteNette2,2) + p03*Math.pow(HauteurChuteNette2,3)+tableau3().get(j-it);
					//calcul de Fn*
					if(Puissancecalcule>=Puissance) {
						Puissance=Puissancecalcule;
						xn=it;
					}
					
				}
				
			}
			tableau2.add(Puissance);
		}
		return tableau2;
	}
	
	public ArrayList<Double> tableau1(){
		xn=0;
		p00 =-688.9;
		p10 =0.8937;
		p01 =67.96;
		p11 =-0.05336;
		p02 =-2.223;
		p12 =0.001051;
		p03 =0.02414;
		
		ArrayList<Double> tableau1=new ArrayList<Double>();
		for(it=0;it<=QmaxTurb; it+=5) {
			HauteurChuteNette1=172.11-139.009-0.5*Math.pow(10, -5)*Math.pow(it, 2);
			//gain maximale de la turbine suivante pour le it correspondant (max de chaque ligne du tableau d'apres:Puissance= constante*(160-it*5)+constante*HauteurChuteNette2

			Puissancecalcule= p00 + p10*it + p01*HauteurChuteNette1 + p11*it*HauteurChuteNette1 + p02*Math.pow(HauteurChuteNette1,2) + p12*it*Math.pow(HauteurChuteNette1,2) + p03*Math.pow(HauteurChuteNette1,3)+tableau2().get(QmaxTurb-it);
			
			//calcul de Fn*
			if(Puissancecalcule>=Puissance) {
				Puissance=Puissancecalcule;
				xn=it;
			}
		}
		tableau1.add(Puissance);
		return tableau1;
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

}
