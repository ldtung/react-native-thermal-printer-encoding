package com.dantsu.escposprinter_encoding.utils;

import java.io.UnsupportedEncodingException;

public class TextUtils {

  public TextUtils() {

  }

  public byte[] getBytesWithEncoding(String strText, String charset) throws UnsupportedEncodingException {
    if (strText == null) {
      return null;
    }
    if ("TCVN-3-1".equalsIgnoreCase(charset)) {
      return strText.getBytes(charset);
    }
    return strText.getBytes(charset);
  }
}
