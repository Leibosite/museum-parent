package com.qingruan.museum.agent.msgcenter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class PublishCenter implements InitializingBean, Runnable {/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
//
//	@Autowired
//	private ZeroMQContextHolder zeroMQContextHolder;
//
//	/*
//	 * 2014-04-10为了让admin管理主机房、备机房两组engine。将endPoint设置为可配置多个，使用“,”隔开
//	 */
//	private String endPoint;
//
//	private int bufferSize;
//
//	private BlockingQueue<Object> queue;
//
//	public void sendMsg(Object msg) {
//		if (msg == null) {
//			return;
//		}
//
//		try {
//			queue.put(msg);
//		} catch (Exception e) {
//			log.error(ExceptionLogUtil.getErrorStack(e));
//		}
//	}
//
//	@Override
//	public void run() {
//		/*
//		 * 2014-04-10为了让admin管理主机房、备机房两组engine。将endPoint设置为可配置多个，使用“,”隔开
//		 */
//		String[] s = endPoint.split(",");
//		List<Socket> pubList = new ArrayList<Socket>();
//		for (String ep : s) {
//			Socket publisher = zeroMQContextHolder.getContext().socket(ZMQ.PUB);
//			publisher.connect(ep.trim());// applicationProperties中有可能在逗号之间存在空格，此处做trim
//			pubList.add(publisher);
//		}
//		// Socket publisher = zeroMQContextHolder.getContext().socket(ZMQ.PUB);
//		// publisher.bind(endPoint);
//
//		try {
//			while (true) {
//				Object msg = queue.take();
//				/*
//				 * 2014-04-10，改为多个publisher
//				 */
//				for (Socket publisher : pubList) {
//					publisher.send(PublishTopic.ADMIN_PUBLICATION, ZMQ.SNDMORE);
//
//					publisher.send(JsonDispatcher.domainToJson(msg), 0);
//				}
//			}
//		} catch (Exception ex) {
//			log.error(ExceptionLogUtil.getErrorStack(ex));
//		}
//		/*
//		 * 2014-04-10，改为多个publisher
//		 */
//		for (Socket publisher : pubList) {
//			publisher.close();
//		}
//	}
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		queue = new LinkedBlockingQueue<Object>(bufferSize);
//
//		new Thread(this).start();
//	}
//
//	public int getBufferSize() {
//		return bufferSize;
//	}
//
//	public void setBufferSize(int bufferSize) {
//		this.bufferSize = bufferSize;
//	}
//
//	public String getEndPoint() {
//		return endPoint;
//	}
//
//	public void setEndPoint(String endPoint) {
//		this.endPoint = endPoint;
//	}
//
}
