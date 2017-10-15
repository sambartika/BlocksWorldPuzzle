package tamu.ai.blocksworld;

import java.util.*;

public class Controller {
	
	static Actions act;
	private static Scanner scan;
	static PriorityQueue<String> open= new PriorityQueue<String> (new StateComparetor());
	static HashSet<String> visited= new HashSet<String>();
	static Map<String, String> mapp= new HashMap<String, String>();
	static int noIterations;
	static int maxQueueSize;
	static int maxIterations= 15000;
	static int depth=0;
	
	static int findMin(String ss)
	{
		int pos= 1000;
		char min='Z';
		
		for(int i=0; i<ss.length(); i++)
		{
			if(min> ss.charAt(i))
			{
				min= ss.charAt(i);
				pos= i;
			}
		}
		
		return pos;
	}
		
	public static int computeHeuristic(Node curr, Node goal)
	{
		int hn= 0;
		int len=0;
		
		if(curr.stkArr[0]!= null)
		{
			len= curr.stkArr[0].length();
			
			if(len >0 && !curr.stkArr[0].equals(goal.stkArr[0].substring(0, len)))
			{
				int i=0;
				while(i< len && curr.stkArr[0].charAt(i)== goal.stkArr[0].charAt(i))
				{
					i++;
				}

				hn= hn+ 2*(len-i);
			}
		}

		
		for(int i=1; i< Node.noStks; i++)
		{
			if(curr.stkArr[i]!= null)
			{
				int count= curr.stkArr[i].length();
				int posMin=1000;
				len= curr.stkArr[i].length();
				if(len >1)
				{
					int collitions=0;
					posMin= findMin(curr.stkArr[i]);
					
					if((len-posMin-1)>1)
					{
						count= count+ (len-posMin-1);
					}
					else
					{	
						for(int j=0; j<len-1; j++)
						{
							if(curr.stkArr[i].charAt(j) < curr.stkArr[i].charAt(j+1))
								collitions++;
								
						}
						count+= collitions;
					}
				}
				hn= hn+ count;
			}
		}		
		return hn;
	}

	
	public static Node aStarSearch(Node start, Node goal)
	{
		noIterations=0;
		maxQueueSize=0;
		int gn=0;
		int hn= computeHeuristic(start, goal);
		String[] keyVal= start.concatAll(null, gn, hn);
		open.add(keyVal[1]+ keyVal[0]);
		mapp.put(keyVal[0], keyVal[1]);
		
		while(!open.isEmpty())
		{
			String curr1= open.remove();	
			if(open.size()> maxQueueSize)
			{
				maxQueueSize= open.size();
			}
			
			String str[]= curr1.split(",", -1);
			int len= str.length;
			int i= 2+Node.noStks;
			String ss[]= new String[Node.noStks];
			Node[] curr= new Node[2];			
			
			String kv0="";
			for(int j=0; i<len && j< Node.noStks; i++, j++)
			{
				ss[j]= str[i];
				kv0= kv0+ str[i]+",";
			}
			curr[0]= new Node(ss, Node.noStks, Node.noBlocks);
			
			String kv1= str[0]+","+str[1]+",";
			i=2;
			for(int j=0; j< Node.noStks; i++, j++)
			{
				ss[j]= str[i];
				kv1= kv1+str[i]+",";
			}
			curr[1]= new Node(ss, Node.noStks, Node.noBlocks);
			
			int f= Integer.parseInt(str[0])+Integer.parseInt(str[1]);
			System.out.println("iterations= " +noIterations+"    queue="+open.size()
			+"    f=g+h="+f+"    depth="+str[0]);
			
			noIterations++;
			if(curr[0].goalTest(goal))
			{
				depth= Integer.parseInt(str[0]);
				return curr[0];
			}
			
			if(noIterations> maxIterations)
				return null;
			
			visited.add(kv0);
			
			Node[] succesor= act.createSuccesor(curr[0]);
			
			for(i=0; i< succesor.length; i++)
			{			
				if(succesor[i]== null)
					continue;
				
				keyVal= succesor[i].concatAll(curr[0], 0, 0);
				kv1= mapp.get(keyVal[0]);
				
				if(kv1!= null)
				{
					String par[]= kv1.split(",");
					if(visited.contains(keyVal[0]))
					{
						if(Integer.parseInt(par[0])> Integer.parseInt(str[0])+1)
						{
							
							keyVal= succesor[i].concatAll(curr[0], Integer.parseInt(str[0])+1, Integer.parseInt(str[1]));
							mapp.put(keyVal[0], keyVal[1]);
						}
						continue;
						
					}
					else if(open.contains(kv1+keyVal[0]))
					{
						if(Integer.parseInt(par[0])> Integer.parseInt(str[0])+1)
						{
							keyVal= succesor[i].concatAll(curr[0], Integer.parseInt(str[0])+1, Integer.parseInt(str[1]));
							open.remove(kv1+keyVal[0]);
							open.add(keyVal[1]+ keyVal[0]);
							mapp.put(keyVal[0], keyVal[1]);
						}
						continue;
					}
				}
				else
				{
					hn= computeHeuristic(succesor[i], goal);
					gn= Integer.parseInt(str[0])+1;
					keyVal= succesor[i].concatAll(curr[0], gn, hn);
					open.add(keyVal[1]+ keyVal[0]);
					mapp.put(keyVal[0], keyVal[1]);
				}
				
			}
			
		}
		
		return null;
	}
	
