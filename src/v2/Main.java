package v2;
import v2.Algo;

public class Main {
	
	static int snMax = 550;
	static int nbXn = 160;
	static int nbStage = 5;
	static int nbTurbines = 5;
	static int Qtot = 550;
	static double elevationAmont = 172.110000610351;
	static double[][] gn = new double[snMax+1][nbTurbines];
	static double elevationAval =  0.0;
	
	static public void calculGn() {
		System.out.println(-1.665e-05);
		double hauteurDeChute =  0;
		double p1 = -7.378 * Math.pow(10, -7);
		double p2 = 0.004195;
		double p3 = 137.1;
		elevationAval = p1 * Math.pow(Qtot, 2) + p2 * Qtot + p3;
		for(int sn = 0; sn <= snMax; sn++) {
			for(int j = 0; j < nbTurbines; j++) {
				hauteurDeChute = elevationAmont -  elevationAval - 0.5*Math.pow(10, -5)*Math.pow(sn, 2);
				//  f(x,y) = p00 + p10*x + p01*y + p20*x^2 + p11*x*y + p30*x^3 + p21*x^2*y
				double p00=0,p10=0,p01=0,p11=0,p02=0,p12=0,p03=0,p20=0,p30=0,p21=0;
				if(j == 4) {
					// Coeff turbine 5
					p00 = 0.2946;
					p10 = -0.1834;
					p01 = -0.008074;
					p20 = 0.002706;
					p11 = 0.00809;
					p30 = -1.318e-05;
					p21 = 1.949e-05;
					gn[sn][j]= p00 + p10*sn + p01*hauteurDeChute + p20*sn*sn + p11*hauteurDeChute*sn+ p30*sn*sn*sn + p21*sn*sn*hauteurDeChute;
					//gn[sn][j] = p00 + p10*sn + p01*hauteurDeChute + p11*sn*hauteurDeChute +	p02*Math.pow(hauteurDeChute, 2) + p12*sn*Math.pow(hauteurDeChute, 2) + p03*Math.pow(hauteurDeChute, 3); 
				}
				else if (j == 3 ) {
					// Coeff turbine 4
					p00 = -0.04632;
					p10 = -0.1905;
					p01 = 0.001769;
					p20 = 0.003537;
					p11 = 0.004951;
					p30 = -1.689e-05;
					p21 = 3.487e-05;
					gn[sn][j]= p00 + p10*sn + p01*hauteurDeChute + p20*sn*sn + p11*hauteurDeChute*sn+ p30*sn*sn*sn + p21*sn*sn*hauteurDeChute;
				}
				else if (j == 2 ) {
					// Coeff turbine 3
					p00 = -0.02446;
					p10 = -0.2157;
					p01 = 0.0009464;
					p20 = 0.003541;
					p11 = 0.006353;
					p30 = -1.569e-05;
					p21 = 2.214e-05;
					gn[sn][j]= p00 + p10*sn + p01*hauteurDeChute + p20*sn*sn + p11*hauteurDeChute*sn+ p30*sn*sn*sn + p21*sn*sn*hauteurDeChute;
				}
				else if (j == 1 ) {
					// Coeff turbine 2
					p00 = 0.8122;
					p10 = -0.2442;
					p01 = -0.02374;
					p20 = 0.003838;
					p11 = 0.006492;
					p30 = -1.665e-05;
					p21 = 2.207e-05;
					gn[sn][j]= p00 + p10*sn + p01*hauteurDeChute + p20*sn*sn + p11*hauteurDeChute*sn+ p30*sn*sn*sn + p21*sn*sn*hauteurDeChute;
				}
				else if (j == 0 ) {
					// Coeff turbine 1
					p00 = 0.08651;
					p10 = -0.1976;
					p01 = -0.002543;
					p20 = 0.002892;
					p11 = 0.008183;
					p30 = -1.191e-05;
					p21 = 7.371e-06;
					gn[sn][j]= p00 + p10*sn + p01*hauteurDeChute + p20*sn*sn + p11*hauteurDeChute*sn+ p30*sn*sn*sn + p21*sn*sn*hauteurDeChute;
				}
				if(gn[sn][j] < 0)
					gn[sn][j]=0;
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		calculGn();
		Algo a = new Algo(nbXn, snMax, nbStage, gn);
		a.displayData();
		System.out.println("--------------- BACKWARD  ------------");
		System.out.println("------------- STAGE N = 5 ------------");
		System.out.println("--------------------------------------");
		a.stageN();
		
		
		a.displaySnTab();
		a.displayXnStar();
		a.displayFnStar();
		
		System.out.println("------------ STAGE N = 4 -------------");
		a.stageN_4();
		
		//a.displaytab2Ddouble("tab4 :", a.tab4);
		
		a.displaytab1Ddouble("fn_star4 :", a.fn_star_tab4);
		a.displaytab1Dint("xn_star4 :", a.xn_star_tab4);
		System.out.println("------------ STAGE N = 3 -------------");
		a.stageN_3();

		//a.displaytab2Ddouble("tab3 :", a.tab3);
		
		a.displaytab1Ddouble("fn_star3 :", a.fn_star_tab3);
		a.displaytab1Dint("xn_star3 :", a.xn_star_tab3);
		System.out.println("------------ STAGE N = 2 -------------");
		a.stageN_2();
		
		a.displaytab1Ddouble("fn_star2 :", a.fn_star_tab2);
		System.out.println("taille fnstar tab 2 : "+a.fn_star_tab2.length);
		a.displaytab1Dint("xn_star2 :", a.xn_star_tab2);
//
		System.out.println("------------ STAGE N = 1 -------------");
		a.stage1();
		a.displaytab1Ddouble("tab1 : ", a.tab1);
		
		System.out.println("--------------------------------------");
		System.out.println("------------ FORWARD  ----------------");
		System.out.println("------------ STAGE N = 1 -------------");
		System.out.println("--------------------------------------");
		a.stageForward1();
//		System.out.println("------------ STAGE N = 2 -------------");
//		a.stageForward2();
//		System.out.println("------------ STAGE N = 3 -------------");
//		a.stageForward3();
//		System.out.println("------------ STAGE N = 4 -------------");
//		a.stageForward4();
//		System.out.println("------------ STAGE N = 5 -------------");
//		a.stageForward5();
	}
}
