package com.qingruan.museum.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.domain.models.enums.DeviceType;

//@Transactional(readOnly = true)
public interface DeviceDao extends PagingAndSortingRepository<Device, Long>,
		JpaSpecificationExecutor<Device> {
	// @Transactional(readOnly = false)
	@Modifying
	@Query("update Device p set p.status =:status where p.id=:id")
	void updateByIdDeivceStatus(@Param("status") Integer status,
			@Param("id") Long id);

	// TODO 必须设置 Transactional
	@Transactional(readOnly = false)
	@Modifying
	@Query("update Device p set p.deviceStatus =:deviceStatus where p.id=:id")
	void updateDeivceStatusById(
			@Param("deviceStatus") DeviceStatus deviceStatus,
			@Param("id") Long id);

	// @Transactional(readOnly = false)
	@Modifying
	@Query("update Device p set p.currentPower =:currentPower,p.updateStamp =:updateStamp,p.deviceStatus=:deviceStatus,p.currentMonitorValue=:currentMonitorValue where p.id=:id")
	void updateRunTimeDeviceInfoById(
			@Param("currentPower") Double currentPower,
			@Param("updateStamp") Long updateStamp,
			@Param("deviceStatus") DeviceStatus deviceStatus,
			@Param("id") Long id,
			@Param("currentMonitorValue") String currentMonitorValue);

	@Modifying
	@Query("update Device p set p.settingMonitorValue =:settingMonitorValue where p.id=:id")
	void updateDeivceSettingMonitorValue(
			@Param("settingMonitorValue") String setting_monitor_value,
			@Param("id") Long id);

	/**
	 * 閫氳繃璁惧缂栧彿鏌ユ壘涓�涓澶�
	 * 
	 * @param deviceNo
	 * @return
	 */
	// @Query("select d from Device d where d.deviceNo = :deviceNo")
	// Device findByDeviceNo(@Param("deviceNo") String deviceNo);

	Device findByDeviceNo(String deviceNo);

	/**
	 * 閫氳繃璁惧缂栧彿淇敼璁惧鏇存柊鏃堕棿
	 * 
	 * @param updateAt
	 * @param deviceNo
	 */
	@Modifying
	@Query("update Device d set d.updatedAt = :updateAt where d.deviceNo = :deviceNo")
	void updateUpdateAtByDeviceNo(@Param("updateAt") Date updateAt,
			@Param("deviceNo") String deviceNo);

	/**
	 * 鏍规嵁鏌ヨ缁忕含搴︽槸鍚︿负绌哄垽瀹氳澶囨槸鍚﹀凡缁忚缁戝畾
	 */
	List<Device> findByLongitudeIsNullAndLatitudeIsNull();

	/**
	 * 鏍规嵁鍚嶇О妯＄硦鏌ヨ
	 * 
	 * @param name
	 * @return
	 */
	List<Device> findByNameLike(String name);

	// 鏌ュ嚭璁惧鎬婚噺
	@Query("select  count(*) from Device")
	public int getAllDevices();

	// 鏌ュ嚭璁惧鐢甸噺鍦�0-10涔嬮棿鐨勮澶囨暟
	@Query("select count(*) from Device d where d.currentPower >=0 and d.currentPower <= 10")
	public int get0To10Devices();

	// 鏌ュ嚭璁惧鐢甸噺鍦�10-20涔嬮棿鐨勮澶囨暟
	@Query("select count(*) from Device d where d.currentPower >10 and d.currentPower <= 20")
	public int get10To20Devices();

	// 鏌ュ嚭璁惧鐢甸噺鍦�20-50涔嬮棿鐨勮澶囨暟
	@Query("select  count(*) from Device d where d.currentPower >20 and d.currentPower <= 50")
	public int get20To50Devices();

	// 鏌ュ嚭璁惧鐢甸噺鍦�50-10涔嬮棿鐨勮澶囨暟
	@Query("select  count(*) from Device d where d.currentPower >50 and d.currentPower <= 80")
	public int get50To80Devices();

	// 鏌ュ嚭璁惧鐢甸噺鍦�0-10涔嬮棿鐨勮澶囨暟
	@Query("select  count(*) from Device d where d.currentPower >80 and d.currentPower <= 100")
	public int get80To100Devices();

	Device findByMacAddr(String macAddr);

	Device findByIpAddr(String ipAddr);

	@Query("select p from Device p where p.parentId =:parentId")
	public List<Device> findByParentId(@Param("parentId") Long parentId);

	@Query("select p from Device p where p.repoArea =:repoArea")
	public List<Device> findByRepoArea(@Param("repoArea") RepoArea repoArea);

	@Query("select p from Device p where p.deviceType =:deviceType")
	public List<Device> findByDeviceType(
			@Param("deviceType") DeviceType deviceType);

	@Query("select p from Device p where p.repoArea =:repoArea and p.deviceStatus = 'CONNECTED' and p.deviceType in('MONITORING_POINT_ONE','MONITORING_POINT_TWO')")
	public List<Device> findByRepoAreaAndDeviceType(
			@Param("repoArea") RepoArea repoArea);

	@Query("select p from Device p where  p.deviceType in('MONITORING_POINT_ONE','MONITORING_POINT_TWO') and p.name like:name")
	public List<Device> findMonitorPoint(@Param("name") String name);

	@Query("select p from Device p where  p.deviceType in('MONITORING_STATION') and p.name like:name")
	public List<Device> findMonitorStation(@Param("name") String name);

	@Query("select p from Device p where  p.deviceType in('CONSTANT_TH_MACHINE') and p.name like:name")
	public List<Device> findConstantTh(@Param("name") String name);

	@Query("select p from Device p where p.id =:id")
	public Device findById(@Param("id") Long id);

	@Query("select p from Device p")
	public List<Device> findAllDevice();

	@Query("select p from Device p where p.repoArea.id =:repoAreaId and p.deviceType=:deviceType")
	public List<Device> findByRepoAreaId(@Param("repoAreaId") Long repoAreaId,
			@Param("deviceType") DeviceType deviceType);
}
