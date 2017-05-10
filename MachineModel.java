package project;
import java.util.Map;
import java.util.TreeMap;
public class MachineModel 
{
	public final Map<Integer, Instruction> IMAP = new TreeMap();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallBack callback;
	private Code code = new Code();
	private Job[] jobs = new Job[4];
	private Job currentJob;
	
	public Job getCurrentJob()
	{
		return currentJob;
	}
	public void changeToJob(int i)
	{
		if(i>3||i<0)
		{
			throw new IllegalArgumentException("Illegal argument in job id");
		}
		if(i!=currentJob.getId())
		{
			currentJob.setCurrentAcc(cpu.getAccum());
			currentJob.setCurrentPC(cpu.getPCounter());
			currentJob = jobs[i];
			cpu.setAccum(currentJob.getCurrentAcc());
			cpu.setpCounter(currentJob.getCurrentPC());
			cpu.setMemBase(currentJob.getStartMemoryIndex());
		}
		
	}
	public int getChangedIndex()
	{
		return memory.getChangedIndex();
	}

	public MachineModel() {
		this(() -> System.exit(0));
	}
	public MachineModel(HaltCallBack b)
	{
		for(int i =0;i<jobs.length;i++)
		{
			jobs[i] = new Job();
			jobs[i].setId(i);
			jobs[i].setStartCodeIndex(i*Code.CODE_MAX/4);
			jobs[i].setStartMemoryIndex(i*Memory.DATA_SIZE/4);
		}
		currentJob = jobs[0];
		callback = b;
		//setMemBase?
		
		//INSTRUCTION MAP entry for "ADD"
		IMAP.put(0x3, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in ADD instruction");
			}
			if(level > 0) {
				IMAP.get(0x3).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() + arg);
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry for "SUB"
		IMAP.put(0x4, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in ADD instruction");
			}
			if(level > 0) {
				IMAP.get(0x4).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() - arg);
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry for "MUL"
		IMAP.put(0x5, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in MUL instruction");
			}
			if(level > 0) {
				IMAP.get(0x5).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() * arg);
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry for "DIV"
		IMAP.put(0x6, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in DIV instruction");
			}
			if(level > 0) {
				IMAP.get(0x6).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				if(arg==0)
				{
					throw new DivideByZeroException("Divide by Zero");
				}
				cpu.setAccum(cpu.getAccum() / arg);
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry 0 for "NOP"
		IMAP.put(0x0, (arg, level) -> {
			cpu.incrPC();					
		});
		//INSTRUCTION MAP entry 1 for "LOD"
		IMAP.put(0x1, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in LOD instruction");
			}
			if(level > 0) {
				IMAP.get(0x1).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				cpu.setAccum(arg);	
				cpu.incrPC();
			}

		});
		//INSTRUCTION MAP entry 2 for "STO"
		IMAP.put(0x2, (arg, level) -> {
			if(level < 1 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in STO instruction");
			}
			if(level>1)
			{
				IMAP.get(0x2).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				memory.setData(cpu.getMemBase()+arg, cpu.getAccum());
				cpu.incrPC();
			}

		});

