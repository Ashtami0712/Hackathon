package com.simplogics.base.service;

import com.simplogics.base.exception.FrameworkException;

public interface IPasswordService {

    String validateAndGenerateEncodedPassword(String password) throws FrameworkException;

}
