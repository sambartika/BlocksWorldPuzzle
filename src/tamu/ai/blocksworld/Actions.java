package tamu.ai.blocksworld;

//Action file is this
public class Actions {

	static String actArr[];
	static int noActs;
	
	static void createActions(String sentence[], String permute[], int used[], int len, int currLen)
	{
		if(currLen== len)
		{
			actArr[noActs]= new String();
			actArr[noActs]= permute[0]+","+permute[1];
			noActs++;
			return;
		}
		if(currLen> len)
		{
			return;
		}

		for(int i=0; i<sentence.length; i++)
		{
			if(used[i]== 0)
			{
				used[i]= 1;
				permute[currLen]= sentence[i];
				createActions(sentence, permute, used, len, currLen+1);
				used[i]= 0;
			}
		}
	}
	
	Actions(int noStks)
	{
		Actions.noActs= 0;
		Actions.actArr= new String[noStks*(noStks-1)];
		String acts[]= new String[noStks];
		for(int i=0; i< noStks; i++)
		{
			acts[i]= i+"";
		}
		
		int used[]= new int[noStks];
		for(int j=0; j<noStks; j++ )
			used[j]= 0;
		
		String str[]= new String[noStks];
		
		createActions(acts,str, used, 2, 0);
	}
	
	Node[] createSuccesor(Node currState)
	{
		Node children[]= new Node[noActs];
		
		for(int i=0; i<noActs; i++)
		{
			String inds[]= actArr[i].split(",");
			int ind1= Integer.parseInt(inds[0]);
			int ind2= Integer.parseInt(inds[1]);
			if(isValid(currState, ind1, ind2))
			{
				children[i]= performAction(currState, ind1, ind2);
			}
		}
		return children;
		
	}
	
	boolean isValid(Node state, int ind1, int ind2)
	{
		if(ind1== ind2)
			return false;
		if(state.stkArr[ind1]==null)
		{
			return false;
		}
		
		int len= state.stkArr[ind1].length();

		if(len>=1)
			return true;
		
		return false;
	}
	
	Node performAction(Node state, int ind1, int ind2)
	{
		String ss[]= new String[Node.noStks];
		
		for(int i=0; i< state.stkArr.length; i++)
		{
			if(state.stkArr[i]!= null)
				ss[i]= state.stkArr[i];
			else
				ss[i]= "";
		}
		int len= ss[ind1].length()-1;
		char popped= ss[ind1].charAt(len);
		if(ss[ind2]== null)
			ss[ind2]= new String("");
		
		ss[ind2]= ss[ind2]+popped;
		ss[ind1]= ss[ind1].substring(0, len);
		Node newState= new Node(ss, Node.noStks, Node.noBlocks);
		
		return newState;
	}
}
