package com.example.softwarestudio_final;

import static com.example.softwarestudio_final.Constant.MainMenuOffset;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import aurelienribon.tweenengine.*;
public class MainView extends SurfaceView implements Callback {

	MainActivity mainActivity;
	/*Bitmap startButton;
	Bitmap helpButton;
	Bitmap optionButton;
	Bitmap mainBack;
	Bitmap rope;*/
	Movable startButton;
	Movable helpButton;
	Movable optionButton;
	Movable rope;
	Movable mainBack;
	Movable title;
	TweenManager tm ;
	Thread inAnimation;
	MainDrawThread drawThread=new MainDrawThread();
	boolean isAnimationRunning=true;
	public MainView(MainActivity main){
		super(main);
		this.mainActivity=main;
		this.getHolder().addCallback(this);
		
		Tween.registerAccessor(Movable.class,new MovableAccessor());
		tm = new TweenManager();
	
		helpButton = new Movable ( BitmapFactory.decodeResource(getResources(), R.drawable.help_button),123f,-500f,255,0);
		startButton = new Movable ( BitmapFactory.decodeResource(getResources(), R.drawable.start_button),123f,-500f,255,0);
		mainBack = new Movable( BitmapFactory.decodeResource(getResources(), R.drawable.background3),0,0);
		optionButton = new Movable ( BitmapFactory.decodeResource(getResources(), R.drawable.option_button),123f,-500f,255,0);
		title = new Movable ( BitmapFactory.decodeResource(getResources(), R.drawable.title2),0,-500,255,1);
		
		Timeline.createSequence()
				.pushPause(1f)
				.push(Tween.to(title,MovableAccessor.POSITION_Y,0.4f).target(2000f))
				.pushPause(0.2f)
				.push(Tween.to(title,MovableAccessor.POSITION_Y,0.4f).target(-500f))
				.pushPause(0.3f)
				.push(Tween.to(title,MovableAccessor.POSITION_Y,0.8f).target(2000f))
				.push(Tween.to(title,MovableAccessor.POSITION_Y,0.8f).target(-500f))
				.push(Tween.to(title,MovableAccessor.POSITION_Y,1.0f).target(0f))	
				.beginParallel()
				.push(Tween.to(startButton,MovableAccessor.POSITION_Y,0.8f).target(500f))
				.push(Tween.to(startButton,MovableAccessor.SCALE,0.8f).target(1))
				.end()
				.beginParallel()
				.push(Tween.to(helpButton,MovableAccessor.POSITION_Y,0.8f).target(930f))
				.push(Tween.to(helpButton,MovableAccessor.SCALE,0.8f).target(1))
				.end()
				.beginParallel()
				.push(Tween.to(optionButton,MovableAccessor.POSITION_Y,0.8f).target(1358f))
				.push(Tween.to(optionButton,MovableAccessor.SCALE,0.8f).target(1))
				.end()
				.start(tm);
		drawThread= new MainDrawThread();
		drawThread.start();
		
		inAnimation = new Thread(new Runnable() {
		    private long lastMillis = -1;
		    
		    @Override
		    public void run() {
		        while (isAnimationRunning) {
		            if (lastMillis > 0) {
		                long currentMillis = System.currentTimeMillis();
		                final float delta = (currentMillis - lastMillis) / 1000f;

		               
		                mainActivity.runOnUiThread(new Runnable() {
		                    public void run() {
		                        tm.update(delta);
		                        if(optionButton.getY()==1358f)isAnimationRunning = false;
		                    }
		                });

		                lastMillis = currentMillis;
		            } else {
		                lastMillis = System.currentTimeMillis();
		            }

		            try {
		                Thread.sleep(25);
		            } catch(InterruptedException ex) {
		            }
		        }
		    }
		}); 
		inAnimation.start();
		
		
		
		
	}
	
