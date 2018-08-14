package com.dj.SpringBoot.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dj.SpringBoot.Dao.DataSample;
import com.dj.SpringBoot.Dao.GetWaterInfo;
import com.dj.SpringBoot.Domain.Water;
import com.dj.SpringBoot.Domain.Watershed;

public class calculate {
	private double dp = 6.0;
	private double LWDdp = 4.0;
	private double XCLD = 8.0;
	private double imdp = 2.0;
	private int t;
	private double r;
	private int p;
	
	public calculate(int raintime, double yfxs, int cxq) {
		this.t = raintime;
		this.r = yfxs;
		this.p = cxq;
	}
	public static void main(String[] args) {

	}
	
	/**
	 * 计算经流系数
	 * @param myrainflow
	 * @param watersheds
	 * @return
	 */
	public List<Water> myrainflow(List<Double> myrainflow,List<Watershed> watersheds, 
			List<Double> sum_rain){		
		List<Water> waters = new ArrayList<Water>();
		for(int n=0;n < watersheds.size();n++){
			int id = watersheds.get(n).getId();
			List<Double> watershed_rainflow=new ArrayList<>();
			for(int i=0;i < t;i++){
				double rain = sum_rain.get(i);
				double meanrainflow=(double)myrainflow.get(t*n+i)/rain;
				watershed_rainflow.add(meanrainflow);
			}
			Water w = new Water(id, watershed_rainflow);
			waters.add(w);
//			watersheds.get(n).setRunoff(watershed_rainflow);
		}
		return waters;
	}
	
	/**
	 * 计算单位面积经流量
	 * @param myrainflow
	 * @param watersheds
	 * @return
	 */
	public List<Watershed> myrainflow(List<Double> myrainflow,List<Watershed> watersheds){		
		for(int n=0;n < watersheds.size();n++){
			List<Double> watershed_rainflow=new ArrayList<>();
			double area = watersheds.get(n).getArea();
			for(int i=0;i < t;i++){
				double meanrainflow=(double)myrainflow.get(t*n+i)/area;
				watershed_rainflow.add(meanrainflow);
			}
			watersheds.get(n).setRunoff(watershed_rainflow);
		}
		return watersheds;
	}
	
	public List<Double> runoffCoeff(List<Double> myrainflow, List<Double> sum_rain){
		List<Double> mean_rainflow=new ArrayList<>();
		for(int n=1;n < 23;n++){
			for(int i=0;i < t;i++){
				double meanrainflow=(double)myrainflow.get(t*n+i)/sum_rain.get(t*n+i);
				mean_rainflow.add(meanrainflow);
			}
		}
		return mean_rainflow;
	}

	/**
	 * 计算总经流量，将不透水的经流量加上透水区的经流量
	 * 总经流量 = 不透水率 X 不透水经流量 + （1 - 不透水率）X 透水区经流量
 	 * @param sum_rain	降雨累计量
	 * @param myinfil	瞬时下渗量
	 * @param rainseries	降雨序列
	 * @param watersheds	子流域信息
	 * @return
	 */
 	public List<Double> sumrainflow(List<Double> sum_rain, List<Double> myinfil, 
			List<Double> rainseries, List<Watershed> watersheds){
		List<Double> myarainflow=rainflow(sum_rain, rainseries, watersheds);
		List<Double> myarainflow01=rainflow01(sum_rain, myinfil,rainseries, watersheds);		
		List<Double> sumrainflo=new ArrayList<>();
		for(int i=0;i<watersheds.size();i++){				
			double imper = watersheds.get(i).getImpermeate()/100;
			for(int j=0;j<t;j++){				
				double myrainflow = myarainflow.get(t*i+j);
				double myrainflow01 = myarainflow01.get(t*i+j);		
				double mysumrainflow=imper*myrainflow+(1-imper)*myrainflow01;
//				System.out.println("不透水经流量：" + imper*myrainflow + "透水经流量：" + (1-imper)*myrainflow01 +
//						"; 总经流量："+mysumrainflow + "; 瞬时降雨量：" + rainseries.get(j));
				sumrainflo.add(mysumrainflow);
			}			
//			System.out.println("*****************************");
		}
		return sumrainflo;
	}
 	
