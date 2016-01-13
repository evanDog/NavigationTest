package com.example.navigationtest;

import java.io.InputStream;
import java.util.Calendar;

import com.example.navigationtest.GifHelper.GifFrame;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.ViewGroup.LayoutParams;


public class Navigation extends Activity implements SensorEventListener
{
	private Matrix matrix = new Matrix();
	private float minScaleR;
	
	public PendingIntent mPendingIntent;
	//public TextView uidtext;
	public IntentFilter[] mFilters;
	public NfcAdapter mAdapter;
	public String[][] mTechLists;	
	
	public float currentDegree = 0f;
	public static float currentDegree1;
	public static String mode = "on";// 羅盤旋轉模式
	
	
	private ImageView mapImage;
	
	
	private TextView txtTarget;
	
	private ImageView senseNfc;
			
	private ImageButton zoomInButton;
	private ImageButton zoomOutButton;
	private DisplayMetrics dm;
	private Bitmap bitmap;
	private Button compButton,cancelCompButton, nextFloorButton, firstFloorButton;
	
	private SensorManager sm;
	
	int targetGroup = MainActivity.targetGroup;//目的地清單群組變數
	int targetChild = MainActivity.targetChild;//目的地子清單變數
	public static String result1 = "null";
    public String loadImage = "false";
	
    // GIF code
    private PlayGifTask mGifTask;
    ImageView iv;
    InputStream is;
    
    //progress bar
    
    
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation1);
        //setTarget();//呼叫setTarget方法
        
        //gifcode
        iv = (ImageView)findViewById(R.id.gifimg);
        //gifcode end
        
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
		zoomInButton = (ImageButton)findViewById(R.id.zoomInButton);
		zoomOutButton = (ImageButton)findViewById(R.id.zoomOutButton);
		compButton = (Button)findViewById(R.id.enableCompButton);
		cancelCompButton = (Button)findViewById(R.id.disableCompButton);
		nextFloorButton = (Button)findViewById(R.id.tosecondfloor);
		firstFloorButton = (Button)findViewById(R.id.tofirstfloor);
		senseNfc = (ImageView)findViewById(R.id.sensenfc);
		senseNfc.setImageResource(R.drawable.nfc);
		//bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imgclinic);
		//bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imgtoylet);
		//imgclinic.setImageBitmap(bitmap);
		//imgclinic.setImageBitmap(bitmap);

        //Log.d("error", "oncreate123");
        //不知道幹啥
      //檢查是否具有NFC功能
		
        
      		mAdapter = NfcAdapter.getDefaultAdapter(this);
      		if (mAdapter == null)
      		{
      			Toast.makeText(this, "手機不支援NFC功能", Toast.LENGTH_LONG).show();
      		}
      		else
      		{
      			Toast.makeText(this, "Got NFC!", Toast.LENGTH_LONG).show();		
      			

      			mAdapter = NfcAdapter.getDefaultAdapter(this);
      			mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
      			IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
      			Log.d("error", "1");
      			//Setup an intent filter for all MIME based dispatches        
      			try 
      			{
      				ndef.addDataType("*/*");
      			} 
      			catch (MalformedMimeTypeException e) 
      			{
      				throw new RuntimeException("fail", e);
      			}
      			        
      			mFilters = new IntentFilter[] { ndef, };

      			// Setup a tech list for all NfcF tags
      			mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };
      			        
      		}//end不知道幹啥
     		
      		
    }
    

    
    //不知道幹啥
	
	@Override
	protected void onNewIntent(Intent intent) 
	{
		//Toast.makeText(this, "Discovered tag with intent: " + intent, Toast.LENGTH_LONG).show();
        resolveIntent(intent);
	}

	@Override
	protected void onPause() 
	{
		
		super.onPause();
		mAdapter.disableForegroundDispatch(this);

	}

 	@Override
 	protected void onResume() 
 	{
 		super.onResume();
 		mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists); 	         
 	}//end 不知道幹啥
 	
 	

 	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
 		super.onDestroy();
 		Log.d("onDestroy","onDestroy");
 		if(loadImage == "true")
		{
			((BitmapDrawable)iv.getDrawable()).getBitmap().recycle();
			Log.d("onDestroy","recycle");
		}
 		Log.d("onDestroy","overRecycle");
 		//Gif code
 		if(null != mGifTask) mGifTask.stop();
	}



	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下返回键,關閉Activity
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			Navigation.this.finish();
			Log.d("onKeyDown();","stopActivity");
		}
				
		return super.onKeyDown(keyCode, event);
 	}
