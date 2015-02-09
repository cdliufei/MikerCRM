package com.jeremyfeinstein.slidingmenu.lib.anim;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import android.graphics.Canvas;
import android.view.animation.Interpolator;

public class CustomAnimation {

	public CanvasTransformer getCustomScaleAnimation() {
		return new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);
			}			
		};
	}
	
	Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}		
	};
	
	public CanvasTransformer getCustomSlideAnimation() {
		return new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
			}			
		};
	}
	
	public CanvasTransformer getCustomZoomAnimation() {
		return new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen*0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
			}
		};
	}
}
