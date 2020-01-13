package at.aschl.pgs.data;

import org.apache.tomcat.util.codec.binary.Base64;

public class Image {

  public String filename;
  public String imgdata;

  public Image(String filename, byte[] imgdata) {
    this.filename = filename;
    this.imgdata = Base64.encodeBase64String(imgdata);
  }

  public String getFilename() {
    return filename;
  }

  public byte[] getImgdata() {
    return Base64.decodeBase64(imgdata);
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void setImgdata(String imgdata) {
    this.imgdata = imgdata;
  }
}
