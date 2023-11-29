package com.dantsu.escposprinter_encoding.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.dantsu.escposprinter_encoding.connection.DeviceConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class PrinterHelper {

  private DeviceConnection printerConnection;
  public PrinterHelper(DeviceConnection printerConnection) {
    this.printerConnection = printerConnection;
  }
  public boolean printImage(Bitmap bitmap) {
    byte[] command = decodeBitmap(bitmap);
    return printUnicode(command);
  }

  public boolean printUnicode(byte[] data) {
    try {
      this.printerConnection.write(data);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }


  public boolean printMultiLangText(String stringData, Paint.Align align, float textSize)  {
    return printMultiLangText(stringData, align, textSize, null);
  }

  public boolean printMultiLangText(String stringData, Paint.Align align, float textSize, Typeface typeface)  {
    return printImage(getMultiLangTextAsImage(stringData, align, textSize, typeface));
  }

  public static byte[] decodeBitmap(Bitmap bmp) {
    int bmpWidth = bmp.getWidth();
    int bmpHeight = bmp.getHeight();

    List<String> list = new ArrayList<>();
    StringBuffer sb;
    int zeroCount = bmpWidth % 8;
    String zeroStr = "";
    if (zeroCount > 0) {
      for (int i = 0; i < (8 - zeroCount); i++) zeroStr = zeroStr + "0";
    }

    for (int i = 0; i < bmpHeight; i++) {
      sb = new StringBuffer();
      for (int j = 0; j < bmpWidth; j++) {
        int color = bmp.getPixel(j, i);
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;
        if (r > 160 && g > 160 && b > 160) sb.append("0");
        else sb.append("1");
      }
      if (zeroCount > 0) sb.append(zeroStr);
      list.add(sb.toString());
    }

    List<String> bmpHexList = binaryListToHexStringList(list);
    String commandHexString = "1D763000";
    String widthHexString = Integer
      .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8 : (bmpWidth / 8 + 1));
    if (widthHexString.length() > 2) {
      return null;
    } else if (widthHexString.length() == 1) {
      widthHexString = "0" + widthHexString;
    }
    widthHexString = widthHexString + "00";

    String heightHexString = Integer.toHexString(bmpHeight);
    if (heightHexString.length() > 2) {
      return null;
    } else if (heightHexString.length() == 1) {
      heightHexString = "0" + heightHexString;
    }
    heightHexString = heightHexString + "00";

    List<String> commandList = new ArrayList<>();
    commandList.add(commandHexString + widthHexString + heightHexString);
    commandList.addAll(bmpHexList);

    return hexList2Byte(commandList);
  }

  private static List<String> binaryListToHexStringList(List<String> list) {
    List<String> hexList = new ArrayList<>();
    for (String binaryStr : list) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < binaryStr.length(); i += 8) {
        String str = binaryStr.substring(i, i + 8);
        String hexString = strToHexString(str);
        sb.append(hexString);
      }
      hexList.add(sb.toString());
    }
    return hexList;
  }

  private static String hexStr = "0123456789ABCDEF";
  private static String[] binaryArray = {"0000", "0001", "0010", "0011",
    "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
    "1100", "1101", "1110", "1111"};

  private static String strToHexString(String binaryStr) {
    String hex = "";
    String f4 = binaryStr.substring(0, 4);
    String b4 = binaryStr.substring(4, 8);
    for (int i = 0; i < binaryArray.length; i++) {
      if (f4.equals(binaryArray[i]))
        hex += hexStr.substring(i, i + 1);
    }
    for (int i = 0; i < binaryArray.length; i++) {
      if (b4.equals(binaryArray[i]))
        hex += hexStr.substring(i, i + 1);
    }

    return hex;
  }

  private static byte[] hexList2Byte(List<String> list) {
    List<byte[]> commandList = new ArrayList<>();
    for (String hexStr : list) commandList.add(hexStringToBytes(hexStr));
    return sysCopy(commandList);
  }

  private static byte[] hexStringToBytes(String hexString) {
    if (hexString == null || hexString.equals("")) return null;
    hexString = hexString.toUpperCase();
    int length = hexString.length() / 2;
    char[] hexChars = hexString.toCharArray();
    byte[] d = new byte[length];
    for (int i = 0; i < length; i++) {
      int pos = i * 2;
      d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
    }
    return d;
  }

  private static byte[] sysCopy(List<byte[]> srcArrays) {
    int len = 0;
    for (byte[] srcArray : srcArrays) {
      len += srcArray.length;
    }
    byte[] destArray = new byte[len];
    int destLen = 0;
    for (byte[] srcArray : srcArrays) {
      System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
      destLen += srcArray.length;
    }

    return destArray;
  }

  private static byte charToByte(char c) {
    return (byte) "0123456789ABCDEF".indexOf(c);
  }


  public Bitmap getMultiLangTextAsImage(String text, Paint.Align align, float textSize, Typeface typeface)  {


    Paint paint = new Paint();

    paint.setAntiAlias(true);
    paint.setColor(Color.BLACK);
    paint.setTextSize(textSize);
    if (typeface != null) paint.setTypeface(typeface);

    // A real printlabel width (pixel)
    float xWidth = 640;

    // A height per text line (pixel)
    float xHeight = textSize + 5;

    // it can be changed if the align's value is CENTER or RIGHT
    float xPos = 0f;

    // If the original string data's length is over the width of print label,
    // or '\n' character included,
    // it will be increased per line gerneating.
    float yPos = 27f;

    // If the original string data's length is over the width of print label,
    // or '\n' character included,
    // each lines splitted from the original string are added in this list
    // 'PrintData' class has 3 members, x, y, and splitted string data.
    List<PrintData> printDataList = new ArrayList<PrintData>();

    // if '\n' character included in the original string
    String[] tmpSplitList = text.split("\\n");
    for (int i = 0; i <= tmpSplitList.length - 1; i++) {
      String tmpString = tmpSplitList[i];

      // calculate a width in each split string item.
      float widthOfString = paint.measureText(tmpString);

      // If the each split string item's length is over the width of print label,
      if (widthOfString > xWidth) {
        String lastString = tmpString;
        while (!lastString.isEmpty()) {

          String tmpSubString = "";

          // retrieve repeatedly until each split string item's length is
          // under the width of print label
          while (widthOfString > xWidth) {
            if (tmpSubString.isEmpty())
              tmpSubString = lastString.substring(0, lastString.length() - 1);
            else
              tmpSubString = tmpSubString.substring(0, tmpSubString.length() - 1);

            widthOfString = paint.measureText(tmpSubString);
          }

          // this each split string item is finally done.
          if (tmpSubString.isEmpty()) {
            // this last string to print is need to adjust align
            if (align == Paint.Align.CENTER) {
              if (widthOfString < xWidth) {
                xPos = ((xWidth - widthOfString) / 2);
              }
            } else if (align == Paint.Align.RIGHT) {
              if (widthOfString < xWidth) {
                xPos = xWidth - widthOfString;
              }
            }
            printDataList.add(new PrintData(xPos, yPos, lastString));
            lastString = "";
          } else {
            // When this logic is reached out here, it means,
            // it's not necessary to calculate the x position
            // 'cause this string line's width is almost the same
            // with the width of print label
            printDataList.add(new PrintData(0f, yPos, tmpSubString));

            // It means line is needed to increase
            yPos += 27;
            xHeight += 30;

            lastString = lastString.replaceFirst(tmpSubString, "");
            widthOfString = paint.measureText(lastString);
          }
        }
      } else {
        // This split string item's length is
        // under the width of print label already at first.
        if (align == Paint.Align.CENTER) {
          if (widthOfString < xWidth) {
            xPos = ((xWidth - widthOfString) / 2);
          }
        } else if (align == Paint.Align.RIGHT) {
          if (widthOfString < xWidth) {
            xPos = xWidth - widthOfString;
          }
        }
        printDataList.add(new PrintData(xPos, yPos, tmpString));
      }

      if (i != tmpSplitList.length - 1) {
        // It means the line is needed to increase
        yPos += 27;
        xHeight += 30;
      }
    }

    // If you want to print the text bold
    //paint.setTypeface(Typeface.create(null as String?, Typeface.BOLD))

    // create bitmap by calculated width and height as upper.
    Bitmap bm = Bitmap.createBitmap((int) xWidth, (int) xHeight, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bm);
    canvas.drawColor(Color.WHITE);

    for (PrintData tmpItem : printDataList) {
      canvas.drawText(tmpItem.text, tmpItem.xPos, tmpItem.yPos, paint);
    }
    return bm;
  }

  static class PrintData {
    float xPos;
    float yPos;
    String text;

    public PrintData(float xPos, float yPos, String text) {
      this.xPos = xPos;
      this.yPos = yPos;
      this.text = text;
    }

    public float getxPos() {
      return xPos;
    }

    public void setxPos(float xPos) {
      this.xPos = xPos;
    }

    public float getyPos() {
      return yPos;
    }

    public void setyPos(float yPos) {
      this.yPos = yPos;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }
}
