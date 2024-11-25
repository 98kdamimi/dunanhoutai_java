package com.junyang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {

	@Value("${aws-access-key-id}")
	private String accessKeyId;

	@Value("${aws-secret-access-key}")
	private String secretAccessKey;

	@Value("${aws-region}")
	private String region;

	@Bean
	public AmazonS3 amazonS3() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region)) // 设置 AWS 区域
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)) // 设置访问凭证
				.build();
	}

}
