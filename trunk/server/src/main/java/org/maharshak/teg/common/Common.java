package org.maharshak.teg.common;

public class Common {

	
	public static final int TEG_DEFAULT_PORT=2000;
	
	
	
	



	public static final String TEG_DIRRC=".teg/";

	


	public static final byte M_INF = 1;
	public static final byte M_MSG = 2;
	public static final byte M_IMP = 4;
	public static final byte M_ERR = 8;
	public static final byte M_ALL = (M_INF|M_MSG|M_IMP|M_ERR);

	
	public static final String TEG_NAME = "TEG";
	public static String VERSION = "0.11.2";


	public static int RANDOM_MAX(int smallest, int largest) {
		return ((int) (Math.random()*largest-smallest))+smallest;
	}

	//	public static char *g_reglas[];
//	public static RULES g_reglas[];//Rules




	//	#define MODALIDAD_READONLY	0
	//	#define MODALIDAD_PLAYER	1





	public static 	String g_estados[];


	
	/* Removes invalid characters =;\,:/%. from string n and replaces them with "." */
	public static TEG_STATUS strip_invalid( String n){
		
			assert(n!=null&&!n.isEmpty());

			int l = n.length();


			for(int i=0;i<l;i++) {
				if( n.charAt(i)=='=' || n.charAt(i)==';' || n.charAt(i)=='\\' || n.charAt(i)==',' || n.charAt(i)==':' || n.charAt(i)=='/' || n.charAt(i)=='%')
					//					n.charAt([i]) = '.';
					throw new UnsupportedOperationException("Bad function");
			}


			return TEG_STATUS.TEG_STATUS_SUCCESS;

		}



	}
