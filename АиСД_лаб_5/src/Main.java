import myarr.*;
import heap.*;
import fileio.*;
import java.util.Scanner;

public class Main{
	public static void main(String[] args){
		System.out.println("Input file name:");
		Scanner sc = new Scanner(System.in);
		int [] arr = FileIO.readArrayFromFile(sc.next());
		
		if(arr == null || arr.length == 1){
			System.out.println("Empty heap!");
			return;
		}
		
		int n = arr[0];
		int[] h_arr = new int[arr.length-1];
		for(int i  = 1; i < arr.length; i++){
			h_arr[i-1] = arr[i];
		}
		
		arr = null;
		
		System.out.println("Original array:");
		for(int i  = 0; i < h_arr.length; i++){
			System.out.print(h_arr[i] + " ");
		}
		System.out.println();
		System.out.println("Heap dimension is " + n);
		
		System.out.println("\n\nHeap buildng:");
		MaxHeap tree = new MaxHeap(h_arr, n);
		System.out.println("Heap buildng has been ended!");
		tree.printAsHeap(null, 0);
		System.out.println("\nHeap as array:");
		tree.printAll();
		
		System.out.println("Pres any letter to continue:");
		sc.next();
		
		System.out.println("\n\nPath finding:");
		tree.maxSonPath();
		System.out.println("\n\nPath finding has been ended!");
	}
}
