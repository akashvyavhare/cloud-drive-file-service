package com.clouddrive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {
	
	@Value("${cloud-drive.aws.region}")
	private String awsRegion;
	
	@Value("${cloud-drive.aws.profile}")
	private String awsProfile;

	@Bean
	public S3Client getS3Client() {
		return  S3Client.builder()
				.region(Region.of(awsRegion))
				.credentialsProvider(ProfileCredentialsProvider.create(awsProfile))
				.build();
	}
	
}