 	/**
 	 * 计算海绵体布设后的经流总量
 	 * @param sum_rain
 	 * @param myinfil
 	 * @param rainseries
 	 * @param watersheds
 	 * @return
 	 */
 	public List<Double> sumLIDrainflow(List<Double> sum_rain, List<Double> myinfil, 
			List<Double> rainseries, List<Watershed> watersheds){
		List<Double> myarainflow=rainflow(sum_rain, rainseries, watersheds);
		List<Double> myarainflow01=rainflow01(sum_rain, myinfil,rainseries, watersheds);
		List<Double> myarainflow02 = LWD(sum_rain, myinfil,rainseries, watersheds);
		List<Double> myarainflow03 = XCLD(sum_rain, myinfil,rainseries, watersheds);

		List<Double> sumrainflo=new ArrayList<>();
		for(int i=0;i<watersheds.size();i++){		
			double area = watersheds.get(i).getArea();
			double imper = watersheds.get(i).getImpermeate()/100;
			double lwdi = watersheds.get(i).getLwd()/area;
			double xcli = watersheds.get(i).getXcl()/area;
			double yssdi = watersheds.get(i).getYssd()/area;
			double tspzi = watersheds.get(i).getTspz()/area;
			double xsci = watersheds.get(i).getXsc()/area;
			for(int j=0;j<t;j++){			
				int index = t*i+j;
				double myrainflow = myarainflow.get(index);
				double myrainflow01 = myarainflow01.get(index);		
				double myrainflow02 = myarainflow02.get(index);		
				double myrainflow03 = myarainflow03.get(index);		
 				double mysumrainflow=(imper - lwdi - tspzi)*myrainflow+(1 - imper)*myrainflow01 + 
 						(lwdi + tspzi) * myrainflow02 + (xcli + yssdi + xsci) * myrainflow03;
//				System.out.println("不透水经流量：" + imper*myrainflow + "透水经流量：" + (1-imper)*myrainflow01 +
//						"; 总经流量："+mysumrainflow + "; 瞬时降雨量：" + rainseries.get(j));
				sumrainflo.add(mysumrainflow);
			}			
//			System.out.println("*****************************");
		}
		return sumrainflo;
	}
	
	/**
	 * 不透水区的经流量计算,洼地蓄水量取2.0
	 * @param sum_rain
	 * @param infiltration
	 * @param rainseries
	 * @param watersheds
	 * @return
	 */
	public List<Double> rainflow(List<Double> sum_rain, List<Double> rainseries, List<Watershed> watersheds){
		List<Double> arainflow=new ArrayList<>();
		for(int i=0;i < watersheds.size();i++){		
			double sum=0.0;
			double _width= watersheds.get(i).getWidth();
			double _slope = watersheds.get(i).getSlope();
			double Q;
			double rainDeep=0;	//初始水深，水深大于洼地蓄水量的时候开始计算经流量
			for(int j=0;j<t;j++){
				 rainDeep=rainDeep+(double)rainseries.get(j);				
	            if (rainDeep<imdp){
	                Q=0;
	            }else{
	                Q=_width*(1.49/0.04)*Math.pow((rainDeep-imdp),1.6667)*Math.pow(_slope,0.5);
	                rainDeep=2.0;
	            }
	            arainflow.add(Q/100);	 
	            sum=sum+Q/100;
			}
		}				   		
		return arainflow;         
	}

	/**
	 * 透水区经流量计算,洼地蓄水深取 6.0
	 * @param sum_rain
	 * @param infiltration
	 * @param rainseries
	 * @param watersheds
	 * @return
	 */
	public List<Double> rainflow01(List<Double> sum_rain, List<Double> infiltration, 
			List<Double> rainseries, List<Watershed> watersheds){
		List<Double> arainflow=new ArrayList<>();		
		for(int i=0;i < watersheds.size();i++){		
			double _width= watersheds.get(i).getWidth();
			double _slope = watersheds.get(i).getSlope();
			double rainDeep=0;	//水深
			double sum_Q=0;	// 经流量和		
			for(int j=0;j<t;j++){				
				double Q;	//			
				rainDeep=rainDeep+(double)rainseries.get(j)-(double)infiltration.get(j);
				if(rainDeep<0){rainDeep=0;}
	            if (rainDeep<dp){
	                Q=0;
	            }else{	            	 
	                Q=_width*(float)(1.49/0.2)*(float)Math.pow((rainDeep-dp),1.6667)*(float)Math.pow(_slope,0.5);
	                rainDeep=6.0;
	            }
	            sum_Q=sum_Q+Q;
	            arainflow.add(Q/100);	 
			 }
		}				      
		return arainflow;         
	}
	
