package com.example.navigationtest;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;


public class ImageViewHelper {
	private ImageView imageView;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Bitmap bitmap;

	private float minScaleR;// 最小縮放比例

	private static final int NONE = 0;// 初始狀態
	private static final int DRAG = 1;// 拖曳狀態
	private static final int DRAG1 = 3;
	private static final int ZOOM = 2;// 縮放狀態
	private int mode = NONE;

	private PointF prev = new PointF();
	private PointF mid = new PointF();
	private float dist = 1f;
	private DisplayMetrics dm;
	private ImageButton zoomInButton;
	private ImageButton zoomOutButton;
	
	//private float currentDegree;
	
	//private float radiansF = (float)radians;
	
	public ImageViewHelper(DisplayMetrics dm,ImageView imageView,Bitmap bitmap, ImageButton zoomInButton, ImageButton zoomOutButton, float currentDegree){
		this.dm = dm;
		this.imageView = imageView;
		this.zoomInButton = zoomInButton;
		this.zoomOutButton = zoomOutButton;
		this.bitmap = bitmap;
		//this.currentDegree = currentDegree;
		setImageSize();
		minZoom(); //將地圖大小初始化
//		center();
		imageView.setImageMatrix(matrix);
		
		
		
	}
	public Matrix getMatrix(){
		return matrix;
	}
	public void setZoomIn(){
		//傳回兩者較小的一個 螢幕寬/圖片寬,螢幕高/圖片高 任何一者小於1代表圖片超出螢幕
		minScaleR = Math.min(
                (float) dm.widthPixels / (float) bitmap.getWidth(),
                (float) dm.heightPixels / (float) bitmap.getHeight());
        if (minScaleR < 1.0) {
            //matrix.postScale(minScaleR+1f, minScaleR+1f);
        	matrix.postScale(1.2f, 1.2f);
        }
        else{
        	//matrix.postScale(minScaleR, minScaleR);
        	matrix.postScale(0.8f, 0.8f);
        }
	}
	public void setZoomOut(){
		minScaleR = Math.max(
                (float) dm.widthPixels / (float) bitmap.getWidth(),
                (float) dm.heightPixels / (float) bitmap.getHeight());
        if (minScaleR > 1.0) {
        	//matrix.postScale(minScaleR-1f, minScaleR-1f);
        	//matrix.postScale((minScaleR-(int)minScaleR),(minScaleR-(int)minScaleR));
        	matrix.postScale(0.8f, 0.8f);
        }
        else{
        	//matrix.postScale(minScaleR, minScaleR);
        	matrix.postScale(1.2f, 1.2f);
        }
	}

    //取得最小的比例, 假設圖片比螢幕大
    //則螢幕(寬/長)/圖片(寬/長)會小於1 那麼也就是將圖片進行縮小
    //反之 則進行放大 而圖片越小 放大倍數則會越大
    //如果螢幕跟圖片大小相同 則倍數會為1 即不變


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
    

/*
    public void center() {
        center(true, true);
    }

    //橫向、縱向置中
    public void center(boolean horizontal, boolean vertical) {

        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
        	// 圖片小於螢幕大小，則置中顯示。
        	//大於螢幕，上方則留空白則往上移，下方留空白則往下移
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imageView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }
*/

    //兩點的距離
    public float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    //兩點的中點
    public void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    //建立圖片移動縮放事件 
    public void setImageSize(){
		zoomInButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setZoomIn();
//				center();
				imageView.setImageMatrix(matrix);
			}
        	
        });
        zoomOutButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setZoomOut();
//				center();
				imageView.setImageMatrix(matrix);
			}
        	
        });
		
		imageView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				
				//單點觸摸動作
		        case MotionEvent.ACTION_DOWN:
		            savedMatrix.set(matrix);
		            prev.set(event.getX(), event.getY());
		            
		            if(Navigation.mode == "on")
		            mode = DRAG;
		            else if (Navigation.mode =="off")
		            mode = DRAG1;
		            
		            break;
		        
		        //多點觸摸動作
		        case MotionEvent.ACTION_POINTER_DOWN:
		            dist = spacing(event);
		            // 如果兩點距離超過10, 就判斷為多點觸控模式 即為縮放模式
		            if (spacing(event) > 10f) {
		                savedMatrix.set(matrix);
		                midPoint(mid, event);
		                mode = ZOOM;
		            }
		            break;
		        //單點觸摸離開動作
		        case MotionEvent.ACTION_UP:
		        //多點觸摸離開動作
		        case MotionEvent.ACTION_POINTER_UP:
		            mode = NONE;
		            break;
		        //觸摸點移動動作
		        case MotionEvent.ACTION_MOVE:
		            
		        	if (mode == DRAG) {
		                matrix.set(savedMatrix);
		                
		                double radians = Math.toRadians(-Navigation.currentDegree1);
		                double xRange = ( event.getX()*Math.cos(radians) - event.getY()*Math.sin(radians) )
		                		- ( prev.x * Math.cos(radians) - prev.y * Math.sin(radians) );
		                float xRangef = (float)xRange;
		                
		                //Log.d("xRangef","xRangefloat()");
		                
		                double yRange = ( event.getX()*Math.sin(radians) + event.getY()*Math.cos(radians) )
		                		- ( prev.x * Math.sin(radians) + prev.y * Math.cos(radians) );
		                
		                float yRangef = (float)yRange;
		                //Log.d("yRangef","yRangefloat()");
		                
		                             
		                matrix.postTranslate(xRangef, yRangef);
		            } 
		        	else if (mode == DRAG1)
		        	{
		        		matrix.set(savedMatrix);
		        		matrix.postTranslate(event.getX()-prev.x, event.getY()-prev.y);
		        	}
		        	
		        	else if (mode == ZOOM) {
		                float newDist = spacing(event);//偵測兩根手指移動的距離
		                if (newDist > 10f) {
		                    matrix.set(savedMatrix);
		                    float tScale = newDist / dist;
		                    matrix.postScale(tScale, tScale, mid.x, mid.y);
		                
		                }
		               
		            }
		            break;
		        }
		        imageView.setImageMatrix(matrix);
//		        center();
				return true;
			}
			
		});
	}
}
