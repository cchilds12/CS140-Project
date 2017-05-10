package project;

public class Job 
{
	private int id,startCodeIndex, codeSize, startMemoryIndex, currentPC, currentAcc;
	private States currentState;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStartCodeIndex() {
		return startCodeIndex;
	}
	public void setStartCodeIndex(int startcodeIndex) {
		this.startCodeIndex = startcodeIndex;
	}
	public int getCodeSize() {
		return codeSize;
	}
	public void setCodeSize(int codeSize) {
		this.codeSize = codeSize;
	}
	public int getStartMemoryIndex() {
		return startMemoryIndex;
	}
	public void setStartMemoryIndex(int startMemoryIndex) {
		this.startMemoryIndex = startMemoryIndex;
	}
	public int getCurrentPC() {
		return currentPC;
	}
	public void setCurrentPC(int currentPC) {
		this.currentPC = currentPC;
	}
	public int getCurrentAcc() {
		return currentAcc;
	}
	public void setCurrentAcc(int currentAcc) {
		this.currentAcc = currentAcc;
	}
	public States getCurrentState() {
		return currentState;
	}
	public void setCurrentState(States currentState) {
		this.currentState = currentState;
	}
	public void reset()
	{
		codeSize = 0;
		currentAcc = 0;
		currentPC =startCodeIndex;
		currentState = States.NOTHING_LOADED;
		
	}

}
