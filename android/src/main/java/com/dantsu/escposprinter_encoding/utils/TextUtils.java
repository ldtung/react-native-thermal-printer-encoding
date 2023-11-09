package com.dantsu.escposprinter_encoding.utils;

import com.tinyhack.vncharset.TCVN3;
import com.tinyhack.vncharset.VNCharsetProvider;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class TextUtils {

  public TextUtils() {

  }

  public byte[] getBytesWithEncoding(String strText, String charset) throws UnsupportedEncodingException {
    if (strText == null) {
      return null;
    }
    if ("TCVN-3-1".equalsIgnoreCase(charset)) {
      Charset charsetObj = new VNCharsetProvider().charsetForName("TCVN-3-1");
      return strText.getBytes(charsetObj);
    } else if ("TCVN-3-2".equalsIgnoreCase(charset)) {
      Charset charsetObj = new VNCharsetProvider().charsetForName("TCVN-3-2");
      return strText.getBytes(charsetObj);
    }
    return strText.getBytes(charset);
  }
}
