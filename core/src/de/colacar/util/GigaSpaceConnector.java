package de.colacar.util;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

public class GigaSpaceConnector {

	private static GigaSpace gigaSpace;
	
	public static GigaSpace getGigaSpace(){
        if (gigaSpace == null){
            UrlSpaceConfigurer configurer = new UrlSpaceConfigurer("jini://*/*/ColaCar");
            gigaSpace = new GigaSpaceConfigurer(configurer).create();
        }
        return gigaSpace;
    }
}
