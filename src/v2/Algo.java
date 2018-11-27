package v2;

public class Algo {

	int nbXn;
	int snMax;
	int nbStage;
	double[][] gn;
	int gnI;
	int gnJ;
	int[] snTab;
	
	int[] xn_star_tab5;
	double[] fn_star_tab5;
	
	int[] xn_star_tab4;
	double[] fn_star_tab4;
	double[][] tab4;
	
	int[] xn_star_tab3;
	double[] fn_star_tab3;
	double[][] tab3;
	
	int[] xn_star_tab2;
	double[] fn_star_tab2;
	double[][] tab2;
	
	int xn_star_tab1;
	double fn_star_tab1;
	double[] tab1;
	int debit_restant;
	
	int Qmax1 = 160;
	int Qmax2 = 160;
	int Qmax3 = 160;
	int Qmax4 = 160;
	int Qmax5 = 160;
	public Algo (int nbXn, int snMax, int nbStage,double[][] gn) {
		
		this.nbXn = nbXn;
		this.snMax = snMax;
		this.nbStage = nbStage;
		this.gn =  gn;
		snTab = new int[snMax+1];
		xn_star_tab5 = new int[snMax+1];
		fn_star_tab5 = new double[snMax+1];
		
		fn_star_tab4 = new double[snMax+1];
		xn_star_tab4 = new int[snMax+1];
		tab4 = new double[snMax+1][snMax+1];
		
		fn_star_tab3 = new double[snMax+1];
		xn_star_tab3 = new int[snMax+1];
		tab3 = new double[snMax+1][snMax+1];
		
		fn_star_tab2 = new double[snMax+1];
		xn_star_tab2 = new int[snMax+1];
		tab2 = new double[snMax+1][snMax+1];
		
		tab1 = new double[snMax+1];
		this.debit_restant = snMax;
	}
	