/*將按鈕的目的地顯示出來
 	public void setTarget()
    {
    	//Log.d("error", "321");
    	//宣告介面元件(使用介面元件一定要宣告)
    	txtTarget = (TextView)findViewById(R.id.txtTarget);
    	//下三行程式碼可以做到一模一樣的是只是比較複雜
    	//String originalTarget = txtTarget.getText().toString();
    	//originalTarget += target1;
    	//txtTarget.setText(originalTarget);
    	txtTarget.append(target1);
    }
*/
    
	//解析讀到的NFC封包並讀取UID
    void resolveIntent(Intent intent) 
 	{
 		// 1) Parse the intent and get the action that triggered this intent
        String action = intent.getAction();
        // 2) Check if it was triggered by a tag discovered interruption.
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) 
        {
        // 3) Get an instance of the TAG from the NfcAdapter
        	
        	Tag tagFromIntent = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        	//tag ID
        	byte[] id = tagFromIntent.getId();
        	try 
        	{
        		String result = NfcIdConverter.getHexString(id, id.length);
        		//執行setTarget方法
            	txtTarget = (TextView)findViewById(R.id.txtTarget);
            	txtTarget.append(result);
            	//txtTarget.append(result+"Group:"+targetGroup+"Child:"+targetChild);
            	result1 = result;
            	catchMap(result1);
            	//progrss bar
            	
            	
            	
        		//((TextView)findViewById(R.id.textView1)).setText(String.format(result));
        	} 
        	catch (Exception e1) 
        	{
        		e1.printStackTrace();
        	}
        	
        	
        	/*無用
        	        	
        	//tag支援哪幾種格式
        	String[] temp = tagFromIntent.getTechList();
            
        	
        	for(String s :temp){
              ((TextView)findViewById(R.id.textView3)).append(String.format("s = "+s));
            }
        	*/
        	
        }// End of method
    }
    
    //抓取地圖&地圖控制&控制ListView
    public void catchMap(String idResult)
    {
    	
    	senseNfc.setVisibility(View.GONE);
    	
    	mapImage = (ImageView)findViewById(R.id.map);
    	
    	    	
    	//把NFC的ID字串換成小寫
		String idResultSmall = BigSmallConverter.Str2Char(idResult);
		String draw = "draw";
		//設定要讀取地圖的字串
		String target = draw + idResultSmall + targetGroup + targetChild;
		Log.d("target", String.valueOf(target));
		
		//取得要讀取地圖的記憶體位址並換成整數
		int c =  getResources().getIdentifier(target, "drawable", getPackageName());
		Log.d("a", String.valueOf(c));
		
		//設置地圖路徑 gif code
		is = getResources().openRawResource(c);
		
		
		//讀取地圖
		//mapImage.setImageResource(c);
		
		//讀取地圖gif code
		final GifFrame[] frames = CommonUtil.getGif(is);
        System.out.println("delay:" + frames[1].delay + ",size:" + frames.length);
        //iv = new ImageView(this);
        mGifTask = new PlayGifTask(iv, frames);
        mGifTask.start();
        //讀取地圖 gifcode end
		Log.d("b","setIR");
		
		//progess bar
		
		
		
		//String target2 ="R.drawable." + idResultSmall + targetGroup + targetChild;
		//bitmap = decodeSampledBitmapFromFile(target2, 100, 100);
		Log.d("a1","decodeSampledBitmapFromFile");
		bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(target, "drawable", getPackageName()));
		
		iv.setImageBitmap(bitmap);
		Log.d("a2","setImageBitmap");
    	new ImageViewHelper(dm,iv,bitmap,zoomInButton,zoomOutButton,currentDegree);
    	Log.d("a3","zoom");
		
    	loadImage = "true";
    	Log.d("loadImage","true");
      	//開啟羅盤功能
		compButton.setVisibility(View.VISIBLE);
		compButton.setOnClickListener(onClick);
		
		//取消進度bar
    	
    	
		//設定下一層按鈕
		//第一大門，門診科別
		if(result1.equals("EEC47BEC")&& targetGroup == 2 && targetChild==0)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("EEC47BEC")&& targetGroup == 2 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		//第二大門，門診科別
		if(result1.equals("EEA37BEC")&& targetGroup == 2 && targetChild==0)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("EEA37BEC")&& targetGroup == 2 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		//掛號批價，門診科別
		if(result1.equals("AE737BEC")&& targetGroup == 2 && targetChild==0)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("AE737BEC")&& targetGroup == 2 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		//檢查站，門診科別，B1~一樓
		if(result1.equals("7EFD37C6")&& targetGroup == 2 && targetChild==1)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("7EFD37C6")&& targetGroup == 2 && targetChild==3)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("7EFD37C6")&& targetGroup == 2 && targetChild==4)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		//檢查站，門診科別，一樓~B1
		if(result1.equals("4E827BEC")&& targetGroup == 2 && targetChild==0)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("5E9A78EC")&& targetGroup == 2 && targetChild==0)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("7E7839C6")&& targetGroup == 2 && targetChild==0)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("AE037DFC")&& targetGroup == 2 && targetChild==0)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		
		if(result1.equals("4E827BEC")&& targetGroup == 2 && targetChild==2)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("5E9A78EC")&& targetGroup == 2 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("7E7839C6")&& targetGroup == 2 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("AE037DFC")&& targetGroup == 2 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		//門診科別，檢查站 一樓~B1
		if(result1.equals("3EFB7BEC")&& targetGroup == 3 && targetChild==2)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("43F508C7")&& targetGroup == 3 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("4E5A7BEC")&& targetGroup == 3 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		//門診科別，檢查站，B1~一樓
		if(result1.equals("2E0E39C6")&& targetGroup == 3 && targetChild==0)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("2E0E39C6")&& targetGroup == 3 && targetChild==1)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("2E0E39C6")&& targetGroup == 3 && targetChild==3)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("2E0E39C6")&& targetGroup == 3 && targetChild==4)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		
		if(result1.equals("3E1C3AC6")&& targetGroup == 3 && targetChild==0)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("3E1C3AC6")&& targetGroup == 3 && targetChild==1)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("3E1C3AC6")&& targetGroup == 3 && targetChild==3)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("3E1C3AC6")&& targetGroup == 3 && targetChild==4)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		
		//檢查站，檢查站，B1~一樓
		if(result1.equals("7EFD37C6")&& targetGroup == 3 && targetChild==0)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("7EFD37C6")&& targetGroup == 3 && targetChild==1)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("7EFD37C6")&& targetGroup == 3 && targetChild==3)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("7EFD37C6")&& targetGroup == 3 && targetChild==4)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		//檢查站，檢查站，一樓~B1
		if(result1.equals("4E827BEC")&& targetGroup == 3 && targetChild==2)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("5E9A78EC")&& targetGroup == 3 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("7E7839C6")&& targetGroup == 3 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		else if (result1.equals("AE037DFC")&& targetGroup == 3 && targetChild==2)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
		
		//門診科別，批價
		if(result1.equals("2E0E39C6")&& targetGroup == 1 && targetChild==0)
    	{
    		nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
    		Log.d("001","nextFloorButtonOnClick");
    	}
		else if (result1.equals("3E1C3AC6")&& targetGroup == 1 && targetChild==0)
		{
			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);			
		}
    }
    
    /*
    public static Bitmap decodeSampledBitmapFromFile(String fileName, int reqWidth, int reqHeight) 
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }
    
    
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
      
        inSampleSize = (int) Math.min(Math.ceil((float)width/(float)reqWidth),  Math.ceil((float)height/(float)reqHeight));

        return inSampleSize;
    }
    */

   	 //传感器报告新的值(方向改变)
   	 public void onSensorChanged(SensorEvent event) 
   	 {
   		 if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) 
   		 {
   			 float degree = event.values[0];
   			 
   	   /*
   	   RotateAnimation类：旋转变化动画类
   	    
   	   参数说明:

   	   fromDegrees：旋转的开始角度。
   	   toDegrees：旋转的结束角度。
   	   pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
   	   pivotXValue：X坐标的伸缩值。
   	   pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
   	   pivotYValue：Y坐标的伸缩值
   	   */
   			 RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
   	         Animation.RELATIVE_TO_SELF, 0.5f,
   	         Animation.RELATIVE_TO_SELF, 0.5f);
   	   //旋转过程持续时间
   			 ra.setDuration(200);
   	   //罗盘图片使用旋转动画，針對不同地圖使用不同的羅盤動畫
   			
   			 //控制ListView和啟動旋轉動畫功能
   			 iv.startAnimation(ra);
 			
   			 currentDegree = -degree;
   			 currentDegree1 = -degree;
   		 }
   	 }
   	 
   	 //传感器精度的改变
   	 public void onAccuracyChanged(Sensor sensor, int accuracy)
   	 {

   	 }   	
    
   	 //羅盤按鈕
   	 private Button.OnClickListener onClick = new Button.OnClickListener()
   	 {
   		 public void onClick(View v)
   		 {
     	     // 传感器管理器
   			 sm = (SensorManager) getSystemService(SENSOR_SERVICE);
   			 // 注册传感器(Sensor.TYPE_ORIENTATION(方向传感器);SENSOR_DELAY_FASTEST(0毫秒延迟);
   			 // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))
   			 sm.registerListener(Navigation.this,
   			 sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
   			 SensorManager.SENSOR_DELAY_GAME);
   			 
   			 compButton.setVisibility(View.INVISIBLE);
   			 cancelCompButton.setVisibility(View.VISIBLE);
   			 
   			 cancelCompButton.setOnClickListener(concelButtonOnClick);
   			 mode = "on";
   		 }
   	 };
   	
   	//羅盤取消按鈕 
   	private Button.OnClickListener concelButtonOnClick = new Button.OnClickListener()
  	 {
  		 public void onClick(View v)
  		 {
    	     // 传感器管理器
  			 
  			 // 注册传感器(Sensor.TYPE_ORIENTATION(方向传感器);SENSOR_DELAY_FASTEST(0毫秒延迟);
  			 // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))
  			 sm.unregisterListener(Navigation.this);
  			 cancelCompButton.setVisibility(View.INVISIBLE);
  			 compButton.setVisibility(View.VISIBLE);
  			 compButton.setOnClickListener(onClick);
  			 mode = "off";
  		 }
  	 };
   	 
 	public void minZoom() {
        minScaleR = Math.min(
                (float) dm.widthPixels / (float) bitmap.getWidth(),
                (float) dm.heightPixels / (float) bitmap.getHeight());
        if (minScaleR <= 1.0) {
            matrix.postScale(minScaleR, minScaleR);
        }
        else{
        	matrix.postScale(1.5f, 1.5f);
        }
    }
   	
 	//下一層按鈕
   	private Button.OnClickListener nextFloorButtonOnClick = new Button.OnClickListener()
  	 {
  		 public void onClick(View v)
  		 {
  			mGifTask.stop();
  			((BitmapDrawable)iv.getDrawable()).getBitmap().recycle();
  			String idResultSmall = BigSmallConverter.Str2Char(result1);
  			String draw = "draw";
  			String floor = "b1";
  			//設定要讀取地圖的字串
  			String target = draw + idResultSmall + targetGroup + targetChild + floor;
  			//取得要讀取地圖的記憶體位址並換成整數
  			int c =  getResources().getIdentifier(target, "drawable", getPackageName());
  			Log.d("001", String.valueOf(c));
  			
  			//讀取地圖
  			is = getResources().openRawResource(c);
  			bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(target, "drawable", getPackageName()));
  			
  		//讀取地圖gif code
  			
  			final GifFrame[] frames = CommonUtil.getGif(is);
  	        System.out.println("delay:" + frames[1].delay + ",size:" + frames.length);
  	        //iv = new ImageView(this);
  	        mGifTask = new PlayGifTask(iv, frames);
  	        mGifTask.start();
  	        //讀取地圖 gifcode end
			
  			
  			iv.setImageBitmap(bitmap);
  			Log.d("a2","setImageBitmap");
  			//minZoom();
  			//mapImage.setImageMatrix(matrix);
  	    	Log.d("a3","zoom");
  			Log.d("002","setIR");
  			nextFloorButton.setVisibility(View.INVISIBLE);
  			firstFloorButton.setVisibility(View.VISIBLE);
    		firstFloorButton.setOnClickListener(firstFloorButtonOnClick);
  			
  			//startActivity(new Intent().setClass(Navigation.this, NavigationCrossFloorControl.class));
  			//Log.d("002","startActivity"); 
  		 }
  	 };
  	
  	 //上一層按鈕
  	private Button.OnClickListener firstFloorButtonOnClick = new Button.OnClickListener()
 	 {
 		 public void onClick(View v)
 		 {
 			mGifTask.stop();
 			((BitmapDrawable)iv.getDrawable()).getBitmap().recycle();
 			 String idResultSmall = BigSmallConverter.Str2Char(result1);
 			String draw = "draw";
 			 //設定要讀取地圖的字串
 			String target = draw + idResultSmall + targetGroup + targetChild;
 			//取得要讀取地圖的記憶體位址並換成整數
 			int c =  getResources().getIdentifier(target, "drawable", getPackageName());
 			Log.d("001", String.valueOf(c));
 			
 			//讀取地圖
 			is = getResources().openRawResource(c);
 			
 			//讀取地圖gif code start
 			
 			final GifFrame[] frames = CommonUtil.getGif(is);
 	        System.out.println("delay:" + frames[1].delay + ",size:" + frames.length);
 	        //iv = new ImageView(this);
 	        mGifTask = new PlayGifTask(iv, frames);
 	        mGifTask.start();
 	        //讀取地圖 gifcode end
			
 			bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(target, "drawable", getPackageName()));
 			
 			iv.setImageBitmap(bitmap);
 			Log.d("a2","setImageBitmap");
 			//minZoom();
 			//mapImage.setImageMatrix(matrix);
 			Log.d("a3","zoom");
 			Log.d("002","setIR");
 			firstFloorButton.setVisibility(View.INVISIBLE);
 			nextFloorButton.setVisibility(View.VISIBLE);
    		nextFloorButton.setOnClickListener(nextFloorButtonOnClick);
 			
 			//startActivity(new Intent().setClass(Navigation.this, NavigationCrossFloorControl.class));
 			//Log.d("002","startActivity"); 
 		 }
 	 };
 	 
 	 //GIF圖檔程式控制
     private static class PlayGifTask implements Runnable {
         int i = 0;
         ImageView iv;
         GifFrame[] frames;

         public PlayGifTask(ImageView iv, GifFrame[] frames) {
             this.iv = iv;
             this.frames = frames;
         }

         @Override
         public void run() {
             if (!frames[i].image.isRecycled()) {
                 iv.setImageBitmap(frames[i].image);
             }
             iv.postDelayed(this, frames[i++].delay);
             i %= frames.length;
         }
         
         public void start() {
             iv.post(this);
         }
         
         public void stop() {
             if(null != iv) iv.removeCallbacks(this);
             iv = null;
             if(null != frames) {
                 for(GifFrame frame : frames) {
                     if(frame.image != null && !frame.image.isRecycled()) {
                         frame.image.recycle();
                         frame.image = null;
                     }
                 }
                 frames = null;
             }
         }
     }
   	 
}
