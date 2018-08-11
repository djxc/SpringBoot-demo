package com.dj.SpringBoot.Dao;

import java.util.ArrayList;
import java.util.List;

import com.dj.SpringBoot.Domain.Watershed;
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
//		getDatabyID();
//		List<Watershed> watersheds = getWaterInfo();
//		if (null!=watersheds) {			
//			for (int i=0; i<watersheds.size();i++) {
//				Watershed watershed = watersheds.get(i);
//				double area = watershed.getArea();
//				double impermeate = watershed.getImpermeate();
//				System.out.println("ID:" + watershed.getId() + "面积：" + area + 
//						"不透水：" + impermeate );
//			}
//		}
		GetWaterInfo waterinfo = new GetWaterInfo();
		for (int i=0; i< 23; i++) {			
			changeRainflow(i);
		}
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
	 * 更新数据
	 */
	public static void insertData() {
		List<List<Double>> runoff = calculate.getrunoff(); 
		DataSample.getConnect();
		DataSample.alertRainflow(runoff);
		DataSample.closeAll();
	}
	
	public static List<Double> getDatabyID(int id) {
		DataSample.getConnect();
		List<Double> rainflow = DataSample.selectRainflow(id, 120);		
		DataSample.closeAll();
		return rainflow;
	}
	
	public static void changeRainflow(int time) {
		GetWaterInfo waterinfo = new GetWaterInfo();
		List<List<Double>> runoff = calculate.getrunoff();
		for (int i = 0; i < runoff.size(); i++) {
			watersheds.get(i).setRainflow(runoff.get(i).get(time));
		}
		DataSample.getConnect();
		DataSample.changeRainflow(watersheds);
		DataSample.closeAll();
	}
}
