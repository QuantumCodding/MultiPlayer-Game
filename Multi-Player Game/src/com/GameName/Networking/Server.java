package com.GameName.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.GameName.Networking.Packets.PacketConnectResponse;
import com.GameName.World.ServerWorld;

public class Server {
	public static final int DEFAULT_PORT = 4263;
	
	private ServerSocket server;
	private Socket connecter;
	
	private int port;
	private int maxUsers;
	private String ip;
	
	public NetworkPlayer[] users;
	private int numOnline;
	
	private boolean setUp;
	public boolean running;
	
	private List<ServerWorld> world;
	
	public Server() {
		reset();
	}
	
	public void reset() {
		server = null;
		connecter = null;
		
		maxUsers = 0;
		ip = null;
		port = 0;
		users = null;
		
		setUp = false;
	}
	
	public void setUpServer(int port, int maxUses, String ip) {
		reset();
		
		this.port = port;
		this.ip = ip;
		this.maxUsers = maxUses;
		
		users = new NetworkPlayer[maxUsers];
		
		setUp = true;
	}
	
	public void startServer() {
		if(!setUp) throw new RuntimeException("Server not set up!");
		
		try {
			Inet4Address IPv4 = (Inet4Address) Inet4Address.getByName(ip);
			server = new ServerSocket(port, maxUsers, IPv4);
			
			numOnline = 0;
			running = true;
			
			Thread serverThread = new Thread() {
				public void run() {
					while(running) {
						if(!(numOnline > maxUsers)) {
							try {
								connecter = server.accept();
								
								for(int i = 0; i < users.length; i ++) {
									if(users[i] == null) {
										
										users[i] = new NetworkPlayer(
												new DataInputStream(connecter.getInputStream()), 
												new DataOutputStream(connecter.getOutputStream()), 
												users, i
											);
										
										users[i].sendPacket(new PacketConnectResponse(i, maxUsers));
										
										Thread t = new Thread(users[i]);

										t.setName("NetworkPlayer Thread for IPv4 " + connecter.getInetAddress() + " on port " + connecter.getLocalPort());
										t.start();
										
										numOnline ++;
										break;
									}
								}
							} catch (IOException e) {
								System.err.println("User Failed to Connect to Server!");
							}
						}
					}
				}
			};
			
			serverThread.setName("Server Thread IPv4: " + server.getInetAddress());
			serverThread.start();
			
		} catch(IOException e) {
			System.err.println("Server Failed to start on port " + port + " on the IPv4 adress " + ip);
		}
	}
	
	public void stopServer() {
		running = false;
	}
	
	public ServerWorld getWorld(int index) {
		return world.get(index);
	}
}

