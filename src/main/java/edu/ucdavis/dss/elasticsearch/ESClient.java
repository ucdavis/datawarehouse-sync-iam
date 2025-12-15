package edu.ucdavis.dss.elasticsearch;

import edu.ucdavis.dss.aws.RequestSigner;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import software.amazon.awssdk.http.HttpExecuteRequest;
import software.amazon.awssdk.http.HttpExecuteResponse;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.apache.ApacheHttpClient;

public class ESClient {
	private static final String SERVICE_NAME = "es";
	private static final String REGION = "us-west-2";
	private String host = null;
	private final SdkHttpClient httpClient;

	public ESClient(String host) {
		this.host = host;

		this.httpClient = ApacheHttpClient.builder()
			.socketTimeout(Duration.ofMinutes(5))
			.connectionTimeout(Duration.ofSeconds(5))
			.maxConnections(30)
			.build();
	}

	public void putDocument(String index, String id, String document) throws IOException {
		SdkHttpFullRequest httpFullRequest =
			SdkHttpFullRequest.builder().method(SdkHttpMethod.PUT).putHeader("Content-Type", "application/json")
				.protocol("https").host(this.host).encodedPath("/" + index + "/_doc/" + id)
				.contentStreamProvider(() -> new ByteArrayInputStream(document.getBytes())).build();

		SdkHttpFullRequest signedRequest = RequestSigner.sign(SERVICE_NAME, REGION, httpFullRequest);

		HttpExecuteRequest request = HttpExecuteRequest.builder().request(signedRequest)
			.contentStreamProvider(signedRequest.contentStreamProvider().orElse(null)).build();

		HttpExecuteResponse response = this.httpClient.prepareRequest(request).call();

		try {
			if (!response.httpResponse().isSuccessful()) {
				System.out.println(String.format("PUT document %s in index %s was unsuccessful: %s",
					id, index, response.httpResponse().statusText()));
			}
		} finally {
			if (response.responseBody().isPresent()) {
				response.responseBody().get().close();
			}
		}
	}

	public void deleteDocument(String index, String id) {
		SdkHttpFullRequest httpFullRequest =
			SdkHttpFullRequest.builder().method(SdkHttpMethod.DELETE).putHeader("Content-Type", "application/json")
				.protocol("https").host(this.host).encodedPath("/" + index + "/_doc/" + id).build();

		SdkHttpFullRequest signedRequest = RequestSigner.sign(SERVICE_NAME, REGION, httpFullRequest);

		SdkHttpClient httpClient = ApacheHttpClient.builder().build();
		HttpExecuteRequest request = HttpExecuteRequest.builder().request(signedRequest)
			.contentStreamProvider(signedRequest.contentStreamProvider().orElse(null)).build();

		try {
			HttpExecuteResponse response = httpClient.prepareRequest(request).call();

			if (!response.httpResponse().isSuccessful()) {
				System.out.println(String.format("DELETE document %s in index %s was unsuccessful", id, index));
			}
		} catch (IOException e) {
			System.out.println(
				"Exception thrown while trying to DELETE document in index (" + index + "), ID (" + id + "):");
			System.out.println(e);
		}
	}
}
