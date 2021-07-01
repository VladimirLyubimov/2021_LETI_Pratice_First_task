package fileio;

import java.util.Scanner;
import java.io.*;
import myarr.MyDynamicIntArray;

public class FileIO{
	public static int[] readArrayFromFile(String filename){
		MyDynamicIntArray arr = new MyDynamicIntArray();
		int[] res = null;
		try{
			FileReader fin = new FileReader(filename);
			int c;
			while((c = fin.read()) != -1){
				arr.add(c);
				//System.out.print(c + " ");
			}			
			//System.out.println();
			fin.close();
		}
		catch(IOException err){
			System.out.println(err.getMessage());
			return res;
		}
		
		if(arr.getLength() > 0){
			char[] ch_st = new char[arr.getLength()];
			for(int i = 0; i < arr.getLength(); i++){
				ch_st[i] = (char) arr.getElem(i);
			}
			String st = new String(ch_st);
			String[] values = st.split("\\s");
			res = new int[values.length];
			int i = 0;
			while(i < res.length){
				res[i] = Integer.parseInt(values[i]);
				i++;
			}
		}
		else{
			return res;
		}
		
		return res;
	}
}