		//INSTRUCTION MAP entry 7 for "AND"
		IMAP.put((0x7), (arg, level) -> {
			if(level<0||level>2)
			{
				throw new IllegalArgumentException(
						"Illegal indirection level in AND instruction");
			}
			if(level>0)
			{
				IMAP.get(0x7).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			}
			else
			{
				if(cpu.getAccum()!=0&&arg!=0)
				{
					cpu.setAccum(1);
				}
				else
				{
					cpu.setAccum(0);
				}
				cpu.incrPC();
			}

		});
		//INSTRUCTION MAP entry 8 for "NOT"
		IMAP.put((0x8), (arg, level) -> {
			if(level!=0)
			{
				throw new IllegalArgumentException(
						"Illegal indirection level in NOT instruction");
			}
			else
			{if(cpu.getAccum()!=0)
			{
				cpu.setAccum(0);
			}
			else
			{
				cpu.setAccum(1);
			}
			cpu.incrPC();
			}

		});
		//INSTRUCTION MAP entry 9 for "CMPL"
		IMAP.put(0x9, (arg, level) -> {
			if(level < 1 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in CMPL instruction");
			}
			if(level>1)
			{
				IMAP.get(0x9).execute(memory.getData(cpu.getMemBase()+arg), level-1);				
			}
			else 
			{												
				if(memory.getData(cpu.getMemBase()+arg)<0)
				{
					cpu.setAccum(1);
				}
				else 
				{
					cpu.setAccum(0);
				}
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry 10 for "CMPZ"
		IMAP.put(0xA, (arg, level) -> {
			if(level < 1 || level > 2) {
				throw new IllegalArgumentException(
						"Illegal indirection level in CMPL instruction");
			}
			if(level>1)
			{
				IMAP.get(0xA).execute(memory.getData(cpu.getMemBase()+arg), level-1);				
			}
			else 
			{												
				if(memory.getData(cpu.getMemBase()+arg)==0)
				{
					cpu.setAccum(1);
				}
				else 
				{
					cpu.setAccum(0);
				}
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry 11 for "JUMP"				
		IMAP.put(0xB, (arg, level) -> {
			if(level < 0 || level > 3) {
				throw new IllegalArgumentException(
						"Illegal indirection level in JUMP instruction");
			}
			if(level==3)
			{
				//cpu.setpCounter(arg + currentJob.getStartCodeIndex());
				int arg1 = memory.getData(cpu.getMemBase()+arg); // CORRECTION
				cpu.setpCounter(arg1 + currentJob.getStartCodeIndex());   // CORRECTION
			}
			else if(level >0)
			{
				IMAP.get(0xB).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			}
			else
			{
				cpu.setpCounter(cpu.getPCounter()+arg);
			}
		});
		//INSTRUCTION MAP entry 12 for "JMPZ"
		IMAP.put(0xC, (arg, level) -> {
			if(level < 0 || level > 3) {
				throw new IllegalArgumentException(
						"Illegal indirection level in JMPZ instruction");
			}
			if(cpu.getAccum()==0)
			{
				if(level==3)
				{
					//cpu.setpCounter(arg+currentJob.getStartCodeIndex());
					int arg1 = memory.getData(cpu.getMemBase()+arg); // CORRECTION
					cpu.setpCounter(arg1 + currentJob.getStartCodeIndex());   // CORRECTION
				}
				else if(level >0)
				{
					IMAP.get(0xC).execute(memory.getData(cpu.getMemBase()+arg), level-1);
				}
				else
				{
					cpu.setpCounter(cpu.getPCounter()+arg);
				}
			}
			else
			{
				cpu.incrPC();
			}

		});
		//INSTRUCTION MAP entry 15 for "HALT"
		IMAP.put(0xF, (arg, level) -> {
			callback.halt();			
		});
	}
	public States getCurrentState()
	{
		return currentJob.getCurrentState();
	}
	public void setCurrentState(States currentState)
	{
		currentJob.setCurrentState(currentState);
	}
	public CPU getCpu() {
		return cpu;
	}
	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}
	public Memory getMemory() {
		return memory;
	}
	public void setMemory(Memory memory) {
		this.memory = memory;
	}
	public void setData(int index, int value)
	{
		memory.setData(index, value);
	}
	public int getData(int index)
	{
		return memory.getData(index);
	}
	public int[] getData()
	{
		return memory.getData();
	}
	public void setAccum(int anAccum)
	{
		cpu.setAccum(anAccum);
	}
	public int getAccum()
	{
		return cpu.getAccum();
	}
	public int getpCounter()
	{
		return cpu.getPCounter();
	}
	public void setpCounter(int pCounter)
	{
		cpu.setpCounter(pCounter);
	}
	public void setMemBase(int aMemBase)
	{
		cpu.setMemBase(aMemBase);
	}
	public int getMemBase()
	{
		return cpu.getMemBase();
	}

	public Instruction get(Integer num)
	{
		return IMAP.get(num);
	}
	public void setCode(int index, int op, int indirLvl, int arg)
	{
		code.setCode(index, op, indirLvl, arg);
	}
	public Code getCode()
	{
		return code;
	}
	public void step()
	{
		try
		{
			
			int pc = cpu.getPCounter();
			if(pc<currentJob.getStartCodeIndex()||pc>=currentJob.getStartCodeIndex()+currentJob.getCodeSize())
			{
				throw new CodeAccessException("PC is out of bounds");
			}
			int opcode = code.getOp(pc);
			int indirLvl = code.getIndirLvl(pc);
			int arg = code.getArg(pc);
			get(opcode).execute(arg, indirLvl);
		}
		catch(Exception e)
		{
			callback.halt();
			throw e;    ///?????????
		}
		
	}
	public void clearJob()
	{
		//memory.clear(currentJob.getStartCodeIndex(), currentJob.getStartCodeIndex()+Memory.DATA_SIZE/4);
		//
		//System.out.println("Job ID: " + currentJob.getId() + " Start Code Index " + currentJob.getStartCodeIndex()
		//	+ " Code Size " + currentJob.getCodeSize() + " currentJob.getStartMemoryIndex " + currentJob.getStartMemoryIndex());
		memory.clear(currentJob.getStartMemoryIndex(), currentJob.getStartMemoryIndex()+Memory.DATA_SIZE/4);
		code.clear(currentJob.getStartCodeIndex(), currentJob.getCodeSize());		
		cpu.setAccum(0);
		cpu.setpCounter(currentJob.getStartCodeIndex());
		currentJob.reset();
	}
}



