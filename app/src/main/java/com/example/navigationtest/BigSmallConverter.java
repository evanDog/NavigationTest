package com.example.navigationtest;

public class BigSmallConverter 
{
	public static String Str2Char(String str)      //把字串內容先轉到字元陣列，再把字元陣列中大寫變成小寫，小寫變成大寫，然後在轉成字串形式回傳
    {
        int i;
        char[] tempChar=str.toCharArray();
        
        for(i=0;i<tempChar.length;i++)
        {
            
            if(tempChar[i]>=65 & tempChar[i]<=90)   //如果字元陣列的元素是A~Z,則轉成a~z
            {
                tempChar[i]+=32;
                      
            }else if(tempChar[i]>=97 & tempChar[i]<=122) //如果字元陣列的元素是a~z,則轉成A~Z
            {
                tempChar[i]-=32;
                       
            }else
            {
                           
            }
                System.out.print((char)tempChar[i]);
        }
        
        String temp=new String(tempChar);   //把字元陣列傳回字串
        
        return temp;
    
    }
    
}