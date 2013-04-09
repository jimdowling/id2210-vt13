package common.configuration;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import se.sics.kompics.address.Address;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;


public class Configuration {
	public static int SNAPSHOT_PERIOD = 5000; // ms
	public static int AVAILABLE_TOPICS = 20; 
	
	public InetAddress ip = null;
	
	{
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {			
		}
	}
	
	int webPort = 8080;
	int bootId = Integer.MAX_VALUE;
	int networkPort = 8081;
	Address bootServerAddress = new Address(ip, networkPort, bootId);

	BootstrapConfiguration bootConfiguration = new BootstrapConfiguration(bootServerAddress, 60000, 4000, 3, 30000, webPort, webPort);
	CyclonConfiguration cyclonConfiguration = new CyclonConfiguration(5, 10, 1000, 500000, new BigInteger("2").pow(13), 20);
	TManConfiguration tmanConfiguration = new TManConfiguration(1000);
	SearchConfiguration aggregationConfiguration = new SearchConfiguration(1000);
        
	public void set() throws IOException {
		String c = File.createTempFile("bootstrap.", ".conf").getAbsolutePath();
		bootConfiguration.store(c);
		System.setProperty("bootstrap.configuration", c);

		c = File.createTempFile("cyclon.", ".conf").getAbsolutePath();
		cyclonConfiguration.store(c);
		System.setProperty("cyclon.configuration", c);

		c = File.createTempFile("tman.", ".conf").getAbsolutePath();
		tmanConfiguration.store(c);
		System.setProperty("tman.configuration", c);

		c = File.createTempFile("search.", ".conf").getAbsolutePath();
		aggregationConfiguration.store(c);
		System.setProperty("search.configuration", c);
	}
}