	public void draw(Canvas canvas){

		Paint p = new Paint();
		canvas.save();
		canvas.translate(Constant.LCUX, Constant.LCUY);
		canvas.scale(Constant.RATIO, Constant.RATIO);
		
		mainBack.draw(canvas);
		startButton.draw(canvas);
		helpButton.draw(canvas);
		optionButton.draw(canvas);
		title.draw(canvas);
		/*canvas.drawBitmap(mainBack,0,0,p);
		canvas.drawBitmap(startButton,MainMenuOffset[0][0],MainMenuOffset[0][1],p);
		canvas.drawBitmap(helpButton,MainMenuOffset[1][0],MainMenuOffset[1][1],p);
		
		canvas.drawBitmap(optionButton,MainMenuOffset[2][0],MainMenuOffset[2][1],p);*/
		
		p.setARGB (200,255,0,0);
		p.setTextSize(60);
		canvas.drawText("ratio ="+Constant.RATIO+" LCUX = "+Constant.LCUX+" LCUY = "+ Constant.LCUY,0,0,p);
		canvas.drawText("SWidth="+Constant.SCREEN_WIDTH+"SHeight="+Constant.SCREEN_HEIGHT,0,100,p);
		canvas.drawText("Width="+Constant.WIDTH+"Height="+Constant.HEIGHT,0,200,p);
		canvas.restore();
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		int xx=(int)((e.getX()/Constant.RATIO)-Constant.LCUX);
	 	int yy=(int)((e.getY()/Constant.RATIO)-Constant.LCUY);
	 	Log.d("DEBUG","onMaintouch");
		if(!isAnimationRunning ){
	 	
	 	if(xx>=MainMenuOffset[0][0]&&xx<=MainMenuOffset[0][0]+startButton.bitmap.getWidth()
			&&yy>=MainMenuOffset[0][1]&&yy<=MainMenuOffset[0][1]+startButton.bitmap.getHeight()){
			if(Constant.soundOn)
	 		mainActivity.soundUtil.playEffectsSound(4, 0);
	 		Constant.soundTitle = false;
			mainActivity.myHandler.sendEmptyMessage(2);
	 		
		}
		else if(xx>=MainMenuOffset[1][0]&&xx<=MainMenuOffset[1][0]+helpButton.bitmap.getWidth()
				&&yy>=MainMenuOffset[1][1]&&yy<=MainMenuOffset[1][1]+helpButton.bitmap.getHeight()){
			if(Constant.soundOn)
			mainActivity.soundUtil.playEffectsSound(4, 0);
			mainActivity.myHandler.sendEmptyMessage(3);
		}
		else if(xx>=MainMenuOffset[2][0]&&xx<=MainMenuOffset[2][0]+optionButton.bitmap.getWidth()
				&&yy>=MainMenuOffset[2][1]&&yy<=MainMenuOffset[2][1]+optionButton.bitmap.getHeight()){
			if(Constant.soundOn)
			mainActivity.soundUtil.playEffectsSound(4, 0);
			mainActivity.myHandler.sendEmptyMessage(4);
		}
		 
		}
		return super.onTouchEvent(e);
	}
	
	
	public void repaint(){
		
		SurfaceHolder holder=this.getHolder();
		Canvas canvas = holder.lockCanvas();//���o�e��
		try{
			synchronized(holder){
				draw(canvas);//ø�s
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	private class MainDrawThread extends Thread {
	
		//�o��i��]�n�O�o����?????????? oAo
		@Override
		public void run(){
		
				while(isAnimationRunning)
					repaint();		
				try{
					Thread.sleep(Constant.fps);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					
				}
				
			
		//	gv.drawThreadAlive=true; //<---------------���T�w�n���n�d��
		}
	}
	
	
	
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(Constant.soundOn)
		{
		
				
				this.mainActivity.soundUtil.stop_bg_sound();//�����I������
				this.mainActivity.soundUtil.play_bg_sound();//�}�l�����I������
			
		
		}
		
		
		Canvas canvas=holder.lockCanvas();
		try
		{
			synchronized(holder)
			{
				draw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
