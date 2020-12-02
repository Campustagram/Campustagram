package com.campustagram.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "disc_info")
public class DiscInfo {

	@Id
	@SequenceGenerator(name = "disc_info_seq", sequenceName = "disc_info_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disc_info_seq")
	@Column(nullable = false)
	private Long id;
	private Date createDate = new Date();

	private String rootName1;
	private Long usableSpace1;
	private Long totalSpace1;

	private String rootName2;
	private Long usableSpace2;
	private Long totalSpace2;

	private String rootName3;
	private Long usableSpace3;
	private Long totalSpace3;

	private String rootName4;
	private Long usableSpace4;
	private Long totalSpace4;

	private String rootName5;
	private Long usableSpace5;
	private Long totalSpace5;

	public DiscInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DiscInfo [id=" + id + ", rootName1=" + rootName1 + ", usableSpace1=" + usableSpace1 + ", totalSpace1="
				+ totalSpace1 + ", rootName2=" + rootName2 + ", usableSpace2=" + usableSpace2 + ", totalSpace2="
				+ totalSpace2 + ", rootName3=" + rootName3 + ", usableSpace3=" + usableSpace3 + ", totalSpace3="
				+ totalSpace3 + ", rootName4=" + rootName4 + ", usableSpace4=" + usableSpace4 + ", totalSpace4="
				+ totalSpace4 + ", rootName5=" + rootName5 + ", usableSpace5=" + usableSpace5 + ", totalSpace5="
				+ totalSpace5 + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRootName1() {
		return rootName1;
	}

	public void setRootName1(String rootName1) {
		this.rootName1 = rootName1;
	}

	public Long getUsableSpace1() {
		return usableSpace1;
	}

	public void setUsableSpace1(Long usableSpace1) {
		this.usableSpace1 = usableSpace1;
	}

	public Long getTotalSpace1() {
		return totalSpace1;
	}

	public void setTotalSpace1(Long totalSpace1) {
		this.totalSpace1 = totalSpace1;
	}

	public String getRootName2() {
		return rootName2;
	}

	public void setRootName2(String rootName2) {
		this.rootName2 = rootName2;
	}

	public Long getUsableSpace2() {
		return usableSpace2;
	}

	public void setUsableSpace2(Long usableSpace2) {
		this.usableSpace2 = usableSpace2;
	}

	public Long getTotalSpace2() {
		return totalSpace2;
	}

	public void setTotalSpace2(Long totalSpace2) {
		this.totalSpace2 = totalSpace2;
	}

	public String getRootName3() {
		return rootName3;
	}

	public void setRootName3(String rootName3) {
		this.rootName3 = rootName3;
	}

	public Long getUsableSpace3() {
		return usableSpace3;
	}

	public void setUsableSpace3(Long usableSpace3) {
		this.usableSpace3 = usableSpace3;
	}

	public Long getTotalSpace3() {
		return totalSpace3;
	}

	public void setTotalSpace3(Long totalSpace3) {
		this.totalSpace3 = totalSpace3;
	}

	public String getRootName4() {
		return rootName4;
	}

	public void setRootName4(String rootName4) {
		this.rootName4 = rootName4;
	}

	public Long getUsableSpace4() {
		return usableSpace4;
	}

	public void setUsableSpace4(Long usableSpace4) {
		this.usableSpace4 = usableSpace4;
	}

	public Long getTotalSpace4() {
		return totalSpace4;
	}

	public void setTotalSpace4(Long totalSpace4) {
		this.totalSpace4 = totalSpace4;
	}

	public String getRootName5() {
		return rootName5;
	}

	public void setRootName5(String rootName5) {
		this.rootName5 = rootName5;
	}

	public Long getUsableSpace5() {
		return usableSpace5;
	}

	public void setUsableSpace5(Long usableSpace5) {
		this.usableSpace5 = usableSpace5;
	}

	public Long getTotalSpace5() {
		return totalSpace5;
	}

	public void setTotalSpace5(Long totalSpace5) {
		this.totalSpace5 = totalSpace5;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
