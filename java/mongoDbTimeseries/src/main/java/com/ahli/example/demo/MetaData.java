package com.ahli.example.demo;

import org.springframework.data.mongodb.core.mapping.Field;

public record MetaData(
		@Field("deviceId") String deviceId,
		@Field("dataType") String dataType
) { }
