/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package funcdraw;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author yazeed44
 */
final class PlotFunctionPanel extends JPanel {
    
   private final int size;
	private double maxValue;
	private String equation;
      private final JFrame frame;
	public PlotFunctionPanel(JFrame frame,int s, double v,String equation){
            this.frame = frame;
		size = s;
		maxValue = v;
                 this.setEquation(equation);
		setPreferredSize(new Dimension(size, size));
          // String function = "x^3-0.5*x^2 -3*x";
                
            //   System.out.println( getRange(function));
              // System.out.println(getDomain(function));
               //System.out.println(this.getFuncType(function));
	}

    
	
    @Override
	public void paint(Graphics g){
            super.paint(g);
		g.drawLine(size/2, 0, size/2, size);
		g.drawLine(0, size/2, size, size/2);
		double[] tick = getTicks();
		int x = size/10, y = size/2, vA = 8;
		g.setFont(new Font("Sansserif", Font.PLAIN, size/30));
		for(int i = 0; i < 9; i++){
			g.drawLine(x, y+5, x, y-5);
			if(i != 4 && vA != 4){
				if(i > 4)
					g.drawString(tick[i]+"", x-size/40, y+size/21);
				else
					g.drawString(tick[i]+"", x-size/30, y+size/21);
				if(vA > 4)
					g.drawString(tick[vA]+"", y-size/13, x+size/60);
				else
					g.drawString(tick[vA]+"", y-size/12, x+size/60);
			}
			g.drawLine(y+5, x, y-5, x);
			x+=size/10;
			vA--;
		}
		g.setColor(Color.RED);
		//This is where I need help, I'm almost completely lost. The function has to be plotted in red
                g.setFont(new Font("Sansserif", Font.BOLD, size/20));
       double min = (-maxValue), max = maxValue, ratio = size/(max*2), fx;
        for(;min<=max;min+=0.025){
            //preventing round-off error
            min=Math.round(min*1000.0)/1000.0;
                                     
double result1=solveEquation(equation,min);

            fx = Math.round(result1
                    *1000.0)/1000.0;
         
            g.drawLine((int)(size/2+(ratio*min)), (int)(size/2-(ratio*fx)),
                    (int)(size/2+(ratio*min)), (int)(size/2-(ratio*fx)));
        }
	}
	
	//finds the values of the ticks on the axis e.g. -2.0, -1.5, -1.0, -0.5, 0.0, etc
	private  double[] getTicks(){
		double increment = maxValue / 5, currentTick = -1*(maxValue);
		double[] tick = new double[9];
		for(int i = 0; i < 9; i++){
			currentTick+=increment;
			tick[i] = Math.round(currentTick*100.0)/100.0;
		}
		return tick;
	}
 
        
        public void setMaxValue(double pMaxValue){
            this.maxValue = pMaxValue;
         //   this.paint(this.getGraphics());
            
          //  repaint();
        }
        public void setEquation(String equation){
            this.equation = equation;
            
            repaint();
        }
   
        
        private double solveEquation(String pEquation,double varX){
             Calculable calc = null;
                try {
                    calc = new ExpressionBuilder(pEquation)
                            .withVariable("x", varX)
                            .build();
                } catch (UnknownFunctionException | UnparsableExpressionException ex) {
                
                    JOptionPane.showMessageDialog(this, "هنالك خطا في كتابة المعادلة " + "\n" + "امثلة على كتابة معادلات صحيحة" + "2 * x +3" + 
                            "\n" + "3*x +9" + "\n" + "(x*4)/x", "خطأ", JOptionPane.ERROR_MESSAGE);
                }
                
          assert(calc != null);             
          double result1 = 0.0;
          try {
           result1 =calc.calculate();
          }
          
          catch(ArithmeticException ex){
              return 0.0;
          }
        return result1;

        }
        
        
        private ArrayList<Double> getResults(String pEquation,boolean sorted){
            ArrayList<Double> results = new ArrayList();
            
            for(Double d = -3.0;d <= 3.0;d+=0.5){
                try {
                Double result = solveEquation(pEquation,d);
                
                if(result != 0 && !result.isNaN()){
                    results.add(result);
                }
                }
                catch(ArithmeticException ex){
                  
                }
            }
            if(sorted){
            Collections.sort(results);}
            //System.out.println("results : "+ results);
            
            
            return results;
        }
        
        
        private ArrayList<Double> getExceptionForDomain(String pEquation){
            ArrayList<Double> exceptions = new ArrayList<>();
            
             for(double i = -3.0 ; i <= 3.0; i+=0.5){
                 try {
                    Double result = this.solveEquation(pEquation, i);
                    //System.out.println("result = " + result);
                    if (result == 0 ){
                   //     System.out.println("We got domain  =  it's everything expect " + i);
                        exceptions.add(i);
                    }
                    
                    else if (result.toString().equals("NaN")){
                 //       System.out.println("We got domain  =  it's everything expect " + i);
                        exceptions.add(i);
                    }
                 }
                 catch(java.lang.ArithmeticException ex){
               //      System.out.println("We got domain  =  it's everything expect " + i);
                     exceptions.add(i);
                 }
                }
            
             Collections.sort(exceptions);
           //  System.out.println(exceptions);
            return exceptions;
        }
        
       
        
