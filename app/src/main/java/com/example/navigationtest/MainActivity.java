package com.example.navigationtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;


public class MainActivity extends ExpandableListActivity {

	private static final String ITEM_NAME = "Item Name";
    private static final String ITEM_SUBNAME = "Item Subname";	
    private ExpandableListAdapter mExpaListAdap;
    private TextView mHint;
    public static int targetChild,targetGroup;
	//private Button btnToylet, btnClinic;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupViewComponent();
        //從資源R取得介面元件
        //btnToylet = (Button)findViewById(R.id.btnToylet);
        //btnClinic = (Button)findViewById(R.id.btnClinic);
        
        //btnToylet.setOnClickListener(btnToyletOnClick);
        //btnClinic.setOnClickListener(btnClinicOnClick);
    }
    
    private void setupViewComponent()
    {
    	mHint = (TextView)findViewById(R.id.hint);
    	
    	List<Map<String, String>> groupList = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childList2D = new ArrayList<List<Map<String, String>>>();
       
        //新增List
        //LIST0 門口---------------------------------------------------------------------------
        Map<String, String> group = new HashMap<String, String>();
        group.put(ITEM_NAME, "醫院出入口");
        group.put(ITEM_SUBNAME, "選擇您要去的醫院出入口");
        groupList.add(group);      
        
        List<Map<String, String>> childList = new ArrayList<Map<String, String>>();
        	//child00
        	Map<String, String> child00 = new HashMap<String, String>();
        	child00.put(ITEM_NAME, "第一醫療大樓出入口");
        	child00.put(ITEM_SUBNAME, "導航至第一醫療大樓出入口");
        	childList.add(child00);
        	//child01
        	Map<String, String> child01 = new HashMap<String, String>();
        	child01.put(ITEM_NAME, "第二醫療大樓出入口");
        	child01.put(ITEM_SUBNAME, "導航至第二醫療大樓出入口");
        	childList.add(child01);
        	childList2D.add(childList);
        	
        	
        //LIST1 掛號批價處---------------------------------------------------------------------------
        Map<String, String> group1 = new HashMap<String, String>();
        group1.put(ITEM_NAME, "掛號批價處");
        group1.put(ITEM_SUBNAME, "選擇您要去的掛號批價處");
        groupList.add(group1);      
        
        List<Map<String, String>> childList1 = new ArrayList<Map<String, String>>();
        	//child10
        	Map<String, String> child10 = new HashMap<String, String>();
        	child10.put(ITEM_NAME, "第一醫療大樓F1掛號批價處");
        	child10.put(ITEM_SUBNAME, "導航至第一醫療大樓掛號批價處");
        	childList1.add(child10);
        	childList2D.add(childList1);
        	
        	
        //LIST2 門診科別---------------------------------------------------------------------------
        	Map<String, String> group2 = new HashMap<String, String>();
            group2.put(ITEM_NAME, "門診科別");
            group2.put(ITEM_SUBNAME, "選擇您要去的門診科別");
            groupList.add(group2);      
            
            List<Map<String, String>> childList2 = new ArrayList<Map<String, String>>();
            	//child20
            	Map<String, String> child20 = new HashMap<String, String>();
            	child20.put(ITEM_NAME, "一般內科");
            	child20.put(ITEM_SUBNAME, "導航至一般內科");
            	childList2.add(child20);
            	//child21
            	Map<String, String> child21 = new HashMap<String, String>();
            	child21.put(ITEM_NAME, "心臟科");
            	child21.put(ITEM_SUBNAME, "導航至心臟科");
            	childList2.add(child21);
            	//child22
            	Map<String, String> child22 = new HashMap<String, String>();
            	child22.put(ITEM_NAME, "腸胃肝膽科");
            	child22.put(ITEM_SUBNAME, "導航至腸胃肝膽科");
            	childList2.add(child22);            
            	//child22
            	Map<String, String> child23 = new HashMap<String, String>();
            	child23.put(ITEM_NAME, "骨科");
            	child23.put(ITEM_SUBNAME, "導航至骨科");
            	childList2.add(child23);
            	//child24
            	Map<String, String> child24 = new HashMap<String, String>();
            	child24.put(ITEM_NAME, "小兒科");
            	child24.put(ITEM_SUBNAME, "導航至小兒科");
            	childList2.add(child24);                        	
            	childList2D.add(childList2);        
        //LIST3 檢查站---------------------------------------------------------------------------
            	Map<String, String> group3 = new HashMap<String, String>();
                group3.put(ITEM_NAME, "檢查站");
                group3.put(ITEM_SUBNAME, "選擇您要去的檢查站");
                groupList.add(group3);      
                
                List<Map<String, String>> childList3 = new ArrayList<Map<String, String>>();
                	//child30
                	Map<String, String> child30 = new HashMap<String, String>();
                	child30.put(ITEM_NAME, "抽血站");
                	child30.put(ITEM_SUBNAME, "導航至抽血站");
                	childList3.add(child30);
                	//child31
                	Map<String, String> child31 = new HashMap<String, String>();
                	child31.put(ITEM_NAME, "放射科");
                	child31.put(ITEM_SUBNAME, "導航至放射科");
                	childList3.add(child31);
                	//child32
                	Map<String, String> child32 = new HashMap<String, String>();
                	child32.put(ITEM_NAME, "胃鏡室");
                	child32.put(ITEM_SUBNAME, "導航至胃鏡室");
                	childList3.add(child32);            
                	//child32
                	Map<String, String> child33 = new HashMap<String, String>();
                	child33.put(ITEM_NAME, "心臟超音波檢查室");
                	child33.put(ITEM_SUBNAME, "導航至心臟超音波檢查室");
                	childList3.add(child33);
                	//child34
                	Map<String, String> child34 = new HashMap<String, String>();
                	child34.put(ITEM_NAME, "心電圖室");
                	child34.put(ITEM_SUBNAME, "導航心電圖室");
                	childList3.add(child34);                        	
                	childList2D.add(childList3);        
        
        //LIST4 藥局領藥---------------------------------------------------------------------------
                    Map<String, String> group4 = new HashMap<String, String>();
                    group4.put(ITEM_NAME, "門診領藥處");
                    group4.put(ITEM_SUBNAME, "選擇您要去的門診領藥處");
                    groupList.add(group4);      
                    
                    List<Map<String, String>> childList4 = new ArrayList<Map<String, String>>();
                    	//child40
                    	Map<String, String> child40 = new HashMap<String, String>();
                    	child40.put(ITEM_NAME, "門診領藥處");
                    	child40.put(ITEM_SUBNAME, "導航至門診領藥處");
                    	childList4.add(child40);
                    	childList2D.add(childList4);
        //新增List結束
                
        mExpaListAdap = new SimpleExpandableListAdapter(
                this,
                groupList,
                R.layout.inspection_onlyone_itemlist,
                new String[] {ITEM_NAME, ITEM_SUBNAME},                   
                new int[] {R.id.consulting_Name, R.id.consulting_Num_Now},
                childList2D,                                     
                R.layout.inspection_onlyone_itemlist_child,
                new String[] {ITEM_NAME, ITEM_SUBNAME},
                new int[] {R.id.consulting_Name, R.id.consulting_Num_Now}
                );

            setListAdapter(mExpaListAdap);        
    }
    
    @Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) 
    {
		// 決定目的地
    	targetGroup = groupPosition;
    	targetChild = childPosition;
		 
		String s = "選項名稱"+childPosition+"群組名稱"+groupPosition;
		mHint.setText(s);
    	startActivity(new Intent().setClass(MainActivity.this, Navigation.class));   	

		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}
    
/*	
	private Button.OnClickListener btnToyletOnClick = new Button.OnClickListener()
    {
    	public void onClick(View v)
    	{
    		target = "廁所";
    		startActivity(new Intent().setClass(MainActivity.this, Navigation.class));
    		
    	}
    };
    
    private Button.OnClickListener btnClinicOnClick = new Button.OnClickListener()
    {
    	public void onClick(View v)
    	{
    		target = "大外科門診區";
    		startActivity(new Intent().setClass(MainActivity.this, Navigation.class));

    	}
    };
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
