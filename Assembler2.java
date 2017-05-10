package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler2 
{
	public static void assemble(File input, File output, ArrayList<String> errors)
	{
		/*if(errors == null)
		{
			throw new IllegalArgument Exception
		}*/
		//System.out.println(input.exists());
		ArrayList<String> inText = new ArrayList<>();
		ArrayList<String> code = new ArrayList<>();
		ArrayList<String> data = new ArrayList<>();
		try
		{
			Scanner in = new Scanner(input);
			while(in.hasNextLine())
			{
				String str = in.nextLine();
				/*if(str.trim().length()>0)
				{*/
				inText.add(str);
				//}

			}
			in.close();
		}
		catch(FileNotFoundException e)
		{
			errors.add("FileNotFoundException");
			//System.out.println("File Not Found");
			return;
		}
		boolean separator = false;
		/*for(int i=inText.size();iinText.size(); i++)
		{
			String line = inText.get(i);
			int lineNum = i;

		}*/
		int j = inText.size()-1;
		while(j>=0)
		{
			String line = inText.get(j);
			if(line.trim().length()>0)
			{
				break;
			}
			inText.remove(j);
			j--;
		}
		int index = -1;
		ArrayList<String> outText = new ArrayList<>();
		boolean d = false;
		for(int i=0;i<inText.size();i++)
		{
			String line = inText.get(i);
			int lineNum = i;

			if(line.trim().toUpperCase().startsWith("--"))
			{
				for(int k=0;k<line.length();k++)
				{
					if(line.charAt(k)!='-')
					{
						errors.add("Error: line " + (lineNum +1) + "dashed line has non-dashes");
						break;
					}
				}
				index = i;
				//d = true;
				break;				
			}			

			if(line!=null&&(!(line.isEmpty())))
			{
				if(line.charAt(0)== ' ' || line.charAt(0)== '\t')
				{
					errors.add("Error: line " + (lineNum + 1)+ " starts with white space");				
				}
				if(line.trim().toUpperCase().startsWith("--")&&!separator)				
				{
					separator = true;
					if(line.trim().replace("-","").length()!=0)
					{
						errors.add("Error: line " + (lineNum + 1) + " has a badly formatted data separator");					
					}

				}
				else if(line.trim().toUpperCase().startsWith("--")&&separator)
				{
					errors.add("Error: line " + (lineNum + 1) + " has a duplicate data separator");
				}	
			}



			code.add(line);		
			//System.out.println(line);
			String[] parts = code.get(i).trim().split("\\s+");
			/*for(int j=0;j<parts.length;j++)
				{
					System.out.print(parts[j] + " ");
				}*/
			//System.out.println();
			//System.out.println(parts);
			int opcode = -1;

			if(line.trim().length() == 0)
			{
				if(i==0)
				{
					errors.add("Error: line " + (lineNum + 1)+ " is a blank line");
				}
				else if((i>0)&&(inText.get(i-1).trim().length() > 0))
				{
					errors.add("Error: line " + (lineNum + 1)+ " is a blank line");
				}					

			}

			else if(!InstructionMap.sourceCodes.contains(parts[0].toUpperCase()))
			{
				errors.add("Error: line " + (lineNum + 1)+ " illegal mnemonic");
			}
			else if(InstructionMap.sourceCodes.contains(parts[0].toUpperCase())&&(!(InstructionMap.sourceCodes.contains(parts[0]))))
			{
				errors.add("Error: line " + (lineNum + 1) + " does not have the instruction mnemonic in upper case");
				parts[0] = parts[0].toUpperCase();
				//opcode = InstructionMap.opcode.get(parts[0]);
			}
			else if(InstructionMap.noArgument.contains(parts[0])&&parts.length!=1) //what?
			{
				errors.add("Error: line " + (lineNum + 1)+" this mnemonic cannot take arguments");		
				opcode = InstructionMap.opcode.get(parts[0]);
			}
			else if(!(InstructionMap.noArgument.contains(parts[0])))
			{
				if(parts.length==1)
				{
					errors.add( "Error: line " + (lineNum + 1) + " is missing an argument");
				}
				else if(parts.length>=3)
				{
					errors.add("Error: line " + (lineNum + 1) + " has more than one argument");
				}
				opcode = InstructionMap.opcode.get(parts[0]);
			}
			else
			{
				opcode = InstructionMap.opcode.get(parts[0]);
			}
			if(parts.length==2)
			{
				int indirLvl = 1;
				if(parts[1].startsWith("["))
				{
					if(!(InstructionMap.indirectOK.contains(parts[0])))
					{
						errors.add("Error: line " + (lineNum + 1) + "is missing an argument"); //error message?							
					}
					else if(!parts[1].endsWith("]"))///problem?
					{
						errors.add("Error: line " + (lineNum + 1) + " is missing a closing \"]\""); //error message?
					}
					else
					{
						parts[1] = parts[1].substring(1, parts[1].length()-1);
						indirLvl=2;
					}
				}
				else 
				{
					if(parts[0].endsWith("I"))
					{
						indirLvl =0;
					}
					else if(parts[0].endsWith("A"))
					{
						indirLvl = 3;						
					}

					try {
						int arg = Integer.parseInt(parts[1],16);
					} catch (NumberFormatException e) {
						errors.add("Error: line " + (lineNum + 1)
								+ " argument is not a hex number");
					}
				}


				//System.out.println("here");
				outText.add(Integer.toHexString(opcode).toUpperCase() + " " + indirLvl + " " + parts[1]);
			}
			else if(parts.length==1)
			{
				outText.add(Integer.toHexString(opcode).toUpperCase() + " 0 0");
			}
		}						

		if(index!=-1)
		{
			for(int i=index+1;i<inText.size();i++)
			{
				String s =inText.get(i).trim();
				//System.out.println(s);
				data.add(s);


				//System.out.println(data.get(i).trim().split("\\s+"));
				String[] parts = data.get(i-(index+1)).trim().split("\\s+");
				int lineNum = i;
				if(s.trim().toUpperCase().startsWith("--"))
				{
					for(int k=0;k<s.length();k++)
					{
						if(s.charAt(k)!='-')
						{
							errors.add("Error: line " + (lineNum +1) + " dashed line has non-dashes");
							break;
						}
					}			
				}	
				if(s.trim().length() == 0)
				{
					if(i==0)
					{
						errors.add("Error: line " + (lineNum + 1)+ " is a blank line");
					}
					else if((i>0)&&(inText.get(i-1).trim().length() > 0))
					{
						errors.add("Error: line " + (lineNum + 1)+ " is a blank line");
					}					

				}
				else if(parts.length!=2)
				{
					errors.add("Error: line " + (lineNum + 1) + " data format does not consist of two numbers");
				}
				int arg = 0; 
				if(parts.length>=2)
				{
					try {
						arg = Integer.parseInt(parts[0],16);
					} catch (NumberFormatException e) {
						errors.add("Error: line " + (lineNum + 1)
								+ " data address is not a hex number");
					}
					try {
						arg = Integer.parseInt(parts[1],16);
					} catch (NumberFormatException e) {
						errors.add("Error: line " + (lineNum + 1) 
								+ " data value is not a hex number");
					}catch(ArrayIndexOutOfBoundsException e)
					{
						errors.add("Error: line " + (lineNum + 1)+ " is out of bounds");
					}catch(IllegalArgumentException e)
					{
						errors.add("Error: line " + (lineNum + 1)+ " Illegal Argument Exception");
					}
				}
			}
		}



		outText.add("-1");
		outText.addAll(data);
		if(!(errors.size() > 0))
		{
			try (PrintWriter out = new PrintWriter(output)){
				for(String s : outText) out.println(s);
			} catch (FileNotFoundException e) {
				errors.add("Cannot create output file");
			}
		}
	}
}