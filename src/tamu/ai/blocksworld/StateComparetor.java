package tamu.ai.blocksworld;

import java.util.Comparator;

public class StateComparetor implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		String ss0[]= arg0.split(",");
		String ss1[]= arg1.split(",");
		int a0= Integer.parseInt(ss0[0])+Integer.parseInt(ss0[1]);
		int a1= Integer.parseInt(ss1[0])+Integer.parseInt(ss1[1]);
		if(a0== a1)
		{
			return (Integer.parseInt(ss0[1])-Integer.parseInt(ss1[1]));
		}
		
		return (a0-a1);
	}

}
