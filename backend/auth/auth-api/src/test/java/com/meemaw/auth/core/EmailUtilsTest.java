package com.meemaw.auth.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EmailUtilsTest {

  @Test
  public void email_utils__should_correctly_check_if_business_domain() {
    assertFalse(EmailUtils.isBusinessDomain("hotmail.com"));
    assertFalse(EmailUtils.isBusinessDomain("gmail.com"));
    assertFalse(EmailUtils.isBusinessDomain("yahoo.com"));

    assertTrue(EmailUtils.isBusinessDomain("snuderls.eu"));
    assertTrue(EmailUtils.isBusinessDomain("insight.io"));
    assertTrue(EmailUtils.isBusinessDomain("cognite.com"));
    assertTrue(EmailUtils.isBusinessDomain("revolut.com"));
    assertTrue(EmailUtils.isBusinessDomain("bitstamp.net"));
  }
}
