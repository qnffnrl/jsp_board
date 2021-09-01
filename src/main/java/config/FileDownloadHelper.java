package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileDownloadHelper {

	public static void copy(String filePath, OutputStream os) throws IOException{
		FileInputStream is = null;
		try {
			is = new FileInputStream(filePath);
			byte[] data = new byte[8096];
			int len = -1;
			while((len = is.read(data)) != -1) {
				os.write(data, 0, len);
			}
		}finally {
			if(is != null) {
				try {
					is.close();
				}catch (Exception e) {
				}
			}
		}
	}
	
}
