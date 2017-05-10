package project;

import javax.swing.Timer;

public class StepControl 
{
	private static final int TICK = 500;
	private boolean autoStepOn = false;
	private Timer timer; 
	private GUIMediator gui;
	
	public StepControl(GUIMediator g)
	{
		gui = g;
	}
	public boolean isAutoStepOn()
	{
		return autoStepOn;
	}
	public void setAutoStepOn(boolean a)
	{
		autoStepOn = a;		
	}
	void toggleAutoStep()
	{
		autoStepOn = !autoStepOn;
	}
	void setPeriod(int period)
	{
		timer.setDelay(period);
	}
	public void start() {
		timer = new Timer(TICK, e -> {if(autoStepOn) gui.step();});
		timer.start();
	}

}
