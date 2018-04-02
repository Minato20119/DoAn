/**
 * 
 */
package test;

/**
 * @author Minato
 *
 */
public class Test {
	
	public static void main(String [] args) {
		int x = 13;
		
		StringBuilder sb = new StringBuilder();
		
		String s = "0000000" + Integer.toBinaryString(x);
		System.out.println(Integer.toBinaryString(x));
		System.out.println("s: " + s);
        s = s.substring(s.length() - 8); 
        
        System.out.println(s);
        
        System.out.println(s.substring(0, 4) + "9999");

	}
}
