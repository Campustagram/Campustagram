package com.campustagram.core.common;

import java.io.File;
import java.net.URL;

import org.apache.myfaces.util.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.util.FileCopyUtils;

public class FileUtil {

	private String path;

	/**
	 * Path in yazılışını düzenler.
	 * 
	 * @author Salih Emre Kuru
	 * @return düzenlenmiş path yazılışı
	 */
	public String getPath() {
		URL resource;
		resource = getClass().getClassLoader().getResource(File.separator);
		if (resource != null) {
			path = resource.getPath();
		} else {
			resource = getClass().getClassLoader().getResource("");
			path = resource.getPath();
		}
		path = path.replace("/", File.separator);
		path = path.replace("\\", File.separator);
		path = path.replace(File.separator + "WEB-INF" + File.separator + "classes", "");
		path += "resources" + File.separator;

		path = path.replace("%20", " ");

		if (path.startsWith(File.separator) && (!File.separator.equals("/"))) {
			path = path.substring(1);
		}

		return path;
	}

	/**
	 * paket yüklemek için kullanılır. Yüklenilen paket in adı hash lenerek dosya
	 * sistemine kopyalanır.
	 * 
	 * @author Salih Emre Kuru
	 * @param event
	 * @return packagePath
	 * @throws Exception
	 */
	public String uploadPackage(FileUploadEvent event) throws Exception {
		UploadedFile uploadedPackage = event.getFile();
		File targetFile = null;
		boolean copyFile = false;

		if (null != uploadedPackage) {
			byte[] bytes = uploadedPackage.getContents();
			String filename = FilenameUtils.getName(uploadedPackage.getFileName());
			String filenameHash = CommonCryptographicHash
					.encryptStringMD5SHA1((filename + CommonDate.currentDate()).toCharArray());
			String filenameExt = filename.substring(filename.length() - 4);
			if (filenameExt.equals(".ipa")) {
				targetFile = new File(getPath() + (filenameHash + ".ipa"));
				copyFile = true;
			} else if (filenameExt.equals(".apk")) {
				targetFile = new File(getPath() + (filenameHash + ".apk"));
				copyFile = true;
			}

			if (copyFile) {
				FileCopyUtils.copy(bytes, targetFile);
			}

		}

		String path = targetFile == null ? "" : targetFile.toURI().toString();

		return path.substring(path.lastIndexOf("/resources"));
	}

}
