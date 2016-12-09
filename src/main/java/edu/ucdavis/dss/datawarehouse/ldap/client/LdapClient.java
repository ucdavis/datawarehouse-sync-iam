package edu.ucdavis.dss.datawarehouse.ldap.client;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultTlsDirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;

public class LdapClient {
	private LdapTemplate ldapTemplate = null;
	private String serverUrl, serverBase, serverUser, serverPassword;
	
	public LdapClient(String serverUrl, String serverBase, String serverUser, String serverPassword) {
		this.serverUrl = serverUrl;
		this.serverBase = serverBase;
		this.serverUser = serverUser;
		this.serverPassword = serverPassword;
	}

	public List<String> fetchAllUcdPersonUUIDs() {
		String[] attrsToReturn = {"ucdPersonUUID"};
		
		bind();
		
		int i = 0;
		int inc = 1000;
		
		List<String> allUcdPersonUUIDs = new ArrayList<String>();
		
		for(i = 0; i < 99999999; i += inc) {
			allUcdPersonUUIDs.addAll(ldapTemplate.search("",
					"(&(ucdPersonUUID<=" + (i + inc) + ")(ucdPersonUUID>=" + i + "))",
					SearchControls.SUBTREE_SCOPE,
					attrsToReturn,
					new AttributeMapper()));
			
			if(i % 10000 == 0) {
				System.out.println("i = " + i);
			}
		}
		
		return allUcdPersonUUIDs;
	}

	private boolean bind() {
		if(ldapTemplate != null) { return true; }
		
		LdapContextSource ctxSrc = new LdapContextSource();
		
		ctxSrc.setUrl(serverUrl);
		ctxSrc.setBase(serverBase);
		ctxSrc.setUserDn(serverUser);
		ctxSrc.setPassword(serverPassword);
		
		ctxSrc.setAuthenticationStrategy(new DefaultTlsDirContextAuthenticationStrategy());
		
		ctxSrc.afterPropertiesSet();

		ldapTemplate = new LdapTemplate(ctxSrc);
		
		return true;
	}
	
	private class AttributeMapper implements AttributesMapper<String> {

		@Override
		public String mapFromAttributes(Attributes attributes) throws NamingException {
			Attribute personId = attributes.get("ucdPersonUUID");
			
			return (String)personId.get();
		}
		
	}
}
