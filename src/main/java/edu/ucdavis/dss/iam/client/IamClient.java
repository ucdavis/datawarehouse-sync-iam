package edu.ucdavis.dss.iam.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import edu.ucdavis.dss.iam.dtos.IamDepartment;
import edu.ucdavis.dss.iam.dtos.IamPerson;

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
	 * Returns a list of all people in a department
	 * 
	 * @return list of people as IamPerson DTOs
	 */
	public List<IamPerson> getAllPeopleByDepartmentCode(String deptCode) {
		long startTime;
		
		// First, get all people in the department
		String url = "/api/iam/associations/pps/search?deptCode=" + deptCode;
		HttpGet httpget = new HttpGet(url + "&v=1.0&key=" + apiKey);
		List<IamPerson> people = null;

		try {
			log.debug("HTTP GET: " + url);
			startTime = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);
			log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "s.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				people = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamPerson.class));
			} else {
				log.warn("getAllPeopleByDepartmentCode /api/iam/associations/pps/search?deptCode=" + deptCode + " response from IAM not understood or was empty/null");
				
				return null;
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
			return null;
		}
		
		// No error but IAM did not return any people
		if(people == null) return null;
		
		// Perform two additional queries to obtain the rest of this person's information
		for(IamPerson person : people) {
			// Augment IamPerson with information from /people/contactinfo/ ...
			try {
				url = "/api/iam/people/contactinfo/" + person.getIamId();
				log.debug("HTTP GET: " + url);
				startTime = new Date().getTime();
				httpget = new HttpGet(url + "?v=1.0&key=" + apiKey);
				log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "s.");
				
				CloseableHttpResponse response = httpclient.execute(
						targetHost, httpget, context);

				HttpEntity entity = response.getEntity();

				ObjectMapper mapper = new ObjectMapper();
				
				JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
				JsonNode arrNode = rootNode.findParent("responseData");
				arrNode = rootNode.findPath("results");
				arrNode = arrNode.get(0);
				
				if ((arrNode != null) && (arrNode.isNull() == false)) {
//					if(arrNode.get("email") != null) { person.setEmail(arrNode.get("email").textValue()); }
//					if(arrNode.get("hsEmail") != null) { person.setHsEmail(arrNode.get("hsEmail").textValue()); }
//					if(arrNode.get("campusEmail") != null) { person.setCampusEmail(arrNode.get("campusEmail").textValue()); }
//					if(arrNode.get("addrStreet") != null) { person.setAddrStreet(arrNode.get("addrStreet").textValue()); }
//					if(arrNode.get("addrCity") != null) { person.setAddrCity(arrNode.get("addrCity").textValue()); }
//					if(arrNode.get("addrState") != null) { person.setAddrState(arrNode.get("addrState").textValue()); }
//					if(arrNode.get("addrZip") != null) { person.setAddrZip(arrNode.get("addrZip").textValue()); }
//					if(arrNode.get("postalAddress") != null) { person.setPostalAddress(arrNode.get("postalAddress").textValue()); }
//					if(arrNode.get("workPhone") != null) { person.setWorkPhone(arrNode.get("workPhone").textValue()); }
//					if(arrNode.get("workCell") != null) { person.setWorkCell(arrNode.get("workCell").textValue()); }
//					if(arrNode.get("workPager") != null) { person.setWorkPager(arrNode.get("workPager").textValue()); }
//					if(arrNode.get("workFax") != null) { person.setWorkFax(arrNode.get("workFax").textValue()); }
				} else {
					log.warn("getAllPeopleByDepartmentCode /api/iam/people/contactinfo/" + person.getIamId() + " response from IAM not understood or was empty/null");
					
					continue;
				}

				response.close();
			} catch (IOException e) {
				log.error(exceptionStacktraceToString(e));
				continue;
			}

			// Augment IamPerson with information from /people/search ...
			try {
				url = "/api/iam/people/search?iamId=" + person.getIamId();
				log.debug("HTTP GET: " + url);
				startTime = new Date().getTime();
				httpget = new HttpGet(url + "&v=1.0&key=" + apiKey);
				log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "s.");
				
				CloseableHttpResponse response = httpclient.execute(
						targetHost, httpget, context);

				HttpEntity entity = response.getEntity();

				ObjectMapper mapper = new ObjectMapper();
				
				JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
				JsonNode arrNode = rootNode.findParent("responseData");
				arrNode = rootNode.findPath("results");
				arrNode = arrNode.get(0);
				
				if ((arrNode != null) && (arrNode.isNull() == false)) {
					if(arrNode.get("mothraId") != null) { person.setMothraId(arrNode.get("mothraId").textValue()); }
					if(arrNode.get("ppsId") != null) { person.setPpsId(arrNode.get("ppsId").textValue()); }
					if(arrNode.get("studentId") != null) { person.setStudentId(arrNode.get("studentId").textValue()); }
					if(arrNode.get("bannerPIdM") != null) { person.setBannerPIdM(arrNode.get("bannerPIdM").textValue()); }
					if(arrNode.get("externalId") != null) { person.setExternalId(arrNode.get("externalId").textValue()); }
					if(arrNode.get("oFirstName") != null) { person.setoFirstName(arrNode.get("oFirstName").textValue()); }
					if(arrNode.get("oMiddleName") != null) { person.setoMiddleName(arrNode.get("oMiddleName").textValue()); }
					if(arrNode.get("oLastName") != null) { person.setoLastName(arrNode.get("oLastName").textValue()); }
					if(arrNode.get("oFullName") != null) { person.setoFullName(arrNode.get("oFullName").textValue()); }
					if(arrNode.get("oSuffix") != null) { person.setoSuffix(arrNode.get("oSuffix").textValue()); }
					if(arrNode.get("dFirstName") != null) { person.setdFirstName(arrNode.get("dFirstName").textValue()); }
					if(arrNode.get("dMiddleName") != null) { person.setdMiddleName(arrNode.get("dMiddleName").textValue()); }
					if(arrNode.get("dLastName") != null) { person.setdLastName(arrNode.get("dLastName").textValue()); }
					if(arrNode.get("dSuffix") != null) { person.setdSuffix(arrNode.get("dSuffix").textValue()); }
					if(arrNode.get("dFullName") != null) { person.setdFullName(arrNode.get("dFullName").textValue()); }
					if(arrNode.get("isEmployee") != null) { person.setIsEmployee(arrNode.get("isEmployee").booleanValue()); }
					if(arrNode.get("isHSEmployee") != null) { person.setIsHSEmployee(arrNode.get("isHSEmployee").booleanValue()); }
					if(arrNode.get("isFaculty") != null) { person.setIsFaculty(arrNode.get("isFaculty").booleanValue()); }
					if(arrNode.get("isStudent") != null) { person.setIsStudent(arrNode.get("isStudent").booleanValue()); }
					if(arrNode.get("isStaff") != null) { person.setIsStaff(arrNode.get("isStaff").booleanValue()); }
					if(arrNode.get("isExternal") != null) { person.setIsExternal(arrNode.get("isExternal").booleanValue()); }
					if(arrNode.get("privacyCode") != null) { person.setPrivacyCode(arrNode.get("privacyCode").textValue()); }
				} else {
					log.warn("getAllPeopleByDepartmentCode /api/iam/people/search?iamId=" + person.getIamId() + " response from IAM not understood or was empty/null");
					
					continue;
				}

				response.close();
			} catch (IOException e) {
				log.error(exceptionStacktraceToString(e));
				continue;
			}
			
			// Augment IamPerson with information from /people/prikerbacct ...
			try {
				url = "/api/iam/people/prikerbacct/" + person.getIamId();
				log.debug("HTTP GET: " + url);
				startTime = new Date().getTime();
				httpget = new HttpGet(url + "?key=" + apiKey + "&v=1.0");
				log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "s.");
				
				CloseableHttpResponse response = httpclient.execute(
						targetHost, httpget, context);

				HttpEntity entity = response.getEntity();

				ObjectMapper mapper = new ObjectMapper();
				
				JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
				JsonNode arrNode = rootNode.findParent("responseData");
				arrNode = rootNode.findPath("results");
				arrNode = arrNode.get(0);
				
				if ((arrNode != null) && (arrNode.isNull() == false)) {
					//if(arrNode.get("userId") != null) { person.setLoginId(arrNode.get("userId").textValue()); }
					
				} else {
					log.warn("getAllPeopleByDepartmentCode /api/iam/people/prikerbacct/" + person.getIamId() + " response from IAM not understood or was empty/null");
					
					continue;
				}

				response.close();
			} catch (IOException e) {
				log.error(exceptionStacktraceToString(e));
				continue;
			}
			
			// Augment IamPerson with information from /people/associations/pps ...
			try {
				url = "/api/iam/associations/pps/" + person.getIamId();
				log.debug("HTTP GET: " + url);
				startTime = new Date().getTime();
				httpget = new HttpGet(url + "?key=" + apiKey + "&v=1.0");
				log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "s.");
				
				CloseableHttpResponse response = httpclient.execute(
						targetHost, httpget, context);

				HttpEntity entity = response.getEntity();

				ObjectMapper mapper = new ObjectMapper();
				
				JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
				JsonNode arrNode = rootNode.findParent("responseData");
				arrNode = rootNode.findPath("results");
				
				if ((arrNode != null) && (arrNode.isNull() == false)) {
					Set<IamAssociation> associations = null;
					
					associations = mapper.readValue(
							arrNode.toString(),
							mapper.getTypeFactory().constructCollectionType(
									List.class, IamAssociation.class));
					
					// We'll set up a blank list just in case the client wants to call .size(), etc.
					if(associations == null) associations = new HashSet<IamAssociation>();
					
					person.setAssociations(associations);
					
					for(IamAssociation association : associations) {
						log.debug("Position type: " + association.getPositionType() + "(code: " + association.getPositionTypeCode() + ")");
					}
				} else {
					log.warn("getAllPeopleByDepartmentCode /api/iam/associations/pps/" + person.getIamId() + " response from IAM not understood or was empty/null");
					
					continue;
				}

				response.close();
			} catch (IOException e) {
				log.error(exceptionStacktraceToString(e));
				continue;
			}
		}
		
		return people;
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
