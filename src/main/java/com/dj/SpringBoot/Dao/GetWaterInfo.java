package com.dj.SpringBoot.Dao;

import java.util.List;
import java.util.Map;

import com.dj.SpringBoot.Domain.Water;
import com.dj.SpringBoot.Domain.Watershed;
import com.dj.SpringBoot.Utils.JcsWatershed;
import com.dj.SpringBoot.Utils.calculate;

public class GetWaterInfo {
	private static List<Watershed> watersheds ;
	
	public GetWaterInfo() {
		DataSample.getConnect();
		List<Watershed> watersheds = DataSample.getWatershedInfo();
		DataSample.closeAll();
		this.watersheds = watersheds; 
	}
	public static void main(String[] args) {	
//		insertData();
//		insertLIDData();
//		getDatabyID();
//		List<Watershed> watersheds = getLIDWaterInfo();
//		if (null!=watersheds) {			
//			for (int i=0; i<watersheds.size();i++) {
//				Watershed watershed = watersheds.get(i);
//				double area = watershed.getArea();
//				double impermeate = watershed.getImpermeate();
//				System.out.println("ID:" + watershed.getId() + " ;面积：" + area + 
//						" ;不透水：" + impermeate + " ;lwd:" + watershed.getLwd() +
//						" ;xcl:" + watershed.getXcl() + " ;yssd:" + watershed.getYssd());
//			}
//		}
//		GetWaterInfo waterinfo = new GetWaterInfo();
//		for (int i=0; i< 23; i++) {			
//			changeRainflow(i);
//		}
	}
	/**
	 * 获取流域相关属性
	 * @return
	 */
	public static List<Watershed> getWaterInfo(){
		DataSample.getConnect();
		List<Watershed> watersheds = DataSample.getWatershedInfo();
		DataSample.closeAll();
		return watersheds;
	}

	/**
	 * 获取海绵体布设后的子流域属性
	 * @return
	 */
	public static List<Watershed> getLIDWaterInfo(){
		DataSample.getConnect();
		List<Watershed> watersheds = DataSample.getLIDWatershedInfo();
		DataSample.closeAll();
		return watersheds;
	}
	
	/**
	 * 更新经流量数据
	 * 返回单位面积的经流量，第一维是子流域，第二维是时间。
	 * 将计算的经流量写入每个子流域的表格中
	 */
	public static void insertData(int t, int p, double r) {
		calculate cal = new calculate(t, r, p);
		Map<String, List<Double>> rian=cal.rain();
		List<Double> rainseries=(List<Double>)rian.get("rain");
		List<Double> sum_rain=(List<Double>)rian.get("sumrain");
		List<Double> infil=cal.Infiltration();	//累计下渗量
		List<Double> myinfil = cal.infil(infil, rainseries);	//瞬时下渗量
		List<Watershed> watersheds = GetWaterInfo.getWaterInfo();
		List<Double> myrainflow=cal.sumrainflow(sum_rain, myinfil, rainseries, watersheds);	//流域总经流量计算	
		List<Water> runoffwater =cal.myrainflow(myrainflow, watersheds, sum_rain);	//单位面积产生的经流量		
		
		JcsWatershed jcswater = new JcsWatershed();
		jcswater.putInCache(runoffwater);
		
		DataSample.getConnect();
		DataSample.alertRainflow(runoffwater);
		DataSample.closeAll();
	}
	/**
	 * 更新海绵体布设后的经流量数据
	 * 返回单位面积的经流量，第一维是子流域，第二维是时间。
	 */
	public static void insertLIDData(int t, int p, double r) {
		calculate cal = new calculate(t, r, p);
		Map<String, List<Double>> rian=cal.rain();
		List<Double> rainseries=(List<Double>)rian.get("rain");
		List<Double> sum_rain=(List<Double>)rian.get("sumrain");
		List<Double> infil=cal.Infiltration();	//累计下渗量
		List<Double> myinfil = cal.infil(infil, rainseries);	//瞬时下渗量
		List<Watershed> watersheds = GetWaterInfo.getLIDWaterInfo();
		List<Double> myrainflow=cal.sumLIDrainflow(sum_rain, myinfil, rainseries, watersheds);	//流域总经流量计算	
		List<Water> runoff=cal.myrainflow(myrainflow, watersheds, sum_rain);	//单位面积产生的经流量
		
		JcsWatershed jcswater = new JcsWatershed();
		jcswater.putLIDInCache(runoff);
		
		DataSample.getConnect();
		DataSample.alertLIDRainflow(runoff);
		DataSample.closeAll();
	}
	
	/**
	 * 前端通过ajax传递点击的子流域的id值，在数据库中查询经流量后返回给前端，在echarts中表达出来
	 * @param id
	 * @return
	 */
	public static  Map<String, List<Double>>  getDatabyID(int id) {
		DataSample.getConnect();
		Map<String, List<Double>> rainflow = DataSample.selectRainflow(id, 120);
		DataSample.closeAll();
		return rainflow;
	}
	
	/**
	 * 前端事件序列模块中，传递过来一个时间，从缓存中获取当前时间各个子流域的经流量，然后将其写入smdtv_1中，
	 * 然后前端加载按rainflow字段分层设色显示
	 * @param time
	 */
	public static void changeRainflow(int time) {
		JcsWatershed jcswater = new JcsWatershed();
		List<Water> runoff = jcswater.getRainflows(22);
		DataSample.getConnect();
		DataSample.changeRainflow(runoff, time);
		DataSample.closeAll();
	}
	
}
