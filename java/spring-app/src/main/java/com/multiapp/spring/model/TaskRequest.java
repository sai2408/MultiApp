package com.multiapp.spring.model;

/** JSON body for create/update. Fields are nullable so PUT can be partial. */
public record TaskRequest(String title, Boolean done) {
}
