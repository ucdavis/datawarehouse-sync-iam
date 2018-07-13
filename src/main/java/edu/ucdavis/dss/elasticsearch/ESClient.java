package edu.ucdavis.dss.elasticsearch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;

import edu.ucdavis.dss.aws.RequestSigner;

public class ESClient {
	private static final String SERVICE_NAME = "es";
	private static final String REGION = "us-west-2";
	private String host = null;
	
	public ESClient(String host) {
		this.host = host;
	}

	public HttpResponse putDocument(String index, String type, String id, String document) {
		Request<?> request = new DefaultRequest<Void>(SERVICE_NAME);
		
		request.setContent(new ByteArrayInputStream(document.getBytes()));
		request.setEndpoint(URI.create("https://" + this.host + "/" + index + "/" + type + "/" + id));
		request.setHttpMethod(HttpMethodName.PUT);
		
		RequestSigner.performSigningSteps(SERVICE_NAME, REGION, request);
		
		ExecutionContext context = new ExecutionContext(true);

		ClientConfiguration clientConfiguration = new ClientConfiguration();
		AmazonHttpClient client = new AmazonHttpClient(clientConfiguration);

		MyHttpResponseHandler<Void> responseHandler = new MyHttpResponseHandler<Void>();
		MyErrorHandler errorHandler = new MyErrorHandler();

		try {
			@SuppressWarnings("deprecation")
			Response<Void> response =
					client.execute(request, responseHandler, errorHandler, context);
			
			return response.getHttpResponse();
		} catch (AmazonServiceException e) {
			System.out.println("AWS Exception thrown while trying to PUT document in index (" + index + "), type (" + type + "), ID (" + id + "):");
			System.err.println(e);
			
			return null;
		}
	}

	public HttpResponse deleteDocument(String index, String type, String id) {
		Request<?> request = new DefaultRequest<Void>(SERVICE_NAME);

		// request.setContent(new ByteArrayInputStream(document.getBytes()));
		request.setEndpoint(URI.create("https://" + this.host + "/" + index + "/" + type + "/" + id));
		request.setHttpMethod(HttpMethodName.DELETE);

		RequestSigner.performSigningSteps(SERVICE_NAME, REGION, request);

		ExecutionContext context = new ExecutionContext(true);

		ClientConfiguration clientConfiguration = new ClientConfiguration();
		AmazonHttpClient client = new AmazonHttpClient(clientConfiguration);

		MyHttpResponseHandler<Void> responseHandler = new MyHttpResponseHandler<Void>();
		MyErrorHandler errorHandler = new MyErrorHandler();

		try {
			@SuppressWarnings("deprecation")
			Response<Void> response =
					client.execute(request, responseHandler, errorHandler, context);

			return response.getHttpResponse();
		} catch (AmazonServiceException e) {
			System.out.println("AWS Exception thrown while trying to DELETE document in index (" + index + "), type (" + type + "), ID (" + id + "):");
			System.err.println(e);

			return null;
		}
	}

	public HttpResponse search(String index, String type, String q) {
		Request<?> request = new DefaultRequest<Void>(SERVICE_NAME);
		
		request.setEndpoint(URI.create("https://" + this.host + "/" + index + "/" + type + "/_search"));
		request.addParameter("q", q);
		request.setHttpMethod(HttpMethodName.GET);
		
		RequestSigner.performSigningSteps(SERVICE_NAME, REGION, request);
		
		ExecutionContext context = new ExecutionContext(true);

		ClientConfiguration clientConfiguration = new ClientConfiguration();
		AmazonHttpClient client = new AmazonHttpClient(clientConfiguration);

		MyHttpResponseHandler<Void> responseHandler = new MyHttpResponseHandler<Void>();
		MyErrorHandler errorHandler = new MyErrorHandler();

		try {
			@SuppressWarnings("deprecation")
			Response<Void> response =
					client.execute(request, responseHandler, errorHandler, context);
			
			return response.getHttpResponse();
		} catch (AmazonServiceException e) {
			System.out.println("AWS Exception thrown while trying to search in index (" + index + "), type (" + type + "), query (" + q + "):");
			System.err.println(e);
			
			return null;
		}
	}

	
	public static class MyHttpResponseHandler<T> implements HttpResponseHandler<AmazonWebServiceResponse<T>> {

		public AmazonWebServiceResponse<T> handle(
				com.amazonaws.http.HttpResponse response) throws Exception {

			//InputStream responseStream = response.getContent();
			//String responseString = convertStreamToString(responseStream);
			//System.out.println(responseString);

			AmazonWebServiceResponse<T> awsResponse = new AmazonWebServiceResponse<T>();
			return awsResponse;
		}

		public boolean needsConnectionLeftOpen() {
			return false;
		}
	}

	public static class MyErrorHandler implements HttpResponseHandler<AmazonServiceException> {

		public AmazonServiceException handle(
				com.amazonaws.http.HttpResponse response) throws Exception {
			AmazonServiceException ase = new AmazonServiceException("Elasticsearch");
			ase.setStatusCode(response.getStatusCode());
			ase.setErrorCode(response.getStatusText());
			//System.err.println(convertStreamToString(response.getContent()));

			return ase;
		}

		public boolean needsConnectionLeftOpen() {
			return false;
		}
	}

	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is);
		s.useDelimiter("\\A");
		
		String ret = s.hasNext() ? s.next() : "";

		s.close();
		
		return ret;
	}
}
