package com.dj.SpringBoot.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class calculate {
	public static void main(String[] args) {
		calculate calculate=new calculate();
		Map<String, List<Double>> rian=calculate.rain(0.35,2,120);
		List<Double> rainseries=(List<Double>)rian.get("rain");
		List<Double> sum_rain=(List<Double>)rian.get("sumrain");
		System.out.println(sum_rain);
		List<Double> infil=calculate.Infiltration();
		System.out.println(infil);

		List<Double> myinfil=new ArrayList<>();
		for(int i=1;i<121;i++){
			if(((double)infil.get(i)-(double)infil.get(i-1))<(double)rainseries.get(i)){
				myinfil.add((double)infil.get(i)-(double)infil.get(i-1));
			}else {
				myinfil.add(rainseries.get(i));
			}			
		}
		System.out.println(myinfil);
		  
//		testsql testsql=new testsql();
//		//��ȡƽ���¶ȡ�����������
//		Map info=testsql.myquery();
//		List width=(List)info.get("width");
//		List sum_area=(List)info.get("sum_area");
//		List slope=(List)info.get("mean_slope");
//		
//		List impermeate=testsql.queryimpermeate();	//��ȡ��͸ˮ��					
//		
//		List myrainflow=calculate.sumrainflow(sum_rain, width, slope, myinfil, rainseries, impermeate);	//���㾶����		
//		List mymean_rainflow=calculate.myrainflow(myrainflow, sum_area);	//���㵥λ����ľ�����		
//		testsql.addorupdate(myrainflow,"dbo.T38RIVER");	//�����ݿ��������,���½�����		
//		testsql.addorupdate(mymean_rainflow,"dbo.T38RIVER_RUNOFF");	//���¾���ϵ��
//					
	}
	
	public List myrainflow(List myrainflow,List sum_area){		
		List mean_rainflow=new ArrayList<>();
		for(int n=1;n<173;n++){
			float area=(float)sum_area.get(n)/10000;
			for(int i=0;i<120;i++){
				float meanrainflow=(float)myrainflow.get(120*n+i)/area;
				mean_rainflow.add(meanrainflow);
			}
		}
		return mean_rainflow;
	}
	
	//�����벻�������ۺ�
	public List sumrainflow(List sum_rain,List width,List slope,List myinfil,List rainseries,List impermeate){
		List myarainflow=rainflow(sum_rain, width, slope, myinfil,rainseries);
		List myarainflow01=rainflow01(sum_rain, width, slope, myinfil,rainseries);
		List sumrainflo=new ArrayList<>();
		for(int i=0;i<173;i++){				
			float imper=(float)impermeate.get(i);
			for(int j=0;j<120;j++){				
				float myrainflow=(float)myarainflow.get(120*i+j);
				float myrainflow01=(float)myarainflow01.get(120*i+j);		
				System.out.println("��������"+myrainflow+"��   ��������"+myrainflow01);
				float mysumrainflow=imper*myrainflow+(1-imper)*myrainflow01;
				System.out.println("����������"+mysumrainflow);
				sumrainflo.add(mysumrainflow);
			}			
		}
		return sumrainflo;
	}
	
	//������
	public List rainflow(List sum_rain,List width,List slope,List infiltration,List rainseries){
		double dp=2.0;		
		List arainflow=new ArrayList<>();
		for(int i=0;i<173;i++){		
			float sum=0.0f;
			float _width=(float)width.get(i);
			float _slope=(float)slope.get(i);
			float Q;
			double rainDeep=0;	//ˮ��
			boolean isfull=false;
			for(int j=0;j<120;j++){
				 rainDeep=rainDeep+(double)rainseries.get(j);				
	            if (rainDeep<dp){
	                Q=0;
	            }else{
	                Q=_width*(float)(1.49/0.2)*(float)Math.pow((rainDeep-dp),1.6667)*(float)Math.pow(_slope,0.5);
	                rainDeep=2.0;
	            }
	            arainflow.add(Q/100);	 
	            sum=sum+Q/100;
			}
		}				   		
		return arainflow;         
	}
	//������
	public List rainflow01(List sum_rain,List width,List slope,List infiltration,List rainseries){
		double dp=6.0;
		List arainflow=new ArrayList<>();		
		for(int i=0;i<173;i++){		
			double nest_infil=0.28;	//˲ʱʵ��������
			double sum_infil=0;
			float _width=(float)width.get(i);
			float _slope=(float)slope.get(i);
			double rainDeep=0;	//ˮ��
			float sum_Q=0;	//�ܾ�����
			List mysuminfil=new ArrayList<>();			
			for(int j=0;j<120;j++){
				float Q;	//������				
				 rainDeep=rainDeep+(double)rainseries.get(j)-(double)infiltration.get(j)*2;
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
	 * 计算降雨，返回每时刻降雨序列，以及每时刻降雨总量序列
	 * @param r
	 * @param p
	 * @param t
	 * @return
	 */
	public Map<String, List<Double>> rain(double r,int p,int t){					
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
	 * 计算下渗量，理论值
	 * @return
	 */
	public List<Double> Infiltration(){
		List<Double> infiltration=new ArrayList<>();
		double Fb=50  /60;		//初始入渗率
		double Fs=6.6/60;		//稳定入渗率
		double a=2.1/60;		//常数
		for(int i=0;i<200;i++){
			double f = Fs * i + ((Fb - Fs) * (1-Math.exp(-(a)* i))) / a;			
			infiltration.add(f);
		}	
		return infiltration;
	}
				 		 		
}
