package com.ijoomer.caching;

import java.util.HashMap;

/**
 * This Class Contains UnNormalizeFields IjoomerCachingConstants.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings("serial")
public class IjoomerCachingConstants {

	public static HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
		{
            put("favouriteDishes","");
		}
	};
	public static HashMap<String, String> unRepetedFields = new HashMap<String, String>() {
        {
        }
    };
}