	   public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}
	
	public void displayData() {
		System.out.println("Gn = { ");
		for(int i= 0 ; i < this.gn.length; i++) {
			for(int j = 0; j < this.gn[0].length; j++) {
				System.out.print(this.round(this.gn[i][j],2)+" ");
			}
			System.out.println();
		}
		System.out.println("}");
	}
	
	//*************//
	//** STAGE N **//
	//*************//
	
	public void stageN() {
		// n = 5
		double maxfnstar = 0;
		int maxfnstar_indice = 0;

		for(int i = 1 ; i <= snMax; i++) {
			if(i <= Qmax1 ) {
				snTab[i] = i;
				xn_star_tab5[i] = i;
				System.out.println(" gn[i][nbStage-1] de " + i + " : " + gn[i][nbStage - 1]);
				fn_star_tab5[i] = round(gn[i][nbStage - 1],2); // tab5 = fn_star finally
				if (gn[i][nbStage - 1] > maxfnstar) {
					maxfnstar = round(gn[i][nbStage - 1],4);
					//maxfnstar_indice = i;
				}
			}
			else {
				fn_star_tab5[i] =  maxfnstar;
				xn_star_tab5[i] = Qmax1;
				snTab[i] = i;
			}
		}

		for(int i = Qmax1 ; i <= snMax; i++) {
			
		}
	}
	
	public void displaySnTab() {
		displaytab1Dint("Sn tab : ",this.snTab);
	}
	
	public void displayXnStar() {
		displaytab1Dint("xn_star tab : ",this.xn_star_tab5);
	}
	
	public void displayFnStar() {
		displaytab1Ddouble("tab 5 : fn_star tab : ", fn_star_tab5);
	}

	public void displaytab1Dint(String name, int[] tab) {
		System.out.print(name+"  { ");
		for(int i= 0 ; i < tab.length; i++) {
				System.out.print(tab[i]+" ");
		}
		System.out.println("}");
	}
	
	public void displaytab1Ddouble(String name, double[] tab) {
		System.out.print(name+"  { ");
		for(int i= 0 ; i < tab.length; i++) {
				System.out.print(tab[i]+" ");
		}
		System.out.println("}");
	}
	
	public void displaytab2Ddouble(String name, double [][] tab) {
		System.out.println(name+"  { ");
		for(int i= 0 ; i < tab.length; i++) {
			for(int j = 0; j < tab[0].length; j++) {
				System.out.print(tab[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("}");
	}
	
	//****************************//
	//****** STAGE N-1 => 4 ******//
	//****************************//
	
	public void stageN_4() {
		// Turbines du milieu n ={4, 3, 2}
		
		// Turbine 4 : 
		int n = 4;
		fn_star_tab4[0] = 0 ;
		xn_star_tab4[0] = 0 ; 
		for(int sn = 1 ; sn <= this.snMax; sn++) {
			fn_star_tab4[sn] = round(gn[0][n-1] + this.fn_star_tab5[sn], 2) ;
			xn_star_tab4[sn] = 0; 
			for(int xn = 0; xn <= Qmax4; xn++) {
				if((sn-xn)>=0) {
					this.tab4[sn][xn] = round(gn[xn][n-1], 2)+ this.fn_star_tab5[sn-xn] ;
					//System.out.println(gn[xn][n-1]);
				}
				else
					this.tab4[sn][xn] = 0;
				
				// fn_star tab
				if(this.tab4[sn][xn] > fn_star_tab4[sn]) {
					fn_star_tab4[sn] = this.tab4[sn][xn];
					xn_star_tab4[sn] = xn;
				}				 
			}
		}
	}
		
	public void stageN_3() {
		
		int n = 3;
		fn_star_tab3[0] = 0 ;
		xn_star_tab3[0] = 0 ; 
		for(int sn = 1 ; sn <= this.snMax; sn++) {
			fn_star_tab3[sn] = round(gn[0][n-1] + this.fn_star_tab4[sn], 2) ;
			xn_star_tab3[sn] = 0; 
			for(int xn = 0; xn <= Qmax3; xn++) {
				if((sn-xn)>=0) {
					this.tab3[sn][xn] = round(gn[xn][n-1], 2)+ this.fn_star_tab4[sn-xn] ;
					//System.out.println(gn[xn][n-1]);
				}
				else
					this.tab3[sn][xn] = 0;
				
				// fn_star tab
				if(this.tab3[sn][xn] > fn_star_tab3[sn]) {
					fn_star_tab3[sn] = this.tab3[sn][xn];
					xn_star_tab3[sn] = xn;
				}				 
			}
		}
	}
	
	
	public void stageN_2() {
		
		int n = 2;
		fn_star_tab2[0] = 0 ;
		xn_star_tab2[0] = 0 ; 
		for(int sn = 1 ; sn <= this.snMax; sn++) {
			fn_star_tab2[sn] = round(gn[0][n-1] + this.fn_star_tab3[sn], 2) ;
			xn_star_tab2[sn] = 0; 
			for(int xn = 0; xn <= Qmax2; xn++) {
				if((sn-xn)>=0) {
					this.tab2[sn][xn] = round(gn[xn][n-1], 2)+ this.fn_star_tab3[sn-xn] ;
					//System.out.println(gn[xn][n-1]);
				}
				else
					this.tab2[sn][xn] = 0;
				
				// fn_star tab
				if(this.tab2[sn][xn] > fn_star_tab2[sn]) {
					fn_star_tab2[sn] = this.tab2[sn][xn];
					xn_star_tab2[sn] = xn;
				}				 
			}
		}
	}
	
	
	public void stage1() {
		int n = 1;
		fn_star_tab1 = 0;
		xn_star_tab1 = 0;
		for(int xn = Qmax1; xn > 1 ; xn--) {
				this.tab1[xn] = round(gn[xn][n-1] + this.fn_star_tab2[snMax-xn],2);
				// fn_star
				System.out.println("tab1 ["+xn+"]=" + this.tab1[xn]);
				if(this.tab1[xn] > fn_star_tab1) {
					fn_star_tab1 = this.tab1[xn];
					xn_star_tab1 = xn;
					System.out.println("founded max ["+xn+"]=" + xn_star_tab1);
				}
				if(this.tab1[xn] < fn_star_tab1) {
					break;
				}
		}
		System.out.println("xn_star_tab1 = " + xn_star_tab1);
		System.out.println("fn_star_tab1 = " + fn_star_tab1);
	}
	
	
	public void stageForward1() {
		System.out.println("xn_star du tab 1 = " + xn_star_tab1);
		debit_restant = snMax - xn_star_tab1;
		System.out.println("SOLUTION X1 = " + xn_star_tab1);
		System.out.println("Il reste "+debit_restant + " de debit a répartir ");
	}
	
	
//	public void stageForward2() {
//		int debit_restant_max = debit_restant > 160 ? 160 : debit_restant;
//		System.out.print("Max fn_star_tab2 (sn="+debit_restant_max+") = ");
//		System.out.println(this.fn_star_tab2[debit_restant_max]);
//		int solutionX2 = this.xn_star_tab2[debit_restant_max];
//		System.out.println("SOLUTION X2 = " + solutionX2);
//		debit_restant -= solutionX2;
//		System.out.println("Il reste "+debit_restant + " de debit a répartir ");
//	}
//	
//	public void stageForward3() {	
//		int debit_restant_max = debit_restant > 160 ? 160 : debit_restant;
//		System.out.print("Max fn_star_tab3 (sn="+debit_restant_max+") = ");
//		System.out.println(this.fn_star_tab3[debit_restant_max]);
//		int solutionX3 = this.xn_star_tab3[debit_restant_max];
//		System.out.println("SOLUTION X3 = " + solutionX3);
//		debit_restant -= solutionX3;
//		System.out.println("Il reste "+debit_restant + " de debit a répartir ");
//	}
//	
//	public void stageForward4() {	
//		int debit_restant_max = debit_restant > 160 ? 160 : debit_restant;
//		System.out.print("Max fn_star_tab4 (sn="+debit_restant_max+") = ");
//		System.out.println(this.fn_star_tab4[debit_restant_max]);
//		int solutionX4 = this.xn_star_tab4[debit_restant_max];
//		System.out.println("SOLUTION X4 = " + solutionX4);
//		debit_restant -= solutionX4;
//		System.out.println("Il reste "+debit_restant + " de debit a répartir ");
//	}
//
//    public void stageForward5() {	
//    	if(debit_restant > 160)
//    		debit_restant = 160;
//		int solutionX5 = debit_restant;
//		System.out.println("SOLUTION X5 = "+solutionX5);
//	}
//
//	public int getMaxSn2D(double[][] tab, int sn) {
//		double max = tab[sn][0];
//		int indice = 0;
//		for(int xn = 0 ; xn < tab[0].length; xn++) {
//			if(tab[sn][xn]>max) {
//				max= tab[sn][xn];
//				indice = xn;
//			}
//		}
//		return indice;
//	}
}
