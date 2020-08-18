import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.plaf.basic.BasicTreeUI.TreeCancelEditingAction;

public class port_scannaer extends Thread {
	/**
	 * String that represent the open ports
	 */
	private  String open="\n";
	/**
	 * the maximum number of ports
	 */
	final int Port_number=65536;
	/**
	 * Distinguish between random 
	 * and in order running
	 */
	private boolean IsRandom=false;
	
	private String server;
	/**
	 * set the server 
	 * @param adress String the server address
	 */
	private void setServer(String address) {
		server=address;
	}
	/**
	 * 
	 * @return server String
	 */
	 public String getServer() {
		 return this.server;
	 }

	/**
	 * the of the thread 
	 * @param name String 
	 */
	port_scannaer(String name){
		super(name);
	}

	/**
	 * gets the name of the port and the host name  and checks if it's listening 
	 * to  specific  ports \n
	 * works only if the other computer works as a server
	 * @param port integer 
	 * @param server String representing the server
	 */
	private void IsOpen(String server, int port) {
		try {
			Socket s=	new Socket(getServer(), port);
			s.close();
			open+=""+port+"  is open\n";
			System.out.println("\n"+port+ " is open\n");	
		} catch (UnknownHostException e) {
			System.out.println("not open");
		} catch (IOException e) {
			if (port%10==0)
				System.out.print( "\n  "+port+ " is close");
			else
				System.out.print( "  "+port+ " is close");
		}	 
	}
	/**
	 * runs the thread
	 * if <b>IsRandom ==false</b> then it runs in order 
	 * if <b>IsRandom==true</b> then it runs randomly 
	 */
	@Override
	public void run() {
		open="\n";
		if (IsRandom==false) {
			for (int i=130;i<Port_number;i++) {
				IsOpen(server,i);
				try {
					if (i%10==0)
						sleep(1000);
				}
				catch (Exception e) {

				}
			}
		}
		else {
			int[] arr= new int [Port_number+1];
			int RunTime=Port_number+1;
			while(RunTime!=0) {
				int i=(int) Math.floor(Math.random() * (Port_number));
				if (arr[i]==0) {
					arr[i]=1;
					IsOpen(server,i);
					RunTime--;
				}
			}

		}

	}

	/**
	 * changes <b>IsRandom</b> to true 
	 * and runs the thread  
	 * when it finishes it returns <b>IsRandom</b> to false
	 */
	public void random(){
		IsRandom=true;
		run();
		IsRandom=false;

	}
	
	
	
	
	
	/**
	 * <b>Gets the command from CMD in windows and send what it received from the shell</b>
	 * works only if you have nmap
	 * only for checking if the file was saved.
	 * thanks to "https://www.mkyong.com/java/how-to-execute-shell-command-from-java/" 
	 * @param port integer
	 */

	public void executeCommand(int port) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec("nmap  "+ server + "-p "+ port);
			//			p.waitFor();
			BufferedReader reader = 
					new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			System.out.println("the nmap doesn't works");
		}

		//		System.out.println(output.toString());
		open= output.toString();

	}
	
	/**
	 * save the String <b>open</b> on a specific address
	 * @param output String where to save the file
	 */
	
    public void Save_file(String output) {
		try {
			FileWriter fw = new FileWriter(output);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(open);
			bw.close();
			System.out.println("the file was saved");
		} catch (IOException e) {
			System.out.println("invalid output string");
		}
		
    }







	public static void main(String[] args) {
		port_scannaer scan= new port_scannaer("scanner1");
		// Runs on local host but it should work on any server ip 
		scan.setServer("127.0.0.1");
		System.out.println(scan.server);
				scan.start();
		

	}

}
