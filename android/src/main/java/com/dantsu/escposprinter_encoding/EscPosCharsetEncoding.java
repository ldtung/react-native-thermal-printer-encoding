package com.dantsu.escposprinter_encoding;

public class EscPosCharsetEncoding {
    private String charsetName;
    private byte[] charsetCommand;

    private int windows1252 = 6;
    private int windows1258 = 52;
    private int tcvn1 = 30;
    private int tcvn2 = 31;

    /**
     * Create new instance of EscPosCharsetEncoding.
     *
     * @param charsetName Name of charset encoding (Ex: windows-1252)
     * @param escPosCharsetId Id of charset encoding for your printer (Ex: 16)
     */
    public EscPosCharsetEncoding(String charsetName, int escPosCharsetId) {
        this.charsetName = charsetName;
        this.charsetCommand = new byte[]{0x1B, 0x74, (byte) escPosCharsetId};
    }

    public byte[] getCommand() {
        return this.charsetCommand;
    }

  public String getName() {
    return this.charsetName;
  }

  public int getWindows1258() {
    return windows1258;
  }

  public void setWindows1258(int windows1258) {
    this.windows1258 = windows1258;
  }

  public int getTcvn1() {
    return tcvn1;
  }

  public void setTcvn1(int tcvn1) {
    this.tcvn1 = tcvn1;
  }

  public int getTcvn2() {
    return tcvn2;
  }

  public void setTcvn2(int tcvn2) {
    this.tcvn2 = tcvn2;
  }

  public int getWindows1252() {
    return windows1252;
  }

  public void setWindows1252(int windows1252) {
    this.windows1252 = windows1252;
  }
}
