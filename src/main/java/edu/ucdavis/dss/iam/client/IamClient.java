package edu.ucdavis.dss.iam.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucdavis.dss.iam.dtos.IamAssociation;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamDepartment;

public class IamClient {
	private final Logger log = LoggerFactory.getLogger("IamLogger");

	private CloseableHttpClient httpclient;
	private HttpHost targetHost;
	private HttpClientContext context;
	private String apiKey;

	public IamClient(String apiKey) {
		httpclient = HttpClientBuilder.create().build();
		targetHost = new HttpHost("iet-ws.ucdavis.edu", 443, "https");

		// Add AuthCache to the execution context
		context = HttpClientContext.create();
		
		this.apiKey = apiKey;
	}

	/**
	 * Returns a list of all departments
	 * 
	 * @return list of departments as IamDepartment DTOs
	 */
	public List<IamDepartment> getAllDepartments() {
		HttpGet httpget = new HttpGet("/api/iam/orginfo/pps/depts?v=1.0&key=" + apiKey);
		List<IamDepartment> departments = null;

		try {
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				departments = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamDepartment.class));
			} else {
				log.error("getAllDepartments response from IAM not understood or was empty/null");
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
		}
		
		return departments;
	}
	
	/**
	 * Returns a list of all associations for the department indicated by 'deptCode'
	 * 
	 * @return list of associations as IamAssociation DTOs
	 */
	public List<IamAssociation> getAllAssociationsForDepartment(String deptCode) {
		long startTime;
		
		// First, get all people in the department
		String url = "/api/iam/associations/pps/search?deptCode=" + deptCode;
		HttpGet httpget = new HttpGet(url + "&v=1.0&key=" + apiKey);
		List<IamAssociation> associations = null;

		try {
			log.debug("HTTP GET: " + url);
			startTime = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);
			log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				associations = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamAssociation.class));
			} else {
				log.warn("/api/iam/associations/pps/search?deptCode=" + deptCode + " response from IAM not understood or was empty/null");
				
				return null;
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
			return null;
		}
		
		return associations;
		
