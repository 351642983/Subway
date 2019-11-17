package webMind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class WebMind {

	public List<List<String>> items=new ArrayList<List<String>>();
	public List<List<String>> getItems() {
		return items;
	}
	public List<Integer> getItemsFlag() {
		return itemsFlag;
	}
	public List<Integer> itemsFlag=new ArrayList<Integer>();
//	public static void main(String[] args) {
		// TODO 自动生成的方法存根
//		WebMind a=new WebMind();
//		a.addItem(new String[] {"启1", "启2", "启3", "启4", "启5","启1"},1);
//		a.addItem(new String[] {"启2", "栋2", "栋3"},2);
//		a.addItem(new String[] {"启4", "王2", "王3"},3);
//		a.addItem(new String[] {"梦1", "栋2", "梦3", "王2", "梦5"},4);
//		a.addItem(new String[] {"鬼1", "栋3", "鬼3", "王3", "鬼5"},5);
//		//亚型测试：启1-》鬼5,环形测试：启1-》启4
//		List<List<String>> paths=a.getAllPaths("启4","启1");
//		System.out.println("所有线路：");
//		for(List<String> it:paths)
//		{
//			System.out.println(it);
//		}
//		
//		List<List<String>> noConfusepaths=a.removeAllConfuse(paths);
//		System.out.println("未干扰点线路：");
//		for(List<String> it:noConfusepaths)
//		{
//			System.out.println(it);
//		}
//		
//		List<Integer> pointValues=a.getPointValues(paths);
//		System.out.println("经过站点数：");
//		for(Integer it:pointValues)
//		{
//			System.out.println(it);
//		}
//		a.deleteLine(1);
//		System.out.println(a.getItems());
//		System.out.println(a.getItemsFlag());
		
//	}
	//删除某条线路上的所有节点的信息
	public boolean deleteLine(int num)
	{
		Iterator<Integer> it = itemsFlag.iterator();
		Iterator<List<String>> itemInit = items.iterator();
		while(it.hasNext()){
		    Integer x = it.next();
		    itemInit.next();
		    if(x==num){
		        it.remove();
		        itemInit.remove();
		        return true;
		    }
		}
		return false;
	}
	//清除所有节点信息
	public void clearAllInfo()
	{
		items.clear();
		itemsFlag.clear();
	}
	//获得某条线路的所有站点
	public List<String> getLinesNames(int num)
	{
		if(itemsFlag.contains(num))
		{
			List<String> linesTemp=new ArrayList<String>(items.get(itemsFlag.indexOf(num)));
			return linesTemp;
		}
		return null;
	}
	
	//获得经过的站点数
	public List<Integer> getPointValues(List<List<String>> linePaths)
	{
		List<List<String>> dtemp=new ArrayList<List<String>>(linePaths);
		List<List<String>> noConfusepaths=removeAllConfuse(dtemp);
		List<Integer> pointsValue=new ArrayList<Integer>();
		for(List<String> it:noConfusepaths)
		{
			pointsValue.add((it.size()-1));
		}
		return pointsValue;
	}
	//除去所有干扰点项
	public List<List<String>> removeAllConfuse(List<List<String>> linePaths)
	{
		List<List<String>> linePathsTemp=new ArrayList<List<String>>(linePaths);
		for(List<String> itsTemp:linePathsTemp)
		{
			Iterator<String> it = itsTemp.iterator();
			boolean g_remove=false;
			while(it.hasNext()){
			    String x = it.next();
			    if(!g_remove)
			    {
			    	if(itsTemp.indexOf(x)+2<=itsTemp.size()-1)
			    	{
					    if(x.equals(itsTemp.get(itsTemp.indexOf(x)+2))){
					        it.remove();
					        g_remove=true;
					    }
			    	}
			    }
			    else
			    {
			    	it.remove();
			    	g_remove=false;
			    }
			}
		}
		return linePathsTemp;
	}
	
	//获得从起点到终点的所有路径节点信息
	public List<List<String>> getAllPaths(String start,String end)
	{
		List<List<Integer>> lineList=getAllPathsFlags(start,end);
		List<List<String>> pathsPointsTemp=getAllPathsPoints(lineList);
		List<List<String>> pathsPoints=new ArrayList<List<String>>();
		for(int i=0;i<pathsPointsTemp.size();i++)
		{
			List<String> path=new ArrayList<String>();
			List<String> pathPointTemp=pathsPointsTemp.get(i);
			for(int j=0;j<=pathPointTemp.size();j++)
			{
				if(pathPointTemp.size()==0)
				{
					path.addAll(getPTP(start,end));
					break;
				}
				else if(pathPointTemp.size()==1)
				{
					if(lineList.get(i).size()>=j+2)
					{
						if(getSameLine(start,end)==lineList.get(i).get(j+1))
						{
							path.addAll(getPTP(start,end));
							break;
						}
					}
				}
				if(j==0)
				{
					path.addAll(getPTP(start,pathPointTemp.get(j)));
					path.add("转"+lineList.get(i).get(j+1)+"号线");
				}
				else if(j==pathPointTemp.size())
				{
					path.addAll(getPTP(pathPointTemp.get(j-1),end));
				}
				else
				{
					path.addAll(getPTP(pathPointTemp.get(j-1),pathPointTemp.get(j)));
					path.add("转"+lineList.get(i).get(j+1)+"号线");
				}
				
			}
			if(!pathsPoints.contains(path))
				pathsPoints.add(path);
		}
		return pathsPoints;
	}
	
	//分析路线所经过的节点(转乘站点)
	public List<List<String>> getAllPathsPoints(List<List<Integer>> lineList)
	{
		List<List<String>> pathsPointsTemp=new ArrayList<List<String>>();
		for(List<Integer> it:lineList)
		{
			List<String> pathTemp=new ArrayList<String>();
			for(int i=0;i<it.size()-1;i++)
			{
				pathTemp.add(getLTLPoints(it.get(i),it.get(i+1)));
			}
			pathsPointsTemp.add(pathTemp);
		}
//显示转站点信息
//		for(List<String> it:pathsPointsTemp)
//		{
//			System.out.println(it);
//		}
		return pathsPointsTemp;
	}
	
	
	//获得所有的经过路线(转乘线路)
	public List<List<Integer>> getAllPathsFlags(String start,String end)
	{
		List<List<Integer>> itemsPathsFlagTemp=new ArrayList<List<Integer>>();
		if(!isExistPoint(start)||!isExistPoint(end))
			return itemsPathsFlagTemp;
		if(isSameLine(start,end))
		{
			List<Integer> linesTemp=new ArrayList<Integer>();
			linesTemp.add(getSameLine(start,end));
			itemsPathsFlagTemp.add(linesTemp);
			//return itemsPathsFlagTemp;
			
		}
		List<Integer> itemsFlagTemp=getPointsFlag(start);
		List<Integer> itemsFlagEndTemp=getPointsFlag(end);
		int tempLength=itemsFlagTemp.size();
		for(int i=0;i<tempLength;i++)
		{
			//初始化时出错了?
			List<Integer> pathTemp=new ArrayList<Integer>();
			pathTemp.add(itemsFlagTemp.get(i));
			List<Integer>  tempLength_1=getFrontMinus(pathTemp);
			for(int j=0;j<tempLength_1.size();j++)
			{
				getLinesInit(pathTemp,j,itemsFlagEndTemp,itemsPathsFlagTemp);
			}
			
		}
//显示线路变化信息
//		for(int j=0;j<itemsPathsFlagTemp.size();j++)
//			System.out.println(itemsPathsFlagTemp.get(j));
		

		return itemsPathsFlagTemp;
	}
	
	//利用递归遍历求出路径集合
	public void getLinesInit(List<Integer> init,int num,List<Integer> end,List<List<Integer>> cup)
	{
		List<Integer> initTemp=new ArrayList<Integer>(init);
		if(!end.contains(initTemp.get(initTemp.size()-1)))
		{
			initTemp.add(getFrontMinus(initTemp).get(num));
			for(int j=0;j<getFrontMinus(initTemp).size();j++)
			{
				getLinesInit(initTemp,j,end,cup);
			}
		}
		if(end.contains(initTemp.get(initTemp.size()-1))&&!cup.contains(initTemp))
			cup.add(initTemp);
	}
	

	//求出前驱路线的可选路径
	public List<Integer> getFrontMinus(List<Integer> a)
	{
		List<Integer> b=getLinesFlag(a.get(a.size()-1));
		Iterator<Integer> it = b.iterator();
		while(it.hasNext()){
		    Integer x = it.next();
		    if(a.contains(x)){
		        it.remove();
		    }
		}
		return b;
	}
	
	//添加路径
	public boolean addItem(String []name,Integer num)
	{
		if(itemsFlag.contains(num))
			return false;
		else
		{
			List<String> itemsTemp=new ArrayList<String>();
			for(String it:name)
			{
				itemsTemp.add(it);
			}
			itemsFlag.add(num);
			items.add(itemsTemp);
			return true;
		}
	}
	//判断中节点
	public boolean isPoints(String name)
	{
		if(getPointsFlag(name).size()>=2)
			return true;
		return false;
	}
	//返回在同一路线上从起点到终点的顺序路径
	public List<String> getPTP(String start,String end)
	{
		List<String> itemsTemp=new ArrayList<String>();
		if(!start.equals(end))
		{
			int g_start=-1;
			int g_end=-1;
			for(List<String> it:items)
			{
				if(!(it.contains(start)&&it.contains(end)))
					continue;
				for(String init:it)
				{
					if(init.equals(start)||init.equals(end))
					{
						itemsTemp.add(init);
						if(itemsTemp.size()!=1)
						{
							g_end=it.indexOf(init);
							break;
						}
						else
						{
							g_start=it.indexOf(init);
						}
					}
					else if(itemsTemp.size()!=0)
					{
						itemsTemp.add(init);
					}
				}
				if(it.get(0).equals(it.get(it.size()-1)))
				{
					if(it.size()-itemsTemp.size()<itemsTemp.size())
					{
						List<String> itTemp=new ArrayList<String>(it);
						List<String> itTemptemp=new ArrayList<String>(it);
						itemsTemp.clear();
						itemsTemp.addAll(itTemp.subList(g_end, itTemp.size()-1));
						itemsTemp.addAll(itTemptemp.subList(0, g_start+1));
						
					}
				}
			}
			if(itemsTemp.size()!=0&&itemsTemp.get(0).equals(end))
				Collections.reverse(itemsTemp);
		}
		else
		{
			itemsTemp.add(start);
		}
		return itemsTemp;
	}
	//返回点包含路线
	public List<Integer> getPointsFlag(String name)
	{
		List<Integer> itemsFlagTemp=new ArrayList<Integer>();
		
		int itemsLengthTemp=items.size();
		for(int i=0;i<itemsLengthTemp;i++)
		{
			List<String> itemsTemp=items.get(i);
			for(String init:itemsTemp)
			{
				if(init.equals(name))
				{
					itemsFlagTemp.add(itemsFlag.get(i));
					break;
				}
			}
		}
		return itemsFlagTemp;
	}
	//返回路线所涉及的路线
	public List<Integer> getLinesFlag(Integer num)
	{
		List<Integer> itemsFlagTemp=new ArrayList<Integer>();
		if(!itemsFlag.contains(num))
			return itemsFlagTemp;
		
		List<String> itemsPointsTemp=new ArrayList<String>();
		List<String> itemsTemp=items.get(itemsFlag.indexOf(num));
		for(String init:itemsTemp)
		{
			if(isPoints(init))
			{
				itemsPointsTemp.add(init);
			}
		}
		for(String it:itemsPointsTemp)
		{
			itemsFlagTemp.addAll(getPointsFlag(it));
		}
		Iterator<Integer> it = itemsFlagTemp.iterator();
		while(it.hasNext()){
		    Integer x = it.next();
		    if(x==num){
		        it.remove();
		    }
		}
		
		return itemsFlagTemp;
	}
	//返回线路所涉及的中节点
	public List<String> getLinePoints(Integer num)
	{
	
		List<String> itemsPointsTemp=new ArrayList<String>();
		List<String> itemsTemp=items.get(itemsFlag.indexOf(num));
		for(String init:itemsTemp)
		{
			if(isPoints(init))
			{
				itemsPointsTemp.add(init);
			}
		}
		return itemsPointsTemp;
	}
	//返回从线路1到线路2所要经过的节点
	public String getLTLPoints(Integer line1,Integer line2)
	{
		List<String> itemsPointsTemp=getLinePoints(line1);
		for(String it:itemsPointsTemp)
		{
			List<Integer> itemsFlagTemp=getPointsFlag(it);
			if(itemsFlagTemp.contains(line2))
				return it;
		}
		return "";
	}
	//判断节点是否存在
	public boolean isExistPoint(String name)
	{
		int itemsLengthTemp=items.size();
		for(int i=0;i<itemsLengthTemp;i++)
		{
			List<String> itemsTemp=items.get(i);
			for(String init:itemsTemp)
			{
				if(init.equals(name))
				{
					return true;
				}
			}
		}
		return false;
	}
	//判断两节点是否在同一线路上
	public boolean isSameLine(String name1,String name2)
	{
		for(List<String> it:items)
		{
			if(it.contains(name1)&&it.contains(name2))
				return true;
		}
		return false;
	}
	//返回两节点共同的线路
	public Integer getSameLine(String name1,String name2)
	{
		for(int i=0;i<items.size();i++)
		{
			if(items.get(i).contains(name1)&&items.get(i).contains(name2))
			{
				return itemsFlag.get(i);
			}
		}
		return -1;
	}

}