	/**
	 * 计算绿色屋顶产流量
	 * @param sum_rain
	 * @param infiltration
	 * @param rainseries
	 * @param watersheds
	 * @return
	 */
	public List<Double> LWD(List<Double> sum_rain, List<Double> infiltration, 
			List<Double> rainseries, List<Watershed> watersheds) {
		List<Double> arainflow=new ArrayList<>();		
		for(int i=0;i < watersheds.size();i++){		
			double _width= watersheds.get(i).getWidth();
			double _slope = watersheds.get(i).getSlope();
			double rainDeep=0;	//水深
			double sum_Q=0;	// 经流量和		
			for(int j=0;j<t;j++){				
				double Q;	//			
				rainDeep=rainDeep+(double)rainseries.get(j)-(double)infiltration.get(j) * 1.2;
				if(rainDeep<0){rainDeep=0;}
	            if (rainDeep<LWDdp){
	                Q=0;
	            }else{	            	 
	                Q=_width*(float)(1.49/0.2)*(float)Math.pow((rainDeep-LWDdp),1.6667)*(float)Math.pow(_slope/2,0.5);
	                rainDeep=6.0;
	            }
	            sum_Q=sum_Q+Q;
	            arainflow.add(Q/100);	 
			 }
		}				      
		return arainflow;   
	}
	/**
	 * 下沉式绿地
	 * @param sum_rain
	 * @param infiltration
	 * @param rainseries
	 * @param watersheds
	 * @return
	 */
	public List<Double> XCLD(List<Double> sum_rain, List<Double> infiltration, 
			List<Double> rainseries, List<Watershed> watersheds) {
		List<Double> arainflow=new ArrayList<>();		
		for(int i=0;i < watersheds.size();i++){		
			double _width= watersheds.get(i).getWidth();
			double _slope = watersheds.get(i).getSlope();
			double rainDeep=0;	//水深
			double sum_Q=0;	// 经流量和		
			for(int j=0;j<t;j++){				
				double Q;	//			
				rainDeep=rainDeep+(double)rainseries.get(j)-(double)infiltration.get(j) * 1.4;
				if(rainDeep<0){rainDeep=0;}
	            if (rainDeep<XCLD){
	                Q=0;
	            }else{	            	 
	                Q=_width*(float)(1.49/0.2)*(float)Math.pow((rainDeep-XCLD),1.6667)*(float)Math.pow(_slope,0.5);
	                rainDeep=6.0;
	            }
	            sum_Q=sum_Q+Q;
	            arainflow.add(Q/100);	 
			 }
		}				      
		return arainflow;   
	}
	
	/**
	 * 计算降雨，返回每时刻降雨序列，以及每时刻降雨总量序列
	 * @param r
	 * @param p
	 * @param t
	 * @return
	 */
	
	public Map<String, List<Double>> rain(){					
		int a=2094;
		double b=8.875;
		double n=0.633;
		double c=0.56;
		double sum=0.0;
		double q=167 *a * (1 + c * Math.log10(p));		
		int z=(int)(r * t);
		
		List<Double> rainTimeSeries=new ArrayList<>();		
		List<Double> sumRain=new ArrayList<>();
		Map<String, List<Double>> map = new HashMap<String, List<Double>>();
		
		for (int y=z;y>0;y--){
			double w = (((1 - n) * y / r) + b) / Math.pow((y / r) + b, n + 1);
			double i = q * w;		
			double RainfallIntensity=(i / 27889.678);
			rainTimeSeries.add(RainfallIntensity);
			sum=sum+RainfallIntensity;
			sumRain.add(sum);
		}
		for (int y=0;y<t-z+1;y++){
			double w = (((1 - n) * y / (1 - r)) +b) / Math.pow((y /(1 - r)) + b, n + 1);
			double i = q * w;
			double RainfallIntensity=(i / 27889.678);
			rainTimeSeries.add(RainfallIntensity);
			sum=sum+RainfallIntensity;
			sumRain.add(sum);
		}				
		map.put("rain",rainTimeSeries );
		map.put("sumrain",sumRain);
		return map;		
	}
	
	/**
	 * 计算累计下渗量，理论值
	 * @return
	 */
	public List<Double> Infiltration(){
		List<Double> infiltration=new ArrayList<>();
		double Fb=50.0/60.0;		//初始入渗率
		double Fs=6.6/60.0;		//稳定入渗率
		double a=2.1/60.0;		//常数
		for(int i=0;i<t;i++){
			double f = Fs * i + ((Fb - Fs) * (1-Math.exp(-(a)* i))) / a;			
			infiltration.add(f);
		}	
		return infiltration;
	}
	/**
	 * 计算瞬时下渗率
	 * @return
	 */
	public List<Double> Infiltrate(){
		List<Double> infiltration=new ArrayList<>();
		double Fb=50.0/60.0;		//初始入渗率
		double Fs=6.6/60.0;		//稳定入渗率
		double a=2.1/60.0;		//常数
		for(int i=0;i<t + 150;i++){
			double f = Fs + (Fb - Fs) * Math.exp(-a* i);			
			infiltration.add(f);
		}	
		return infiltration;
	}
	
	/**
	 * 瞬时下渗量计算，瞬时下渗量等于当前时段累计下渗量减去前一时间累计下渗量
	 * 瞬时下渗量不能大于瞬时降雨量
	 * @param infil
	 * @param rainseries
	 * @return
	 */
	public List<Double> infil(List<Double> infil, List<Double> rainseries){
		List<Double> myinfil=new ArrayList<>();
		myinfil.add(0.00);
		for(int i=1;i<t;i++){
			if(((double)infil.get(i)-(double)infil.get(i-1))<(double)rainseries.get(i)){
				myinfil.add((double)infil.get(i)-(double)infil.get(i-1));
			}else {
				myinfil.add(rainseries.get(i));
			}			
		}
		return myinfil;
	}
				 		 		
}