//		// Perform four additional queries to obtain the rest of this person's information
//		for(IamPerson person : people) {
//			// Augment IamPerson with information from /people/search ...
//			try {
//				url = "/api/iam/people/search?iamId=" + person.getIamId();
//				log.debug("HTTP GET: " + url);
//				startTime = new Date().getTime();
//				httpget = new HttpGet(url + "&v=1.0&key=" + apiKey);
//				log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");
//				
//				CloseableHttpResponse response = httpclient.execute(
//						targetHost, httpget, context);
//
//				HttpEntity entity = response.getEntity();
//
//				ObjectMapper mapper = new ObjectMapper();
//				
//				JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
//				JsonNode arrNode = rootNode.findParent("responseData");
//				arrNode = rootNode.findPath("results");
//				arrNode = arrNode.get(0);
//				
//				if ((arrNode != null) && (arrNode.isNull() == false)) {
//					if(arrNode.get("mothraId") != null) { person.setMothraId(arrNode.get("mothraId").textValue()); }
//					if(arrNode.get("ppsId") != null) { person.setPpsId(arrNode.get("ppsId").textValue()); }
//					if(arrNode.get("studentId") != null) { person.setStudentId(arrNode.get("studentId").textValue()); }
//					if(arrNode.get("bannerPIdM") != null) { person.setBannerPIdM(arrNode.get("bannerPIdM").textValue()); }
//					if(arrNode.get("externalId") != null) { person.setExternalId(arrNode.get("externalId").textValue()); }
//					if(arrNode.get("oFirstName") != null) { person.setoFirstName(arrNode.get("oFirstName").textValue()); }
//					if(arrNode.get("oMiddleName") != null) { person.setoMiddleName(arrNode.get("oMiddleName").textValue()); }
//					if(arrNode.get("oLastName") != null) { person.setoLastName(arrNode.get("oLastName").textValue()); }
//					if(arrNode.get("oFullName") != null) { person.setoFullName(arrNode.get("oFullName").textValue()); }
//					if(arrNode.get("oSuffix") != null) { person.setoSuffix(arrNode.get("oSuffix").textValue()); }
//					if(arrNode.get("dFirstName") != null) { person.setdFirstName(arrNode.get("dFirstName").textValue()); }
//					if(arrNode.get("dMiddleName") != null) { person.setdMiddleName(arrNode.get("dMiddleName").textValue()); }
//					if(arrNode.get("dLastName") != null) { person.setdLastName(arrNode.get("dLastName").textValue()); }
//					if(arrNode.get("dSuffix") != null) { person.setdSuffix(arrNode.get("dSuffix").textValue()); }
//					if(arrNode.get("dFullName") != null) { person.setdFullName(arrNode.get("dFullName").textValue()); }
//					if(arrNode.get("isEmployee") != null) { person.setIsEmployee(arrNode.get("isEmployee").booleanValue()); }
//					if(arrNode.get("isHSEmployee") != null) { person.setIsHSEmployee(arrNode.get("isHSEmployee").booleanValue()); }
//					if(arrNode.get("isFaculty") != null) { person.setIsFaculty(arrNode.get("isFaculty").booleanValue()); }
//					if(arrNode.get("isStudent") != null) { person.setIsStudent(arrNode.get("isStudent").booleanValue()); }
//					if(arrNode.get("isStaff") != null) { person.setIsStaff(arrNode.get("isStaff").booleanValue()); }
//					if(arrNode.get("isExternal") != null) { person.setIsExternal(arrNode.get("isExternal").booleanValue()); }
//					if(arrNode.get("privacyCode") != null) { person.setPrivacyCode(arrNode.get("privacyCode").textValue()); }
//				} else {
//					log.warn("getAllPeopleByDepartmentCode /api/iam/people/search?iamId=" + person.getIamId() + " response from IAM not understood or was empty/null");
//					
//					continue;
//				}
//
//				response.close();
//			} catch (IOException e) {
//				log.error(exceptionStacktraceToString(e));
//				continue;
//			}
//			
//			// Augment IamPerson with information from /people/prikerbacct ...
//			try {
//				url = "/api/iam/people/prikerbacct/" + person.getIamId();
//				log.debug("HTTP GET: " + url);
//				startTime = new Date().getTime();
//				httpget = new HttpGet(url + "?key=" + apiKey + "&v=1.0");
//				log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");
//				
//				CloseableHttpResponse response = httpclient.execute(
//						targetHost, httpget, context);
//
//				HttpEntity entity = response.getEntity();
//
//				ObjectMapper mapper = new ObjectMapper();
//				
//				JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
//				JsonNode arrNode = rootNode.findParent("responseData");
//				arrNode = rootNode.findPath("results");
//				arrNode = arrNode.get(0);
//				
//				if ((arrNode != null) && (arrNode.isNull() == false)) {
//					//if(arrNode.get("userId") != null) { person.setLoginId(arrNode.get("userId").textValue()); }
//					
//				} else {
//					log.warn("getAllPeopleByDepartmentCode /api/iam/people/prikerbacct/" + person.getIamId() + " response from IAM not understood or was empty/null");
//					
//					continue;
//				}
//
//				response.close();
//			} catch (IOException e) {
//				log.error(exceptionStacktraceToString(e));
//				continue;
//			}
//			
//			// Augment IamPerson with information from /people/associations/pps ...
//			try {
//				url = "/api/iam/associations/pps/" + person.getIamId();
//				log.debug("HTTP GET: " + url);
//				startTime = new Date().getTime();
//				httpget = new HttpGet(url + "?key=" + apiKey + "&v=1.0");
//				log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");
//				
//				CloseableHttpResponse response = httpclient.execute(
//						targetHost, httpget, context);
//
//				HttpEntity entity = response.getEntity();
//
//				ObjectMapper mapper = new ObjectMapper();
//				
//				JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
//				JsonNode arrNode = rootNode.findParent("responseData");
//				arrNode = rootNode.findPath("results");
//				
//				if ((arrNode != null) && (arrNode.isNull() == false)) {
//					Set<IamAssociation> associations = null;
//					
//					associations = mapper.readValue(
//							arrNode.toString(),
//							mapper.getTypeFactory().constructCollectionType(
//									List.class, IamAssociation.class));
//					
//					// We'll set up a blank list just in case the client wants to call .size(), etc.
//					if(associations == null) associations = new HashSet<IamAssociation>();
//					
//					person.setAssociations(associations);
//					
//					for(IamAssociation association : associations) {
//						log.debug("Position type: " + association.getPositionType() + "(code: " + association.getPositionTypeCode() + ")");
//					}
//				} else {
//					log.warn("getAllPeopleByDepartmentCode /api/iam/associations/pps/" + person.getIamId() + " response from IAM not understood or was empty/null");
//					
//					continue;
//				}
//
//				response.close();
//			} catch (IOException e) {
//				log.error(exceptionStacktraceToString(e));
//				continue;
//			}
		//}
		
		//return people;
	}
	
	/**
	 * Returns the contact info entry for a given IamID
	 * 
	 * @return IamContactInfo or null
	 */
	public List<IamContactInfo> getContactInfo(Long iamId) {
		long startTime;
		
		// First, get all people in the department
		String url = "/api/iam/people/contactinfo/" + iamId;
		HttpGet httpget = new HttpGet(url + "?v=1.0&key=" + apiKey);
		List<IamContactInfo> contactInfos = null;

		try {
			log.debug("HTTP GET: " + url);
			startTime = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);
			log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				contactInfos = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamContactInfo.class));
			} else {
				log.warn("/api/iam/people/contactinfo/" + iamId + " response from IAM not understood or was empty/null");
				
				return null;
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
			return null;
		}
		
		return contactInfos;
	}
	
	// Credit: http://stackoverflow.com/questions/10120709/difference-between-printstacktrace-and-tostring
	public static String exceptionStacktraceToString(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}
}
