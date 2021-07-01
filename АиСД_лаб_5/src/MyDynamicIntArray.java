package myarr;

public class MyDynamicIntArray{
	private int index = 0;
	private int[] arr;
	private final int DEFAULT_SIZE = 20;
	private final double MAX_LOAD = 0.75;
	
	public MyDynamicIntArray(){
		arr = new int[DEFAULT_SIZE];
	}
	
	public void add(int elem){
		if(index >= (arr.length * MAX_LOAD)){
			int[] temp_arr = new int[arr.length];
			int i = 0;
			for(int num : arr){
				temp_arr[i] = num;
				i += 1;
			}
			
			arr = new int[i+DEFAULT_SIZE];
			i = 0;
			for(int num : temp_arr){
				arr[i] = num;
				i += 1;
			}
		}
		
		arr[index] = elem;
		index += 1;
	}
	
	public void replaceElem(int elem, int pos){
		if(pos < arr.length){
			arr[pos] = elem;
		}
	}
	
	public int getElem(int pos){
		return arr[pos];
	}
	
	public int getLength(){
		return index;
	}
	
	public void print(int start, int end){//выводит элементы массива до указаного индекса (не включительно)
		for(int i = start; i < end; i++){
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
	
	public void print(){
		print(0, index);
	}
}
