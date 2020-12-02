package com.campustagram.core.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "commonStatistics")
@ViewScoped
public class CommonStatistics {

	public static List<Double> getListWithoutOutliers(List<Double> input) {
		List<Double> newData = new ArrayList<>();
		newData.addAll(input);
		newData.removeAll(getOutliers(input));
		return newData;
	}

	public static Map<String, Double> get5NumSummary(List<Double> input) {
		Map<String, Double> output = new HashMap<>();
		List<Double> data1;
		List<Double> data2;
		if (input.size() % 2 == 0) {
			data1 = input.subList(0, input.size() / 2);
			data2 = input.subList(input.size() / 2, input.size());
		} else {
			data1 = input.subList(0, input.size() / 2);
			data2 = input.subList(input.size() / 2 + 1, input.size());
		}
		double q1 = getMedian(data1);
		double q3 = getMedian(data2);
		double iqr = q3 - q1;
		double lowerInnerFence = q1 - (1.5 * iqr);
		double upperInnerFence = q3 + (1.5 * iqr);
		double lowerOuterFence = q1 - (3 * iqr);
		double upperOuterFence = q3 + (3 * iqr);

		output.put("q1", q1);
		output.put("q3", q3);
		output.put("iqr", iqr);
		output.put("lowerInnerFence", lowerInnerFence);
		output.put("upperInnerFence", upperInnerFence);
		output.put("lowerOuterFence", lowerOuterFence);
		output.put("upperOuterFence", upperOuterFence);

		return output;
	}

	public static List<Double> getOutliers(List<Double> input) {
		List<Double> output = new ArrayList<>();
		List<Double> data1;
		List<Double> data2;
		if (input.size() % 2 == 0) {
			data1 = input.subList(0, input.size() / 2);
			data2 = input.subList(input.size() / 2, input.size());
		} else {
			data1 = input.subList(0, input.size() / 2);
			data2 = input.subList(input.size() / 2 + 1, input.size());
		}
		double q1 = getMedian(data1);
		double q3 = getMedian(data2);
		double iqr = q3 - q1;
		double lowerFence = q1 - 1.5 * iqr;
		double upperFence = q3 + 1.5 * iqr;
		for (int i = 0; i < input.size(); i++) {
			if (input.get(i) < lowerFence || input.get(i) > upperFence)
				output.add(input.get(i));
		}
		return output;
	}

	public static Double getMedian(List<Double> data) {
		if (data.size() % 2 == 0)
			return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
		else
			return data.get(data.size() / 2);
	}

	public static Double getMean(List<Double> values) {
		double sum = 0;
		for (double value : values) {
			sum += value;
		}
		return (sum / values.size());
	}

	public static Double getStandardDeviation(List<Double> input) {
		double sum = 0.0;
		double standardDeviation = 0.0;
		int length = input.size();

		for (double num : input) {
			sum += num;
		}

		double mean = sum / length;

		for (double num : input) {
			standardDeviation += Math.pow(num - mean, 2);
		}

		return Math.sqrt(standardDeviation / length);
	}

	public static Double getVariance(List<Double> data) {
		double mean = getMean(data);
		double temp = 0;
		for (double a : data)
			temp += (a - mean) * (a - mean);
		return temp / (data.size() - 1);
	}

	public static List<Double> getModes(List<Double> numbers) {
		final Map<Double, Long> countFrequencies = numbers.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		final long maxFrequency = countFrequencies.values().stream().mapToLong(count -> count).max().orElse(-1);

		return countFrequencies.entrySet().stream().filter(tuple -> tuple.getValue() == maxFrequency)
				.map(Map.Entry::getKey).collect(Collectors.toList());
	}

}