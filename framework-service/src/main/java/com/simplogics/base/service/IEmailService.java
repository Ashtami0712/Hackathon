package com.simplogics.base.service;

import com.simplogics.base.exception.FrameworkException;

import java.util.Map;

public interface IEmailService {

    void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) throws FrameworkException;

}
