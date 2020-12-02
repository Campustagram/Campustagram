package com.campustagram.core.common;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.campustagram.core.controller.log.ILogger;
import com.campustagram.core.model.CpuInfo;
import com.campustagram.core.model.DiscInfo;
import com.campustagram.core.model.MemoryInfo;

@ManagedBean(name = "commonPerformance")
@ViewScoped
public class CommonPerformance {
	private static ILogger logger = new ILogger();
	private static final String ACTIVE_CLASS_NAME = "CommonPerformance";

	/**
	 * Generates memory information
	 * 
	 * @author Salih Emre Kuru
	 * @return
	 */
	public static MemoryInfo generateMemoryInfo() {
		final String ACTIVE_METHOD_NAME = "generateMemoryInfo";
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		MemoryInfo ramInfo = new MemoryInfo();
		ramInfo.setFreeMemory(Runtime.getRuntime().freeMemory());
		ramInfo.setTotalMemory(Runtime.getRuntime().totalMemory());
		ramInfo.setUsedMemory(ramInfo.getTotalMemory() - ramInfo.getFreeMemory());
		ramInfo.setUsedPercentage(((ramInfo.getUsedMemory() * 1.0) / ramInfo.getTotalMemory()) * 100);

		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);

					switch (method.getName()) {
					case "getCommittedVirtualMemorySize":
						ramInfo.setCommittedVirtualMemorySize(CommonFunctions.convertToLong(value));
						break;
					case "getFreePhysicalMemorySize":
						ramInfo.setFreePhysicalMemorySize(CommonFunctions.convertToLong(value));
						break;
					case "getFreeSwapSpaceSize":
						ramInfo.setFreeSwapSpaceSize(CommonFunctions.convertToLong(value));
						break;
					case "getTotalPhysicalMemorySize":
						ramInfo.setTotalPhysicalMemorySize(CommonFunctions.convertToLong(value));
						break;
					case "getTotalSwapSpaceSize":
						ramInfo.setTotalSwapSpaceSize(CommonFunctions.convertToLong(value));
						break;

					default:
						break;
					}

				} catch (Exception e) {
					value = e;
				}
			}
		}
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return ramInfo;
	}

	/**
	 * Generates cpu information
	 * 
	 * @author Salih Emre Kuru
	 * @return
	 */

	public static CpuInfo generateCpuInfo() {
		final String ACTIVE_METHOD_NAME = "generateCpuInfo";
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		CpuInfo cpuInfo = new CpuInfo();

		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);

					switch (method.getName()) {

					case "getProcessCpuTime":
						cpuInfo.setProcessCpuTime(CommonFunctions.convertToLong(value));
						break;
					case "getProcessCpuLoad":
						cpuInfo.setProcessCpuLoad(CommonFunctions.convertToDouble(value));
						break;
					case "getSystemCpuLoad":
						cpuInfo.setSystemCpuLoad(CommonFunctions.convertToDouble(value));
						break;

					default:
						break;
					}

				} catch (Exception e) {
					value = e;
				}
			}
		}
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return cpuInfo;
	}

	/**
	 * Generates disc information
	 * 
	 * @author Salih Emre Kuru
	 * @return
	 */
	public static DiscInfo generateDiscInfo() throws Exception {
		final String ACTIVE_METHOD_NAME = "generateDiscInfo";
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		DiscInfo discInfo = new DiscInfo();
		int rootPart = 0;
		for (Path root : FileSystems.getDefault().getRootDirectories()) {
			++rootPart;
			try {
				FileStore store = Files.getFileStore(root);
				switch (rootPart) {
				case 1:
					discInfo.setRootName1(root.toString());
					discInfo.setUsableSpace1(store.getUsableSpace());
					discInfo.setTotalSpace1(store.getTotalSpace());
					break;
				case 2:
					discInfo.setRootName2(root.toString());
					discInfo.setUsableSpace2(store.getUsableSpace());
					discInfo.setTotalSpace2(store.getTotalSpace());
					break;
				case 3:
					discInfo.setRootName3(root.toString());
					discInfo.setUsableSpace3(store.getUsableSpace());
					discInfo.setTotalSpace3(store.getTotalSpace());
					break;
				case 4:
					discInfo.setRootName4(root.toString());
					discInfo.setUsableSpace4(store.getUsableSpace());
					discInfo.setTotalSpace4(store.getTotalSpace());
					break;
				case 5:
					discInfo.setRootName5(root.toString());
					discInfo.setUsableSpace5(store.getUsableSpace());
					discInfo.setTotalSpace5(store.getTotalSpace());
					break;

				default:
					System.out.println("path not found");
					break;
				}

			} catch (IOException e) {

			}
		}
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return discInfo;
	}
}