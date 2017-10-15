package tamu.ai.blocksworld;

public class Node {
	String stkArr[];
	static int noStks;
	static int noBlocks;	
	
	Node(String sl[], int noStks, int noBlocks)
	{
		Node.noStks= noStks;
		Node.noBlocks= noBlocks;
		stkArr= new String[sl.length];
		
		for(int i=0; i< sl.length; i++)
		{
			if(sl[i]!= null)
				stkArr[i]= sl[i];
			else
				stkArr[i]= "";
		}
		
	}
	
	void showState()
	{
		for(int i=0; i< noStks; i++)
		{
			if(stkArr[i]!= null)
			{
				System.out.println((i+1)+"|  "+ stkArr[i]);
			}
		}
		System.out.println();
	}
	
	boolean goalTest(Node goal)
	{
		for(int i=0; i< noStks; i++)
		{
			if(this.stkArr[i]== null)
			{
				if(goal.stkArr[i]!= null)
				{
					if(goal.stkArr[i].length()!=0)
						return false;
				}
			}
			else if(goal.stkArr[i]== null)
			{
				if(this.stkArr[i]!= null)
				{
					if(this.stkArr[i].length()!=0)
						return false;
				}
			}
			else if(!goal.stkArr[i].equals(this.stkArr[i]))
				return false;
		}
		return true;
	}
	
	String[] concatAll(Node parent, int gn, int hn)
	{
		String[] keyVal= new String[2];
		keyVal[0]="";
		keyVal[1]="";
		for(int i=0; i< noStks; i++)
		{
			keyVal[0]= keyVal[0]+this.stkArr[i]+",";
		}

		keyVal[1]= gn+","+hn+",";
		if(parent!= null)
		{
			for(int i=0; i< noStks; i++)
			{
				keyVal[1]= keyVal[1]+parent.stkArr[i]+",";
			}
		}
		else
		{
			for(int i=0; i< noStks; i++)
			{
				keyVal[1]= keyVal[1]+",";
			}
		}
		
		return keyVal;
	}

}
