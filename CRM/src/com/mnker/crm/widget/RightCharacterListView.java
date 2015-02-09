package com.mnker.crm.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class RightCharacterListView extends View {  
	 private char[] l;  
	    private SectionIndexer sectionIndexter = null;  
	    private ListView list;  
	    private TextView mDialogText;
	    private int m_nItemHeight = 40;  
	    public RightCharacterListView(Context context) {  
	        super(context);  
	        init();  
	    }  
	    public RightCharacterListView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	        init();  
	    }  
	    private void init() {  
	        l = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',  
	                'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };  
	    }  
	    
	    
	    public RightCharacterListView(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle); 
	        init();  
	    }  
	    public void setListView(ListView _list,int screenH,float density) {  
	        list = _list;  
	        sectionIndexter = (SectionIndexer) _list.getAdapter();  
//	        m_nItemHeight = (screenH-400)/26;
	        int offset=400;
	        if(density>=2){
	        offset=(int) (50*density);
	        }else {
	        offset=(int) (30*density);
			}
	        m_nItemHeight = (int) ((screenH-(56+71*density+offset))/26);
	    }  
	    public void setTextView(TextView mDialogText) {  
	    	this.mDialogText = mDialogText;  
	    }  
	    public boolean onTouchEvent(MotionEvent event) {  
	        super.onTouchEvent(event);  
	        try {
		        int i = (int) event.getY();  
		        int idx = i / m_nItemHeight;  
		        if (idx >= l.length) {  
		            idx = l.length - 1;  
		        } else if (idx < 0) {  
		            idx = 0;  
		        }  
		        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {  
		        	if(mDialogText!=null){
		        	mDialogText.setVisibility(View.VISIBLE);
		        	mDialogText.setText(""+l[idx]);
		        	}
		            if (sectionIndexter == null) {  
		                sectionIndexter = (SectionIndexer) list.getAdapter();  
		            }  
		          
		            int position = sectionIndexter.getPositionForSection(l[idx]);  
		            Log.d("zaokun", "onTouchEvent position==="+position);
		            if (position == -1) {  
		                return true;  
		            }  
		            
		            list.setSelection(position);  
		        }else{
		        	mDialogText.setVisibility(View.INVISIBLE);
		        }  
			} catch (Exception e) {
				// TODO: handle exception
			}

	        return true;  
	    }  
	    protected void onDraw(Canvas canvas) {  
	        Paint paint = new Paint();  
	        paint.setColor(0xff595c61);  
	        paint.setTextSize(20);  
	        paint.setTextAlign(Paint.Align.CENTER);  
	        float widthCenter = getMeasuredWidth() / 2;  
	        for (int i = 0; i < l.length; i++) {  
	            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight + (i * m_nItemHeight), paint);  
	        }  
	        super.onDraw(canvas);  
	    }  
}