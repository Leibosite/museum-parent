package com.qingruan.museum.agent.service.schedule.archive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.repository.record.ActivityDataRecordDao;
import com.qingruan.museum.framework.log.ExceptionLog;
import com.qingruan.museum.framework.util.GlobalParameter.TimeFormat;
import com.qingruan.museum.framework.util.TimeUtil;

/**
 * @desp 原始数据定期归档整理
 * @author john
 *
 */

@Service
@Slf4j
public class RegularArchiveSchedule {
	@Autowired
	private ActivityDataRecordDao activityDataRecordDao;
	private String zipFileName;
	private String tmpPath;
	private Long currentTime;

	public void computeRegularArchiveSchedule() {
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/application.properties");
		if (in == null) {
			log.error("{ properties file is not found }");
			throw new RuntimeException();
		}
		
		currentTime = System.currentTimeMillis();

		try {
			prop.load(in);
			String username = prop.getProperty("jdbc.username").trim();
			String password = prop.getProperty("jdbc.password").trim();
			String database = prop.getProperty("db.name").trim();
			String host = prop.getProperty("db.host");
			String ftpHost = prop.getProperty("ftp.host");
			String ftpUser = prop.getProperty("ftp.username");
			String ftpPwd = prop.getProperty("ftp.password");
			String ftpPath = prop.getProperty("ftp.path");
			String dbPath = prop.getProperty("db.path");
			tmpPath = prop.getProperty("tmpfile.path");
			
			String tableName = "activity_data_record";
			String mysql = "mysqldump -t -c -h " + host + " -u" + username + " -p" + password + " " + database + " " + tableName + " -w \"update_stamp <= '" + currentTime + "'\"";
			String[] shell = new String[] { "sh", "-c", dbPath + mysql };
			if (backUpActivityData(shell, tableName)) {
				FileInputStream is = new FileInputStream(new File(tmpPath + zipFileName));
				uploadFileByApacheByBinary(ftpHost, ftpUser, ftpPwd, is, ftpPath, zipFileName);
				is.close();
				deleteActivityData();
				log.info("{ backup table success }");
			} else {
				log.info("{ backup table fail }");
			}
		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}
	}

	private boolean backUpActivityData(String[] shell, String tableName) throws IOException {
		String line = "";
		StringBuffer strBuffer = new StringBuffer();
		InputStream inputStream = null;
		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;
		FileOutputStream fileOutput = null;
		OutputStreamWriter outputWriter = null;
		FileInputStream fileInput = null;
		ZipOutputStream zipOutput = null;
		File file = null;
		try {
			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec(shell);

			file = new File(tmpPath + tableName + ".sql");
			inputStream = process.getInputStream();
			inputReader = new InputStreamReader(inputStream, "utf-8");
			bufferReader = new BufferedReader(inputReader);
			while ((line = bufferReader.readLine()) != null) {
				strBuffer.append(line + "\r\n");
			}

			fileOutput = new FileOutputStream(file);
			outputWriter = new OutputStreamWriter(fileOutput, "utf-8");
			outputWriter.write(strBuffer.toString());
			outputWriter.flush();

			zipOutput = new ZipOutputStream(new FileOutputStream(tmpPath));
			fileInput = new FileInputStream(file);
			zipFileName = tableName + "_" + TimeUtil.getStringTime(currentTime, TimeFormat.YEAR_MONTH_DAY_HOUR_MIN_SEC);;
			zipOutput.putNextEntry(new ZipEntry(zipFileName));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fileInput.read(buffer)) > 0) {
				zipOutput.write(buffer, 0, len);
			}
			zipOutput.closeEntry();

			if (process.waitFor() == 0) {
				return true;
			}
		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		} finally {
			if (bufferReader != null) {
				bufferReader.close();
			}
			if (inputReader != null) {
				inputReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputWriter != null) {
				outputWriter.close();
			}
			if (fileOutput != null) {
				fileOutput.close();
			}
			if (fileInput != null) {
				fileInput.close();
			}
			if (zipOutput != null) {
				zipOutput.close();
			}
			if (file != null) {
				file.delete();
			}
		}
		return false;
	}

	public static boolean uploadFileByApacheByBinary(String server, String username, String password,
			FileInputStream in, String path, String fileName) throws Exception {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, 21);
			ftpClient.login(username, password);
			String encoding = System.getProperty("file.encoding");
			ftpClient.setControlEncoding(encoding);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
			boolean change = ftpClient.changeWorkingDirectory(path);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			boolean result = false;
            if (change) {
                result = ftpClient.storeFile(new String(fileName.getBytes(encoding), "iso-8859-1"), in);
                if (result) {
                    System.out.println("{ ftp upload success }");
                }
            }
			
		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
			return false;
		} finally {
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
		}
		return true;
	}

	private void deleteActivityData() {
		activityDataRecordDao.deleteByNowTime(currentTime);
	}
}
