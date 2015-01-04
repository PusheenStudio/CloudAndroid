package com.example.softwarestudio_final;


public class PlayTimeCounter {
		
	int runTime=-1;
	GameView gv;
	int index10S,indexS;
	
	public PlayTimeCounter(GameView gv){
		this.gv = gv;
	
		TimeThread timeThread = new TimeThread();
		timeThread.start();
	}
	
	public int getRunTime(){
		return runTime;
	}
	
	private class TimeThread extends Thread
	{
		@Override
		public void run() {
			while(runTime<Constant.timeLimit)
			{
				if(!gv.pause)
				{
					runTime++;
				
					
					index10S=(Constant.timeLimit-runTime)%60/10;//練習模式下的10秒位
					indexS=(Constant.timeLimit-runTime)%60%10;//練習模式下的秒位
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(runTime>=Constant.timeLimit)
			{
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				gv.drawThreadAlive=false;//gameview 畫的線程停止
				gv.drawResault();
			}
		}
	}
	
}
