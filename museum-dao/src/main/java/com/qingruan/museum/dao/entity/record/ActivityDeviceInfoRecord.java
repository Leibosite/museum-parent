/**
 2015年3月10日
 14cells
 
 */
package com.qingruan.museum.dao.entity.record;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.IdEntityTimeStamp;
import com.qingruan.museum.dao.entity.UuidEntityTimeStamp;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "activity_device_info_record")
@ToString(includeFieldNames = true)
public class ActivityDeviceInfoRecord extends IdEntityTimeStamp implements Serializable {

	private static final long serialVersionUID = -2240045403182107925L;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;
	
	@ManyToOne
	@JoinColumn(name = "device_station_id")
	private Device deviceStation;

	@Column(name = "status")
	private Integer status;
	@Column(name = "desp", nullable = false, length = 255)
	private String desp;

	@Column(name = "date_time")
	private String dateTime;
	@Column(name = "power")
	private Double power;

}