	static void tracePath(Node goal)
	{
		if(goal== null)
			return;
		
		String keyVal[]= goal.concatAll(null, 0, 0);
		String val= mapp.get(keyVal[0]);
		if(val== null)
			return;
		String subVal[]= val.split(",", -1);
		String ss[]= new String[Node.noStks];

		if(subVal.length< Node.noStks)
			return;
		
		for(int j=0; j< Node.noStks; j++)
		{
			ss[j]= subVal[j+2];
		}
		Node parent= new Node(ss, Node.noStks, Node.noBlocks);
		tracePath(parent);
		goal.showState();
	}
	
	public static void main(String args[])
	{
		scan = new Scanner(System.in);
		int noStks= scan.nextInt();
		int noBlocks= scan.nextInt();
		act= new Actions(noStks);
				
		String stk[]= new String[noStks];
		stk[0]="";
		for(int i=0; i< noBlocks; i++)
		{
			stk[0]= stk[0]+ (char)(65+i);
		}
		
		for(int i=1; i< noStks; i++)
		{
			stk[i]= "";
		}
		
		Node goalState= new Node(stk, noStks, noBlocks);
		
		int avgDepth=0;
		int avgGoalTests=0;
		int avgQueueSize=0;
		int successCount=0;
		for(int i=0; i<10;i++)
		{
			System.out.println("Random input no "+ (i+1));
			System.out.println("Goal state is");
			goalState.showState();
			
			Node startState= problemGenerator(goalState, noStks, noBlocks); 
			System.out.println("Start state is");
			startState.showState();
			
			Node goal= aStarSearch(startState, goalState);
			if(goal!= null)
			{
				successCount++;
				System.out.println("Success! depth="+depth+"    total_goal_tests="+noIterations
						+"    max_queue_size="+ maxQueueSize);
				tracePath(goal);
			}
			else
			{
				System.out.println("Failure after iterations"+ maxIterations);
			}
			avgDepth+= depth;
			avgGoalTests+= noIterations;
			avgQueueSize+= maxQueueSize;
			
			open.clear();
			visited.clear();
			mapp.clear();
			depth=0;
			maxQueueSize= 0;
			noIterations=0;
		}
		
		System.out.println("The number of failures are "+ (10- successCount));
		if(successCount>0)
			System.out.println("average depth="+(int)Math.ceil(avgDepth/successCount)+"    average total_goal_tests="
				+(int)Math.ceil(avgGoalTests/successCount)
				+"    average max_queue_size="+ (int)Math.ceil(avgQueueSize/successCount));

	}
	
	static Node problemGenerator(Node goalState, int noStks, int noBlocks)
	{
		Random rand = new Random();
		//int count=0;
		//String ss[]={"IHG", "EJA", "BCFD"};
		Node startState= new Node(goalState.stkArr, Node.noStks, Node.noBlocks);
		
		for(int i=0;i< noBlocks; i++)
		{
			int n1=0;
			int  n2 = rand.nextInt(noStks-1)+1 ;
			while(!act.isValid(startState, n1, n2))
			{
				n2 = rand.nextInt(noStks) ;
			}
			startState= act.performAction(startState, n1, n2);
			//count++;			
		}
		
		for(int i=0; i<20;i++)
		{
			int  n1 = rand.nextInt(noStks) ;
			int  n2 = rand.nextInt(noStks) ;
			while(!act.isValid(startState, n1, n2) || startState.stkArr[n2].length()+1 > noBlocks/2)
			{
				n1= rand.nextInt(noStks) ;
				n2 = rand.nextInt(noStks) ;
			}
			startState= act.performAction(startState, n1, n2);
			//count++;
		}
		
		for(int i=0; i< startState.stkArr.length; i++)
		{
			if(startState.stkArr[i]== null || startState.stkArr[i].length()==0)
			{
				int n2= i;
				int  n1 = rand.nextInt(noStks) ;
				while(!act.isValid(startState, n1, n2))
				{
					n1= rand.nextInt(noStks) ;
				}
				
				startState= act.performAction(startState, n1, n2);
				//count++;
			}
		}
		
		return startState;
	}
}