        private boolean isExceptionsAboveSpectfiedNumber(ArrayList<Double> exceptions){
            boolean isExceptionsAboveSpectfiedNumber = true;
            Double number = exceptions.get(0);
            int i = 0;
            for(Double exception : exceptions){
                i ++;
                if(i >= exceptions.size())
                {
                    return isExceptionsAboveSpectfiedNumber;
                }
                
            if (exceptions.get(i) - exceptions.get(i-1) != 0.5 ){
                isExceptionsAboveSpectfiedNumber = false;
            }
            
        }
            
            return isExceptionsAboveSpectfiedNumber;
        }
        
          private String inverseFunction(String pEquation){
            StringBuilder invEquation = new StringBuilder(pEquation);
            
            int plusIndex = invEquation.indexOf("+"),minusIndex = invEquation.indexOf("-"),multiplyIndex = invEquation.indexOf("*"),
                    divisonIndex = invEquation.indexOf("/"),twiceIndex = invEquation.indexOf("^"),rootIndex = invEquation.indexOf("sqrt"),
                    cosIndex = invEquation.indexOf("cos"),sinIndex = invEquation.indexOf("sin"),tan = invEquation.indexOf("tan")
                    ;
                    
                    Map<String,Integer> indexs = new HashMap();
                   
                    
                    
                     indexs.put("/",multiplyIndex);
                    indexs.put("*",divisonIndex);
                    indexs.put("sqrt",twiceIndex);
                    indexs.put("^",rootIndex);
                     indexs.put("-",plusIndex);
                    indexs.put("+",minusIndex);
                    
                    
                    int exception = 0;
                    
            for(String opposite : indexs.keySet()){
                
                    try {
                        if(indexs.get(opposite) == -1){
                      //      System.out.println("We will throw the opposite of  this : " + opposite);
                            continue;
                        }
                        int index = indexs.get(opposite);
                        System.out.println("index = " + index + ",opposite = " + opposite);
                        System.out.println("Equation = " + invEquation.substring(index, index+1));
                        switch (invEquation.charAt(index)+"") {
                           
                            case "sqrt":
                               // invEquation.deleteCharAt(index);
                               
                                
                                break;
                            case "^":
                             //   invEquation.deleteCharAt(index);
                                break;
                            case "/":
                                if(Integer.parseInt(invEquation.charAt(index+1)+"") != exception){
                                invEquation.deleteCharAt(index);
                                exception = Integer.parseInt(invEquation.charAt(index)+ "");
                                invEquation.insert(invEquation.indexOf("x"), invEquation.charAt(index)+"*");
                                
                                invEquation.deleteCharAt(index+2);
                                
                                System.out.println("Inside / = " + invEquation.toString());}
                                break;
                                
                                 case "*":
                                     System.out.println(invEquation.substring(index-1, index));
                                     if(Integer.parseInt(invEquation.substring(index-1,index)) != exception){
                                invEquation.deleteCharAt(index);
                                invEquation.insert(invEquation.length(), "/"+invEquation.substring(index-1, index));
                                exception = Integer.parseInt(invEquation.substring(index-1, index));
                                invEquation.deleteCharAt(index-1);
                                
                                System.out.println("Inside * = "+ invEquation.toString());}
                                break;
                            case "+":
                                
                                invEquation.deleteCharAt(index);
                                invEquation.insert(index, opposite);
                                System.out.println("Inside + = " + invEquation.toString());
                                break;
                            case "-":
                                System.out.println("Inside -");
                                invEquation.deleteCharAt(index);
                                invEquation.insert(index, opposite);
                                break;
                                
                           
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                        }
            return invEquation.toString();
        }
        
         public String getDomain(String pEquation){
            ArrayList<Double> exceptions = this.getExceptionForDomain(pEquation);
            
            int size = exceptions.size();
            
            if (exceptions.isEmpty()){
                return "المجال هو جميع الاعداد الحقيقية  R";
            }
            
            else if(size == 1&&exceptions.get(0) == 0){
                return "المجال هو جميع الاعداد الحقيقية R ";
            }
            
            else if(size == 1){
                return "المجال هو جميع الاعداد ماعدا  " + exceptions.get(0);
            }
              else if (size == 2&&-exceptions.get(0) != exceptions.get(size-1) ){
                return "المجال هو جميع الاعداد ماعدا "+ exceptions.get(size-1) + ", " + exceptions.get(size-2);
            }
            
            else if (size == 2 && -exceptions.get(0) == exceptions.get(size-1) ){
                return " المجال هو جميع الاعداد الحقيقة ماعدا +- " + exceptions.get(size-1);
            }
            
           else if (size >= 3 && isExceptionsAboveSpectfiedNumber(exceptions)){
                return "المجال هو جميع الاعداد الحقيقة اللتي اكبر من "+ exceptions.get(size-1);
            }            
            
            else if (size == 3 ){
                return "المجال هو جميع الاعداد ماعدا  " + exceptions.toString();
            }
            
            return "لايوجد مجال";
            
        }
        
         //there's alot of bugs
        public String getRange(String pEquation){
        /*    ArrayList<Double> results = getResults(pEquation,true);
            
            boolean isItZero = false;
       for (Double result : results) {
           if (result <= 0) {
               isItZero = true;
           }
       }
            
            if (!isItZero){
                return "مدى الدالة هو جميع الاعداد الحقيقية الموجبة  R+";
            }
            
            return "مدى الدالة من "+ results.get(0) + "الى موجب مالا نهاية";*/
            
            StringBuilder invEquation = new StringBuilder(pEquation);
            
            
            invEquation = new StringBuilder(inverseFunction(invEquation.toString()));
            System.out.println("Inverse Equation =  "+invEquation.toString());
            
            
            return "مدى الدالة هو " + (this.getDomain(invEquation.toString()).substring(9));
            
        }
        
        
      
        
        public String getInversedFunction(String pFunction){
            
            return "الدالة العكسية هي  : " + this.inverseFunction(pFunction);
        }
        
        public String getZeros(String pEquation){
            return "الاصفار للدالة =  " +getExceptionForDomain(pEquation);
        }
        
        
        public String getYPart(String pEquation){
            return "مقطع Y = "+(int)this.solveEquation(pEquation, 0);
        }
        
        public String getSmallestFxValue(String pEquation){
            ArrayList<Double> results = this.getResults(pEquation,true);
            
            return "القيمة الصغرى المطلقة للدالة f = "+results.get(0);
        }
        
        public String getAverageResult(String pEquation){
            
           Double averageResult = 0.0;
           int i = 0 ;
           for(Double result : this.getResults(pEquation, true)){
               averageResult += result;
               i++;
           }
           
           return "متوسط معدل التغير للدالة f = "+averageResult/i;
        }
        
      /*  public String getSimilarityAxis(String pEquation){
            
            if(this.solveEquation(pEquation,-1.0) == solveEquation(pEquation,1.0)){
                return "الدالة متماثلة حول محور Y";
            }
            
            else if (-this.solveEquation(pEquation, -1.0) == this.solveEquation(pEquation, 1.0)){
                return "الدالة متماثلة حول نقطة الاصل";
            }
            
            return "الدالة متماثلة حول محور X";
        }*/
        
        public String getFuncType(String pEquation){

            if((solveEquation(pEquation, -1)) == (solveEquation(pEquation,1))){
            
                return "هذه الدالة زوجية";
            }
            
            else if (solveEquation(pEquation,-1) == -solveEquation(pEquation,1)){
                return "هذه الدالة فردية";
            }
            
            return "الدالة ليست زوجية وليست فردية";
        }
        
        
       /* public String getIncrementPart(String pEquation){
            ArrayList<Double> results = this.getResults(pEquation,false);
            String incrementPart = "فترات الزيادة هي  ";
            
            
            
            return incrementPart;
        }*/
        
        
}
