package com.ou.ret.web.rest.errors;

public class PhoneNumberAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PhoneNumberAlreadyUsedException() {
        super(ErrorConstants.PHONE_NUMBER_ALREADY_USED_TYPE, "Phone number already used!", "userManagement", "userexists");
    }
}
